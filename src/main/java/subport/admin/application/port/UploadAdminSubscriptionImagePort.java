package subport.admin.application.port;

import org.springframework.web.multipart.MultipartFile;

public interface UploadAdminSubscriptionImagePort {

	String upload(MultipartFile image);
}
