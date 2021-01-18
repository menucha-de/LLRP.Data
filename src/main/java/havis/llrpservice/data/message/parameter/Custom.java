package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class Custom extends Parameter {

	private static final long serialVersionUID = -6127879981349000812L;

	private TLVParameterHeader parameterHeader;
	private long vendorID;
	private long subType;
	private byte[] data;

	public Custom() {
	}

	public Custom(TLVParameterHeader header, long vendorID, long subType, byte[] data) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.CUSTOM);
		this.vendorID = vendorID;
		this.subType = subType;
		this.data = data;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getVendorID() {
		return vendorID;
	}

	public void setVendorID(long vendorID) {
		this.vendorID = vendorID;
	}

	public long getSubType() {
		return subType;
	}

	public void setSubType(long subType) {
		this.subType = subType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Custom [parameterHeader=" + parameterHeader + ", vendorID=" + vendorID + ", subType=" + subType + ", data=" + Arrays.toString(data) + "]";
	}
}