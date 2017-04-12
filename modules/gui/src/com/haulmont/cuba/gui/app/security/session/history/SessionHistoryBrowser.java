package com.haulmont.cuba.gui.app.security.session.history;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.security.entity.SessionLogEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class SessionHistoryBrowser extends AbstractLookup {

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Table<SessionLogEntry> sessionsTable;

    @Inject
    protected GroupDatasource<SessionLogEntry, UUID> sessionsDs;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Label lastUpdateTsLab;

    @Named("sessionsTable.refresh")
    protected Action refreshAction;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        sessionsTable.setTextSelectionEnabled(true);

        sessionsDs.addCollectionChangeListener(e -> {
            String time = Datatypes.getNN(Date.class)
                    .format(timeSource.currentTimestamp(), userSessionSource.getLocale());
            lastUpdateTsLab.setValue(time);
        });

        addAction(refreshAction);
    }

    public void refresh() {
        sessionsDs.refresh();
    }

}
