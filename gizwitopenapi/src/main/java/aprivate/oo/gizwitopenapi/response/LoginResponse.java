package aprivate.oo.gizwitopenapi.response;

/**
 * Created by zhuxiaolong on 2017/8/31.
 */

public class LoginResponse {


    /**
     * uid : 29db4f0d806e451a84264ba3da64d9de
     * token : 86a0ee91548f4971832e371811702316
     * expire_at : 13894002020
     */

    private String uid;
    private String token;
    private long expire_at;

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

    public long getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(long expire_at) {
        this.expire_at = expire_at;
    }
}
