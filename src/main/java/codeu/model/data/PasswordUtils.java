package codeu.model.data;

/** small class that manages password restrictions **/
public class PasswordUtils {

    public PasswordUtils() {
    }

    public boolean isPasswordCorrect(String password){
        if(password.length()>=5&&password.length()<=13 && password.matches("(.*[a-z].*)")&& password.matches( "(.*[0-9].*)")){
            return true;
        }
        return false;
    }


}
