package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.Parser;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.pointer.CollectionPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.NoPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 20:44:41
 */
public class HintProvider {
    private DomainModel model;
    private static final char CARET_POSITION_SYMBOL = '~';

    public HintProvider(DomainModel model) {
        if (model == null)
            throw new NullPointerException("No model passed");
        this.model = model;
    }

    public static String getLastWord(String queryString, int caretPosition) {
        if (caretPosition < 0)
            return "";

        // todo
        if (queryString.charAt(caretPosition) == ' ') {
            return "";
        }
        int lastWordStart = queryString.lastIndexOf(' ', caretPosition);
        return queryString.substring(lastWordStart + 1, caretPosition + 1);
    }

    public HintResponse requestHint(String queryStringWithCaret) throws RecognitionException {
        int caretPosition = queryStringWithCaret.indexOf(CARET_POSITION_SYMBOL);
        if (caretPosition == -1)
            throw new IllegalStateException("No caret position found");

        if (caretPosition == 0)
            throw new IllegalStateException("Caret at the beginning of the qeury");

        caretPosition -= 1;
        String queryString = queryStringWithCaret.substring(0, caretPosition + 1) +
                queryStringWithCaret.substring(caretPosition + 2);

        return requestHint(queryString, caretPosition);
    }

    public HintResponse requestHint(String input, int cursorPos) throws RecognitionException {
        // todo текст-дерево-обратная генерация запроса по дереву будет съедать эти символы в исходном выражении
        input = input.replace("\n", " ");
        input = input.replace("\r", " ");
        input = input.replace("\t", " ");
        String lastWord = getLastWord(input, cursorPos);

        CommonTree tree = Parser.parse(input);

        return (!lastWord.contains(".")) ?
                hintEntityName(lastWord) :
                hintFieldName(lastWord, tree, cursorPos);
    }

    private HintResponse hintFieldName(String lastWord, CommonTree tree, int caretPosition) {
        TreeVisitor visitor = new TreeVisitor();
        IdVarSelector idVarSelector = new IdVarSelector(model);
        visitor.visit(tree, idVarSelector);
        List<ErrorRec> errorRecs = idVarSelector.getInvalidNodes();
        QueryVariableContext root = idVarSelector.getContextTree();
        QueryVariableContext queryVC = root.getContextByCaretPosition(caretPosition);

        EntityPath path = EntityPath.parseEntityPath(lastWord);
        Pointer pointer = path.walk(model, queryVC);
        if (pointer instanceof NoPointer) {
            List<String> errorMessages = prepareErrorMessages(errorRecs);
            errorMessages.add(0, "Cannot parse [" + lastWord + "]");
            return new HintResponse("Query error", errorMessages);
        }

        if (pointer instanceof CollectionPointer) {
            List<String> errorMessages = prepareErrorMessages(errorRecs);
            errorMessages.add(0, "Cannot get attribute of collection [" + lastWord + "]");
            return new HintResponse("Query error", errorMessages);
        }

        if (!(pointer instanceof EntityPointer)) {
            List<String> errorMessages = prepareErrorMessages(errorRecs);
            return new HintResponse("Query error", errorMessages);
        }

        Entity targetEntity = ((EntityPointer) pointer).getEntity();
        List<Attribute> attributes = targetEntity.findAttributesStartingWith(
                path.lastEntityFieldPattern);

        List<Option> options = new ArrayList<Option>();
        for (Attribute attribute : attributes) {
            options.add(new Option(attribute.getName(), attribute.getUserFriendlyName()));
        }
        return new HintResponse(options, path.lastEntityFieldPattern);
    }

    private List<String> prepareErrorMessages(List<ErrorRec> errorRecs) {
        List<String> errorMessages = new ArrayList<String>();
        for (ErrorRec errorRec : errorRecs) {
            CommonTree errorNode = errorRec.node;
            StringBuilder b = new StringBuilder();
            for (Object child : errorNode.getChildren()) {
                CommonTree childNode = (CommonTree) child;
                b.append(childNode.getText());
            }
            String errorMessage = "Error near: \"" + b + "\"";
            errorMessages.add(errorMessage);
        }
        return errorMessages;
    }

    private HintResponse hintEntityName(String lastWord) {
        List<Entity> matchingEntities = model.findEntitiesStartingWith(lastWord);

        List<Option> options = new ArrayList<Option>();
        for (Entity entity : matchingEntities) {
            options.add(new Option(entity.getName(), entity.getUserFriendlyName()));
        }
        return new HintResponse(options, lastWord);
    }
}
