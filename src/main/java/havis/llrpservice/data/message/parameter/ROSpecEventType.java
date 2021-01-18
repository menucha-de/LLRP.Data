package havis.llrpservice.data.message.parameter;

public enum ROSpecEventType {

	START_OF_ROSPEC((short) 0), //
	END_OF_ROSPEC((short) 1), //
	PREEMPTION_OF_ROSPEC((short) 2);

	private final short value;

	private ROSpecEventType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static ROSpecEventType get(short value) {
		for (ROSpecEventType eventType : ROSpecEventType.values()) {
			if (eventType.value == value) {
				return eventType;
			}
		}
		return null;
	}
}