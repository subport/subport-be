package subport.domain.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;

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

	private LocalDateTime lastLoginAt;

	private boolean deleted;

	private boolean firstLogin;

	public Member(
		String providerId,
		String nickname,
		String email,
		LocalDateTime now
	) {
		this.providerId = providerId;
		this.nickname = nickname;
		this.email = email;
		this.paymentReminderEnabled = false;
		this.reminderDaysBefore = 3;
		this.lastLoginAt = now;
		this.deleted = false;
		this.firstLogin = true;
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

	public void updateLastLoginAt(LocalDateTime now) {
		this.lastLoginAt = now;
	}

	public void deactivate() {
		this.deleted = true;
	}

	public void activate() {
		this.deleted = false;
	}

	public void completeFirstLogin() {
		this.firstLogin = false;
	}
}
