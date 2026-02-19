package subport.admin.application.port;

import subport.admin.domain.Admin;

public interface LoadAdminPort {

	Admin loadAdmin(String email);
}
