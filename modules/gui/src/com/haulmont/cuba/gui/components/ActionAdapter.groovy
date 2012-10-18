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

import com.haulmont.cuba.core.global.MessageProvider

/**
 * @deprecated Use anonymous classes
 */
@Deprecated
public class ActionAdapter extends AbstractAction {

  private Map<String, Closure> methods
  private String messagesPack

  def ActionAdapter(String id, Map<String, Closure> methods) {
    super(id)
    this.methods = methods
  }

  def ActionAdapter(String id, String messagesPack, Map<String, Closure> methods) {
    super(id)
    this.methods = methods
    this.messagesPack = messagesPack
  }

  public void actionPerform(Component component) {
    Closure closure = methods['actionPerform']
    closure?.call(component)
  }

  public String getCaption() {
    Closure closure = methods['getCaption']
    if (closure)
      return closure.call()
    else if (messagesPack)
      return MessageProvider.getMessage(messagesPack, id)
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
    Closure closure = methods['isEnabled']
    if (closure)
      return closure.call()
    else
      return super.isEnabled()
  }
}