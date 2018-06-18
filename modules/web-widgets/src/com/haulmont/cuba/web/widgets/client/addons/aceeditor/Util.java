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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Util {

	// TODO: A better way to convert would be better. This is a bit inefficient.
	public static int cursorPosFromLineCol(String text, int line, int col,
			int firstLineNum) {
		return cursorPosFromLineCol(text.split("\n", -1), line, col,
				firstLineNum);
	}

	public static int cursorPosFromLineCol(String[] lines, int line, int col,
			int firstLineNum) {
		line -= firstLineNum;
		int pos = 0;
		for (int currLine = 0; currLine < lines.length; ++currLine) {
			if (currLine < line) {
				pos += lines[currLine].length() + 1;
			} else if (currLine == line) {
				pos += col;
				break;
			}
		}
		return pos;
	}
	
	public static int cursorPosFromLineCol(int[] lineLengths, int line, int col,
			int firstLineNum) {
		line -= firstLineNum;
		int pos = 0;
		for (int currLine = 0; currLine < lineLengths.length; ++currLine) {
			if (currLine < line) {
				pos += lineLengths[currLine] + 1;
			} else if (currLine == line) {
				pos += col;
				break;
			}
		}
		return pos;
	}

	// TODO: A better way to convert would be better. This is a bit inefficient.
	public static int[] lineColFromCursorPos(String text, int pos,
			int firstLineNum) {
		return lineColFromCursorPos(text.split("\n", -1), pos, firstLineNum);
	}

	public static int[] lineColFromCursorPos(String[] lines, int pos,
			int firstLineNum) {
		int lineno = 0;
		int col = pos;
		for (String li : lines) {
			if (col <= li.length()) {
				break;
			}
			lineno += 1;
			col -= (li.length() + 1);
		}
		lineno += firstLineNum;

		return new int[] { lineno, col };
	}
	
	public static int[] lineColFromCursorPos(int[] lineLengths, int pos,
			int firstLineNum) {
		int lineno = 0;
		int col = pos;
		for (int len : lineLengths) {
			if (col <= len) {
				break;
			}
			lineno += 1;
			col -= (len + 1);
		}
		lineno += firstLineNum;

		return new int[] { lineno, col };
	}

	public static int count(char c, String text) {
		int n = 0;
		int from = 0;
		while (true) {
			int index = text.indexOf(c, from);
			if (index == -1) {
				return n;
			} else {
				++n;
				from = index + 1;
			}
		}
	}

	public static int startColOfCursorLine(String text, int cursor) {
		int i = cursor;
		while (i > 0) {
			char c = text.charAt(i - 1);
			if (c == '\n') {
				break;
			}
			--i;
		}
		return i;
	}

	public static String indentationStringOfCursorLine(String text, int cursor) {
		int lineStart = startColOfCursorLine(text, cursor);
		int firstCharAt = lineStart;
		while (firstCharAt < text.length()) {
			// TODO: tab indentation?
			if (text.charAt(firstCharAt) != ' ') {
				break;
			}
			++firstCharAt;
		}
		return text.substring(lineStart, firstCharAt);
	}

	// There's no string join method in java libs??
	public static String join(String[] lines) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String li : lines) {
			if (first) {
				sb.append(li);
				first = false;
			} else {
				sb.append("\n").append(li);
			}

		}
		return sb.toString();
	}
	
	public static <K,V> boolean sameMaps(
			Map<K, V> map1,
			Map<K, V> map2) {
		if (map1.size() != map2.size()) {
			return false;
		}
		for(Entry<K, V> e : map1.entrySet()) {
			V m1 = e.getValue();
			V m2 = map2.get(e.getKey());
			if (!m1.equals(m2)) {
				return false;
			}
		}
		return true;
	}

	public static <V> boolean sameSets(Set<V> set1, Set<V> set2) {
		if (set1.size()!=set2.size()) {
			return false;
		}
		for (V v : set1) {
			if (!set2.contains(v)) {
				return false;
			}
		}
		return true;
	}

	public static String replaceContents(AceRange ofThis, String inText, String withThis) {
		String[] lines = inText.split("\n", -1);
		int start = Util.cursorPosFromLineCol(lines, ofThis.getStartRow(), ofThis.getStartCol(), 0);
		int end = Util.cursorPosFromLineCol(lines, ofThis.getEndRow(), ofThis.getEndCol(), 0);
		return inText.substring(0, start) + withThis + inText.substring(end);
	}

}
