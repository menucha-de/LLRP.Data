package havis.llrpservice.data.message.parameter;

public enum UHFC1G2RFModeTableEntrySpectralMaskIndicator {

	UNKNOWN((short) 0), //
	SINGLE_INTERROGATOR_MODE_MASK((short) 1), //
	MULTI_INTERROGATOR_MODE_MASK((short) 2), //
	DENSE_INTERROGATOR_MODE_MASK((short) 3);

	private final short value;

	private UHFC1G2RFModeTableEntrySpectralMaskIndicator(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static UHFC1G2RFModeTableEntrySpectralMaskIndicator get(short value) {
		for (UHFC1G2RFModeTableEntrySpectralMaskIndicator ind : UHFC1G2RFModeTableEntrySpectralMaskIndicator.values()) {
			if (ind.value == value) {
				return ind;
			}
		}
		return null;
	}
}