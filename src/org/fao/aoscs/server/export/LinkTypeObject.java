package org.fao.aoscs.server.export;

import net.sf.gilead.pojo.gwt.LightEntity;

public class LinkTypeObject extends LightEntity {
   
    private static final long serialVersionUID = 1L;
    public String linktypeid;
    public String languagecode;
    public String linkdesc;
    public String linkabr;
    public String linkdescription;
    public String createdate;
    public String rlinkcode;
    public String parentlinktypeid;
    public String linklevel;
    
    public String getLinktypeid()
    {
        return linktypeid;
    }
    public void setLinktypeid(String linktypeid)
    {
        this.linktypeid = linktypeid;
    }
    public String getLanguagecode()
    {
        return languagecode;
    }
    public void setLanguagecode(String languagecode)
    {
        this.languagecode = languagecode;
    }
    public String getLinkdesc()
    {
        return linkdesc;
    }
    public void setLinkdesc(String linkdesc)
    {
        this.linkdesc = linkdesc;
    }
    public String getLinkabr()
    {
        return linkabr;
    }
    public void setLinkabr(String linkabr)
    {
        this.linkabr = linkabr;
    }
    public String getLinkdescription()
    {
        return linkdescription;
    }
    public void setLinkdescription(String linkdescription)
    {
        this.linkdescription = linkdescription;
    }
    public String getCreatedate()
    {
        return createdate;
    }
    public void setCreatedate(String createdate)
    {
        this.createdate = createdate;
    }
    public String getRlinkcode()
    {
        return rlinkcode;
    }
    public void setRlinkcode(String rlinkcode)
    {
        this.rlinkcode = rlinkcode;
    }
    public String getParentlinktypeid()
    {
        return parentlinktypeid;
    }
    public void setParentlinktypeid(String parentlinktypeid)
    {
        this.parentlinktypeid = parentlinktypeid;
    }
    public String getLinklevel()
    {
        return linklevel;
    }
    public void setLinklevel(String linklevel)
    {
        this.linklevel = linklevel;
    }
    
    
}
