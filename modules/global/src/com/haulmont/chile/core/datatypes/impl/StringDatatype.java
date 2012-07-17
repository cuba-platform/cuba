/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Locale;

public class StringDatatype implements Datatype<String> {

	public static String NAME = "string";
	
	public String format(String value) {
		return value;
	}

    @Override
    public String format(String value, Locale locale) {
        return value == null ? "" : value;
    }

	public Class getJavaClass() {
		return String.class;
	}

	public String getName() {
		return NAME;
	}

	public int getSqlType() {
		return Types.VARCHAR;
	}

	public String parse(String value) {
		return value;
	}

    public String parse(String value, Locale locale) throws ParseException {
        return value;
    }

	public String read(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getString(index);
	}

	public void write(PreparedStatement statement, int index, String value) throws SQLException {
		if (value == null) {
			statement.setString(index, null);
		} else {
			statement.setString(index, value);
		}
	}

    @Override
    public String toString() {
        return NAME;
    }
}
