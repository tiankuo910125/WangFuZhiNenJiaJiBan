package com.demo.smarthome.communication.jsonbean;

/**
 * Created by wangdongyang on 17/2/9.
 */
public class OutModel_QRCode {

    private String status;
    private String message;
    private int code;
    private QRCode result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public QRCode getResult() {
        return result;
    }

    public void setResult(QRCode result) {
        this.result = result;
    }
}
