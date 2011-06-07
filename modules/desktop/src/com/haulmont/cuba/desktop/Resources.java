/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.global.ScriptingProvider;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class Resources {

    protected List<String> roots;

    protected Map<String, byte[]> cache = new HashMap<String, byte[]>();

    private Log log = LogFactory.getLog(Resources.class);

    public Resources(String locations) {
        StrTokenizer tokenizer = new StrTokenizer(locations);
        roots = tokenizer.getTokenList();
        Collections.reverse(roots);
    }

    public Icon getIcon(String name) {
        if (!name.startsWith("/"))
            name = "/" + name;

        byte[] bytes = cache.get(name);
        if (bytes == null) {
            for (String root : roots) {
                InputStream stream = ScriptingProvider.getResourceAsStream(root + name);
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
                InputStream stream = ScriptingProvider.getResourceAsStream("/com/haulmont/cuba/desktop/res/icons/attention.png");
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
