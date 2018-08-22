package org.trivia.wetalk.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class DingtalkAuthenticationProvider extends AbstractAuthenticationProvider implements AuthenticationProvider {

    public DingtalkAuthenticationProvider(DingtalkService dingtalkService) {
        super(dingtalkService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DingtalkPreAuthenticationToken auth = (DingtalkPreAuthenticationToken) super.authenticate(authentication);
        return attemptAuthenticate(auth.getName(), "userid");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DingtalkPreAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public boolean support(String ua) {
        return is(ua, "dingtalk");
    }

    @Override
    public PreAuthenticatedAuthenticationToken getPreAuthenticationToken(String code) {
        return new DingtalkPreAuthenticationToken(code, null);
    }

    static class DingtalkPreAuthenticationToken extends PreAuthenticatedAuthenticationToken {
        public DingtalkPreAuthenticationToken(Object aPrincipal, Object aCredentials) {
            super(aPrincipal, aCredentials);
        }
    }
}
