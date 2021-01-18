package havis.llrpservice.data.message.parameter;

public final class ParameterTypes {

	public enum ParameterType {

		// TV parameters (Type Value):
		ANTENNA_ID((short) 1), //
		FIRST_SEEN_TIMESTAMP_UTC((short) 2), //
		FIRST_SEEN_TIMESTAMP_UPTIME((short) 3), //
		LAST_SEEN_TIMESTAMP_UTC((short) 4), //
		LAST_SEEN_TIMESTAMP_UPTIME((short) 5), //
		PEAK_RSSI((short) 6), //
		CHANNEL_INDEX((short) 7), //
		TAG_SEEN_COUNT((short) 8), //
		RO_SPEC_ID((short) 9), //
		INVENTORY_PARAMETER_SPEC_ID((short) 10), //
		C1G2_CRC((short) 11), //
		C1G2_PC((short) 12), //
		EPC_96((short) 13), // //
		SPEC_INDEX((short) 14), //
		CLIENT_REQUEST_OP_SPEC_RESULT((short) 15), //
		ACCESS_SPEC_ID((short) 16), //
		OP_SPEC_ID((short) 17), //
		C1G2_SINGULATION_DETAILS((short) 18), //
		C1G2_XPCW1((short) 19), //
		C1G2_XPCW2((short) 20), //

		// TLV parameters (Type Length Value):
		UTC_TIMESTAMP((short) 128), //
		UPTIME((short) 129), //
		GENERAL_DEVICE_CAPABILITIES((short) 137), //
		RECEIVE_SENSITIVITY_TABLE_ENTRY((short) 139), //
		PER_ANTENNA_AIR_PROTOCOL((short) 140), //
		GPIO_CAPABILITIES((short) 141), //
		LLRP_CAPABILITIES((short) 142), //
		REGULATORY_CAPABILITIES((short) 143), //
		UHF_BAND_CAPABILITIES((short) 144), //
		TRANSMIT_POWER_LEVEL_TABLE_ENTRY((short) 145), //
		FREQUENCY_INFORMATION((short) 146), //
		FREQUENCY_HOP_TABLE((short) 147), //
		FIXED_FREQUENCY_TABLE((short) 148), //
		PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE((short) 149), //
		RO_SPEC((short) 177), //
		RO_BOUNDARY_SPEC((short) 178), //
		RO_SPEC_START_TRIGGER((short) 179), //
		PERIODIC_TRIGGER_VALUE((short) 180), //
		GPI_TRIGGER_VALUE((short) 181), //
		RO_SPEC_STOP_TRIGGER((short) 182), //
		AI_SPEC((short) 183), //
		AI_SPEC_STOP_TRIGGER((short) 184), //
		TAG_OBSERVATION_TRIGGER((short) 185), //
		INVENTORY_PARAMETER_SPEC((short) 186), //
		RF_SURVEY_SPEC((short) 187), //
		RF_SURVEY_SPEC_STOP_TRIGGER((short) 188), //
		ACCESS_SPEC((short) 207), //
		ACCESS_SPEC_STOP_TRIGGER((short) 208), //
		ACCESS_COMMAND((short) 209), //
		CLIENT_REQUEST_OP_SPEC((short) 210), //
		CLIENT_REQUEST_RESPONSE((short) 211), //
		LLRP_CONFIGURATION_STATE_VALUE((short) 217), //
		IDENTIFICATION((short) 218), //
		GPO_WRITE_DATA((short) 219), //
		KEEPALIVE_SPEC((short) 220), //
		ANTENNA_PROPERTIES((short) 221), //
		ANTENNA_CONFIGURATION((short) 222), //
		RF_RECEIVER((short) 223), //
		RF_TRANSMITTER((short) 224), //
		GPI_PORT_CURRENT_STATE((short) 225), //
		EVENTS_AND_REPORTS((short) 226), //
		RO_REPORT_SPEC((short) 237), //
		TAG_REPORT_CONTENT_SELECTOR((short) 238), //
		ACCESS_REPORT_SPEC((short) 239), //
		TAG_REPORT_DATA((short) 240), //
		EPC_DATA((short) 241), //
		RF_SURVEY_REPORT_DATA((short) 242), //
		FREQUENCY_RSSI_LEVEL_ENTRY((short) 243), //
		READER_EVENT_NOTIFICATION_SPEC((short) 244), //
		EVENT_NOTIFICATION_STATE((short) 245), //
		READER_EVENT_NOTIFICATION_DATA((short) 246), //
		HOPPING_EVENT((short) 247), //
		GPI_EVENT((short) 248), //
		RO_SPEC_EVENT((short) 249), //
		REPORT_BUFFER_LEVEL_WARNING_EVENT((short) 250), //
		REPORT_BUFFER_OVERFLOW_ERROR_EVENT((short) 251), //
		READER_EXCEPTION_EVENT((short) 252), //
		RF_SURVEY_EVENT((short) 253), //
		AI_SPEC_EVENT((short) 254), //
		ANTENNA_EVENT((short) 255), //
		CONNECTION_ATTEMPT_EVENT((short) 256), //
		CONNECTION_CLOSE_EVENT((short) 257), //
		LLRP_STATUS((short) 287), //
		FIELD_ERROR((short) 288), //
		PARAMETER_ERROR((short) 289), //
		C1G2_LLRP_CAPABILITIES((short) 327), //
		UHF_C1G2_RF_MODE_TABLE((short) 328), //
		UHF_C1G2_RF_MODE_TABLE_ENTRY((short) 329), //
		C1G2_INVENTORY_COMMAND((short) 330), //
		C1G2_FILTER((short) 331), //
		C1G2_TAG_INVENTORY_MASK((short) 332), //
		C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION((short) 333), //
		C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION((short) 334), //
		C1G2_RF_CONTROL((short) 335), //
		C1G2_SINGULATION_CONTROL((short) 336), //
		C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION((short) 337), //
		C1G2_TAG_SPEC((short) 338), //
		C1G2_TARGET_TAG((short) 339), //
		C1G2_READ((short) 341), //
		C1G2_WRITE((short) 342), //
		C1G2_KILL((short) 343), //
		C1G2_LOCK((short) 344), //
		C1G2_LOCK_PAYLOAD((short) 345), //
		C1G2_BLOCK_ERASE((short) 346), //
		C1G2_BLOCK_WRITE((short) 347), //
		C1G2_EPC_MEMORY_SELECTOR((short) 348), //
		C1G2_READ_OP_SPEC_RESULT((short) 349), //
		C1G2_WRITE_OP_SPEC_RESULT((short) 350), //
		C1G2_KILL_OP_SPEC_RESULT((short) 351), //
		C1G2_LOCK_OP_SPEC_RESULT((short) 352), //
		C1G2_BLOCK_ERASE_OP_SPEC_RESULT((short) 353), //
		C1G2_BLOCK_WRITE_OP_SPEC_RESULT((short) 354), //
		LOOP_SPEC((short) 355), //
		SPEC_LOOP_EVENT((short) 356), //
		C1G2_RECOMMISSION((short) 357), //
		C1G2_BLOCK_PERMALOCK((short) 358), //
		C1G2_GET_BLOCK_PERMALOCK_STATUS((short) 359), //
		C1G2_RECOMMISSION_OP_SPEC_RESULT((short) 360), //
		C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT((short) 361), //
		C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT((short) 362), //
		MAXIMUM_RECEIVE_SENSITIVITY((short) 363), //
		RF_SURVEY_FREQUENCY_CAPABILITIES((short) 365), //
		CUSTOM((short) 1023);

		private final short value;

		private ParameterType(short value) {
			this.value = value;
		}

		public final short getValue() {
			return value;
		}
	}

	public static final ParameterType get(short value) {
		for (ParameterType type : ParameterType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}