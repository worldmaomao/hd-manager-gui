package github.com.worldmaomao.disk;

import github.com.worldmaomao.vo.LocalDiskVo;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 在windows系统，扫描磁盘
 *
 */
public class WindowsDiskScanner implements DiskScanner {


    @Override
    public List<LocalDiskVo> scan() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] roots = File.listRoots();
        List<LocalDiskVo> voList = new ArrayList<LocalDiskVo>();
        for(File file : roots) {
            LocalDiskVo diskVo = new LocalDiskVo();
            diskVo.setName(fsv.getSystemDisplayName(file));
            diskVo.setRootPath(file.getPath());
            diskVo.setIsUsb(false);
            voList.add(diskVo);
        }
        return voList;
    }

    public static void main(String[] args) {
        WindowsDiskScanner scanner = new WindowsDiskScanner();
        System.out.println(scanner.scan());
    }

}
