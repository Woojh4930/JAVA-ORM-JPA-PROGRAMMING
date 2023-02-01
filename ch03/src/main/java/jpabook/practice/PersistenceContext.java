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
        emf.close();
    }

}
