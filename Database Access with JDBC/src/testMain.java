import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class testMain {
    public static void main(String[] args) throws SQLException, IOException {

        Scanner scanner = new Scanner(System.in);
        testHW homework = new testHW();
        homework.setConnection("root", "EnigmA59612530");


        int exNumber = Integer.parseInt(scanner.nextLine());

        switch (exNumber) {
            case 2:
                homework.getVillainsNamesEx2();
            case 3:
                homework.getMinionNamesEx3();
            case 4:
                homework.addMinionEx4();
            case 5:
                homework.changeTownNamesCasingEx5();
            case 6:
                homework.removeVillainEx6();
            case 7:
                homework.printMinionNamesEx7();
            case 8:
                homework.increaseMinionsAgeEx8();
            case 9:
                homework.increaseAgeStoredProcedure();
        }


    }
}
