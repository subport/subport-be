package subport.application.subscription.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface UploadSubscriptionImagePort {

	String upload(MultipartFile image);
}
