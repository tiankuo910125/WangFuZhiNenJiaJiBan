package aprivate.oo.gizwitopenapi.requestbody;

/**
 * Created by zhuxiaolong on 2017/8/31.
 */

public class LoginBody {

    /**
     * username : string
     * password : string
     * lang : en
     */

    private String username;
    private String password;
    private String lang="en";

    public LoginBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
