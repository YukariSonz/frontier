package com.yammer.metrics.util;

import junit.framework.*;

/**
 * The class <code>AtomicGaugeTest</code> contains tests for the class <code>{@link AtomicGauge}</code>.
 *
 * @generatedBy CodePro at 18/10/13 19:02
 * @author rc3011
 * @version $Revision: 1.0 $
 */
public class AtomicGaugeTest extends TestCase {
	/**
	 * Run the AtomicGauge() constructor test.
	 *
	 * @generatedBy CodePro at 18/10/13 19:02
	 */
	public void testAtomicGauge_1()
		throws Exception {
		AtomicGauge result = new AtomicGauge();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Run the Object getValue() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18/10/13 19:02
	 */
	public void testGetValue_1()
		throws Exception {
		AtomicGauge fixture = new AtomicGauge();
		fixture.setValue((Object) null);

		Object result = fixture.getValue();

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the void setValue(T) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 18/10/13 19:02
	 */
	public void testSetValue_1()
		throws Exception {
		AtomicGauge fixture = new AtomicGauge();
		fixture.setValue((Object) null);

		fixture.setValue(null);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @see TestCase#setUp()
	 *
	 * @generatedBy CodePro at 18/10/13 19:02
	 */
	protected void setUp()
		throws Exception {
		super.setUp();
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @see TestCase#tearDown()
	 *
	 * @generatedBy CodePro at 18/10/13 19:02
	 */
	protected void tearDown()
		throws Exception {
		super.tearDown();
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 18/10/13 19:02
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			// Run all of the tests
			junit.textui.TestRunner.run(AtomicGaugeTest.class);
		} else {
			// Run only the named tests
			TestSuite suite = new TestSuite("Selected tests");
			for (int i = 0; i < args.length; i++) {
				TestCase test = new AtomicGaugeTest();
				test.setName(args[i]);
				suite.addTest(test);
			}
			junit.textui.TestRunner.run(suite);
		}
	}
}