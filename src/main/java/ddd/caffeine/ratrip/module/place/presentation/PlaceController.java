package ddd.caffeine.ratrip.module.place.presentation;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ddd.caffeine.ratrip.module.place.presentation.dto.PlaceInRegionResponseDto;
import ddd.caffeine.ratrip.module.place.presentation.dto.detail.PlaceDetailsByThirdPartyRequestDto;
import ddd.caffeine.ratrip.module.place.presentation.dto.detail.PlaceDetailsByUUIDRequestDto;
import ddd.caffeine.ratrip.module.place.presentation.dto.detail.PlaceDetailsResponseDto;
import ddd.caffeine.ratrip.module.place.presentation.dto.search.PlaceSearchRequestDto;
import ddd.caffeine.ratrip.module.place.presentation.dto.search.PlaceSearchResponseDto;
import ddd.caffeine.ratrip.module.place.service.PlaceService;
import lombok.RequiredArgsConstructor;

/**
 * 장소 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/place")
public class PlaceController {
	private final PlaceService placeService;

	@GetMapping("search")
	public ResponseEntity<PlaceSearchResponseDto> callPlaceSearchApi(
		@Valid @ModelAttribute PlaceSearchRequestDto request) {

		PlaceSearchResponseDto response = placeService.searchPlaces(request.mapByThirdPartySearchOption());
		return ResponseEntity.ok(response);
	}

	@GetMapping("third-party-id")
	public ResponseEntity<PlaceDetailsResponseDto> callPlaceDetailsApiByThirdPartyId(
		@Valid @ModelAttribute PlaceDetailsByThirdPartyRequestDto request) {

		PlaceDetailsResponseDto response = placeService.readPlaceDetailsByThirdPartyId(
			request.mapByThirdPartyDetailSearchOption());

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<PlaceDetailsResponseDto> callPlaceDetailsApiByUUID(
		@Valid @ModelAttribute PlaceDetailsByUUIDRequestDto request) {

		PlaceDetailsResponseDto response = placeService.readPlaceDetailsByUUID(request.getId());
		return ResponseEntity.ok(response);
	}

	/**
	 * default page = 0
	 * Todo : default size 정하기.
	 */
	@GetMapping(value = "regions")
	public ResponseEntity<PlaceInRegionResponseDto> callPlacesInRegionsApi(
		@RequestParam(name = "region", required = false, defaultValue = "전국") List<String> regions,
		@PageableDefault(
			size = 5, sort = "popular", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceInRegionResponseDto response = placeService.readPlacesInRegionsApi(regions, pageable);
		return ResponseEntity.ok(response);
	}
}
