package havis.llrpservice.data.message.parameter;

public enum GPIPortCurrentStateGPIState {

	LOW((short) 0), //
	HIGH((short) 1), //
	UNKNOWN((short) 2);

	private final short value;

	private GPIPortCurrentStateGPIState(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static GPIPortCurrentStateGPIState get(short value) {
		for (GPIPortCurrentStateGPIState triggerType : GPIPortCurrentStateGPIState.values()) {
			if (triggerType.value == value) {
				return triggerType;
			}
		}
		return null;
	}
}