package havis.llrpservice.data.message.parameter;

public enum AntennaEventType {

	ANTENNA_DISCONNECTED((short) 0), //
	ANTENNA_CONNECTED((short) 1);

	private final short value;

	private AntennaEventType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static AntennaEventType get(short value) {
		for (AntennaEventType eventType : AntennaEventType.values()) {
			if (eventType.value == value) {
				return eventType;
			}
		}
		return null;
	}
}