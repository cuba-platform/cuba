package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import groovy.lang.Binding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentLoaderContext implements ComponentLoader.Context {

    protected DsContext dsContext;
    protected IFrame frame;
    protected transient Binding binding;

    protected List<ComponentLoader.PostInitTask> postInitTasks = new ArrayList<ComponentLoader.PostInitTask>();
    protected Map<String, Object> parameters;

    protected ComponentLoader.Context parent;
    
    private static final long serialVersionUID = 5925275133830025528L;

    public ComponentLoaderContext(DsContext dsContext, Map<String, Object> parameters) {
        this.dsContext = dsContext;
        this.parameters = parameters;
    }

    public Map<String, Object> getParams() {
        return parameters;
    }

    public DsContext getDsContext() {
        return dsContext;
    }

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

    public IFrame getFrame() {
        return frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
    }

    public void addPostInitTask(ComponentLoader.PostInitTask task) {
        postInitTasks.add(task);
    }

    public ComponentLoader.Context getParent() {
        return parent;
    }

    public void setParent(ComponentLoader.Context parent) {
        this.parent = parent;
    }

    public void executePostInitTasks() {
        if (!getPostInitTasks().isEmpty()) {
            new TastExecutor(getPostInitTasks().get(0)).run();
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

    private class TastExecutor implements Runnable, Serializable {
        private final ComponentLoader.PostInitTask task;

        private static final long serialVersionUID = 4776677725415883750L;

        private TastExecutor(ComponentLoader.PostInitTask task) {
            this.task = task;
        }

        public void run() {
            removeTask(task, ComponentLoaderContext.this);
            task.execute(ComponentLoaderContext.this, frame);
            if (!getPostInitTasks().isEmpty()) {
                new TastExecutor(getPostInitTasks().get(0)).run();
            }
        }
    }
}
