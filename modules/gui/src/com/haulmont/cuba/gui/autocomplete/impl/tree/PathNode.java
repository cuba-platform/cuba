package com.haulmont.cuba.jpql.impl.tree;

import com.haulmont.cuba.jpql.impl.DomainModel;
import com.haulmont.cuba.jpql.impl.EntityPath;
import com.haulmont.cuba.jpql.impl.HintProviderHelper;
import com.haulmont.cuba.jpql.impl.QueryVariableContext;
import com.haulmont.cuba.jpql.impl.pointer.Pointer;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 * Author: Alexander Chevelev
 * Date: 30.10.2010
 * Time: 4:15:07
 */
public class PathNode extends CommonTree {
    private String path;

    private PathNode(Token token, String path) {
        super(token);
        this.path = path;
    }

    public PathNode(int type, String path) {
        this(new CommonToken(type, path), path);
    }

    @Override
    public Tree dupNode() {
        return new PathNode(token, path);
    }

    public Pointer walk(DomainModel model, QueryVariableContext queryVC) {
        EntityPath entityPath = HintProviderHelper.parseEntityPath(path);
        Pointer pointer = entityPath.walk(model, queryVC);
        if (!(entityPath.lastEntityFieldPattern == null || "".equals(entityPath.lastEntityFieldPattern))) {
            pointer = pointer.next(model, entityPath.lastEntityFieldPattern);
        }
        return pointer;
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "Path is: " + path);
    }
}
