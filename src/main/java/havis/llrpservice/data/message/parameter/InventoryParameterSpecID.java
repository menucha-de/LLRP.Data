package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class InventoryParameterSpecID extends Parameter {

	private static final long serialVersionUID = -1970320056193774647L;

	private TVParameterHeader parameterHeader;
	private int inventoryParameterSpecID;

	public InventoryParameterSpecID() {
	}

	public InventoryParameterSpecID(TVParameterHeader parameterHeader, int inventoryParameterSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.INVENTORY_PARAMETER_SPEC_ID);
		this.inventoryParameterSpecID = inventoryParameterSpecID;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getInventoryParameterSpecID() {
		return inventoryParameterSpecID;
	}

	public void setInventoryParameterSpecID(int inventoryParameterSpecID) {
		this.inventoryParameterSpecID = inventoryParameterSpecID;
	}

	@Override
	public String toString() {
		return "InventoryParameterSpecID [parameterHeader=" + parameterHeader + ", inventoryParameterSpecID=" + inventoryParameterSpecID + "]";
	}
}