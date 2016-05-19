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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.export.ExportDataProvider;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 */
public class DesktopEmbedded extends DesktopAbstractComponent<JPanel> implements Embedded {

    private Type type; // only IMAGE currently supported, hope object and browser will never needed for desktop
    private Image image;

    public DesktopEmbedded() {
        type = Type.IMAGE;
        impl = new ImagePanel();
    }

    private void setContents(BufferedImage image, String description) {
        this.image = image;
        impl.setToolTipText(description);
        impl.repaint();
        // todo when width = -1 or height = -1 set width to image.getWidth() and height to image.getHeight(), need screen to test it
    }

    @Override
    public void setMIMEType(String mt) {
        // do nothing, desktop components don't render any MIME types
    }

    @Override
    public void setSource(@Nullable URL src) {
        if (src != null) {
            try {
                BufferedImage image = ImageIO.read(src);
                setContents(image, src.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            setContents(null, null);
        }
    }

    @Override
    public void setSource(@Nullable String src) {
        if (src != null) {
            // the same as in WebEmbedded
            if (src.startsWith("http") || src.startsWith("https")) {
                try {
                    setSource(new URL(src));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new UnsupportedOperationException("Unsupported source for image");
            }
        } else {
            setContents(null, null);
        }
    }

    @Override
    public void setSource(String fileName,@Nullable InputStream src) {
        if (src != null) {
            try {
                BufferedImage image = ImageIO.read(src);
                setContents(image, fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    src.close();
                } catch (IOException e) {
                    // nothing
                }
            }
        } else {
            setContents(null, null);
        }
    }

    @Override
    public void setSource(String fileName,@Nullable ExportDataProvider dataProvider) {
        if (dataProvider != null) {
            try {
                BufferedImage image = ImageIO.read(dataProvider.provide());
                setContents(image, fileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                dataProvider.close();
            }
        } else {
            setContents(null, null);
        }
    }

    @Override
    public void setRelativeSource(String src) {
        throw new UnsupportedOperationException("setRelativeSource is not implemented for DesktopEmbedded");
    }

    @Override
    public void resetSource() {
        setContents(null, null);
    }

    @Override
    public void addParameter(String name, String value) {
        throw new UnsupportedOperationException("Any embedded except of image is not supported");
    }

    @Override
    public void removeParameter(String name) {
        throw new UnsupportedOperationException("Any embedded except of image is not supported");
    }

    @Override
    public Map<String, String> getParameters() {
        throw new UnsupportedOperationException("Any embedded except of image is not supported");
    }

    @Override
    public void setType(Type t) {
        if (t != Type.IMAGE) {
            throw new UnsupportedOperationException("Any embedded except of image is not supported");
        }
        type = t;
    }

    @Override
    public Type getType() {
        return type;
    }

    private class ImagePanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            if (image != null) {
                // automatically resizes to fit width & height
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }
}
