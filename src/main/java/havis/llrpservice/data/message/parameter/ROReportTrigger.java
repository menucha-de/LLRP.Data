package havis.llrpservice.data.message.parameter;

public enum ROReportTrigger {

	NONE((short) 0), //
	UPON_N_TAGREPORTDATA_PARAMETERS_OR_END_OF_AISPEC((short) 1), //
	UPON_N_TAGREPORTDATA_PARAMETERS_OR_END_OF_ROSPEC((short) 2), //
	UPON_N_SECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC((short) 3), //
	UPON_N_SECONDS_OR_END_OF_ROSPEC((short) 4), //
	UPON_N_MILLISECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC((short) 5), //
	UPON_N_MILLISECONDS_OR_END_OF_ROSPEC((short) 6);

	private final short value;

	private ROReportTrigger(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static ROReportTrigger get(short value) {
		for (ROReportTrigger trigger : ROReportTrigger.values()) {
			if (trigger.value == value) {
				return trigger;
			}
		}
		return null;
	}
}