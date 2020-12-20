//package com.swingit.appserver.utils;
//
//import com.swingit.common.DateUtils;
//import com.swingit.common.services.LocalDatastore;
//import com.swingit.common.services.LocalEntity;
//import com.swingit.common.services.LocalKey;
//import com.swingit.common.types.Community;
//import com.swingit.common.utils.TestRequest;
//import com.swingit.crm.CrmTransaction;
//import com.swingit.crm.server.legacy.services.CrmServerGlobals;
//import com.swingit.crm.server.legacy.services.LocalTablesService;
//import com.swingit.crm.types.MetaData;
//import com.swingit.crm.utils.CrmTestUtils;
//import com.swingit.crm.server.legacy.registration.model.gae.types.GaeCourse;
//import com.swingit.crm.server.legacy.registration.model.gae.types.GaeCourseOcc;
//import com.swingit.crm.server.legacy.registration.model.gae.types.GaeDancer;
//import com.swingit.crm.server.legacy.registration.model.gae.types.GaeSession;
//import com.swingit.crm.server.util.CrmServerUtils;
//import swingit.server.crm.data.api.EntityType;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.Map;
//
//public class ServerTestUtils
//{
//	private static LocalTablesService tables = LocalTablesService.getInstance();
//
//	public static TestRequest createSubmitRegFormReq(Map<String, Object> application) throws IOException
//	{
//		TestRequest req = new TestRequest();
//		req.setParam("action", "submit-reg-form");
//		req.setContents(application);
//		return req;
//	}
//
//	public static void addEntityToTransaction(CrmTransaction transaction, GaeDancer entity)
//	{
//		CrmServerUtils.addEntityToTransaction(transaction, entity.getEntity());
//	}
//
//	public static String[] createDateStrings(int length, int month, int year)
//	{
//		LocalDate[] dates = CrmTestUtils.createDates(length, month, year);
//		String[] dateString = new String[length];
//		for (int i = 0; i < length; i++)
//			dateString[i] = DateUtils.extractStr(dates[i]);
//		return dateString;
//	}
//
//	public static void initializeLocalDatastore()
//	{
//		EntityType[] types = MetaData.getInstance().getOrderedTypes();
//		LocalDatastore datastore = LocalDatastore.getInstance();
//		for (EntityType type : types)
//		{
//			Long keyId = MetaData.getInstance().getDatastoreKey(type.name());
//			if (keyId == null)
//			{
//				continue;
//			}
//			LocalKey key = datastore.createKey("Type", keyId);
//			LocalEntity entity = new LocalEntity(key);
//			entity.setUnindexedProperty(CrmServerGlobals.MAX_ID, 0L);
//			entity.setUnindexedProperty(CrmServerGlobals.CHANGE_TIME, new Date());
//			entity.setUnindexedProperty(CrmServerGlobals.NAME, type.name());
//			datastore.put(entity);
//		}
//
//		Long keyId = MetaData.getInstance().getVersionKey();
//		LocalKey key = datastore.createKey(CrmServerGlobals.REQUIRED_VERSION_TYPE, keyId);
//		LocalEntity entity = new LocalEntity(key);
//		entity.setUnindexedProperty(CrmServerGlobals.REQUIRED_VERSION_FIELD, 0L);
//		datastore.put(entity);
//	}
//
//	public static GaeCourseOcc createGaeCourseOcc()
//	{
//
//		long id = nextId(EntityType.Activity);
//		GaeCourse course = new GaeCourse(id, 1, "Course", 4, null, null);
//		tables.insert(course);
//
//		id = nextId(EntityType.ActivityOcc);
//		GaeCourseOcc co = new GaeCourseOcc(1, 1, course.getId(), Community.TLV, "");
//		tables.insert(co);
//
//		for (int i = 0; i < course.getLength(); i++)
//		{
//			id = nextId(EntityType.SessionOcc);
//			GaeSession session = new GaeSession(id, 1, co.getId(), i, (i+1) + "/1/37");
//			tables.insert(session);
//		}
//
//		return co;
//	}
//
//	public static long nextId(EntityType type)
//	{
//		return tables .getTable(type).selectAll().size() + 1;
//	}
//}