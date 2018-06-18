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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDiff.TransportMarkerAddition;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDiff.TransportMarkerDiff;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDiff.TransportMarkerSetDiff;

@SuppressWarnings("serial")
public class MarkerSetDiff implements Serializable {
	
	private final Map<String, MarkerAddition> added;
	private final Map<String, MarkerDiff> moved;
	private final Set<String> removed;
	
	public MarkerSetDiff(Map<String, MarkerAddition> added, Set<String> removed) {
		this.added = added;
		this.moved = Collections.emptyMap();
		this.removed = removed;
	}
	
	public MarkerSetDiff(Map<String, MarkerAddition> added,
			Map<String, MarkerDiff> moved, Set<String> removed) {
		this.added = added;
		this.moved = moved;
		this.removed = removed;
	}

	public static MarkerSetDiff diff(Map<String, AceMarker> m1, Map<String, AceMarker> m2, String text2) {

		Map<String, MarkerAddition> added = new HashMap<String, MarkerAddition>();
		Map<String, MarkerDiff> diffs = new HashMap<String, MarkerDiff>();
		for (Entry<String, AceMarker> e : m2.entrySet()) {
			AceMarker c1 = m1.get(e.getKey());
			if (c1 != null) {
				MarkerDiff d = MarkerDiff.diff(c1, e.getValue());
				if (!d.isIdentity()) {
					diffs.put(e.getKey(), d);
				}
			} else {
				added.put(e.getKey(), new MarkerAddition(e.getValue(), text2));
			}
		}

		Set<String> removedIds = new HashSet<String>(m1.keySet());
		removedIds.removeAll(m2.keySet());

		return new MarkerSetDiff(added, diffs, removedIds);
	}

//	public Map<String, TransportMarker> applyTo(Map<String, TransportMarker> markers) {
//		Map<String, TransportMarker> markers2 = new HashMap<String, TransportMarker>();
//		for (Entry<String, TransportMarkerAddition> e : added.entrySet()) {
//			TransportMarker adjusted = e.getValue().marker; // TODO: adjust
//			if (adjusted != null) {
//				markers2.put(e.getKey(), adjusted);
//			}
//		}
//
//		for (Entry<String, TransportMarker> e : markers.entrySet()) {
//			if (removed.contains(e.getKey())) {
//				continue;
//			}
//			TransportMarker m = e.getValue();
//			if (added.containsKey(e.getKey())) {
//				 m = added.get(e.getKey()).marker;
//			}
//			TransportMarkerDiff md = moved.get(e.getKey());
//			if (md != null) {
//				markers2.put(e.getKey(), md.applyTo(m));
//			} else {
//				markers2.put(e.getKey(), m);
//			}
//		}
//
//		return markers2;
//	}
	
	public Map<String, AceMarker> applyTo(Map<String, AceMarker> markers, String text2) {
		Map<String, AceMarker> markers2 = new HashMap<String, AceMarker>();
		for (Entry<String, MarkerAddition> e : added.entrySet()) {
			AceMarker adjusted = e.getValue().getAdjustedMarker(text2);
			if (adjusted != null) {
				markers2.put(e.getKey(), adjusted);
			}
		}

		for (Entry<String, AceMarker> e : markers.entrySet()) {
			if (removed.contains(e.getKey())) {
				continue;
			}
			AceMarker m = e.getValue();
			
			// ???
			if (markers2.containsKey(e.getKey())) {
				 m = markers2.get(e.getKey());
			}
			
			MarkerDiff md = moved.get(e.getKey());
			if (md != null) {
				markers2.put(e.getKey(), md.applyTo(m));
			} else {
				markers2.put(e.getKey(), m);
			}
		}

		return markers2;
	}

	@Override
	public String toString() {
		return "added: " + added + "\n" +
				"moved: " + moved + "\n" +
				"removed: " + removed;
	}

	public boolean isIdentity() {
		return added.isEmpty() && moved.isEmpty() && removed.isEmpty();
	}

	public TransportMarkerSetDiff asTransportDiff() {
		TransportMarkerSetDiff msd = new TransportMarkerSetDiff();
		msd.added = getTransportAdded();
		msd.moved = getTransportMoved();
		msd.removed = getTransportRemoved();
		return msd;
	}

	private Map<String, TransportMarkerAddition> getTransportAdded() {
		HashMap<String, TransportMarkerAddition> ta = new HashMap<String, TransportMarkerAddition>();
		for (Entry<String, MarkerAddition> e : added.entrySet()) {
			ta.put(e.getKey(), e.getValue().asTransport());
		}
		return ta;
	}

	private Map<String, TransportMarkerDiff> getTransportMoved() {
		HashMap<String, TransportMarkerDiff> ta = new HashMap<String, TransportMarkerDiff>();
		for (Entry<String, MarkerDiff> e : moved.entrySet()) {
			ta.put(e.getKey(), e.getValue().asTransport());
		}
		return ta;
	}

	private Set<String> getTransportRemoved() {
		return removed; // No need for a defensive copy??
	}

	public static MarkerSetDiff fromTransportDiff(TransportMarkerSetDiff td) {
		return new MarkerSetDiff(
				addedFromTransport(td.added),
				movedFromTransport(td.moved),
				removedFromTransport(td.removed));
	}

	private static Map<String, MarkerAddition> addedFromTransport(
			Map<String, TransportMarkerAddition> added2) {
		HashMap<String, MarkerAddition> added = new HashMap<String, MarkerAddition>();
		for (Entry<String, TransportMarkerAddition> e : added2.entrySet()) {
			added.put(e.getKey(), MarkerAddition.fromTransport(e.getValue()));
		}
		return added;
	}

	private static Map<String, MarkerDiff> movedFromTransport(
			Map<String, TransportMarkerDiff> mt) {
		HashMap<String, MarkerDiff> moved = new HashMap<String, MarkerDiff>();
		for (Entry<String, TransportMarkerDiff> e : mt.entrySet()) {
			moved.put(e.getKey(), MarkerDiff.fromTransport(e.getValue()));
		}
		return moved;
	}

	private static Set<String> removedFromTransport(Set<String> tr) {
		return tr; // No need for a defensive copy??
	}
	
}
