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

## ch.03 영속성 관리
- **엔티티의 생명주기**
  - 비영속(new/transient)
    - 엔티티 객체를 생성한 순간
    - 영속성 컨텍스트, 데이터베이스와 아무 관련 없음
    - new Object
  - 영속(managed)
    - 엔티티 매니저를 통해 영속성 컨텍스트에 저장
    - 영속성 컨텍스트에 의해 관리 (영속성 컨텍스트의 1차 캐시에서 관리)
    - persist(Object), find(), JPQL
  - 준영속(detached)
    - 영속성 컨텍스트가 관리하던 엔티티를 관리하지 않게 된 경우
    - detach(Object), close(), clear()
  - 삭제(removed)
    - 영속성 컨텍스트와 데이터베이스에서 엔티티 삭제
    - remove(Object)
- **영속성 컨텍스트의 특징**
  - 영속성 컨텍스트와 식별자 값
    - 영속성 컨텍스트는 식별자 값으로 엔티티 구분
    - 영속 상태의 엔티티는 식별자 값이 반드시 존재
  - 영속성 컨텍스트와 데이터베이스 저장
    - em.flush()를 직접 호출한 경우
    - 트랜잭션을 커밋 시 flush() 자동 호출
    - JPQL 쿼리 실행 시 flush() 자동 호출
    - **flush 역할**
      - 변경 감지 : 1차 캐시의 모든 엔티티를 스냅샷(영속 엔티티의 최초 상태를 복사한 것)과 비교 후 수정된 엔티티의 수정 쿼리를 만들어 지연 SQL 저장소에 등록
      - 쓰기 지연 SQL 저장소의 쿼리를 DB에 전송 (등록, 수정, 삭제 쿼리)
  - 영속성 컨텍스트의 엔티티 관리 장점
    - 1차 캐시 : 영속성 컨텍스트 내에 존재하는 캐시, 주로 영속 등록한 엔티티와 조회한 엔티티의 정보 저장
    - 동일성 보장 : 1차 캐시에서 가지고 있는 동일한 엔티티 인스턴스를 항상 반환
    - 트랜잭션을 지원하는 쓰기 지연 : flush()가 호출되기 전까지 등록, 수정, 삭제 쿼리를 쓰기 지연 SQL 저장소에 저장
    - 변경 감지 : 1차 캐시의 엔티티와 스냅샷을 비교하여 변경된 부분의 수정 쿼리를 SQL 저장소에 등록
    - 지연 로딩 : 실제 객체 대신 프록시 객체를 로딩, 해당 객체를 실제 사용할 때 컨텍스트를 통해 데이터 호출