public class User {
    private String login;
    private String password;
    private String permission;

    public User(String login, String password, String permission) {
        this.login = login;
        this.password = password;
        this.permission = permission;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isCanRead() {
        return this.permission.contains("R");
    }

    public boolean isCanAdd() {
        return this.permission.contains("A");
    }

    public boolean isCanEdit() {
        return this.permission.contains("E");
    }
}
