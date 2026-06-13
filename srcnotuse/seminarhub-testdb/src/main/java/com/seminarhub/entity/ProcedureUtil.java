package com.seminarhub.entity;

import lombok.NoArgsConstructor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import static com.seminarhub.entity.functionUtil.*;

@NoArgsConstructor
public class ProcedureUtil {

    public void sp_insert_member() {
        String filePath = "members.csv"; // 저장할 파일 경로
        int startNo = 1; // 시작 번호
        int endNo = 10000000; // 끝 번호
        int chunkSize = 500000; // 한 번에 생성할 회원 수

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = startNo; i <= endNo; i += chunkSize) {
                for (int j = 0; j < chunkSize && (i + j) <= endNo; j++) {
                    long memberNo = (long) (i + j);
                    String id = f_randid(40); // 랜덤 아이디 생성
                    String name = f_randname(); // 랜덤 이름 생성
                    String password = f_randpw(); // 랜덤 패스워드 생성
                    char gender = f_randgender(0.65, 0.35); // 랜덤 성별 생성
                    int age = f_randminmax(15, 100); // 랜덤 나이 생성
                    String address = f_randaddress(); // 랜덤 주소 생성
                    String job = f_randjob(); // 랜덤 직업 생성
                    String grade = f_randgrade(); // 랜덤 등급 생성
                    int points = f_randminmax(0, 30000); // 랜덤 적립금 생성

                    // instDt는 항상 updtDt보다 앞서도록 설정
                    LocalDateTime instDt = f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now());
                    LocalDateTime updtDt = f_randdatetime(instDt.plusSeconds(1), LocalDateTime.now()); // instDt 이후의 값으로 설정

                    // CSV 형식으로 작성, 필드를 "로 감쌈
                    writer.write(String.format("%d,\"%s\",\"%s\",\"%s\",\"%c\",%d,\"%s\",\"%s\",\"%s\",\"%d\",\"%s\",\"%s\",NULL\n",
                            memberNo, id, name, password, gender, age, address, job, grade, points, instDt, updtDt));
                }
                System.out.printf("Processed %d ~ %d\n", i, i + chunkSize - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("회원 정보 파일 생성 완료");
    }

    public void sp_insert_company(int cnt) {
        String filePath = "company.csv"; // 저장할 파일 경로

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 1; i <= cnt; i++) {
                long company_no = (long) (i);
                String name = f_randcompanyname();  // 회사명
                String phone = f_randphone();       // 전화번호
                String contact = f_randname();      // 담당자명
                String address = f_randaddress();   // 주소

                // instDt는 항상 updtDt보다 앞서도록 설정
                LocalDateTime instDt = f_randdatetime(
                        LocalDateTime.of(2000, 1, 1, 0, 0),
                        LocalDateTime.of(2024, 11, 2, 0, 0)
                );
                LocalDateTime updtDt = f_randdatetime(
                        instDt.plusSeconds(1),  // instDt 이후로 설정
                        LocalDateTime.of(2024, 11, 2, 0, 0)
                );

                // CSV 형식으로 작성, 필드를 "로 감쌈
                writer.write(String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",NULL\n",
                        company_no, name, phone, contact, address, instDt, updtDt));

                if ((i + 1) % 100000 == 0) { // 매 10000건마다 진행상황 출력
                    System.out.printf("Processed %d companies\n", i + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("회사 정보 파일 생성 완료");
    }

    public void sp_insert_seminar(int max_company_no) {
        String filePath = "seminar.csv";
        long seminarNo = 1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 1; i <= max_company_no; i++) {
                int seminarCount = (int) (Math.random() * 101);

                for (int j = 0; j < seminarCount; j++) {
                    // 1. regStartDate와 regEndDate 설정 (regStartDate가 항상 작거나 같은 값)
                    LocalDateTime regStartDate = f_randdatetime(
                            LocalDateTime.of(2000, 1, 1, 0, 0),
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );
                    LocalDateTime regEndDate = f_randdatetime(
                            regStartDate.plusSeconds(1),  // regStartDate 이후부터 가능
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );


                    // 3. startDate 설정 (regEndDate 이후의 값)
                    LocalDateTime startDate = f_randdatetime(
                            regEndDate.plusSeconds(1),  // regEndDate 이후부터 가능
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );

                    // 4. endDate 설정 (startDate 이후의 값)
                    LocalDateTime endDate = f_randdatetime(
                            startDate.plusSeconds(1),  // startDate 이후부터 가능
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );

                    // 2. instDt 설정 (regStartDate 이전의 값)
                    LocalDateTime instDt = f_randdatetime(
                            LocalDateTime.of(2000, 1, 1, 0, 0),
                            regStartDate.minusSeconds(1)  // regStartDate 이전까지 가능
                    );

                    // 5. updtDt 설정 (instDt 이후의 값)
                    LocalDateTime updtDt = f_randdatetime(
                            instDt.plusSeconds(1),  // instDt 이후부터 가능
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );

                    int maxCapacity = f_randminmax(300, 1000);
                    int availableSeats = f_randminmax(0, maxCapacity);  // 최대 수용인원 이하로 설정

                    // CSV 형식으로 작성
                    writer.write(String.format("%d,\"%s\",\"%s\",%d,%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,\"%s\",\"%s\",NULL\n",
                            seminarNo++,                    // seminar_no
                            f_randseminarname(),            // name
                            f_randseminardesciption(),      // description
                            f_randminmax(10000, 50000),     // price
                            availableSeats,                 // available_seats
                            regStartDate,                   // reg_start_date
                            regEndDate,                     // reg_end_date
                            startDate,                      // start_date
                            endDate,                        // end_date
                            f_randaddress(),                // address
                            i,                              // company_no
                            maxCapacity,                    // max_capacity
                            instDt,                         // inst_dt
                            updtDt                          // updt_dt
                    ));
                }

                if (i % 10000000 == 0) {
                    System.out.printf("Processed company %d / %d\n", i, max_company_no);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("세미나 정보 파일 생성 완료");
    }

    public void sp_insert_order(int maxCount, int maxMemberNo, int maxSeminarNo) {
        String filePath = "orders.csv";
        long orderNo = 1;  // PK 추가

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // CSV 헤더 작성
//        writer.write("\"order_no\",\"member_no\",\"seminar_no\",\"quantity\",\"inst_dt\",\"updt_dt\",\"del_dt\"\n");

            // 1부터 최대 회원번호까지 순회
            for (int i = 1; i <= maxMemberNo; i++) {
                int memberNo = i;

                // 각 회원당 0부터 maxCount 사이의 랜덤한 주문 횟수 생성
//                int randomOrderCount = (int) (Math.random() * (maxCount + 1));
                int randomOrderCount = (int) Math.max(0, ThreadLocalRandom.current().nextGaussian() * maxCount * 15 + maxCount);

                // 랜덤하게 정해진 횟수만큼 주문 생성
                for (int j = 1; j <= randomOrderCount; j++) {
                    LocalDateTime instDt = f_randdatetime(
                            LocalDateTime.of(2000, 1, 1, 0, 0),
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );
                    LocalDateTime updtDt = f_randdatetime(
                            instDt.plusSeconds(1), // instDt 이후부터 시작
                            LocalDateTime.of(2024, 11, 2, 0, 0)
                    );

                    // CSV 형식으로 작성, 필드를 "로 감쌈
                    writer.write(String.format("%d,%d,%d,%d,\"%s\",\"%s\",NULL\n",
                            orderNo++,                                   // order_no (PK)
                            memberNo,                                    // member_no
                            (int) (1 + Math.random() * maxSeminarNo),   // seminar_no
                            f_randminmax(1, 10),                         // quantity
                            instDt,                                      // inst_dt
                            updtDt                                       // updt_dt
                    ));
                }

                // 진행상황 출력 (100000건마다)
                if (i % 1000000 == 0) {
                    System.out.printf("Processed member %d / %d\n", i, maxMemberNo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("주문 정보 파일 생성 완료");
    }

    public void sp_insert_board(int cnt, int maxMemberNo) {
        String filePath = "board.csv"; // 저장할 CSV 파일 경로
        long boardNo = 1; // board_no 초기화

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // CSV 헤더 작성
            // writer.write("board_no,title,content,member_no,inst_dt,updt_dt,del_dt\n");

            // 1부터 최대 회원번호까지 순회
            for (int i = 1; i <= maxMemberNo; i++) {
                int memberNo = i;

                // 평균적으로 cnt 개의 게시물을 생성하되, 임의로 더 적거나 많게 설정
                // 정규분포를 사용하여 게시물 수 생성
                int randomBoardCount = (int) Math.max(0, ThreadLocalRandom.current().nextGaussian() * cnt * 7+ cnt);

                // 랜덤하게 정해진 횟수만큼 게시물 생성
                for (int j = 0; j < randomBoardCount; j++) {
                    String title = f_randboardtitle();
                    String content = f_randboardcontent();
                    LocalDateTime instDt = f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.of(2024, 11, 2, 0, 0));
                    LocalDateTime updtDt = f_randdatetime(instDt.plusSeconds(1), LocalDateTime.of(2024, 11, 2, 0, 0));

                    // CSV 형식으로 작성
                    writer.write(String.format("%d,\"%s\",\"%s\",%d,\"%s\",\"%s\",NULL\n",
                            boardNo++,                  // board_no (PK)
                            title,                      // title
                            content,                    // content
                            memberNo,                  // member_no
                            instDt,                    // inst_dt
                            updtDt                     // updt_dt
                    ));
                }

                // 진행상황 출력 (1000건마다)
                if (i % 1000000 == 0) {
                    System.out.printf("Processed member %d / %d\n", i, maxMemberNo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("게시판 데이터 파일 생성 완료");
    }

    public void sp_insert_member_points_history(int pointCnt, int maxMemberNo) {
        String filePath = "member_points_history.csv";
        long historyNo = 1; // history_no 초기화

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 1부터 최대 회원번호까지 순회
            for (int memberNo = 1; memberNo <= maxMemberNo; memberNo++) {
                // 각 회원의 이력 건수를 0~pointCnt 사이의 랜덤값으로 설정
                int recordsToCreate = (int) Math.max(0, ThreadLocalRandom.current().nextGaussian() * pointCnt * 10 + pointCnt);

                // 회원의 초기값 설정
                int currentPoint = 0;
                int version = 1;
                // 각 회원의 시작 시간을 랜덤으로 설정
                LocalDateTime currentDateTime = f_randdatetime(
                        LocalDateTime.of(2000, 1, 1, 0, 0),
                        LocalDateTime.of(2024, 11, 2, 0, 0)
                );

                // 각 회원별 이력 생성
                for (int v = 1; v <= recordsToCreate; v++) {
                    // 포인트 타입 먼저 결정
                    String pointType = f_randpointtype();

                    // 포인트 타입에 따른 amount 값 생성
                    int amount;
                    if (pointType.equals("WITHDRAW")) {
                        // withdraw인 경우 음수값만 생성 (-40000 ~ -1)
                        amount = -f_randintminmax(1, 40000);
                    } else {
                        // 다른 타입인 경우 기존 로직대로 처리
                        amount = f_randintminmax(-40000, 50000);
                    }

                    // point가 0 미만이 되는 경우 amount 조정
                    if ((currentPoint + amount) < 0) {
                        amount = -currentPoint;
                    }

                    // 포인트 누적
                    currentPoint += amount;

                    // CSV 형식으로 작성
                    writer.write(String.format("%d,%d,%d,\"\",%d,%d,%s,\"%s\",\"%s\",NULL\n",
                            historyNo++,        // history_no (PK)
                            memberNo,           // member_no
                            version,            // version
                            amount,             // amount
                            currentPoint,       // point
                            pointType,          // type
                            currentDateTime,    // inst_dt
                            currentDateTime     // updt_dt
                    ));

                    // 다음 기록을 위해 시간 증가 (1분~24시간 사이 랜덤)
                    currentDateTime = currentDateTime.plusSeconds(60 + (int)(Math.random() * 86340));
                    version++;
                }

                // 진행상황 출력 (1000000건마다)
                if (memberNo % 1000000 == 0) {
                    System.out.printf("Processed member %d / %d\n", memberNo, maxMemberNo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("포인트 이력 데이터 파일 생성 완료");
    }



}
