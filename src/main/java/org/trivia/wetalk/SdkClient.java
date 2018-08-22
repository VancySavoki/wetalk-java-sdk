package org.trivia.wetalk;

import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public interface SdkClient<MediaType, Message, File> {

    Map token(String corpId, String corpSecret);

    Map userInfo(String accessToken, String code);

    Map userDetail(String accessToken, String userid);

    Map department(String accessToken, int departmentId);

    Response sendMessage(String accessToken, Message message);

    UploadResponse uploadFile(String accessToken, MediaType mediaType, File media);
    @Bean
    default FormEncoder getEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}
