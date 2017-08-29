package com.ibm.commerce.extension.objects;

/**
 * Key class for Entity Bean: XorderItems
 */
public class XorderItemsKey implements java.io.Serializable {

	static final long serialVersionUID = 3206093459760846163L;

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
	 * Creates an empty key for Entity Bean: XorderItems
	 */
	public XorderItemsKey() {
	}

	/**
	 * Creates a key for Entity Bean: XorderItems
	 */
	public XorderItemsKey(
		java.lang.Integer orderLineItem_Id,
		java.lang.Long orders_Id,
		java.lang.Long orderItems_Id,
		java.lang.String salesOrder_Id) {
		this.orderLineItem_Id = orderLineItem_Id;
		this.orders_Id = orders_Id;
		this.orderItems_Id = orderItems_Id;
		this.salesOrder_Id = salesOrder_Id;
	}

	/**
	 * Returns true if both keys are equal.
	 */
	public boolean equals(java.lang.Object otherKey) {
		if (otherKey instanceof com.ibm.commerce.extension.objects.XorderItemsKey) {
			com.ibm.commerce.extension.objects.XorderItemsKey o = (com.ibm.commerce.extension.objects.XorderItemsKey) otherKey;
			return ((this.orderLineItem_Id.equals(o.orderLineItem_Id))
				&& (this.orders_Id.equals(o.orders_Id))
				&& (this.orderItems_Id.equals(o.orderItems_Id)) && (this.salesOrder_Id
				.equals(o.salesOrder_Id)));
		}
		return false;
	}

	/**
	 * Returns the hash code for the key.
	 */
	public int hashCode() {
		return (orderLineItem_Id.hashCode()
			+ orders_Id.hashCode()
			+ orderItems_Id.hashCode() + salesOrder_Id.hashCode());
	}
}
