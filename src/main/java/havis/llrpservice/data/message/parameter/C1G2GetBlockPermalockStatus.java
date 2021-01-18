package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2GetBlockPermalockStatus extends Parameter {

	private static final long serialVersionUID = -5295695919558587256L;

	private TLVParameterHeader parameterHeader;
	private int opSpecID;
	private long accessPW;
	private byte mB;
	private int blockPointer;
	private int blockRange;

	public C1G2GetBlockPermalockStatus() {
	}

	public C1G2GetBlockPermalockStatus(TLVParameterHeader parameterHeader, int opSpecID, long accessPW, byte mB, int blockPointer, int blockRange) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_GET_BLOCK_PERMALOCK_STATUS);
		this.opSpecID = opSpecID;
		this.accessPW = accessPW;
		this.mB = mB;
		this.blockPointer = blockPointer;
		this.blockRange = blockRange;
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

	public int getBlockPointer() {
		return blockPointer;
	}

	public void setBlockPointer(int blockPointer) {
		this.blockPointer = blockPointer;
	}

	public int getBlockRange() {
		return blockRange;
	}

	public void setBlockRange(int blockRange) {
		this.blockRange = blockRange;
	}

	@Override
	public String toString() {
		return "C1G2GetBlockPermalockStatus [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + ", accessPW=" + accessPW + ", mB=" + mB
				+ ", blockPointer=" + blockPointer + ", blockRange=" + blockRange + "]";
	}
}