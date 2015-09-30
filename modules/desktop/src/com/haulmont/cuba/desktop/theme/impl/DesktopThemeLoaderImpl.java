/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.desktop.theme.DesktopThemeLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
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
 * @author Alexander Budarov
 * @version $Id$
 */
@Component(DesktopThemeLoader.NAME)
public class DesktopThemeLoaderImpl implements DesktopThemeLoader {

    private static final String BORDER_TAG = "border";

    @Inject
    private DesktopConfig config;

    @Inject
    private Resources resources;

    protected Logger log = LoggerFactory.getLogger(getClass());

    // like '255 128 0'
    private static final Pattern DECIMAL_COLOR_PATTERN = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(\\d+)$");

    @Override
    public DesktopTheme loadTheme(String themeName) {
        String themeLocations = config.getResourceLocations();
        StrTokenizer tokenizer = new StrTokenizer(themeLocations);
        String[] locationList = tokenizer.getTokenArray();

        List<String> resourceLocationList = new ArrayList<>();
        DesktopThemeImpl theme = createTheme(themeName, locationList);
        theme.setName(themeName);
        for (String location : locationList) {
            resourceLocationList.add(getResourcesDir(themeName, location));

            String xmlLocation = getConfigFileName(themeName, location);
            Resource resource = resources.getResource(xmlLocation);
            if (resource.exists()) {
                try {
                    loadThemeFromXml(theme, resource);
                } catch (IOException e) {
                    log.error("Error", e);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }

        DesktopResources desktopResources = new DesktopResources(resourceLocationList, resources);
        theme.setResources(desktopResources);

        return theme;
    }

    // read config files and search for <class> element which should contain custom theme class
    private DesktopThemeImpl createTheme(String themeName, String[] locationList) {
        String themeClassName = null;
        for (String location : locationList) {
            String xmlLocation = getConfigFileName(themeName, location);
            Resource resource = resources.getResource(xmlLocation);
            if (resource.exists()) {
                try {
                    Document doc = readXmlDocument(resource);
                    final Element rootElement = doc.getRootElement();

                    List<Element> classElements = rootElement.elements("class");
                    if (!classElements.isEmpty()) {
                        themeClassName = classElements.get(0).getTextTrim();
                    }
                } catch (IOException e) {
                    log.error("Error", e);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }
        if (themeClassName != null) {
            try {
                Class themeClass = Class.forName(themeClassName);
                return (DesktopThemeImpl) themeClass.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return new DesktopThemeImpl();
    }

    private String getResourcesDir(String themeName, String location) {
        return location + "/" + themeName;
    }

    private String getConfigFileName(String themeName, String location) {
        return location + "/" + themeName + "/" + themeName + ".xml";
    }

    private void loadThemeFromXml(DesktopThemeImpl theme, Resource resource) throws IOException {
        log.info("Loading theme file " + resource.getURL());

        Document doc = readXmlDocument(resource);
        final Element rootElement = doc.getRootElement();

        for (Element element : (List<Element>) rootElement.elements()) {
            String elementName = element.getName();
            if ("lookAndFeel".equals(elementName)) {
                String lookAndFeel = element.getTextTrim();
                if (StringUtils.isNotEmpty(lookAndFeel)) {
                    theme.setLookAndFeel(lookAndFeel);
                }
            } else if ("ui-defaults".equals(elementName)) {
                loadUIDefaults(theme.getUiDefaults(), element);
            } else if ("layout".equals(elementName)) {
                loadLayoutSettings(theme, element);
            } else if ("style".equals(elementName)) {
                DesktopStyle style = loadStyle(element);
                theme.addStyle(style);
            } else if ("include".equals(elementName)) {
                includeThemeFile(theme, element, resource);
            } else if ("class".equals(elementName)) {
                // ignore it
            } else {
                log.error("Unknown tag: " + elementName);
            }
        }
    }

    private Document readXmlDocument(Resource resource) throws IOException {
        Document doc;
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            try {
                SAXReader reader = new SAXReader();
                doc = reader.read(stream);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        } finally {
            IOUtils.closeQuietly(stream);
        }
        return doc;
    }

    private void includeThemeFile(DesktopThemeImpl theme, Element element, Resource resource) throws IOException {
        String fileName = element.attributeValue("file");
        if (StringUtils.isEmpty(fileName)) {
            log.error("Missing 'file' attribute to include");
            return;
        }

        Resource relativeResource = resource.createRelative(fileName);
        if (relativeResource.exists()) {
            log.info("Including theme file " + relativeResource.getURL());
            loadThemeFromXml(theme, relativeResource);
        } else {
            log.error("Resource " + fileName + " not found, ignore it");
        }
    }

    private void loadLayoutSettings(DesktopThemeImpl theme, Element element) {
        try {
            String margin = element.attributeValue("margin-size");
            if (margin != null) {
                theme.setMarginSize(Integer.valueOf(margin));
            }

            String spacing = element.attributeValue("spacing-size");
            if (spacing != null) {
                theme.setSpacingSize(Integer.valueOf(spacing));
            }
        } catch (NumberFormatException e) {
            log.error("Invalid integer value at layout settings: " + e.getMessage());
        }
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
        } else {
            Element componentsElement = element.element(componentsSubTag);
            if (componentsElement != null) {
                String componentsStr = componentsElement.getTextTrim();
                StrTokenizer tokenizer = new StrTokenizer(componentsStr);
                components = new ArrayList<>();
                for (String className : tokenizer.getTokenArray()) {
                    try {
                        components.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        log.error("Unknown component class: " + className);
                    }
                }
            }
        }

        List<ComponentDecorator> decorators = new ArrayList<>();
        for (Element childElement : (List<Element>) element.elements()) {
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
        } else if ("background".equals(elementName)) {
            Color value = loadColorValue(element.attributeValue("color"));
            return new PropertyPathDecorator(property, value, state);
        } else if ("foreground".equals(elementName)) {
            Color value = loadColorValue(element.attributeValue("color"));
            return new PropertyPathDecorator(property, value, state);
        } else if ("font".equals(elementName)) {
            return loadFontDecorator(element, property, state);
        } else if (BORDER_TAG.equals(elementName)) {
            Border border = loadBorder(element);
            return new PropertyPathDecorator(property, border, state);
        } else if ("icon".equals(elementName)) {
            return loadIconDecorator(element);
        }

        log.error("Unknown style tag: " + elementName);
        return null;
    }

    private ComponentDecorator loadIconDecorator(Element element) {
        String iconName = element.attributeValue("name");
        if (StringUtils.isEmpty(iconName)) {
            log.error("icon requires 'name' attribute");
            return null;
        }
        return new IconDecorator(iconName);
    }

    private ComponentDecorator loadFontDecorator(Element element, String property, String state) {
        try {
            String family = element.attributeValue("family");
            Integer style = element.attributeValue("style") != null
                    ? convertFontStyle(element.attributeValue("style")) : null;
            Integer size = element.attributeValue("size") != null
                    ? Integer.parseInt(element.attributeValue("size")) : null;
            FontDecorator decorator = new FontDecorator(property, family, style, size);
            decorator.setState(state);
            return decorator;
        } catch (NumberFormatException e) {
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
            } catch (NumberFormatException e) {
                log.error("Border margins value should be like '0 0 0 0': " + value);
            }
        } else if ("line".equals(type)) {
            String color = element.attributeValue("color");
            String width = element.attributeValue("width");
            Color borderColor = loadColorValue(color);
            if (borderColor == null) {
                log.error("Invalid line border color");
                return null;
            }
            if (width != null) {
                return BorderFactory.createLineBorder(borderColor, Integer.parseInt(width));
            } else {
                return BorderFactory.createLineBorder(borderColor);
            }
        } else if ("compound".equals(type)) {
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
        } else {
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
        } else if ("font".equals(elementName)) {
            return loadFontForUIDefault(element);
        } else if ("insets".equals(elementName)) {
            return loadInsets(element);
        } else if ("dimension".equals(elementName)) {
            return loadDimension(element);
        } else {
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
        } catch (NumberFormatException e) {
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
        } catch (NumberFormatException e) {
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
        } catch (NumberFormatException e) {
            log.error("Unparseable size: " + size);
            return null;
        }

        return new Font(family, styleInt, sizeInt);
    }

    private Integer convertFontStyle(String styleStr) {
        if ("bold".equals(styleStr)) {
            return Font.BOLD;
        } else if ("italic".equals(styleStr)) {
            return Font.ITALIC;
        } else if ("bold-italic".equals(styleStr)) {
            return Font.BOLD | Font.ITALIC;
        } else if (styleStr == null || "plain".equals(styleStr)) {
            return Font.PLAIN;
        } else {
            log.error("Unknown font style: " + styleStr);
            return null;
        }
    }

    private Color loadColorValue(String value) {
        if (value == null) {
            return null;
        }

        if ("transparent".equalsIgnoreCase(value)) {
            return new Color(255, 255, 255, 0); // completely transparent color
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