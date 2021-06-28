import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrintAllMinionsAge {
    public static void main(String[] args) throws SQLException {
        Connection connection = SetUp.databaseSetUp();

        int maxId = DatabaseSizeCounter(connection);
        int temp = maxId;

        for (int i = 1;i < maxId; i++){
            if(temp == i - 1){
                break;
            }
            System.out.println(getMinion(i, connection));
            System.out.println(getMinion(temp, connection));
            temp--;
        }

    }

    public static String getMinion(int id, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT minions_db.minions.name FROM minions_db.minions WHERE id = ?;");
        statement.setInt(1, id);
        ResultSet res = statement.executeQuery();
        res.next();
        return res.getString(1);
    }

    public static int DatabaseSizeCounter(Connection connection) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT minions_db.minions.id FROM minions_db.minions ORDER BY id DESC;");
        ResultSet res = statement.executeQuery();
        res.next();
        return res.getInt(1);
    }

}
