package cuba.client.web.ui.emailHistory.browse;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TableActionsHelper;

import java.util.Map;

/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Sergey Ovchinnikov
 * Created: 19.04.11 15:59
 *
 * $Id$
 */
public class SendingMessageBrowser extends AbstractLookup {

    public SendingMessageBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        Table table = getComponent("table");
        TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();

//        if (getComponent("filter") != null) {
//            helper.createFilterApplyAction("filter.apply");
//            helper.createFilterClearAction("filter.clear", "filterPanel");
//        } else {
//            helper.createRefreshAction();
//        }
    }
}
