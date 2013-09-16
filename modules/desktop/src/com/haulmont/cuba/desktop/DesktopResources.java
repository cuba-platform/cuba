/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopResources {

    protected List<String> roots;

    protected Map<String, byte[]> cache = new HashMap<String, byte[]>();

    private Resources resources;

    private Log log = LogFactory.getLog(DesktopResources.class);

    public DesktopResources(List<String> roots, Resources resources) {
        this.resources = resources;
        this.roots = new ArrayList<String>(roots);
        Collections.reverse(this.roots);
    }

    public Icon getIcon(String name) {
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

        return new ImageIcon(bytes);
    }
}
