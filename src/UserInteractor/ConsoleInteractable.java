package UserInteractor;

import java.math.BigDecimal;
import java.util.function.Predicate;

public interface ConsoleInteractable extends Interactable {
    void displayMessage(String message);

    String readLine(String promptingMessage);

    String readLine(String promptingMessage, String remindMessage, Predicate<String> predicate);

    int readInt(String promptingMessage, String remindMessage, Predicate<Integer> predicate);

    double readDouble(String promptingMessage, String remindMessage, Predicate<Double> predicate);

    BigDecimal readBigDecimal(String promptingMessage, String remindMessage, Predicate<BigDecimal> predicate);
}
