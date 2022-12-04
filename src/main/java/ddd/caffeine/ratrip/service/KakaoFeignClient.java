package ddd.caffeine.ratrip.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import ddd.caffeine.ratrip.configuration.FeignConfig;

@FeignClient(name = "KakaoFeignClient", url = "https://dapi.kakao.com", configuration = FeignConfig.class)
public interface KakaoFeignClient {
	@GetMapping(value = "v2/local/search/keyword.json")
	KakaoFeignResponseDto readPlacesByKeywordAndCategory(
		@RequestHeader("Authorization") String header,
		@RequestParam("query") String query
	);
}
