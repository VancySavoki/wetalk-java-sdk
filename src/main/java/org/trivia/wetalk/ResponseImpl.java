package org.trivia.wetalk;

public class ResponseImpl implements Response {
    private int errcode;
    private String errmsg;

    @Override
    public int getErrcode() {
        return errcode;
    }

    public ResponseImpl setErrcode(int errcode) {
        this.errcode = errcode;
        return this;
    }

    @Override
    public String getErrmsg() {
        return errmsg;
    }

    public ResponseImpl setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }
}
