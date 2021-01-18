package havis.llrpservice.data.message.parameter;

public enum C1G2KillOpSpecResultValues {

	SUCCESS((short) 0), //
	ZERO_KILL_PASSWORD_ERROR((short) 1), //
	INSUFFICIENT_POWER_TO_PERFORM_KILL_OPERATION((short) 2), //
	NON_SPECIFIC_TAG_ERROR((short) 3), //
	NO_RESPONSE_FROM_TAG((short) 4), //
	NON_SPECIFIC_READER_ERROR((short) 5), //
	INCORRECT_PASSWORD_ERROR((short) 6);

	private final short value;

	private C1G2KillOpSpecResultValues(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2KillOpSpecResultValues get(short value) {
		for (C1G2KillOpSpecResultValues resultValues : C1G2KillOpSpecResultValues.values()) {
			if (resultValues.value == value) {
				return resultValues;
			}
		}
		return null;
	}
}