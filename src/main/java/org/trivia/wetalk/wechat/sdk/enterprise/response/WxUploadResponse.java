package org.trivia.wetalk.wechat.sdk.enterprise.response;

import org.trivia.wetalk.UploadResponseImpl;
import org.trivia.wetalk.wechat.sdk.enterprise.request.upload.WxUploadMediaType;

public class WxUploadResponse extends UploadResponseImpl<WxUploadMediaType> {

    @Override
    public WxUploadResponse setErrcode(int errcode) {
        super.setErrcode(errcode);
        return this;
    }

    @Override
    public WxUploadResponse setErrmsg(String errmsg) {
        super.setErrmsg(errmsg);
        return this;
    }
}
