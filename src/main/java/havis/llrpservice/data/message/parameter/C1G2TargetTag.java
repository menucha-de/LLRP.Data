package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.BitSet;

public class C1G2TargetTag extends Parameter {

	private static final long serialVersionUID = 6906228392842015055L;

	private TLVParameterHeader parameterHeader;
	private byte memoryBank;
	private boolean isMatch;
	private int pointer;
	private int maskBitCount;
	private BitSet tagMask;
	private int dataBitCount;
	private BitSet tagData;

	public C1G2TargetTag() {
	}

	public C1G2TargetTag(TLVParameterHeader parameterHeader, byte memoryBank, boolean isMatch, int pointer, BitSet tagMask, BitSet tagData) {
		this.parameterHeader = parameterHeader;
		parameterHeader.setParameterType(ParameterType.C1G2_TARGET_TAG);
		this.memoryBank = memoryBank;
		this.isMatch = isMatch;
		this.pointer = pointer;
		this.tagMask = tagMask;
		this.tagData = tagData;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public byte getMemoryBank() {
		return memoryBank;
	}

	public void setMemoryBank(byte memoryBank) {
		this.memoryBank = memoryBank;
	}

	public boolean isMatch() {
		return isMatch;
	}

	public void setMatch(boolean isMatch) {
		this.isMatch = isMatch;
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

	public int getDataBitCount() {
		return dataBitCount;
	}

	public void setDataBitCount(int dataBitCount) {
		this.dataBitCount = dataBitCount;
	}

	public BitSet getTagData() {
		return tagData;
	}

	public void setTagData(BitSet tagData) {
		this.tagData = tagData;
	}

	@Override
	public String toString() {
		return "C1G2TargetTag [parameterHeader=" + parameterHeader + ", memoryBank=" + memoryBank + ", isMatch=" + isMatch + ", pointer=" + pointer
				+ ", maskBitCount=" + maskBitCount + ", tagMask=" + tagMask + ", dataBitCount=" + dataBitCount + ", tagData=" + tagData + "]";
	}
}