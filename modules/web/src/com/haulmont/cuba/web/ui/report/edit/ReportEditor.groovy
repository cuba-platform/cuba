/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 13:35:51
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report.edit

import com.haulmont.chile.core.model.MetaPropertyPath
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.MessageProvider
import com.haulmont.cuba.core.global.MetadataProvider
import com.haulmont.cuba.core.global.PersistenceHelper
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.components.actions.AddAction
import com.haulmont.cuba.gui.components.actions.CreateAction
import com.haulmont.cuba.gui.components.actions.EditAction
import com.haulmont.cuba.gui.components.actions.RemoveAction
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.data.DataService
import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.report.BandDefinition
import com.haulmont.cuba.report.Orientation
import com.haulmont.cuba.report.Report
import com.haulmont.cuba.report.ReportInputParameter
import org.apache.commons.lang.StringUtils
import com.haulmont.cuba.gui.components.*
import com.haulmont.cuba.gui.WindowManager.OpenType
import com.haulmont.cuba.gui.data.DsContext.CommitListener
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.report.ReportTemplate
import com.haulmont.cuba.core.app.FileStorageService
import com.haulmont.cuba.gui.ServiceLocator
import com.haulmont.cuba.core.entity.FileDescriptor
import com.haulmont.cuba.core.global.FileStorageException

public class ReportEditor extends AbstractEditor {

    def ReportEditor(IFrame frame) {
        super(frame);
    }

    def void setItem(Entity item) {
        super.setItem(item);
        Report report = (Report) getItem();
        this.report = report;
        if (PersistenceHelper.isNew(item)) {
            BandDefinition definition = new BandDefinition()
            definition.setName('Root')
            report.setRootBandDefinition(definition)
        }
        if (!StringUtils.isEmpty(this.report.name)) {
            caption = MessageProvider.formatMessage(getClass(), 'reportEditor.format', this.report.name)
        }
        bandTree.datasource.refresh()
        bandTree.expandTree()
    }

    private Report report

    private Tree bandTree
    private Datasource treeDs
    private deletedFiles = [:]

    protected void init(Map<String, Object> params) {
        super.init(params);
        initGeneral()
        initTemplates()
        initParameters()
        initRoles()
        initValuesFormats()

        getDsContext().get('reportDs').refresh()
        getDsContext().addListener(new CommitListener() {
            void beforeCommit(CommitContext<Entity> context) {
                // delete descriptors from db
                for (Entity entity: context.commitInstances) {
                    if (ReportTemplate.isInstance(entity)) {
                        java.util.List deletedFilesList = (java.util.List) deletedFiles.get(entity)
                        if ((deletedFilesList != null) && (deletedFilesList.size() > 0)) {
                            context.removeInstances.add((Entity) deletedFilesList.get(0))
                        }
                    }
                }
            }

            void afterCommit(CommitContext<Entity> context, Map<Entity, Entity> result) {
                FileStorageService storageService = ServiceLocator.lookup(FileStorageService.NAME)

                for (Entity entity: context.commitInstances) {
                    if (ReportTemplate.isInstance(entity) && result.containsKey(entity)) {
                        java.util.List deletedFilesList = (java.util.List) deletedFiles.get(entity)
                        for (FileDescriptor fileDescriptor: deletedFilesList) {
                            removeQuietly(storageService, fileDescriptor)
                        }
                    }
                }

                for (Entity entity: context.removeInstances) {
                    if (ReportTemplate.isInstance(entity) && result.containsKey(entity)) {
                        java.util.List deletedFilesList = (java.util.List) deletedFiles.get(entity)
                        for (FileDescriptor fileDescriptor: deletedFilesList) {
                            removeQuietly(storageService, fileDescriptor)
                        }
                        ReportTemplate template = (ReportTemplate) entity
                        removeQuietly(storageService, template.templateFileDescriptor)
                    }
                }
            }

            private void removeQuietly(storageService, fileDescriptor) {
                try {
                    storageService.removeFile(fileDescriptor)
                } catch (FileStorageException ignored) { }
            }
        })
    }

    private def initParameters() {
        com.haulmont.chile.core.model.MetaClass metaClass = MetadataProvider.getSession().getClass(ReportInputParameter.class)
        MetaPropertyPath mpp = new MetaPropertyPath(metaClass, metaClass.getProperty('position'))

        final CollectionDatasource parametersDs = getDsContext().get('parametersDs')

        Table parametersTable = getComponent('generalFrame.parametersFrame.inputParametersTable')
        parametersTable.addAction(
                new CreateAction(parametersTable, WindowManager.OpenType.DIALOG) {
                    @Override
                    protected Map<String, Object> getInitialValues() {
                        return new HashMap(['position': parametersDs.itemIds.size(), 'report': report])
                    }
                }
        )
        parametersTable.addAction(new RemoveAction(parametersTable, false));
        parametersTable.addAction(new EditAction(parametersTable, WindowManager.OpenType.DIALOG));

        Button upButton = getComponent('generalFrame.parametersFrame.up')
        Button downButton = getComponent('generalFrame.parametersFrame.down')

        def up = [
                actionPerform: {Component component ->
                    ReportInputParameter parameter = (ReportInputParameter) parametersDs.getItem()
                    if (parameter) {
                        Collection parametersList = report.getInputParameters()
                        int index = parameter.position
                        if (index > 0) {
                            ReportInputParameter previousParameter = null
                            for (ReportInputParameter _param: parametersList) {
                                if (_param.position == index - 1) {
                                    previousParameter = _param;
                                    break;
                                }
                            }
                            if (previousParameter) {
                                parameter.position = previousParameter.position
                                previousParameter.position = index
                                parametersTable.sortBy(mpp, true)
                            }
                        }
                    }
                }
        ]

        def down = [
                actionPerform: {Component component ->
                    ReportInputParameter parameter = (ReportInputParameter) parametersDs.getItem()
                    if (parameter) {
                        Collection parametersList = report.getInputParameters()
                        int index = parameter.position
                        if (index < parametersDs.itemIds.size() - 1) {
                            ReportInputParameter nextParameter = null
                            for (ReportInputParameter _param: parametersList) {
                                if (_param.position == index + 1) {
                                    nextParameter = _param;
                                    break;
                                }
                            }
                            if (nextParameter) {
                                parameter.position = nextParameter.position
                                nextParameter.position = index
                                parametersTable.sortBy(mpp, true)
                            }
                        }
                    }
                }
        ]

        upButton.action = new ActionAdapter('generalFrame.up', messagesPack, up)
        downButton.action = new ActionAdapter('generalFrame.down', messagesPack, down)
    }

    private def initValuesFormats() {
        Table formatsTable = getComponent('generalFrame.formatsFrame.valuesFormatsTable')

        formatsTable.addAction(
                new CreateAction(formatsTable, WindowManager.OpenType.DIALOG) {
                    @Override
                    protected Map<String, Object> getInitialValues() {
                        return new HashMap(['report': report])
                    }
                }
        )
        formatsTable.addAction(new RemoveAction(formatsTable, false))
        formatsTable.addAction(new EditAction(formatsTable, WindowManager.OpenType.DIALOG))
    }

    private def initRoles() {
        final CollectionDatasource parametersDs = getDsContext().get('rolesDs')
        Table rolesTable = getComponent('securityFrame.rolesTable')
        def handler = [
                handleLookup: {Collection items ->
                    if (items)
                        items.each {Entity item -> parametersDs.addItem(item)}
                }
        ] as Window.Lookup.Handler
        rolesTable.addAction(new AddAction(rolesTable, handler))
        rolesTable.addAction(new RemoveAction(rolesTable, false))

        Table screenTable = getComponent('securityFrame.screenTable')
        screenTable.addAction(
                new CreateAction(screenTable) {
                    @Override protected Map<String, Object> getInitialValues() {
                        return new HashMap(['report': report])
                    }
                }
        )
        screenTable.addAction(new RemoveAction(screenTable, false))
    }

    private def initGeneral() {
        bandTree = (Tree) getComponent('generalFrame.serviceTree')
        treeDs = getDsContext().get('treeDs')
        View bandDefinitionView = MetadataProvider.getViewRepository().getView(BandDefinition.class, 'report.edit')
        DataService dataService = getDsContext().getDataService()

        Button createBandDefinitionButton = getComponent('generalFrame.createBandDefinition')
        Button editBandDefinitionButton = getComponent('generalFrame.editBandDefinition')
        Button removeBandDefinitionButton = getComponent('generalFrame.removeBandDefinition')
        Button upButton = getComponent('generalFrame.up')
        Button downButton = getComponent('generalFrame.down')

        def createBandDefinition = [
                actionPerform: {Component component ->
                    BandDefinition parentDefinition = treeDs.getItem()
                    if (parentDefinition) {
                        if (!Orientation.VERTICAL.equals(parentDefinition.orientation)) {

                            Window.Editor editor = openEditor(
                                    'report$BandDefinition.edit',
                                    new BandDefinition(),
                                    WindowManager.OpenType.THIS_TAB,
                                    (Map<String, Object>) [
                                            'parentDefinition': parentDefinition,
                                            'position': parentDefinition.childrenBandDefinitions?.size()
                                    ]
                            )

                            editor.addListener(
                                    [
                                            windowClosed: { String actionId -> treeDs.refresh() }
                                    ] as Window.CloseListener
                            )
                        } else {
                            showNotification(getMessage('generalFrame.noChildSupport'), IFrame.NotificationType.HUMANIZED)
                        }
                    }
                }
        ]

        def editBandDefinition = [
                actionPerform: { Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
                    if (definition) {
                        Window.Editor editor = openEditor('report$BandDefinition.edit', definition, WindowManager.OpenType.THIS_TAB)
                        editor.addListener(
                                [
                                        windowClosed: {
                                            String actionId -> refreshBand(actionId, editor, definition)
                                        }
                                ] as Window.CloseListener)
                    }
                }
        ]

        def removeBandDefinition = [
                actionPerform: { Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
//                    removeBandFromChilds(report.getRootBandDefinition(), definition)
                    if (definition) dataService.remove(definition)
                    treeDs.refresh()
                }
        ]

        def up = [
                actionPerform: {Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
                    if (definition && definition.getParentBandDefinition()) {
                        BandDefinition parentDefinition = dataService.reload(definition.getParentBandDefinition(), bandDefinitionView);
                        java.util.List definitionsList = parentDefinition.getChildrenBandDefinitions()
                        int index = definitionsList.indexOf(definition);
                        if (index > 0) {
                            BandDefinition previousDefinition = definitionsList.get(index - 1)
                            definition.position = definition.position - 1
                            previousDefinition.position = previousDefinition.position + 1
                            dataService.commit(definition, bandDefinitionView)
                            dataService.commit(previousDefinition, bandDefinitionView)
                            treeDs.refresh()
                        }
                    }
                }
        ]

        def down = [
                actionPerform: {Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
                    if (definition && definition.getParentBandDefinition()) {
                        BandDefinition parentDefinition = dataService.reload(definition.getParentBandDefinition(), bandDefinitionView);
                        java.util.List definitionsList = parentDefinition.getChildrenBandDefinitions()
                        int index = definitionsList.indexOf(definition);
                        if (index < definitionsList.size() - 1) {
                            BandDefinition nextDefinition = definitionsList.get(index + 1)
                            definition.position = definition.position + 1
                            nextDefinition.position = nextDefinition.position - 1
                            dataService.commit(definition, bandDefinitionView)
                            dataService.commit(nextDefinition, bandDefinitionView)
                            treeDs.refresh()
                        }
                    }
                }
        ]

        createBandDefinitionButton.action = new ActionAdapter('generalFrame.createBandDefinition', messagesPack, createBandDefinition)
        editBandDefinitionButton.action = new ActionAdapter('generalFrame.editBandDefinition', messagesPack, editBandDefinition)
        removeBandDefinitionButton.action = new ActionAdapter('generalFrame.removeBandDefinition', messagesPack, removeBandDefinition)
        upButton.action = new ActionAdapter('generalFrame.up', messagesPack, up)
        downButton.action = new ActionAdapter('generalFrame.down', messagesPack, down)
    }

    private def initTemplates() {
        Table templatesTable = getComponent('generalFrame.templatesTable')
        templatesTable.addAction(new CreateAction(templatesTable, OpenType.DIALOG) {
            @Override
            protected Map<String, Object> getInitialValues() {
                return new HashMap(['report': report])
            }

            @Override
            protected Map<String, Object> getWindowParams() {
                return new HashMap(['deletedContainer': deletedFiles])
            }
        });
        templatesTable.addAction(new EditAction(templatesTable, OpenType.DIALOG) {
            @Override
            protected Map<String, Object> getWindowParams() {
                return new HashMap(['deletedContainer': deletedFiles])
            }
        });
        templatesTable.addAction(new RemoveAction(templatesTable, false));

        Button defaultTemplateBtn = getComponent("generalFrame.defaultTemplateBtn")
        defaultTemplateBtn.action = new AbstractAction("report.defaultTemplate") {
            void actionPerform(Component component) {
                ReportTemplate template = templatesTable.getSingleSelected()
                if ((template != null) && !template.getDefaultFlag()) {
                    template.setDefaultFlag(true)
                    Collection itemIds = templatesTable.getDatasource().getItemIds()
                    for (id in itemIds){
                        ReportTemplate temp = (ReportTemplate)templatesTable.getDatasource().getItem(id)
                        if (!template.equals(temp) && (temp.getDefaultFlag()))
                            temp.setDefaultFlag(false)
                    }
                    templatesTable.refresh();
                }
            }
        };
    }

    private def refreshBand(actionId, editor, band) {
        if (COMMIT_ACTION_ID.equals(actionId)) {
            treeDs.refresh()
        }
    }

    private void replaceInBands(BandDefinition rootBand, BandDefinition lastBand, BandDefinition newBand) {
        java.util.List<BandDefinition> defs = rootBand.getChildrenBandDefinitions()
        if (defs != null) {
            def index = defs.indexOf(lastBand)
            if (index >= 0) {
                defs.remove(lastBand)
            }
            else {
                for (BandDefinition childBand: defs) {
                    replaceInBands(childBand, lastBand, newBand)
                }
            }
        }
    }

    private void removeBandFromChilds(BandDefinition rootBand, BandDefinition band) {
        java.util.List<BandDefinition> defs = rootBand.getChildrenBandDefinitions()
        if (defs.contains(band))
            defs.remove(band)
        else {
            for (BandDefinition childBand: defs) {
                removeBandFromChilds(childBand, band)
            }
        }
    }

    public void commitAndClose() {
        super.commitAndClose();
    }
}
