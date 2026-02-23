package subport.api.application.subscription.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface UploadCustomSubscriptionImagePort {

	String upload(MultipartFile image);
}
