package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class PerAntennaReceiveSensitivityRange extends Parameter {

	private static final long serialVersionUID = -1138078703254554308L;

	private TLVParameterHeader parameterHeader;
	private int antennaID;
	private int receiveSensitivityIndexMin;
	private int receiveSensitivityIndexMax;

	public PerAntennaReceiveSensitivityRange() {
	}

	public PerAntennaReceiveSensitivityRange(TLVParameterHeader header, int antennaID, int receiveSensitivityIndexMin, int receiveSensitivityIndexMax) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE);
		this.antennaID = antennaID;
		this.receiveSensitivityIndexMin = receiveSensitivityIndexMin;
		this.receiveSensitivityIndexMax = receiveSensitivityIndexMax;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	public int getReceiveSensitivityIndexMin() {
		return receiveSensitivityIndexMin;
	}

	public void setReceiveSensitivityIndexMin(int receiveSensitivityIndexMin) {
		this.receiveSensitivityIndexMin = receiveSensitivityIndexMin;
	}

	public int getReceiveSensitivityIndexMax() {
		return receiveSensitivityIndexMax;
	}

	public void setReceiveSensitivityIndexMax(int receiveSensitivityIndexMax) {
		this.receiveSensitivityIndexMax = receiveSensitivityIndexMax;
	}

	@Override
	public String toString() {
		return "PerAntennaReceiveSensitivityRange [parameterHeader=" + parameterHeader + ", antennaID=" + antennaID + ", receiveSensitivityIndexMin="
				+ receiveSensitivityIndexMin + ", receiveSensitivityIndexMax=" + receiveSensitivityIndexMax + "]";
	}
}