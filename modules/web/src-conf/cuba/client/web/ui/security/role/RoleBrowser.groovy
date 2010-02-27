package cuba.client.web.ui.security.role

import com.haulmont.cuba.gui.components.AbstractLookup
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.TableActionsHelper
import com.haulmont.cuba.web.rpt.WebExportDisplay

class RoleBrowser extends AbstractLookup {

  public RoleBrowser(IFrame frame) {
    super(frame)
  }

  protected void init(Map<String, Object> params) {
    Table table = getComponent("roles")

    TableActionsHelper helper = new TableActionsHelper(this, table)
    helper.createCreateAction()
    helper.createEditAction()
    helper.createRemoveAction()
    helper.createExcelAction(new WebExportDisplay())

    table.refresh()

    String windowOpener = params['param$windowOpener']
    if (windowOpener == 'sec$User.edit') {
      table.multiSelect = true
    }
  }

}
