import javax.persistence.EntityManager;
import java.util.Scanner;

public class Engine implements Runnable{

    private EntityManager entityManager;
    private Scanner sc;

    public Engine(EntityManager entityManager){
        this.entityManager = entityManager;
        this.sc = new Scanner(System.in);
    }

    public void run() {

    }
}
