package com.ibm.commerce.extension.objects;

/**
 * Key class for Entity Bean: XaddressRelation
 */
public class XaddressRelationKey implements java.io.Serializable {

	static final long serialVersionUID = 3206093459760846163L;

	/**
	 * Implementation field for persistent attribute: billtoId
	 */
	public java.lang.Long billtoId;

	/**
	 * Implementation field for persistent attribute: rrcId
	 */
	public java.lang.Long rrcId;

	/**
	 * Implementation field for persistent attribute: shiptoId
	 */
	public java.lang.Long shiptoId;

	/**
	 * Creates an empty key for Entity Bean: XaddressRelation
	 */
	public XaddressRelationKey() {
	}

	/**
	 * Creates a key for Entity Bean: XaddressRelation
	 */
	public XaddressRelationKey(
		java.lang.Long billtoId,
		java.lang.Long rrcId,
		java.lang.Long shiptoId) {
		this.billtoId = billtoId;
		this.rrcId = rrcId;
		this.shiptoId = shiptoId;
	}

	/**
	 * Returns true if both keys are equal.
	 */
	public boolean equals(java.lang.Object otherKey) {
		if (otherKey instanceof com.ibm.commerce.extension.objects.XaddressRelationKey) {
			com.ibm.commerce.extension.objects.XaddressRelationKey o = (com.ibm.commerce.extension.objects.XaddressRelationKey) otherKey;
			return ((this.billtoId.equals(o.billtoId))
				&& (this.rrcId.equals(o.rrcId)) && (this.shiptoId
				.equals(o.shiptoId)));
		}
		return false;
	}

	/**
	 * Returns the hash code for the key.
	 */
	public int hashCode() {
		return (billtoId.hashCode() + rrcId.hashCode() + shiptoId.hashCode());
	}
}
