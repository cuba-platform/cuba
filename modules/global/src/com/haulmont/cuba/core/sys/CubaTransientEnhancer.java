/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Devyatkin
 * Created: 05.03.11 11:07
 *
 * $Id$
 */
/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Devyatkin
 * Created: 03.03.11 14:06
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;


import com.haulmont.bali.util.Dom4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import org.dom4j.Element;
import serp.bytecode.*;
import serp.bytecode.visitor.BCVisitor;


import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class CubaTransientEnhancer {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error:No argument");
            return;
        }
        String metaConfigName = args[0];

        try {
            FileInputStream in = new FileInputStream(metaConfigName);
            Document document = Dom4j.readDocument(in);
            Collection<BCClass> classes = getBCClasses(document);
            CubaTransientEnhancer enhancer = new CubaTransientEnhancer();
            for (BCClass cl : classes) {
                enhancer.enhanceSetters(cl);
                cl.write();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enhanceSetters(BCClass editingClass) {
        BCMethod[] methods = editingClass.getDeclaredMethods();
        Code code;
        for (BCMethod method : methods) {
            String name = method.getName();
            if ((!name.startsWith("set")) || (method.getReturnType() != void.class))
                continue;
            code = method.getCode(false);
            LocalVariableTable table =  code.getLocalVariableTable(false);
            if (table.getLocalVariable(StringUtils.lowerCase(name.replace("set","")+"_local"))!=null){
                return;
            }


            String fieldName = StringUtils.uncapitalize(name.replace("set",""));
            code.aload().setThis();
            table.addLocalVariable(StringUtils.lowerCase(name.replace("set","")+"_local"),method.getParamTypes()[0]).setStartPc(5);
            code.invokevirtual().setMethod("get" + StringUtils.capitalize(fieldName) , method.getParamTypes()[0], new Class[]{});
            code.astore().setLocal(2);

            code.afterLast();
            Instruction vreturn = code.previous();
            code.before(vreturn);

            code.aload().setLocal(2);
            code.aload().setLocal(1);
            code.invokestatic().setMethod(ObjectUtils.class, "equals", boolean.class, new Class[]{Object.class,Object.class});
            IfInstruction ifne = code.ifne();
            code.aload().setThis();
            code.constant().setValue(fieldName);
            code.aload().setLocal(2);
            code.aload().setLocal(1);
            code.invokevirtual().setMethod("propertyChanged", void.class, new Class[]{String.class,Object.class,Object.class});

            ifne.setTarget(vreturn);

            code.calculateMaxStack();
            code.calculateMaxLocals();
        }
    }

    private static Collection<BCClass> getBCClasses(Document document) {
        Project project = new Project();

        Collection<BCClass> classes = new LinkedList<BCClass>();
        List<Element> metaModels = document.getRootElement().elements("metadata-model");
        for (Element metaModel : metaModels) {
            List<Element> elements = metaModel.elements("class");
            for (Element classElement : elements) {
                try {
                      classes.add(project.loadClass(Class.forName(classElement.getText())));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classes;

    }

}