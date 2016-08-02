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

package com.haulmont.cuba.gui.config;

import java.util.Locale;

/**
 * Class that represents information about the web browser the user is using.
 * Provides information such as browser name and version, screen resolution and IP address.
 */
public final class DeviceInfo {
    private int screenHeight = -1;
    private int screenWidth = -1;

    private String browserApplication = null;
    private Locale locale;
    private String address;
    private boolean secureConnection;

    private boolean touchDevice;

    private boolean isChromeFrameCapable = false;
    private boolean isChromeFrame = false;

    private boolean isSafari = false;
    private boolean isChrome = false;
    private boolean isFirefox = false;
    private boolean isOpera = false;
    private boolean isIE = false;
    private boolean isEdge = false;

    private boolean isWindowsPhone;
    private boolean isIPad;
    private boolean isIPhone;

    private OperatingSystem operatingSystem = OperatingSystem.UNKNOWN;

    private int browserMajorVersion = -1;
    private int browserMinorVersion = -1;

    /**
     * Gets the height of the screen in pixels. This is the full screen
     * resolution and not the height available for the application.
     *
     * @return the height of the screen in pixels.
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * Gets the width of the screen in pixels. This is the full screen
     * resolution and not the width available for the application.
     *
     * @return the width of the screen in pixels.
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * Get the browser user-agent string.
     *
     * @return The raw browser userAgent string
     */
    public String getBrowserApplication() {
        return browserApplication;
    }

    public void setBrowserApplication(String browserApplication) {
        this.browserApplication = browserApplication;
    }

    /**
     * Get the default locate of the browser.
     */
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Is the connection made using HTTPS?
     */
    public boolean isSecureConnection() {
        return secureConnection;
    }

    public void setSecureConnection(boolean secureConnection) {
        this.secureConnection = secureConnection;
    }

    /**
     * Tests whether the user is using Firefox.
     *
     * @return true if the user is using Firefox, false if the user is not using
     * Firefox or if no information on the browser is present
     */
    public boolean isFirefox() {
        return isFirefox;
    }

    public void setFirefox(boolean firefox) {
        isFirefox = firefox;
    }

    /**
     * Tests whether the user is using Internet Explorer.
     *
     * @return true if the user is using Internet Explorer, false if the user is
     * not using Internet Explorer or if no information on the browser
     * is present
     */
    public boolean isIE() {
        return isIE;
    }

    public void setIE(boolean IE) {
        isIE = IE;
    }

    /**
     * Tests whether the user is using Edge.
     *
     * @return true if the user is using Edge, false if the user is not using
     * Edge or if no information on the browser is present
     */
    public boolean isEdge() {
        return isEdge;
    }

    public void setEdge(boolean edge) {
        isEdge = edge;
    }

    /**
     * Tests whether the user is using Safari.
     *
     * @return true if the user is using Safari, false if the user is not using
     * Safari or if no information on the browser is present
     */
    public boolean isSafari() {
        return isSafari;
    }

    public void setSafari(boolean safari) {
        isSafari = safari;
    }

    /**
     * Tests whether the user is using Opera.
     *
     * @return true if the user is using Opera, false if the user is not using
     * Opera or if no information on the browser is present
     */
    public boolean isOpera() {
        return isOpera;
    }

    public void setOpera(boolean opera) {
        isOpera = opera;
    }

    /**
     * Tests whether the user is using Chrome.
     *
     * @return true if the user is using Chrome, false if the user is not using
     * Chrome or if no information on the browser is present
     */
    public boolean isChrome() {
        return isChrome;
    }

    public void setChrome(boolean chrome) {
        isChrome = chrome;
    }

    /**
     * Tests whether the user is using Chrome Frame.
     *
     * @return true if the user is using Chrome Frame, false if the user is not
     * using Chrome or if no information on the browser is present
     */
    public boolean isChromeFrame() {
        return isChromeFrame;
    }

    public void setChromeFrame(boolean chromeFrame) {
        isChromeFrame = chromeFrame;
    }

    /**
     * Tests whether the user's browser is Chrome Frame capable.
     *
     * @return true if the user can use Chrome Frame, false if the user can not
     * or if no information on the browser is present
     */
    public boolean isChromeFrameCapable() {
        return isChromeFrameCapable;
    }

    public void setChromeFrameCapable(boolean chromeFrameCapable) {
        isChromeFrameCapable = chromeFrameCapable;
    }

    /**
     * Gets the major version of the browser the user is using.
     * <p>
     * <p>
     * Note that Internet Explorer in IE7 compatibility mode might return 8 in
     * some cases even though it should return 7.
     * </p>
     *
     * @return The major version of the browser or -1 if not known.
     */
    public int getBrowserMajorVersion() {
        return browserMajorVersion;
    }

    public void setBrowserMajorVersion(int browserMajorVersion) {
        this.browserMajorVersion = browserMajorVersion;
    }

    /**
     * Gets the minor version of the browser the user is using.
     *
     * @return The minor version of the browser or -1 if not known.
     * @see #getBrowserMajorVersion()
     */
    public int getBrowserMinorVersion() {
        return browserMinorVersion;
    }

    public void setBrowserMinorVersion(int browserMinorVersion) {
        this.browserMinorVersion = browserMinorVersion;
    }

    /**
     * Tests whether the user is using Linux.
     *
     * @return true if the user is using Linux, false if the user is not using
     * Linux or if no information on the browser is present
     */
    public boolean isLinux() {
        return operatingSystem == OperatingSystem.LINUX;
    }

    /**
     * Tests whether the user is using Mac OS X.
     *
     * @return true if the user is using Mac OS X, false if the user is not
     * using Mac OS X or if no information on the browser is present
     */
    public boolean isMacOSX() {
        return operatingSystem == OperatingSystem.MACOSX;
    }

    /**
     * Tests whether the user is using Windows.
     *
     * @return true if the user is using Windows, false if the user is not using
     * Windows or if no information on the browser is present
     */
    public boolean isWindows() {
        return operatingSystem == OperatingSystem.WINDOWS;
    }

    /**
     * Tests whether the user is using Windows Phone.
     *
     * @return true if the user is using Windows Phone, false if the user is not
     * using Windows Phone or if no information on the browser is
     * present
     */
    public boolean isWindowsPhone() {
        return isWindowsPhone;
    }

    public void setWindowsPhone(boolean windowsPhone) {
        isWindowsPhone = windowsPhone;
    }

    /**
     * Tests if the browser is run on Android.
     *
     * @return true if run on Android false if the user is not using Android or
     * if no information on the browser is present
     */
    public boolean isAndroid() {
        return operatingSystem == OperatingSystem.ANDROID;
    }

    /**
     * Tests if the browser is run in iOS.
     *
     * @return true if run in iOS false if the user is not using iOS or if no
     * information on the browser is present
     */
    public boolean isIOS() {
        return operatingSystem == OperatingSystem.IOS;
    }

    /**
     * Tests if the browser is run on IPhone.
     *
     * @return true if run on IPhone false if the user is not using IPhone or if
     * no information on the browser is present
     */
    public boolean isIPhone() {
        return isIPhone;
    }

    public void setIPhone(boolean IPhone) {
        isIPhone = IPhone;
    }

    /**
     * Tests if the browser is run on IPad.
     *
     * @return true if run on IPad false if the user is not using IPad or if no
     * information on the browser is present
     */
    public boolean isIPad() {
        return isIPad;
    }

    public void setIPad(boolean IPad) {
        isIPad = IPad;
    }

    /**
     * @return true if the browser is detected to support touch events
     */
    public boolean isTouchDevice() {
        return touchDevice;
    }

    public void setTouchDevice(boolean touchDevice) {
        this.touchDevice = touchDevice;
    }

    /**
     * Gets the IP-address of the web browser.
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public double getAspectRatio() {
        if (screenHeight <= 0 || screenWidth <= 0) {
            return -1;
        }

        double max = Math.max(screenHeight, screenWidth);
        double min = Math.min(screenHeight, screenWidth);

        return max / min;
    }

    public enum OperatingSystem {
        UNKNOWN, WINDOWS, MACOSX, LINUX, IOS, ANDROID
    }
}