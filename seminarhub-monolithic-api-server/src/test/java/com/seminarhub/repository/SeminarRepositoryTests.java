package com.seminarhub.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.dto.SeminarPageResultDTO;
import com.seminarhub.dto.Seminar_Member_Seminar_PaymentResponseDTO;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Seminar;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertNotNull;


/**
 * [ 2023-08-08 daeho.kang ]
 * Description : Test For QueryDSL Or JPQL Custom Method
 *
 */
@SpringBootTest
public class SeminarRepositoryTests {
    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private SeminarQuerydslRepository seminarQuerydslRepository;
    private final String seminar_name= "SeminarTest";

    private final String seminar_remove_name= "SeminarRemoveTest";

    private final String[] seminar_nameArr = {
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

    private final String[] seminar_explanations = {
            "Explore the latest trends and advancements in artificial intelligence and machine learning technologies. Learn from industry experts and gain practical insights into AI implementation.",
            "Dive deep into the world of sustainable energy solutions. Discover innovative approaches to renewable energy, conservation, and environmental impact mitigation.",
            "Unleash your creativity in this hands-on workshop. Develop your artistic skills and explore various mediums, from painting and sculpture to digital art and multimedia installations.",
            "Delve into the realm of astrophysics and cosmology. Understand the mysteries of the universe, dark matter, black holes, and the origins of galaxies. Ideal for astronomy enthusiasts and students.",
            "Enhance your leadership skills and learn effective strategies for team management. Explore the principles of motivational leadership, conflict resolution, and fostering a positive work environment.",
            "Embark on a culinary journey around the world. Master the art of cooking with international flavors and spices. From Italian pasta to Japanese sushi, broaden your culinary expertise.",
            "Uncover the secrets of ancient civilizations. Explore archaeological discoveries, decipher ancient scripts, and learn about the rise and fall of empires such as Egypt, Rome, and Mesopotamia.",
            "Dive into the world of virtual reality and augmented reality. Experience immersive simulations, learn about VR/AR development tools, and explore the future applications of these technologies.",
            "Master the art of storytelling across various mediums. From traditional literature and cinema to interactive storytelling in video games, explore the techniques that captivate audiences.",
            "Examine the impact of climate change on our planet. Understand the science behind global warming, rising sea levels, and extreme weather events. Explore solutions for a sustainable future.",
            "Discover the intricacies of quantum physics and its applications in modern technology. Delve into quantum computing, quantum entanglement, and the potential of quantum communication.",
            "Gain insights into the world of cybersecurity and digital forensics. Learn about threat detection, encryption methods, and ethical hacking techniques to secure digital assets.",
            "Explore the evolution of art movements from Renaissance to Contemporary. Analyze famous artworks, understand artistic techniques, and delve into the cultural contexts that shaped art history.",
            "Deepen your understanding of sustainable agriculture and eco-friendly farming practices. Explore organic farming, permaculture, and innovative techniques for ensuring food security.",
            "Dive into the world of space exploration and interplanetary travel. Learn about upcoming missions to Mars, the search for extraterrestrial life, and the future of human colonization in space.",
            "Examine the psychological aspects of decision-making and behavior. Understand cognitive biases, human motivation, and the impact of social influence on individual choices.",
            "Uncover the history of revolutions and social movements that shaped nations. From the French Revolution to the Civil Rights Movement, explore pivotal moments in human history.",
            "Explore the fusion of technology and art in interactive installations. Learn about digital sculptures, interactive visual displays, and immersive experiences in the realm of digital art.",
            "Deep dive into the world of marine biology and oceanography. Study marine ecosystems, marine species, and the impact of climate change on ocean health.",
            "Master the art of entrepreneurship and startup success. Learn about ideation, market analysis, funding strategies, and scaling your business in a competitive market.",
            "Explore the history of ancient civilizations, from Mesopotamia to the Indus Valley. Understand their social structures, architectural marvels, and cultural contributions to humanity.",
            "Delve into the world of artificial intelligence and machine learning. Learn about neural networks, natural language processing, and the ethical implications of AI in society.",
            "Discover the wonders of the human brain and the complexities of neuroscience. Study brain anatomy, neural pathways, and the latest research on brain-related disorders.",
            "Master the art of effective leadership and team management. Learn about leadership styles, conflict resolution, and motivational techniques for leading high-performing teams.",
            "Uncover the secrets of the universe with astrophysics. Explore black holes, dark matter, and the origins of the cosmos, unraveling the mysteries of the vast, celestial expanse.",
            "Deepen your understanding of classical literature and its enduring impact on culture. Analyze works of Shakespeare, Dickens, and Austen, exploring themes of love, power, and society.",
            "Embark on a journey through the world of finance and investment. Learn about stock markets, portfolio management, and investment strategies for long-term financial growth.",
            "Examine the role of women in history and their contributions to society. Explore women's suffrage movements, feminist literature, and the fight for gender equality.",
            "Dive into the realms of fantasy literature and mythology. Explore magical worlds, legendary creatures, and the hero's journey in epic tales from various cultures.",
            "Master the art of storytelling and creative writing. Develop compelling characters, intricate plots, and immersive settings in your journey to becoming a skilled wordsmith."
    };
    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : Setting the Data for Test
     */
//    @BeforeEach
//    public void setup() throws Exception{
//        if(seminarRepository.findBySeminar_name(seminar_name).isEmpty()){
//            // CREATE Seminar
//            Seminar seminar = Seminar.builder()
//                    .seminar_name(seminar_name)
//                    .build();
//
//            seminarRepository.save(seminar);
//        }
//        if(seminarRepository.findBySeminar_name(seminar_remove_name).isEmpty()){
//            // CREATE Seminar
//            Seminar seminar = Seminar.builder()
//                    .seminar_name(seminar_remove_name)
//                    .build();
//
//            seminarRepository.save(seminar);
//        }
//    }

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : seminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         s1_0.seminar_no,
     *         s1_0.del_dt,
     *         s1_0.inst_dt,
     *         s1_0.seminar_explanation,
     *         s1_0.seminar_name,
     *         s1_0.updt_dt
     *     from
     *         seminar s1_0
     *     where
     *         s1_0.seminar_name=?
     *         and s1_0.del_dt is null
     *      Seminar(seminar_no=1, seminar_name=SeminarTest, seminar_explanation=null, del_dt=null)
     */
//    @DisplayName("findBySeminar_name Test")
//    @Test
//    public void testGetWithSeminar_name(){
//        // given, when
//        Optional<Seminar> seminar = seminarRepository.findBySeminar_name(seminar_name);
//
//        // then
//        assertNotNull(seminar.get());
//        System.out.println(seminar.get());
//    }

    @DisplayName("DUmmyDataInsert")
    @Test
    public void dummyInsert(){

        for(int i=5; i<7; i++){
            Seminar seminar  = Seminar.builder()
                    .seminar_name("SeminarDummyIndex"+i)
                    .seminar_explanation("SeminarDummyExplanationIndex"+i)
                    .seminar_price((long) 2000)
                    .build();
            seminarRepository.save(seminar);
        }
    }
    @DisplayName("jpaQueryFactoryfindBySeminar_name Test")
    @Test
    public void testGetWithSeminar_name123(){
        // given, when
        List<Seminar> seminar = seminarQuerydslRepository.findByName("SeminarDummyIndex5");

        // then
        assertNotNull(seminar.size());
        System.out.println(seminar.get(0));
    }
    
    
    @DisplayName("FindSeminarByBooleanBuilder Test")
    @Test
    public void testFindSeminarByBooleanBuilder(){
        // given // when
        List<Seminar> seminarList = seminarQuerydslRepository.findSeminarByBooleanBuilder("SeminarDummyIndex23", "");

        // then
        assertNotNull( seminarList.size());
//        System.out.println(seminar.);
        seminarList.stream().forEach(seminar -> System.out.println(seminar.toString()));
    }

    @DisplayName("FindSeminarByBooleanExpression Test")
    @Test
    public void testFindSeminarByBooleanExpression(){
        // given // when
        List<Seminar> seminarList = seminarQuerydslRepository.findSeminarByBooleanExpression("SeminarDummyIndex23", "");

        // then
        assertNotNull( seminarList.size());
//        System.out.println(seminar.);
        seminarList.stream().forEach(seminar -> System.out.println(seminar.toString()));
    }

    @DisplayName("FindSeminarWithExists")
    @Test
    public void testwithExists(){
        boolean flag = seminarQuerydslRepository.exist("SeminarDummyIndex25");

        System.out.println(flag);
    }

    @DisplayName("testCrossJoin")
    @Test
    @Transactional // To Maintain the Proxy Object Until the End of the Test
    public void testCrossJoin(){
        List<Member_Seminar> memberSeminarList = seminarQuerydslRepository.crossJoin();
        memberSeminarList.stream().forEach(memberSeminar -> System.out.println(memberSeminar.getMember()+" "+memberSeminar.getSeminar()));
    }

    @DisplayName("dummyInsertWithJdbcTemplate")
    @Test
    public void dummyInsertWithJdbcTemplate(){
        List<SeminarDTO> seminarDTOList = new LinkedList<>();
        Random random = new Random();
        for(int i=11; i<100000; i++) {
            Timestamp timestamp = generateRandomTimestamp();
            SeminarDTO seminarDTO = SeminarDTO.builder()
                    .seminar_name(seminar_nameArr[random.nextInt(seminar_nameArr.length + 1) % seminar_nameArr.length] + " "+ i)
                    .seminar_explanation(seminar_explanations[random.nextInt(seminar_explanations.length + 1) % seminar_explanations.length] + " "+i)
                    .seminar_price(random.nextLong(999999) + 1)
                    .seminar_max_participants(random.nextLong(100) + 1)
                    .inst_dt(timestamp)
                    .updt_dt(timestamp)
                    .build();
            seminarDTOList.add(seminarDTO);
        }
        Long startTime = System.currentTimeMillis();
        seminarQuerydslRepository.jdbcBulkInsert(seminarDTOList);
        Long endTime = System.currentTimeMillis();
        System.out.println("Execution Time:"+ (endTime - startTime) + "ms");

    }
    private Timestamp generateRandomTimestamp() {
        long minDate = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long maxDate = Timestamp.valueOf("2023-12-31 23:59:59").getTime();

        long randomTime = ThreadLocalRandom.current().nextLong(minDate, maxDate);

        return new Timestamp(randomTime);
    }

    @Autowired
    private JPAQueryFactory queryFactory;

    @DisplayName("Querydsl PagingExample")
    @Test
    public void PagingExample(){
        int pageNo = 190000 - 1;
        String keyword="Seminar";
        int pageSize = 10;
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithKeyword(keyword,pageNo, pageSize);
        for(int i=0;i<seminarPageResultDTOList.size();i++){
            System.out.println("cnt:"+i+" "+seminarPageResultDTOList.get(i).toString());
        }
    }
    @Test
    public void testBulkUpdatePrice(){
        seminarQuerydslRepository.bulkUpdatePrice();
    }

    @Test
    public void testBulkUpdateSeminar_nameSeminar_explanation(){
        System.out.println(seminar_nameArr.length);
        seminarQuerydslRepository.bulkUpdateSeminar_nameSeminar_explanation(seminar_nameArr, seminar_explanations);
    }


    @DisplayName("Querydsl PagingExampleWithCoveringIndex")
    @Test
    public void PagingExampleWithCoveringIndex() {
        // Given
        int pageNo = 190000 - 1;
        String keyword = "Seminar";
        int pageSize = 10;

        // When
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithKeywordWithCoveringIndex(keyword, pageNo, pageSize);

        // Then
        for (int i = 0; i < seminarPageResultDTOList.size(); i++) {
            System.out.println("cnt:" + i + " " + seminarPageResultDTOList.get(i).toString());
            
        }
    }

    @DisplayName("testPagingSeminarWithSeminar_Price test")
    @Test
    public void testPagingSeminarWithSeminar_Price() {
        // Given
        int pageNo = 30000;
        long seminar_price = 10000;
        int pageSize = 10;

        // When
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithSeminar_Price(seminar_price, pageNo, pageSize);

        // Then
        for (int i = 0; i < seminarPageResultDTOList.size(); i++) {
            System.out.println("cnt:" + i + " " + seminarPageResultDTOList.get(i).toString());
            
        }
    }

    @DisplayName("testPagingSeminarWithCoveringIndexWithSeminar_Price")
    @Test
    public void testPagingSeminarWithCoveringIndexWithSeminar_Price() {
        // Given
        int pageNo = 30000;
        long seminar_price = 10000;
        int pageSize = 10;

        // When
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithCoveringIndexWithSeminar_Price(seminar_price, pageNo, pageSize);

        // Then
        for (int i = 0; i < seminarPageResultDTOList.size(); i++) {
            System.out.println("cnt:" + i + " " + seminarPageResultDTOList.get(i).toString());
            
        }
    }

    @DisplayName("testParticipateOnSeminar")
    @Test
    public void testParticipateOnSeminar() {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        // When
        seminarQuerydslRepository.participateOnSeminar(member_no, seminar_no);

        // Then
        
    }

    @DisplayName("testParticipateOnSeminarWithoutLock")
    @Test
    public void testParticipateOnSeminarWithoutLock() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        final int executeNumber = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(40);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        // When
        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    seminarQuerydslRepository.participateOnSeminar(member_no, seminar_no);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        
    }

    @DisplayName("testParticipateOnSeminarWithPessimisticLock")
    @Test
    public void testParticipateOnSeminarWithMultiThread() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        final int executeNumber = 200;
        final ExecutorService executorService = Executors.newFixedThreadPool(40);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        // When
        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    seminarQuerydslRepository.participateOnSeminarWithPESSIMISTICLock(member_no, seminar_no);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        
    }

    @DisplayName("testParticipateOnSeminarWithPessimisticLock")
    @Test
    public void testparticipateOnSeminarWithPESSIMISTICLockAddPayment() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        final int executeNumber = 40;
        final ExecutorService executorService = Executors.newFixedThreadPool(40);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        // When
        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    seminarQuerydslRepository.participateOnSeminarWithPESSIMISTICLockAddPayment(member_no, seminar_no);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        
    }

    @Transactional // Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListForMember_SeminarAndPayment() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        // When
        List<Seminar> seminar = seminarQuerydslRepository.getSeminar(seminar_no);
        System.out.println(seminar.get(0).toString());

        // Then
        for (int i = 0; i < seminar.get(0).getMember_seminar_list().size(); i++) {
            System.out.println("----------------------------------------");
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).toString());
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).getPayment().toString());
        }
        
    }

    @Transactional // Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPaymentFetchJoin")
    @Test
    public void testgetListForMember_SeminarAndPaymentWithFetchJoin() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;

        // When
        List<Seminar> seminar = seminarQuerydslRepository.getSeminarFetchJoin(seminar_no);
        System.out.println(seminar.get(0).toString());

        // Then
        for (int i = 0; i < seminar.get(0).getMember_seminar_list().size(); i++) {
            System.out.println("----------------------------------------");
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).toString());
            System.out.println(seminar.get(0).getMember_seminar_list().get(i).getPayment().toString());
        }
        
    }

    @Transactional // Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListSeminar() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 10;

        // When
        List<Seminar> seminar = seminarQuerydslRepository.getListSeminar(pageNo, pageSize);
        System.out.println("CNT:" + seminar.size());

        // Then
        for (int i = 0; i < seminar.size(); i++) {
            System.out.println("---------------------------------------");
            System.out.println("Seminar:" + seminar.get(i).toString());
            for (int j = 0; j < seminar.get(i).getMember_seminar_list().size(); j++) {
                System.out.println(seminar.get(i).getMember_seminar_list().get(j).toString());
                System.out.println(seminar.get(i).getMember_seminar_list().get(j).getPayment().toString());
            }
        }
        
    }

    @Transactional // Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListSeminarWithFetchJoin() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 50;

        // When
        List<Seminar> seminar = seminarQuerydslRepository.getListSeminarWithFetchJoin(seminar_no, pageNo, pageSize);
        System.out.println("CNT:" + seminar.size() + " ");

        // Then
        for (int i = 0; i < seminar.size(); i++) {
            System.out.println(seminar.get(i).toString());
            System.out.println(seminar.get(i).getMember_seminar_list().toString());
        }
        
    }

    @Transactional // Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment")
    @Test
    public void testgetListSeminarWithBatch() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 50;

        // When
        List<Seminar> seminar = seminarQuerydslRepository.getListSeminarWithBatch(seminar_no, pageNo, pageSize);
        System.out.println("CNT:" + seminar.size());

        // Then
        for (int i = 0; i < seminar.size(); i++) {
            System.out.println("---------------------------------------");
            for (int j = 0; j < seminar.get(i).getMember_seminar_list().size(); j++) {
                // Member_Seminar member_seminar = seminar.get(i).getMember_seminar_list().get(j);
                // System.out.println(member_seminar.toString());
                // System.out.println(member_seminar.getPayment().toString());
            }
        }
        
    }

    @Transactional // Proxy 유지
    @DisplayName("test getListForMember_SeminarAndPayment With DTO")
    @Test
    public void testgetListSeminarWithFetchJoinWIthDTO() throws InterruptedException {
        // Given
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 10;

        // When
        List<Seminar_Member_Seminar_PaymentResponseDTO> seminar = seminarQuerydslRepository.getListForMember_SeminarAndPayment(seminar_no);
        System.out.println("CNT:" + seminar.size());
        for (int i = 0; i < seminar.size(); i++) {
            System.out.println("---------------------------------------");
            System.out.println("Seminar:" + seminar.get(i).toString());
            seminar.get(i).getMember_seminar_no();
            seminar.get(i).getMember_no();
            seminar.get(i).getSeminar_explanation();
            seminar.get(i).getSeminar_price();
            seminar.get(i).getSeminar_explanation();
            seminar.get(i).getMember_seminar_no();
            seminar.get(i).getMember_no();
            seminar.get(i).getPayment_no();
            seminar.get(i).getAmount();
        }
        
    }

    @DisplayName("Querydsl PagingExample")
    @Test
    public void PagingSeminarWithWholewordExample() {
        // Given
        int pageNo = 20;
        String keyword = "Pepper";
        int pageSize = 10;

        // When
        Long startTime = System.currentTimeMillis();
        List<SeminarPageResultDTO> seminarPageResultDTOList = seminarQuerydslRepository.pagingSeminarWithWholeword(keyword, pageNo, pageSize);
        Long endTime = System.currentTimeMillis();
        System.out.println("Execution Time:" + (endTime - startTime) + "ms");

        // Then
        for (int i = 0; i < seminarPageResultDTOList.size(); i++) {
            System.out.println("cnt:" + i + " " + seminarPageResultDTOList.get(i).toString());
        }
        
    }

}