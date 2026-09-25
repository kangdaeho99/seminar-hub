
## **🤝 Seminar-Hub Service **

> Seminar-Hub Provides the REST API Server

## 📕 Tech Stack
- Java
- Spring Boot 4.0.0 (현재 빌드: common, data, settlement-api, batch)
- Spring Cloud
  - Spring Cloud Config
  - Spring Cloud Netflix Eureka
  - Spring Cloud Kubernetes DiscoveryServer
  - Spring Cloud Kubernetes Discovery
  - Spring Cloud Gateway
  - openFeign
- Architecture
  - MVC Pattern 
  - Multi Module
- Container Orchestration
  - Kubernetes 1.28, Docker-Compose
- Security
  - Spring Security 6
  - JWT (Access Token)
  - Redis In memory DB for Refresh Token
- Infrastructure
  - Ingress, Deployment, Service, Pod
  - Docker, DockerHub
- DB
  - Spring Data JPA(ORM)
- API Documentation
  - Swagger
- Test
  - JUnit, Mock
- Cloud
  - AWS EC2, AWS RDS


## 📕 Project Architecture
> Multi Module

![MultiModule](https://user-images.githubusercontent.com/48047377/272231005-39e30faa-6dca-47cd-bbf0-1897cb1aee14.png)

> Kubernetes

![k8sDeployment](https://user-images.githubusercontent.com/48047377/272234456-e3991da8-291a-46db-86a3-4db35bdcb5c3.png)


<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!----------------------------------------------------------------------->
<!--
## **🤝 세미나 관리 REST API Server ( Seminar-Hub Service )   — (진행중)**

> Spring Boot 3.0.2 를 통해 세미나 관리 REST API Server를 제공합니다. 


- **링크 :** [http://ec2-3-38-238-26.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui.html](http://ec2-3-38-238-26.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui.html)

- **깃허브 README & 소스코드 :**    

    [https://github.com/kangdaeho99/seminar-hub](https://github.com/kangdaeho99/seminar-hub)


![Swagger 화면](https://github.com/kangdaeho99/seminar-hub/assets/48047377/b79d0c1b-e853-4079-882f-6b0e8678539e)



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

    -  https://passionfruit200.tistory.com/426 ( 기본적인 Spring Security 구조에 대하여 library 코드로 알아보기 )

    -  https://passionfruit200.tistory.com/451 (  Interceptor를 활용하여 Handler Method에 회원권한체크해보기 )

    -  https://passionfruit200.tistory.com/419 ( 회원권한 테이블 (Member, MemberRole, Role)을 @ManyToMany 사용하지않고 Bridge Table 사용하여 설계해보기 )

    -  https://passionfruit200.tistory.com/414 ( docker 적용하는 이유, Spring Boot 3.0.x 에 docker를 AWS EC2와 함께 적용해보기 [1] )

    -  https://passionfruit200.tistory.com/397 ( Exception(예외) 처리의 중요성, JAVA에서 Exception 하기전에 알아야할 내용들, @RestControllerAdvice를 활용하여 예외처리 구현하기)

    -  https://passionfruit200.tistory.com/395 ( soft delete 처리하는 과정, @SQLDelete, @Where 을 사용안하는이유, BaseEntity에 deleted_at을 추가안하는이유 )

    -  https://passionfruit200.tistory.com/392 ( JUnit, Mock으로 단위테스트 작성해보기, 단위테스트의 필요성, JUnit LifeCycle )

    -  https://passionfruit200.tistory.com/389 ( BaseEntity란, @EntityListeners로 엔티티의 변화를 감지하는 방법, Persistence Context란 )

    -  https://passionfruit200.tistory.com/386 ( [Spring Boot]JPA의 findById vs getReferenceById 의 차이점과 언제사용하는것이 맞는지에 대하여 )


 <br/>


## 📒 현재 DB 구조 ( 회원관련쪽 진행중 )

> Member, MemberRole, Role 회원 관련 DB 구성이 완료된 상태입니다.


![DB 배포구조](https://github.com/kangdaeho99/seminar-hub/assets/48047377/60ebef9e-a0e5-4d58-866f-1dc20dda7dc6)


<br/><br/>



<br/>


-->
