/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.desktop.gui.components;

import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.events.EventRouter;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import org.jdesktop.swingx.JXImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

public class DesktopImage extends DesktopAbstractComponent<JXImageView> implements Image {

    private static final Logger log = LoggerFactory.getLogger(DesktopImage.class);

    protected static final Map<Class<? extends Resource>, Class<? extends Resource>> resourcesClasses;

    static {
        ImmutableMap.Builder<Class<? extends Resource>, Class<? extends Resource>> builder =
                new ImmutableMap.Builder<>();

        builder.put(UrlResource.class, DesktopUrlResource.class);
        builder.put(ClasspathResource.class, DesktopClasspathResource.class);
        builder.put(FileDescriptorResource.class, DesktopFileDescriptorResource.class);
        builder.put(FileResource.class, DesktopFileResource.class);
        builder.put(StreamResource.class, DesktopStreamResource.class);

        resourcesClasses = builder.build();
    }

    protected Resource resource;
    protected Runnable resourceUpdateHandler;

    protected Datasource datasource;
    protected MetaPropertyPath metaPropertyPath;

    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected WeakItemChangeListener weakItemChangeListener;

    protected ScaleMode scaleMode = ScaleMode.NONE;

    protected EventRouter eventRouter;
    protected MouseListener mouseListener;
    protected String alternateText;

    protected boolean scalingEnabled = true;

    public DesktopImage() {
        impl = new JXImageView();
        impl.setDragEnabled(false);
        impl.setEditable(false);
        impl.setBackgroundPainter((g, object, width, height) ->
                g.setBackground(Color.gray));

        impl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                impl.setImage(scaleImage(impl.getImage()));
            }
        });

        resourceUpdateHandler = () -> {
            java.awt.Image image = this.resource == null
                    ? null
                    : _getResource(resource);
            impl.setImage(image);
        };
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if ((datasource == null && property != null) || (datasource != null && property == null)) {
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");
        }

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null))) {
            return;
        }

        if (this.datasource != null) {
            metaPropertyPath = null;

            impl.setImage((java.awt.Image) null);

            //noinspection unchecked
            this.datasource.removeItemPropertyChangeListener(weakItemPropertyChangeListener);
            weakItemPropertyChangeListener = null;

            //noinspection unchecked
            this.datasource.removeItemChangeListener(weakItemChangeListener);
            weakItemChangeListener = null;

            this.datasource = null;
        }

        if (datasource != null) {
            //noinspection unchecked
            this.datasource = datasource;

            metaPropertyPath = AppBeans.get(MetadataTools.class)
                    .resolveMetaPropertyPathNN(datasource.getMetaClass(), property);

            updateComponent();

            itemPropertyChangeListener = e -> {
                if (e.getProperty().equals(metaPropertyPath.toString())) {
                    updateComponent();
                }
            };
            weakItemPropertyChangeListener = new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener);
            //noinspection unchecked
            this.datasource.addItemPropertyChangeListener(weakItemPropertyChangeListener);

            itemChangeListener = e ->
                    updateComponent();

            weakItemChangeListener = new WeakItemChangeListener(datasource, itemChangeListener);
            //noinspection unchecked
            datasource.addItemChangeListener(weakItemChangeListener);
        }
    }

    protected void updateComponent() {
        Object propertyValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
        Resource resource = createImageResource(propertyValue);

        updateValue(resource);
    }

    protected void updateValue(Resource value) {
        Resource oldValue = this.resource;
        if (oldValue != null) {
            ((DesktopAbstractResource) oldValue).setResourceUpdatedHandler(null);
        }

        this.resource = value;

        java.awt.Image image = null;
        if (value != null && ((DesktopAbstractResource) value).hasSource()) {
            image = _getResource(resource);
        }
        impl.setImage(image);

        if (value != null) {
            ((DesktopAbstractResource) value).setResourceUpdatedHandler(resourceUpdateHandler);
        }

        getEventRouter().fireEvent(SourceChangeListener.class, SourceChangeListener::sourceChanged,
                new SourceChangeEvent(this, oldValue, this.resource));
    }

    protected Resource createImageResource(Object propertyValue) {
        if (propertyValue == null) {
            return null;
        }

        if (propertyValue instanceof FileDescriptor) {
            FileDescriptorResource imageResource = createResource(FileDescriptorResource.class);
            imageResource.setFileDescriptor((FileDescriptor) propertyValue);
            return imageResource;
        }

        if (propertyValue instanceof byte[]) {
            StreamResource imageResource = createResource(StreamResource.class);
            Supplier<InputStream> streamSupplier = () ->
                    new ByteArrayDataProvider((byte[]) propertyValue).provide();
            imageResource.setStreamSupplier(streamSupplier);
            return imageResource;
        }

        throw new GuiDevelopmentException(
                "The Image component supports only FileDescriptor and byte[] datasource property value binding",
                getFrame().getId());
    }

    protected java.awt.Image _getResource(Resource resource) {
        try {
            BufferedImage image = ((DesktopAbstractResource) resource).getResource();
            scalingEnabled = true;
            return scaleImage(image);
        } catch (Exception e) {
            scalingEnabled = false;
            log.info("An error occurred while loading image", e);
            return getErrorMessageImage();
        }
    }

    protected BufferedImage getErrorMessageImage() {
        BufferedImage errorImage = new BufferedImage(400, 100, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = errorImage.getGraphics();

        graphics.setColor(new Color(214, 217, 224));
        graphics.fillRect(0, 0, 400, 100);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));

        if (alternateText == null || alternateText.isEmpty()) {
            graphics.drawString("An error occurred while loading image.", 10, 25);
            graphics.drawString("Check logs for more information.", 10, 45);
        } else {
            graphics.drawString(alternateText, 10, 25);
        }

        graphics.dispose();

        return errorImage;
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public ScaleMode getScaleMode() {
        return scaleMode;
    }

    @Override
    public void setScaleMode(ScaleMode scaleMode) {
        Preconditions.checkNotNullArgument(scaleMode);

        this.scaleMode = scaleMode;

        DesktopAbstractResource desktopResource = (DesktopAbstractResource) this.resource;
        if (desktopResource != null && desktopResource.hasSource()) {
            impl.setImage(_getResource(desktopResource));
        }
    }

    protected java.awt.Image scaleImage(java.awt.Image image) {
        if (image == null) {
            return null;
        }

        if (!scalingEnabled) {
            return image;
        }

        float implHeight = impl.getHeight();
        float implWidth = impl.getWidth();

        if (implHeight <= 0 || implWidth <= 0) {
            return image;
        }

        switch (scaleMode) {
            case FILL:
                return _scaleImage(image, implHeight, implWidth);
            case CONTAIN:
            case SCALE_DOWN:
                float newH = implHeight;
                float newW = implWidth;

                float scaleCoef;

                if (implHeight > implWidth) {
                    scaleCoef = implWidth / image.getWidth(impl);
                    newH = Math.round(image.getHeight(impl) * scaleCoef);
                } else {
                    scaleCoef = implHeight / image.getHeight(impl);
                    newW = Math.round(image.getWidth(impl) * scaleCoef);
                }
                return _scaleImage(image, newH, newW);
        }
        return image;
    }

    protected java.awt.Image _scaleImage(java.awt.Image image, float newHeight, float newWidth) {
        return image.getScaledInstance(Math.round(newWidth), Math.round(newHeight), java.awt.Image.SCALE_SMOOTH);
    }

    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    @Override
    public void addClickListener(ClickListener listener) {
        getEventRouter().addListener(ClickListener.class, listener);

        if (mouseListener == null) {
            mouseListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    getEventRouter().fireEvent(ClickListener.class, ClickListener::onClick,
                            new ClickEvent(DesktopImage.this, convertMouseEvent(e)));
                }
            };
        }
        impl.addMouseListener(mouseListener);
    }

    @Override
    public void removeClickListener(ClickListener listener) {
        getEventRouter().removeListener(ClickListener.class, listener);

        if (!getEventRouter().hasListeners(ClickListener.class)) {
            impl.removeMouseListener(mouseListener);
        }
    }

    @Override
    public Resource getSource() {
        return resource;
    }

    @Override
    public void setSource(Resource resource) {
        if (this.resource == null && resource == null
                || (this.resource != null && this.resource.equals(resource))) {
            return;
        }

        updateValue(resource);
    }

    @Override
    public <R extends Resource> R setSource(Class<R> type) {
        R resource = createResource(type);

        updateValue(resource);

        return resource;
    }

    @Override
    public <R extends Resource> R createResource(Class<R> type) {
        if (ThemeResource.class.isAssignableFrom(type) || RelativePathResource.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException("Theme and RelativePath Resources are not supported for desktop client.");
        }

        Class<? extends Resource> resourceClass = resourcesClasses.get(type);
        if (resourceClass == null) {
            throw new IllegalStateException(String.format("Can't find resource class for '%s'", type.getTypeName()));
        }

        try {
            return type.cast(resourceClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating the '%s' resource instance",
                    type.getTypeName()), e);
        }
    }

    @Override
    public void setAlternateText(String alternateText) {
        this.alternateText = alternateText;
    }

    @Override
    public String getAlternateText() {
        return alternateText;
    }

    @Override
    public void addSourceChangeListener(SourceChangeListener listener) {
        getEventRouter().addListener(SourceChangeListener.class, listener);
    }

    @Override
    public void removeSourceChangeListener(SourceChangeListener listener) {
        getEventRouter().removeListener(SourceChangeListener.class, listener);
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
    }

    @Override
    protected void setCaptionToComponent(String caption) {
        super.setCaptionToComponent(caption);

        requestContainerUpdate();
    }

    protected MouseEventDetails convertMouseEvent(MouseEvent event) {
        MouseEventDetails.MouseButton button = null;
        switch (event.getButton()) {
            case 1:
                button = MouseEventDetails.MouseButton.LEFT;
                break;
            case 2:
                button = MouseEventDetails.MouseButton.RIGHT;
                break;
            case 3:
                button = MouseEventDetails.MouseButton.MIDDLE;
                break;
        }

        MouseEventDetails details = new MouseEventDetails();
        details.setButton(button);

        details.setClientX(event.getXOnScreen());
        details.setClientY(event.getYOnScreen());

        details.setAltKey(event.isAltDown());
        details.setCtrlKey(event.isControlDown());
        details.setMetaKey(event.isMetaDown());
        details.setShiftKey(event.isShiftDown());
        details.setDoubleClick(event.getClickCount() == 2);

        details.setRelativeX(event.getX());
        details.setRelativeY(event.getY());

        return details;
    }
}
