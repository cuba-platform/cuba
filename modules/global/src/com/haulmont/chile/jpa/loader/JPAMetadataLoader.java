package com.haulmont.chile.jpa.loader;

import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.model.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JPAMetadataLoader extends ChileMetadataLoader {
    private Log LOG = LogFactory.getLog(JPAMetadataLoader.class);

    @Override
    protected ClassMetadataLoader createAnnotationsLoader(Session session) {
        return new JPAAnnotationsLoader(session);
    }
}
