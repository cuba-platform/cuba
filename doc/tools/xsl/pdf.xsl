<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
				xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:import href="xhtml/docbook.xsl"/>
    <xsl:import href="html-common.xsl"/>	
	<xsl:param name="section.autolabel">1</xsl:param>
    <xsl:param name="section.label.includes.component.label">3</xsl:param>
	<xsl:param name="section.autolabel.max.depth">3</xsl:param> 
	<xsl:param name="formal.title.placement">figure after</xsl:param>
	<xsl:param name="autotoc.label.separator">. </xsl:param>
	
	<xsl:param name="toc.pointer.graphic">0</xsl:param>
	<xsl:param name="toc.pointer.text">a</xsl:param>
	
	<xsl:param name="formal.title.placement">
        figure before
        example before
        equation before
        table before
        procedure before
    </xsl:param>
	
	<!--<xsl:attribute-set name="toc.line.properties">
		<xsl:attribute name="font-weight">
			<xsl:choose>
				<xsl:when test="self::chapter">bold</xsl:when>
				<xsl:otherwise>normal</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
	</xsl:attribute-set>-->

	<!--<xsl:attribute-set name="toc.margin.properties">
		<xsl:attribute name="start-indent">0.5in</xsl:attribute>
	</xsl:attribute-set>-->

<!--
<xsl:template name="page.number.format">
  <xsl:param name="element" select="local-name(.)"/>
  <xsl:choose>
    <xsl:when test="$element = 'toc'">1</xsl:when>
    <xsl:when test="$element = 'preface'">i</xsl:when>
    <xsl:when test="$element = 'dedication'">i</xsl:when>
    <xsl:otherwise>1</xsl:otherwise>
  </xsl:choose>
</xsl:template>
-->	
	
	
    <!-- Use custom <head> content, to include stylesheets and bookmarks -->
	
    <xsl:template name="output.html.stylesheets">
        <link href="style.css" rel="stylesheet" type="text/css"/>
        <link href="print.css" rel="stylesheet" type="text/css" media="print"/>
    </xsl:template>

    <xsl:template name="user.head.content">
        <bookmarks>
            <xsl:apply-templates select="chapter|appendix|preface|glossary" mode="bookmarks"/>
        </bookmarks>
    </xsl:template>

    <xsl:template match="*" mode="bookmarks">
        <bookmark>
            <xsl:attribute name="name">
                <xsl:apply-templates select="." mode="object.title.markup"/>
            </xsl:attribute>
            <xsl:attribute name="href">#<xsl:call-template name="object.id"/></xsl:attribute>
            <xsl:apply-templates select="section[parent::chapter|parent::appendix]" mode="bookmarks"/>
        </bookmark>
    </xsl:template>

	<xsl:template name="book.titlepage.recto">	  
		<xsl:choose>
			<xsl:when test="bookinfo/title">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
			</xsl:when>
			<xsl:when test="info/title">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/title"/>
			</xsl:when>
			<xsl:when test="title">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="title"/>
			</xsl:when>
		</xsl:choose>
  
		<xsl:choose>
			<xsl:when test="bookinfo/subtitle">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/subtitle"/>
			</xsl:when>
			<xsl:when test="info/subtitle">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/subtitle"/>
			</xsl:when>
			<xsl:when test="subtitle">
				<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="subtitle"/>
			</xsl:when>
		</xsl:choose>
  
		<xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>

	</xsl:template>
	
	<!--<xsl:attribute-set name="toc.margin.properties">
		<xsl:attribute name="space-before.minimum">0.5em</xsl:attribute>
		<xsl:attribute name="space-before.optimum">1em</xsl:attribute>
		<xsl:attribute name="space-before.maximum">3em</xsl:attribute>
		<xsl:attribute name="space-after.minimum">0.5em</xsl:attribute>
		<xsl:attribute name="space-after.optimum">1em</xsl:attribute>
		<xsl:attribute name="space-after.maximum">2em</xsl:attribute>
	</xsl:attribute-set>-->
	



</xsl:stylesheet>