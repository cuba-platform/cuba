/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.map;

import com.haulmont.cuba.gui.components.Component;

/**
 * <p>$Id$</p>
 *
 * @author Sergei Maslyakov
 */
public interface MapViewer extends Component {
    String NAME = "mapviewer";

    void showLocationOnMap(final Location location);

    public class Location {
        private String fullName;
        private String shortName;
        private Double latitude;
        private Double longitude;

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getFullName() {
            return fullName;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public String getShortName() {
            return shortName;
        }
    }
}