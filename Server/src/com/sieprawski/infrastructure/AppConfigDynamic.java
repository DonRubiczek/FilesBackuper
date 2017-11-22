package com.sieprawski.infrastructure;

public class AppConfigDynamic {

    private static AppConfigDynamic instance = null;

    private AppConfig appConfig;

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    protected AppConfigDynamic() {

    }

    public static AppConfigDynamic getInstance() {

        if(instance == null) {
            instance = new AppConfigDynamic();
        }

        return instance;
    }
}
