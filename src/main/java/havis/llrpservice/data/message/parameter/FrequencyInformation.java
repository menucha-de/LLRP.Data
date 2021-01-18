package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class FrequencyInformation extends Parameter {

	private static final long serialVersionUID = -4600174032376272346L;

	private TLVParameterHeader parameterHeader;
	private List<FrequencyHopTable> freqHopInfo;
	private FixedFrequencyTable fixedFreqInfo;
	boolean hopping;

	private void init(TLVParameterHeader header) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.FREQUENCY_INFORMATION);
	}

	public FrequencyInformation() {
	}

	public FrequencyInformation(TLVParameterHeader header, boolean hopping) {
		init(header);
		this.hopping = hopping;
	}

	public FrequencyInformation(TLVParameterHeader header, List<FrequencyHopTable> freqHopInfo) {
		init(header);
		this.freqHopInfo = freqHopInfo;
		hopping = true;
	}

	public FrequencyInformation(TLVParameterHeader header, FixedFrequencyTable fixedFreqInfo) {
		init(header);
		this.fixedFreqInfo = fixedFreqInfo;
		hopping = false;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public List<FrequencyHopTable> getFreqHopInfo() {
		return freqHopInfo;
	}

	public void setFreqHopInfo(List<FrequencyHopTable> freqHopInfo) {
		this.freqHopInfo = freqHopInfo;
	}

	public FixedFrequencyTable getFixedFreqInfo() {
		return fixedFreqInfo;
	}

	public void setFixedFreqInfo(FixedFrequencyTable fixedFreqInfo) {
		this.fixedFreqInfo = fixedFreqInfo;
	}

	public boolean isHopping() {
		return hopping;
	}

	public void setHopping(boolean hopping) {
		this.hopping = hopping;
	}

	@Override
	public String toString() {
		return "FrequencyInformation [parameterHeader=" + parameterHeader + ", freqHopInfo=" + freqHopInfo + ", fixedFreqInfo=" + fixedFreqInfo + "]";
	}
}