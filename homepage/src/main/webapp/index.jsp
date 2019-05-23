<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<input type="button" value="회원가입" onclick="location.href='./member/memberJoinForm.do'">
	<c:if test="${ member == null }">
		<input type="button" value="로그인" onclick="location.href='./member/login_form.do'">
	</c:if>
	<c:if test="${member !=null }">
		<input type="button" value="로그아웃" onclick="">
	</c:if>	
</body>
</html>