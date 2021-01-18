package havis.llrpservice.data.message.parameter;

public enum ProtocolId {

	UNSPECIFIED_AIR_PROTOCOL(0), //
	EPC_GLOBAL_C1G2(1);

	private int value;

	private ProtocolId(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ProtocolId get(int value) {
		for (ProtocolId pID : ProtocolId.values()) {
			if (pID.value == value) {
				return pID;
			}
		}
		return null;
	}
}