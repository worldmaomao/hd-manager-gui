package com.github.worldmaomao.servcie;

import com.github.worldmaomao.vo.LoginVo;

import java.io.IOException;

/**
 */
public interface LoginService {

    String login(LoginVo loginVo) throws IOException;
}
