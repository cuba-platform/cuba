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
package com.haulmont.cuba.web.widgets.addons.aceeditor;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceRange;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.Util;

public class TextRange extends AceRange {

	private final String text;
	int start = -1;
	int end = -1;
	
	public TextRange(String text, int row1, int col1, int row2, int col2) {
		super(row1, col1, row2, col2);
		this.text = text;
	}
	
	public TextRange(String text, AceRange range) {
		this(text, range.getStartRow(), range.getStartCol(), range.getEndRow(), range.getEndCol());
	}
	
	public TextRange(String text, int start, int end) {
		this(text, AceRange.fromPositions(start, end, text));
	}

	public int getStart() {
		if (start==-1) {
			start = Util.cursorPosFromLineCol(text, getStartRow(), getStartCol(), 0);
		}
		return start;
	}

	public int getEnd() {
		if (end==-1) {
			end = Util.cursorPosFromLineCol(text, getEndRow(), getEndCol(), 0);
		}
		return end;
	}
	
	public int getCursorPosition() {
		return getEnd();
	}
	
	public TextRange withNewText(String newText) {
		return new TextRange(newText, getStart(), getEnd());
	}
	
}
