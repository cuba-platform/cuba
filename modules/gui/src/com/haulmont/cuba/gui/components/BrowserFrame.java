/*
 * Copyright (c) 2008-2017 Haulmont.
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

import java.util.EnumSet;

/**
 * A component displaying an embedded web page. Implemented as a HTML <code>iframe</code> element.
 */
public interface BrowserFrame extends ResourceView {
    String NAME = "browserFrame";

    /**
     * @return value of the attribute sandbox
     */
    String getSandbox();

    /**
     * Sets value of the attribute sandbox. This attribute applies extra restrictions to the content in the frame.
     *
     * @param value sandbox
     */
    void setSandbox(String value);

    /**
     * Sets value of the attribute sandbox.
     *
     * @param sandbox sandbox
     */
    void setSandbox(Sandbox sandbox);

    /**
     * Sets value of the attribute sandbox.
     *
     * @param sandboxSet EnumSet of {@link Sandbox}
     */
    void setSandbox(EnumSet<Sandbox> sandboxSet);

    /**
     * Standard values of the attribute sandbox of iframe HTML element. The value of the attribute can either be
     * empty to apply all restrictions, or space-separated tokens to lift particular restrictions
     */
    enum Sandbox {
        /**
         * Allows the resource to submit forms.
         */
        ALLOW_FORMS("allow-forms"),
        /**
         * Lets the resource open modal windows.
         */
        ALLOW_MODALS("allow-modals"),
        /**
         * Lets the resource lock the screen orientation.
         */
        ALLOW_ORIENTATION_LOCK("allow-orientation-lock"),
        /**
         * Lets the resource use the Pointer Lock API.
         */
        ALLOW_POINTER_LOCK("allow-pointer-lock"),
        /**
         * Allows popups (such as window.open(), target="_blank", or showModalDialog()).
         */
        ALLOW_POPUPS("allow-popups"),
        /**
         * Lets the sandboxed document open new windows without those windows inheriting the sandboxing.
         */
        ALLOW_POPUPS_TO_ESCAPE_SANDBOX("allow-popups-to-escape-sandbox"),
        /**
         * Lets the resource start a presentation session.
         */
        ALLOW_PRESENTATION("allow-presentation"),
        /**
         * Allows the iframe content to be treated as being from the same origin.
         */
        ALLOW_SAME_ORIGIN("allow-same-origin"),
        /**
         * Lets the resource run scripts.
         */
        ALLOW_SCRIPTS("allow-scripts"),
        /**
         * Lets the resource request access to the parent's storage capabilities with the Storage Access API.
         */
        ALLOW_STORAGE_ACCESS_BY_USER_ACTIVATION("allow-storage-access-by-user-activation"),
        /**
         * Lets the resource navigate the top-level browsing context (the one named _top).
         */
        ALLOW_TOP_NAVIGATION("allow-top-navigation"),
        /**
         * Lets the resource navigate the top-level browsing context, but only if initiated by a user gesture.
         */
        ALLOW_TOP_NAVIGATION_BY_USER_ACTIVATION("allow-top-navigation-by-user-activation"),
        /**
         * Lets the resource navigate the top-level browsing context (the one named _top).
         */
        ALLOW_DOWNLOADS_WITHOUT_USER_ACTIVATION("allow-downloads-without-user-activation"),
        /**
         * Applies all restrictions.
         */
        DENY_ALL("");

        private String value;

        Sandbox(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Sets value of the attribute srcdoc. Inline HTML to embed, overriding the src attribute.
     * You can also specify a value for the attribute srcdoc using attribute srcdocFile in xml by passing the path
     * to the file with HTML code.
     *
     * @param value inline HTML code
     */
    void setSrcdoc(String value);

    /**
     * @return value of the attribute srcdoc
     */
    String getSrcdoc();

    /**
     * Sets value of the attribute allow. Specifies a feature policy for the iframe.
     *
     * @param value allow
     */
    void setAllow(String value);

    /**
     * Sets value of the attribute allow. Specifies a feature policy for the iframe.
     *
     * @param allow allow
     */
    void setAllow(Allow allow);

    /**
     * Sets value of the attribute allow.
     * The value of the attribute can be a space-separated list of allow features.
     *
     * @param allowSet EnumSet of {@link Allow}
     */
    void setAllow(EnumSet<Allow> allowSet);

    /**
     * @return value of the attribute allow
     */
    String getAllow();

    /**
     * Standard values of the attribute allow of iframe HTML element.
     */
    enum Allow {
        /**
         * Controls whether the current document is allowed to autoplay media requested through the interface.
         */
        AUTOPLAY("autoplay"),
        /**
         * Controls whether the current document is allowed to use video input devices.
         */
        CAMERA("camera"),
        /**
         * Controls whether the current document is allowed to set document.domain.
         */
        DOCUMENT_DOMAIN("document-domain"),
        /**
         * Controls whether the current document is allowed to use the Encrypted Media Extensions API (EME).
         */
        ENCRYPTED_MEDIA("encrypted-media"),
        /**
         * Controls whether the current document is allowed to use Element.requestFullScreen().
         */
        FULLSCREEN("fullscreen"),
        /**
         * Controls whether the current document is allowed to use the Geolocation Interface.
         */
        GEOLOCATION("geolocation"),
        /**
         * Controls whether the current document is allowed to use audio input devices.
         */
        MICROPHONE("microphone"),
        /**
         * Controls whether the current document is allowed to use the Web MIDI API.
         */
        MIDI("midi"),
        /**
         * Controls whether the current document is allowed to use the Payment Request API.
         */
        PAYMENT("payment"),
        /**
         * Controls whether the current document is allowed to use the WebVR API.
         */
        VR("vr");

        private String value;

        Allow(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * @return value of the attribute referrerpolicy
     */
    String getReferrerPolicy();

    /**
     * Sets value of the attribute referrerpolicy. This attribute indicates which referrer to send when fetching
     * the frame's resource
     *
     * @param value referrerpolicy
     */
    void setReferrerPolicy(String value);

    /**
     * Sets value of the attribute referrerpolicy.
     *
     * @param referrerPolicy referrerpolicy
     */
    void setReferrerPolicy(ReferrerPolicy referrerPolicy);

    /**
     * Standard values of the attribute referrerpolicy of iframe HTML element.
     */
    enum ReferrerPolicy {
        /**
         * The Referer header will not be sent.
         */
        NO_REFERRER("no-referrer"),
        /**
         * The Referer header will not be sent to origins without TLS (HTTPS).
         */
        NO_REFERRER_WHEN_DOWNGRADE("no-referrer-when-downgrade"),
        /**
         * The sent referrer will be limited to the origin of the referring page: its scheme, host, and port.
         */
        ORIGIN("origin"),
        /**
         * The referrer sent to other origins will be limited to the scheme, the host, and the port.
         * Navigations on the same origin will still include the path.
         */
        ORIGIN_WHEN_CROSS_ORIGIN("origin-when-cross-origin"),
        /**
         * A referrer will be sent for same origin, but cross-origin requests will contain no referrer information.
         */
        SAME_ORIGIN("same-origin"),
        /**
         * Only send the origin of the document as the referrer when the protocol security level stays
         * the same (HTTPS-&gt;HTTPS), but don't send it to a less secure destination (HTTPS-&gt;HTTP).
         */
        STRICT_ORIGIN("strict-origin"),
        /**
         * Send a full URL when performing a same-origin request, only send the origin when the protocol security
         * level stays the same (HTTPS-&gt;HTTPS), and send no header to a less secure destination (HTTPS-&gt;HTTP).
         */
        STRICT_ORIGIN_WHEN_CROSS_ORIGIN("strict-origin-when-cross-origin"),
        /**
         * The referrer will include the origin and the path.
         * This value is unsafe, because it leaks origins and paths from TLS-protected resources to insecure origins.
         */
        UNSAFE_URL("unsafe-url");

        private String value;

        ReferrerPolicy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
