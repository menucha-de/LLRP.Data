package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2BlockWriteOpSpecResult extends Parameter {

	private static final long serialVersionUID = -8545902897972657698L;

	private TLVParameterHeader parameterHeader;
	private C1G2BlockWriteOpSpecResultValues result;
	private int opSpecID;
	private int numWordsWritten;

	public C1G2BlockWriteOpSpecResult() {
	}

	public C1G2BlockWriteOpSpecResult(TLVParameterHeader parameterHeader, C1G2BlockWriteOpSpecResultValues result, int opSpecID, int numWordsWritten) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_BLOCK_WRITE_OP_SPEC_RESULT);
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

	public C1G2BlockWriteOpSpecResultValues getResult() {
		return result;
	}

	public void setResult(C1G2BlockWriteOpSpecResultValues result) {
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
		return "C1G2BlockWriteOpSpecResult [parameterHeader=" + parameterHeader + ", result=" + result + ", opSpecID=" + opSpecID + ", numWordsWritten="
				+ numWordsWritten + "]";
	}
}