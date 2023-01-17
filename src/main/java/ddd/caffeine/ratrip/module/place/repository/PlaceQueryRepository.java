package ddd.caffeine.ratrip.module.place.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import ddd.caffeine.ratrip.module.place.model.Category;
import ddd.caffeine.ratrip.module.place.model.Place;
import ddd.caffeine.ratrip.module.place.model.Region;

public interface PlaceQueryRepository {
	Slice<Place> findPlacesInRegions(List<Region> regions, Pageable pageable);

	Slice<Place> findPlacesInCategories(Set<Category> categories, Pageable pageable);
}
