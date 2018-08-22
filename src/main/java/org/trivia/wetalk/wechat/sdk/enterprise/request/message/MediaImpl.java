package org.trivia.wetalk.wechat.sdk.enterprise.request.message;

import java.util.HashMap;
import java.util.Map;

public class MediaImpl extends AbstractMedia {
    private Map file = new HashMap();
    private WxMessageType msgtype = WxMessageType.file;

    public Map getFile() {
        return file;
    }

    public WxMessageType getMsgtype() {
        return msgtype;
    }

    @Override
    public MediaImpl setMsgtype(WxMessageType type) {
        return this;
    }

    @Override
    public MediaImpl setMediaId(String id) {
        this.file.put("media_id", id);
        return this;
    }
}