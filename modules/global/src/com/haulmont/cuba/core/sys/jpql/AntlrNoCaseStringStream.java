/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;

/*
 * ANTLRNoCaseFileStream.java
 *
 * Created on January 25, 2008, 2:12 PM
 * @author Jim Idle
 */

/**
 * Author: Alexander Chevelev
 * Date: 16.10.2010
 * Time: 12:56:36
 */



public class AntlrNoCaseStringStream extends ANTLRStringStream {
    public AntlrNoCaseStringStream(String str) {
        super(str);
    }

    public int LA(int i) {
        if (i == 0) {
            return 0; // undefined
        }
        if (i < 0) {
            i++; // e.g., translate LA(-1) to use offset 0
        }

        if ((p + i - 1) >= n) {

            return CharStream.EOF;
        }
        return Character.toUpperCase(data[p + i - 1]);
    }
}
