package com.hundsun.web.controller;


import org.trivia.wetalk.util.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.trivia.wetalk.client.Dingtalk;
import org.trivia.wetalk.client.QyWechat;
import org.trivia.wetalk.service.*;

import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;



@Controller
public class AuthController implements ErrorViewResolver {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private ProviderManager providerManager;
    private String API_BASEPATH = '/api';
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client." + QyWechat.PREFIX)
    public AuthorizationCodeResourceDetails wechatClient() {
        return new AuthorizationCodeResourceDetails();
    }
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client." + Dingtalk.PREFIX)
    public AuthorizationCodeResourceDetails dingtalkClient() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public ProviderManager providerManager(
            QyWechat qyWechatFeignClient, Dingtalk dingtalk,
            AuthorizationCodeResourceDetails wechatClient,
            AuthorizationCodeResourceDetails dingtalkClient
    ) {
        return new ProviderManager(Arrays.asList(
                new DingtalkAuthenticationProvider(dingtalkService(dingtalk, dingtalkClient)),
                new WechatAuthenticationProvider(wechatService(qyWechatFeignClient, wechatClient))
        ));
    }

    private DingtalkService dingtalkService(Dingtalk dingtalk, AuthorizationCodeResourceDetails dingtalkClient) {
        return new DingtalkService(dingtalk, dingtalkClient);
    }
    private QyWechatService wechatService(QyWechat qyWechat, AuthorizationCodeResourceDetails wechatClient) {
        return new QyWechatService(qyWechat, wechatClient);
    }
    public AuthController() {
    }

    /**
     *
     * @param modelMap
     * @param ua user-agent
     * @param code oauth2 authorization_code
     * @param state oauth2 state
     * @return view name
     * @throws UnavailableException
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.GET, produces = MimeTypeUtils.TEXT_HTML_VALUE)
    public String index(
                       @ModelAttribute ModelMap modelMap,
                       @RequestHeader(HttpHeaders.USER_AGENT) String ua,
                       @RequestParam(required = false) String code,
                       @RequestParam(required = false) URL state) throws UnavailableException {

        Authentication auth = userInfo(modelMap);
        if (auth == null
                || auth.getPrincipal() == "anonymousUser"
                || AnonymousAuthenticationToken.class.isAssignableFrom(auth.getClass())) {
           if (code != null && !code.isEmpty()) {
               try {
                   if (authenticate(code, ua)) {
                       logger.info(ua);
                       return "redirect:" + state.toString();
                   }
               } catch (Exception ex) {
                   logger.error(ua);
                   throw new RequestRejectedException("service unavailable");
               }
           }
        }
        return "index";
    }

    private boolean authenticate(String code, String ua) {
        Authentication auth = null;
        for (AuthenticationProvider provider: providerManager.getProviders()) {
            final Support supporter = (Support) provider;
            if (supporter.support(ua)) {
                auth = supporter.getPreAuthenticationToken(code);
                break;
            }
        }
        AuthUtils.attemptAuthenticate(providerManager.authenticate(auth));
        return true;
    }

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        if (status.value() == 404) {
            return new ModelAndView("forward:/", HttpStatus.OK);
        } else {
            return new ModelAndView("error", model);
        }
    }

}