package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2RecommissionOpSpecResult extends Parameter {

	private static final long serialVersionUID = 1799731007470248408L;

	private TLVParameterHeader parameterHeader;
	private C1G2RecommissionOpSpecResultValues result;
	private int opSpecID;

	public C1G2RecommissionOpSpecResult() {
	}

	public C1G2RecommissionOpSpecResult(TLVParameterHeader parameterHeader, C1G2RecommissionOpSpecResultValues result, int opSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_RECOMMISSION_OP_SPEC_RESULT);
		this.result = result;
		this.opSpecID = opSpecID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2RecommissionOpSpecResultValues getResult() {
		return result;
	}

	public void setResult(C1G2RecommissionOpSpecResultValues result) {
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
		return "C1G2RecommissionOpSpecResult [parameterHeader=" + parameterHeader + ", result=" + result + ", opSpecID=" + opSpecID + "]";
	}
}