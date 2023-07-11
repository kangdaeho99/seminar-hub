## **🤝 세미나 관리 REST API Server ( Seminar-Hub Service )   — (진행중)**
> Spring Boot 3.0.2 를 통해 세미나 관리 REST API Server를 제공합니다. 

- **링크 :** [http://ec2-3-38-238-26.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui.html](http://ec2-3-38-238-26.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui.html)
- **깃허브 README & 소스코드 :**    
    [https://github.com/kangdaeho99/seminar-hub](https://github.com/kangdaeho99/seminar-hub)

![Swagger 화면](https://github.com/kangdaeho99/seminar-hub/assets/48047377/764ea046-0d35-4fbf-a64b-d75a3018e3f2)


## 📕 프로젝트 배포 구조
> AWS EC2 1대, AWS RDS 1대, Docker, Jenkins in Docker ( AWS EC2 Instance 1개로 사용하기 위한 구조 ) 입니다.

![프로젝트 배포구조](https://user-images.githubusercontent.com/48047377/252376158-f1711893-77c7-4777-86bd-9c59f99e7a39.PNG)

<br/><br/>


- 프로젝트 설명 : 세미나 관리 REST API 입니다.
- 백엔드 기술스택 :
    - Spring Boot 3.0.2 ( JPA )
    - Spring Security 6
    - JWT
    - Spring Docs OPENAPI 3.0 를 활용하여 Swagger DOC 작성
    - JUnit & Mock 을 활용한 단위테스트 진행
    - AWS EC2
    - AWS RDS
    - Docker & DockerHub
    - Jenkins
    
- 백엔드에 개발 중 고려한점들 글 정리 :
    -  https://passionfruit200.tistory.com/419 ( 회원권한 테이블 (Member, Member_Role, Role)을 @ManyToMany 사용하지않고 Bridge Table 사용하여 설계해보기 )
    -  https://passionfruit200.tistory.com/414 ( docker 적용하는 이유, Spring Boot 3.0.x 에 docker를 AWS EC2와 함께 적용해보기 [1] )
    -  https://passionfruit200.tistory.com/397 ( Exception(예외) 처리의 중요성, JAVA에서 Exception 하기전에 알아야할 내용들, @RestControllerAdvice를 활용하여 예외처리 구현하기)
    -  https://passionfruit200.tistory.com/395 ( soft delete 처리하는 과정, @SQLDelete, @Where 을 사용안하는이유, BaseEntity에 deleted_at을 추가안하는이유 )
    -  https://passionfruit200.tistory.com/392 ( JUnit, Mock으로 단위테스트 작성해보기, 단위테스트의 필요성, JUnit LifeCycle )
    -  https://passionfruit200.tistory.com/389 ( BaseEntity란, @EntityListeners로 엔티티의 변화를 감지하는 방법, Persistence Context란 )
    -  https://passionfruit200.tistory.com/386 ( [Spring Boot]JPA의 findById vs getReferenceById 의 차이점과 언제사용하는것이 맞는지에 대하여 )


<br/>

