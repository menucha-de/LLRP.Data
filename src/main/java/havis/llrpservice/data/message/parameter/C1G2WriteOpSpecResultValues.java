package havis.llrpservice.data.message.parameter;

public enum C1G2WriteOpSpecResultValues {

	SUCCESS((short) 0), //
	TAG_MEMORY_OVERRUN_ERROR((short) 1), //
	TAG_MEMORY_LOCKED_ERROR((short) 2), //
	INSUFFICIENT_POWER_TO_PERFORM_MEMORY_WRITE_OPERATION((short) 3), //
	NON_SPECIFIC_TAG_ERROR((short) 4), //
	NO_RESPONSE_FROM_TAG((short) 5), //
	NON_SPECIFIC_READER_ERROR((short) 6), //
	INCORRECT_PASSWORD_ERROR((short) 7);

	private final short value;

	private C1G2WriteOpSpecResultValues(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2WriteOpSpecResultValues get(short value) {
		for (C1G2WriteOpSpecResultValues resultValues : C1G2WriteOpSpecResultValues.values()) {
			if (resultValues.value == value) {
				return resultValues;
			}
		}
		return null;
	}
}