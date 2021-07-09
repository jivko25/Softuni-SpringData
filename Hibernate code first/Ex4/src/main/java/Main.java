import jdk.jfr.Percentage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory enf = Persistence.createEntityManagerFactory("Ex4");
        EntityManager entityManager = enf.createEntityManager();
    }
}
