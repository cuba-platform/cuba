package com.haulmont.chile.jpa.loader;

import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.model.Session;

public class JPAMetadataLoader extends ChileMetadataLoader {

    public JPAMetadataLoader(Session session) {
        super(session);
    }

    @Override
    protected ClassMetadataLoader createAnnotationsLoader(Session session) {
        return new JPAAnnotationsLoader(session);
    }
}
