/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.toolkit.gwt.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

/**
 * A collection of properties of arbitrary type, for interoperability with
 * non-GWT JavaScript libraries.
 */
public abstract class Properties extends JavaScriptObject {
  /**
   * An exception thrown by the get methods when the key is found but the
   * value has an unexpected type.
   */
  @SuppressWarnings("serial")
  public static class TypeException extends Exception {
    private TypeException(String key, String expected, String actual) {
      super("Properties.get" + expected + "(" + key +
          ") failed.  Unexpected type : " + actual + ".");
    }

    private TypeException() {
    }
  }

  /**
   * Create an empty Properties object.
   * @return An empty Properties object.
   */
  public static Properties create() {
    return JavaScriptObject.createObject().cast();
  }

  protected Properties() {
  }

  /**
   * Get a Boolean value mapped to the specified key.
   *
   * @param key The name of the Boolean property.
   * @return A Boolean value, or null if the key is not found.
   * @throws TypeException if the key is found but the object returned
   * is not a Boolean.
   */
  public final Boolean getBoolean(String key) throws TypeException {
    if (containsKey(key)) {
      String type = typeof(key);
      if (type.equals("boolean")) {
        return nativeGetBoolean(key);
      } else {
        throw new TypeException(key, "Boolean", type);
      }
    } else {
      return null;
    }
  }

  /**
   * Get a Date object mapped to the specified key.
   *
   * @param key The name of the Date property.
   * @return A Date object, or null if the key is not found.
   * @throws JavaScriptException if the key is found but the object returned
   * is not a Date.
   * @throws TypeException If the key is found but the value is not an object.
   */
  public final Date getDate(String key)
      throws JavaScriptException, TypeException {
    return JsDate.toJava((JsDate) getObject(key));
  }

  /**
   * Get a Double value mapped to the specified key.
   *
   * @param key The name of the Double property.
   * @return A Double value, or null if the key is not found.
   * @throws TypeException If the key is found but the value is not a
   * number (integers are fine).
   */
  public final Double getNumber(String key) throws TypeException {
    if (containsKey(key)) {
      String type = typeof(key);
      if (type.equals("number")) {
        return nativeGetNumber(key);
      } else {
        throw new TypeException(key, "Number", type);
      }
    } else {
      return null;
    }
  }

  /**
   * Get a JavaScriptObject mapped to the specified key.
   *
   * @param key The name of the JavaScriptObject property.
   * @return A JavaScriptObject, or null if the key is not found.
   * @throws TypeException If the key is found but the value is not a
   * JavaScriptObject.
   */
  public final JavaScriptObject getObject(String key) throws TypeException {
    if (containsKey(key)) {
      String type = typeof(key);
      if (type.equals("object")) {
        return nativeGetObject(key);
      } else {
        throw new TypeException(key, "Object", type);
      }
    } else {
      return null;
    }
  }

  /**
   * Get a String mapped to the specified key.
   *
   * @param key The name of the String property.
   * @return A String, or null if the key is not found.
   * @throws TypeException If the key is found but the value is not a
   * String.
   */
  public final String getString(String key) throws TypeException {
    if (containsKey(key)) {
      String type = typeof(key);
      if (type.equals("string")) {
        return nativeGetString(key);
      } else {
        throw new TypeException(key, "String", type);
      }
    } else {
      return null;
    }
  }

  /**
   * Remove the property at the specified key.
   *
   * @param key The name of the property to remove.
   */
  public final native void remove(String key) /*-{
    delete this[key];
  }-*/;

  /**
   * Set a property.
   *
   * @param key The name of the property.
   * @param value The value of the property.
   */
  public final void set(String key, Boolean value) {
    if (value == null) {
      remove(key);
    } else {
      setBoolean(key, value);
    }
  }

  /**
   * Set a property.
   *
   * @param key The name of the property.
   * @param value The value of the property.
   */
  public final void set(String key, Date value) {
    set(key, JsDate.toJs(value));
  }

  /**
   * Set a property.
   *
   * @param key The name of the property.
   * @param value The value of the property.
   */
  public final void set(String key, Double value) {
    if (value == null) {
      remove(key);
    } else {
      setNumber(key, value);
    }
  }

  /**
   * Set a property.
   *
   * @param key The name of the property.
   * @param value The value of the property.
   */
  public final native void set(String key, JavaScriptObject value) /*-{
    this[key] = value;
  }-*/;

  /**
   * Set a property.
   *
   * @param key The name of the property.
   * @param value The value of the property.
   */
  public final native void set(String key, String value) /*-{
    this[key] = value;
  }-*/;

  /**
   * Executes the JavaScript typeof operator against the property with the
   * given key.  Note that the typeof undefined is "undefined" and the typeof
   * null is "object".
   *
   * @param key The name of the property to type check.
   * @return The JavaScript type of the property, as defined by the JavaScipt
   * typeof operator.
   */
  public final native String typeof(String key) /*-{
    return typeof this[key];
  }-*/;

  private native boolean containsKey(String key) /*-{
    return this[key] != null;
  }-*/;

  private native boolean nativeGetBoolean(String key) /*-{
    return this[key];
  }-*/;

  private native double nativeGetNumber(String key) /*-{
    return this[key];
  }-*/;

  private native JavaScriptObject nativeGetObject(String key) /*-{
    return this[key];
  }-*/;

  private native String nativeGetString(String key) /*-{
    return this[key];
  }-*/;

  private native void setBoolean(String key, boolean value) /*-{
    this[key] = value;
  }-*/;

  private native void setNumber(String key, double value) /*-{
    this[key] = value;
  }-*/;
}
