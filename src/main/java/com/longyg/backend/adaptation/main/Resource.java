package com.longyg.backend.adaptation.main;

import java.io.InputStream;

/**
 * Created by ylong on 2/13/2017.
 */
public class Resource {
    private String path;
    private InputStream inputStream;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
