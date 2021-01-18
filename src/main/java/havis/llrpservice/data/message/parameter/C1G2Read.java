package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2Read extends Parameter {

	private static final long serialVersionUID = -5953773684871851817L;

	private TLVParameterHeader parameterHeader;
	private int opSpecId;
	private long accessPw;
	private byte memoryBank;
	private int wordPointer;
	private int wordCount;

	public C1G2Read() {
	}

	public C1G2Read(TLVParameterHeader parameterHeader, int opSpecId, long accessPw, byte memoryBank, int wordPointer, int wordCount) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_READ);
		this.opSpecId = opSpecId;
		this.accessPw = accessPw;
		this.memoryBank = memoryBank;
		this.wordPointer = wordPointer;
		this.wordCount = wordCount;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getOpSpecId() {
		return opSpecId;
	}

	public void setOpSpecId(int opSpecId) {
		this.opSpecId = opSpecId;
	}

	public long getAccessPw() {
		return accessPw;
	}

	public void setAccessPw(long accessPw) {
		this.accessPw = accessPw;
	}

	public byte getMemoryBank() {
		return memoryBank;
	}

	public void setMemoryBank(byte mb) {
		this.memoryBank = mb;
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
		return "C1G2Read [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecId + ", accessPw=" + accessPw + ", memoryBank=" + memoryBank
				+ ", wordPointer=" + wordPointer + ", wordCount=" + wordCount + "]";
	}
}