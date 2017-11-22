package com.sieprawski.models;

public class FilesModel {

    private String absolutePath;
    private String clientPath;
    private String hash;

    public FilesModel(String absolutePath, String clientPath, String hash) {

        this.absolutePath = absolutePath;
        this.clientPath = clientPath;
        this.hash = hash;

    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getClientPath() {
        return clientPath;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "\n" + this.absolutePath + " : " + this.clientPath + " : " + this.hash;
    }
}
