package CustomExceptions;

public class NullOrEmptyStringException extends IllegalArgumentException {
    private static String templateMessage = " cannot be null or empty!";

    public NullOrEmptyStringException(String properyName) {
        super(properyName + templateMessage);
    }
}
