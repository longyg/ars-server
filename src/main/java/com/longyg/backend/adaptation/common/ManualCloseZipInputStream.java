package com.longyg.backend.adaptation.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by ylong on 2/14/2017.
 */
public class ManualCloseZipInputStream extends ZipInputStream {
    public ManualCloseZipInputStream(InputStream in) {
        super(in);
    }
    /**
     * Overwrite the implementation in ZipInputStream
     * so the stream will not closed automatically
     */
    public void close() {}
    /**
     * New method introduced
     * Should be called explicitly to close stream
     * @throws IOException
     */
    public void doClose() throws IOException { super.close(); }
}
