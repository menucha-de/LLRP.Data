package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class C1G2ReadOpSpecResult extends Parameter {

	private static final long serialVersionUID = -6685036142855798749L;
	private TLVParameterHeader parameterHeader;
	private C1G2ReadOpSpecResultValues result;
	private int opSpecID;
	private int readDataWordCount;
	private byte[] readData;

	public C1G2ReadOpSpecResult() {
	}

	public C1G2ReadOpSpecResult(TLVParameterHeader parameterHeader, C1G2ReadOpSpecResultValues result, int opSpecID, byte[] readData) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_READ_OP_SPEC_RESULT);
		this.result = result;
		this.opSpecID = opSpecID;
		this.readData = readData;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2ReadOpSpecResultValues getResult() {
		return result;
	}

	public void setResult(C1G2ReadOpSpecResultValues result) {
		this.result = result;
	}

	public int getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(int opSpecID) {
		this.opSpecID = opSpecID;
	}

	public int getReadDataWordCount() {
		return readDataWordCount;
	}

	public void setReadDataWordCount(int readDataWordCount) {
		this.readDataWordCount = readDataWordCount;
	}

	public byte[] getReadData() {
		return readData;
	}

	public void setReadData(byte[] readData) {
		this.readData = readData;
	}

	@Override
	public String toString() {
		return "C1G2ReadOpSpecResult [parameterHeader=" + parameterHeader + ", result=" + result + ", opSpecID=" + opSpecID + ", readDataWordCount="
				+ readDataWordCount + ", readData=" + Arrays.toString(readData) + "]";
	}
}