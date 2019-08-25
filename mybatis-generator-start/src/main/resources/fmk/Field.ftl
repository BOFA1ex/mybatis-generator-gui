
<#list fieldBean.annotates as annotate>
	${annotate}
</#list>
	${fieldBean.modifier} ${fieldBean.type} ${fieldBean.name};
	