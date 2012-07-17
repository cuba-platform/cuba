package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.Session;
import org.dom4j.Element;

import java.io.InputStream;

public interface XmlMetadataLoader {
    Session loadXml(String xml);
    Session loadXml(Element xml);
    Session loadXml(InputStream xml);

    Session getSession();
}
