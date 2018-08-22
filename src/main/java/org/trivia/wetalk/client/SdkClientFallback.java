package org.trivia.wetalk.client;

import org.trivia.wetalk.*;

import java.util.HashMap;
import java.util.Map;

public abstract class SdkClientFallback implements SdkClient {

    @Override
    public Map token(String corpId, String corpSecret) {
        return getErrorFallback();
    }

    @Override
    public Map userInfo(String accessToken, String authCode) {
        return getErrorFallback();
    }

    @Override
    public Map userDetail(String accessToken, String userid) {
        return getErrorFallback();
    }

    @Override
    public Map department(String accessToken, int departmentId) {
        return getErrorFallback();
    }

    @Override
    public Response sendMessage(String accessToken, Object o) {
        return new ResponseImpl().setErrcode(-1).setErrmsg("unknown");
    }

    @Override
    public UploadResponse uploadFile(String accessToken, Object o, Object media) {
        return new UploadResponseImpl<>().setErrcode(-1).setErrmsg("unknown");
    }

    private Map getErrorFallback() {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", "an unexpected remote procedure call occurred");
        map.put("error_description", "invalid token");
        return map;
    }
}