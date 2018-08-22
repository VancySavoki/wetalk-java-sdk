package org.trivia.wetalk.wechat.sdk.enterprise.request.message;

public interface Textcard extends WxMessage {
    Textcard setTitle(String title);

    Textcard setDescription(String description);

    Textcard setUrl(String url);
}