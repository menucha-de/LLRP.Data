package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class C1G2Write extends Parameter {

	private static final long serialVersionUID = 1964980245261463168L;

	private TLVParameterHeader parameterHeader;
	private int opSpecId;
	private long accessPw;
	private byte memoryBank;
	private int wordPointer;
	private int writeDataWordCount;
	private byte[] writeData;

	public C1G2Write() {
	}

	public C1G2Write(TLVParameterHeader parameterHeader, int opSpecId, long accessPw, byte memoryBank, int wordPointer, byte[] writeData) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_WRITE);
		this.opSpecId = opSpecId;
		this.accessPw = accessPw;
		this.memoryBank = memoryBank;
		this.wordPointer = wordPointer;
		this.writeData = writeData;
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
		return "C1G2Write [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecId + ", accessPw=" + accessPw + ", memoryBank=" + memoryBank
				+ ", wordPointer=" + wordPointer + ", writeDataWordCount=" + writeDataWordCount + ", writeData=" + Arrays.toString(writeData) + "]";
	}
}