/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UUIDDatatype implements Datatype<UUID>{

    public static String NAME = "uuid";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return UUID.class;
    }

    @Nonnull
    @Override
    public String format(UUID value) {
        return value == null ? "" : value.toString();
    }

    @Nonnull
    public String format(UUID value, Locale locale) {
        return format(value);
    }

    @Override
    public UUID parse(String value) throws ParseException {
        return StringUtils.isBlank(value) ? null : UUID.fromString(value.trim());
    }

    @Override
    public UUID parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    @Override
    public UUID read(ResultSet resultSet, int index) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(PreparedStatement statement, int index, UUID value) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSqlType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return NAME;
    }
}