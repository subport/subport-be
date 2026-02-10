package subport.adapter.out.objectstorage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import subport.admin.application.port.UploadAdminSubscriptionImagePort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.UploadCustomSubscriptionImagePort;

@Component
@RequiredArgsConstructor
public class OciObjectStorageAdapter implements
	UploadCustomSubscriptionImagePort,
	UploadAdminSubscriptionImagePort {

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

	private void validateFile(MultipartFile image) {
		String contentType = image.getContentType();
		if (contentType == null || !contentType.startsWith(IMAGE_TYPE_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_IMAGE_FILE_TYPE);
		}

		if (image.getSize() > MAX_FILE_SIZE) {
			throw new CustomException(ErrorCode.INVALID_IMAGE_FILE_SIZE);
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
			throw new CustomException(ErrorCode.FILE_READ_FAILED, e);
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
}
