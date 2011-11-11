package org.fao.aoscs.client.locale;

import com.google.gwt.i18n.client.Constants;
//
//The order of the variables must match with the .properties files.
//
public interface LocaleConstants extends Constants{

	//Mainpage
	public String mainLocale();
	public String mainPageTitle();
	public String mainVersion();
	public String mainVersionAllCaps();
	public String mainRegister();
	public String mainMustLogin();	
	public String mainMustLoginOr();		
	public String mainMustRegister();		
	public String mainToUseWB();
	public String mainRegisterTitle();
	public String mainForgotPassword();
	public String mainForgotPasswordTitle();
	public String mainHome();
	public String mainHomeTitle();
	public String mainHelp();
	public String mainHelpTitle();
	public String mainPartner();
	public String mainAck1();
	public String mainAck2();
	public String mainWelcome();
	public String mainBrief();
	public String mainLearnMore();
	public String mainBrowserWarn();
	
	
	//Mainpage>messages
	public String mainSessionExpired();
	
	//Footer
	public String footerPartners();
	public String footerContactUs();
	public String footerCopyRight();
	public String footerOrganization();
	
	//Log
	public String logSiteStatistics();
	public String logTotalVisitors();
	public String logTotalDuration();
	public String logIPAddress();
	public String logCountry();
	public String logLoginTime();
	public String logLogoutTime();
	public String logDuration();
	public String logLoggedAs();
	
	//Log>messages
	public String logStartFail();
	public String logEndFail();
	public String logSignoutFail();
	public String logDataFail();
	public String logNoData();
	public String logError();
	 
	//Menubar
	public String menuHome();
	public String menuGlossary();
	public String menuAdministration();
	public String menuAbout();
	public String menuWebServices();
	public String menuUsers();
	public String menuGroups();
	public String menuPreferences();
	public String menuComments();
	public String menuLogViewer();
	public String menuLanguage();
	
	//Toolbar
	public String toolbarHome();
	public String toolbarHomeTitle();
	public String toolbarSearch();
	public String toolbarSearchTitle();
	public String toolbarConcepts();
	public String toolbarConceptsTitle();
	public String toolbarRelationships();
	public String toolbarRelationshipsTitle();
	public String toolbarSchemes();
	public String toolbarSchemesTitle();
	public String toolbarValidation();
	public String toolbarValidationTitle();
	public String toolbarConsistency();
	public String toolbarConsistencyTitle();
	public String toolbarImport();
	public String toolbarImportTitle();
	public String toolbarExport();
	public String toolbarExportTitle();
	public String toolbarStatistics();
	public String toolbarStatisticsTitle();
	public String toolbarUsers();
	public String toolbarUsersTitle();
	public String toolbarGroups();
	public String toolbarGroupsTitle();
	public String toolbarPreferences();
	public String toolbarPreferencesTitle();


	//Statusbar
	public String statusWelcome();
	public String statusSignOut();
	public String statusRSS();
	public String statusRSSTitle();
	public String statusHelp();
	public String statusHelpTitle();
	public String statusComments();
	public String statusCommentsTitle();
	
	//Help
	public String helpDefault();
	public String helpSearch();
	public String helpConcept();
	public String helpRelationship();
	public String helpPreference();
	public String helpImport();
	public String helpExport();
	public String helpUser();
	public String helpGroup();
	public String helpValidation();
	public String helpConsistency();
	public String helpClassification();
	public String helpStatistics();
	public String helpAbout();
	public String helpTextVersion();
	public String helpVideoVersion();
	public String helpViewTextVersion();
	public String helpViewVideoVersion();
	public String helpPartner();
	
	//Help>messages
	public String helpFail();
	public String helpNoVideo();
	
	//Login
	public String loginLogin();
	public String loginPassword();
	public String loginSignIn();
	public String loginRememberMe();
	public String loginSignInAsAnonymous();
	public String loginSignInAsVisitor();
	public String loginTitle();

	//Login>messages
	public String loginNoEmpty();
	public String loginNoMatch();
	public String loginNoAssignGroup();
	public String loginNoAssignLang();
	public String loginFail();
	
	//SelectPreferences
	public String selPref();
	public String selPrefGroup();
	public String selPrefLanguage();
	public String selPrefOntology();
	public String selLoadOntologyFail();
	
	//SelectPreferences>messages
	public String selPrefNoMatch();
	public String selPrefNoLoad();
	public String selPrefFail();
	
	//ForgotPassword
	public String fpChangePassword();
	public String fpUserName();
	public String fpEmail();
	public String fpNewPassword();
	public String fpConfirmPassword();
	
	//ForgotPassword>messages
	public String fpPasswordMismatch();
	public String fpPasswordEmpty();
	public String fpPasswordMinChar();
	public String fpNoUser();
	public String fpChangePasswordFail();
	
	//Register
	public String registerUser();
	public String registerSelectLanguage();
	public String registerSelectOntology();
	public String registerSelectGroup();
	public String registerChatTypes();
	public String registerMSN();
	public String registerYAHOO();
	public String registerSKYPE();
	public String registerGOOGLETALK();
	public String registerLoginName();
	public String registerPassword();
	public String registerConfirmPassword();
	public String registerFirstName();
	public String registerLastName();
	public String registerAffiliation();
	public String registerContactAddress();
	public String registerPostalCode();
	public String registerCountry();
	public String registerWorkPhone();
	public String registerMobilePhone();
	public String registerEmail();
	public String registerChat();
	public String registerURL();
	public String registerGroups();
	public String registerLanguages();
	public String registerOntology();
	public String registerComment();

	
	//Register>messages
	public String registerCountryListFail();
	public String registerMaxChar();
	public String registerRemainingChar();
	public String registerSessionExpired();
	public String registerUserExists();
	public String registerUserError();
	public String registerUserSuccess();
	public String registerUserEditFail();
	public String registerNoLangSelect();
	public String registerNoOntologySelect();
	public String registerNoGroupSelect();
	public String registerNoLogin();
	public String registerPasswordMismatch();
	public String registerPasswordMin();
	public String registerNoFirstName();
	public String registerNoLastName();
	public String registerNoAffiliation();
	public String registerNoContactAddress();
	public String registerNoCountry();
	public String registerNoEmail();
	public String registerInvalidEmail();
	public String registerNoChat();
	public String registerCommentsMax();
	public String registerShowUserFail();
	public String registerListLangFail();
	public String registerListOntologyFail();
	public String registerListGroupFail();
	public String registerNoData();
	public String registerRemoveLang();
	public String registerRemoveOntology();
	public String registerRemoveGroup();

	
	//LanguageFilter
	public String langFilterSelectLang();
	public String langFilterAddLang();
	public String langFilterOrder();
	public String langFilterLang();
	public String langFilterCode();
	public String langFilterNote();
	public String langFilterAddLanguage();
	public String langFilterEditLanguage();
	public String langFilterDeleteLanguage();
	public String langFilterMessage();
	
	//LanguageFilter>messages
	public String langFilterAddLangFail();
	public String langFilterEditLangFail();
	public String langFilterDeleteLangFail();
	public String langFilterDeleteWarning();
	public String langFilterSelectLanguage();
		
	//Comments
	public String commentPost();
	public String commentComments();
	public String commentOptional();
	public String commentName();
	public String commentEmail();
	public String commentDate();
	public String commentAbout();
	public String commentSearch();
	public String commentConcepts();
	public String commentRelationships();
	public String commentSchemes();
	public String commentValidation();
	public String commentImport();
	public String commentExport();
	public String commentConsistency();
	public String commentStatistics();
	public String commentUsers();
	public String commentGroups();
	public String commentPreferences();
	
	//Comments>messages
	public String commentSuccess();
	public String commentFail();
	public String commentValidate();
	public String commentListError();
	
	//Home
	public String homeRecentChanges();
	public String homeAbout();
	public String homeConceptTermRelationshipScheme();
	public String homeChange();
	public String homeOldValue();
	public String homeAction();
	public String homeUser();
	public String homeDate();
	public String homeNoTerm();
	
	//Home>messages
	public String homeNoData();
	public String homeListError();
	public String homeRCDataFail();
	public String homeAboutDataFail();
	public String homeGlossaryDataFail();

	//Search
	public String searchTitle();
	public String searchResults();
	public String searchContains();
	public String searchExactMatch();
	public String searchStartsWith();
	public String searchEndsWith();
	public String searchExactWord();
	public String searchFuzzySearch();
	public String searchConcept();
	public String searchDate();
	public String searchButton();
	public String searchGoButton();
	public String searchShowAdv();
	public String searchCaseSensitive();
	public String searchIncludeNotes();
	public String searchPreferredTermsOnly();
	public String searchIncludeSpell();
	public String searchAdvSearch();
	public String searchRelationship();
	public String searchSubvocabulary();
	public String searchTermCode();
	public String searchStatus();
	public String searchScheme();
	public String searchLanguageFilter();
	public String searchSelectLang();
	public String searchBrowseRelationship();
	public String searchClearRelationship();
	public String searchRelationshipBrowser();
	public String searchBrowseSubVocabulary();
	public String searchClearSubVocabulary();
	public String searchSubVocabularyBrowser();
	public String searchRepository();
	public String searchCode();
	public String searchSearching();
	public String searchConceptAttribute();
	public String searchTermAttribute();
	public String searchSelectConceptAttribute();
	public String searchSelectTermAttribute();
	public String searchFilterAddWarn();
	public String searchFilterRemoveWarn();
	public String searchHelpCaseSensitive();
	public String searchHelpIncludeDescription();
	public String searchHelpSearchPreferredOnly();
	public String searchHelpLanguage(); 
	
	//Search>messages
	public String searchNoResult();
	public String searchListError();
	public String searchNoLangSelect();
	public String searchNoGeo();
	public String searchNoSci();
	public String searchNoData();
	public String searchRemoveLang();
	public String searchResultSizeFail();
	
	public String searchTermCodeRepositoryWarn(); 
	public String searchIndexTitle(); 
	public String searchIndexConfirm(); 
	public String searchIndexWaitMsg(); 
	public String searchIndexComplete();
	public String searchIndexFail(); 
	public String searchIndexNotAvailable(); 
	public String searchLanguageNotFound(); 

	//Validation
	public String valTitle();
	public String valReload();
	public String valFilterApply();
	public String valAcceptAll();
	public String valRejectAll();
	public String valValidate();
	public String valFilterVal();
	public String valAll();
	public String valFilterUser();
	public String valFilterStatus();
	public String valFilterAction();
	public String valFilterDate();
	public String valClearDate();
	public String valClearDateTitle();
	public String valFrom();
	public String valTo();
	public String valDateFormat();
	public String valToday();
	public String valThisWeek();
	public String valThisMonth();
	public String valConcept();
	public String valChange();
	public String valOldValue();
	public String valAction();
	public String valOwner();
	public String valModifier();
	public String valCreateDate();
	public String valModifiedDate();
	public String valStatus();
	public String valNote();
	
	//Validation>messages
	public String valLoadInitDataFail();
	public String valLoadValDataSizeFail();
	public String valLoadValDataFail();
	public String valDateInvalid();
	public String valDateStartInvalid();
	public String valDateEndInvalid();
	public String valDateLaterMax();
	public String valNoData();
	public String valListError();
	
	//Consistency
	public String conTitle();
	public String conToday();
	public String conThisWeek();
	public String conThisMonth();
	public String conFilterHide();
	public String conFilterShow();
	public String conSelectType();
	public String conReload();
	public String conAll();
	public String conFilterStatus();
	public String conFilterStatusDest();
	public String conFilterLang();
	public String conFilterTermCodeType();
	public String conFilterDate();
	public String conClearDate();
	public String conClearDateTitle();
	public String conFrom();
	public String conTo();
	public String conDateFormat();
	public String conConcept();
	public String conConceptDest();
	public String conTerm();
	public String conConceptTerm();
	public String conTermCode();
	public String conTermCodeProperty();
	public String conRelationship();
	public String conDateCreated();
	public String conDateModified();
	public String conStatus();
	public String conStatsDest();
	public String conNotAvailable();
	
	//Consistency>messages
	public String conLoadInitDataFail();
	public String conLoadConDataFail();
	public String conDateInvalid();
	public String conDateStartInvalid();
	public String conDateEndInvalid();
	public String conDateLaterMax();
	
	//Export
	public String exportTitle();
	public String exportSelectLang();
	public String exportLangFilter();
	public String exportLang();
	public String exportCode();
	public String exportSubVocabulary();
	public String exportDate();
	public String exportConcept();
	public String exportTermCode();
	public String exportFormat();
	public String exportScheme();
	public String exportSubVocabularyBrowser();
	public String exportCreate();
	public String exportUpdate();
	public String exportFrom();
	public String exportTo();
	public String exportConceptBrowser();
	public String exportTermCodeBrowser();
	public String exportTermCodeType();
	public String exportLoading();
	public String exportSKOS();
	public String exportSQL();
	public String exportPreview();
	public String exportButton();
	public String exportPreviewButton();
	public String exportIncludeChildren();	
	
	//Export>messages
	public String exportInitFail();
	public String exportSelectFormat();
	public String exportDataFail();
	public String exportNoGeo();
	public String exportNoSci();
	public String exportNoData();
	public String exportLogFail();
	public String exportSelectDateRange();
	
	//Statistics
	public String statTitle();
	public String statUserInfo();
	public String statUserTotal();
	public String statUserLastConnect();
	public String statLanguage();
	public String statUserCount();
	public String statStatus();
	public String statCountConcept();
	public String statCountTerm();
	public String statUser();
	public String statCountRelationship();
	public String statCountConnection();
	public String statLastModification();
	public String statProposed();
	public String statValidated();
	public String statPublished();
	public String statCreate();
	public String statEdit();
	public String statDelete();
	public String statRelationship();
	public String statDomain();
	public String statRange();
	public String statExportFormat();
	public String statCount();
	public String statTotal();
	public String statPrinterFriendly();
	public String statPrintPreviewTitle();
	public String statPrintPreview();
	
	public String statReload();
	public String statSelectStat();
	public String statSelectAnyStat();
	
	public String statUserStat();
	public String statPerLang();
	public String statPerStatus();
	public String statPerUser();
	public String statActionPerUser();
	public String statPerRelationship();
	public String statExportStat();
	
	//Statistics>messages
	public String statLoadFail();
	public String statNoInfo();
	
	//Print>messages
	public String printFailed();
	
	//Users
	public String userManagement();
	public String userUsers();
	public String userGroup();
	public String userLang();
	public String userOntology();
	public String userSelectGroup();
	public String userSelectLang();
	public String userSelectOntology();
	public String userDetails();
	public String userActivated();
	public String userActive();
	public String userInActive();
	public String userNew();
	public String userChatTypes();
	public String userMSN();
	public String userYAHOO();
	public String userSKYPE();
	public String userGOOGLETALK();
	public String userName();
	public String userPassword();
	public String userConfirmPassword();
	public String userFirstName();
	public String userLastName();
	public String userEmail();
	public String userAffiliation();
	public String userAddress();
	public String userPostalCode();
	public String userCountry();
	public String userURL();
	public String userRegistratonDate();
	public String userWorkPhone();
	public String userMobilePhone();
	public String userChat();
	public String userComment();
	

	//Users>messages
	public String userNoLogin();
	public String userNoFirstName();
	public String userNoLastName();
	public String userNoAffiliation();
	public String userNoAddress();
	public String userNoCountry();
	public String userNoEmail();
	public String userEmailMismatch();
	public String userNoChat();
	public String userCommentMax();
	public String userNoPassword();
	public String userPasswordMin();
	public String userPasswordMismatch();
	public String userListUserFail();
	public String userListGroupFail();
	public String userListLangFail();
	public String userListOntologyFail();
	public String userListCountryFail();
	public String userSessionExpired();
	public String userShowUserProfileFail();
	public String userNoLang();
	public String userNoOntology();
	public String userLangRemoveFail();
	public String userOntologyRemoveFail();
	public String userNoGroup();
	public String userGroupRemoveFail();
	public String userRemoveUserGroupFail();
	public String userRemoveUserLangFail();
	public String userRemoveUserFail();
	public String userAddFail();
	public String userExits();
	public String userUpdateFail();
	public String userNoData();
	public String userAddUserGroupFail();
	public String userAddUserLanguageFail();
	public String userAddUserOntologyFail();

	public String userConfirmRemoveLang();
	public String userConfirmRemoveOntology();
	public String userConfirmRemoveGroup();
	public String userConfirmAddUser();
	public String userConfirmSaveData();
	public String userCancelAddData();
	public String userCancelEditData();
	public String userActivateAdditionalInfo();

	
	//Groups
	public String groupManagment();
	public String groupExisting();
	public String groupPermissions();
	public String groupMembers();
	public String groupActions();
	public String groupDescription();
	public String groupAddUser();
	public String groupAddPermit();
	public String groupAddAction();

	public String groupName();
	public String groupAdd();
	public String groupEdit();
	
	//Groups>messages
	public String groupDeleteMain();
	public String groupRemoveFail();
	public String groupNoUserSelect();
	public String groupRemoveUserFail();
	public String groupNoPermitSelect();
	public String groupRemovePermitFail();
	public String groupNoActionSelect();
	public String groupRemoveActionFail();
	public String groupLoadUserListFail();
	public String groupLoadDescprtionFail();
	public String groupLoadPermissionFail();
	public String groupLoadGroupsFail();
	public String groupNoGroupName();
	public String groupAddFail();
	public String groupEditFail();
	public String groupUserListFail();
	public String groupSelectUser();
	public String groupSelectAction();
	public String groupAddUserFail();
	public String groupGetUserMail();
	public String groupAddActionFail();
	public String groupSendMailFail();
	public String groupPermissionListFail();
	public String groupAddPermissionFail();
	public String groupConfirmRemoveUsers();
	public String groupConfirmRemoveUser();
	public String groupConfirmRemovePermission();
	public String groupConfirmRemoveAction();
	
	
	//LoadingDialog
	public String ldLoading();
	
	//Preference
	public String preferences();
	public String prefDetails();
	public String prefLogin();
	public String prefNewPassword();
	public String prefConfirmPassword();
	public String prefEmail();
	public String prefNewEmail();
	public String prefConfirmEmail();
	public String prefOntology();
	public String prefInitialScreen();
	public String prefShowURI();
	public String prefShowNonPreferredTermsAlso();
	public String prefHideDeprecated();
	public String prefShowAlsoNonselectedLanguages();
	public String prefLanguageCS();
	public String prefLanguageInterface();
	public String prefSelectLanguages();
	
	//Preference>messages
	public String prefEmailMismatch();
	
	public String prefPasswordMismatch();
	public String prefPasswordMinCharacter();
	public String prefPasswordEmailchangeFail();
	
	public String prefLanguageNotSelected();
	public String prefLanguageSelectRemove();
	public String prefLanguageSaveFail();
	public String prefLanguageSelectOne();
	public String prefLanguageNoRemoveAll();
	
	public String prefNotLoad();
	public String prefSaved();
	public String prefNotSaved();
	
	//Concept
	public String conceptTitle();
	public String conceptReload();
	public String conceptMove();
	public String conceptCopy();
	public String conceptRemove();
	public String conceptVisualize();
	public String conceptAddNew();
	public String conceptContinueCreateConcept();
	public String conceptCheckAvailability();
	public String conceptTopLevelConcept();
	public String conceptChildConcept();
	public String conceptSameLevelConcept();
	public String conceptDelete();
	public String conceptCreateNew();	
	public String conceptLabel();
	public String conceptPosition();
	public String conceptNamespace();
	public String conceptNamespacePrefix();
	public String conceptAddNamespace();
	
	public String conceptShowURI();
	public String conceptShowNonPreferredTermsAlso();
	public String conceptTerm();
	public String conceptNewTerm();
	public String conceptTerms();
	public String conceptDefinition();
	public String conceptDefinitions();
	public String conceptScopeNote();
	public String conceptScopeNotes();
	public String conceptEditorialNote();
	public String conceptEditorialNotes();
	public String conceptNote();
	public String conceptNotes();
	public String conceptAttribute();
	public String conceptAttributes();
	public String conceptRelationship();
	public String conceptRelationships();
	public String conceptImage();
	public String conceptImages();
	public String conceptHistory();	
	public String conceptHierarchy();
	public String conceptAddNewTerm();
	public String conceptPreferredTerm();
	public String conceptNonPreferredTerm();
	public String conceptEditTerm();
	public String conceptDeleteTerm();	
	public String conceptLanguage();	
	public String conceptAddDefinition();
	public String conceptEditDefinition();
	public String conceptDeleteDefinition();
	public String conceptNoDefinition();	
	public String conceptNoHierarchy();
	public String conceptAddScopeNote();
	public String conceptNoScopeNote();
	public String conceptAddEditorialNote();
	public String conceptNoEditorialNote();
	public String conceptNo();
	public String conceptAddNewTranslation();
	public String conceptAddTranslation();
	public String conceptEditTranslation();
	public String conceptDeleteTranslation();
	public String conceptCreateDate();
	public String conceptUpdateDate();
	public String conceptSource();
	public String conceptAddNewSource();
	public String conceptEditSource();
	public String conceptDeleteSource();
	public String conceptEditExternalSource();
	public String conceptUrl();	
	public String conceptSourceTitle();	
	public String conceptUri();
	public String conceptValue();
	public String conceptAddNewValue();
	public String conceptEditValue();	
	public String conceptDeleteValue();
	public String conceptEditScopeNote();	
	public String conceptDeleteScopeNote();
	public String conceptEditEditorialNote();	
	public String conceptDeleteEditorialNote();
	public String conceptAddNotes();	
	public String conceptEditNotes();	
	public String conceptDeleteNotes();
	public String conceptNoNotes();
	public String conceptAddAttributes();	
	public String conceptEditAttributes();	
	public String conceptDeleteAttributes();
	public String conceptNoAttributes();
	public String conceptAddDefinitionLabel();
	public String conceptDeleteDefinitionLabel();
	public String conceptAddNewRelationship();
	public String conceptEditRelationship();
	public String conceptDeleteRelationship();
	public String conceptNoRelationships();
	public String conceptCreateRelationshipToAnotheConcept();
	public String conceptEditRelationshipToAnotheConcept();
	public String conceptDestination();
	public String conceptRelationshipBrowser();
	public String conceptBrowser();
	public String conceptAddImage();
	public String conceptAddNewImage();
	public String conceptEditImage();	
	public String conceptDeleteImage();
	public String conceptName();
	public String conceptDescription();
	public String conceptNoImages();
	public String conceptAddSourceName();
	public String conceptAddNewLabel();
	public String conceptDeleteImageLabel();
	public String conceptChange();
	public String conceptOldValue();
	public String conceptAction();
	public String conceptUser();
	public String conceptDate();
	public String conceptStatus();
	public String conceptShowEntireText();	
	public String conceptClickForDefinition();	
	public String conceptNewChild();
	public String conceptNewParent();
	public String conceptSetRoot();
	public String conceptMappedConcept();
	public String conceptMappedConcepts();
	public String conceptAddMappedConcept();
	public String conceptDeleteMappedConcept();
	public String conceptNoTerm();
	public String conceptSelectRootConcept();
	public String conceptSetDefault();
	public String conceptCurrentRoot();
	public String conceptNewRoot();
	public String conceptPreferred();
	public String conceptWikipedia();
	public String conceptNavigationHistory();
	public String conceptNoNavigationHistory();
	public String conceptShowAdvOptions();
	public String conceptShowAdvOptionsHelp();
	public String conceptFoundExistingConcepts();
	public String conceptStructuredProperty();
	
	// Concept>messages
	public String conceptCheckAvailabilityFail();
	public String conceptConceptExist();
	public String conceptConceptNotExist();
	public String conceptGoToSelectedConceptWarningTitle();
	public String conceptGoToSelectedConceptWarning();
	public String conceptAddFail();
	public String conceptAddNamespaceFail();
	public String conceptAddDefinitionFail();
	public String conceptDeleteFail();
	public String conceptCopyFail();
	public String conceptRemoveFail();
	public String conceptRemoveFailOnlyOne();
	public String conceptReloadFail();
	public String conceptLoadFail();
	public String conceptDefinitionDeleteWarning();
	public String conceptDefinitionSourceDeleteWarning();
	public String conceptValueDeleteWarning();
	public String conceptImageDeleteWarning();
	public String conceptImageSourceDeleteWarning();	
	public String conceptNoMappedConcept();	
	public String conceptGetMappedConceptFail();	
	public String conceptAddMappedConceptFail();	
	public String conceptDeleteMappedConceptWarning();	
	public String conceptDeleteMappedConceptFail();	
	public String conceptCompleteInfo();	
	public String conceptCorrectURIPrefix();	
	public String conceptMoveFail();	
	public String conceptGetDefinitionFail();	
	public String conceptDeleteExternalSourceFail();	
	public String conceptEditExternalSourceFail();	
	public String conceptAddExternalSourceFail();	
	public String conceptEditTranslationFail();	
	public String conceptAddTranslationFail();	
	public String conceptDeleteDefinitionFail();	
	public String conceptGetImageFail();
	public String conceptAddImageFail();
	public String conceptDeleteImageFail();
	public String conceptMakeConceptInfoPanelFail();
	public String conceptAddValueFail();
	public String conceptEditValueFail();
	public String conceptDeleteValueFail();
	public String conceptGetRelationshipFail();
	public String conceptCreateRelationshipTreeFail();
	public String conceptEditRelationshipFail();
	public String conceptDeleteRelationshipFail();	
	public String conceptGetSearchResultFail();	
	public String conceptGetTermFail();
	public String conceptAddTermFail();
	public String conceptAddTermFailDuplicate();
	public String conceptEditTermFail();
	public String conceptDeleteTermFail();
	public String conceptNoDataToDisplay();
	public String conceptInformationFor();
	public String conceptSelectCategory();
	public String conceptSelectConcept();
	public String conceptSelectTreeItem();
	public String conceptHierarchyLoadFail();
	public String conceptAddTermNoLangSelected();
	
	//Shared>messages
	public String sharedConceptNotFound();
	public String sharedGetTreePathFail();
	public String sharedGetTreeItemFail();
	

	// Relationship
	public String relTitle();	
	public String relAddNewRelationship();
	public String relCreateNewRelationship();
	public String relDeleteRelationship();	
	public String relReloadRelationship();
	public String relShowUri();
	public String relUri();
	public String relChildRelationship();
	public String relSameRelationship();
	public String relRelationship();
	public String relLabel();
	public String relLabels();
	public String relLanguage();
	public String relPosition();
	public String relDefinition();
	public String relDefinitions();
	public String relProperty();
	public String relProperties();
	public String relObjectProperties();
	public String relDatatypeProperties();
	public String relShowEntireText();
	public String relAddNewLabel();
	public String relEditLabel();
	public String relDeleteLabel();
	public String relNoLabel();
	public String relCreateLabel();
	public String relAddNewDefinition();
	public String relEditDefinition();
	public String relDeleteDefinition();
	public String relNoDefinition();
	public String relCreateDefinition();
	public String relAddNewProperty();	
	public String relDeleteProperty();
	public String relNoProperty();
	public String relCreateProperty();
	public String relAddNewInvProperty();
	public String relEditInvProperty();
	public String relDeleteInvProperty();
	public String relNoInvProperty();
	public String relCreateInvProperty();
	public String relBrowser();
	public String relInvProperty();
	public String relDomain();
	public String relDomains();
	public String relRange();
	public String relRanges();
	public String relAddNewDomain();
	public String relAddNewRange();
	public String relEditRange();
	public String relAddRange();
	public String relDeleteRange();
	public String relDeleteDomain();
	public String relType();
	public String relValue();
	public String relValues();
	public String relClass();
	public String relAddRangeValue();
	public String relCreateNewDomainRange();
	public String relRelationshipBrowser();
					
	// Relationship>messages
	public String relReloadFail();
	public String relGetTreeItemFail();
	public String relAddNewReltionshipFail();
	public String relDeleteReltionshipFail();
	public String relAddLabelFail();
	public String relEditLabelFail();	
	public String relDeleteLabelWarning();
	public String relDeleteLabelFail();
	public String relAddDefinitionFail();
	public String relEditDefinitionFail();
	public String relDeleteDefinitionWarning();
	public String relDeleteDefinitionFail();
	public String relAddPropertyFail();
	public String relDeletePropertyWarning();
	public String relDeletePropertyFail();
	public String relClickForDefinition();
	public String relCreateRelationshipTreeFail();
	public String relAddInvPropertyFail();
	public String relEditInvPropertyFail();
	public String relDeleteInvPropertyFail();
	public String relAddNewDomainRangeFail();
	public String relDeleteDomainRangeWarning();
	public String relDeleteDomainRangeFail();
	public String relInitDataFail();	
	public String relSelectRelationship();
	public String relFailRelationshipBrowser();
	
	//Term
	public String termAddCode();
	public String termEditCode();
	public String termDeleteCode();
	public String termType();
	public String termCode();
	public String termCodes();
	public String termRepository();
	public String termHistory();
	public String termValue();
	public String termLanguage();
	public String termRelationship();
	public String termRelationships();
	public String termTerm();
	public String termDestination();	
	public String termSpellingVariation();
	public String termSpellingVariations();
	public String termAddSpellingVariant();
	public String termEditValue();
	public String termDeleteValue();	
	public String termAddRelationship();	
	public String termEditRelationship();	
	public String termDeleteRelationship();	
	public String termRelationshipDeletion();	
	public String termRelationshipBrowser();	
	public String termClickForDefinition();	
	public String termBrowser();	
	public String termCodeBrowser();	
		
	//Term>messages
	public String termNoTermCodes();
	public String termGetTermInfoFail();
	public String termGetTermCodeFail();
	public String termCompleteInfo();
	public String termTypeIntegerRequired();
	public String termTypeDecimalRequired();
	public String termAddCodeFail();
	public String termEditCodeFail();
	public String termAddDefinitionFail();
	public String termAddValueFail();
	public String termEditValueFail();
	public String termDeleteValueFail();
	public String termGetRelationshipFail();
	public String termAddRelationshipFail();
	public String termEditRelationshipFail();
	public String termDeleteRelationshipFail();
	public String termGetTermFail();
	public String termNoSpellingVariant();
	public String termNoRelationship();
	public String termCreateRelationshipTreeFail();		
	public String termCreateRelationshipToAnotherTerm();	
	public String termNoTranslationRel();	
	public String termChangeCodeAGROVOC();	
	public String termSelectCodeAGROVOC();	
	
	//Scheme	
	public String schemeTitle();
	public String schemeShowUri();
	public String schemeShowNonPreferredTermsAlso();
	public String schemeNoCategory();
	public String schemeShowEntireText();
	public String schemeUri();
	public String schemeAddNewCategory();
	public String schemeDeleteCategory();
	public String schemeNew();
	public String schemeEdit();
	public String schemeDelete();
	public String schemeReload();
	public String schemeShowDescription();
	public String schemeCreateNew();
	public String schemeName();
	public String schemeNamespace();
	public String schemeDescription();
	public String schemeConceptBrowser();
	public String schemeChildItem();
	public String schemeSameItem();
	public String schemeLanguage();
	public String schemePositions();
	public String schemePosition();
	public String schemeConcept();
	public String schemeCreateByConcept();
	
	//Scheme/messages
	public String schemeInitDataFail();
	public String schemeReloadTreeFail();
	public String schemeLoadTreeFail();
	public String schemeAddFail();
	public String schemeEditFail();
	public String schemeDeleteWarning();
	public String schemeDeleteFail();
	public String schemeCreateFromScratch();
	public String schemeSelectExisting();
	public String schemeCompleteInfo();
	public String schemeAddCategoryFromScratchFail();
	public String schemeDeleteCategoryWarning();
	public String schemeDeleteCategoryFail();
	
	//Legends
	public String legendShowMore();
	public String legendShowLess();
	public String legendTitle();
	public String legendConcept();
	public String legendTerm();
	public String legendRelationshipObjectType();
	public String legendRelationshipDataType();
	public String legendCategory();
	public String legendClassification();
	
	
	//Preference>buttons
	public String buttonAdd();
	public String buttonAddAgain();
	public String buttonOk();
	public String buttonEdit();
	public String buttonDelete();
	public String buttonUpdate();
	public String buttonCancel();
	public String buttonCreate();
	public String buttonRemove();
	public String buttonSave();
	public String buttonClear();
	public String buttonIndex();
	public String buttonSubmit();
	public String buttonSelect();
	public String buttonSelectAll();
	public String buttonClearAll();
	public String buttonPrint();
	public String buttonApply();
	public String buttonAccept();
	public String buttonReject();
	public String buttonClose();
	public String buttonExpandAll();
	public String buttonCollapseAll();
	public String buttonSearch();
	public String buttonViewConceptTree();
	public String buttonViewSearchResult();
	
}