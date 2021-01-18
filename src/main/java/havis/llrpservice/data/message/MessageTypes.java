package havis.llrpservice.data.message;

public final class MessageTypes {

	public enum MessageType {

		GET_READER_CAPABILITIES((short) 1), //
		GET_READER_CONFIG((short) 2), //
		SET_READER_CONFIG((short) 3), //
		CLOSE_CONNECTION_RESPONSE((short) 4), //
		GET_READER_CAPABILITIES_RESPONSE((short) 11), //
		GET_READER_CONFIG_RESPONSE((short) 12), //
		SET_READER_CONFIG_RESPONSE((short) 13), //
		CLOSE_CONNECTION((short) 14), //
		ADD_ROSPEC((short) 20), //
		DELETE_ROSPEC((short) 21), //
		START_ROSPEC((short) 22), //
		STOP_ROSPEC((short) 23), //
		ENABLE_ROSPEC((short) 24), //
		DISABLE_ROSPEC((short) 25), //
		GET_ROSPECS((short) 26), //
		ADD_ROSPEC_RESPONSE((short) 30), //
		DELETE_ROSPEC_RESPONSE((short) 31), //
		START_ROSPEC_RESPONSE((short) 32), //
		STOP_ROSPEC_RESPONSE((short) 33), //
		ENABLE_ROSPEC_RESPONSE((short) 34), //
		DISABLE_ROSPEC_RESPONSE((short) 35), //
		GET_ROSPECS_RESPONSE((short) 36), //
		ADD_ACCESSSPEC((short) 40), //
		DELETE_ACCESSSPEC((short) 41), //
		ENABLE_ACCESSSPEC((short) 42), //
		DISABLE_ACCESSSPEC((short) 43), //
		GET_ACCESSSPECS((short) 44), //
		CLIENT_REQUEST_OP((short) 45), //
		GET_SUPPORTED_VERSION((short) 46), //
		SET_PROTOCOL_VERSION((short) 47), //
		ADD_ACCESSSPEC_RESPONSE((short) 50), //
		DELETE_ACCESSSPEC_RESPONSE((short) 51), //
		ENABLE_ACCESSSPEC_RESPONSE((short) 52), //
		DISABLE_ACCESSSPEC_RESPONSE((short) 53), //
		GET_ACCESSSPECS_RESPONSE((short) 54), //
		CLIENT_REQUEST_OP_RESPONSE((short) 55), //
		GET_SUPPORTED_VERSION_RESPONSE((short) 56), //
		SET_PROTOCOL_VERSION_RESPONSE((short) 57), //
		GET_REPORT((short) 60), //
		RO_ACCESS_REPORT((short) 61), //
		KEEPALIVE((short) 62), //
		READER_EVENT_NOTIFICATION((short) 63), //
		ENABLE_EVENTS_AND_REPORTS((short) 64), //
		KEEPALIVE_ACK((short) 72), //
		ERROR_MESSAGE((short) 100), //
		CUSTOM_MESSAGE((short) 1023);

		private final short value;

		private MessageType(short value) {
			this.value = value;
		}

		public final short getValue() {
			return value;
		}
	}

	public static final MessageType get(short value) {
		for (MessageType type : MessageType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}