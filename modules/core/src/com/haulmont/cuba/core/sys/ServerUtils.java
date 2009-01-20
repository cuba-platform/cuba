/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.01.2009 9:20:48
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

import java.net.URL;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class ServerUtils
{
    private static Method getCachedFileMethod;
    private static Method getRealURLMethod;

    public static URL translateUrl(URL url) {
        if ("vfszip".equals(url.getProtocol())) {
            try {
                if (getCachedFileMethod == null)
                    getCachedFileMethod = VFS.class.getMethod("getCachedFile", URL.class);
                VirtualFile vf = (VirtualFile) getCachedFileMethod.invoke(null, url);

                if (getRealURLMethod == null)
                    getRealURLMethod = VFSUtils.class.getMethod("getRealURL", VirtualFile.class);
                URL result = (URL) getRealURLMethod.invoke(null, vf);

                return result;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return url;
        }
    }


}
