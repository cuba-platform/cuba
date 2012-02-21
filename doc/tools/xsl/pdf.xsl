<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
				xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:import href="xhtml/docbook.xsl"/>
    <xsl:import href="html-common.xsl"/>
	
	<xsl:param name="ulink.hyphenate.chars">/&amp;?</xsl:param>
	<xsl:param name="ulink.hyphenate">&#xAD;</xsl:param>
	
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
	
	<xsl:attribute-set name="toc.margin.properties">
		<xsl:attribute name="space-before.minimum">0.5em</xsl:attribute>
		<xsl:attribute name="space-before.optimum">1em</xsl:attribute>
		<xsl:attribute name="space-before.maximum">3em</xsl:attribute>
		<xsl:attribute name="space-after.minimum">0.5em</xsl:attribute>
		<xsl:attribute name="space-after.optimum">1em</xsl:attribute>
		<xsl:attribute name="space-after.maximum">2em</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>