package havis.llrpservice.data.message;

public enum ProtocolVersion {

	LLRP_V1_0_1((byte) 1), //
	LLRP_V1_1((byte) 2);

	private final byte value;

	private ProtocolVersion(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public static ProtocolVersion get(byte value) {
		for (ProtocolVersion version : ProtocolVersion.values()) {
			if (version.value == value) {
				return version;
			}
		}
		return null;
	}
}