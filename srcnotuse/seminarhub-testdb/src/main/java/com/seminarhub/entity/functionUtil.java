package com.seminarhub.entity;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@NoArgsConstructor
//@AllArgsConstructor
public class functionUtil {
    private static final Random RANDOM = new Random();
    private static final String[] streets = {
            "테헤란로", "강남대로", "압구정로", "영동대로", "위례성대로",
            "도산대로", "삼성로", "양재천로", "학동로", "봉은사로",
            "잠실로", "올림픽로", "천호대로", "중앙로", "평화로",
            "통일로", "해운대로", "광복로", "중앙대로", "동백로",
            "수영로", "반송로"
    };

    private static final String[] apartments = {
            "래미안", "자이", "힐스테이트", "롯데캐슬", "푸르지오",
            "e편한세상", "더샵", "아이파크", "SK뷰", "포레나",
            "리버Park", "센트럴파크", "골든뷰", "그린", "파크뷰"
    };

    private static final String[] aptSuffixes = {
            "레이크", "파크", "센트럴", "스카이", "퍼스트",
            "프레스티지", "포레스트", "그랜드", "힐스", "타워",
            "베이", "시티", "원", "타운"
    };

    private static final String[][] districts = {
            // 서울특별시 구역
            {
                    "강남구", "서초구", "종로구", "동대문구", "마포구",
                    "송파구", "양천구", "중랑구", "성동구", "노원구",
                    "구로구", "용산구", "성북구", "은평구", "서대문구",
                    "강서구", "강동구", "관악구", "광진구", "중구", "동작구", "영등포구"
            },
            // 부산광역시 구역
            {
                    "해운대구", "부산진구", "동래구", "남구", "북구",
                    "사상구", "금정구", "연제구", "수영구", "사하구",
                    "서구", "동구", "영도구", "중구", "강서구", "기장군"
            },
            // 대구광역시 구역
            {
                    "중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"
            },
            // 인천광역시 구역
            {
                    "중구", "동구", "미추홀구", "연수구", "남동구",
                    "부평구", "계양구", "서구", "강화군", "옹진군"
            },
            // 광주광역시 구역
            {
                    "동구", "서구", "남구", "북구", "광산구"
            },
            // 대전광역시 구역
            {
                    "동구", "중구", "서구", "유성구", "대덕구"
            },
            // 울산광역시 구역
            {
                    "중구", "남구", "동구", "북구", "울주군"
            },
            // 경기도 구역
            {
                    "수원시", "성남시", "고양시", "용인시", "부천시",
                    "안산시", "안양시", "남양주시", "화성시", "평택시",
                    "의정부시", "시흥시", "파주시", "김포시", "광명시",
                    "광주시", "군포시", "이천시", "오산시", "하남시",
                    "양주시", "구리시", "안성시", "포천시", "의왕시",
                    "여주시", "양평군", "동두천시", "과천시", "가평군", "연천군"
            },
            // 강원도 구역
            {
                    "춘천시", "원주시", "강릉시", "동해시", "태백시",
                    "속초시", "삼척시", "홍천군", "횡성군", "영월군",
                    "평창군", "정선군", "철원군", "화천군", "양구군",
                    "인제군", "고성군", "양양군"
            },
            // 충청북도 구역
            {
                    "청주시", "충주시", "제천시", "보은군", "옥천군",
                    "영동군", "진천군", "괴산군", "음성군", "단양군", "증평군"
            },
            // 충청남도 구역
            {
                    "천안시", "공주시", "보령시", "아산시", "서산시",
                    "논산시", "계룡시", "당진시", "금산군", "부여군",
                    "서천군", "청양군", "홍성군", "예산군", "태안군"
            },
            // 전라북도 구역
            {
                    "전주시", "군산시", "익산시", "정읍시", "남원시",
                    "김제시", "완주군", "진안군", "무주군", "장수군",
                    "임실군", "순창군", "고창군", "부안군"
            },
            // 전라남도 구역
            {
                    "목포시", "여수시", "순천시", "나주시", "광양시",
                    "담양군", "곡성군", "구례군", "고흥군", "보성군",
                    "화순군", "장흥군", "강진군", "해남군", "영암군",
                    "무안군", "함평군", "영광군", "장성군", "완도군", "진도군", "신안군"
            },
            // 경상북도 구역
            {
                    "포항시", "경주시", "김천시", "안동시", "구미시",
                    "영주시", "영천시", "상주시", "문경시", "경산시",
                    "군위군", "의성군", "청송군", "영양군", "영덕군",
                    "청도군", "고령군", "성주군", "칠곡군", "예천군", "봉화군", "울진군", "울릉군"
            },
            // 경상남도 구역
            {
                    "창원시", "진주시", "통영시", "사천시", "김해시",
                    "밀양시", "거제시", "양산시", "의령군", "함안군",
                    "창녕군", "고성군", "남해군", "하동군", "산청군",
                    "함양군", "거창군", "합천군"
            },
            // 제주특별자치도 구역
            {
                    "제주시", "서귀포시"
            }
    };

    private static final String[] cities = {
            "서울특별시", "부산광역시", "대구광역시", "인천광역시",
            "광주광역시", "대전광역시", "울산광역시", "경기도",
            "강원도", "충청북도", "충청남도", "전라북도",
            "전라남도", "경상북도", "경상남도", "제주특별자치도"
    };

    private static final Random rand = new Random();


    public static String f_randaddress() {
        // 시/도 선택
        int cityIndex = rand.nextInt(cities.length);
        String city = cities[cityIndex];
        String district = districts[cityIndex][rand.nextInt(districts[cityIndex].length)];

        // 도로명, 건물번호, 아파트 이름, 동, 호수 조합
        String street = streets[rand.nextInt(streets.length)];
        int buildingNumber = rand.nextInt(300) + 1;
        String apartment = apartments[rand.nextInt(apartments.length)] + aptSuffixes[rand.nextInt(aptSuffixes.length)];
        int dongNumber = rand.nextInt(9) + 1;
        int hoNumber = rand.nextInt(100) + 1;

        return city + " " + district + " " + street + " " + buildingNumber + "번길 " + apartment + " " + dongNumber + "동 " + hoNumber + "호";
    }

    public static Integer f_randminmax(int min, int max) {
        // 최소값이 최대값보다 큰 경우 예외 처리
        if (min > max) {
            return null; // 잘못된 범위 시 null 반환
        }

        // 랜덤 값을 생성하고 반환 (범위 내)
        return rand.nextInt(max - min + 1) + min;
    }

    /**
     * 주어진 시작일과 종료일 사이의 랜덤한 날짜와 시간을 반환합니다.
     *
     * @param startDate 시작 날짜와 시간 (LocalDateTime)
     * @param endDate 종료 날짜와 시간 (LocalDateTime)
     * @return 임의의 날짜와 시간 (LocalDateTime)
     */
    public static LocalDateTime f_randdatetime(LocalDateTime startDate, LocalDateTime endDate) {
        // 시작 날짜와 종료 날짜 사이의 시간 차이를 초 단위로 계산
        long randomDiff = ChronoUnit.SECONDS.between(startDate, endDate);

        // 만약 시작 날짜가 종료 날짜보다 크면 예외 처리
        if (randomDiff < 0) {
            return null; // 잘못된 범위 시 null 반환
        }

        // 무작위 시간 차이를 생성하고 시작 날짜에 더함
        long randomSeconds = (long) (rand.nextDouble() * randomDiff);
        return startDate.plusSeconds(randomSeconds); // 무작위 날짜 및 시간 반환
    }
    // 한국어 형용사 및 명사
    private static final String[] adjectivesKor = {"아름다운", "새로운", "강력한", "성공적인", "고급스러운", "혁신적인", "믿을 수 있는", "우수한", "효율적인", "편리한", "신속한"};
    private static final String[] nounsKor = {"미래", "성장", "솔루션", "테크", "정보", "글로벌", "파워", "아이디어", "시스템", "파트너", "에너지", "통신"};

    // 영어 형용사 및 명사
    private static final String[] adjectivesEng = {"Advanced", "NextGen", "Future", "Green", "Fast", "Prime", "Secure", "Smart", "Trusted", "Dynamic"};
    private static final String[] nounsEng = {"Tech", "Solution", "Global", "Partners", "Systems", "Network", "Consulting", "Energy", "Industries", "Venture"};

    // 접미사
    private static final String[] suffixes = {"Company", "Corporation", "Inc", "Ltd", "Group", "Partners", "Solutions"};
    // 배열에서 랜덤 요소를 반환하는 메서드
    private static String getRandomElement(String[] array) {
        return array[rand.nextInt(array.length)];
    }
    public static String f_randcompanyname() {
        String name;

        // 랜덤하게 한국어/영어 형용사 및 명사 선택
        String adjKor = getRandomElement(adjectivesKor);
        String nounKor = getRandomElement(nounsKor);
        String adjEng = getRandomElement(adjectivesEng);
        String nounEng = getRandomElement(nounsEng);
        String suffix = getRandomElement(suffixes);

        // 랜덤 패턴 선택
        int pattern = rand.nextInt(5);
        switch (pattern) {
            case 0:
                name = adjKor + " " + nounKor + " " + suffix; // 예: 아름다운 미래 Company
                break;
            case 1:
                name = adjEng + " " + nounKor + " " + suffix; // 예: Advanced 미래 Inc
                break;
            case 2:
                name = nounKor + " " + suffix; // 예: 테크 Ltd
                break;
            case 3:
                name = nounEng + " " + suffix; // 예: Tech Solutions
                break;
            default:
                name = adjKor + " " + nounEng + " " + suffix; // 예: 신속한 Consulting Group
                break;
        }

        // 이름이 255자 초과하는지 검사하고, 초과하면 앞부분만 잘라냄
        if (name.length() > 255) {
            name = name.substring(0, 255);
        }

        return name;
    }

    private static final String[] EMOJIS = {"✨ ", "⭐ ", "🌟 ", "💫 ", "🌠 ",
            "📝 ", "✏️ ", "📚 ", "📖 ", "✍️ ",
            "🎮 ", "🎲 ", "🎯 ", "🎪 ", "🎨 ",
            "🏃 ", "💪 ", "🚴 ", "🎾 ", "⚽ ",
            "🎵 ", "🎶 ", "🎸 ", "🎹 ", "🎼 ",
            "💼 ", "📊 ", "📈 ", "🎯 ", "📱 ",
            "🎬 ", "📺 ", "🎭 ", "🎦 ", "📽️ ",
            "🏠 ", "🏡 ", "🏘️ ", "🏰 ", "🌆 ",
            "🌱 ", "🌿 ", "🍀 ", "🌺 ", "🌸 ",
            "📱 ", "💻 ", "🖥️ ", "🖨️ ", "📊 ",
            "📅 ", "📆 ", "🗓️ ", "⌚ ", "⏳ ",
            "💬 ", "📣 ", "📰 ", "🗣️ ", "💭 "};

    private static final String[][] TOPICS = {
            {"2024 트렌드 ", "요즘 대세 ", "최근 유행하는 ", "핫한 ", "인기 있는 "},
            {"프로그래밍", "어학", "자격증", "취업준비", "독서토론"},
            {"취미 공유 - ", "관심사 모임 : ", "동호회 방문기 : ", "주말 활동 : ", "신규 모임 : "},
            {"보드게임", "캘리그라피", "가죽공예", "도예", "드로잉"},
            {"운동 일지 ", "다이어트 도전 ", "건강관리 ", "체중감량 ", "식단관리 "},
            {"Day-", "Week-", "Month-", "Season-", "Challenge-"},
            {"플레이리스트 공유 - ", "추천 음악 : ", "이달의 노래 : ", "좋아하는 곡 : ", "인생 음악 : "},
            {"잔잔한 팝송", "힙합 모음", "재즈 컬렉션", "클래식 명곡", "케이팝 베스트"},
            {"주식/코인 ", "부동산 ", "재테크 ", "투자 ", "금융 "},
            {"시황 분석", "동향 보고", "전망 정리", "투자 전략", "쟁점 정리"},
            {"[넷플릭스] ", "[디즈니+] ", "[왓챠] ", "[티빙] ", "[웨이브] "},
            {"신작 ", "화제작 ", "명작 ", "추천작 ", "인기작 "},
            {"리뷰", "추천", "소개", "후기", "분석"},
            {"홈스타일링 ", "인테리어 ", "집꾸미기 ", "공간정리 ", "하우스투어 "},
            {"노하우", "팁 공유", "비포&애프터", "꿀팁 모음", "성공사례"},
            {"식물 키우기 ", "반려식물 ", "홈가드닝 ", "플랜테리어 ", "베란다정원 "},
            {"시작하기", "관리법", "실패담", "성공기", "노하우"},
            {"최신 기술 ", "IT 뉴스 ", "기술 트렌드 ", "프로그램 언어 ", "앱 추천 "},
            {"이번주 일정 : ", "회의", "세미나", "워크숍", "모임", "행사"},
            {"자유 게시판 - ", "소통", "질문", "정보 공유", "의견", "토론"}
    };

    public static String f_randboardtitle() {
        Random rand = new Random();

        // 랜덤 날짜 생성
        String randYear = "2024";
        String randMonth = String.format("%02d", rand.nextInt(12) + 1);
        String randDay = String.format("%02d", rand.nextInt(28) + 1);

        // 0-11 사이의 랜덤 숫자 생성
        int randNum = rand.nextInt(12);

        StringBuilder result = new StringBuilder();

        switch (randNum) {
            case 0:
                result.append(EMOJIS[rand.nextInt(5)]);
                result.append(TOPICS[0][rand.nextInt(5)]);
                result.append(TOPICS[1][rand.nextInt(5)]);
                break;
            case 1:
                result.append(EMOJIS[rand.nextInt(5) + 5]);
                result.append("스터디 모집 [");
                result.append(TOPICS[1][rand.nextInt(5)]);
                result.append("] ");
                result.append(randMonth).append("/").append(randDay).append(" 시작");
                break;
            case 2:
                result.append(EMOJIS[rand.nextInt(5) + 10]);
                result.append(TOPICS[2][rand.nextInt(5)]);
                result.append(TOPICS[3][rand.nextInt(5)]);
                break;
            case 3:
                result.append(EMOJIS[rand.nextInt(5) + 15]);
                result.append(TOPICS[4][rand.nextInt(5)]);
                result.append(TOPICS[5][rand.nextInt(5)]);
                result.append(rand.nextInt(100));
                break;
            case 4:
                result.append(EMOJIS[rand.nextInt(5) + 20]);
                result.append(TOPICS[6][rand.nextInt(5)]);
                result.append(TOPICS[7][rand.nextInt(5)]);
                break;
            case 5:
                result.append(EMOJIS[rand.nextInt(5) + 25]);
                result.append("[");
                result.append(randYear).append(" ").append(randMonth).append("월] ");
                result.append(TOPICS[8][rand.nextInt(5)]);
                result.append(TOPICS[9][rand.nextInt(5)]);
                break;
            case 6:
                result.append(EMOJIS[rand.nextInt(5) + 30]);
                result.append(TOPICS[10][rand.nextInt(5)]);
                result.append(TOPICS[11][rand.nextInt(5)]);
                result.append(TOPICS[12][rand.nextInt(5)]);
                break;
            case 7:
                result.append(EMOJIS[rand.nextInt(5) + 35]);
                result.append(TOPICS[13][rand.nextInt(5)]);
                result.append(TOPICS[14][rand.nextInt(5)]);
                break;
            case 8:
                result.append(EMOJIS[rand.nextInt(5) + 40]);
                result.append(TOPICS[15][rand.nextInt(5)]);
                result.append(TOPICS[16][rand.nextInt(5)]);
                break;
            case 9:
                result.append(EMOJIS[rand.nextInt(5) + 45]);
                result.append(TOPICS[17][rand.nextInt(5)]);
                break;
            case 10:
                result.append(EMOJIS[rand.nextInt(5) + 50]);
                result.append(TOPICS[18][rand.nextInt(5)]);
                result.append(randMonth).append("/").append(randDay);
                break;
            case 11:
                result.append(EMOJIS[rand.nextInt(5) + 55]);
                result.append(TOPICS[19][rand.nextInt(5)]);
                break;
        }

        return result.toString();
    }

    // 랜덤 문구 배열 선언
    private static final String[] INTRO = {
            "안녕하세요", "여러분", "드디어", "오늘도", "항상 그렇듯",
            "어제처럼", "기다리던", "갑자기", "역시나", "예상대로"
    };

    private static final String[] SUBJECT = {
            "강아지가", "고양이가", "친구가", "동생이", "회사 사람들이",
            "옆집 아저씨가", "우리 팀이", "택배기사님이", "거북이가", "날씨가",
            "음식이", "책이", "영화가", "게임이", "커피가"
    };

    private static final String[] ACTION = {
            "뛰어다녔습니다", "춤을 췄어요", "노래를 불렀어요", "잠들었어요", "사라졌어요",
            "나타났어요", "소리를 질렀어요", "문을 두드렸어요", "전화를 걸었어요", "메시지를 보냈어요",
            "인사를 했어요", "숨어버렸어요", "도망갔어요", "웃고 있어요", "울고 있어요"
    };

    private static final String[] OBJECT = {
            "치킨을", "피자를", "책을", "노트북을", "휴대폰을",
            "가방을", "신발을", "모자를", "안경을", "시계를",
            "커피를", "케이크를", "편지를", "사진을", "열쇠를"
    };

    private static final String[] FEELING = {
            "정말 즐거웠어요", "너무 슬펐어요", "매우 놀라웠어요", "조금 아쉬웠어요", "무척 행복했어요",
            "많이 당황스러웠어요", "너무 신기했어요", "조금 무서웠어요", "굉장히 흥미로웠어요", "완전 짜증났어요",
            "은근히 재미있었어요", "살짝 긴장됐어요", "진짜 황당했어요", "엄청 기뻤어요", "약간 답답했어요"
    };

    private static final String[] LOCATION = {
            "집에서", "회사에서", "학교에서", "공원에서", "카페에서",
            "도서관에서", "지하철에서", "버스에서", "마트에서", "영화관에서",
            "병원에서", "식당에서", "헬스장에서", "수영장에서", "놀이공원에서"
    };

    private static final String[] TIME_EXP = {
            "오늘", "어제", "방금", "조금 전에", "아까",
            "새벽에", "저녁에", "점심시간에", "퇴근 후에", "주말에",
            "휴일에", "출근길에", "한밤중에", "아침일찍", "저녁늦게"
    };

    private static final String[] CLOSING = {
            "다들 좋은 하루 보내세요!", "이상 끝!", "여기까지입니다.",
            "다음에 또 올게요~", "궁금한 점은 댓글 남겨주세요!",
            "읽어주셔서 감사합니다.", "모두 화이팅!", "다음 소식도 기대해주세요!",
            "오늘도 행복하세요~", "그럼 이만 총총..."
    };

    public static String f_randboardcontent() {
        String content;

        // 랜덤 문장 구조 결정
        switch (RANDOM.nextInt(5) + 1) {
            case 1:
                content = String.format("%s! %s %s %s %s. %s. %s",
                        getRandomElement(INTRO),
                        getRandomElement(TIME_EXP),
                        getRandomElement(LOCATION),
                        getRandomElement(SUBJECT),
                        getRandomElement(ACTION),
                        getRandomElement(FEELING),
                        getRandomElement(CLOSING));
                break;

            case 2:
                content = String.format("%s %s %s %s. %s! %s",
                        getRandomElement(TIME_EXP),
                        getRandomElement(SUBJECT),
                        getRandomElement(OBJECT),
                        getRandomElement(ACTION),
                        getRandomElement(FEELING),
                        getRandomElement(CLOSING));
                break;

            case 3:
                content = String.format("%s~ %s %s %s. %s %s. %s",
                        getRandomElement(INTRO),
                        getRandomElement(LOCATION),
                        getRandomElement(OBJECT),
                        getRandomElement(ACTION),
                        getRandomElement(SUBJECT),
                        getRandomElement(FEELING),
                        getRandomElement(CLOSING));
                break;

            case 4:
                content = String.format("%s %s %s %s. %s! %s! %s",
                        getRandomElement(SUBJECT),
                        getRandomElement(TIME_EXP),
                        getRandomElement(LOCATION),
                        getRandomElement(ACTION),
                        getRandomElement(FEELING),
                        getRandomElement(INTRO),
                        getRandomElement(CLOSING));
                break;

            default:
                content = String.format("%s! %s %s %s. %s %s %s. %s",
                        getRandomElement(INTRO),
                        getRandomElement(SUBJECT),
                        getRandomElement(OBJECT),
                        getRandomElement(ACTION),
                        getRandomElement(TIME_EXP),
                        getRandomElement(LOCATION),
                        getRandomElement(FEELING),
                        getRandomElement(CLOSING));
                break;
        }

        return content;
    }

    /**
     * 주어진 비율에 따라 랜덤 성별을 반환합니다.
     *
     * @param maleRatio 남성 비율 (0.0 ~ 1.0)
     * @param femaleRatio 여성 비율 (0.0 ~ 1.0)
     * @return 'M' 또는 'F', 비율이 잘못된 경우 null 반환
     */
    public static Character f_randgender(double maleRatio, double femaleRatio) {
        // 총 비율이 1이 아닌 경우 null 반환
        if (Double.compare(maleRatio + femaleRatio, 1.0) != 0) {
            return null; // 비율 합이 잘못된 경우
        }

        // 랜덤 값 생성
        double randomValue = RANDOM.nextDouble();

        // 비율에 따라 'M' 또는 'F' 반환
        return randomValue < maleRatio ? 'M' : 'F';
    }

    /**
     * 랜덤 등급을 반환합니다.
     *
     * @return 랜덤 등급 ('bronze', 'silver', 'gold', 'platinum')
     */
    public static String f_randgrade() {
        // 랜덤 숫자 생성 (1~4)
        int gradeChoice = RANDOM.nextInt(4) + 1;
        String grade;

        // 등급 결정
        switch (gradeChoice) {
            case 1:
                grade = "bronze";
                break;
            case 2:
                grade = "silver";
                break;
            case 3:
                grade = "gold";
                break;
            case 4:
                grade = "platinum";
                break;
            default:
                grade = null; // 이 경우는 발생하지 않음
        }
        return grade;
    }

    // 사용할 문자 집합
    private static final String CHARACTERS =
            "AAABBBCCCDDDDEEEFFFGGGHHHIIIJJJKKKLLLMMMNNNOOOPQQQRRRSSSTTTUUUUVVVWWWXXXYYZZZ" + // 대문자
                    "aaabbbcccddddeeefffgGGGHHHIIIjjjkkklllmmmnnnoooppqqqrrrssstttuuuuvvvwwwwxxxxyyyzzz" + // 소문자
                    "000111222333444555666777888999" + // 숫자
                    "!!!@@@###$$$%%%^^^&&&*****()___+++===---[]{}|;:.<>?`~';"; // 특수문자

    /**
     * 지정된 길이만큼 랜덤 ID를 생성합니다.
     *
     * @param length 생성할 ID의 길이
     * @return 랜덤 ID
     */
    public static String f_randid(int length) {
        StringBuilder randomString = new StringBuilder(length);

        // 랜덤 문자열 생성
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(index));
        }

        return randomString.toString();
    }

    // 직업 목록을 외부 배열로 선언
    private static final String[] JOBS = {
            "의사", "변호사", "교사", "간호사", "회계사",
            "마케팅 전문가", "프로젝트 매니저", "인사 관리자", "작가", "연구원",
            "패션 디자이너", "셰프", "건축가", "물리치료사", "안전 관리 전문가",
            "서비스업 종사자", "자영업자", "영업 사원", "생명과학자", "게임 디자이너",
            "정치인", "소방관", "경찰관", "지리학자", "환경 과학자",
            "시민권 변호사", "통계학자", "심리학자", "영화 감독", "프로듀서",
            "작곡가", "사진작가", "이벤트 기획자", "물리학자", "화학자",
            "전기 엔지니어", "기계 엔지니어", "토목 엔지니어", "소프트웨어 테스트 엔지니어", "농업 전문가",
            "보건 관리자", "소셜 미디어 매니저", "웹 디자이너", "데이터 엔지니어", "컨설턴트",
            "보험 전문가", "기술 영업 사원", "품질 보증 전문가", "보안 전문가", "행정 직원"
    };

    /**
     * 랜덤 직업을 생성합니다.
     *
     * @return 랜덤 직업
     */
    public static String f_randjob() {
        int jobIndex = RANDOM.nextInt(JOBS.length); // 0부터 JOBS.length - 1까지 랜덤 인덱스 생성
        return JOBS[jobIndex]; // 랜덤으로 선택된 직업 반환
    }


    // 성 리스트와 이름 리스트를 외부 배열로 선언
    private static final String LAST_NAMES = "김이박최정강조윤장임한오서신권황안송류홍전문배백유남심구변우민설탁복진차기장위표명함감고질엽방노명하마양문라채성원마연빈고천봉석임진주조추도탁신국";
    private static final String FIRST_NAMES = "준호민수서연지민성현수현재민가영시우하준유나수아도윤서준예진지후태민은서지아유진채원민재현우다윤정민다인민호세연서영성민가온준혁다온도훈예빈예서서윤찬영다현채민성찬태훈지성수현연우나연민서하윤수진해솔채윤우빈준수하영지호혜린은우서아지우나현정훈소율태연정원세아영호진혁윤수호성준우림승민건우정빈지후승우예림유빈다혜성빈채린시현태호가온세준승훈유진아린민지태경연서지후서진현아혜수도연준서소민태현주하태윤세민채아유림서후수빈태윤도현유나승연연주지유수빈민경다온승호하늘수연태훈나현경민은호지현서영효주지한채민하진정서희수서빈도경시윤경훈진호은빈태주연호서우진서지환서원주현정인서담현성연태우지서채아유경소연정연효린성유다형연희소연";

    /**
     * 랜덤 이름을 생성합니다.
     *
     * @return 랜덤 이름
     */
    public static String f_randname() {
        // 성 선택
        int lastNameIndex = RANDOM.nextInt(LAST_NAMES.length());
        String lastName = LAST_NAMES.substring(lastNameIndex, lastNameIndex + 1);

        // 이름 첫 글자 선택
        int firstNameIndex1 = RANDOM.nextInt(FIRST_NAMES.length());
        String firstName1 = FIRST_NAMES.substring(firstNameIndex1, firstNameIndex1 + 1);

        // 이름 두 번째 글자 선택
        int firstNameIndex2 = RANDOM.nextInt(FIRST_NAMES.length());
        String firstName2 = FIRST_NAMES.substring(firstNameIndex2, firstNameIndex2 + 1);

        // 최종 이름 조합
        return lastName + firstName1 + firstName2;
    }


    /**
     * 랜덤 전화번호를 생성합니다.
     *
     * @return 랜덤 전화번호
     */
    public static String f_randphone() {
        String areaCode;
        // 1. 지역 코드 설정: 서울(02), 휴대전화(010), 기타지역(0XX) 중 선택
        int areaCodeChoice = RANDOM.nextInt(3); // 0, 1, 2 중 선택

        switch (areaCodeChoice) {
            case 0:
                areaCode = "02"; // 서울 번호
                break;
            case 1:
                areaCode = "010"; // 휴대전화
                break;
            default:
                // 기타 지역번호: 031, 032, 051, 052, 053 등
                areaCode = String.format("%03d", RANDOM.nextInt(90) + 10); // 010 ~ 099 범위에서 임의 선택
                break;
        }

        // 2. 전화번호의 가운데 및 마지막 네 자리 부분 설정
        String firstPart = String.format("%04d", RANDOM.nextInt(10000)); // 0~9999 범위
        String secondPart = String.format("%04d", RANDOM.nextInt(10000)); // 0~9999 범위

        // 3. 최종 전화번호 조합
        return String.format("%s-%s-%s", areaCode, firstPart, secondPart);
    }

    private static final String PWCHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";

    /**
     * 랜덤 비밀번호를 생성합니다.
     *
     * @return 랜덤 비밀번호
     */
    public static String f_randpw() {
        // 랜덤 비밀번호의 길이를 14에서 63 사이로 설정
        int length = 14 + RANDOM.nextInt(50); // 14 ~ 63 사이의 길이

        StringBuilder password = new StringBuilder(length);
        int charsLength = PWCHARACTERS.length();

        // 지정된 길이만큼 랜덤한 문자 추가
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(charsLength); // 0부터 charsLength-1 사이의 랜덤 인덱스
            password.append(PWCHARACTERS.charAt(index)); // 랜덤 문자 추가
        }

        return password.toString(); // 최종 비밀번호 반환
    }

    private static final String[] INTRO_TEMPLATES = {
            "현대 사회에서 %s의 중요성은 날로 커지고 있습니다.",
            "4차 산업혁명 시대에 %s는 필수적인 역량이 되었습니다.",
            "급변하는 디지털 환경에서 %s의 가치는 무한합니다.",
            "전문가들이 주목하는 %s. 이제 체계적으로 배워보세요.",
            "글로벌 트렌드로 자리잡은 %s. 이제는 선택이 아닌 필수입니다."
    };

    private static final String[] TARGET_AUDIENCE_TEMPLATES = {
            "• 이제 막 %s을(를) 시작하시는 초보자분들",
            "• %s 분야의 전문성을 높이고 싶으신 실무자분들",
            "• %s에 관심이 있는 학생분들",
            "• %s 관련 창업을 준비하시는 분들",
            "• %s 실무 경험을 쌓고 싶으신 취업준비생분들"
    };

    private static final String[] CURRICULUM_TEMPLATES = {
            "1. %s 기초 개념과 원리 이해",
            "2. 실전 %s 활용 및 응용",
            "3. %s 프로젝트 실습",
            "4. %s 현업 사례 분석",
            "1. %s 기본 이론 학습",
            "2. %s 실무 테크닉 익히기",
            "3. %s 문제 해결 방법론",
            "4. %s 최신 트렌드 분석"
    };

    private static final String[] BENEFITS_TEMPLATES = {
            "✓ %s의 핵심 원리를 완벽하게 이해할 수 있습니다.",
            "✓ 실무에서 바로 활용 가능한 %s 스킬을 습득합니다.",
            "✓ %s 관련 문제 해결 능력이 향상됩니다.",
            "✓ %s 분야의 최신 트렌드를 파악할 수 있습니다."
    };

    private static final String[] METHODOLOGY_TEMPLATES = {
            "• 실습 중심의 hands-on 학습으로 진행됩니다.",
            "• 현업 전문가의 1:1 멘토링이 제공됩니다.",
            "• 실전 프로젝트를 통한 포트폴리오를 구축합니다.",
            "• 이론과 실습을 병행하는 블렌디드 러닝으로 진행됩니다.",
            "• 소규모 그룹 워크샵을 통한 심도 있는 학습이 이루어집니다.",
            "• 실시간 질의응답과 피드백이 제공됩니다."
    };

    private static final String[] REQUIREMENTS_TEMPLATES = {
            "• 기본적인 %s 이해가 있으면 좋습니다.",
            "• 노트북 또는 데스크톱 컴퓨터가 필요합니다.",
            "• 열정과 의지만 있다면 충분합니다.",
            "• %s 관련 기초 지식이 있으면 도움이 됩니다.",
            "• 실습을 위한 개발 환경 세팅이 필요합니다.",
            "• 학습을 위한 시간 투자가 가능해야 합니다."
    };

    private static final String[] KEYWORDS = {
            "클라우드 컴퓨팅", "인공지능", "빅데이터 분석", "디지털 마케팅", "블록체인",
            "사이버 보안", "프로젝트 관리", "UX/UI 디자인", "모바일 앱 개발",
            "데이터 사이언스", "머신러닝", "웹 개발", "게임 개발", "소프트웨어 아키텍처",
            "DevOps", "API 개발", "프론트엔드 개발", "백엔드 개발"
    };

    public static String f_randseminardesciption() {
        String keyword = getRandomKeyword();

        String intro = getRandomTemplate(INTRO_TEMPLATES, keyword);
        String targetAudience = getRandomTemplate(TARGET_AUDIENCE_TEMPLATES, keyword);
        String curriculum = getRandomTemplate(CURRICULUM_TEMPLATES, keyword);
        String benefits = getRandomTemplate(BENEFITS_TEMPLATES, keyword);
        String methodology = getRandomTemplate(METHODOLOGY_TEMPLATES);
        String requirements = getRandomTemplate(REQUIREMENTS_TEMPLATES, keyword);

        // 최종 설명문 조합
        return String.format("%s\n\n【강의 대상】\n%s\n\n【커리큘럼】\n%s\n\n【기대효과】\n%s\n\n【교육 방식】\n%s\n\n【수강 준비사항】\n%s",
                intro, targetAudience, curriculum, benefits, methodology, requirements);
    }

    private static String getRandomTemplate(String[] templates, String... args) {
        String template = templates[RANDOM.nextInt(templates.length)];
        return String.format(template, (args.length > 0) ? args[0] : "");
    }

    private static String getRandomKeyword() {
        return KEYWORDS[RANDOM.nextInt(KEYWORDS.length)];
    }


    private static final String[] TOPIC_TECH = {
            "자바", "파이썬", "자바스크립트", "머신러닝", "웹 개발", "블록체인", "사이버 보안",
            "데이터 과학", "SQL", "클라우드 컴퓨팅", "모바일 앱 개발", "API 설계",
            "UX/UI 디자인", "프론트엔드", "백엔드 개발", "시스템 아키텍처",
            "DevOps", "마이크로서비스", "IoT 개발", "인공지능",
            "빅데이터 분석", "네트워크 보안", "클라우드 네이티브", "도커/쿠버네티스"
    };

    private static final String[] TOPIC_GAMES = {
            "스타크래프트", "리그 오브 레전드", "FIFA", "오버워치", "게임 디자인",
            "이스포츠", "Twitch 스트리밍", "게임 개발", "언리얼 엔진", "유니티",
            "VR 게임", "AI 게임 제작", "게임 마케팅", "게임 스토리텔링",
            "메타버스 게임", "인디 게임 제작", "게임 수익화", "모바일 게임 최적화",
            "게임 커뮤니티 관리", "게임 밸런싱"
    };

    private static final String[] TOPIC_DAILY = {
            "시간 관리", "생산성", "하이킹", "요리", "여행 블로깅",
            "사진 촬영", "재무 계획", "마음 챙김", "개인 브랜딩",
            "소셜 미디어 마케팅", "프리랜싱", "취미 생활", "정리 정돈",
            "도전과 성장", "디지털 노마드", "미니멀리즘", "웰빙 라이프스타일",
            "재테크", "부동산 투자", "온라인 창업"
    };

    private static final String[] ADJECTIVES = {
            "기초", "중급", "고급", "심화", "전문가 과정",
            "실용적", "인터랙티브", "포괄적", "최신", "핵심",
            "체계적", "입문", "경험 기반", "실습 중심", "비즈니스 관점에서의",
            "창의적", "전략적", "효율적인", "혁신적인", "모바일 중심의",
            "집중 탐구", "토론형", "심층", "응용", "재미있는",
            "창조적인", "복합적인", "통합적인", "대안적인", "미래 지향적인",
            "현장 중심", "실전", "트렌드", "글로벌"
    };

    private static final String[] DESCRIPTIONS = {
            "시리즈", "마스터하기", "완전 정복", "워크샵", "부트캠프",
            "핵심 가이드", "실전 프로젝트", "케이스 스터디", "현장 실습",
            "실무 적용", "문제 해결", "프로젝트 실습", "현업 사례",
            "실전 노하우", "심화 과정", "기술 리뷰", "트렌드 분석",
            "전략 수립", "실무 테크닉"
    };

    private static final String[] TARGETS = {
            "초보자를 위한", "실무자를 위한", "현업 전문가의",
            "예비 창업자를 위한", "직장인을 위한", "학생을 위한",
            "시니어를 위한", "주니어 개발자를 위한", "리더를 위한",
            "프리랜서를 위한"
    };

    private static final String[] DURATIONS = {
            "원데이", "3일 완성", "일주일 완성", "2주 완성", "한 달 완성",
            "주말", "평일"
    };

    public static String f_randseminarname() {
        StringBuilder seminarName = new StringBuilder();

        // 50% 확률로 기간 추가
        if (RANDOM.nextDouble() < 0.5) {
            seminarName.append(getRandomElement(DURATIONS)).append(" ");
        }

        // 30% 확률로 대상 추가
        if (RANDOM.nextDouble() < 0.3) {
            seminarName.append(getRandomElement(TARGETS)).append(" ");
        }

        // 형용사 (필수)
        seminarName.append(getRandomElement(ADJECTIVES)).append(" ");

        // 주제 (필수)
        String[] selectedTopic = getRandomTopic();
        seminarName.append(getRandomElement(selectedTopic)).append(" ");

        // 보조 설명구 (필수)
        seminarName.append(getRandomElement(DESCRIPTIONS)).append(" 세미나");

        return seminarName.toString().trim();
    }

    private static String[] getRandomTopic() {
        int topicIndex = RANDOM.nextInt(3);
        switch (topicIndex) {
            case 0:
                return TOPIC_TECH;
            case 1:
                return TOPIC_GAMES;
            case 2:
                return TOPIC_DAILY;
            default:
                return new String[0]; // 이 경우는 발생하지 않음
        }
    }

    public static int f_randintminmax(int min, int max) {
        // 최소값이 최대값보다 큰 경우 예외 처리
        if (min > max) {
            return 0; // Java에서는 NULL 대신 기본값 0을 반환하거나
            // throw new IllegalArgumentException("min cannot be greater than max"); // 예외를 던질 수 있습니다
        }
        // Math.random()은 0.0 이상 1.0 미만의 난수를 생성
        // MySQL의 RAND()와 동일한 기능
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static String f_randpointtype() {
        // Math.random()을 사용하여 0-14 사이의 난수 생성
        int randomNum = (int) (Math.random() * 15);

        // MySQL의 CASE문을 switch문으로 변환
        String result;
        switch (randomNum) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 7:
                result = "SEMINAR_PURCHASE";
                break;
            case 4:
            case 6:
            case 9:
            case 11:
            case 14:  // ELSE에 해당
                result = "WITHDRAW";
                break;
            case 8:
                result = "EVENT";
                break;
            case 10:
                result = "PROMOTION";
                break;
            case 12:
                result = "ADMIN_MANUAL";
                break;
            case 13:
                result = "REFERRAL";
                break;
            default:
                result = "WITHDRAW";
                break;
        }

        return result;
    }
}
