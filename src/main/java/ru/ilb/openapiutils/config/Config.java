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

import java.util.List;

/**
 *
 * @author slavb
 */
public class Config {

    /**
     * list of packages excluded from model generation
     */
    private List<String> ignorePackage;

    /**
     * autotag operation based on first path component
     */
    private boolean autoTags;

    /**
     * list of media types marked as binary format in spec. required for proper javascript client generation
     */
    private List<String> binaryMediaTypes;

    /**
     * list of media types marked as object type in spec. required for proper javascript client generation
     */
    private List<String> objectMediaTypes;

    /**
     * remove unreferenced schemas/classes. Parameter for OpenAPISpecFilterImpl
     */
    private boolean removeUnreferencedDefinitions;

    public List<String> getIgnorePackage() {
        return ignorePackage;
    }

    public void setIgnorePackage(List<String> ignorePackage) {
        this.ignorePackage = ignorePackage;
    }

    public boolean getAutotags() {
        return autoTags;
    }

    public void setAutoTags(boolean autoTags) {
        this.autoTags = autoTags;
    }

    public List<String> getBinaryMediaTypes() {
        return binaryMediaTypes;
    }

    public void setBinaryMediaTypes(List<String> binaryMediaTypes) {
        this.binaryMediaTypes = binaryMediaTypes;
    }

    public List<String> getObjectMediaTypes() {
        return objectMediaTypes;
    }

    public void setObjectMediaTypes(List<String> objectMediaTypes) {
        this.objectMediaTypes = objectMediaTypes;
    }

    public boolean isRemoveUnreferencedDefinitions() {
        return removeUnreferencedDefinitions;
    }

    public void setRemoveUnreferencedDefinitions(boolean removeUnreferencedDefinitions) {
        this.removeUnreferencedDefinitions = removeUnreferencedDefinitions;
    }

    @Override
    public String toString() {
        return "Config{" + "ignorePackage=" + ignorePackage + ", autoTags=" + autoTags + ", removeUnreferencedDefinitions=" + removeUnreferencedDefinitions + '}';
    }
}
