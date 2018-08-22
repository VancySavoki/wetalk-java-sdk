package org.trivia.wetalk.service;

import org.trivia.wetalk.SdkClient;

import java.util.Map;

public interface TokenService {
    Map getTokenMap();

    String getAccessToken();

    SdkClient getClient();
}
