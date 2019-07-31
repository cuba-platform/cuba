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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.gui.components.HasDataLoadingSettings;
import com.haulmont.cuba.gui.components.HasPresentations;
import com.haulmont.cuba.gui.components.HasSettings;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.settings.Settings;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.walkComponents;

@Component(ScreenSettings.NAME)
public class ScreenSettings {
    public static final String NAME = "cuba_ScreenSettings";

    private static final Logger log = LoggerFactory.getLogger(ScreenSettings.class);

    /**
     * Apply settings for screen.
     *
     * @param screen   screen
     * @param settings settings
     */
    public void applySettings(Screen screen, Settings settings) {
        checkNotNullArgument(screen);
        checkNotNullArgument(settings);

        walkComponents(
                screen.getWindow(),
                (component, name) -> {
                    if (component.getId() != null
                            && component instanceof HasSettings) {
                        log.trace("Applying settings for {} : {} ", name, component);

                        Element e = settings.get(name);
                        ((HasSettings) component).applySettings(e);

                        if (component instanceof HasPresentations
                                && e.attributeValue("presentation") != null) {
                            String def = e.attributeValue("presentation");
                            if (!StringUtils.isEmpty(def)) {
                                UUID defaultId = UUID.fromString(def);
                                ((HasPresentations) component).applyPresentationAsDefault(defaultId);
                            }
                        }
                    }
                }
        );
    }

    /**
     * Apply settings for data components.
     *
     * @param screen   screen
     * @param settings settings
     */
    public void applyDataLoadingSettings(Screen screen, Settings settings) {
        checkNotNullArgument(screen);
        checkNotNullArgument(settings);

        walkComponents(
                screen.getWindow(),
                (component, name) -> {
                    if (component.getId() != null
                            && component instanceof HasDataLoadingSettings) {
                        log.trace("Applying settings for {} : {} ", name, component);

                        Element e = settings.get(name);
                        ((HasDataLoadingSettings) component).applyDataLoadingSettings(e);
                    }
                }
        );
    }

    /**
     * Save settings of screen.
     *
     * @param screen   screen
     * @param settings settings
     */
    public void saveSettings(Screen screen, Settings settings) {
        checkNotNullArgument(screen);
        checkNotNullArgument(settings);

        walkComponents(
                screen.getWindow(),
                (component, name) -> {
                    if (component.getId() != null
                            && component instanceof HasSettings) {
                        log.trace("Saving settings for {} : {}", name, component);

                        Element e = settings.get(name);
                        boolean modified = ((HasSettings) component).saveSettings(e);

                        if (component instanceof HasPresentations
                                && ((HasPresentations) component).isUsePresentations()) {
                            Object def = ((HasPresentations) component).getDefaultPresentationId();
                            e.addAttribute("presentation", def != null ? def.toString() : "");
                            Presentations presentations = ((HasPresentations) component).getPresentations();
                            if (presentations != null) {
                                presentations.commit();
                            }
                        }
                        if (modified) {
                            settings.setModified(true);
                        }
                    }
                }
        );
        settings.commit();
    }
}