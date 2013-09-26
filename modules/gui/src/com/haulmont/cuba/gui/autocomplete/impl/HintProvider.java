/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.NoEntity;
import com.haulmont.cuba.core.sys.jpql.pointer.CollectionPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.NoPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chevelev
 * @version $Id$
 */
public class HintProvider {
    private DomainModel model;
    private static final char CARET_POSITION_SYMBOL = '~';
    public static final Pattern COLLECTION_MEMBER_PATTERN = Pattern.compile(".*\\sin\\s*[(]\\s*[a-zA-Z0-9]+[.][a-zA-Z0-9.]*$");
    public static final Pattern JOIN_PATTERN = Pattern.compile(".*\\sjoin\\s*[a-zA-Z0-9]+[.][a-zA-Z0-9.]*$");

    public HintProvider(DomainModel model) {
        if (model == null)
            throw new NullPointerException("No model passed");
        this.model = model;
    }

    /**
     * Returns word in query denoting entity or field parameter user have requested hint for
     * @param queryString query string
     * @param caretPosition caret position
     * @return matched word or empty string
     */
    public static String getLastWord(String queryString, int caretPosition) {
        if (caretPosition < 0)
            return "";

        if (Character.isSpaceChar(queryString.charAt(caretPosition))) {
            return "";
        }

        String[] words = queryString.substring(0, caretPosition + 1).split("\\s");
        String result = words[words.length - 1];

        if (result.startsWith("in("))
            result = result.substring(3);

        return result;
    }

    public HintResponse requestHint(String queryStringWithCaret) throws RecognitionException {
        int caretPosition = queryStringWithCaret.indexOf(CARET_POSITION_SYMBOL);
        if (caretPosition == -1)
            throw new IllegalStateException("No caret position found");

        if (caretPosition == 0)
            throw new IllegalStateException("Caret at the beginning of the query");

        caretPosition -= 1;
        String queryString = queryStringWithCaret.substring(0, caretPosition + 1) +
                queryStringWithCaret.substring(caretPosition + 2);

        MacroProcessor macroProcessor = new MacroProcessor();
        HintRequest hintRequest = macroProcessor.inlineFake(queryString, caretPosition);
        return requestHint(hintRequest);
    }

    public HintResponse requestHint(HintRequest hintRequest) throws RecognitionException {
        String input = hintRequest.getQuery();
        int cursorPos = hintRequest.getPosition();
        Set<InferredType> expectedTypes = hintRequest.getExpectedTypes() == null ?
                EnumSet.of(InferredType.Any) :
                hintRequest.getExpectedTypes();
        String lastWord = getLastWord(input, cursorPos);
        expectedTypes = narrowExpectedTypes(input, cursorPos, expectedTypes);

        return (!lastWord.contains(".")) ?
                hintEntityName(lastWord) :
                hintFieldName(lastWord, input, cursorPos, expectedTypes);
    }

    private HintResponse hintFieldName(String lastWord, String input, int caretPosition, Set<InferredType> expectedTypes) throws RecognitionException {
        QueryTreeAnalyzer queryAnalyzer = new QueryTreeAnalyzer();
        queryAnalyzer.prepare(model, input);
        List<ErrorRec> errorRecs = queryAnalyzer.getInvalidIdVarNodes();
        QueryVariableContext root = queryAnalyzer.getRootQueryVariableContext();
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

        List<Option> options = new ArrayList<>();
        Entity targetEntity = ((EntityPointer) pointer).getEntity();
        if (targetEntity instanceof NoEntity)
            return new HintResponse(options, path.lastEntityFieldPattern);

        List<Attribute> attributes = targetEntity.findAttributesStartingWith(
                path.lastEntityFieldPattern, expectedTypes);

        for (Attribute attribute : attributes) {
            options.add(new Option(attribute.getName(), attribute.getUserFriendlyName()));
        }
        return new HintResponse(options, path.lastEntityFieldPattern);
    }

    private List<String> prepareErrorMessages(List<ErrorRec> errorRecs) {
        List<String> errorMessages = new ArrayList<>();
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

        List<Option> options = new ArrayList<>();
        for (Entity entity : matchingEntities) {
            options.add(new Option(entity.getName(), entity.getUserFriendlyName()));
        }
        return new HintResponse(options, lastWord);
    }

    public static Set<InferredType> narrowExpectedTypes(String input, int cursorPos, Set<InferredType> expectedTypes) {
        if (StringUtils.isEmpty(input)) {
            return expectedTypes;
        }

        if (cursorPos >= 0 && cursorPos < input.length() && input.charAt(cursorPos) == ' ') {
            return expectedTypes;
        }

        String matchingInput = input.substring(0, cursorPos + 1);
        Matcher matcher = COLLECTION_MEMBER_PATTERN.matcher(matchingInput);
        if (matcher.matches()) {
            return EnumSet.of(InferredType.Collection, InferredType.Entity);
        }
        matcher = JOIN_PATTERN.matcher(matchingInput);
        if (matcher.matches()) {
            return EnumSet.of(InferredType.Collection, InferredType.Entity);
        }
        return expectedTypes;
    }
}