package cuba.client.web.ui.report.definition.edit

import com.haulmont.cuba.gui.components.AbstractEditor
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.LookupField
import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.data.ValueListener
import com.haulmont.cuba.report.DataSetType
import com.haulmont.cuba.gui.components.Label
import com.haulmont.cuba.gui.components.Component

public class DatasetEditor extends AbstractEditor {

    def DatasetEditor(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        super.init(params);
        LookupField lookupField = getComponent('type')
        TextField textField = getComponent('text')
        Label label = getComponent('dataSet_text')

        lookupField.addListener(
                [
                        valueChanged: {Object source, String property, Object prevValue, Object value ->
                            [textField, label].each {Component c -> c.visible = !(value && [DataSetType.SINGLE, DataSetType.MULTI].contains(value))}
                        }
                ] as ValueListener
        )
    }


}