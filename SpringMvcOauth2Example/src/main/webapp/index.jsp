<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>The jQuery Example</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

</head>
<body>
	<h1>Hello</h1>

	<div class="result">
		Videos: </br>
	</div>

	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							//document.write("Hello, World!");

							var AUTH_SERVER = "http://localhost:8080/SpringMvcOauth2Example/oauth/token?grant_type=password&username=mamuka&password=arabuli";
							var encoded = btoa("acme:acmesecret");

							var dat = {};

							$
									.ajax({

										type : 'POST',
										url : AUTH_SERVER,
										//data: PASSWORD_GRANT,,
										contentType : 'json',
										headers : {
											'Content-Type' : 'application/json',
											'Accept' : 'application/json',
											"Authorization" : "Basic "
													+ encoded
										},
										success : function(data) {
											console.log(data);
											var access_token = data.access_token;
											document.cookie = access_token;
											var url_req = "http://localhost:8080/SpringMvcOauth2Example/rest/videos?access_token="
													+ access_token;
											$
													.get(
															url_req,
															function(datas) {
																$
																		.each(
																				datas,
																				function(
																						i,
																						item) {
																					$(
																							".result")
																							.append(
																									i
																											+ 1
																											+ " - "
																											+ item.name
																											+ "</br>");
																				});
																console
																		.log(datas);
																dat = datas;
																var diva = $('.result'); //.append(datas);
																diva
																		.data(datas);
																alert("Load was performed.");
															});
											console.log(access_token);
										}
									});
							console.log(dat);
						});
	</script>
</body>
</html>
