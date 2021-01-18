package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class FrequencyRSSILevelEntry extends Parameter {

	private static final long serialVersionUID = 4485881075308535803L;

	private TLVParameterHeader parameterHeader;
	private long frequency;
	private long bandWidth;
	private byte averageRSSI;
	private byte peekRSSI;
	private UTCTimestamp utcTimestamp;
	private Uptime upTime;

	private void init(TLVParameterHeader parameterHeader, long frequency, long bandWidth, byte averageRSSI, byte peekRSSI) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.FREQUENCY_RSSI_LEVEL_ENTRY);
		this.frequency = frequency;
		this.bandWidth = bandWidth;
		this.averageRSSI = averageRSSI;
		this.peekRSSI = peekRSSI;
	}

	public FrequencyRSSILevelEntry() {
	}

	public FrequencyRSSILevelEntry(TLVParameterHeader parameterHeader, long frequency, long bandWidth, byte averageRSSI, byte peekRSSI,
			UTCTimestamp utcTimestamp) {
		init(parameterHeader, frequency, bandWidth, averageRSSI, peekRSSI);
		this.utcTimestamp = utcTimestamp;
	}

	public FrequencyRSSILevelEntry(TLVParameterHeader parameterHeader, long frequency, long bandWidth, byte averageRSSI, byte peekRSSI, Uptime upTime) {
		init(parameterHeader, frequency, bandWidth, averageRSSI, peekRSSI);
		this.upTime = upTime;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public long getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(long bandWidth) {
		this.bandWidth = bandWidth;
	}

	public byte getAverageRSSI() {
		return averageRSSI;
	}

	public void setAverageRSSI(byte averageRSSI) {
		this.averageRSSI = averageRSSI;
	}

	public byte getPeekRSSI() {
		return peekRSSI;
	}

	public void setPeekRSSI(byte peekRSSI) {
		this.peekRSSI = peekRSSI;
	}

	public UTCTimestamp getUtcTimestamp() {
		return utcTimestamp;
	}

	public void setUtcTimestamp(UTCTimestamp utcTimestamp) {
		this.utcTimestamp = utcTimestamp;
	}

	public Uptime getUpTime() {
		return upTime;
	}

	public void setUpTime(Uptime upTime) {
		this.upTime = upTime;
	}

	@Override
	public String toString() {
		return "FrequencyRSSILevelEntry [parameterHeader=" + parameterHeader + ", frequency=" + frequency + ", bandWidth=" + bandWidth + ", averageRSSI="
				+ averageRSSI + ", peekRSSI=" + peekRSSI + ", utcTimestamp=" + utcTimestamp + ", upTime=" + upTime + "]";
	}
}