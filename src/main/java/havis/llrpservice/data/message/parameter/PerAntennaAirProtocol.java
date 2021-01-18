package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class PerAntennaAirProtocol extends Parameter {

	private static final long serialVersionUID = -6469901128579071638L;

	private TLVParameterHeader parameterHeader;
	private int antennaID;
	private int numProtocols;
	private List<ProtocolId> airProtocolsSupported;

	public PerAntennaAirProtocol() {
	}

	public PerAntennaAirProtocol(TLVParameterHeader header, int antennaID, List<ProtocolId> airProtocolsSupported) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.PER_ANTENNA_AIR_PROTOCOL);
		this.antennaID = antennaID;
		this.airProtocolsSupported = airProtocolsSupported;
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

	public int getNumProtocols() {
		return numProtocols;
	}

	public void setNumProtocols(int numProtocols) {
		this.numProtocols = numProtocols;
	}

	public List<ProtocolId> getAirProtocolsSupported() {
		return airProtocolsSupported;
	}

	public void setAirProtocolsSupported(List<ProtocolId> airProtocolsSupported) {
		this.airProtocolsSupported = airProtocolsSupported;
	}

	@Override
	public String toString() {
		return "PerAntennaAirProtocol [parameterHeader=" + parameterHeader + ", antennaID=" + antennaID + ", numProtocols=" + numProtocols
				+ ", airProtocolsSupported=" + airProtocolsSupported + "]";
	}
}