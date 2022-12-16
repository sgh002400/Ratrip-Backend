package ddd.caffeine.ratrip.common.jwt;

import static ddd.caffeine.ratrip.common.exception.ExceptionInformation.*;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import ddd.caffeine.ratrip.common.exception.CommonException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class JwtUtil {
	private final JwtSecretKeyProvider jwtSecretKeyProvider;
	private final RedisTemplate<String, Object> redisTemplate;

	public UUID validateTokensAndGetUserId(String accessToken, String refreshToken) {
		validateTokenClaims(refreshToken);
		return getUserIdFromTokens(accessToken, refreshToken);
	}

	private void validateTokenClaims(String token) {
		parseClaim(token);
	}

	private UUID getUserIdFromTokens(String accessToken, String refreshToken) {
		UUID userId = getUserIdFromAccessToken(accessToken);
		validateExistRefreshToken(refreshToken, userId);
		return userId;
	}

	private void validateExistRefreshToken(String refreshToken, UUID userId) {
		Object refreshTokenFromDb = redisTemplate.opsForValue().get("RT:" + userId);

		if (refreshTokenFromDb == null) {
			throw new CommonException(NOT_FOUND_REFRESH_TOKEN_EXCEPTION);
		}

		if (!refreshToken.equals(refreshTokenFromDb)) {
			throw new CommonException(DIFFERENT_REFRESH_TOKEN_EXCEPTION);
		}
	}

	private Claims parseClaim(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(jwtSecretKeyProvider.getSecretKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SecurityException e) {
			throw new CommonException(INVALID_JWT_SIGNATURE_EXCEPTION);
		} catch (MalformedJwtException e) {
			throw new CommonException(INVALID_JWT_TOKEN_EXCEPTION);
		} catch (ExpiredJwtException e) {
			throw new CommonException(EXPIRED_JWT_TOKEN_EXCEPTION);
		} catch (UnsupportedJwtException e) {
			throw new CommonException(UNSUPPORTED_JWT_TOKEN_EXCEPTION);
		} catch (IllegalArgumentException e) {
			throw new CommonException(NOT_FOUND_JWT_CLAIMS_EXCEPTION);
		}
	}

	public UUID getUserIdFromAccessToken(String accessToken) {
		UUID userId = UUID.fromString(parseClaim(accessToken).get(JwtConstants.USER_ID, String.class));
		validateExistUserIdFromAccessToken(userId);
		return userId;
	}

	private void validateExistUserIdFromAccessToken(UUID userId) {
		if (userId == null) {
			throw new CommonException(NOT_FOUND_JWT_USERID_EXCEPTION);
		}
	}

	public void validateAccessToken(String accessToken) {
		UUID userId = UUID.fromString(parseClaim(accessToken).get(JwtConstants.USER_ID, String.class));
		validateExistUserIdFromAccessToken(userId);
	}
}
