package com.longyg.frontend.Utils;

import java.io.File;

public class FileUtils {
    public static final String ARS_XLS_ROOT_PATH = "ars";
    public static final String ARS_XLS_FILENAME_PREFIX = "Adaptation Requirements Specification";

    public static String getArsFilePath(String neType, String neVersion) throws Exception {
        String outDirPath = ARS_XLS_ROOT_PATH + File.separator + neType + File.separator + neVersion;
        String filename = ARS_XLS_FILENAME_PREFIX + " " + neType + " " + neVersion + ".xls";
        File outDir = new File(outDirPath);
        if (outDir.exists() && !outDir.isDirectory()) {
            throw new Exception(outDir.getAbsolutePath() + " is not a directory");
        } else if (!outDir.exists()) {
            outDir.mkdirs();
        }

        return outDirPath + File.separator + filename;
    }
}
