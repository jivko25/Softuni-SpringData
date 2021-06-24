import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class SetUp {
    public static final Scanner sc = new Scanner(System.in);
    public static Connection databaseSetUp() throws SQLException {
        Properties prob = new Properties();
        prob.setProperty("user", sc.nextLine());
        prob.setProperty("password", sc.nextLine());

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db",
                prob);

    }
}
