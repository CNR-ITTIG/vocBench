package org.fao.aoscs.client.module.constant;

import java.util.HashMap;

public class ModelConstants {
	
	public static String BASENAMESPACE;
	public static String COMMONBASENAMESPACE;
	public static String ONTOLOGYBASENAMESPACE;
	public static String ONTOLOGYBASENAMESPACEPREFIX;
	public static String NAMESPACESEPARATOR;
	public static String CDOMAINCONCEPT;
	public static String CLEXICALIZATION;
	public static String CDEFINITION;
	public static String CIMAGE;
	public static String CGEOGRAPHICCONCEPT;
	public static String CSCIENTIFICTERM;
	public static String CNOUN;
	public static String CCATEGORY;
	public static String OWLTHING;	
	public static String OWLNAMESPACE;
	public static String CCLASSIFICATIONSCHEME;
	
	public static String RWBOBJECTPROPERTY;
	public static String RWBDATATYPEPROPERTY;
	public static String RCONCEPTEDITORIALDATATYPEPROPERTY;
	public static String RCONCEPTDOMAINDATATYPEPROPERTY;
	public static String RTERMEDITORIALDATATYPEPROPERTY;
	public static String RTERMDOMAINDATATYPEPROPERTY;
	
	public static String RHASSTATUS;
	public static String RHASRELATEDCONCEPT;
	public static String RHASRELATEDTERM;
	public static String RHASLEXICALIZATION;
	public static String RISLEXICALIZATIONOF;
	public static String RHASDATECREATED;
	public static String RHASUPDATEDDATE;
	public static String RHASCODE;
	public static String RISMAINLABEL;
	public static String RHASDEFINITION;
	public static String RISDEFINITIONOF;
	public static String RHASCODEAGROVOC;
	public static String RHASCODEFAOPA;
	public static String RHASCODEASC;
	public static String RHASCODEASFA;
	public static String RHASCODETAXONOMIC;
	public static String RHASCODEFAOTERM;
	public static String RHASFISHERY3ALPHACODE;
	public static String RHASSPELLINGVARIANT;
	public static String RHASSCOPENOTE;
	public static String RHASEDITORIALNOTE;
	public static String RHASNOTE;
	public static String RHASCHANGENOTE;
	public static String RHASEXAMPLENOTE;
	public static String RHASHISTORYNOTE;
	public static String RHASDEFINITIONNOTE;
	public static String RHASCATEGORY;
	public static String RTAKENFROMSOURCE;
	public static String RHASSOURCELINK;
	public static String RHASIMAGE;
	public static String RISIMAGEOF;
	public static String RHASIMAGELINK;
	public static String RHASIMAGESOURCE;
	public static String RHASMAPPEDDOMAINCONCEPT;
	public static String RISSUBCATEGORYOF;
	public static String RHASSUBCATEGORY;
	public static String RBELONGSTOSCHEME;
	public static String RHAS;
	public static String RIS;
	public static String SUBCATEGORY;
	public static String SUBCATEGORYOF;
	public static String RHASTRANSLATION;
	public static String RISABBREVIATIONOF;
	public static String RHASACRONYM;
	public static String RHASSYNONYM;
	public static String RISREFERENCEDINANNOTAIONOF;
	public static String RTHESAURUSRELATIONSHIP;	
	public static String RSUBCLASSOF;
	public static String RHASSUBCLASS;
	public static String RISINSTANCEOF;
	public static String RHASINSTANCE;
	public static String RISSPATIALLYINCLUDEDIN;
	public static String RISPARTOF;
	public static String RISPUBLISHEDBY;
	public static String RHASNUMBER;
	public static String RHASDATE;
	public static String RISSPATIALLYINCLUDEDINCITY;
	public static String RISSPATIALLYINCLUDEDINSTATE;
	public static String RHASTERMTYPE;
	public static String RISPARTOFSUBVOCABULARY;

	public static String RHASISSN;
	public static String RHASISSNL;
	public static String RISHOLDBY;
	public static String RHASCALLNUMBER;
	public static String RFOLLOWS;
	public static String RPRECEDES;
	public static String RISOTHERLANGUAGEEDITIONOF;

	public static void loadConstants(HashMap<String, String> cMap)
	{
		BASENAMESPACE  			= (String)cMap.get("BASENAMESPACE");
		COMMONBASENAMESPACE  		= (String)cMap.get("COMMONBASENAMESPACE");
		ONTOLOGYBASENAMESPACE  	= (String)cMap.get("ONTOLOGYBASENAMESPACE");
		ONTOLOGYBASENAMESPACEPREFIX  	= (String)cMap.get("ONTOLOGYBASENAMESPACEPREFIX");
		NAMESPACESEPARATOR  	= (String)cMap.get("NAMESPACESEPARATOR");
		CDOMAINCONCEPT			= (String)cMap.get("CDOMAINCONCEPT");
		CLEXICALIZATION			= (String)cMap.get("CLEXICALIZATION");
		CDEFINITION				= (String)cMap.get("CDEFINITION");
		CIMAGE					= (String)cMap.get("CIMAGE");
		CGEOGRAPHICCONCEPT		= (String)cMap.get("CGEOGRAPHICCONCEPT");
		CSCIENTIFICTERM			= (String)cMap.get("CSCIENTIFICTERM");
		CNOUN					= (String)cMap.get("CNOUN");	
		CCATEGORY				= (String)cMap.get("CCATEGORY");
		OWLTHING				= (String)cMap.get("OWLTHING");
		OWLNAMESPACE			= (String)cMap.get("OWLNAMESPACE");
		CCLASSIFICATIONSCHEME	= (String)cMap.get("CCLASSIFICATIONSCHEME");	

		RWBOBJECTPROPERTY					= (String)cMap.get("RWBOBJECTPROPERTY");
		RWBDATATYPEPROPERTY  				= (String)cMap.get("RWBDATATYPEPROPERTY");
		RCONCEPTEDITORIALDATATYPEPROPERTY  	= (String)cMap.get("RCONCEPTEDITORIALDATATYPEPROPERTY");
		RCONCEPTDOMAINDATATYPEPROPERTY  	= (String)cMap.get("RCONCEPTDOMAINDATATYPEPROPERTY");
		RTERMEDITORIALDATATYPEPROPERTY  	= (String)cMap.get("RTERMEDITORIALDATATYPEPROPERTY");
		RTERMDOMAINDATATYPEPROPERTY  		= (String)cMap.get("RTERMDOMAINDATATYPEPROPERTY");
		
		RHASSTATUS  			= (String)cMap.get("RHASSTATUS");
		RHASRELATEDCONCEPT  	= (String)cMap.get("RHASRELATEDCONCEPT");
		RHASRELATEDTERM  		= (String)cMap.get("RHASRELATEDTERM");
		RHASLEXICALIZATION  	= (String)cMap.get("RHASLEXICALIZATION");
		RISLEXICALIZATIONOF		= (String)cMap.get("RISLEXICALIZATIONOF");
		RHASDATECREATED  		= (String)cMap.get("RHASDATECREATED");
		RHASUPDATEDDATE  		= (String)cMap.get("RHASUPDATEDDATE");
		RHASCODE				= (String)cMap.get("RHASCODE");
		RISMAINLABEL			= (String)cMap.get("RISMAINLABEL");
		RHASDEFINITION			= (String)cMap.get("RHASDEFINITION");
		RISDEFINITIONOF			= (String)cMap.get("RISDEFINITIONOF");
		RHASCODEAGROVOC			= (String)cMap.get("RHASCODEAGROVOC");
		RHASCODEFAOPA			= (String)cMap.get("RHASCODEFAOPA");
		RHASCODEASC				= (String)cMap.get("RHASCODEASC");
		RHASCODEASFA			= (String)cMap.get("RHASCODEASFA");
		RHASCODETAXONOMIC		= (String)cMap.get("RHASCODETAXONOMIC");
		RHASCODEFAOTERM			= (String)cMap.get("RHASCODEFAOTERM");
		RHASFISHERY3ALPHACODE	= (String)cMap.get("RHASFISHERY3ALPHACODE");
		RHASSPELLINGVARIANT		= (String)cMap.get("RHASSPELLINGVARIANT");
		RHASSCOPENOTE			= (String)cMap.get("RHASSCOPENOTE");
		RHASEDITORIALNOTE		= (String)cMap.get("RHASEDITORIALNOTE");
		RHASNOTE				= (String)cMap.get("RHASNOTE");
		RHASCHANGENOTE			= (String)cMap.get("RHASCHANGENOTE");
		RHASEXAMPLENOTE			= (String)cMap.get("RHASEXAMPLENOTE");
		RHASHISTORYNOTE			= (String)cMap.get("RHASHISTORYNOTE");
		RHASDEFINITIONNOTE		= (String)cMap.get("RHASDEFINITIONNOTE");
		RHASCATEGORY			= (String)cMap.get("RHASCATEGORY");
		RTAKENFROMSOURCE		= (String)cMap.get("RTAKENFROMSOURCE");
		RHASSOURCELINK			= (String)cMap.get("RHASSOURCELINK");
		RHASIMAGE				= (String)cMap.get("RHASIMAGE");
		RISIMAGEOF				= (String)cMap.get("RISIMAGEOF");
		RHASIMAGELINK			= (String)cMap.get("RHASIMAGELINK");
		RHASIMAGESOURCE			= (String)cMap.get("RHASIMAGESOURCE");
		RHASMAPPEDDOMAINCONCEPT	= (String)cMap.get("RHASMAPPEDDOMAINCONCEPT");
		RISSUBCATEGORYOF		= (String)cMap.get("RISSUBCATEGORYOF");
		RHASSUBCATEGORY			= (String)cMap.get("RHASSUBCATEGORY");
		RBELONGSTOSCHEME		= (String)cMap.get("RBELONGSTOSCHEME");
		RHAS					= (String)cMap.get("RHAS");
		RIS						= (String)cMap.get("RIS");
		SUBCATEGORY				= (String)cMap.get("SUBCATEGORY");
		SUBCATEGORYOF			= (String)cMap.get("SUBCATEGORYOF");
		RHASTRANSLATION         = (String)cMap.get("RHASTRANSLATION");
		RISABBREVIATIONOF       = (String)cMap.get("RISABBREVIATIONOF");
		RHASACRONYM             = (String)cMap.get("RHASACRONYM");
		RHASSYNONYM             = (String)cMap.get("RHASSYNONYM");
		RISREFERENCEDINANNOTAIONOF = (String)cMap.get("RISREFERENCEDINANNOTAIONOF");
		RTHESAURUSRELATIONSHIP = (String)cMap.get("RTHESAURUSRELATIONSHIP");		
		RSUBCLASSOF             = (String)cMap.get("RSUBCLASSOF");
	    RHASSUBCLASS            = (String)cMap.get("RHASSUBCLASS");
	    RISINSTANCEOF           = (String)cMap.get("RISINSTANCEOF"); 
	    RHASINSTANCE            = (String)cMap.get("RHASINSTANCE");
	    RISSPATIALLYINCLUDEDIN  = (String)cMap.get("RISSPATIALLYINCLUDEDIN");
	    RISPARTOF  				= (String)cMap.get("RISPARTOF");
	    RISPUBLISHEDBY			= (String)cMap.get("RISPUBLISHEDBY");
	    RHASNUMBER				= (String)cMap.get("RHASNUMBER");
		RHASDATE				= (String)cMap.get("RHASDATE");
		RISSPATIALLYINCLUDEDINCITY	= (String)cMap.get("RISSPATIALLYINCLUDEDINCITY");
		RISSPATIALLYINCLUDEDINSTATE	= (String)cMap.get("RISSPATIALLYINCLUDEDINSTATE");
	    RHASTERMTYPE	            = (String)cMap.get("RHASTERMTYPE");
	    RISPARTOFSUBVOCABULARY		= (String)cMap.get("RISPARTOFSUBVOCABULARY");
	    
	    RHASISSN					= (String)cMap.get("RHASISSN");
	    RHASISSNL					= (String)cMap.get("RHASISSNL");
	    RISHOLDBY					= (String)cMap.get("RISHOLDBY");
	    RHASCALLNUMBER				= (String)cMap.get("RHASCALLNUMBER");
	    RFOLLOWS					= (String)cMap.get("RFOLLOWS");
	    RPRECEDES					= (String)cMap.get("RPRECEDES");
	    RISOTHERLANGUAGEEDITIONOF	= (String)cMap.get("RISOTHERLANGUAGEEDITIONOF");
		
	}
}