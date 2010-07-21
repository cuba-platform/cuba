/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 14:35:35
 *
 * $Id$
 */
package cuba.client.web.ui.report.definition.edit

import com.haulmont.cuba.gui.components.AbstractEditor
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.report.BandDefinition
import com.haulmont.cuba.report.DataSet
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.components.ActionAdapter
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.TableActionsHelper
import com.haulmont.cuba.gui.components.ValueProvider

public class BandDefinitionEditor extends AbstractEditor {

    def BandDefinitionEditor(IFrame frame) {
        super(frame);
    }

    def void setItem(Entity item) {
        BandDefinition definition = (BandDefinition) item
        definition.setParentBandDefinition(parentDefinition)
        definition.position = position ?: definition.position
        super.setItem(definition);
    }

    private BandDefinition parentDefinition
    private Integer position

    protected void init(Map<String, Object> params) {
        super.init(params);
        parentDefinition = params['param$parentDefinition']
        position = (Integer) params['param$position']

        Table table = getComponent('dataSets')
        TableActionsHelper tah = new TableActionsHelper(this, table)
        tah.createCreateAction([
                getValues: {
                    return ['bandDefinition': getItem()]
                },
                getParameters: {
                    return [:]
                }
        ] as ValueProvider)
        tah.createEditAction()
        tah.createRemoveAction(false)
    }
}
