package org.fao.aoscs.client;

import org.fao.aoscs.client.module.classification.service.ClassificationServiceAsync;
import org.fao.aoscs.client.module.classification.service.ClassificationService.ClassificationServiceUtil;
import org.fao.aoscs.client.module.comment.service.CommentServiceAsync;
import org.fao.aoscs.client.module.comment.service.CommentService.CommentServiceUtil;
import org.fao.aoscs.client.module.concept.service.ConceptServiceAsync;
import org.fao.aoscs.client.module.concept.service.ConceptService.ConceptServiceUtil;
import org.fao.aoscs.client.module.consistency.service.ConsistencyServiceAsync;
import org.fao.aoscs.client.module.consistency.service.ConsistencyService.ConsistencyServiceUtil;
import org.fao.aoscs.client.module.export.service.ExportServiceAsync;
import org.fao.aoscs.client.module.export.service.ExportService.ExportServiceUtil;
import org.fao.aoscs.client.module.logging.service.LoggingServiceAsync;
import org.fao.aoscs.client.module.logging.service.LoggingService.LoggingServiceUtil;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceServiceAsync;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.module.relationship.service.RelationshipServiceAsync;
import org.fao.aoscs.client.module.relationship.service.RelationshipService.RelationshipServiceUtil;
import org.fao.aoscs.client.module.search.service.SearchServiceAsync;
import org.fao.aoscs.client.module.search.service.SearchService.SearchServiceUtil;
import org.fao.aoscs.client.module.system.service.SystemServiceAsync;
import org.fao.aoscs.client.module.system.service.SystemService.SystemServiceUtil;
import org.fao.aoscs.client.module.term.service.TermServiceAsync;
import org.fao.aoscs.client.module.term.service.TermService.TermServiceUtil;
import org.fao.aoscs.client.module.validation.service.ValidationServiceAsync;
import org.fao.aoscs.client.module.validation.service.ValidationService.ValidationServiceUtil;
import org.fao.aoscs.client.query.service.QueryServiceAsync;
import org.fao.aoscs.client.query.service.QueryService.QueryServiceUtil;
import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeServiceAsync;
import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeService.TreeServiceUtil;

public class Service {
	public static SystemServiceAsync systemService = SystemServiceUtil.getInstance();
	public static ConceptServiceAsync conceptService = ConceptServiceUtil.getInstance();
	public static RelationshipServiceAsync relationshipService = RelationshipServiceUtil.getInstance();
	public static TermServiceAsync termService = TermServiceUtil.getInstance();
	public static TreeServiceAsync treeService = TreeServiceUtil.getInstance();
	public static SearchServiceAsync searchSerice = SearchServiceUtil.getInstance();
	public static ValidationServiceAsync validationService = ValidationServiceUtil.getInstance();
	public static ConsistencyServiceAsync consistencyService = ConsistencyServiceUtil.getInstance();
	public static ClassificationServiceAsync classificationService = ClassificationServiceUtil.getInstance();
	public static CommentServiceAsync commentService = CommentServiceUtil.getInstance();
	public static QueryServiceAsync queryService = QueryServiceUtil.getInstance();
	public static LoggingServiceAsync loggingService = LoggingServiceUtil.getInstance();
	public static ExportServiceAsync exportService = ExportServiceUtil.getInstance();
	public static UsersPreferenceServiceAsync userPreferenceService = UserPreferenceServiceUtil.getInstance();
}
