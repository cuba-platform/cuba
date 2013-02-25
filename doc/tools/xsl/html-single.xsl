<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="html/docbook.xsl"/>
    <xsl:import href="html-common.xsl"/>
	<xsl:param name="ignore.image.scaling">1</xsl:param>
	
	<!--  Number figures continuosly through the book--> 
	<xsl:template match="figure" mode="label.markup">
		<xsl:number format="1" from="book" level="any" /> 
	</xsl:template>

    <xsl:variable name="headercode">
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
        <script type="text/javascript">
        $(document).ready(function() {

            $('div.book').add('<a href="#" id="toc-btn" class="toc-btn">. . .</a>').appendTo(document.body);
            $('div.book').add('<div id="toc-panel" class="toc-panel"></div>').appendTo(document.body);

            $('div.toc').clone().appendTo('#toc-panel');

            var panel = $('#toc-panel');
            var button = $('#toc-btn');

            button.attr( 'href', 'javascript:void(0)' ).mousedown(function() {
                panel.toggle('fast');
                button.toggleClass('active');
                return false;
            });

            $(document).bind('mousedown', function() {
                panel.hide('fast');
                button.removeClass('active');
            });

            panel.bind('mousedown', function(e) {
                e.stopPropagation();
            });
        });
        </script>
    </xsl:variable>

    <xsl:template name="user.header.content">
        <xsl:copy-of select="$headercode"/>
    </xsl:template>

    <xsl:output method="html"
            encoding="UTF-8"
            indent="yes"/>

</xsl:stylesheet>