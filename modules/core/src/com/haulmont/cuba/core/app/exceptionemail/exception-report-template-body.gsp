<html>
<body>
<p>${timestamp}</p>
<p>${toHtml(errorMessage)}</p>
<p>${toHtml(stacktrace)}</p>
<p>User login: ${user.getLogin()}</p>
<p>User message: ${toHtml(userMessage)}</p>
</body>
</html>