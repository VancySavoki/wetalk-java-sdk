package org.trivia.wetalk.service;

import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.trivia.wetalk.client.Dingtalk;

public class DingtalkService extends TokenServiceImpl {
    public DingtalkService(Dingtalk client, AuthorizationCodeResourceDetails clientDetails) {
        super(client, clientDetails);
    }

}
