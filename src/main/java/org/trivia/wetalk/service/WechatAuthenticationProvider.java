package org.trivia.wetalk.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class WechatAuthenticationProvider extends AbstractAuthenticationProvider implements AuthenticationProvider {

    public WechatAuthenticationProvider(QyWechatService qyWechatService) {
        super(qyWechatService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatPreAuthenticationToken auth = (WechatPreAuthenticationToken) super.authenticate(authentication);
        return attemptAuthenticate(auth.getName(), "UserId");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatPreAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public boolean support(String ua) {
        return is(ua, "micromessenger");
    }

    @Override
    public PreAuthenticatedAuthenticationToken getPreAuthenticationToken(String code) {
        return new WechatPreAuthenticationToken(code, null);
    }

    static class WechatPreAuthenticationToken extends PreAuthenticatedAuthenticationToken {
        public WechatPreAuthenticationToken(Object aPrincipal, Object aCredentials) {
            super(aPrincipal, aCredentials);
        }
    }
}
