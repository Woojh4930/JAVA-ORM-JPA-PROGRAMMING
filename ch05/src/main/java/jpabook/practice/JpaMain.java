package jpabook.practice;

import org.h2.security.XTEA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook-practice");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            testSave(em);
            testFind(em);
//            queryLogicJoin(em);
            /*팀2 로 업데이트*/
//            updateRelation(em);
            /*팀을 삭제*/
//            deleteRelation(em);
            biDirection(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void biDirection(EntityManager em) {

        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers();

        for (Member member :
                members) {
            System.out.println("member.username = " + member.getUsername());
        }
    }

    private static void deleteRelation(EntityManager em) {

        Member member1 = em.find(Member.class, "member1");
        Member member2 = em.find(Member.class, "member2");
        Team team = em.find(Team.class, "team1");

        member1.setTeam(null);
        member2.setTeam(null);
        em.remove(team);
    }

    private static void updateRelation(EntityManager em) {

        Team team2 = new Team("team2", "팀2");
        em.persist(team2);

        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    private static void queryLogicJoin(EntityManager em) {

        String jpql = "select m from Member m join m.team t where " + "t.name=:teamName";

        List<Member> resultList = em.createQuery(jpql, Member.class).setParameter("teamName", "팀1").getResultList();

        for (Member member :
                resultList) {
            System.out.println("[query] member.username=" + member.getUsername());
        }
    }

    private static void testFind(EntityManager em) {
        Member member = em.find(Member.class, "member1");
        Team team = member.getTeam();
        System.out.println("팀 이름 = " + team.getName());
    }

    private static void testSave(EntityManager em) {
        //팀1 저장
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        //회원1 저장
        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        //회원2 저장
        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);

    }
}
