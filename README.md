# 자바 ORM 표준 JPA 프로그래밍 연습

## ch.02 JPA 시작
- maven 사용
- JPA Hibernate 와 h2 database를 의존성에 추가
- h2 database를 연결해줄 persistence.xml 생성
- 엔티티 매니저 팩토리 -> 엔티티 매니저 -> 트랜잭션 순으로 생성
- 트랜잭션에서 SQL문을 실행할 엔티티 매니저를 로직에 파라미터로 보내기 (성공하면 커밋, 실패하면 롤백)
- 엔티티 매니저를 통해 미리 만들어놓은 엔티티 객체 정보를 DB 테이블에 넣거나 {persist(Object)}
- 삭제하는 역할을 수행 {remove(Object)} ** update의 경우 엔티티의 setter를 이용하면 자동으로 SQL문이 실행된다.
- 하나의 객체만 찾으려면 {find(class, PK)} 를 이용
- 하나의 테이블의 정보를 전부 찾기 위해선 {createQuery(JPQL, class).getResultList()} 를 이용
- JPQL은 JPA 내에서 사용가능한 쿼리문으로 엔티티 객체를 대상으로 하고 클래스와 필드가 대상
- 참고로 SQL은 데이터베이스 테이블을 대상으로 쿼리

### troubleShooting
- 문제 : h2 DB를 설치하고 DB를 여는 과정에서 test DB를 찾을 수 없다는 에러가 뜸.
  - 해결 : 에러가 뜬 경로에 빈 텍스트 문서를 생성하고 확장자명까지 포함하여 파일명을 "test.mv.db"로 설정한다.
- 문제 : h2 driver version과 server version이 일치하지 않은 현상 발생 (driver version = 15, server version = 17)
  - 해결 : h2 database 의존성에서 1.4.187이었던 version을 2.1.210로 수정 후 빌드.
- 문제 : persistence를 찾지 못함.
  - 해결 : persistence.xml의 경로를 jpa-practice/ 에서 jpa-practice/src/main/resources/META-INF/ 로 수정
- 문제 : Member 객체를 찾지 못하는 에러 발생.
  - 해결 : 두 파일의 패키지를 jpa-practice/src/main/java/jpa/practice 로 이동