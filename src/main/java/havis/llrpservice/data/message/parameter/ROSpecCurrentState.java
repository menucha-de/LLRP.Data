package havis.llrpservice.data.message.parameter;

public enum ROSpecCurrentState {

	DISABLED((short) 0), //
	INACTIVE((short) 1), //
	ACTIVE((short) 2);

	private final short value;

	private ROSpecCurrentState(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static ROSpecCurrentState get(short value) {
		for (ROSpecCurrentState currentState : ROSpecCurrentState.values()) {
			if (currentState.value == value) {
				return currentState;
			}
		}
		return null;
	}
}