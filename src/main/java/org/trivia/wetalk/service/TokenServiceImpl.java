package org.trivia.wetalk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.trivia.wetalk.SdkClient;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class TokenServiceImpl implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final ScheduledExecutorService executors = Executors.newScheduledThreadPool(1);
    private SdkClient client;
    private SoftReference<Map> accessTokenRefer;
    private AuthorizationCodeResourceDetails clientDetails;

    public TokenServiceImpl(SdkClient client, AuthorizationCodeResourceDetails clientDetails) {
        this.client = client;
        this.clientDetails = clientDetails;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executors.shutdownNow();
        }));
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            logger.info("run scheduled task");
            try {
                initAccessTokenRefer();
            } catch (Exception e) {
                logger.error("run scheduled task {}", e.getCause());
            }

        }
    }

    @Override
    public Map getTokenMap() {
        Map map;
        if (accessTokenRefer == null) {
            return initAccessTokenRefer();
        } else {
            map = accessTokenRefer.get();
            if (isValid(map)) {
                return initAccessTokenRefer();
            } else {
                return map;
            }

        }
    }

    @Override
    public String getAccessToken() {
        Map token = getTokenMap();
        return (String) token.get("access_token");
    }

    @Override
    public SdkClient getClient() {
        return this.client;
    }

    private boolean isValid(Map map) {
        return map == null || map.containsKey("error") || !map.containsKey("expires_in");
    }

    private Map getToken() {
        return client.token(clientDetails.getClientId(), clientDetails.getClientSecret());
    }

    private synchronized Map initAccessTokenRefer() {
        Map map = null;
        for (int counter = 3; isValid(map) && counter >= 0; counter--, map = getToken()) ;
        accessTokenRefer = new SoftReference(map);
        executors.schedule(new Task(), map.containsKey("expires_in") ? Long.valueOf((Integer) map.get("expires_in")) / 2 : 3600, TimeUnit.SECONDS);
        return map;
    }


}
