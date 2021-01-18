package havis.llrpservice.data.message.parameter;

public enum C1G2FilterTruncateAction {

	UNSPECIFIED((byte) 0), //
	DO_NOT_TRUNCATE((byte) 1), //
	TRUNCATE((byte) 2);

	private final byte value;

	private C1G2FilterTruncateAction(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public static C1G2FilterTruncateAction get(byte value) {
		for (C1G2FilterTruncateAction type : C1G2FilterTruncateAction.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}