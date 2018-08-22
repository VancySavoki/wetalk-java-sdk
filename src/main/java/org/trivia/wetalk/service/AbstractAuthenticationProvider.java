package org.trivia.wetalk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.rcp.RemoteAuthenticationException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;
import org.trivia.wetalk.SdkClient;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAuthenticationProvider implements RemoteAuthenticationManager, Support {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final long EXPIRES_IN = 86400 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationProvider.class);
    TokenService service;
    SdkClient client;

    public AbstractAuthenticationProvider(TokenService service) {
        this.service = service;
        this.client = service.getClient();
    }

    protected static boolean is(final String ua, final String token) {
        return ua.toLowerCase().indexOf(token) > 0;
    }

    protected Authentication attemptAuthenticate(String code, Object userIdKey) {
        final String accessToken = service.getAccessToken();
        final Map userInfo = client.userInfo(accessToken, code);
        if (userInfo.containsKey(userIdKey)) {
            final String username;
            final Map userDetail = client.userDetail(accessToken, (String) userInfo.get(userIdKey));

            if (!(userDetail.get("errmsg")).equals("ok")) {
                logger.error("login failed with code {}, {}", userDetail.get("errcode"), userDetail.get("errmsg"));
                return null;
            }
            if (afterAuthenticate(accessToken, userDetail)) return null;
            username = ((String) userDetail.get("email")).split("@")[0];
            return getUserAuthentication(username, userDetail);
        } else {
            logger.error("error:{}, description:{}", userInfo.get("errcode"), userInfo.get("errmsg"));
            return null;
        }

    }

    private boolean afterAuthenticate(String accessToken, Map userDetail) {
        return false;
    }

    private Authentication getUserAuthentication(String username, Map details) {
        final ExpiresAuthenticationToken authenticationToken
                = new ExpiresAuthenticationToken(username, "", attemptAuthentication(username, null));
        authenticationToken.setDetails(details);
        return authenticationToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> attemptAuthentication(String username, String ignore) throws RemoteAuthenticationException {
        final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (username.equals("admin")) {
            grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
            return grantedAuthorities;
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_USER));
            return grantedAuthorities;
        }
    }

    protected Authentication authenticate(Authentication authentication) throws AuthenticationException {
        assertNonNull(authentication);
        return authentication;
    }

    private void assertNonNull(Object authentication) {
        Assert.notNull(authentication, "authentication cannot be null");
    }

    class ExpiresAuthenticationToken extends UsernamePasswordAuthenticationToken {
        private long expires_in;

        public ExpiresAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> grantedAuthorities) {
            super(principal, credentials, grantedAuthorities);
            this.expires_in = Instant.now().toEpochMilli() + EXPIRES_IN;
        }

        public long getExpires_in() {
            return expires_in;
        }
    }
}
