<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<body>
	<h1>Struts 2 注解示例</h1>

	<s:form action="Welcome">
		<s:textfield name="username" label="用户名" />
		<s:password name="password" label="密码" />
		<s:submit value="提交"/>
	</s:form>

</body>
</html>