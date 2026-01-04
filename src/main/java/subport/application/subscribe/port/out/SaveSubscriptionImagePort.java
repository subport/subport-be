package subport.application.subscribe.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface SaveSubscriptionImagePort {

	String saveSubscriptionImage(MultipartFile image);
}
