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

/**
 * Ace theme defines the appearance of the editor.
 * 
 */
public enum AceTheme {
	ambiance,
	chrome,
	clouds,
	clouds_midnight,
	cobalt,
	crimson_editor,
	dawn, dreamweaver,
	eclipse,
	github,
	idle_fingers,
	katzenmilch,
	kuroir,
	kr,
	merbivore,
	merbivore_soft,
	mono_industrial,
	monokai,
	pastel_on_dark,
	solarized_dark,
	solarized_light,
	terminal,
	textmate, tomorrow,
	tomorrow_night,
	tomorrow_night_blue,
	tomorrow_night_bright,
	tomorrow_night_eighties,
	twilight,
	vibrant_ink,
	xcode;
	
	public String getRequireString() {
		return "ace/theme/"+this.toString();
	}
}
