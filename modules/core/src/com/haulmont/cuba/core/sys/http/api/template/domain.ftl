<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Domain model description</title>
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

<�2>Known entities:</�2>
<ul>
    <#list knownEntities as entity>
        <li><a href="#${entity.name}">${entity.name}</a>
    </#list>
</ul>


<#list knownEntities as entity>
    <a name="${entity.name}"></a>
    <h2>${entity.name}</h2>
    ${entity.parent}
    <p>${entity.description}</p>
    <h3>Fields</h3>
    <table border="0" cellspacing="1" cellpadding="0" width="90%">
        <col width="15%">
        <col width="20%">
        <col width="15%">
        <col width="25%">
        <col width="25%">
    <#list entity.properties as property>
        <tr>
            <td class="propertyName">${property.name}</td>
            <td>${property.javaType} ${property.enum}</td>
            <td>${property.description}</td>
            <td><i>${property.cardinality} ${property.ordered} ${property.mandatory}. ${property.readOnly}.</i></td>
            <td>
                <#list property.annotations as ann>
                    ${ann}<br>
                </#list>
            </td>
        </tr>
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
