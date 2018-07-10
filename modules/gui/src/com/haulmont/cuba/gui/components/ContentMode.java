package com.haulmont.cuba.gui.components;

/**
 * Defines how the client should interpret textual values.
 */
public enum ContentMode {
    /**
     * Textual values are displayed as plain text.
     */
    TEXT,

    /**
     * Textual values are displayed as preformatted text. In this mode newlines
     * are preserved when rendered on the screen.
     */
    PREFORMATTED,

    /**
     * Textual values are interpreted and displayed as HTML. Care should be
     * taken when using this mode to avoid Cross-site Scripting (XSS) issues.
     */
    HTML
}
