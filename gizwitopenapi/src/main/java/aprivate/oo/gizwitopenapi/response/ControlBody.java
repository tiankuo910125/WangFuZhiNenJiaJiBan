package aprivate.oo.gizwitopenapi.response;

/**
 * Created by zhuxiaolong on 2017/9/1.
 */

public class ControlBody {

    /**
     * attrs : {"temp":30}
     */

    private AttrsBean attrs;

    public AttrsBean getAttrs() {
        return attrs;
    }

    public void setAttrs(AttrsBean attrs) {
        this.attrs = attrs;
    }

    public static class AttrsBean {
        /**
         * temp : 30
         */

        private int temp;

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }
    }
}
