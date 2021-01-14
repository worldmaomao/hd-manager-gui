package github.com.worldmaomao.servcie;

import github.com.worldmaomao.vo.LoginVo;

import java.io.IOException;

/**
 */
public interface LoginService {

    String login(LoginVo loginVo) throws IOException;
}
