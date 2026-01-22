package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
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
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionForSpendingRecord;
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
	public MemberSubscriptionDetail loadDetail(Long memberSubscriptionId) {
		MemberSubscriptionJpaEntity memberSubscriptionEntity = memberSubscriptionRepository
			.findByIdWithFetch(memberSubscriptionId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_NOT_FOUND));

		SubscriptionJpaEntity subscriptionEntity = memberSubscriptionEntity.getSubscription();
		PlanJpaEntity planEntity = memberSubscriptionEntity.getPlan();

		return new MemberSubscriptionDetail(
			memberSubscriptionEntity.getId(),
			subscriptionEntity.getId(),
			subscriptionEntity.getName(),
			subscriptionEntity.getLogoImageUrl(),
			subscriptionEntity.getType(),
			memberSubscriptionEntity.getStartDate(),
			memberSubscriptionEntity.getLastPaymentDate(),
			memberSubscriptionEntity.getNextPaymentDate(),
			memberSubscriptionEntity.getReminderDaysBefore(),
			memberSubscriptionEntity.isActive(),
			memberSubscriptionEntity.isDutchPay(),
			memberSubscriptionEntity.getDutchPayAmount(),
			memberSubscriptionEntity.getMemo(),
			memberSubscriptionEntity.getExchangeRate(),
			planEntity.getId(),
			planEntity.getName(),
			planEntity.getAmount(),
			planEntity.getAmountUnit(),
			planEntity.getDurationMonths(),
			memberSubscriptionEntity.getMember().getId()
		);
	}

	@Override
	public List<MemberSubscriptionDetail> loadDetails(
		Long memberId,
		boolean active,
		String sortBy
	) {
		Sort sort = createSort(sortBy);

		return memberSubscriptionRepository.findByMemberIdAndActive(memberId, active, sort).stream()
			.map(ms -> {
				SubscriptionJpaEntity subscription = ms.getSubscription();
				PlanJpaEntity plan = ms.getPlan();

				return new MemberSubscriptionDetail(
					ms.getId(),
					subscription.getId(),
					subscription.getName(),
					subscription.getLogoImageUrl(),
					subscription.getType(),
					ms.getStartDate(),
					ms.getLastPaymentDate(),
					ms.getNextPaymentDate(),
					ms.getReminderDaysBefore(),
					ms.isActive(),
					ms.isDutchPay(),
					ms.getDutchPayAmount(),
					ms.getMemo(),
					ms.getExchangeRate(),
					plan.getId(),
					plan.getName(),
					plan.getAmount(),
					plan.getAmountUnit(),
					plan.getDurationMonths(),
					ms.getMember().getId()
				);
			}).toList();
	}

	@Override
	public List<MemberSubscriptionForSpendingRecord> loadForSpendingRecordByNextPaymentDate(LocalDate currentDate) {
		return memberSubscriptionRepository.findByNextPaymentDateAndActiveTrueWithFetch(currentDate).stream()
			.map(ms -> {
				PlanJpaEntity plan = ms.getPlan();
				SubscriptionJpaEntity subscription = ms.getSubscription();
				return new MemberSubscriptionForSpendingRecord(
					ms.getId(),
					ms.getStartDate(),
					ms.getReminderDaysBefore(),
					ms.getMemo(),
					ms.isDutchPay(),
					ms.getDutchPayAmount(),
					ms.getExchangeRate(),
					ms.getExchangeRateDate(),
					ms.isActive(),
					ms.getLastPaymentDate(),
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

	private Sort createSort(String sortBy) {
		return switch (sortBy) {
			case "type" -> Sort.by(Sort.Direction.ASC, "subscription.type", "subscription.name");
			case "nextPaymentDate" -> Sort.by(Sort.Direction.ASC, "nextPaymentDate", "subscription.name");
			default -> Sort.by(Sort.Direction.ASC, "subscription.name");
		};
	}
}
