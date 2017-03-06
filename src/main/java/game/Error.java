package game;

public class Error {
    public static final String EMPTY_LOGIN = "login is empty";
    public static final String EMPTY_PASSWORD = "password is empty";
    public static final String EMPTY_EMAIL = "email is empty";
    public static final String ERROR_LOGIN = "login is already exists";
    public static final String ERROR_SIGNUP = "wrong login or password";
    public static final String ERROR_SESSION = "session is not found";
    public static final String ERROR_USER = "user does not exists";
    public static final String ERROR_AUTH = "user is not auth";

    private String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
