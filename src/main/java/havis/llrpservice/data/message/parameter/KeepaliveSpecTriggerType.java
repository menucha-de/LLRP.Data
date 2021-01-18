package havis.llrpservice.data.message.parameter;

public enum KeepaliveSpecTriggerType {

	NULL((short) 0), //
	PERIODIC((short) 1);

	private final short value;

	private KeepaliveSpecTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static KeepaliveSpecTriggerType get(short value) {
		for (KeepaliveSpecTriggerType triggerType : KeepaliveSpecTriggerType.values()) {
			if (triggerType.value == value) {
				return triggerType;
			}
		}
		return null;
	}
}