/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import groovy.lang.Binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class ComponentLoaderContext implements ComponentLoader.Context {

    protected DsContext dsContext;
    protected IFrame frame;
    protected String fullFrameId;
    protected String currentFrameId;
    protected transient Binding binding;

    protected List<ComponentLoader.PostInitTask> postInitTasks = new ArrayList<>();
    protected Map<String, Object> parameters;

    protected ComponentLoader.Context parent;

    public ComponentLoaderContext(DsContext dsContext, Map<String, Object> parameters) {
        this.dsContext = dsContext;
        this.parameters = parameters;
    }

    @Override
    public Map<String, Object> getParams() {
        return parameters;
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    @Override
    public Binding getBinding() {
        if (binding == null) {
            binding = new Binding();
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String name = entry.getKey().replace('$', '_');
                binding.setVariable(name, entry.getValue());
            }
        }
        return binding;
    }

    @Override
    public IFrame getFrame() {
        return frame;
    }

    @Override
    public void setFrame(IFrame frame) {
        this.frame = frame;
    }

    @Override
    public String getFullFrameId() {
        return fullFrameId;
    }

    @Override
    public void setFullFrameId(String frameId) {
        this.fullFrameId = frameId;
    }

    @Override
    public String getCurrentIFrameId() {
        return currentFrameId;
    }

    @Override
    public void setCurrentIFrameId(String currentFrameId) {
        this.currentFrameId = currentFrameId;
    }

    @Override
    public void addPostInitTask(ComponentLoader.PostInitTask task) {
        postInitTasks.add(task);
    }

    @Override
    public ComponentLoader.Context getParent() {
        return parent;
    }

    @Override
    public void setParent(ComponentLoader.Context parent) {
        this.parent = parent;
    }

    @Override
    public void executePostInitTasks() {
        if (!getPostInitTasks().isEmpty()) {
            new TaskExecutor(getPostInitTasks().get(0)).run();
        }
    }

    public List<ComponentLoader.PostInitTask> getPostInitTasks() {
        return postInitTasks;
    }

    protected void removeTask(ComponentLoader.PostInitTask task, ComponentLoaderContext context) {
        if (context.getPostInitTasks().remove(task) && context.getParent() != null) {
            removeTask(task, (ComponentLoaderContext) context.getParent());
        }
    }

    private class TaskExecutor implements Runnable {

        private final ComponentLoader.PostInitTask task;

        private TaskExecutor(ComponentLoader.PostInitTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            removeTask(task, ComponentLoaderContext.this);
            task.execute(ComponentLoaderContext.this, frame);
            if (!getPostInitTasks().isEmpty()) {
                new TaskExecutor(getPostInitTasks().get(0)).run();
            }
        }
    }
}
