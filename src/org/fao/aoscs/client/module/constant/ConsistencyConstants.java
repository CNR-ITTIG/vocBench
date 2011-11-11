package org.fao.aoscs.client.module.constant;


public class ConsistencyConstants {
	
	private static int typelist[] = new int[]{0, 1,1,2,1,1, 1,1,1,1,1, 2,1,1,3,1,  1, 1,1,1};
	private static String typeValue[] = new String[]{"","Concept","Term","Relationship"};
	public static final String noresult_list[]= new String[12];
	public static final String result_list[] = new String[7];
    public static final  String[] getNoResultConsistencyType()
	{
		noresult_list[0] =  "Check: Concepts (published, validated) with less or more than one main label in one particular language";
		noresult_list[1] =  "Check: Concepts (published, validated), which have the same main label in a particular language";
		noresult_list[2] =  "Check: Terms that are not associated to any concept(orphaned terms)";
		noresult_list[3] =  "Check: Concepts/Terms with no status";
		noresult_list[4] =  "Check: Children of deprecated concepts";
		noresult_list[5] =  "Check: Concepts that have no sub classes AND no other relationships";
		noresult_list[6] =  "Check: Concepts that are in the same hierarchy branch AND have additional semantic relationship(s)";
		noresult_list[7] =  "Check: Concepts which have the same ingoing and outgoing non-symmetric relationship";
		noresult_list[8] =  "Check: Concepts that have not been assigned their inverse relationship";
		noresult_list[9] =  "Check: Concepts (not deprecated) with all terms 'deprecated' or 'proposed deprecated'";
		noresult_list[10] = "Check: Terms that have no relationships to other terms";
		noresult_list[11] = "Check: Terms that have duplicate termcodes";
		return noresult_list;
	}

	public static final  String[] getResultConsistencyType()
	{
		result_list[0] = "List: Concepts with status 'deprecated' or 'proposed deprecated'";
		result_list[1] = "List: Unused relationships";
		result_list[2] = "List: Concepts (validated, published) without any definition";
		result_list[3] = "List: Terms that have exact same spelling in different languages";
		result_list[4] = "List: Terms that have no AGROVOC code";
		result_list[5] = "List: Terms that have duplicate termcodes";
		result_list[6] = "List: Concepts that have no term yet in a specified language";
		return result_list;
	}
	
	public static int getResultType(int selection)
	{
		return typelist[selection];
	}
	
	public static String getResultTypeValue(int selection)
	{
		return typeValue[getResultType(selection)];
	}
}
