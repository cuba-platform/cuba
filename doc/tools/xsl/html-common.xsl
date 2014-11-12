<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xslthl="http://xslthl.sf.net"
                version="1.0">
    <xsl:import href="highlight.xsl"/>

    <xsl:import href="highlighting/common.xsl"/>
    <xsl:import href="html/highlight.xsl"/>
	<xsl:param name="use.id.as.filename">1</xsl:param>
    <xsl:param name="use.extensions">1</xsl:param>
    <xsl:param name="toc.section.depth">3</xsl:param>
    <xsl:param name="section.autolabel">1</xsl:param>
    <xsl:param name="section.label.includes.component.label">3</xsl:param>
	<xsl:param name="section.autolabel.max.depth">3</xsl:param>
	<xsl:param name="simplesect.autolabel">0</xsl:param>
    <xsl:param name="css.decoration">0</xsl:param>
    <!--<xsl:param name="highlight.source" select="1"/>-->
	<xsl:param name="preface.autolabel">0</xsl:param>
	<!--<xsl:param name="generate.index" select="1"/>
	<xsl:param name="index.on.type" select="1"/>-->
	<xsl:param name="entry.propagate.style" select="2"/> 
	<xsl:param name="emphasis.propagate.style" select="1"/> 
	<xsl:param name="chunk.separate.lots" select="1"/>
	<xsl:param name="autotoc.label.separator">. </xsl:param>
	<xsl:param name="autotoc.label.in.hyperlink" select="1"></xsl:param>	
	<xsl:param name="ulink.hyphenate.chars">_.%#/&amp;?</xsl:param>
	<xsl:param name="ulink.hyphenate">&#xAD;</xsl:param>

	<xsl:param name="chunker.output.encoding" select="'windows-1251'"/>
	<!--<xsl:param name="highlight.xslthl.config" select="'file:///highlighting/xslthl-config.xml'"/>-->
    
	<xsl:variable name="p.text-indent" select="'10%'"/> 
	
	<xsl:param name="generate.toc">
        book toc,title,example
    </xsl:param>

    <xsl:param name="formal.title.placement">
        figure before
        example before
        equation before
        table before
        procedure before
    </xsl:param>

    <xsl:param name="html.stylesheet">DUMMY</xsl:param>
    <xsl:template name="output.html.stylesheets">
        <link href="style.css" rel="stylesheet" type="text/css"/>
    </xsl:template>
	
	<!--<xsl:template name="customXref">
        <xsl:param name="target"/>
        <xsl:param name="content">
            <xsl:apply-templates select="$target" mode="object.title.markup"/>
        </xsl:param>
        <a>
            <xsl:attribute name="href">
                <xsl:call-template name="href.target">
                    <xsl:with-param name="object" select="$target"/>
                </xsl:call-template>
            </xsl:attribute>
            <xsl:attribute name="title">
                <xsl:apply-templates select="$target" mode="object.title.markup.textonly"/>
            </xsl:attribute>
            <xsl:value-of select="$content"/>
        </a>
    </xsl:template>
	
	<xsl:template name="user.footer.content">
		<HR/>
		<xsl:apply-templates select="//copyright[1]" mode="titlepage.mode"/>
	</xsl:template>-->
	
	<!--  Number figures continuosly through the book--> 
	<xsl:template match="figure" mode="label.markup">
		<xsl:number format="1" from="book" level="any" /> 
	</xsl:template>
</xsl:stylesheet>
