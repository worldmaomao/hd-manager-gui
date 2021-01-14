package github.com.worldmaomao.disk;

import github.com.worldmaomao.vo.LocalDiskVo;

import java.util.List;

/**
 * 扫描本地磁盘
 */
public interface DiskScanner {
    List<LocalDiskVo> scan();
}
