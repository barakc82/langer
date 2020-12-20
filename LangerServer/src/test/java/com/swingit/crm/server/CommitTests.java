//package com.swingit.crm.server;
//
//import com.google.appengine.api.datastore.EntityNotFoundException;
//import com.swingit.appserver.utils.ServerTestUtils;
//import com.swingit.common.services.LocalCache;
//import com.swingit.crm.server.legacy.Commit;
//import com.swingit.crm.server.legacy.GaeEntity;
//import com.swingit.crm.server.legacy.HLLServer;
//import com.swingit.crm.server.legacy.services.LocalTablesService;
//import com.swingit.crm.server.legacy.services.LocalWebService;
//import com.swingit.crm.server.legacy.services.WebServices;
//import com.swingit.crm.types.MetaData;
//import com.swingit.crm.server.legacy.registration.model.gae.types.GaeParticipation;
//import com.swingit.crm.server.legacy.registration.model.types.properties.CommonProperty;
//import com.swingit.crm.server.legacy.registration.model.types.properties.ParticipationProperty;
//import junit.framework.TestCase;
//import org.junit.Test;
//import swingit.server.crm.data.api.EntityType;
//
//import javax.mail.internet.AddressException;
//import java.io.IOException;
//
//
//public class CommitTests extends TestCase
//{
//	private static final LocalWebService localService = new LocalWebService();
//
//
//	protected void setUp() throws Exception
//	{
//		MetaData.getInstance().setUser("hllqaapp");
//		WebServices.registerServiceProvider(localService);
//		HLLServer.getInstance().initialize();
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
//	public void testConstructor()
//	{
//		new Commit(localService, null);
//	}
//
//	public void testPaymentCommit() throws AddressException, IOException, EntityNotFoundException
//	{
//		TestingTransaction transaction = new TestingTransaction();
//
//		GaeEntity payment = new GaeEntity(EntityType.Payment);
//		payment.set(CommonProperty.Id, 1);
//		payment.set(CommonProperty.Timestamp, 1);
//		transaction.add(payment);
//
//		GaeParticipation p = new GaeParticipation("1", "1", false, "Payment_0");
//		transaction.add(p);
//
//		Commit commit = new Commit(localService, transaction);
//		commit.execute();
//		boolean crash = localService.getReporter().readCrash();
//		assertFalse(crash);
//
//		GaeEntity pEntity = LocalTablesService.getInstance().getEntity(EntityType.Participation, 1);
//		Long paymentId = pEntity.getAsLongOrNull(ParticipationProperty.PaymentId);
//		assertNotNull(paymentId);
//		assertEquals(1, paymentId.longValue());
//	}
//}