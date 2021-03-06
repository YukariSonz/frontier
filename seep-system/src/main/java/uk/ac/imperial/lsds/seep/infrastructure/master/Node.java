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
package uk.ac.imperial.lsds.seep.infrastructure.master;

import java.net.InetAddress;
import java.io.Serializable;

/**
* Node. Node class models a physical node with its IP and port number.
*/

public class Node implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int nodeId = 0;
	
	private InetAddress ip;
	private InetAddress controlIp;
	private InetAddress masterCommsIp;
	private int port;
	
	public int getNodeId(){
		return nodeId;
	}
	
	public InetAddress getIp(){
		return ip;
	}	
	
	public InetAddress getControlIp()
	{
		return controlIp;
	}

	public InetAddress getMasterWorkerCommsIp(){
		return masterCommsIp;
	}

	public int getPort() {
		return port;
	}

	public Node setIp(InetAddress newIp){
		throw new RuntimeException("Todo setip.");
		//return new Node(newIp, controlIp, masterCommsIp, port);
	}

	public Node setControlIp(InetAddress newControlIp){
		return new Node(ip, newControlIp, masterCommsIp, port);
	}

	public Node setMasterWorkerCommsIp(InetAddress newMasterCommsIp){
		return new Node(ip, controlIp, newMasterCommsIp, port);
	}


	public Node(int nodeId){
		this.nodeId = nodeId;
	}
	
	public Node(InetAddress ip, int port) {
		super();
		this.ip = ip;
		this.masterCommsIp = null;
		this.port = port;
		throw new RuntimeException("TODO: Get rid of this.");
	}

	public Node(InetAddress ip, InetAddress controlIp, InetAddress masterCommsIp, int port) {
		super();
		this.ip = ip;
		this.controlIp = controlIp;
		this.masterCommsIp = masterCommsIp;
		this.port = port;
	}
	
	@Override 
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return "Node [ip=" + ip + ", controlIp="+controlIp+", masterCommsIp=" + masterCommsIp + ", port=" + port + "]";
	}

}
