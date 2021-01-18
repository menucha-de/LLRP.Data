package havis.llrpservice.data.message.parameter;

public enum C1G2LockPayloadPrivilege {

	READ_WRITE((short) 0), //
	PERMALOCK((short) 1), //
	PERMAUNLOCK((short) 2), //
	UNLOCK((short) 3);

	private short value;

	private C1G2LockPayloadPrivilege(short value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static C1G2LockPayloadPrivilege get(short value) {
		for (C1G2LockPayloadPrivilege pID : C1G2LockPayloadPrivilege.values()) {
			if (pID.value == value) {
				return pID;
			}
		}
		return null;
	}
}