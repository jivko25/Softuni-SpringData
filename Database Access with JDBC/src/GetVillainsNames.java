import java.sql.Connection;
import java.util.Scanner;
import java.sql.*;

public class GetVillainsNames {
    public static void main(String[] args) throws SQLException{
        Connection connection = SetUp.databaseSetUp();

        PreparedStatement prep = connection.prepareStatement("SELECT villains.name, COUNT(DISTINCT mv.minion_id) AS `minion_count` FROM minions_db.villains\n" +
                "JOIN minions_db.minions_villains mv on villains.id = mv.villain_id\n" +
                "GROUP BY villains.name\n" +
                "HAVING minion_count > 15\n" +
                "ORDER BY minion_count DESC;");

        ResultSet rs = prep.executeQuery();

        while(rs.next()) {
            System.out.println(rs.getString("name") + " " + rs.getInt("minion_count"));
        }
    }

}
