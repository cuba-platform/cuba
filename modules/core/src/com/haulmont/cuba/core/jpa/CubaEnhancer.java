/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 15.12.2008 13:19:13
 * $Id$
 */
package com.haulmont.cuba.core.jpa;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Clazz;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.cuba.core.global.MetadataProvider;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.GeneralException;
import org.apache.openjpa.util.OpenJPAException;
import serp.bytecode.*;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CubaEnhancer implements PCEnhancer.AuxiliaryEnhancer {
    private static final Class INSTANCE_TYPE = Instance.class;
    protected org.apache.commons.logging.Log log;

    protected static final Localizer _loc = Localizer.forPackage(PCEnhancer.class);
    private static final String VALUE_LISTENERS_FIELD = "__valueListeners";
    private static final String METHODS_CACHE_FIELD = "__cache";

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
                if (anInterface.getName().equals(INSTANCE_TYPE.getName())) {
                    log.trace(String.format("Class %s already enchanced", _managedType.getType()));
                    return;
                }
            }

            enchanceSetters();

            if (meta != null) {
                enhanceClass();
                addInstanceFields();
                addInstanceMethods();
            }
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
            if (!name.startsWith("set")) continue;

            code = method.getCode(false);

            final String fieldName = StringUtils.uncapitalize(name.replace("set", ""));

            if (isSetterCode(code, fieldName)) {
                method.removeCode();
                code = method.getCode(true);

                code.aload().setThis();
                code.invokevirtual().setMethod("get" + StringUtils.capitalize(fieldName) , method.getParamTypes()[0], new Class[]{});
                code.astore().setLocal(2);
                code.aload().setThis();
                code.aload().setLocal(1);
                code.invokestatic().setMethod("pcSet" + fieldName, method.getReturnType(), new Class[]{_pc.getType(), method.getParamTypes()[0]});
                code.aload().setLocal(2);
                code.aload().setLocal(1);
                code.invokestatic().setMethod(ObjectUtils.class, "equals", boolean.class, new Class[]{Object.class,Object.class});
                code.ifne().setOffset(11);
                code.aload().setThis();
                code.constant().setValue(fieldName);
                code.aload().setLocal(2);
                code.aload().setLocal(1);
                code.invokevirtual().setMethod("propertyChanged", void.class, new Class[]{String.class,Object.class,Object.class});
                code.vreturn();

                code.calculateMaxStack();
                code.calculateMaxLocals();
            }
        }
    }

    private boolean isSetterCode(Code code, String fieldName) {
        final Instruction[] instructions = code.getInstructions();
        if (instructions.length == 4) {
            final Instruction instruction = instructions[2];
            if (instruction instanceof MethodInstruction) {
                final MethodInstruction methodInstruction = (MethodInstruction) instruction;
                return methodInstruction.getMethodName().equals("pcSet"+ fieldName);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void addInstanceFields() {
        final BCField valueListenersField = _pc.declareField(VALUE_LISTENERS_FIELD, Collection.class);
        valueListenersField.setTransient(true);
        valueListenersField.makeProtected();

        final BCField cacheField = _pc.declareField(METHODS_CACHE_FIELD, MethodsCache.class);
        cacheField.setTransient(true);
        cacheField.setStatic(true);
        cacheField.makeProtected();

        final Code code = getClassInitCode();
        code.anew().setType(MethodsCache.class);
        code.dup();
        code.constant().setValue(_pc);
        code.invokespecial().setMethod(MethodsCache.class, "<init>", void.class, new Class[]{Class.class});
        code.putstatic().setField(METHODS_CACHE_FIELD, MethodsCache.class);
        code.vreturn();

        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void addInstanceMethods() {
        createGetMetaclassMethod();
        createPropertyChangedMethod();

        createAddListenerMethod();
        createRemoveListenerMethod();

        createGetValueMethod();
        createSetValueMethod();
        createGetValueExMethod();
        createSetValueExMethod();
    }

    protected void createPropertyChangedMethod() {
        // protected void propertyChanged(String property, Object prevValue, Object value)
        BCMethod method = _pc.declareMethod("propertyChanged", void.class, new Class[]{String.class,Object.class,Object.class});
        method.makeProtected();
        Code code = method.getCode(true);

        code.aload().setThis();
        code.getfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.ifnull().setOffset(49);

        code.aload().setThis();
        code.getfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.invokeinterface().setMethod(Collection.class, "iterator", Iterator.class, new Class[]{});
        code.astore().setLocal(4);
        code.aload().setLocal(4);
        code.invokeinterface().setMethod(Iterator.class, "hasNext", boolean.class, new Class[]{});
        code.ifeq().setOffset(28);
        code.aload().setLocal(4);
        code.invokeinterface().setMethod(Iterator.class, "next", Object.class, new Class[]{});
        code.checkcast().setType(ValueListener.class);
        code.astore().setLocal(5);
        code.aload().setLocal(5);
        code.aload().setParam(0);
        code.aload().setParam(1);
        code.aload().setParam(2);
        code.invokeinterface().setMethod(ValueListener.class, "propertyChanged", void.class, new Class[]{String.class,Object.class,Object.class});
        code.go2().setOffset(-32);

        code.vreturn();

        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    private void createGetMetaclassMethod() {
        // public Clazz getMetaClass()
        BCMethod method = _pc.declareMethod("getMetaClass", Clazz.class, new Class[]{});
        method.makePublic();

        Code code = method.getCode(true);

        code.invokestatic().setMethod(MetadataProvider.class, "getSession", Session.class, new Class[]{});
        code.aload().setThis();

        code.invokevirtual().setMethod("getClass", Class.class, new Class[]{});
        code.invokeinterface().setMethod(Session.class, "getClass", Clazz.class, new Class[]{Class.class});

        code.areturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void createAddListenerMethod() {
        // public void addListener()
        BCMethod method = _pc.declareMethod("addListener", void.class, new Class[]{ValueListener.class});
        method.makePublic();

        Code code = method.getCode(true);
        code.aload().setThis();
        code.getfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.ifnonnull().setOffset(14);
        code.aload().setThis();
        code.anew().setType(ArrayList.class);
        code.dup();
        code.invokespecial().setMethod(ArrayList.class, "<init>", void.class, null);
        code.putfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.aload().setThis();
        code.getfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.aload().setLocal(1);
        code.invokeinterface().setMethod(Collection.class, "add", boolean.class, new Class[]{Object.class});
        code.pop();

        code.vreturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void createRemoveListenerMethod() {
        // public void removeListener()
        BCMethod method = _pc.declareMethod("removeListener", void.class, new Class[]{ValueListener.class});
        method.makePublic();

        Code code = method.getCode(true);
        code.aload().setThis();
        code.getfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.ifnull().setOffset(14);
        code.aload().setThis();
        code.getfield().setField(VALUE_LISTENERS_FIELD, Collection.class);
        code.aload().setLocal(1);
        code.invokeinterface().setMethod(Collection.class, "remove", boolean.class, new Class[]{Object.class});
        code.pop();

        code.vreturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void createGetValueMethod() {
        //public <T> T getValue(String name);
        BCMethod method = _pc.declareMethod("getValue", Object.class, new Class[]{String.class});
        method.makePublic();

        Code code = method.getCode(true);

        code.getstatic().setField(METHODS_CACHE_FIELD, MethodsCache.class);
        code.aload().setThis();
        code.aload().setLocal(1);
        code.invokevirtual().setMethod(MethodsCache.class, "invokeGetter", Object.class, new Class[]{Object.class, String.class});

        code.areturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void createSetValueMethod() {
        //public void setValue(String name, Object value);
        BCMethod method = _pc.declareMethod("setValue", void.class, new Class[]{String.class, Object.class});
        method.makePublic();

        Code code = method.getCode(true);

        code.getstatic().setField(METHODS_CACHE_FIELD, MethodsCache.class);
        code.aload().setThis();
        code.aload().setLocal(1);
        code.aload().setLocal(2);
        code.invokevirtual().setMethod(MethodsCache.class, "invokeSetter", void.class, new Class[]{Object.class, String.class, Object.class});
        
        code.vreturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void createGetValueExMethod() {
        //public <T> T getValueEx(String propertyPath);
        BCMethod method = _pc.declareMethod("getValueEx", Object.class, new Class[]{String.class});
        method.makePublic();

        Code code = method.getCode(true);

        code.aload().setThis();
        code.aload().setLocal(1);
        code.invokestatic().setMethod(InstanceUtils.class, "getValueEx", Object.class, new Class[]{Instance.class, String.class});

        code.areturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void createSetValueExMethod() {
        //public void setValueEx(String propertyPath, Object value);
        BCMethod method = _pc.declareMethod("setValueEx", void.class, new Class[]{String.class, Object.class});
        method.makePublic();

        Code code = method.getCode(true);

        code.aload().setThis();
        code.aload().setLocal(1);
        code.aload().setLocal(2);
        code.invokestatic().setMethod(InstanceUtils.class, "setValueEx", Object.class, new Class[]{Instance.class, String.class, Object.class});

        code.vreturn();
        code.calculateMaxStack();
        code.calculateMaxLocals();
    }

    protected void enhanceClass() {
        _pc.declareInterface(INSTANCE_TYPE);
    }

    private Code getClassInitCode() {
        BCMethod clinit = _pc.getDeclaredMethod("<clinit>");
        Code code;
        if (clinit != null) {
            code = clinit.getCode(true);
            Code template = (Code) AccessController.doPrivileged(J2DoPrivHelper.newCodeAction());
            code.searchForward(template.vreturn());
            code.previous();
            code.set(template.nop());
            code.next();

            return code;
        }

        // add static initializer method if non exists
        clinit = _pc.declareMethod("<clinit>", void.class, null);
        clinit.makePackage();
        clinit.setStatic(true);
        clinit.setFinal(true);

        code = clinit.getCode(true);
        return code;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean skipEnhance(BCMethod m) {
        return false;
    }
}
