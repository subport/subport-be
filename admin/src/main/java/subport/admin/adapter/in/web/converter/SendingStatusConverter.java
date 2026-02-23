package subport.admin.adapter.in.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import subport.domain.emailnotification.SendingStatus;

@Component
public class SendingStatusConverter implements Converter<String, SendingStatus> {

	@Override
	public SendingStatus convert(String value) {
		return SendingStatus.valueOf(value.toUpperCase());
	}
}
