package havis.llrpservice.data.message;

public enum GetReaderConfigRequestedData {

	ALL((short) 0), IDENTIFICATION((short) 1), ANTENNA_PROPERTIES((short) 2), ANTENNA_CONFIGURATION((short) 3), RO_REPORT_SPEC((short) 4), READER_EVENT_NOTIFICATION(
			(short) 5), ACCESS_REPORT_SPEC((short) 6), LLRP_CONFIGURATION_STATE_VALUE((short) 7), KEEPALIVE_SPEC((short) 8), GPI_CURRENT_STATE((short) 9), GPO_WRITE_DATA(
			(short) 10), EVENTS_AND_REPORTS((short) 11);

	private final short value;

	private GetReaderConfigRequestedData(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static GetReaderConfigRequestedData get(short value) {
		for (GetReaderConfigRequestedData requestedData : GetReaderConfigRequestedData.values()) {
			if (requestedData.value == value) {
				return requestedData;
			}
		}
		return null;
	}
}