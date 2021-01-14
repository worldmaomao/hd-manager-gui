package github.com.worldmaomao.disk;

import github.com.worldmaomao.vo.LocalDiskVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 在windows系统，扫描磁盘
 *
 */
public class WindowsDiskScanner implements DiskScanner {


    @Override
    public List<LocalDiskVo> scan() {
        List<LocalDiskVo> voList = new ArrayList<LocalDiskVo>();
        return voList;
    }

}
