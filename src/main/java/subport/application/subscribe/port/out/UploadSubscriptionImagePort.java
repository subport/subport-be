package subport.application.subscribe.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface UploadSubscriptionImagePort {

	String upload(MultipartFile image);
}
