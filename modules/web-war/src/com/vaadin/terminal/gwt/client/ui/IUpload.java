/*
 * Copyright 2008 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class IUpload extends FormPanel implements Paintable, ClickListener,
        FormHandler {

    public static final String CLASSNAME = "v-upload";

    /**
     * FileUpload component that opens native OS dialog to select file.
     */
    FileUpload fu = new FileUpload();

    Panel panel = new HorizontalPanel();

    ApplicationConnection client;

    private String paintableId;

    /**
     * Button that initiates uploading
     */
    private final Button submitButton;

    /**
     * When expecting big files, programmer may initiate some UI changes when
     * uploading the file starts. Bit after submitting file we'll visit the
     * server to check possible changes.
     */
    private Timer t;

    /**
     * some browsers tries to send form twice if submit is called in button
     * click handler, some don't submit at all without it, so we need to track
     * if form is already being submitted
     */
    private boolean submitted = false;

    private boolean enabled = true;

    public IUpload() {
        super();
        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);


        setWidget(panel);
//        panel.setStyleName("v-upload-panel");
        panel.add(fu);
        submitButton = new Button();
        submitButton.setStyleName("v-upload-submit");
        submitButton.addClickListener(this);
        panel.add(submitButton);
/*        Panel clearPanel = new HorizontalPanel();
        clearPanel.setStyleName("v-upload-clear");
        panel.add(clearPanel);*/

        addFormHandler(this);

        setStyleName(CLASSNAME);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }
        this.client = client;
        paintableId = uidl.getId();
        setAction(client.getAppUri());
        submitButton.setText(uidl.getStringAttribute("buttoncaption"));
        fu.setName(paintableId + "_file");

        if (uidl.hasAttribute("disabled") || uidl.hasAttribute("readonly")) {
            disableUpload();
        } else if (uidl.getBooleanAttribute("state")) {
            enableUploaod();
        }

    }

    public void onClick(Widget sender) {
        submit();
    }

    public void onSubmit(FormSubmitEvent event) {
        if (fu.getFilename().length() == 0 || submitted || !enabled) {
            event.setCancelled(true);
            ApplicationConnection
                    .getConsole()
                    .log(
                            "Submit cancelled (disabled, no file or already submitted)");
            return;
        }
        // flush possibly pending variable changes, so they will be handled
        // before upload
        client.sendPendingVariableChanges();

        submitted = true;
        ApplicationConnection.getConsole().log("Submitted form");

        disableUpload();

        /*
         * Visit server a moment after upload has started to see possible
         * changes from UploadStarted event. Will be cleared on complete.
         */
        t = new Timer() {
            @Override
            public void run() {
                client.sendPendingVariableChanges();
            }
        };
        t.schedule(800);
    }

    protected void disableUpload() {
        submitButton.setEnabled(false);
        fu.setVisible(false);
        enabled = false;
    }

    protected void enableUploaod() {
        submitButton.setEnabled(true);
        fu.setVisible(true);
        enabled = true;
    }

    public void onSubmitComplete(FormSubmitCompleteEvent event) {
        if (client != null) {
            if (t != null) {
                t.cancel();
            }
            ApplicationConnection.getConsole().log("Submit complete");
            client.sendPendingVariableChanges();
        }
        submitted = false;
        enableUploaod();
    }

}
