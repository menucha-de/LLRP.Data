package havis.llrpservice.data.message.serializer;

import havis.llrpservice.data.DataTypeConverter;
import havis.llrpservice.data.message.AddAccessSpec;
import havis.llrpservice.data.message.AddAccessSpecResponse;
import havis.llrpservice.data.message.AddROSpec;
import havis.llrpservice.data.message.AddROSpecResponse;
import havis.llrpservice.data.message.ClientRequestOP;
import havis.llrpservice.data.message.ClientRequestOPResponse;
import havis.llrpservice.data.message.CloseConnection;
import havis.llrpservice.data.message.CloseConnectionResponse;
import havis.llrpservice.data.message.CustomMessage;
import havis.llrpservice.data.message.DeleteAccessSpec;
import havis.llrpservice.data.message.DeleteAccessSpecResponse;
import havis.llrpservice.data.message.DeleteROSpec;
import havis.llrpservice.data.message.DeleteROSpecResponse;
import havis.llrpservice.data.message.DisableAccessSpec;
import havis.llrpservice.data.message.DisableAccessSpecResponse;
import havis.llrpservice.data.message.DisableROSpec;
import havis.llrpservice.data.message.DisableROSpecResponse;
import havis.llrpservice.data.message.EnableAccessSpec;
import havis.llrpservice.data.message.EnableAccessSpecResponse;
import havis.llrpservice.data.message.EnableEventsAndReports;
import havis.llrpservice.data.message.EnableROSpec;
import havis.llrpservice.data.message.EnableROSpecResponse;
import havis.llrpservice.data.message.ErrorMessage;
import havis.llrpservice.data.message.GetAccessSpecs;
import havis.llrpservice.data.message.GetAccessSpecsResponse;
import havis.llrpservice.data.message.GetROSpecs;
import havis.llrpservice.data.message.GetROSpecsResponse;
import havis.llrpservice.data.message.GetReaderCapabilities;
import havis.llrpservice.data.message.GetReaderCapabilitiesRequestedData;
import havis.llrpservice.data.message.GetReaderCapabilitiesResponse;
import havis.llrpservice.data.message.GetReaderConfig;
import havis.llrpservice.data.message.GetReaderConfigRequestedData;
import havis.llrpservice.data.message.GetReaderConfigResponse;
import havis.llrpservice.data.message.GetReport;
import havis.llrpservice.data.message.GetSupportedVersion;
import havis.llrpservice.data.message.GetSupportedVersionResponse;
import havis.llrpservice.data.message.Keepalive;
import havis.llrpservice.data.message.KeepaliveAck;
import havis.llrpservice.data.message.Message;
import havis.llrpservice.data.message.MessageHeader;
import havis.llrpservice.data.message.MessageTypes;
import havis.llrpservice.data.message.ProtocolVersion;
import havis.llrpservice.data.message.ROAccessReport;
import havis.llrpservice.data.message.ReaderEventNotification;
import havis.llrpservice.data.message.SetProtocolVersion;
import havis.llrpservice.data.message.SetProtocolVersionResponse;
import havis.llrpservice.data.message.SetReaderConfig;
import havis.llrpservice.data.message.SetReaderConfigResponse;
import havis.llrpservice.data.message.StartROSpec;
import havis.llrpservice.data.message.StartROSpecResponse;
import havis.llrpservice.data.message.StopROSpec;
import havis.llrpservice.data.message.StopROSpecResponse;
import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.AccessReportSpec;
import havis.llrpservice.data.message.parameter.AccessSpec;
import havis.llrpservice.data.message.parameter.AntennaConfiguration;
import havis.llrpservice.data.message.parameter.AntennaProperties;
import havis.llrpservice.data.message.parameter.C1G2LLRPCapabilities;
import havis.llrpservice.data.message.parameter.ClientRequestResponse;
import havis.llrpservice.data.message.parameter.Custom;
import havis.llrpservice.data.message.parameter.EventsAndReports;
import havis.llrpservice.data.message.parameter.GPIPortCurrentState;
import havis.llrpservice.data.message.parameter.GPOWriteData;
import havis.llrpservice.data.message.parameter.GeneralDeviceCapabilities;
import havis.llrpservice.data.message.parameter.Identification;
import havis.llrpservice.data.message.parameter.KeepaliveSpec;
import havis.llrpservice.data.message.parameter.LLRPCapabilities;
import havis.llrpservice.data.message.parameter.LLRPConfigurationStateValue;
import havis.llrpservice.data.message.parameter.LLRPStatus;
import havis.llrpservice.data.message.parameter.ParameterHeader;
import havis.llrpservice.data.message.parameter.RFSurveyReportData;
import havis.llrpservice.data.message.parameter.ROReportSpec;
import havis.llrpservice.data.message.parameter.ROSpec;
import havis.llrpservice.data.message.parameter.ReaderEventNotificationData;
import havis.llrpservice.data.message.parameter.ReaderEventNotificationSpec;
import havis.llrpservice.data.message.parameter.RegulatoryCapabilities;
import havis.llrpservice.data.message.parameter.TLVParameterHeader;
import havis.llrpservice.data.message.parameter.TagReportData;
import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;
import havis.llrpservice.data.message.parameter.serializer.InvalidParameterTypeException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * On deserializing empty lists will be generated for mandatory and optional lists. 
 * On serializing the mandatory lists must not be null, but the optional lists can be null.
 */
public class ByteBufferSerializer {
	public static final byte MESSAGE_HEADER_LENGTH = 10;

	private havis.llrpservice.data.message.parameter.serializer.ByteBufferSerializer parameterSerializer = new havis.llrpservice.data.message.parameter.serializer.ByteBufferSerializer();

	public MessageHeader deserializeMessageHeader(ByteBuffer data)
			throws InvalidProtocolVersionException, InvalidMessageTypeException {
		// reserved 3 bits
		short s = data.getShort();
		byte reserved = (byte) ((s & 0xFFFF) >> 13);
		// version 3 bits
		byte protocolVersion = (byte) ((s >> 10) & 0x07);
		ProtocolVersion version = ProtocolVersion.get(protocolVersion);
		if (version == null) {
			throw new InvalidProtocolVersionException(
					"Invalid protocol version " + protocolVersion);
		}
		// message type 10 bits
		short messageType = (short) (s & 0x3FF);
		MessageType type = MessageTypes.get(messageType);
		if (type == null) {
			throw new InvalidMessageTypeException("Invalid message type "
					+ messageType);
		}
		// length 4 bytes
		long length = DataTypeConverter.uint(data.getInt());
		// id 4 bytes
		long id = DataTypeConverter.uint(data.getInt());
		MessageHeader message = new MessageHeader(reserved, version, id);
		message.setMessageType(type);
		message.setMessageLength(length);
		return message;
	}

	public void serialize(MessageHeader header, ByteBuffer data) {
		// reserved 3 bits
		short s = (short) (header.getReserved() << 13);
		// version 3 bits
		s |= header.getVersion().getValue() << 10;
		// message type 10 bits
		s |= header.getMessageType().getValue();
		data.putShort(s);
		// length 4 bytes
		data.putInt((int) header.getMessageLength());
		// id 4 bytes
		data.putInt((int) header.getId());
	}

	public Message deserializeMessage(MessageHeader header, ByteBuffer data)
			throws InvalidMessageTypeException, InvalidParameterTypeException {
		MessageType messageType = header.getMessageType();
		switch (messageType) {
		case READER_EVENT_NOTIFICATION:
			return deserializeReaderEventNotification(header, data);
		case GET_SUPPORTED_VERSION:
			return deserializeGetSupportedVersion(header, data);
		case GET_SUPPORTED_VERSION_RESPONSE:
			return deserializeGetSupportedVersionResponse(header, data);
		case SET_PROTOCOL_VERSION:
			return deserializeSetProtocolVersion(header, data);
		case SET_PROTOCOL_VERSION_RESPONSE:
			return deserializeSetProtocolVersionResponse(header, data);
		case GET_READER_CAPABILITIES:
			return deserializeGetReaderCapabilities(header, data);
		case GET_READER_CAPABILITIES_RESPONSE:
			return deserializeGetReaderCapabilitiesResponse(header, data);
		case GET_READER_CONFIG:
			return deserializeGetReaderConfig(header, data);
		case GET_READER_CONFIG_RESPONSE:
			return deserializeGetReaderConfigResponse(header, data);
		case SET_READER_CONFIG:
			return deserializeSetReaderConfig(header, data);
		case SET_READER_CONFIG_RESPONSE:
			return deserializeSetReaderConfigResponse(header, data);
		case ADD_ROSPEC:
			return deserializeAddROSpec(header, data);
		case ADD_ROSPEC_RESPONSE:
			return deserializeAddROSpecResponse(header, data);
		case DELETE_ROSPEC:
			return deserializeDeleteROSpec(header, data);
		case DELETE_ROSPEC_RESPONSE:
			return deserializeDeleteROSpecResponse(header, data);
		case START_ROSPEC:
			return deserializeStartROSpec(header, data);
		case START_ROSPEC_RESPONSE:
			return deserializeStartROSpecResponse(header, data);
		case STOP_ROSPEC:
			return deserializeStopROSpec(header, data);
		case STOP_ROSPEC_RESPONSE:
			return deserializeStopROSpecResponse(header, data);
		case ENABLE_ROSPEC:
			return deserializeEnableROSpec(header, data);
		case ENABLE_ROSPEC_RESPONSE:
			return deserializeEnableROSpecResponse(header, data);
		case DISABLE_ROSPEC:
			return deserializeDisableROSpec(header, data);
		case DISABLE_ROSPEC_RESPONSE:
			return deserializeDisableROSpecResponse(header, data);
		case GET_ROSPECS:
			return deserializeGetROSpecs(header, data);
		case GET_ROSPECS_RESPONSE:
			return deserializeGetROSpecsResponse(header, data);
		case ADD_ACCESSSPEC:
			return deserializeAddAccessSpec(header, data);
		case ADD_ACCESSSPEC_RESPONSE:
			return deserializeAddAccessSpecResponse(header, data);
		case DELETE_ACCESSSPEC:
			return deserializeDeleteAccessSpec(header, data);
		case DELETE_ACCESSSPEC_RESPONSE:
			return deserializeDeleteAccessSpecResponse(header, data);
		case ENABLE_ACCESSSPEC:
			return deserializeEnableAccessSpec(header, data);
		case ENABLE_ACCESSSPEC_RESPONSE:
			return deserializeEnableAccessSpecResponse(header, data);
		case DISABLE_ACCESSSPEC:
			return deserializeDisableAccessSpec(header, data);
		case DISABLE_ACCESSSPEC_RESPONSE:
			return deserializeDisableAccessSpecResponse(header, data);
		case GET_ACCESSSPECS:
			return deserializeGetAccessSpecs(header, data);
		case GET_ACCESSSPECS_RESPONSE:
			return deserializeGetAccessSpecsResponse(header, data);
		case CLIENT_REQUEST_OP:
			return deserializeClientRequestOP(header, data);
		case CLIENT_REQUEST_OP_RESPONSE:
			return deserializeClientRequestOPResponse(header, data);
		case GET_REPORT:
			return deserializeGetReport(header, data);
		case KEEPALIVE:
			return deserializeKeepalive(header, data);
		case KEEPALIVE_ACK:
			return deserializeKeepaliveAck(header, data);
		case ENABLE_EVENTS_AND_REPORTS:
			return deserializeEnableEventsAndReports(header, data);
		case ERROR_MESSAGE:
			return deserializeErrorMessage(header, data);
		case RO_ACCESS_REPORT:
			return deserializeROAccessReport(header, data);
		case CLOSE_CONNECTION:
			return deserializeCloseConnection(header, data);
		case CLOSE_CONNECTION_RESPONSE:
			return deserializeCloseConnectionResponse(header, data);
		case CUSTOM_MESSAGE:
			return deserializeCustomMessage(header, data);

		default:
			throw new InvalidMessageTypeException("Invalid message type "
					+ messageType);
		}
	}

	public void serialize(Message message, ByteBuffer data)
			throws InvalidMessageTypeException, InvalidParameterTypeException {
		MessageType messageType = message.getMessageHeader().getMessageType();
		switch (messageType) {
		case READER_EVENT_NOTIFICATION:
			serialize((ReaderEventNotification) message, data);
			break;
		case GET_SUPPORTED_VERSION:
			serialize((GetSupportedVersion) message, data);
			break;
		case GET_SUPPORTED_VERSION_RESPONSE:
			serialize((GetSupportedVersionResponse) message, data);
			break;
		case SET_PROTOCOL_VERSION:
			serialize((SetProtocolVersion) message, data);
			break;
		case SET_PROTOCOL_VERSION_RESPONSE:
			serialize((SetProtocolVersionResponse) message, data);
			break;
		case GET_READER_CAPABILITIES:
			serialize((GetReaderCapabilities) message, data);
			break;
		case GET_READER_CAPABILITIES_RESPONSE:
			serialize((GetReaderCapabilitiesResponse) message, data);
			break;
		case GET_READER_CONFIG:
			serialize((GetReaderConfig) message, data);
			break;
		case GET_READER_CONFIG_RESPONSE:
			serialize((GetReaderConfigResponse) message, data);
			break;
		case SET_READER_CONFIG:
			serialize((SetReaderConfig) message, data);
			break;
		case SET_READER_CONFIG_RESPONSE:
			serialize((SetReaderConfigResponse) message, data);
			break;
		case ADD_ROSPEC:
			serialize((AddROSpec) message, data);
			break;
		case ADD_ROSPEC_RESPONSE:
			serialize((AddROSpecResponse) message, data);
			break;
		case DELETE_ROSPEC:
			serialize((DeleteROSpec) message, data);
			break;
		case DELETE_ROSPEC_RESPONSE:
			serialize((DeleteROSpecResponse) message, data);
			break;
		case START_ROSPEC:
			serialize((StartROSpec) message, data);
			break;
		case START_ROSPEC_RESPONSE:
			serialize((StartROSpecResponse) message, data);
			break;
		case STOP_ROSPEC:
			serialize((StopROSpec) message, data);
			break;
		case STOP_ROSPEC_RESPONSE:
			serialize((StopROSpecResponse) message, data);
			break;
		case ENABLE_ROSPEC:
			serialize((EnableROSpec) message, data);
			break;
		case ENABLE_ROSPEC_RESPONSE:
			serialize((EnableROSpecResponse) message, data);
			break;
		case DISABLE_ROSPEC:
			serialize((DisableROSpec) message, data);
			break;
		case DISABLE_ROSPEC_RESPONSE:
			serialize((DisableROSpecResponse) message, data);
			break;
		case GET_ROSPECS:
			serialize((GetROSpecs) message, data);
			break;
		case GET_ROSPECS_RESPONSE:
			serialize((GetROSpecsResponse) message, data);
			break;
		case ADD_ACCESSSPEC:
			serialize((AddAccessSpec) message, data);
			break;
		case ADD_ACCESSSPEC_RESPONSE:
			serialize((AddAccessSpecResponse) message, data);
			break;
		case DELETE_ACCESSSPEC:
			serialize((DeleteAccessSpec) message, data);
			break;
		case DELETE_ACCESSSPEC_RESPONSE:
			serialize((DeleteAccessSpecResponse) message, data);
			break;
		case ENABLE_ACCESSSPEC:
			serialize((EnableAccessSpec) message, data);
			break;
		case ENABLE_ACCESSSPEC_RESPONSE:
			serialize((EnableAccessSpecResponse) message, data);
			break;
		case DISABLE_ACCESSSPEC:
			serialize((DisableAccessSpec) message, data);
			break;
		case DISABLE_ACCESSSPEC_RESPONSE:
			serialize((DisableAccessSpecResponse) message, data);
			break;
		case GET_ACCESSSPECS:
			serialize((GetAccessSpecs) message, data);
			break;
		case GET_ACCESSSPECS_RESPONSE:
			serialize((GetAccessSpecsResponse) message, data);
			break;
		case CLIENT_REQUEST_OP:
			serialize((ClientRequestOP) message, data);
			break;
		case CLIENT_REQUEST_OP_RESPONSE:
			serialize((ClientRequestOPResponse) message, data);
			break;
		case GET_REPORT:
			serialize((GetReport) message, data);
			break;
		case KEEPALIVE:
			serialize((Keepalive) message, data);
			break;
		case KEEPALIVE_ACK:
			serialize((KeepaliveAck) message, data);
			break;
		case ENABLE_EVENTS_AND_REPORTS:
			serialize((EnableEventsAndReports) message, data);
			break;
		case ERROR_MESSAGE:
			serialize((ErrorMessage) message, data);
			break;
		case RO_ACCESS_REPORT:
			serialize((ROAccessReport) message, data);
			break;
		case CLOSE_CONNECTION:
			serialize((CloseConnection) message, data);
			break;
		case CLOSE_CONNECTION_RESPONSE:
			serialize((CloseConnectionResponse) message, data);
			break;
		case CUSTOM_MESSAGE:
			serialize((CustomMessage) message, data);
			break;
		default:
			throw new InvalidMessageTypeException("Invalid message type "
					+ messageType);
		}
	}

	public long getLength(Message message) throws InvalidMessageTypeException,
			InvalidParameterTypeException {
		MessageType messageType = message.getMessageHeader().getMessageType();
		switch (messageType) {
		case READER_EVENT_NOTIFICATION:
			return getLength((ReaderEventNotification) message);
		case GET_SUPPORTED_VERSION:
			return getLength((GetSupportedVersion) message);
		case GET_SUPPORTED_VERSION_RESPONSE:
			return getLength((GetSupportedVersionResponse) message);
		case SET_PROTOCOL_VERSION:
			return getLength((SetProtocolVersion) message);
		case SET_PROTOCOL_VERSION_RESPONSE:
			return getLength((SetProtocolVersionResponse) message);
		case GET_READER_CAPABILITIES:
			return getLength((GetReaderCapabilities) message);
		case GET_READER_CAPABILITIES_RESPONSE:
			return getLength((GetReaderCapabilitiesResponse) message);
		case GET_READER_CONFIG:
			return getLength((GetReaderConfig) message);
		case GET_READER_CONFIG_RESPONSE:
			return getLength((GetReaderConfigResponse) message);
		case SET_READER_CONFIG:
			return getLength((SetReaderConfig) message);
		case SET_READER_CONFIG_RESPONSE:
			return getLength((SetReaderConfigResponse) message);
		case ADD_ROSPEC:
			return getLength((AddROSpec) message);
		case ADD_ROSPEC_RESPONSE:
			return getLength((AddROSpecResponse) message);
		case DELETE_ROSPEC:
			return getLength((DeleteROSpec) message);
		case DELETE_ROSPEC_RESPONSE:
			return getLength((DeleteROSpecResponse) message);
		case START_ROSPEC:
			return getLength((StartROSpec) message);
		case START_ROSPEC_RESPONSE:
			return getLength((StartROSpecResponse) message);
		case STOP_ROSPEC:
			return getLength((StopROSpec) message);
		case STOP_ROSPEC_RESPONSE:
			return getLength((StopROSpecResponse) message);
		case ENABLE_ROSPEC:
			return getLength((EnableROSpec) message);
		case ENABLE_ROSPEC_RESPONSE:
			return getLength((EnableROSpecResponse) message);
		case DISABLE_ROSPEC:
			return getLength((DisableROSpec) message);
		case DISABLE_ROSPEC_RESPONSE:
			return getLength((DisableROSpecResponse) message);
		case GET_ROSPECS:
			return getLength((GetROSpecs) message);
		case GET_ROSPECS_RESPONSE:
			return getLength((GetROSpecsResponse) message);
		case ADD_ACCESSSPEC:
			return getLength((AddAccessSpec) message);
		case ADD_ACCESSSPEC_RESPONSE:
			return getLength((AddAccessSpecResponse) message);
		case DELETE_ACCESSSPEC:
			return getLength((DeleteAccessSpec) message);
		case DELETE_ACCESSSPEC_RESPONSE:
			return getLength((DeleteAccessSpecResponse) message);
		case ENABLE_ACCESSSPEC:
			return getLength((EnableAccessSpec) message);
		case ENABLE_ACCESSSPEC_RESPONSE:
			return getLength((EnableAccessSpecResponse) message);
		case DISABLE_ACCESSSPEC:
			return getLength((DisableAccessSpec) message);
		case DISABLE_ACCESSSPEC_RESPONSE:
			return getLength((DisableAccessSpecResponse) message);
		case GET_ACCESSSPECS:
			return getLength((GetAccessSpecs) message);
		case GET_ACCESSSPECS_RESPONSE:
			return getLength((GetAccessSpecsResponse) message);
		case CLIENT_REQUEST_OP:
			return getLength((ClientRequestOP) message);
		case CLIENT_REQUEST_OP_RESPONSE:
			return getLength((ClientRequestOPResponse) message);
		case GET_REPORT:
			return getLength((GetReport) message);
		case KEEPALIVE:
			return getLength((Keepalive) message);
		case KEEPALIVE_ACK:
			return getLength((KeepaliveAck) message);
		case ENABLE_EVENTS_AND_REPORTS:
			return getLength((EnableEventsAndReports) message);
		case ERROR_MESSAGE:
			return getLength((ErrorMessage) message);
		case RO_ACCESS_REPORT:
			return getLength((ROAccessReport) message);
		case CLOSE_CONNECTION:
			return getLength((CloseConnection) message);
		case CLOSE_CONNECTION_RESPONSE:
			return getLength((CloseConnectionResponse) message);
		case CUSTOM_MESSAGE:
			return getLength((CustomMessage) message);
		default:
			throw new InvalidMessageTypeException("Invalid message type "
					+ messageType);
		}
	}

	/**
	 * Section 17.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GetSupportedVersion deserializeGetSupportedVersion(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		return new GetSupportedVersion(header);
	}

	public void serialize(GetSupportedVersion ren, ByteBuffer data) {
		// set message length
		MessageHeader header = ren.getMessageHeader();
		header.setMessageLength(getLength(ren));
		// serialize message
		serialize(header, data);

	}

	public long getLength(GetSupportedVersion ren) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GetSupportedVersionResponse deserializeGetSupportedVersionResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message

		ProtocolVersion currentVersion = ProtocolVersion.get(data.get());
		ProtocolVersion supportedVersion = ProtocolVersion.get(data.get());

		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);

		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new GetSupportedVersionResponse(header, currentVersion,
				supportedVersion, status);
	}

	public void serialize(GetSupportedVersionResponse ren, ByteBuffer data) {
		// set message length
		MessageHeader header = ren.getMessageHeader();
		header.setMessageLength(getLength(ren));
		// serialize message
		serialize(header, data);

		data.put(ren.getCurrentVersion().getValue());

		data.put(ren.getSupportedVersion().getValue());

		parameterSerializer.serialize(ren.getStatus(), data);
	}

	public long getLength(GetSupportedVersionResponse ren) {
		return MESSAGE_HEADER_LENGTH + 2
				+ parameterSerializer.getLength(ren.getStatus());
	}

	/**
	 * Section 17.1.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public SetProtocolVersion deserializeSetProtocolVersion(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ProtocolVersion version = ProtocolVersion.get(data.get());
		return new SetProtocolVersion(header, version);
	}

	public void serialize(SetProtocolVersion ren, ByteBuffer data) {
		// set message length
		MessageHeader header = ren.getMessageHeader();
		header.setMessageLength(getLength(ren));
		// serialize message
		serialize(header, data);
		data.put(ren.getProtocolVersion().getValue());
	}

	public long getLength(SetProtocolVersion ren) {
		return MESSAGE_HEADER_LENGTH + 1;
	}

	/**
	 * Section 17.1.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public SetProtocolVersionResponse deserializeSetProtocolVersionResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new SetProtocolVersionResponse(header, status);
	}

	public void serialize(SetProtocolVersionResponse ren, ByteBuffer data) {
		// set message length
		MessageHeader header = ren.getMessageHeader();
		header.setMessageLength(getLength(ren));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(ren.getStatus(), data);
	}

	public long getLength(SetProtocolVersionResponse ren) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(ren.getStatus());
	}

	/**
	 * Section 17.1.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GetReaderCapabilities deserializeGetReaderCapabilities(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		int bufferStartPosition = data.position();
		GetReaderCapabilitiesRequestedData requestedData = GetReaderCapabilitiesRequestedData
				.get(data.get());
		GetReaderCapabilities grc = null;
		List<Custom> customExtensionPoint = new ArrayList<>();
		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case CUSTOM:
				Custom cus = parameterSerializer.deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				customExtensionPoint.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType()
								+ " (expected " + ParameterType.CUSTOM + ")");
			}
		}
		grc = new GetReaderCapabilities(header, requestedData);
		grc.setCustomExtensionPoint(customExtensionPoint);
		return grc;
	}

	public void serialize(GetReaderCapabilities grc, ByteBuffer data) {
		// set message length
		MessageHeader header = grc.getMessageHeader();
		header.setMessageLength(getLength(grc));
		// serialize message
		serialize(header, data);
		data.put((byte) grc.getRequestedData().getValue());
		if (grc.getCustomExtensionPoint() != null
				&& !grc.getCustomExtensionPoint().isEmpty()) {
			for (Custom cus : grc.getCustomExtensionPoint()) {
				parameterSerializer.serialize(cus, data);
			}
		}
	}

	public long getLength(GetReaderCapabilities grc) {
		int length = MESSAGE_HEADER_LENGTH + 1;
		if (grc.getCustomExtensionPoint() != null
				&& !grc.getCustomExtensionPoint().isEmpty()) {
			for (Custom cus : grc.getCustomExtensionPoint()) {
				length += parameterSerializer.getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GetReaderCapabilitiesResponse deserializeGetReaderCapabilitiesResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		GetReaderCapabilitiesResponse grcr = null;
		int bufferStartPosition = data.position();
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}

		grcr=new GetReaderCapabilitiesResponse(header, status);
		
		GeneralDeviceCapabilities genDevCap = null;
		LLRPCapabilities llrpCap = null;
		RegulatoryCapabilities regCap = null;
		C1G2LLRPCapabilities c1g2LLRPCap = null;
		List<Custom> customExtensionPoint = new ArrayList<>();

		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case GENERAL_DEVICE_CAPABILITIES:
				genDevCap = parameterSerializer
						.deserializeGeneralDeviceCapabilities(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case LLRP_CAPABILITIES:
				llrpCap = parameterSerializer.deserializeLLRPCapabilities(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case REGULATORY_CAPABILITIES:
				regCap = parameterSerializer.deserializeRegulatoryCapabilities(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case C1G2_LLRP_CAPABILITIES:
				c1g2LLRPCap = parameterSerializer
						.deserializeC1G2LLRPCapabilities(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case CUSTOM:
				Custom cus = parameterSerializer.deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				customExtensionPoint.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		if (genDevCap != null) {
			grcr = new GetReaderCapabilitiesResponse(header, status, genDevCap);
			grcr.setLlrpCap(llrpCap);
			grcr.setC1g2llrpCap(c1g2LLRPCap);
			grcr.setRegulatoryCap(regCap);
		} else if (llrpCap != null) {
			grcr = new GetReaderCapabilitiesResponse(header, status, llrpCap);
			grcr.setGeneralDeviceCap(genDevCap);
			grcr.setC1g2llrpCap(c1g2LLRPCap);
			grcr.setRegulatoryCap(regCap);
		} else if (c1g2LLRPCap != null) {
			grcr = new GetReaderCapabilitiesResponse(header, status,
					c1g2LLRPCap);
			grcr.setLlrpCap(llrpCap);
			grcr.setGeneralDeviceCap(genDevCap);
			grcr.setRegulatoryCap(regCap);
		} else if (regCap != null) {
			grcr = new GetReaderCapabilitiesResponse(header, status, regCap);
			grcr.setLlrpCap(llrpCap);
			grcr.setC1g2llrpCap(c1g2LLRPCap);
			grcr.setGeneralDeviceCap(genDevCap);
		}

		if (!customExtensionPoint.isEmpty()) {
			grcr.setCustomExtensionPoint(customExtensionPoint);
		}
		return grcr;
	}

	public void serialize(GetReaderCapabilitiesResponse grcr, ByteBuffer data) {
		// set message length
		MessageHeader header = grcr.getMessageHeader();
		header.setMessageLength(getLength(grcr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(grcr.getStatus(), data);
		if (grcr.getGeneralDeviceCap() != null) {
			parameterSerializer.serialize(grcr.getGeneralDeviceCap(), data);
		}
		if (grcr.getLlrpCap() != null) {
			parameterSerializer.serialize(grcr.getLlrpCap(), data);
		}
		if (grcr.getRegulatoryCap() != null) {
			parameterSerializer.serialize(grcr.getRegulatoryCap(), data);
		}
		if (grcr.getC1g2llrpCap() != null) {
			parameterSerializer.serialize(grcr.getC1g2llrpCap(), data);
		}
		if (grcr.getCustomExtensionPoint() != null
				&& !grcr.getCustomExtensionPoint().isEmpty()) {
			for (Custom cus : grcr.getCustomExtensionPoint()) {
				parameterSerializer.serialize(cus, data);
			}
		}
	}

	public long getLength(GetReaderCapabilitiesResponse grcr) {
		int length = MESSAGE_HEADER_LENGTH;
		length += parameterSerializer.getLength(grcr.getStatus());
		if (grcr.getGeneralDeviceCap() != null) {
			length += parameterSerializer.getLength(grcr.getGeneralDeviceCap());
		}
		if (grcr.getLlrpCap() != null) {
			length += parameterSerializer.getLength(grcr.getLlrpCap());
		}
		if (grcr.getRegulatoryCap() != null) {
			length += parameterSerializer.getLength(grcr.getRegulatoryCap());
		}
		if (grcr.getC1g2llrpCap() != null) {
			length += parameterSerializer.getLength(grcr.getC1g2llrpCap());
		}
		if (grcr.getCustomExtensionPoint() != null
				&& !grcr.getCustomExtensionPoint().isEmpty()) {
			for (Custom cus : grcr.getCustomExtensionPoint()) {
				length += parameterSerializer.getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.7
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public AddROSpec deserializeAddROSpec(MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		ROSpec roSpec = null;
		switch (parameterHeader.getParameterType()) {
		case RO_SPEC:
			roSpec = parameterSerializer.deserializeROSpec(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.RO_SPEC + ")");
		}
		return new AddROSpec(header, roSpec);
	}

	public void serialize(AddROSpec aros, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = aros.getMessageHeader();
		header.setMessageLength(getLength(aros));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(aros.getRoSpec(), data);
	}

	public long getLength(AddROSpec aros) throws InvalidParameterTypeException {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(aros.getRoSpec());
	}

	/**
	 * Section 17.1.8
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AddROSpecResponse deserializeAddROSpecResponse(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new AddROSpecResponse(header, status);
	}

	public void serialize(AddROSpecResponse arosr, ByteBuffer data) {
		// set message length
		MessageHeader header = arosr.getMessageHeader();
		header.setMessageLength(getLength(arosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(arosr.getStatus(), data);
	}

	public long getLength(AddROSpecResponse arosr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(arosr.getStatus());
	}

	/**
	 * Section 17.1.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DeleteROSpec deserializeDeleteROSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long roSpecID = DataTypeConverter.uint(data.getInt());
		return new DeleteROSpec(header, roSpecID);
	}

	public void serialize(DeleteROSpec dros, ByteBuffer data) {
		// set message length
		MessageHeader header = dros.getMessageHeader();
		header.setMessageLength(getLength(dros));
		// serialize message
		serialize(header, data);
		data.putInt((int) dros.getRoSpecID());
	}

	public long getLength(DeleteROSpec dros) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.10
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DeleteROSpecResponse deserializeDeleteROSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new DeleteROSpecResponse(header, status);
	}

	public void serialize(DeleteROSpecResponse drosr, ByteBuffer data) {
		// set message length
		MessageHeader header = drosr.getMessageHeader();
		header.setMessageLength(getLength(drosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(drosr.getStatus(), data);
	}

	public long getLength(DeleteROSpecResponse drosr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(drosr.getStatus());
	}

	/**
	 * Section 17.1.11
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public StartROSpec deserializeStartROSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long roSpecID = DataTypeConverter.uint(data.getInt());
		return new StartROSpec(header, roSpecID);
	}

	public void serialize(StartROSpec sros, ByteBuffer data) {
		// set message length
		MessageHeader header = sros.getMessageHeader();
		header.setMessageLength(getLength(sros));
		// serialize message
		serialize(header, data);
		data.putInt((int) sros.getRoSpecID());
	}

	public long getLength(StartROSpec sros) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.12
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public StartROSpecResponse deserializeStartROSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new StartROSpecResponse(header, status);
	}

	public void serialize(StartROSpecResponse srosr, ByteBuffer data) {
		// set message length
		MessageHeader header = srosr.getMessageHeader();
		header.setMessageLength(getLength(srosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(srosr.getStatus(), data);
	}

	public long getLength(StartROSpecResponse srosr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(srosr.getStatus());
	}

	/**
	 * Section 17.1.13
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public StopROSpec deserializeStopROSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long roSpecID = DataTypeConverter.uint(data.getInt());
		return new StopROSpec(header, roSpecID);
	}

	public void serialize(StopROSpec stros, ByteBuffer data) {
		// set message length
		MessageHeader header = stros.getMessageHeader();
		header.setMessageLength(getLength(stros));
		// serialize message
		serialize(header, data);
		data.putInt((int) stros.getRoSpecID());
	}

	public long getLength(StopROSpec stros) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.14
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public StopROSpecResponse deserializeStopROSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new StopROSpecResponse(header, status);
	}

	public void serialize(StopROSpecResponse strosr, ByteBuffer data) {
		// set message length
		MessageHeader header = strosr.getMessageHeader();
		header.setMessageLength(getLength(strosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(strosr.getStatus(), data);
	}

	public long getLength(StopROSpecResponse strosr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(strosr.getStatus());
	}

	/**
	 * Section 17.1.15
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EnableROSpec deserializeEnableROSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long roSpecID = DataTypeConverter.uint(data.getInt());
		return new EnableROSpec(header, roSpecID);
	}

	public void serialize(EnableROSpec eros, ByteBuffer data) {
		// set message length
		MessageHeader header = eros.getMessageHeader();
		header.setMessageLength(getLength(eros));
		// serialize message
		serialize(header, data);
		data.putInt((int) eros.getRoSpecID());
	}

	public long getLength(EnableROSpec eros) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.16
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EnableROSpecResponse deserializeEnableROSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new EnableROSpecResponse(header, status);
	}

	public void serialize(EnableROSpecResponse erosr, ByteBuffer data) {
		// set message length
		MessageHeader header = erosr.getMessageHeader();
		header.setMessageLength(getLength(erosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(erosr.getStatus(), data);
	}

	public long getLength(EnableROSpecResponse erosr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(erosr.getStatus());
	}

	/**
	 * Section 17.1.17
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DisableROSpec deserializeDisableROSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long roSpecID = DataTypeConverter.uint(data.getInt());
		return new DisableROSpec(header, roSpecID);
	}

	public void serialize(DisableROSpec dros, ByteBuffer data) {
		// set message length
		MessageHeader header = dros.getMessageHeader();
		header.setMessageLength(getLength(dros));
		// serialize message
		serialize(header, data);
		data.putInt((int) dros.getRoSpecID());
	}

	public long getLength(DisableROSpec dros) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.18
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DisableROSpecResponse deserializeDisableROSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new DisableROSpecResponse(header, status);
	}

	public void serialize(DisableROSpecResponse drosr, ByteBuffer data) {
		// set message length
		MessageHeader header = drosr.getMessageHeader();
		header.setMessageLength(getLength(drosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(drosr.getStatus(), data);
	}

	public long getLength(DisableROSpecResponse drosr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(drosr.getStatus());
	}

	/**
	 * Section 17.1.19
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetROSpecs deserializeGetROSpecs(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		return new GetROSpecs(header);
	}

	public void serialize(GetROSpecs gros, ByteBuffer data) {
		// set message length
		MessageHeader header = gros.getMessageHeader();
		header.setMessageLength(getLength(gros));
		// serialize message
		serialize(header, data);
	}

	public long getLength(GetROSpecs gros) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.20
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetROSpecsResponse deserializeGetROSpecsResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		GetROSpecsResponse getROSR = null;
		int bufferStartPosition = data.position();
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		getROSR = new GetROSpecsResponse(header, status);

		List<ROSpec> roSpecList = new ArrayList<>();
		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RO_SPEC:
				ROSpec ros = parameterSerializer.deserializeROSpec(
						(TLVParameterHeader) parameterHeader, data);
				roSpecList.add(ros);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType()
								+ " (expected " + ParameterType.RO_SPEC + ")");
			}
		}
		getROSR.setRoSpecList(roSpecList);

		return getROSR;
	}

	public void serialize(GetROSpecsResponse grosr, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = grosr.getMessageHeader();
		header.setMessageLength(getLength(grosr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(grosr.getStatus(), data);
		if (grosr.getRoSpecList() != null && !grosr.getRoSpecList().isEmpty()) {
			for (ROSpec ros : grosr.getRoSpecList()) {
				parameterSerializer.serialize(ros, data);
			}
		}
	}

	public long getLength(GetROSpecsResponse grosr)
			throws InvalidParameterTypeException {
		int length = MESSAGE_HEADER_LENGTH
				+ +parameterSerializer.getLength(grosr.getStatus());
		;
		if (grosr.getRoSpecList() != null && !grosr.getRoSpecList().isEmpty()) {
			for (ROSpec ros : grosr.getRoSpecList()) {
				length += parameterSerializer.getLength(ros);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.21
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AddAccessSpec deserializeAddAccessSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		AccessSpec ass = null;
		switch (parameterHeader.getParameterType()) {
		case ACCESS_SPEC:
			ass = parameterSerializer.deserializeAccessSpec(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.ACCESS_SPEC + ")");
		}
		return new AddAccessSpec(header, ass);
	}

	public void serialize(AddAccessSpec aas, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = aas.getMessageHeader();
		header.setMessageLength(getLength(aas));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(aas.getAccessSpec(), data);
	}

	public long getLength(AddAccessSpec aas)
			throws InvalidParameterTypeException {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(aas.getAccessSpec());
	}

	/**
	 * Section 17.1.22
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AddAccessSpecResponse deserializeAddAccessSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new AddAccessSpecResponse(header, status);
	}

	public void serialize(AddAccessSpecResponse aasRe, ByteBuffer data) {
		// set message length
		MessageHeader header = aasRe.getMessageHeader();
		header.setMessageLength(getLength(aasRe));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(aasRe.getStatus(), data);
	}

	public long getLength(AddAccessSpecResponse aasRe) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(aasRe.getStatus());
	}

	/**
	 * Section 17.1.23
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DeleteAccessSpec deserializeDeleteAccessSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long accessSpecID = DataTypeConverter.uint(data.getInt());
		return new DeleteAccessSpec(header, accessSpecID);
	}

	public void serialize(DeleteAccessSpec das, ByteBuffer data) {
		// set message length
		MessageHeader header = das.getMessageHeader();
		header.setMessageLength(getLength(das));
		// serialize message
		serialize(header, data);
		data.putInt((int) das.getAccessSpecID());
	}

	public long getLength(DeleteAccessSpec das) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.24
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DeleteAccessSpecResponse deserializeDeleteAccessSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new DeleteAccessSpecResponse(header, status);
	}

	public void serialize(DeleteAccessSpecResponse dasRe, ByteBuffer data) {
		// set message length
		MessageHeader header = dasRe.getMessageHeader();
		header.setMessageLength(getLength(dasRe));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(dasRe.getStatus(), data);
	}

	public long getLength(DeleteAccessSpecResponse dasRe) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(dasRe.getStatus());
	}

	/**
	 * Section 17.1.25
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EnableAccessSpec deserializeEnableAccessSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long accessSpecID = DataTypeConverter.uint(data.getInt());
		return new EnableAccessSpec(header, accessSpecID);
	}

	public void serialize(EnableAccessSpec eas, ByteBuffer data) {
		// set message length
		MessageHeader header = eas.getMessageHeader();
		header.setMessageLength(getLength(eas));
		// serialize message
		serialize(header, data);
		data.putInt((int) eas.getAccessSpecID());
	}

	public long getLength(EnableAccessSpec eas) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.26
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EnableAccessSpecResponse deserializeEnableAccessSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new EnableAccessSpecResponse(header, status);
	}

	public void serialize(EnableAccessSpecResponse easRe, ByteBuffer data) {
		// set message length
		MessageHeader header = easRe.getMessageHeader();
		header.setMessageLength(getLength(easRe));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(easRe.getStatus(), data);
	}

	public long getLength(EnableAccessSpecResponse easRe) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(easRe.getStatus());
	}

	/**
	 * Section 17.1.27
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DisableAccessSpec deserializeDisableAccessSpec(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		long accessSpecID = DataTypeConverter.uint(data.getInt());
		return new DisableAccessSpec(header, accessSpecID);
	}

	public void serialize(DisableAccessSpec das, ByteBuffer data) {
		// set message length
		MessageHeader header = das.getMessageHeader();
		header.setMessageLength(getLength(das));
		// serialize message
		serialize(header, data);
		data.putInt((int) das.getAccessSpecID());
	}

	public long getLength(DisableAccessSpec das) {
		return MESSAGE_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.1.28
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public DisableAccessSpecResponse deserializeDisableAccessSpecResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new DisableAccessSpecResponse(header, status);
	}

	public void serialize(DisableAccessSpecResponse dasRe, ByteBuffer data) {
		// set message length
		MessageHeader header = dasRe.getMessageHeader();
		header.setMessageLength(getLength(dasRe));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(dasRe.getStatus(), data);
	}

	public long getLength(DisableAccessSpecResponse dasRe) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(dasRe.getStatus());
	}

	/**
	 * Section 17.1.29
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetAccessSpecs deserializeGetAccessSpecs(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		return new GetAccessSpecs(header);
	}

	public void serialize(GetAccessSpecs gas, ByteBuffer data) {
		// set message length
		MessageHeader header = gas.getMessageHeader();
		header.setMessageLength(getLength(gas));
		// serialize message
		serialize(header, data);
	}

	public long getLength(GetAccessSpecs gas) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.30
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetAccessSpecsResponse deserializeGetAccessSpecsResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		GetAccessSpecsResponse getasRe = null;
		int bufferStartPosition = data.position();
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		getasRe = new GetAccessSpecsResponse(header, status);

		List<AccessSpec> accessSpecList = new ArrayList<>();
		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case ACCESS_SPEC:
				AccessSpec as = parameterSerializer.deserializeAccessSpec(
						(TLVParameterHeader) parameterHeader, data);
				accessSpecList.add(as);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType()
								+ " (expected " + ParameterType.ACCESS_SPEC
								+ ")");
			}
		}
		getasRe.setAccessSpecList(accessSpecList);

		return getasRe;
	}

	public void serialize(GetAccessSpecsResponse getasRe, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = getasRe.getMessageHeader();
		header.setMessageLength(getLength(getasRe));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(getasRe.getStatus(), data);
		if (getasRe.getAccessSpecList() != null
				&& !getasRe.getAccessSpecList().isEmpty()) {
			for (AccessSpec as : getasRe.getAccessSpecList()) {
				parameterSerializer.serialize(as, data);
			}
		}
	}

	public long getLength(GetAccessSpecsResponse getasRe)
			throws InvalidParameterTypeException {
		int length = MESSAGE_HEADER_LENGTH
				+ +parameterSerializer.getLength(getasRe.getStatus());

		if (getasRe.getAccessSpecList() != null
				&& !getasRe.getAccessSpecList().isEmpty()) {
			for (AccessSpec as : getasRe.getAccessSpecList()) {
				length += parameterSerializer.getLength(as);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.31
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ClientRequestOP deserializeClientRequestOP(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		TagReportData tagRD = null;
		switch (parameterHeader.getParameterType()) {
		case TAG_REPORT_DATA:
			tagRD = parameterSerializer.deserializeTagReportData(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.TAG_REPORT_DATA + ")");
		}
		return new ClientRequestOP(header, tagRD);
	}

	public void serialize(ClientRequestOP clientROP, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = clientROP.getMessageHeader();
		header.setMessageLength(getLength(clientROP));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(clientROP.getTagReportData(), data);
	}

	public long getLength(ClientRequestOP clientROP)
			throws InvalidParameterTypeException {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(clientROP.getTagReportData());
	}

	/**
	 * Section 17.1.32
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ClientRequestOPResponse deserializeClientRequestOPResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		ClientRequestResponse clientRR = null;
		switch (parameterHeader.getParameterType()) {
		case CLIENT_REQUEST_RESPONSE:
			clientRR = parameterSerializer.deserializeClientRequestResponse(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.CLIENT_REQUEST_RESPONSE + ")");
		}
		return new ClientRequestOPResponse(header, clientRR);
	}

	public void serialize(ClientRequestOPResponse clientROPRes, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = clientROPRes.getMessageHeader();
		header.setMessageLength(getLength(clientROPRes));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(clientROPRes.getClientRequestResponse(),
				data);
	}

	public long getLength(ClientRequestOPResponse clientROPRes)
			throws InvalidParameterTypeException {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(clientROPRes
						.getClientRequestResponse());
	}

	/**
	 * Section 17.1.33
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetReport deserializeGetReport(MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		return new GetReport(header);
	}

	public void serialize(GetReport gR, ByteBuffer data) {
		// set message length
		MessageHeader header = gR.getMessageHeader();
		header.setMessageLength(getLength(gR));
		// serialize message
		serialize(header, data);
	}

	public long getLength(GetReport gR) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.34
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ROAccessReport deserializeROAccessReport(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message

		int bufferStartPosition = data.position();
		ROAccessReport roAR = new ROAccessReport(header);

		List<TagReportData> tagReportDataList = new ArrayList<>();
		List<RFSurveyReportData> rfSurveyReportDataList = new ArrayList<>();
		List<Custom> customList = new ArrayList<>();

		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case TAG_REPORT_DATA:
				TagReportData tagRD = parameterSerializer
						.deserializeTagReportData(
								(TLVParameterHeader) parameterHeader, data);
				tagReportDataList.add(tagRD);
				break;
			case RF_SURVEY_REPORT_DATA:
				RFSurveyReportData rfSRD = parameterSerializer
						.deserializeRFSurveyReportData(
								(TLVParameterHeader) parameterHeader, data);
				rfSurveyReportDataList.add(rfSRD);
				break;

			case CUSTOM:
				Custom cus = parameterSerializer.deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				customList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		roAR.setTagReportDataList(tagReportDataList);
		roAR.setRfSurveyReportDataList(rfSurveyReportDataList);
		roAR.setCusList(customList);

		return roAR;
	}

	public void serialize(ROAccessReport roAR, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set message length
		MessageHeader header = roAR.getMessageHeader();
		header.setMessageLength(getLength(roAR));
		// serialize message
		serialize(header, data);

		if (roAR.getTagReportDataList() != null
				&& !roAR.getTagReportDataList().isEmpty()) {
			for (TagReportData tagRD : roAR.getTagReportDataList()) {
				parameterSerializer.serialize(tagRD, data);
			}
		}

		if (roAR.getRfSurveyReportDataList() != null
				&& !roAR.getRfSurveyReportDataList().isEmpty()) {
			for (RFSurveyReportData rfSRD : roAR.getRfSurveyReportDataList()) {
				parameterSerializer.serialize(rfSRD, data);
			}
		}

		if (roAR.getCusList() != null && !roAR.getCusList().isEmpty()) {
			for (Custom cus : roAR.getCusList()) {
				parameterSerializer.serialize(cus, data);
			}
		}
	}

	public long getLength(ROAccessReport roAR)
			throws InvalidParameterTypeException {
		int length = MESSAGE_HEADER_LENGTH;

		if (roAR.getTagReportDataList() != null
				&& !roAR.getTagReportDataList().isEmpty()) {
			for (TagReportData tagRD : roAR.getTagReportDataList()) {
				length += parameterSerializer.getLength(tagRD);
			}
		}

		if (roAR.getRfSurveyReportDataList() != null
				&& !roAR.getRfSurveyReportDataList().isEmpty()) {
			for (RFSurveyReportData rfSRD : roAR.getRfSurveyReportDataList()) {
				length += parameterSerializer.getLength(rfSRD);
			}
		}

		if (roAR.getCusList() != null && !roAR.getCusList().isEmpty()) {
			for (Custom cus : roAR.getCusList()) {
				length += parameterSerializer.getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.35
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public Keepalive deserializeKeepalive(MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		return new Keepalive(header);
	}

	public void serialize(Keepalive keepA, ByteBuffer data) {
		// set message length
		MessageHeader header = keepA.getMessageHeader();
		header.setMessageLength(getLength(keepA));
		// serialize message
		serialize(header, data);
	}

	public long getLength(Keepalive keepA) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.36
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public KeepaliveAck deserializeKeepaliveAck(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		return new KeepaliveAck(header);
	}

	public void serialize(KeepaliveAck keepaACK, ByteBuffer data) {
		// set message length
		MessageHeader header = keepaACK.getMessageHeader();
		header.setMessageLength(getLength(keepaACK));
		// serialize message
		serialize(header, data);
	}

	public long getLength(KeepaliveAck keepaACK) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.37
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ReaderEventNotification deserializeReaderEventNotification(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		ReaderEventNotificationData rend = null;
		switch (parameterHeader.getParameterType()) {
		case READER_EVENT_NOTIFICATION_DATA:
			rend = parameterSerializer.deserializeReaderEventNotificationData(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.READER_EVENT_NOTIFICATION_DATA + ")");
		}
		return new ReaderEventNotification(header, rend);
	}

	public void serialize(ReaderEventNotification ren, ByteBuffer data) {
		// set message length
		MessageHeader header = ren.getMessageHeader();
		header.setMessageLength(getLength(ren));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(ren.getReaderEventNotificationData(),
				data);
	}

	public long getLength(ReaderEventNotification ren) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(ren
						.getReaderEventNotificationData());
	}

	/**
	 * Section 17.1.38
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EnableEventsAndReports deserializeEnableEventsAndReports(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		return new EnableEventsAndReports(header);
	}

	public void serialize(EnableEventsAndReports enEAR, ByteBuffer data) {
		// set message length
		MessageHeader header = enEAR.getMessageHeader();
		header.setMessageLength(getLength(enEAR));
		// serialize message
		serialize(header, data);
	}

	public long getLength(EnableEventsAndReports enEAR) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.39
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ErrorMessage deserializeErrorMessage(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new ErrorMessage(header, status);
	}

	public void serialize(ErrorMessage eM, ByteBuffer data) {
		// set message length
		MessageHeader header = eM.getMessageHeader();
		header.setMessageLength(getLength(eM));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(eM.getStatus(), data);
	}

	public long getLength(ErrorMessage eM) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(eM.getStatus());
	}

	/**
	 * Section 17.1.40
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetReaderConfig deserializeGetReaderConfig(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		int bufferStartPosition = data.position();
		int antennaID = DataTypeConverter.ushort(data.getShort());
		GetReaderConfigRequestedData requestedData = GetReaderConfigRequestedData
				.get((short) DataTypeConverter.ubyte(data.get()));
		int gpiPortNum = DataTypeConverter.ushort(data.getShort());
		int gpoPortNum = DataTypeConverter.ushort(data.getShort());
		GetReaderConfig grConfig = new GetReaderConfig(header, antennaID,
				requestedData, gpiPortNum, gpoPortNum);
		List<Custom> customList = new ArrayList<>();
		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case CUSTOM:
				Custom cus = parameterSerializer.deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				customList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType()
								+ " (expected " + ParameterType.CUSTOM + ")");
			}
		}
		grConfig.setCustomList(customList);
		return grConfig;
	}

	public void serialize(GetReaderConfig grConfig, ByteBuffer data) {
		// set message length
		MessageHeader header = grConfig.getMessageHeader();
		header.setMessageLength(getLength(grConfig));
		// serialize message
		serialize(header, data);
		data.putShort((short) grConfig.getAntennaID());
		data.put((byte) grConfig.getRequestedData().getValue());
		data.putShort((short) grConfig.getGpiPortNum());
		data.putShort((short) grConfig.getGpoPortNum());
		if (grConfig.getCustomList() != null
				&& !grConfig.getCustomList().isEmpty()) {
			for (Custom cus : grConfig.getCustomList()) {
				parameterSerializer.serialize(cus, data);
			}
		}
	}

	public long getLength(GetReaderConfig grConfig) {
		int length = MESSAGE_HEADER_LENGTH + 7;
		if (grConfig.getCustomList() != null
				&& !grConfig.getCustomList().isEmpty()) {
			for (Custom cus : grConfig.getCustomList()) {
				length += parameterSerializer.getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.41
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GetReaderConfigResponse deserializeGetReaderConfigResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message

		int bufferStartPosition = data.position();
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		GetReaderConfigResponse getReaderConfigRes = new GetReaderConfigResponse(
				header, status);

		Identification identification = null;
		List<AntennaProperties> antennaPropertiesList = new ArrayList<>();
		List<AntennaConfiguration> antennaConfigurationList = new ArrayList<>();
		ReaderEventNotificationSpec readerEventNotificationSpec = null;
		ROReportSpec roReportSpec = null;
		AccessReportSpec accessReportSpec = null;
		LLRPConfigurationStateValue llrpConfigurationStateValue = null;
		KeepaliveSpec keepaliveSpec = null;
		List<GPIPortCurrentState> gpiPortCurrentStateList = new ArrayList<>();
		List<GPOWriteData> gpoWriteDataList = new ArrayList<>();
		EventsAndReports eventAndReports = null;
		List<Custom> customList = new ArrayList<>();

		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case IDENTIFICATION:
				identification = parameterSerializer.deserializeIdentification(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case ANTENNA_PROPERTIES:
				AntennaProperties ap = parameterSerializer
						.deserializeAntennaProperties(
								(TLVParameterHeader) parameterHeader, data);
				antennaPropertiesList.add(ap);
				break;
			case ANTENNA_CONFIGURATION:
				AntennaConfiguration ac = parameterSerializer
						.deserializeAntennaConfiguration(
								(TLVParameterHeader) parameterHeader, data);
				antennaConfigurationList.add(ac);
				break;
			case READER_EVENT_NOTIFICATION_SPEC:
				readerEventNotificationSpec = parameterSerializer
						.deserializeReaderEventNotificationSpec(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case RO_REPORT_SPEC:
				roReportSpec = parameterSerializer.deserializeROReportSpec(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case ACCESS_REPORT_SPEC:
				accessReportSpec = parameterSerializer
						.deserializeAccessReportSpec(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case LLRP_CONFIGURATION_STATE_VALUE:
				llrpConfigurationStateValue = parameterSerializer
						.deserializeLLRPConfigurationStateValue(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case KEEPALIVE_SPEC:
				keepaliveSpec = parameterSerializer.deserializeKeepaliveSpec(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case GPI_PORT_CURRENT_STATE:
				GPIPortCurrentState gpiCS = parameterSerializer
						.deserializeGPIPortCurrentState(
								(TLVParameterHeader) parameterHeader, data);
				gpiPortCurrentStateList.add(gpiCS);
				break;
			case GPO_WRITE_DATA:
				GPOWriteData gpoWD = parameterSerializer
						.deserializeGPOWriteData(
								(TLVParameterHeader) parameterHeader, data);
				gpoWriteDataList.add(gpoWD);
				break;
			case EVENTS_AND_REPORTS:
				eventAndReports = parameterSerializer
						.deserializeEventsAndReports(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case CUSTOM:
				Custom cus = parameterSerializer.deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				customList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		getReaderConfigRes.setIdentification(identification);
		getReaderConfigRes.setAntennaPropertiesList(antennaPropertiesList);
		getReaderConfigRes
				.setAntennaConfigurationList(antennaConfigurationList);
		getReaderConfigRes
				.setReaderEventNotificationSpec(readerEventNotificationSpec);
		getReaderConfigRes.setRoReportSpec(roReportSpec);
		getReaderConfigRes.setAccessReportSpec(accessReportSpec);
		getReaderConfigRes
				.setLlrpConfigurationStateValue(llrpConfigurationStateValue);
		getReaderConfigRes.setKeepaliveSpec(keepaliveSpec);
		getReaderConfigRes.setGpiPortCurrentStateList(gpiPortCurrentStateList);
		getReaderConfigRes.setGpoWriteDataList(gpoWriteDataList);
		getReaderConfigRes.setEventAndReports(eventAndReports);
		getReaderConfigRes.setCustomList(customList);

		return getReaderConfigRes;
	}

	public void serialize(GetReaderConfigResponse getReaderConfigRes,
			ByteBuffer data) {
		// set message length
		MessageHeader header = getReaderConfigRes.getMessageHeader();
		header.setMessageLength(getLength(getReaderConfigRes));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(getReaderConfigRes.getStatus(), data);

		if (getReaderConfigRes.getIdentification() != null) {
			parameterSerializer.serialize(
					getReaderConfigRes.getIdentification(), data);
		}

		if (getReaderConfigRes.getAntennaPropertiesList() != null
				&& !getReaderConfigRes.getAntennaPropertiesList().isEmpty()) {
			for (AntennaProperties ap : getReaderConfigRes
					.getAntennaPropertiesList()) {
				parameterSerializer.serialize(ap, data);
			}
		}

		if (getReaderConfigRes.getAntennaConfigurationList() != null
				&& !getReaderConfigRes.getAntennaConfigurationList().isEmpty()) {
			for (AntennaConfiguration ac : getReaderConfigRes
					.getAntennaConfigurationList()) {
				parameterSerializer.serialize(ac, data);
			}
		}

		if (getReaderConfigRes.getReaderEventNotificationSpec() != null) {
			parameterSerializer.serialize(
					getReaderConfigRes.getReaderEventNotificationSpec(), data);
		}

		if (getReaderConfigRes.getRoReportSpec() != null) {
			parameterSerializer.serialize(getReaderConfigRes.getRoReportSpec(),
					data);
		}

		if (getReaderConfigRes.getAccessReportSpec() != null) {
			parameterSerializer.serialize(
					getReaderConfigRes.getAccessReportSpec(), data);
		}

		if (getReaderConfigRes.getLlrpConfigurationStateValue() != null) {
			parameterSerializer.serialize(
					getReaderConfigRes.getLlrpConfigurationStateValue(), data);
		}

		if (getReaderConfigRes.getKeepaliveSpec() != null) {
			parameterSerializer.serialize(
					getReaderConfigRes.getKeepaliveSpec(), data);
		}

		if (getReaderConfigRes.getGpiPortCurrentStateList() != null
				&& !getReaderConfigRes.getGpiPortCurrentStateList().isEmpty()) {
			for (GPIPortCurrentState gpi : getReaderConfigRes
					.getGpiPortCurrentStateList()) {
				parameterSerializer.serialize(gpi, data);
			}
		}

		if (getReaderConfigRes.getGpoWriteDataList() != null
				&& !getReaderConfigRes.getGpoWriteDataList().isEmpty()) {
			for (GPOWriteData gpo : getReaderConfigRes.getGpoWriteDataList()) {
				parameterSerializer.serialize(gpo, data);
			}
		}

		if (getReaderConfigRes.getEventAndReports() != null) {
			parameterSerializer.serialize(
					getReaderConfigRes.getEventAndReports(), data);
		}

		if (getReaderConfigRes.getCustomList() != null
				&& !getReaderConfigRes.getCustomList().isEmpty()) {
			for (Custom cus : getReaderConfigRes.getCustomList()) {
				parameterSerializer.serialize(cus, data);
			}
		}
	}

	public long getLength(GetReaderConfigResponse getReaderConfigRes) {
		int length = MESSAGE_HEADER_LENGTH;
		length += parameterSerializer.getLength(getReaderConfigRes.getStatus());

		if (getReaderConfigRes.getIdentification() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getIdentification());
		}

		if (getReaderConfigRes.getAntennaPropertiesList() != null
				&& !getReaderConfigRes.getAntennaPropertiesList().isEmpty()) {
			for (AntennaProperties ap : getReaderConfigRes
					.getAntennaPropertiesList()) {
				length += parameterSerializer.getLength(ap);
			}
		}

		if (getReaderConfigRes.getAntennaConfigurationList() != null
				&& !getReaderConfigRes.getAntennaConfigurationList().isEmpty()) {
			for (AntennaConfiguration ac : getReaderConfigRes
					.getAntennaConfigurationList()) {
				length += parameterSerializer.getLength(ac);
			}
		}

		if (getReaderConfigRes.getReaderEventNotificationSpec() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getReaderEventNotificationSpec());
		}

		if (getReaderConfigRes.getRoReportSpec() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getRoReportSpec());
		}

		if (getReaderConfigRes.getAccessReportSpec() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getAccessReportSpec());
		}

		if (getReaderConfigRes.getLlrpConfigurationStateValue() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getLlrpConfigurationStateValue());
		}

		if (getReaderConfigRes.getKeepaliveSpec() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getKeepaliveSpec());
		}

		if (getReaderConfigRes.getGpiPortCurrentStateList() != null
				&& !getReaderConfigRes.getGpiPortCurrentStateList().isEmpty()) {
			for (GPIPortCurrentState gpi : getReaderConfigRes
					.getGpiPortCurrentStateList()) {
				length += parameterSerializer.getLength(gpi);
			}
		}

		if (getReaderConfigRes.getGpoWriteDataList() != null
				&& !getReaderConfigRes.getGpoWriteDataList().isEmpty()) {
			for (GPOWriteData gpo : getReaderConfigRes.getGpoWriteDataList()) {
				length += parameterSerializer.getLength(gpo);
			}
		}

		if (getReaderConfigRes.getEventAndReports() != null) {
			length += parameterSerializer.getLength(getReaderConfigRes
					.getEventAndReports());
		}

		if (getReaderConfigRes.getCustomList() != null
				&& !getReaderConfigRes.getCustomList().isEmpty()) {
			for (Custom cus : getReaderConfigRes.getCustomList()) {
				length += parameterSerializer.getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.42
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public SetReaderConfig deserializeSetReaderConfig(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message

		int bufferStartPosition = data.position();
		byte b = data.get();
		boolean reset = (b & 0x80) > 0;
		SetReaderConfig setReaderConfig = new SetReaderConfig(header, reset);

		ReaderEventNotificationSpec readerEventNotificationSpec = null;
		List<AntennaProperties> antennaPropertiesList = new ArrayList<>();
		List<AntennaConfiguration> antennaConfigurationList = new ArrayList<>();
		ROReportSpec roReportSpec = null;
		AccessReportSpec accessReportSpec = null;
		KeepaliveSpec keepaliveSpec = null;
		List<GPOWriteData> gpoWriteDataList = new ArrayList<>();
		List<GPIPortCurrentState> gpiPortCurrentStateList = new ArrayList<>();
		EventsAndReports eventAndReports = null;
		List<Custom> customList = new ArrayList<>();

		while (header.getMessageLength() > (data.position()
				- bufferStartPosition + MESSAGE_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = parameterSerializer
					.deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case READER_EVENT_NOTIFICATION_SPEC:
				readerEventNotificationSpec = parameterSerializer
						.deserializeReaderEventNotificationSpec(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case ANTENNA_PROPERTIES:
				AntennaProperties ap = parameterSerializer
						.deserializeAntennaProperties(
								(TLVParameterHeader) parameterHeader, data);
				antennaPropertiesList.add(ap);
				break;
			case ANTENNA_CONFIGURATION:
				AntennaConfiguration ac = parameterSerializer
						.deserializeAntennaConfiguration(
								(TLVParameterHeader) parameterHeader, data);
				antennaConfigurationList.add(ac);
				break;
			case RO_REPORT_SPEC:
				roReportSpec = parameterSerializer.deserializeROReportSpec(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case ACCESS_REPORT_SPEC:
				accessReportSpec = parameterSerializer
						.deserializeAccessReportSpec(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case KEEPALIVE_SPEC:
				keepaliveSpec = parameterSerializer.deserializeKeepaliveSpec(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case GPO_WRITE_DATA:
				GPOWriteData gpoWD = parameterSerializer
						.deserializeGPOWriteData(
								(TLVParameterHeader) parameterHeader, data);
				gpoWriteDataList.add(gpoWD);
				break;
			case GPI_PORT_CURRENT_STATE:
				GPIPortCurrentState gpiCS = parameterSerializer
						.deserializeGPIPortCurrentState(
								(TLVParameterHeader) parameterHeader, data);
				gpiPortCurrentStateList.add(gpiCS);
				break;
			case EVENTS_AND_REPORTS:
				eventAndReports = parameterSerializer
						.deserializeEventsAndReports(
								(TLVParameterHeader) parameterHeader, data);
				break;
			case CUSTOM:
				Custom cus = parameterSerializer.deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				customList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		setReaderConfig.setAntennaPropertiesList(antennaPropertiesList);
		setReaderConfig.setAntennaConfigurationList(antennaConfigurationList);
		setReaderConfig
				.setReaderEventNotificationSpec(readerEventNotificationSpec);
		setReaderConfig.setRoReportSpec(roReportSpec);
		setReaderConfig.setAccessReportSpec(accessReportSpec);
		setReaderConfig.setKeepaliveSpec(keepaliveSpec);
		setReaderConfig.setGpiPortCurrentStateList(gpiPortCurrentStateList);
		setReaderConfig.setGpoWriteDataList(gpoWriteDataList);
		setReaderConfig.setEventAndReports(eventAndReports);
		setReaderConfig.setCustomList(customList);

		return setReaderConfig;
	}

	public void serialize(SetReaderConfig setReaderConfig, ByteBuffer data) {
		// set message length
		MessageHeader header = setReaderConfig.getMessageHeader();
		header.setMessageLength(getLength(setReaderConfig));
		// serialize message
		serialize(header, data);
		byte b = (byte) 0x00;
		if (setReaderConfig.isResetToFactoryDefaults()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);

		if (setReaderConfig.getReaderEventNotificationSpec() != null) {
			parameterSerializer.serialize(
					setReaderConfig.getReaderEventNotificationSpec(), data);
		}

		if (setReaderConfig.getAntennaPropertiesList() != null
				&& !setReaderConfig.getAntennaPropertiesList().isEmpty()) {
			for (AntennaProperties ap : setReaderConfig
					.getAntennaPropertiesList()) {
				parameterSerializer.serialize(ap, data);
			}
		}

		if (setReaderConfig.getAntennaConfigurationList() != null
				&& !setReaderConfig.getAntennaConfigurationList().isEmpty()) {
			for (AntennaConfiguration ac : setReaderConfig
					.getAntennaConfigurationList()) {
				parameterSerializer.serialize(ac, data);
			}
		}

		if (setReaderConfig.getRoReportSpec() != null) {
			parameterSerializer.serialize(setReaderConfig.getRoReportSpec(),
					data);
		}

		if (setReaderConfig.getAccessReportSpec() != null) {
			parameterSerializer.serialize(
					setReaderConfig.getAccessReportSpec(), data);
		}

		if (setReaderConfig.getKeepaliveSpec() != null) {
			parameterSerializer.serialize(setReaderConfig.getKeepaliveSpec(),
					data);
		}

		if (setReaderConfig.getGpoWriteDataList() != null
				&& !setReaderConfig.getGpoWriteDataList().isEmpty()) {
			for (GPOWriteData gpo : setReaderConfig.getGpoWriteDataList()) {
				parameterSerializer.serialize(gpo, data);
			}
		}

		if (setReaderConfig.getGpiPortCurrentStateList() != null
				&& !setReaderConfig.getGpiPortCurrentStateList().isEmpty()) {
			for (GPIPortCurrentState gpi : setReaderConfig
					.getGpiPortCurrentStateList()) {
				parameterSerializer.serialize(gpi, data);
			}
		}

		if (setReaderConfig.getEventAndReports() != null) {
			parameterSerializer.serialize(setReaderConfig.getEventAndReports(),
					data);
		}

		if (setReaderConfig.getCustomList() != null
				&& !setReaderConfig.getCustomList().isEmpty()) {
			for (Custom cus : setReaderConfig.getCustomList()) {
				parameterSerializer.serialize(cus, data);
			}
		}
	}

	public long getLength(SetReaderConfig setReaderConfig) {
		int length = MESSAGE_HEADER_LENGTH + 1;

		if (setReaderConfig.getReaderEventNotificationSpec() != null) {
			length += parameterSerializer.getLength(setReaderConfig
					.getReaderEventNotificationSpec());
		}

		if (setReaderConfig.getAntennaPropertiesList() != null
				&& !setReaderConfig.getAntennaPropertiesList().isEmpty()) {
			for (AntennaProperties ap : setReaderConfig
					.getAntennaPropertiesList()) {
				length += parameterSerializer.getLength(ap);
			}
		}

		if (setReaderConfig.getAntennaConfigurationList() != null
				&& !setReaderConfig.getAntennaConfigurationList().isEmpty()) {
			for (AntennaConfiguration ac : setReaderConfig
					.getAntennaConfigurationList()) {
				length += parameterSerializer.getLength(ac);
			}
		}

		if (setReaderConfig.getRoReportSpec() != null) {
			length += parameterSerializer.getLength(setReaderConfig
					.getRoReportSpec());
		}

		if (setReaderConfig.getAccessReportSpec() != null) {
			length += parameterSerializer.getLength(setReaderConfig
					.getAccessReportSpec());
		}

		if (setReaderConfig.getKeepaliveSpec() != null) {
			length += parameterSerializer.getLength(setReaderConfig
					.getKeepaliveSpec());
		}

		if (setReaderConfig.getGpoWriteDataList() != null
				&& !setReaderConfig.getGpoWriteDataList().isEmpty()) {
			for (GPOWriteData gpo : setReaderConfig.getGpoWriteDataList()) {
				length += parameterSerializer.getLength(gpo);
			}
		}

		if (setReaderConfig.getGpiPortCurrentStateList() != null
				&& !setReaderConfig.getGpiPortCurrentStateList().isEmpty()) {
			for (GPIPortCurrentState gpi : setReaderConfig
					.getGpiPortCurrentStateList()) {
				length += parameterSerializer.getLength(gpi);
			}
		}

		if (setReaderConfig.getEventAndReports() != null) {
			length += parameterSerializer.getLength(setReaderConfig
					.getEventAndReports());
		}

		if (setReaderConfig.getCustomList() != null
				&& !setReaderConfig.getCustomList().isEmpty()) {
			for (Custom cus : setReaderConfig.getCustomList()) {
				length += parameterSerializer.getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.1.43
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public SetReaderConfigResponse deserializeSetReaderConfigResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new SetReaderConfigResponse(header, status);
	}

	public void serialize(SetReaderConfigResponse srcr, ByteBuffer data) {
		// set message length
		MessageHeader header = srcr.getMessageHeader();
		header.setMessageLength(getLength(srcr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(srcr.getStatus(), data);
	}

	public long getLength(SetReaderConfigResponse srcr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(srcr.getStatus());
	}

	/**
	 * Section 17.1.44
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public CloseConnection deserializeCloseConnection(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		return new CloseConnection(header);
	}

	public void serialize(CloseConnection cc, ByteBuffer data) {
		// set message length
		MessageHeader header = cc.getMessageHeader();
		header.setMessageLength(getLength(cc));
		// serialize message
		serialize(header, data);
	}

	public long getLength(CloseConnection cc) {
		return MESSAGE_HEADER_LENGTH;
	}

	/**
	 * Section 17.1.45
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public CloseConnectionResponse deserializeCloseConnectionResponse(
			MessageHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize message
		ParameterHeader parameterHeader = parameterSerializer
				.deserializeParameterHeader(data);
		LLRPStatus status = null;
		switch (parameterHeader.getParameterType()) {
		case LLRP_STATUS:
			status = parameterSerializer.deserializeLLRPStatus(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType() + " (expected "
					+ ParameterType.LLRP_STATUS + ")");
		}
		return new CloseConnectionResponse(header, status);
	}

	public void serialize(CloseConnectionResponse ccr, ByteBuffer data) {
		// set message length
		MessageHeader header = ccr.getMessageHeader();
		header.setMessageLength(getLength(ccr));
		// serialize message
		serialize(header, data);
		parameterSerializer.serialize(ccr.getStatus(), data);
	}

	public long getLength(CloseConnectionResponse ccr) {
		return MESSAGE_HEADER_LENGTH
				+ parameterSerializer.getLength(ccr.getStatus());
	}

	/**
	 * Section 17.1.46
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public CustomMessage deserializeCustomMessage(MessageHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize message
		int bufferStartPosition = data.position();
		long vendorIdentifier = DataTypeConverter.uint(data.getInt());
		short messageSubType = DataTypeConverter.ubyte(data.get());

		byte[] vsp = new byte[(int) header.getMessageLength() - data.position()
				+ bufferStartPosition - MESSAGE_HEADER_LENGTH];
		data.get(vsp);
		return new CustomMessage(header, vendorIdentifier, messageSubType, vsp);
	}

	public void serialize(CustomMessage cM, ByteBuffer data) {
		// set message length
		MessageHeader header = cM.getMessageHeader();
		header.setMessageLength(getLength(cM));
		// serialize message
		serialize(header, data);
		data.putInt((int) cM.getVendorIdentifier());
		data.put((byte) cM.getMessageSubType());
		data.put(cM.getVendorPayload());
	}

	public long getLength(CustomMessage cM) {
		return MESSAGE_HEADER_LENGTH + 5 + cM.getVendorPayload().length;
	}
}
