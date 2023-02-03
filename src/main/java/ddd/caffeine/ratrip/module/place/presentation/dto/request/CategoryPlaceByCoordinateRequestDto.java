package ddd.caffeine.ratrip.module.place.presentation.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ddd.caffeine.ratrip.common.validator.RequestDataValidator;
import ddd.caffeine.ratrip.module.place.application.dto.CategoryPlaceByCoordinateDto;
import ddd.caffeine.ratrip.module.place.domain.sub_domain.Category;
import lombok.Getter;

@Getter
public class CategoryPlaceByCoordinateRequestDto {
	@NotBlank(message = "Category must not be blank")
	private final Category category;

	@NotNull(message = "Latitude must not be null")
	private final Double latitude;

	@NotNull(message = "Longitude must not be null")
	private final Double longitude;

	public CategoryPlaceByCoordinateRequestDto(Double latitude, Double longitude, Category category) {
		validateParameters(latitude, longitude);
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private void validateParameters(double latitude, double longitude) {
		RequestDataValidator.validateRangeLatitude(latitude);
		RequestDataValidator.validateRangeLongitude(longitude);
	}

	public CategoryPlaceByCoordinateDto toServiceDto() {
		return CategoryPlaceByCoordinateDto.of(category, latitude, longitude);
	}
}
