package subport.adapter.out.persistence.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

	Optional<RefreshTokenJpaEntity> findByTokenValue(String tokenValue);

	void deleteByTokenValue(String tokenValue);
}
