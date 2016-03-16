/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.core.credits;

import javax.annotation.Nullable;

/**
 *
 */
public class CreditsItem implements Comparable<CreditsItem> {

    private String name;
    private String webPage;
    private String licenseId;
    private String license;
    private String acknowledgement;
    private boolean fork;

    public CreditsItem(String name, String webPage, String licenseId, String license, String acknowledgement,
                       boolean fork) {
        this.fork = fork;
        if (name == null || webPage == null || license == null)
            throw new NullPointerException("Argument is null");
        this.name = name;
        this.webPage = webPage;
        this.licenseId = licenseId;
        this.license = license;
        this.acknowledgement = acknowledgement;
    }

    public String getName() {
        return name;
    }

    public String getWebPage() {
        return webPage;
    }

    @Nullable
    public String getLicenseId() {
        return licenseId;
    }

    public String getLicense() {
        return license;
    }

    @Nullable
    public String getAcknowledgement() {
        return acknowledgement;
    }

    public boolean isFork() {
        return fork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditsItem that = (CreditsItem) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(CreditsItem o) {
        return o == null ? 1 : (name.compareTo(o.name));
    }
}
