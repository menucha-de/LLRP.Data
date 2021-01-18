package havis.llrpservice.data.message.parameter;

public enum ROSpecStopTriggerType {

	NULL((short) 0), //
	DURATION((short) 1), //
	GPI_WITH_TIMEOUT_VALUE((short) 2);

	private final short value;

	private ROSpecStopTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static ROSpecStopTriggerType get(short value) {
		for (ROSpecStopTriggerType triggerType : ROSpecStopTriggerType.values()) {
			if (triggerType.value == value) {
				return triggerType;
			}
		}
		return null;
	}
}