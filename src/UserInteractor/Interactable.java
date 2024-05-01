package UserInteractor;

import java.math.BigDecimal;

public interface Interactable {
    void displayMessage(String message);

    String readLine(String promptingMessage);

    int readInt(String promptingMessage);

    double readDouble(String promptingMessage);

    BigDecimal readBigDecimal(String promptingMessage);
}
