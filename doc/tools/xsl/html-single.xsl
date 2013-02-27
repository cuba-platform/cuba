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
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script type="text/javascript">
        $(document).ready(function() {
            // use template from document for panel
            var tocPanel = $('div#toc-panel-template').clone()[0];
            tocPanel.id = 'toc-panel';

            $('div#toc-panel-template').remove();

            $('div.book').add('<a class="toc-btn" id="toc-btn" href="#">. . .</a>').appendTo(document.body);
            $('div.book').add(tocPanel).appendTo(document.body);

            var tocPanelContent = $('div.toc').clone();
            tocPanelContent[0].id = 'toc-panel-content';
            tocPanelContent.appendTo('#toc-panel');

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

            var searchBox = $('#toc-search-box')[0];
            var searchQuery = '';

            setInterval(function() {
                if (searchQuery != searchBox.value) {
                    searchQuery = searchBox.value;

                    if (!searchQuery) searchQuery = '';

                    $('#toc-panel-content a').each(function() {
                        if (searchQuery == '') {
                            $(this).removeClass('toc-search-result');
                            $(this).removeClass('toc-search-hidden');
                        } else {
                            if (this.innerText.toLowerCase().indexOf(searchQuery.toLowerCase()) >= 0) {
                                $(this).addClass('toc-search-result');
                                $(this).removeClass('toc-search-hidden');
                            } else {
                                $(this).addClass('toc-search-hidden');
                                $(this).removeClass('toc-search-result');
                            }
                        }
                    });

                    // trace path from root to node
                    if (searchQuery != '') {
                        $('#toc-panel-content a').each(function() {
                            if ($(this).hasClass('toc-search-result')) {
                                var parent = this.parentNode;
                                while (parent != tocPanel) {
                                    if (parent.tagName.toLowerCase() == 'dd')
                                        $(parent).prev('dt').find('a').removeClass('toc-search-hidden');

                                    parent = parent.parentNode;
                                }
                            }
                        });
                    }
                }
            }, 300);
        });
        </script>
    </xsl:variable>

    <xsl:template name="user.header.content">
        <div id="toc-panel-template" class="toc-panel">
            <div style="display: table; width:100%">
                <div class="toc-search" style="display: table-row">
                    <div class="toc-search-label" style="display: table-cell">Поиск</div>
                    <div style="display: table-cell">
                        <input id="toc-search-box" type="text" style="width: 100%"/>
                    </div>
                </div>
            </div>
        </div>

        <xsl:copy-of select="$headercode"/>
    </xsl:template>

    <xsl:output method="html"
            encoding="UTF-8"
            indent="yes"/>

</xsl:stylesheet>