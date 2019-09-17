

<#list methodBean.annotates as annotate>
    ${annotate}
</#list>
    ${methodBean.modifier} ${methodBean.returnContent} ${methodBean.name}(<#list methodBean.params as paramBean><#include "Param.ftl"><#if paramBean_has_next>, </#if></#list>)<#if methodBean.exceptions?exists && (methodBean.exceptions?size > 0)> throws <#list methodBean.exceptions as exceptionName>${exceptionName}<#if exceptionName_has_next>, </#if></#list></#if>{
        ${methodBean.methodContent!''}
    }