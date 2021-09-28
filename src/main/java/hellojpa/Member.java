package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity //JPA가 관리하고, 매핑해서 사용하는 구나
//@Table name을 지정해서 테이블과 객체 이름이 다를 경우 사용
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    /*@Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    //@Temporal 자바 문법 추가 후 사용하지 않음
    // LocalDate, LocalDateTime 사용
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    @Lob    //varchar 범위보다 큰 값을 넣고 싶을 때
    private String description;

    @Transient      // DB와 매핑 하지 않음 / DB 컬럼 추가 안됌
    private int temp;*/

    public Member() {
    }

}
