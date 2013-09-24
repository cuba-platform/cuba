/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.vaadin.shared.AbstractComponentState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMultiUploadState extends AbstractComponentState {

    public static final String BUTTON_IMAGE_KEY = "buttonImage";

    public static final String SWFUPLOAD_BOOTSTRAP_JS_KEY = "SWFUPLOAD_BOOTSTRAP_JS";
    public static final String SWFUPLOAD_FLASH_KEY = "SWFUPLOAD_FLASH";

    public String buttonCaption = "Upload";
    public int buttonWidth = 90;
    public int buttonHeight = 25;

    public String fileTypes = "*.*";
    public String fileTypesDescription = "All files";

    public int queueSizeLimit = 100;   // 100 files
    public double fileSizeLimit = 10.0; // 10 MB
    public double queueUploadLimit = 200.0; // 100
    public String jsessionId = "";
}