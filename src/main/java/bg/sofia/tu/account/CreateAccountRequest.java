package bg.sofia.tu.account;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 26/08/2016 @ 11:49.
 */
public class CreateAccountRequest {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreateAccountRequest [");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
