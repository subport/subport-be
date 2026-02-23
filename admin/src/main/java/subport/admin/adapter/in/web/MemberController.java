package subport.admin.adapter.in.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.member.MemberQueryService;
import subport.admin.application.member.dto.MembersResponse;

@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberQueryService memberService;

	@GetMapping
	public ResponseEntity<MembersResponse> searchMembers(
		@RequestParam(required = false) Boolean deleted,
		@RequestParam(required = false) Boolean reminderEnabled,
		@RequestParam(required = false) String email,
		@PageableDefault(
			size = 15,
			sort = "id",
			direction = Sort.Direction.ASC
		) Pageable pageable
	) {
		return ResponseEntity.ok(
			memberService.searchMembers(deleted, reminderEnabled, email, pageable)
		);
	}
}
