package subport.api.application.feedback.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import subport.api.application.feedback.port.in.SubmitTestFeedbackUseCase;
import subport.api.application.feedback.port.in.dto.SubmitTestFeedbackRequest;
import subport.api.application.feedback.port.out.SaveTestFeedbackPort;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.domain.feedback.TestFeedback;
import subport.domain.member.Member;
import subport.domain.member.MemberRole;

@Service
@Transactional
@RequiredArgsConstructor
public class SubmitTestFeedbackService implements SubmitTestFeedbackUseCase {

	private final LoadMemberPort loadMemberPort;
	private final SaveTestFeedbackPort saveTestFeedbackPort;

	@Override
	public void submitTestFeedback(Long memberId, SubmitTestFeedbackRequest request) {
		Member member = loadMemberPort.load(memberId);

		if (member.getRole() == MemberRole.GUEST) {
			member = null;
		}

		TestFeedback testFeedback = new TestFeedback(
			request.overall(),
			request.featureRequest(),
			member
		);

		saveTestFeedbackPort.save(testFeedback);
	}
}
