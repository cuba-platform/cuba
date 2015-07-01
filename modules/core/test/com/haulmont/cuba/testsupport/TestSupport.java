/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import java.io.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TestSupport {

    public static <T> T reserialize(Serializable object) throws Exception {
        if (object == null)
            return null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.close();
        bos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        T result = (T) ois.readObject();
        ois.close();
        bis.close();

        return result;
    }
}
