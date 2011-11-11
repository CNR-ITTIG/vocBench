package org.fao.aoscs.server.export;

import java.util.HashMap;
import java.util.Set;

public class GenerateSKOS {
	
	public String generateHead(){
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
				"<rdf:RDF \n" +
				"\t xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n" +
				"\t xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\" \n" +
				"\t xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" \n" +
				"\t xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \n" +
				"\t xmlns:dcterms=\"http://purl.org/dc/terms/\" \n" +
				"\t xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" \n>\n";
	}
	
	public String generateHead(HashMap<String, String> namespaceList){
		String temp = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
				"<rdf:RDF \n" +
				"\t xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n" +
				"\t xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\" \n" +
				"\t xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" \n" +
				"\t xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \n" +
				"\t xmlns:dcterms=\"http://purl.org/dc/terms/\" \n" +
				"\t xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" \n";
				Set<String> prefixList = namespaceList.keySet();
				for(String prefix : prefixList){
					temp += "\t xmlns:"+prefix+"=\""+namespaceList.get(prefix)+"\" \n";
				}
				temp += ">\n";
				return temp;
	}	
	
	public String generateEnd(){
		return "</rdf:RDF>";
	}
	
	// The SKOS Concept Class
	public String generateHeadConcept(String uri){
		return "\t<skos:Concept rdf:about=\""+uri+"\">\n";
	}
	
	// The SKOS Concept Class
	public String generateTailConcept(){
		return  "\t</skos:Concept>\n";
	}
	
	// Concept Schemes #1
	public String generateConceptScheme(String termcode){
		return "\t\t<skos:ConceptScheme>"+termcode+"</skos:ConceptScheme>";
	}
	
	// Concept Schemes #2
	public String generateInScheme(String termcode){
		return "\t\t<skos:inScheme>"+termcode+"</skos:inScheme>";
	}
	
	// Concept Schemes #3
	public String generateHasTopConcept(String termcode){
		return "\t\t<skos:hasTopConcept>"+termcode+"</skos:hasTopConcept>";
	}
	
	// Concept Schemes #4
	public String generateTopConceptOf(String termcode){
		return "\t\t<skos:topConceptOf>"+termcode+"</skos:topConceptOf>";
	}
	
	// Lexical Labels #1
	public String generatealtLabel(String lang, String label){
		return "\t\t<skos:altLabel xml:lang=\""+lang+"\">"+label+"</skos:altLabel>\n";
	}
	
	// Lexical Labels #2
	public String generatehiddenLabel(String lang, String label){
		return "\t\t<skos:hiddenLabel xml:lang=\""+lang+"\">"+label+"</skos:hiddenLabel>\n";
	}
	
	// Lexical Labels #3
	public String generateprefLabel(String lang, String label){
		return "\t\t<skos:prefLabel xml:lang=\""+lang+"\">"+label+"</skos:prefLabel>\n";
	}
	
	// Notations
	public String generateNotation(String lang, String label){
		return "\t\t<skos:notation xml:lang=\""+lang+"\">"+label+"</skos:notation>\n";
	}
	
	// Documentation Properties #1
	public String generateChangeNoteHead(){
		return "\t\t<skos:changeNote rdf:parseType=\"Resource\">\n";
	}
	
	// Documentation Properties
	public String generateChangeNoteEnd(){
		return "\t\t</skos:changeNote>\n";
	}
	
	// Documentation Properties #2
	public String generateDefinition(String lang, String label){
		return "\t\t<skos:definition xml:lang="+lang+">"+label+"</skos:definition>\n";
	}
	
	// Documentation Properties #3
	public String generateEditorialNote(String lang, String label){
		return "\t\t<skos:editorialNote xml:lang="+lang+">"+label+"</skos:editorialNote>\n";
	}
	
	// Documentation Properties #4
	public String generateExample(String lang, String label){
		return "\t\t<skos:example xml:lang="+lang+">"+label+"</skos:example>\n";
	}
	
	// Documentation Properties #5
	public String generateHistoryNote(String lang, String label){
		return "\t\t<skos:historyNote xml:lang="+lang+">"+label+"</skos:historyNote>\n";
	}
	
	// Documentation Properties #6
	public String generateNote(String lang, String label){
		return "\t\t<skos:note xml:lang="+lang+">"+label+"</skos:note>\n";
	}
	
	// Documentation Properties #7
	public String generateScopeNote(String lang, String label){
		return "\t\t<skos:scopeNote xml:lang=\""+lang+"\">"+label+"</skos:scopeNote>\n";
	}
	
	// Semantic Relations #1
	public String generateBroader(String uri){
		return "\t\t<skos:broader rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Semantic Relations #2
	public String generateBroaderTransitive(String uri){
		return "\t\t<skos:broaderTransitive rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Semantic Relations #3
	public String generateNarrower(String uri){
		return "\t\t<skos:narrower rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Semantic Relations #4
	public String generateNarrowerTransitive(String uri){
		return "\t\t<skos:narrowerTransitive rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Semantic Relations #5
	public String generateRelated(String uri){
		return "\t\t<skos:related rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Semantic Relations #6
	public String generateSemanticRelation(String uri){
		return "\t\t<skos:semanticRelation rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Concept Collections #1
	public String generateCollection(String list){
		return "\t\t<skos:Collection rdf:resource=\""+list+"\"/>\n";
	}
	
	// Concept Collections #2
	public String generateOrderedCollection(String list){
		return "\t\t<skos:OrderedCollection rdf:resource=\""+list+"\"/>\n";
	}
	
	// Concept Collections #3
	public String generateMember(String list){
		return "\t\t<skos:member rdf:resource=\""+list+"\"/>\n";
	}
	
	// Concept Collections #4
	public String generateMemberList(String list){
		return "\t\t<skos:memberList rdf:resource=\""+list+"\"/>\n";
	}
	
	// Mapping Properties #1
	public String generateBroadMatch(String uri){
		return "\t\t<skos:broadMatch rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Mapping Properties #2
	public String generateCloseMatch(String uri){
		return "\t\t<skos:closeMatch rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Mapping Properties #3
	public String generateExactMatch(String uri){
		return "\t\t<skos:exactMatch rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Mapping Properties #4
	public String generateMappingRelation(String uri){
		return "\t\t<skos:mappingRelation rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Mapping Properties #5
	public String generateNarrowMatch(String uri){
		return "\t\t<skos:narrowMatch rdf:resource=\""+uri+"\"/>\n";
	}
	
	// Mapping Properties #6
	public String generateRelatedMatch(String uri){
		return "\t\t<skos:relatedMatch rdf:resource=\""+uri+"\"/>\n";
	}
	
	public String generateDate(String datetime){
		return "\t\t\t<dc:date>"+datetime+"</dc:date>\n";
	}
	
	public String generateRelationship(String relationship, String value){
		return "\t\t<"+relationship+">"+value+"</"+relationship+">\n";
	}
	
	public String generateRelationshipHeader(String relationship){
		return "\t\t<"+relationship+">\n";
	}
	
	public String generateRelationshipTail(String relationship){
		return "\t\t</"+relationship+">\n";
	}
	
	public String generateConceptScheme(String conceptScheme, String title, String description, String creatorName, String rights, String issued, String modified)
	{
		 
		String returnStr = "\t<skos:ConceptScheme rdf:about=\""+conceptScheme+"\">\n";
		
		if(title != null)
		{
			returnStr += "\t\t<dc:title>"+title+"</dc:title>\n";
		}
		if(description != null)
		{
			returnStr += "\t\t<dc:description>"+description+"</dc:description>\n";
		}
		if(creatorName != null)
		{
			returnStr += 
				"\t\t<dc:creator>\n"+
				"\t\t\t<foaf:Organization>\n" +
				"\t\t\t\t<foaf:name>"+creatorName+"</foaf:name>\n" +
				"\t\t\t</foaf:Organization>\n"+
				"\t\t</dc:creator>\n";
		}
		if(rights != null)
		{
			returnStr += "\t\t<dc:rights>"+rights+"</dc:rights>\n";
		}
		if(issued != null)
		{
			returnStr += "\t\t<dcterms:issued>"+issued+"</dcterms:issued>\n";
		}
		if(modified != null)
		{
			returnStr += "\t\t<dcterms:modified>"+modified+"</dcterms:modified>  \n";
		}
		returnStr += "\t</skos:ConceptScheme>\n\n";
		return returnStr;
	}
	
	public String generateConceptScheme()
	{
		
		String returnStr = 
		"\t<skos:ConceptScheme rdf:about=\"http://www.fao.org/aims/aos/agrovoc\">\n"+
		"\t\t<dc:title>AGROVOC</dc:title>\n"+
		"\t\t<dc:description>FAO Multilingual Thesaurus.</dc:description>\n"+
		"\t\t<dc:creator>\n"+
		"\t\t\t<foaf:Organization>\n" +
		"\t\t\t\t<foaf:name>FAO</foaf:name>\n" +
		"\t\t\t</foaf:Organization>\n"+
		"\t\t</dc:creator>\n"+
		"\t\t<dc:rights>Free to all for non commercial use.</dc:rights>\n"+
		//"\t\t<dcterms:issued></dcterms:issued>\n"+
		// "\t\t<dcterms:modified></dcterms:modified>\n"+  
		"\t</skos:ConceptScheme>\n\n";
		return returnStr;
	}
	
	
	
	
	
	public static void main(String args[]){
		
		GenerateSKOS g = new GenerateSKOS();
		String generate ="";
		generate +=g.generateHead();
		generate +=g.generateHeadConcept("imm1");
		generate +=g.generateprefLabel("lang1", "label1");
		generate +=g.generateprefLabel("lang2", "label2");
		generate +=g.generateTailConcept();
		generate +=g.generateHeadConcept("imm2");
		generate +=g.generateprefLabel("lang3", "label3");
		generate +=g.generateprefLabel("lang4", "label4");
		generate +=g.generateTailConcept();
		generate +=g.generateEnd();
		System.out.println(generate);
		
	}
}
