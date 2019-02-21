/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.ScreenOptions;
import com.haulmont.cuba.gui.sys.UiControllerProperty;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader.InitTask;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader.InjectTask;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader.PostInitTask;

import java.util.*;

public class ComponentLoaderContext implements ComponentLoader.Context {

    protected ScreenOptions options;

    protected ScreenData screenData;
    protected DsContext dsContext;
    protected Frame frame;
    protected String fullFrameId;
    protected String currentFrameId;

    protected List<PostInitTask> postInitTasks = new ArrayList<>();
    protected List<InjectTask> injectTasks = new ArrayList<>();
    protected List<InitTask> initTasks = new ArrayList<>();

    protected Map<String, Object> parameters;
    protected List<UiControllerProperty> properties = Collections.emptyList();
    protected Map<String, String> aliasesMap = new HashMap<>();

    protected ComponentLoader.Context parent;

    public ComponentLoaderContext(ScreenOptions options) {
        this.options = options;

        this.parameters = Collections.emptyMap();
        if (options instanceof MapScreenOptions) {
            parameters = ((MapScreenOptions) options).getParams();
        }
    }

    @Override
    public ScreenOptions getOptions() {
        return options;
    }

    @Override
    public Map<String, Object> getParams() {
        return parameters;
    }

    public ScreenData getScreenData() {
        return screenData;
    }

    public void setScreenData(ScreenData screenData) {
        this.screenData = screenData;
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Frame frame) {
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
    public String getCurrentFrameId() {
        return currentFrameId;
    }

    @Override
    public void setCurrentFrameId(String currentFrameId) {
        this.currentFrameId = currentFrameId;
    }

    @Override
    public void addPostInitTask(PostInitTask task) {
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

    public List<UiControllerProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<UiControllerProperty> properties) {
        this.properties = properties;
    }

    @Override
    public void executePostInitTasks() {
        for (PostInitTask postInitTask : postInitTasks) {
            postInitTask.execute(this, frame);
        }
        postInitTasks.clear();
    }

    @Override
    public void addInjectTask(InjectTask task) {
        injectTasks.add(task);
    }

    @Override
    public void executeInjectTasks() {
        for (InjectTask injectTask : injectTasks) {
            injectTask.execute(ComponentLoaderContext.this, frame);
        }
        injectTasks.clear();
    }

    @Override
    public void addInitTask(InitTask task) {
        initTasks.add(task);
    }

    @Override
    public void executeInitTasks() {
        for (InitTask initTask : initTasks) {
            initTask.execute(this, frame);
        }
        initTasks.clear();
    }

    public List<InjectTask> getInjectTasks() {
        return injectTasks;
    }

    public List<PostInitTask> getPostInitTasks() {
        return postInitTasks;
    }

    public List<InitTask> getInitTasks() {
        return initTasks;
    }

    @Override
    public Map<String, String> getAliasesMap() {
        return aliasesMap;
    }
}