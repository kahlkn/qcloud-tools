package store.code.extension.exmail.way1.common.bean;

public abstract class BaseResult {
    private String errmsg;
    private Long errcode;

    public Long getErrcode() {

        return errcode;
    }

    public void setErrcode(Long errcode) {

        this.errcode = errcode;
    }

    public String getErrmsg() {

        return errmsg;
    }

    public void setErrmsg(String errmsg) {

        this.errmsg = errmsg;
    }

}
