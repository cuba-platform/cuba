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

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDiff.TransportMarkerDiff;

// TODO This is not the best way to diff markers.
// This is just stupid line-col diff without considering the
// text at all. That's why the markers don't always stay
// where you expect them to.
// Hence you can't currently use markers in anything you need to be
// sure their position is correct.

public class MarkerDiff {
	private final RangeDiff rangeDiff;
	public MarkerDiff(RangeDiff rangeDiff) {
		this.rangeDiff = rangeDiff;
	}
	public AceMarker applyTo(AceMarker m) {
		return m.withNewPosition(rangeDiff.applyTo(m.getRange()));
	}
	public static MarkerDiff diff(AceMarker m1, AceMarker m2) {
		return new MarkerDiff(RangeDiff.diff(m1.getRange(), m2.getRange()));
	}
	public boolean isIdentity() {
		return rangeDiff.isIdentity();
	}
	@Override
	public String toString() {
		return rangeDiff.toString();
	}
	public TransportMarkerDiff asTransport() {
		return new TransportMarkerDiff(rangeDiff.asTransport());
	}
	public static MarkerDiff fromTransport(TransportMarkerDiff tmd) {
		return new MarkerDiff(RangeDiff.fromTransport(tmd.rangeDiff));
	}
}
