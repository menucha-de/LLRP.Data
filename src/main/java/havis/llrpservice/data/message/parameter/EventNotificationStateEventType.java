package havis.llrpservice.data.message.parameter;

public enum EventNotificationStateEventType {

	UPON_HOPPING_TO_NEXT_CHANNEL((int) 0), //
	GPI_EVENT((int) 1), //
	ROSPEC_EVENT((int) 2), //
	REPORT_BUFFER_FILL_WARNING((int) 3), //
	READER_EXCEPTION_EVENT((int) 4), //
	RFSURVEY_EVENT((int) 5), //
	AISPEC_EVENT((int) 6), //
	AISPEC_EVENT_WITH_SINGULATION_DETAILS((int) 7), //
	ANTENNA_EVENT((int) 8), //
	SPECLOOP_EVENT((int) 9);

	private final int value;

	private EventNotificationStateEventType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static EventNotificationStateEventType get(int value) {
		for (EventNotificationStateEventType eventType : EventNotificationStateEventType.values()) {
			if (eventType.value == value) {
				return eventType;
			}
		}
		return null;
	}
}