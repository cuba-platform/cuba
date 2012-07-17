/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 22.05.2009 9:42:27
 *
 * $Id: UUIDDatatype.java 4904 2011-05-31 09:45:58Z krivopustov $
 */
package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import org.apache.commons.lang.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

public class UUIDDatatype implements Datatype<UUID>{

    public static String NAME = "uuid";

    public String getName() {
        return NAME;
    }

    public Class getJavaClass() {
        return UUID.class;
    }

    public String format(UUID value) {
        return value.toString();
    }

    public String format(UUID value, Locale locale) {
        return format(value);
    }

    public UUID parse(String value) throws ParseException {
        return StringUtils.isBlank(value) ? null : UUID.fromString(value.trim());
    }

    public UUID parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    public UUID read(ResultSet resultSet, int index) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void write(PreparedStatement statement, int index, UUID value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getSqlType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
