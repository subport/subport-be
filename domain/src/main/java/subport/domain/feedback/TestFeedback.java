package subport.domain.feedback;

import jakarta.persistence.Entity;
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
@Table(name = "test_feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TestFeedback extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String overall;

	private String featureRequest;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public TestFeedback(
		String overall,
		String featureRequest,
		Member member
	) {
		this.overall = overall;
		this.featureRequest = featureRequest;
		this.member = member;
	}
}
