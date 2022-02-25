# JPA
* META-INF 정보를 persistence클래스가 읽고 EntityMangerFactory 를 생성후 EntityManger를 통해 작업을 할 수 있다.
* JPA direct 방언을 통해 여러 데이터베이스를 사용 할 수 있다.
* JPA도 transaction을 만들어 transaction 단위로 실행된다.
* jpa 저장할땐 em.perist(객체)를 넣지만 수정시에는 객체를 다루듯이 사용하기에 set등으로 수정만 하고 persist하지 않아도 된다.
  * **WHY?** : jpa를 통해 entity를 가져오면 jpa가 그 객체를 관리하면 객체가 변경이 되면 transaction이 커밋시점에 체크하여 업데이트 쿼리를 날려준다.
* EntityMangerFactory  어플리케이션 전체에서 하나만 생성하여 공유하여 사용
* EntityManger는 쓰레드간 공유 x 한번 사용하고 close 하고버려야한다. -> dbconnection 사용하듯이
* **JPA에서 모든 데이터 변경은 transaction 안에서 실행!**
* jpa는 쿼리를 짤때 객체가 대상으로 쿼리를 짠다.
  ~~~
  em.createQuery("SELECT m FROM Member ") <- 객체 Member를 대상으로 전체 조회가 된다.
  select * from Member 가 아님 <- 테이블 조회
  ~~~
  *JPQL은 객체 대상 쿼리 / SQL은 데이터베이스 테이블 대상 쿼리이다.