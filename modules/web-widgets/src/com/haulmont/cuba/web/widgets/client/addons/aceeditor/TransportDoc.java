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
import java.util.Map;
import java.util.Set;

import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.MarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.AceAnnotation.RowAnnotation;

/**
 * 
 * Classes to be used internally by the ace editor component,
 * for transporting between client and server, etc.
 * 
 * This may seem a bit overkill???
 * 
 */
@SuppressWarnings("serial")
public class TransportDoc implements Serializable {
	
	public interface TransportableAs<T> {
		public T asTransport();
	}
	
	public interface TransportableOf<T> extends Serializable {
		public T fromTransport();
	}

	public static class TransportMarker implements Serializable {
		public String markerId = null;
		public TransportRange range;
		public String onChange;
		public String cssClass;
		public String type;
		public boolean inFront = false;
		@Override
		public boolean equals(Object o) {
			if (o instanceof TransportMarker) {
				TransportMarker om = (TransportMarker)o;
				return markerId.equals(om.markerId) && range.equals(om.range) && onChange.equals(om.onChange) &&
						cssClass.equals(om.cssClass) && type.equals(om.type) && inFront==om.inFront;
			}
			return false;
		}
		@Override
		public int hashCode() {
			return range.hashCode(); // ?
		}
		@Override
		public String toString() {
			return "((Marker " + markerId + " at "+range+", " + cssClass + "))";
		}
	}
	
	public static class TransportRange implements Serializable {
		public int row1;
		public int col1;
		public int row2;
		public int col2;
		public TransportRange() {}
		public TransportRange(int row1, int col1, int row2, int col2) {
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
		}
		@Override
		public String toString() {
			return "[(" + row1+","+col1+")-(" + row2+","+col2+")]";
		}
	}
	
	public static class TransportAnnotation implements Serializable {
		public String message;
		public AceAnnotation.Type type;
		public TransportAnnotation() {}
		public TransportAnnotation(String message, AceAnnotation.Type type) {
			this.message = message;
			this.type = type;
		}
	}
	
	public static class TransportRowAnnotation implements TransportableOf<RowAnnotation> {
		public int row;
		public TransportAnnotation ann;
		public TransportRowAnnotation() {}
		public TransportRowAnnotation(int row, TransportAnnotation ann) {
			this.row = row;
			this.ann = ann;
		}
		@Override
		public RowAnnotation fromTransport() {
			return new RowAnnotation(row, AceAnnotation.fromTransport(ann));
		}
	}
	
	public static class TransportMarkerAnnotation implements TransportableOf<MarkerAnnotation> {
		public String markerId;
		public TransportAnnotation ann;
		public TransportMarkerAnnotation() {}
		public TransportMarkerAnnotation(String markerId, TransportAnnotation ann) {
			this.markerId = markerId;
			this.ann = ann;
		}
		@Override
		public MarkerAnnotation fromTransport() {
			return new MarkerAnnotation(markerId, AceAnnotation.fromTransport(ann));
		}
	}
	
	
	
	public String text;
	public Map<String, TransportMarker> markers;
	public Set<TransportRowAnnotation> rowAnnotations;
	public Set<TransportMarkerAnnotation> markerAnnotations;
	
	@Override
	public String toString() {
		return "doc text >>>>>>>>>>>\n"+text+"\n/////////////////\n"+markers+"\n<<<<<<<<<<<<<<<";
	}
	
}
