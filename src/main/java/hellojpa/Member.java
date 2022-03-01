package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name="USERNAME")
    private String username;

    //Member는 1 : Team 은 N 관계
    @ManyToOne
    @JoinColumn(name = "TEAM_ID") //fk 가 될 컬럼명 <- Team에 fk가 될 컬럼 이름  주키 여기서 수정 등록 가능
    private Team team;

    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
