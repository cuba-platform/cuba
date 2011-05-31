/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.restapi;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityLoadInfo;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.global.MetadataProvider;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.traversal.NodeFilter;

import javax.activation.MimeType;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

public class XMLConvertor implements Convertor {
    public static final MimeType MIME_TYPE_XML;
    public static final String MIME_STR = "text/xml;charset=UTF-8";

    public static final String ELEMENT_INSTANCE = "instance";
    public static final String ELEMENT_URI = "uri";
    public static final String ELEMENT_REF = "ref";
    public static final String ELEMENT_NULL_REF = "null";
    public static final String ELEMENT_MEMBER = "member";
    public static final String ATTR_ID = "id";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_VERSION = "version";
    public static final String ATTR_NULL = "null";
    public static final String ATTR_MEMBER_TYPE = "member-type";
    public static final String NULL_VALUE = "null";

    private static final String EMPTY_TEXT = " ";
    public static final char DASH = '-';
    public static final char UNDERSCORE = '_';

    public static final String INSTANCE_XSD = "instance.xsd";
    public static final String ROOT_ELEMENT_INSTANCE = "instances";

    private static DocumentBuilder _builder;

    public static final String COMMIT_REQUEST_XSD = "CommitRequest.xsd";
    public static final String MAPPING_ROOT_ELEMENT_INSTANCE = "mapping";
    public static final String PAIR_ELEMENT = "pair";

    private static final Transformer _transformer;
    private static LSParser requestConfigParser;
    private static DOMImplementationLS lsImpl;

    static {
        try {
            MIME_TYPE_XML = new MimeType(MIME_STR);

            _builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            lsImpl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            requestConfigParser = lsImpl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS,
                    null);

            // Set options on the parser
            DOMConfiguration config = requestConfigParser.getDomConfig();
            config.setParameter("validate", Boolean.TRUE);
            config.setParameter("element-content-whitespace", Boolean.FALSE);
            config.setParameter("comments", Boolean.FALSE);
            requestConfigParser.setFilter(new LSParserFilter() {
                public short startElement(Element elementArg) {
                    return LSParserFilter.FILTER_ACCEPT;
                }

                public short acceptNode(Node nodeArg) {
                    return "".equals(nodeArg.getTextContent().trim()) ?
                            LSParserFilter.FILTER_REJECT :
                            LSParserFilter.FILTER_ACCEPT;
                }

                public int getWhatToShow() {
                    return NodeFilter.SHOW_TEXT;
                }
            });

            /*
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsd = XMLConvertor.class.getResourceAsStream(INSTANCE_XSD);
            _instancesXsd = factory.newSchema(new StreamSource(xsd));
            */

            _transformer = TransformerFactory.newInstance().newTransformer();
            _transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            _transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            _transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            _transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            _transformer.setOutputProperty(OutputKeys.INDENT, "no");
            _transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MimeType getMimeType() {
        return MIME_TYPE_XML;
    }

    public Document process(Entity entity, MetaClass metaclass, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Element root = newDocument(ROOT_ELEMENT_INSTANCE);
        encodeEntityInstance(entity, root, false, metaclass);
        Document doc = root.getOwnerDocument();
        decorate(doc, requestURI);
        return doc;
    }

    public Document process(List<Entity> entities, MetaClass metaClass, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Element root = newDocument(ROOT_ELEMENT_INSTANCE);
        for (Entity entity : entities) {
            encodeEntityInstance(entity, root, false, metaClass);
        }
        Document doc = root.getOwnerDocument();
        decorate(doc, requestURI);
        return doc;
    }

    public Object process(Map<Entity, Entity> entityMap, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Element root = newDocument(MAPPING_ROOT_ELEMENT_INSTANCE);
        Document doc = root.getOwnerDocument();
        for (Map.Entry<Entity, Entity> entry : entityMap.entrySet()) {
            Element pair = doc.createElement(PAIR_ELEMENT);
            root.appendChild(pair);
            encodeEntityInstance(
                    entry.getKey(),
                    pair, false,
                    getMetaClass(entry.getKey())
            );
            encodeEntityInstance(
                    entry.getValue(),
                    pair, false,
                    getMetaClass(entry.getValue())
            );
        }
        return doc;
    }

    public void write(HttpServletResponse response, Object o) throws IOException {
        Document doc = (Document) o;
        response.setContentType(MIME_STR);
        try {
            _transformer.transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public CommitRequest parseCommitRequest(String content) {
        try {
            LSInput lsInput = lsImpl.createLSInput();
            lsInput.setStringData(content);
            Document commitRequestDoc = requestConfigParser.parse(lsInput);
            Node rootNode = commitRequestDoc.getFirstChild();
            if (!"CommitRequest".equals(rootNode.getNodeName()))
                throw new IllegalArgumentException("Not a CommitRequest xml passed: " + rootNode.getNodeName());

            CommitRequest result = new CommitRequest();

            NodeList children = rootNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                String childNodeName = child.getNodeName();
                if ("commitInstances".equals(childNodeName)) {
                    NodeList entitiesNodeList = child.getChildNodes();
                    result.setCommitInstances(parseNodeList(result, entitiesNodeList));
                } else if ("removeInstances".equals(childNodeName)) {
                    NodeList entitiesNodeList = child.getChildNodes();
                    result.setRemoveInstances(parseNodeList(result, entitiesNodeList));
                } else if ("softDeletion".equals(childNodeName)) {
                    result.setSoftDeletion(Boolean.parseBoolean(child.getTextContent()));
                }
            }
            return result;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List parseNodeList(CommitRequest result, NodeList entitiesNodeList) throws InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException, ParseException {
        List entities = new ArrayList(entitiesNodeList.getLength());
        for (int j = 0; j < entitiesNodeList.getLength(); j++) {
            Node entityNode = entitiesNodeList.item(j);
            if (ELEMENT_INSTANCE.equals(entityNode.getNodeName())) {
                InstanceRef ref = result.parseInstanceRefAndRegister(getIdAttribute(entityNode));
                MetaClass metaClass = ref.getMetaClass();
                Object instance = ref.getInstance();
                parseEntity(result, instance, metaClass, entityNode);
                entities.add(instance);
            }
        }
        return entities;
    }

    private void parseEntity(CommitRequest commitRequest, Object bean, MetaClass metaClass, Node node)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException, ParseException {

        NodeList fields = node.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node fieldNode = fields.item(i);
            String fieldName = getFieldName(fieldNode);
            MetaProperty property = metaClass.getProperty(fieldName);

            if (MetadataHelper.isTransient(bean, fieldName))
                continue;

            String xmlValue = fieldNode.getTextContent();
            if (isNullValue(fieldNode)) {
                setNullField(bean, fieldName);
            }

            Object value;
            switch (property.getType()) {
                case DATATYPE:
                case ENUM:
                    if (property.getAnnotatedElement().isAnnotationPresent(Id.class)) {
                        // it was parsed in the beginning
                        continue;
                    }

                    value = property.getType() == MetaProperty.Type.DATATYPE ?
                            property.getRange().<Object>asDatatype().parse(xmlValue) :
                            property.getRange().asEnumeration().parse(xmlValue);
                    setField(bean, fieldName, value);
                    break;
                case AGGREGATION:
                case ASSOCIATION:
                    if (!property.getRange().getCardinality().isMany()) {
                        if (property.getAnnotatedElement().isAnnotationPresent(Embedded.class)) {
                            MetaClass embeddedMetaClass = property.getRange().asClass();
                            value = embeddedMetaClass.createInstance();
                            parseEntity(commitRequest, value, embeddedMetaClass, fieldNode);
                        } else {
                            value = parseEntityReference(fieldNode, commitRequest);
                        }
                        setField(bean, fieldName, value);
                    } else {
                        NodeList memberNodes = fieldNode.getChildNodes();
                        Collection<Object> members = property.getRange().isOrdered() ? new ArrayList<Object>() : new HashSet<Object>();
                        for (int memberIndex = 0; memberIndex < memberNodes.getLength(); memberIndex++) {
                            Node memberNode = memberNodes.item(memberIndex);
                            members.add(parseEntityReference(memberNode, commitRequest));
                        }
                        setField(bean, fieldName, members);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown property type");
            }
        }
    }

    private Object parseEntityReference(Node node, CommitRequest commitRequest)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException {
        Node childNode = node.getFirstChild();
        if (ELEMENT_NULL_REF.equals(childNode.getNodeName())) {
            return null;
        }

        InstanceRef ref = commitRequest.parseInstanceRefAndRegister(getIdAttribute(childNode));
        return ref.getInstance();
    }

    private void setField(Object result, String fieldName, Object value) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        new PropertyDescriptor(fieldName, result.getClass()).
                getWriteMethod().invoke(result, new Object[]{value});
    }

    private void setNullField(Object bean, String fieldName) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        setField(bean, fieldName, null);
    }

    private String getFieldName(Node fieldNode) {
        return getAttributeValue(fieldNode, ATTR_NAME);
    }

    private String getIdAttribute(Node node) {
        return getAttributeValue(node, ATTR_ID);
    }

    private String getAttributeValue(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getNodeValue();
    }

    /**
     * Create a new document with the given tag as the root element.
     *
     * @param rootTag the tag of the root element
     * @return the document element of a new document
     */

    public Element newDocument(String rootTag) {
        Document doc = _builder.newDocument();
        Element root = doc.createElement(rootTag);
        doc.appendChild(root);
        String[] nvpairs = new String[]{
                "xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI,
//                "xsi:noNamespaceSchemaLocation", INSTANCE_XSD,
                ATTR_VERSION, "1.0",
        };
        for (int i = 0; i < nvpairs.length; i += 2) {
            root.setAttribute(nvpairs[i], nvpairs[i + 1]);
        }
        return root;
    }


    Document decorate(Document doc, String uri) {
        Element root = doc.getDocumentElement();
        Element instance = (Element) root.getElementsByTagName(ELEMENT_INSTANCE).item(0);
        Element uriElement = doc.createElement(ELEMENT_URI);
        uriElement.setTextContent(uri == null ? NULL_VALUE : uri);
        root.insertBefore(uriElement, instance);
        return doc;
    }

    /**
     * Encodes the closure of a persistent instance into a XML element.
     *
     * @param entity    the managed instance to be encoded. Can be null.
     * @param parent    the parent XML element to which the new XML element be added. Must not be null. Must be
     *                  owned by a document.
     * @param isRef
     * @param metaClass
     * @return the new element. The element has been appended as a child to the given parent in this method.
     */
    private Element encodeEntityInstance(final Entity entity, final Element parent,
                                         boolean isRef, MetaClass metaClass)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (parent == null)
            throw new NullPointerException("No parent specified");

        Document doc = parent.getOwnerDocument();
        if (doc == null)
            throw new NullPointerException("No document specified");

        if (entity == null || isRef) {
            return encodeRef(parent, entity);
        }
        Element root = doc.createElement(ELEMENT_INSTANCE);
        parent.appendChild(root);
        root.setAttribute(ATTR_ID, ior(entity));
        List<MetaProperty> properties = ConvertorHelper.getOrderedProperties(metaClass);
        for (MetaProperty property : properties) {
            Element child;
            if (MetadataHelper.isTransient(entity, property.getName()))
                continue;

            Object value = ((Instance) entity).getValue(property.getName());
            switch (property.getType()) {
                case DATATYPE:
                    String nodeType;
                    if (property.getAnnotatedElement().isAnnotationPresent(Id.class)) {
                        nodeType = ELEMENT_REF;
                    } else if (property.getAnnotatedElement().isAnnotationPresent(Version.class)) {
                        nodeType = "version";
                    } else {
                        nodeType = "basic";
                    }
                    child = doc.createElement(nodeType);
                    child.setAttribute(ATTR_NAME, property.getName());
                    if (value == null) {
                        encodeNull(child);
                    } else {
                        String str = property.getRange().<Object>asDatatype().format(value);
                        encodeBasic(child, str, property.getJavaType());
                    }
                    break;
                case ENUM:
                    child = property.getAnnotatedElement().isAnnotationPresent(Id.class) ?
                            doc.createElement(ELEMENT_REF) :
                            doc.createElement("enum");
                    child.setAttribute(ATTR_NAME, property.getName());
                    if (value == null) {
                        encodeNull(child);
                    } else {
                        String str = property.getRange().asEnumeration().format(value);
                        encodeBasic(child, str, property.getJavaType());
                    }
                    break;
                case AGGREGATION:
                case ASSOCIATION:
                    if (!property.getRange().getCardinality().isMany()) {
                        boolean isEmbedded = property.getAnnotatedElement().isAnnotationPresent(Embedded.class);
                        child = doc.createElement(isEmbedded ?
                                "embedded" :
                                property.getRange().getCardinality().name().replace(UNDERSCORE, DASH).toLowerCase()
                        );
                        child.setAttribute(ATTR_NAME, property.getName());
                        child.setAttribute(ATTR_TYPE, typeOfEntityProperty(property));
                        if (isEmbedded) {
                            encodeEntityInstance((Entity) value, child, false, property.getRange().asClass());
                        } else {
                            encodeEntityInstance((Entity) value, child, true, property.getRange().asClass());
                        }
                    } else {
                        child = doc.createElement(getCollectionReferenceTag(property));
                        child.setAttribute(ATTR_NAME, property.getName());
                        child.setAttribute(ATTR_TYPE, typeOf(property.getJavaType()));
                        child.setAttribute(ATTR_MEMBER_TYPE, typeOfEntityProperty(property));
                        if (value == null) {
                            encodeNull(child);
                            break;
                        }
                        Collection<?> members = (Collection<?>) value;
                        for (Object o : members) {
                            Element member = doc.createElement(ELEMENT_MEMBER);
                            child.appendChild(member);
                            if (o == null) {
                                encodeNull(member);
                            } else {
                                encodeEntityInstance((Entity) o, member, true, property.getRange().asClass());
                            }
                        }
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown property type");
            }

            if (child != null) {
                root.appendChild(child);
            }
        }
        return root;
    }

    private String typeOfEntityProperty(MetaProperty property) {
        return property.getRange().asClass().getName();
    }

    /**
     * Sets the given value element as null. The <code>null</code> attribute is set to true.
     *
     * @param element the XML element to be set
     */
    private void encodeNull(Element element) {
        element.setAttribute(ATTR_NULL, "true");
    }

    private boolean isNullValue(Node fieldNode) {
        Node nullAttr = fieldNode.getAttributes().getNamedItem(ATTR_NULL);
        return nullAttr == null ?
                false :
                "true".equals(nullAttr.getNodeValue());
    }

    private Element encodeRef(Element parent, Entity entity) {
        Element ref = parent.getOwnerDocument().createElement(entity == null ? ELEMENT_NULL_REF : ELEMENT_REF);
        if (entity != null)
            ref.setAttribute(ATTR_ID, ior(entity));
        // IMPORTANT: for xml transformer not to omit the closing tag, otherwise dojo is confused
        ref.setTextContent(EMPTY_TEXT);
        parent.appendChild(ref);
        return ref;
    }


    /**
     * Sets the given value element. The <code>type</code> is set to the given runtime type.
     * String form of the given object is set as the text content.
     *
     * @param element     the XML element to be set
     * @param obj         value of the element. Never null.
     * @param runtimeType attribute type
     */
    private void encodeBasic(Element element, Object obj, Class<?> runtimeType) {
        element.setAttribute(ATTR_TYPE, typeOf(runtimeType));
        element.setTextContent(obj == null ? NULL_VALUE : obj.toString());
    }

    String ior(Entity entity) {
        return EntityLoadInfo.create(entity).toString();
    }

    String typeOf(Class<?> cls) {
        return cls.getSimpleName();
    }

    private String getCollectionReferenceTag(MetaProperty property) {
        return property.getRange().getCardinality().name().replace(UNDERSCORE, DASH).toLowerCase();
    }

    private MetaClass getMetaClass(Entity entity) {
        return MetadataProvider.getSession().getClass(entity.getClass());
    }
}
