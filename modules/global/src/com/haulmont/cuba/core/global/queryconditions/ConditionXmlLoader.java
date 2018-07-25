package com.haulmont.cuba.core.global.queryconditions;

import com.haulmont.bali.util.Dom4j;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads the tree of {@link Condition}s from XML.
 * <p>
 * Use {@link #addFactory(String, Function)} method to add your own functions creating conditions from XML elements.
 * By default, {@link LogicalCondition} and {@link JpqlCondition} are supported.
 */
@Component(ConditionXmlLoader.NAME)
public class ConditionXmlLoader {

    public static final String NAME = "cuba_ConditionXmlLoader";

    private Map<String, Function<Element, Condition>> factories = new LinkedHashMap<>();

    public ConditionXmlLoader() {
        factories.put("and",
                element -> {
                    if (element.getName().equals("and")) {
                        Condition condition = new LogicalCondition(LogicalCondition.Type.AND);
                        for (Element el : element.elements()) {
                            ((LogicalCondition) condition).getConditions().add(fromXml(el));
                        }
                        return condition;
                    } else {
                        return null;
                    }
        });
        factories.put("or",
                element -> {
                    if (element.getName().equals("or")) {
                        Condition condition = new LogicalCondition(LogicalCondition.Type.OR);
                        for (Element el : element.elements()) {
                            ((LogicalCondition) condition).getConditions().add(fromXml(el));
                        }
                        return condition;
                    } else {
                        return null;
                    }
        });
        factories.put("jpql",
                element -> {
                    if (element.getName().equals("jpql")) {
                        List<PropertyCondition.Entry> entries = element.elements().stream()
                                .map(el -> new PropertyCondition.Entry(el.getName(), el.getText()))
                                .collect(Collectors.toList());
                        return new JpqlCondition(entries);
                    }
                    return null;
        });
    }

    /**
     * Adds a function creating a condition from XML element.
     * @param name      name that can be used later in {@link #removeFactory(String)} method to remove the function
     * @param factory   function creating a condition from XML element
     */
    public void addFactory(String name, Function<Element, Condition> factory) {
        factories.put(name, factory);
    }

    /**
     * Removes a factory by its name.
     */
    public void removeFactory(String name) {
        factories.remove(name);
    }

    /**
     * Creates a conditions tree from XML string.
     */
    public Condition fromXml(String xml) {
        Element element = Dom4j.readDocument(xml).getRootElement();
        return fromXml(element);
    }

    /**
     * Creates a conditions tree from XML element.
     */
    public Condition fromXml(Element element) {
        for (Function<Element, Condition> factory : factories.values()) {
            Condition condition = factory.apply(element);
            if (condition != null)
                return condition;
        }
        throw new RuntimeException("Cannot create condition for element " + element.getName());
    }
}
