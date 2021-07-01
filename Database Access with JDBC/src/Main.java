import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int numberOfTask = Integer.parseInt(sc.nextLine());
        switch (numberOfTask){
            case 2:
                GetVillainsNames.GetVillainsNames();
                break;
            case 3:
                GetMinionNames.GetMinionNames();
                break;
            case 4:
                AddMinion.AddMinion();
                break;
            case 5:
                ChangeTownNamesCasing.ChangeTownNamesCasing();
                break;
            case 7:
                PrintAllMinionsAge.PrintAllMinionsAge();
                break;
            case 8:
                IncreseMinionsAge.IncreaseMinionsAge();
                break;
        }
    }
}
