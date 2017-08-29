package com.ibm.commerce.extension.objects;

/**
 * Bean implementation class for Enterprise Bean: XorderItems
 */
public class XorderItemsBean extends com.ibm.commerce.base.objects.ECEntityBean
	implements
		javax.ejb.EntityBean {


	/**
	 * Implementation field for persistent attribute: orderLineItem_Id
	 */
	public java.lang.Integer orderLineItem_Id;

	/**
	 * Implementation field for persistent attribute: orders_Id
	 */
	public java.lang.Long orders_Id;

	/**
	 * Implementation field for persistent attribute: orderItems_Id
	 */
	public java.lang.Long orderItems_Id;

	/**
	 * Implementation field for persistent attribute: salesOrder_Id
	 */
	public java.lang.String salesOrder_Id;

	/**
	 * Implementation field for persistent attribute: totalWeight
	 */
	public java.math.BigDecimal totalWeight;

	/**
	 * Implementation field for persistent attribute: weighType
	 */
	public java.lang.String weighType;

	/**
	 * Implementation field for persistent attribute: hotOrder
	 */
	public java.lang.String hotOrder;

	/**
	 * Implementation field for persistent attribute: ordStatus
	 */
	public java.lang.String ordStatus;

	/**
	 * Implementation field for persistent attribute: ordItemStatus
	 */
	public java.lang.Character ordItemStatus;

	/**
	 * Implementation field for persistent attribute: estimateDeliveryDate
	 */
	public java.sql.Timestamp estimateDeliveryDate;

	/**
	 * Implementation field for persistent attribute: bolIdentifier
	 */
	public java.lang.String bolIdentifier;

	/**
	 * Implementation field for persistent attribute: manifestIdentifier
	 */
	public java.lang.String manifestIdentifier;

	/**
	 * Implementation field for persistent attribute: invoiceIdentifier
	 */
	public java.lang.String invoiceIdentifier;

	/**
	 * Implementation field for persistent attribute: optCounter
	 */
	public short optCounter;

	
	/**
	 * Implementation field for persistent attribute: sourceCountry
	 */
	public java.lang.String sourceCountry;


	/**
	 * Implementation field for persistent attribute: sourceState
	 */
	public java.lang.String sourceState;


	/**
	 * Implementation field for persistent attribute: sourceCity
	 */
	public java.lang.String sourceCity;


	/**
	 * Implementation field for persistent attribute: unit
	 */
	public java.lang.String unit;


	/**
	 * Implementation field for persistent attribute: includeInTonnage
	 */
	public java.lang.String includeInTonnage;


	/**
	 * Implementation field for persistent attribute: sourcing_Id
	 */
	public java.lang.String sourcing_Id;


	/**
	 * ejbCreate method for a CMP entity bean.
	 */
	public com.ibm.commerce.extension.objects.XorderItemsKey ejbCreate(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id) throws javax.ejb.CreateException {
		_initLinks();
		this.orderLineItem_Id = orderLineItem_Id;
		this.orders_Id = orders_Id;
		this.orderItems_Id = orderItems_Id;
		this.salesOrder_Id = salesOrder_Id;
		return null;
	}

	/**
	 * ejbPostCreate
	 */
	public void ejbPostCreate(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id) throws javax.ejb.CreateException {
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
	 * Get accessor for persistent attribute: totalWeight
	 */
	public java.math.BigDecimal getTotalWeight() {
		return totalWeight;
	}

	/**
	 * Set accessor for persistent attribute: totalWeight
	 */
	public void setTotalWeight(java.math.BigDecimal newTotalWeight) {
		totalWeight = newTotalWeight;
	}

	/**
	 * Get accessor for persistent attribute: weighType
	 */
	public java.lang.String getWeighType() {
		return weighType;
	}

	/**
	 * Set accessor for persistent attribute: weighType
	 */
	public void setWeighType(java.lang.String newWeighType) {
		weighType = newWeighType;
	}

	/**
	 * Get accessor for persistent attribute: hotOrder
	 */
	public java.lang.String getHotOrder() {
		return hotOrder;
	}

	/**
	 * Set accessor for persistent attribute: hotOrder
	 */
	public void setHotOrder(java.lang.String newHotOrder) {
		hotOrder = newHotOrder;
	}

	/**
	 * Get accessor for persistent attribute: ordStatus
	 */
	public java.lang.String getOrdStatus() {
		return ordStatus;
	}

	/**
	 * Set accessor for persistent attribute: ordStatus
	 */
	public void setOrdStatus(java.lang.String newOrdStatus) {
		ordStatus = newOrdStatus;
	}

	/**
	 * Get accessor for persistent attribute: ordItemStatus
	 */
	public java.lang.Character getOrdItemStatus() {
		return ordItemStatus;
	}

	/**
	 * Set accessor for persistent attribute: ordItemStatus
	 */
	public void setOrdItemStatus(java.lang.Character newOrdItemStatus) {
		ordItemStatus = newOrdItemStatus;
	}

	/**
	 * Get accessor for persistent attribute: estimateDeliveryDate
	 */
	public java.sql.Timestamp getEstimateDeliveryDate() {
		return estimateDeliveryDate;
	}

	/**
	 * Set accessor for persistent attribute: estimateDeliveryDate
	 */
	public void setEstimateDeliveryDate(
		java.sql.Timestamp newEstimateDeliveryDate) {
		estimateDeliveryDate = newEstimateDeliveryDate;
	}

	/**
	 * Get accessor for persistent attribute: bolIdentifier
	 */
	public java.lang.String getBolIdentifier() {
		return bolIdentifier;
	}

	/**
	 * Set accessor for persistent attribute: bolIdentifier
	 */
	public void setBolIdentifier(java.lang.String newBolIdentifier) {
		bolIdentifier = newBolIdentifier;
	}

	/**
	 * Get accessor for persistent attribute: manifestIdentifier
	 */
	public java.lang.String getManifestIdentifier() {
		return manifestIdentifier;
	}

	/**
	 * Set accessor for persistent attribute: manifestIdentifier
	 */
	public void setManifestIdentifier(java.lang.String newManifestIdentifier) {
		manifestIdentifier = newManifestIdentifier;
	}

	/**
	 * Get accessor for persistent attribute: invoiceIdentifier
	 */
	public java.lang.String getInvoiceIdentifier() {
		return invoiceIdentifier;
	}

	/**
	 * Set accessor for persistent attribute: invoiceIdentifier
	 */
	public void setInvoiceIdentifier(java.lang.String newInvoiceIdentifier) {
		invoiceIdentifier = newInvoiceIdentifier;
	}

	/**
	 * Get accessor for persistent attribute: optCounter
	 */
	public short getOptCounter() {
		return optCounter;
	}

	/**
	 * Set accessor for persistent attribute: optCounter
	 */
	public void setOptCounter(short newOptCounter) {
		optCounter = newOptCounter;
	}
	public java.lang.Long getOrdersId() {
		   return orders_Id;
		}
	public java.lang.Long getOrderItemsId() {
		   return orderItems_Id;
		}
	public java.lang.Integer getOrderLineItemId() {
		   return orderLineItem_Id;
		}
	public java.lang.String getSalesOrderId() {
		   return salesOrder_Id;
		}
	public com.ibm.commerce.extension.objects.XorderItemsKey ejbCreate(
			   java.lang.Integer orderLineItem_Id,java.lang.Long orders_Id,java.lang.Long orderItems_Id,
			   java.lang.String salesOrder_Id,java.math.BigDecimal totalWeight,java.lang.String weighType,
			   java.lang.String hotOrder,java.lang.String sourcing_Id,java.lang.String ordStatus,
			   java.lang.Character ordItemStatus,java.sql.Timestamp estimateDeliveryDate,java.lang.String 
			   bolIdentifier,java.lang.String manifestIdentifier,java.lang.String invoiceIdentifier,java.lang.String 
			   sourceCountry,java.lang.String sourceState,java.lang.String sourceCity,java.lang.String unit,java.lang.String includeInTonnage)
			   throws javax.ejb.CreateException {
				this.initializeFields();
			      _initLinks();
			      this.orderLineItem_Id=orderLineItem_Id;
			      this.orders_Id=orders_Id;
			      this.orderItems_Id=orderItems_Id;
			      this.salesOrder_Id=salesOrder_Id;
			      this.totalWeight=totalWeight;
			      this.weighType=weighType;
			      this.hotOrder=hotOrder;
			      this.sourcing_Id=sourcing_Id;
			      this.ordStatus=ordStatus;
			      this.ordItemStatus=ordItemStatus;
			      this.estimateDeliveryDate=estimateDeliveryDate;
			      this.bolIdentifier=bolIdentifier;
			      this.manifestIdentifier=manifestIdentifier;
			      this.invoiceIdentifier=invoiceIdentifier;
			      this.sourceCountry=sourceCountry;
			      this.sourceState=sourceState;
			      this.sourceCity=sourceCity;
			      this.unit=unit;
			      this.includeInTonnage=includeInTonnage;
			      XorderItemsKey objOrderItemKey = new XorderItemsKey(orderLineItem_Id,orders_Id,orderItems_Id,salesOrder_Id);
			      this.initializeOptCounter(objOrderItemKey);

			      return null;
			}
	public void ejbPostCreate(
			   java.lang.Integer orderLineItem_Id,java.lang.Long orders_Id,java.lang.Long orderItems_Id,
			   java.lang.String salesOrder_Id,java.math.BigDecimal totalWeight,java.lang.String weighType,
			   java.lang.String hotOrder,java.lang.String sourcing_Id,java.lang.String ordStatus,
			   java.lang.Character ordItemStatus,java.sql.Timestamp estimateDeliveryDate,java.lang.String 
			   bolIdentifier,java.lang.String manifestIdentifier,java.lang.String invoiceIdentifier,java.lang.String 
			   sourceCountry,java.lang.String sourceState,java.lang.String sourceCity,java.lang.String unit,java.lang.String includeInTonnage)
			   throws javax.ejb.CreateException {
	}

	/**
	 * _copyFromEJB
	 */
	public java.util.Hashtable _copyFromEJB() {
		com.ibm.ivj.ejb.runtime.AccessBeanHashtable h = new com.ibm.ivj.ejb.runtime.AccessBeanHashtable();
	
		h.put("orderLineItemId", getOrderLineItemId());
		h.put("ordersId", getOrdersId());
		h.put("hotOrder", getHotOrder());
		h.put("invoiceIdentifier", getInvoiceIdentifier());
		h.put("weighType", getWeighType());
		h.put("manifestIdentifier", getManifestIdentifier());
		h.put("bolIdentifier", getBolIdentifier());
		h.put("optCounter", new Short(getOptCounter()));
		h.put("estimateDeliveryDate", getEstimateDeliveryDate());
		h.put("totalWeight", getTotalWeight());
		h.put("orderItemsId", getOrderItemsId());
		h.put("ordItemStatus", getOrdItemStatus());
		h.put("ordStatus", getOrdStatus());
		h.put("salesOrderId", getSalesOrderId());
		h.put("sourceCity", getSourceCity());
		h.put("sourceState", getSourceState());
		h.put("sourceCountry", getSourceCountry());
		h.put("unit", getUnit());
		h.put("includeInTonnage", getIncludeInTonnage());
		h.put("sourcing_Id", getSourcing_Id());
		h.put("__Key", getEntityContext().getPrimaryKey());
	
		return h;
	}

	/**
	 * _copyToEJB
	 */
	public void _copyToEJB(java.util.Hashtable h) {
		java.lang.String localHotOrder = (java.lang.String) h.get("hotOrder");
		java.lang.String localInvoiceIdentifier = (java.lang.String) h
			.get("invoiceIdentifier");
		java.lang.String localWeighType = (java.lang.String) h.get("weighType");
		java.lang.String localManifestIdentifier = (java.lang.String) h
			.get("manifestIdentifier");
		java.lang.String localBolIdentifier = (java.lang.String) h
			.get("bolIdentifier");
		Short localOptCounter = (Short) h.get("optCounter");
		java.sql.Timestamp localEstimateDeliveryDate = (java.sql.Timestamp) h
			.get("estimateDeliveryDate");
		java.math.BigDecimal localTotalWeight = (java.math.BigDecimal) h
			.get("totalWeight");
		java.lang.Character localOrdItemStatus = (java.lang.Character) h
			.get("ordItemStatus");
		java.lang.String localOrdStatus = (java.lang.String) h.get("ordStatus");
		java.lang.String localSourceCity = (java.lang.String) h
			.get("sourceCity");
		java.lang.String localSourceState = (java.lang.String) h
			.get("sourceState");
		java.lang.String localSourceCountry = (java.lang.String) h
			.get("sourceCountry");
		java.lang.String localUnit = (java.lang.String) h.get("unit");
		java.lang.String localIncludeInTonnage = (java.lang.String) h
			.get("includeInTonnage");
		java.lang.String localSourcing_Id = (java.lang.String) h
			.get("sourcing_Id");
	
		if (h.containsKey("hotOrder"))
			setHotOrder((localHotOrder));
		if (h.containsKey("invoiceIdentifier"))
			setInvoiceIdentifier((localInvoiceIdentifier));
		if (h.containsKey("weighType"))
			setWeighType((localWeighType));
		if (h.containsKey("manifestIdentifier"))
			setManifestIdentifier((localManifestIdentifier));
		if (h.containsKey("bolIdentifier"))
			setBolIdentifier((localBolIdentifier));
		if (h.containsKey("optCounter"))
			setOptCounter((localOptCounter).shortValue());
		if (h.containsKey("estimateDeliveryDate"))
			setEstimateDeliveryDate((localEstimateDeliveryDate));
		if (h.containsKey("totalWeight"))
			setTotalWeight((localTotalWeight));
		if (h.containsKey("ordItemStatus"))
			setOrdItemStatus((localOrdItemStatus));
		if (h.containsKey("ordStatus"))
			setOrdStatus((localOrdStatus));
		if (h.containsKey("sourceCity"))
			setSourceCity((localSourceCity));
		if (h.containsKey("sourceState"))
			setSourceState((localSourceState));
		if (h.containsKey("sourceCountry"))
			setSourceCountry((localSourceCountry));
		if (h.containsKey("unit"))
			setUnit((localUnit));
		if (h.containsKey("includeInTonnage"))
			setIncludeInTonnage((localIncludeInTonnage));
		if (h.containsKey("sourcing_Id"))
			setSourcing_Id((localSourcing_Id));
	}

	/**
	 * Get accessor for persistent attribute: sourceCountry
	 */
	public java.lang.String getSourceCountry() {
		return sourceCountry;
	}

	/**
	 * Set accessor for persistent attribute: sourceCountry
	 */
	public void setSourceCountry(java.lang.String newSourceCountry) {
		sourceCountry = newSourceCountry;
	}

	/**
	 * Get accessor for persistent attribute: sourceState
	 */
	public java.lang.String getSourceState() {
		return sourceState;
	}

	/**
	 * Set accessor for persistent attribute: sourceState
	 */
	public void setSourceState(java.lang.String newSourceState) {
		sourceState = newSourceState;
	}

	/**
	 * Get accessor for persistent attribute: sourceCity
	 */
	public java.lang.String getSourceCity() {
		return sourceCity;
	}

	/**
	 * Set accessor for persistent attribute: sourceCity
	 */
	public void setSourceCity(java.lang.String newSourceCity) {
		sourceCity = newSourceCity;
	}

	/**
	 * Get accessor for persistent attribute: unit
	 */
	public java.lang.String getUnit() {
		return unit;
	}

	/**
	 * Set accessor for persistent attribute: unit
	 */
	public void setUnit(java.lang.String newUnit) {
		unit = newUnit;
	}

	/**
	 * Get accessor for persistent attribute: includeInTonnage
	 */
	public java.lang.String getIncludeInTonnage() {
		return includeInTonnage;
	}

	/**
	 * Set accessor for persistent attribute: includeInTonnage
	 */
	public void setIncludeInTonnage(java.lang.String newIncludeInTonnage) {
		includeInTonnage = newIncludeInTonnage;
	}

	/**
	 * Get accessor for persistent attribute: sourcing_Id
	 */
	public java.lang.String getSourcing_Id() {
		return sourcing_Id;
	}

	/**
	 * Set accessor for persistent attribute: sourcing_Id
	 */
	public void setSourcing_Id(java.lang.String newSourcing_Id) {
		sourcing_Id = newSourcing_Id;
	}
		
}
