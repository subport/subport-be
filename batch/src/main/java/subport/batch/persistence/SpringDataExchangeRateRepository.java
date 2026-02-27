package subport.batch.persistence;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.exchangeRate.ExchangeRate;

public interface SpringDataExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

	Optional<ExchangeRate> findByRequestDate(LocalDate requestDate);
}
