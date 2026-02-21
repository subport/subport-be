package subport.domain.faq;

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
@Table(name = "faq")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Faq extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String question;

	private String answer;

	public Faq(
		String question,
		String answer
	) {
		this.question = question;
		this.answer = answer;
	}

	public void update(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
}
