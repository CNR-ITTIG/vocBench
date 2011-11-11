package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.utility.TimeConverter;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImageObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TranslationObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptImage extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private AddNewImage addNewImage;
	private DeleteImage deleteImage;
	private AddImageLabel addImageLabel;
	private EditExternalSource editExternalSource;
	private AddExternalSource addExternalSource;
	private DeleteExternalSource deleteExternalSource;
	private EditImageLabel editImageLabel;
	private DeleteImageLabel deleteImageLabel;
	
	public ConceptImage(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable, initData, conceptDetailPanel, classificationDetailPanel);
		
	}
	private void attachNewImgButton(){
		functionPanel.clear();
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGECREATE, getConceptObject().getStatusID());
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddImage(), constants.conceptAddImage(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if( addNewImage == null || !addNewImage.isLoaded)
					addNewImage = new AddNewImage();
				addNewImage.show();
			}
		});			
		functionPanel.add(add);
	}
	
	private HorizontalPanel getImageNumber(int number , final IDObject dObj){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(3);
		hp.add(new Label(Integer.toString(number)));
		
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGEDELETE, getConceptObject().getStatusID());
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteImage(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteImage == null || !deleteImage.isLoaded)
					deleteImage = new DeleteImage(dObj);
				deleteImage.show();
			}
		});
		hp.add(delete);
		
		return hp;
	}
	
	private HorizontalPanel getAddTranslationFunction(final IDObject ido){
		HorizontalPanel hp = new HorizontalPanel();
		
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGETRANSLATIONCREATE, getConceptObject().getStatusID());
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddNewTranslation(), constants.conceptAddTranslation(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addImageLabel == null || !addImageLabel.isLoaded)
					addImageLabel = new AddImageLabel(ido);
				addImageLabel.show();
			}
		});
		hp.add(add);

		return hp;
	}
	
	public static native void openURL(String url)/*-{
    $wnd.open(url,'_blank','');
}-*/;
	
	private Image getImageFrame(final IDObject dObj){
		Image img = new Image();
		img.setSize("100px", "65px");
		if(dObj.getIDSourceURL()!=null){
			img.setUrl(dObj.getIDSourceURL());
			img.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					openURL(dObj.getIDSourceURL());
				}
			});
			img.setStyleName(Style.Link);
			
		}
		DOM.setStyleAttribute(img.getElement(), "marginRight", "3px");
		return img;
	}
	private HorizontalPanel getDateTable(final IDObject dObj){
		Grid table = new Grid(3,2);
		table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
		table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
		table.setWidget(2, 0, new HTML(constants.conceptSource()));
		table.setWidget(0, 1, new HTML(TimeConverter.formatDate(dObj.getIDDateCreate())));
		table.setWidget(1, 1, new HTML(TimeConverter.formatDate(dObj.getIDDateModified())));
		
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.setSpacing(3);
		if(dObj.hasSource()){
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGESOURCEEDIT, getConceptObject().getStatusID());
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditSource(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editExternalSource == null || !editExternalSource.isLoaded)
						editExternalSource = new EditExternalSource(dObj);
					editExternalSource.show();
				}
			});
			
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGESOURCEDELETE, getConceptObject().getStatusID());
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteSource(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteExternalSource == null || !deleteExternalSource.isLoaded)
						deleteExternalSource = new DeleteExternalSource(dObj);
					deleteExternalSource.show();
				}
			});
			
			hp.add(edit);
			hp.add(delete);
			
			HTML link =  new HTML("<A HREF=\""+dObj.getIDSourceURL()+"\" target=\"_blank\">"+dObj.getIDSource()+"</A>");
			hp.add(link);
		}else{
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGESOURCECREATE, getConceptObject().getStatusID());
			LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", constants.conceptAddNewSource(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
				if(addExternalSource == null || !addExternalSource.isLoaded)
					addExternalSource = new AddExternalSource(dObj);
				addExternalSource.show();
				}
			});
			hp.add(add);
		}
		
		table.setWidget(2, 1,hp);
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidth("100%");
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(getImageFrame(dObj));
		panel.add(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		panel.setWidth("100%");
		return panel;
	}
	
	private VerticalPanel getTranslationTable(IDObject dObj){
		ArrayList<TranslationObject> list = dObj.getIDTranslationList();
		Grid table = new Grid(list.size()+1,2);
		table.setWidget(0, 0, new HTML(constants.conceptName()));
		table.setWidget(0, 1, new HTML(constants.conceptDescription()));
		for (int i = 0; i < list.size(); i++) {
			final TranslationObject tObj = (TranslationObject) list.get(i);
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGETRANSLATIONEDIT, getConceptObject().getStatusID());
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditTranslation(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
				if(editImageLabel == null || !editImageLabel.isLoaded)
					editImageLabel = new EditImageLabel(tObj);
				editImageLabel.show();
				}
			});
		
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_IMAGETRANSLATIONDELETE, getConceptObject().getStatusID());
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteTranslation(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteImageLabel == null || !deleteImageLabel.isLoaded)
						deleteImageLabel = new DeleteImageLabel(tObj);
					deleteImageLabel.show();
				}
			});
			
			hp.add(edit);
			hp.add(delete);
			
			HTML name =new HTML(tObj.getLabel()+"("+tObj.getLang()+")");
			name.setWordWrap(false);
			
			HTML description = new HTML(tObj.getDescription());
			description.setWordWrap(true);
			hp.add(description);
			
			table.getColumnFormatter().setWidth(1, "100%");
			table.setWidget(i+1, 0, name);
			table.setWidget(i+1, 1, hp);
		}
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true);
	}
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getImageObject()!=null)
		{
			initData(cDetailObj.getImageObject());
		}
		else
		{
			AsyncCallback<ImageObject> callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results) {
					cDetailObj.setImageObject(results);
					initData(results);
				}
				public void onFailure(Throwable caught) {
					Window.alert(constants.conceptGetImageFail());
				}
			};
			Service.conceptService.getConceptImage(conceptObject.getName(), MainApp.userOntology, callback);
		}
		
	}
	
	private void initData(ImageObject imgObj)
	{
		clearPanel();
		if(!imgObj.isEmpty()){
			attachNewImgButton(); 
			ArrayList<IDObject> iObjList = imgObj.getImageListOnly();
			Grid table = new Grid(iObjList.size()+1,2);
			table.setWidget(0, 0, new HTML(constants.conceptNo()));					
			table.setWidget(0, 1, new HTML(constants.conceptImage()));					
			table.setWidth("100%");
			for (int i = 0; i < iObjList.size(); i++) {
				IDObject dObj = (IDObject) iObjList.get(i);
				table.setWidget(i+1, 0, getImageNumber(i+1, dObj));
				
				VerticalPanel vp = new VerticalPanel();
				
				HorizontalPanel func = getAddTranslationFunction(dObj);
				vp.add(func);
				vp.setCellHorizontalAlignment(func, HasHorizontalAlignment.ALIGN_RIGHT);
				
				vp.add(getTranslationTable(dObj));
				vp.add(getDateTable(dObj));
				vp.setWidth("100%");
				vp.setSpacing(5);
				
				table.setWidget(i+1, 1, vp);
			}
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) 
				conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.image, Convert.replaceSpace(iObjList.size()>1?constants.conceptImages():constants.conceptImage())+"&nbsp;("+(iObjList.size())+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1", true));
		}else{
			attachNewImgButton();
			Label sayNo = new Label(constants.conceptNoImages());
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.image, Convert.replaceSpace(constants.conceptImage())+"&nbsp;(0)");
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	
	public class DeleteExternalSource extends FormDialogBox implements ClickHandler{
		private IDObject ido;
		public DeleteExternalSource(IDObject ido){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptDeleteSource());
			setWidth("400px");
			this.initLayout();
		
		}
		public void initLayout() {
			HTML message = new HTML(constants.conceptImageSourceDeleteWarning());
			
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			
			addWidget(table);
		};
		
		public void onSubmit() {
			sayLoading();
			
			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteExternalSourceFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageSourceDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageSourceDelete));
			
			Service.conceptService.deleteImageExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
		}
		
	}
	public class AddExternalSource extends FormDialogBox implements ClickHandler{
		private TextBox source;
		private TextBox URL;
		private IDObject ido;
		
		public AddExternalSource(IDObject ido){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptAddSourceName());
			setWidth("400px");
			this.initLayout();
		
		}
		public void initLayout() {
			source = new TextBox();
			source.setWidth("100%");

			URL = new TextBox();
			URL.setWidth("100%");
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptSource()));
			table.setWidget(1, 0, new HTML(constants.conceptUrl()));
			table.setWidget(0, 1, source);
			table.setWidget(1, 1, URL);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		
		public boolean passCheckInput() {
			boolean pass = false;
			if(source.getText().length()==0 || URL.getText().equals("") ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}

		public void onSubmit() {
			sayLoading();
			
			IDObject idoNew = new IDObject();
			idoNew.setIDUri(ido.getIDUri());
			idoNew.setIDUri(ido.getIDName());
			idoNew.setIDSource(source.getText());
			idoNew.setIDSourceURL(URL.getText());
			idoNew.setIDType(IDObject.IMAGE);

			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddExternalSourceFail());
				}
			};
			 
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageSourceCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageSourceCreate));
			
			Service.conceptService.addImageExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, idoNew, conceptObject, callback);
		}
	
	}
	public class EditExternalSource extends FormDialogBox implements ClickHandler{
		private TextBox source;
		private TextBox URL;
		private IDObject ido;
		
		public EditExternalSource(IDObject ido){
			super();
			this.ido = ido;
			this.setText(constants.conceptEditExternalSource());
			setWidth("400px");
			this.initLayout();
		}
		
		public void initLayout() {

			source = new TextBox();
			source.setText(ido.getIDSource());
			source.setWidth("100%");
			
			URL = new TextBox();
			URL.setText(ido.getIDSourceURL());
			URL.setWidth("100%");
			
			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptSource()));
			table.setWidget(1, 0, new HTML(constants.conceptUrl()));
			table.setWidget(0, 1, source);
			table.setWidget(1, 1, URL);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		};

		public boolean passCheckInput() {
			boolean pass = false;
			if( source.getText().length()==0 || URL.getText().equals("") ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		
		public void onSubmit() {
			sayLoading();
			
			IDObject idoNew = new IDObject();
			idoNew.setIDUri(ido.getIDUri());
			idoNew.setIDUri(ido.getIDName());
			idoNew.setIDSource(source.getText());
			idoNew.setIDSourceURL( URL.getText());
			idoNew.setIDType(IDObject.IMAGE);

			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptEditExternalSourceFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageSourceEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageSourceEdit));
	
			
			Service.conceptService.editImageExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, idoNew, conceptObject, callback);
		}
		
	}
	public class AddImageLabel extends FormDialogBox implements ClickHandler{
		private TextBox name;
		private ListBox language;
		private TextArea descArea;
		private IDObject ido;
		
		public AddImageLabel(IDObject ido){ 
			super(constants.buttonCreate(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptAddNewLabel());
			setWidth("400px");
			this.initLayout();
			
		}
		public void initLayout() {
			name = new TextBox();
			name.setWidth("100%");

			descArea = new TextArea();
			descArea.setWidth("100%");
			descArea.setVisibleLines(3);
			
			language = new ListBox();
			language =  Convert.makeListBoxWithValue(Convert.excludeLanguage((ArrayList<String[]>)MainApp.getLanguage(),ido.getExistingLanguage()));
			language.setWidth("100%");
			
			Grid table = new Grid(3,2);
			table.setWidget(0, 0, new HTML(constants.conceptName()));			
			table.setWidget(1, 0, new HTML(constants.conceptDescription()));			
			table.setWidget(2, 0, new HTML(constants.conceptLanguage()));			
			table.setWidget(0, 1, name);
			table.setWidget(1, 1, descArea);
			table.setWidget(2, 1, language);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || descArea.getText().equals("")|| name.getText().length()==0 ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
		public void onSubmit() {
			sayLoading();
			
			TranslationObject transObjNew = new TranslationObject();
			transObjNew.setDescription(descArea.getText());
			transObjNew.setLabel(name.getText());
			transObjNew.setLang(language.getValue(language.getSelectedIndex()));
			transObjNew.setUri(ido.getIDUri());
			transObjNew.setType(TranslationObject.IMAGETRANSLATION);
			
			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddImageFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageTranslationCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageTranslationCreate));
			
			
			Service.conceptService.addImageLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObjNew, ido, conceptObject, callback);
		}
		
	}
	public class DeleteImageLabel extends FormDialogBox implements ClickHandler{
		private TranslationObject transObj;
		
		public DeleteImageLabel(TranslationObject transObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.transObj = transObj;
			this.setText(constants.conceptDeleteImageLabel());
			setWidth("400px");
			this.initLayout();
			
		}
		public void initLayout() {
			HTML message = new HTML(messages.conceptImageTranslationDeleteWarning(transObj.getLabel()));

			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			
			addWidget(table);
		}
		public void onSubmit() {
			sayLoading();
			
			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddImageFail());
				}
			};
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageTranslationDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageTranslationDelete));
			
			
			Service.conceptService.deleteImageLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObj, conceptObject, callback);
		}
	
	}
	
	public class EditImageLabel extends FormDialogBox implements ClickHandler{
		private TextArea descArea;
		private ListBox language;
		private TextBox name;
		private TranslationObject transObj;
		
		public EditImageLabel(TranslationObject transObj){
			super();
			this.transObj = transObj;
			this.setText(constants.conceptEditImage());
			this.initLayout();
			setWidth("400px");
			
		}
		public void initLayout() {
			descArea = new TextArea();
			descArea.setText(transObj.getDescription());
			descArea.setWidth("100%");
			descArea.setVisibleLines(3);

			name = new TextBox();
			name.setText(transObj.getLabel());
			name.setWidth("100%");
			
			language = new ListBox();
			language =  Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),transObj.getLang());
			language.setWidth("100%");
			language.setEnabled(false);
			
			
			Grid table = new Grid(3,2);
			table.setWidget(0, 0, new HTML(constants.conceptName()));				
			table.setWidget(1, 0, new HTML(constants.conceptDescription()));			
			table.setWidget(2, 0, new HTML(constants.conceptLanguage()));			
			table.setWidget(0, 1, name);
			table.setWidget(1, 1, descArea);
			table.setWidget(2, 1, language);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}
		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("") || descArea.getText().equals("") ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}
	    public void onSubmit() {
	    	sayLoading();
	    	
	    	TranslationObject transObjNew = new TranslationObject();
	    	transObjNew.setDescription(descArea.getText());
	    	transObjNew.setLabel(name.getText().replaceAll("\"", "\\\\\""));
	    	transObjNew.setLang(language.getValue(language.getSelectedIndex()));
	    	transObjNew.setUri(transObj.getUri());
	    	transObjNew.setType(TranslationObject.IMAGETRANSLATION);

	    	AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddImageFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageTranslationEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageTranslationEdit));
			
			Service.conceptService.editImageLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObj, transObjNew, conceptObject, callback);
	    }
	
	}
	
	public class DeleteImage extends FormDialogBox implements ClickHandler{
		private IDObject ido ;
		
		public DeleteImage(IDObject ido){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.setText(constants.conceptDeleteImage());
			this.ido = ido;
			setWidth("400px");
			this.initLayout();
		
		}
		public void initLayout() {
			HTML message = new HTML(constants.conceptImageDeleteWarning());
		
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0, 1, message);
			
			addWidget(table);
		}
		public void onSubmit() {
			sayLoading();
		
			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptDeleteImageFail());
				}
			};
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageDelete));
			
			
			Service.conceptService.deleteImage(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
		}
		
	}
	public class AddNewImage extends FormDialogBox implements ClickHandler{
		
		private TextArea description;
		private ListBox language;
		private TextBox source;
		private TextBox name;
		private TextBox url;
		
		public AddNewImage(){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.setText(constants.conceptAddNewImage());
			this.setWidth("400px");
			this.initLayout();
		}
		
		public void initLayout() {
			name = new TextBox();
			name.setWidth("100%");

			url = new TextBox();
			url.setWidth("100%");

			description = new TextArea();
			description.setVisibleLines(3);
			description.setWidth("100%");

			language = new ListBox();
			language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
			language.setWidth("100%");
			
			source = new TextBox();
			source.setWidth("100%");
						
			Grid table = new Grid(5,2);
			table.setWidget(0, 0, new HTML(constants.conceptName()));
			table.setWidget(1, 0, new HTML(constants.conceptDescription()));
			table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(3, 0, new HTML(constants.conceptSource()));
			table.setWidget(4, 0, new HTML(constants.conceptUrl()));
			table.setWidget(0, 1, name);
			table.setWidget(1, 1, description);
			table.setWidget(2, 1, language);
			table.setWidget(3, 1, source);
			table.setWidget(4, 1, url);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");			
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));			
			
		}
		public boolean passCheckInput() {
		    boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("--None--") || description.getText().equals("") || source.getText().length()==0 || url.getText().length()==0 || name.getText().length()==0){
				pass = false;
			}else {
				pass = true;
			}
			
			return pass;
		}
		public void onSubmit() {
			sayLoading();
			
			TranslationObject transObj = new TranslationObject();
			transObj.setLang(language.getValue(language.getSelectedIndex()));
			transObj.setLabel(name.getText());
			transObj.setType(TranslationObject.IMAGETRANSLATION);
			transObj.setDescription(description.getText());
			
			IDObject ido = new IDObject();
			ido.setIDSourceURL(url.getText());
			ido.setIDSource(source.getText());
			ido.addIDTranslationList(transObj);
			ido.setIDType(IDObject.IMAGE);
			
			AsyncCallback<ImageObject>  callback = new AsyncCallback<ImageObject>(){
				public void onSuccess(ImageObject results){
					cDetailObj.setImageObject(results);
					ConceptImage.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAddImageFail());
				}
			};
			
			
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditImageCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditImageCreate));
			
			Service.conceptService.addImage(MainApp.userOntology,actionId, status, MainApp.userId, transObj, ido, conceptObject, callback);
		}
	
	}
}
