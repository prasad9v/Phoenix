package com.ibm.commerce.extension.objects;

/**
 * Bean implementation class for Enterprise Bean: XaddressRelation
 */
public class XaddressRelationBean
	extends
		com.ibm.commerce.base.objects.ECEntityBean
	implements
		javax.ejb.EntityBean {

	/**
	 * Implementation field for persistent attribute: billtoId
	 */
	public java.lang.Long billtoId;

	/**
	 * Implementation field for persistent attribute: rrcName
	 */
	public java.lang.String rrcName;

	/**
	 * Implementation field for persistent attribute: rrcId
	 */
	public java.lang.Long rrcId;

	/**
	 * Implementation field for persistent attribute: shiptoName
	 */
	public java.lang.String shiptoName;

	/**
	 * Implementation field for persistent attribute: shiptoId
	 */
	public java.lang.Long shiptoId;

	/**
	 * Implementation field for persistent attribute: optCounter
	 */
	public short optCounter;

	/**
	 * Implementation field for persistent attribute: billtoName
	 */
	public java.lang.String billtoName;

	/**
	 * Implementation field for persistent attribute: createdTime
	 */
	public java.sql.Timestamp createdTime;

	/**
	 * Implementation field for persistent attribute: lastUpdate
	 */
	public java.sql.Timestamp lastUpdate;

	/**
	 * Implementation field for persistent attribute: markForDelete
	 */
	public java.lang.Integer markForDelete;

	/**
	 * ejbCreate method for a CMP entity bean.
	 */
	public com.ibm.commerce.extension.objects.XaddressRelationKey ejbCreate(
		java.lang.Long billtoId,
		java.lang.Long rrcId,
		java.lang.Long shiptoId) throws javax.ejb.CreateException {
		_initLinks();
		this.billtoId = billtoId;
		this.rrcId = rrcId;
		this.shiptoId = shiptoId;
		return null;
	}
	public com.ibm.commerce.extension.objects.XaddressRelationKey ejbCreate(
			java.lang.Long billtoId,
			java.lang.String billtoName,
			java.lang.Long rrcId,
			java.lang.String rrcName,
			java.lang.Long shiptoId,
			java.lang.String shiptoName,
			java.sql.Timestamp createdTime,
			java.sql.Timestamp lastUpdate,
			java.lang.Integer markForDelete) throws javax.ejb.CreateException {
		this.initializeFields();
		_initLinks();
		this.billtoId = billtoId;
		this.billtoName = billtoName;
		this.rrcId = rrcId;
		this.rrcName=rrcName;
		this.shiptoId = shiptoId;
		this.shiptoName = shiptoName;
		this.createdTime=createdTime;
		this.lastUpdate=lastUpdate;
		this.markForDelete=markForDelete;
		XaddressRelationKey addrRelKey = new XaddressRelationKey(billtoId,rrcId,shiptoId); 
		this.initializeOptCounter(addrRelKey);
		return null;
	}

	/**
	 * ejbPostCreate
	 */
	public void ejbPostCreate(
		java.lang.Long billtoId,
		java.lang.Long rrcId,
		java.lang.Long shiptoId) throws javax.ejb.CreateException {
	}
	public void ejbPostCreate(
			java.lang.Long billtoId,
			java.lang.String billtoName,
			java.lang.Long rrcId,
			java.lang.String rrcName,
			java.lang.Long shiptoId,
			java.lang.String shiptoName,
			java.sql.Timestamp createdTime,
			java.sql.Timestamp lastUpdate,
			java.lang.Integer markForDelete) throws javax.ejb.CreateException {
		}

	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
		_initLinks();
	}

	/**
	 * ejbLoad
	 */
	public void ejbLoad() {
		super.ejbLoad();
		_initLinks();
	}

	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() throws javax.ejb.RemoveException {
		try {
			_removeLinks();
		} catch (java.rmi.RemoteException e) {
			throw new javax.ejb.RemoveException(e.getMessage());
		}
	}

	/**
	 * ejbStore
	 */
	public void ejbStore() {
		super.ejbStore();
	}

	/**
	 * This method was generated for supporting the associations.
	 */
	protected void _initLinks() {
	}

	/**
	 * This method was generated for supporting the associations.
	 */
	protected java.util.Vector _getLinks() {
		java.util.Vector links = new java.util.Vector();
		return links;
	}

	/**
	 * This method was generated for supporting the associations.
	 */
	protected void _removeLinks()
		throws java.rmi.RemoteException,
		javax.ejb.RemoveException {
		java.util.List links = _getLinks();
		for (int i = 0; i < links.size(); i++) {
			try {
				((com.ibm.ivj.ejb.associations.interfaces.Link) links.get(i))
					.remove();
			} catch (javax.ejb.FinderException e) {
			} //Consume Finder error since I am going away
		}
	}

	/**
	 * Get accessor for persistent attribute: rrcName
	 */
	public java.lang.String getRrcName() {
		return rrcName;
	}

	/**
	 * Set accessor for persistent attribute: rrcName
	 */
	public void setRrcName(java.lang.String newRrcName) {
		rrcName = newRrcName;
	}

	/**
	 * Get accessor for persistent attribute: shiptoName
	 */
	public java.lang.String getShiptoName() {
		return shiptoName;
	}

	/**
	 * Set accessor for persistent attribute: shiptoName
	 */
	public void setShiptoName(java.lang.String newShiptoName) {
		shiptoName = newShiptoName;
	}
	public java.lang.Long getBillToId() {
		return billtoId;
	}
	public java.lang.Long getRrcId() {
		return rrcId;
	}
	public java.lang.Long getShipToId() {
		return shiptoId;
	}
	/**
	 * _copyFromEJB
	 */
	public java.util.Hashtable _copyFromEJB() {
		com.ibm.ivj.ejb.runtime.AccessBeanHashtable h = new com.ibm.ivj.ejb.runtime.AccessBeanHashtable();
	
		h.put("shipToId", getShipToId());
		h.put("shiptoName", getShiptoName());
		h.put("billToId", getBillToId());
		h.put("rrcId", getRrcId());
		h.put("rrcName", getRrcName());
		h.put("createdTime", getCreatedTime());
		h.put("markForDelete", getMarkForDelete());
		h.put("billtoName", getBilltoName());
		h.put("lastUpdate", getLastUpdate());
		h.put("__Key", getEntityContext().getPrimaryKey());
	
		return h;
	}
	/**
	 * _copyToEJB
	 */
	public void _copyToEJB(java.util.Hashtable h) {
		java.lang.String localShiptoName = (java.lang.String) h
			.get("shiptoName");
		java.lang.String localRrcName = (java.lang.String) h.get("rrcName");
		java.sql.Timestamp localCreatedTime = (java.sql.Timestamp) h
			.get("createdTime");
		java.lang.Integer localMarkForDelete = (java.lang.Integer) h
			.get("markForDelete");
		java.lang.String localBilltoName = (java.lang.String) h
			.get("billtoName");
		java.sql.Timestamp localLastUpdate = (java.sql.Timestamp) h
			.get("lastUpdate");
	
		if (h.containsKey("shiptoName"))
			setShiptoName((localShiptoName));
		if (h.containsKey("rrcName"))
			setRrcName((localRrcName));
		if (h.containsKey("createdTime"))
			setCreatedTime((localCreatedTime));
		if (h.containsKey("markForDelete"))
			setMarkForDelete((localMarkForDelete));
		if (h.containsKey("billtoName"))
			setBilltoName((localBilltoName));
		if (h.containsKey("lastUpdate"))
			setLastUpdate((localLastUpdate));
	}
	/**
	 * Get accessor for persistent attribute: billtoName
	 */
	public java.lang.String getBilltoName() {
		return billtoName;
	}
	/**
	 * Set accessor for persistent attribute: billtoName
	 */
	public void setBilltoName(java.lang.String newBilltoName) {
		billtoName = newBilltoName;
	}
	/**
	 * Get accessor for persistent attribute: createdTime
	 */
	public java.sql.Timestamp getCreatedTime() {
		return createdTime;
	}
	/**
	 * Set accessor for persistent attribute: createdTime
	 */
	public void setCreatedTime(java.sql.Timestamp newCreatedTime) {
		createdTime = newCreatedTime;
	}
	/**
	 * Get accessor for persistent attribute: lastUpdate
	 */
	public java.sql.Timestamp getLastUpdate() {
		return lastUpdate;
	}
	/**
	 * Set accessor for persistent attribute: lastUpdate
	 */
	public void setLastUpdate(java.sql.Timestamp newLastUpdate) {
		lastUpdate = newLastUpdate;
	}
	/**
	 * Get accessor for persistent attribute: markForDelete
	 */
	public java.lang.Integer getMarkForDelete() {
		return markForDelete;
	}
	/**
	 * Set accessor for persistent attribute: markForDelete
	 */
	public void setMarkForDelete(java.lang.Integer newMarkForDelete) {
		markForDelete = newMarkForDelete;
	}
}
