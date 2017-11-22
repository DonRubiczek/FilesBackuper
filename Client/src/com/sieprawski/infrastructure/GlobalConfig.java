package com.sieprawski.infrastructure;

import java.io.Serializable;

public class GlobalConfig implements Serializable {

    private static final long serialVersionUID = -4116661876130628226L;
    private String host;
    private int port;
    private ScheduleOptions option;

    public GlobalConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public ScheduleOptions getScheduledBackupOption() {
        return option;
    }
    public void setScheduledBackupOption(ScheduleOptions option) {
        this.option = option;
    }


}
