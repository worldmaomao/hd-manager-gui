package com.github.worldmaomao.disk;

import com.github.worldmaomao.vo.LocalDiskVo;

import java.util.List;

/**
 * 扫描本地磁盘
 */
public interface DiskScanner {
    List<LocalDiskVo> scan();
}
