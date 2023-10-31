package com.seminarhub.elasticsearch.service;

import com.seminarhub.elasticsearch.document.SeminarDocument;
import com.seminarhub.elasticsearch.dto.PageRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class SeminarElasticSearchServiceTests {

    @Autowired
    private SeminarElasticSearchService seminarElasticSearchService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private String[] seminar_nameArr = {
            "Arizona", "Mifflin", "Forster", "Macpherson", "Merry", "John Wall",
            "Texas", "Arrowood", "Hooker", "Fulton", "Hauk", "Mendota", "Ridgeway",
            "South", "Golf", "Dorton", "Maple Wood", "Corry", "Sugar", "Moland",
            "Ryan", "Welch", "Scoville", "Moulton", "Duke", "Debra", "Crownhardt",
            "Northwestern", "Waywood", "Surrey", "Stone Corner", "Marquette",
            "Fremont", "Coolidge", "Del Mar", "Sunfield", "Northview", "Sloan",
            "Village", "Marcy", "Service", "Judy", "Dryden", "Namekagon", "Barby",
            "Packers", "Mockingbird", "Hovde", "Thierer", "American", "Little Fleur",
            "Welch", "Golden Leaf", "Haas", "Coolidge", "Coleman", "Sage",
            "Stone Corner", "Columbus", "Garrison", "West", "Texas", "Hermina",
            "Ilene", "Claremont", "Westend", "Beilfuss", "Rieder", "Mitchell",
            "Toban", "Grayhawk", "Magdeline", "Northwestern", "Grasskamp", "6th",
            "Eastlawn", "Eastlawn", "Homewood", "Calypso", "Cambridge", "Service",
            "Bobwhite", "Sullivan", "Eastwood", "Utah", "David", "Hermina", "Rigney",
            "Division", "Northport", "Shelley", "Glacier Hill", "Ilene", "Scott",
            "Mariners Cove", "Melby", "Bashford", "Kim", "Boyd", "Pearson",
            "Swallow", "Oriole", "Magdeline", "Park Meadow", "Lindbergh", "Hanson",
            "Hoard", "Commercial", "Fulton", "Welch", "Waywood", "Melvin", "Talisman",
            "Lerdahl", "Memorial", "Quincy", "Bayside", "Lukken", "Garrison",
            "Fieldstone", "Harbort", "Northwestern", "Fieldstone", "Birchwood",
            "Parkside", "Londonderry", "Pennsylvania", "Annamark", "Forster"};
    private String[] seminar_explanations = {
            "3291 Westport Center", "05 Corben Lane", "35096 Merry Alley", "9635 Cordelia Pass",
            "499 Caliangt Trail", "093 Ruskin Crossing", "86690 Debs Road", "06 Menomonie Avenue",
            "1814 Continental Hill", "7244 Village Court", "18 Homewood Hill", "19145 Clyde Gallagher Plaza",
            "913 Bowman Road", "8844 Division Point", "823 Thompson Junction", "4 Banding Alley",
            "32604 Farmco Parkway", "8 Parkside Way", "0120 Melvin Trail", "10 Norway Maple Avenue",
            "359 Kenwood Parkway", "6 Laurel Court", "67 Anniversary Park", "901 Schlimgen Court",
            "953 Continental Trail", "27477 Shopko Crossing", "5454 Carpenter Trail", "420 Shopko Alley",
            "156 Ryan Drive", "19873 Farwell Point", "30032 Garrison Trail", "40 Mallard Pass",
            "47 Eagle Crest Place", "16 Bashford Park", "6419 Kedzie Street", "487 Dixon Court",
            "8661 Spohn Junction", "614 Longview Terrace", "38462 Lunder Trail", "29 Sunfield Lane",
            "0299 Forest Trail", "2199 Oak Valley Junction", "80 Muir Point", "581 Tomscot Way",
            "4 Colorado Center", "21 Mosinee Way", "99381 Onsgard Alley", "9 Prairieview Terrace",
            "46 Village Green Court", "5 Warbler Street", "4 Marcy Circle", "874 Oneill Lane",
            "54 Washington Crossing", "38 Macpherson Pass", "19007 Moulton Hill", "6 Granby Alley",
            "90288 Lakewood Lane", "2486 Mesta Center", "5242 Bluejay Junction", "801 Mifflin Court",
            "25116 Londonderry Terrace", "6301 Kim Pass", "76730 South Crossing", "98473 Clyde Gallagher Street",
            "406 Anzinger Trail", "6 Bellgrove Hill", "92 Crownhardt Junction", "105 Charing Cross Crossing",
            "0892 Sommers Crossing", "9277 John Wall Drive", "51 Jenifer Park", "22 Prairieview Plaza",
            "874 Debra Parkway", "75105 Colorado Plaza", "4 Springs Hill", "02496 Lindbergh Terrace",
            "59 Lawn Way", "179 Logan Court", "89 Haas Junction", "1648 Oakridge Center",
            "45 Dunning Point", "6348 Lukken Terrace", "268 Troy Plaza", "2260 Erie Pass",
            "29 Bay Avenue", "936 Service Trail", "0536 Fairfield Trail", "4970 Clarendon Parkway",
            "9 Upham Point", "4407 Melody Point", "2 Duke Junction", "042 Dexter Way",
            "4832 Lien Crossing", "29411 Dakota Parkway", "2414 Heffernan Plaza", "8 Pleasure Plaza",
            "3 Gina Parkway", "234 Lake View Road", "022 Prairieview Plaza", "976 Monica Circle",
            "410 Longview Alley", "470 7th Circle", "469 Little Fleur Crossing", "89173 Schiller Crossing",
            "013 Doe Crossing Alley", "368 Dahle Court", "193 Eastwood Park", "570 Spaight Point",
            "266 Hermina Point", "1430 Messerschmidt Hill", "49 Mayer Street", "6073 Longview Park",
            "54 Spohn Road", "209 Butterfield Parkway", "9151 Mccormick Hill", "03441 Continental Avenue",
            "45527 Bluejay Crossing", "6 Atwood Terrace", "628 Quincy Junction", "76331 Macpherson Avenue",
            "57260 Florence Plaza", "496 Bowman Junction", "344 American Ash Court", "51375 Stephen Court",
            "090 Petterle Alley", "9 Laurel Avenue", "3 Graedel Park", "930 Esch Circle",
            "5616 Fisk Park", "4544 Surrey Drive", "0999 Northwestern Park", "581 Muir Road",
            "4546 Beilfuss Drive", "0 New Castle Center", "655 Dorton Alley", "96213 Arkansas Crossing",
            "3060 Fuller Plaza", "4 Gerald Trail", "6550 Heath Plaza", "9 Mifflin Crossing",
            "32751 Michigan Pass", "58441 Mallard Junction", "69 Old Gate Street", "572 Lotheville Parkway",
            "3 3rd Junction", "81465 Helena Terrace", "785 Sutteridge Center", "0377 Mallard Drive",
            "8708 Elgar Circle", "09815 Sunbrook Parkway", "1799 Sheridan Alley", "1 Twin Pines Road",
            "9446 Grim Plaza", "61 Elmside Parkway", "72 Kensington Hill", "6843 Stoughton Terrace",
            "9 Blackbird Alley", "8383 Anzinger Park", "8073 Sutherland Plaza", "291 Moland Trail",
            "98 American Plaza", "375 Forest Run Pass", "923 Grasskamp Road", "804 Harbort Terrace",
            "278 Hollow Ridge Alley", "98 Thompson Plaza", "1 Parkside Junction", "9826 Maywood Junction",
            "166 Ruskin Park", "1072 Pennsylvania Avenue", "05591 Warrior Way", "88 Texas Trail",
            "44843 Toban Hill", "1154 Talisman Hill", "34583 Aberg Place", "39 Crowley Place",
            "21 Russell Terrace", "5 Cherokee Plaza", "9591 Spohn Drive", "41312 5th Avenue",
            "1360 Golf View Parkway", "07 Sage Circle", "96 Pine View Way", "69 Independence Way",
            "4 Crownhardt Avenue", "5220 Monterey Place", "94 Westend Point", "67 Warrior Crossing",
            "9260 Lunder Way", "51174 Grim Parkway", "1671 Almo Pass", "90 Morningstar Place",
            "4971 Loeprich Hill", "14 Acker Street", "7 Doe Crossing Junction", "5140 Utah Street",
            "3 Toban Center", "736 Corscot Trail", "733 Talmadge Park", "0538 Rigney Pass",
            "209 Warbler Parkway", "2 Sage Plaza", "3884 Ramsey Junction", "2147 Morning Trail",
            "3 Starling Place", "9803 Sunfield Trail", "29 Barnett Pass", "39 Glendale Terrace",
            "8 Fremont Trail", "702 Green Court", "89455 Forest Center", "622 Oneill Parkway",
            "582 8th Park", "8 Mesta Place", "11101 Bay Avenue", "8 Melody Crossing",
            "6679 Norway Maple Avenue", "4 Dawn Junction", "2604 Clove Hill", "18 Fuller Place",
            "895 Bobwhite Terrace", "1322 Sutteridge Place", "59331 Morrow Hill", "81 Lake View Parkway",
            "6627 Eagan Parkway", "06105 Ridgeview Hill", "89210 Heath Road", "6 Lien Circle",
            "3 Waxwing Center", "59194 Kings Avenue", "864 Vera Street", "32 Debs Center",
            "40517 Hintze Street", "9 Rieder Place", "0 Arrowood Court", "40 Charing Cross Plaza",
            "0 North Hill", "0 Tennyson Crossing", "80646 Linden Avenue", "663 Northwestern Parkway",
            "27 Warrior Crossing", "11374 Larry Center", "9656 Stephen Drive", "1 Lawn Junction",
            "67664 Summer Ridge Point", "490 Dorton Plaza", "88735 Rigney Place", "507 Westend Point",
            "412 Redwing Center", "1 Novick Road", "792 Kipling Point", "18 Iowa Parkway",
            "214 Jenifer Alley", "7907 High Crossing Place", "483 Arrowood Trail", "58244 Dapin Road",
            "40 Blue Bill Park Road", "42 Portage Center", "27 Killdeer Lane", "877 Buena Vista Road",
            "5269 Logan Center", "6 Mccormick Street", "40 Algoma Place", "3 Acker Trail",
            "44 Waywood Point", "14199 Muir Hill", "63296 New Castle Crossing", "77 Almo Court",
            "8241 International Center", "72 Hazelcrest Place", "9316 Vidon Terrace", "56 Stuart Center",
            "2 Arizona Street", "08 Packers Hill", "2 Ridgeway Center", "089 Redwing Drive",
            "82 Orin Park", "10650 Starling Center", "226 Arapahoe Plaza", "3 Manley Pass",
            "5778 Meadow Valley Junction", "08065 American Ash Terrace", "8 Cordelia Place", "93 Jenifer Place",
            "85 Buena Vista Place", "3627 Linden Circle", "83752 Everett Street", "2899 Scott Way",
            "7617 Bluestem Junction", "40 Trailsway Parkway", "6956 Declaration Center", "36056 Stephen Center",
            "164 Dorton Point", "32608 Hoffman Trail", "5 Fairview Terrace", "1 Carberry Terrace",
            "345 Magdeline Point", "8 Schiller Crossing", "0661 Division Crossing", "80 Eggendart Road",
            "8 Oak Plaza", "22504 Westend Crossing", "1 Dwight Place", "9515 Gateway Hill",
            "95095 Leroy Junction", "99750 Badeau Circle", "234 Mifflin Road", "65211 Kensington Way",
            "89947 Tony Pass", "261 Sullivan Center", "1 Logan Trail", "815 8th Hill",
            "49 Myrtle Lane", "1 Oriole Avenue", "94365 Crest Line Plaza", "71 Sundown Junction",
            "9400 Canary Point", "9904 Gerald Place", "5 Mockingbird Place", "04 Arkansas Hill",
            "8479 Bluestem Park", "7 Scott Parkway", "9 Pawling Road", "92251 Petterle Hill",
            "490 Marquette Junction", "569 5th Point", "799 Oak Valley Hill", "6607 Veith Court",
            "49718 Grayhawk Drive", "4597 Melody Circle", "63749 Killdeer Avenue", "56 Florence Pass",
            "37 Dryden Plaza", "95508 Fuller Junction", "930 Mayfield Point", "68 Corscot Point",
            "69136 Dorton Circle", "05 Steensland Way", "991 Parkside Trail", "41981 Westerfield Place",
            "298 Knutson Crossing", "68089 Talisman Alley", "358 Duke Hill", "423 3rd Drive",
            "450 Sommers Lane", "852 Hermina Avenue", "225 Mockingbird Hill", "9013 Independence Hill",
            "2 Farmco Junction", "9 Onsgard Hill", "1 Lindbergh Trail", "872 Dakota Hill",
            "12 Almo Way", "6033 Gateway Crossing", "4 2nd Pass", "0040 Lien Junction",
            "60547 Burrows Crossing", "14182 Lakewood Gardens Trail", "28020 Mockingbird Trail", "158 Carey Street",
            "244 Summit Pass", "5858 Homewood Plaza", "6 Rieder Road", "315 Homewood Avenue",
            "1906 Weeping Birch Alley", "388 Lakewood Gardens Terrace", "77 Hintze Plaza", "92 Fisk Way",
            "4947 Emmet Lane", "495 Donald Hill", "9 Bashford Plaza", "6 4th Drive",
            "244 Ludington Lane", "180 Sunfield Trail", "00 Katie Pass", "10036 Barby Court",
            "8911 Bobwhite Alley", "06 Mayer Center", "77 Duke Terrace", "24 Anhalt Park",
            "1 Park Meadow Junction", "912 Pierstorff Plaza", "962 Cottonwood Plaza", "7896 Tony Parkway",
            "403 Mosinee Circle", "22 Caliangt Center", "91 Summit Circle", "2 Lotheville Center",
            "256 Grasskamp Lane", "390 Clemons Street", "7121 Fremont Center", "29 Prairieview Trail",
            "9390 Acker Trail", "51839 Mccormick Court", "5 Stang Pass", "0 Buena Vista Drive",
            "8 Eastlawn Center", "951 Fordem Trail", "1 Cordelia Parkway", "682 Farmco Crossing",
            "1053 Stephen Alley", "44253 Bartelt Terrace", "3584 Comanche Circle", "09 John Wall Center",
            "98 Hauk Plaza", "1514 Kensington Point", "1495 Monterey Hill", "2057 Farragut Park",
            "52 Main Drive", "61911 Hoepker Street", "395 Sherman Hill", "12 Amoth Point",
            "95201 Charing Cross Alley", "49729 Reinke Terrace", "51639 Red Cloud Way", "9 2nd Road",
            "713 Claremont Drive", "286 Paget Alley", "5487 Iowa Avenue", "477 Longview Circle",
            "1421 Weeping Birch Circle", "3 Dorton Center", "7506 Summit Trail", "37839 Colorado Street",
            "31 Starling Drive", "66 Barnett Trail", "02745 Hooker Trail", "0922 Aberg Place",
            "383 Weeping Birch Lane", "5423 Farmco Plaza", "3 Milwaukee Court", "81 Messerschmidt Plaza",
            "68 Logan Terrace", "909 Rusk Terrace", "0 Sunbrook Plaza", "82242 Shoshone Alley",
            "697 Everett Plaza", "6012 Sunbrook Street", "09 Arizona Junction", "58404 Di Loreto Street",
            "88410 Dahle Park", "9 Main Terrace", "39527 Glendale Court", "8 Stuart Point",
            "805 Eagle Crest Road", "635 Meadow Ridge Way", "338 Anniversary Center", "4 Prairieview Circle",
            "890 Randy Terrace", "10 Norway Maple Junction", "7017 Maple Wood Way", "10 Iowa Road",
            "92658 Rutledge Alley", "7547 Marquette Alley", "6439 New Castle Parkway", "18 4th Street",
            "13438 Meadow Vale Circle", "39 Brown Street", "24 Lakeland Hill", "358 Union Alley",
            "716 Sachtjen Avenue", "5621 Carberry Alley", "01 Jackson Place", "51057 3rd Court",
            "82744 Springs Hill", "16 Nevada Center", "9295 Oak Road", "3 Westridge Avenue",
            "352 Bashford Way", "323 Bowman Way", "06 Hazelcrest Avenue", "225 Parkside Way",
            "29432 Fairview Trail", "5 Michigan Lane", "6546 Sunfield Center", "1385 Ridge Oak Road",
            "213 Meadow Valley Point", "07 Parkside Plaza", "9 Veith Court", "51 Stuart Junction",
            "0160 Westend Pass", "8652 Tennyson Plaza", "27142 Sage Junction", "10893 Corben Plaza",
            "83589 Carberry Avenue", "5911 Fremont Street", "80579 Arapahoe Hill", "2599 Amoth Pass",
            "52 Huxley Pass", "7702 Dovetail Point", "05442 Bultman Avenue", "4 Kings Way",
            "3 Kipling Road", "5923 Almo Crossing", "07225 International Alley", "3 Texas Lane",
            "4919 Leroy Terrace", "01 Green Ridge Point", "7456 Brickson Park Trail", "6 Eagle Crest Alley",
            "927 Judy Lane", "54648 Marquette Street", "0 Moulton Crossing", "44306 Debs Road",
            "7931 Eastlawn Parkway", "84351 Killdeer Circle", "18 Crownhardt Point", "76627 Fremont Center",
            "3 Jay Avenue", "259 Heffernan Circle", "772 Pleasure Trail", "85272 Hollow Ridge Park",
            "1656 Charing Cross Parkway", "727 Anderson Street", "87135 Waubesa Center", "60034 Hallows Place",
            "3395 Union Crossing", "9 Elgar Center", "76 Oxford Center", "0878 Mcbride Pass",
            "19471 Trailsway Plaza", "67 Linden Alley", "84181 Moulton Road", "06444 Eagle Crest Plaza",
            "41 Artisan Hill", "8529 Utah Parkway", "8340 Macpherson Lane", "04366 Vera Alley",
            "76 Fuller Junction", "844 Tennessee Avenue", "74505 Bunker Hill Crossing", "61 Arizona Hill",
            "3 Corben Alley", "6 Erie Junction", "3 Heath Alley", "8199 Hayes Place", "69 Nevada Pass", "375 Lindbergh Drive",
            "5 Pankratz Avenue", "365 Pawling Point", "0586 Fremont Way", "522 Pleasure Pass",
            "0 East Parkway", "2237 Northland Pass", "8971 Hollow Ridge Hill", "8 Meadow Valley Way",
            "4 Amoth Parkway", "29 Anniversary Parkway", "7248 Hintze Junction", "23 Anthes Terrace",
            "9 Valley Edge Park", "605 Brickson Park Trail", "7 Crescent Oaks Alley", "54 Northview Way",
            "709 Elka Lane", "948 Artisan Point", "503 Sherman Lane", "1 Hansons Plaza",
            "891 David Avenue", "992 Gina Place", "33 Bluejay Trail", "2 Warrior Trail",
            "00945 Everett Alley", "667 Messerschmidt Junction", "021 Twin Pines Terrace", "7 Waxwing Terrace",
            "58 Eliot Terrace", "04 Armistice Circle", "9529 Corry Lane", "6 Atwood Hill",
            "35 Veith Place", "6609 Gina Avenue", "59 8th Way", "13 Lakeland Court",
            "8593 Stuart Lane", "4 Kropf Alley", "205 Stephen Drive", "22720 Acker Road",
            "12386 Cascade Center", "24543 Talisman Avenue", "51586 Caliangt Parkway", "1 Anzinger Drive",
            "07 Prentice Alley", "10 Tomscot Park", "8247 Monterey Pass", "16941 Corscot Terrace",
            "6 Lakeland Circle", "2 Dapin Center", "09 Eggendart Drive", "9998 Chinook Lane",
            "10 Lien Park", "9 Sauthoff Way", "7460 Bobwhite Pass", "44 Huxley Pass",
            "8 Scofield Crossing", "6610 Moose Avenue", "44702 Marcy Junction", "39 Stone Corner Drive",
            "300 Sullivan Trail", "6130 Brickson Park Terrace", "769 Randy Junction", "7986 Corry Court",
            "49863 Petterle Court", "348 Ramsey Place", "79961 Fair Oaks Way", "82002 Pleasure Park",
            "122 Forest Run Avenue", "25585 Dayton Street", "17 Fordem Park", "73 Anniversary Pass",
            "3560 Chinook Court", "445 Bay Parkway", "105 Birchwood Crossing", "68439 Hallows Court",
            "1 Jay Plaza", "755 Mariners Cove Road", "058 Sommers Trail", "55219 Sutherland Avenue",
            "30209 Holy Cross Lane", "57054 Crownhardt Pass", "83783 Gulseth Parkway", "714 Myrtle Park",
            "685 Shelley Street", "9932 Carberry Lane", "004 Eastwood Way", "6 Muir Street",
            "52888 Buena Vista Place", "25451 Bonner Court", "25 Vernon Way", "9677 Sommers Terrace",
            "4874 Veith Lane", "99198 Mitchell Parkway", "4 Dapin Place", "184 Anderson Terrace",
            "65579 Marquette Avenue", "364 Banding Street", "01 Cherokee Way", "8085 Commercial Street",
            "92 Kingsford Parkway", "74199 Memorial Place", "008 Dahle Center", "0541 Victoria Road",
            "2763 Hovde Point", "14920 Boyd Street", "14997 Cordelia Crossing"
    };

    @DisplayName("elasticsearch bulkElasticInsert test")
    @Test
    public void bulkElasticInsert() {
        // Given
        List<IndexQuery> indexQueries = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 23, 53, 23);
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC); // 현재 시각을 UTC로 가져옴
        System.out.println(LocalDateTime.now());
        String str = LocalDateTime.now().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        // When
        System.out.println(LocalDateTime.now().toString());
        for(int i=2; i<10; i++) {
            SeminarDocument seminarDocument = SeminarDocument.builder()
                    .seminar_no((long) i)
                    .seminar_name(seminar_nameArr[random.nextInt(seminar_nameArr.length + 1) % seminar_nameArr.length] + " "+ i)
                    .seminar_explanation(seminar_explanations[random.nextInt(seminar_explanations.length + 1) % seminar_explanations.length] + " "+i)
                    .seminar_max_participants(random.nextLong(100) + 1)
                    .inst_dt(LocalDateTime.now())
                    .updt_dt(LocalDateTime.now())
                    .build();

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(seminarDocument.getSeminar_no().toString())
                    .withObject(seminarDocument)
                    .build();
            indexQueries.add(indexQuery);
        }

        // Then
        IndexCoordinates indexCoordinates = IndexCoordinates.of("seminar"); // 여기에 적절한 인덱스 이름을 넣어주세요
        System.out.println(elasticsearchOperations.bulkIndex(indexQueries, indexCoordinates));
    }

    @DisplayName("elasticsearch search test")
    @Test
    public void testSeminarSearch() {
        // Given
        Long startTime = System.currentTimeMillis();
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("eastwood")
                .type("seminar_explanation seminar_name")
                .page(0)
                .size(10)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        // When
        SearchHits<SeminarDocument> searchHits = seminarElasticSearchService.searchByKeywordAndType(pageRequestDTO, pageable);
        Long endTime = System.currentTimeMillis();

        // Then
        System.out.println("Execution Time:" + (endTime - startTime) + "ms");
        System.out.println(searchHits.getTotalHits());
        System.out.println(searchHits.getMaxScore());
        for (SearchHit<SeminarDocument> searchHit : searchHits) {
            System.out.println(searchHit.getContent().toString());
        }
    }

}
