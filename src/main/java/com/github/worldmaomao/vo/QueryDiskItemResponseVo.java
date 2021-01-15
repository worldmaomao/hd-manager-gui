package com.github.worldmaomao.vo;

import lombok.Data;

import java.util.List;

/**
 */
@Data
public class QueryDiskItemResponseVo {

    private int page;
    private int pageSize;
    private int totalCount;
    private List<DiskItemVo> list;
}
