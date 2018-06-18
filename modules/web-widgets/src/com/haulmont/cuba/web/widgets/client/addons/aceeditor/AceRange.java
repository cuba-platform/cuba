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

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRange;


/**
 * 
 * 
 */
public class AceRange implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int row1;
	private final int col1;
	private final int row2;
	private final int col2;


	public AceRange(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}
	
	public static AceRange fromPositions(int start, int end, String text) {
		return fromPositions(start, end, text.split("\n", -1));
	}
	
	public static AceRange fromPositions(int start, int end, String[] lines) {
		int[] rc1 = Util.lineColFromCursorPos(lines, start, 0);
		int[] rc2 = start==end ? rc1 : Util.lineColFromCursorPos(lines, end, 0);
		return new AceRange(rc1[0], rc1[1], rc2[0], rc2[1]);
	}

	public int getStartRow() {
		return row1;
	}
	
	public int getStartCol() {
		return col1;
	}
	
	public int getEndRow() {
		return row2;
	}
	
	public int getEndCol() {
		return col2;
	}
	
	public int[] getPositions(String text) {
		return getPositions(text.split("\n", -1));
	}
	
	public int[] getPositions(String[] lines) {
		int start = Util.cursorPosFromLineCol(lines, row1, col1, 0);
		int end = isZeroLength() ? start : Util.cursorPosFromLineCol(lines, row2, col2, 0);
		return new int[]{start,end};
	}
	
	

	public TransportRange asTransport() {
		TransportRange tr = new TransportRange();
		tr.row1 = getStartRow();
		tr.col1 = getStartCol();
		tr.row2 = getEndRow();
		tr.col2 = getEndCol();
		return tr;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof AceRange) {
			AceRange or = (AceRange)o;
			return row1 == or.row1 && col1 == or.col1 && row2 == or.row2 && col2 == or.col2;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return row1+col1+row2+col2; // ?
	}
	
	public boolean isBackwards() {
		return row1> row2 || (row1==row2 && col1>col2);
	}
	
	public AceRange reversed() {
		return new AceRange(row2, col2, row1, col1);
	}
	
	public boolean isZeroLength() {
		return row1==row2 && col1==col2;
	}

	public static AceRange fromTransport(TransportRange tr) {
		return new AceRange(tr.row1, tr.col1, tr.row2, tr.col2);
	}
	
	@Override
	public String toString() {
		return "[("+row1+","+col1+")-("+row2+","+col2+")]";
	}

}