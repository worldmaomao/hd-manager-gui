package com.github.worldmaomao.servcie;

import com.github.worldmaomao.vo.*;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface DiskItemService {


    QueryDiskItemResponseVo query(QueryDiskItemRequestVo vo) throws IOException;

    List<ImportDiskItemResultVo> batchImport(List<ImportDiskItemVo> itemVoList) throws IOException;

    List<DeleteDiskItemResultVo> batchDelete(List<String> diskItemIdList) throws IOException;


}
