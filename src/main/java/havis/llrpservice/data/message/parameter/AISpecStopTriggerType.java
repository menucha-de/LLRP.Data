package havis.llrpservice.data.message.parameter;

public enum AISpecStopTriggerType {

	NULL((short) 0), //
	DURATION((short) 1), //
	GPI_WITH_TIMEOUT((short) 2), //
	TAG_OBSERVATION((short) 3);

	private final short value;

	private AISpecStopTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static AISpecStopTriggerType get(short value) {
		for (AISpecStopTriggerType type : AISpecStopTriggerType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}