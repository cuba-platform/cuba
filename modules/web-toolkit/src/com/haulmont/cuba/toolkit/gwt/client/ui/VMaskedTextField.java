package com.haulmont.cuba.toolkit.gwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VMaskedTextField extends VTextField {

    private char placeholder = '_';

    private StringBuilder string;

    protected String mask;

    private List<Mask> maskTest;

    private void debug(String msg) {
        ApplicationConnection.getConsole().log(msg);
    }

    private KeyPressHandler keyPressHandler = new KeyPressHandler() {
		public void onKeyPress(KeyPressEvent e) {
            if (isReadOnly())
                return;
            debug("keyPressHandler.onKeyPress: " + e.toDebugString());
            if (e.getCharCode() == KeyCodes.KEY_BACKSPACE
					|| e.getCharCode() == KeyCodes.KEY_DELETE
					|| e.getCharCode() == KeyCodes.KEY_END
					|| e.getCharCode() == KeyCodes.KEY_ENTER
					|| e.getCharCode() == KeyCodes.KEY_ESCAPE
					|| e.getCharCode() == KeyCodes.KEY_HOME
					|| e.getCharCode() == KeyCodes.KEY_LEFT
					|| e.getCharCode() == KeyCodes.KEY_PAGEDOWN
					|| e.getCharCode() == KeyCodes.KEY_PAGEUP
					|| e.getCharCode() == KeyCodes.KEY_RIGHT
					|| e.getCharCode() == KeyCodes.KEY_TAB
					|| e.isAltKeyDown() 
                    || e.isControlKeyDown()
                    || e.isMetaKeyDown()) {
                debug("keyPressHandler.onKeyPress: return immediately");
                e.preventDefault(); // KK: otherwise incorrectly handles combinations like Shift+'='
                return;
            }
			if (getCursorPos() < maskTest.size()) {
				Mask m = maskTest.get(getCursorPos());
				if (m != null) {
					if (m.isValid(e.getCharCode())) {
                        debug("keyPressHandler.onKeyPress: valid, m=" + m);
						int pos = getCursorPos();
						string.setCharAt(pos, m.getChar(e.getCharCode()));
						setValue(string.toString());
						updateCursor(pos);
					}
				} else {
                    debug("keyPressHandler.onKeyPress: m=null");
					updateCursor(getCursorPos());
				}
			}
			e.preventDefault();
		}
	};
	
	private KeyDownHandler keyDownHandler = new KeyDownHandler() {
		public void onKeyDown(KeyDownEvent event) {
			if (isReadOnly())
                return;
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
				int pos = getPreviousPos(getCursorPos());
				Mask m = maskTest.get(pos);
				if (m != null) {
					string.setCharAt(pos, placeholder);
					setValue(string.toString());
				}
				setCursorPos(pos);
				event.preventDefault();
			} else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
				int pos = getCursorPos();
				
				Mask m = maskTest.get(pos);
				if (m != null) {
					string.setCharAt(pos, placeholder);
					setValue(string.toString());
				}
				updateCursor(pos);
				event.preventDefault();
			} else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
				setCursorPos(getNextPos(getCursorPos()));
				event.preventDefault();
			} else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
				setCursorPos(getPreviousPos(getCursorPos()));
				event.preventDefault();
			} else if (event.getNativeKeyCode() == KeyCodes.KEY_HOME || event.getNativeKeyCode() == KeyCodes.KEY_UP) {
				setCursorPos(getPreviousPos(0));
				event.preventDefault();
			} else if (event.getNativeKeyCode() == KeyCodes.KEY_END || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
				setCursorPos(getPreviousPos(getValue().length()) + 1);
				event.preventDefault();
			}
		}
	};

	private FocusHandler focusHandler = new FocusHandler() {
		
		public void onFocus(FocusEvent event) {
            if (isReadOnly())
                return;
            debug("focusHandler.onFocus");
			if (getValue().isEmpty())
				setMask(mask);
			else
				setCursorPos(getPreviousPos(0));
		}
	};
	
	private BlurHandler blurHandler = new BlurHandler() {
		
		public void onBlur(BlurEvent event) {
            if (isReadOnly())
                return;
            debug("blurHandler.onBlur");
			for (int i = 0; i < string.length(); i++) {
				char c = string.charAt(i);

				if (maskTest.get(i) != null && c == placeholder) {
					valueChange(true);
					return;
				}
			}
		}
	};

	public VMaskedTextField() {
		super();
		addKeyPressHandler(keyPressHandler);
		addKeyDownHandler(keyDownHandler);
		addFocusHandler(focusHandler);
		addBlurHandler(blurHandler);
        debug("VMaskedTextField created");
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        debug("updateFromUIDL: " + uidl);
		setMask(uidl.getStringAttribute("mask"));
		super.updateFromUIDL(uidl, client);
	}
	
	
	public void setText(String value) {
        debug("setText: " + value);
		if("".equals(value)){
            setMask(mask);
            return;
        }
        string = new StringBuilder(value);
		super.setText(value);
	}

	private void setMask(String mask) {
        debug("setMask: " + mask);
		this.mask = mask;
		string = new StringBuilder();
		maskTest = new ArrayList<Mask>();
		
		for (int i = 0; i < mask.length(); i++) {
			char c = mask.charAt(i);

			if (c == '\'') {
				maskTest.add(null);
				string.append(mask.charAt(++i));
			} else if (c == '#') {
				maskTest.add(new NumericMask());
				string.append(placeholder);
			} else if (c == 'U') {
				maskTest.add(new UpperCaseMask());
				string.append(placeholder);
			} else if (c == 'L') {
				maskTest.add(new LowerCaseMask());
				string.append(placeholder);
			} else if (c == '?') {
				maskTest.add(new LetterMask());
				string.append(placeholder);
			} else if (c == 'A') {
				maskTest.add(new AlphanumericMask());
				string.append(placeholder);
			} else if (c == '*') {
				maskTest.add(new WildcardMask());
				string.append(placeholder);
			} else if (c == 'H') {
				maskTest.add(new HexMask());
				string.append(placeholder);
			} else if (c == '~') {
				maskTest.add(new SignMask());
				string.append(placeholder);
			} else {
				maskTest.add(null);
				string.append(c);
			}				
		}
		setText(string.toString());
//		updateCursor(0); // KK: commented out because leads to grab focus
	}
	
	/*
	
	public String getValue() {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);

			if (maskTest.get(i) != null && c != placeholder)
				result.append(c);
		}
		return result.toString();
	}
	*/

	private void updateCursor(int pos) {
		setCursorPos(getNextPos(pos));	
	}

	private int getNextPos(int pos) {
		while (++pos < maskTest.size() && maskTest.get(pos) == null);
		return pos;
	}
	
	int getPreviousPos(int pos) {
		while (--pos >= 0 && maskTest.get(pos) == null);
		if (pos < 0)
			return getNextPos(pos);
		return pos;
	}
	
	public static interface Mask {
		boolean isValid(char c);

		char getChar(char c);
	}

	public static abstract class AbstractMask implements Mask {
		
		public char getChar(char c) {
			return c;
		}
	}

	public static class NumericMask extends AbstractMask {
		
		public boolean isValid(char c) {
			return Character.isDigit(c);
		}
	}

	public static class LetterMask extends AbstractMask {
		
		public boolean isValid(char c) {
			return Character.isLetter(c);
		}
	}

	public static class LowerCaseMask implements Mask {
		
		public boolean isValid(char c) {
			return Character.isLetter(getChar(c));
		}

		
		public char getChar(char c) {
			return Character.toLowerCase(c);
		}
	}

	public static class UpperCaseMask implements Mask {
		
		public boolean isValid(char c) {
			return Character.isLetter(getChar(c));
		}

		
		public char getChar(char c) {
			return Character.toUpperCase(c);
		}
	}
	
	public static class AlphanumericMask extends AbstractMask {
		
		public boolean isValid(char c) {
			return Character.isLetter(c) || Character.isDigit(c);
		}
	}
	
	public static class WildcardMask extends AbstractMask {
		
		public boolean isValid(char c) {
			return true;
		}
	}
	
	public static class SignMask extends AbstractMask {
		
		public boolean isValid(char c) {
			return c == '-' || c == '+';
		}
	}
	
	/**
     * Represents a hex character, 0-9a-fA-F. a-f is mapped to A-F
     */
    public static class HexMask implements Mask {
        public boolean isValid(char c) {
            return ((c == '0' || c == '1' ||
                     c == '2' || c == '3' ||
                     c == '4' || c == '5' ||
                     c == '6' || c == '7' ||
                     c == '8' || c == '9' ||
                     c == 'a' || c == 'A' ||
                     c == 'b' || c == 'B' ||
                     c == 'c' || c == 'C' ||
                     c == 'd' || c == 'D' ||
                     c == 'e' || c == 'E' ||
                     c == 'f' || c == 'F'));
        }

        public char getChar(char c) {
            if (Character.isDigit(c)) {
                return c;
            }
            return Character.toUpperCase(c);
        }
    }
}
