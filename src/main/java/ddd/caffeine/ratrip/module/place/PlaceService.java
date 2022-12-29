package ddd.caffeine.ratrip.module.place;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ddd.caffeine.ratrip.module.place.presentation.dto.PlaceSearchResponseDto;
import ddd.caffeine.ratrip.module.recommend.domain.KakaoFeignModel;
import ddd.caffeine.ratrip.module.recommend.service.KakaoFeignClient;
import lombok.RequiredArgsConstructor;

/**
 * 1. 장소를 검색 하여, 있다면 데이터 내려주고, 없다면 api 호출.
 *
 * 예외
 * - 장소가 업데이트 되었을 경우. (이사 갔거나 뿌셨거나)
 * - 장소가 업데이트 되었는데 사진이 없을경우 -> 프론트에 기본적으로 사진 못찾음 이미지 띄워주게 해야 할 듯.
 *
 */
@Service
@RequiredArgsConstructor
public class PlaceService {

	@Value("${KAKAO_API_KEY}")
	private String KAKAO_API_KEY;
	private final KakaoFeignClient kakaoFeignClient;
	private final PlaceValidator placeValidator;

	public PlaceSearchResponseDto searchPlaces(String keyword, String latitude, String longitude, int page) {
		validateSearchRequestParameters(latitude, longitude, page);
		KakaoFeignModel kakaoFeignModel = readPlaces(keyword, latitude, longitude, page);

		return kakaoFeignModel.mapByPlaceSearchResponseDto();
	}

	private KakaoFeignModel readPlaces(String keyword, String latitude, String longitude, int page) {
		final String KAKAO_REQUEST_HEADER = "KakaoAK " + KAKAO_API_KEY;
		//20KM (MAX)
		final int PLACE_RADIUS = 20000;
		return kakaoFeignClient.readPlacesByKeywordAndCategory(
			KAKAO_REQUEST_HEADER, keyword, latitude, longitude, PLACE_RADIUS, page);
	}

	private void validateSearchRequestParameters(String latitude, String longitude, int page) {
		placeValidator.validatePageSize(page);
		placeValidator.validateRangeLatitude(latitude);
		placeValidator.validateRangeLongitude(longitude);
	}

}
