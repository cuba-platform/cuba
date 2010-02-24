package cuba.client.web.ui.core.locking

import com.haulmont.cuba.core.app.LockService
import com.haulmont.cuba.core.global.LockInfo
import com.haulmont.cuba.gui.ServiceLocator
import com.haulmont.cuba.gui.components.*

class LockBrowser extends AbstractWindow {

  public LockBrowser(IFrame frame) {
    super(frame)
  }

  protected void init(Map<String, Object> params) {
    Table table = getComponent("locks")

    TableActionsHelper helper = new TableActionsHelper(this, table)
    helper.createRefreshAction()

    table.addAction(
            new ActionAdapter('unlock', getMessagesPack(),
                    [
                            actionPerform: {
                              LockInfo lockInfo = table.getSingleSelected()
                              if (lockInfo) {
                                LockService service = ServiceLocator.lookup(LockService.NAME)
                                service.unlock(lockInfo.entityName, lockInfo.entityId)
                                table.refresh()
                              }
                            }
                    ]
            )
    )

    table.refresh()
  }
}