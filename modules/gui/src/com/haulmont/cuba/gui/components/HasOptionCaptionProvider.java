/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components;

import java.util.function.Function;

/**
 * A component that is marked with this interface allows to manage caption for options displayed
 * by this component.
 *
 * @param <I> option item type
 */
public interface HasOptionCaptionProvider<I> extends Component {

    /**
     * Sets function that provides caption for option items.
     *
     * @param optionCaptionProvider caption provider for options
     */
    void setOptionCaptionProvider(Function<? super I, String> optionCaptionProvider);

    /**
     * @return caption provider for options
     */
    Function<? super I, String> getOptionCaptionProvider();
}
