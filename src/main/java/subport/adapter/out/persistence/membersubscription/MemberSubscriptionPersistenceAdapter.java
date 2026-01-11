package subport.adapter.out.persistence.membersubscription;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.adapter.out.persistence.subscription.PlanJpaEntity;
import subport.adapter.out.persistence.subscription.SpringDataPlanRepository;
import subport.adapter.out.persistence.subscription.SpringDataSubscriptionRepository;
import subport.adapter.out.persistence.subscription.SubscriptionJpaEntity;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.out.DeleteMemberSubscriptionPort;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Component
@RequiredArgsConstructor
public class MemberSubscriptionPersistenceAdapter implements
	SaveMemberSubscriptionPort,
	LoadMemberSubscriptionPort,
	DeleteMemberSubscriptionPort {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SpringDataPlanRepository planRepository;
	private final MemberSubscriptionMapper memberSubscriptionMapper;

	@Override
	public Long save(MemberSubscription memberSubscription) {
		MemberJpaEntity memberEntity = memberRepository.getReferenceById(memberSubscription.getMemberId());
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.getReferenceById(
			memberSubscription.getSubscriptionId());
		PlanJpaEntity planEntity = planRepository.getReferenceById(
			memberSubscription.getPlanId());

		MemberSubscriptionJpaEntity memberSubscriptionEntity = memberSubscriptionMapper.toJpaEntity(
			memberSubscription,
			memberEntity,
			subscriptionEntity,
			planEntity
		);

		return memberSubscriptionRepository.save(memberSubscriptionEntity).getId();
	}

	@Override
	public MemberSubscription load(Long memberSubscriptionId) {
		MemberSubscriptionJpaEntity memberSubscriptionEntity = memberSubscriptionRepository
			.findById(memberSubscriptionId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_NOT_FOUND));

		return memberSubscriptionMapper.toDomain(memberSubscriptionEntity);
	}

	@Override
	public void delete(Long memberSubscriptionId) {
		memberSubscriptionRepository.deleteById(memberSubscriptionId);
	}
}
