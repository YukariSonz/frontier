/*******************************************************************************
 * Copyright (c) 2013 Imperial College London.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Raul Castro Fernandez - initial design and implementation
 ******************************************************************************/
package uk.co.imperial.lsds.seep.processingunit;

import java.util.ArrayList;

import uk.co.imperial.lsds.seep.operator.State;

public class StreamStateChunk extends State{

	private static final long serialVersionUID = 1L;
	private ArrayList<Object> microBatch;
	
	public StreamStateChunk(){
	}
	
	public ArrayList<Object> getMicroBatch(){
		return microBatch;
	}
	
	public StreamStateChunk(ArrayList<Object> microBatch){
		this.microBatch = microBatch;
	}

}