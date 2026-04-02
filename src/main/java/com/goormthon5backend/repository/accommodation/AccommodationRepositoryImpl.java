package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.domain.entity.QAccommodation;
import com.goormthon5backend.domain.entity.QInventory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private static final QAccommodation accommodation = QAccommodation.accommodation;
    private static final QInventory inventory = QInventory.inventory;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Accommodation> findAllByAreaGroup(
        List<String> areaGroup,
        LocalDate startDate,
        LocalDate endDate
    ) {
        return findAllByConditions(
            areaGroupCondition(areaGroup),
            null,
            availabilityCondition(startDate, endDate)
        );
    }

    @Override
    public List<Accommodation> findAllByKeyword(
        String keyword,
        LocalDate startDate,
        LocalDate endDate
    ) {
        return findAllByConditions(
            null,
            keywordCondition(keyword),
            availabilityCondition(startDate, endDate)
        );
    }

    private List<Accommodation> findAllByConditions(
        Predicate areaCondition,
        Predicate keywordCondition,
        Predicate availabilityCondition
    ) {
        BooleanBuilder where = new BooleanBuilder();
        if (areaCondition != null) {
            where.and(areaCondition);
        }
        if (keywordCondition != null) {
            where.and(keywordCondition);
        }
        if (availabilityCondition != null) {
            where.and(availabilityCondition);
        }

        return queryFactory.selectFrom(accommodation)
            .where(where)
            .orderBy(accommodation.accommodationId.desc())
            .fetch();
    }

    private Predicate areaGroupCondition(List<String> areaGroup) {
        if (areaGroup == null || areaGroup.isEmpty()) {
            return null;
        }

        List<String> keywords = areaGroup.stream()
            .filter(value -> value != null && !value.isBlank())
            .flatMap(value -> Stream.of(value.split("[&,]")))
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .distinct()
            .toList();

        if (keywords.isEmpty()) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();
        keywords.forEach(keyword -> builder.or(accommodation.address.mainAddress.contains(keyword)));
        return builder;
    }

    private Predicate keywordCondition(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        String normalizedKeyword = keyword.trim();
        return accommodation.address.mainAddress.contains(normalizedKeyword)
            .or(accommodation.address.detailAddress.contains(normalizedKeyword))
            .or(accommodation.user.name.contains(normalizedKeyword));
    }

    private Predicate availabilityCondition(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        return JPAExpressions
            .selectOne()
            .from(inventory)
            .where(
                inventory.accommodation.accommodationId.eq(accommodation.accommodationId),
                inventory.stayDate.goe(startDate.atStartOfDay()),
                inventory.stayDate.lt(endDate.plusDays(1).atStartOfDay())
            )
            .notExists();
    }
}
