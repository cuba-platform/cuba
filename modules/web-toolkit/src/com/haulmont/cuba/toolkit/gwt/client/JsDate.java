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

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

/**
 * Wraps a JavaScript Date object.
 */
public class JsDate extends JavaScriptObject {
  /**
   * Create a JsDate with the current time.
   *
   * @return A JsDate with the current time.
   */
  public static native JsDate create() /*-{
    var result = new $wnd.Date();
    // Safari bug: see issue 219
    result.constructor = $wnd.Date;
    return result;
  }-*/;

  /**
   * Create a date given the time in milliseconds.
   *
   * @param time A time value in milliseconds.  Uses the double type for
   * efficiency.
   * @return A JsDate with the given time.
   */
  public static native JsDate create(double time) /*-{
    var result = new $wnd.Date(time);
    // Safari bug: see issue 219
    result.constructor = $wnd.Date;
    return result;
  }-*/;
  /**
   * Tests if the JavaScriptObject can be cast to JsDate.  Checks if the
   * underlying JavaScript object has a method called getTime() that returns
   * a number.
   *
   * @param js A JavaScriptObject that may or may not be wrapping a JavaScript
   * Date object.
   * @return false if the JavaScriptObject is null or if it cannot be cast
   * to JsDate, otherwise true.
   */
  public static native boolean isDate(JavaScriptObject js) /*-{
    var result = false;
    if (js != null) {
      if (typeof js.getTime == 'function') {
        var time = js.getTime();
        if (typeof time == 'number') {
          result = true;
        }
      }
    }
    return result;
  }-*/;

  /**
   * Converts a JsDate to a Java Date.  If the JsDate is null, the function
   * returns null.
   *
   * @param js A JsDate.
   * @return A Java Date object, or null, if js is null.
   */
  public static Date toJava(JsDate js) {
    return js == null ? null : new Date(js.getTime());
  }

  /**
   * Converts a Java Date to a JsDate.  If the Date is null, the function
   * returns null.
   *
   * @param java A Java Date.
   * @return An analogous JsDate, or null if the original Java Date was null.
   */
  public static JsDate toJs(Date java) {
    return java == null ? null : create(java.getTime());
  }

  protected JsDate() {
  }

  /**
   * A wrapper for the JavaScript Date's getTime() method.
   *
   * @return The time in milliseconds corresponding to this JsDate.
   */
  public final long getTime() {
    return (long) doubleTime();
  }

  private native double doubleTime() /*-{
    return this.getTime();
  }-*/;
}
