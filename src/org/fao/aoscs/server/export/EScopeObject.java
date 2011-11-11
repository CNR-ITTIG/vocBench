package org.fao.aoscs.server.export;

public class EScopeObject {
    
    private String scopeid;
	private String scopedesc;
	private String languagecode;
	private String scopegrpid;
	
    public EScopeObject()
    {
        super();
    }
    public String getScopeid()
    {
        return scopeid;
    }
    public void setScopeid(String scopeid)
    {
        this.scopeid = scopeid;
    }
    public String getScopedesc()
    {
        return scopedesc;
    }
    public void setScopedesc(String scopedesc)
    {
        this.scopedesc = scopedesc;
    }
    public String getLanguagecode()
    {
        return languagecode;
    }
    public void setLanguagecode(String languagecode)
    {
        this.languagecode = languagecode;
    }
    public String getScopegrpid()
    {
        return scopegrpid;
    }
    public void setScopegrpid(String scopegrpid)
    {
        this.scopegrpid = scopegrpid;
    }
	
	
	
	
}
