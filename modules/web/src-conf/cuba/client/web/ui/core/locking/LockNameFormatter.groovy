/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.02.2010 15:02:19
 *
 * $Id$
 */
package cuba.client.web.ui.core.locking

import com.haulmont.cuba.core.global.MetadataProvider
import com.haulmont.cuba.core.global.MessageUtils

class LockNameFormatter implements com.haulmont.cuba.gui.components.Formatter {

  String format(Object value) {
    com.haulmont.chile.core.model.MetaClass mc = MetadataProvider.getSession().getClass(value)
    if (mc) {
      return MessageUtils.getEntityCaption(mc)
    } else
      return value;
  }
}
