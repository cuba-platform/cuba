/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 15:20:07
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Band {
    private Map<String, Object> data;
    private Band parentBand;
    private List<Band> children = new ArrayList<Band>();
    private String name;
    private int level;
    private Orientation orientation = Orientation.HORIZONTAL;

    public Band(String name, int level, Band parentBand, Orientation orientation) {
        this.name = name;
        this.level = level;
        this.parentBand = parentBand;
        this.orientation = orientation;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<Band> getChildren() {
        return children;
    }

    public void setChildren(List<Band> children) {
        this.children = children;
    }

    public void addChild(Band band) {
        children.add(band);
    }

    public void addChildren(List<Band> bands) {
        children.addAll(bands);
    }

    public void addParameter(String name, Object value) {
        data.put(name, value);
    }

    public Object getParameter(String name) {
        return data.get(name);
    }

    public void addAllParameters(Map<String, Object> parameters) {
        data.putAll(parameters);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Band getParentBand() {
        return parentBand;
    }

    public void setParentBand(Band parentBand) {
        this.parentBand = parentBand;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        StringBuffer sbf = new StringBuffer();
        sbf.append(name).append(":").append(data.toString()).append("\n");
        for (Band band : children) {
            for (int i = 0; i < level; i++)
                sbf.append("\t");
            sbf.append(band.toString());
        }
        return sbf.toString();
    }

    public Band getChildByName(String bandName) {
        if (bandName == null) {
            throw new NullPointerException("Parameter bandName can not be null.");
        }
        if (getChildren() != null) {
            for (Band child : getChildren()) {
                if (bandName.equals(child.getName())) {
                    return child;
                }
            }
        }
        return null;
    }
}
