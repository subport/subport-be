package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.SubscriptionPlan;

@Component
public class SubscriptionPlanMapper {

	public SubscriptionPlanJpaEntity toJpaEntity(
		SubscriptionPlan subscriptionPlan,
		MemberJpaEntity memberEntity,
		SubscriptionJpaEntity subscriptionEntity
	) {
		return new SubscriptionPlanJpaEntity(
			subscriptionPlan.getPlanName(),
			subscriptionPlan.getAmount(),
			subscriptionPlan.getAmountUnit(),
			subscriptionPlan.getDurationMonths(),
			subscriptionPlan.isSystemProvided(),
			memberEntity,
			subscriptionEntity
		);
	}

	public SubscriptionPlan toDomain(SubscriptionPlanJpaEntity subscriptionPlanEntity) {
		boolean systemProvided = subscriptionPlanEntity.isSystemProvided();
		MemberJpaEntity memberEntity = subscriptionPlanEntity.getMember();

		Long memberId = null;
		if (!systemProvided && memberEntity != null) {
			memberId = memberEntity.getId();
		}

		return SubscriptionPlan.withId(
			subscriptionPlanEntity.getId(),
			subscriptionPlanEntity.getPlanName(),
			subscriptionPlanEntity.getAmount(),
			subscriptionPlanEntity.getAmountUnit(),
			subscriptionPlanEntity.getDurationMonths(),
			systemProvided,
			memberId,
			subscriptionPlanEntity.getSubscription().getId()
		);
	}
}
