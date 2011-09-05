/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.credits;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CreditsItem implements Comparable<CreditsItem> {

    private String name;
    private String webPage;
    private String license;

    public CreditsItem(String name, String webPage, String license) {
        if (name == null || webPage == null || license == null)
            throw new NullPointerException("Argument is null");
        this.name = name;
        this.webPage = webPage;
        this.license = license;
    }

    public String getName() {
        return name;
    }

    public String getWebPage() {
        return webPage;
    }

    public String getLicense() {
        return license;
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
