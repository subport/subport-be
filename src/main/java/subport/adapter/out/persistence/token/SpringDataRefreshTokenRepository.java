package subport.adapter.out.persistence.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.token.RefreshToken;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByTokenValue(String tokenValue);

	void deleteByTokenValue(String tokenValue);
}
