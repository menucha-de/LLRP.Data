package havis.llrpservice.data.message.parameter;

public enum IdentificationIDType {

	MAC_ADDRESS((short) 0), //
	EPC((short) 1);

	private final short value;

	private IdentificationIDType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static IdentificationIDType get(short value) {
		for (IdentificationIDType idType : IdentificationIDType.values()) {
			if (idType.value == value) {
				return idType;
			}
		}
		return null;
	}
}