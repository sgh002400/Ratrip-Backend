package ddd.caffeine.ratrip.module.place.domain.bookmark.repository.dao;

import java.util.UUID;

import com.querydsl.core.annotations.QueryProjection;

import ddd.caffeine.ratrip.module.place.domain.sub_domain.Category;
import lombok.Getter;

@Getter
public class BookMarkPlaceDao {
	private final UUID id;
	private final String name;
	private final String detailAddress;
	private final String imageUrl;
	private final Category category;

	private final String bookmarkId;
	private final boolean isActivated;

	@QueryProjection
	public BookMarkPlaceDao(UUID id, String name, String detailAddress, String imageUrl, Category category,
		String bookmarkId,
		boolean isActivated) {
		this.id = id;
		this.name = name;
		this.detailAddress = detailAddress;
		this.imageUrl = imageUrl;
		this.category = category;
		this.bookmarkId = bookmarkId;
		this.isActivated = isActivated;
	}
}
