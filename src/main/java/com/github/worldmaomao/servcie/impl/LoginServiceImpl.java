package com.github.worldmaomao.servcie.impl;

import com.alibaba.fastjson.JSON;
import com.github.worldmaomao.config.Config;
import com.github.worldmaomao.http.HttpUtil;
import com.github.worldmaomao.vo.LoginVo;
import com.github.worldmaomao.excpetion.ResponseErrorException;
import com.github.worldmaomao.servcie.LoginService;
import com.github.worldmaomao.vo.ResponseVo;
import lombok.Builder;

import java.io.IOException;

/**
 */
@Builder
public class LoginServiceImpl implements LoginService {

    private static final String LOGIN_URL = "/api/v1/login";

    private Config config;

    public LoginServiceImpl(Config config) {
        this.config = config;
    }


    public String login(LoginVo loginVo) throws IOException {
        String url = String.format(config.getServerUrl() + LOGIN_URL);
        ResponseVo response = new HttpUtil().post(url, JSON.toJSONBytes(loginVo));
        if (response.getCode() == 0) {
            return (String) response.getData();
        } else {
            throw new ResponseErrorException(response.getMessage());
        }
    }
}
