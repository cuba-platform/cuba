/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.settings;

import org.dom4j.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SettingsElementWrapper implements Element {
    private final Element target;
    private final Settings settings;

    public SettingsElementWrapper(Element target, Settings settings) {
        this.target = target;
        this.settings = settings;
    }

    @Override
    public QName getQName() {
        return target.getQName();
    }

    @Override
    public void setQName(QName qname) {
        target.setQName(qname);

         modified();
    }

    @Override
    public Namespace getNamespace() {
        return target.getNamespace();
    }

    @Override
    public QName getQName(String qualifiedName) {
        return target.getQName(qualifiedName);
    }

    @Override
    public Namespace getNamespaceForPrefix(String prefix) {
        return target.getNamespaceForPrefix(prefix);
    }

    @Override
    public Namespace getNamespaceForURI(String uri) {
        return target.getNamespaceForURI(uri);
    }

    @Override
    public List<Namespace> getNamespacesForURI(String uri) {
        return target.getNamespacesForURI(uri);
    }

    @Override
    public String getNamespacePrefix() {
        return target.getNamespacePrefix();
    }

    @Override
    public String getNamespaceURI() {
        return target.getNamespaceURI();
    }

    @Override
    public String getQualifiedName() {
        return target.getQualifiedName();
    }

    @Override
    public List<Namespace> additionalNamespaces() {
        return target.additionalNamespaces();
    }

    @Override
    public List<Namespace> declaredNamespaces() {
        return target.declaredNamespaces();
    }

    @Override
    public Element addAttribute(String name, String value) {
        Element element = target.addAttribute(name, value);
        modified();
        return element;
    }

    @Override
    public Element addAttribute(QName qName, String value) {
        Element element = target.addAttribute(qName, value);
        modified();
        return element;
    }

    @Override
    public Element addComment(String comment) {
        Element element = target.addComment(comment);
        modified();
        return element;
    }

    @Override
    public Element addCDATA(String cdata) {
        Element element = target.addCDATA(cdata);
        modified();
        return element;
    }

    @Override
    public Element addEntity(String name, String text) {
        Element element = target.addEntity(name, text);
        modified();
        return element;
    }

    @Override
    public Element addNamespace(String prefix, String uri) {
        Element element = target.addNamespace(prefix, uri);
        modified();
        return element;
    }

    @Override
    public Element addProcessingInstruction(String target, String text) {
        Element element = this.target.addProcessingInstruction(target, text);
        modified();
        return element;
    }

    @Override
    public Element addProcessingInstruction(String target, Map<String, String> data) {
        Element element = this.target.addProcessingInstruction(target, data);
        modified();
        return element;
    }

    @Override
    public Element addText(String text) {
        Element element = target.addText(text);
        modified();
        return element;
    }

    @Override
    public void add(Attribute attribute) {
        target.add(attribute);
        modified();
    }

    @Override
    public void add(CDATA cdata) {
        target.add(cdata);
        modified();
    }

    @Override
    public void add(Entity entity) {
        target.add(entity);
        modified();
    }

    @Override
    public void add(Text text) {
        target.add(text);
        modified();
    }

    @Override
    public void add(Namespace namespace) {
        target.add(namespace);
        modified();
    }

    @Override
    public boolean remove(Attribute attribute) {
        boolean remove = target.remove(attribute);
        modified();
        return remove;
    }

    @Override
    public boolean remove(CDATA cdata) {
        boolean remove = target.remove(cdata);
        modified();
        return remove;
    }

    @Override
    public boolean remove(Entity entity) {
        boolean remove = target.remove(entity);
        modified();
        return remove;
    }

    @Override
    public boolean remove(Namespace namespace) {
        boolean remove = target.remove(namespace);
        modified();
        return remove;
    }

    @Override
    public boolean remove(Text text) {
        boolean remove = target.remove(text);
        modified();
        return remove;
    }

    @Override
    public String getText() {
        return target.getText();
    }

    @Override
    public String getTextTrim() {
        return target.getTextTrim();
    }

    @Override
    public String getStringValue() {
        return target.getStringValue();
    }

    @Override
    public Object getData() {
        return target.getData();
    }

    @Override
    public void setData(Object data) {
        target.setData(data);
        modified();
    }

    @Override
    public List<Attribute> attributes() {
        return target.attributes();
    }

    @Override
    public void setAttributes(List<Attribute> attributes) {
        target.setAttributes(attributes);
        modified();
    }

    @Override
    public int attributeCount() {
        return target.attributeCount();
    }

    @Override
    public Iterator<Attribute> attributeIterator() {
        return target.attributeIterator();
    }

    @Override
    public Attribute attribute(int index) {
        return target.attribute(index);
    }

    @Override
    public Attribute attribute(String name) {
        return target.attribute(name);
    }

    @Override
    public Attribute attribute(QName qName) {
        return target.attribute(qName);
    }

    @Override
    public String attributeValue(String name) {
        return target.attributeValue(name);
    }

    @Override
    public String attributeValue(String name, String defaultValue) {
        return target.attributeValue(name, defaultValue);
    }

    @Override
    public String attributeValue(QName qName) {
        return target.attributeValue(qName);
    }

    @Override
    public String attributeValue(QName qName, String defaultValue) {
        return target.attributeValue(qName, defaultValue);
    }

    @Override
    public void setAttributeValue(String name, String value) {
        target.setAttributeValue(name, value);
        modified();
    }

    @Override
    public void setAttributeValue(QName qName, String value) {
        target.setAttributeValue(qName, value);
        modified();
    }

    @Override
    public Element element(String name) {
        return target.element(name);
    }

    @Override
    public Element element(QName qName) {
        return target.element(qName);
    }

    @Override
    public List<Element> elements() {
        return target.elements();
    }

    @Override
    public List<Element> elements(String name) {
        return target.elements(name);
    }

    @Override
    public List<Element> elements(QName qName) {
        return target.elements(qName);
    }

    @Override
    public Iterator<Element> elementIterator() {
        return target.elementIterator();
    }

    @Override
    public Iterator<Element> elementIterator(String name) {
        return target.elementIterator(name);
    }

    @Override
    public Iterator<Element> elementIterator(QName qName) {
        return target.elementIterator(qName);
    }

    @Override
    public boolean isRootElement() {
        return target.isRootElement();
    }

    @Override
    public boolean hasMixedContent() {
        return target.hasMixedContent();
    }

    @Override
    public boolean isTextOnly() {
        return target.isTextOnly();
    }

    @Override
    public void appendAttributes(Element element) {
        target.appendAttributes(element);
        modified();
    }

    @Override
    public Element createCopy() {
        return target.createCopy();
    }

    @Override
    public Element createCopy(String name) {
        return target.createCopy(name);
    }

    @Override
    public Element createCopy(QName qName) {
        return target.createCopy(qName);
    }

    @Override
    public String elementText(String name) {
        return target.elementText(name);
    }

    @Override
    public String elementText(QName qname) {
        return target.elementText(qname);
    }

    @Override
    public String elementTextTrim(String name) {
        return target.elementTextTrim(name);
    }

    @Override
    public String elementTextTrim(QName qname) {
        return target.elementTextTrim(qname);
    }

    @Override
    public Node getXPathResult(int index) {
        return target.getXPathResult(index);
    }

    @Override
    public Node node(int index) throws IndexOutOfBoundsException {
        return target.node(index);
    }

    @Override
    public int indexOf(Node node) {
        return target.indexOf(node);
    }

    @Override
    public int nodeCount() {
        return target.nodeCount();
    }

    @Override
    public Element elementByID(String elementID) {
        return target.elementByID(elementID);
    }

    @Override
    public List<Node> content() {
        return target.content();
    }

    @Override
    public Iterator<Node> nodeIterator() {
        return target.nodeIterator();
    }

    @Override
    public void setContent(List<Node> content) {
        target.setContent(content);
        modified();
    }

    @Override
    public void appendContent(Branch branch) {
        target.appendContent(branch);
        modified();
    }

    @Override
    public void clearContent() {
        target.clearContent();
        modified();
    }

    @Override
    public List<ProcessingInstruction> processingInstructions() {
        return target.processingInstructions();
    }

    @Override
    public List<ProcessingInstruction> processingInstructions(String target) {
        return this.target.processingInstructions(target);
    }

    @Override
    public ProcessingInstruction processingInstruction(String target) {
        return this.target.processingInstruction(target);
    }

    @Override
    public void setProcessingInstructions(List<ProcessingInstruction> listOfPIs) {
        target.setProcessingInstructions(listOfPIs);
        modified();
    }

    @Override
    public Element addElement(String name) {
        Element element = target.addElement(name);
        modified();
        return element;
    }

    @Override
    public Element addElement(QName qname) {
        Element element = target.addElement(qname);
        modified();
        return element;
    }

    @Override
    public Element addElement(String qualifiedName, String namespaceURI) {
        Element element = target.addElement(qualifiedName, namespaceURI);
        modified();
        return element;
    }

    @Override
    public boolean removeProcessingInstruction(String target) {
        boolean b = this.target.removeProcessingInstruction(target);
        modified();
        return b;
    }

    @Override
    public void add(Node node) {
        target.add(node);
        modified();
    }

    @Override
    public void add(Comment comment) {
        target.add(comment);
        modified();
    }

    @Override
    public void add(Element element) {
        target.add(element);
        modified();
    }

    @Override
    public void add(ProcessingInstruction pi) {
        target.add(pi);
        modified();
    }

    @Override
    public boolean remove(Node node) {
        boolean remove = target.remove(node);
        modified();
        return remove;
    }

    @Override
    public boolean remove(Comment comment) {
        boolean remove = target.remove(comment);
        modified();
        return remove;
    }

    @Override
    public boolean remove(Element element) {
        boolean remove = target.remove(element);
        modified();
        return remove;
    }

    @Override
    public boolean remove(ProcessingInstruction pi) {
        boolean remove = target.remove(pi);
        modified();
        return remove;
    }

    @Override
    public void normalize() {
        target.normalize();
    }

    @Override
    public boolean supportsParent() {
        return target.supportsParent();
    }

    @Override
    public Element getParent() {
        return target.getParent();
    }

    @Override
    public void setParent(Element parent) {
        target.setParent(parent);
        modified();
    }

    @Override
    public Document getDocument() {
        return target.getDocument();
    }

    @Override
    public void setDocument(Document document) {
        target.setDocument(document);
        modified();
    }

    @Override
    public boolean isReadOnly() {
        return target.isReadOnly();
    }

    @Override
    public boolean hasContent() {
        return target.hasContent();
    }

    @Override
    public String getName() {
        return target.getName();
    }

    @Override
    public void setName(String name) {
        target.setName(name);
        modified();
    }

    @Override
    public void setText(String text) {
        target.setText(text);
        modified();
    }

    @Override
    public String getPath() {
        return target.getPath();
    }

    @Override
    public String getPath(Element context) {
        return target.getPath(context);
    }

    @Override
    public String getUniquePath() {
        return target.getUniquePath();
    }

    @Override
    public String getUniquePath(Element context) {
        return target.getUniquePath(context);
    }

    @Override
    public String asXML() {
        return target.asXML();
    }

    @Override
    public void write(Writer writer) throws IOException {
        target.write(writer);
    }

    @Override
    public short getNodeType() {
        return target.getNodeType();
    }

    @Override
    public String getNodeTypeName() {
        return target.getNodeTypeName();
    }

    @Override
    public Node detach() {
        return target.detach();
    }

    @Override
    public List<Node> selectNodes(String xpathExpression) {
        return target.selectNodes(xpathExpression);
    }

    @Override
    public Object selectObject(String xpathExpression) {
        return target.selectObject(xpathExpression);
    }

    @Override
    public List<Node> selectNodes(String xpathExpression, String comparisonXPathExpression) {
        return target.selectNodes(xpathExpression, comparisonXPathExpression);
    }

    @Override
    public List<Node> selectNodes(String xpathExpression, String comparisonXPathExpression, boolean removeDuplicates) {
        return target.selectNodes(xpathExpression, comparisonXPathExpression, removeDuplicates);
    }

    @Override
    public Node selectSingleNode(String xpathExpression) {
        return target.selectSingleNode(xpathExpression);
    }

    @Override
    public String valueOf(String xpathExpression) {
        return target.valueOf(xpathExpression);
    }

    @Override
    public Number numberValueOf(String xpathExpression) {
        return target.numberValueOf(xpathExpression);
    }

    @Override
    public boolean matches(String xpathExpression) {
        return target.matches(xpathExpression);
    }

    @Override
    public XPath createXPath(String xpathExpression) throws InvalidXPathException {
        return target.createXPath(xpathExpression);
    }

    @Override
    public Node asXPathResult(Element parent) {
        return target.asXPathResult(parent);
    }

    @Override
    public void accept(Visitor visitor) {
        target.accept(visitor);
    }

    @Override
    public Object clone() {
        throw new RuntimeException("Element wrapper is not cloneable");
    }

    protected void modified() {
        settings.setModified(true);
    }
}