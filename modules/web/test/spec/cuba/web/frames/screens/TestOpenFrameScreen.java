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

package spec.cuba.web.frames.screens;

import com.haulmont.cuba.gui.app.security.user.browse.UserBrowser;
import com.haulmont.cuba.gui.components.AbstractWindow;

import javax.inject.Inject;
import java.util.Map;

public class TestOpenFrameScreen extends AbstractWindow {

    protected UserBrowser userBrowse;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        userBrowse = (UserBrowser) openFrame(getFrame(), "sec$User.browse");
    }

    public UserBrowser getUserBrowse() {
        return userBrowse;
    }
}