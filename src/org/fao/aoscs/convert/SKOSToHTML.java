package org.fao.aoscs.convert;

import java.util.StringTokenizer;

public class SKOSToHTML {
	public static void main(String[] args) {
	  String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?><rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"  xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" ><skos:Concept rdf:about=\"http://www.fao.org/aims/aos/agrovoc/c_338\"><skos:altLabel xml:lang=\"PL\">Amidek</skos:altLabel><skos:prefLabel xml:lang=\"JA\">アミド</skos:prefLabel><skos:prefLabel xml:lang=\"IT\">Amidi</skos:prefLabel><skos:prefLabel xml:lang=\"PL\">Amidy</skos:prefLabel><skos:prefLabel xml:lang=\"LO\">ເອໄມດ</skos:prefLabel><skos:prefLabel xml:lang=\"SK\">amidy</skos:prefLabel><skos:prefLabel xml:lang=\"PT\">Amida</skos:prefLabel><skos:prefLabel xml:lang=\"ZH\">酰胺</skos:prefLabel><skos:prefLabel xml:lang=\"TH\">อะไมด์</skos:prefLabel><skos:prefLabel xml:lang=\"CS\">amidy</skos:prefLabel><skos:prefLabel xml:lang=\"DE\">AMID</skos:prefLabel><skos:prefLabel xml:lang=\"EN\">Amides</skos:prefLabel><skos:prefLabel xml:lang=\"ES\">Amidas</skos:prefLabel><skos:prefLabel xml:lang=\"FA\">آمیدها</skos:prefLabel><skos:prefLabel xml:lang=\"FR\">   Amide    </skos:prefLabel><skos:prefLabel xml:lang=\"HI\">अमाईड्स</skos:prefLabel><skos:prefLabel xml:lang=\"HU\">amid</skos:prefLabel>     			<skos:prefLabel xml:lang=\"AR\">أميد       </skos:prefLabel><skos:prefLabel xml:lang=\"SS\"/></skos:Concept></rdf:RDF>";
	  System.out.println(formatXML(str));
  }

	public static String formatXML(String str)
	{
		String newstr = "";
		int level = 0;
		if(str.length()>0)
		{	
			StringTokenizer st = new StringTokenizer(str, "<");
			while(st.hasMoreTokens())
			{
				String line = ""+st.nextToken().trim();
				if(line.length()>0)
				{
					String tab = "";
					if(!line.startsWith("?") && !line.startsWith("/")) level++;
					for(int i=1;i<level;i++)	tab += "\t";
					newstr += "\n"+tab+"<"+line;
					if(!line.startsWith("?") && (line.endsWith("/>") || line.startsWith("/")))  level--;
				}
			}
		}
		return newstr;
	}
}
