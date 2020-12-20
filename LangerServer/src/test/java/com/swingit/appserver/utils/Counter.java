package com.langer.appserver.utils;

public class Counter 
{
	private Integer count = 0;

	public void increment() 
	{
		System.out.println(Thread.currentThread().getId() + " entered");
		synchronized (count) 
		{
			System.out.println("currently: " + count);
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getId() + " done");
		}
	}
}
