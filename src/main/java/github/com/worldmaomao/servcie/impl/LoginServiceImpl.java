package github.com.worldmaomao.servcie.impl;

import com.alibaba.fastjson.JSON;
import github.com.worldmaomao.config.Config;
import github.com.worldmaomao.excpetion.ResponseErrorException;
import github.com.worldmaomao.http.HttpUtil;
import github.com.worldmaomao.servcie.LoginService;
import github.com.worldmaomao.vo.LoginVo;
import github.com.worldmaomao.vo.ResponseVo;
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
