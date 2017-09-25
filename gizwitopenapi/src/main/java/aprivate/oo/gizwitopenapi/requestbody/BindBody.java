package aprivate.oo.gizwitopenapi.requestbody;

/**
 * Created by WZH on 2017/9/20.
 */

public class BindBody {


    /**
     * product_key : string
     * mac : string
     * remark : string
     * dev_alias : string
     * set_owner : 0
     */

    private String product_key;
    private String mac;
    private String dev_alias;

    public BindBody(String product_key, String mac, String dev_alias) {
        this.product_key = product_key;
        this.mac = mac;
        this.dev_alias = dev_alias;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDev_alias() {
        return dev_alias;
    }

    public void setDev_alias(String dev_alias) {
        this.dev_alias = dev_alias;
    }

    @Override
    public String toString() {
        return "BindBody{" +
                "product_key='" + product_key + '\'' +
                ", mac='" + mac + '\'' +
                ", dev_alias='" + dev_alias + '\'' +
                '}';
    }
}
