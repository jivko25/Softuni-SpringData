import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class IncreseMinionsAge {
    public static void IncreaseMinionsAge() throws SQLException {
        Connection connection = SetUp.databaseSetUp();
        Scanner sc = new Scanner(System.in);
        String [] ids = sc.nextLine().split(" ");
        for (String id : ids) {
            PreparedStatement preparedStatement = connection.prepareStatement("update minions_db.minions\n" +
                    "set minions.name = LOWER(minions.name),\n" +
                    "    minions.age = minions.age + 1\n" +
                    "where minions.id = ?;");
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.execute();
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT minions_db.minions.name, minions_db.minions.age FROM minions_db.minions ORDER BY minions.id;");

        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            System.out.printf("%s %s\n", res.getString(1), res.getInt(2));
        }

    }
}
