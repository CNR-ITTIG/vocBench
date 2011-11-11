package org.fao.aoscs.server.export;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.server.RelationshipServiceImpl;
import org.fao.aoscs.server.protege.ProtegeModelFactory;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

public class ExportSQL {
    
    static HashMap<String,String> mapScheme = new HashMap<String,String>(); 
    static HashMap<String,String> mapScope = new HashMap<String,String>();
    
    
    
    public static String getExportSQL(ExportParameterObject exp, OntologyInfo ontoInfo)
    {
        
        mapScope.put("Acronym","AC");
        mapScope.put("Common name for animals","CA");
        mapScope.put("Common name for bacteria","CB");
        mapScope.put("Common name for fungi","CF");
        mapScope.put("Common name for plants","CP");
        mapScope.put("Common name for viruses","CV");
        mapScope.put("Taxonomic terms for animals","TA");
        mapScope.put("Taxonomic terms for bacteria","TB");
        mapScope.put("Taxonomic terms for fungi","TF");
        mapScope.put("Taxonomic terms for plants","TP");
        mapScope.put("Taxonomic terms for viruses","TV");
        mapScope.put("Geographical below country level","GL");
        mapScope.put("Geographical above country level","GG");
        mapScope.put("Geographical country level","GC");
        mapScope.put("Chemicals","CH");
        
        OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
        String expStr = "";        
        ArrayList<EConceptObject> conceptList = ExportUtility.convert2AgrovocObject(owlModel, exp);
        HashMap<String, ERelationshipObject> objectList = ExportUtility.getObjectPropertyTree(owlModel, ModelConstants.RWBOBJECTPROPERTY).getRelationshipList();
        HashMap<String, ERelationshipObject> dataList = ExportUtility.getDataTypePropertyTree(owlModel, ModelConstants.RWBDATATYPEPROPERTY).getRelationshipList();
        HashMap<String, LinkTypeObject> objectDump = dumpxxx(objectList, 0, owlModel, 100);
        HashMap<String, LinkTypeObject> dataDump = dumpxxx(dataList,dataList.size(), owlModel, objectDump.size()+100);
        
        expStr  = SQLConstants.SetupHeader();
        expStr += SQLConstants.GenerateCreateTableScript();
        
        expStr += dumpTermLink(owlModel, conceptList, objectDump);
        expStr += dumpLinktype(owlModel, conceptList, objectDump, dataDump);
        expStr += dumpLanguage();
        expStr += dumpAgrovocterm(conceptList, exp);              

        expStr += dumpCatschemes(owlModel, exp);
        expStr += dumpCategory(owlModel, exp);
        expStr += dumpMapping(owlModel);
        expStr += dumpTagtype(owlModel);
        expStr += dumpTermtag(owlModel, conceptList);
        expStr += dumpTermStatus();
        expStr += dumpMaintenancegroups();
        expStr += dumpScope(owlModel);
        expStr += SQLConstants.SetupFinalString();
        
        //owlModel.dispose();        
        return expStr;     
    }
    
    private static HashMap<String, LinkTypeObject> dumpxxx(HashMap<String, ERelationshipObject> list, int total, OWLModel owlModel, int index)
    {
        HashMap<String, LinkTypeObject> result = new HashMap<String, LinkTypeObject>(); 
        HashMap<String, Integer> relations = new HashMap<String, Integer>();
        ArrayList<ERelationshipObject> relationList = new ArrayList<ERelationshipObject>(list.values());
        for(int i=0;i<relationList.size();i++) 
        {
            ERelationshipObject robj = relationList.get(i);
            relations.put(robj.getName(), total+i+1);
        }
        
        Iterator<String> orItem = list.keySet().iterator();        
        while(orItem.hasNext())
        {                                            
            ERelationshipObject orObj = list.get(orItem.next());
            HashMap<String,String> labelList = orObj.getLabelList();
            Iterator<String> labelItem = labelList.keySet().iterator();            
            ArrayList<LabelObject> labelObj = new RelationshipServiceImpl().getRelationshipComments(orObj.getName(), owlModel);
            while(labelItem.hasNext())
            {
                LinkTypeObject lto = new LinkTypeObject();
                lto.setLanguagecode((String) labelItem.next());
                lto.setLinkdesc(ExportUtility.escapeChar(labelList.get(lto.getLanguagecode())));
                lto.setLinkabr(orObj.getName());
                lto.setLinkdescription(null);
                lto.setCreatedate("");
                lto.setRlinkcode(""+relations.get(orObj.getInversePropertyName()));
                lto.setLinklevel(orObj.getLinkLevel());
                lto.setParentlinktypeid(null);
               
                ArrayList<String> parents = orObj.getParent();
                String parentid = "";
                for(int i=0 ; i < labelObj.size() ; i++)
                {   if(((LabelObject)labelObj.get(i)).getLanguage()!=null)
                    {   if (((LabelObject)labelObj.get(i)).getLanguage().equals(lto.getLanguagecode()))
                        {                            
                            String linkdescription = labelObj.get(i).getLabel();
                            linkdescription = ExportUtility.escapeChar(linkdescription);
                            lto.setLinkdescription(linkdescription);
                            break;
                        }                        
                    }
                }
                for(int j=0; j<parents.size(); j++)
                {    
                    parentid = parents.get(j);
                    lto.setParentlinktypeid(parentid);
                    result.put(lto.getLinkabr(), lto);
                }
            }                           
        }
        java.util.Iterator<String> keyList = result.keySet().iterator();
        int k = 1;
        while(keyList.hasNext())
        {
            LinkTypeObject obj = result.get(keyList.next());
            obj.setLinktypeid(""+(index+k));
            k++;
        }
        return result;
    }
  
    public static String dumpTagtype(OWLModel owlModel)
    {          
        String temp =
            "--\n"+
            "-- Dumping data for table `tagtype`\n"+
            "--\n"+
            "/*!40000 ALTER TABLE `tagtype` DISABLE KEYS */;\n";
               
        String insert = "INSERT INTO `tagtype` (`tagtypeid`,`tagdesc`,`languagecode`) VALUES ";
        temp += insert+"\n";
        
        temp += "('10','Scope Note','EN'),\n";
        temp += "('20','History Note','EN'),\n";
        temp += "('30','Definition','EN'),\n";
        temp += "('40','Comments','EN')\n";
        
        ArrayList<ETagTypeObject> ttList = ExportUtility.concert2TagType(owlModel, 50);
        int i = 1;
        for(ETagTypeObject item : ttList)
        {
            temp += check600(i, insert) + "('"+item.getTagtypeid()+"','"+item.getTagdesc()+"','"+item.getLanguagecode().toUpperCase()+"')";
            i++;
        }
        temp += ";\n";
        temp += "\n/*!40000 ALTER TABLE `tagtype` ENABLE KEYS */;\n";
        return temp;
    }
    
    public static String dumpTermtag(OWLModel owlModel, ArrayList<EConceptObject> conceptList)
    {   
        String ret = "";
        ArrayList<ETermTagObject> ttList = ExportUtility.concept2Termtag(owlModel, conceptList);
        if(ttList.size() > 0)
        {
            ret =
                "--\n"+
                "-- Dumping data for table `termtag`\n"+
                "--\n"+
                "/*!40000 ALTER TABLE `termtag` DISABLE KEYS */;\n";
                   
            String insert = "INSERT INTO `termtag` (`termcode`,`languagecode`,`tagtypeid`,`tagtext`,`createdate`,`lastupdate`) VALUES ";
            ret += insert+"\n";
            
            //ConceptServiceImpl.getNonFuncValue(conceptObject.getName(), property, ontoInfo);
          //TODO if scopenote is of same language append all in one string with a separator
            int i = 1;
            for(ETermTagObject item : ttList)
            {
                ret += check600(i, insert) + "('"+item.getTermcode()+"','"+item.getLanguagecode()+"','"+item.getTagtypeid()+"','"+ ExportUtility.escapeChar(item.getTagtext())+"','"+item.getCreatedate()+"','"+item.getLastupdate()+"')";
                i++;
            }
            ret += ";\n";
            ret += "\n/*!40000 ALTER TABLE `termtag` ENABLE KEYS */;\n";
        }
        return ret;
    }
    
    public static String dumpMapping(OWLModel owlModel)
    {
        String temp = "";
        ArrayList<MappingObject> sList = ExportUtility.createMappingObject(owlModel);
        if(sList.size() > 0)
        {
            temp =
           
                "--\n"+
                "-- Dumping data for table `mapping`\n"+
                "--\n"+
                "/*!40000 ALTER TABLE `mapping` DISABLE KEYS */;\n";
                   
            String insert = "INSERT INTO `mapping` (`catcode`,`termcode`,`schemeid`,`parentcategoryid`) VALUES ";
            temp += insert+"\n";
    
            int i = 1;
            for(MappingObject item : sList)
            {
                temp += check600(i, insert) + "('"+item.getCatCode()+"','"+item.getTermCode()+"','"+item.getSchemeId()+"','"+item.getParentCategoryId()+"')";
                i++;
            }        
            temp += ";\n";
            temp += "/*!40000 ALTER TABLE `mapping` ENABLE KEYS */;\n";
        }
        return temp;
    }
    
    public static String dumpCatschemes(OWLModel owlModel, ExportParameterObject exp)
    {
        String temp = "";
        OWLNamedClass cls = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
       
        int i=1;
        int id = 1;
        if(cls.getInstanceCount(true) > 0)
        {
            String insert = 
                "--\n"+
                "-- Dumping data for table `catschemes`\n"+
                "--\n"+
                "/*!40000 ALTER TABLE `catschemes` DISABLE KEYS */;\n";
            
            insert += "INSERT INTO `catschemes` (`schemeid`,`maintenancegroupid`,`scheme`,`languagecode`,`active`) VALUES ";
            temp += insert+"\n";
            
            for (Iterator<?> iter = cls.getInstances(true).iterator(); iter.hasNext();) 
            {
                OWLIndividual element = (OWLIndividual) iter.next();
                for (Iterator<?> itt = element.getLabels().iterator(); itt.hasNext();) 
                {
                    RDFSLiteral label = (RDFSLiteral) itt.next();
                    String lang = label.getLanguage().toUpperCase();
                    String scheme = ExportUtility.escapeChar(label.getString());
                    temp += check600(i, insert) + "('"+(10+id)+"','10','"+scheme+"','"+lang+"','y')";
                    i++;
                    if(lang.equalsIgnoreCase("en"))
                        mapScheme.put(scheme, ""+id);
                }
                id++;                        
            }       
            temp += ";\n";
            temp += "/*!40000 ALTER TABLE `catschemes` ENABLE KEYS */;\n";
        }    
        return temp;
    }
    
    public static String dumpCategory(OWLModel owlModel, ExportParameterObject exp)
    {
        String temp = "";
        //ArrayList<EConceptObject> categoryList = ExportUtility.convert2AgrovocCat(owlModel, exp);\
        ArrayList<String[]> catList = ExportUtility.getCategory(owlModel, mapScheme);
        String insert = "";
        if(catList.size() > 0)
        {
            temp = 
                "--\n"+
                "-- Dumping data for table `categories`\n"+
                "--\n"+
                "/*!40000 ALTER TABLE `categories` DISABLE KEYS */;\n";
            
            insert = "INSERT INTO `categories` (`schemeid`,`categoryid`,`languagecode`,`categoryname`,`parentcategoryid`) VALUES ";
            temp += insert+"\n";
            
            int i=1;
            for (String[] cat : catList)         
            {            
                temp += check600(i, insert) + "('"+cat[0]+"','"+cat[1]+"','"+cat[2]+"','"+cat[3]+"',"+ExportUtility.checkNull(cat[4])+")";
                i++;
            }  
            temp += ";\n";
            temp += "/*!40000 ALTER TABLE `categories` ENABLE KEYS */;\n";
        }    
        return temp;
    }
    
    public static String dumpAgrovocterm(ArrayList<EConceptObject> conceptList, ExportParameterObject exp)
    {
        String temp = "";
        String insert = "";
        if(conceptList.size() > 0)
        {
           temp =
           "--\n" +
           "-- Dumping data for table `agrovocterm`\n" +
           "--\n"+        
           "/*!40000 ALTER TABLE `agrovocterm` DISABLE KEYS */;\n";
           
           insert = "INSERT INTO `agrovocterm` (`termcode`,`languagecode`,`termspell`,`statusid`,`createdate`,`frequencyiad`,`frequencycad`,`lastupdate`,`scopeid`,`idowner`,`termsense`,`termoffset`) VALUES \n";            
           temp += insert+"\n";
           
            int i=1;
            for (Iterator<EConceptObject> iter = conceptList.iterator(); iter.hasNext();)         
            {           
                EConceptObject concept = (EConceptObject) iter.next();
                String scopeId = ExportUtility.getScopeId(concept.getConceptName());
                HashMap<String,ETermObject> termList = concept.getTerm();
                Iterator<String> itterm = termList.keySet().iterator();            
                while(itterm.hasNext())
                {                                
                    ETermObject tObj = concept.getTermObject((String) itterm.next());
                    scopeId = tObj.isHasAcronymOf()? "AC" : scopeId; 
                    if(tObj != null && tObj.getCodeAGROVOC() != null)
                    {                                
                        String tcagrovoc =tObj.getCodeAGROVOC();
                        String lang = tObj.getLanguage().toUpperCase();
                        String termSpell = ExportUtility.escapeChar(tObj.getLabel());
                        String statusId = ExportUtility.getStatusId(tObj);
                        temp += check600(i, insert) + "('"+tcagrovoc+"','"+lang+"','"+termSpell+"','"+statusId+"','"+tObj.getCreateDate()+"',225,0,'"+tObj.getUpdateDate()+"',"+ExportUtility.checkNull(scopeId)+",NULL,NULL,NULL)";                    
                        i++;
                    }                                                
                }
            }    
            temp += ";\n" +
                    "/*!40000 ALTER TABLE `agrovocterm` ENABLE KEYS */;\n";
        }
        return temp;        
    }

    public static String dumpLinktype(OWLModel owlModel, ArrayList<EConceptObject> conceptList, HashMap<String, LinkTypeObject> objectDump, HashMap<String, LinkTypeObject> dataDump)
    {          
        String temp =
            "--\n"+
            "-- Dumping data for table `linktype`\n"+
            "--\n"+
            "/*!40000 ALTER TABLE `linktype` DISABLE KEYS */;\n";
               
        String insert = "INSERT INTO `linktype` (`linktypeid`,`languagecode`,`linkdesc`,`linkabr`,`linkdescription`,`createdate`,`rlinkcode`,`parentlinktypeid`,`linklevel`) VALUES ";
        temp += insert+"\n";
        temp += SQLConstants.dumpLinkTypeConstants();
        
        int k = 71;
        java.util.Iterator<String> descObjList = objectDump.keySet().iterator();   
        while(descObjList.hasNext())
        {
            String desc = (String) descObjList.next();
            LinkTypeObject obj = objectDump.get(desc);
            temp += check600(k, insert) + "('"+obj.getLinktypeid()+"','"+obj.getLanguagecode().toUpperCase()+"','"+obj.getLinkdesc()+"','"+obj.getLinkabr()+"','"+obj.getLinkdescription()+"','"+obj.getCreatedate()+"','"+obj.getRlinkcode()+"','"+obj.getParentlinktypeid()+"','"+obj.getLinklevel()+"')";
            k++;            
            
        }
        java.util.Iterator<String> descDataList = dataDump.keySet().iterator();   
        while(descDataList.hasNext())
        {
            String desc = (String) descDataList.next();
            LinkTypeObject obj = dataDump.get(desc);
            temp += check600(k, insert) + "('"+obj.getLinktypeid()+"','"+obj.getLanguagecode().toUpperCase()+"','"+obj.getLinkdesc()+"','"+obj.getLinkabr()+"','"+obj.getLinkdescription()+"','"+obj.getCreatedate()+"','"+obj.getRlinkcode()+"','"+obj.getParentlinktypeid()+"','"+obj.getLinklevel()+"')";
            k++;
        }
        
        /*if(parents.size() > 0)
        {   for(int j=0; j<parents.size(); j++)
            {                     
                parentid = parents.get(j);
                temp += check600(k, insert) + "('"+linkid+"','"+languagecode.toUpperCase()+"','"+label+"','"+linkabbr+"','"+definition+"','"+createdate+"','"+rlinkcode+"','"+parentid+"','"+linklevel+"')";
                k++;
            }
        }else
        {
            temp += check600(k, insert) + "('"+linkid+"','"+languagecode+"','"+label+"','"+linkabbr+"','"+definition+"','"+createdate+"','"+rlinkcode+"','"+parentid+"','"+linklevel+"')";
            k++;
        }
        linkid++;*/
        
        
        temp += ";\n";
        temp += "\n/*!40000 ALTER TABLE `linktype` ENABLE KEYS */;\n";
        return temp;
    }
    
    public static String dumpTermLink(OWLModel owlModel, ArrayList<EConceptObject> conceptList, HashMap<String, LinkTypeObject> objectDump ){
        String temp = "";
        String insert = "";
        if(conceptList.size() > 0)
        {
           temp =
           "--\n" +
           "-- Dumping data for table `termlink`\n" +
           "--\n"+        
           "/*!40000 ALTER TABLE `termlink` DISABLE KEYS */;\n";
           
           insert = "INSERT INTO `termlink` (`termcode1`,`termcode2`,`languagecode1`,`languagecode2`,`linktypeid`,`createdate`,`maintenancegroupid`,`newlinktypeid`,`confirm`,`technique`,`update`,`updmaintenancegroupid`) VALUES \n";
           temp += insert+"\n";
           
           //-----------------------
           int i = 1;
           for(EConceptObject cObj : conceptList)
           {
               
               if(cObj != null) 
               {
                   //RelationObject rObj = cObj.getConcetpRelation();
                   //HashMap<RelationshipObject, ArrayList<ConceptObject>> rlst = rObj.getResult();                        
                   
                   HashMap<String,ETermObject> termList = cObj.getTerm();
                   java.util.Iterator<String> itterm = termList.keySet().iterator();   
                   while(itterm.hasNext())
                   {
                       String termuri = (String) itterm.next();
                       
                       ETermObject tObj = cObj.getTermObject(termuri);
                       
                       ETermRelationshipObject tRel = ExportUtility.getTermRelationship(cObj.getConceptName(), ProtegeWorkbenchUtility.getNameFromURI(owlModel,tObj.getUri()), owlModel);
                       
                       HashMap<RelationshipObject, ArrayList<ETermObject>> tRelList = tRel.getResult();
                       
                       java.util.Iterator<RelationshipObject> tRelIte = tRelList.keySet().iterator();   
                       
                       while(tRelIte.hasNext())
                       {
                           RelationshipObject relObj = tRelIte.next();
                           ArrayList<ETermObject> tRelatedList = tRelList.get(relObj);
                           for(ETermObject tRelated : tRelatedList)
                           {
                               String termcode1 = tObj.getCodeAGROVOC();
                               String termcode2 = tRelated.getCodeAGROVOC();
                               String languagecode1 = tObj.getLanguage().toUpperCase();
                               String languagecode2 = tRelated.getLanguage().toUpperCase();
                               String linktypeid = objectDump.get(relObj.getName()).getLinktypeid();
                               String createDate = checkNull(tObj.getCreateDate());
                               String maintenancegroupid = "";
                               String newlinktypeid = "";
                               String confirm = "";
                               String technique = "";
                               String updateDate = checkNull(tObj.getUpdateDate());
                               String updmaintenancegroupid = "";
                               
                               String temp2 = "\t('"+termcode1+"','"+termcode2+"','"+languagecode1+"','"+languagecode2+"'," +
                               "'"+linktypeid+"','"+createDate+"','"+maintenancegroupid+"','"+newlinktypeid+"',"+
                               "'"+confirm+"','"+technique+"','"+updateDate+"'," + 
                               "'"+updmaintenancegroupid+"')";
                               
                               temp += check600(i, insert) + temp2;                    
                               i++;
                           }
                       }
                   }                           
               }
           }
           temp += ";\n" +
           "/*!40000 ALTER TABLE `termlink` ENABLE KEYS */;\n";
        }        
        return temp;
    }
    
    public static String dumpLanguage()
    {
        String sql = "SELECT language_code,language_note,local_language FROM language_code ORDER BY language_code";
        ArrayList<String[]> langlist = QueryFactory.getHibernateSQLQuery(sql);
        
        String groupid="0"; 
        int order =0 ; 
        String createdate = "";
        String lngstr = "";        
        if(langlist.size()>0)
        {
            lngstr= "--\n"+
                    "-- Dumping data for table `language`\n"+
                    "--\n"+
                    "/*!40000 ALTER TABLE `language` DISABLE KEYS */;\n" +
                    "INSERT INTO `language` (`languagecode`,`name`,`lnggroupid`,`originalname`,`lngorder`,`createdate`) VALUES \n";
            for(int i=0;i<langlist.size();i++)
            {
                String[] item = (String[]) langlist.get(i); 
                order = order +1;
                if(i!=0) lngstr = lngstr + ",\n";
                lngstr = lngstr + "\t('"+item[0].toUpperCase() +"','"+item[1]+ "','"+groupid + "','"+item[2]+ "','" + order + "','"+ createdate +  "')";          
            }    
            lngstr += ";\n" + 
                      "/*!40000 ALTER TABLE `language` ENABLE KEYS */;\n"; 
        }                        
        return lngstr;        
    }
        
    public static String dumpTermStatus(){
        String temp = "" +
         "--\n"+
         "-- Dumping data for table `termstatus`\n"+
         "--\n"+
         "/*!40000 ALTER TABLE `termstatus` DISABLE KEYS */;\n" +
         "INSERT INTO `termstatus` (`statusid`, `statusdesc`, `languagecode`) VALUES \n" + 
         "(0, 'Deleted Descriptor', 'AR'),  \n" +
         "(0, 'Deleted Descriptor', 'EN'), \n" +
         "(0, 'Descriptor Suprimido', 'ES'), \n" +
         "(0, 'Deleted Descriptor', 'FR'), \n" +
         "(0, 'Deleted Descriptor', 'ZH'), \n" +
         "(10, 'Descriptor without relations', 'AR'), \n" +
         "(10, 'Alternative descriptor (with sameAs relations)', 'EN'), \n" +
         "(10, 'Descriptor sin relaciones', 'ES'), \n" +
         "(10, 'Descriptor without relations', 'FR'), \n" +
         "(10, 'Descriptor without relations', 'ZH'), \n" +
         "(20, 'Descriptor with relations', 'AR'), \n" +
         "(20, 'Descriptor with relations', 'EN'), \n" +
         "(20, 'Descriptor con relaciones', 'ES'), \n" +
         "(20, 'Descriptor with relations', 'FR'), \n" +
         "(20, 'Descriptor with relations', 'ZH'), \n" +
         "(50, 'Descriptor without NT relations', 'AR'), \n" +
         "(50, 'Descriptor without NT relations', 'EN'), \n" +
         "(50, 'Descriptor sin relaciones NT', 'ES'), \n" +
         "(50, 'Descriptor without NT relations', 'FR'), \n" +
         "(50, 'Descriptor without NT relations', 'ZH'), \n" +
         "(60, 'Descriptor without BT relations', 'AR'), \n" +
         "(60, 'Descriptor without BT relations (Top Term)', 'EN'), \n" +
         "(60, 'Descriptor sin relaciones BT', 'ES'), \n" +
         "(60, 'Descriptor without BT relations', 'FR'), \n" +
         "(60, 'Descriptor without BT relations', 'ZH'), \n" +
         "(70, 'Non-Descriptor with USE relation', 'AR'), \n" +
         "(70, 'Non-Descriptor with USE relation', 'EN'), \n" +
         "(70, 'No-Descriptor con relación USE', 'ES'), \n" +
         "(70, 'Non-Descriptor with USE relation', 'FR'), \n" +
         "(70, 'Non-Descriptor with USE relation', 'ZH'), \n" +
         "(80, 'Non-Descriptor with SEE relation', 'AR'), \n" +
         "(80, 'Non-Descriptor with SEE relation', 'EN'), \n" +
         "(80, 'No-Descriptor con relación SEE', 'ES'), \n" +
         "(80, 'Non-Descriptor with SEE relation', 'FR'), \n" +
         "(80, 'Non-Descriptor with SEE relation', 'ZH'), \n" +
         "(90, 'Non-Descriptor without relations', 'AR'), \n" +
         "(90, 'Non-Descriptor without relations', 'EN'), \n" +
         "(90, 'No-Descriptor sin relaciones', 'ES'), \n" +
         "(90, 'Non-Descriptor without relations', 'FR'), \n" +
         "(90, 'Non-Descriptor without relations', 'ZH'), \n" +
         "(100, 'Proposed Descriptor', 'AR'), \n" +
         "(100, 'Proposed Descriptor', 'EN'), \n" +
         "(100, 'Descriptor Propuesto', 'ES'), \n" +
         "(100, 'Proposed Descriptor', 'FR'), \n" +
         "(100, 'Proposed Descriptor', 'ZH'), \n" +
         "(110, 'AGROVOC Version', 'AR'), \n" +
         "(110, 'AGROVOC Version', 'EN'), \n" +
         "(110, 'AGROVOC Edición', 'ES'), \n" +
         "(110, 'AGROVOC Version', 'FR'), \n" +
         "(110, 'AGROVOC Version', 'ZH'), \n" +
         "(120, 'Not accepted', 'AR'), \n" +
         "(120, 'Not accepted', 'EN'), \n" +
         "(120, 'Not accepted', 'ES'), \n" +
         "(120, 'Non accepte', 'FR'), \n" +
         "(120, 'Not accepted', 'ZH');" +
         "/*!40000 ALTER TABLE `termstatus` ENABLE KEYS */;\n";
        return temp;
    }
        
    public static String dumpMaintenancegroups(){
        
        String sql = "select users_groups_name,users_group_id,username,password " +
        " FROM users_groups,users,users_groups_map " +
        " Where users_groups_map.users_group_id = users_groups.users_groups_id " +
        " and users_groups_map.users_id = users.user_id";
        ArrayList<String[]> datalist = QueryFactory.getHibernateSQLQuery(sql);
        
        String temp = "";
        String insert = "";
        if(datalist.size()>0)
        {
            temp =
                "--\n" +
                "-- Dumping data for table `maintenancegroups`\n"+
                "--\n"+
                "/*!40000 ALTER TABLE `maintenancegroups` DISABLE KEYS */;\n";
                
                insert = "INSERT INTO `maintenancegroups` (`name`,`maintenancegroupid`,`login`,`password`) VALUES \n";
                temp += insert+"\n";
                
            
            int k = 1 ;
            for(int i=0;i<datalist.size();i++)
            {
                
                String[] item = (String[]) datalist.get(i); 
                String temp2 = "\t('"+item[0] +"','"+item[1]+ "','"+item[2] + "','"+item[3]+ "')";
                
                temp += check600(k, insert) + temp2;                    
                k++;
            }
            temp += ";\n" +
            "/*!40000 ALTER TABLE `maintenancegroups` ENABLE KEYS */;\n";
         }        
         return temp;
    }
    
    public static String dumpScope(OWLModel owlModel)
    {        
        String insert = " INSERT INTO `scope` (`scopeid`, `scopedesc`, `languagecode`, `scopegrpid`) VALUES \n";
        String temp = "" + 
        "-- Dumping data for table `scope`\n"+
        "--\n"+
        "/*!40000 ALTER TABLE `scope` DISABLE KEYS */;\n\n"
        + insert;
        /*" INSERT INTO `scope` (`scopeid`, `scopedesc`, `languagecode`, `scopegrpid`) VALUES \n" +
        " ('AC', 'Acronym', 'AR', 20), \n" +
        " ('AC', 'Acronym', 'EN', 20), \n" +
        " ('AC', 'Acronym', 'ES', 20), \n" +
        " ('AC', 'Acronym', 'FR', 20), \n" +
        " ('AC', 'Acronym', 'ZH', 20), \n" +
        " ('CA', 'Common name for animals', 'EN', 20), \n" +
        " ('CB', 'Common name for bacteria', 'EN', 20), \n" +
        " ('CF', 'Common name for fungi', 'EN', 20), \n" +
        " ('CH', 'مواد كيميائية', 'AR', 20), \n" +
        " ('CH', 'Chemicals', 'EN', 20), \n" +
        " ('CH', 'Productos químicos', 'ES', 20), \n" +
        " ('CH', 'Produits chimiques', 'FR', 20), \n" +
        " ('CH', '化学药品', 'ZH', 20), \n" +
        " ('CP', 'Common name for plants', 'EN', 20), \n" +
        " ('CV', 'Common name for viruses', 'EN', 20), \n" +
        " ('GC', 'مصطلح جغرافى (مستوى البلد)', 'AR', 10), \n" +
        " ('GC', 'Geographic Terms (country level)', 'EN', 10), \n" +
        " ('GC', 'Término geográfico (nivel del país)', 'ES', 10), \n" +
        " ('GC', 'Terme géographique (au niveau du pays)', 'FR', 10), \n" +
        " ('GC', '地名（国家级）', 'ZH', 10), \n" +
        " ('GG', 'مصطلح جغرافلى(فوق مستوى البلد)', 'AR', 10), \n" +
        " ('GG', 'Geographic Terms (above country level)', 'EN', 10), \n" +
        " ('GG', 'Término geográfico (por encima del nivel del país)', 'ES', 10), \n" +
        " ('GG', 'Terme géographique (niveau supérieur à celui du pays)', 'FR', 10), \n" +
        " ('GG', '地名（国家级以上）', 'ZH', 10), \n" +
        " ('GL', 'مصطلح جغرافى( دون مستوى البلد)', 'AR', 10), \n" +
        " ('GL', 'Geographic Terms (below country level)', 'EN', 10), \n" +
        " ('GL', 'Término geográfico (por debajo del nivel del país)', 'ES', 10), \n" +
        " ('GL', 'Terme géographique (niveau inférieur à celui du pays)', 'FR', 10), \n" +
        " ('GL', '地名（国家级以下）', 'ZH', 10), \n" +
        " ('TA', 'مصطلح تصنيفى(حيوانات)', 'AR', 100), \n" +
        " ('TA', 'Taxonomic Terms (animals)', 'EN', 100), \n" +
        " ('TA', 'Término taxonómico (animales)', 'ES', 100), \n" +
        " ('TA', 'Terme taxonomique (animaux)', 'FR', 100), \n" +
        " ('TA', '分类学名（动物）', 'ZH', 100), \n" +
        " ('TB', 'مصطلح تصنيفى( بكتيريا)', 'AR', 100), \n" +
        " ('TB', 'Taxonomic Terms (bacteria)', 'EN', 100), \n" +
        " ('TB', 'Término taxonómico (bacterias)', 'ES', 100), \n" +
        " ('TB', 'Terme taxonomique (bactéries)', 'FR', 100), \n" +
        " ('TB', '分类学名（细菌）', 'ZH', 100), \n" +
        " ('TF', 'مصطلح تصنيفى(فطر)', 'AR', 100), \n" +
        " ('TF', 'Taxonomic Terms (fungi)', 'EN', 100), \n" +
        " ('TF', 'Término taxonómico (hongos)', 'ES', 100), \n" +
        " ('TF', 'Terme taxonomique (champignons)', 'FR', 100), \n" +
        " ('TF', '分类学名（真菌）', 'ZH', 100), \n" +
        " ('TP', 'مصطلح تصنيفى(نبات)', 'AR', 100), \n" +
        " ('TP', 'Taxonomic Terms (plants)', 'EN', 100), \n" +
        " ('TP', 'Término taxonómico (plantas)', 'ES', 100), \n" +
        " ('TP', 'Terme taxonomique (végétaux)', 'FR', 100), \n" +
        " ('TP', '分类学名（植物）', 'ZH', 100), \n" +
        " ('TV', 'مصدلح تصنيفى(فيروسات)', 'AR', 10), \n" +
        " ('TV', 'Taxonomic Terms (viruses)', 'EN', 10), \n" +
        " ('TV', 'Término taxonómico (virus)', 'ES', 10), \n" +
        " ('TV', 'Terme taxonomique (virus)', 'FR', 10), \n" +
        " ('TV', '分类学名（病毒）', 'ZH', 10) \n";*/
        
        ArrayList<EScopeObject> list = ExportUtility.getScopeEntries(owlModel, mapScope);
        int i = 55;
        if(list.size() > 0)
        {
            for(EScopeObject so : list){           
                temp += check600(i, insert) + "('"+so.getScopeid()+"','"+so.getScopedesc()+"','"+so.getLanguagecode()+"','"+so.getScopegrpid()+"')";                    
                i++;
            }
        }
        temp += ";\n" +
        "/*!40000 ALTER TABLE `agrovocterm` ENABLE KEYS */;\n";
        return temp;        
    }
    
    public static Boolean comparedate(String datec,String dates,String datef ) throws ParseException
    {
        Boolean returnvalue = false;
        DateFormat du = new SimpleDateFormat("dd/MM/yyyy"); // datestart = 02/04/2008
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // dbdate = 2006-09-14
        java.util.Date d1 = null;
        java.util.Date d3 ;
        if(datec==null) datec="";
        if(dates==null) dates = "";
        if(datef==null) datef = "";
            
        if(datec!="") {
            d1 = df.parse(datec);
            if(dates!=""){
                java.util.Date d2 = du.parse(dates);
                if(datef!=""){
                    d3 = du.parse(datef);
                    if(d1.after(d2) & d1.before(d3))    returnvalue = true;
                }else{
                    if(d1.after(d2)) returnvalue = true; 
                }
            }else{ 
                if(datef!=""){ // no date start
                    d3 = du.parse(datef);
                    if(d1.before(d3))   returnvalue= true;
                }else{ // no date criteria
                    returnvalue = true;
                }
            }
        }
        else            // --- if datec is empty
        {
            returnvalue = true; // if datevalue in db empty return export
        }
        return returnvalue;     
        
    }
    
    
    public static String check600(int i, String insert)
    {
        String ret = "";
        if(i==1)
            ret = "";
        else 
        {
            if(i%600 == 0)
                ret = ";\n\n" + insert;
            else
                ret = ",\n";
        }
        return ret;
    }
    
    public static String checkNull(String value){
        if(value == null)
            return "";
        else return value;
    }

}
