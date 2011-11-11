package org.fao.aoscs.server.export;

public class ETagTypeObject {
    
    private String tagtypeid;
    private String tagdesc;
    private String languagecode;
    public String getTagtypeid()
    {
        return tagtypeid;
    }
    public void setTagtypeid(String tagtypeid)
    {
        this.tagtypeid = tagtypeid;
    }
    public String getTagdesc()
    {
        return tagdesc;
    }
    public void setTagdesc(String tagdesc)
    {
        this.tagdesc = tagdesc;
    }
    public String getLanguagecode()
    {
        return languagecode;
    }
    public void setLanguagecode(String languagecode)
    {
        this.languagecode = languagecode;
    }

}
