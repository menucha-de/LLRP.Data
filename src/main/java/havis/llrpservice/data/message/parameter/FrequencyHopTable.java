package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class FrequencyHopTable extends Parameter {

	private static final long serialVersionUID = -5592725727202505914L;

	private TLVParameterHeader parameterHeader;
	private short hopTableID;
	private int numHops;
	private List<Long> hopFrequency;

	public FrequencyHopTable() {
	}

	public FrequencyHopTable(TLVParameterHeader header, short hopTableID, List<Long> hopFrequency) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.FREQUENCY_HOP_TABLE);
		this.hopFrequency = hopFrequency;
		this.hopTableID = hopTableID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public short getHopTableID() {
		return hopTableID;
	}

	public void setHopTableID(short hopTableID) {
		this.hopTableID = hopTableID;
	}

	public List<Long> getHopFrequency() {
		return hopFrequency;
	}

	public void setHopFrequency(List<Long> hopFrequency) {
		this.hopFrequency = hopFrequency;
	}

	public int getNumHops() {
		return numHops;
	}

	public void setNumHops(int numHops) {
		this.numHops = numHops;
	}

	@Override
	public String toString() {
		return "FrequencyHopTable [parameterHeader=" + parameterHeader + ", hopTableID=" + hopTableID + ", numHops=" + numHops + ", hopFrequency="
				+ hopFrequency + "]";
	}
}