package jpabook.practice;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook-practice");

        //비영속과 영속 엔티티의 영속 컨텍스트 1차 캐시 존재 유무 확인
        PersistenceContext.newToManaged(emf);
        System.out.println("-----------------------------------------------");
        PersistenceContext.mergeEntity(emf);
        emf.close();
    }
}
