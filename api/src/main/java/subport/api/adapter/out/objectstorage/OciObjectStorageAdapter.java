package subport.api.adapter.out.objectstorage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.subscription.port.out.DeleteCustomSubscriptionImagePort;
import subport.api.application.subscription.port.out.UploadCustomSubscriptionImagePort;
import subport.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class OciObjectStorageAdapter implements
	UploadCustomSubscriptionImagePort,
	DeleteCustomSubscriptionImagePort {

	private static final String IMAGE_TYPE_PREFIX = "image/";
	private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

	private final S3Client s3Client;

	@Value("${oci.bucket-name}")
	private String bucketName;

	@Value("${oci.namespace}")
	private String namespace;

	@Value("${oci.region}")
	private String region;

	@Override
	public String upload(MultipartFile image) {
		validateFile(image);

		String fileName = generateFileName(image.getOriginalFilename());

		byte[] fileBytes = readFileBytes(image);

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

	private void validateFile(MultipartFile image) {
		String contentType = image.getContentType();
		if (contentType == null || !contentType.startsWith(IMAGE_TYPE_PREFIX)) {
			throw new CustomException(ApiErrorCode.IMAGE_FILE_TYPE_NOT_SUPPORTED);
		}

		if (image.getSize() > MAX_FILE_SIZE) {
			throw new CustomException(ApiErrorCode.IMAGE_FILE_SIZE_EXCEEDED);
		}
	}

	private String generateFileName(String originalFilename) {
		String extension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		return UUID.randomUUID() + extension;
	}

	private byte[] readFileBytes(MultipartFile image) {
		try {
			return image.getBytes();
		} catch (IOException e) {
			throw new CustomException(ApiErrorCode.IMAGE_FILE_READ_FAILED, e);
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
