package CustomExceptions;

public class NegativeNumberException extends IllegalArgumentException {
    private static String templateMessage = " cannot be a negative number!";

    public NegativeNumberException(String propertyName) {
        super(propertyName + templateMessage);
    }
}
