package havis.llrpservice.data.message.parameter;

public enum UHFC1G2RFModeTableEntryForwardLinkModulation {

	PR_ASK((short) 0), //
	SSB_ASK((short) 1), //
	DSB_ASK((short) 2);

	private final short value;

	private UHFC1G2RFModeTableEntryForwardLinkModulation(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static UHFC1G2RFModeTableEntryForwardLinkModulation get(short value) {
		for (UHFC1G2RFModeTableEntryForwardLinkModulation mod : UHFC1G2RFModeTableEntryForwardLinkModulation.values()) {
			if (mod.value == value) {
				return mod;
			}
		}
		return null;
	}
}