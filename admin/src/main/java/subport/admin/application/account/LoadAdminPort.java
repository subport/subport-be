package subport.admin.application.account;

import subport.admin.domain.Admin;

public interface LoadAdminPort {

	Admin loadAdmin(Long adminId);

	Admin loadAdmin(String email);
}
