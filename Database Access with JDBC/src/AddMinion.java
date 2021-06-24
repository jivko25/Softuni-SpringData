import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddMinion {

        public static Scanner sc = new Scanner(System.in);
        public static Connection connection;

    public static void main(String[] args) throws SQLException {

        connection = SetUp.databaseSetUp();

        PreparedStatement prep = connection.prepareStatement("INSERT INTO minions_db.minions (name, age, town_id)\n" +
                "VALUES (?, ?, (SELECT minions_db.towns.id FROM minions_db.towns WHERE towns.name = ?));");

        String [] inputArr = sc.nextLine().split(" ");
        prep.setString(1, inputArr[1]);
        prep.setInt(2, Integer.parseInt(inputArr[2]));
        prep.setString(3, inputArr[3]);

        prep.execute();

        PreparedStatement prepV = connection.prepareStatement("INSERT INTO minions_db.villains (name)\n" +
                "VALUES (?);");

        String [] inputArrV = sc.nextLine().split(" ");
        prepV.setString(1, inputArrV[1]);

        prepV.execute();

        PreparedStatement prepRelation = connection.prepareStatement("INSERT INTO minions_db.minions_villains (minion_id, villain_id)\n" +
                "VALUES ((SELECT minions_db.minions.id FROM minions_db.minions WHERE name = ?), (SELECT minions_db.villains.id FROM minions_db.villains WHERE name = ?));");

        prepRelation.setString(1, inputArr[1]);
        prepRelation.setString(2, inputArrV[1]);

        System.out.printf("Villain %s was added to the database.\n", inputArrV[1]);
        System.out.printf("Successfully added %s to be minion of %s", inputArr[1], inputArrV[1]);

    }
}
