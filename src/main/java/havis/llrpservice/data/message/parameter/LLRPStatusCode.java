package havis.llrpservice.data.message.parameter;

public enum LLRPStatusCode {

	// Message
	M_SUCCESS(0), //
	M_PARAMETER_ERROR(100), //
	M_FIELD_ERROR(101), //
	M_UNEXPECTED_PARAMETER(102), //
	M_MISSING_PARAMETER(103), //
	M_DUPLICATE_PARAMETER(104), //
	M_OVERFLOW_PARAMETER(105), //
	M_OVERFLOW_FIELD(106), //
	M_UNKNOWN_PARAMETER(107), //
	M_UNKNOWN_FIELD(108), //
	M_UNSUPPORTED_MESSAGE(109), //
	M_UNSUPPORTED_VERSION(110), //
	M_UNSUPPORTED_PARAMETER(111), //
	M_UNEXPECTED_MESSAGE(112), //

	// Parameter
	P_PARAMETER_ERROR(200), //
	P_FIELD_ERROR(201), //
	P_UNEXPECTED_PARAMETER(202), //
	P_MISSING_PARAMETER(203), //
	P_DUPCLICATE_PARAMETER(204), //
	P_OVERFLOW_PARAMETER(205), //
	P_OVERFLOW_FIELD(206), //
	P_UNKNOWN_PARAMETER(207), //
	P_UNKNOWN_FIELD(208), //
	P_UNSUPPORTED_PARAMETER(209), //

	// Field
	A_INVALID(300), //
	A_OUT_OF_RANGE(301), //

	// Reader
	R_DEVICE_ERROR(401);

	private int value;

	private LLRPStatusCode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static LLRPStatusCode get(int value) {
		for (LLRPStatusCode code : LLRPStatusCode.values()) {
			if (code.value == value) {
				return code;
			}
		}
		return null;
	}
}