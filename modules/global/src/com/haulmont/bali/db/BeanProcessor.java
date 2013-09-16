/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <code>BeanProcessor</code> matches column names to bean property names
 * and converts <code>ResultSet</code> columns into objects for those bean
 * properties.  Subclasses should override the methods in the processing chain
 * to customize behavior.
 * </p>
 *
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @see BasicRowProcessor
 *
 * @since DbUtils 1.1
 */
public class BeanProcessor {

    /**
     * Special array value used by <code>mapColumnsToProperties</code> that
     * indicates there is no bean property that matches a column from a
     * <code>ResultSet</code>.
     */
    protected static final int PROPERTY_NOT_FOUND = -1;

    /**
     * Set a bean's primitive properties to these defaults when SQL NULL
     * is returned.  These are the same as the defaults that ResultSet get*
     * methods return in the event of a NULL column.
     */
    private static final Map primitiveDefaults = new HashMap();

    static {
        primitiveDefaults.put(Integer.TYPE, new Integer(0));
        primitiveDefaults.put(Short.TYPE, new Short((short) 0));
        primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
        primitiveDefaults.put(Float.TYPE, new Float(0));
        primitiveDefaults.put(Double.TYPE, new Double(0));
        primitiveDefaults.put(Long.TYPE, new Long(0));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, new Character('\u0000'));
    }

    /**
     * Constructor for BeanProcessor.
     */
    public BeanProcessor() {
        super();
    }

    /**
     * Convert a <code>ResultSet</code> row into a JavaBean.  This
     * implementation uses reflection and <code>BeanInfo</code> classes to
     * match column names to bean property names.  Properties are matched to
     * columns based on several factors:
     * <br/>
     * <ol>
     *     <li>
     *     The class has a writable property with the same name as a column.
     *     The name comparison is case insensitive.
     *     </li>
     *
     *     <li>
     *     The column type can be converted to the property's set method
     *     parameter type with a ResultSet.get* method.  If the conversion fails
     *     (ie. the property was an int and the column was a Timestamp) an
     *     SQLException is thrown.
     *     </li>
     * </ol>
     *
     * <p>
     * Primitive bean properties are set to their defaults when SQL NULL is
     * returned from the <code>ResultSet</code>.  Numeric fields are set to 0
     * and booleans are set to false.  Object bean properties are set to
     * <code>null</code> when SQL NULL is returned.  This is the same behavior
     * as the <code>ResultSet</code> get* methods.
     * </p>
     *
     * @param rs ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @throws SQLException if a database access error occurs
     * @return the newly created bean
     */
    public Object toBean(ResultSet rs, Class type) throws SQLException {

        PropertyDescriptor[] props = this.propertyDescriptors(type);

        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        return this.createBean(rs, type, props, columnToProperty);
    }

    /**
     * Convert a <code>ResultSet</code> into a <code>List</code> of JavaBeans.
     * This implementation uses reflection and <code>BeanInfo</code> classes to
     * match column names to bean property names. Properties are matched to
     * columns based on several factors:
     * <br/>
     * <ol>
     *     <li>
     *     The class has a writable property with the same name as a column.
     *     The name comparison is case insensitive.
     *     </li>
     *
     *     <li>
     *     The column type can be converted to the property's set method
     *     parameter type with a ResultSet.get* method.  If the conversion fails
     *     (ie. the property was an int and the column was a Timestamp) an
     *     SQLException is thrown.
     *     </li>
     * </ol>
     *
     * <p>
     * Primitive bean properties are set to their defaults when SQL NULL is
     * returned from the <code>ResultSet</code>.  Numeric fields are set to 0
     * and booleans are set to false.  Object bean properties are set to
     * <code>null</code> when SQL NULL is returned.  This is the same behavior
     * as the <code>ResultSet</code> get* methods.
     * </p>
     *
     * @param rs ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @throws SQLException if a database access error occurs
     * @return the newly created List of beans
     */
    public List toBeanList(ResultSet rs, Class type) throws SQLException {
        List results = new ArrayList();

        if (!rs.next()) {
            return results;
        }

        PropertyDescriptor[] props = this.propertyDescriptors(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        do {
            results.add(this.createBean(rs, type, props, columnToProperty));
        } while (rs.next());

        return results;
    }

    /**
     * Creates a new object and initializes its fields from the ResultSet.
     *
     * @param rs The result set.
     * @param type The bean type (the return type of the object).
     * @param props The property descriptors.
     * @param columnToProperty The column indices in the result set.
     * @return An initialized object.
     * @throws SQLException if a database error occurs.
     */
    private Object createBean(ResultSet rs, Class type,
            PropertyDescriptor[] props, int[] columnToProperty)
            throws SQLException {

        Object bean = this.newInstance(type);

        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            PropertyDescriptor prop = props[columnToProperty[i]];
            Class propType = prop.getPropertyType();

            Object value = this.processColumn(rs, i, propType);

            if (propType != null && value == null && propType.isPrimitive()) {
                value = primitiveDefaults.get(propType);
            }

            this.callSetter(bean, prop, value);
        }

        return bean;
    }

    /**
     * Calls the setter method on the target object for the given property.
     * If no setter method exists for the property, this method does nothing.
     * @param target The object to set the property on.
     * @param prop The property to set.
     * @param value The value to pass into the setter.
     * @throws SQLException if an error occurs setting the property.
     */
    private void callSetter(Object target, PropertyDescriptor prop, Object value)
            throws SQLException {

        Method setter = prop.getWriteMethod();

        if (setter == null) {
            return;
        }

        Class[] params = setter.getParameterTypes();
        try {
            // convert types for some popular ones
            if (value != null) {
                if (value instanceof java.util.Date) {
                    if (params[0].getName().equals("java.sql.Date")) {
                        value = new java.sql.Date(((java.util.Date) value).getTime());
                    } else
                    if (params[0].getName().equals("java.sql.Time")) {
                        value = new java.sql.Time(((java.util.Date) value).getTime());
                    } else
                    if (params[0].getName().equals("java.sql.Timestamp")) {
                        value = new java.sql.Timestamp(((java.util.Date) value).getTime());
                    }
                }
            }

            // Don't call setter if the value object isn't the right type
            if (this.isCompatibleType(value, params[0])) {
                setter.invoke(target, new Object[] { value });
            } else {
              throw new SQLException(
                  "Cannot set " + prop.getName() + ": incompatible types.");
            }

        } catch (IllegalArgumentException e) {
            throw new SQLException(
                "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException(
                "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    /**
     * ResultSet.getObject() returns an Integer object for an INT column.  The
     * setter method for the property might take an Integer or a primitive int.
     * This method returns true if the value can be successfully passed into
     * the setter method.  Remember, Method.invoke() handles the unwrapping
     * of Integer into an int.
     *
     * @param value The value to be passed into the setter method.
     * @param type The setter's parameter type.
     * @return boolean True if the value is compatible.
     */
    private boolean isCompatibleType(Object value, Class type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (
            type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            return true;

        } else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            return true;

        } else if (
            type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            return true;

        } else if (
            type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            return true;

        } else if (
            type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            return true;

        } else {
            return false;
        }

    }

    /**
     * Factory method that returns a new instance of the given Class.  This
     * is called at the start of the bean creation process and may be
     * overridden to provide custom behavior like returning a cached bean
     * instance.
     *
     * @param c The Class to create an object from.
     * @return A newly created object of the Class.
     * @throws SQLException if creation failed.
     */
    protected Object newInstance(Class c) throws SQLException {
        try {
            return c.newInstance();

        } catch (InstantiationException e) {
            throw new SQLException(
                "Cannot create " + c.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                "Cannot create " + c.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Returns a PropertyDescriptor[] for the given Class.
     *
     * @param c The Class to retrieve PropertyDescriptors for.
     * @return A PropertyDescriptor[] describing the Class.
     * @throws SQLException if introspection failed.
     */
    private PropertyDescriptor[] propertyDescriptors(Class c)
        throws SQLException {
        // Introspector caches BeanInfo classes for better performance
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);

        } catch (IntrospectionException e) {
            throw new SQLException(
                "Bean introspection failed: " + e.getMessage());
        }

        return beanInfo.getPropertyDescriptors();
    }

    /**
     * The positions in the returned array represent column numbers.  The
     * values stored at each position represent the index in the
     * <code>PropertyDescriptor[]</code> for the bean property that matches
     * the column name.  If no bean property was found for a column, the
     * position is set to <code>PROPERTY_NOT_FOUND</code>.
     *
     * @param rsmd The <code>ResultSetMetaData</code> containing column
     * information.
     *
     * @param props The bean property descriptors.
     *
     * @throws SQLException if a database access error occurs
     *
     * @return An int[] with column index to property index mappings.  The 0th
     * element is meaningless because JDBC column indexing starts at 1.
     */
    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd,
            PropertyDescriptor[] props) throws SQLException {

        int cols = rsmd.getColumnCount();
        int columnToProperty[] = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnName(col);
            for (int i = 0; i < props.length; i++) {

                if (columnName.equalsIgnoreCase(props[i].getName())) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

    /**
     * Convert a <code>ResultSet</code> column into an object.  Simple
     * implementations could just call <code>rs.getObject(index)</code> while
     * more complex implementations could perform type manipulation to match
     * the column's type to the bean property type.
     *
     * <p>
     * This implementation calls the appropriate <code>ResultSet</code> getter
     * method for the given property type to perform the type conversion.  If
     * the property type doesn't match one of the supported
     * <code>ResultSet</code> types, <code>getObject</code> is called.
     * </p>
     *
     * @param rs The <code>ResultSet</code> currently being processed.  It is
     * positioned on a valid row before being passed into this method.
     *
     * @param index The current column index being processed.
     *
     * @param propType The bean property type that this column needs to be
     * converted into.
     *
     * @throws SQLException if a database access error occurs
     *
     * @return The object from the <code>ResultSet</code> at the given column
     * index after optional type processing or <code>null</code> if the column
     * value was SQL NULL.
     */
    protected Object processColumn(ResultSet rs, int index, Class propType)
        throws SQLException {

        if (propType.equals(String.class)) {
            return rs.getString(index);

        } else if (
            propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
            return new Integer(rs.getInt(index));

        } else if (
            propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
            return new Boolean(rs.getBoolean(index));

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            return new Long(rs.getLong(index));

        } else if (
            propType.equals(Double.TYPE) || propType.equals(Double.class)) {
            return new Double(rs.getDouble(index));

        } else if (
            propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            return new Float(rs.getFloat(index));

        } else if (
            propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            return new Short(rs.getShort(index));

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            return new Byte(rs.getByte(index));

        } else if (propType.equals(Timestamp.class)) {
            return rs.getTimestamp(index);

        } else {
            return rs.getObject(index);
        }

    }

}
