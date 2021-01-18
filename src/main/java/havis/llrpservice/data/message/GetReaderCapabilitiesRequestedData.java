package havis.llrpservice.data.message;

public enum GetReaderCapabilitiesRequestedData {

	ALL((short) 0), GENERAL_DEVICE_CAPABILITIES((short) 1), LLRP_CAPABILITIES((short) 2), REGULATORY_CAPABILITIES((short) 3), C1G2_LLRP_CAPABILITIES((short) 4);

	private final short value;

	private GetReaderCapabilitiesRequestedData(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static GetReaderCapabilitiesRequestedData get(short value) {
		for (GetReaderCapabilitiesRequestedData requestedData : GetReaderCapabilitiesRequestedData.values()) {
			if (requestedData.value == value) {
				return requestedData;
			}
		}
		return null;
	}
};