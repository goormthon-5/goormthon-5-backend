package com.goormthon5backend.repository.accommodation;

import com.goormthon5backend.domain.entity.Accommodation;
import com.goormthon5backend.domain.entity.QAccommodation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private static final QAccommodation accommodation = QAccommodation.accommodation;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Accommodation> findAllByAreaGroup(List<String> areaGroup) {
        return findAllByConditions(areaGroupCondition(areaGroup), null);
    }

    @Override
    public List<Accommodation> findAllByKeyword(String keyword) {
        return findAllByConditions(null, keywordCondition(keyword));
    }

    private List<Accommodation> findAllByConditions(Predicate areaCondition, Predicate keywordCondition) {
        BooleanBuilder where = new BooleanBuilder();
        if (areaCondition != null) {
            where.and(areaCondition);
        }
        if (keywordCondition != null) {
            where.and(keywordCondition);
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
}
