package havis.llrpservice.data.message.parameter;

public enum AccessReportTrigger {

	WHENEVER_ROREPORT_IS_GENERATED((short) 0), //
	END_OF_ACCESSSPEC((short) 1);

	private final short value;

	private AccessReportTrigger(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static AccessReportTrigger get(short value) {
		for (AccessReportTrigger trigger : AccessReportTrigger.values()) {
			if (trigger.value == value) {
				return trigger;
			}
		}
		return null;
	}
}