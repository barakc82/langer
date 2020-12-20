//package com.swingit.crm.server;
//
//import com.swingit.appserver.utils.ServerTestUtils;
//import com.swingit.common.ApplicationException;
//import com.swingit.common.services.LocalCache;
//import com.swingit.common.types.Community;
//import com.swingit.common.utils.TestRequest;
//import com.swingit.common.utils.TestResponse;
//import com.swingit.crm.CrmServerActionType;
//import com.swingit.crm.ObtainIdsResp;
//import com.swingit.crm.server.legacy.GaeEntity;
//import com.swingit.crm.server.legacy.HLLServer;
//import com.swingit.crm.server.legacy.services.LocalTablesService;
//import com.swingit.crm.server.legacy.services.LocalWebService;
//import com.swingit.crm.server.legacy.services.Table;
//import com.swingit.crm.server.legacy.services.WebServices;
//import com.swingit.crm.services.LocalReporter;
//import com.swingit.crm.types.MetaData;
//import com.swingit.crm.server.legacy.registration.model.gae.types.*;
//import junit.framework.TestCase;
//import org.junit.Test;
//import swingit.server.crm.data.api.EntityType;
//
//import java.io.IOException;
//import java.util.Collection;
//
//
//public class HLLServerTests extends TestCase
//{
//	private static final LocalWebService localService = new LocalWebService();
//	private static LocalReporter localReporter;
//
//	protected void setUp() throws Exception
//	{
//		MetaData.getInstance().setUser("hllqaapp");
//		WebServices.registerServiceProvider(localService);
//		HLLServer.getInstance().initialize();
//		localReporter = (LocalReporter) HLLServer.getInstance().getReporter();
//		ServerTestUtils.initializeLocalDatastore();
//	}
//
//	protected void tearDown() throws Exception
//	{
//		LocalTablesService.getInstance().clear();
//		LocalCache.getInstance().clear();
//	}
//
//	private GaeCourse addCourseBwithAasPrereq() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table activities = tables.getTable(EntityType.Activity);
//		GaeCourse courseB = new GaeCourse(2, "course B", 4, null, "1");
//		activities.insert(courseB);
//		addCourseOccurrence(courseB);
//		return courseB;
//	}
//
//	private GaeCourse addCourseA()
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table activities = tables.getTable(EntityType.Activity);
//		GaeCourse courseA = new GaeCourse(1, "course A", 4);
//		activities.insert(courseA);
//		addCourseOccurrence(courseA);
//		return courseA;
//	}
//
//	private GaeObject addCourseOccurrence(GaeCourse course)
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table activityOccurrences = tables.getTable(EntityType.ActivityOcc);
//		long id = activityOccurrences.generateId();
//		GaeCourseOcc co = new GaeCourseOcc(id, 1L, course.getId(), Community.TLV, "");
//		activityOccurrences.insert(co);
//
//		Table sessionsOccurrences = tables.getTable(EntityType.SessionOcc);
//		for (int i = 0; i < 4; i++)
//		{
//			long soId = sessionsOccurrences.generateId();
//			GaeSession so = new GaeSession(soId, 1L, co.getId(), i, id + ".1");
//			System.out.println(so.getEntity().getIdStr() + " " + course.getName() + " " + co.getId());
//			sessionsOccurrences.insert(so.getEntity());
//		}
//		return co;
//	}
//
//	private GaeDancer addStudentA()
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeDancer dancer = new GaeDancer(1L, 1L, "x", "y");
//		dancer.setEmail("studentA@x.com");
//		tables.insert(dancer);
//		return dancer;
//	}
///*
//	@Test
//	public void testConstructor() throws IOException, ApplicationException
//	{
//		HLLServer.getInstance();
//	}
//
//	@Test
//	public void testNoAccessToken() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		HttpServletRequest req = new TestRequest();
//		req.removeAttribute("Authorization");
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(403, resp.getStatus());
//	}
//
//	@Test
//	public void testAccessDeniedForPost() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		HttpServletRequest req = new TestRequest();
//		req.setAttribute("Authorization", "not authorized");
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(403, resp.getStatus());
//	}
//
//	@Test
//	public void testAccessDeniedForGet() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		HttpServletRequest req = new TestRequest();
//		req.setAttribute("Authorization", "not authorized");
//		HttpServletResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(403, resp.getStatus());
//	}
//
//	@Test
//	public void testEmptyRegisteration() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//	}
//
//	@Test
//	public void testMissingMail() throws IOException
//	{
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.fullName, "test");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//	}
//
//	@Test
//	public void testUnknwonCourse() throws IOException
//	{
//		addStudentA();
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		AlertType alert = localReporter.readAlert();
//		assertEquals(AlertType.UnrecognizedCourseName, alert);
//	}
//
//	@Test
//	public void testUnknwonCourseOcc() throws IOException
//	{
//		addStudentA();
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table activities = tables.getTable(EntityType.Activity);
//		GaeCourse courseA = new GaeCourse(1, "course A", 4);
//		activities.insert(courseA);
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		AlertType alert = localReporter.readAlert();
//		assertEquals(AlertType.CourseHasNoOccurrences, alert);
//	}
//
//	@Test
//	public void testMissingSessions() throws IOException
//	{
//		addStudentA();
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table activities = tables.getTable(EntityType.Activity);
//		GaeCourse courseA = new GaeCourse(1, "course A", 4);
//		activities.insert(courseA);
//		GaeObject coA = new GaeCourseOcc(1, 1L, courseA.getId());
//		Table activityOccurrences = tables.getTable(EntityType.ActivityOcc);
//		activityOccurrences.insert(coA);
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		boolean crash = localReporter.readCrash();
//		assertTrue(crash);
//	}
//
//	@Test
//	public void testUnknwonDancer() throws IOException
//	{
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		AlertType alert = localReporter.readAlert();
//		assertEquals(AlertType.UnrecognizedDancer, alert);
//	}
//
//	@Test
//	public void testUnknwonRole() throws IOException
//	{
//		addStudentA();
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "Some unknown role");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		AlertType alert = localReporter.readAlert();
//		assertEquals(AlertType.UnrecognizedRole, alert);
//	}
//
//	@Test
//	public void testSubmitRegForm() throws IOException, ApplicationException
//	{
//		addStudentA();
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//	}
//
//	@Test
//	public void testSubmitRegFormOverrideReg() throws IOException, ApplicationException
//	{
//		GaeDancer dancer = addStudentA();
//		GaeCourse courseA = addCourseA();
//
//		GaeRegistration reg = new GaeRegistration(1L, 1L, dancer.getId(), 1, Role.Leader, RegState.Approved);
//		LocalTablesService tables = LocalTablesService.getInstance();
//		tables.insert(reg);
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put(courseA.getName(), "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(2, registrations.size());
//		GaeEntity regEntity = regTable.get(1);
//		ReqMet reqMet = regEntity.getAsReqMet(RegProperty.ReqMet);
//		System.out.println(reqMet);
//		assertNotNull(reqMet);
//	}
//
//	@Test
//	public void testCommit() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		ServerTestUtils.addEntityToTransaction(transaction, dancer);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId("Dancer");
//		assertEquals(1, maxId);
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table dancerTable = tables.getTable(EntityType.Dancer);
//		Collection<GaeEntity> dancerEntities = dancerTable.selectAll();
//		assertEquals(1, dancerEntities.size());
//		GaeEntity dancerEntity = dancerEntities.iterator().next();
//		assertTrue(dancerEntity.getId() > -1);
//	}
//
//	@Test
//	public void testGetStatus() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
//	{
//		Status status = new Status();
//		status.setCurOpId(5L);
//		status.setRequiredVersion(100);
//		TypeProperties typeProperties = new TypeProperties(2304L, 200L);
//		status.put(EntityType.Dancer.name(), typeProperties);
//
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.UPDATE_STATUS);
//		req.setContents(status);
//
//		HLLServer server = HLLServer.getInstance();
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		req = new TestRequest();
//		req.setAction(CrmServerActionType.GET_STATUS);
//
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//		status = resp.getContents(Status.class);
//		assertEquals(5, status.getCurOpId());
//		assertEquals(100, status.getRequiredVersion());
//		typeProperties = status.get(EntityType.Dancer.name());
//		assertNotNull(typeProperties);
//		assertEquals(200L, typeProperties.getChangeTime());
//		assertEquals(5, status.getCurOpId());
//	}
//
//	@Test
//	public void testHeartbeat() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.HEARTBEAT);
//		req.setParam("last-known-op-id", "0");
//		req.setParam("client-id", "client1");
//
//		HttpServletResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//	}
//
//	public void testHeartbeatWithNoClientId() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.HEARTBEAT);
//		req.setParam("last-known-op-id", "0");
//
//		HttpServletResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(500, resp.getStatus());
//	}
//
//	@Test
//	public void testFailedRegFormSubmtssion() throws IOException, ApplicationException
//	{
//		addStudentA();
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//
//		tables.failOnNextAccess(new IOException("Tables service failed"));
//
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		assertTrue(localReporter.readCrash());
//	}
//
//	@Test
//	public void testGeneralFailedCommit() throws IOException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		ServerTestUtils.addEntityToTransaction(transaction, dancer);
//		req.setContents(transaction);
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		tables.failOnNextAccess(new IOException("Tables service failed"));
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		assertTrue(localReporter.readCrash());
//
//		Collection<GaeEntity> dancers = tables.selectAll(EntityType.Dancer);
//		assertTrue(dancers.isEmpty());
//	}
//
//	@Test
//	public void testConnectionExceptionCommit() throws IOException
//	{
//		IOException e = new IOException("Could not fetch URL");
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		ServerTestUtils.addEntityToTransaction(transaction, dancer);
//		req.setContents(transaction);
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		tables.failOnNextAccess(e);
//
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		assertFalse(localReporter.readCrash());
//
//		Collection<GaeEntity> dancers = tables.selectAll(EntityType.Dancer);
//		assertTrue(dancers.isEmpty());
//	}
//
//	@Test
//	public void testGoogleJsonRespFailedCommit() throws IOException, ApplicationException
//	{
//		IOException[] exceptions = new IOException[3];
//		GoogleJsonError jsonError = new GoogleJsonError();
//		jsonError.setCode(500);
//		Builder builder = new HttpResponseException.Builder(500, "retry", new HttpHeaders());
//		exceptions[0] = new GoogleJsonResponseException(builder, jsonError);
//
//		jsonError = new GoogleJsonError();
//		jsonError.setCode(503);
//		builder = new HttpResponseException.Builder(503, "retry", new HttpHeaders());
//		exceptions[1] = new GoogleJsonResponseException(builder, jsonError);
//
//		jsonError = new GoogleJsonError();
//		jsonError.setCode(0);
//		jsonError.setMessage("Rate Limit");
//		builder = new HttpResponseException.Builder(0, "retry", new HttpHeaders());
//		exceptions[2] = new GoogleJsonResponseException(builder, jsonError);
//
//		for (IOException e : exceptions)
//		{
//			HLLServer server = HLLServer.getInstance();
//			TestRequest req = new TestRequest();
//			req.setAction(CrmServerActionType.COMMIT);
//
//			TestingTransaction transaction = new TestingTransaction("test commit");
//			GaeDancer dancer = new GaeDancer("a", "b");
//			ServerTestUtils.addEntityToTransaction(transaction, dancer);
//			req.setContents(transaction);
//
//			LocalTablesService tables = LocalTablesService.getInstance();
//			tables.failOnNextAccess(e);
//
//			HttpServletResponse resp = new TestResponse();
//			server.doPost(req, resp);
//			assertEquals(200, resp.getStatus());
//			assertFalse(localReporter.readCrash());
//
//			Collection<GaeEntity> dancers = tables.selectAll(EntityType.Dancer);
//			assertTrue(dancers.isEmpty());
//		}
//	}
//
//	@Test
//	public void testPrereq() throws IOException
//	{
//		addStudentA();
//		addCourseA();
//		addCourseBwithAasPrereq();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//		GaeEntity reg = registrations.iterator().next();
//		RegState regState = reg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Waiting, regState);
//	}
//
//	@Test
//	public void testUpdate() throws IOException, InstantiationException, IllegalAccessException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		transaction.add(dancer.getEntity());
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		req = new TestRequest();
//		req.setAction(CrmServerActionType.HEARTBEAT);
//		req.setParam("last-known-op-id", "0");
//		req.setParam("client-id", "client2");
//
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//		UpdateResponse updateResp = resp.getContents(UpdateResponse.class);
//		assertEquals(1, updateResp.getCurOpId());
//		assertEquals(1, updateResp.getOperations().size());
//	}
//
//	@Test
//	public void testUpdateMaxIds() throws ClientProtocolException, IOException, ApplicationException, URISyntaxException, InstantiationException, IllegalAccessException
//	{
//		Status status = new Status();
//		TypeProperties typeProperties = new TypeProperties(2304L, 0L);
//		status.put("Dancer", typeProperties);
//
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.UPDATE_STATUS);
//		req.setContents(status);
//
//		HLLServer server = HLLServer.getInstance();
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		req = new TestRequest();
//		req.setType("Dancer");
//		req.setAction(CrmServerActionType.GET_MAX_ID);
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//		long maxId = resp.getContents(Long.class);
//		assertEquals(2304L, maxId);
//
//		req = new TestRequest();
//		req.setType("Dancer");
//		req.setAction(CrmServerActionType.NEXT_ID);
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//		long nextId = resp.getContents(Long.class);
//		assertEquals(2305L, nextId);
//	}
//
//	@Test
//	public void testSetMaxIds() throws ClientProtocolException, IOException, ApplicationException, URISyntaxException, InstantiationException, IllegalAccessException
//	{
//		for (EntityType type : MetaData.getInstance().getOrderedTypes())
//		{
//			Table table = LocalTablesService.getInstance().getTable(type);
//			GaeEntity swingit.server.crm.data.api.entity = new GaeEntity(type);
//			swingit.server.crm.data.api.entity.set("id", "1");
//			table.insert(swingit.server.crm.data.api.entity);
//		}
//
//		Status maxIds = new Status();
//		TypeProperties typeProperties = new TypeProperties(2304L, 0);
//		maxIds.put("Dancer", typeProperties);
//
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.SET_MAX_IDS);
//
//		HLLServer server = HLLServer.getInstance();
//		TestResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		req = new TestRequest();
//		req.setType("Dancer");
//		req.setAction(CrmServerActionType.GET_MAX_ID);
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//		long maxId = resp.getContents(Long.class);
//		assertEquals(1L, maxId);
//	}
//
//	@Test
//	public void testReferencedCommit() throws IOException
//	{
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		transaction.add(dancer);
//		GaeRegistration reg = new GaeRegistration("Dancer_0", "1", Role.Leader, RegState.Approved);
//		transaction.add(reg);
//
//		req.setContents(transaction);
//
//		HttpServletResponse resp = new TestResponse();
//		LocalTablesService tables = LocalTablesService.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		boolean crash = localReporter.readCrash();
//		assertFalse(crash);
//
//		Collection<GaeEntity> dancerEntities = tables.selectAll(EntityType.Dancer);
//		assertEquals(1, dancerEntities.size());
//		GaeEntity dancerEntity = dancerEntities.iterator().next();
//		assertTrue(dancerEntity.getId() > -1);
//
//		Collection<GaeEntity> regEntities = tables.selectAll(EntityType.Registration);
//		assertEquals(1, regEntities.size());
//		GaeEntity regEntity = regEntities.iterator().next();
//		assertTrue(regEntity.getId() > 0);
//		assertEquals(dancerEntity.getId(), regEntity.getAsLong(RegProperty.DancerId));
//	}
//
//	@Test
//	public void testImport() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer1 = new GaeDancer("a", "b");
//		transaction.add(dancer1);
//		GaeDancer dancer2 = new GaeDancer("c", "d");
//		transaction.add(dancer2);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId("Dancer");
//		assertEquals(2, maxId);
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table dancerTable = tables.getTable(EntityType.Dancer);
//		Collection<GaeEntity> dancerEntities = dancerTable.selectAll();
//		assertEquals(2, dancerEntities.size());
//		GaeEntity dancerEntity = dancerEntities.iterator().next();
//		assertTrue(dancerEntity.getId() > -1);
//	}
//
//	@Test
//	public void testAutomaticReqMetRegistrations() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeDancer dancer = addStudentA();
//		addCourseA();
//		addCourseBwithAasPrereq();
//
//		GaeRegistration reg = new GaeRegistration(1L, 1L, dancer.getId(), 1, Role.Leader, RegState.Approved);
//		tables.insert(reg);
//
//		GaeParticipation p = new GaeParticipation(1L, 1L, dancer.getIdStr(), "1", true, "");
//		tables.insert(p);
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		GaeEntity newReg = regTable.get(1);
//		assertNotNull(newReg);
//
//		RegState regState = newReg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Waiting, regState);
//	}
//
//	@Test
//	public void testNewCourseOcc() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeCourse course = new GaeCourse(1, "new course", 4);
//		tables.insert(course);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeCourseOcc co = new GaeCourseOcc(course.getId());
//		GaeSession so = new GaeSession(-1, 1, co.getId(), 0, "1.1.17");
//		transaction.add(co);
//		transaction.add(so);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId("ActivityOcc");
//		assertEquals(1, maxId);
//		maxId = obtainIdsResp.getMaxId("SessionOcc");
//		assertEquals(1, maxId);
//	}
//
//	@Test
//	public void testRegReport() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
//	{
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		transaction.add(dancer);
//		GaeRegistration reg = new GaeRegistration(dancer.getId(), 1, Role.Leader, RegState.Approved);
//		transaction.add(reg);
//		GaeParticipation p = new GaeParticipation(dancer.getIdStr(), "1", true, "");
//		transaction.add(p);
//
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId("Dancer");
//		assertEquals(1, maxId);
//		maxId = obtainIdsResp.getMaxId("Registration");
//		assertEquals(1, maxId);
//	}
//*/
////	@Test
////	@Ignore
////	public void testObjectResolution() throws IOException
////	{
////		LocalTablesService tables = LocalTablesService.getInstance();
////		addCourseA();
////
////		GaeDancer dancerVer1 = new GaeDancer(1, 1L, "a", "b");
////		dancerVer1.setEmail("a@b.com");
////		dancerVer1.setPhoneNum("123");
////		dancerVer1.setDefaultRole(Role.Leader);
////		tables.insert(dancerVer1);
////		GaeDancer dancerVer2 = new GaeDancer(1, 2L, "a", "b");
////		dancerVer2.setEmail("a@b.com");
////		dancerVer2.setDefaultRole(Role.Leader);
////		tables.insert(dancerVer2);
////
////		Map<String, Object> application = new HashMap<>();
////		application.put(Hebrew.emailAddr, "a@b.com");
////		application.put(Hebrew.phoneNum, "456");
////		application.put("course A", "[Leader]");
////
////		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
////		HttpServletResponse resp = new TestResponse();
////		HLLServer server = HLLServer.getInstance();
////		server.doPost(req, resp);
////		assertEquals(200, resp.getStatus());
////
////		Collection<GaeEntity> dancers = tables.getTable(EntityType.Dancer).selectAll();
////		assertEquals(2, dancers.size());
////
////		GaeDancer dancerVer3 = new GaeDancer(2, 1L, "c", "d");
////		dancerVer3.setEmail("b@b.com");
////		dancerVer3.setDefaultRole(Role.Leader);
////		tables.insert(dancerVer3);
////		GaeDancer dancerVer4 = new GaeDancer(2, 2L, "c", "d");
////		dancerVer4.setEmail("b@b.com");
////		dancerVer4.setPhoneNum("123");
////		dancerVer4.setDefaultRole(Role.Leader);
////		tables.insert(dancerVer4);
////
////		application = new HashMap<>();
////		application.put(Hebrew.emailAddr, "b@b.com");
////		application.put(Hebrew.phoneNum, "456");
////		application.put("course A", "[Leader]");
////
////		req = ServerTestUtils.createSubmitRegFormReq(application);
////		resp = new TestResponse();
////		server.doPost(req, resp);
////		assertEquals(200, resp.getStatus());
////
////		assertEquals(4, dancers.size());
////	}
//
////	@Test
////	public void testLargeCommit() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
////	{
////		HLLServer server = HLLServer.getInstance();
////		TestRequest req = new TestRequest();
////		req.setAction(CrmServerActionType.COMMIT);
////
////		TestingTransaction transaction = new TestingTransaction("test commit");
////		int numOfDancers = 1000;
////		for (int i = 0; i < numOfDancers; i++)
////		{
////			GaeDancer dancer = new GaeDancer("a" + i, "b" + i);
////			transaction.add(dancer);
////		}
////		req.setContents(transaction);
////
////		TestResponse resp = new TestResponse();
////		server.doPost(req, resp);
////		assertEquals(200, resp.getStatus());
////
////		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
////		long maxId = obtainIdsResp.getMaxId("Dancer");
////		assertEquals(numOfDancers, maxId);
////
////		LocalTablesService tables = LocalTablesService.getInstance();
////		Table dancerTable = tables.getTable(EntityType.Dancer);
////		Collection<GaeEntity> dancerEntities = dancerTable.selectAll();
////		assertEquals(numOfDancers, dancerEntities.size());
////	}
///*
//	@Test
//	public void testPositiveReqMetRegistrations() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeDancer dancer = addStudentA();
//		addCourseA();
//		addCourseBwithAasPrereq();
//
//		GaeRegistration reg = new GaeRegistration(1L, 1L, dancer.getId(), 1, Role.Leader, RegState.Approved, ReqMet.Yes);
//		tables.insert(reg);
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		GaeEntity newReg = regTable.get(1);
//		assertNotNull(newReg);
//
//		RegState regState = newReg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Approved, regState);
//	}
//
//	@Test
//	public void testReqFailureForPost() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setFailure("action");
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(500, resp.getStatus());
//	}
//
//	@Test
//	public void testReqFailureForGet() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setFailure("action");
//		HttpServletResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(500, resp.getStatus());
//	}
///*
//	@Test
//	public void testIdentifyCourseOccWithCommunityName() throws IOException, ApplicationException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//
//		addStudentA();
//		addCourseA();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A - Tel Aviv", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//	}
//
//	@Test
//	public void testFormName() throws IOException, ApplicationException
//	{
//		addStudentA();
//		addCourseA();
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeEntity co = tables.getTable(EntityType.ActivityOcc).selectAll().iterator().next();
//		co.set("form_name", "course C");
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course C", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//		GaeEntity reg = registrations.iterator().next();
//		assertEquals(co.getId(), reg.getAsLong(RegProperty.CourseOccId));
//	}
//
//	@Test
//	public void testCommitHllObj() throws IOException, InstantiationException, IllegalAccessException
//	{
//		Course course = new Course(-1, 0, "course A", DanceType.Balboa, CourseType.Basic, 4, false,
//				Collections.<Course> emptyList(), false);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeEntity swingit.server.crm.data.api.entity = new GaeEntity(EntityType.Activity, course.getProperties());
//		transaction.add(swingit.server.crm.data.api.entity);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId("Activity");
//		assertEquals(1, maxId);
//	}
//
//	@Test
//	public void testCommitPrivate() throws IOException, InstantiationException, IllegalAccessException
//	{
//		Payment payment = new Payment(220, PaymentMethod.Cash, null, Calendar.getInstance());
//		GaePrivate gaePrivate = new GaePrivate(payment.getId());
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		transaction.add(gaePrivate);
//		GaeEntity paymentEntity = new GaeEntity(EntityType.Payment, payment.getProperties());
//		transaction.add(paymentEntity);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId(EntityType.Private.name());
//		assertEquals(1, maxId);
//		maxId = obtainIdsResp.getMaxId(EntityType.Payment.name());
//		assertEquals(1, maxId);
//	}
//	/*
//	@Test
//	public void testFulfilledRequirements() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeDancer dancer = addStudentA();
//		GaeCourse courseA = addCourseA();
//		addCourseBwithAasPrereq();
//
//		GaeFulfilledRequirement fr = new GaeFulfilledRequirement(1L, 1L, dancer.getIdStr(), courseA.getIdStr());
//		tables.insert(fr);
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Solo]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//
//		GaeEntity newReg = registrations.iterator().next();
//		RegState regState = newReg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Approved, regState);
//	}
//
//	@Test
//	public void testUpdateDateOfBirth() throws IOException, ApplicationException
//	{
//		GaeDancer dancerA = addStudentA();
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put(Hebrew.birthdayDate, "1.1");
//		application.put("course A", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table dancerTable = tables.getTable(EntityType.Dancer);
//		Collection<GaeEntity> dancers = dancerTable.selectAll();
//		assertEquals(2, dancers.size());
//		GaeEntity dancerEntity = dancerTable.get(dancerA.getId());
//		assertEquals("1.1", dancerEntity.get("date_of_birth"));
//	}
//
//	@Test
//	public void testUnknownAction() throws IOException
//	{
//		TestRequest req = new TestRequest();
//		req.setType("Dancer");
//		req.setAction(CrmServerActionType.COMMIT);
//		TestResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doGet(req, resp);
//		assertEquals(400, resp.getStatus());
//	}
//
//	@Test
//	public void testPrereqWithNoRegistrations() throws IOException
//	{
//		addStudentA();
//		addCourseA();
//		addCourseBwithAasPrereq();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//		GaeEntity reg = registrations.iterator().next();
//		RegState regState = reg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Waiting, regState);
//	}
//
//	public void testSubmitRegFormSoloRole() throws IOException, ApplicationException
//	{
//		addStudentA();
//		addCourseA();
//
//		HLLServer server = HLLServer.getInstance();
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course A", "[Solo]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//	}
///*
//	@Test
//	public void testApprovedAutomaticReqMetRegistrations() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeDancer dancer = addStudentA();
//
//		Table activities = tables.getTable(EntityType.Activity);
//		GaeCourse courseA = new GaeCourse(100, "course A", 4);
//		activities.insert(courseA);
//		addCourseOccurrence(courseA);
//
//		GaeCourse courseB = new GaeCourse(2, "course B", 4, null, "100");
//		activities.insert(courseB);
//		addCourseOccurrence(courseB);
//
//		GaeRegistration reg = new GaeRegistration(1L, 1L, dancer.getId(), 1, Role.Leader, RegState.Approved);
//		tables.insert(reg);
//
//		for (int i = 0; i < courseA.getLength(); i++)
//		{
//			GaeParticipation p = new GaeParticipation(i, 1L, dancer.getIdStr(), "1", true, "");
//			tables.insert(p);
//		}
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		HLLServer server = HLLServer.getInstance();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		GaeEntity newReg = regTable.get(1);
//		assertNotNull(newReg);
//		RegState regState = newReg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Approved, regState);
//	}
//
//	@Test
//	public void testHandleNan() throws IOException, ApplicationException, InstantiationException, IllegalAccessException
//	{
//		List<String> columns =
//				Arrays.asList(DancerProperty.FirstName.getName(), DancerProperty.LastName.getName(), DancerProperty.Blocks.getName());
//		GaeEntity dancer = new GaeEntity(EntityType.Dancer, columns, Arrays.asList((Object) "a", "b", "NaN"));
//		assertEquals("a", dancer.get("first_name"));
//		assertEquals("", dancer.get("blocks"));
//	}
//
//	@Test
//	public void testResultCollection() throws IOException
//	{
//		addStudentA();
//		addCourseA();
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		Table activities = tables.getTable(EntityType.Activity);
//		GaeCourse courseB = new GaeCourse(2, 2, "course B", 4, null, "");
//		activities.insert(courseB);
//		addCourseOccurrence(courseB);
//		courseB = new GaeCourse(2, 1, "course B", 4, null, "1");
//		activities.insert(courseB);
//
//		HLLServer server = HLLServer.getInstance();
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.emailAddr, "studentA@x.com");
//		application.put("course B", "[Leader]");
//
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Table regTable = tables.getTable(EntityType.Registration);
//		Collection<GaeEntity> registrations = regTable.selectAll();
//		assertEquals(1, registrations.size());
//		GaeEntity reg = registrations.iterator().next();
//		RegState state = reg.getAsRegState(RegProperty.State);
//		assertEquals(RegState.Approved, state);
//	}
//
//	@Test
//	public void testCommitAvoidUpdate() throws IOException, InstantiationException, IllegalAccessException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("client1");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		transaction.add(dancer);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		ObtainIdsResp obtainIdsResp = resp.getContents(ObtainIdsResp.class);
//		long maxId = obtainIdsResp.getMaxId("Dancer");
//		assertEquals(1, maxId);
//
//		Table dancerTable = tables.getTable(EntityType.Dancer);
//		Collection<GaeEntity> dancerEntities = dancerTable.selectAll();
//		assertEquals(1, dancerEntities.size());
//		GaeEntity dancerEntity = dancerEntities.iterator().next();
//		assertTrue(dancerEntity.getId() > -1);
//
//		req = new TestRequest();
//		req.setAction(CrmServerActionType.HEARTBEAT);
//		req.setParam("last-known-op-id", "0");
//		req.setParam("client-id", "client1");
//
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//		UpdateResponse updateResp = resp.getContents(UpdateResponse.class);
//		assertTrue(updateResp.getOperations().isEmpty());
//	}
//
//	@Test
//	public void testUnknownUser() throws IOException, InstantiationException, IllegalAccessException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.VERIFY_CREDENTIALS);
//
//		Credentials credentials = new Credentials("a", "b");
//		req.setContents(credentials);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		VerifyCredentialsResponse credResp = resp.getContents(VerifyCredentialsResponse.class);
//		assertEquals(VerifyCredentialsResponse.UserUnknown, credResp);
//	}
//
//	@Test
//	public void testWrongPassword() throws IOException, InstantiationException, IllegalAccessException, ApplicationException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		User user = new User(1, 1, "a", "b".hashCode(), UserRole.TeamMember);
//		tables.insert(user);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.VERIFY_CREDENTIALS);
//
//		Credentials credentials = new Credentials("a", "c");
//		req.setContents(credentials);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		VerifyCredentialsResponse credResp = resp.getContents(VerifyCredentialsResponse.class);
//		assertEquals(VerifyCredentialsResponse.WrongPassword, credResp);
//	}
//
//	@Test
//	public void testRegisterUser() throws IOException, InstantiationException, IllegalAccessException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.REGISTER_USER);
//
//		Credentials credentials = new Credentials("a", "b");
//		req.setContents(credentials);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		RegisterUserResp regUserResp = resp.getContents(RegisterUserResp.class);
//		assertEquals(RegisterUserResp.OK, regUserResp);
//	}
//
//	@Test
//	public void testUserAlreadyExists() throws IOException, InstantiationException, IllegalAccessException, ApplicationException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		User user = new User(1, 1, "a", 1, UserRole.TeamMember);
//		tables.insert(user);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.REGISTER_USER);
//
//		Credentials credentials = new Credentials("a", "b");
//		req.setContents(credentials);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		RegisterUserResp regUserResp = resp.getContents(RegisterUserResp.class);
//		assertEquals(RegisterUserResp.UserAlreadyExists, regUserResp);
//	}
//
//	@Test
//	public void testSignIn() throws IOException, InstantiationException, IllegalAccessException, ApplicationException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		User user = new User(1, 1, "a", "b".hashCode(), UserRole.TeamMember);
//		tables.insert(user);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.VERIFY_CREDENTIALS);
//
//		Credentials credentials = new Credentials("a", "b");
//		req.setContents(credentials);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		VerifyCredentialsResponse credResp = resp.getContents(VerifyCredentialsResponse.class);
//		assertEquals(VerifyCredentialsResponse.OK, credResp);
//	}
//
//	@Test
//	public void testSignInPermDenied() throws IOException, InstantiationException, IllegalAccessException, ApplicationException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		User user = new User(1, 1, "a", "b".hashCode(), UserRole.Unauthorized);
//		tables.insert(user);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.VERIFY_CREDENTIALS);
//
//		Credentials credentials = new Credentials("a", "b");
//		req.setContents(credentials);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		VerifyCredentialsResponse credResp = resp.getContents(VerifyCredentialsResponse.class);
//		assertEquals(VerifyCredentialsResponse.PermissionDenied, credResp);
//	}
//
//	@Test
//	public void testUpdateRequiredVersion() throws IOException, InstantiationException, IllegalAccessException
//	{
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.UPDATE_REQUIRED_VERSION);
//		req.setParam("version", "123");
//
//		HLLServer server = HLLServer.getInstance();
//		TestResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		req = new TestRequest();
//		req.setAction(CrmServerActionType.GET_STATUS);
//		resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		Status status = resp.getContents(Status.class);
//		assertEquals(123, status.getRequiredVersion());
//	}
//
//	@Test
//	public void testRegSpreadsheetTriggerUpdateByApprovedReg() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		addCourseA();
//		addCourseBwithAasPrereq();
//		GaeRegistration reg = new GaeRegistration(1L, 1L, Role.Leader, RegState.Removed);
//		tables.insert(reg);
//
//		GaeEntity co = tables.getEntity(EntityType.ActivityOcc, 1);
//		co.set("reg_spreadsheet_id", "abcd");
//		co.set(ActivityOccProperty.SheetId, 1234);
//		addStudentA();
//		GaeDancer d = addStudentA();
//		List<String> leaderNames = Arrays.asList(d.getName());
//
//		ExpectedUpdateSheetReq expectedUpdateSheetReq = new ExpectedUpdateSheetReq("abcd", 1234, leaderNames, 1);
//		localService.getSheetsService().expect(expectedUpdateSheetReq);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		reg = new GaeRegistration(1L, 1L, Role.Leader, RegState.Approved);
//		transaction.add(reg);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		boolean crash = localReporter.readCrash();
//		assertFalse(crash);
//	}
//
//	@Test
//	public void testRegSpreadsheetTriggerUpdateByWaitingReg() throws IOException
//	{
//		LocalTablesService tables = LocalTablesService.getInstance();
//		addCourseA();
//
//		GaeEntity co = tables.getEntity(EntityType.ActivityOcc, 1);
//		co.set("reg_spreadsheet_id", "abcd");
//		co.set(ActivityOccProperty.SheetId, 1234);
//		GaeDancer d = addStudentA();
//		List<String> leaderNames = Arrays.asList(d.getName());
//
//		ExpectedUpdateSheetReq expectedUpdateSheetReq = new ExpectedUpdateSheetReq("abcd", 1234, leaderNames, 0);
//		localService.getSheetsService().expect(expectedUpdateSheetReq);
//
//		TestingTransaction transaction = new TestingTransaction("test commit");
//		GaeRegistration reg = new GaeRegistration(1L, 1, Role.Leader, RegState.Waiting);
//		transaction.add(reg);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		boolean crash = localReporter.readCrash();
//		assertFalse(crash);
//	}
//
//	@Test
//	public void testTriggerRegSpreadsheetUpdateDirectly() throws IOException
//	{
//		addCourseA();
//
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.TRIGGER_REG_SPREADSHEET_UPDATE);
//		req.setParam("course-occ", "1");
//
//		HLLServer server = HLLServer.getInstance();
//		TestResponse resp = new TestResponse();
//		server.doGet(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		boolean crash = localReporter.readCrash();
//		assertFalse(crash);
//	}
//
//	@Test
//	public void testActionTypeWebFormat()
//	{
//		assertEquals("get-max-id", CrmServerActionType.GET_MAX_ID.toWebFormat());
//	}
//
//	@Test
//	public void testEnums()
//	{
//		RegisterUserResp.valueOf(RegisterUserResp.values()[0].toString());
//		VerifyCredentialsResponse.valueOf(VerifyCredentialsResponse.values()[0].toString());
//	}
//
//
//	@Test
//	public void testSubmitRegFormExistingReg() throws IOException, ApplicationException
//	{
//		MetaData.getInstance().setUser("hllqaapp");
//		LocalWebService localService = new LocalWebService();
//		Dancer d = new Dancer("a", "b");
//		String email = "ab@cd.com";
//		d.setEmail(email);
//		localService.getTablesService().insert(d);
//
//		String courseName = "course";
//		Course course = new Course(courseName, 8);
//		localService.getTablesService().insert(course);
//		CourseOcc co = CrmTestUtils.createCourseOcc(course, Community.TLV, 1, 17);
//		co.setFormName(courseName);
//		localService.getTablesService().insert(co);
//		for (SessionOcc so : co.getSessionOccurrences())
//			localService.getTablesService().insert(so);
//		Registration reg = new Registration(1, 1, d , co , Role.Leader, "", "", RegState.Approved, 1, ReqMet.Automatic);
//		localService.getTablesService().insert(reg);
//		//HLLServer.getInstance().initialize(localService);
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.fullName, "name");
//		application.put(Hebrew.phoneNum, "phone");
//		application.put(Hebrew.emailAddr, email);
//		application.put(Hebrew.birthdayDate, "1.1.99");
//		application.put(co.getFormName(), Role.Leader.getName());
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = ServerTestUtils.createSubmitRegFormReq(application);
//		HttpServletResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		assertFalse(localReporter.readCrash());
//	}
//
//	@Test
//	public void testResponseCache() throws IOException, ApplicationException
//	{
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("client1");
//		GaeDancer dancer = new GaeDancer("a", "b");
//		transaction.add(dancer);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//
//		transaction = new TestingTransaction("client1");
//		dancer = new GaeDancer("a", "b");
//		transaction.add(dancer);
//		req.setContents(transaction);
//
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//	}
//
//	@Test
//	public void testTriggerRegSpreadSheetUpdateWithRemovedReg() throws IOException, ApplicationException
//	{
//		GaeDancer d = addStudentA();
//		addCourseA();
//
//		LocalTablesService tables = LocalTablesService.getInstance();
//		GaeEntity co = tables.getEntity(EntityType.ActivityOcc, 1);
//		co.set("reg_spreadsheet_id", "abcd");
//		co.set(ActivityOccProperty.SheetId, 1234);
//
//		Table registrations = tables.getTable(EntityType.Registration);
//		GaeRegistration reg = new GaeRegistration(d.getId(), 1, Role.Leader, RegState.Approved);
//		registrations.insert(reg);
//
//		List<String> emptyList = Arrays.<String>asList();
//		ExpectedUpdateSheetReq expectedUpdateSheetReq = new ExpectedUpdateSheetReq("abcd", 1234, emptyList, 0);
//		localService.getSheetsService().expect(expectedUpdateSheetReq);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//
//		TestingTransaction transaction = new TestingTransaction("client1");
//		reg = new GaeRegistration(reg.getId(), reg.getTimestamp() + 1, null, 1, Role.Leader, RegState.Approved);
//		transaction.add(reg);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		assertFalse(localReporter.readCrash());
//	}
//
//	public void testPaymentCommit() throws IOException
//	{
//		GaeDancer d = addStudentA();
//		addCourseA();
//
//		TestingTransaction transaction = new TestingTransaction();
//
//		GaeEntity payment = new GaeEntity(EntityType.Payment);
//		payment.set(CommonProperty.Id, 1);
//		payment.set(CommonProperty.Timestamp, 1);
//		transaction.add(payment);
//
//		GaeParticipation p = new GaeParticipation(d.getIdStr(), "1", false, "Payment_0");
//		transaction.add(p);
//
//		HLLServer server = HLLServer.getInstance();
//		TestRequest req = new TestRequest();
//		req.setAction(CrmServerActionType.COMMIT);
//		req.setContents(transaction);
//
//		TestResponse resp = new TestResponse();
//		server.doPost(req, resp);
//		assertEquals(200, resp.getStatus());
//		assertFalse(localReporter.readCrash());
//
//		GaeEntity pEntity = LocalTablesService.getInstance().getEntity(EntityType.Participation, 1);
//		Long paymentId = pEntity.getAsLongOrNull(ParticipationProperty.PaymentId);
//		assertNotNull(paymentId);
//		assertEquals(1, paymentId.longValue());
//	}*/
//}