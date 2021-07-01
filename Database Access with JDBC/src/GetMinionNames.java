import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GetMinionNames {
    public static void GetMinionNames() throws SQLException {
        Scanner sc = new Scanner(System.in);
        Connection connection = SetUp.databaseSetUp();
        int villainID = Integer.parseInt(sc.nextLine());

        PreparedStatement getVilllainName = connection.prepareStatement("Select minions_db.villains.name FROM minions_db.villains\n" +
                "WHERE minions_db.villains.id = ?;");

        getVilllainName.setInt(1,villainID);

        PreparedStatement prep = connection.prepareStatement("SELECT m.name, m.age FROM minions_db.villains v\n" +
                "JOIN minions_db.minions_villains mv on v.id = mv.villain_id\n" +
                "JOIN minions_db.minions m on m.id = mv.minion_id\n" +
                "WHERE villain_id = ?;");
        prep.setInt(1,villainID);

        ResultSet rs = prep.executeQuery();
        ResultSet rsName = getVilllainName.executeQuery();

        if(rs.next() == false){
            System.out.printf("No villain with ID %d exists in the database.", villainID);
        }
        else {
        rsName.next();
            System.out.printf("Villain: %s\n", rsName.getString("name"));
            int n = 1;
            while (rs.next()) {
                System.out.printf("%d. %s %d\n", n, rs.getString("name"), rs.getInt("age"));
                n++;
            }
        }
    }
}
