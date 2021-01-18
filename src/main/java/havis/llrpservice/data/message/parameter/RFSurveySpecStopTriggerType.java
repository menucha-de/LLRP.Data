package havis.llrpservice.data.message.parameter;

public enum RFSurveySpecStopTriggerType {

	NULL((short) 0), //
	DURATION((short) 1), //
	N_ITERATIONS_THROUGH_THE_FREQUENCY_RANGE((short) 2);

	private final short value;

	private RFSurveySpecStopTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static RFSurveySpecStopTriggerType get(short value) {
		for (RFSurveySpecStopTriggerType triggerType : RFSurveySpecStopTriggerType.values()) {
			if (triggerType.value == value) {
				return triggerType;
			}
		}
		return null;
	}
}