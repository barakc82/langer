package com.langer.crm.server;

import com.langer.server.legacy.registration.model.gae.types.GaeObject;
import com.langer.crm.CrmTransaction;
import com.langer.server.legacy.GaeEntity;
import com.langer.server.util.CrmServerUtils;

public class TestingTransaction extends CrmTransaction
{
	public TestingTransaction() {}

	public TestingTransaction(String clientId)
	{
		super(clientId);
	}
	
	public void add(GaeEntity entity) 
	{
		CrmServerUtils.addEntityToTransaction(this, entity);
	}

	public void add(GaeObject gaeObj) 
	{
		add(gaeObj.getEntity());
	}
}
