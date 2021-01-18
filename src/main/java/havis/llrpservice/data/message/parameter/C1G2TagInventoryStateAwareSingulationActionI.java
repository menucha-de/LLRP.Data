package havis.llrpservice.data.message.parameter;

public enum C1G2TagInventoryStateAwareSingulationActionI {

	STATE_A(false), //
	STATE_B(true);

	private final boolean value;

	private C1G2TagInventoryStateAwareSingulationActionI(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public static C1G2TagInventoryStateAwareSingulationActionI get(boolean value) {
		for (C1G2TagInventoryStateAwareSingulationActionI val : C1G2TagInventoryStateAwareSingulationActionI.values()) {
			if (val.value == value) {
				return val;
			}
		}
		return null;
	}
}