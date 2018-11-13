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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.MarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.RowAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.GwtTextDiff.Diff;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.GwtTextDiff.Patch;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportMarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRowAnnotation;

import com.google.gwt.core.client.JsArray;

public class ClientSideDocDiff {

	public static final DiffMatchPatchJSNI dmp = DiffMatchPatchJSNI.newInstance();

	private final JsArray<GwtTextDiff.Patch> textPatches;
	private final MarkerSetDiff markerSetDiff;
	private final SetDiff<RowAnnotation,TransportRowAnnotation> rowAnnDiff;
	private final SetDiff<MarkerAnnotation,TransportMarkerAnnotation> markerAnnDiff;

	public static ClientSideDocDiff fromTransportDiff(TransportDiff ad) {
		
		JsArray<Patch> patches = dmp.patch_fromText(ad.patchesAsString);
		MarkerSetDiff msd = MarkerSetDiff.fromTransportDiff(ad.markerSetDiff);
		
		SetDiff<RowAnnotation,TransportRowAnnotation> rowAnns = ad.rowAnnDiff==null ? null : 
				SetDiff.fromTransport(ad.rowAnnDiff);
		SetDiff<MarkerAnnotation,TransportMarkerAnnotation> markerAnns =  ad.markerAnnDiff==null ? null : 
				SetDiff.fromTransport(ad.markerAnnDiff);
		
		return new ClientSideDocDiff(patches, msd, rowAnns, markerAnns);
	}
	
	public static ClientSideDocDiff diff(AceDoc doc1, AceDoc doc2) {
		JsArray<GwtTextDiff.Patch> patches = dmp.patch_make(doc1.getText(), doc2.getText());
		MarkerSetDiff msd = MarkerSetDiff.diff(doc1.getMarkers(), doc2.getMarkers(), doc2.getText());

		SetDiff<RowAnnotation,TransportRowAnnotation> rowAnnDiff = diffRA(doc1.getRowAnnotations(), doc2.getRowAnnotations());		
		SetDiff<MarkerAnnotation,TransportMarkerAnnotation> markerAnnDiff = diffMA(doc1.getMarkerAnnotations(), doc2.getMarkerAnnotations());
		
		return new ClientSideDocDiff(patches, msd, rowAnnDiff, markerAnnDiff);
	}
	
	private static SetDiff<MarkerAnnotation, TransportMarkerAnnotation> diffMA(
			Set<MarkerAnnotation> anns1,
			Set<MarkerAnnotation> anns2) {
		if (anns2 == null && anns1 != null) {
			return null;
		}
		if (anns1==null) {
			anns1 = Collections.emptySet();
		}
		if (anns2==null) {
			anns2 = Collections.emptySet();
		}
		return new SetDiff.Differ<MarkerAnnotation,TransportMarkerAnnotation>().diff(anns1, anns2);
	}

	private static SetDiff<RowAnnotation, TransportRowAnnotation> diffRA(
			Set<RowAnnotation> anns1,
			Set<RowAnnotation> anns2) {
		if (anns2 == null && anns1 != null) {
			return null;
		}
		if (anns1==null) {
			anns1 = Collections.emptySet();
		}
		if (anns2==null) {
			anns2 = Collections.emptySet();
		}
		return new SetDiff.Differ<RowAnnotation,TransportRowAnnotation>().diff(anns1, anns2);
	}
	
	private ClientSideDocDiff(JsArray<GwtTextDiff.Patch> patches, MarkerSetDiff markerSetDiff,
			SetDiff<RowAnnotation,TransportRowAnnotation> rowAnnDiff,
			SetDiff<MarkerAnnotation,TransportMarkerAnnotation> markerAnnDiff) {
		this.textPatches = patches;
		this.markerSetDiff = markerSetDiff;
		this.rowAnnDiff = rowAnnDiff;
		this.markerAnnDiff = markerAnnDiff;
	}
	
	public String getPatchesString() {
		return dmp.patch_toText(textPatches);
	}
	
	public AceDoc applyTo(AceDoc doc) {
		String text = dmp.patch_apply(textPatches, doc.getText());
		Map<String, AceMarker> markers = markerSetDiff.applyTo(doc.getMarkers(), text);
		
		Set<RowAnnotation> rowAnns = rowAnnDiff==null ? null : rowAnnDiff.applyTo(doc.getRowAnnotations());
		Set<MarkerAnnotation> markerAnns = markerAnnDiff==null ? null : markerAnnDiff.applyTo(doc.getMarkerAnnotations());
		
		return new AceDoc(text, markers, rowAnns, markerAnns);
	}

	public TransportDiff asTransport() {
		TransportDiff d = new TransportDiff();
		d.patchesAsString = getPatchesString();
		d.markerSetDiff = markerSetDiff.asTransportDiff();
		d.rowAnnDiff = rowAnnDiff==null ? null : rowAnnDiff.asTransportRowAnnotations();
		d.markerAnnDiff = markerAnnDiff==null ? null : markerAnnDiff.asTransportMarkerAnnotations();
		return d;
	}

	public boolean isIdentity() {
		return textPatches == null || textPatches.length()==0;
	}
	
	@Override
	public String toString() {
		return getPatchesString() + "\nMSD: " + markerSetDiff.toString() + "\nrad:" + rowAnnDiff + ", mad:" + markerAnnDiff;
	}
	
	public static class Adjuster {
		private String s1;
		private String s2;
		private String[] lines1;
		private String[] lines2;
		private JsArray<Diff> diffs;
		private boolean stringsEqual;
		private boolean calcDone;
		public Adjuster(String s1, String s2) {
			this.s1 = s1;
			this.s2 = s2;
			stringsEqual = s1.equals(s2);
		}
		public AceRange adjust(AceRange r) {
			if (stringsEqual) {
				return r;
			}
			if (!calcDone) {
				calc();
			}
			boolean zeroLength = r.isZeroLength();
			int start1 = Util.cursorPosFromLineCol(lines1, r.getStartRow(), r.getStartCol(), 0);
			int end1 = zeroLength ? start1 : Util.cursorPosFromLineCol(lines1, r.getEndRow(), r.getEndCol(), 0);
			int start2 = dmp.diff_xIndex(diffs, start1);
			int end2 = zeroLength ? start2 : dmp.diff_xIndex(diffs, end1);
			int[] startRowCol = Util.lineColFromCursorPos(lines2, start2, 0);
			int[] endRowCol = zeroLength ? startRowCol : Util.lineColFromCursorPos(lines2, end2, 0);
			return new AceRange(startRowCol[0], startRowCol[1], endRowCol[0], endRowCol[1]);
		}
		private void calc() {
			lines1 = s1.split("\n", -1);
			lines2 = s2.split("\n", -1);
			diffs = dmp.diff_main(s1, s2);
		}
	}
}
