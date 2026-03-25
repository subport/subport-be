package subport.batch.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import subport.domain.plan.Plan;

public interface SpringDataPlanRepository extends JpaRepository<Plan, Long> {

	@Modifying
	@Query("""
		DELETE FROM Plan p
		WHERE p.member.id IN :memberIds
		""")
	void deleteAllByMemberIds(List<Long> memberIds);
}
