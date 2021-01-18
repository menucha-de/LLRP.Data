package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ClientRequestOpSpecResult extends Parameter {

	private static final long serialVersionUID = 5830435162779817743L;

	private TVParameterHeader parameterHeader;
	private int opSpecID;

	public ClientRequestOpSpecResult() {
	}

	public ClientRequestOpSpecResult(TVParameterHeader parameterHeader, int opSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.CLIENT_REQUEST_OP_SPEC_RESULT);
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
		return "ClientRequestOpSpecResult [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + "]";
	}
}