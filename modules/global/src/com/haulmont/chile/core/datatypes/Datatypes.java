package com.haulmont.chile.core.datatypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

import com.haulmont.bali.util.ReflectionHelper;

import javax.ejb.Local;

/**
 * Contains instances of all Datatype's registered for the application.
 * <p>
 * Automatically loads datatype definitions from the file <code>datatypes.xml</code> in the root of classpath. If no
 * such file found, configures datatypes from <code>/com/haulmont/chile/core/datatypes/datatypes.xml</code>
 */
public class Datatypes {

    private static Log log = LogFactory.getLog(Datatypes.class);

    private static Datatypes instance = new Datatypes();

    private Map<Class, Datatype> datatypeByClass = new HashMap<Class, Datatype>();
    private Map<String, Datatype> datatypeByName = new HashMap<String, Datatype>();

    private Map<Locale, FormatStrings> formatStringsMap = new HashMap<Locale, FormatStrings>();

    private Datatypes() {
        SAXReader reader = new SAXReader();
        URL resource = Datatypes.class.getResource("/datatypes.xml");
        if (resource == null) {
            log.info("Can't find /datatypes.xml, using default datatypes settings");
            resource = Datatypes.class.getResource("/com/haulmont/chile/core/datatypes/datatypes.xml");
        }

        try {
            Document document = reader.read(resource);
            Element element = document.getRootElement();

            List<Element> datatypeElements = element.elements("datatype");
            for (Element datatypeElement : datatypeElements) {
                String datatypeClassName = datatypeElement.attributeValue("class");
                try {
                    Datatype datatype;
                    Class<Datatype> datatypeClass = ReflectionHelper.getClass(datatypeClassName);
                    try {
                        final Constructor<Datatype> constructor = datatypeClass.getConstructor(Element.class);
                        datatype = constructor.newInstance(datatypeElement);
                    } catch (Throwable e) {
                        datatype = datatypeClass.newInstance();
                    }

                    __register(datatype);
                } catch (Throwable e) {
                    log.error(String.format("Fail to load datatype '%s'", datatypeClassName), e);
                }
            }
        } catch (DocumentException e) {
            log.error("Fail to load datatype settings", e);
        }
    }

    private void __register(Datatype datatype) {
        datatypeByClass.put(datatype.getJavaClass(), datatype);
        datatypeByName.put(datatype.getName(), datatype);
    }

    @Deprecated
    public static Datatypes getInstance() {
        return instance;
    }

    /**
     * Returns localized format strings.
     * @param locale selected locale
     * @return {@link FormatStrings} object, or null if no formats are registered for the locale
     */
    public static FormatStrings getFormatStrings(Locale locale) {
        return instance.formatStringsMap.get(new Locale(locale.getLanguage()));
    }

    public static void setFormatStrings(Locale locale, FormatStrings formatStrings) {
        instance.formatStringsMap.put(new Locale(locale.getLanguage()), formatStrings);
    }

    public static void register(Datatype datatype) {
        instance.__register(datatype);
    }

    /**
     * Get Datatype instance by its unique name
     * @return Datatype instance or null if not found
     */
    public static <T extends Datatype> T get(String name) {
        return (T) instance.datatypeByName.get(name);
    }

    /**
     * Get Datatype instance by the corresponding Java class. This method tries to find matching supertype too.
     * @return Datatype instance or null if not found
     */
    public static <T> Datatype<T> get(Class<T> clazz) {
        Datatype datatype = instance.datatypeByClass.get(clazz);
        if (datatype == null) {
            // if no exact type found, try to find matching super-type
            for (Map.Entry<Class, Datatype> entry : instance.datatypeByClass.entrySet()) {
                if (entry.getKey().isAssignableFrom(clazz)) {
                    return entry.getValue();
                }
            }
        }
        return datatype;
    }

    /**
     * All registered Datatype names
     */
    public static Set<String> getNames() {
        return Collections.unmodifiableSet(instance.datatypeByName.keySet());
    }
}
