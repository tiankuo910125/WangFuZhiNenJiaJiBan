package aprivate.oo.gizwitopenapi.response;

/**
 * Created by WZH on 2017/9/14.
 */

public class RegistResponse {

    /**
     * uid : string
     * token : string
     * expire_at : 0
     */

    private String uid;
    private String token;
    private int expire_at;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(int expire_at) {
        this.expire_at = expire_at;
    }
}
