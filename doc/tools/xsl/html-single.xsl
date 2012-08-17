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
	
</xsl:stylesheet>