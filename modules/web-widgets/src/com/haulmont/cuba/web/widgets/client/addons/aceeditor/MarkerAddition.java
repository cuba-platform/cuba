/*
 * Copyright 2017 Antti Nieminen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.aceeditor;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDiff.TransportMarkerAddition;

public class MarkerAddition {
	private final AceMarker marker;
	private final String startContext;
	private final String endContext;
	public MarkerAddition(AceMarker marker, String text2) {
		this.marker = marker;
		
		// TODO
		startContext = "";
		endContext = "";
	}
	private MarkerAddition(AceMarker marker, String startContext, String endContext) {
		this.marker = marker;
		this.startContext = startContext;
		this.endContext = endContext;
	}
	public AceMarker getAdjustedMarker(String text) {
		// TODO adjust
		return marker;
	}
	public TransportMarkerAddition asTransport() {
		return new TransportMarkerAddition(marker.asTransport(), startContext, endContext);
	}
	public static MarkerAddition fromTransport(TransportMarkerAddition ta) {
		return new MarkerAddition(AceMarker.fromTransport(ta.marker), ta.startContext, ta.endContext);
	}
	

}
