/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.05.2009 18:33:40
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

import java.util.UUID;
import java.sql.SQLException;

public class PostgresUUID extends org.postgresql.util.PGobject
{
    private static final long serialVersionUID = -8115115840321643248L;

    public PostgresUUID(UUID uuid) throws SQLException {
        super();
        this.setType("uuid");
        this.setValue(uuid.toString());
    }
}
