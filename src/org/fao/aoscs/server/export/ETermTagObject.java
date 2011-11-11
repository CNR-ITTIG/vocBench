package org.fao.aoscs.server.export;

public class ETermTagObject {
    private String termcode;
    private String languagecode;
    private String tagtypeid;
    private String tagtext;
    private String createdate;
    private String lastupdate;
    public String getCreatedate()
    {
        return createdate;
    }
    public void setCreatedate(String createdate)
    {
        this.createdate = createdate;
    }
    public String getTermcode()
    {
        return termcode;
    }
    public void setTermcode(String termcode)
    {
        this.termcode = termcode;
    }
    public String getLanguagecode()
    {
        return languagecode;
    }
    public void setLanguagecode(String languagecode)
    {
        this.languagecode = languagecode;
    }
    public String getTagtypeid()
    {
        return tagtypeid;
    }
    public void setTagtypeid(String tagtypeid)
    {
        this.tagtypeid = tagtypeid;
    }
    public String getTagtext()
    {
        return tagtext;
    }
    public void setTagtext(String tagtext)
    {
        this.tagtext = tagtext;
    }
    public String getLastupdate()
    {
        return lastupdate;
    }
    public void setLastupdate(String lastupdate)
    {
        this.lastupdate = lastupdate;
    }
}
