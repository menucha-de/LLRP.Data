package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class Identification extends Parameter {

	private static final long serialVersionUID = -5133588642016399739L;

	private TLVParameterHeader parameterHeader;
	private IdentificationIDType idType;
	private int byteCount;
	private byte[] readerID;

	public Identification() {
	}

	public Identification(TLVParameterHeader header, IdentificationIDType idType, byte[] data) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.IDENTIFICATION);
		this.idType = idType;
		this.readerID = data;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public IdentificationIDType getIdType() {
		return idType;
	}

	public void setIdType(IdentificationIDType idType) {
		this.idType = idType;
	}

	public int getByteCount() {
		return byteCount;
	}

	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	public byte[] getReaderID() {
		return readerID;
	}

	public void setReaderID(byte[] data) {
		this.readerID = data;
	}

	@Override
	public String toString() {
		return "Identification [parameterHeader=" + parameterHeader + ", idType=" + idType + ", byteCount=" + byteCount + ", readerID="
				+ Arrays.toString(readerID) + "]";
	}
}