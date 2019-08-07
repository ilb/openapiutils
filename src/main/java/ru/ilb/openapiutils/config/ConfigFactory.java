/*
 * Copyright 2019 slavb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.openapiutils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author slavb
 */
public class ConfigFactory {

    static final Logger LOG = Logger.getLogger(ConfigFactory.class.getName());

    public static Config getConfig() {
        Config config = new Config();
        config.setIgnorePackage(getListProperty("ignorePackage", null));
        config.setAutoTags(getBooleanProperty("autoTags", true));

        LOG.log(Level.FINE, config.toString());
        return config;
    }

    private static final String PROPERTIES_PATH = "openapiutils.properties";

    private static Properties properties;

    private static Properties getProperties() {
        if (properties == null) {
            properties = loadProperties();
        }
        return properties;
    }

    private static String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    private static List<String> getListProperty(String key, List<String> defaultValue) {
        String str = getProperties().getProperty(key);
        List<String> res = defaultValue;
        if (str != null && !str.trim().isEmpty()) {
            res = Arrays.asList(str.trim().split(","));

        }
        return res;
    }

    private static Boolean getBooleanProperty(String key, Boolean defaultValue) {
        String str = getProperties().getProperty(key);
        Boolean res = defaultValue;
        if (str != null && !str.trim().isEmpty()) {
            res = Boolean.parseBoolean(str.trim());
        }
        return res;
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH)) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return props;
    }

}
