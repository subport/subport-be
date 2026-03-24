package subport.domain.feedback;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.domain.BaseTimeEntity;
import subport.domain.member.Member;

@Entity
@Table(name = "feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private FeedbackCategory category;

	private String subCategory;

	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public Feedback(
		FeedbackCategory category,
		String subCategory,
		String content,
		Member member
	) {
		this.category = category;
		this.subCategory = subCategory;
		this.content = content;
		this.member = member;
	}
}
