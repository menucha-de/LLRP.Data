package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class OpSpecID extends Parameter {

	private static final long serialVersionUID = 972169662444843798L;

	private TVParameterHeader parameterHeader;
	private int opSpecID;

	public OpSpecID() {
	}

	public OpSpecID(TVParameterHeader parameterHeader, int opSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.OP_SPEC_ID);
		this.opSpecID = opSpecID;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(int opSpecID) {
		this.opSpecID = opSpecID;
	}

	@Override
	public String toString() {
		return "OpSpecID [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + "]";
	}
}