/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import org.apache.commons.codec.binary.Base64;

import java.text.ParseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Locale;

public class ByteArrayDatatype implements Datatype<byte[]> {

    public static final String NAME = "byteArray";

    public String getName() {
        return NAME;
    }

    public Class getJavaClass() {
        return byte[].class;
    }

    public String format(byte[] value) {
        if (value == null)
            return null;

        return new String(Base64.encodeBase64(value));
    }

    public String format(byte[] value, Locale locale) {
        if (value == null)
            return "";

        return format(value);
    }

    public byte[] parse(String value) throws ParseException {
        if (value == null || value.length() == 0)
            return null;

        return Base64.decodeBase64(value.getBytes());
    }

    public byte[] parse(String value, Locale locale) throws ParseException {
        return parse(value);
    }

    public byte[] read(ResultSet resultSet, int index) throws SQLException {
        byte[] value = resultSet.getBytes(index);
        return resultSet.wasNull() ? null : value;
    }

    public void write(PreparedStatement statement, int index, byte[] value) throws SQLException {
        if (value == null) {
            statement.setNull(index, getSqlType());
        } else {
            statement.setBytes(index, value);
        }
    }

    public int getSqlType() {
        return Types.VARBINARY;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
