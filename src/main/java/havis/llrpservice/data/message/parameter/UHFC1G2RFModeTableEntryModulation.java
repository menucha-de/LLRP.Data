package havis.llrpservice.data.message.parameter;

public enum UHFC1G2RFModeTableEntryModulation {

	FM0((short) 0), //
	_2((short) 1), //
	_4((short) 2), //
	_8((short) 3);

	private final short value;

	private UHFC1G2RFModeTableEntryModulation(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static UHFC1G2RFModeTableEntryModulation get(short value) {
		for (UHFC1G2RFModeTableEntryModulation mod : UHFC1G2RFModeTableEntryModulation.values()) {
			if (mod.value == value) {
				return mod;
			}
		}
		return null;
	}
}