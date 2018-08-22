package org.trivia.wetalk.service;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public interface Support {
    boolean support(String ua);

    PreAuthenticatedAuthenticationToken getPreAuthenticationToken(String code);
}
