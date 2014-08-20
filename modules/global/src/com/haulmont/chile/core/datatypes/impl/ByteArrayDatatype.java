/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ByteArrayDatatype implements Datatype<byte[]> {

    public static final String NAME = "byteArray";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return byte[].class;
    }

    @Nonnull
    @Override
    public String format(byte[] value) {
        if (value == null)
            return "";

        return new String(Base64.encodeBase64(value), StandardCharsets.UTF_8);
    }

    @Nonnull
    @Override
    public String format(byte[] value, Locale locale) {
        if (value == null)
            return "";

        return format(value);
    }

    @Override
    public byte[] parse(String value) throws ParseException {
        if (value == null || value.length() == 0)
            return null;

        return Base64.decodeBase64(value.getBytes());
    }

    @Override
    public byte[] parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    @Override
    public byte[] read(ResultSet resultSet, int index) throws SQLException {
        byte[] value = resultSet.getBytes(index);
        return resultSet.wasNull() ? null : value;
    }

    @Override
    public void write(PreparedStatement statement, int index, byte[] value) throws SQLException {
        if (value == null) {
            statement.setNull(index, getSqlType());
        } else {
            statement.setBytes(index, value);
        }
    }

    @Override
    public int getSqlType() {
        return Types.VARBINARY;
    }

    @Override
    public String toString() {
        return NAME;
    }
}