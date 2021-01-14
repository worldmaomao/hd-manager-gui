package github.com.worldmaomao.config;


public class ConfigLoader {

    private static final Config config = new Config();

    static {
        config.setServerUrl("http://127.0.0.1:48077");
    }

    public static Config loadConfig() {
        return config;
    }

    public static void storeConfig(Config config) {

    }


}
