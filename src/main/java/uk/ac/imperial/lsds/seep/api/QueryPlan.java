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
package uk.ac.imperial.lsds.seep.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.imperial.lsds.seep.P;
import uk.ac.imperial.lsds.seep.infrastructure.NodeManager;
import uk.ac.imperial.lsds.seep.infrastructure.master.Node;
import uk.ac.imperial.lsds.seep.operator.Operator;
import uk.ac.imperial.lsds.seep.operator.QuerySpecificationI;
import uk.ac.imperial.lsds.seep.state.CustomState;
import uk.ac.imperial.lsds.seep.state.LargeState;
import uk.ac.imperial.lsds.seep.state.Partitionable;
import uk.ac.imperial.lsds.seep.state.State;
import uk.ac.imperial.lsds.seep.state.StateWrapper;

public class QueryPlan {
	
	final private Logger LOG = LoggerFactory.getLogger(QueryPlan.class);
	
	public static final int CONTROL_SOCKET = Integer.parseInt(P.valueFor("controlSocket"));
	public static final int DATA_SOCKET = Integer.parseInt(P.valueFor("dataSocket"));
	
	private ArrayList<Operator> ops = new ArrayList<Operator>();
	private ArrayList<StateWrapper> states = new ArrayList<StateWrapper>();
	private ArrayList<ScaleOutIntentBean> scIntents = new ArrayList<ScaleOutIntentBean>();
	private Map<Operator, Integer> partitionRequirements = new LinkedHashMap<Operator, Integer>(0);
	public Map<Integer, QuerySpecificationI> elements = new HashMap<Integer, QuerySpecificationI>();
	//More than one source is supported
	private ArrayList<Operator> src = new ArrayList<Operator>();
	private Operator snk;
	//Mapping of operators to node
	private Map<Integer, ArrayList<Operator>> mapOperatorToNode = new LinkedHashMap<Integer, ArrayList<Operator>>();
	
	public ArrayList<Operator> getOps() {
		return ops;
	}
	
	public ArrayList<StateWrapper> getStates(){
		return states;
	}
	
	public ArrayList<ScaleOutIntentBean> getScaleOutIntents(){
		return scIntents;
	}

	public Map<Integer, QuerySpecificationI> getElements() {
		return elements;
	}
	
	public Map<Operator, Integer> getPartitionRequirements(){
		return partitionRequirements;
	}

	public ArrayList<Operator> getSrc() {
		return src;
	}

	public Operator getSnk() {
		return snk;
	}
	
	public Map<Integer, ArrayList<Operator>> getMapOperatorToNode(){
		return mapOperatorToNode;
	}
	
	/** User facing methods **/
	
	public Operator newStatefulSource(Operator op, int opId, StateWrapper s, List<String> attributes){
		// Configure operator
		if(s.getOwnerId() != opId){
			LOG.error("Operator id: "+opId+" does not own state: "+s.getOwnerId());
			System.exit(0);
		}
		this.newStatefulOperator(op, opId, s, attributes);
		this.setSource(op);
		
		op.getOpContext().setIsSource(true);
		return op;
	}
	
	public Operator newStatelessSource(Operator op, int opId, List<String> attributes){
		// Configure operator
		this.newStatelessOperator(op, opId, attributes);
		this.setSource(op);
		
		op.getOpContext().setIsSource(true);
		return op;
	}
	
	public Operator newStatefulSink(Operator op, int opId, StateWrapper s, List<String> attributes){
		// Configure operator
		if(s.getOwnerId() != opId){
			LOG.error("Operator id: "+opId+" does not own state: "+s.getOwnerId());
			System.exit(0);
		}
		this.newStatefulOperator(op, opId, s, attributes);
		this.setSink(op);
		
		op.getOpContext().setIsSink(true);
		
		return op;
	}
	
	public Operator newStatelessSink(Operator op, int opId, List<String> attributes){
		this.newStatelessOperator(op, opId, attributes);
		this.setSink(op);
		
		op.getOpContext().setIsSink(true);
		
		return op;
	}
	
	public Operator newStatelessOperator(Operator op, int opId, List<String> attributes){
		// Configure operator
		op.setOperatorId(opId);
		op.setSubclassOperator();
		op._declareWorkingAttributes(attributes);

		this.addOperator(op);
		
		return op;
	}
	
	public Operator newStatefulOperator(Operator op, int opId, StateWrapper s, List<String> attributes){
		// Configure operator
		if(s.getOwnerId() != opId){
			LOG.error("Operator id: "+opId+" does not own state: "+s.getOwnerId());
			System.exit(0);
		}
		op.setOperatorId(opId);
		op.setStateWrapper(s);
		op.setSubclassOperator();
		op._declareWorkingAttributes(attributes);
		// Register state
		this.registerState(s);
		this.addOperator(op);
		return op;
	}
	
	public StateWrapper newState(State s, int ownerId, int checkpointInterval, String keyAttribute){
		StateWrapper sw = new StateWrapper(ownerId, checkpointInterval, s);
		if(s instanceof CustomState){
			if((CustomState)s instanceof Partitionable){
				((Partitionable)s).setKeyAttribute(keyAttribute);
			}
			else{
				// say that keyattribute is ignored as state does not implement Partitionable
			}
		}
		else if(s instanceof LargeState){
			// say that keyAttribute is ignored
		}
		return sw;
	}
	
	/** Static scaling methods **/
	
	/** This is the preferred function, that will automatically load balance the static partitioning**/
	public void scaleOut(Operator opToScaleOut, int numPartitions){
		partitionRequirements.put(opToScaleOut, numPartitions);
	}
	
	/** This function is provided in case the user wants to manually define which partitions to be done**/
	public void scaleOut(Operator opToScaleOut, int newOpId, Node newProvisionedNode){
		// Register the intent to scale out
		ScaleOutIntentBean soib = new ScaleOutIntentBean(opToScaleOut, newOpId, newProvisionedNode);
		scIntents.add(soib);
	}
	
	/** Mapping between operator and node **/
	
	public void place(Operator o, Node n){
		int nodeId = n.getNodeId();
		if(mapOperatorToNode.containsKey(nodeId)){
			mapOperatorToNode.get(nodeId).add(o);
		}
		else{
			ArrayList<Operator> opsOfNode = new ArrayList<Operator>();
			opsOfNode.add(o);
			mapOperatorToNode.put(nodeId, opsOfNode);
		}
	}
	
	/** Private methods **/
	private void setSource(Operator source) {
		LOG.info("Configured NEW SOURCE, Operator: {}", src.toString());
		src.add(source);
	}

	private void setSink(Operator snk){
		LOG.info("Configured SINK as Operator: {}", snk.toString());
		this.snk = snk;
	}
	
	private void registerState(StateWrapper s){
		states.add(s);
		LOG.debug("Added new State to Query");
	}
	
	private void addOperator(Operator o) {
		ops.add(o);
		elements.put(o.getOperatorId(), o);
		LOG.info("Added new Operator to Infrastructure: {}", o.toString());
	}
}