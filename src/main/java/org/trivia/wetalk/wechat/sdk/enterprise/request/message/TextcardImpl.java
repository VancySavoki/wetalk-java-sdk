package org.trivia.wetalk.wechat.sdk.enterprise.request.message;

import java.util.HashMap;
import java.util.Map;

public class TextcardImpl extends AbstractTextcard {

    private WxMessageType msgtype = WxMessageType.textcard;
    private Map textcard = new HashMap();

    public WxMessageType getMsgtype() {
        return msgtype;
    }

    public Map getTextcard() {
        return textcard;
    }

    @Override
    public TextcardImpl setTitle(String title) {
        textcard.put("title", title);
        return this;
    }

    @Override
    public TextcardImpl setDescription(String description) {
        textcard.put("description", description);
        return this;
    }

    @Override
    public TextcardImpl setUrl(String url) {
        textcard.put("url", url);
        return this;
    }
}