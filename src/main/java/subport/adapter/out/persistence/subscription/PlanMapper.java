package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.Plan;

@Component
public class PlanMapper {

	public PlanJpaEntity toJpaEntity(
		Plan plan,
		MemberJpaEntity memberEntity,
		SubscriptionJpaEntity subscriptionEntity
	) {
		return new PlanJpaEntity(
			plan.getName(),
			plan.getAmount(),
			plan.getAmountUnit(),
			plan.getDurationMonths(),
			plan.isSystemProvided(),
			memberEntity,
			subscriptionEntity
		);
	}

	public Plan toDomain(PlanJpaEntity PlanEntity) {
		boolean systemProvided = PlanEntity.isSystemProvided();
		MemberJpaEntity memberEntity = PlanEntity.getMember();

		Long memberId = null;
		if (!systemProvided && memberEntity != null) {
			memberId = memberEntity.getId();
		}

		return Plan.withId(
			PlanEntity.getId(),
			PlanEntity.getName(),
			PlanEntity.getAmount(),
			PlanEntity.getAmountUnit(),
			PlanEntity.getDurationMonths(),
			systemProvided,
			memberId,
			PlanEntity.getSubscription().getId()
		);
	}
}
