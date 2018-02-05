package com.longyg.backend.adaptation.config;

/**
 * Created by ylong on 2/9/2017.
 */
public class SvnInfo {
    private String svnRoot;
    private String username;
    private String password;

    public String getSvnRoot() {
        return svnRoot;
    }

    public void setSvnRoot(String svnRoot) {
        this.svnRoot = svnRoot;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
