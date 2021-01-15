package com.github.worldmaomao.vo;

import lombok.Data;

/**
 */
@Data
public class QueryDiskItemRequestVo {

    private int page;
    private int pageSize;
    private String diskId;
    private String fileName;
}
