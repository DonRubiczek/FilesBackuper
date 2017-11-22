package com.sieprawski.infrastructure;

import java.io.Serializable;

public class AppConfig implements Serializable {

    private static final long serialVersionUID = 3691305301775499500L;
    private String backupDirPath;
    private int portNumber;

    public int getPortNumber() {
        return portNumber;
    }

    public String getBackupDirPath() {
        return backupDirPath;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public void setBackupDirPath(String backupDirPath) {
        this.backupDirPath = backupDirPath;
    }
}
