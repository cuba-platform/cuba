/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.plaf.nimbus;

import com.haulmont.cuba.desktop.sys.vcl.SearchComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Default "ComboBoxTextFieldPainter" painter class does not support background color overriding. <br>
 * Allows draw combobox without button for SearchComboBox.
 */
public class MandatoryComboBoxTextFieldPainter extends BaseMandatoryRegionPainter {
    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ComboBoxTextFieldPainter to determine which region/state is being painted
    //by that instance.
    static final int BACKGROUND_DISABLED = 1;
    static final int BACKGROUND_ENABLED = 2;
    static final int BACKGROUND_SELECTED = 3;

    private int state; //refers to one of the static final integers above
    private PaintContext ctx;

    private boolean drawButtonBorder = true;

    //the following 4 variables are reused during the painting code of the layers
//    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
//    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
//    private Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of ComboBoxTextFieldPainter would be created. For the moment at least,
    //however, all are created for each instance.
    private Color color1 = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.74509805f, -237);
    private Color color2 = decodeColor("nimbusBlueGrey", -0.006944418f, -0.07187897f, 0.06666666f, 0);
    private Color color3 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07703349f, 0.0745098f, 0);
    private Color color4 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07968931f, 0.14509803f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.07856284f, 0.11372548f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.040395975f, -0.60315615f, 0.29411763f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.016586483f, -0.6051466f, 0.3490196f, 0);
    private Color color8 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.0965403f, -0.18431371f, 0);
    private Color color9 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.1048766f, -0.05098039f, 0);
    private Color color10 = decodeColor("nimbusLightBackground", 0.6666667f, 0.004901961f, -0.19999999f, 0);
    private Color color11 = decodeColor("nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
    private Color color12 = decodeColor("nimbusBlueGrey", 0.055555582f, -0.105344966f, 0.011764705f, 0);

    //Array of current component colors, updated in each paint call
    private Object[] componentColors;

    public MandatoryComboBoxTextFieldPainter(PaintContext ctx, int state) {
        this.ctx = ctx;
        this.state = state;
    }

    public static MandatoryComboBoxTextFieldPainter backgroundEnabledPainter() {
        return new MandatoryComboBoxTextFieldPainter(new AbstractRegionPainterPaintContext(new Insets(5, 3, 3, 5),
                new Dimension(64, 24), false, "NINE_SQUARE_SCALE", Double.POSITIVE_INFINITY, 2.0), BACKGROUND_ENABLED);
    }

    public static MandatoryComboBoxTextFieldPainter backgroundDisabledPainter() {
        return new MandatoryComboBoxTextFieldPainter(new AbstractRegionPainterPaintContext(new Insets(5, 3, 3, 5),
                new Dimension(64, 24), false, "NINE_SQUARE_SCALE", Double.POSITIVE_INFINITY, 2.0), BACKGROUND_DISABLED);
    }

    public static MandatoryComboBoxTextFieldPainter backgroundSelectedPainter() {
        return new MandatoryComboBoxTextFieldPainter(new AbstractRegionPainterPaintContext(new Insets(5, 3, 3, 5),
                new Dimension(64, 24), false, "NINE_SQUARE_SCALE", Double.POSITIVE_INFINITY, 2.0), BACKGROUND_SELECTED);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        drawButtonBorder = !(c.getParent() instanceof SearchComboBox);

        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case BACKGROUND_DISABLED: paintBackgroundDisabled(g); break;
            case BACKGROUND_ENABLED: paintBackgroundEnabled(g); break;
            case BACKGROUND_SELECTED: paintBackgroundSelected(g); break;
        }
    }

    // todo changes here and where "(Color) componentColors[0]" appear
    @Override
    protected Object[] getExtendedCacheKeys(JComponent c) {
        Object[] extendedCacheKeys;
        extendedCacheKeys = new Object[] {
                     getComponentColor(c, "background", color11, 0.0f, 0.0f, 0)};
        return extendedCacheKeys;
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundDisabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(decodeGradient1(rect));
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(decodeGradient2(rect));
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(color6);
        g.fill(rect);
        rect = decodeRect5();
        g.setPaint(color7);
        g.fill(rect);
    }

    private void paintBackgroundEnabled(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(decodeGradient3(rect));
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(decodeGradient4(rect));
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(color12);
        g.fill(rect);
        rect = decodeRect5();
        g.setPaint((Color) componentColors[0]);
        g.fill(rect);
    }

    private void paintBackgroundSelected(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color1);
        g.fill(rect);
        rect = decodeRect2();
        g.setPaint(decodeGradient3(rect));
        g.fill(rect);
        rect = decodeRect3();
        g.setPaint(decodeGradient4(rect));
        g.fill(rect);
        rect = decodeRect4();
        g.setPaint(color12);
        g.fill(rect);
        rect = decodeRect5();
        g.setPaint((Color) componentColors[0]);
        g.fill(rect);
    }

    private Rectangle2D decodeRect1() {
        if (drawButtonBorder) {
            rect.setRect(decodeX(0.6666667f), //x
                         decodeY(2.3333333f), //y
                         decodeX(3.0f) - decodeX(0.6666667f), //width
                         decodeY(2.6666667f) - decodeY(2.3333333f)); //height
        } else {
            rect.setRect(decodeX(0.6666667f), //x
                    decodeY(2.3333333f), //y
                    decodeX(3.0f) - 2 * decodeX(0.6666667f), //width
                    decodeY(2.6666667f) - decodeY(2.3333333f)); //height
        }
        return rect;
    }

    private Rectangle2D decodeRect2() {
        if (drawButtonBorder) {
            rect.setRect(decodeX(0.6666667f), //x
                         decodeY(0.4f), //y
                         decodeX(3.0f) - decodeX(0.6666667f), //width
                         decodeY(1.0f) - decodeY(0.4f)); //height
        } else {
            rect.setRect(decodeX(0.6666667f), //x
                    decodeY(0.4f), //y
                    decodeX(3.0f) - 2 * decodeX(0.6666667f), //width
                    decodeY(1.0f) - decodeY(0.4f)); //height
        }
        return rect;
    }

    private Rectangle2D decodeRect3() {
        if (drawButtonBorder) {
            rect.setRect(decodeX(1.0f), //x
                         decodeY(0.6f), //y
                         decodeX(3.0f) - decodeX(1.0f), //width
                         decodeY(1.0f) - decodeY(0.6f)); //height
        } else {
            rect.setRect(decodeX(1.0f), //x
                    decodeY(0.6f), //y
                    decodeX(3.0f) - 2 * decodeX(1.0f), //width
                    decodeY(1.0f) - decodeY(0.6f)); //height
        }
        return rect;
    }

    private Rectangle2D decodeRect4() {
        if (drawButtonBorder) {
            rect.setRect(decodeX(0.6666667f), //x
                    decodeY(1.0f), //y
                    decodeX(3.0f) - decodeX(0.6666667f), //width
                    decodeY(2.3333333f) - decodeY(1.0f)); //height
        } else {
            rect.setRect(decodeX(0.6666667f), //x
                    decodeY(1.0f), //y
                    decodeX(3.0f) - 2 * decodeX(0.6666667f), //width
                    decodeY(2.3333333f) - decodeY(1.0f)); //height
        }
        return rect;
    }

    private Rectangle2D decodeRect5() {
        if (drawButtonBorder) {
            rect.setRect(decodeX(1.0f), //x
                    decodeY(1.0f), //y
                    decodeX(3.0f) - decodeX(1.0f), //width
                    decodeY(2.0f) - decodeY(1.0f)); //height
        } else {
            rect.setRect(decodeX(1.0f), //x
                    decodeY(1.0f), //y
                    decodeX(3.0f) - 2 * decodeX(1.0f), //width
                    decodeY(2.0f) - decodeY(1.0f)); //height
        }
        return rect;
    }

    private Paint decodeGradient1(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        float h = (float)bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f,0.5f,1.0f },
                new Color[] { color2,
                            decodeColor(color2,color3,0.5f),
                            color3});
    }

    private Paint decodeGradient2(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        float h = (float)bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (1.0f * h) + y, (0.5f * w) + x, (0.0f * h) + y,
                new float[] { 0.0f,0.5f,1.0f },
                new Color[] { color4,
                            decodeColor(color4,color5,0.5f),
                            color5});
    }

    private Paint decodeGradient3(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        float h = (float)bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.0f,0.49573863f,0.99147725f },
                new Color[] { color8,
                            decodeColor(color8,color9,0.5f),
                            color9});
    }

    private Paint decodeGradient4(Shape s) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        float w = (float)bounds.getWidth();
        float h = (float)bounds.getHeight();
        return decodeGradient((0.5f * w) + x, (0.0f * h) + y, (0.5f * w) + x, (1.0f * h) + y,
                new float[] { 0.1f,0.49999997f,0.9f },
                new Color[] { color10,
                            decodeColor(color10,(Color) componentColors[0],0.5f),
                            (Color) componentColors[0]});
    }
}