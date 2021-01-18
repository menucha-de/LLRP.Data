package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2BlockPermalockOpSpecResult extends Parameter {

	private static final long serialVersionUID = 67656255078199790L;

	private TLVParameterHeader parameterHeader;
	private C1G2BlockPermalockOpSpecResultValues result;
	private int opSpecID;

	public C1G2BlockPermalockOpSpecResult() {
	}

	public C1G2BlockPermalockOpSpecResult(TLVParameterHeader parameterHeader, C1G2BlockPermalockOpSpecResultValues result, int opSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT);
		this.result = result;
		this.opSpecID = opSpecID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2BlockPermalockOpSpecResultValues getResult() {
		return result;
	}

	public void setResult(C1G2BlockPermalockOpSpecResultValues result) {
		this.result = result;
	}

	public int getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(int opSpecID) {
		this.opSpecID = opSpecID;
	}

	@Override
	public String toString() {
		return "C1G2BlockPermalockOpSpecResult [parameterHeader=" + parameterHeader + ", result=" + result + ", opSpecID=" + opSpecID + "]";
	}
}