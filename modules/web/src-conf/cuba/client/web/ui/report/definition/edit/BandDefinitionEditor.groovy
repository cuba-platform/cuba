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

import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.MessageProvider
import com.haulmont.cuba.gui.AppConfig
import com.haulmont.cuba.gui.UserSessionClient
import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport
import com.haulmont.cuba.gui.autocomplete.JpqlSuggestionFactory
import com.haulmont.cuba.gui.autocomplete.Suggester
import com.haulmont.cuba.gui.autocomplete.Suggestion
import com.haulmont.cuba.gui.components.actions.RemoveAction
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.gui.data.ValueListener
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter
import com.haulmont.cuba.report.BandDefinition
import com.haulmont.cuba.report.DataSet
import com.haulmont.cuba.report.DataSetType
import com.haulmont.cuba.report.Orientation
import com.haulmont.cuba.security.entity.EntityOp
import com.haulmont.cuba.gui.components.*

public class BandDefinitionEditor extends AbstractEditor implements Suggester {

    private static volatile Collection<com.haulmont.chile.core.model.MetaClass> metaClasses;

    def BandDefinitionEditor(IFrame frame) {
        super(frame);
    }

    def void setItem(Entity item) {
        BandDefinition definition = (BandDefinition) item
        definition.setParentBandDefinition(parentDefinition)
        definition.position = position ?: definition.position
        if (!definition.orientation) { definition.orientation = Orientation.HORIZONTAL }
        super.setItem(definition);
        selectFirstDataset()
    }

    private BandDefinition parentDefinition
    private Integer position

    protected void init(Map<String, Object> params) {
        super.init(params);
        parentDefinition = params['param$parentDefinition']
        position = (Integer) params['param$position']

        Table table = getComponent('dataSets')
        table.addAction(new RemoveAction(table, false))

        table.addAction(new ActionAdapter('create', [
                actionPerform: {
                    Component component ->
                    DataSet dataset = new DataSet()
                    dataset.bandDefinition = (BandDefinition) item
                    dataset.name = dataset.bandDefinition.name ?: 'dataset'
                    dataset.type = DataSetType.GROOVY
                    table.datasource.addItem(dataset)
                },
                getCaption: {
                    MessageProvider.getMessage(AppConfig.instance.messagesPack, 'actions.Create')
                },
                isEnabled: {
                    UserSessionClient.userSession.isEntityOpPermitted(table.datasource.metaClass, EntityOp.CREATE)
                }
        ]))

        initDataSetControls()
    }

    def initDataSetControls() {
        LookupField lookupField = getComponent('type')
        AutoCompleteTextField textField = getComponent('text')
        TextField nameField = getComponent('datasetName')
        Label label = getComponent('dataSet_text')

        lookupField.addListener(
                [
                        valueChanged: {Object source, String property, Object prevValue, Object value ->
                            [textField, label].each {Component c -> c.visible = !(value && [DataSetType.SINGLE, DataSetType.MULTI].contains(value))}

                            textField.setSuggester(DataSetType.JPQL.equals(value) ? this : null)
                        }
                ] as ValueListener
        )

        def enableDatasetControls = {
            boolean value ->
            [lookupField, textField, nameField].each {it.enabled = value}
        }

        Table datasets = getComponent('dataSets')
        CollectionDatasource ds = datasets.datasource
        ds.addListener([
                itemChanged: {
                    Datasource ds1, Entity prevItem, Entity item1 ->
                    enableDatasetControls(item1 != null)
                }
        ] as DsListenerAdapter)

        enableDatasetControls(false)
    }

    def selectFirstDataset() {
        Table datasets = getComponent('dataSets')
        CollectionDatasource ds = datasets.datasource
        ds.refresh()
        if (!ds.itemIds.empty) {
            def item = ds.getItem(ds.itemIds.iterator().next())
            def set = new HashSet();
            set.add(item)
            datasets.setSelected(set)
        }
    }

    java.util.List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
        String query = (String) source.getValue()
        if (query == null || "".equals(query.trim())) {
            return Collections.emptyList();
        }
        def queryPosition = cursorPosition - 1
        return JpqlSuggestionFactory.requestHint(query, queryPosition, source, cursorPosition)
    }
}
