import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ChangeTownNamesCasing {
    public static void main(String[] args) throws SQLException {
        Connection connection = SetUp.databaseSetUp();
        Scanner sc = new Scanner(System.in);

        String targetCountry = sc.nextLine();

        PreparedStatement coutEffectedRows = connection.prepareStatement("SELECT COUNT(minions_db.towns.name) FROM minions_db.towns WHERE country = ?;");
        PreparedStatement updateRowsStatement = connection.prepareStatement("UPDATE minions_db.towns SET name = UPPER(name) WHERE country = ?;");
        PreparedStatement selectStatement = connection.prepareStatement("SELECT minions_db.towns.name FROM minions_db.towns WHERE country = ?;");

        coutEffectedRows.setString(1, targetCountry);
        updateRowsStatement.setString(1, targetCountry);
        selectStatement.setString(1, targetCountry);

        ResultSet countRes = coutEffectedRows.executeQuery();
        updateRowsStatement.execute();
        ResultSet selectRes = selectStatement.executeQuery();

        countRes.next();
        if(countRes.getInt(1) == 0){
            System.out.println("No town names were affected.");
        } else {
            selectRes.next();
            System.out.printf("%d town names were affected\n", countRes.getInt(1));
            System.out.printf("[%s", selectRes.getString(1));
            while (selectRes.next()){
                System.out.printf(", %s", selectRes.getString(1));
            }
            System.out.print("]");
        }
    }
}
