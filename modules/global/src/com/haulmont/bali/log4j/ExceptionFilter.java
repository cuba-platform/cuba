/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.bali.log4j;

import com.haulmont.cuba.core.global.RemoteException;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>Log4J filter.</p>
 * <p>Excludes all exceptions that was declared in children's <code>&lt;exclude .../&gt;</code> tag by its
 * class or message pattern declared as a regular expression. Filters only by class name and does not exclude
 * any subclasses' instances of listed exceptions. Possible usage:</p>
 * <pre>
 * &lt;appender ...&gt;
 *      &lt;.../&gt;
 *      &lt;filter class="com.haulmont.bali.log4j.ExceptionFilter"&gt;
 *          &lt;exclude class="com.haulmont.cuba.security.global.NoUserSessionException"/&gt;
 *          &lt;exclude class="com.haulmont.cuba.security.global.LoginException"&gt;
 *              &lt;message-pattern&gt;Unknown login name or bad password: .+&lt;/message-pattern&gt;
 *          &lt;/exclude&gt;
 *      &lt;/filter&gt;
 * &lt;/appender&gt;
 * </pre>
 * <p>If <code>message-pattern</code> tags are omitted exception with specified class is always excluded.
 * One exception class can have many <code>message-pattern</code> declarations:</p>
 * <pre>
 * &lt;exclude class="some.package.SomeClass"&gt;
 *     &lt;message-pattern&gt;Pattern&lt;/message-pattern&gt;
 *     &lt;message-pattern&gt;Another pattern&lt;/message-pattern&gt;
 * &lt;/exclude&gt;
 * </pre>
 * <p>Message filters can be applied to all exception classes if <code>class</code> attribute is <code>"ALL"</code>
 * For instance:</p>
 * <pre>
 * &lt;exclude class="ALL"&gt;
 *      &lt;message-pattern&gt;Some weird message pattern&lt;/message-pattern&gt;
 * &lt;/exclude&gt;
 * </pre>
 * <p><strong>Note:</strong> This filter does not perform checking or any kind of validation. Any other child nodes
 * and attributes are <em>ignored</em>. Invalid class names in
 * <code>class</code> attribute are <em>accepted</em>.</p>
 *
 * @author kozlov
 * @version $Id$
 */
public class ExceptionFilter extends Filter implements UnrecognizedElementHandler {

    private static final String GLOBAL = "ALL";

    private Map<String, List<Pattern>> excluded = new HashMap<>();

    {
        excluded.put(GLOBAL, new ArrayList<Pattern>());
    }

    @Override
    public int decide(LoggingEvent event) {
        ThrowableInformation information = event.getThrowableInformation();
        if (information == null) {
            return NEUTRAL;
        }
        Throwable throwable = information.getThrowable();
        if (throwable instanceof RemoteException) {
            RemoteException remote = (RemoteException) throwable;
            List<RemoteException.Cause> causes = remote.getCauses();
            if (causes != null && !causes.isEmpty()) {
                return checkThrowable(causes.get(0).getThrowable());
            } else {
                return NEUTRAL;
            }
        }
        return checkThrowable(throwable);
    }

    @Override
    public boolean parseUnrecognizedElement(Element element, Properties props) {
        if (!element.getTagName().equals("exclude")) {
            return false;
        }
        String className = element.getAttribute("class");
        NodeList children = element.getChildNodes();
        List<Pattern> patterns = new ArrayList<>();
        for (int j = 0; j < children.getLength(); j++) {
            Node node = children.item(j);
            if (node instanceof Element) {
                Element patternNode = (Element) node;
                if (patternNode.getTagName().equals("message-pattern")) {
                    String content = patternNode.getTextContent();
                    try {
                        patterns.add(Pattern.compile(content));
                    } catch (PatternSyntaxException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
        }
        excluded.put(className, patterns);
        return true;
    }

    private int checkThrowable(Throwable exception) {
        if (exception == null) {
            return NEUTRAL;
        }
        String message = exception.getMessage();
        String className = exception.getClass().getName();
        if (message != null) {
            for (Pattern pattern : excluded.get(GLOBAL)) {
                if (pattern.matcher(message).matches()) {
                    return DENY;
                }
            }
        }
        if (excluded.containsKey(className)) {
            List<Pattern> messagePatterns = excluded.get(className);
            if (messagePatterns.isEmpty()) {
                return DENY;
            } else if (message != null) {
                for (Pattern pattern : messagePatterns) {
                    if (pattern.matcher(message).matches()) {
                        return DENY;
                    }
                }
            }
        }
        return NEUTRAL;
    }
}
