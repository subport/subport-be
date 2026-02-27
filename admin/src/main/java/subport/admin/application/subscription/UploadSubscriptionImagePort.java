package subport.admin.application.subscription;

import org.springframework.web.multipart.MultipartFile;

public interface UploadSubscriptionImagePort {

	String upload(MultipartFile image, String subscriptionName);
}
