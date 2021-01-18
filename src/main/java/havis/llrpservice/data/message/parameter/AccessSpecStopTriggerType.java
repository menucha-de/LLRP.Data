package havis.llrpservice.data.message.parameter;

public enum AccessSpecStopTriggerType {

	NULL((short) 0), //
	OPERATION_COUNT((short) 1);

	private final short value;

	private AccessSpecStopTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static AccessSpecStopTriggerType get(short value) {
		for (AccessSpecStopTriggerType type : AccessSpecStopTriggerType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}