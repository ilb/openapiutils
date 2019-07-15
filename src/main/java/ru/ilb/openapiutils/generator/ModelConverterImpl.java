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
package ru.ilb.openapiutils.generator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

/**
 *
 * @author slavb
 */
public class ModelConverterImpl implements ModelConverter {

    static final Logger LOG = Logger.getLogger(ModelConverterImpl.class.getName());

    private final List<String> ignorePackage;

    public ModelConverterImpl() {
        Properties properties = loadProperties();
        String ignorePackageStr = properties.getProperty("generator.ignorePackage");
        if (ignorePackageStr != null && !ignorePackageStr.trim().isEmpty()) {
            ignorePackage = Arrays.asList(ignorePackageStr.trim().split(","));
            LOG.log(Level.INFO, "ignorePackage={0}", ignorePackage);
        } else {
            ignorePackage = null;
        }

    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (ignorePackage != null) {
            JavaType _type = Json.mapper().constructType(type.getType());
            if (_type != null) {
                Class<?> cls = _type.getRawClass();
                if (cls.getPackage() != null && ignorePackage.stream().anyMatch(s -> s.contains(cls.getPackage().getName()))) {
                    LOG.log(Level.FINE, "skip class={0}", cls.getCanonicalName());
                    Schema schema = new Schema();
                    schema.setType("object");
                    return schema;
                }
            }
        }
        // multipart/form-data
        Multipart multipart = AnnotationsUtils.getAnnotation(Multipart.class, type.getCtxAnnotations());
        if (multipart != null) {
            Schema schema = chain.next().resolve(type, context, chain);
            schema.setType("object");
            schema.setFormat(null);
            schema.addProperties(multipart.value(), new Schema().type("string").format("binary"));
            return schema;
        }
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("openapiutils.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return props;
    }

}
