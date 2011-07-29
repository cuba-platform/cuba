/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.sys.ConfigurationResourceLoader;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.Resources;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.desktop.theme.DesktopThemeLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopThemeLoaderImpl extends DesktopThemeLoader {

    private static final String BORDER_TAG = "border";

    protected Log log = LogFactory.getLog(getClass());

    // like '255 128 0'
    private static final Pattern DECIMAL_COLOR_PATTERN = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(\\d+)$");

    public DesktopTheme loadTheme(String themeName) {
        final String themeLocations = ConfigProvider.getConfig(DesktopConfig.class).getResourceLocations();
        StrTokenizer tokenizer = new StrTokenizer(themeLocations);
        ConfigurationResourceLoader resourceLoader = new ConfigurationResourceLoader();

        List<String> resourceLocationList = new ArrayList<String>();
        DesktopThemeImpl theme = new DesktopThemeImpl(themeName);
        for (String location : tokenizer.getTokenArray()) {
            resourceLocationList.add(getResourcesDir(themeName, location));

            String xmlLocation = getConfigFileName(themeName, location);
            Resource resource = resourceLoader.getResource(xmlLocation);
            if (resource.exists()) {
                InputStream stream = null;
                    try {
                        stream = resource.getInputStream();
                        loadThemeFromXml(theme, stream);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    finally {
                        IOUtils.closeQuietly(stream);
                    }
            }
            else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }

        Resources resources = new Resources(resourceLocationList);
        theme.setResources(resources);

        return theme;
    }

    private String getResourcesDir(String themeName, String location) {
        return location + "/" + themeName;
    }

    private String getConfigFileName(String themeName, String location) {
        return location + "/" + themeName + "/" + themeName + ".xml";
    }

    private void loadThemeFromXml(DesktopThemeImpl theme, InputStream stream) {
        Document doc;
        try {
            SAXReader reader = new SAXReader();
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        final Element rootElement = doc.getRootElement();

        List<DesktopStyle> styles = new ArrayList<DesktopStyle>();

        for (Element element : (List<Element>) rootElement.elements()) {
            String elementName = element.getName();
            if ("lookAndFeel".equals(elementName)) {
                String lookAndFeel = element.getTextTrim();
                if (StringUtils.isNotEmpty(lookAndFeel)) {
                    theme.setLookAndFeel(lookAndFeel);
                }
            }
            else if ("ui-defaults".equals(elementName)) {
                loadUIDefaults(theme.getUiDefaults(), element);
            }
            else if ("style".equals(elementName)) {
                DesktopStyle style = loadStyle(element);
                styles.add(style);
            }
            else {
                log.error("Unknown tag: " + elementName);
            }
        }

        styles.addAll(theme.getStyles());
        theme.setStyles(styles);
    }

    private DesktopStyle loadStyle(Element element) {
        final String componentsSubTag = "components";

        String styleName = element.attributeValue("name");

        List<Class> components = null;
        if (element.attributeValue("component") != null) {
            String className = element.attributeValue("component");
            try {
                components = Collections.singletonList((Class) Class.forName(className));
            } catch (ClassNotFoundException e) {
                log.error("Unknown component class: " + className);
            }
        }
        else {
            Element componentsElement = element.element(componentsSubTag);
            if (componentsElement != null) {
                String componentsStr = componentsElement.getTextTrim();
                StrTokenizer tokenizer = new StrTokenizer(componentsStr);
                components = new ArrayList<Class>();
                for (String className: tokenizer.getTokenArray()) {
                    try {
                        components.add(Class.forName(className));
                    }
                    catch (ClassNotFoundException e) {
                        log.error("Unknown component class: " + className);
                    }
                }
            }
        }

        List<ComponentDecorator> decorators = new ArrayList<ComponentDecorator>();
        for (Element childElement: (List<Element>) element.elements()) {
            if (!componentsSubTag.equals(childElement.getName())) {
                ComponentDecorator decorator = loadDecorator(childElement);
                if (decorator != null) {
                    decorators.add(decorator);
                }
            }
        }

        return new DesktopStyle(styleName, decorators, components);
    }

    private ComponentDecorator loadDecorator(Element element) {
        String elementName = element.getName();
        String property = element.attributeValue("property");
        if (property == null) {
            property = elementName;
        }

        String state = element.attributeValue("state");

        if ("custom".equals(elementName)) {
            String className = element.attributeValue("class");
            return new CustomDecorator(className);
        }
        else if ("background".equals(elementName)) {
            Color value = loadColorValue(element.attributeValue("color"));
            return new PropertyPathDecorator(property, value, state);
        }
        else if ("foreground".equals(elementName)) {
            Color value = loadColorValue(element.attributeValue("color"));
            return new PropertyPathDecorator(property, value, state);
        }
        else if ("font".equals(elementName)) {
            return loadFontDecorator(element, property, state);
        }
        else if (BORDER_TAG.equals(elementName)) {
            Border border = loadBorder(element);
            return new PropertyPathDecorator(property, border, state);
        }
        log.error("Unknown style tag: " + elementName);
        return null;
    }

    private ComponentDecorator loadFontDecorator(Element element, String property, String state) {
        try {
            String family = element.attributeValue("family");
            Integer style = element.attributeValue("style") != null
                    ? convertFontStyle(element.attributeValue("style")) : null;
            Integer size = element.attributeValue("size") != null
                    ? Integer.parseInt(element.attributeValue("size")) : null;
            FontDecorator decorator =  new FontDecorator(property, family, style, size);
            decorator.setState(state);
            return decorator;
        }
        catch (NumberFormatException e) {
            log.error("Error loading font for style", e);
            return null;
        }
    }

    private Border loadBorder(Element element) {
        String type = element.attributeValue("type");
        if ("empty".equals(type)) {
            String value = element.attributeValue("margins");
            String[] values = value.split(" ");
            if (values.length != 4) {
                log.error("Border margins value should be like '0 0 0 0': " + value);
                return null;
            }
            try {
                int top = Integer.parseInt(values[0]);
                int right = Integer.parseInt(values[1]);
                int bottom = Integer.parseInt(values[2]);
                int left = Integer.parseInt(values[3]);
                return BorderFactory.createEmptyBorder(top, left, bottom, right);
            }
            catch (NumberFormatException e) {
                log.error("Border margins value should be like '0 0 0 0': " + value);
            }
        }
        else if ("line".equals(type)) {
            String color = element.attributeValue("color");
            String width = element.attributeValue("width");
            Color borderColor = loadColorValue(color);
            if (borderColor == null) {
                log.error("Invalid line border color");
                return null;
            }
            if (width != null) {
                return BorderFactory.createLineBorder(borderColor, Integer.parseInt(width));
            }
            else {
                return BorderFactory.createLineBorder(borderColor);
            }
        }
        else if ("compound".equals(type)) {
            if (element.elements().size() < 2) {
                log.error("Compound border should have two child borders");
                return null;
            }
            final Element child1 = (Element) element.elements().get(0);
            final Element child2 = (Element) element.elements().get(1);
            if (!BORDER_TAG.equals(child1.getName()) || !BORDER_TAG.equals(child2.getName())) {
                log.error("Compound border should have two child borders");
                return null;
            }
            Border outsideBorder = loadBorder(child1);
            Border insideBorder = loadBorder(child2);
            if (outsideBorder == null || insideBorder == null) {
                return null;
            }
            return BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        }
        else {
            log.error("Unknown border type: " + type);
        }
        return null;
    }

    private void loadUIDefaults(Map<String, Object> uiDefaults, Element rootElement) {
        for (Element element : (List<Element>) rootElement.elements()) {
            String propertyName = element.attributeValue("property");

            Object value = loadUIValue(element);
            if (value != null) {
                uiDefaults.put(propertyName, value);
            }
        }
    }

    private Object loadUIValue(Element element) {
        String elementName = element.getName();
        if ("color".equals(elementName)) {
            String value = element.attributeValue("value");
            if (value == null) {
                log.error("Color requires value attribute specified");
                return null;
            }
            return loadColorValue(value);
        }
        else if ("font".equals(elementName)) {
            return loadFontForUIDefault(element);
        }
        else if ("insets".equals(elementName)) {
            return loadInsets(element);
        }
        else if ("dimension".equals(elementName)) {
            return loadDimension(element);
        }
        else {
            log.error("Uknown UI property value: " + elementName);
            return null;
        }
    }

    private Dimension loadDimension(Element element) {
        String value = element.attributeValue("value");
        if (value == null) {
            log.error("Dimension value should specified");
            return null;
        }
        String[] values = value.split(" ");
        if (values.length != 2) {
            log.error("Dimension value should be like '0 0': " + value);
            return null;
        }

        try {
            int width = Integer.parseInt(values[0]);
            int height = Integer.parseInt(values[1]);
            return new Dimension(width, height);
        }
        catch (NumberFormatException e) {
            log.error("Dimension value should be like '0 0': " + value);
            return null;
        }

    }

    private Insets loadInsets(Element element) {
        String value = element.attributeValue("value");
        if (value == null) {
            log.error("Insets value should specified");
            return null;
        }
        String[] values = value.split(" ");
        if (values.length != 4) {
            log.error("Insets value should be like '0 0 0 0': " + value);
            return null;
        }

        try {
            int top = Integer.parseInt(values[0]);
            int right = Integer.parseInt(values[1]);
            int bottom = Integer.parseInt(values[2]);
            int left = Integer.parseInt(values[3]);
            return new Insets(top, left, bottom, right);
        }
        catch (NumberFormatException e) {
            log.error("Insets value should be like '0 0 0 0': " + value);
            return null;
        }
    }

    private Font loadFontForUIDefault(Element element) {
        String family = element.attributeValue("family");
        if (family == null) {
            log.error("Font family required for ui-defaults");
            return null;
        }

        String styleStr = element.attributeValue("style");
        Integer styleInt = convertFontStyle(styleStr);
        if (styleInt == null) {
            log.error("Unknown style: " + styleStr);
            return null;
        }

        String size = element.attributeValue("size");
        if (size == null) {
            log.error("Font size required for ui-defauls");
            return null;
        }

        int sizeInt;
        try {
            sizeInt = Integer.parseInt(size);
        }
        catch (NumberFormatException e) {
            log.error("Unparseable size: " + size);
            return null;
        }

        return new Font(family, styleInt, sizeInt);
    }

    private Integer convertFontStyle(String styleStr) {
        if ("bold".equals(styleStr)) {
            return Font.BOLD;
        }
        else if ("italic".equals(styleStr)) {
            return Font.ITALIC;
        }
        else if ("bold-italic".equals(styleStr)) {
            return Font.BOLD | Font.ITALIC;
        }
        else if (styleStr == null || "plain".equals(styleStr)) {
            return Font.PLAIN;
        }
        else {
            log.error("Unknown font style: " + styleStr);
            return null;
        }
    }

    private Color loadColorValue(String value) {
        if (value == null) {
            return null;
        }

        if (value.length() == 7 && value.charAt(0) == '#') { // html
            final int radix = 16;
            int r = Integer.parseInt(value.substring(1, 3), radix);
            int g = Integer.parseInt(value.substring(3, 5), radix);
            int b = Integer.parseInt(value.substring(5, 7), radix);
            return new Color(r, g, b);
        }
        final Matcher matcher = DECIMAL_COLOR_PATTERN.matcher(value);
        if (matcher.matches()) {
            int r = Integer.parseInt(matcher.group(1));
            int g = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));
            return new Color(r, g, b);
        }
        log.error("Unparseable color value: " + value);
        return null;
    }

}
