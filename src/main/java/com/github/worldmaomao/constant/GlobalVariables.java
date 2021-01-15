package com.github.worldmaomao.constant;

import com.github.worldmaomao.config.ConfigLoader;
import com.github.worldmaomao.cache.DiskCache;
import com.github.worldmaomao.cache.LocalDiskCache;
import com.github.worldmaomao.disk.DiskScanner;
import com.github.worldmaomao.disk.DiskScannerFactory;
import com.github.worldmaomao.servcie.DiskService;
import com.github.worldmaomao.servcie.impl.DiskServiceImpl;
import com.github.worldmaomao.vo.DiskVo;

import javax.swing.*;
import java.io.IOException;
import java.util.List;


public class GlobalVariables {

    public static final String Title = "离线硬盘资料管理工具";

    public static String JWT = null;
    public static final DiskCache DiskCache = new DiskCache();
    public static final LocalDiskCache LocalDiskCache = new LocalDiskCache();


    public static final void loadLocalDisk() {
        DiskScanner diskScanner = DiskScannerFactory.create();
        if (diskScanner == null) {
            System.out.println("fail to create diskScanner");
            return;
        }
        GlobalVariables.LocalDiskCache.loadDisk(diskScanner.scan());
    }


    public static final void loadDiskData() {
        System.out.println("load disk data");
        DiskService diskService = new DiskServiceImpl(ConfigLoader.loadConfig());
        List<DiskVo> diskVoList = null;
        try {
            diskVoList = diskService.queryAll();
        } catch (IOException e) {
            try {
                diskVoList = diskService.queryAll();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "读取硬盘数据错误", "错误", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (diskVoList == null || diskVoList.isEmpty()) {
            return;
        }
        GlobalVariables.DiskCache.loadDisk(diskVoList);
    }
}
