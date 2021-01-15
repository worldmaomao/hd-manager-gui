package com.github.worldmaomao.config;


import com.alibaba.fastjson.JSON;
import net.sf.cglib.beans.BeanCopier;

import java.io.*;

public class ConfigLoader {
    private static final String FileName = "config.json";
    private static final String HomePath = System.getProperty("user.home");
    private static final String ConfigDir = ".hd-mgr";

    private static final BeanCopier copier = BeanCopier.create(Config.class, Config.class, false);


    private static Config config;


    private static final File getConfigFile() {
        File dir = new File(HomePath + File.separator + ConfigDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File configFile = new File(dir, FileName);
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configFile;
    }

    public static synchronized Config loadConfig() {
        if (config != null) {
            return config;
        }
        File file = getConfigFile();
        try {
            config = JSON.parseObject(new FileInputStream(file), Config.class);
            if(config == null) {
                config = new Config();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static synchronized void storeConfig(Config newConfig) {
        try {
            JSON.writeJSONString(new FileOutputStream(getConfigFile()), newConfig);
            copier.copy(config, newConfig, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
