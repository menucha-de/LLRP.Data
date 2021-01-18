package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2BlockErase extends Parameter {

	private static final long serialVersionUID = -8403963543482364309L;

	private TLVParameterHeader parameterHeader;
	private int opSpecID;
	private long accessPW;
	private byte mB;
	private int wordPointer;
	private int wordCount;

	public C1G2BlockErase() {
	}

	public C1G2BlockErase(TLVParameterHeader parameterHeader, int opSpecID, long accessPW, byte mB, int wordPointer, int wordCount) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_BLOCK_ERASE);
		this.opSpecID = opSpecID;
		this.accessPW = accessPW;
		this.mB = mB;
		this.wordPointer = wordPointer;
		this.wordCount = wordCount;
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

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	@Override
	public String toString() {
		return "C1G2BlockErase [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + ", accessPW=" + accessPW + ", mB=" + mB + ", wordPointer="
				+ wordPointer + ", wordCount=" + wordCount + "]";
	}
}