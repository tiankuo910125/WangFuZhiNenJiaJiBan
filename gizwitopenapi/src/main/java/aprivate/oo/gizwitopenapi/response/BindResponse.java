package aprivate.oo.gizwitopenapi.response;

import java.util.List;

/**
 * Created by WZH on 2017/9/20.
 */

public class BindResponse {


    /**
     * remark :
     * ws_port : 8080
     * did : B455xg7yvRdtFLv9mYRbr4
     * port_s : 8883
     * is_disabled : false
     * mac : b827eb2ed586
     * product_key : 67087b99edf04191b8f0282d26036185
     * port : 1883
     * host : sandbox.gizwits.com
     * role : normal
     * dev_alias : wzh
     * is_online : false
     * passcode : IICICJLCKE
     * dev_label : []
     * type : normal
     * wss_port : 8880
     */

    private String remark;
    private int ws_port;
    private String did;
    private int port_s;
    private boolean is_disabled;
    private String mac;
    private String product_key;
    private int port;
    private String host;
    private String role;
    private String dev_alias;
    private boolean is_online;
    private String passcode;
    private String type;
    private int wss_port;
    private List<?> dev_label;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getWs_port() {
        return ws_port;
    }

    public void setWs_port(int ws_port) {
        this.ws_port = ws_port;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public int getPort_s() {
        return port_s;
    }

    public void setPort_s(int port_s) {
        this.port_s = port_s;
    }

    public boolean isIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDev_alias() {
        return dev_alias;
    }

    public void setDev_alias(String dev_alias) {
        this.dev_alias = dev_alias;
    }

    public boolean isIs_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWss_port() {
        return wss_port;
    }

    public void setWss_port(int wss_port) {
        this.wss_port = wss_port;
    }

    public List<?> getDev_label() {
        return dev_label;
    }

    public void setDev_label(List<?> dev_label) {
        this.dev_label = dev_label;
    }
}
