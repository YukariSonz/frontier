package uk.ac.imperial.lsds.seep.reliable;

import uk.ac.imperial.lsds.seep.GLOBALS;
import uk.ac.imperial.lsds.seep.processingunit.IProcessingUnit;
import uk.ac.imperial.lsds.seep.runtimeengine.TimestampTracker;

public class FailureCtrlWriter implements Runnable {

	private final IProcessingUnit processingUnit;
	private boolean goOn = true;

	public FailureCtrlWriter(IProcessingUnit processingUnit){
		this.processingUnit = processingUnit;
	}
	
	public void run(){
		int sleep = new Integer(GLOBALS.valueFor("ackEmitInterval"));
		while(goOn){
			
			processingUnit.getDispatcher().getNodeFailureCtrl();
			
			/*
			TimestampTracker currentTsV = processingUnit.getLastACK();
			System.out.println("ACKWorker: EmitACK");
			processingUnit.emitACK(currentTsV);
			*/
			
			processingUnit.emitFailureCtrl();
			
			try{
				Thread.sleep(sleep);
			}
			catch(InterruptedException ie){
				System.out.println("ACKWorker: while trying to sleep "+ie.getMessage());
				ie.printStackTrace();
			}
		}
	}
	
	public void stop(){
		this.goOn = false;
	}
}
