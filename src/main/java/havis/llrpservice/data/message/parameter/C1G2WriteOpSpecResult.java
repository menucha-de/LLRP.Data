package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2WriteOpSpecResult extends Parameter {

	private static final long serialVersionUID = -7315353957281391572L;

	private TLVParameterHeader parameterHeader;
	private C1G2WriteOpSpecResultValues result;
	private int opSpecID;
	private int numWordsWritten;

	public C1G2WriteOpSpecResult() {
	}

	public C1G2WriteOpSpecResult(TLVParameterHeader parameterHeader, C1G2WriteOpSpecResultValues result, int opSpecID, int numWordsWritten) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_WRITE_OP_SPEC_RESULT);
		this.result = result;
		this.opSpecID = opSpecID;
		this.numWordsWritten = numWordsWritten;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2WriteOpSpecResultValues getResult() {
		return result;
	}

	public void setResult(C1G2WriteOpSpecResultValues result) {
		this.result = result;
	}

	public int getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(int opSpecID) {
		this.opSpecID = opSpecID;
	}

	public int getNumWordsWritten() {
		return numWordsWritten;
	}

	public void setNumWordsWritten(int numWordsWritten) {
		this.numWordsWritten = numWordsWritten;
	}

	@Override
	public String toString() {
		return "C1G2WriteOpSpecResult [parameterHeader=" + parameterHeader + ", result=" + result + ", opSpecID=" + opSpecID + ", numWordsWritten="
				+ numWordsWritten + "]";
	}
}