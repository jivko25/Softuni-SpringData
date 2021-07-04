import entities.Address;
import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Engine implements Runnable{
    private EntityManager entityManager;
    private Scanner sc;

    public Engine(EntityManager entityManager){
        this.entityManager = entityManager;
        this.sc = new Scanner(System.in);
    }


    @Override
    public void run() {
        System.out.print("Enter number of exersise:");
        int ex = Integer.parseInt(sc.nextLine());
        System.out.println();
        switch (ex){
            case 2:
                ex2();
                break;
            case 3:
                ex3();
                break;
            case 4:
                ex4();
                break;
            case 5:
                ex5();
                break;
            case 6:
                ex6();
                break;
            case 7:
                ex7();
                break;
            case 8:
                ex8();
                break;
            case 9:
                ex9();
                break;
            case 10:
                ex10();
                break;
            case 11:
                ex11();
                break;
            case 12:
                ex12();
                break;
            case 13:
                ex13();
                break;
        }
        System.out.println("Do you want next task (Enter next/close)!");
        String next = sc.nextLine();
        switch (next){
            case "next":
                run();
                break;
            case "close":
                System.out.println("Bye! :)");
                break;
        }
    }

    private void ex2() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t SET t.name = UPPER(t.name) WHERE length(t.name) <= 5 ");
        int affectedRows = query.executeUpdate();
        System.out.println(affectedRows);
        entityManager.getTransaction().commit();
    }

    private void ex3() {
        String [] search = sc.nextLine().split("\\s+");
        Long singleResult = entityManager.createQuery("SELECT count (e) FROM Employee e " +
                "WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                .setParameter("f_name", search[0])
                .setParameter("l_name", search[1])
                .getSingleResult();
        System.out.println(singleResult == 0 ? "No" : "Yes");
    }

    private void ex4() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.salary > :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .forEach(e -> System.out.println(e.getFirstName()));
    }

    private void ex5() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.department.name = :d_name " +
                "ORDER BY e.salary, e.id ", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultList()
                .forEach(e -> System.out.printf("%s %s from Research and Development - $%.2f\n", e.getFirstName(), e.getLastName(), e.getSalary()));


    }

    private void ex6() {
        System.out.println("Enter employee last name:");
        String lastName = sc.nextLine();
        Address address = new Address();
        address.setText("Vitoshka 15");
        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();
        Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();
        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private void ex7() {
        List <Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                "ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses
                .forEach(address -> {
                    System.out.printf("%s, %s - %d employees%n",
                            address.getText(),
                            address.getTown() == null ? "Unknows" : address.getTown().getName(),
                            address.getEmployees().size());
                });
    }

    private void ex8() {
        int id = Integer.parseInt(sc.next());
        Employee employee = entityManager.find(Employee.class, 147);
        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        Set<Project> projects = employee.getProjects();
        projects.forEach(project -> System.out.println(project.getName()));
    }

    private void ex9() {
        List<Project> projects = entityManager
                .createQuery("SELECT p FROM Project p " +
                                "ORDER BY p.name ", Project.class)
                .setMaxResults(10)
                .getResultList();
        projects.forEach(project -> System.out.printf("Project name: %s%n" +
                "Project description: %s%n" +
                "Project start date: %s%n" +
                "Project end date: %s%n", project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate()));
    }

    private void ex10() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE Employee e SET e.salary = e.salary * 1.12 " +
                                    "WHERE e.department.id IN :ids ")
                    .setParameter("ids", Set.of(1, 2, 4, 11))
                    .executeUpdate();
        entityManager.getTransaction().commit();
        System.out.println("test");
        entityManager.createQuery("SELECT e FROM Employee e " +
                                    "WHERE e.department.name = :engineering " +
                                    "OR e.department.name = :tool_design " +
                                    "OR e.department.name = :marketing " +
                                    "OR e.department.name = :information_serveces", Employee.class)
                    .setParameter("engineering", "Engineering")
                    .setParameter("tool_design","Tool Design")
                    .setParameter("marketing", "Marketing")
                    .setParameter("information_serveces", "Information Serveces")
                    .getResultList()
                    .forEach(employee -> System.out.printf("%s %s (%.2f)%n", employee.getFirstName(), employee.getLastName(), employee.getSalary()));
    }

    private void ex11() {
    }

    private void ex12() {
    }

    private void ex13() {
    }

}
