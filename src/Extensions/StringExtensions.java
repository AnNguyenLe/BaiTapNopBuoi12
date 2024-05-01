package Extensions;

public class StringExtensions {
    public static boolean isNullOrEmpty(String text){
       return text == null || text.trim().isEmpty();
    }
}
