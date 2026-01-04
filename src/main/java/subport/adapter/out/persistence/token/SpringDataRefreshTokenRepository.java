package subport.adapter.out.persistence.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {
}
