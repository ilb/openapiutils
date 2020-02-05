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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import ru.ilb.openapiutils.config.Config;
import ru.ilb.openapiutils.config.ConfigFactory;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author slavb
 */
// TODO: separate all conditions to different ModelConverters
public class ModelConverterImpl implements ModelConverter {

    static final Logger LOG = Logger.getLogger(ModelConverterImpl.class.getName());

    private final Config config = ConfigFactory.getConfig();

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        //  Uses XmlElement's type instead of declared.
        //  Needs to work with XmlJavaTypeAdapter, which marshals declared type to type, specified in XmlElement annotation
        if (type.isSchemaProperty()) {
            XmlElement xmlElementAnnotation = AnnotationsUtils.getAnnotation(XmlElement.class, type.getCtxAnnotations());
            if (xmlElementAnnotation != null && !xmlElementAnnotation.type().isAssignableFrom(XmlElement.DEFAULT.class)) {
                type = new AnnotatedType(xmlElementAnnotation.type());
            }
        }

        JavaType _type = Json.mapper().constructType(type.getType());
        Class cls = _type != null ? _type.getRawClass() : null;

        if (cls != null) {
            LOG.log(Level.FINE, "resolve class={0}", cls.getCanonicalName());
        }

        if (config.getIgnorePackage() != null) {
            if (cls != null) {
                if (cls.getPackage() != null && config.getIgnorePackage().stream().anyMatch(s -> s.contains(cls.getPackage().getName()))) {
                    LOG.log(Level.FINE, "skip class={0}", cls.getCanonicalName());
                    Schema schema = new Schema();
                    schema.setType("object");
                    return schema;
                }
            }
        }
        if (cls != null && MultipartBody.class.isAssignableFrom(cls)) {
            Schema schema = new Schema();
            schema.setType("object");
            //schema.additionalProperties(new Schema().type("object"));
            //schema.additionalProperties(new Schema().type("string"));
            // multi file upload
            //schema.addProperties("file", new ArraySchema().type("array").items(new Schema().type("string").format("binary")));
            // single file upload
            schema.addProperties("file", new Schema().type("string").format("binary"));
            return schema;
        }
        // multipart/form-data annotation
        Multipart multipart = AnnotationsUtils.getAnnotation(Multipart.class, type.getCtxAnnotations());
        if (multipart != null) {
            Schema schema = chain.next().resolve(type, context, chain);
            schema.setType("object");
            schema.setFormat(null);
            schema.addProperties(multipart.value(), new Schema().type("string").format("binary"));
            return schema;
        }
        if (chain.hasNext()) {
            Schema schema = chain.next().resolve(type, context, chain);
            return schema;
        } else {
            return null;
        }
    }

}
