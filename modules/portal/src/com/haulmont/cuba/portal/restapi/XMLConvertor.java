/*
 * Based on JEST, part of the OpenJPA framework.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.datatypes.impl.StringDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.portal.config.RestConfig;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang.StringUtils;
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
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

/**
 * This class is deprecated and probably will be removed in next releases
 * It does not support new platform's major features as : dynamic attributes, in memory row level security
 * Please use XML API v.2
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public class XMLConvertor implements Convertor {
    public static final MimeType MIME_TYPE_XML;
    public static final String MIME_STR = "text/xml;charset=UTF-8";
    public static final String TYPE_XML = "xml";

    public static final String ELEMENT_INSTANCE = "instance";
    public static final String ELEMENT_URI = "uri";
    public static final String ELEMENT_REF = "ref";
    public static final String ELEMENT_NULL_REF = "null";
    public static final String ELEMENT_MEMBER = "member";
    public static final String ATTR_ID = "id";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_VERSION = "version";
    public static final String ATTR_NULL = "null";
    public static final String ATTR_MEMBER_TYPE = "member-type";
    public static final String NULL_VALUE = "null";

    protected static final String EMPTY_TEXT = " ";
    public static final char DASH = '-';
    public static final char UNDERSCORE = '_';

    public static final String ROOT_ELEMENT_INSTANCE = "instances";

    public static final String MAPPING_ROOT_ELEMENT_INSTANCE = "mapping";
    public static final String PAIR_ELEMENT = "pair";

    static {
        try {
            MIME_TYPE_XML = new MimeType(MIME_STR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Metadata metadata;

    protected final RestConfig restConfig;

    public XMLConvertor() {
        metadata = AppBeans.get(Metadata.NAME);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        restConfig = configuration.getConfig(RestConfig.class);
    }

    @Override
    public MimeType getMimeType() {
        return MIME_TYPE_XML;
    }

    @Override
    public String getType() {
        return TYPE_XML;
    }

    @Override
    public String process(Entity entity, MetaClass metaclass, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Element root = newDocument(ROOT_ELEMENT_INSTANCE);
        encodeEntityInstance(new HashSet<Entity>(), entity, root, false, metaclass, view);
        Document doc = root.getOwnerDocument();
        return documentToString(doc);
    }

    @Override
    public String process(List<Entity> entities, MetaClass metaClass, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Element root = newDocument(ROOT_ELEMENT_INSTANCE);
        for (Entity entity : entities) {
            encodeEntityInstance(new HashSet<Entity>(), entity, root, false, metaClass, view);
        }
        Document doc = root.getOwnerDocument();
        return documentToString(doc);
    }

    @Override
    public String process(Set<Entity> entities)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Element root = newDocument(MAPPING_ROOT_ELEMENT_INSTANCE);
        Document doc = root.getOwnerDocument();
        for (Entity entity : entities) {
            Element pair = doc.createElement(PAIR_ELEMENT);
            root.appendChild(pair);
            encodeEntityInstance(new HashSet<Entity>(), entity, pair, false, getMetaClass(entity), null);
            encodeEntityInstance(new HashSet<Entity>(), entity, pair, false, getMetaClass(entity), null);
        }
        return documentToString(doc);
    }

    @Override
    public CommitRequest parseCommitRequest(String content) {
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS lsImpl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSParser requestConfigParser = lsImpl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

            // Set options on the parser
            DOMConfiguration config = requestConfigParser.getDomConfig();
            config.setParameter("validate", Boolean.TRUE);
            config.setParameter("element-content-whitespace", Boolean.FALSE);
            config.setParameter("comments", Boolean.FALSE);
            requestConfigParser.setFilter(new LSParserFilter() {
                @Override
                public short startElement(Element elementArg) {
                    return LSParserFilter.FILTER_ACCEPT;
                }

                @Override
                public short acceptNode(Node nodeArg) {
                    return StringUtils.isBlank(nodeArg.getTextContent()) ?
                            LSParserFilter.FILTER_REJECT : LSParserFilter.FILTER_ACCEPT;
                }

                @Override
                public int getWhatToShow() {
                    return NodeFilter.SHOW_TEXT;
                }
            });
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

                    Set<String> commitIds = new HashSet<>(entitiesNodeList.getLength());
                    for (int j = 0; j < entitiesNodeList.getLength(); j++) {
                        Node idNode = entitiesNodeList.item(j).getAttributes().getNamedItem("id");
                        if (idNode == null)
                            continue;

                        String id = idNode.getTextContent();
                        if (id.startsWith("NEW-"))
                            id = id.substring(id.indexOf('-') + 1);
                        commitIds.add(id);
                    }

                    result.setCommitIds(commitIds);
                    result.setCommitInstances(parseNodeList(result, entitiesNodeList));
                } else if ("removeInstances".equals(childNodeName)) {
                    NodeList entitiesNodeList = child.getChildNodes();

                    List removeInstances = parseNodeList(result, entitiesNodeList);
                    result.setRemoveInstances(removeInstances);
                } else if ("softDeletion".equals(childNodeName)) {
                    result.setSoftDeletion(Boolean.parseBoolean(child.getTextContent()));
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List parseNodeList(CommitRequest commitRequest, NodeList entitiesNodeList) throws InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException, ParseException {
        List entities = new ArrayList(entitiesNodeList.getLength());
        for (int j = 0; j < entitiesNodeList.getLength(); j++) {
            Node entityNode = entitiesNodeList.item(j);
            if (ELEMENT_INSTANCE.equals(entityNode.getNodeName())) {
                InstanceRef ref = commitRequest.parseInstanceRefAndRegister(getIdAttribute(entityNode));
                MetaClass metaClass = ref.getMetaClass();
                Object instance = ref.getInstance();
                parseEntity(commitRequest, instance, metaClass, entityNode);
                entities.add(instance);
            }
        }
        return entities;
    }

    private void parseEntity(CommitRequest commitRequest, Object bean, MetaClass metaClass, Node node)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException, ParseException {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        NodeList fields = node.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node fieldNode = fields.item(i);
            String fieldName = getFieldName(fieldNode);
            MetaProperty property = metaClass.getProperty(fieldName);

            if (!attrModifyPermitted(metaClass, property.getName()))
                continue;

            if (metadataTools.isTransient(bean, fieldName))
                continue;

            String xmlValue = fieldNode.getTextContent();
            if (isNullValue(fieldNode)) {
                setNullField(bean, fieldName);
                continue;
            }

            Object value;

            switch (property.getType()) {
                case DATATYPE:
                    if (property.getAnnotatedElement().isAnnotationPresent(Id.class)) {
                        // it was parsed in the beginning
                        continue;
                    }

                    String typeName = property.getRange().asDatatype().getName();
                    if (!StringDatatype.NAME.equals(typeName) && "null".equals(xmlValue)) {
                        value = null;
                    } else {
                        value = property.getRange().asDatatype().parse(xmlValue);
                    }
                    setField(bean, fieldName, value);
                    break;
                case ENUM:
                    value = property.getRange().asEnumeration().parse(xmlValue);
                    setField(bean, fieldName, value);
                    break;
                case COMPOSITION:
                case ASSOCIATION: {
                    if ("null".equals(xmlValue)) {
                        setField(bean, fieldName, null);
                        break;
                    }
                    MetaClass propertyMetaClass = propertyMetaClass(property);
                    //checks if the user permitted to read and update a property
                    if (!updatePermitted(propertyMetaClass) && !readPermitted(propertyMetaClass))
                        break;

                    if (!property.getRange().getCardinality().isMany()) {
                        if (property.getAnnotatedElement().isAnnotationPresent(Embedded.class)) {
                            MetaClass embeddedMetaClass = property.getRange().asClass();
                            value = metadata.create(embeddedMetaClass);
                            parseEntity(commitRequest, value, embeddedMetaClass, fieldNode);
                        } else {
                            String id = getRefId(fieldNode);

                            //reference to an entity that also a commit instance
                            //will be registered later
                            if (commitRequest.getCommitIds().contains(id)) {
                                EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);
                                Entity ref = metadata.create(loadInfo.getMetaClass());
                                ref.setValue("id", loadInfo.getId());
                                setField(bean, fieldName, ref);
                                break;
                            }

                            value = parseEntityReference(fieldNode, commitRequest);
                        }
                        setField(bean, fieldName, value);
                    } else {
                        NodeList memberNodes = fieldNode.getChildNodes();
                        Collection<Object> members =
                                property.getRange().isOrdered() ? new ArrayList<>() : new HashSet<>();

                        for (int memberIndex = 0; memberIndex < memberNodes.getLength(); memberIndex++) {
                            Node memberNode = memberNodes.item(memberIndex);
                            members.add(parseEntityReference(memberNode, commitRequest));
                        }
                        setField(bean, fieldName, members);
                    }
                    break;
                }
                default:
                    throw new IllegalStateException("Unknown property type");
            }
        }
    }

    private String getRefId(Node refNode) {
        Node childNode = refNode.getFirstChild();
        do {
            if (ELEMENT_REF.equals(childNode.getNodeName())) {
                Node idNode = childNode.getAttributes().getNamedItem(ATTR_ID);
                return idNode != null ? idNode.getTextContent() : null;
            }
            childNode = childNode.getNextSibling();
        } while (childNode != null);
        return null;
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
                getWriteMethod().invoke(result, value);
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
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = builder.newDocument();
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
     * @param visited
     * @param entity    the managed instance to be encoded. Can be null.
     * @param parent    the parent XML element to which the new XML element be added. Must not be null. Must be
     *                  owned by a document.
     * @param isRef
     * @param metaClass @return the new element. The element has been appended as a child to the given parent in this method.
     * @param view view on which loaded the entity
     */
    private Element encodeEntityInstance(HashSet<Entity> visited, final Entity entity, final Element parent,
                                         boolean isRef, MetaClass metaClass, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!readPermitted(metaClass))
            return null;

        if (parent == null)
            throw new NullPointerException("No parent specified");

        Document doc = parent.getOwnerDocument();
        if (doc == null)
            throw new NullPointerException("No document specified");

        if (entity == null) {
            return encodeRef(parent, entity);
        }

        isRef |= !visited.add(entity);

        if (isRef) {
            return encodeRef(parent, entity);
        }
        Element root = doc.createElement(ELEMENT_INSTANCE);
        parent.appendChild(root);
        root.setAttribute(ATTR_ID, ior(entity));

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        List<MetaProperty> properties = ConvertorHelper.getOrderedProperties(metaClass);
        for (MetaProperty property : properties) {
            Element child;

            if (!attrViewPermitted(metaClass, property.getName()))
                continue;

            if (!isPropertyIncluded(view, property, metadataTools)){
                continue;
            }

            Object value = entity.getValue(property.getName());
            switch (property.getType()) {
                case DATATYPE:
                    String nodeType;
                    if (property.equals(metadataTools.getPrimaryKeyProperty(metaClass))
                            && !property.getJavaType().equals(String.class)) {
                        // skipping id for non-String-key entities
                        continue;
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
                        String str = property.getRange().asDatatype().format(value);
                        encodeBasic(child, str, property.getJavaType());
                    }
                    break;
                case ENUM:
                    child = doc.createElement("enum");
                    child.setAttribute(ATTR_NAME, property.getName());
                    if (value == null) {
                        encodeNull(child);
                    } else {
                        //noinspection unchecked
                        String str = property.getRange().asEnumeration().format(value);
                        encodeBasic(child, str, property.getJavaType());
                    }
                    break;
                case COMPOSITION:
                case ASSOCIATION: {
                    MetaClass meta = propertyMetaClass(property);
                    //checks if the user permitted to read a property
                    if (!readPermitted(meta)) {
                        child = null;
                        break;
                    }

                    View propertyView = (view == null ? null : view.getProperty(property.getName()).getView());

                    if (!property.getRange().getCardinality().isMany()) {
                        boolean isEmbedded = property.getAnnotatedElement().isAnnotationPresent(Embedded.class);
                        child = doc.createElement(isEmbedded ?
                                "embedded" :
                                property.getRange().getCardinality().name().replace(UNDERSCORE, DASH).toLowerCase()
                        );
                        child.setAttribute(ATTR_NAME, property.getName());
                        if (isEmbedded) {
                            encodeEntityInstance(visited, (Entity) value, child, false,
                                    property.getRange().asClass(), propertyView);
                        } else {
                            encodeEntityInstance(visited, (Entity) value, child, false,
                                    property.getRange().asClass(), propertyView);
                        }
                    } else {
                        child = doc.createElement(getCollectionReferenceTag(property));
                        child.setAttribute(ATTR_NAME, property.getName());
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
                                encodeEntityInstance(visited, (Entity) o, member, true,
                                        property.getRange().asClass(), propertyView);
                            }
                        }
                    }
                    break;
                }
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

    private MetaClass propertyMetaClass(MetaProperty property) {
        return property.getRange().asClass();
    }

    /**
     * Sets the given value element as null. The <code>null</code> attribute is set to true.
     *
     * @param element the XML element to be set
     */
    protected void encodeNull(Element element) {
        element.setAttribute(ATTR_NULL, "true");
    }

    protected boolean isPropertyIncluded(View view, MetaProperty metaProperty, MetadataTools metadataTools) {
        if (view == null) {
            return true;
        }

        ViewProperty viewProperty = view.getProperty(metaProperty.getName());
        return (viewProperty != null);
    }

    protected boolean isNullValue(Node fieldNode) {
        Node nullAttr = fieldNode.getAttributes().getNamedItem(ATTR_NULL);
        return nullAttr == null ?
                false :
                "true".equals(nullAttr.getNodeValue());
    }

    protected Element encodeRef(Element parent, Entity entity) {
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
    protected void encodeBasic(Element element, Object obj, Class<?> runtimeType) {
        element.setTextContent(obj == null ? NULL_VALUE : obj.toString());
    }

    String ior(Entity entity) {
        return EntityLoadInfo.create(entity).toString();
    }

    String typeOf(Class<?> cls) {
        return cls.getSimpleName();
    }

    protected String getCollectionReferenceTag(MetaProperty property) {
        return property.getRange().getCardinality().name().replace(UNDERSCORE, DASH).toLowerCase();
    }

    protected MetaClass getMetaClass(Entity entity) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClass(entity.getClass());
    }

    protected boolean attrViewPermitted(MetaClass metaClass, String property) {
        return attrPermitted(metaClass, property, EntityAttrAccess.VIEW);
    }

    protected boolean attrModifyPermitted(MetaClass metaClass, String property) {
        return attrPermitted(metaClass, property, EntityAttrAccess.MODIFY);
    }

    protected boolean attrPermitted(MetaClass metaClass, String property, EntityAttrAccess entityAttrAccess) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityAttrPermitted(metaClass, property, entityAttrAccess);
    }

    protected boolean readPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.READ);
    }

    protected boolean updatePermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.UPDATE);
    }

    protected boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(metaClass, entityOp);
    }

    @Override
    public String processServiceMethodResult(Object result, Class resultType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServiceRequest parseServiceRequest(String content) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity parseEntity(String content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection parseEntitiesCollection(String content, Class<? extends Collection> collectionClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Integer> getApiVersions() {
        return Arrays.asList(1);
    }

    protected String documentToString(Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}