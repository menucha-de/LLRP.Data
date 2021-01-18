package havis.llrpservice.data.message.parameter;

public enum C1G2TagInventoryStateAwareSingulationActionS {

	SL(false), //
	_SL(true);

	private final boolean value;

	private C1G2TagInventoryStateAwareSingulationActionS(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public static C1G2TagInventoryStateAwareSingulationActionS get(boolean value) {
		for (C1G2TagInventoryStateAwareSingulationActionS val : C1G2TagInventoryStateAwareSingulationActionS.values()) {
			if (val.value == value) {
				return val;
			}
		}
		return null;
	}
}