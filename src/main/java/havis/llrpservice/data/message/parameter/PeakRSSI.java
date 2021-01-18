package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class PeakRSSI extends Parameter {

	private static final long serialVersionUID = 2591479027093437636L;

	private TVParameterHeader parameterHeader;
	private byte peakRSSI;

	public PeakRSSI() {
	}

	public PeakRSSI(TVParameterHeader parameterHeader, byte peakRSSI) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.PEAK_RSSI);
		this.peakRSSI = peakRSSI;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public byte getPeakRSSI() {
		return peakRSSI;
	}

	public void setPeakRSSI(byte peakRSSI) {
		this.peakRSSI = peakRSSI;
	}

	@Override
	public String toString() {
		return "PeakRSSI [parameterHeader=" + parameterHeader + ", peakRSSI=" + peakRSSI + "]";
	}
}