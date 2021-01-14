package github.com.worldmaomao.servcie;

import github.com.worldmaomao.vo.DiskCreateVo;
import github.com.worldmaomao.vo.DiskVo;
import github.com.worldmaomao.vo.ServiceResultVo;

import java.io.IOException;
import java.util.List;

/**
 */
public interface DiskService {


    List<DiskVo> queryAll() throws IOException;

    ServiceResultVo delete(String diskId) throws IOException;

    ServiceResultVo create(DiskCreateVo vo) throws IOException;

}
