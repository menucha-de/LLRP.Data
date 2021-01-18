package havis.llrpservice.data.message.parameter;

public enum C1G2TagInventoryStateAwareFilterActionTarget {

	SL((short) 0), //
	INVENTORIED_STATE_FOR_SESSION_S0((short) 1), //
	INVENTORIED_STATE_FOR_SESSION_S1((short) 2), //
	INVENTORIED_STATE_FOR_SESSION_S2((short) 3), //
	INVENTORIED_STATE_FOR_SESSION_S3((short) 4);

	private final short value;

	private C1G2TagInventoryStateAwareFilterActionTarget(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2TagInventoryStateAwareFilterActionTarget get(short value) {
		for (C1G2TagInventoryStateAwareFilterActionTarget type : C1G2TagInventoryStateAwareFilterActionTarget.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}