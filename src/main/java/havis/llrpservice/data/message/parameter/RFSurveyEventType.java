package havis.llrpservice.data.message.parameter;

public enum RFSurveyEventType {

	START_OF_RFSURVEY((short) 0), //
	END_OF_RFSURVEY((short) 1);

	private final short value;

	private RFSurveyEventType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static RFSurveyEventType get(short value) {
		for (RFSurveyEventType eventType : RFSurveyEventType.values()) {
			if (eventType.value == value) {
				return eventType;
			}
		}
		return null;
	}
}