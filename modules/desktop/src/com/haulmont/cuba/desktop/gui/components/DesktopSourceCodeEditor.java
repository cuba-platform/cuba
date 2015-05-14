/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.Suggester;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopSourceCodeEditor extends DesktopAbstractTextField<RSyntaxTextArea> implements SourceCodeEditor {

    protected JComponent composition;

    protected Suggester suggester;
    protected Mode mode;

    protected boolean showGutter = true;
    protected boolean showPrintMargin = true;
    protected boolean highlightActiveLine = true;

    @Override
    protected RSyntaxTextArea createTextComponentImpl() {
        RSyntaxTextArea impl = new RSyntaxTextArea();

        int height = (int) impl.getPreferredSize().getHeight();
        impl.setMinimumSize(new Dimension(0, height));

        composition = new JScrollPane(impl);
        composition.setPreferredSize(new Dimension(150, height));
        composition.setMinimumSize(new Dimension(0, height));

        doc.putProperty("filterNewlines", false);

        return impl;
    }

    @Override
    public JComponent getComposition() {
        return composition;
    }

    @Override
    protected TextFieldListener createTextListener() {
        return new TextFieldListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                updateMissingValueState();
            }
        };
    }

    @Override
    protected Document createDocument() {
        return new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;

        switch (mode) {
            case Groovy:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
                break;

            case HTML:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
                break;

            case Java:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                break;

            case JavaScript:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
                break;

            case Properties:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
                break;

            case SQL:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
                break;

            case XML:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
                break;

            case Text:
                impl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                break;
        }
    }

    @Override
    public Suggester getSuggester() {
        return suggester;
    }

    @Override
    public void setSuggester(Suggester suggester) {
        this.suggester = suggester;
    }

    @Override
    public AutoCompleteSupport getAutoCompleteSupport() {
        return new AutoCompleteSupport() {
            @Override
            public int getCursorPosition() {
                return 0;
            }

            @Override
            public Object getValue() {
                return null;
            }
        };
    }

    @Override
    public void setShowGutter(boolean showGutter) {
        this.showGutter = showGutter;
    }

    @Override
    public boolean isShowGutter() {
        return showGutter;
    }

    @Override
    public void setShowPrintMargin(boolean showPrintMargin) {
        this.showPrintMargin = showPrintMargin;
    }

    @Override
    public boolean isShowPrintMargin() {
        return showPrintMargin;
    }

    @Override
    public void setHighlightActiveLine(boolean highlightActiveLine) {
        this.highlightActiveLine = highlightActiveLine;
    }

    @Override
    public boolean isHighlightActiveLine() {
        return highlightActiveLine;
    }

}