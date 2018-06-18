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

import com.haulmont.cuba.web.widgets.addons.aceeditor.AceEditor;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportMarkerAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportRowAnnotation;
import com.haulmont.cuba.web.widgets.client.addons.aceeditor.TransportDoc.TransportableAs;

/**
 * An annotation for {@link AceEditor}.
 *
 */
public class AceAnnotation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum Type {
		error,
		warning,
		info
	}
	
	private final String message;
	private final Type type;
	
	public AceAnnotation(String message, Type type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public Type getType() {
		return type;
	}

	public TransportAnnotation asTransport() {
		return new TransportAnnotation(message, type);
	}
	
	public static AceAnnotation fromTransport(TransportAnnotation ta) {
		return new AceAnnotation(ta.message, ta.type);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof AceAnnotation) {
			AceAnnotation oa = (AceAnnotation)o;
			return type.equals(oa.type) && message.equals(oa.message);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return message.hashCode(); // ?
	}
	
	@Override
	public String toString() {
		return "<"+message+", "+type+">";
	}
	
	
	public static class MarkerAnnotation implements TransportableAs<TransportMarkerAnnotation>, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final String markerId;
		private final AceAnnotation ann;
		public MarkerAnnotation(String markerId, AceAnnotation ann) {
			this.markerId = markerId;
			this.ann = ann;
		}
		public String getMarkerId() {
			return markerId;
		}
		public AceAnnotation getAnnotation() {
			return ann;
		}
		@Override
		public boolean equals(Object o) {
			if (o instanceof MarkerAnnotation) {
				MarkerAnnotation oma = (MarkerAnnotation)o;
				return markerId.equals(oma.markerId) && ann.equals(oma.ann);
			}
			return false;
		}
		@Override
		public int hashCode() {
			return markerId.hashCode(); // ?
		}
		
		@Override
		public TransportMarkerAnnotation asTransport() {
			return new TransportMarkerAnnotation(markerId, ann.asTransport());
		}
		
		@Override
		public String toString() {
			return markerId+": " + ann;
		}
	}
	
	public static class RowAnnotation implements TransportableAs<TransportRowAnnotation>, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final int row;
		private final AceAnnotation ann;
		public RowAnnotation(int row, AceAnnotation ann) {
			this.row = row;
			this.ann = ann;
		}
		public int getRow() {
			return row;
		}
		public AceAnnotation getAnnotation() {
			return ann;
		}
		@Override
		public boolean equals(Object o) {
			if (o instanceof RowAnnotation) {
				RowAnnotation ora = (RowAnnotation)o;
				return row==ora.row && ann.equals(ora.ann);
			}
			return false;
		}
		@Override
		public int hashCode() {
			return row; // ?
		}
		@Override
		public TransportRowAnnotation asTransport() {
			return new TransportRowAnnotation(row, ann.asTransport());
		}
		@Override
		public String toString() {
			return row+": " + ann;
		}
	}
}
