package havis.llrpservice.data.message.parameter;

public enum C1G2LockPayloadDataField {

	KILL_PASSWORD((short) 0), //
	ACCESS_PASSWORD((short) 1), //
	EPC_MEMORY((short) 2), //
	TID_MEMORY((short) 3), //
	USER_MEMORY((short) 4);

	private short value;

	private C1G2LockPayloadDataField(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static C1G2LockPayloadDataField get(short value) {
		for (C1G2LockPayloadDataField pID : C1G2LockPayloadDataField.values()) {
			if (pID.value == value) {
				return pID;
			}
		}
		return null;
	}
}