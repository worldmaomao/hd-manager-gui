package com.github.worldmaomao.disk;

import com.github.worldmaomao.vo.LocalDiskVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 在Linux系统，扫描磁盘
 *
 */
public class LinuxDiskScanner implements DiskScanner {


    @Override
    public List<LocalDiskVo> scan() {
        List<LocalDiskVo> voList = new ArrayList<LocalDiskVo>();
        return voList;
    }

}
