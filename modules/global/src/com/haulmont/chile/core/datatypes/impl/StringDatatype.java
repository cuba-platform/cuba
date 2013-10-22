/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
public class StringDatatype implements Datatype<String> {

	public static String NAME = "string";

    @Nonnull
    @Override
	public String format(String value) {
		return value == null ? "" : value;
	}

    @Nonnull
    @Override
    public String format(String value, Locale locale) {
        return format(value);
    }

    @Override
	public Class getJavaClass() {
		return String.class;
	}

    @Override
	public String getName() {
		return NAME;
	}

    @Override
	public int getSqlType() {
		return Types.VARCHAR;
	}

    @Override
	public String parse(String value) {
		return value;
	}

    @Override
    public String parse(String value, Locale locale) throws ParseException {
        return value;
    }

    @Override
	public String read(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getString(index);
	}

    @Override
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