package codeu.model.data;

import java.util.regex.Pattern;

/** small class that manages password restrictions **/
public class PasswordUtils {

    private PasswordUtils() {
    }

    private static final Pattern CONTAINS_ALPHA = Pattern.compile("(.*[a-z].*)");
    private static final Pattern CONTAINS_DIGIT = Pattern.compile("(.*[0-9].*)");


    public static boolean isPasswordCorrect(String password){
        if (password == null) {
            return false;
        }

        if(password.length()>=5&&password.length()<=13 && CONTAINS_ALPHA.matcher(password).matches() && CONTAINS_DIGIT.matcher(password).matches()){
            return true;
        }
        return false;
    }


}
