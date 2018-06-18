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


import com.haulmont.cuba.web.widgets.client.addons.aceeditor.GwtTextDiff.Diff;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.GwtTextDiff.Patch;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class DiffMatchPatchJSNI extends JavaScriptObject {

	protected DiffMatchPatchJSNI() {
	}

	native final static public DiffMatchPatchJSNI newInstance() /*-{
		return new $wnd.diff_match_patch();
	}-*/;

	native final public JsArray<Diff> diff_main(String text1, String text2) /*-{
		return this.diff_main(text1, text2);
	}-*/;

	native final public int match_main(String text, String pattern, int loc) /*-{
		return this.match_main(text, pattern, loc);
	}-*/;

	native final public String patch_apply(JsArray<Patch> patches, String text) /*-{
		return this.patch_apply(patches, text)[0];
	}-*/;

	native final public JsArray<Patch> patch_fromText(String text) /*-{
		return this.patch_fromText(text);
	}-*/;

	native final public String patch_toText(JsArray<Patch> patches) /*-{
		return this.patch_toText(patches);
	}-*/;

	native final public int diff_xIndex(JsArray<Diff> diffs, int pos) /*-{
		return this.diff_xIndex(diffs, pos);
	}-*/;

	native final public int diff_xIndex_patches(JsArray<Patch> patches, int pos) /*-{
		for (var i=0; i<patches.length; i++) {
			pos = this.diff_xIndex(patches[i].diffs, pos);
		}
		return pos;
	}-*/;

	native final public JsArray<Patch> patch_make_diff_main(String text1, String text2) /*-{
		return this.patch_make(text1, this.diff_main(text1,text2));
	}-*/;
	
	native final public JsArray<Patch> patch_make(String text1, String text2) /*-{
		return this.patch_make(text1, text2);
	}-*/;

	native final public void setMatch_Threshold(double d) /*-{
		this.Match_Threshold = d;
	}-*/;
	
	native final public void setPatch_Margin(int m) /*-{
		this.Patch_Margin = m;
	}-*/;
	
	native final public void setMatch_Distance(int m) /*-{
		this.Match_Distance = m;
	}-*/;
	
	native final public void setDiff_EditCost(int c) /*-{
		this.Diff_EditCost = c;
	}-*/;
	
	
}
