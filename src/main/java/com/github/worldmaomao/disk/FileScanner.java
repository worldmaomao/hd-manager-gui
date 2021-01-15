package com.github.worldmaomao.disk;

import com.github.worldmaomao.vo.LocalFileVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件扫描工具
 *
 */
public class FileScanner {

    private static final String Point = ".";

    private String path;
    private int scanDeep;
    private boolean includeDir = false;
    private boolean includeFileWithDotPrefix = false;

    public FileScanner(String path, int scanDeep, boolean includeDir, boolean includeFileWithDotPrefix) {
        File dir = new File(path);
        if (dir.isFile() || !dir.exists()) {
            throw new IllegalArgumentException(String.format("路径[%s]错误，必须指向目录", path));
        }
        if (scanDeep < 1 || scanDeep > 5) {
            scanDeep = 1;
        }
        this.path = path;
        this.scanDeep = scanDeep;
        this.includeDir = includeDir;
        this.includeFileWithDotPrefix = includeFileWithDotPrefix;
    }

    private void walkDir(File dir, List<LocalFileVo> fileVoList, int currentDeep) {
        if (currentDeep > this.scanDeep) {
            return;
        }
        File[] subFiles = dir.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                if (this.canDirAdd(file)) {
                    fileVoList.add(this.toLocaFileVo(file));
                }
                if (currentDeep <= this.scanDeep) {
                    int newDeep = currentDeep;
                    this.walkDir(file, fileVoList, newDeep + 1);
                }
            } else {
                if (canFileAdd(file)) {
                    fileVoList.add(this.toLocaFileVo(file));
                }
            }
        }
    }

    private LocalFileVo toLocaFileVo(File file) {
        LocalFileVo vo = new LocalFileVo();
        vo.setFileName(file.getName());
        vo.setFileType(file.isDirectory() ? "dir" : "file");
        vo.setFilePath(file.getAbsolutePath());
        if (file.isFile()) {
            vo.setSize(file.length());
        }
        return vo;
    }

    private boolean canFileAdd(File dir) {
        if (this.includeFileWithDotPrefix && dir.getName().startsWith(Point)) {
            return false;
        }
        // add other rules
        return true;
    }

    private boolean canDirAdd(File dir) {
        if (!this.includeDir) {
            return false;
        }
        if (this.includeFileWithDotPrefix && dir.getName().startsWith(Point)) {
            return false;
        }
        // add other rules
        return true;
    }

    public List<LocalFileVo> scan() {
        List<LocalFileVo> fileVoList = new ArrayList();
        File dir = new File(this.path);
        if (!dir.isDirectory()) {
            return new ArrayList<LocalFileVo>();
        }
        walkDir(dir, fileVoList, 1);
        return fileVoList;
    }

    public static void main(String[] args) {
        FileScanner scanner = new FileScanner("/System/Volumes/Data", 2, true, true);
        List<LocalFileVo> fileList = scanner.scan();
        System.out.println(fileList.size());
        System.out.println(fileList);
    }

}
