<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="${pageContext.request.contextPath}/css/jquery-1.12.3.min.js">
	$(document).ready(function() {
		

		});

</script>


<style>
h4 {
	color: red;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Members of Admission Committee Login</title>
</head>
<body>

<c:if test="${sessionScope.logmac==false}">

	<form  id="macform" action="macLogin.obj" method="POST">
		<h3>Enter your user name and password</h3>

		Username<input type="text" name="username" required><br>
		Password<input type="password" name="pwd" required><br>
		<button type="submit" id="sub">submit</button>
	</form>
</c:if>

	<c:if test="${logsuccess==true}">
		<script>
			$(hideform);
		</script>
	</c:if>
	<c:if test="${credentials==false}">


		<h4>The entered credentials are incorrect.Please try again</h4>
	</c:if>

	<c:if test="${credentials==true}">
		<a href="viewapplications.obj?viewvalue=0">View applications for a
			specific program</a>
		<br>
		<c:if test="${value==1}">
			<form action="viewapplications.obj">
				<input type="hidden" name="viewvalue" value="1"> Enter the
				program name to check applications<input type="text" name="name"
					required>
				<button type="submit">Submit</button>
			</form>
		</c:if>

		<center>

			<c:if test="${list ne null}">
				<table width="700" align="center" border="1" bgcolor="#CCCCCC">
					<tr>

						<th>Application Id</th>
						<th>Full Name</th>
						<th>Scheduled Program ID</th>
						<th>Highest Qualification</th>
						<th>Date of Birth</th>
						<th>Marks Obtained</th>
						<th>Date of Interview</th>
						<th>Email Id</th>
						<th>Address</th>
						<th>Goals</th>
						<th>Status</th>
					</tr>
					<c:forEach var="applicant" items="${list}">
						<tr>
							<td>${applicant.application_Id}</td>
							<td>${applicant.full_Name}</td>
							<td>${applicant.scheduled_Program_Id}</td>
							<td>${applicant.highest_Qualification}</td>
							<td>${applicant.date_Of_Birth}</td>
							<td>${applicant.marks_Obtained}</td>
							<td>${applicant.date_Of_Interview}</td>
							<td>${applicant.email_Id}</td>
							<td>${applicant.address}</td>
							<td>${applicant.goals}</td>
							<td>${applicant.status}</td>

						</tr>
					</c:forEach>
				</table>
			</c:if>
		</center>
		<br />








		<a href="acceptapplication.obj?viewvalue=2">Accept an application</a>
		<br>
		<c:if test="${value==3}">
			<form action="acceptapplication.obj">
				<input type="hidden" name="viewvalue" value="3"> Enter the
				applicant ID to accept<input type="text" name="appid" required><br>
				Enter the interview date<input type="date" name="intdate" required><br>
				<button type="submit">Submit</button>
			</form>
		</c:if>

		<c:if test="${msg ne null}">
			<h3>${msg}</h3>
		</c:if>



		<a href="rejectapplication.obj?viewvalue=3">Reject an application</a>
		<br>
		<c:if test="${value==4}">
			<form action="rejectapplication.obj">
				<input type="hidden" name="viewvalue" value="4"> Enter the
				applicant ID to accept<input type="text" name="rejid" required><br>

				<button type="submit">Submit</button>
			</form>
		</c:if>
		<c:if test="${rejmsg ne null}">
			<h3>${rejmsg}</h3>
		</c:if>








		<a href="confirmstatus.obj?viewvalue=4">Confirm the status of an
			applicant post interview</a>
		<br>
		<c:if test="${value==5}">
			<form action="confirmstatus.obj">
				<input type="hidden" name="viewvalue" value="5"> Enter the
				applicant ID <input type="text" name="statid" required><br>

				<button type="submit">Submit</button>
			</form>
		</c:if>
		<c:if test="${statmsg ne null}">
			<h3>${statmsg}</h3>
		</c:if>






	</c:if>

<a href="logoutmac.obj">Logout</a><br>
	<a href="showHomePage.obj">Return to main menu</a>
</body>


</html>