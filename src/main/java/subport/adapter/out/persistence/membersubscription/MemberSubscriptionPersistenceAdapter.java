package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Component
@RequiredArgsConstructor
public class MemberSubscriptionPersistenceAdapter implements
	SaveMemberSubscriptionPort,
	LoadMemberSubscriptionPort,
	UpdateMemberSubscriptionPort,
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
	public List<MemberSubscriptionDetail> loadDetailsByNextPaymentDate(LocalDate currentDate) {
		return memberSubscriptionRepository.findByNextPaymentDateAndActiveTrueWithFetch(currentDate).stream()
			.map(ms -> {
				PlanJpaEntity plan = ms.getPlan();
				SubscriptionJpaEntity subscription = ms.getSubscription();
				return new MemberSubscriptionDetail(
					ms.getId(),
					ms.getStartDate(),
					ms.getReminderDaysBefore(),
					ms.getMemo(),
					ms.isDutchPay(),
					ms.getDutchPayAmount(),
					ms.getExchangeRate(),
					ms.getExchangeRateDate(),
					ms.isActive(),
					ms.getNextPaymentDate(),
					ms.getMember().getId(),
					subscription.getId(),
					plan.getId(),
					plan.getAmount(),
					plan.getDurationMonths(),
					subscription.getName(),
					subscription.getLogoImageUrl());
			})
			.toList();
	}

	@Override
	public void update(MemberSubscription memberSubscription) {
		MemberSubscriptionJpaEntity memberSubscriptionEntity = memberSubscriptionRepository
			.findById(memberSubscription.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_NOT_FOUND));

		memberSubscriptionEntity.apply(memberSubscription);

		Long newPlanId = memberSubscription.getPlanId();
		if (!memberSubscriptionEntity.getPlan().getId().equals(newPlanId)) {
			PlanJpaEntity planEntity = planRepository.getReferenceById(newPlanId);
			memberSubscriptionEntity.updatePlan(planEntity);
		}
	}

	@Override
	public void update(List<MemberSubscription> memberSubscriptions) {
		List<Long> ids = memberSubscriptions.stream()
			.map(MemberSubscription::getId)
			.toList();

		Map<Long, MemberSubscription> domainMap = memberSubscriptions.stream()
			.collect(Collectors.toMap(
				MemberSubscription::getId,
				Function.identity()
			));

		List<MemberSubscriptionJpaEntity> memberSubscriptionEntities = memberSubscriptionRepository.findAllById(ids);
		for (MemberSubscriptionJpaEntity entity : memberSubscriptionEntities) {
			entity.apply(
				domainMap.get(entity.getId())
			);
		}
	}

	@Override
	public void delete(Long memberSubscriptionId) {
		memberSubscriptionRepository.deleteById(memberSubscriptionId);
	}
}
