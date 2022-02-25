# JPA
* META-INF 정보를 persistence클래스가 읽고 EntityMangerFactory 를 생성후 EntityManger를 통해 작업을 할 수 있다.
~~~
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
~~~
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

### 영속성 컨텍스트
  * 엔티티를 영구 저장하는 환경이라는 뜻
  * EntityManagerFactory를 통해 고객요청이 오면 EntityManger를 생성한다. 이 EntityManger 커넥션풀 통해 디비를 사용한다.
  * 엔티티의 생명주기
    * 비영속성 : 영속성 컨텍스트와 관계 x 새로운 상태 최초 상태 생성된 상태
    ~~~
    객체 생성한 상태
    Member member = new Member();
    memeber.setId(1L)
    member.setName("hi")
    ~~~
    <br/>
  
    * 영속 : 영속성컨텍스트에 관리 되는상태 persist
    ~~~
    em.persist(member)
    ~~~
    <br/>
    * 준영 : 영속성컨텍스트에 분리
    
    ~~~
    em.detach(member)
    ~~~
    <br/>
    * 삭제 : 삭제
    
    ~~~
    em.remove(member)
    ~~~
  
### 엔티티 매핑
  * 객체와 테이블 매핑 : @Entity , @Table
  * 필드와 컬럼 매핑 : @Column
  * 기본 키 매핑 : @Id
  * 연관관계 매핑 : @ManyToOne, @ManyToMany , @JoinColumn -> 테이블관에 관계 매핑 1:N 1:1 ....
<br><br/>
    * @Entity (name = jpa 구분이름 기본값 권장)
      *db table 명 변경은 @Table("~~)
      *@Table (name , catalog , schema ,uniqueConstraint : DDL 생성시 유니크 제약조건 생성) 사용 
      * JPA 관리 엔티티 필수값 / 기본생성자 필수 final 클래스 ,enum , interface , inner 클래스 사용 x , 저장할 필드에 final 사용 x
      
    * 데이터베이스 스키마 자동 생성
      * 개발단계일때 사용 해야하고 DDL을 어플리케이션 실행 시점에 생성해준다. 테이블중심 -> 객체 중심
      * 데이터베이스 방언에 맞는 DDL생성을 도와준다. (mysql , oracle ...)
      ~~~
      <property name="hibernate.hbm2ddl.auto" value="create" />
      속성 : create / create-drop : 종료시점에 테이블 drop / update : 변경분만 반영
      validate : 엔티티와 테이블 정상매핑 확인 / none : 사용 안함
      ~~~