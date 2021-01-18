package havis.llrpservice.data.message.parameter;

public enum ConnectionAttemptEventStatusType {

	SUCCESS((short) 0), //
	FAILED_READER_CONNECTION_EXISTS((short) 1), //
	FAILED_CLIENT_CONNECTION_EXISTS((short) 2), //
	FAILED_ANY_OTHER_REASON((short) 3), //
	ANOTHER_CONNECTION_ATTEMPTED((short) 4);

	private final short value;

	private ConnectionAttemptEventStatusType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static ConnectionAttemptEventStatusType get(short value) {
		for (ConnectionAttemptEventStatusType type : ConnectionAttemptEventStatusType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}