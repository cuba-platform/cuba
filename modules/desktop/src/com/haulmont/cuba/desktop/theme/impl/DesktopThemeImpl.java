/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.Resources;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopThemeImpl implements DesktopTheme {

    private String name;

    private String lookAndFeel;

    private Map<String, Object> uiDefaults;

    private List<DesktopStyle> styles;

    private Resources resources;

    protected Log log = LogFactory.getLog(getClass());

    public DesktopThemeImpl(String name) {
        this.name = name;
        this.uiDefaults = new HashMap<String, Object>();
        this.styles = new ArrayList<DesktopStyle>();
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Map<String, Object> getUiDefaults() {
        return uiDefaults;
    }

    public List<DesktopStyle> getStyles() {
        return styles;
    }

    public String getLookAndFeel() {
        return lookAndFeel;
    }

    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);

        try {
            UIManager.setLookAndFeel(lookAndFeel);
            initUIDefaults();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    private void initUIDefaults() {
        for (String propertyName: uiDefaults.keySet()) {
            UIManager.getLookAndFeelDefaults().put(propertyName, uiDefaults.get(propertyName));
        }
    }

    @Override
    public void applyStyle(Object component, String styleName) {
        applyStyle(component, styleName, null);
    }

    @Override
    public void applyStyle(Object component, String styleName, Set<String> state) {
        DesktopStyle style = findStyle(component.getClass(), styleName);
        if (style == null) {
            log.warn("Can not find style " + styleName + " for component " + component);
            return;
        }

        for (ComponentDecorator decorator: style.getDecorators()) {
            try {
                decorator.decorate(component, state);
            }
            catch (Exception e) {
                log.error("Error applying decorator " + decorator + " to " + component, e);
            }
        }
    }

    private DesktopStyle findStyle(Class componentClass, String styleName) {
        for (DesktopStyle desktopStyle: styles) {
            if (desktopStyle.getName().equals(styleName) && desktopStyle.isSupported(componentClass)) {
                return desktopStyle;
            }
        }
        return null;
    }

    public void setStyles(List<DesktopStyle> styles) {
        this.styles = styles;
    }
}
