/*
 * Copyright (c) 2008-2020 Haulmont.
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
 */

package com.haulmont.cuba.web.sys.sanitizer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.haulmont.cuba.gui.components.RichTextArea;
import org.owasp.html.CssSchema;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

import static com.haulmont.cuba.gui.components.HtmlAttributes.CSS.FONT;

/**
 * Utility bean that sanitizes a sting of HTML according to the factory's policy to prevent Cross-site Scripting (XSS)
 * in HTML context.
 * <p>
 * The default policy factory contains special policies for the font element, because the {@link RichTextArea} component
 * supports the font element as value. Also default policy factory contains policies that are not contained in
 * standard {@link Sanitizers}.
 */
@Component(HtmlSanitizer.NAME)
public class HtmlSanitizer {

    public static final String NAME = "cuba_HtmlSanitizer";

    /**
     * Font size regexp. Intended to match size attribute value of font element.
     * <p>
     * Regexp explanation:
     * <ul>
     *     <li>{@code [0-7]} - matches a number in the range 0 to 7</li>
     *     <li>{@code |} - acts like a boolean OR</li>
     *     <li>{@code [+-]?(?:[0-9]+)} - matches a relative font size value</li>
     * </ul>
     * <p>
     * Example:
     * <pre>{@code
     *      &lt;font size="7"/&gt;
     * }</pre>
     */
    protected static final String FONT_SIZE_REGEXP = "[0-7]|[+-]?(?:[0-9]+)";
    protected static final Pattern FONT_SIZE_PATTERN = Pattern.compile(FONT_SIZE_REGEXP);
    protected static final String FONT_SIZE_ATTRIBUTE_NAME = "size";

    /**
     * Font face regexp. Intended to match face attribute value of font element.
     * <p>
     * Regexp explanation:
     * <ul>
     *     <li>{@code [\w;, \-]+} - matches font names separated by comma or semicolon</li>
     * </ul>
     * <p>
     * Example:
     * <pre>{@code
     *      &lt;font face="Verdana"/&gt;
     * }</pre>
     */
    protected static final String FONT_FACE_REGEXP = "[\\w;, \\-]+";
    protected static final Pattern FONT_FACE_PATTERN = Pattern.compile(FONT_FACE_REGEXP);
    protected static final String FONT_FACE_ATTRIBUTE_NAME = "face";

    /**
     * Font color regexp. Intended to match color attribute value that of font element.
     * <p>
     * Regexp explanation:
     * <ul>
     *     <li>{@code (#(?:[0-9a-f]{2}){2,4}|(#[0-9a-f]{3})} - matches hexademical color</li>
     *     <li>{@code |} - acts like a boolean OR</li>
     *     <li>{@code (rgb|hsl)a?\((-?\d+%?[,\s]+){2,3}\s*[d\.]+%?\)} - matches RGB, RGBA, HSL, HSLA colors</li>
     *     <li>{@code "color_name"} - matches color by name</li>
     * </ul>
     * <p>
     * Example:
     * <pre>{@code
     *      &lt;font color="#0000ff"/&gt;
     * }</pre>
     */
    protected static final String FONT_COLOR_REGEXP = "(#(?:[0-9a-f]{2}){2,4}|(#[0-9a-f]{3})" +
            "|(rgb|hsl)a?\\((-?\\d+%?[,\\s]+){2,3}\\s*[d\\.]+%?\\)" +
            "|\\b(|aliceblue|antiquewhite|aqua|aquamarine|azure|beige|bisque|black|blanchedalmond|blue|blueviolet|brown" +
            "|burlywood|cadetblue|chartreuse|chocolate|coral|cornflowerblue|cornsilk|crimson|cyan|darkblue|darkcyan" +
            "|darkgoldenrod|darkgray|darkgreen|darkgrey|darkkhaki|darkmagenta|darkolivegreen|darkorange|darkorchid" +
            "|darkred|darksalmon|darkseagreen|darkslateblue|darkslategray|darkslategrey|darkturquoise|darkviolet" +
            "|deeppink|deepskyblue|dimgray|dimgrey|dodgerblue|firebrick|floralwhite|forestgreen|fuchsia|gainsboro" +
            "|ghostwhite|goldenrod|gold|green|greenyellow|gray|grey|honeydew|hotpink|indianred|indigo|ivory|khaki" +
            "|lavenderblush|lavender|lawngreen|lemonchiffon|lightblue|lightcoral|lightcyan|lightgoldenrodyellow" +
            "|lightgray|lightgreen|lightgrey|lightpink|lightsalmon|lightseagreen|lightskyblue|lightslategray" +
            "|lightslategrey|lightsteelblue|lightyellow|lime|limegreen|linen|magenta|maroon|mediumaquamarine|mediumblue" +
            "|mediumorchid|mediumpurple|mediumseagreen|mediumslateblue|mediumspringgreen|mediumturquoise|mediumvioletred" +
            "|midnightblue|mintcream|mistyrose|moccasin|navajowhite|navy|oldlace|olive|olivedrab|orange|orangered" +
            "|orchid|palegoldenrod|palegreen|paleturquoise|palevioletred|papayawhip|peachpuff|peru|pink|plum|powderblue" +
            "|purple|rebeccapurple|red|rosybrown|royalblue|saddlebrown|salmon|sandybrown|seagreen|seashell" +
            "|sienna|silver|skyblue|slateblue|slategray|slategrey|snow|springgreen|steelblue|tan|teal|thistle|tomato" +
            "|turquoise|violet|wheat|white|whitesmoke|yellow|yellowgreen)\\b)";
    protected static final Pattern FONT_COLOR_PATTERN = Pattern.compile(FONT_COLOR_REGEXP);
    protected static final String FONT_COLOR_ATTRIBUTE_NAME = "color";

    /**
     * Html class regexp. Intended to match class attribute value.
     * <p>
     * Regexp explanation:
     * <ul>
     *     <li>{@code a-zA-Z} - matches a single character in the range: a-z, A-Z</li>
     *     <li>{@code 0-9} - matches a single character in the range: 0-9</li>
     *     <li>{@code ,} - matches a comma character</li>
     *     <li>{@code \\s} - matches any whitespace character</li>
     *     <li>{@code \\-} - matches a dash character</li>
     *     <li>{@code _} - matches an underscore character </li>
     *     <li>{@code []+} - matches between one and unlimited times</li>
     * </ul>
     * <p>
     * Example:
     * <pre>{@code
     *      &lt;div class="v-app"/&gt;
     * }</pre>
     */
    protected static final String CLASS_REGEXP = "[a-zA-Z0-9\\s,\\-_]+";
    protected static final Pattern CLASS_PATTERN = Pattern.compile(CLASS_REGEXP);
    protected static final String CLASS_ATTRIBUTE_NAME = "class";

    /**
     * The additional css schema whitelist that was not included in the default whitelist in {@code Sanitizers.STYLES}. .
     */
    protected static final ImmutableSet<String> DEFAULT_WHITELIST = ImmutableSet.of(
            "clip",
            "opacity",
            "overflow",
            "overflow-x",
            "overflow-y",
            "page-break-after",
            "page-break-before",
            "page-break-inside",
            "play-during",
            "visibility",
            "zoom",
            "z-index"
    );

    protected PolicyFactory policyFactory;

    public HtmlSanitizer() {
        initDefaultPolicyFactory();
    }

    /**
     * Sanitizes a string of HTML according to the factory's policy.
     *
     * @param html the string of HTML to sanitize
     * @return a string of HTML that complies with the factory's policy
     */
    public String sanitize(@Nullable String html) {
        return policyFactory.sanitize(html);
    }

    /**
     * @return a policy factory
     */
    @Nonnull
    public PolicyFactory getPolicyFactory() {
        return policyFactory;
    }

    /**
     * Sets policy factory.
     *
     * @param policyFactory a policy factory
     */
    public void setPolicyFactory(@Nonnull PolicyFactory policyFactory) {
        this.policyFactory = policyFactory;
    }

    /**
     * Init default policy factory that is used to produce HTML sanitizer policies that sanitize a sting of HTML.
     */
    protected void initDefaultPolicyFactory() {
        policyFactory = new HtmlPolicyBuilder()
                .allowCommonInlineFormattingElements()
                .allowAttributes(FONT_COLOR_ATTRIBUTE_NAME).matching(FONT_COLOR_PATTERN).onElements(FONT)
                .allowAttributes(FONT_FACE_ATTRIBUTE_NAME).matching(FONT_FACE_PATTERN).onElements(FONT)
                .allowAttributes(FONT_SIZE_ATTRIBUTE_NAME).matching(FONT_SIZE_PATTERN).onElements(FONT)
                .allowAttributes(CLASS_ATTRIBUTE_NAME).matching(CLASS_PATTERN).globally()
                .allowStyling(CssSchema.withProperties(DEFAULT_WHITELIST))
                .allowStyling(CssSchema.withProperties(getAdditionalStylePolicies()))
                .toFactory()
                .and(Sanitizers.FORMATTING)
                .and(Sanitizers.LINKS)
                .and(Sanitizers.BLOCKS)
                .and(Sanitizers.IMAGES)
                .and(Sanitizers.STYLES)
                .and(Sanitizers.TABLES);
    }

    /**
     * @return additional style policies that were not included in {@code Sanitizers.STYLES}.
     */
    protected ImmutableMap<String, CssSchema.Property> getAdditionalStylePolicies() {
        ImmutableMap.Builder<String, CssSchema.Property> builder = ImmutableMap.builder();
        builder.put("align-content", new CssSchema.Property(0,
                ImmutableSet.of("baseline", "first baseline", "last baseline", "space-between", "space-around",
                        "space-evenly", "stretch", "unsafe", "safe", "center", "start", "end", "flex-start", "flex-end",
                        "normal", "safe center", "unsafe center", "inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("align-items", new CssSchema.Property(1,
                ImmutableSet.of("baseline", "first", "last", "unsafe", "safe", "center", "start", "end", "self-start",
                        "self-end", "flex-start", "flex-end", "normal", "stretch", "inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("align-self", new CssSchema.Property(1,
                ImmutableSet.of("auto", "normal", "center", "start", "end", "self-start", "self-end", "flex-start",
                        "flex-end", "baseline", "first", "last", "stretch", "unsafe", "safe", "center", "inherit",
                        "initial", "unset"),
                ImmutableMap.of()));

        builder.put("bottom", new CssSchema.Property(5,
                ImmutableSet.of("auto", "inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("clear", new CssSchema.Property(0,
                ImmutableSet.of("none", "left", "right", "both", "inline-start", "inline-end", "inherit", "initial",
                        "unset"),
                ImmutableMap.of()));

        builder.put("content", new CssSchema.Property(8,
                ImmutableSet.of("none", "normal", "open-quote", "close-quote", "no-open-quote", "no-close-qoute",
                        "url", "counter", "inherit"),
                ImmutableMap.of()));

        builder.put("cursor", new CssSchema.Property(272,
                ImmutableSet.of("auto", "default", "none", "context-menu", "help", "pointer", "progress", "wait",
                        "cell", "crosshair", "text", "vertical-text", "alias", "copy", "move", "no-drop", "not-allowed",
                        "e-resize", "n-resize", "ne-resize", "nw-resize", "s-resize", "se-resize", "sw-resize",
                        "w-resize", "ew-resize", "ns-resize", "nesw-resize", "nwse-resize", "col-resize", "row-resize",
                        "all-scroll", "zoom-in", "zoom-out", "grab", "grabbing", ","),
                ImmutableMap.of()));

        builder.put("flex", new CssSchema.Property(1,
                ImmutableSet.of("auto", "inherit", "initial", "unset", "none", "min-content"),
                ImmutableMap.of()));

        builder.put("flex-basis", new CssSchema.Property(1,
                ImmutableSet.of("auto", "inherit", "initial", "unset", "fill", "max-content", "min-content",
                        "fit-content", "content"),
                ImmutableMap.of()));

        builder.put("flex-direction", new CssSchema.Property(1,
                ImmutableSet.of("inherit", "initial", "unset", "row", "row-reverse", "column", "column=reverse"),
                ImmutableMap.of()));

        builder.put("flex-flow", new CssSchema.Property(0,
                ImmutableSet.of("inherit", "initial", "unset", "row", "row-reverse", "column", "column=reverse",
                        "nowrap", "wrap", "wrap-reverse"),
                ImmutableMap.of()));

        builder.put("flex-grow", new CssSchema.Property(1,
                ImmutableSet.of("inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("flex-shrink", new CssSchema.Property(1,
                ImmutableSet.of("inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("flex-wrap", new CssSchema.Property(0,
                ImmutableSet.of("inherit", "initial", "unset", "nowrap", "wrap", "wrap-reverse"),
                ImmutableMap.of()));

        builder.put("justify-content", new CssSchema.Property(0,
                ImmutableSet.of("normal", "center", "start", "end", "flex-start", "flex-end", "left", "right",
                        "space-between", "space-around", "space-evenly", "stretch", "safe center", "unsafe center",
                        "inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("left", new CssSchema.Property(5,
                ImmutableSet.of("auto", "inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("display", new CssSchema.Property(0,
                ImmutableSet.of("-moz-inline-box", "-moz-inline-stack", "block", "inline", "inline-block",
                        "inline-table", "list-item", "run-in", "table", "table-caption", "table-cell",
                        "table-column", "table-column-group", "table-footer-group", "table-header-group",
                        "table-row", "table-row-group", "flow", "flow-root", "flex", "grid", "ruby", "ruby-base",
                        "ruby-text", "ruby-base-container", "ruby-text-container", "contents", "inline-list-item",
                        "inline-flex", "inline-grid"),
                ImmutableMap.of()));

        builder.put("float", new CssSchema.Property(0,
                ImmutableSet.of("left", "right", "none", "inline-start", "inline-end"),
                ImmutableMap.of()));

        builder.put("order", new CssSchema.Property(5,
                ImmutableSet.of("inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("position", new CssSchema.Property(0,
                ImmutableSet.of("static", "relative", "absolute", "sticky", "fixed"),
                ImmutableMap.of()));

        builder.put("right", new CssSchema.Property(5,
                ImmutableSet.of("auto", "inherit", "initial", "unset"),
                ImmutableMap.of()));

        builder.put("top", new CssSchema.Property(5,
                ImmutableSet.of("auto", "inherit", "initial", "unset"),
                ImmutableMap.of()));

        return builder.build();
    }
}
