package com.github.worldmaomao.vo;

import lombok.Data;

/**
 */
@Data
public class ResponseVo {

    private int code;
    private String message;
    private Object data;
}
