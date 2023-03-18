package ddd.caffeine.ratrip.module.travel_plan.presentation.dto.response;

import java.util.UUID;

import ddd.caffeine.ratrip.module.place.domain.Category;
import ddd.caffeine.ratrip.module.place.domain.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceModelInDaySchedule {
	private UUID id;
	private String name;
	private Category category;
	private Location location;

	@Builder
	public PlaceModelInDaySchedule(UUID id, String name, Category category, Location location) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.location = location;
	}
}