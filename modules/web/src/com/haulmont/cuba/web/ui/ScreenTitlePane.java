/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.12.2008 13:17:46
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.resource.Messages;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.Label;

import java.util.Iterator;
import java.util.LinkedList;

public class ScreenTitlePane extends ExpandLayout
{
    private LinkedList<String> captions = new LinkedList<String>();

    private Label label;
    private Button closeBtn;

    public ScreenTitlePane() {
        super(ExpandLayout.ORIENTATION_HORIZONTAL);
        setHeight(-1);
        setMargin(true);

        label = new Label();
        addComponent(label);

        closeBtn = new Button(Messages.getString("closeBtn"), new Button.ClickListener()
        {
            public void buttonClick(Button.ClickEvent event) {
                App.getInstance().getScreenManager().closeScreen();
            }
        });
        closeBtn.setStyleName(Button.STYLE_LINK);
        addComponent(closeBtn);
    }

    public String getCurrentCaption() {
        if (captions.isEmpty())
            return null;
        else
            return captions.getLast();
    }

    public void addCaption(String caption) {
        captions.add(caption);
        writeCaptions();
    }

    public void removeCaption() {
        if (!captions.isEmpty()) {
            captions.removeLast();
            writeCaptions();
        }
    }

    private void writeCaptions() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = captions.iterator(); it.hasNext();) {
            String caption = it.next();
            sb.append(caption);
            if (it.hasNext())
                sb.append(" -> ");
        }
        label.setValue(sb.toString());
    }
}
