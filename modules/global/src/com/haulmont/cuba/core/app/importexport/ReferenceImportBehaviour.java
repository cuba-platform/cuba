/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.importexport;

/**
 * Enum describes a behavior for references during entities import: missing reference can be ignored
 * or an exception can be thrown.
 *
 * @author gorbunkov
 * @version $Id$
 */
public enum ReferenceImportBehaviour {
    IGNORE_MISSING,
    ERROR_ON_MISSING
}
