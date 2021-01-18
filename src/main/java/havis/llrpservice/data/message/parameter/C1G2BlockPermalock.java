package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class C1G2BlockPermalock extends Parameter {

	private static final long serialVersionUID = 8102344041799005331L;

	private TLVParameterHeader parameterHeader;
	private int opSpecID;
	private long accessPW;
	private byte mB;
	private int blockPointer;
	private int blockMaskWordCount;
	private byte[] blockMask;

	public C1G2BlockPermalock() {
	}

	public C1G2BlockPermalock(TLVParameterHeader parameterHeader, int opSpecID, long accessPW, byte mB, int blockPointer, byte[] blockMask) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_BLOCK_PERMALOCK);
		this.opSpecID = opSpecID;
		this.accessPW = accessPW;
		this.mB = mB;
		this.blockPointer = blockPointer;
		this.blockMask = blockMask;
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

	public int getBlockMaskWordCount() {
		return blockMaskWordCount;
	}

	public void setBlockMaskWordCount(int blockMaskWordCount) {
		this.blockMaskWordCount = blockMaskWordCount;
	}

	public byte[] getBlockMask() {
		return blockMask;
	}

	public void setBlockMask(byte[] blockMask) {
		this.blockMask = blockMask;
	}

	@Override
	public String toString() {
		return "C1G2BlockPermalock [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + ", accessPW=" + accessPW + ", mB=" + mB
				+ ", blockPointer=" + blockPointer + ", blockMaskWordCount=" + blockMaskWordCount + ", blockMask=" + Arrays.toString(blockMask) + "]";
	}
}