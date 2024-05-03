package UserInteractor;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.function.Predicate;

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
    public String readLine(String promptingMessage, String remindMessage, Predicate<String> predicate) {
        String value;
        do {
            displayMessage(promptingMessage);
            value = scanner.nextLine();
            if (predicate.test(value)) {
                displayMessage(remindMessage + "\n");
            }
        } while (predicate.test(value));

        return value;
    }

    @Override
    public int readInt(String promptingMessage, String remindMessage, Predicate<Integer> predicate) {
        boolean isValid;
        String userInput;
        do {
            displayMessage(promptingMessage);
            userInput = scanner.nextLine().trim();
            isValid = isValidInt(userInput, predicate);

            if (!isValid) {
                displayMessage(remindMessage + "\n");
            }
        } while (!isValid);

        return Integer.parseInt(userInput);
    }

    @Override
    public long readLong(String promptingMessage, String remindMessage, Predicate<Long> predicate) {
        boolean isValid;
        String userInput;
        do {
            displayMessage(promptingMessage);
            userInput = scanner.nextLine().trim();
            isValid = isValidLong(userInput, predicate);

            if (!isValid) {
                displayMessage(remindMessage + "\n");
            }
        } while (!isValid);

        return Long.parseLong(userInput);
    }

    @Override
    public double readDouble(String promptingMessage, String remindMessage, Predicate<Double> predicate) {
        boolean isValid;
        String userInput;
        do {
            displayMessage(promptingMessage);
            userInput = scanner.nextLine().trim();
            isValid = isValidDouble(userInput, predicate);

            if (!isValid) {
                displayMessage(remindMessage + "\n");
            }
        } while (!isValid);

        return Double.parseDouble(userInput);
    }

    @Override
    public BigDecimal readBigDecimal(String promptingMessage, String remindMessage, Predicate<BigDecimal> predicate) {
        boolean isValid;
        String userInput;
        do {
            displayMessage(promptingMessage);
            userInput = scanner.nextLine().trim();
            isValid = isValidBigDecimal(userInput, predicate);

            if (!isValid) {
                displayMessage(remindMessage + "\n");
            }
        } while (!isValid);

        return BigDecimal.valueOf(Double.parseDouble(userInput));
    }

    private boolean isParsableToInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isParsableToLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInt(String userInput, Predicate<Integer> falseConditionPredicate) {
        if (!isParsableToInt(userInput)) {
            return false;
        }

        int value = Integer.parseInt(userInput);
        return !falseConditionPredicate.test(value);
    }

    private boolean isValidLong(String userInput, Predicate<Long> falseConditionPredicate) {
        if (!isParsableToLong(userInput)) {
            return false;
        }

        long value = Long.parseLong(userInput);
        return !falseConditionPredicate.test(value);
    }

    private boolean isParsableToDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDouble(String userInput, Predicate<Double> falseConditionPredicate) {
        if (!isParsableToDouble(userInput)) {
            return false;
        }

        double value = Double.parseDouble(userInput);
        return !falseConditionPredicate.test(value);
    }

    private boolean isParsableToBigDecimal(String str) {
        try {
            BigDecimal.valueOf(Double.parseDouble(str));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidBigDecimal(String userInput, Predicate<BigDecimal> falseConditionPredicate) {
        if (!isParsableToBigDecimal(userInput)) {
            return false;
        }

        BigDecimal value = BigDecimal.valueOf(Double.parseDouble(userInput));
        return !falseConditionPredicate.test(value);
    }
}
