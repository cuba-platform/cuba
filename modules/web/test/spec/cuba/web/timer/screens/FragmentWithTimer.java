/*
 * Copyright (c) 2008-2019 Haulmont.
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

package spec.cuba.web.timer.screens;

import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController("test_FragmentWithTimer")
@UiDescriptor("fragment-with-timer.xml")
public class FragmentWithTimer extends ScreenFragment {
    @Inject
    private Timer testTimer;

    private int ticksCounter = 0;
    private boolean stopped = false;

    @Subscribe("testTimer")
    private void onTimerTick(Timer.TimerActionEvent event) {
        this.ticksCounter += 1;
    }

    @Subscribe("testTimer")
    private void onTimerStop(Timer.TimerStopEvent event) {
        this.stopped = true;
    }

    public Timer getTimer() {
        return testTimer;
    }

    public int getTicksCounter() {
        return ticksCounter;
    }

    public boolean isStopped() {
        return stopped;
    }
}