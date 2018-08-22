package org.trivia.wetalk.service;


import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.trivia.wetalk.client.QyWechat;

public class QyWechatService extends TokenServiceImpl {
    public QyWechatService(QyWechat client, AuthorizationCodeResourceDetails clientDetails) {
        super(client, clientDetails);
    }
}
