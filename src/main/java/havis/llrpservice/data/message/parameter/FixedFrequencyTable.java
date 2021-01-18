package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class FixedFrequencyTable extends Parameter {

	private static final long serialVersionUID = -5941560665499145522L;

	private TLVParameterHeader parameterHeader;
	private int numFrequencies;
	private List<Long> frequency;

	public FixedFrequencyTable() {
	}

	public FixedFrequencyTable(TLVParameterHeader header, List<Long> frequency) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.FIXED_FREQUENCY_TABLE);
		this.frequency = frequency;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public List<Long> getFrequency() {
		return frequency;
	}

	public void setFrequency(List<Long> frequency) {
		this.frequency = frequency;
	}

	public int getNumFrequencies() {
		return numFrequencies;
	}

	public void setNumFrequencies(int numFrequencies) {
		this.numFrequencies = numFrequencies;
	}

	@Override
	public String toString() {
		return "FixedFrequencyTable [parameterHeader=" + parameterHeader + ", numFrequencies=" + numFrequencies + ", frequency=" + frequency + "]";
	}
}