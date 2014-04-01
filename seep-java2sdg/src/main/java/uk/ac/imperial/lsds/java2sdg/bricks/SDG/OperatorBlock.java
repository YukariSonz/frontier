package uk.ac.imperial.lsds.java2sdg.bricks.SDG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.imperial.lsds.java2sdg.bricks.TaskElement;

public class OperatorBlock {
	
	private final int id;
	private final int workflowId;
	private final boolean stateful;
	private int stateId;
	private List<TaskElement> taskElements;
	
	private List<Stream> downstreamOperator;
	private List<Stream> upstreamOperator;

	private OperatorBlock(int id, int workflowId, int stateId, boolean stateful){
		this.id = id;
		this.workflowId = workflowId;
		this.stateful = stateful;
		this.stateId = stateId;
		this.taskElements = new ArrayList<TaskElement>();
		this.downstreamOperator = new ArrayList<Stream>();
		this.upstreamOperator = new ArrayList<Stream>();
	}
	
	public int getId(){
		return id;
	}
	
	public int getWorkflowId(){
		return workflowId;
	}
	
	public int getStateId(){
		return stateId;
	}
	
	public static OperatorBlock makeStatefulOperator(int id, int workflowId, int stateId){
		return new OperatorBlock(id, workflowId, stateId, true);
	}

	public static OperatorBlock makeStatelessOperator(int id, int workflowId, int stateId){
		return new OperatorBlock(id, workflowId, stateId, false);
	}
	
	public void addTE(TaskElement te){
		this.taskElements.add(te);
	}
	
	public TaskElement getTE(){
		if(taskElements.size() >= 1)
			return this.taskElements.get(0);
		else{
			return null;
		}
	}
	
	public List<TaskElement> getTEs(){
		return taskElements;
	}
	
	public void addDownstream(int id, int workflowId, StreamType type){
		Stream s = new Stream(id, workflowId, type);
		downstreamOperator.add(s);
	}

	public List<Stream> getDownstreamOperator(){
		return downstreamOperator;
	}
	
	public int getDownstreamSize(){
		return downstreamOperator.size();
	}

	public List<Stream> getUpstreamOperator(){
		return upstreamOperator;
	}
	
	public int getUpstreamSize(){
		return upstreamOperator.size();
	}
	
	public void addUpstream(int id, int workflowId){
		Stream s = new Stream(id, workflowId, StreamType.UPSTREAM);
		upstreamOperator.add(s);
	}
	
	public void reconfigureDownstream(int prevId, int prevWorkflowId, int newId, int newWorkflowId){
		StreamType st = null; // We want to just propagate the stream type. There is no reconf option here
		Iterator<Stream> down = downstreamOperator.iterator();
		while(down.hasNext()){
			Stream d = down.next();
			// Detect old stream and remove it
			if(d.getId() == prevId && d.getWorkflowId() == prevWorkflowId){
				st = d.getType();
				down.remove();
			}
		}
		// Create new stream
		this.addDownstream(newId, newWorkflowId, st);
	}
	
	public void reconfigureUpstream(int prevId, int prevWorkflowId, int newId, int newWorkflowId){
		Iterator<Stream> up = upstreamOperator.iterator();
		while(up.hasNext()){
			Stream d = up.next();
			// Detect old stream and remove it
			if(d.getId() == prevId && d.getWorkflowId() == prevWorkflowId){
				up.remove();
			}
		}
		// Create new stream
		this.addUpstream(newId, newWorkflowId);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("--> OB - "+this.id+" from workflow: "+this.workflowId+"\n");
		if(this.stateful){
			sb.append("Stateful - "+this.stateId+"\n");
		}
		else{
			sb.append("Stateless \n");
		}
		sb.append("#TEs: "+this.taskElements.size()+"\n");
		for(int i = 0; i < this.taskElements.size(); i++){
			sb.append("\n");
			sb.append(this.taskElements.get(i));
			sb.append("\n");
		}
		StringBuilder sbdown = new StringBuilder();
		sbdown.append("[");
		for(Stream s : downstreamOperator){
			sbdown.append(s.getId()+"-"+s.getWorkflowId()+", ");
		}
		sbdown.append("]");
		sb.append("#Downstream: "+sbdown.toString()+"\n");
		
		StringBuilder sbup = new StringBuilder();
		sbup.append("[");
		for(Stream s : upstreamOperator){
			sbup.append(s.getId()+"-"+s.getWorkflowId()+", ");
		}
		sbup.append("]");
		sb.append("#Upstream: "+sbup.toString()+"\n");
		
		return sb.toString();
	}
}