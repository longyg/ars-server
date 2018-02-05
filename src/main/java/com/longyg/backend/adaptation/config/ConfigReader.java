package com.longyg.backend.adaptation.config;

import org.apache.log4j.Logger;

import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylong on 2/9/2017.
 */
public class ConfigReader {
    private static final Logger LOG = Logger.getLogger(ConfigReader.class);

    public void readConfig(File configFile, ConfigRepository configRepository) throws Exception {
        InputStream is = null;
        JsonReader reader = null;
        try {
            is = new FileInputStream(configFile);
            reader = Json.createReader(is);
            JsonObject obj = reader.readObject();
            configRepository.setType(obj.getString("type"));
            configRepository.setRoot(obj.getString("root"));
            configRepository.setNeVersion(obj.getString("NEVersion"));
            configRepository.setLastVersion(obj.getString("lastVersion"));
            configRepository.setForceDownload(!obj.getString("forceDownload").equalsIgnoreCase("no"));
            configRepository.setLoadPropPath(obj.getString("loadProperties"));

            SvnInfo svnInfo = new SvnInfo();
            svnInfo.setSvnRoot(obj.getString("root"));
            svnInfo.setUsername(obj.getString("svn_username"));
            svnInfo.setPassword(obj.getString("svn_password"));

            configRepository.setSvnInfo(svnInfo);

            JsonArray resources = obj.getJsonArray("resources");
            List<String> list = new ArrayList<String>();
            for (JsonString resource : resources.getValuesAs(JsonString.class)) {
                LOG.debug(resource.getString());
                list.add(resource.getString());
            }

            configRepository.setResoures(list);

            JsonObject outObj = obj.getJsonObject("output");
            configRepository.setOutputRoot(outObj.getString("root"));

            JsonObject omesConfigObj = obj.getJsonObject("OmesGenerateConfig");
            if (null != omesConfigObj) {
                OmesGenerateConfig omesConfig = new OmesGenerateConfig();
                omesConfig.setAdditionalParentDn(omesConfigObj.getString("AdditionalParentDN"));
                configRepository.setOmesConfig(omesConfig);
            }

        } catch (Exception e) {
            LOG.error("Exception while parsing config", e);
            throw new Exception("Exception while parsing config", e);
        } finally {
            if (null != reader) {
                reader.close();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
