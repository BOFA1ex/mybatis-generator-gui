package ${packageValue};

<#list imports as importName>
    import ${importName};
</#list>

<#list annotates as annotateName>
    ${annotateName}
</#list>
${modifier} class ${simplName}<#if superClass??> extends ${superClass}</#if><#if interfaceClasses?? && (interfaceClasses?size > 0) > implements <#list interfaceClasses as interfaceName>${interfaceName}<#if interfaceName_has_next>, </#if></#list></#if> {
<#list fields as fieldBean>
    <#include "Field.ftl">
</#list>
<#list methods as methodBean>
    <#include "Method.ftl">
</#list>

}