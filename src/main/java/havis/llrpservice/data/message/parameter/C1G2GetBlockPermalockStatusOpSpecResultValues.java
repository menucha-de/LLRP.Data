package havis.llrpservice.data.message.parameter;

public enum C1G2GetBlockPermalockStatusOpSpecResultValues {

	SUCCESS((short) 0), //
	NON_SPECIFIC_TAG_ERROR((short) 1), //
	NO_RESPONSE_FROM_TAG((short) 2), //
	NON_SPECIFIC_READER_ERROR((short) 3), //
	INCORRECT_PASSWORD_ERROR((short) 4), //
	TAG_MEMORY_OVERRUN_ERROR((short) 5);

	private final short value;

	private C1G2GetBlockPermalockStatusOpSpecResultValues(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2GetBlockPermalockStatusOpSpecResultValues get(short value) {
		for (C1G2GetBlockPermalockStatusOpSpecResultValues resultValues : C1G2GetBlockPermalockStatusOpSpecResultValues.values()) {
			if (resultValues.value == value) {
				return resultValues;
			}
		}
		return null;
	}
}