package subport.application.subscribe.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.objectstorage.OciObjectStorageAdapter;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionUseCase;
import subport.application.subscribe.port.out.SaveCustomSubscriptionPort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@RequiredArgsConstructor
public class RegisterCustomSubscriptionService implements RegisterCustomSubscriptionUseCase {

	private final SaveCustomSubscriptionPort saveCustomSubscriptionPort;
	private final OciObjectStorageAdapter ociObjectStorageAdapter;

	@Transactional
	@Override
	public void register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	) {
		String publicUrl = ociObjectStorageAdapter.upload(image);
		Subscription subscription = Subscription.withoutId(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			publicUrl,
			null,
			memberId
		);
		saveCustomSubscriptionPort.save(subscription);
	}
}
