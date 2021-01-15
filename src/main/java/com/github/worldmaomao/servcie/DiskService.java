package com.github.worldmaomao.servcie;

import com.github.worldmaomao.vo.DiskVo;
import com.github.worldmaomao.vo.DiskCreateVo;
import com.github.worldmaomao.vo.ServiceResultVo;

import java.io.IOException;
import java.util.List;

/**
 */
public interface DiskService {


    List<DiskVo> queryAll() throws IOException;

    ServiceResultVo delete(String diskId) throws IOException;

    ServiceResultVo create(DiskCreateVo vo) throws IOException;

}
