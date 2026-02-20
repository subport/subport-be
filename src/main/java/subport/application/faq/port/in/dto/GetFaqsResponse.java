package subport.application.faq.port.in.dto;

import java.util.List;

public record GetFaqsResponse(List<GetFaqResponse> faqs) {
}
