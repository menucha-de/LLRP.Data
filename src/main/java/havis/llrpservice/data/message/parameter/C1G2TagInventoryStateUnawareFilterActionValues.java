package havis.llrpservice.data.message.parameter;

public enum C1G2TagInventoryStateUnawareFilterActionValues {

	SELECT__UNSELECT((short) 0), //
	SELECT__DO_NOTHING((short) 1), //
	DO_NOTHING__UNSELECT((short) 2), //
	UNSELECT__DO_NOTHING((short) 3), //
	UNSELECT__SELECT((short) 4), //
	DO_NOTHING__SELECT((short) 5);

	private final short value;

	private C1G2TagInventoryStateUnawareFilterActionValues(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2TagInventoryStateUnawareFilterActionValues get(short value) {
		for (C1G2TagInventoryStateUnawareFilterActionValues actionValues : C1G2TagInventoryStateUnawareFilterActionValues.values()) {
			if (actionValues.value == value) {
				return actionValues;
			}
		}
		return null;
	}
}