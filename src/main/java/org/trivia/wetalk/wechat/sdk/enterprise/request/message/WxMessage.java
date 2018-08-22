package org.trivia.wetalk.wechat.sdk.enterprise.request.message;

public interface WxMessage {
    WxMessage setToUser(String[] users);

    WxMessage setToParty(String[] parties);

    WxMessage setMsgtype(WxMessageType type);

    WxMessage setAgentid(int agentid);

    WxMessage setToTag(String[] tags);
}