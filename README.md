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

    * enum 타입을 db에서도 사용하고 싶다면 @Enumerated 어노테이션 사용
    * 날짜 타입을 하고 싶다면 @Temporal(TemporalType.TIMESTAMP) TIME DATE TIMESTAMP 디비와 동일하게 3가지 종류가 있다.
    * @Lob -> 많은양에 글 작성위해 
    * #### 매핑 어노테이션
      * @Column : 컬럼매핑
        * name : 필드와 컬럼 이름
        * insertable , updateable :등록 변경 가능여부
        * nullable(DDL) : null 허용 여부
        * unique(DDL) : unique 제약조건 <- 잘 사용하지않는다. Table 매핑할때 유니크제약조건을 보통건다.
        * column_Definition : 데이터베이스에 컬럼 정보를 넣음
          * varchar(100) default 'EMPTY'
        * length(DDL) :문자길이 제약조건 String만 가능
        * precision,scale(DDL) : BigDecimal(BigInteger) 상춍 double,float 사용x
      * @Temporal :날짜 타입 매핑
        * 참고 LocalDateTime , LocalDate 사용시에는 생략가능
      * @Enumerated : enum 타입 매핑
        * 주의사항 : ORDINAL 사용 x ORDINAL : enum 순서를 저장(구분을 하기가 힘들다) default가 ORDINAL/ STRING : enum 이름을 데이터 베이스에 저장
      * @Lob : BLOB CLOB 매핑
      * @Transient : 특정 필드를 컬럼에 매핑 안함 -> 디비와 관계없이 계산 등을 원할때

    * ### 기본키 매핑
      * @Id
        * 직접할당
      * @GeneratedValue
        * 자동생성
        * IDENTITY : 데이터베이스에 위임 , mysql id가 null이면 자동생성해줌
        * IDENTITY 전략
          * db에 커밋이 되어야 pk값을 알수 잇다 하지만 jpa는 영속성 컨텍스트에 넣으려면 pk값을 알아야 사용가능 -> id 값으로 entity찾음
            * identity 전략은 그래서 예외적으로 em.pesist 호출 시점에 디비에 insert를 한다. 커밋시점이 아니라 persist 할때 isnert됨
             그래서 transaction이 끝나지않고 persist 만해도 객체를 찾을수 있습니다. 
        ~~~
        mysql 일 경우 autoincrement
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        ~~~
        <br></br>
    * SEQUENCE : 데이터베이스에 시퀀스 오브젝트 사용 ORACLE
      * @SequenceGenerator 필요
      * pk를 디비 sequence에서 가져옴 네트워크를 한번 더 타서 insert하기에 성능이 안좋을수 있지만
        * 대응 -> **allocationSize** :  키값을 가져올때 allocationSize 설정한 사이즈만큼 디비에서 미리 가져온후 쌓아놓고 사용한다. 네트워크를 한번더 타서 성능이 저하되는 이슈를 덜어준다. 
    ~~~
    @Entity
    @SequenceGenerator(
    name ="MEMBER_SEQ_GENERATOR",
    sequenceName ="MEMBER_SEQ",
    initialValue = 1,allocationSize = 1)
    public class Member {
    
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "MEMBER_SEQ_GENERATOR")
        private Long id;
    ~~~
    <br></br>
      * TABLE : 키생성용 테이블 만들어 데이터베이스 시퀀스를 흉내내는 전략 모든 DB가능 **잘 안씀니다.**
        * 단점 :성능이 안좋다.
        * @TableGenerator  필요
          * AUTO : 방언에 따라 자동지정
          ~~~
          @Entity
          @TableGenerator(
          name = "MEMBER_SEQ_GENERATOR",
          table = "MY_SEQUENCE"
          ,pkColumnValue = "MEMBER_SEQ",allocationSize = 1)
          
          public class Member {
          @Id
          @GeneratedValue(strategy = GenerationType.TABLE,
          generator = "MEMBER_SEQ_GENERATOR")
          private Long id;  
          ~~~
          
      * 권장하는 식별자 전략
        * 기본키 제약조건 : null x , 유일 , 변하면 안됨
        * 자연키(주민,전화번호) 적절치 않음 /  먼 미래까지 변하지않고 위 조건을 만족하는 키 대리키 이용 (sequence...) 업무로직과 상관없는 키
        * 주민등록번호도 적절하지않음
        * 권장 Long 타입 + 대체키 + 키 생성전략

### JPA1

* JPA 테이블 설계방식을 데이터베이스설계와 맞게 하면 객체 그래프 탐색이 불가능
* 전혀 객체지향스럽지 않음
~~~
ex)
Order order = em.find(Order.class, 1L);
Long memberId = order.getMemberId();
Member member = em.find(Member.class, memberId);
~~~