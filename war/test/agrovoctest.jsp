<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*, org.fao.aoscs.domain.*, org.fao.aoscs.client.module.constant.ModelConstants, edu.stanford.smi.protegex.owl.model.*, edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral" %>
<%
	String ontology = (request.getParameter("ontology")!=null)?request.getParameter("ontology"):"";
	String concept = (request.getParameter("concept")!=null)?request.getParameter("concept"):"c_domain_concept";
	String term = (request.getParameter("term")!=null)?request.getParameter("term"):"";
	
	boolean showOnlyMainLabel = true;

	ArrayList<String> userSelectedLanguage = new ArrayList<String>();
	userSelectedLanguage.add("en");  
	userSelectedLanguage.add("la");
	userSelectedLanguage.add("fr");
	userSelectedLanguage.add("es");
	userSelectedLanguage.add("it");
	userSelectedLanguage.add("th");
%>

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
	function loadConceptTree(ontology, concept)
	{
		parent.document.getElementById('conceptTree').contentWindow.location.href='agrovoctest.jsp?ontology='+ontology+'&concept='+concept;
	}
	function loadConceptDetail(ontology, term)
	{
		parent.document.getElementById('conceptDetail').contentWindow.location.href='agrovoctest.jsp?ontology='+ontology+'&term='+term;
	}
</script>
</head>
<body rightmargin="0" bottommargin="0" marginwidth="0" leftmargin="0" marginheight="0" topmargin="0">
<%
if(!ontology.equals(""))
{
	String result = "";
	org.fao.aoscs.client.module.constant.ModelConstants.loadConstants(loadModelConstants());
	OntologyInfo ontoInfo = new OntologyInfo();
	ontoInfo.setDbDriver("com.mysql.jdbc.Driver");
	ontoInfo.setDbUrl("jdbc:mysql://158.108.33.132:3306/"+ontology+"?requireSSL=false&useUnicode=true&characterEncoding=UTF-8");			
	ontoInfo.setDbTableName(ontology);
	ontoInfo.setDbUsername("root");
	ontoInfo.setDbPassword("onmodao");

	try 
	{
		
		if(request.getParameter("term")==null)
		{
			Date d = new Date();
			OWLModel owlModel = getOWLModel(ontoInfo);
			ArrayList<TreeObject> ctObj = new ArrayList<TreeObject>();
			OWLNamedClass rootCls = owlModel.getOWLNamedClass(concept);
		    if(rootCls !=null){
				for(Iterator it = rootCls.getSubclasses(false).iterator(); it.hasNext();) {
					OWLNamedClass cls = (OWLNamedClass) it.next();
					TreeObject cObj = makeTreeObject(owlModel, cls, showOnlyMainlabel, false, userSelectedLanguage);
					ctObj.add(cObj);
				}
			}
		    owlModel.dispose();
		    out.println("<table><tr><td>Time elapsed to get data from server: </td><td>"+(new Date().getTime()-d.getTime())/(1000)+" secs"+"</td></tr></table>");
			
		    //Client side
			out.println("<table><tr><td>Total no. of concepts: </td><td>"+ctObj.size()+"</td></tr></table>");
			
			HashMap<String, TreeObject> cList = new HashMap<String, TreeObject>();
			for (Iterator<TreeObject> itr = ctObj.iterator(); itr.hasNext();)
			{ 
				TreeObject cObj = (TreeObject) itr.next();
				cList.put(cObj.getLabel(), cObj);
			}
			
			List<String> labelKeys = new ArrayList<String>(); 
			labelKeys.addAll(cList.keySet()); 
			Collections.sort(labelKeys);
			result +=  "<table>";
			for(Iterator it = labelKeys.iterator(); it.hasNext();) 
			{
					String label = (String) it.next();
					TreeObject cObj = (TreeObject) cList.get(label);
					result +=  "<tr><td width='10' align='center'>";
					if(cObj.isHasChild())
						result += "<a href=\"javascript:loadConceptTree('"+ontology+"','"+cObj.getName()+"');\">+</a>";
					result +=  "</td><td><a href=\"javascript:loadConceptDetail('"+ontology+"','"+cObj.getName()+"');\">"+convert2Widget(cObj, label, userSelectedLanguage, showOnlyMainLabel)+"</a></td></tr>";
			}
			result += "</table>";
			out.println(result);
			out.println("<table><tr><td>Time elapsed to render data: </td><td>"+(new Date().getTime()-d.getTime())/(1000)+" secs"+"</td></tr></table>");
			
		}
		else
		{
			Date d = new Date();
			OWLModel owlModel = getOWLModel(ontoInfo);
			ConceptTermObject ctObj = new ConceptTermObject();
			OWLNamedClass owlCls = owlModel.getOWLNamedClass(term);
			OWLIndividual individual = getConceptInstance(owlModel, owlCls);
	
			for (Iterator iter = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter.hasNext();) {
				OWLIndividual termInstance = (OWLIndividual) iter.next();
				Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
				
				TermObject termObject = new TermObject();
				termObject.setUri(termInstance.getURI());
				termObject.setName(termInstance.getName());		
				termObject.setConceptUri(owlCls.getURI());
				termObject.setConceptName(owlCls.getName());
				
				Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
				Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
				Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
				if(status !=null){
					termObject.setStatus(status.toString());
				}
				if(createDate != null && updateDate != null){
					try {
						termObject.setDateCreate(getDate(""+createDate));
						termObject.setDateModified(getDate(""+updateDate));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if(mainLabel != null){
					termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
				}
	    		Collection labelList = termInstance.getLabels();
	    		for (Iterator iterator = labelList.iterator(); iterator.hasNext();) {
	    			Object obj = iterator.next();
	    	    	if (obj instanceof DefaultRDFSLiteral) {
	    	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    	    		termObject.setLabel(element.getString());
	    	    		termObject.setLang(element.getLanguage().toLowerCase());
	    			}
				}
	    		ctObj.addTermList(termObject.getLang(), termObject);
			}
			owlModel.dispose();
			out.println("<table><tr><td>Time elapsed to get data from server: </td><td>"+(new Date().getTime()-d.getTime())/(1000)+" secs"+"</td></tr></table>");
			
			//Client side
			if(!ctObj.isEmpty()){
				HashMap termList = ctObj.getTermList();
				Iterator it = termList.keySet().iterator();
				result +=  "<table width='90%'cellspacing=1 cellpadding=1><tr><td width='100%' bgcolor='#D1D1D1'><table width='100%' ><tr style='color:#FFFFFF;background-color:#3C5E9E;'><td>Language</td><td>Term</td></tr>";
				while(it.hasNext()){
					final String language = (String) it.next();		
					result +=  "<tr><td bgcolor='#F4F4F4'>"+language+"</td><td bgcolor='#FFFFFF'><table>";
	
					ArrayList list = (ArrayList) termList.get(language);
					for (int i = 0; i < list.size(); i++) {
						TermObject tObj = (TermObject) list.get(i);
						result +=  "<tr><td>"+getTermColorByStatus(tObj)+"</td></tr>";		
					}
					result += "</table></td></tr>";			
				}
				result += "</table></td></tr></table>";		
			}else{
				result += "<img src='../images/label-not-found.gif'>";
			}
			out.println(result);
			out.println("<table><tr><td>Time elapsed to render data: </td><td>"+(new Date().getTime()-d.getTime())/(1000)+" secs"+"</td></tr></table>");
		}
	}
	catch(Exception e)
	{
		out.println("<br>Error: "+e.getMessage());
	}
}
	
%>


</body>
</html>
<%!
	
	/*String getTreeObjectLabel(TreeObject cObj, boolean showOnlyMainlabel, String searchText, ArrayList<String> langList)
	{
		String label = "";
		if(!cObj.getLabel().isEmpty()){
			Iterator<TreeLabelObject> it = cObj.getLabel().iterator();
			ArrayList<String> sortedList = new ArrayList<String>();
			HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
			while(it.hasNext()){
				TreeLabelObject tObj = (TreeLabelObject) it.next();
				if(showOnlyMainlabel){
					if(tObj.isMainLabel() && langList.contains(tObj.getLang())){
						if(searchText.toLowerCase().equals("")?true:tObj.getLabel().toLowerCase().contains(searchText.toLowerCase()))
						{
							sortedList.add(tObj.getLang()+"###"+tObj.getLabel());
						}
					}			
				}else{
					if(langList.contains(tObj.getLang())){
						if(searchText.toLowerCase().equals("")?true:tObj.getLabel().toLowerCase().contains(searchText.toLowerCase()))
						{
							sortedList.add(tObj.getLang()+"###"+tObj.getLabel());
							checkMainLabelList.put(tObj.getLang()+"###"+tObj.getLabel(), tObj.isMainLabel());
						}
						
					}			
				}
			}
			Collections.sort(sortedList);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
						label = label + "<b>"+ element[1] + " ("+element[0]+");</b> ";
					else
						label = label + element[1] + " ("+element[0]+"); ";
						
				}
			}
			
			if(label.length()==0)
				label = "###EMPTY###"+cObj.getName();
			
		}
		return label;
	}*/
	String getConceptLabel(ConceptObject cObj, boolean showOnlyMainlabel, String searchText, ArrayList<String> langList)
	{
		String label = "";
		if(!cObj.getTerm().isEmpty()){
			Iterator<String> it = cObj.getTerm().keySet().iterator();
			ArrayList<String> sortedList = new ArrayList<String>();
			HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
			while(it.hasNext()){
				String termIns = (String) it.next();
				TermObject tObj = (TermObject) cObj.getTerm().get(termIns);
				if(showOnlyMainlabel){
					if(tObj.isMainLabel() && langList.contains(tObj.getLang())){
						if(searchText.toLowerCase().equals("")?true:tObj.getLabel().toLowerCase().contains(searchText.toLowerCase()))
						{
							sortedList.add(tObj.getLang()+"###"+tObj.getLabel());
						}
					}			
				}else{
					if(langList.contains(tObj.getLang())){
						if(searchText.toLowerCase().equals("")?true:tObj.getLabel().toLowerCase().contains(searchText.toLowerCase()))
						{
							sortedList.add(tObj.getLang()+"###"+tObj.getLabel());
							checkMainLabelList.put(tObj.getLang()+"###"+tObj.getLabel(), tObj.isMainLabel());
						}
						
					}			
				}
			}
			Collections.sort(sortedList);
			
			for (int i = 0; i < sortedList.size(); i++) {
				String str =  (String) sortedList.get(i);
				String[] element = str.split("###");
				if(element.length==2){
					if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
						label = label + "<b>"+ element[1] + " ("+element[0]+");</b> ";
					else
						label = label + element[1] + " ("+element[0]+"); ";
						
				}
			}
			
			if(label.length()==0)
				label = "###EMPTY###"+cObj.getName();
			
		}
		return label;
	}

	String convert2Widget(TreeObject cObj, String label, ArrayList<String> langList,boolean showOnlyMainlabel)
	{

		String html = "";
		String conceptNameSpace = "http://www.fao.org/aims/aos/agrovoc#";
		if(cObj.getNameSpace().equals(ModelConstants.BASENAMESPACE)){
			html += "<img src='../images/concept_logo.gif'>";
		}else{
			html += "<img src='../images/category_logo.gif'>";
		}
			
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0)
		{
			html += "<img src='../images/label-not-found.gif'>";
		}
		else
		{
			if(label.length()>7)
			{
				if(label.substring(label.length()-2).equals("; "))
					label = label.substring(0,label.length()-2);
			}
			String status = cObj.getStatus();
			
			if(status!=null){
				if(status.equals("deprecated")){
					html += "<font color=\"black\">"+"<STRIKE>"+label+"</STRIKE>"+"</font>";
				}else if(status.equals("validated")){
					html += "<font color=\"#579250\">"+label+"</font>";
				}else if(status.equals("published")){
					html += "<font color=\"#1E1894\">"+label+"</font>";
				}else if(status.equals("proposed deprecated")){
					html += "<font color=\"#A7A7A7\">"+label+"</font>";
				}else if(status.equals("revised")){
					html += "<font color=\"brown\">"+label+"</font>";
				}else if(status.equals("proposed")){
					html += "<font color=\"#EA92DC\">"+label+"</font>";
				}else if(status.equals("proposed (guest)")){
					html += "<font color=\"#FFC294\">"+label+"</font>";
				}else if(status.equals("revised (guest)")){
					html += "<font color=\"brown\">"+label+"</font>";
				}
			}else{
				html += label;
			}
		}
		return html;
	}
	
	String convert2Widget(ConceptObject cObj, String label, ArrayList<String> langList,boolean showOnlyMainlabel)
	{

		String html = "";
		String conceptNameSpace = "http://www.fao.org/aims/aos/agrovoc#";
		if(cObj.getNameSpace().equals(conceptNameSpace)){
			html += "<img src='../images/concept_logo.gif'>";
		}else{
			html += "<img src='../images/category_logo.gif'>";
		}
			
		if(label.startsWith("###EMPTY###"))
			label = "";
		if(label.length()==0)
		{
			html += "<img src='../images/label-not-found.gif'>";
		}
		else
		{
			if(label.length()>7)
			{
				if(label.substring(label.length()-2).equals("; "))
					label = label.substring(0,label.length()-2);
			}
			String status = cObj.getStatus();
			
			if(status!=null){
				if(status.equals("deprecated")){
					html += "<font color=\"black\">"+"<STRIKE>"+label+"</STRIKE>"+"</font>";
				}else if(status.equals("validated")){
					html += "<font color=\"#579250\">"+label+"</font>";
				}else if(status.equals("published")){
					html += "<font color=\"#1E1894\">"+label+"</font>";
				}else if(status.equals("proposed deprecated")){
					html += "<font color=\"#A7A7A7\">"+label+"</font>";
				}else if(status.equals("revised")){
					html += "<font color=\"brown\">"+label+"</font>";
				}else if(status.equals("proposed")){
					html += "<font color=\"#EA92DC\">"+label+"</font>";
				}else if(status.equals("proposed (guest)")){
					html += "<font color=\"#FFC294\">"+label+"</font>";
				}else if(status.equals("revised (guest)")){
					html += "<font color=\"brown\">"+label+"</font>";
				}
			}else{
				html += label;
			}
		}
		return html;
	}

	HashMap<String, String> loadModelConstants()
	{
		HashMap<String, String> mcMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("org.fao.aoscs.server.ModelConstants");
		Enumeration<String> en = rb.getKeys();
		while(en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			mcMap.put(key, rb.getString(key));
		}
		org.fao.aoscs.client.module.constant.ModelConstants.loadConstants(mcMap);
		return mcMap;

	}
	
	TreeObject makeTreeObject(OWLModel owlModel, OWLNamedClass cls, boolean showOnlyMainlabel, boolean isHideDeprecated, ArrayList<String> langList){
		TreeObject treeObj = new TreeObject();
		OWLIndividual instance = getConceptInstance(owlModel, cls);
		if(instance != null)
		{
			treeObj.setInstance(instance.getURI());
			treeObj.setUri(cls.getURI());
			treeObj.setName(cls.getName());
			treeObj.setHasChild(cls.getSubclassCount()>0);
			Object status = instance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			if(status!=null)	treeObj.setStatus(status.toString());
			treeObj.setLabel(makeLabelObject(owlModel, instance, cls, showOnlyMainlabel, isHideDeprecated, langList));
		}	
		return treeObj;
	}
	
	String makeLabelObject(OWLModel owlModel, OWLIndividual instance, OWLNamedClass owlCls, boolean showOnlyMainlabel, boolean isHideDeprecated, ArrayList<String> langList){
		ArrayList<String> sortedList = new ArrayList<String>();
		HashMap<String, Boolean> checkMainLabelList = new HashMap<String, Boolean>();
		
		for (Iterator iter2 = instance.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter2.hasNext();) 
		{
			OWLIndividual termInstance = (OWLIndividual) iter2.next();
			
			Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			
			boolean isMainLabel = Boolean.valueOf(termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL)).toString());
			for (Iterator iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
		    	if (obj instanceof DefaultRDFSLiteral) {
		    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
		    		String label = element.getString();
		    		String lang = element.getLanguage().toLowerCase();
		    		if(showOnlyMainlabel){
						if(isMainLabel && langList.contains(lang) && checkDeprecated(status.toString(), isHideDeprecated) ){
							sortedList.add(lang+"###"+label);
						}			
					}else{
						if(langList.contains(lang) && checkDeprecated(status.toString(), isHideDeprecated)){
							sortedList.add(lang+"###"+label);
							checkMainLabelList.put(lang+"###"+label, isMainLabel);
						}			
					}
		    	}
			}
		}
		Collections.sort(sortedList);
		String termLabel = "";
		for (int i = 0; i < sortedList.size(); i++) {
			String str =  (String) sortedList.get(i);
			String[] element = str.split("###");
			String separator = "; ";
			if(i==(sortedList.size()-1))
				separator = "";
			if(element.length==2){
				if(checkMainLabelList.get(str) != null && checkMainLabelList.get(str))
				{
					termLabel = termLabel + "<b>"+ element[1] + " ("+element[0]+")"+separator+"</b>";
				}
				else
				{
					termLabel = termLabel + element[1] + " ("+element[0]+")"+separator;
				}
			}
		}
		if(termLabel.length()==0)
			termLabel = "###EMPTY###"+treeObject.getUri();
		
		return termLabel;
	}
	
	
	ConceptObject makeConceptObject(OWLModel owlModel,OWLNamedClass cls){
		OWLIndividual conceptIns = getConceptInstance(owlModel, cls);

		ConceptObject cObj = new ConceptObject();
		if(conceptIns != null)
		{
			cObj.setConceptInstance(conceptIns.getURI());	
			cObj.setName(cls.getName());
			cObj.setNameSpace(conceptIns.getNamespace());
			cObj.setUri(((OWLNamedClass)conceptIns.getProtegeType()).getURI());
			if(cls.getSubclassCount()>0){
	    		cObj.setHasChild(true);
	    	}else{
	    		cObj.setHasChild(false);
	    	}
	    	
			Object createDate = conceptIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
			Object updateDate = conceptIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
			try {
				if(createDate != null && updateDate !=null){
					cObj.setDateCreate(getDate(""+createDate));
					cObj.setDateModified(getDate(""+updateDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Object status = conceptIns.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
			if(status!=null){
				cObj.setStatus(status.toString());
				cObj.setStatusID(org.fao.aoscs.client.module.constant.OWLStatusConstants.getOWLStatusID(status.toString()));
			}
			
			for (Iterator iter2 = conceptIns.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION)).iterator(); iter2.hasNext();) {
				OWLIndividual termInstance = (OWLIndividual) iter2.next();
				TermObject termObject = makeTermObject(owlModel, termInstance, cls);
				cObj.addTerm(termObject.getUri(), termObject);
			}
		}	
		return cObj;
	}
	
	OWLIndividual getConceptInstance(OWLModel owlModel,OWLNamedClass owlCls){
		OWLIndividual individual = null;
		if(owlCls!=null){
			if(owlCls.getInstanceCount(false)>0){
				for (Iterator iter = owlCls.getInstances(false).iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof OWLIndividual) {
						individual = (OWLIndividual) obj;
					}
				}
			}
		}
		return individual;
	}
	
	TermObject makeTermObject(OWLModel owlModel,OWLIndividual termInstance,OWLNamedClass owlCls){
		TermObject termObject = new TermObject();
		termObject.setConceptUri(owlCls.getURI());
		termObject.setConceptName(owlCls.getName());
		termObject.setUri(termInstance.getURI());
		termObject.setName(termInstance.getName());
		
		
		
		Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
		Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
		try {
			if(createDate != null && updateDate !=null){
				termObject.setDateCreate(getDate(""+createDate));
				termObject.setDateModified(getDate(""+updateDate));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
		if(status!=null){
			termObject.setStatus(status.toString());
			termObject.setStatusID(org.fao.aoscs.client.module.constant.OWLStatusConstants.getOWLStatusID(status.toString()));
		}

		Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
		if(mainLabel != null){
			termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
		}
		
		for (Iterator iterator = termInstance.getLabels().iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
	    	if (obj instanceof DefaultRDFSLiteral) {
	    		DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
	    		termObject.setLabel(element.getString());
	    		termObject.setLang(element.getLanguage().toLowerCase());
			}
		}
		return termObject;
	}
	
	Date getDate(String date) 
	{
		if(!date.equals("null"))
		{
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return getDate(date, "EEE MMM d  HH:mm:ss z yyyy") ;
			}
		}
		else
			return null;
	}
	
	Date getDate(String date, String format) 
	{
		if(!date.equals("null"))
		{
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else
			return null;
	}
	
	OWLModel getOWLModel(OntologyInfo ontoInfo){
		edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory factory = new edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory();         
		Collection<Object> errors = new ArrayList<Object>();
		edu.stanford.smi.protege.model.Project prj = edu.stanford.smi.protege.model.Project.createNewProject(factory, errors);
		edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory.setSources(prj.getSources(), ontoInfo.getDbDriver(), ontoInfo.getDbUrl(), ontoInfo.getDbTableName(), ontoInfo.getDbUsername(), ontoInfo.getDbPassword());
		prj.createDomainKnowledgeBase(factory, errors, true);
		OWLModel owlModel = (OWLModel) prj.getKnowledgeBase();
		return owlModel;
	}
	
	String getTermColorByStatus(TermObject termObj){
		String item = new String();
		String status = termObj.getStatus();
		if(status!=null){
			if(status.equals("deprecated")){
				item = "<font color=\"black\"><STRIKE>"+termObj.getLabel()+"</STRIKE></font>";
			}else if(status.equals("validated")){
				item="<font color=\"#579250\">"+termObj.getLabel()+"</font>";
			}else if(status.equals("published")){
				item="<font color=\"#1E1894\">"+termObj.getLabel()+"</font>";
			}else if(status.equals("proposed deprecated")){
				item="<font color=\"#A7A7A7\">"+termObj.getLabel()+"</font>";
			}else if(status.equals("revised")){
				item="<font color=\"brown\">"+termObj.getLabel()+"</font>";
			}else if(status.equals("proposed")){
				item="<font color=\"#EA92DC\">"+termObj.getLabel()+"</font>";
			}else if(status.equals("proposed (guest)")){
				item="<font color=\"#FFC294\">"+termObj.getLabel()+"</font>";
			}else if(status.equals("revised (guest)")){
				item="<font color=\"brown\">"+termObj.getLabel()+"</font>";
			}
		}else{
			item = termObj.getLabel();
		}
		if(termObj.isMainLabel()){
			item = item + "&nbsp;&nbsp;(Preferred)"; 
		}
		return item;
	}
	

%>
