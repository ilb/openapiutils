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
    private boolean autotags;

    public List<String> getIgnorePackage() {
        return ignorePackage;
    }

    public void setIgnorePackage(List<String> ignorePackage) {
        this.ignorePackage = ignorePackage;
    }

    public boolean getAutotags() {
        return autotags;
    }

    public void setAutotags(boolean autotags) {
        this.autotags = autotags;
    }

    @Override
    public String toString() {
        return "Config{" + "ignorePackage=" + ignorePackage + ", autotags=" + autotags + '}';
    }

}
