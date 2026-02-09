package subport.domain.member;

import java.time.LocalDate;

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

	public Member(
		String providerId,
		String nickname,
		String email
	) {
		this.providerId = providerId;
		this.nickname = nickname;
		this.email = email;
		this.paymentReminderEnabled = true;
		this.reminderDaysBefore = 1;
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
}
