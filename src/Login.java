public class Login {
    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        if (password.equals("secret")) {
            return true;
        }
        return false;
    }
}
