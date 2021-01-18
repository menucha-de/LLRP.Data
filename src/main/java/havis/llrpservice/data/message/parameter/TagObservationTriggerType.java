package havis.llrpservice.data.message.parameter;

public enum TagObservationTriggerType {

	UPON_SEEING_N_TAG_OBSERVATIONS_OR_TIMEOUT((short) 0), //
	UPON_SEEING_NO_MORE_NEW_TAG_OBSERVATIONS_FOR_T_MS_OR_TIMEOUT((short) 1), //
	N_ATTEMPTS_TO_SEE_ALL_TAGS_IN_THE_FOV_OR_TIMEOUT((short) 2), //
	UPON_SEEING_N_UNIQUE_TAG_OBSERVATIONS_OR_TIMEOUT((short) 3), //
	UPON_SEEING_NO_MORE_NEW_UNIQUE_TAG_OBSERVATIONS_FOR_T_MS_OR_TIMEOUT((short) 4);

	private final short value;

	private TagObservationTriggerType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static TagObservationTriggerType get(short value) {
		for (TagObservationTriggerType triggerType : TagObservationTriggerType.values()) {
			if (triggerType.value == value) {
				return triggerType;
			}
		}
		return null;
	}
}