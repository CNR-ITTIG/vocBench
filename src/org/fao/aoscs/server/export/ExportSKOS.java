package org.fao.aoscs.server.export;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationObject;
import org.fao.aoscs.domain.RelationshipObject;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

public class ExportSKOS {
	/**
	 * IMPORTANT: consider only descriptors if the non descriptors are related by 
	 * other relationships!
	 * leave out the deleted terms for all queries!
	 * 
	 * Last update of the code: 27/08/2007
	 *[?/
	 * numeric URI or label URI
	 */
	
	public static boolean DEBUG = false;
	public static String FAO_NS	= "http://www.fao.org/aims/aos/agrovoc";
//	public static String FAO_NS	= "http://www.fao.org/aos/agrovoc";
	public static String FAO_URI	= FAO_NS + "/c_";
	public static boolean FAO_NUM_URI = false;
	public static String FILENAME	= "ag_skos_070810.rdf";
	
	static String expStr = "";
	static EConceptObject cObj ;
	
	public static String ExportToSKOS(OntologyInfo ontoInfo,ArrayList<String> criterialist)
	{
		String datetype = (String) criterialist.get(1);
		String datestart = (String) criterialist.get(2);
		String dateend = (String) criterialist.get(3);
		String schemevar = (String) criterialist.get(4);
		//String subvocab  = (String) criterialist.get(5); 
		String termcode= (String) criterialist.get(6);
		String concepturi = (String) criterialist.get(0);
		String startcode = (String) criterialist.get(7);
		ArrayList<String> explang = new ArrayList<String>();
		
		if(criterialist.get(8) != null)
		{
			for(int i=8;i<criterialist.size();i++)
			{
				explang.add((String) criterialist.get(i)); 
			}			
		}
		
//********** what is subvocab *********
		AgrovocData agDat = new AgrovocData(ontoInfo) ; 
		AgrovocObject agObj = new AgrovocObject() ;
		agObj = agDat.getAgrovocObject();
		
		
		
		expStr = initialHeader();

		HashMap<String, EConceptObject> conceptList = agObj.getConceptList();
		Iterator<String> it = conceptList.keySet().iterator();
		int i =0;
		while(it.hasNext()){
			i=i+1;
			String conceptURI = (String)it.next();

			if(concepturi == null) concepturi = "";
			if(concepturi.equals(null)) concepturi = "";
			
			if(concepturi.equals(conceptURI) || concepturi==""){ // check concepturi criteria
				cObj =  agObj.getConceptObject(conceptURI);
				if(cObj != null) {
					String updatedate = cObj.getUpdateDate();
					String createdate = cObj.getCreateDate();
					ArrayList<String> schemelist = cObj.getScheme(); // cObj.getScheme();
//	**********update create start/end date 
					//--- check criteria		
					String datecheck ="" ;
					if(datetype.equals("update")){		datecheck = updatedate; 	}
					if(datetype.equals("create")){		datecheck = createdate; 	}
					
					try {
						if(comparedate(datecheck,datestart,dateend)){
				//--------- end date criteria ---------
							expStr = expStr + expScheme(schemelist,schemevar);	
							expStr = expStr +"<skos:Concept rdf:about=\""+conceptURI+"\">\n";

							//--------------- information ------------		
 							expStr = expStr + expInfo(termcode,startcode,explang);
						
							// scope note
							expStr = expStr + expScopeNote();
				// =========== parent section ======
							expStr = expStr + expParent();
				//	=========== child section ======================	
							expStr = expStr + expChild();
				//------------- relate section
							expStr = expStr + expRelation();
							expStr = expStr + "</skos:Concept>\n\n";
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}// if(cObj != null) {		
			} // if concepturi.equal
		}// while
		expStr = expStr + finalHeader();
		return expStr;
	} 
	
	/**
	 * @param agObj
	 * @return
	 */
	/**
	 * @param agObj
	 * @return
	 */
	public static String expParent()
	{
		ConceptObject mycObj = cObj.getParentConceptObject();
		String retStr = "";		
		
		String lblparent = mycObj.getUri();
		if(lblparent!=null)		retStr = retStr +"<skos:broader rdf:resource=\""+lblparent+"\"/>\n" ;
		
		return retStr;
	}
	
	public static String expChild()
	{
		String retStr = "";
		HashMap<String,String> chlist = cObj.getChild();
		if(chlist!=null){
			Iterator<String> itch = chlist.keySet().iterator();
			while(itch.hasNext()){
				String ch = (String) itch.next() ; 
				retStr = retStr +"<skos:narrower rdf:resource=\""+ch+"\"/>\n";
			} 
		}
		return retStr; // = retStr +"\n";
	}
	
	public static String expInfo(String termcriteria, String startcode, ArrayList<String> explang)
	{
		HashMap<String,ETermObject> termList = cObj.getTerm();
		Iterator<String> itterm = termList.keySet().iterator();	
		
		int j = 0; 
		String mlabel = ""; 			
		String nmlabel = ""; 		
		String tlabel ="";
		String tlang ="";				
		String retstr = "";
		
		while(itterm.hasNext()){
			j = j+1;
			String termcode = "";
			String termURI = (String) itterm.next();	
			if(termURI== null) termURI = "";
		//	if(termcriteria.equals(termURI) || termcriteria.isEmpty()){ // check termcode
				ETermObject tObj = cObj.getTermObject(termURI); //agObj.getTermObject(termuri);
		
				if(tObj != null){
			//		criteria
					if(startcode!=null){
						if(termcriteria!=null){
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEAGROVOC) 
								termcode = tObj.getCodeAGROVOC();
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEFAOPA) 
								termcode = tObj.getCodeFAOPA();
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASFISHERY3ALPHACODE) 
								termcode = tObj.getCodeFishery();
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEASC) 
								termcode = tObj.getCodeASC();
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEFAOTERM) 
								termcode = tObj.getCodeFAOTERM();
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEASFA) 
								termcode = tObj.getCodeASFA();
							if(startcode == ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODETAXONOMIC) 
								termcode = tObj.getCodeTaxonomic();	
						}
					}
			// end criteria					
					tlabel =tObj.getLabel();
					tlang =tObj.getLanguage().toLowerCase(); 
		//==========Check language Here xxxxxxxxxxxxxxxxxxxxxxxxxxxx		
				if(termcode==null) termcode = "";
					if(termcriteria==null) termcriteria = "";
					if(termcriteria!="")  // check criteria
					{
						if(termcode == termcriteria)
						{
							for(int i=0;i<explang.size();i++)
							{
								String selectlang = (String) explang.get(i);
								if(tlang.equals(selectlang)) // == (String) explang.get(i))
								{
									if(tObj.isMainLabel()){					
										mlabel = mlabel +"<skos:prefLabel xml:lang=\""+tlang+"\">\""+tlabel+"\"</skos:prefLabel>\n";
									}else{
										nmlabel = nmlabel +"<skos:altLabel xml:lang=\""+tlang+"\">\""+tlabel+"\"</skos:altLabel>\n"; 
									}
								}	
							}
						}
					}
					else
					{
						if(termcode == termcriteria)
						{
							for(int i=0;i<explang.size();i++)
							{
								String selectlang = (String) explang.get(i);
								if(tlang.equals(selectlang)) // == (String) explang.get(i))
								{
									if(tObj.isMainLabel()){					
										mlabel = mlabel +"<skos:prefLabel xml:lang=\""+tlang+"\">\""+tlabel+"\"</skos:prefLabel>\n";
									}else{
										nmlabel = nmlabel +"<skos:altLabel xml:lang=\""+tlang+"\">\""+tlabel+"\"</skos:altLabel>\n"; 
									}		
									
								}
							}
						}
					}
				}
		} // while */	
		retstr = retstr + mlabel + nmlabel;	
		return retstr;
	}
	
	public static String expRelation()
	{
		String retStr = "";
		RelationObject rObj = cObj.getConcetpRelation();
		ConceptObject mycObj = cObj.getParentConceptObject();
		String lblparent = mycObj.getUri() ;

		HashMap<RelationshipObject, ArrayList<ConceptObject>> rlst = rObj.getResult();
		
		if(rlst!=null){
			Iterator<RelationshipObject> itrelation = rlst.keySet().iterator();
			while(itrelation.hasNext()){
				ArrayList<ConceptObject> conlst = (ArrayList<ConceptObject>) rlst.get(itrelation.next());
				for(int i=0;i<conlst.size();i++){		
					ConceptObject rcObj = (ConceptObject) conlst.get(i);
					String lblrc = rcObj.getUri();
					if(!lblrc.equals(null)){
						if(!lblrc.equals(lblparent)) retStr = retStr +"<skos:related rdf:resource=\""+lblrc+"\"/>\n"; 
					}
				}
			} 
		}
		return retStr ; 
	}
	public static String expScopeNote()
	{
		String retStr = "";
		ArrayList<LabelObject> lstscopenote = cObj.getScopeNoteList();
		for(int i=0;i<lstscopenote.size();i++){
			LabelObject labelObj = lstscopenote.get(i);
			retStr = retStr +"<skos:scopeNote xml:lang=\""+labelObj.getLanguage()+"\">"+labelObj.getLabel()+"/skos:scopeNote>";
			i++;
		}
		String cobjupdate = cObj.getUpdateDate();
		
		String lastcmod =  "-" ; //The last modification for this concept was for the term in SK";
		retStr = retStr +	
				"<skos:changeNote rdf:parseType=\"Resource\">\n"+
				" <rdf:value>"+lastcmod +" </rdf:value>\n"+
				" <dc:date>"+cobjupdate+"</dc:date>\n"+
				"</skos:changeNote>\n";	
		return retStr;
		
	}
	public static String expScheme(ArrayList<String> schemelist, String schemecriteria)
	{
		String returnStr = "";
		for(int k=0;k<schemelist.size();k++){
			if(schemecriteria == null) schemecriteria = "";
			if(schemecriteria.equals(null)) schemecriteria = "";
			if(schemecriteria == schemelist.get(k) || schemecriteria=="")
			{ 
				returnStr = returnStr +
						"<skos:ConceptScheme rdf:about=\""+schemelist.get(k)+"\">\n"+
						"  <dc:title>AGROVOC</dc:title>\n"+
						"  <dc:description>FAO Multilingual Thesaurus.</dc:description>\n"+
						"  <dc:creator>\n"+
						"    <foaf:Organization>\n" +
						"       <foaf:name>FAO</foaf:name>\n" +
						"	 </foaf:Organization>\n"+
						"  </dc:creator>\n"+
						"  <dc:rights>Free to all for non commercial use.</dc:rights>\n"+
						"  <dcterms:issued>"+cObj.getCreateDate()+"</dcterms:issued>\n"+
						"  <dcterms:modified>"+cObj.getUpdateDate()+"</dcterms:modified>  \n"+
						"</skos:ConceptScheme>\n\n";
			} 
		} 	
		return returnStr;
	}
			
	public static String initialHeader()
	{
		return "" +
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+
				"<rdf:RDF \n"+
				"  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"+
				"  xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"\n"+
				"  xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n"+
				"  xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"+
				"  xmlns:dcterms=\"http://purl.org/dc/terms/\"\n"+
				"  xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n"+
				">\n";
	}
	public static String finalHeader()
	{
		return "</rdf:RDF>\n";
		
	}	
	
	
	public static String schemainformation(AgrovocObject agObj){
		// ----- no use
		
		return ""+
				"<skos:ConceptScheme rdf:about=\"http://www.fao.org/aims/aos/agrovoc\">\n"+
				"  <dc:title>AGROVOC</dc:title>\n"+
				"  <dc:description>FAO Multilingual Thesaurus.</dc:description>\n"+
				"  <dc:creator>\n"+
				"    <foaf:Organization>" +
				"		<foaf:name>FAO</foaf:name>" +
				"    </foaf:Organization>\n"+
				"  </dc:creator>\n"+
				"  <dc:rights>Free to all for non commercial use.</dc:rights>\n"+
				"  <dcterms:issued>Sat Nov 24 12:38:05 CET 2007</dcterms:issued>\n"+
				"  <dcterms:modified>2007-11-21 10:54:12.0</dcterms:modified>  \n"+
				"</skos:ConceptScheme>\n";
	}
	public static Boolean comparedate(String datec,String dates,String datef ) throws ParseException
	{
		Boolean returnvalue = false;
		DateFormat du = new SimpleDateFormat("dd/MM/yyyy"); // datestart = 02/04/2008
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // dbdate = 2006-09-14
		
		java.util.Date d1 = null;
		java.util.Date d3 ;
		if(datec==null) datec="";
		if(datef == null) datef = "";
		if(dates == null) dates = "";
		
		if(datec!= "") {
			d1 = df.parse(datec);			
			if(dates!= ""){
				java.util.Date d2 = du.parse(dates);
				if(datef!= ""){
					d3 = du.parse(datef);
					if(d1.after(d2) & d1.before(d3))	returnvalue = true;
				}else{
					if(d1.after(d2)) returnvalue = true; 
				}
			}else{ 
				if(datef!=null){ // no date start
					d3 = du.parse(datef);
					if(d1.before(d3))	returnvalue= true;
				}else{ // no date criteria
					returnvalue = true;
				}
			}
		}
		else			// --- if datec is empty
		{
			returnvalue = true; // if datevalue in db empty return export
		}
		return returnvalue;		
		
	}
} // end class

