package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ClientRequestOpSpec extends Parameter {

	private static final long serialVersionUID = 573185686184442873L;

	private TLVParameterHeader parameterHeader;
	private int opSpecID;

	public ClientRequestOpSpec() {
	}

	public ClientRequestOpSpec(TLVParameterHeader parameterHeader, int opSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.CLIENT_REQUEST_OP_SPEC);
		this.opSpecID = opSpecID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
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
		return "ClientRequestOpSpec [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + "]";
	}
}