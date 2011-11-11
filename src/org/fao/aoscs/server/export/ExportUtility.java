package org.fao.aoscs.server.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.module.constant.ModelConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.server.ClassificationServiceImpl;
import org.fao.aoscs.server.ConceptServiceImpl;
import org.fao.aoscs.server.RelationshipServiceImpl;
import org.fao.aoscs.server.protege.ProtegeWorkbenchUtility;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDataRange;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class ExportUtility {
    
    public static HashMap<String,String> termcodeMap = new HashMap<String,String>(); 
    
    public static ETermObject makeTermObject(OWLModel owlModel, OWLIndividual termInstance, OWLNamedClass owlCls)
    {
        ETermObject termObject = new ETermObject();
        termObject.setUri(termInstance.getURI());
        
        Object createDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
        Object updateDate = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
        if(createDate!=null)
            termObject.setCreateDate(createDate.toString());
        if(updateDate!=null)
            termObject.setUpdateDate(updateDate.toString());
        
        Object status = termInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
        if(status!=null){
            termObject.setStatus(status.toString());
        }
        Object mainLabel = termInstance.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RISMAINLABEL));
        if(mainLabel != null){
            termObject.setMainLabel(Boolean.valueOf(mainLabel.toString()));
        }

        Collection<?> termLabelList = termInstance.getLabels();
        for (Iterator<?> iterator = termLabelList.iterator(); iterator.hasNext();) {
            Object obj = iterator.next();
            if (obj instanceof DefaultRDFSLiteral) {
                DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                termObject.setLabel(element.getString());
                termObject.setLanguage(element.getLanguage());
            }
        }
        
        OWLProperty hasFishery3AlphaCode = owlModel.getOWLProperty(ModelConstants.RHASFISHERY3ALPHACODE);
        OWLProperty hasTaxonomicCode = owlModel.getOWLProperty(ModelConstants.RHASCODETAXONOMIC);
        OWLProperty hasCodeFaoPa = owlModel.getOWLProperty(ModelConstants.RHASCODEFAOPA);
        OWLProperty hasCodeAgrovoc = owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC);
        OWLProperty hasCodeFaoterm = owlModel.getOWLProperty(ModelConstants.RHASCODEFAOTERM);
        OWLProperty hasCodeAsfa = owlModel.getOWLProperty(ModelConstants.RHASCODEASFA);
        OWLProperty hasCodeAsc = owlModel.getOWLProperty(ModelConstants.RHASCODEASC);
        termObject.setCodeAGROVOC(getLiteralValue(owlModel, termInstance, hasCodeAgrovoc));
        
        if(hasCodeAsc != null)  termObject.setCodeASC(getLiteralValue(owlModel, termInstance, hasCodeAsc));
        if(hasCodeAsfa != null) termObject.setCodeASFA(getLiteralValue(owlModel, termInstance, hasCodeAsfa));
        if(hasCodeFaoPa != null) termObject.setCodeFAOPA(getLiteralValue(owlModel, termInstance, hasCodeFaoPa));
        if(hasCodeFaoterm != null) termObject.setCodeFAOTERM(getLiteralValue(owlModel, termInstance, hasCodeFaoterm));
        if(hasFishery3AlphaCode != null) termObject.setCodeFishery(getLiteralValue(owlModel, termInstance, hasFishery3AlphaCode));
        if(hasTaxonomicCode != null) termObject.setCodeTaxonomic(getLiteralValue(owlModel, termInstance, hasTaxonomicCode));

        termObject.setAbbreviationOf(termInstance.hasPropertyValue(owlModel.getOWLProperty(ModelConstants.RISABBREVIATIONOF)));
        
        return termObject;
    }
    
    public static String getLiteralValue(OWLModel owlModel,OWLIndividual ins,OWLProperty prop){
        String literal = null;        
        if(ins.getPropertyValueLiteral(prop)!= null){
            literal = ins.getPropertyValueLiteral(prop).toString();
        }       
        return literal;
    }

    private static ArrayList<String> getSchemeOf(OWLIndividual ins,OWLProperty belongToScheme){
        ArrayList<String> list = new ArrayList<String>();
        
        Collection<?> c = ins.getPropertyValues(belongToScheme);
        for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
            OWLIndividual element = (OWLIndividual) iter.next();
            list.add(stripSuffix(element.getURI(),element.getNamespace()));
        }
        return list;
    }
    
    public static ArrayList<String> getParentRelObject(OWLModel owlModel, String rootNode){
        ArrayList<String> names = new ArrayList<String>();
        OWLProperty rootProp = owlModel.getOWLProperty(rootNode);
        for (Iterator<?> iter = rootProp.getSuperproperties(false).iterator(); iter.hasNext();) 
        {
            OWLProperty prop = (OWLProperty) iter.next(); 
            names.add(prop.getURI().replaceAll(prop.getNamespace(), ""));            
        }
        return names;
    }
    
    private static ConceptObject getParentConceptObject(OWLNamedClass cls){
        ConceptObject parentObject = null;
        OWLNamedClass parentCls = null;
        for (Iterator<?> iter = cls.getSuperclasses(false).iterator(); iter.hasNext();) 
        {
            parentCls = (OWLNamedClass) iter.next();
        }
        if(parentCls != null){
            if((parentCls.getOWLModel() != null) && (parentCls != null))
            {
                parentObject = ProtegeWorkbenchUtility.makeConceptObject(parentCls.getOWLModel(),parentCls);
            }
        } 
        return parentObject;
    }
    
    private static boolean chkSchemeOf(OWLModel owlModel, OWLNamedClass allCls, String schemeURI){
        boolean chk = false;
        OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, allCls);
        if(ins != null){
            for (Iterator<?> iter = ins.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME)).iterator(); iter.hasNext();) {
                OWLIndividual element = (OWLIndividual) iter.next();
                chk = element.getURI().equals(schemeURI);
                if(chk) return true;
            }
        }
        return chk;

    }
    
    public static ArrayList<EConceptObject> convert2AgrovocObject(OWLModel owlModel, ExportParameterObject exp)
    {   
        ArrayList<EConceptObject> list = new ArrayList<EConceptObject>(); 

        String rootNode = ModelConstants.CDOMAINCONCEPT;         
        if(exp.getConceptURI()!=null){
            rootNode = ProtegeWorkbenchUtility.getNameFromURI(owlModel, exp.getConceptURI());
        }
        
        OWLNamedClass cls = owlModel.getOWLNamedClass(rootNode);
        
        if(exp.getSchemeURI()==null)
        {
            list = getConceptList(owlModel, cls);                       
        }
        else
        {
            for (Iterator<?> iter = cls.getSubclasses(false).iterator(); iter.hasNext();) 
            {
                OWLNamedClass allCls = (OWLNamedClass) iter.next();
                if(!allCls.getName().equals(ModelConstants.CDOMAINCONCEPT))
                {                    
                    if(chkSchemeOf(owlModel, allCls, exp.getSchemeURI()))
                    {
                        list = getConceptList(owlModel, allCls);                        
                    }
                }
            }            
        }        
        return list;
    }
    
    public static ArrayList<String[]> getCategory(OWLModel owlModel, HashMap<String,String> schemeMap)
    {
        ArrayList<String[]> list = new ArrayList<String[]>();
        try
        {
            OWLNamedClass scheme = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
            OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
            for (Iterator<?> iter = scheme.getInstances(true).iterator(); iter.hasNext();) 
            {
                OWLIndividual schemeIns = (OWLIndividual) iter.next();
                
                if(!schemeIns.isBeingDeleted())
                {
                    String sLabel = null;
                    for (Iterator<?> itt = schemeIns.getLabels().iterator(); itt.hasNext();) 
                    {
                        RDFSLiteral label = (RDFSLiteral) itt.next();
                        if(label.getLanguage().equalsIgnoreCase("en"))
                            sLabel = label.getString();     
                    }
                    String schemeId = schemeMap.get(sLabel);
                    if(schemeId != null)
                    {
                        String rIsSubCategory = ModelConstants.RIS+schemeIns.getLocalName().replaceAll("i_", "")+ModelConstants.SUBCATEGORYOF;
                        for (Iterator<?> iterator = schemeIns.getPropertyValues(hasCategory).iterator(); iterator.hasNext();) 
                        {
                            OWLIndividual categoryIns = (OWLIndividual) iterator.next();
                            OWLProperty isSubPropertyOf = owlModel.getOWLProperty(rIsSubCategory);    
                            ConceptObject cObj = ProtegeWorkbenchUtility.makeConceptObject(owlModel, (OWLNamedClass)categoryIns.getProtegeType());      
                            OWLIndividual po = getCategoryParent(isSubPropertyOf, categoryIns);

                            String categoryId = ExportUtility.stripSuffix(cObj.getUri(), cObj.getNameSpace()).replaceAll("c_","");
                            String catParent = null;
                            if(po!= null)
                            {
                                if(po.getURI() != null && po.getNamespace() != null)
                                    catParent = ExportUtility.stripSuffix(po.getURI(), po.getNamespace()).replaceAll("i_", "");
                            }
                            for(TermObject tObj : cObj.getTerm().values())
                            {                  
                                String category[] = new String[5];  //`schemeid`,`categoryid`,`languagecode`,`categoryname`,`parentcategoryid`
                                category[0] = schemeId;
                                category[1] = categoryId;
                                category[2] = tObj.getLang().toUpperCase();
                                category[3] = ExportUtility.escapeChar(tObj.getLabel());
                                category[4] = catParent;
                                list.add(category);
                            }
                        }
                    }
                }
            }
            ///owlModel.dispose();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    
    public static OWLIndividual getCategoryParent(OWLProperty isSubPropertyOf,OWLIndividual categoryIns){
        OWLIndividual ins  = null;
        for (Iterator<?> iter = categoryIns.getPropertyValues(isSubPropertyOf).iterator(); iter.hasNext();) {
            OWLIndividual parentIns = (OWLIndividual) iter.next();
            ins = parentIns;
        }
        return ins;
    }
    
    public static ArrayList<EConceptObject> convert2AgrovocCat(OWLModel owlModel, ExportParameterObject exp)
    {   
        ArrayList<EConceptObject> list = new ArrayList<EConceptObject>();
        
        OWLNamedClass cls = owlModel.getOWLNamedClass(ModelConstants.CCATEGORY);
        
        for (Iterator<?> iter = cls.getSubclasses(false).iterator(); iter.hasNext();) 
        {
            OWLNamedClass allCls = (OWLNamedClass) iter.next();
            if( !allCls.getName().equals(ModelConstants.CDOMAINCONCEPT) )
            {        
                for (Iterator<?> iter2 = allCls.getSubclasses(true).iterator(); iter2.hasNext();) 
                {            
                    OWLNamedClass subc = (OWLNamedClass) iter2.next();
                    OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, subc);
                    if(ins != null)
                    {
                        EConceptObject  cObj = new EConceptObject();
                        cObj.setScheme(getSchemeOf(ins, owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME)));
                        cObj.setConceptURI(subc.getURI());
                        cObj.setConceptName(subc.getName());                    
                        cObj.setConceptIns(ins.getURI());
                        cObj.setNamespace(subc.getNamespace());
                        cObj.setParentConceptObject(getParentConceptObject(subc));
                        cObj.setConcetpRelation(new ConceptServiceImpl().getConceptRelationship(owlModel, subc.getName()));
                        Collection<?> childList = subc.getSubclasses(true);
                        for (Iterator<?> iterator1 = childList.iterator(); iterator1.hasNext();) 
                        {                        
                            OWLNamedClass child = (OWLNamedClass) iterator1.next();
                            if(!child.getURI().equals(ModelConstants.CDOMAINCONCEPT)){
                                cObj.addChild(child.getURI());
                            }
                        }
                        
                        Object createDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
                        Object updateDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
                        
                        if(createDate != null && updateDate !=null){
                            cObj.setCreateDate(createDate.toString());
                            cObj.setUpdateDate(updateDate.toString());
                        }
                        
                        Object status = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
                        if(status!=null){
                            cObj.setStatus(status.toString());
                        }    
                        
                        Collection<?> lexicon = ins.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
                        for (Iterator<?> iter3 = lexicon.iterator(); iter3.hasNext();) 
                        {
                            OWLIndividual termInstance = (OWLIndividual) iter3.next();
                            ETermObject termObject = makeTermObject(owlModel, termInstance, cls);
                            if(!termObject.equals(null))    
                                cObj.addTerm(termObject);
                        }
                        list.add(cObj);
                    }
                }
            }            
        }                       
        return list;
    }
    
    public static ArrayList<MappingObject> createMappingObject(OWLModel owlModel)
    {
        ArrayList<MappingObject> schemeList = new ArrayList<MappingObject>(); 
        try
        {
            OWLNamedClass scheme = owlModel.getOWLNamedClass(ModelConstants.CCLASSIFICATIONSCHEME);
            OWLProperty hasCategory = owlModel.getOWLProperty(ModelConstants.RHASCATEGORY);
            OWLProperty mapProp = owlModel.getOWLProperty(ModelConstants.RHASMAPPEDDOMAINCONCEPT);
            for (Iterator<?> iter = scheme.getInstances(true).iterator(); iter.hasNext();) 
            {
                OWLIndividual schemeIns = (OWLIndividual) iter.next();
                String schemeId = stripSuffix(schemeIns.getURI(), schemeIns.getNamespace());
                SchemeObject sObj = new ClassificationServiceImpl().getSchemeObject(schemeIns);
                OWLProperty isSubPropertyOf = owlModel.getOWLProperty(sObj.getRIsSub());
                for (Iterator<?> iterator = schemeIns.getPropertyValues(hasCategory).iterator(); iterator.hasNext();) 
                {
                    OWLIndividual categoryIns = (OWLIndividual) iterator.next();
                    String parentId = "0";
                    OWLIndividual parent = new ClassificationServiceImpl().getCategoryParent(isSubPropertyOf, categoryIns);
                    if(parent!=null ){
                        parentId = stripSuffix(parent.getURI(), parent.getNamespace());
                    }      
                    
                    Collection<?> co = categoryIns.getPropertyValues(mapProp);
                    for (Iterator<?> iter2 = co.iterator(); iter2.hasNext();) 
                    {
                        Object obb = iter2.next();
                        if (obb instanceof OWLIndividual) 
                        {
                            OWLIndividual destConceptIns = (OWLIndividual) obb;                            
                                OWLNamedClass cObjCls = (OWLNamedClass)destConceptIns.getProtegeType();
                                String termCode = stripSuffix(cObjCls.getURI(), destConceptIns.getNamespace());
                                MappingObject mapObj = new MappingObject();
                                mapObj.setCatCode(categoryIns.getName());
                                mapObj.setTermCode(termCode);
                                mapObj.setSchemeId(schemeId);
                                mapObj.setParentCategoryId(parentId);                                                    
                                schemeList.add(mapObj);
                        }
                    }
                }
               
            }            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return schemeList;
    }
    
    public static ArrayList<ETagTypeObject> concert2TagType(OWLModel owlModel, int id)
    {
        ArrayList<ETagTypeObject> tagList = new ArrayList<ETagTypeObject>();
        
        // Ignore the following Datatype Properties... already hardcoded
        ArrayList<String> ignoreList = new ArrayList<String>();
        ignoreList.add(ModelConstants.RHASSCOPENOTE);
        ignoreList.add(ModelConstants.RHASEDITORIALNOTE);
        
        // List of properties who's children need to be exported
        ArrayList<OWLProperty> propList = new ArrayList<OWLProperty>();
        propList.add(owlModel.getOWLProperty(ModelConstants.RHASCODE));
        propList.add(owlModel.getOWLProperty(ModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
        
        for(OWLProperty rootProp : propList)
        {
            for (Iterator<?> iter = rootProp.getSubproperties(true).iterator(); iter.hasNext(); ) 
            {
                OWLDatatypeProperty childProp = (OWLDatatypeProperty) iter.next();
                
                if(!ignoreList.contains(childProp.getName()))
                {
                    Collection<?> labelList = childProp.getLabels();
                    for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) 
                    {
                        Object obj = iterator.next();
                        if (obj instanceof DefaultRDFSLiteral) 
                        {
                            DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
                            ETagTypeObject tt = new ETagTypeObject();
                            tt.setTagtypeid(String.valueOf(id));
                            tt.setTagdesc(label.getBrowserText());
                            tt.setLanguagecode(label.getLanguage());
                            tagList.add(tt);                        
                        }
                    }
                    id+=10;
                }
            }
        }
        return tagList;
    }
    
    public static ArrayList<EScopeObject> getScopeEntries(OWLModel owlModel, HashMap<String,String> mapScope)
    {
        ArrayList<EScopeObject> scopeList = new ArrayList<EScopeObject>();
        
        // List of properties who's children need to be exported
        ArrayList<OWLProperty> propList = new ArrayList<OWLProperty>();
        propList.add(owlModel.getOWLProperty(ModelConstants.RHASTERMTYPE));
        propList.add(owlModel.getOWLProperty(ModelConstants.RISPARTOFSUBVOCABULARY));
        
        for(OWLProperty rootProp : propList)
        {
            Collection<?> c = rootProp.getRanges(true);
            for (Iterator<?> iter = c.iterator(); iter.hasNext();) 
            {
                Object obj = (Object) iter.next();
                if (obj instanceof DefaultOWLDataRange)
                {
                    DefaultOWLDataRange type = (DefaultOWLDataRange) obj;               
                    List<?> rdfList = type.getOneOfValues();
                    rdfList = type.getOneOfValueLiterals();
                    int i = 0;
                    for (Iterator<?> itrr = rdfList.iterator(); itrr.hasNext();) 
                    {
                        DefaultRDFSLiteral ind = (DefaultRDFSLiteral) itrr.next();                  
                        EScopeObject s = new EScopeObject();
                        s.setScopeid((mapScope.get(ind.toString()) == null)? "A"+i : mapScope.get(ind.toString()));
                        s.setScopedesc(ind.toString());
                        s.setLanguagecode("EN");
                        s.setScopegrpid("100");
                        scopeList.add(s);
                    }               
                }
            }
        }
        return scopeList;
    }
    
    public static ArrayList<ETermTagObject> concept2Termtag(OWLModel owlModel, ArrayList<EConceptObject> conceptList)
    {        
        ArrayList<ETermTagObject> termTags = new ArrayList<ETermTagObject>();
        // for each concept
        for (EConceptObject concept: conceptList)         
        {
            HashMap<String,ETermObject> termList = concept.getTerm();
            Iterator<String> itterm = termList.keySet().iterator();
            OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlModel.getOWLNamedClass(concept.getConceptName()));
            
            String termcode = "";
            Object cDate = individual.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
            Object uDate = individual.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
            
            String createDate = (cDate != null)? cDate.toString() : "";
            String updateDate = (uDate != null)? uDate.toString() : "";
            
            // for each term - get the main descriptor term and use this termcode            
            while(itterm.hasNext())
            {                
                ETermObject tObj = concept.getTermObject(itterm.next());
                if(tObj.isMainLabel() && tObj.getLanguage().equalsIgnoreCase("en"))
                {                    
                    String temp = stripSuffix(individual.getURI(), individual.getNamespace());    
                    termcode = temp.replaceAll("i_","");                    
                    break;
                }
            }      
            // SCOPE NOTE
            termTags = getTermTag(owlModel, individual, termcode, ModelConstants.RHASSCOPENOTE, 10, createDate, updateDate, termTags);
            
            // EDITORIAL NOTE
            termTags = getTermTag(owlModel, individual, termcode, ModelConstants.RHASEDITORIALNOTE, 20, createDate, updateDate, termTags);
            
            // DEFINITION
            termTags = getDefinitions(owlModel, individual, termcode, createDate, updateDate, termTags);
            
            // TAXONOMIC CODE
            termTags = getTaxonomicCode(owlModel, individual, termcode, createDate, updateDate, termTags);
            
            
        }
        return termTags;
        
    }
    
    public static ArrayList<ETermTagObject> getTermTag(OWLModel owlModel, OWLIndividual individual, String termcode, String property, int tagtypeid, String cDate, String uDate, ArrayList<ETermTagObject> termTags)
    {
        OWLProperty nProp = owlModel.getOWLProperty(property);            
        Collection<?> props = individual.getPropertyValues(nProp);
        
        HashMap<String, ETermTagObject> tempList = new HashMap<String, ETermTagObject>();
        
        for (Iterator<?> iter = props.iterator(); iter.hasNext();)
        {
            Object o = iter.next();
            if(o instanceof RDFSLiteral)
            {
                RDFSLiteral ol = (RDFSLiteral) o;
                ETermTagObject temp = new ETermTagObject();
                temp.setTermcode(termcode);
                temp.setLanguagecode(ol.getLanguage().toUpperCase());
                temp.setTagtext(ol.getBrowserText());
                temp.setTagtypeid(""+tagtypeid);
                temp.setCreatedate(cDate.toString());
                temp.setLastupdate(uDate.toString());
                String key = temp.getTermcode()+"-"+temp.getLanguagecode();
                if(tempList.containsKey(key))
                {
                    ETermTagObject e = tempList.get(key);
                    e.setTagtext( e.getTagtext() + ";" + temp.getTagtext());
                    tempList.put(key,e);
                }else{
                    tempList.put(key, temp);
                }
            }                
        }
        Iterator<String> ittemp = tempList.keySet().iterator();
        while(ittemp.hasNext()){
            String key = ittemp.next();
            termTags.add(tempList.get(key));
        }
        return termTags;
    }
    
    public static ArrayList<ETermTagObject> getDefinitions(OWLModel owlModel, OWLIndividual individual, String termcode, String cDate, String uDate, ArrayList<ETermTagObject> termTags){
        OWLProperty hasDef = owlModel.getOWLProperty(ModelConstants.RHASDEFINITION);
        HashMap<String, ETermTagObject> tempList = new HashMap<String, ETermTagObject>();
        Collection<?> props3 = individual.getPropertyValues(hasDef);
        for (Iterator<?> iter = props3.iterator(); iter.hasNext();) 
        {
            Object o = iter.next();
            if(o instanceof OWLIndividual)
            {
                OWLIndividual defInstance = (OWLIndividual) o;
                Object cd = defInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
                Object ud = defInstance.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
                for (Iterator<?> iterator = defInstance.getLabels().iterator(); iterator.hasNext();) 
                {                        
                    Object obj = iterator.next();                    
                    if (obj instanceof DefaultRDFSLiteral) 
                    {
                        DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                        ETermTagObject temp = new ETermTagObject();
                        temp.setTermcode(termcode);
                        temp.setLanguagecode(element.getLanguage());
                        temp.setTagtext(element.getBrowserText());
                        temp.setTagtypeid("30");
                        temp.setCreatedate((cd == null) ? cDate.toString() : cd.toString());
                        temp.setLastupdate((ud == null) ? uDate.toString() : ud.toString());
                        String key = temp.getTermcode()+"-"+temp.getLanguagecode();
                        if(tempList.containsKey(key))
                        {
                            ETermTagObject e = tempList.get(key);
                            e.setTagtext( e.getTagtext() + ";" + temp.getTagtext());
                            tempList.put(key,e);
                        }else{
                            tempList.put(key, temp);
                        }                            
                    }
                }
            }
        }
        Iterator<String> ittemp = tempList.keySet().iterator();
        while(ittemp.hasNext()){
            String key = ittemp.next();
            termTags.add(tempList.get(key));
        }
        return termTags;
    }
    
    public static ArrayList<ETermTagObject> getTaxonomicCode(OWLModel owlModel, OWLIndividual individual, String termcode, String cDate, String uDate, ArrayList<ETermTagObject> termTags)
    {
        Collection<?> lexicon = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
        HashMap<String, ETermTagObject> tempList = new HashMap<String, ETermTagObject>();
        for(Iterator<?> iter = lexicon.iterator(); iter.hasNext();) 
        {
            OWLIndividual termInd = (OWLIndividual) iter.next();                
            String txcode = "";
            String lang = "";
            Collection<?> labelList = termInd.getLabels();
            for(Iterator<?> iterator = labelList.iterator(); iterator.hasNext();){
                Object obj = iterator.next();
                if(obj instanceof DefaultRDFSLiteral){
                    DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                    lang = element.getLanguage();                      
                }
            }
            Object tempcode = termInd.getPropertyValue(owlModel.getOWLProperty(ModelConstants.RHASCODEAGROVOC));
            if(tempcode != null)
            {
                OWLProperty codeTax = owlModel.getOWLProperty(ModelConstants.RHASCODETAXONOMIC);
                Collection<?> props4   = termInd.getPropertyValues(codeTax);
                for(Iterator<?> iter1 = props4.iterator(); iter1.hasNext();)
                {
                    String s = (String)iter1.next();
                    txcode = s;
                    if(txcode != null){
                        ETermTagObject temp = new ETermTagObject();
                        temp.setTermcode(""+tempcode);
                        temp.setLanguagecode(lang);
                        temp.setTagtext(txcode);
                        temp.setTagtypeid("50");
                        temp.setCreatedate(cDate.toString());
                        temp.setLastupdate(uDate.toString());
                        String key = temp.getTermcode()+"-"+temp.getLanguagecode();
                        if(tempList.containsKey(key))
                        {
                            ETermTagObject e = tempList.get(key);
                            e.setTagtext( e.getTagtext() + ";" + temp.getTagtext());
                            tempList.put(key,e);
                        }else{
                            tempList.put(key, temp);
                        }  
                    }
                }
               
            }
        }
        Iterator<String> ittemp = tempList.keySet().iterator();
        while(ittemp.hasNext()){
            String key = ittemp.next();
            termTags.add(tempList.get(key));
        }
        return termTags;
    }
    
    public static AgrovocObject convert2AgrovocCategory(OWLModel owlModel, ExportParameterObject exp)
    {   
        AgrovocObject agObj = new AgrovocObject();
        String rootNode = ModelConstants.CCATEGORY;
        if(exp.getConceptURI()!=null) rootNode = exp.getConceptURI();
        
        OWLNamedClass cls = owlModel.getOWLNamedClass(rootNode);
        
        ArrayList<EConceptObject> list = new ArrayList<EConceptObject>();
        for (Iterator<?> iter = cls.getSubclasses(false).iterator(); iter.hasNext();) 
        {
            Collection<?> c = cls.getSubclasses(true);
            for (Iterator<?> iter2 = c.iterator(); iter2.hasNext();) 
            {            
                OWLNamedClass subc = (OWLNamedClass) iter2.next();            
                if(!subc.getURI().equals(ModelConstants.CDOMAINCONCEPT))
                {
                    OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, subc);
                    if(ins != null){                        
                        EConceptObject  cObj = new EConceptObject();
                        cObj.setScheme(getSchemeOf(ins, owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME)));
                        cObj.setConceptURI(subc.getURI());
                        cObj.setConceptName(subc.getName());                    
                        cObj.setConceptIns(ins.getURI());
                        cObj.setNamespace(subc.getNamespace());
                        cObj.setParentConceptObject(getParentConceptObject(subc));
                        cObj.setConcetpRelation(new ConceptServiceImpl().getConceptRelationship(owlModel, subc.getName()));
                        Collection<?> childList = subc.getSubclasses(true);
                        for (Iterator<?> iterator1 = childList.iterator(); iterator1.hasNext();) 
                        {                        
                            OWLNamedClass child = (OWLNamedClass) iterator1.next();
                            if(!child.getURI().equals(ModelConstants.CDOMAINCONCEPT)){
                                cObj.addChild(child.getURI());
                            }
                        }
                        
                        Object createDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
                        Object updateDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
                        
                        if(createDate != null && updateDate !=null){
                            cObj.setCreateDate(createDate.toString());
                            cObj.setUpdateDate(updateDate.toString());
                        }
                        
                        Object status = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
                        if(status!=null){
                            cObj.setStatus(status.toString());
                        }    
                        
                        Collection<?> lexicon = ins.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
                        for (Iterator<?> iter3 = lexicon.iterator(); iter3.hasNext();) 
                        {
                            OWLIndividual termInstance = (OWLIndividual) iter3.next();
                            ETermObject termObject = makeTermObject(owlModel, termInstance, cls);
                            if(!termObject.equals(null))    cObj.addTerm(termObject);
                        }
                        list.add(cObj);
                    }
                }
            }
            
        }
        for(int i=0;i<list.size();i++)
        {
            agObj.addConceptList(list.get(i));
        }
                
        return agObj;
    }
    
    public static ArrayList<EConceptObject> getConceptList(OWLModel owlModel, OWLNamedClass cls)
    {
        ArrayList<EConceptObject> list = new ArrayList<EConceptObject>();
        if(!cls.getName().equals(ModelConstants.CDOMAINCONCEPT))
        {
            OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, cls);
            EConceptObject  co = new EConceptObject();
            co.setScheme(getSchemeOf(ins, owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME)));
            co.setConceptURI(cls.getURI());
            co.setConceptName(cls.getName());
            co.setConceptIns(ins.getURI());
            co.setNamespace(cls.getNamespace());
            co.setParentConceptObject(getParentConceptObject(cls));
            co.setConcetpRelation(new ConceptServiceImpl().getConceptRelationship(owlModel, cls.getName()));
            
            Object createDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
            Object updateDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
            
            if(createDate != null){
                co.setCreateDate(createDate.toString());
            }
            if(updateDate !=null){
                co.setUpdateDate(updateDate.toString());
            }
            
            
            Object status = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
            if(status!=null){
                co.setStatus(status.toString());
            }    
            
            Collection<?> lexicon = ins.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
            for (Iterator<?> iter2 = lexicon.iterator(); iter2.hasNext();)
            {
                OWLIndividual termInstance = (OWLIndividual) iter2.next();
                ETermObject termObject = makeTermObject(owlModel, termInstance, cls);
                Collection<?> coll = termInstance.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASACRONYM));
                if(coll.size() > 0)
                    termObject.setHasAcronymOf(true);
                if(!termObject.equals(null))    
                    co.addTerm(termObject);
            }
            list.add(co);
        }                        
        
        Collection<?> c = cls.getSubclasses(true);
        for (Iterator<?> iter = c.iterator(); iter.hasNext();) 
        {            
            OWLNamedClass allCls = (OWLNamedClass) iter.next();
            if(!allCls.getURI().equals(ModelConstants.CDOMAINCONCEPT))
            {                
                OWLIndividual ins = ProtegeWorkbenchUtility.getConceptInstance(owlModel, allCls);
                if(ins != null)
                {
                    EConceptObject  cObj = new EConceptObject();
                    cObj.setScheme(getSchemeOf(ins, owlModel.getOWLProperty(ModelConstants.RBELONGSTOSCHEME)));
                    cObj.setConceptURI(allCls.getURI());
                    cObj.setConceptName(allCls.getName());
                    cObj.setConceptIns(ins.getURI());
                    cObj.setNamespace(allCls.getNamespace());
                    cObj.setParentConceptObject(getParentConceptObject(allCls));
                    cObj.setConcetpRelation(new ConceptServiceImpl().getConceptRelationship(owlModel, allCls.getName()));
                    Collection<?> childList = allCls.getSubclasses(true);
                    for (Iterator<?> iterator1 = childList.iterator(); iterator1.hasNext();) 
                    {                        
                        OWLNamedClass child = (OWLNamedClass) iterator1.next();
                        if(!child.getURI().equals(ModelConstants.CDOMAINCONCEPT)){
                            cObj.addChild(child.getURI());
                        }
                    }
                    
                    Object createDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASDATECREATED));
                    Object updateDate = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASUPDATEDDATE));
                    if(createDate != null){
                        cObj.setCreateDate(createDate.toString());
                    }
                    if( updateDate !=null){
                        cObj.setUpdateDate(updateDate.toString());
                    }
                    
                    Object status = ins.getPropertyValueLiteral(owlModel.getOWLProperty(ModelConstants.RHASSTATUS));
                    if(status!=null){
                        cObj.setStatus(status.toString());
                    }    
                    
                    Collection<?> lexicon = ins.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
                    for (Iterator<?> iter2 = lexicon.iterator(); iter2.hasNext();) 
                    {
                        OWLIndividual termInstance = (OWLIndividual) iter2.next();
                        ETermObject termObject = makeTermObject(owlModel, termInstance, cls);
                        Collection<?> coll = termInstance.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASACRONYM));
                        if(coll.size() > 0)
                            termObject.setHasAcronymOf(true);
                        if(!termObject.equals(null))    
                            cObj.addTerm(termObject);
                    }
                    list.add(cObj);
                }
            }            
        }
        return list;
    }
    
    public static ETermRelationshipObject getTermRelationship(String cls, String termIns, OWLModel owlModel)
    {
        ETermRelationshipObject trObj = new ETermRelationshipObject();
        
        OWLNamedClass owlCls = owlModel.getOWLNamedClass(cls);
        OWLIndividual individual = ProtegeWorkbenchUtility.getConceptInstance(owlModel, owlCls);
        OWLObjectProperty hasRelatedTerm = owlModel.getOWLObjectProperty(ModelConstants.RHASRELATEDTERM);
        
        Collection<OWLObjectProperty> propList = getAllObjectPropperties(owlModel, hasRelatedTerm);
        propList.add(hasRelatedTerm);
        Collection<?> lexicon = individual.getPropertyValues(owlModel.getOWLProperty(ModelConstants.RHASLEXICALIZATION));
        
        //ArrayList<String> hasSynonymTerms = new ArrayList<>;
        
        for (Iterator<?> iter = lexicon.iterator(); iter.hasNext();) 
        {
            OWLIndividual termInstance = (OWLIndividual) iter.next();
            if(termInstance.getName().equalsIgnoreCase(termIns))
            {
                for (Iterator<OWLObjectProperty> iter2 = propList.iterator(); iter2.hasNext();) 
                {
                    ArrayList<ETermObject> termList = new ArrayList<ETermObject>();
                    OWLObjectProperty prop = iter2.next();
                    if(prop.getName().equals(ModelConstants.RHASTRANSLATION))
                        continue;
                    if(prop.getName().equals(ModelConstants.RHASSYNONYM))
                    {
                        
                    }
                    else
                    {
                        RelationshipObject rObj = new RelationshipObject();
                        rObj.setName(prop.getName());
                        rObj.setUri(prop.getURI());
                        rObj.setType(RelationshipObject.OBJECT);
                        Collection<?> labelList = prop.getLabels();
                        for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) 
                        {
                            Object obj = iterator.next();
                            if (obj instanceof DefaultRDFSLiteral) {
                                DefaultRDFSLiteral element = (DefaultRDFSLiteral) obj;
                                rObj.addLabel(element.getString(), element.getLanguage());
                            }
                        }
                        
                        Collection<?>  coll = termInstance.getPropertyValues(prop);
                        for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) 
                        {
                            Object insObj = iterator.next();
                            if (insObj instanceof DefaultOWLIndividual) 
                            {
                                DefaultOWLIndividual tIns = (DefaultOWLIndividual) insObj;
                                ETermObject tObj = makeTermObject(owlModel, tIns, owlCls);
                                termList.add(tObj);
                            }
                        }
                        if(!termList.isEmpty()){
                            trObj.addResult(rObj, termList);
                        }
                    }
                }
            }
        }
        ///owlModel.dispose();  
        return trObj;
    }
    
    public static Collection<OWLObjectProperty> getAllObjectPropperties(OWLModel owlModel,OWLObjectProperty rootProp){
        Collection<OWLObjectProperty> result = new ArrayList<OWLObjectProperty>();
        Collection<?> propList = rootProp.getSubproperties(true);
        for (Iterator<?> iter = propList.iterator(); iter.hasNext();) {
            OWLObjectProperty prop = (OWLObjectProperty) iter.next();
            result.add(prop);
        }
        return result;
    }
    
    public static String getScopeId(String name){
        if(name.equals("c_chemical_concept"))
            return "CH";
        else if(name.equals("c_geographic_concept_country_level"))
            return "GC";
        else if(name.equals("c_geographic_concept_above_country_level"))
            return "GG";
        else if(name.equals("c_geographic_concept_below_country_level"))
            return "GL";
        else if(name.equals("c_taxonomic_term_animals"))
            return "TA";
        else if(name.equals("c_taxonomic_term_bacteria"))
            return "TB";
        else if(name.equals("c_taxonomic_term_fungi"))
            return "TF";
        else if(name.equals("c_taxonomic_term_plant"))
            return "TP";
        else if(name.equals("c_taxonomic_term_viruses"))
            return "TV";
        else
            return null;
    }
    
    public static String getStatusId(ETermObject tObj){
        String tstatuscode = null;
        String tstatus = tObj.getStatus();
        String tcagrovoc =tObj.getCodeAGROVOC();
        if(tObj.isMainLabel())
        {
            if(tstatus.equals(OWLStatusConstants.PROPOSED))          tstatuscode = "10";
            else if(tstatus.equals(OWLStatusConstants.VALIDATED))    tstatuscode = "15";
            else if(tstatus.equals(OWLStatusConstants.PUBLISHED))    tstatuscode = "20";
            else if(tstatuscode == null)         tstatuscode = "50";
        }else{
            if(tstatus.equals(OWLStatusConstants.PROPOSED))  tstatuscode = "70";
            if(tstatus.equals(OWLStatusConstants.VALIDATED)) tstatuscode = "80";
            if(tstatus.equals(OWLStatusConstants.PUBLISHED)) tstatuscode = "90";
            if(tstatuscode == null)      tstatuscode = "60";
        }
        if(tstatus.equals(OWLStatusConstants.PROPOSED_GUEST))       tstatuscode = "100";
        if(tstatus.equals(OWLStatusConstants.PROPOSED_DEPRECATED))   tstatuscode = "120";
        if(tcagrovoc!= null){
            if(tcagrovoc.equals("36000")) tstatuscode = "110";
        }
        if(tstatus.equals(OWLStatusConstants.DEPRECATED))   tstatuscode = "0";
        return tstatuscode;
    }
    
    public static String stripSuffix(String uri, String namespace){       
       return uri.replaceAll(namespace, "");       
    }

    public static ERelationshipTreeObject getObjectPropertyTree(OWLModel owlModel,String rootNode)
    {
        ERelationshipTreeObject rtObj = new ERelationshipTreeObject();
        
        RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
        OWLObjectProperty rootProp = owlModel.getOWLObjectProperty(rootNode);
        
        for(Iterator<?> iter = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp).iterator(); iter.hasNext();) 
        {
            OWLObjectProperty prop = (OWLObjectProperty) iter.next();
            ERelationshipObject rObj = new ERelationshipObject();
            // first level
            for (Iterator<?> iterator = prop.getLabels().iterator(); iterator.hasNext();) 
            {
                DefaultRDFSLiteral label = (DefaultRDFSLiteral) iterator.next();
                rObj.addLabelList(label.getString(), label.getLanguage());
            }
            rObj.setParent(ExportUtility.getParentRelObject(owlModel , prop.getName()));
            
            String linkLevel = determineCH(prop.getName()); 
            rObj.setLinkLevel(linkLevel == null? getLinkLevel(prop.getName(),true) : linkLevel);
            
            rObj.setInversePropertyName(getInversePropertyName(prop));
            rObj.setUri(prop.getURI());
            rObj.setType(RelationshipObject.OBJECT);
            rObj.setRootItem(true);
            rObj.setName(prop.getName());
            
            rtObj.addRelationshipList(rObj);
            rtObj.addParentChild(rootProp.getURI(), rObj);
            // get lower level
            getChildObjectProperty(prop, owlModel,rtObj , prop);            
            rtObj.addRelationshipDefinition(rObj.getUri() , new RelationshipServiceImpl().getRelationshipComments(rObj.getName(), owlModel));
        }       
        return rtObj;
    }
    
    public static void getChildObjectProperty(OWLObjectProperty rootProp, OWLModel owlModel,ERelationshipTreeObject rtObj, OWLObjectProperty masterProp)
    {
        RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
        for (Iterator<?> iter = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp).iterator(); iter.hasNext();) 
        {   
            OWLObjectProperty childProp = (OWLObjectProperty) iter.next();          
            
            
            ERelationshipObject rObj = new ERelationshipObject();
            rObj.setInversePropertyName(getInversePropertyName(childProp));
            for (Iterator<?> iterator = childProp.getLabels().iterator(); iterator.hasNext();) 
            {
                Object obj = iterator.next();
                if (obj instanceof DefaultRDFSLiteral) {
                    DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
                    rObj.addLabelList(label.getString(), label.getLanguage());
                }else{
                    rObj.addLabelList(obj.toString(), "");          
                }
            }
                        
            rObj.setParent(ExportUtility.getParentRelObject(owlModel , childProp.getName()));
            String linkLevel = determineCH(childProp.getName()); 
            rObj.setLinkLevel(linkLevel == null? getLinkLevel(masterProp.getName(),false) : linkLevel);
            //rObj.setLinkLevel(getLinkLevel(masterProp.getName(), false));
                                                
            rObj.setUri(childProp.getURI());
            rObj.setType(RelationshipObject.OBJECT);
            rObj.setRootItem(false);
            rObj.setName(childProp.getName());
            
            rtObj.addRelationshipList(rObj);
            rtObj.addParentChild(rootProp.getURI(), rObj);
            getChildObjectProperty(childProp, owlModel,rtObj, masterProp);
        }
    }
    
    public static String determineCH(String relName)
    {
        String linkLevel = "";
                  
        if(relName.equals(ModelConstants.RSUBCLASSOF) || 
           relName.equals(ModelConstants.RHASSUBCLASS) ||
           relName.equals(ModelConstants.RISINSTANCEOF) ||
           relName.equals(ModelConstants.RHASINSTANCE))
            linkLevel = "CH";
        else
            return null;
        return linkLevel;
    }
    
    public static String getLinkLevel(String relName, boolean firstLevel)
    {
        String linkLevel = null;
        if(firstLevel){          
            if(relName.equals(ModelConstants.RHASRELATEDCONCEPT))
                linkLevel = "TR";
            if(relName.equals(ModelConstants.RISREFERENCEDINANNOTAIONOF))
                linkLevel = "TT";
        }else{          
            if(relName.equals(ModelConstants.RHASRELATEDCONCEPT))
                linkLevel = "CR";
            if(relName.equals(ModelConstants.RHASRELATEDTERM))
                linkLevel = "TT";       
            if(relName.equals(ModelConstants.RTHESAURUSRELATIONSHIP))
                linkLevel = "TR";
        }
        return linkLevel;
    }
    
    public static String getInversePropertyName(OWLObjectProperty prop)
    {   
        String name = null;
        RDFProperty ip = prop.getInverseProperty();
        if(ip != null)
            name = ip.getName();
        return name;
    }
    
    public static ERelationshipTreeObject getDataTypePropertyTree(OWLModel owlModel, String rootNode)
    {
        ERelationshipTreeObject rtObj = new ERelationshipTreeObject();

        RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
        OWLDatatypeProperty rootProp = owlModel.getOWLDatatypeProperty(rootNode);
        Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
        
        for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
            
            OWLDatatypeProperty prop = (OWLDatatypeProperty) iter.next();
            ERelationshipObject rObj = new ERelationshipObject();
            Collection<?> labelList = prop.getLabels();

            for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
                DefaultRDFSLiteral label = (DefaultRDFSLiteral) iterator.next();
                rObj.addLabelList(label.getString(), label.getLanguage());
            }
            
            rObj.setParent(ExportUtility.getParentRelObject(owlModel , prop.getName()));
            rObj.setUri(prop.getURI());
            rObj.setType(RelationshipObject.DATATYPE);
            rObj.setRootItem(true);
            rObj.setName(prop.getName());
            
            rtObj.addRelationshipList(rObj);
            rtObj.addParentChild(rootProp.getURI(), rObj);
            
            getChildDataTypeProperty(prop, owlModel,rtObj);
        }
        
        return rtObj;
    }
    
    public static void getChildDataTypeProperty(OWLDatatypeProperty rootProp, OWLModel owlModel,ERelationshipTreeObject rtObj){
        
        RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_PROPERTY_OF);
        Collection<?> results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, rootProp);
        
        for (Iterator<?> iter = results.iterator(); iter.hasNext();) {
            OWLDatatypeProperty childProp = (OWLDatatypeProperty) iter.next();
        
            ERelationshipObject rObj = new ERelationshipObject();
            Collection<?> labelList = childProp.getLabels();
            
            for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
                Object obj = iterator.next();
                if (obj instanceof DefaultRDFSLiteral) {
                    DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
                    rObj.addLabelList(label.getString(), label.getLanguage());
                }
            }
            
            rObj.setParent(ExportUtility.getParentRelObject(owlModel , childProp.getName()));
            rObj.setUri(childProp.getURI());
            rObj.setType(RelationshipObject.DATATYPE);
            rObj.setRootItem(false);
            rObj.setName(childProp.getName());
            
            rtObj.addRelationshipList(rObj);
            rtObj.addParentChild(rootProp.getURI(), rObj);
            getChildDataTypeProperty(childProp, owlModel,rtObj);
        }
    }

    public static String checkNull(String value){
        if(value == null)
            return "NULL";
        else 
            return "'" + value + "'";
    }
    
    public static String escapeChar(String value)
    {
              
       String temp = value.replaceAll("'", "\\\\'");
       temp = temp.replaceAll("\"", "\\\\'");       
       return temp;    
    }
}




