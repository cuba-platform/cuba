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

import java.util.*;

@SuppressWarnings({"UnusedDeclaration"})
public class Band {
    private Map<String, Object> data;
    private Band parentBand;

    private Map<String, List<Band>> childrenBands = new LinkedHashMap<String, List<Band>>();

    private String name;
    private int level;
    private Orientation orientation = Orientation.HORIZONTAL;
    private Set<String> bandDefinitionNames = null;
    private HashMap<String, ReportValueFormat> valuesFormats = null;

    public Band(String name, int level, Band parentBand, Orientation orientation) {
        this.name = name;
        this.level = level;
        this.parentBand = parentBand;
        this.orientation = orientation;
    }

    public Map<String, List<Band>> getChildrenBands() {
        return childrenBands;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<Band> getChildrenList() {
        List<Band> bandList = new ArrayList<Band>();
        for (List<Band> bands : childrenBands.values())
            bandList.addAll(bands);
        return bandList;
    }

    public void addChild(Band band) {
        if (!childrenBands.containsKey(band.getName())) {
            childrenBands.put(band.getName(), new ArrayList<Band>());
        }
        List<Band> bands = childrenBands.get(band.getName());
        bands.add(band);
    }

    public void addChildren(List<Band> bands) {
        for (Band band : bands)
            addChild(band);
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

    public String getFullName() {
        String fullName = name;
        Band upBand = parentBand;
        while ((upBand != null) && (upBand.level > 1)) {
            fullName = upBand.getName() + "." + fullName;
            upBand = upBand.parentBand;
        }
        return fullName;
    }

    @Override
    public String toString() {
        StringBuffer sbf = new StringBuffer();
        sbf.append(name).append(":").append(data.toString()).append("\n");
        for (Band band : getChildrenList()) {
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
        if (getChildrenList() != null) {
            for (Band child : getChildrenList()) {
                if (bandName.equals(child.getName())) {
                    return child;
                }
            }
        }
        return null;
    }

    public List<Band> getChildrenByName(String bandName) {
        if (bandName == null) {
            throw new NullPointerException("Parameter bandName can not be null.");
        }

        List<Band> children = new ArrayList<Band>();
        if (getChildrenList() != null) {
            for (Band child : getChildrenList()) {
                if (bandName.equals(child.getName())) {
                    children.add(child);
                }
            }
        }
        return children;
    }

    public Set<String> getBandDefinitionNames() {
        return bandDefinitionNames;
    }

    public void setBandDefinitionNames(Set<String> bandDefinitionNames) {
        this.bandDefinitionNames = bandDefinitionNames;
    }

    public HashMap<String, ReportValueFormat> getValuesFormats() {
        return valuesFormats;
    }

    public void setValuesFormats(HashMap<String, ReportValueFormat> valuesFormats) {
        this.valuesFormats = valuesFormats;
    }
}
