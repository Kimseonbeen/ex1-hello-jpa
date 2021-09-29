package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // persistence-unit name = hello
        // EntityManagerFactory = 하나만 생성해서 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManager는 클라이언트 요청이 올때마다 사용했다가 close해서 닫는거
        // 쓰레드간에 공유X (사용하고 버려야 한다.)
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        /**
         * 중요 ! JPA의 모든 데이터 변경은 트랜잭션 안에서 실행 !
         */

        /**
         * persist를 한다해도 DB에 값이 저장되는것은 아니다.
         * 찾을 때 쿼리를 날리지 않고 1차 캐시에 키값(id)으로 찾아낸다.
         * Member findMember1 = em.find(Member.class, 100L);
         */
        try {

            // 저장
            Team team = new Team();
            team.setName("TeamA");
            /**
             * 역방향(주인이 아닌 방향)만 연관관계 설정
             * 가짜 매핑 주인이 아닌것에 데이터를 넣으면 반영 되지 않음
             * 읽기 전용이다. JPA만 봤을 때는 add 하는 것이 아니지만
             * 객체 관계를 고려하면 항상 양쪽다 값을 입력해야 한다.
             */
            // team.getMembers().add(member);
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team);
            em.persist(member);

//            em.flush();
//            em.clear();

            Team findTeam = em.find(Team.class, team.getId());  // 1차 캐시
            List<Member> members = findTeam.getMembers();

            // flush(), clear()를 하지 않고 그냥 1차 캐시에 있는것을 가져오면 list 컬렉션엔 member가 담겨 있지 않음
            System.out.println("========================");
            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("========================");


           /* Member findMember = em.find(Member.class, member.getId());
            // 쿼리가 안 나가는 이유
            // 위에 쿼리를 사용해서 1차 캐시에 들어가 있느 상태라
            // 1차캐시로 조회
            List<Member> members = findMember.getTeam().getMembers();*/


            // Member member = em.find(Member.class, 150L);
            // member.setName("AAAA");

            // 준영속 상태 / 영속성 컨텍스트에서 member entity 더 이상 관리 X
            // 그러므로 위의 update 쿼리 커밋 시점에 실행 안됌
            //em.detach(member);

            //영속성 컨테스트 통으로 초기화
            // em.clear();

            // clear로 1차 캐시 삭제로 인해 다시 쿼리 실행
            // Member member2 = em.find(Member.class, 150L);


            /**
             * 객체 값만 바꿔도 DB 값이 변경 되는 이유 !
             * JPA를 통해 엔티티를 가져오면 JPA가 관리를 하게 됌
             * 그 후 JPA가 변경 됐는지 안됐는지 트렌젝션 커밋 시점에 체크를 함
             * 변경을 확인하면 커밋 직전에 update쿼리를 날린다....
             */

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();


    }
}
