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
import com.haulmont.cuba.core.app.FileStorageService
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.entity.FileDescriptor
import com.haulmont.cuba.gui.ServiceLocator
import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.WindowManager.OpenType
import com.haulmont.cuba.gui.components.actions.AddAction
import com.haulmont.cuba.gui.components.actions.CreateAction
import com.haulmont.cuba.gui.components.actions.EditAction
import com.haulmont.cuba.gui.components.actions.RemoveAction
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.data.DataService
import com.haulmont.cuba.gui.data.DsContext.CommitListener
import org.apache.commons.lang.StringUtils
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.gui.components.*
import com.haulmont.cuba.report.*
import com.haulmont.cuba.gui.data.CollectionDatasource.RefreshMode

public class ReportEditor extends AbstractEditor {

    def ReportEditor(IFrame frame) {
        super(frame);
    }

    def void setItem(Entity item) {
        Report report = (Report) item;
        BandDefinition rootDefinition = null

        if (PersistenceHelper.isNew(item)) {
            rootDefinition = new BandDefinition()
            rootDefinition.setName('Root')
            report.setRootBandDefinition(rootDefinition)
            report.setBands(new HashSet<BandDefinition>([rootDefinition]))

            CollectionDatasource groupsDs = getDsContext().get('groupsDs')
            groupsDs.refresh()
            if (groupsDs.getItemIds() != null) {
                def id = groupsDs.getItemIds().iterator().next()
                report.setGroup((ReportGroup)groupsDs.getItem(id))
            }
        }
        if (!StringUtils.isEmpty(report.name)) {
            caption = MessageProvider.formatMessage(getClass(), 'reportEditor.format', report.name)
        }

        super.setItem(item);
        this.report = (Report) getItem();

        if (PersistenceHelper.isNew(item))
            rootDefinition.setReport(report)

        bandTree.datasource.refresh()
        bandTree.expandTree()
    }

    private Report report

    private Tree bandTree
    private CollectionDatasource treeDs
    private deletedFiles = [:]

    public void init(Map<String, Object> params) {
        super.init(params);
        initGeneral()
        initTemplates()
        initParameters()
        initRoles()
        initValuesFormats()

        getDsContext().get('reportDs').refresh()
        getDsContext().addListener(new CommitListener() {

            @Override
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

            @Override
            void afterCommit(CommitContext<Entity> context, Set<Entity> result) {
                FileStorageService storageService = ServiceLocator.lookup(FileStorageService.NAME)

                for (Entity entity: context.commitInstances) {
                    if (ReportTemplate.isInstance(entity) && result.contains(entity)) {
                        java.util.List deletedFilesList = (java.util.List) deletedFiles.get(entity)
                        for (FileDescriptor fileDescriptor: deletedFilesList) {
                            removeQuietly(storageService, fileDescriptor)
                        }
                    }
                }

                for (Entity entity: context.removeInstances) {
                    if (ReportTemplate.isInstance(entity) && result.contains(entity)) {
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
                            refreshBandsTree()
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
                            refreshBandsTree()
                        }
                    }
                }
        ]

        Tree tree = getComponent('generalFrame.serviceTree')
        createBandDefinitionButton.action = new CreateAction(tree, OpenType.THIS_TAB, 'generalFrame.editBandDefinition') {
            @Override
            void actionPerform(Component component) {
                BandDefinition parentDefinition = (BandDefinition)treeDs.getItem()
                if (parentDefinition) {
                    super.actionPerform(component)
                }
            }

            @Override
            protected Map<String, Object> getInitialValues() {
                BandDefinition parentDefinition = (BandDefinition) treeDs.getItem()
                return (Map<String, Object>) [
                        'parentBandDefinition': parentDefinition,
                        'position': parentDefinition.childrenBandDefinitions?.size(),
                        'report': report
                ]
            }
        }
        // new ActionAdapter('generalFrame.createBandDefinition', messagesPack, createBandDefinition)
        editBandDefinitionButton.action = new EditAction(bandTree, OpenType.THIS_TAB, 'generalFrame.editBandDefinition')
        // new ActionAdapter('generalFrame.editBandDefinition', messagesPack, editBandDefinition)
        removeBandDefinitionButton.action = new RemoveAction(bandTree, false, 'generalFrame.removeBandDefinition')
        // new ActionAdapter('generalFrame.removeBandDefinition', messagesPack, removeBandDefinition)
        upButton.action = new ActionAdapter('generalFrame.up', messagesPack, up)
        downButton.action = new ActionAdapter('generalFrame.down', messagesPack, down)
    }

    private def refreshBandsTree() {
        treeDs.setRefreshMode(RefreshMode.NEVER)
        bandTree.refresh()
        treeDs.setRefreshMode(RefreshMode.ALWAYS)
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

            @Override
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

    public void commitAndClose() {
        super.commitAndClose();
    }
}
