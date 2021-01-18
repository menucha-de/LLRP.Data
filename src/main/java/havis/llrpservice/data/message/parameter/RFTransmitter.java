package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class RFTransmitter extends Parameter {

	private static final long serialVersionUID = -4635631356786798112L;

	private TLVParameterHeader parameterHeader;
	private int hopTableID;
	private int channelIndex;
	private int transmitPower;

	public RFTransmitter() {
	}

	public RFTransmitter(TLVParameterHeader header, int hopTableID, int channelIndex, int transmitPower) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.RF_TRANSMITTER);
		this.hopTableID = hopTableID;
		this.channelIndex = channelIndex;
		this.transmitPower = transmitPower;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getHopTableID() {
		return hopTableID;
	}

	public void setHopTableID(int hopTableID) {
		this.hopTableID = hopTableID;
	}

	public int getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(int channelIndex) {
		this.channelIndex = channelIndex;
	}

	public int getTransmitPower() {
		return transmitPower;
	}

	public void setTransmitPower(int transmitPower) {
		this.transmitPower = transmitPower;
	}

	@Override
	public String toString() {
		return "RFTransmitter [parameterHeader=" + parameterHeader + ", hopTableID=" + hopTableID + ", channelIndex=" + channelIndex + ", transmitPower="
				+ transmitPower + "]";
	}
}