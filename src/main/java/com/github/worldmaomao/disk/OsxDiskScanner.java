package com.github.worldmaomao.disk;

import com.github.worldmaomao.vo.LocalDiskVo;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 在osx系统，扫描磁盘
 *
 */
public class OsxDiskScanner implements DiskScanner {

    private static final String infoEndLine = "**********";

    @Override
    public List<LocalDiskVo> scan() {
        List<LocalDiskVo> voList = new ArrayList<LocalDiskVo>();
        try {
            Process p = Runtime.getRuntime().exec("diskutil info -all");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            LocalDiskVo diskVo = new LocalDiskVo();
            String line = reader.readLine();
            boolean deviceEndFlag = false;
            boolean mounted = false;
            while (line != null) {
                // System.out.println(line);
                if (!deviceEndFlag) {
                    if (line.indexOf("Volume Name:") >= 0) {
                        String[] strs = line.split("Volume Name:");
                        if (strs[1].indexOf("no file system") == -1 || strs[1].indexOf("No") == -1) {
                            mounted = true;
                        }
                        if (mounted) {
                            diskVo.setName(strs[1].trim());
                        }
                    } else if (line.indexOf("Removable Media:") >= 0) {
                        if (mounted) {
                            String[] strs = line.split("Removable Media:");
                            diskVo.setIsUsb(strs[1].trim().equalsIgnoreCase("Removable"));
                        }
                    } else if (line.indexOf("Mount Point:") >= 0) {
                        if (mounted) {
                            String[] strs = line.split("Mount Point:");
                            diskVo.setRootPath(strs[1].trim());
                        }
                    } else {
                        // do nothing.
                    }

                } else {
                    diskVo = new LocalDiskVo();
                    deviceEndFlag = false;
                    mounted = false;
                }
                if (line.equals(infoEndLine)) {
                    deviceEndFlag = true;
                    if (mounted
                            && StringUtils.isNotEmpty(diskVo.getRootPath())
                            && !diskVo.getRootPath().equalsIgnoreCase("/private/var/vm")
                            && !diskVo.getRootPath().equalsIgnoreCase("/")) {
                        voList.add(diskVo);
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voList;
    }

    public static void main(String[] args) {
        OsxDiskScanner s = new OsxDiskScanner();
        List<LocalDiskVo> list = s.scan();
        System.out.println(list);
    }
}
