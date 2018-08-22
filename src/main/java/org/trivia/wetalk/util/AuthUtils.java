package org.trivia.wetalk.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

public final class AuthUtils {

    private AuthUtils() {
    }
    public static Authentication getAuthentication(ModelMap modelMap) {
        return modelMap.containsAttribute(SPRING_SECURITY_CONTEXT_KEY) ?
                ((SecurityContext) modelMap.get(SPRING_SECURITY_CONTEXT_KEY)).getAuthentication() :
                SecurityContextHolder.getContext().getAuthentication();
    }
    public static void attemptAuthenticate(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
