/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 15.12.2008 13:19:13
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.GeneralException;
import org.apache.openjpa.util.OpenJPAException;
import serp.bytecode.*;

public class CubaEnhancer implements PCEnhancer.AuxiliaryEnhancer {

    private static final Class ENHANCED_TYPE = CubaEnhanced.class;

    protected org.apache.commons.logging.Log log;

    protected static final Localizer _loc = Localizer.forPackage(PCEnhancer.class);

    private BCClass _pc;
    private BCClass _managedType;

    public CubaEnhancer() {
        log = LogFactory.getLog(OpenJPAConfiguration.LOG_ENHANCE);
    }

    public void run(BCClass bc, ClassMetaData meta) {
        _pc = bc;
        _managedType = bc;

        log.trace(String.format("CUBA specific enhancing for %s", _pc.getClassName()));

        try {
            if (_pc.isInterface())
                return;

            Class[] interfaces = _managedType.getDeclaredInterfaceTypes();
            for (Class anInterface : interfaces) {
                if (anInterface.getName().equals(ENHANCED_TYPE.getName())) {
                    log.trace(String.format("Class %s already enchanced", _managedType.getType()));
                    return;
                }
            }

            enchanceSetters();

            _pc.declareInterface(ENHANCED_TYPE);

        } catch (OpenJPAException ke) {
            throw ke;
        } catch (Exception e) {
            throw new GeneralException(_loc.get("enhance-error", _managedType.getType().getName(), e.getMessage()), e);
        }
    }

    private void enchanceSetters() throws NoSuchMethodException {
        BCMethod[] methods = _managedType.getDeclaredMethods();
        Code code;
        for (final BCMethod method : methods) {
            final String name = method.getName();
            if (!name.startsWith("set") || method.getReturnType() != void.class)
                continue;

            code = method.getCode(false);

            final String fieldName = StringUtils.uncapitalize(name.replace("set", ""));

            code.aload().setThis();
            code.invokevirtual().setMethod("get" + StringUtils.capitalize(fieldName), method.getParamTypes()[0], new Class[]{});
            code.astore().setLocal(2);

            code.afterLast();
            Instruction vreturn = code.previous();

            code.afterLast();
            code.previous();
            /*
             find instruction pcSet + fieldName and invoke propertyChanged
             */
            code.beforeFirst();
            while (code.hasNext()) {
                Instruction inst = code.next();
                if (MethodInstruction.class.isAssignableFrom(inst.getClass())) {
                    if (((MethodInstruction) inst).getMethodName().equals("pcSet" + fieldName)) {
                        code.after(inst);
                        code.aload().setLocal(2);
                        code.aload().setLocal(1);
                        code.invokestatic().setMethod(ObjectUtils.class, "equals", boolean.class, new Class[]{Object.class, Object.class});
                        IfInstruction ifne = code.ifne();
                        code.aload().setThis();
                        code.constant().setValue(fieldName);
                        code.aload().setLocal(2);
                        code.aload().setLocal(1);
                        code.invokevirtual().setMethod("propertyChanged", void.class, new Class[]{String.class, Object.class, Object.class});
                        ifne.setTarget(vreturn);
                    }
                }

            }

            code.calculateMaxStack();
            code.calculateMaxLocals();
        }
    }

    public boolean skipEnhance(BCMethod m) {
        return false;
    }
}
