package ddd.caffeine.ratrip.module.travel_plan.domain.repository.implementation;

import static ddd.caffeine.ratrip.module.travel_plan.domain.QDaySchedule.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ddd.caffeine.ratrip.module.travel_plan.domain.DaySchedule;
import ddd.caffeine.ratrip.module.travel_plan.domain.repository.DayScheduleQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DayScheduleQueryRepositoryImpl implements DayScheduleQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public DaySchedule findByTravelPlanIdAndDate(UUID travelPlanId, LocalDate date) {
		return jpaQueryFactory
			.selectFrom(daySchedule)
			.where(
				daySchedule.travelPlan.id.eq(travelPlanId),
				daySchedule.date.eq(date)
			)
			.fetchOne();
	}

	@Override
	public List<DaySchedule> findByTravelPlanId(UUID travelPlanId) {
		return jpaQueryFactory
			.selectFrom(daySchedule)
			.where(
				daySchedule.travelPlan.id.eq(travelPlanId), daySchedule.isDeleted.isFalse()
			)
			.fetch();
	}

}