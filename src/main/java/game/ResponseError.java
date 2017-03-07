package game;

public class ResponseError {
    public static final ResponseError EMPTY_LOGIN = new ResponseError("login is empty");
    public static final ResponseError EMPTY_PASSWORD = new ResponseError("password is empty");
    public static final ResponseError EMPTY_EMAIL = new ResponseError("email is empty");
    public static final ResponseError ERROR_LOGIN = new ResponseError("login is already exists");
    public static final ResponseError ERROR_SIGNUP = new ResponseError("wrong login or password");
    public static final ResponseError ERROR_SESSION = new ResponseError("session is not found");
    public static final ResponseError ERROR_USER = new ResponseError("user does not exists");
    public static final ResponseError ERROR_AUTH = new ResponseError("user is not auth");

    private String error;

    public ResponseError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
