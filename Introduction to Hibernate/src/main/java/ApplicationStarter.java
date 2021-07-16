import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationStarter implements Runnable {

    private final EntityManager em;
    private final BufferedReader reader;

    public ApplicationStarter(EntityManager em) {
        this.em = em;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Please enter exercise number:");
                int exerciseNumber = Integer.parseInt(reader.readLine());
                switch (exerciseNumber) {
                    case 0:
                        return;
                    case 2:
                        changeCasing_2();
                        break;
                    case 3:
                        containsEmployee_3();
                        break;
                    case 4:
                        employeesWithSalaryOver50000_4();
                        break;
                    case 5:
                        employeesFromDepartment_5();
                        break;
                    case 6:
                        addingANewAddressAndUpdatingEmployee_6();
                        break;
                    case 7:
                        addressesWithEmployeeCount_7();
                        break;
                    case 8:
                        getEmployeeWithProject_8();
                        break;
                    case 9:
                        findLatestTenProjects_9();
                        break;
                    case 10:
                        increaseSalaries_10();
                        break;
                    case 11:
                        findEmployeesByFirstName_11();
                        break;
                    case 12:
                        employeesMaximumSalaries_12();
                        break;
                    case 13:
                        removeTowns_13();
                        break;
                    default:
                        System.out.println("Please enter a valid exercise number:");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Invalid input format!");
            } catch (NumberFormatException | NoResultException e) {
                System.out.println("Invalid input parameters!");
            }
        }
    }

    private void changeCasing_2() {

        em.getTransaction().begin();
        Query allTowns = em.createQuery("SELECT t FROM Town t WHERE length(t.name) <= 5");
        List<Town> list = allTowns.getResultList();
        for (Town town : list) {
            town.setName(town.getName().toUpperCase());
            em.persist(town);
        }

        em.getTransaction().commit();

        System.out.println("Qualifying towns successfully updated!");

        System.out.println();
    }

    private void containsEmployee_3() throws IOException {
        System.out.println("Please enter the name of the employee:");
        String employeeName = reader.readLine();

        Query query = em.createQuery("SELECT e.firstName FROM Employee e WHERE concat(e.firstName,' ', e.lastName) = :employee");
        query.setParameter("employee", employeeName);
        try {
            query.getSingleResult();
            formatResult();
            System.out.println("Yes");
        } catch (NoResultException e) {
            formatResult();
            System.out.println("No");
        } finally {
            System.out.println();
        }
    }

    private void employeesWithSalaryOver50000_4() throws IOException {
        System.out.println("Please enter minimum salary:");
        BigDecimal salary = BigDecimal.valueOf(Double.parseDouble(reader.readLine()));
        Query query = em.createQuery("SELECT e.firstName FROM Employee e WHERE e.salary > :salary");
        query.setParameter("salary", salary);
        query.getResultStream().forEach(System.out::println);
        System.out.println();
    }

    private void employeesFromDepartment_5() throws IOException {
        System.out.println("Please enter the department name:");
        String departmentName = reader.readLine();

        TypedQuery<Employee> query = em.createQuery("SELECT e " +
                "FROM Employee e " +
                "JOIN e.department d " +
                "WHERE d.name = :department " +
                "ORDER BY e.salary, e.id", Employee.class);

        query.setParameter("department", departmentName);
        List<Employee> resultList = query.getResultList();

        formatResult();

        if (resultList.isEmpty()) {
            System.out.println("No such department exists!");
            System.out.println();
            return;
        }

        resultList.forEach(e -> {
            String output = String.format("%s %s from %s - $%.2f",
                    e.getFirstName(), e.getLastName(), e.getDepartment().getName(), e.getSalary())
                    .replace(",", ".");
            System.out.println(output);
        });

        System.out.println();
    }

    private void addingANewAddressAndUpdatingEmployee_6() throws IOException {
        System.out.println("Please enter the employee's last name:");
        String lastName = reader.readLine();
        em.getTransaction().begin();

        Address address = new Address();
        address.setText("Vitoshka 15");
        em.persist(address);
        em.getTransaction().commit();
        int addressId = address.getId();

        em.getTransaction().begin();
        int addressChange = em.createQuery("UPDATE Employee e " +
                "SET e.address.id = :id " +
                "WHERE e.lastName = :lastName")
                .setParameter("lastName", lastName)
                .setParameter("id", addressId)
                .executeUpdate();

        em.getTransaction().commit();

        String forPrint = (addressChange == 1)
                ? String.format("Address of %s updated successfully!", lastName)
                : "No employee address was updated!";
        formatResult();
        System.out.println(forPrint);
        System.out.println();
    }

    private void addressesWithEmployeeCount_7() {
        formatResult();

        em.createQuery("SELECT a " +
                "FROM Employee e " +
                "JOIN e.address a " +
                "GROUP BY e.address " +
                "ORDER BY COUNT(e.address) DESC ", Address.class)
                .getResultStream()
                .limit(10)
                .forEach(a -> System.out.printf("%s, %s - %d employees%n",
                        a.getText(), a.getTown().getName(), a.getEmployees().size()));

        System.out.println();
    }

    private void getEmployeeWithProject_8() throws IOException, NoResultException {
        System.out.println("Please enter employee ID:");
        int id = Integer.parseInt(reader.readLine());

        Employee employee = em.createQuery("SELECT e FROM Employee e " +
                "WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();

        formatResult();

        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        employee.getProjects().stream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .forEach(p -> System.out.println("      " + p.getName()));

        System.out.println();
    }

    private void findLatestTenProjects_9() {
        formatResult();

        em.createQuery("SELECT p FROM Project p " +
                "ORDER BY p.startDate DESC ", Project.class)
                .setMaxResults(10)
                .getResultStream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .forEach(p -> {
                    System.out.println("Project name: " + p.getName());
                    System.out.println("      " + "Project Description: " + p.getDescription());
                    System.out.println("      " + "Project Start Date: " +
                            p.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
                    System.out.println("      " + "Project End Date: " +
                            p.getEndDate());
                });

        System.out.println();
    }

    private void increaseSalaries_10() {
        List<Employee> resultList = em.createQuery("SELECT e FROM Employee e " +
                "WHERE e.department.name IN " +
                "('Engineering', 'Tool Design', 'Marketing', 'Information Services')", Employee.class)
                .getResultList();

        formatResult();

        for (Employee e : resultList) {
            em.getTransaction().begin();
            e.setSalary(e.getSalary().multiply(BigDecimal.valueOf(1.12)));
            em.persist(e);
            em.getTransaction().commit();
            String output = String.format("%s %s ($%.2f)",
                    e.getFirstName(), e.getLastName(), e.getSalary())
                    .replace(",", ".");
            System.out.println(output);
        }


        System.out.println();

    }

    private void findEmployeesByFirstName_11() throws IOException {
        System.out.println("Please enter a pattern:");
        String input = reader.readLine().toLowerCase();

        List<Employee> list = em.createQuery("SELECT e FROM Employee e " +
                "WHERE LOWER(e.firstName) LIKE CONCAT(:regex, '%')", Employee.class)
                .setParameter("regex", input)
                .getResultList();

        formatResult();

        if (list.isEmpty()) {
            System.out.println("No first name starts with the provided pattern.");
        } else {
            list.forEach(e -> {
                String output = String.format("%s %s - %s - ($%.2f)",
                        e.getFirstName(), e.getLastName(), e.getJobTitle(), e.getSalary())
                        .replace(",", ".");
                System.out.println(output);
            });
        }

        System.out.println();
    }

    private void employeesMaximumSalaries_12() {
        List<Department> list = em.createQuery("SELECT e.department " +
                "FROM Employee e " +
                "GROUP BY e.department " +
                "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000", Department.class).getResultList();

        formatResult();

        for (Department department : list) {
            BigDecimal maxSalary = findMaxSalary(department);
            String output = String.format("%s %.2f", department.getName(), maxSalary).replace(",", ".");
            System.out.println(output);
        }

        System.out.println();
    }

    private void removeTowns_13() throws IOException {
        System.out.println("Please enter town name:");
        String townName = reader.readLine();

        Town town = em.createQuery("SELECT t FROM Town t WHERE t.name = :townName", Town.class)
                .setParameter("townName", townName)
                .getSingleResult();

        formatResult();

        int townId = town.getId();
        em.getTransaction().begin();

        List<Integer> addressesId = em.createQuery("SELECT a FROM Address a WHERE a.town.id = :townId", Address.class)
                .setParameter("townId", townId)
                .getResultStream()
                .map(Address::getId)
                .collect(Collectors.toList());

        em.createQuery("UPDATE Employee e " +
                "SET e.address = null " +
                "WHERE e.address.id IN(:addresses)")
                .setParameter("addresses", addressesId)
                .executeUpdate();

        int deletedAddresses = em.createQuery("DELETE FROM Address a " +
                "WHERE a.id IN (:addresses)")
                .setParameter("addresses", addressesId)
                .executeUpdate();

        em.remove(town);
        em.getTransaction().commit();

        System.out.printf("%d address in %s deleted%n", deletedAddresses, townName);

        System.out.println();
    }

    private BigDecimal findMaxSalary(Department department) {
        Employee employee = department.getEmployees()
                .stream()
                .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                .orElse(new Employee());

        return employee.getSalary();
    }


    private void formatResult() {
        System.out.println();
        System.out.println("Result:");
    }
}
