<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Test Concept Tree Load</title>
<style>
body {
  color: black;
  margin: 0px;
  border: 0px;
  padding: 0px;
  background: #fff;
  direction: ltr;
}

body,table td,select {
  font-family: Arial, Arial Unicode MS, sans-serif, Tahoma, Trebuchet MS;
  font-size: 11px;
}

img {
  border: 0;
}

a,a:visited,a:hover {
  color: #0000AA;
  text-decoration: none;
  border-width: 0px;
  padding: 0px 0px 0px 0px;
  font-size: 11px;
  position: relative;
  
}
</style>
<script>
	function loadOntology()
	{
		document.getElementById('conceptTree').contentWindow.location.href='agrovoctest.jsp?ontology='+document.frm.ontology.value;
	}
</script>
</head>
<body rightmargin="0" bottommargin="0" marginwidth="0" leftmargin="0" marginheight="0" topmargin="0">
<form name='frm' method='post'>
	<table width='100%' cellspacing='0' cellpadding='0' border='1'>
		<tr>
			<td colspan=2>
				Ontology: <select name="ontology">
					<option value="" >Select ontology</option>
					<option value="blankmodel">Blank Model</option>
					<option value="agrovocwb_rice2_v_0_2_1_beta_dev">Rice</option>
					<option value="fullagrovoc_20090423_1421">Full agrovoc</option>
					<option value="testonenode">AGROVOC_TIME</option>
					<option value="agrovocwb_organism_v_0_1_beta_ton">AGROVOC_ORGANISM</option>
					<option value="agrovocwb_conferences_v_1_0">Conferences</option>
					<option value="agrovocwb_journals_v_1_0">Journals</option>
					<option value="agrovocwb_corporate_bodies_v_1_0">Corporate Body</option>
					<option value="agrovocwb_projects_v_1_0">Projects</option>
				</select>
				<input type=button name=submit onClick='javascript:loadOntology()' value='Go' />
			</td>
		</tr>
		<tr>
			<td width='50%'>
				<iframe id='conceptTree' width="100%" height="400">
				  <p>Your browser does not support iframes.</p>
				</iframe>
			</td>
			<td width='50%'>
				<iframe id='conceptDetail' width="100%" height="400">
				  <p>Your browser does not support iframes.</p>
				</iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
