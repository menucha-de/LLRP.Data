package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2CRC extends Parameter {

	private static final long serialVersionUID = 8119612289985071747L;

	private TVParameterHeader parameterHeader;
	private int crc;

	public C1G2CRC() {
	}

	public C1G2CRC(TVParameterHeader parameterHeader, int crc) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_CRC);
		this.crc = crc;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	@Override
	public String toString() {
		return "C1G2CRC [parameterHeader=" + parameterHeader + ", crc=" + crc + "]";
	}
}