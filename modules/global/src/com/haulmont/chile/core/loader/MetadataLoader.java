package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.Session;

public interface MetadataLoader extends ClassMetadataLoader, XmlMetadataLoader {
    
    Session postProcess();
}
