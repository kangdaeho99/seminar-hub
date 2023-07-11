package com.seminarhub.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * AuthMemberDTO는 User를 상속하여 부모 클래스인 User 클래스의 생성자를 super를 통해 호출합니다.
 * 부모 클래스인 User 클래스의 생성자를 호출할 수 있는 코드를 만듭니다. (부모 클래스인 User 클래스에 사용자 정의 생성자가 있으므로 반드시 호출해야합니다.)
 * Entity와 DTO 클래스를 별도로 구성시키기 위해 AuthMemberDTO를 생성합니다.
 * AuthMemberDTO는 DTO역할을 수행하면서 Spring Security에서 User 클래스를 extends(상속)하여 인가/인증 작업에 사용할 수 있습니다.
 * password는 부모 클래스를 사용하므로 별도의 멤버변수를 선언하지 않습니다.
 */
@Log4j2
@Getter
@Setter
@ToString
public class MemberAuthDTO extends User {

    private Long member_no;
    private String member_id;
    private String member_password;
    private String member_nickname;
    private boolean member_from_social;

    /**
     * [ 2023-07-11 daeho.kang ]
     * Description : It Calls User Constructor
     */
    public MemberAuthDTO(Long member_no, String member_id, String member_password, String member_nickname, boolean member_from_social, Collection<? extends GrantedAuthority> authorities){
        super(member_id, member_password, authorities);
        this.member_no = member_no;
        this.member_nickname = member_nickname;
        this.member_from_social = member_from_social;
    }
}
