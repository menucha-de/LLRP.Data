package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class C1G2BlockWrite extends Parameter {

	private static final long serialVersionUID = 900483360112977197L;

	private TLVParameterHeader parameterHeader;
	private int opSpecID;
	private long accessPW;
	private byte mB;
	private int wordPointer;
	private int writeDataWordCount;
	private byte[] writeData;

	public C1G2BlockWrite() {
	}

	public C1G2BlockWrite(TLVParameterHeader parameterHeader, int opSpecID, long accessPW, byte mB, int wordPointer, byte[] writeData) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_BLOCK_WRITE);
		this.opSpecID = opSpecID;
		this.accessPW = accessPW;
		this.mB = mB;
		this.wordPointer = wordPointer;
		this.writeData = writeData;
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

	public long getAccessPW() {
		return accessPW;
	}

	public void setAccessPW(long accessPW) {
		this.accessPW = accessPW;
	}

	public byte getmB() {
		return mB;
	}

	public void setmB(byte mB) {
		this.mB = mB;
	}

	public int getWordPointer() {
		return wordPointer;
	}

	public void setWordPointer(int wordPointer) {
		this.wordPointer = wordPointer;
	}

	public int getWriteDataWordCount() {
		return writeDataWordCount;
	}

	public void setWriteDataWordCount(int writeDataWordCount) {
		this.writeDataWordCount = writeDataWordCount;
	}

	public byte[] getWriteData() {
		return writeData;
	}

	public void setWriteData(byte[] writeData) {
		this.writeData = writeData;
	}

	@Override
	public String toString() {
		return "C1G2BlockWrite [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + ", accessPW=" + accessPW + ", mB=" + mB + ", wordPointer="
				+ wordPointer + ", writeDataWordCount=" + writeDataWordCount + ", writeData=" + Arrays.toString(writeData) + "]";
	}
}