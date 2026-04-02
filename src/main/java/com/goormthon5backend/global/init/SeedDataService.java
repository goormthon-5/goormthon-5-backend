package com.goormthon5backend.global.init;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeedDataService {

    private final EntityManager entityManager;

    @Transactional
    public void seed() {
        Long userCount = ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM users").getSingleResult()).longValue();
        if (userCount > 0) {
            return;
        }

        Long user1 = insertUser("홍길동", 29, "010-1111-1111");
        Long user2 = insertUser("김예약", 35, "010-2222-2222");
        Long user3 = insertUser("이여행", 31, "010-3333-3333");

        Long accommodation1 = insertAccommodation(
            "성산 일출봉 뷰 스테이",
            "성산 근처 조용한 숙소",
            "제주특별자치도 서귀포시 성산읍",
            "고성리 101-1",
            "63643",
            33.4580f,
            126.9420f,
            120000,
            user1
        );
        Long accommodation2 = insertAccommodation(
            "구좌 감성 독채",
            "구좌 바다와 가까운 독채",
            "제주특별자치도 제주시 구좌읍",
            "세화리 202-2",
            "63361",
            33.5230f,
            126.8590f,
            150000,
            user1
        );
        Long accommodation3 = insertAccommodation(
            "서귀포 오션하우스",
            "서귀포 시내 접근 좋은 숙소",
            "제주특별자치도 서귀포시 서귀동",
            "중앙로 77",
            "63595",
            33.2470f,
            126.5610f,
            110000,
            user2
        );
        Long accommodation4 = insertAccommodation(
            "남원 숲속 스테이",
            "남원 자연 속 힐링 숙소",
            "제주특별자치도 서귀포시 남원읍",
            "위미리 88-3",
            "63612",
            33.2800f,
            126.7170f,
            98000,
            user3
        );

        Long option1 = insertOption("UNKNOWN");
        Long option2 = insertOption("UNKNOWN");
        Long option3 = insertOption("UNKNOWN");

        insertAccommodationOption(accommodation1, option1, 10000);
        insertAccommodationOption(accommodation2, option2, 5000);
        insertAccommodationOption(accommodation3, option3, 0);

        insertGuestBook("깨끗하고 좋았어요", "TEXT", 5, accommodation1, user2);
        insertGuestBook("뷰가 정말 멋져요", "TEXT", 4, accommodation1, user3);
        insertGuestBook("위치가 편리해요", "TEXT", 4, accommodation3, user1);

        Long reservationId = insertReservation(2, user2);
        Long inventoryId = insertInventory(accommodation1, "2026-04-13 00:00:00");
        insertReservationInventory(inventoryId, reservationId);
    }

    private Long insertUser(String name, int age, String phoneNumber) {
        entityManager.createNativeQuery(
            """
                INSERT INTO users (name, age, phone_number)
                VALUES (:name, :age, :phoneNumber)
                """
        )
            .setParameter("name", name)
            .setParameter("age", age)
            .setParameter("phoneNumber", phoneNumber)
            .executeUpdate();
        return lastInsertId();
    }

    private Long insertAccommodation(
        String name,
        String description,
        String mainAddress,
        String detailAddress,
        String postalCode,
        Float latitude,
        Float longitude,
        Integer cost,
        Long userId
    ) {
        entityManager.createNativeQuery(
            """
                INSERT INTO accommodations (
                    name,
                    description,
                    main_address,
                    detail_address,
                    postal_code,
                    latitude,
                    longitude,
                    cost,
                    user_id
                )
                VALUES (
                    :name,
                    :description,
                    :mainAddress,
                    :detailAddress,
                    :postalCode,
                    :latitude,
                    :longitude,
                    :cost,
                    :userId
                )
                """
        )
            .setParameter("name", name)
            .setParameter("description", description)
            .setParameter("mainAddress", mainAddress)
            .setParameter("detailAddress", detailAddress)
            .setParameter("postalCode", postalCode)
            .setParameter("latitude", latitude)
            .setParameter("longitude", longitude)
            .setParameter("cost", cost)
            .setParameter("userId", userId)
            .executeUpdate();
        return lastInsertId();
    }

    private Long insertOption(String name) {
        entityManager.createNativeQuery(
            """
                INSERT INTO options (name)
                VALUES (:name)
                """
        )
            .setParameter("name", name)
            .executeUpdate();
        return lastInsertId();
    }

    private void insertAccommodationOption(Long accommodationId, Long optionId, Integer cost) {
        entityManager.createNativeQuery(
            """
                INSERT INTO accommodation_options (accommodation_id, option_id, cost)
                VALUES (:accommodationId, :optionId, :cost)
                """
        )
            .setParameter("accommodationId", accommodationId)
            .setParameter("optionId", optionId)
            .setParameter("cost", cost)
            .executeUpdate();
    }

    private void insertGuestBook(String content, String type, Integer rating, Long accommodationId, Long userId) {
        entityManager.createNativeQuery(
            """
                INSERT INTO guest_book (content, type, rating, updated_at, created_at, accommodation_id, user_id)
                VALUES (:content, :type, :rating, NOW(), NOW(), :accommodationId, :userId)
                """
        )
            .setParameter("content", content)
            .setParameter("type", type)
            .setParameter("rating", rating)
            .setParameter("accommodationId", accommodationId)
            .setParameter("userId", userId)
            .executeUpdate();
    }

    private Long insertReservation(Integer guestCount, Long userId) {
        entityManager.createNativeQuery(
            """
                INSERT INTO reservatons (guest_count, created_at, user_id)
                VALUES (:guestCount, NOW(), :userId)
                """
        )
            .setParameter("guestCount", guestCount)
            .setParameter("userId", userId)
            .executeUpdate();
        return lastInsertId();
    }

    private Long insertInventory(Long accommodationId, String stayDateTime) {
        entityManager.createNativeQuery(
            """
                INSERT INTO inventorys (accommodation_id, stay_date)
                VALUES (:accommodationId, :stayDate)
                """
        )
            .setParameter("accommodationId", accommodationId)
            .setParameter("stayDate", stayDateTime)
            .executeUpdate();
        return lastInsertId();
    }

    private void insertReservationInventory(Long inventoryId, Long reservationId) {
        entityManager.createNativeQuery(
            """
                INSERT INTO reservaton_inventory (inventory_id, reservaton_id)
                VALUES (:inventoryId, :reservationId)
                """
        )
            .setParameter("inventoryId", inventoryId)
            .setParameter("reservationId", reservationId)
            .executeUpdate();
    }

    private Long lastInsertId() {
        return ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }
}
