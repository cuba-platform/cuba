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

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.Resources;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class DesktopResources {

    private Logger log = LoggerFactory.getLogger(DesktopResources.class);

    protected List<String> roots;

    protected Map<String, byte[]> cache = new HashMap<>();

    protected Resources resources;

    public DesktopResources(List<String> roots, Resources resources) {
        this.resources = resources;
        this.roots = new ArrayList<>(roots);

        Collections.reverse(this.roots);
    }

    public Image getImage(String name) {
        byte[] bytes = getImageBytes(name);

        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            log.warn("Unable to read image from bytes", e);
            return null;
        }
    }

    public Icon getIcon(String name) {
        byte[] bytes = getImageBytes(name);

        return new ImageIcon(bytes);
    }

    protected byte[] getImageBytes(String name) {
        if (!name.startsWith("/"))
            name = "/" + name;

        byte[] bytes = cache.get(name);
        if (bytes == null) {
            for (String root : roots) {
                InputStream stream = resources.getResourceAsStream(root + name);
                if (stream != null) {
                    try {
                        bytes = IOUtils.toByteArray(stream);
                        cache.put(name, bytes);
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        IOUtils.closeQuietly(stream);
                    }
                }
            }
            if (bytes == null) {
                log.warn("Resource " + name + " not found in " + roots);
                InputStream stream = resources.getResourceAsStream("/com/haulmont/cuba/desktop/res/nimbus/icons/attention.png");
                if (stream != null) {
                    try {
                        bytes = IOUtils.toByteArray(stream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        IOUtils.closeQuietly(stream);
                    }
                }
            }
        }
        return bytes;
    }
}