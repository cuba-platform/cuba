/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.export.ExportDataProvider;

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
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
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
    public void setSource(URL src) {
        try {
            BufferedImage image = ImageIO.read(src);
            setContents(image, src.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSource(String src) {
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
    }

    @Override
    public void setSource(String fileName, InputStream src) {
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
    }

    @Override
    public void setSource(String fileName, ExportDataProvider dataProvider) {
        try {
            BufferedImage image = ImageIO.read(dataProvider.provide());
            setContents(image, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dataProvider.close();
        }
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
