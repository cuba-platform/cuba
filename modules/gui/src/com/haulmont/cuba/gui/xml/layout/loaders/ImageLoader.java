/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader extends AbstractComponentLoader<Image> {

    @Override
    public void createComponent() {
        resultComponent = (Image) factory.createComponent(Image.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);
        loadAlign(resultComponent, element);

        loadDatasource(resultComponent, element);

        loadImageResource(resultComponent, element);

        loadScaleMode(resultComponent, element);
        loadAlternateText(resultComponent, element);
    }

    protected void loadAlternateText(Image resultComponent, Element element) {
        String alternateText = element.attributeValue("alternateText");
        if (StringUtils.isNotEmpty(alternateText)) {
            resultComponent.setAlternateText(alternateText);
        }
    }

    protected void loadScaleMode(Image image, Element element) {
        String scaleModeString = element.attributeValue("scaleMode");
        Image.ScaleMode scaleMode = Image.ScaleMode.NONE;
        if (scaleModeString != null) {
            scaleMode = Image.ScaleMode.valueOf(scaleModeString);
        }
        image.setScaleMode(scaleMode);
    }

    protected void loadImageResource(Image image, Element element) {
        if (loadFileImageResource(image, element)) return;

        if (loadThemeImageResource(image, element)) return;

        if (loadClasspathImageResource(image, element)) return;

        if (loadRelativePathImageResource(image, element)) return;

        loadUrlImageResource(image, element);
    }

    protected boolean loadRelativePathImageResource(Image resultComponent, Element element) {
        Element relativePath = element.element("relativePath");
        if (relativePath == null)
            return false;

        String path = relativePath.attributeValue("path");
        if (StringUtils.isEmpty(path)) {
            throw new GuiDevelopmentException("No path provided for the RelativePathResource", context.getFullFrameId());
        }

        RelativePathResource resource = resultComponent.createResource(RelativePathResource.class);

        resource.setPath(path);

        loadMimeType(resource, relativePath);

        resultComponent.setSource(resource);

        return true;
    }

    protected void loadUrlImageResource(Image resultComponent, Element element) {
        Element urlResource = element.element("url");
        if (urlResource == null)
            return;

        String url = urlResource.attributeValue("url");
        if (StringUtils.isEmpty(url)) {
            throw new GuiDevelopmentException("No url provided for the UrlResource", context.getFullFrameId());
        }

        UrlResource resource = resultComponent.createResource(UrlResource.class);
        try {
            resource.setUrl(new URL(url));

            loadMimeType(resource, urlResource);

            resultComponent.setSource(resource);
        } catch (MalformedURLException e) {
            String msg = String.format("An error occurred while creating UrlResource with the given url: %s", url);
            throw new GuiDevelopmentException(msg, context.getFullFrameId());
        }
    }

    protected boolean loadClasspathImageResource(Image resultComponent, Element element) {
        Element classpathResource = element.element("classpath");
        if (classpathResource == null)
            return false;

        String classpathPath = classpathResource.attributeValue("path");
        if (StringUtils.isEmpty(classpathPath)) {
            throw new GuiDevelopmentException("No path provided for the ClasspathResource", context.getFullFrameId());
        }

        ClasspathResource resource = resultComponent.createResource(ClasspathResource.class);

        resource.setPath(classpathPath);

        loadMimeType(resource, classpathResource);
        loadStreamSettings(resource, classpathResource);

        resultComponent.setSource(resource);

        return true;
    }

    protected boolean loadThemeImageResource(Image resultComponent, Element element) {
        Element themeResource = element.element("theme");
        if (themeResource == null)
            return false;

        String themePath = themeResource.attributeValue("path");
        if (StringUtils.isEmpty(themePath)) {
            throw new GuiDevelopmentException("No path provided for the ThemeResource", context.getFullFrameId());
        }

        resultComponent.setSource(ThemeResource.class).setPath(themePath);

        return true;
    }

    protected boolean loadFileImageResource(Image resultComponent, Element element) {
        Element fileResource = element.element("file");
        if (fileResource == null)
            return false;

        String filePath = fileResource.attributeValue("path");
        if (StringUtils.isEmpty(filePath)) {
            throw new GuiDevelopmentException("No path provided for the FileResource", context.getFullFrameId());
        }

        File file = new File(filePath);
        if (!file.exists()) {
            String msg = String.format("Can't load FileResource. File with given path does not exists: %s", filePath);
            throw new GuiDevelopmentException(msg, context.getFullFrameId());
        }

        FileResource resource = resultComponent.createResource(FileResource.class);

        resource.setFile(file);

        loadStreamSettings(resource, fileResource);

        resultComponent.setSource(resource);

        return true;
    }

    protected void loadDatasource(Image component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            Datasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException(String.format("Datasource '%s' is not defined", datasource),
                        getContext().getFullFrameId(), "Component ID", component.getId());
            }
            String property = element.attributeValue("property");
            if (StringUtils.isEmpty(property)) {
                throw new GuiDevelopmentException(
                        String.format("Can't set datasource '%s' for component '%s' because 'property' " +
                                "attribute is not defined", datasource, component.getId()),
                        context.getFullFrameId());
            }

            component.setDatasource(ds, property);
        }
    }

    protected void loadMimeType(ResourceView.HasMimeType resource, Element resourceElement) {
        String mimeType = resourceElement.attributeValue("mimeType");
        if (StringUtils.isNotEmpty(mimeType)) {
            resource.setMimeType(mimeType);
        }
    }

    protected void loadStreamSettings(ResourceView.HasStreamSettings resource, Element resourceElement) {
        String cacheTime = resourceElement.attributeValue("cacheTime");
        if (StringUtils.isNotEmpty(cacheTime)) {
            resource.setCacheTime(Long.parseLong(cacheTime));
        }

        String bufferSize = resourceElement.attributeValue("bufferSize");
        if (StringUtils.isNotEmpty(bufferSize)) {
            resource.setBufferSize(Integer.parseInt(bufferSize));
        }
    }
}