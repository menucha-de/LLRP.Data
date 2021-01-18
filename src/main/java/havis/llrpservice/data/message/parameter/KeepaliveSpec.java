package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class KeepaliveSpec extends Parameter {

	private static final long serialVersionUID = 2102564023736522999L;

	private TLVParameterHeader parameterHeader;
	private KeepaliveSpecTriggerType triggerType;
	private long timeInterval;

	private void init(TLVParameterHeader header) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.KEEPALIVE_SPEC);
	}

	public KeepaliveSpec() {
	}

	public KeepaliveSpec(TLVParameterHeader header, KeepaliveSpecTriggerType triggerType) {
		init(header);
		this.triggerType = triggerType;
	}

	public KeepaliveSpec(TLVParameterHeader header, KeepaliveSpecTriggerType triggerType, long timeInterval) {
		init(header);
		this.triggerType = triggerType;
		this.timeInterval = timeInterval;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public KeepaliveSpecTriggerType getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(KeepaliveSpecTriggerType triggerType) {
		this.triggerType = triggerType;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	@Override
	public String toString() {
		return "KeepaliveSpec [parameterHeader=" + parameterHeader + ", triggerType=" + triggerType + ", timeInterval=" + timeInterval + "]";
	}
}