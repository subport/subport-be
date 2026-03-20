package subport.domain.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.domain.BaseTimeEntity;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String providerId;

	private String nickname;

	private String email;

	private boolean paymentReminderEnabled;

	private int reminderDaysBefore;

	private LocalDateTime lastActiveAt;

	private boolean deleted;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	public static Member ofMember(
		String providerId,
		String nickname,
		String email,
		LocalDateTime now
	) {
		Member member = new Member();
		member.providerId = providerId;
		member.nickname = nickname;
		member.email = email;
		member.paymentReminderEnabled = true;
		member.reminderDaysBefore = 3;
		member.lastActiveAt = now;
		member.deleted = false;
		member.role = MemberRole.MEMBER;
		return member;
	}

	public static Member ofGuest(String nickname) {
		Member member = new Member();
		member.nickname = nickname;
		member.paymentReminderEnabled = false;
		member.reminderDaysBefore = 3;
		member.deleted = false;
		member.role = MemberRole.GUEST;
		return member;
	}

	public void update(String nickname, String email) {
		this.nickname = nickname;
		this.email = email;
	}

	public LocalDate calculatePaymentReminderDate(LocalDate nextPaymentDate) {
		if (paymentReminderEnabled) {
			return nextPaymentDate.minusDays(reminderDaysBefore);
		}

		return null;
	}

	public void updateReminderSettings(boolean paymentReminderEnabled, int reminderDaysBefore) {
		this.paymentReminderEnabled = paymentReminderEnabled;

		if (paymentReminderEnabled) {
			this.reminderDaysBefore = reminderDaysBefore;
		}
	}

	public void updateLastActiveAt(LocalDateTime now) {
		this.lastActiveAt = now;
	}

	public void withdraw() {
		this.deleted = true;
	}

	public void reactivate() {
		this.deleted = false;
	}
}
