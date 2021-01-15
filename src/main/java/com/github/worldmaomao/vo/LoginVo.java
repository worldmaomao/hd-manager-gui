package com.github.worldmaomao.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录信息
 *
 */
@Data
@Builder
public class LoginVo {
    private String username;
    private String password;
}
