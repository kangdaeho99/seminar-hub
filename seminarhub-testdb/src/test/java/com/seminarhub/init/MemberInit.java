package com.seminarhub.init;

import com.seminarhub.entity.JdbcTemplateUtil;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.functionUtil;
import com.seminarhub.entity.ProcedureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.seminarhub.entity.functionUtil.*;

//@SpringBootTest
public class MemberInit {
    JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getJdbcTemplate();
    ProcedureUtil procedureUtil = new ProcedureUtil();
    @Test
    public void procedureInsertMemberCSVTest(){
        procedureUtil.sp_insert_member();
    }

    @Test
    public void procedureInsertCompanyCSVTest(){
        procedureUtil.sp_insert_company(500000); // 50만개의 회사 데이터 생성
    }
    @Test
    public void procedureInsertSeminarCSVTest(){
        procedureUtil.sp_insert_seminar(500000); // 50만개의 회사 데이터 생성
    }

    @Test
    public void procedureInsertOrderCSVTest(){
//        procedureUtil.sp_insert_order(100, 10000000, ??); //하나의 회원당 평균 50개씩, 10000000명의 회원이, Seminar 최대 seminar_no값을 넣으면됩니다.
//        procedureUtil.sp_insert_order(20, 10000000, 24970170); //하나의 회원당 평균 50개씩, 10000000명의 회원이, Seminar 최대 seminar_no값을 넣으면됩니다.
//        procedureUtil.sp_insert_order(10, 10000000, 24970170); //하나의 회원당 평균 50개씩, 10000000명의 회원이, Seminar 최대 seminar_no값을 넣으면됩니다.

        // maxCount * 10 + maxCount일떄 (5, 10, 최대세미나값) -> 300건정도 나옴.
         // (10, 100) -> 5000건정도. 500000000
        // (5, 100) -> 2000건 정도. 200000000
        // (3, 100) -> 1500건 정도. 200000000

        //int randomOrderCount = (int) Math.max(0, ThreadLocalRandom.current().nextGaussian() * maxCount * 15 + maxCount);
        // (2, 100) -> 1200건정도. 120000000
        procedureUtil.sp_insert_order(2, 10000000, 24970170); //하나의 회원당 평균 50개씩, 10000000명의 회원이, Seminar 최대 seminar_no값을 넣으면됩니다.
        //그러면 50 * 100000000 * 24970170
    }

    @Test
    public void procedureInsertBoardCSVTest(){
//        procedureUtil.sp_insert_board(3, 10000000); //하나의 회원당 평균 3개씩, 10000000명의 회원이, 30 *
        // (1, 100) -> 800건. 80000000
        // * 15 (1, 100) -> 539 건, 53900000
//        * 7 (1, 100) -> 300건, 30000000
//        procedureUtil.sp_insert_board(1, 100); //하나의 회원당 평균 3개씩, 10000000명의 회원이, 30 *
        procedureUtil.sp_insert_board(1, 10000000); //하나의 회원당 평균 3개씩, 10000000명의 회원이, 30 *
    }
    // 10 -> 50개. 10 000000 -> 50000000
    @Test
    public void procedureInsertMemberPointCSVTest(){
        procedureUtil.sp_insert_member_points_history(1, 10000000);
    }



    @Test
    public void functionUtilTest(){
        System.out.println(functionUtil.f_randaddress());
        System.out.println(f_randminmax(15, 100));
        System.out.println(functionUtil.f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.of(2024, 11, 2, 0, 0)));
        System.out.println(functionUtil.f_randcompanyname());
        System.out.println(functionUtil.f_randboardtitle());
        System.out.println(functionUtil.f_randboardcontent());
        System.out.println(functionUtil.f_randgender(0.65, 0.35));
        System.out.println(functionUtil.f_randgrade());
        System.out.println(functionUtil.f_randjob());
        System.out.println(functionUtil.f_randid(40));
        System.out.println(functionUtil.f_randname());
        System.out.println(functionUtil.f_randphone());
        System.out.println(functionUtil.f_randpw());
        System.out.println(functionUtil.f_randseminardesciption());
        System.out.println(functionUtil.f_randseminarname());
    }
    @Test
    public void selectMember(){
        // 회원 목록 조회 테스트
        List<Member> selectedMembers = selectMembers();
        System.out.println("조회된 회원 목록:");
        for (Member member : selectedMembers) {
            System.out.println(member);
        }
    }


//1563529 1:26:20 --> 1567693 30초동안 4000건.
    @Test
    public void initMember() {
        System.out.println("회원 추가 테스트 시작");

        int chunkSize = 5000; // 1만 개씩 처리
        int startNo = 1538622;
        int endNo = 2000000;

        for (int i = startNo; i <= endNo; i += chunkSize) {
            List<Member> members = new ArrayList<>();
            for (int j = 0; j < chunkSize && i + j <= endNo; j++) {
                members.add(new Member(
                        (long) i + j,
                        f_randid(40),
                        f_randname(),
                        f_randpw(),
                        f_randgender(0.65, 0.35),
                        f_randminmax(15, 100),
                        f_randaddress(),
                        f_randjob(),
                        f_randgrade(),
                        f_randminmax(0, 30000),
                        f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now()),
                        f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now()),
                        null
                ));
            }

            String sql = "INSERT INTO member (member_no, id, name, pw, gender, age, address, job, grade, points, inst_dt, updt_dt, del_dt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // 배치 업데이트 수행
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    Member member = members.get(j);
                    ps.setLong(1, member.getMemberNo());
                    ps.setString(2, member.getId());
                    ps.setString(3, member.getName());
                    ps.setString(4, member.getPw());
                    ps.setString(5, String.valueOf(member.getGender()));
                    ps.setInt(6, member.getAge());
                    ps.setString(7, member.getAddress());
                    ps.setString(8, member.getJob());
                    ps.setString(9, member.getGrade());
                    ps.setInt(10, member.getPoints());
                    ps.setTimestamp(11, Timestamp.valueOf(member.getInstDt()));
                    ps.setTimestamp(12, Timestamp.valueOf(member.getUpdtDt()));
                    ps.setNull(13, java.sql.Types.TIMESTAMP);
                }

                @Override
                public int getBatchSize() {
                    return members.size();
                }
            });

            System.out.printf("Processed %d ~ %d\n", i, i + chunkSize - 1);
        }

        System.out.println("회원 추가 완료");
    }

    @Test
    public void initMemberssss() throws SQLException {
        System.out.println("회원 추가 테스트 시작");

        int chunkSize = 5000; // 배치 크기 줄이기
        int startNo = 1672834;
        int endNo = 2000000;

        // 트랜잭션 시작
        jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);

        try {
            for (int i = startNo; i <= endNo; i += chunkSize) {
                List<Member> members = new ArrayList<>();
                for (int j = 0; j < chunkSize && i + j <= endNo; j++) {
                    members.add(new Member(
                            (long) i + j,
                            f_randid(40),
                            f_randname(),
                            f_randpw(),
                            f_randgender(0.65, 0.35),
                            f_randminmax(15, 100),
                            f_randaddress(),
                            f_randjob(),
                            f_randgrade(),
                            f_randminmax(0, 30000),
                            f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now()),
                            f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now()),
                            null
                    ));
                }

                String sql = "INSERT INTO member (member_no, id, name, pw, gender, age, address, job, grade, points, inst_dt, updt_dt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // 배치 업데이트 수행
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        Member member = members.get(j);
                        ps.setLong(1, member.getMemberNo());
                        ps.setString(2, member.getId());
                        ps.setString(3, member.getName());
                        ps.setString(4, member.getPw());
                        ps.setString(5, String.valueOf(member.getGender()));
                        ps.setInt(6, member.getAge());
                        ps.setString(7, member.getAddress());
                        ps.setString(8, member.getJob());
                        ps.setString(9, member.getGrade());
                        ps.setInt(10, member.getPoints());
                        ps.setTimestamp(11, Timestamp.valueOf(member.getInstDt()));
                        ps.setTimestamp(12, Timestamp.valueOf(member.getUpdtDt()));
                    }

                    @Override
                    public int getBatchSize() {
                        return members.size();
                    }
                });

                System.out.printf("Processed %d ~ %d\n", i, Math.min(i + chunkSize - 1, endNo));
            }

            // 트랜잭션 커밋
            jdbcTemplate.getDataSource().getConnection().commit();
        } catch (SQLException e) {
            // 오류 발생 시 롤백
            jdbcTemplate.getDataSource().getConnection().rollback();
            throw new RuntimeException(e);
        } finally {
            // 트랜잭션 종료
            jdbcTemplate.getDataSource().getConnection().setAutoCommit(true);
        }

        System.out.println("회원 추가 완료");
    }

    @Test
    public void initMemberHEAPOUT() {
        System.out.println("회원 추가 테스트 시작");

        // 여러 회원 생성
        List<Member> members = new ArrayList<>();
        for (int i = 1501000; i <= 2000000; i++) {
            members.add(new Member(
                    (long) i,
                    f_randid(40), // 랜덤 아이디 생성
                    f_randname(), // 랜덤 이름 생성
                    f_randpw(), // 랜덤 패스워드 생성
                    f_randgender(0.65, 0.35), // 랜덤 성별 생성
                    f_randminmax(15, 100), // 랜덤 나이 생성
                    f_randaddress(), // 랜덤 주소 생성
                    f_randjob(), // 랜덤 직업 생성
                    f_randgrade(), // 랜덤 등급 생성
                    f_randminmax(0, 30000), // 초기 적립금 0
                    f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now()), // 랜덤 삽입일시
                    f_randdatetime(LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now()), // 랜덤 수정일시
                    null // 삭제일시는 NULL로 설정
            ));
        }

        String sql = "INSERT INTO member (member_no, id, name, pw, gender, age, address, job, grade, points, inst_dt, updt_dt, del_dt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        // 배치 업데이트 수행
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Member member = members.get(i);
                ps.setLong(1, member.getMemberNo()); // member_no 직접 설정
                ps.setString(2, member.getId());
                ps.setString(3, member.getName());
                ps.setString(4, member.getPw());
                ps.setString(5, String.valueOf(member.getGender()));
                ps.setInt(6, member.getAge());
                ps.setString(7, member.getAddress());
                ps.setString(8, member.getJob());
                ps.setString(9, member.getGrade());
                ps.setInt(10, member.getPoints());
                ps.setTimestamp(11, Timestamp.valueOf(member.getInstDt()));
                ps.setTimestamp(12, Timestamp.valueOf(member.getUpdtDt()));
                ps.setNull(13, java.sql.Types.TIMESTAMP); // 삭제일시는 NULL로 설정
            }

            @Override
            public int getBatchSize() {
                return 1000;
            }
        });

        System.out.println("회원 추가 완료");
    }

    public List<Member> selectMembers() {
        String sql = "SELECT * FROM member ORDER BY member_no LIMIT 10";
        return jdbcTemplate.query(sql, new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Member(
                        rs.getLong("member_no"),
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("pw"),
                        rs.getString("gender").charAt(0),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getString("job"),
                        rs.getString("grade"),
                        rs.getInt("points"),
                        rs.getTimestamp("inst_dt").toLocalDateTime(),
                        rs.getTimestamp("updt_dt").toLocalDateTime(),
                        rs.getTimestamp("del_dt") != null ? rs.getTimestamp("del_dt").toLocalDateTime() : null
                );
            }
        });
    }
}
