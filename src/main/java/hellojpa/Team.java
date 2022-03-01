package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") //팀에 의해서 관리가 된다. 외래키 여기서는 조회만 가능 수정 불가능
    private List<Member> members = new ArrayList<>();
}
