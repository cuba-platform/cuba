/*
* Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: Konstantin Krivopustov
* Created: 14.12.2009 15:34:59
*
* $Id$
*/
import com.haulmont.cuba.core.SecurityProvider

return SecurityProvider.currentUserSession().getUser().getLogin() == 'admin'
