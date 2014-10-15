package de.qabel.core.drop;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class DropListenerTest {

	static public class TestMO1 extends ModelObject {
		public String content;
	}

	static public class TestMO2 extends ModelObject {
		public String content;
	}

	@Test
	public void dropListenerTest() throws InterruptedException {

		TestMO1 mo1 = new TestMO1();
		mo1.content = "payload data";

		TestMO2 mo2 = new TestMO2();
		mo1.content = "payload data";

		DropController dc = new DropController();
		BlockingQueue<DropMessage<ModelObject>> bq1 = dc.register(mo1);
		BlockingQueue<DropMessage<ModelObject>> bq2 = dc.register(mo1);
		BlockingQueue<DropMessage<ModelObject>> bq3 = dc.register(mo2);
		
		DropMessage<TestMO1> dm = new DropMessage<TestMO1>();
		Date date = new Date(1412687357);

		dm.setTime(date);
		dm.setSender("foo");
		dm.setData(mo1);
		dm.setAcknowledgeID("bar");
		dm.setVersion(1);
		dm.setModelObject(TestMO1.class);

		dc.handleDrop(dm);
	
		assertEquals(dm, bq1.take());
		assertEquals(dm, bq2.take());
		assertNull(bq3.poll(1L, TimeUnit.MILLISECONDS));
	}
}
