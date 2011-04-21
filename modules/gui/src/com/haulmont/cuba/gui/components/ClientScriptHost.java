package com.haulmont.cuba.gui.components;

/**
 * Component for evaluate custom script code at client side
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ClientScriptHost extends Component, Component.BelongToFrame{

    void evaluateScript(String script);

    void viewDocument(String documentUrl);

    void getResource(String resourceUrl);
}