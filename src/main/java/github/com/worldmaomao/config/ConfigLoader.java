package github.com.worldmaomao.config;


import java.io.File;

public class ConfigLoader {
    private static final String FileName = "config.json";
    private static final String HomePath = System.getProperty("user.home");
    private static final String ConfigDir = ".hd-mgr";

    private static Config config = new Config();

    static {
        config.setServerUrl("http://127.0.0.1:48077");
    }

    private static final File getConfigFile() {
        File dir = new File(HomePath + File.separator + ConfigDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File configFile = new File(dir, FileName);
        return configFile;
    }

    public static synchronized Config loadConfig() {
        return config;
    }

    public static void storeConfig(Config config) {

    }


}
