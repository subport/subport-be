package subport.admin.adapter.out.persistence.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.admin.domain.Admin;

public interface SpringDataAdminRepository extends JpaRepository<Admin, Long> {

	Optional<Admin> findByEmail(String email);
}
