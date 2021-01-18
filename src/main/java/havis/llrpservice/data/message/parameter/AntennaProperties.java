package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AntennaProperties extends Parameter {

	private static final long serialVersionUID = 7217657121318492964L;

	private TLVParameterHeader parameterHeader;
	private boolean connected;
	private int antennaID;
	private short antennaGain;

	public AntennaProperties() {
	}

	public AntennaProperties(TLVParameterHeader header, boolean connected, int antennaID, short antennaGain) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.ANTENNA_PROPERTIES);
		this.connected = connected;
		this.antennaID = antennaID;
		this.antennaGain = antennaGain;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	public short getAntennaGain() {
		return antennaGain;
	}

	public void setAntennaGain(short antennaGain) {
		this.antennaGain = antennaGain;
	}

	@Override
	public String toString() {
		return "AntennaProperties [parameterHeader=" + parameterHeader + ", connected=" + connected + ", antennaID=" + antennaID + ", antennaGain="
				+ antennaGain + "]";
	}
}