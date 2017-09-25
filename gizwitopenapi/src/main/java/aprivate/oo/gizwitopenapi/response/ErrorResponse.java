package aprivate.oo.gizwitopenapi.response;

/**
 * Created by zhuxiaolong on 2017/9/1.
 */

public class ErrorResponse {

    /**
     * error_message : appid invalid!
     * error_code : 9003
     * detail_message : null
     */

    private String error_message;
    private int error_code;
    private Object detail_message;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public Object getDetail_message() {
        return detail_message;
    }

    public void setDetail_message(Object detail_message) {
        this.detail_message = detail_message;
    }
}
