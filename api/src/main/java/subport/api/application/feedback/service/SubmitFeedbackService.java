package subport.api.application.feedback.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import subport.api.application.feedback.port.in.SubmitFeedbackUseCase;
import subport.api.application.feedback.port.in.dto.SubmitFeedbackRequest;
import subport.api.application.feedback.port.out.SaveFeedbackPort;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.domain.feedback.Feedback;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class SubmitFeedbackService implements SubmitFeedbackUseCase {

	private final LoadMemberPort loadMemberPort;
	private final SaveFeedbackPort saveFeedbackPort;

	@Override
	public void submitFeedback(Long memberId, SubmitFeedbackRequest request) {
		Member member = loadMemberPort.load(memberId);

		Feedback feedback = new Feedback(
			request.category(),
			request.subCategory(),
			request.content(),
			member
		);

		saveFeedbackPort.save(feedback);
	}
}
