/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 17:25:26
 * $Id$
 */
package com.haulmont.cuba.gui.components;

/**
 * Progress bar is a component that visually displays the progress of some task.
 * <p/>
 * Component accepts float values from 0.0f to 1.0f. 0 means no progress, 1.0 - full progress.
 * <p/>
 * To indicate that a task of unknown length is executing, you can put a progress bar into indeterminate mode.
 */
public interface ProgressBar extends Component.HasValue {
    String NAME = "progressBar";

    boolean isIndeterminate();

    void setIndeterminate(boolean indeterminate);

}
