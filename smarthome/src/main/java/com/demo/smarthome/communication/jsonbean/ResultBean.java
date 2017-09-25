package com.demo.smarthome.communication.jsonbean;

/**
 * Created by liukun on 2016/3/29.
 */
public class ResultBean {
    private String status;
    private String message;
    private Integer code;
    private Object result;
    private Object request;

    public void setStatus(String n)
    {
        this.status = n;
    }
    public void setMessage(String rm)
    {
        this.message = rm;
    }
    public void setCode(Integer t)
    {
        this.code = t;
    }
    public void setResult(Object r)
    {
        this.result = r;
    }
    public void setRequest(Object r)
    {
        this.request = r;
    }

    public String getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public Integer getCode(){return this.code;}
    public Object getResult(){return this.result;}
    public Object getRequest(){return this.request;}

    @Override
    public String toString() {
        return "ResultBean{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", result=" + result +
                ", request=" + request +
                '}';
    }
}

