package aprivate.oo.gizwitopenapi.response;

import java.util.List;

/**
 * Created by zhuxiaolong on 2017/9/1.
 */

public class BindingList {

    private List<DevicesBean> devices;

    public List<DevicesBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DevicesBean> devices) {
        this.devices = devices;
    }

    public static class DevicesBean {
        /**
         * remark :
         * wifi_soft_version : 04020006
         * ws_port : 8080
         * did : L8tYTYsmeJePWFJwNyEjCu
         * port_s : 8883
         * is_disabled : false
         * host : sandbox.gizwits.com
         * product_key : f88edfe309694f8db7a66085e841dcc5
         * wss_port : 8880
         * mac : b827ebcd085d
         * role : normal
         * dev_alias :
         * is_sandbox : true
         * is_online : true
         * passcode : TPTCKVLYFE
         * dev_label : []
         * type : normal
         * port : 1883
         */

        private String remark;
        private String wifi_soft_version;
        private int ws_port;
        private String did;
        private int port_s;
        private boolean is_disabled;
        private String host;
        private String product_key;
        private int wss_port;
        private String mac;
        private String role;
        private String dev_alias;
        private boolean is_sandbox;
        private boolean is_online;
        private String passcode;
        private String type;
        private int port;
        private List<?> dev_label;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getWifi_soft_version() {
            return wifi_soft_version;
        }

        public void setWifi_soft_version(String wifi_soft_version) {
            this.wifi_soft_version = wifi_soft_version;
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

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getProduct_key() {
            return product_key;
        }

        public void setProduct_key(String product_key) {
            this.product_key = product_key;
        }

        public int getWss_port() {
            return wss_port;
        }

        public void setWss_port(int wss_port) {
            this.wss_port = wss_port;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
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

        public boolean isIs_sandbox() {
            return is_sandbox;
        }

        public void setIs_sandbox(boolean is_sandbox) {
            this.is_sandbox = is_sandbox;
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

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public List<?> getDev_label() {
            return dev_label;
        }

        public void setDev_label(List<?> dev_label) {
            this.dev_label = dev_label;
        }
    }
}
