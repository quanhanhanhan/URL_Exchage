package com.qh.Long_Short;
import junit.framework.TestCase;
public class changeTest extends TestCase{
	Change change;
	
	//��ʼ��
	protected void setUp() throws Exception
	{
		//����Change����
		change = new Change();
		super.setUp();
	}
	
	public void testmethod() {
		String hh = change.process_change();
		String ss = "http://www.sina.com";
		assertEquals(hh,ss);
		
	}
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
}