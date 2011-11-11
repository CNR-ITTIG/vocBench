package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SearchParameterObject extends LightEntity{

	private static final long serialVersionUID = -3557871012597270299L;

	public  static String CONTAIN = "CONTAIN";
	
	public  static String EXACT_MATCH = "EXACT_MATCH";
	
	public  static String START_WITH = "START_WITH";
	
	public  static String END_WITH = "END_WITH";
	
	public  static String EXACT_WORD = "EXACT_WORD";
	
	public  static String FUZZY_SEARCH = "FUZZY_SEARCH";
	
	private String regex = SearchParameterObject.CONTAIN;
	
	private String keyword = null;

	private boolean caseSensitive = false;
	
	private boolean includeNotes = false;
		
	private boolean onlyPreferredTerm = false;

	private String relationship = null;
	
	private String termCodeRepository = null;
	
	private String termCode = null;
	
	private String status = null;
	
	private String scheme = null;
	
	private HashMap<String,ArrayList<NonFuncObject>> conceptAttribute = new HashMap<String,ArrayList<NonFuncObject>>();
	private HashMap<String,ArrayList<NonFuncObject>> conceptNote = new HashMap<String,ArrayList<NonFuncObject>>();
	
	private HashMap<String,ArrayList<NonFuncObject>> termAttribute = new HashMap<String,ArrayList<NonFuncObject>>();
	private HashMap<String,ArrayList<NonFuncObject>> termNote = new HashMap<String,ArrayList<NonFuncObject>>();
	
	private ArrayList<String> selectedLangauge = new ArrayList<String>();
	
	public void setKeyword(String text){
		this.keyword = text;
	}
	
	public void setRegex(String reg){
		this.regex = reg;
	}
	
	public void setCaseSensitive(boolean logic){
		this.caseSensitive = logic;
	}
	
	public void setIncludeNotes(boolean logic){
		this.includeNotes = logic;
	}
	
	public void setRelationship(String relationshipURI){
		this.relationship = relationshipURI;
	}
	
	public void setTermCodeRepository(String repositoryURI){
		this.termCodeRepository = repositoryURI;
	}
	
	public void setTermCode(String code){
		this.termCode = code;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public void setScheme(String scheme){
		this.scheme = scheme;
	}
	
	public String getKeyword(){
		return this.keyword;
	}
	
	public String getRegex(){
		return this.regex;
	}
	
	public boolean getCaseSensitive(){
		return this.caseSensitive;
	}
	
	public boolean getIncludeNotes(){
		return this.includeNotes ;
	}

	public String getRelationship(){
		return this.relationship ;
	}
	
	public String getTermCodeRepository(){
		return this.termCodeRepository ;
	}
	
	public String getTermCode(){
		return this.termCode  ;
	}
	
	public String getStatus(){
		return this.status ;
	}
	
	public String getScheme(){
		return this.scheme ;
	}
	
	public boolean isKeywordEmpty(){
		if(this.keyword==null){
			return true;	
		}else{
			return false;
		}
	}
	
	public boolean isRelationshipEmpty(){
		if(this.relationship==null){
			return true;	
		}else{
			return false;
		}
	}
	
	public boolean isTermCodeRepositoryEmpty(){
		if(this.termCodeRepository==null){
			return true;	
		}else{
			return false;
		}
	}
	
	public boolean isTermCodeEmpty(){
		if(this.termCode==null){
			return true;	
		}else{
			return false;
		}
	}
	
	public boolean isStatusEmpty(){
		if(this.status==null){
			return true;	
		}else{
			return false;
		}
	}
	
	public boolean isSchemeEmpty(){
		if(this.scheme==null){
			return true;	
		}else{
			return false;
		}
	}
	
	public void clear(){
		
		this.regex = SearchParameterObject.CONTAIN;
		
		this.keyword = null;

		this.caseSensitive = false;
		
		this.includeNotes = false;
		
		this.onlyPreferredTerm = false;
		
		this.relationship = null;
		
		this.termCodeRepository = null;
		
		this.termCode = null;
		
		this.status = null;
		
		this.scheme = null;
		
		conceptAttribute = new HashMap<String,ArrayList<NonFuncObject>>();
		
		termAttribute = new HashMap<String,ArrayList<NonFuncObject>>();
		
		selectedLangauge = new ArrayList<String>();
	}

	public void setSelectedLangauge(ArrayList<String> selectedLangauge) {
		this.selectedLangauge = selectedLangauge;
	}

	public ArrayList<String> getSelectedLangauge() {
		return selectedLangauge;
	}
	
	public void addSelectedLanguage(String lc) {
		if(!selectedLangauge.contains(lc)){
			selectedLangauge.add(lc);
		}
	}
	
	public void removeSelectedLanguage(String lc) {
		if(selectedLangauge.contains(lc)){
			selectedLangauge.remove(lc);
		}
	}
	
	public void clearSelectedLanguage() {
			selectedLangauge.clear();
	}

    public boolean isOnlyPreferredTerm()
    {
        return onlyPreferredTerm;
    }

    public void setOnlyPreferredTerm(boolean onlyPreferredTerm)
    {
        this.onlyPreferredTerm = onlyPreferredTerm;
    }

    public HashMap<String, ArrayList<NonFuncObject>> getTermAttribute()
    {
        return termAttribute;
    }

    public void setTermAttribute(HashMap<String, ArrayList<NonFuncObject>> termAttribute)
    {
        this.termAttribute = termAttribute;
    }
    
    public boolean addTermAttribute(String rel, NonFuncObject obj)
    {
        return addProperties(this.termAttribute, rel, obj);
    }
    
    public boolean removeTermAttribute(String rel, NonFuncObject obj)
    {
    	return removeProperties(this.termAttribute, rel, obj);
    }
    
    /*public boolean addTermAttribute(String rel, NonFuncObject obj)
    {
        if(this.termAttribute.containsKey(rel))
        {
            ArrayList<NonFuncObject> objs = this.termAttribute.get(rel);
            
            for(NonFuncObject o : objs)
            {
                if(obj.getLanguage() == null)
                {
                    if(o.getValue().equals(obj.getValue()))
                    {
                        return false;
                    }
                }else
                {
                    if(o.getLanguage().equals(obj.getLanguage()) && o.getValue().equals(obj.getValue()))
                    {
                        return false;
                    }
                }
            }
            objs.add(obj);     
            return true;
        }else{
            ArrayList<NonFuncObject> objs = new ArrayList<NonFuncObject>();
            objs.add(obj);
            this.termAttribute.put(rel, objs);
            return true;
        }
    }
    
    public boolean removeTermAttribute(String rel, NonFuncObject obj){
        if(this.termAttribute.containsKey(rel))
        {
            ArrayList<NonFuncObject> objs = this.termAttribute.get(rel);
            ArrayList<NonFuncObject> objsClone = new ArrayList<NonFuncObject>(objs);
            try{
                for(int i =0; i<objsClone.size(); i++)
                {
                    NonFuncObject o = objsClone.get(i);
                    if(obj.getLanguage() != null)
                    {
                        if(o.getLanguage().equals(obj.getLanguage()) && o.getValue().equals(obj.getValue()))
                        {
                            objs.remove(i);
                        }
                    }else{
                        if(o.getValue().equals(obj.getValue()))
                        {
                            objs.remove(i);
                        }
                    }
                    i++;                
                }
                if(objs.size() == 0)
                    this.termAttribute.remove(rel);
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }else{            
            return false;
        }
        
    }*/
    
    public HashMap<String, ArrayList<NonFuncObject>> getConceptAttribute()
    {
        return conceptAttribute;
    }

    public void setConceptAttribute(HashMap<String, ArrayList<NonFuncObject>> conceptAttribute)
    {
        this.conceptAttribute = conceptAttribute;
    }
    
    public boolean addConceptAttribute(String rel, NonFuncObject obj)
    {
        return addProperties(conceptAttribute, rel, obj);
    }
    
    public boolean removeConceptAttribute(String rel, NonFuncObject obj)
    {
    	return removeProperties(conceptAttribute, rel, obj);
    }
    
    /*public boolean addConceptAttribute(String rel, NonFuncObject obj)
    {
        if(this.conceptAttribute.containsKey(rel))
        {
            ArrayList<NonFuncObject> objs = this.conceptAttribute.get(rel);
            
            for(NonFuncObject o : objs)
            {
                if(obj.getLanguage() == null)
                {
                    if(o.getValue().equals(obj.getValue()))
                    {
                        return false;
                    }
                }else
                {
                    if(o.getLanguage().equals(obj.getLanguage()) && o.getValue().equals(obj.getValue()))
                    {
                        return false;
                    }
                }
            }
            objs.add(obj);     
            return true;
        }else{
            ArrayList<NonFuncObject> objs = new ArrayList<NonFuncObject>();
            objs.add(obj);
            this.conceptAttribute.put(rel, objs);
            return true;
        }
    }
    
    public boolean removeConceptAttribute(String rel, NonFuncObject obj){
        if(this.conceptAttribute.containsKey(rel))
        {
            ArrayList<NonFuncObject> objs = this.conceptAttribute.get(rel);
            ArrayList<NonFuncObject> objsClone = new ArrayList<NonFuncObject>(objs);
            try{
                for(int i =0; i<objsClone.size(); i++)
                {
                    NonFuncObject o = objsClone.get(i);
                    if(obj.getLanguage() != null)
                    {
                        if(o.getLanguage().equals(obj.getLanguage()) && o.getValue().equals(obj.getValue()))
                        {
                            objs.remove(i);
                        }
                    }else{
                        if(o.getValue().equals(obj.getValue()))
                        {
                            objs.remove(i);
                        }
                    }
                    i++;                
                }
                if(objs.size() == 0)
                    this.conceptAttribute.remove(rel);
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }else{            
            return false;
        }
        
    }*/

    public HashMap<String, ArrayList<NonFuncObject>> getTermNote() {
		return termNote;
	}

	public void setTermNote(HashMap<String, ArrayList<NonFuncObject>> termNote) {
		this.termNote = termNote;
	}
	
	public boolean addTermNote(String rel, NonFuncObject obj)
    {
        return addProperties(this.termNote, rel, obj);
    }
    
    public boolean removeTermNote(String rel, NonFuncObject obj)
    {
    	return removeProperties(this.termNote, rel, obj);
    }
	
	public HashMap<String, ArrayList<NonFuncObject>> getConceptNote() {
		return conceptNote;
	}

	public void setConceptNote(HashMap<String, ArrayList<NonFuncObject>> conceptNote) {
		this.conceptNote = conceptNote;
	}
	
	public boolean addConceptNote(String rel, NonFuncObject obj)
    {
        return addProperties(this.conceptNote, rel, obj);
    }
    
    public boolean removeConceptNote(String rel, NonFuncObject obj)
    {
    	return removeProperties(this.conceptNote, rel, obj);
    }

	public boolean addProperties(HashMap<String,ArrayList<NonFuncObject>> hashMap, String rel, NonFuncObject obj)
    {
        if(hashMap.containsKey(rel))
        {
            ArrayList<NonFuncObject> objs = hashMap.get(rel);
            
            for(NonFuncObject o : objs)
            {
                if(obj.getLanguage() == null)
                {
                    if(o.getValue().equals(obj.getValue()))
                    {
                        return false;
                    }
                }else
                {
                    if(o.getLanguage().equals(obj.getLanguage()) && o.getValue().equals(obj.getValue()))
                    {
                        return false;
                    }
                }
            }
            objs.add(obj);     
            return true;
        }else{
            ArrayList<NonFuncObject> objs = new ArrayList<NonFuncObject>();
            objs.add(obj);
            hashMap.put(rel, objs);
            return true;
        }
    }
    
    public boolean removeProperties(HashMap<String,ArrayList<NonFuncObject>> hashMap, String rel, NonFuncObject obj){
        if(hashMap.containsKey(rel))
        {
            ArrayList<NonFuncObject> objs = hashMap.get(rel);
            ArrayList<NonFuncObject> objsClone = new ArrayList<NonFuncObject>(objs);
            try{
                for(int i =0; i<objsClone.size(); i++)
                {
                    NonFuncObject o = objsClone.get(i);
                    if(obj.getLanguage() != null)
                    {
                        if(o.getLanguage().equals(obj.getLanguage()) && o.getValue().equals(obj.getValue()))
                        {
                            objs.remove(i);
                        }
                    }else{
                        if(o.getValue().equals(obj.getValue()))
                        {
                            objs.remove(i);
                        }
                    }
                    i++;                
                }
                if(objs.size() == 0)
                	hashMap.remove(rel);
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }else{            
            return false;
        }
        
    }
	
}
