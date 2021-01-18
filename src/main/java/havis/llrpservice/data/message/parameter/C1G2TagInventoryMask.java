package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.BitSet;

public class C1G2TagInventoryMask extends Parameter {

	private static final long serialVersionUID = -4688892420389735754L;

	private TLVParameterHeader parameterHeader;
	private byte mB;
	private int pointer;
	private int maskBitCount;
	private BitSet tagMask;

	public C1G2TagInventoryMask() {
	}

	public C1G2TagInventoryMask(TLVParameterHeader header, byte mB, int pointer, BitSet tagMask) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_TAG_INVENTORY_MASK);
		this.mB = mB;
		this.pointer = pointer;
		this.tagMask = tagMask;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public byte getmB() {
		return mB;
	}

	public void setmB(byte mB) {
		this.mB = mB;
	}

	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	public int getMaskBitCount() {
		return maskBitCount;
	}

	public void setMaskBitCount(int maskBitCount) {
		this.maskBitCount = maskBitCount;
	}

	public BitSet getTagMask() {
		return tagMask;
	}

	public void setTagMask(BitSet tagMask) {
		this.tagMask = tagMask;
	}

	@Override
	public String toString() {
		return "C1G2TagInventoryMask [parameterHeader=" + parameterHeader + ", mB=" + mB + ", pointer=" + pointer + ", maskBitCount=" + maskBitCount
				+ ", tagMask=" + tagMask + "]";
	}
}