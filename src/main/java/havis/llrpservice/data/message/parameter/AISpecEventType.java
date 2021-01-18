package havis.llrpservice.data.message.parameter;

public enum AISpecEventType {

	END_OF_AISPEC((short) 0);

	private final short value;

	private AISpecEventType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static AISpecEventType get(short value) {
		for (AISpecEventType eventType : AISpecEventType.values()) {
			if (eventType.value == value) {
				return eventType;
			}
		}
		return null;
	}
}