package ddd.caffeine.ratrip.module.recommend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ddd.caffeine.ratrip.module.recommend.domain.RecommendPlaceData;
import ddd.caffeine.ratrip.module.recommend.domain.kakao.KakaoFeignModel;
import ddd.caffeine.ratrip.module.recommend.domain.naver.NaverImageModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendPlaceService {

	@Value("${KAKAO_API_KEY}")
	private String KAKAO_API_KEY;

	@Value("${NAVER_ID}")
	private String NAVER_ID;

	@Value("${NAVER_SECRET}")
	private String NAVER_SECRET;

	private final KakaoFeignClient kakaoFeignClient;
	private final NaverFeignClient naverFeignClient;
	private final RecommendPlaceValidator recommendPlaceValidator;

	public KakaoFeignModel recommendPlaces(String region, String keyword, int page) {
		recommendPlaceValidator.validatePageSize(page);
		KakaoFeignModel missingImagePlaces = readPlaces(region, keyword, page);
		return injectImageModelEachPlace(missingImagePlaces);
	}

	private KakaoFeignModel readPlaces(String region, String keyword, int page) {
		final String KAKAO_REQUEST_HEADER = "KakaoAK " + KAKAO_API_KEY;
		String query = region + " " + keyword;
		return kakaoFeignClient.readPlacesByKeywordAndCategory(KAKAO_REQUEST_HEADER, query, page);
	}

	/**
	 * Naver API - 너무 많은 요청 Error
	 */
	private KakaoFeignModel injectImageModelEachPlace(KakaoFeignModel kakaoFeignModel) {
		final int DATA_COUNT = 1;
		final String SORT_TYPE = "sim";

		List<RecommendPlaceData> documents = kakaoFeignModel.getDocuments();
		for (int i = 0; i < documents.size(); i++) {
			String placeName = readPlaceName(documents.get(i));
			NaverImageModel naverImageModel = naverFeignClient.readImageModelByPlaceName(
				NAVER_ID, NAVER_SECRET, placeName, DATA_COUNT, SORT_TYPE
			);
			documents.get(i).injectImageModel(naverImageModel);
		}
		return kakaoFeignModel;
	}

	private String readPlaceName(RecommendPlaceData kakaoPlaceData) {
		return kakaoPlaceData.getPlaceName();
	}
}
