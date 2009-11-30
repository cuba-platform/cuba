/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.11.2009 13:05:44           
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components

import com.haulmont.cuba.gui.components.AbstractAction
import com.haulmont.cuba.gui.components.Component

public class ActionAdapter extends AbstractAction {

  private Map<String, Closure> methods

  def ActionAdapter(String id, Map<String, Closure> methods) {
    super(id)
    this.methods = methods
  }

  public void actionPerform(Component component) {
    Closure closure = methods['actionPerform']
    closure?.call(component)
  }

  public String getCaption() {
    Closure closure = methods['getCaption']
    if (closure)
      return closure.call()
    else
      return super.getCaption()
  }

  public String getIcon() {
    Closure closure = methods['getIcon']
    if (closure)
      return closure.call()
    else
      return super.getIcon()
  }

  public boolean isEnabled() {
    Closure closure = methods['getIcon']
    if (closure)
      return closure.call()
    else
      return super.isEnabled()
  }


}