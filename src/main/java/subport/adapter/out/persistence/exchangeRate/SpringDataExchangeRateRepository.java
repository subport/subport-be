package subport.adapter.out.persistence.exchangeRate;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataExchangeRateRepository extends JpaRepository<ExchangeRateJpaEntity, Long> {

	Optional<ExchangeRateJpaEntity> findByRequestDate(LocalDate requestDate);
}
