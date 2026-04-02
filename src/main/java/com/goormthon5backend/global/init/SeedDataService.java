package com.goormthon5backend.global.init;

import com.goormthon5backend.domain.enums.GenderType;
import com.goormthon5backend.domain.enums.GuestBookType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SeedDataService {

    private final List<String> posts = Arrays.asList(
            "할망이 차려주신 아침밥 덕분에 고향 집에 온 기분이었어요. 정말 따뜻한 휴식이었네요.",
            "방이 너무 깨끗하고 조용해서 꿀잠 잤습니다. 마당 평상에서 본 밤하늘은 잊지 못할 거예요.",
            "삼춘이랑 같이 나간 포구 낚시가 이번 여행 최고의 하이라이트였습니다. 또 오고 싶어요!",
            "혼자 떠난 여행이라 걱정했는데 어르신이 친손자처럼 대해주셔서 외롭지 않게 잘 쉬다 갑니다.",
            "제주어 교실 너무 재밌었어요! 육지 가서 친구들한테 자랑하려구요. 건강하게 계세요 할망!",
            "LP 음악 들으면서 마신 차 한 잔이 요즘 쌓였던 스트레스를 다 날려버렸네요. 힐링 그 자체.",
            "직접 빚은 만두가 너무 맛있어서 배가 터지는 줄 알았어요. 어르신의 정성이 느껴지는 집입니다.",
            "동네 비밀 산책길은 관광지보다 훨씬 좋았어요. 숨겨진 제주의 매력을 발견한 기분입니다.",
            "뜨개질 배우면서 나눈 대화들이 참 따뜻했습니다. 인생의 지혜를 배우고 가는 소중한 시간이었어요.",
            "귤 따기 체험도 재밌고 텃밭에서 바로 따주신 간식도 꿀맛이었어요. 시골 정취 제대로 느꼈네요.",
            "마당 바비큐는 분위기가 최고였어요. 밤하늘 별 보며 삼춘이랑 나눈 대화들이 기억에 남네요.",
            "아침에 들리는 새소리와 할망의 정겨운 목소리가 그리울 것 같습니다. 꼭 다시 방문할게요.",
            "숙소 곳곳에 정성이 가득해요. 조용하게 혼자 생각 정리하기에 이보다 더 좋은 곳은 없을 듯.",
            "오일장 구경 같이 가서 맛있는 것도 먹고 정말 즐거웠어요. 정 많은 할망 덕분에 행복했습니다.",
            "바둑 한 판 두면서 시간 가는 줄 몰랐네요. 하르방의 옛날 이야기들은 정말 흥미진진했습니다.",
            "함께한 요가와 명상 덕분에 몸과 마음이 한결 가벼워졌어요. 진정한 쉼이 있는 공간입니다.",
            "장 담그기 체험은 처음이었는데 신기하고 재밌었어요. 다음에 제가 담근 장 맛보러 올게요!",
            "강아지와 함께한 산책이 너무 즐거웠어요. 동네 분들도 다들 반갑게 인사해주셔서 좋았습니다.",
            "차 마시며 독서하기 딱 좋은 평상이에요. 바람 소리, 파도 소리 들으며 정말 잘 쉬었습니다.",
            "이런 게 진짜 제주 여행이죠. 화려하진 않지만 마음이 넉넉해지는 숙소였습니다. 강력 추천해요!"
    );

    private final List<String> hostPersonalities = Arrays.asList(
            "정 많은",
            "다정한",
            "푸근한",
            "인정 많은",
            "수다스러운",
            "호탕한",
            "유쾌한",
            "솔직한",
            "털털한",
            "인심 좋은",
            "정겨운"
    );

    private final List<String> hostTraits = Arrays.asList(
            "아침형",
            "새벽형",
            "부지런한",
            "일찍 일어나는",
            "규칙적인",
            "손이 빠른",
            "생활력 강한"
    );

    private final List<String> cleanlinessLevels = Arrays.asList("LV1", "LV2", "LV3");

    private final Random random = new Random();

    private final EntityManager entityManager;

    @Transactional
    public void seed() {
        Long userCount = ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM users").getSingleResult()).longValue();
        if (userCount > 0) {
            return;
        }
 
        // 1. 옵션(액티비티 배지) 생성
        Long opt1 = insertOption("할망 손맛 집밥");
        Long opt2 = insertOption("제철 텃밭 간식");
        Long opt3 = insertOption("동네 비밀 산책");
        Long opt4 = insertOption("포구 밤낚시");
        Long opt5 = insertOption("별 구경 명당");
        Long opt6 = insertOption("귤 따기 체험");
        Long opt7 = insertOption("마당 평상 차담");
        Long opt8 = insertOption("LP판 음악 감상");
        Long opt9 = insertOption("함께 빚는 만두");
        Long opt10 = insertOption("할망 제주어 교실");
        Long opt11 = insertOption("옛날 이야기 보따리");
        Long opt12 = insertOption("뜨개질 원데이");
        Long opt13 = insertOption("장기·바둑 한판");
        Long opt14 = insertOption("함께하는 요가");
        Long opt15 = insertOption("함께하는 독서");
        Long opt16 = insertOption("숲길 명상 걷기");
        Long opt17 = insertOption("오일장 구경 가기");
        Long opt18 = insertOption("전통 장 담그기");
        Long opt19 = insertOption("마당 바비큐");
        Long opt20 = insertOption("반려견 산책 메이트");

        // 2. 초고령 유저(호스트) 50명 생성
        Long u1 = insertUser("김복희", GenderType.FEMALE, 82, "010-1001-0001");
        Long u2 = insertUser("이용식", GenderType.MALE, 79, "010-1002-0002");
        Long u3 = insertUser("박영자", GenderType.FEMALE, 85, "010-1003-0003");
        Long u4 = insertUser("최창석", GenderType.MALE, 81, "010-1004-0004");
        Long u5 = insertUser("정정순", GenderType.FEMALE, 78, "010-1005-0005");
        Long u6 = insertUser("강종필", GenderType.MALE, 80, "010-1006-0006");
        Long u7 = insertUser("한숙자", GenderType.FEMALE, 77, "010-1007-0007");
        Long u8 = insertUser("고만수", GenderType.MALE, 84, "010-1008-0008");
        Long u9 = insertUser("양춘자", GenderType.FEMALE, 79, "010-1009-0009");
        Long u10 = insertUser("오덕배", GenderType.MALE, 82, "010-1010-0010");
        Long u11 = insertUser("문명숙", GenderType.FEMALE, 81, "010-1011-0011");
        Long u12 = insertUser("신기태", GenderType.MALE, 78, "010-1012-0012");
        Long u13 = insertUser("조말숙", GenderType.FEMALE, 83, "010-1013-0013");
        Long u14 = insertUser("윤봉섭", GenderType.MALE, 80, "010-1014-0014");
        Long u15 = insertUser("임정희", GenderType.FEMALE, 76, "010-1015-0015");
        Long u16 = insertUser("백성호", GenderType.MALE, 82, "010-1016-0016");
        Long u17 = insertUser("심점례", GenderType.FEMALE, 85, "010-1017-0017");
        Long u18 = insertUser("권명자", GenderType.FEMALE, 79, "010-1018-0018");
        Long u19 = insertUser("황영철", GenderType.MALE, 81, "010-1019-0019");
        Long u20 = insertUser("손미자", GenderType.FEMALE, 78, "010-1020-0020");
        Long u21 = insertUser("유순철", GenderType.MALE, 83, "010-1021-0021");
        Long u22 = insertUser("전분남", GenderType.FEMALE, 84, "010-1022-0022");
        Long u23 = insertUser("송광수", GenderType.MALE, 77, "010-1023-0023");
        Long u24 = insertUser("한인숙", GenderType.FEMALE, 80, "010-1024-0024");
        Long u25 = insertUser("남길자", GenderType.FEMALE, 82, "010-1025-0025");
        Long u26 = insertUser("허병호", GenderType.MALE, 79, "010-1026-0026");
        Long u27 = insertUser("조경자", GenderType.FEMALE, 81, "010-1027-0027");
        Long u28 = insertUser("장영호", GenderType.MALE, 84, "010-1028-0028");
        Long u29 = insertUser("최옥순", GenderType.FEMALE, 77, "010-1029-0029");
        Long u30 = insertUser("고재식", GenderType.MALE, 80, "010-1030-0030");
        Long u31 = insertUser("배정수", GenderType.MALE, 82, "010-1031-0031");
        Long u32 = insertUser("안영숙", GenderType.FEMALE, 79, "010-1032-0032");
        Long u33 = insertUser("지상철", GenderType.MALE, 78, "010-1033-0033");
        Long u34 = insertUser("류필순", GenderType.FEMALE, 85, "010-1034-0034");
        Long u35 = insertUser("심순태", GenderType.MALE, 81, "010-1035-0035");
        Long u36 = insertUser("오순임", GenderType.FEMALE, 83, "010-1036-0036");
        Long u37 = insertUser("변용구", GenderType.MALE, 79, "010-1037-0037");
        Long u38 = insertUser("한순자", GenderType.FEMALE, 77, "010-1038-0038");
        Long u39 = insertUser("강복순", GenderType.FEMALE, 82, "010-1039-0039");
        Long u40 = insertUser("최덕구", GenderType.MALE, 80, "010-1040-0040");
        Long u41 = insertUser("김영애", GenderType.FEMALE, 78, "010-1041-0041");
        Long u42 = insertUser("박정순", GenderType.FEMALE, 84, "010-1042-0042");
        Long u43 = insertUser("정영기", GenderType.MALE, 81, "010-1043-0043");
        Long u44 = insertUser("이금자", GenderType.FEMALE, 79, "010-1044-0044");
        Long u45 = insertUser("백희숙", GenderType.FEMALE, 82, "010-1045-0045");
        Long u46 = insertUser("유만석", GenderType.MALE, 80, "010-1046-0046");
        Long u47 = insertUser("전명희", GenderType.FEMALE, 77, "010-1047-0047");
        Long u48 = insertUser("송성환", GenderType.MALE, 83, "010-1048-0048");
        Long u49 = insertUser("조정옥", GenderType.FEMALE, 81, "010-1049-0049");
        Long u50 = insertUser("강충식", GenderType.MALE, 79, "010-1050-0050");

        // 3. 청년 유저 2명 생성
        Long u51 = insertUser("이정민", GenderType.MALE, 29, "010-1111-1111");
        Long u52 = insertUser("조영찬", GenderType.MALE, 35, "010-2222-2222");

//        (random.nextInt(10) + 3)

        // 4. 숙소 데이터 생성 (50개) - 옵션 개수 0~3개 다양화 + 방명록 0~10개 랜덤
        Long a1 = insertAccommodation("하가리 연화지 돌담집", "연화못 근처 돌담집입니다. 텃밭 채소 시골 밥상과 마당 평상이 정겹습니다.", "제주시", "애월읍 하가리", "제주시 애월읍 하가로 145", 63038, 33.4612f, 126.3451f, 85000, u1);
        insertAccommodationOption(a1, opt1, 15000);
        insertAccommodationOption(a1, opt7, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a1, u51);
        }

        Long a2 = insertAccommodation("온평포구 바다 사나이네", "바다와 함께 산 삼춘의 이야기. 밤낚시의 손맛을 느껴보세요.", "서귀포시", "성산읍 온평리", "서귀포시 성산읍 온평포구로 54", 63643, 33.3821f, 126.9012f, 70000, u2);
        insertAccommodationOption(a2, opt4, 20000);
        insertAccommodationOption(a2, opt11, 5000);
        insertAccommodationOption(a2, opt10, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a2, u51);
        }

        Long a3 = insertAccommodation("와산리 고요한 숲집", "중산간 마을의 숲 옆집입니다. 아무 방해 없이 새 소리 들으며 쉬어가세요.", "제주시", "조천읍 와산리", "제주시 조천읍 와산로 92", 63341, 33.4723f, 126.6589f, 60000, u3);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a3, u51);
        }

        Long a4 = insertAccommodation("신례리 귤밭 지기 하우스", "사방이 귤나무인 정겨운 집. 귤 따기 체험과 마당 바비큐를 즐기세요.", "서귀포시", "남원읍 신례리", "서귀포시 남원읍 신례천로 167", 63614, 33.3015f, 126.6214f, 95000, u4);
        insertAccommodationOption(a4, opt6, 12000);
        insertAccommodationOption(a4, opt19, 20000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a4, u51);
        }

        Long a5 = insertAccommodation("고산리 수월봉 노을 맛집", "노을이 예쁜 언덕 집. LP 음악을 들으며 할망과 만두를 빚어보세요.", "제주시", "한경면 고산리", "제주시 한경면 고산로 12", 63014, 33.3512f, 126.1723f, 75000, u5);
        insertAccommodationOption(a5, opt8, 5000);
        insertAccommodationOption(a5, opt9, 10000);
        insertAccommodationOption(a5, opt1, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a5, u51);
        }

        Long a6 = insertAccommodation("무릉리 돌담 올레길", "올레길이 지나는 마을입니다. 삼춘과 함께 동네 비밀 산책을 떠나요.", "서귀포시", "대정읍 무릉리", "서귀포시 대정읍 무릉중앙로 21", 63512, 33.2845f, 126.2312f, 65000, u6);
        insertAccommodationOption(a6, opt3, 8000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a6, u51);
        }

        Long a7 = insertAccommodation("서광리 다원 옆 휴식처", "푸른 다원이 펼쳐진 서광리. 평상에서 차 한잔과 독서를 즐기세요.", "서귀포시", "안덕면 서광리", "서귀포시 안덕면 서광동로 103", 63521, 33.3056f, 126.3412f, 90000, u7);
        insertAccommodationOption(a7, opt7, 5000);
        insertAccommodationOption(a7, opt15, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a7, u51);
        }

        Long a8 = insertAccommodation("평대리 제주어 서당", "바다가 보이는 평대리 집. 할망에게 제주어를 배우며 힐링하세요.", "제주시", "구좌읍 평대리", "제주시 구좌읍 평대서길 45", 63360, 33.5321f, 126.8412f, 80000, u8);
        insertAccommodationOption(a8, opt10, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a8, u51);
        }

        Long a9 = insertAccommodation("한경면 바다 앞 고요", "바다 뷰가 환상적인 언덕 끝자락. 조용히 파도 소리만 듣고 싶은 분께.", "제주시", "한경면 고산리", "제주시 한경면 고산로 12", 63014, 33.3512f, 126.1723f, 110000, u9);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a9, u51);
        }

        Long a10 = insertAccommodation("가시리 숲길 명상소", "따라비오름 아래 마을. 숲길 명상과 LP 음악으로 마음을 달래보세요.", "서귀포시", "표선면 가시리", "서귀포시 표선면 가시로 565", 63625, 33.3541f, 126.7412f, 70000, u10);
        insertAccommodationOption(a10, opt16, 12000);
        insertAccommodationOption(a10, opt8, 5000);
        insertAccommodationOption(a10, opt14, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a10, u51);
        }

        Long a11 = insertAccommodation("우도 연평리 별 구경", "우도의 푸른 밤을 느끼는 집. 마당에서 쏟아지는 별을 감상하세요.", "제주시", "우도면 연평리", "제주시 우도면 우도로 122", 63311, 33.5041f, 126.9512f, 100000, u11);
        insertAccommodationOption(a11, opt5, 5000);
        insertAccommodationOption(a11, opt11, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a11, u51);
        }

        Long a12 = insertAccommodation("추자도 포구 앞 돌담집", "추자도의 정취가 가득한 곳. 삼춘과 함께 밤낚시의 재미를!", "제주시", "추자면 대서리", "제주시 추자면 대서길 8", 63001, 33.9612f, 126.2912f, 60000, u12);
        insertAccommodationOption(a12, opt4, 18000);
        insertAccommodationOption(a12, opt3, 7000);
        insertAccommodationOption(a12, opt13, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a12, u51);
        }

        Long a13 = insertAccommodation("삼달리 장독대 마당", "전통 장이 익어가는 정겨운 마당. 할망과 함께 장을 담그고 만두를 빚어봐요.", "서귀포시", "성산읍 삼달리", "서귀포시 성산읍 삼달로 24", 63640, 33.3612f, 126.8512f, 85000, u13);
        insertAccommodationOption(a13, opt18, 20000);
        insertAccommodationOption(a13, opt9, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a13, u51);
        }

        Long a14 = insertAccommodation("태흥리 낚시왕 삼춘", "남원 바다가 마당인 집. 낚시 후 즐기는 마당 바비큐는 최고입니다.", "서귀포시", "남원읍 태흥리", "서귀포시 남원읍 태흥장터로 15", 63618, 33.2841f, 126.6812f, 120000, u14);
        insertAccommodationOption(a14, opt4, 20000);
        insertAccommodationOption(a14, opt19, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a14, u51);
        }

        Long a15 = insertAccommodation("수산리 요가 할망네", "새소리에 깨어 요가를 즐기는 아침. 평상에서 차를 마시며 쉬세요.", "제주시", "애월읍 수산리", "제주시 애월읍 수산서길 7", 63056, 33.4641f, 126.3912f, 80000, u15);
        insertAccommodationOption(a15, opt14, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a15, u51);
        }

        Long a16 = insertAccommodation("상모리 바둑 한판", "대정읍 역사가 깃든 마을. 하르방과 바둑 두며 옛 이야기를 들어보세요.", "서귀포시", "대정읍 상모리", "서귀포시 대정읍 상모대동로 34", 63510, 33.2141f, 126.2612f, 60000, u16);
        insertAccommodationOption(a16, opt13, 5000);
        insertAccommodationOption(a16, opt11, 5000);
        insertAccommodationOption(a16, opt7, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a16, u51);
        }

        Long a17 = insertAccommodation("북촌리 이야기 방", "조천 바다가 보이는 집. 오일장에 가서 할망 단골집을 구경해요.", "제주시", "조천읍 북촌리", "제주시 조천읍 북촌3길 22", 63334, 33.5512f, 126.6912f, 75000, u17);
        insertAccommodationOption(a17, opt11, 5000);
        insertAccommodationOption(a17, opt17, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a17, u51);
        }

        Long a18 = insertAccommodation("덕수리 꽃 피는 집", "계절마다 꽃이 피는 정원집. 텃밭 간식을 먹으며 쉬어가는 공간.", "서귀포시", "안덕면 덕수리", "서귀포시 안덕면 덕수회관로 10", 63528, 33.2512f, 126.3112f, 70000, u18);
        insertAccommodationOption(a18, opt2, 7000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a18, u51);
        }

        Long a19 = insertAccommodation("종달리 웃음 부자네", "제주 동쪽 끝 종달리. 마당 바비큐와 제주어 교실에 초대합니다.", "제주시", "구좌읍 종달리", "제주시 구좌읍 종달로 62", 63365, 33.4912f, 126.9112f, 90000, u19);
        insertAccommodationOption(a19, opt19, 20000);
        insertAccommodationOption(a19, opt10, 10000);
        insertAccommodationOption(a19, opt3, 8000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a19, u51);
        }

        Long a20 = insertAccommodation("세화리 장맛 할망", "따뜻한 햇살 아래 장이 익어갑니다. 정성 가득한 집밥을 드립니다.", "서귀포시", "표선면 세화리", "서귀포시 표선면 세화로 19", 63628, 33.3212f, 126.7912f, 85000, u20);
        insertAccommodationOption(a20, opt1, 15000);
        insertAccommodationOption(a20, opt18, 20000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a20, u51);
        }

        Long a21 = insertAccommodation("신창리 바람 부는 포구", "풍차가 보이는 해안 마을. 밤포구 낚시와 별 구경이 가능합니다.", "제주시", "한경면 신창리", "제주시 한경면 신창로 44", 63022, 33.3441f, 126.1712f, 95000, u21);
        insertAccommodationOption(a21, opt4, 18000);
        insertAccommodationOption(a21, opt5, 5000);
        insertAccommodationOption(a21, opt19, 20000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a21, u51);
        }

        Long a22 = insertAccommodation("명월리 텃밭 마당", "오래된 나무가 멋진 마을. 할망과 만두를 빚으며 시골을 느껴요.", "제주시", "한림읍 명월리", "제주시 한림읍 명월로 89", 63030, 33.3912f, 126.2512f, 70000, u22);
        insertAccommodationOption(a22, opt9, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a22, u51);
        }

        Long a23 = insertAccommodation("하례리 산책 메이트", "산책길이 예쁜 하례리. 강아지와 함께 숲길을 걸어보세요.", "서귀포시", "남원읍 하례리", "서귀포시 남원읍 하례로 112", 63611, 33.2712f, 126.6112f, 75000, u23);
        insertAccommodationOption(a23, opt20, 10000);
        insertAccommodationOption(a23, opt16, 12000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a23, u51);
        }

        Long a24 = insertAccommodation("성산 수산리 고요방", "성산 일출봉 근처 조용한 마을. 평상에서 책 읽으며 고요를 즐기세요.", "서귀포시", "성산읍 수산리", "서귀포시 성산읍 수산서길 4", 63638, 33.4312f, 126.8712f, 65000, u24);
        insertAccommodationOption(a24, opt15, 5000);
        insertAccommodationOption(a24, opt7, 5000);
        insertAccommodationOption(a24, opt2, 7000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a24, u51);
        }

        Long a25 = insertAccommodation("선흘리 숲속 음악실", "람사르 습지가 있는 선흘리. LP 음악과 텃밭 간식이 있는 집.", "제주시", "조천읍 선흘리", "제주시 조천읍 선흘동2길 18", 63336, 33.5112f, 126.7112f, 80000, u25);
        insertAccommodationOption(a25, opt8, 5000);
        insertAccommodationOption(a25, opt2, 8000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a25, u51);
        }

        Long a26 = insertAccommodation("용수리 바람 언덕", "삼춘과 제주어를 배우며 동네 한 바퀴 산책하는 소박한 여행.", "제주시", "한경면 용수리", "제주시 한경면 용수길 5", 63013, 33.3212f, 126.1612f, 70000, u26);
        insertAccommodationOption(a26, opt10, 10000);
        insertAccommodationOption(a26, opt3, 7000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a26, u51);
        }

        Long a27 = insertAccommodation("하모리 오일장 나들이", "모슬포항 근처 마을. 할망과 오일장에 가서 맛있는 것을 사와요.", "서귀포시", "대정읍 하모리", "서귀포시 대정읍 하모중앙로 78", 63513, 33.2212f, 126.2512f, 80000, u27);
        insertAccommodationOption(a27, opt17, 10000);
        insertAccommodationOption(a27, opt9, 10000);
        insertAccommodationOption(a27, opt1, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a27, u51);
        }

        Long a28 = insertAccommodation("덕천리 숲길 휴식", "상큼한 숲 내음 가득한 덕천리. 아무 생각 없이 쉬고 싶은 분께.", "제주시", "구좌읍 덕천리", "제주시 구좌읍 덕천길 31", 63355, 33.4812f, 126.7812f, 75000, u28);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a28, u51);
        }

        Long a29 = insertAccommodation("감산리 다정 할망네", "안덕계곡 근처 마을. 집밥과 뜨개질로 따뜻한 정을 나눕니다.", "서귀포시", "안덕면 감산리", "서귀포시 안덕면 감산로 22", 63529, 33.2412f, 126.3612f, 85000, u29);
        insertAccommodationOption(a29, opt1, 15000);
        insertAccommodationOption(a29, opt12, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a29, u51);
        }

        Long a30 = insertAccommodation("추자도 신양 포구", "추자 바다가 한눈에. 삼춘의 옛 이야기를 들으며 낚시하세요.", "제주시", "추자면 신양리", "제주시 추자면 신양말길 15", 63002, 33.9412f, 126.3212f, 60000, u30);
        insertAccommodationOption(a30, opt11, 5000);
        insertAccommodationOption(a30, opt4, 18000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a30, u51);
        }

        Long a31 = insertAccommodation("하천리 정원사 집", "꽃과 나무가 숨 쉬는 정원. 마당 바비큐와 텃밭 체험이 가능합니다.", "서귀포시", "표선면 하천리", "서귀포시 표선면 하천로 91", 63630, 33.3412f, 126.8212f, 70000, u31);
        insertAccommodationOption(a31, opt19, 20000);
        insertAccommodationOption(a31, opt2, 8000);
        insertAccommodationOption(a31, opt3, 7000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a31, u51);
        }

        Long a32 = insertAccommodation("봉성리 힐링 스테이", "애월 중산간 평화로운 마을. 할망과 책 읽고 요가하며 휴식하세요.", "제주시", "애월읍 봉성리", "제주시 애월읍 봉성로 56", 63045, 33.4112f, 126.3312f, 85000, u32);
        insertAccommodationOption(a32, opt14, 15000);
        insertAccommodationOption(a32, opt15, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a32, u51);
        }

        Long a33 = insertAccommodation("우도 비양도 뷰", "우도 동쪽 끝 별장. 동네 비밀 산책과 밤하늘 별구경을 함께해요.", "제주시", "우도면 서광리", "제주시 우도면 우도해안길 312", 63312, 33.5141f, 126.9412f, 100000, u33);
        insertAccommodationOption(a33, opt3, 10000);
        insertAccommodationOption(a33, opt5, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a33, u51);
        }

        Long a34 = insertAccommodation("의귀리 나물 할망", "숲속 마을 의귀리. 직접 캐온 나물 집밥과 간식이 정겹습니다.", "서귀포시", "남원읍 의귀리", "서귀포시 남원읍 의귀로 45", 63613, 33.3112f, 126.6512f, 90000, u34);
        insertAccommodationOption(a34, opt1, 15000);
        insertAccommodationOption(a34, opt2, 7000);
        insertAccommodationOption(a34, opt11, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a34, u51);
        }

        Long a35 = insertAccommodation("대림리 바둑 정자", "한림의 조용한 시골 마을. 하르방과 바둑 두며 차 한잔 하세요.", "제주시", "한림읍 대림리", "제주시 한림읍 대림길 19", 63025, 33.4212f, 126.2712f, 65000, u35);
        insertAccommodationOption(a35, opt13, 5000);
        insertAccommodationOption(a35, opt7, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a35, u51);
        }

        Long a36 = insertAccommodation("난산리 정 많은 집", "성산읍 작은 마을. 할망의 장 담그기 비법과 옛 이야기를 들어봐요.", "서귀포시", "성산읍 난산리", "서귀포시 성산읍 난산로 82", 63639, 33.4112f, 126.8812f, 75000, u36);
        insertAccommodationOption(a36, opt18, 15000);
        insertAccommodationOption(a36, opt11, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a36, u51);
        }

        Long a37 = insertAccommodation("세화리 바비큐 삼춘", "세화 바다가 보이는 돌담집. 포구 낚시 후 바비큐를 즐기세요.", "제주시", "구좌읍 세화리", "제주시 구좌읍 세화3길 11", 63358, 33.5212f, 126.8512f, 95000, u37);
        insertAccommodationOption(a37, opt4, 20000);
        insertAccommodationOption(a37, opt19, 20000);
        insertAccommodationOption(a37, opt5, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a37, u51);
        }

        Long a38 = insertAccommodation("광평리 숲길 스테이", "안덕 중산간 맑은 공기 가득한 곳. 요가와 명상으로 쉬어가는 집.", "서귀포시", "안덕면 광평리", "서귀포시 안덕면 광평로 6", 63524, 33.3312f, 126.3812f, 80000, u38);
        insertAccommodationOption(a38, opt14, 15000);
        insertAccommodationOption(a38, opt16, 12000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a38, u51);
        }

        Long a39 = insertAccommodation("함덕리 손맛 집", "함덕 해변 근처 마을. 오일장 구경 가고 할망 집밥을 맛보세요.", "제주시", "조천읍 함덕리", "제주시 조천읍 함덕6길 29", 63331, 33.5412f, 126.6612f, 90000, u39);
        insertAccommodationOption(a39, opt17, 10000);
        insertAccommodationOption(a39, opt1, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a39, u51);
        }

        Long a40 = insertAccommodation("판포리 낚시 하르방", "스노클링 명당 판포포구 앞. 하르방과 제주어를 배우며 밤낚시!", "제주시", "한경면 판포리", "제주시 한경면 판포4길 8", 63021, 33.3612f, 126.2012f, 85000, u40);
        insertAccommodationOption(a40, opt4, 18000);
        insertAccommodationOption(a40, opt10, 10000);
        insertAccommodationOption(a40, opt3, 7000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a40, u51);
        }

        Long a41 = insertAccommodation("토산리 뜨개질방", "따뜻한 햇살 아래 뜨개질을 배워보세요. 소박한 휴식을 선사합니다.", "서귀포시", "표선면 토산리", "서귀포시 표선면 토산중앙로 42", 63627, 33.2912f, 126.7512f, 75000, u41);
        insertAccommodationOption(a41, opt12, 10000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a41, u51);
        }

        Long a42 = insertAccommodation("고내리 포근한 삼춘", "애월 해안도로 옆 고내리. 마당 별 구경하며 차 한잔의 여유.", "제주시", "애월읍 고내리", "제주시 애월읍 고내로 15", 63040, 33.4712f, 126.3712f, 85000, u42);
        insertAccommodationOption(a42, opt7, 5000);
        insertAccommodationOption(a42, opt5, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a42, u51);
        }

        Long a43 = insertAccommodation("동일리 비밀 산책", "대정읍 동일리 바다 앞 돌담집. 삼춘과 함께 비밀 산책을 떠나요.", "서귀포시", "대정읍 동일리", "서귀포시 대정읍 동일로 45", 63514, 33.2312f, 126.2312f, 70000, u43);
        insertAccommodationOption(a43, opt3, 7000);
        insertAccommodationOption(a43, opt4, 18000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a43, u51);
        }

        Long a44 = insertAccommodation("한남리 만두 할망", "숲 체험 마을 한남리. 할망의 집밥과 함께 빚는 만두 체험.", "서귀포시", "남원읍 한남리", "서귀포시 남원읍 한남로 12", 63612, 33.2912f, 126.6312f, 80000, u44);
        insertAccommodationOption(a44, opt9, 10000);
        insertAccommodationOption(a44, opt1, 15000);
        insertAccommodationOption(a44, opt11, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a44, u51);
        }

        Long a45 = insertAccommodation("협재리 조용한 독서", "협재 바다가 보이는 예쁜 집. 혼자 조용히 책 읽기 좋은 공간.", "제주시", "한림읍 협재리", "제주시 한림읍 협재로 74", 63032, 33.3912f, 126.2412f, 90000, u45);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a45, u51);
        }

        Long a46 = insertAccommodation("교래리 숲속 이야기", "교래 숲속 마을의 정겨운 집. 숲길 걷고 옛 이야기를 들어보세요.", "제주시", "조천읍 교래리", "제주시 조천읍 교래로 115", 63340, 33.4312f, 126.6712f, 75000, u46);
        insertAccommodationOption(a46, opt11, 5000);
        insertAccommodationOption(a46, opt16, 12000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a46, u51);
        }

        Long a47 = insertAccommodation("신풍리 텃밭 간식", "평화로운 신풍리 마을. 텃밭을 가꾸고 신선한 간식을 따 먹어요.", "서귀포시", "성산읍 신풍리", "서귀포시 성산읍 신풍남로 22", 63641, 33.3412f, 126.8612f, 70000, u47);
        insertAccommodationOption(a47, opt2, 7000);
        insertAccommodationOption(a47, opt19, 15000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a47, u51);
        }

        Long a48 = insertAccommodation("위미리 귤밭 친구", "남원 위미항 근처 귤밭 집. 귤 따기 체험 후 강아지와 산책해요.", "서귀포시", "남원읍 위미리", "서귀포시 남원읍 위미대성로 104", 63617, 33.2712f, 126.6612f, 85000, u48);
        insertAccommodationOption(a48, opt6, 10000);
        insertAccommodationOption(a48, opt20, 10000);
        insertAccommodationOption(a48, opt7, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a48, u51);
        }

        Long a49 = insertAccommodation("하도리 차 마시는 방", "철새 도래지 하도리의 고요한 집. 제주어를 배우며 차를 마셔요.", "제주시", "구좌읍 하도리", "제주시 구좌읍 하도로 56", 63362, 33.5112f, 126.8912f, 80000, u49);
        insertAccommodationOption(a49, opt10, 10000);
        insertAccommodationOption(a49, opt7, 5000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a49, u51);
        }

        Long a50 = insertAccommodation("신창리 밤낚시 하르방", "풍차가 보이는 신창 바다. 하르방과 밤낚시하며 바비큐를 즐기세요.", "제주시", "한경면 신창리", "제주시 한경면 신창해안로 15", 63022, 33.3441f, 126.1712f, 85000, u50);
        insertAccommodationOption(a50, opt4, 20000);
        insertAccommodationOption(a50, opt19, 20000);
        for (int i = 0; i < random.nextInt(10); i++) {
            insertGuestBook(posts.get(random.nextInt(posts.size())), GuestBookType.TEXT, random.nextInt(5), a50, u51);
        }

        // 5. 각 숙소마다 예약 가능한 날짜에 맞춰 예약 생성하기
        LocalDate today = LocalDate.now();
        List<LocalDate> stayDates = today.datesUntil(today.plusDays(31)).toList();
        stayDates.stream()
                .filter(e -> Math.random() > 0.2)
                .map((eachDate) -> insertInventory(a1, eachDate))
                .filter(e -> Math.random() > 0.8)
                .forEach((randomlyPickedInventory) -> {
                    Long reservationId = insertReservation(1, u51);
                    insertReservationInventory(randomlyPickedInventory, reservationId);
                });
    }


    private Long insertUser(String name, GenderType gender, int age, String phoneNumber) {
        entityManager.createNativeQuery(
                        """
                                INSERT INTO users (name, gender, age, phone_number)
                                VALUES (:name, :gender, :age, :phoneNumber)
                                """
                )
                .setParameter("name", name)
                .setParameter("gender", gender)
                .setParameter("age", age)
                .setParameter("phoneNumber", phoneNumber)
                .executeUpdate();
        return lastInsertId();
    }

    private Long insertAccommodation(
            String name,
            String description,
            String addressGroup,
            String addressShort,
            String addressDetail,
            Integer postalCode,
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
                                    address_group,
                                    address_short,
                                    address_detail,
                                    postal_code,
                                    latitude,
                                    longitude,
                                    cost,
                                    user_id
                                )
                                VALUES (
                                    :name,
                                    :description,
                                    :addressGroup,
                                    :addressShort,
                                    :addressDetail,
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
                .setParameter("addressGroup", addressGroup)
                .setParameter("addressShort", addressShort)
                .setParameter("addressDetail", addressDetail)
                .setParameter("postalCode", postalCode)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("cost", cost)
                .setParameter("userId", userId)
                .executeUpdate();
        Long accommodationId = lastInsertId();
        insertAccommodationHostInfo(
                accommodationId,
                hostPersonalities.get(random.nextInt(hostPersonalities.size())),
                hostTraits.get(random.nextInt(hostTraits.size())),
                cleanlinessLevels.get(random.nextInt(cleanlinessLevels.size())),
                random.nextBoolean()
        );
        return accommodationId;
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

    private void insertAccommodationHostInfo(
            Long accommodationId,
            String personality,
            String trait,
            String cleanlinessLevel,
            boolean hasWifi
    ) {
        entityManager.createNativeQuery(
                        """
                                INSERT INTO accommodation_host_info (
                                    accommodation_id,
                                    personality,
                                    trait,
                                    cleanliness_level,
                                    has_wifi
                                )
                                VALUES (
                                    :accommodationId,
                                    :personality,
                                    :trait,
                                    :cleanlinessLevel,
                                    :hasWifi
                                )
                                """
                )
                .setParameter("accommodationId", accommodationId)
                .setParameter("personality", personality)
                .setParameter("trait", trait)
                .setParameter("cleanlinessLevel", cleanlinessLevel)
                .setParameter("hasWifi", hasWifi)
                .executeUpdate();
    }

    private void insertGuestBook(String content, GuestBookType type, Integer rating, Long accommodationId, Long userId) {
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

    private Long insertInventory(Long accommodationId, LocalDate stayDate) {
        entityManager.createNativeQuery(
                        """
                                INSERT INTO inventorys (accommodation_id, stay_date)
                                VALUES (:accommodationId, :stayDate)
                                """
                )
                .setParameter("accommodationId", accommodationId)
                .setParameter("stayDate", stayDate)
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
