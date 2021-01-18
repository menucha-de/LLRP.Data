package havis.llrpservice.data.message.parameter;

public enum C1G2BlockPermalockOpSpecResultValues {

	SUCCESS((short) 0), //
	INSUFFICIENT_POWER_TO_PERFORM_BLOCK_PERMALOCK_OPERATION((short) 1), //
	NON_SPECIFIC_TAG_ERROR((short) 2), //
	NO_RESPONSE_FROM_TAG((short) 3), //
	NON_SPECIFIC_READER_ERROR((short) 4), //
	INCORRECT_PASSWORD_ERROR((short) 5), //
	TAG_MEMORY_OVERRUN_ERROR((short) 6);

	private final short value;

	private C1G2BlockPermalockOpSpecResultValues(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2BlockPermalockOpSpecResultValues get(short value) {
		for (C1G2BlockPermalockOpSpecResultValues resultValues : C1G2BlockPermalockOpSpecResultValues.values()) {
			if (resultValues.value == value) {
				return resultValues;
			}
		}
		return null;
	}
}