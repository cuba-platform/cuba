package spec.cuba.web.components.webabstractactionsholdercomponent

import com.haulmont.cuba.gui.components.actions.BaseAction
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.webabstractactionsholdercomponent.screen.WaahcTestScreen

class WebAbstractActionsHolderComponentTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.webabstractactionsholdercomponent.screen'])
    }

    def "Add a new action to the table when having updatable captions of actions"() {
        showMainWindow()

        def waahcScreen = screens.create(WaahcTestScreen)
        waahcScreen.show()

        def table = waahcScreen.table
        def actionsCount = table.actions.size();

        def actionToAdd = new BaseAction("hello")
                .withCaption("Some caption")

        when: 'Some of action has update its caption and new action added to the table'
        waahcScreen.test.setCaption("Some caption1")
        table.addAction(actionToAdd)

        then: 'No exception must be thrown'
        noExceptionThrown()

        and: 'Count of actions should be incremented'
        waahcScreen.table.actions.size() == actionsCount + 1
    }
}
