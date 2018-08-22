package org.trivia.wetalk.wechat.sdk.enterprise.request.message;

import com.google.common.collect.Iterators;
import com.netflix.servo.util.Strings;

abstract class AbstractWxMessage implements WxMessage {
    private String touser;
    private String toparty;
    private String totag;
    private int agentid;

    public int getAgentid() {
        return agentid;
    }

    public String getTouser() {
        return touser;
    }

    @Override
    public WxMessage setToUser(String[] users) {
        this.touser = Strings.join("|", Iterators.forArray(users));
        return this;
    }

    @Override
    public WxMessage setToParty(String[] parties) {
        this.toparty = Strings.join("|", Iterators.forArray(parties));
        return this;
    }

    @Override
    public WxMessage setMsgtype(WxMessageType type) {
        return this;
    }

    @Override
    public WxMessage setAgentid(int agentid) {
        this.agentid = agentid;
        return this;
    }

    @Override
    public WxMessage setToTag(String[] tags) {
        this.totag = Strings.join("|", Iterators.forArray(tags));
        return this;
    }
}
