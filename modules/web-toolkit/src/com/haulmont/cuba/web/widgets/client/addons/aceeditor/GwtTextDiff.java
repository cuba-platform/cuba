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

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class GwtTextDiff{
	
	public static final int DIFF_DELETE = -1;
	public static final int DIFF_INSERT = 1;
	public static final int DIFF_EQUAL = 0;
	
	public static final class Patch extends JavaScriptObject {
		
		protected Patch() {
			
		}
		
		native int getStart1() /*-{
			return this.start1;
		}-*/;
		
		native public JsArray<Diff> getDiffsJsArray() /*-{
			return this.diffs;
		}-*/;
		
		public List<Diff> getDiffs() {
			JsArray<Diff> diffs = getDiffsJsArray();
			LinkedList<Diff> dili = new LinkedList<Diff>();
			for (int i=0; i<diffs.length(); ++i) {
				dili.add(diffs.get(i));
			}
			return dili;
		}
	}
	
	public static final class Diff extends JavaScriptObject {
		
		protected Diff() {
			
		}
		
		public native int getOperation() /*-{
			return this[0];
		}-*/;
		
		public native String getText() /*-{
			return this[1];
		}-*/;
	}
	

	private static final DiffMatchPatchJSNI dmp = DiffMatchPatchJSNI
			.newInstance();

	private JsArray<Patch> patches;

//	public static final GwtTextDiff IDENTITY = new GwtTextDiff(Patch.createArray());

	public static GwtTextDiff diff(String v1, String v2) {
		return new GwtTextDiff(v1, v2);
	}

	private static GwtTextDiff fromPatches(JsArray<Patch> patches) {
		return new GwtTextDiff(patches);
	}

	private GwtTextDiff(JsArray<Patch> patches) {
		this.patches = patches;
	}

	private GwtTextDiff(String v1, String v2) {
		this.patches = dmp.patch_make_diff_main(v1, v2);
	}

	public String applyTo(String value) {
		return dmp.patch_apply(patches, value);
	}

	public boolean isIdentity() {
		return patches.length() == 0;
	}

	public String getDiffString() {
		return getDMP().patch_toText(patches);
	}

	public static int positionInNewText(String text1, int cursorPos,
			String text2) {
		// TODO: calculating the difference every time is a bit slow
		return dmp.diff_xIndex(dmp.diff_main(text1, text2), cursorPos);
	}

	public static DiffMatchPatchJSNI getDMP() {
		return dmp;
	}

	public static GwtTextDiff fromString(String s) {
		return fromPatches(dmp.patch_fromText(s));
	}

	public int adjustPosition(int pos) {
		return dmp.diff_xIndex_patches(patches, pos);
	}

	public List<Patch> getPatches() {
		LinkedList<Patch> pali = new LinkedList<Patch>();
		for (int i=0; i<patches.length(); ++i) {
			pali.add(patches.get(i));
		}
		return pali;
	}
	


}
