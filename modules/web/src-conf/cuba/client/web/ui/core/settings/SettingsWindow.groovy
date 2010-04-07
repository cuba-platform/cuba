/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.01.2010 17:19:31
 *
 * $Id$
 */
package cuba.client.web.ui.core.settings

import com.haulmont.cuba.gui.components.AbstractWindow
import com.haulmont.cuba.gui.components.OptionsGroup
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.ActionAdapter
import com.haulmont.cuba.web.app.UserSettingHelper
import com.haulmont.cuba.web.AppWindow.Mode
import com.haulmont.cuba.gui.UserSessionClient
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.gui.WindowManager

class SettingsWindow extends AbstractWindow {

  def SettingsWindow(IFrame frame) {
    super(frame);
  }

  protected void init(Map<String, Object> params) {
    Mode mode = UserSettingHelper.loadAppWindowMode()
    String msgTabbed = getMessage('modeTabbed')
    String msgSingle = getMessage('modeSingle')

    OptionsGroup modeOptions = getComponent("mainWindowMode")
    modeOptions.setOptionsList([msgTabbed, msgSingle])
    if (mode == Mode.TABBED)
      modeOptions.setValue(msgTabbed)
    else
      modeOptions.setValue(msgSingle)

    Button changePasswBtn = getComponent("changePassw")
    User user = UserSessionClient.getUserSession().getUser()
    changePasswBtn.setAction(
            new ActionAdapter("changePassw", getMessagesPack(),
                    [
                            "actionPerform": {
                              openEditor('sec$User.changePassw', user, WindowManager.OpenType.DIALOG)
                            }
                    ]
            )
    )
    if (!user.equals(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser())) {
      changePasswBtn.setEnabled(false)
    }


    Button okBtn = getComponent("ok")
    okBtn.setAction(new ActionAdapter('ok', getMessagesPack(), [
            'actionPerform': {
              Mode m = modeOptions.getValue() == msgTabbed ? Mode.TABBED : Mode.SINGLE
              UserSettingHelper.saveAppWindowMode(m)
              showNotification(getMessage('modeChangeNotification'), IFrame.NotificationType.HUMANIZED)
              close('ok')
            }
    ]))

    Button cancelBtn = getComponent("cancel")
    cancelBtn.setAction(new ActionAdapter('cancel', getMessagesPack(), [
            'actionPerform': { close('cancel') }
    ]))
  }
}
