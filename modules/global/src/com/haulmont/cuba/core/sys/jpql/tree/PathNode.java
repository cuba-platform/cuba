package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.EntityPath;
import com.haulmont.cuba.core.sys.jpql.QueryVariableContext;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
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
        EntityPath entityPath = EntityPath.parseEntityPath(path);
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
