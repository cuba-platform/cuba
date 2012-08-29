<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Data model description</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
</head>

<style>
    h1{
        font-size: 1.75em;
    }

    table {
        background-color:#eee;
    }

    table td {
        padding: 4px;
        background-color:white;
        vertical-align: top;
    }

    table .propertyName{
        font-weight: bold;
        padding-top: 10px;
    }
</style>

<#macro printView view>
    <ul>
        <#list view.properties as property>
                <li>${property.name} ${property.lazy}</li>
                <#if property.view ??>
                    <@printView view = property.view/>
                </#if>
            </#list>
    </ul>
</#macro>

<body style="margin: 40px;">

<h1>Domain model description</h1>

<h2>Available basic types:</h2>
<ul>
    <#list availableTypes as type>
        <li>${type}</li>
    </#list>
</ul>

<h2>Known entities:</h2>
<ul>
    <#list knownEntities as entity>
        <li><a href="#${entity.name}">${entity.name} - ${entity.description}</a></li>
    </#list>
</ul>


<#list knownEntities as entity>
    <a name="${entity.name}"></a>
    <h2>${entity.name}</h2>
    <p>Table: ${entity.tableName}</p>
    <#if entity.parent ??>
        ${entity.parent}
    </#if>
    <p>${entity.description}</p>
    <h3>Fields</h3>
    <table border="1" bordercolor="lightgray" cellspacing="0" cellpadding="0" width="90%">
        <col width="15%">
        <col width="20%">
        <col width="15%">
        <col width="25%">
        <col width="25%">
        <tr>
            <th>Property</th>
            <th>Column</th>
            <th>Type</th>
            <th>Description</th>
            <th>Cardinality</th>
            <th>Misc</th>
        </tr>
    <#list entity.properties as property>
        <#if property.persistent>
            <tr>
                        <td class="propertyName">${property.name}</td>
                        <td>${property.tableName}</td>
                        <td>${property.javaType} ${property.enum}</td>
                        <td>${property.description}</td>
                        <td><i>${property.cardinality} ${property.ordered} ${property.mandatory}. ${property.readOnly}.</i></td>
                        <td>
                            <#list property.annotations as ann>
                                ${ann}<br>
                            </#list>
                        </td>
            </tr>
        </#if>
    </#list>
    </table>

    <#if entity.views ??>
    <h3>Views</h3>
    <ul>
    <#list entity.views as aview>
          <li><b>${aview.name}</b></li>
           <@printView view=aview/>
    </#list>
    </ul>
    </#if>
    <p>&nbsp;</p>
</#list>

</body>
</html>
