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
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;

import java.util.Date;

@MetaClass(name = "sys$ScreenProfilerEvent")
public class ScreenProfilerEvent extends AbstractNotPersistentEntity {

    @MetaProperty
    protected Date eventTs;
    @MetaProperty
    protected String screen;
    @MetaProperty
    protected String user;
    @MetaProperty
    protected Integer clientTime;
    @MetaProperty
    protected Integer serverTime;
    @MetaProperty
    protected Integer networkTime;
    @MetaProperty
    protected Integer totalTime;

    public Date getEventTs() {
        return eventTs;
    }

    public void setEventTs(Date eventTs) {
        this.eventTs = eventTs;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getClientTime() {
        return clientTime;
    }

    public void setClientTime(Integer clientTime) {
        this.clientTime = clientTime;
    }

    public Integer getServerTime() {
        return serverTime;
    }

    public void setServerTime(Integer serverTime) {
        this.serverTime = serverTime;
    }

    public Integer getNetworkTime() {
        return networkTime;
    }

    public void setNetworkTime(Integer networkTime) {
        this.networkTime = networkTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    @MetaProperty
    public Double getServerTimeSec() {
        return serverTime / 1000.0;
    }

    @MetaProperty
    public Double getClientTimeSec() {
        return clientTime / 1000.0;
    }

    @MetaProperty
    public Double getNetworkTimeSec() {
        return networkTime / 1000.0;
    }

    @MetaProperty
    public Double getTotalTimeSec() {
        return totalTime / 1000.0;
    }

}
