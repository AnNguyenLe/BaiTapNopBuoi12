package UserInteractor.Console;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleInteractor implements ConsoleInteractable {

    private Scanner scanner;

    public ConsoleInteractor(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void displayMessage(String message) {
        System.out.print(message);
    }

    @Override
    public String readLine(String promptingMessage) {
        displayMessage(promptingMessage);
        return scanner.nextLine();
    }

    @Override
    public int readInt(String promptingMessage) {
        displayMessage(promptingMessage);
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    @Override
    public double readDouble(String promptingMessage) {
        displayMessage(promptingMessage);
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    @Override
    public BigDecimal readBigDecimal(String promptingMessage) {
        displayMessage(promptingMessage);
        BigDecimal value = scanner.nextBigDecimal();
        scanner.nextLine();
        return value;
    }


}
