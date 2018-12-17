package component;

import main.Config;

public class Login {
    public static boolean authenticate(String password) {
        return Config.isPasswordCorrect(password);
    }
}
