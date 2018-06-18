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

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.MarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.RowAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportMarker;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportMarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRowAnnotation;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class AceDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String text;
	
	// key: markerId
	private final Map<String, AceMarker> markers;

	private final Set<RowAnnotation> rowAnnotations;

	private final Set<MarkerAnnotation> markerAnnotations;

	public AceDoc() {
		this("");
	}

	public AceDoc(String text) {
        this(text,
                Collections.<String, AceMarker> emptyMap(),
                Collections.<RowAnnotation>emptySet(),
                Collections.<MarkerAnnotation>emptySet());
	}
	
	public AceDoc(String text, Map<String, AceMarker> markers) {
        this(text, markers,
                Collections.<RowAnnotation>emptySet(),
                Collections.<MarkerAnnotation>emptySet());
	}

	public AceDoc(String text, Map<String, AceMarker> markers,
			Set<RowAnnotation> rowAnnotations,
			Set<MarkerAnnotation> markerAnnotations) {
        if (text == null) {
            text = "";
        }

		this.text = text;
		this.markers = markers;
		this.rowAnnotations = rowAnnotations;
		this.markerAnnotations = markerAnnotations;
	}
	
	public String getText() {
		return text;
	}
	
	public Map<String, AceMarker> getMarkers() {
		return Collections.unmodifiableMap(markers);
	}
	
	public Set<RowAnnotation> getRowAnnotations() {
		if (rowAnnotations==null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(rowAnnotations);
	}
	
	public Set<MarkerAnnotation> getMarkerAnnotations() {
		if (markerAnnotations==null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(markerAnnotations);
	}
	
	public boolean hasRowAnnotations() {
		return rowAnnotations != null;
	}
	
	public boolean hasMarkerAnnotations() {
		return markerAnnotations != null;
	}

	@Override
	public String toString() {
		return text + "\n/MARKERS: "+markers+"\nra:"+rowAnnotations+", ma:"+markerAnnotations;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AceDoc) {
			AceDoc od = (AceDoc) other;
			return textEquals(text, od.text) &&
					Util.sameMaps(this.markers, od.markers) &&
					Util.sameSets(this.markerAnnotations, od.markerAnnotations) &&
					Util.sameSets(this.rowAnnotations, od.rowAnnotations);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getText().hashCode();
	}

    public boolean textEquals(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

	public AceDoc withText(String newText) {
		return new AceDoc(newText, markers, rowAnnotations, markerAnnotations);
	}

	public TransportDoc asTransport() {
		TransportDoc td = new TransportDoc();
		td.text = text;

		td.markers = getTransportMarkers();
		td.markerAnnotations = getTransportMarkerAnnotations();
		td.rowAnnotations = getTransportRowAnnotations();

		return td;
	}

	/* TODO private */ Map<String, TransportMarker> getTransportMarkers() {
		HashMap<String, TransportMarker> ms = new HashMap<String, TransportMarker>(
				markers.size());
		for (Entry<String, AceMarker> e : markers.entrySet()) {
			ms.put(e.getKey(), e.getValue().asTransport());
		}
		return ms;
	}
	

	private Set<TransportRowAnnotation> getTransportRowAnnotations() {
		if (rowAnnotations==null) {
			return null;
		}
		HashSet<TransportRowAnnotation> anns = new HashSet<TransportRowAnnotation>(
				rowAnnotations.size());
		for (RowAnnotation ra : rowAnnotations) {
			anns.add(ra.asTransport());
		}
		return anns;
	}

	private Set<TransportMarkerAnnotation> getTransportMarkerAnnotations() {
		if (markerAnnotations==null) {
			return null;
		}
		HashSet<TransportMarkerAnnotation> anns = new HashSet<TransportMarkerAnnotation>(
				markerAnnotations.size());
		for (MarkerAnnotation ma : markerAnnotations) {
			anns.add(ma.asTransport());
		}
		return anns;
	}

	public static AceDoc fromTransport(TransportDoc doc) {
		String text = doc.text;
		Map<String, AceMarker> markers = markersFromTransport(doc.markers, doc.text);
		Set<RowAnnotation> rowAnnotations = rowAnnotationsFromTransport(doc.rowAnnotations);
		Set<MarkerAnnotation> markerAnnotations = markerAnnotationsFromTransport(doc.markerAnnotations);
		return new AceDoc(text, markers, rowAnnotations, markerAnnotations);
	}

	private static Map<String, AceMarker> markersFromTransport(
			Map<String, TransportMarker> markers, String text) {
		HashMap<String, AceMarker> ms = new HashMap<String, AceMarker>();
		for (Entry<String, TransportMarker> e : markers.entrySet()) {
			ms.put(e.getKey(), AceMarker.fromTransport(e.getValue()));
		}
		return ms;
	}

	private static Set<MarkerAnnotation> markerAnnotationsFromTransport(
			Set<TransportMarkerAnnotation> markerAnnotations) {
		if (markerAnnotations==null) {
			return null;
		}
		HashSet<MarkerAnnotation> anns = new HashSet<MarkerAnnotation>(markerAnnotations.size());
		for (TransportMarkerAnnotation ta : markerAnnotations) {
			anns.add(ta.fromTransport());
		}
		return anns;
	}

	private static Set<RowAnnotation> rowAnnotationsFromTransport(
			Set<TransportRowAnnotation> rowAnnotations) {
		if (rowAnnotations==null) {
			return null;
		}
		HashSet<RowAnnotation> anns = new HashSet<RowAnnotation>(rowAnnotations.size());
		for (TransportRowAnnotation ta : rowAnnotations) {
			anns.add(ta.fromTransport());
		}
		return anns;
	}
	
	// TODO?
	public AceDoc withMarkers(Set<AceMarker> newMarkers) {
		HashMap<String, AceMarker> markers2 = new HashMap<String, AceMarker>(newMarkers.size());
		for (AceMarker m : newMarkers) {
			markers2.put(m.getMarkerId(), m);
		}
		return new AceDoc(text, markers2, rowAnnotations, markerAnnotations);
	}
	
	public AceDoc withMarkers(Map<String, AceMarker> newMarkers) {
		return new AceDoc(text, newMarkers, rowAnnotations, markerAnnotations);
	}
	public AceDoc withAdditionalMarker(AceMarker marker) {
		HashMap<String, AceMarker> markers2 = new HashMap<String, AceMarker>(markers);
		markers2.put(marker.getMarkerId(), marker);
		return new AceDoc(text, markers2, rowAnnotations, markerAnnotations);
	}
	public AceDoc withAdditionalMarkers(Map<String, AceMarker> addMarkers) {
		HashMap<String, AceMarker> newMarkers = new HashMap<String, AceMarker>(markers);
		newMarkers.putAll(addMarkers);
		return new AceDoc(text, newMarkers, rowAnnotations, markerAnnotations);
	}

	public AceDoc withoutMarker(String markerId) {
		HashMap<String, AceMarker> markers2 = new HashMap<String, AceMarker>(markers);
		markers2.remove(markerId);
		return new AceDoc(text, markers2, rowAnnotations, markerAnnotations);
	}

	public AceDoc withoutMarkers() {
		Map<String, AceMarker> noMarkers = Collections.emptyMap();
		return new AceDoc(text, noMarkers, rowAnnotations, markerAnnotations);
	}
	
	public AceDoc withoutMarkers(Set<String> without) {
		Map<String, AceMarker> newMarkers = new HashMap<String, AceMarker>(markers);
		for (String m : without) {
			newMarkers.remove(m);
		}
		return new AceDoc(text, newMarkers, rowAnnotations, markerAnnotations);
	}

	public AceDoc withRowAnnotations(Set<RowAnnotation> ranns) {
		return new AceDoc(text, markers, ranns, markerAnnotations);
	}
	
	public AceDoc withMarkerAnnotations(Set<MarkerAnnotation> manns) {
		return new AceDoc(text, markers, rowAnnotations, manns);
	}

	public AceDoc withAdditionalMarkerAnnotation(MarkerAnnotation mann) {
		HashSet<MarkerAnnotation> manns = markerAnnotations==null?new HashSet<MarkerAnnotation>():new HashSet<MarkerAnnotation>(markerAnnotations);
		manns.add(mann);
		return new AceDoc(text, markers, rowAnnotations, manns);
	}
	
	public AceDoc withAdditionalRowAnnotation(RowAnnotation rann) {
		HashSet<RowAnnotation> ranns = rowAnnotations==null?new HashSet<RowAnnotation>():new HashSet<RowAnnotation>(rowAnnotations);
		ranns.add(rann);
		return new AceDoc(text, markers, ranns, markerAnnotations);
	}
}