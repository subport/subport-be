package subport.admin.adapter.out.objectstorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.application.subscription.DeleteSubscriptionImagePort;
import subport.admin.application.subscription.UploadSubscriptionImagePort;
import subport.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class OciObjectStorageAdapter implements
	UploadSubscriptionImagePort,
	DeleteSubscriptionImagePort {

	private static final String IMAGE_TYPE_PREFIX = "image/";

	private final S3Client s3Client;

	@Value("${oci.bucket-name}")
	private String bucketName;

	@Value("${oci.namespace}")
	private String namespace;

	@Value("${oci.region}")
	private String region;

	@Override
	public String upload(MultipartFile image, String subscriptionName) {
		validateImage(image);

		String fileName = subscriptionName + extractExtension(image.getOriginalFilename());

		byte[] fileBytes = resizeImage(image);

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.contentType(image.getContentType())
			.build();

		s3Client.putObject(
			putObjectRequest,
			RequestBody.fromBytes(fileBytes)
		);

		return getPublicUrl(fileName);
	}

	@Override
	public void delete(String imageUrl) {
		String fileName = extractFileNameFromUrl(imageUrl);

		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.build();

		s3Client.deleteObject(deleteObjectRequest);
	}

	private void validateImage(MultipartFile image) {
		String contentType = image.getContentType();
		if (contentType == null || !contentType.startsWith(IMAGE_TYPE_PREFIX)) {
			throw new CustomException(AdminErrorCode.IMAGE_FILE_TYPE_NOT_SUPPORTED);
		}
	}

	private String extractExtension(String originalFilename) {
		if (originalFilename != null && originalFilename.contains(".")) {
			return originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		return "";
	}

	private byte[] resizeImage(MultipartFile image) {
		String originalFilename = image.getOriginalFilename();
		if (originalFilename != null && originalFilename.toLowerCase().endsWith(".webp")) {
			try {
				return image.getBytes();
			} catch (IOException e) {
				throw new CustomException(AdminErrorCode.IMAGE_FILE_READ_FAILED, e);
			}
		}

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Thumbnails.of(image.getInputStream())
				.size(240, 240)
				.keepAspectRatio(true)
				.toOutputStream(outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new CustomException(AdminErrorCode.IMAGE_FILE_READ_FAILED, e);
		}
	}

	private String getPublicUrl(String fileName) {
		return String.format(
			"https://%s.compat.objectstorage.%s.oraclecloud.com/%s/%s",
			namespace,
			region,
			bucketName,
			fileName
		);
	}

	private String extractFileNameFromUrl(String imageUrl) {
		return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
	}
}
