package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2SingulationDetails extends Parameter {

	private static final long serialVersionUID = 3100597421757453092L;

	private TVParameterHeader parameterHeader;
	private int numCollisionSlots;
	private int numEmptySlots;

	public C1G2SingulationDetails() {
	}

	public C1G2SingulationDetails(TVParameterHeader parameterHeader, int numCollisionSlots, int numEmptySlots) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_SINGULATION_DETAILS);
		this.numCollisionSlots = numCollisionSlots;
		this.numEmptySlots = numEmptySlots;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getNumCollisionSlots() {
		return numCollisionSlots;
	}

	public void setNumCollisionSlots(int numCollisionSlots) {
		this.numCollisionSlots = numCollisionSlots;
	}

	public int getNumEmptySlots() {
		return numEmptySlots;
	}

	public void setNumEmptySlots(int numEmptySlots) {
		this.numEmptySlots = numEmptySlots;
	}

	@Override
	public String toString() {
		return "C1G2SingulationDetails [parameterHeader=" + parameterHeader + ", numCollisionSlots=" + numCollisionSlots + ", numEmptySlots=" + numEmptySlots
				+ "]";
	}
}