package cuba.client.web.ui.security.role

import com.haulmont.cuba.gui.components.AbstractLookup
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.TableActionsHelper
import com.haulmont.cuba.web.rpt.WebExportDisplay
import com.haulmont.cuba.web.app.LinkColumnHelper
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.gui.components.Window
import com.haulmont.cuba.gui.WindowManager

class RoleBrowser extends AbstractLookup {

  private Table table

  public RoleBrowser(IFrame frame) {
    super(frame)
  }

  protected void init(Map<String, Object> params) {
    table = getComponent("roles")

    TableActionsHelper helper = new TableActionsHelper(this, table)
    helper.createCreateAction()
    helper.createEditAction()
    helper.createRemoveAction()
    helper.createExcelAction(new WebExportDisplay())
    
    LinkColumnHelper.initColumn(table, "name",
            { Entity entity ->
              openRole(entity)
            } as LinkColumnHelper.Handler);

    table.refresh()

    String windowOpener = params['param$windowOpener']
    if (windowOpener == 'sec$User.edit') {
      table.multiSelect = true
    }
  }

  private void openRole(Entity entity) {
    Window window = openEditor('sec$Role.edit', entity, WindowManager.OpenType.THIS_TAB)
    window.addListener({String actionId ->
      if (actionId == Window.COMMIT_ACTION_ID) {
        table.getDatasource().refresh()
      }
    } as Window.CloseListener)
  }

}
