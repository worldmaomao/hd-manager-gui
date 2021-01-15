package com.github.worldmaomao.cache;

import com.github.worldmaomao.vo.DiskVo;
import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DiskCache {

    private static final BeanCopier copier = BeanCopier.create(DiskVo.class, DiskVo.class, false);


    private List<DiskVo> diskVoList;
    private Map<String, DiskVo> diskVoMap;

    public synchronized void loadDisk(List<DiskVo> list) {
        diskVoList = new ArrayList<DiskVo>();
        diskVoMap = new HashMap<String, DiskVo>();
        for (DiskVo vo : list) {
            DiskVo newVo = new DiskVo();
            copier.copy(vo, newVo, null);
            diskVoList.add(newVo);
            diskVoMap.put(newVo.getName(), newVo);
        }
    }

    public synchronized List<DiskVo> getDiskList() {
        if (diskVoList == null || diskVoList.isEmpty()) {
            return new ArrayList<DiskVo>();
        }
        List<DiskVo> resultList = new ArrayList<DiskVo>(diskVoList.size());
        for (DiskVo vo : diskVoList) {
            DiskVo newVo = new DiskVo();
            copier.copy(vo, newVo, null);
            resultList.add(newVo);
        }
        return resultList;

    }

    public synchronized DiskVo getDiskByName(String name) {
        if (diskVoMap == null || diskVoMap.size() == 0) {
            return null;
        }
        DiskVo vo = diskVoMap.get(name);
        if (vo == null) {
            return null;
        }
        DiskVo newVo = new DiskVo();
        copier.copy(vo, newVo, null);
        return newVo;

    }

    public synchronized List<String> getDiskNames() {
        if (diskVoList == null || diskVoList.isEmpty()) {
            return new ArrayList<String>();
        }
        return diskVoList.stream().map(DiskVo::getName).collect(Collectors.toList());
    }
}
