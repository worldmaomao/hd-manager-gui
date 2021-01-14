package github.com.worldmaomao.cache;

import github.com.worldmaomao.vo.LocalDiskVo;
import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LocalDiskCache {

    private static final BeanCopier copier = BeanCopier.create(LocalDiskVo.class, LocalDiskVo.class, false);


    private List<LocalDiskVo> localDiskVoList;
    private Map<String, LocalDiskVo> localDiskVoMap;

    public synchronized void loadDisk(List<LocalDiskVo> list) {
        localDiskVoList = new ArrayList<LocalDiskVo>();
        localDiskVoMap = new HashMap<String, LocalDiskVo>();
        for (LocalDiskVo vo : list) {
            LocalDiskVo newVo = new LocalDiskVo();
            copier.copy(vo, newVo, null);
            localDiskVoList.add(newVo);
            localDiskVoMap.put(newVo.getName(), newVo);
        }
    }

    public synchronized List<LocalDiskVo> getDiskList() {
        if (localDiskVoList == null || localDiskVoList.isEmpty()) {
            return new ArrayList<LocalDiskVo>();
        }
        List<LocalDiskVo> resultList = new ArrayList<>(localDiskVoList.size());
        for (LocalDiskVo vo : localDiskVoList) {
            LocalDiskVo newVo = new LocalDiskVo();
            copier.copy(vo, newVo, null);
            resultList.add(newVo);
        }
        return resultList;

    }

    public synchronized LocalDiskVo getDiskByName(String name) {
        if (localDiskVoMap == null || localDiskVoMap.size() == 0) {
            return null;
        }
        LocalDiskVo vo = localDiskVoMap.get(name);
        if (vo == null) {
            return null;
        }
        LocalDiskVo newVo = new LocalDiskVo();
        copier.copy(vo, newVo, null);
        return newVo;

    }

    public synchronized List<String> getDiskNames() {
        if (localDiskVoList == null || localDiskVoList.isEmpty()) {
            return new ArrayList<String>();
        }
        return localDiskVoList.stream().map(LocalDiskVo::getName).collect(Collectors.toList());
    }
}
