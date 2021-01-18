package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AntennaId extends Parameter {

	private static final long serialVersionUID = 6982407895226102671L;

	private TVParameterHeader parameterHeader;
	private int antennaId;

	public AntennaId() {
	}

	public AntennaId(TVParameterHeader parameterHeader, int antennaId) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.ANTENNA_ID);
		this.antennaId = antennaId;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getAntennaId() {
		return antennaId;
	}

	public void setAntennaId(int antennaId) {
		this.antennaId = antennaId;
	}

	@Override
	public String toString() {
		return "AntennaId [parameterHeader=" + parameterHeader + ", antennaId=" + antennaId + "]";
	}
}