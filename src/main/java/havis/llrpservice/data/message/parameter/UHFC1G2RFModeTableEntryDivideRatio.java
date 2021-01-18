package havis.llrpservice.data.message.parameter;

public enum UHFC1G2RFModeTableEntryDivideRatio {

	_8(false), //
	_64DIV3(true);

	private final boolean value;

	private UHFC1G2RFModeTableEntryDivideRatio(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public static UHFC1G2RFModeTableEntryDivideRatio get(boolean value) {
		for (UHFC1G2RFModeTableEntryDivideRatio ratio : UHFC1G2RFModeTableEntryDivideRatio.values()) {
			if (ratio.value == value) {
				return ratio;
			}
		}
		return null;
	}
}