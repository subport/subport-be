package subport.application.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.token.port.in.SaveRefreshTokenUseCase;
import subport.application.token.port.out.SaveRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Service
@RequiredArgsConstructor
public class SaveRefreshTokenService implements SaveRefreshTokenUseCase {

	private final SaveRefreshTokenPort saveRefreshTokenPort;

	@Transactional
	@Override
	public void save(RefreshToken refreshToken) {
		saveRefreshTokenPort.save(refreshToken);
	}
}
