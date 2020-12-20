//package com.swingit.crm.server;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.swingit.appserver.utils.ServerTestUtils;
//import com.swingit.common.ApplicationException;
//import com.swingit.common.Hebrew;
//import com.swingit.common.services.LocalCache;
//import com.swingit.common.types.Community;
//import com.swingit.crm.server.legacy.services.LocalWebService;
//import com.swingit.crm.server.legacy.services.WebServiceProvider;
//import com.swingit.crm.server.legacy.services.WebServices;
//import com.swingit.crm.server.legacy.services.LocalTablesService;
//import com.swingit.crm.types.Course;
//import com.swingit.crm.types.CourseOcc;
//import com.swingit.crm.types.Dancer;
//import com.swingit.crm.types.MetaData;
//import com.swingit.crm.types.Registration;
//import com.swingit.crm.types.SessionOcc;
//import com.swingit.crm.types.enums.RegState;
//import com.swingit.crm.types.enums.ReqMet;
//import com.swingit.crm.types.enums.Role;
//import com.swingit.crm.utils.CrmTestUtils;
//
//import junit.framework.TestCase;
//
//
//public class SubmitRegFormTests extends TestCase //TODO yshabi make new tests
//{
//	protected void setUp() throws Exception
//	{
//		MetaData.getInstance().setUser("hllqaapp");
//		WebServiceProvider localService = new LocalWebService();
//		WebServices.registerServiceProvider(localService);
//		ServerTestUtils.initializeLocalDatastore();
//	}
//
//	protected void tearDown() throws Exception
//	{
//		LocalTablesService.getInstance().clear();
//		LocalCache.getInstance().clear();
//	}
//
//	@Test
//	public void testConstructor() throws IOException
//	{
//		Map<String, Object> application = new HashMap<>();
//		new SubmitRegForm(application);
//	}
//
//	@Test
//	public void testExistingReg() throws IOException, ApplicationException
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
//		CourseOcc co = CrmTestUtils.createCourseOcc(course , Community.TLV, 1, 17);
//		co.setFormName(courseName);
//		localService.getTablesService().insert(co);
//		for (SessionOcc so : co.getSessionOccurrences())
//			localService.getTablesService().insert(so);
//		Registration reg = new Registration(1, 1, d , co , Role.Leader, "", "", RegState.Approved, LocalDateTime.now(), ReqMet.Automatic);
//		localService.getTablesService().insert(reg);
////		HLLServer.getInstance().initialize(localService);
//
//		Map<String, Object> application = new HashMap<>();
//		application.put(Hebrew.fullName, "name");
//		application.put(Hebrew.phoneNum, "phone");
//		application.put(Hebrew.emailAddr, email);
//		application.put(Hebrew.birthdayDate, "1.1.99");
//		application.put(co.getFormName(), Role.Leader.getName());
//
//		SubmitRegForm submitRegForm = new SubmitRegForm(application);
//		submitRegForm.run();
//	}
//}