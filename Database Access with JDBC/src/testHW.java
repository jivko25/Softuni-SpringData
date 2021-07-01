import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;


public class testHW {

    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";

    private static final String MINIONS_DATABASE = "minions_db";


    private Connection connection;

    public void setConnection(String user, String password) throws SQLException {


        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);


        connection = DriverManager.getConnection(CONNECTION_STRING + MINIONS_DATABASE, properties);


    }

    public void getVillainsNamesEx2() throws SQLException {


        String query = "SELECT v.name , COUNT(mv.minion_id) AS `count`\n" +
                "FROM villains AS v\n" +
                "JOIN minions_villains AS mv\n" +
                "ON mv.`villain_id` = v.`id`\n" +
                "GROUP BY v.id\n" +
                "HAVING `count` > 15\n" +
                "ORDER BY `count` DESC ";

        PreparedStatement statement = connection
                .prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n", resultSet.getString(1)
                    , resultSet.getInt(2));
        }

    }

    public void getMinionNamesEx3() throws IOException, SQLException {
        System.out.println("Enter Villain id");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        int villainId = Integer.parseInt(reader.readLine());


        String villainName = getEntityNameById(villainId, "villains");

        if (villainName == null) {
            System.out.printf("No villain with id %d exists in the database.", villainId);
            return;
        }

        System.out.printf("Villain: %s%n", villainName);

        String query = "SELECT m.`name` , m.`age` FROM minions AS m\n" +
                "JOIN `minions_villains` AS mv\n" +
                "ON mv.`minion_id` = m.`id`\n" +
                "WHERE mv.`villain_id` = ?;";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, villainId);

        ResultSet resultSet = statement.executeQuery();


        int counter = 0;

        while (resultSet.next()) {
            System.out.printf("%d. %s %d%n", ++counter,
                    resultSet.getString("name")
                    , resultSet.getInt("age"));
        }


    }


    private String getEntityNameById(int entityId, String tableName) throws SQLException {

        String query = String.format("SELECT name FROM %s WHERE id = ?", tableName);

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, entityId);

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next() ? resultSet.getString("name") : null;
    }

    public void addMinionEx4() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter Minion: name , age , town");
        System.out.println("Enter Villain: name");
        String[] minionsInfo = scanner.nextLine().split("\\s+");

        String minionName = minionsInfo[0];
        int age = Integer.parseInt(minionsInfo[1]);
        String townName = minionsInfo[2];


        int townId = getEntityIdByName(townName, "towns");

        if (townId < 0) {
            String query = "INSERT INTO towns(name) value(?)";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, townName);
            statement.execute();

            System.out.printf("Town %s was added to the database.", townName);
        }

        String[] villainsInfo = scanner.nextLine().split("\\s+");

        String villainName = villainsInfo[0];

        int villainId = getEntityIdByName(villainName, "villains");

        if (villainId < 0) {
            String query = "INSERT INTO villains(name) value(?)";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, villainName);
            statement.execute();

            System.out.printf("Villain %s was added to the database.", villainName);

            System.out.println();
        }

        int minionsId = getEntityIdByName(minionName, "minions");

        String query = "INSERT INTO minions_villains (minion_id , villain_id) VALUES (? , ? )";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, minionsId);
        statement.setInt(2, villainId);


        statement.execute();

        System.out.printf("Successfully added %s to be minion of %s", minionName, villainName);
    }

    private int getEntityIdByName(String entityName, String tableName) throws SQLException {

        String query = String.format("SELECT id FROM %s WHERE name = ?", tableName);

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, entityName);

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next() ? resultSet.getInt(1) : -1;
    }

    public void changeTownNamesCasingEx5() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter country");
        String country = scanner.nextLine();

        String query = "UPDATE towns SET name = UPPER(name) WHERE country = ?;";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, country);

        int townsAffected = statement.executeUpdate();

        System.out.printf("%d town names were affected.%n", townsAffected);

        PreparedStatement preparedStatementNames =
                connection.prepareStatement("SELECT name FROM towns WHERE country = ?");

        preparedStatementNames.setString(1, country);

        ResultSet resultSet = preparedStatementNames.executeQuery();

        while (resultSet.next()) {
            System.out.print(resultSet.getString("name") + ", ");
        }

    }


    public void removeVillainEx6() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter id");

        int id = Integer.parseInt(scanner.nextLine());

        int deleteMinions = deleteMinions(id);

        String villainName = getEntityNameById(id, "villains");

        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM villains WHERE id = ?");

        preparedStatement.setInt(1, id);

        preparedStatement.executeUpdate();

        System.out.printf("%s was deleted\n" +
                "%d minions released\n", villainName, deleteMinions);


    }

    private int deleteMinions(int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?");

        statement.setInt(1, id);

        return statement.executeUpdate();


    }


    public void printMinionNamesEx7() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM minions");

        ResultSet resultSet = preparedStatement.executeQuery();


        ArrayDeque<String> names = new ArrayDeque<String>();

        while (resultSet.next()) {
            names.add(resultSet.getString(1));

        }

        while (names.size() > 0) {
            System.out.println(names.pollFirst());
            System.out.println(names.pollLast());

        }


    }

    public void increaseMinionsAgeEx8() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter id");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `name` , `age` FROM minions");

        preparedStatement.executeQuery();

        String[] input = scanner.nextLine().split(" ");

        for (int i = 0; i < input.length; i++) {
            int current = Integer.parseInt(input[i]);


            PreparedStatement statement = connection.prepareStatement("UPDATE `minions` SET `name` = lower(`name`) WHERE `id` = ?");

            statement.setInt(1, current);

            statement.execute();


            PreparedStatement statementAge = connection.prepareStatement("UPDATE `minions` SET `age` = `age` + 1 WHERE `id` = ?");

            statementAge.setInt(1, current);
            statementAge.execute();


        }

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n", resultSet.getString(1), resultSet.getInt(2));
        }
    }

    public void increaseAgeStoredProcedure() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        int minionId = Integer.parseInt(scanner.nextLine());

        String query = "CALL usp_get_older(?)";

        CallableStatement callableStatement = connection.prepareCall(query);
        callableStatement.setInt(1, minionId);

        callableStatement.execute();

    }
}



