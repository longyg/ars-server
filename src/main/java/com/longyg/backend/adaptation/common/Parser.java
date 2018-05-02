package com.longyg.backend.adaptation.common;

import java.io.InputStream;

/**
 * Created by ylong on 2/17/2017.
 */
public interface Parser {
    void parse(InputStream is) throws Exception;
}
