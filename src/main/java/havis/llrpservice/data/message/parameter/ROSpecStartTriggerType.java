package havis.llrpservice.data.message.parameter;

public enum ROSpecStartTriggerType {

	NULL_NO_START_TRIGGER((short) 0), //
	IMMEDIATE((short) 1), //
	PERIODIC((short) 2), //
	GPI((short) 3);

	private final short value;

	private ROSpecStartTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static ROSpecStartTriggerType get(short value) {
		for (ROSpecStartTriggerType triggerType : ROSpecStartTriggerType.values()) {
			if (triggerType.value == value) {
				return triggerType;
			}
		}
		return null;
	}
}