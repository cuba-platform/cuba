package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.IFrame;

import java.util.List;
import java.util.ArrayList;

public class ComponentLoaderContext implements ComponentLoader.Context {
    protected DsContext dsContext;
    protected IFrame frame;

    protected List<ComponentLoader.LazyTask> lazyTasks = new ArrayList<ComponentLoader.LazyTask>();

    public ComponentLoaderContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public DsContext getDSContext() {
        return dsContext;
    }

    public IFrame getFrame() {
        return frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
    }

    public void addLazyTask(ComponentLoader.LazyTask task) {
        lazyTasks.add(task);
    }

    public void executeLazyTasks() {
        for (ComponentLoader.LazyTask task : lazyTasks) {
            task.execute(this, frame);
        }
    }
}
