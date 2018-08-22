package org.trivia.wetalk.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trivia.wetalk.Response;
import org.trivia.wetalk.SdkClient;
import org.trivia.wetalk.UploadMediaType;
import org.trivia.wetalk.UploadResponse;
import org.trivia.wetalk.wechat.sdk.enterprise.request.message.WxMessage;

import java.util.Map;


@FeignClient(url = "https://oapi.dingtalk.com", name = Dingtalk.PREFIX)
public interface Dingtalk extends SdkClient<UploadMediaType, WxMessage, MultipartFile> {
    String PREFIX = "dingtalk";
    String BASE_PATH = "";

    @RequestMapping(value = BASE_PATH + "/gettoken", method = RequestMethod.GET)
    Map token(@RequestParam("corpid") String corpId, @RequestParam("corpsecret") String corpSecret);

    @RequestMapping(value = BASE_PATH + "/user/getuserinfo", method = RequestMethod.GET)
    Map userInfo(@RequestParam("access_token") String accessToken, @RequestParam("code") String code);

    @RequestMapping(value = BASE_PATH + "/user/get", method = RequestMethod.GET)
    Map userDetail(@RequestParam("access_token") String accessToken, @RequestParam("userid") String userid);

    @RequestMapping(value = BASE_PATH + "/department/get", method = RequestMethod.GET)
    Map department(@RequestParam("access_token") String accessToken, @RequestParam("id") int departmentId);

    @RequestMapping(value = BASE_PATH + "/topapi/message/corpconversation/asyncsend_v2", method = RequestMethod.POST)
    Response sendMessage(@RequestParam("access_token") String accessToken, @RequestBody WxMessage wxmessage);

    @RequestMapping(
            value = BASE_PATH + "/media/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UploadResponse uploadFile(@RequestParam("access_token") String accessToken, @RequestParam("type") UploadMediaType mediaType, @RequestPart("media") MultipartFile media);

}
