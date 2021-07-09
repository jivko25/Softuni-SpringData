import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Engine2 implements Runnable {

    private  final EntityManager entityManager;
    private BufferedReader bufferedReader;

    public Engine2(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader=new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Select exercises number");

        try {
            int exNumber =Integer.parseInt(bufferedReader.readLine());

            switch (exNumber){
                case 2-> exTwo();
                case 3-> exTree();
                case 4-> exFour();
                case 5-> exFive();
                case 6->exSix();
                case 7->exSeven();
                case 8->exEight();
                case 9->exNine();
                case 10->exTen();
                case 11->exEleven();
                case 12->exTwelve();
                case 13->exThirteen();

            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void exThirteen() throws IOException {
        System.out.println("Enter town name:");
        String townName=bufferedReader.readLine();

        Town town=entityManager
                .createQuery("SELECT t FROM Town t " +
                                "WHERE t.name=:t_name",
                        Town.class)
                .setParameter("t_name",townName)
                .getSingleResult();

        int affectedRows =removeAddressesByTownId(town.getId());
        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        System.out.printf("%d address in %s is deleted",affectedRows,townName);
    }

    private int removeAddressesByTownId(Integer id) {
        List<Address> addresses=entityManager.createQuery("SELECT a FROM Address a " +
                "WHERE a.town.id=:p_id",Address.class)
                .setParameter("p_id",id)
                .getResultList();

        entityManager.getTransaction().begin();
        addresses.forEach(entityManager::remove);
        entityManager.getTransaction().commit();

        return  addresses.size();
    }

    @SuppressWarnings("unchecked")
    private void exTwelve() {
        List<Object[]>rows=entityManager
                .createQuery("SELECT d.name, MAX(e.salary)  FROM Department AS d\n" +
                        "JOIN Employee  AS e ON e.department.id=d.id\n" +
                        "GROUP BY d.name\n" +
                        "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000 ",Object[].class)
                .getResultList();

        rows.forEach(objects -> {
            System.out.printf("%s - %.2f%n",objects[0],(BigDecimal)objects[1]);
        });
    }

    private void exEleven() throws IOException {
        System.out.println("Enter pattern: ");
        String pattern=bufferedReader.readLine();

        List<Employee>employees=entityManager.createQuery("SELECT e FROM Employee e WHERE SUBSTRING(e.firstName, 1, :len) = :pattern",Employee.class)
                .setParameter("len",pattern.length())
                .setParameter("pattern",pattern)
                .getResultList();

        employees.forEach(employee -> {
            System.out.printf("%s %s - %s ($%.2f)%n",employee.getFirstName(),employee.getLastName(),
                    employee.getJobTitle(),employee.getSalary());
        });

    }

    private void exTen() {
        entityManager.getTransaction().begin();
        int affectedRows= entityManager.createQuery("UPDATE Employee e " +
                "SET e.salary=e.salary*1.2 " +
                "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1,2,4,11))
                .executeUpdate();
        entityManager.getTransaction().commit();

        System.out.println(affectedRows);
    }

    private void exNine() {
        List<Project> projects=entityManager.createQuery(
                "SELECT p FROM Project p ORDER BY " +
                        "p.startDate DESC",Project.class)
                .setMaxResults(10)
                .getResultList();

        projects.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p-> System.out.printf
                        ("Project name:%s%n\tProject Description:%s%n...%n\tProject Start Date:%s%n\tProject End Date: %s%n",
                                p.getName(),p.getDescription(),p.getStartDate(),p.getEndDate()));

    }

    private void exEight() throws IOException {
        System.out.println("Enter id:");
        int id=Integer.parseInt(bufferedReader.readLine());
        Employee employee=entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.id=:id",Employee.class)
                .setParameter("id",id).getSingleResult();

        System.out.printf("%s %s - %s%n",employee.getFirstName(),
                employee.getLastName(),employee.getJobTitle());

        employee.getProjects().stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p-> System.out.printf("\t%s%n",p.getName()));

    }

    private void exSeven() {
        List<Address> addresses=entityManager
                .createQuery("SELECT  a FROM Address a " +
                        "ORDER BY a.employees.size DESC",Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses.forEach(address -> {
            System.out.printf("%s , %s - %d employees%n",
                    address.getText(),
                    address.getTown()==null
                            ? "Unknown" : address.getTown().getName(),
                    address.getEmployees().size());
        });
    }

    private void exSix() throws IOException {
        System.out.println("Enter employee last name:");
        String lastName=bufferedReader.readLine();

        Employee employee = entityManager
                .createQuery("SELECT e FROM Employee e "+
                        "WHERE e.lastName= :l_name",Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address=createAddress("Vitoshka 15");

        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address createAddress(String addressText) {
        Address address=new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void exFive() {
        entityManager
                .createQuery("SELECT e FROM  Employee e "+
                        "WHERE e.department.name= :d_name "+
                        "ORDER BY e.salary, e.id",Employee.class)
                .setParameter("d_name","Research and Development")
                .getResultStream()
                .forEach(employee -> {
                    System.out.printf("%s %s from %s - $%.2f%n",
                            employee.getFirstName(),
                            employee.getLastName(),
                            employee.getDepartment().getName(),
                            employee.getSalary());
                });
    }

    private void exFour() {
        entityManager
                .createQuery("SELECT e FROM Employee  e "+
                        "WHERE  e.salary > :min_salary",Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void exTree() throws IOException {
        System.out.println("Enter employee full name: ");
        String[] fullName=bufferedReader.readLine().split("\\s+");
        String firstName=fullName[0];
        String lastName=fullName[1];

        Long singleResult= entityManager.createQuery("SELECT count(e) FROM Employee e " +
                        "WHERE e.firstName = :f_name AND e.lastName = :l_name ",
                Long.class)
                .setParameter("f_name",firstName)
                .setParameter("l_name",lastName)
                .getSingleResult();

        System.out.println(singleResult==0? "No" : "Yes");
    }

    private void exTwo() {
        entityManager.getTransaction().begin();
        Query query =entityManager.createQuery("UPDATE Town t " +
                "SET t.name=upper(t.name) " +
                "WHERE length(t.name)<=5 ");

        System.out.println(query.executeUpdate());
        entityManager.getTransaction().commit();
    }
}

