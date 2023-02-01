package jpabook.practice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class PersistenceContext {

    private static final String PK = "member1";

    public static void newToManaged(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        //객체를 생성한 상태(비영속)
        Member member = new Member();
        member.setId(PK);
        member.setUsername("회원1");

        try {
            tx.begin();
            System.out.println("비영속 엔티티 탐색");
            Member findMember = em.find(Member.class, PK);
            System.out.println("비영속 엔티티 탐색 성공 = " + findMember.getUsername());
        } catch (Exception e) {
            System.out.println("비영속 엔티티 탐색 실패");
        }

        System.out.println("비영속 엔티티 영속 엔티티로 변경");

        //객체를 저장한 상태(영속 - 1차 캐시)
        em.persist(member);

        try {
            System.out.println("영속 엔티티 탐색");
            Member findMember = em.find(Member.class, PK);
            System.out.println("영속 엔티티 탐색 성공 = " + findMember.getUsername());
        } catch (Exception e) {
            System.out.println("비영속 엔티티 탐색 실패");
        }
        em.close();
    }

    public static void mergeEntity(EntityManagerFactory emf) {

        Member member = createMember("memberA", "회원1", emf);

        member.setUsername("회원명 변경");

        mergeMember(member, emf);
    }

    private static void mergeMember(Member member, EntityManagerFactory emf) {

        EntityManager em2 = emf.createEntityManager();
        EntityTransaction tx2 = em2.getTransaction();

        tx2.begin();
        Member mergeMember = em2.merge(member);
        tx2.commit();

        //준영속 상태
        System.out.println("member = " + member.getUsername());

        //영속 상태
        System.out.println("mergeMember = " + mergeMember.getUsername());

        System.out.println("em2 contains member = " + em2.contains(member));
        System.out.println("em2 contains mergeMember = " + em2.contains(mergeMember));

        tx2.begin();
        em2.remove(mergeMember);
        tx2.commit();

        em2.close();
    }

    private static Member createMember(String id, String username, EntityManagerFactory emf) {


        EntityManager em1 = emf.createEntityManager();
        EntityTransaction tx1 = em1.getTransaction();

        tx1.begin();
        Member member = new Member();
        member.setId(id);
        member.setUsername(username);
        member.setAge(20);

        em1.persist(member);
        tx1.commit();

        em1.close(); // 영속성 컨텍스트1 종료, member 엔티티는 준영속 상태

        return member;
    }
}
