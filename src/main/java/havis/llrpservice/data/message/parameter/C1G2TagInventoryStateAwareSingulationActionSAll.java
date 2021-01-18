package havis.llrpservice.data.message.parameter;

public enum C1G2TagInventoryStateAwareSingulationActionSAll {

	NO(false), //
	ALL(true);

	private final boolean value;

	private C1G2TagInventoryStateAwareSingulationActionSAll(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public static C1G2TagInventoryStateAwareSingulationActionSAll get(boolean value) {
		for (C1G2TagInventoryStateAwareSingulationActionSAll val : C1G2TagInventoryStateAwareSingulationActionSAll.values()) {
			if (val.value == value) {
				return val;
			}
		}
		return null;
	}
}