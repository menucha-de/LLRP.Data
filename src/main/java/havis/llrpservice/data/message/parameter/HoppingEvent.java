package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class HoppingEvent extends Parameter {

	private static final long serialVersionUID = -1371319316068914199L;

	private TLVParameterHeader parameterHeader;
	private int hopTableID;
	private int nextChannelIndex;

	public HoppingEvent() {
	}

	public HoppingEvent(TLVParameterHeader parameterHeader, int hopTableID, int nextChannelIndex) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.HOPPING_EVENT);
		this.hopTableID = hopTableID;
		this.nextChannelIndex = nextChannelIndex;
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

	public int getNextChannelIndex() {
		return nextChannelIndex;
	}

	public void setNextChannelIndex(int nextChannelIndex) {
		this.nextChannelIndex = nextChannelIndex;
	}

	@Override
	public String toString() {
		return "HoppingEvent [parameterHeader=" + parameterHeader + ", hopTableID=" + hopTableID + ", nextChannelIndex=" + nextChannelIndex + "]";
	}
}