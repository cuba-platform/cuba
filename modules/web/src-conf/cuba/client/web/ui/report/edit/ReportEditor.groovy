/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 13:35:51
 *
 * $Id$
 */
package cuba.client.web.ui.report.edit

import com.haulmont.cuba.gui.components.AbstractEditor
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.PersistenceHelper
import com.haulmont.cuba.report.Report
import com.haulmont.cuba.report.BandDefinition
import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.components.Window

import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.gui.components.ActionAdapter
import com.haulmont.cuba.core.global.MetadataProvider
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.report.ReportInputParameter
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.TableActionsHelper

import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.gui.data.DataService
import com.haulmont.cuba.report.Orientation
import com.haulmont.cuba.gui.components.TextField
import com.haulmont.cuba.gui.components.CheckBox
import com.haulmont.cuba.gui.data.ValueListener
import com.haulmont.cuba.gui.components.FileUploadField
import com.haulmont.cuba.web.filestorage.FileDisplay
import com.haulmont.cuba.gui.components.FileUploadField.Listener.Event
import com.haulmont.cuba.core.global.MessageProvider
import com.haulmont.cuba.core.global.TimeProvider
import com.haulmont.cuba.web.app.FileDownloadHelper
import com.haulmont.cuba.core.app.FileStorageService
import com.haulmont.cuba.core.global.FileStorageException
import com.haulmont.cuba.gui.ServiceLocator
import com.haulmont.cuba.gui.components.ValueProvider
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.SortInfo
import com.haulmont.chile.core.model.MetaPropertyPath
import com.haulmont.cuba.web.gui.components.WebComponentsHelper

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
            definition.setName("Root")
            report.setRootBandDefinition(definition)
        }

        templateDescriptor = report.templateFileDescriptor;
        if (templateDescriptor)
            templatePath.setCaption(templateDescriptor.getName())

        [templatePath, uploadTemplate].each {Component c -> c.setEnabled(!report.getIsCustom())}
        customClass.setEnabled(report.getIsCustom())


        getDsContext().get('treeDs').refresh()
    }

    private Button templatePath;
    private CheckBox isCustom;
    private TextField customClass;
    private com.haulmont.cuba.core.entity.FileDescriptor templateDescriptor;
    private Report report;
    private FileUploadField uploadTemplate;
    private com.haulmont.cuba.core.entity.FileDescriptor oldTemplateDescriptor

    protected void init(Map<String, Object> params) {
        super.init(params);
        initGeneral()
        initParameters()
        initRoles()

        getDsContext().get('reportDs').refresh()
    }

    private def initParameters() {
        com.haulmont.chile.core.model.MetaClass metaClass = MetadataProvider.getSession().getClass(ReportInputParameter.class);
        MetaPropertyPath mpp = new MetaPropertyPath(metaClass, metaClass.getProperty("position"));

        final CollectionDatasource parametersDs = getDsContext().get('parametersDs')

        Table parametersTable = getComponent('parametersFrame.inputParametersTable')
        TableActionsHelper paramHelper = new TableActionsHelper(this, parametersTable)
        paramHelper.createCreateAction([getValues: {['position': parametersDs.itemIds.size(),'report':report]}, getParameters: {[:]}] as ValueProvider)
        paramHelper.createRemoveAction(false)
        paramHelper.createEditAction()

        Button upButton = getComponent('parametersFrame.up')
        Button downButton = getComponent('parametersFrame.down')

        def up = [
                actionPerform: {Component component ->
                    ReportInputParameter parameter = (ReportInputParameter) parametersDs.getItem()
                    if (parameter) {
                        List parametersList = report.getInputParameters()
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
                                parameter.position = parameter.position - 1
                                previousParameter.position = previousParameter.position + 1
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
                        List parametersList = report.getInputParameters()
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
                                parameter.position = parameter.position + 1
                                nextParameter.position = nextParameter.position - 1
                                parametersTable.sortBy(mpp, true)
                            }
                        }
                    }
                }
        ]

        upButton.action = new ActionAdapter(getMessage('generalFrame.up'), up)
        downButton.action = new ActionAdapter(getMessage('generalFrame.down'), down)
    }

    private def initRoles() {
        final CollectionDatasource parametersDs = getDsContext().get('rolesDs')
        Table rolesTable = getComponent('securityFrame.rolesTable')
        TableActionsHelper paramHelper = new TableActionsHelper(this, rolesTable)
        paramHelper.createRemoveAction(false)
        def handler = [
                handleLookup: {Collection items ->
                    if (items)
                        items.each {Entity item -> parametersDs.addItem(item)}
                }
        ] as Window.Lookup.Handler
        paramHelper.createAddAction(handler)

        Table screenTable = getComponent('securityFrame.screenTable')
        TableActionsHelper screenTableHelper = new TableActionsHelper(this, screenTable)
        screenTableHelper.createCreateAction([
                getValues: {
                    return ['report': report]
                },
                getParameters: {->
                    return [:]
                }
        ] as ValueProvider)
        screenTableHelper.createRemoveAction(false)
    }

    Datasource treeDs;

    private def initGeneral() {
        treeDs = getDsContext().get('treeDs')
        View bandDefinitionView = MetadataProvider.getViewRepository().getView(BandDefinition.class, "report.edit")
        DataService dataService = getDsContext().getDataService()

        Button createBandDefinitionButton = getComponent('generalFrame.createBandDefinition')
        Button editBandDefinitionButton = getComponent('generalFrame.editBandDefinition')
        Button removeBandDefinitionButton = getComponent('generalFrame.removeBandDefinition')
        Button upButton = getComponent('generalFrame.up')
        Button downButton = getComponent('generalFrame.down')
        uploadTemplate = getComponent('generalFrame.uploadTemplate');
        customClass = getComponent('generalFrame.customClass')
        isCustom = getComponent('generalFrame.isCustom')
        templatePath = getComponent('generalFrame.templatePath')

        isCustom.addListener([
                valueChanged: {Object source, String property, Object prevValue, Object value ->
                    Boolean isCustom = Boolean.TRUE.equals(value)
                    [templatePath, uploadTemplate].each {Component c -> c.setEnabled(!isCustom)}
                    customClass.setEnabled(isCustom)
                }

        ] as ValueListener)

        def createBandDefinition = [
                actionPerform: {Component component ->
                    BandDefinition parentDefinition = treeDs.getItem()
                    if (parentDefinition) {
                        if (!Orientation.VERTICAL.equals(parentDefinition.orientation)) {
                            Window.Editor editor = openEditor('report$BandDefinition.edit', new BandDefinition(), WindowManager.OpenType.THIS_TAB, (Map<String, Object>) ['parentDefinition': parentDefinition, 'position': parentDefinition.childrenBandDefinitions?.size()])
                            editor.addListener(
                                    [
                                            windowClosed: {String actionId ->
                                                treeDs.refresh()
                                            }
                                    ] as Window.CloseListener
                            )
                        } else {
                            showNotification(getMessage('generalFrame.noChildSupport'), IFrame.NotificationType.HUMANIZED)
                        }
                    }
                }
        ]

        def editBandDefinition = [
                actionPerform: {Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
                    if (definition) {
                        Window.Editor editor = openEditor('report$BandDefinition.edit', definition, WindowManager.OpenType.THIS_TAB)
                        editor.addListener(
                                [
                                        windowClosed: {String actionId ->
                                            treeDs.refresh()
                                        }
                                ] as Window.CloseListener)
                    }
                }
        ]

        def removeBandDefinition = [
                actionPerform: {Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
                    if (definition) dataService.remove(definition)
                    treeDs.refresh()
                }
        ]

        def up = [
                actionPerform: {Component component ->
                    BandDefinition definition = (BandDefinition) treeDs.getItem()
                    if (definition && definition.getParentBandDefinition()) {
                        BandDefinition parentDefinition = dataService.reload(definition.getParentBandDefinition(), bandDefinitionView);
                        List definitionsList = parentDefinition.getChildrenBandDefinitions()
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
                        List definitionsList = parentDefinition.getChildrenBandDefinitions()
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

        def showLink = [
                actionPerform: {Component component ->
                    if (templateDescriptor != null) {
                        FileDisplay fileDisplay = new FileDisplay(true);
                        fileDisplay.show(templateDescriptor.getName(), templateDescriptor, true);
                    }
                }
        ]

        def uploadListener = [
                uploadStarted: {Event event ->
                    uploadTemplate.setEnabled(false);
                },

                uploadFinished: {Event event ->
                    uploadTemplate.setEnabled(true);
                },

                uploadSucceeded: {Event event ->
                    templateDescriptor = new com.haulmont.cuba.core.entity.FileDescriptor();
                    templateDescriptor.setName(uploadTemplate.getFileName());
                    templateDescriptor.setExtension(FileDownloadHelper.getFileExt(uploadTemplate.getFileName()));
                    templateDescriptor.setSize(uploadTemplate.getBytes().length);
                    templateDescriptor.setCreateDate(TimeProvider.currentTimestamp());
                    saveFile(templateDescriptor, uploadTemplate);
                    templatePath.setCaption(templateDescriptor.getName());
                    oldTemplateDescriptor = report.templateFileDescriptor
                    report.templateFileDescriptor = templateDescriptor;
                    showNotification(MessageProvider.getMessage(ReportEditor.class, 'uploadSuccess'), IFrame.NotificationType.HUMANIZED);
                },

                uploadFailed: {Event event ->
                    showNotification(MessageProvider.getMessage(ReportEditor.class, 'uploadUnsuccess'), IFrame.NotificationType.WARNING);
                },

                updateProgress: {
                    long readBytes, long contentLength ->

                }
        ] as FileUploadField.Listener;

        uploadTemplate.addListener(uploadListener);
        createBandDefinitionButton.action = new ActionAdapter(getMessage('generalFrame.createBandDefinition'), createBandDefinition)
        editBandDefinitionButton.action = new ActionAdapter(getMessage('generalFrame.editBandDefinition'), editBandDefinition)
        removeBandDefinitionButton.action = new ActionAdapter(getMessage('generalFrame.removeBandDefinition'), removeBandDefinition)
        upButton.action = new ActionAdapter(getMessage('generalFrame.up'), up)
        downButton.action = new ActionAdapter(getMessage('generalFrame.down'), down)
        templatePath.action = new ActionAdapter(getMessage('report.template'), showLink)
    }

    private void saveFile(com.haulmont.cuba.core.entity.FileDescriptor fd, FileUploadField uploadField) {
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.NAME);
        try {
            fss.saveFile(fd, uploadField.getBytes());
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    public void commitAndClose() {
        super.commitAndClose();
        if (oldTemplateDescriptor) {
            FileStorageService storageService = ServiceLocator.lookup(FileStorageService.NAME)
            storageService.removeFile(oldTemplateDescriptor)
        }
    }
}
