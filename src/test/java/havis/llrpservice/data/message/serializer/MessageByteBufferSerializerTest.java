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
import havis.llrpservice.data.message.MessageTypes.MessageType;
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
import havis.llrpservice.data.message.parameter.*;
import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;
import havis.llrpservice.data.message.parameter.serializer.InvalidParameterTypeException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class MessageByteBufferSerializerTest {

	@Test
	public void roAccessReport() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// EPC96 13
		byte[] epc = { 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, (byte) 0x80,
				(byte) 0x90, (byte) 0xA0, (byte) 0xB0, (byte) 0xC0 };
		EPC96 epc96 = new EPC96(new TVParameterHeader(), epc);

		// tagReportData 17
		TagReportData tagRD = new TagReportData(
				new TLVParameterHeader((byte) 0), epc96);
		// tagReportData List
		List<TagReportData> tagReportDataList = new ArrayList<>();
		tagReportDataList.add(tagRD);

		// rfSurveyReportData 35
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		FrequencyRSSILevelEntry fle = new FrequencyRSSILevelEntry(
				new TLVParameterHeader((byte) 0), (long) 10000, (long) 20000,
				(byte) -5, (byte) 20, utc);
		// rfSurveyReportData List
		List<FrequencyRSSILevelEntry> frequencyRSSILevelEntryList = new ArrayList<>();
		frequencyRSSILevelEntryList.add(fle);
		RFSurveyReportData rfSRD = new RFSurveyReportData(
				new TLVParameterHeader((byte) 0), frequencyRSSILevelEntryList);

		ROSpecID roSID = new ROSpecID(new TVParameterHeader(), 11111111L);
		rfSRD.setRoSpecID(roSID);

		List<RFSurveyReportData> rfSurveyReportDataList = new ArrayList<>();
		rfSurveyReportDataList.add(rfSRD);

		// ROAccessReport
		ROAccessReport roAR = new ROAccessReport(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		roAR.setTagReportDataList(tagReportDataList);
		roAR.setRfSurveyReportDataList(rfSurveyReportDataList);

		int messageLength = (int) serializer.getLength(roAR);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(roAR, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.RO_ACCESS_REPORT.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TAG_REPORT_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		byte[] place = new byte[17 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_REPORT_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 35);
		data.rewind();

		// deserialize
		roAR = serializer.deserializeROAccessReport(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = roAR.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.RO_ACCESS_REPORT);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		Assert.assertArrayEquals(roAR.getTagReportDataList().get(0).getEpc96()
				.getEpc(), epc);

		Assert.assertEquals(roAR.getRfSurveyReportDataList().get(0)
				.getFrequencyRSSILevelEntryList().get(0).getPeekRSSI(), 20);
		Assert.assertEquals(roAR.getRfSurveyReportDataList().get(0)
				.getRoSpecID().getRoSpecID(), 11111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(roAR), 35 + 17 + 10);
	}

	@Test
	public void customMessage() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		byte[] cusData = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x05, 0x05 };
		CustomMessage cusMes = new CustomMessage(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), 12345678L, (short) 100,
				cusData);
		int messageLength = (int) serializer.getLength(cusMes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(cusMes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.CUSTOM_MESSAGE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 12345678L);
		Assert.assertEquals(data.get(), 100);
		byte[] dst = new byte[7];
		data.get(dst, 0, 7);
		Assert.assertArrayEquals(dst, cusData);
		data.rewind();

		// deserialize
		cusMes = serializer.deserializeCustomMessage(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = cusMes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.CUSTOM_MESSAGE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(cusMes.getVendorIdentifier(), 12345678L);
		Assert.assertEquals(cusMes.getMessageSubType(), 100);
		Assert.assertArrayEquals(cusMes.getVendorPayload(), cusData);

		// getLength
		Assert.assertEquals(serializer.getLength(cusMes), 22);
	}

	@Test
	public void errorMessage() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		ErrorMessage eM = new ErrorMessage(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), llrpStatus);
		int messageLength = (int) serializer.getLength(eM);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(eM, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ERROR_MESSAGE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		eM = serializer.deserializeErrorMessage(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = eM.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ERROR_MESSAGE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(eM), messageLength);
	}

	@Test
	public void enableEventsAndReports()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		EnableEventsAndReports enEAR = new EnableEventsAndReports(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(enEAR);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(enEAR, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ENABLE_EVENTS_AND_REPORTS.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		enEAR = serializer.deserializeEnableEventsAndReports(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = enEAR.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ENABLE_EVENTS_AND_REPORTS);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(enEAR), 10);
	}

	@Test
	public void keepaliveAck() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		KeepaliveAck kaAck = new KeepaliveAck(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(kaAck);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(kaAck, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.KEEPALIVE_ACK.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		kaAck = serializer.deserializeKeepaliveAck(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = kaAck.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.KEEPALIVE_ACK);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(kaAck), 10);
	}

	@Test
	public void keepalive() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		Keepalive ka = new Keepalive(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(ka);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ka, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.KEEPALIVE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		ka = serializer.deserializeKeepalive(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ka.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.KEEPALIVE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ka), 10);
	}

	@Test
	public void getReport() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		GetReport getR = new GetReport(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(getR);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getR, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.GET_REPORT.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		getR = serializer.deserializeGetReport(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getR.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_REPORT);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(getR), 10);
	}

	@Test
	public void clientRequestOPResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// EPCData 8
		BitSet epc = new BitSet();
		epc.set(0);
		epc.set(1);
		epc.set(2);
		epc.set(3);
		epc.set(8);
		epc.set(10);
		epc.set(12);
		epc.set(14);
		EPCData epcData = new EPCData(new TLVParameterHeader((byte) 0), epc);
		epcData.setEpcLengthBits(16);

		// C1G2Read 15
		C1G2Read c1g2Read = new C1G2Read(new TLVParameterHeader((byte) 0),
				33333, 111111111L, (byte) 3, 1234, 10000);

		// C1G2Recommission 11
		C1G2Recommission c1g2Re = new C1G2Recommission(new TLVParameterHeader(
				(byte) 0), 1000, 11111111L, true, false, false);

		List<Parameter> opSpecList = new ArrayList<>();
		opSpecList.add(c1g2Read);
		opSpecList.add(c1g2Re);
		ClientRequestResponse crRes = new ClientRequestResponse(
				new TLVParameterHeader((byte) 0), 11111111L, epcData);
		crRes.setOpSpecList(opSpecList);

		ClientRequestOPResponse cROPRes = new ClientRequestOPResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				crRes);
		int messageLength = (int) serializer.getLength(cROPRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(cROPRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.CLIENT_REQUEST_OP_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		cROPRes = serializer.deserializeClientRequestOPResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = cROPRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.CLIENT_REQUEST_OP_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(cROPRes), 52);
	}

	@Test
	public void clientRequestOP() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// EPC96 13
		byte[] epc = { 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, (byte) 0x80,
				(byte) 0x90, (byte) 0xA0, (byte) 0xB0, (byte) 0xC0 };
		EPC96 epc96 = new EPC96(new TVParameterHeader(), epc);

		// TagReportData
		TagReportData tagRD = new TagReportData(
				new TLVParameterHeader((byte) 0), epc96);

		// ROSpecID 5
		ROSpecID roSID = new ROSpecID(new TVParameterHeader(), 11111111L);
		tagRD.setRoSpecID(roSID);

		// InventoryParameterSpecID 3
		InventoryParameterSpecID ipsID = new InventoryParameterSpecID(
				new TVParameterHeader(), 1234);
		tagRD.setInvParaSpecID(ipsID);

		// PeakRSSI 2
		PeakRSSI rssi = new PeakRSSI(new TVParameterHeader(), (byte) -12);
		tagRD.setPeakRSSI(rssi);

		// FirstSeenTimestampUTC 9
		FirstSeenTimestampUTC fsUTC = new FirstSeenTimestampUTC(
				new TVParameterHeader(), new BigInteger("111111113"));
		tagRD.setFirstSTUTC(fsUTC);

		// LastSeenTimestampUTC 9
		LastSeenTimestampUTC lsUTC = new LastSeenTimestampUTC(
				new TVParameterHeader(), new BigInteger("111111113"));
		tagRD.setLastSTUTC(lsUTC);

		// c1g2TagDataList
		List<Parameter> c1g2TagDataList = new ArrayList<>();
		// C1G2PC 3
		C1G2PC pc = new C1G2PC(new TVParameterHeader(), 1234);
		c1g2TagDataList.add(pc);
		// C1G2CRC 3
		C1G2CRC crc = new C1G2CRC(new TVParameterHeader(), 1234);
		c1g2TagDataList.add(crc);
		tagRD.setC1g2TagDataList(c1g2TagDataList);

		// opSpecResultList
		List<Parameter> opSpecResultList = new ArrayList<>();
		// C1G2ReadOpSpecResult 13
		byte[] readData = { 0x10, 0x20, (byte) 0xFE };
		C1G2ReadOpSpecResult c1g2ROSR = new C1G2ReadOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2ReadOpSpecResultValues.SUCCESS, 1234, readData);
		opSpecResultList.add(c1g2ROSR);
		// C1G2LockOpSpecResult 7
		C1G2LockOpSpecResult c1g2LOSR = new C1G2LockOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2LockOpSpecResultValues.SUCCESS, 1234);
		opSpecResultList.add(c1g2LOSR);
		tagRD.setOpSpecResultList(opSpecResultList);

		ClientRequestOP cROP = new ClientRequestOP(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), tagRD);
		int messageLength = (int) serializer.getLength(cROP);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(cROP, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.CLIENT_REQUEST_OP.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		cROP = serializer.deserializeClientRequestOP(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = cROP.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.CLIENT_REQUEST_OP);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(cROP), 81);
	}

	@Test
	public void closeConnectionResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		CloseConnectionResponse ccR = new CloseConnectionResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(ccR);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ccR, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.CLOSE_CONNECTION_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		ccR = serializer.deserializeCloseConnectionResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ccR.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.CLOSE_CONNECTION_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ccR), messageLength);
	}

	@Test
	public void closeConnection() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		CloseConnection cc = new CloseConnection(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(cc);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(cc, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.CLOSE_CONNECTION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		cc = serializer.deserializeCloseConnection(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = cc.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.CLOSE_CONNECTION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(cc), 10);
	}

	@Test
	public void getAccessSpecsResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize

		// LLRPStatus 31
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		GetAccessSpecsResponse getASRes = new GetAccessSpecsResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);

		// AccessSpecStopTrigger 7
		AccessSpecStopTrigger aSSTrigger = new AccessSpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				AccessSpecStopTriggerType.OPERATION_COUNT, 555);

		// AccessCommand 4 + 34 + 6 + 15
		// C1G2TagSpec 34
		// 1111000010101010
		BitSet mask = new BitSet();
		mask.set(0);
		mask.set(1);
		mask.set(2);
		mask.set(3);
		mask.set(8);
		mask.set(10);
		mask.set(12);
		mask.set(14);

		// 0111111011001100
		BitSet tagData = new BitSet();
		tagData.set(1);
		tagData.set(2);
		tagData.set(3);
		tagData.set(4);
		tagData.set(5);
		tagData.set(6);
		tagData.set(8);
		tagData.set(9);
		tagData.set(12);
		tagData.set(13);

		C1G2TargetTag c1g2TT = new C1G2TargetTag(new TLVParameterHeader(
				(byte) 0), (byte) 3, true, 100, mask, tagData);
		c1g2TT.setDataBitCount(16);
		c1g2TT.setMaskBitCount(16);

		C1G2TagSpec c1g2TagSpec = new C1G2TagSpec(new TLVParameterHeader(
				(byte) 0), c1g2TT);
		c1g2TagSpec.setTagPattern2(c1g2TT);

		// ClientRequestOpSpec 6
		ClientRequestOpSpec crOpSpec = new ClientRequestOpSpec(
				new TLVParameterHeader((byte) 0), 5555);

		// C1G2Read 15
		C1G2Read c1g2Read = new C1G2Read(new TLVParameterHeader((byte) 0),
				33333, 111111111L, (byte) 3, 1234, 10000);

		List<Parameter> opSpecList = new ArrayList<>();
		opSpecList.add(crOpSpec);
		opSpecList.add(c1g2Read);

		AccessCommand ac = new AccessCommand(new TLVParameterHeader((byte) 0),
				c1g2TagSpec, opSpecList);

		// AccessReportSpec 5
		AccessReportSpec ars = new AccessReportSpec(new TLVParameterHeader(
				(byte) 0), AccessReportTrigger.END_OF_ACCESSSPEC);

		AccessSpec aSpec = new AccessSpec(new TLVParameterHeader((byte) 0),
				11111111L, 7777, ProtocolId.EPC_GLOBAL_C1G2, true, 22222222L,
				aSSTrigger, ac);
		aSpec.setAccessReportSpec(ars);

		//
		List<AccessSpec> accessSpecList = new ArrayList<>();
		accessSpecList.add(aSpec);
		getASRes.setAccessSpecList(accessSpecList);
		int messageLength = (int) serializer.getLength(getASRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getASRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.GET_ACCESSSPECS_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		getASRes = serializer.deserializeGetAccessSpecsResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getASRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_ACCESSSPECS_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		TLVParameterHeader header = getASRes.getStatus().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 1);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.LLRP_STATUS);
		Assert.assertEquals(header.getParameterLength(), 31);

		header = getASRes.getAccessSpecList().get(0).getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_SPEC);
		Assert.assertEquals(header.getParameterLength(), 87);
		Assert.assertEquals(getASRes.getAccessSpecList().get(0)
				.getAccessSpecId(), 11111111L);
		Assert.assertEquals(getASRes.getAccessSpecList().get(0).getAntennaId(),
				7777);
		Assert.assertEquals(
				getASRes.getAccessSpecList().get(0).getProtocolId(),
				ProtocolId.EPC_GLOBAL_C1G2);
		Assert.assertEquals(getASRes.getAccessSpecList().get(0)
				.isCurrentState(), true);
		Assert.assertEquals(getASRes.getAccessSpecList().get(0).getRoSpecId(),
				22222222L);

		header = getASRes.getAccessSpecList().get(0).getAccessSpecStopTrigger()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_SPEC_STOP_TRIGGER);
		Assert.assertEquals(header.getParameterLength(), 7);

		header = getASRes.getAccessSpecList().get(0).getAccessCommand()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_COMMAND);
		Assert.assertEquals(header.getParameterLength(), 4 + 34 + 6 + 15);

		header = getASRes.getAccessSpecList().get(0).getAccessCommand()
				.getC1g2TagSpec().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_SPEC);
		Assert.assertEquals(header.getParameterLength(), 34);

		header = (TLVParameterHeader) getASRes.getAccessSpecList().get(0)
				.getAccessCommand().getOpSpecList().get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.CLIENT_REQUEST_OP_SPEC);
		Assert.assertEquals(header.getParameterLength(), 6);
		Assert.assertEquals(((ClientRequestOpSpec) getASRes.getAccessSpecList()
				.get(0).getAccessCommand().getOpSpecList().get(0))
				.getOpSpecID(), 5555);

		header = (TLVParameterHeader) getASRes.getAccessSpecList().get(0)
				.getAccessCommand().getOpSpecList().get(1).getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.C1G2_READ);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(((C1G2Read) getASRes.getAccessSpecList().get(0)
				.getAccessCommand().getOpSpecList().get(1)).getWordCount(),
				10000);

		header = getASRes.getAccessSpecList().get(0).getAccessReportSpec()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_REPORT_SPEC);
		Assert.assertEquals(header.getParameterLength(), 5);

		// getLength
		Assert.assertEquals(serializer.getLength(getASRes), 10 + 31 + 87);
	}

	@Test
	public void getAccessSpecs() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		GetAccessSpecs getAS = new GetAccessSpecs(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(getAS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getAS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.GET_ACCESSSPECS.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		getAS = serializer.deserializeGetAccessSpecs(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getAS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_ACCESSSPECS);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(getAS), 10);
	}

	@Test
	public void disableAccessSpecResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		DisableAccessSpecResponse disableASRes = new DisableAccessSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(disableASRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(disableASRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DISABLE_ACCESSSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		disableASRes = serializer.deserializeDisableAccessSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = disableASRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DISABLE_ACCESSSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(disableASRes), messageLength);
	}

	@Test
	public void disableAccessSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		DisableAccessSpec disableAS = new DisableAccessSpec(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(disableAS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(disableAS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DISABLE_ACCESSSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		disableAS = serializer.deserializeDisableAccessSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = disableAS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DISABLE_ACCESSSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(disableAS.getAccessSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(disableAS), 14);
	}

	@Test
	public void enableAccessSpecResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		EnableAccessSpecResponse enableASRes = new EnableAccessSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(enableASRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(enableASRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ENABLE_ACCESSSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		enableASRes = serializer.deserializeEnableAccessSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = enableASRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ENABLE_ACCESSSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(enableASRes), messageLength);
	}

	@Test
	public void enableAccessSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		EnableAccessSpec enableAS = new EnableAccessSpec(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(enableAS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(enableAS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ENABLE_ACCESSSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		enableAS = serializer.deserializeEnableAccessSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = enableAS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ENABLE_ACCESSSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(enableAS.getAccessSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(enableAS), 14);
	}

	@Test
	public void deleteAccessSpecResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		DeleteAccessSpecResponse deleteASRes = new DeleteAccessSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(deleteASRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(deleteASRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DELETE_ACCESSSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		deleteASRes = serializer.deserializeDeleteAccessSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = deleteASRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DELETE_ACCESSSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(deleteASRes), messageLength);
	}

	@Test
	public void deleteAccessSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		DeleteAccessSpec deleteAS = new DeleteAccessSpec(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(deleteAS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(deleteAS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DELETE_ACCESSSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		deleteAS = serializer.deserializeDeleteAccessSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = deleteAS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DELETE_ACCESSSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(deleteAS.getAccessSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(deleteAS), 14);
	}

	@Test
	public void addAccessSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		AddAccessSpecResponse addASRes = new AddAccessSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(addASRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(addASRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ADD_ACCESSSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		addASRes = serializer.deserializeAddAccessSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = addASRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ADD_ACCESSSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(addASRes), messageLength);
	}

	@Test
	public void addAccessSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// AccessSpecStopTrigger 7
		AccessSpecStopTrigger aSSTrigger = new AccessSpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				AccessSpecStopTriggerType.OPERATION_COUNT, 555);

		// AccessCommand 4 + 34 + 6 + 15
		// C1G2TagSpec 34
		// 1111000010101010
		BitSet mask = new BitSet();
		mask.set(0);
		mask.set(1);
		mask.set(2);
		mask.set(3);
		mask.set(8);
		mask.set(10);
		mask.set(12);
		mask.set(14);

		// 0111111011001100
		BitSet tagData = new BitSet();
		tagData.set(1);
		tagData.set(2);
		tagData.set(3);
		tagData.set(4);
		tagData.set(5);
		tagData.set(6);
		tagData.set(8);
		tagData.set(9);
		tagData.set(12);
		tagData.set(13);

		C1G2TargetTag c1g2TT = new C1G2TargetTag(new TLVParameterHeader(
				(byte) 0), (byte) 3, true, 100, mask, tagData);
		c1g2TT.setDataBitCount(16);
		c1g2TT.setMaskBitCount(16);

		C1G2TagSpec c1g2TagSpec = new C1G2TagSpec(new TLVParameterHeader(
				(byte) 0), c1g2TT);
		c1g2TagSpec.setTagPattern2(c1g2TT);

		// ClientRequestOpSpec 6
		ClientRequestOpSpec crOpSpec = new ClientRequestOpSpec(
				new TLVParameterHeader((byte) 0), 5555);

		// C1G2Read 15
		C1G2Read c1g2Read = new C1G2Read(new TLVParameterHeader((byte) 0),
				33333, 111111111L, (byte) 3, 1234, 10000);

		List<Parameter> opSpecList = new ArrayList<>();
		opSpecList.add(crOpSpec);
		opSpecList.add(c1g2Read);

		AccessCommand ac = new AccessCommand(new TLVParameterHeader((byte) 0),
				c1g2TagSpec, opSpecList);

		// AccessReportSpec 5
		AccessReportSpec ars = new AccessReportSpec(new TLVParameterHeader(
				(byte) 0), AccessReportTrigger.END_OF_ACCESSSPEC);

		AccessSpec aSpec = new AccessSpec(new TLVParameterHeader((byte) 0),
				11111111L, 7777, ProtocolId.EPC_GLOBAL_C1G2, true, 22222222L,
				aSSTrigger, ac);
		aSpec.setAccessReportSpec(ars);

		AddAccessSpec addAS = new AddAccessSpec(new MessageHeader((byte) 0,
				ProtocolVersion.LLRP_V1_1, 2014), aSpec);

		int messageLength = 10 + 87;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(addAS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (0 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.ADD_ACCESSSPEC.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2014);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 87);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7777);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ProtocolId.EPC_GLOBAL_C1G2.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 22222222L);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		byte[] place = new byte[7 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_COMMAND.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 59);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 34);
		place = new byte[34 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CLIENT_REQUEST_OP_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		place = new byte[6 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_READ.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 15);
		place = new byte[15 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_REPORT_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		data.rewind();

		// deserialize
		addAS = serializer.deserializeAddAccessSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = addAS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 0);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ADD_ACCESSSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		TLVParameterHeader header = addAS.getAccessSpec().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_SPEC);
		Assert.assertEquals(header.getParameterLength(), 87);
		Assert.assertEquals(addAS.getAccessSpec().getAccessSpecId(), 11111111L);
		Assert.assertEquals(addAS.getAccessSpec().getAntennaId(), 7777);
		Assert.assertEquals(addAS.getAccessSpec().getProtocolId(),
				ProtocolId.EPC_GLOBAL_C1G2);
		Assert.assertEquals(addAS.getAccessSpec().isCurrentState(), true);
		Assert.assertEquals(addAS.getAccessSpec().getRoSpecId(), 22222222L);

		header = addAS.getAccessSpec().getAccessSpecStopTrigger()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_SPEC_STOP_TRIGGER);
		Assert.assertEquals(header.getParameterLength(), 7);

		header = addAS.getAccessSpec().getAccessCommand().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_COMMAND);
		Assert.assertEquals(header.getParameterLength(), 4 + 34 + 6 + 15);

		header = addAS.getAccessSpec().getAccessCommand().getC1g2TagSpec()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_SPEC);
		Assert.assertEquals(header.getParameterLength(), 34);

		header = (TLVParameterHeader) addAS.getAccessSpec().getAccessCommand()
				.getOpSpecList().get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.CLIENT_REQUEST_OP_SPEC);
		Assert.assertEquals(header.getParameterLength(), 6);
		Assert.assertEquals(((ClientRequestOpSpec) addAS.getAccessSpec()
				.getAccessCommand().getOpSpecList().get(0)).getOpSpecID(), 5555);

		header = (TLVParameterHeader) addAS.getAccessSpec().getAccessCommand()
				.getOpSpecList().get(1).getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.C1G2_READ);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(((C1G2Read) addAS.getAccessSpec()
				.getAccessCommand().getOpSpecList().get(1)).getWordCount(),
				10000);

		header = addAS.getAccessSpec().getAccessReportSpec()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_REPORT_SPEC);
		Assert.assertEquals(header.getParameterLength(), 5);

		// getLength
		Assert.assertEquals(serializer.getLength(addAS), messageLength);
	}

	@Test
	public void getROSpecsResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// LLRPStatus 31
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		GetROSpecsResponse getROSRes = new GetROSpecsResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);

		// ROBoundarySpec 53
		PeriodicTriggerValue pTV = new PeriodicTriggerValue(
				new TLVParameterHeader((byte) 0), 111111111L, 111111112L);
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		pTV.setUtc(utc);
		ROSpecStartTrigger roSStartT = new ROSpecStartTrigger(
				new TLVParameterHeader((byte) 0), ROSpecStartTriggerType.GPI);
		roSStartT.setPeriodicTV(pTV);

		GPITriggerValue gpiTV1 = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		ROSpecStopTrigger roSST = new ROSpecStopTrigger(new TLVParameterHeader(
				(byte) 0), ROSpecStopTriggerType.GPI_WITH_TIMEOUT_VALUE,
				(long) 0);
		roSST.setGpiTriggerValue(gpiTV1);

		ROBoundarySpec robs = new ROBoundarySpec(new TLVParameterHeader(
				(byte) 0), roSStartT, roSST);

		// AISpec 58
		List<Integer> idList = new ArrayList<>();
		idList.add(1);
		idList.add(2);

		GPITriggerValue gpiTV2 = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		AISpecStopTrigger aiSST = new AISpecStopTrigger(new TLVParameterHeader(
				(byte) 0), AISpecStopTriggerType.GPI_WITH_TIMEOUT, (long) 0);
		aiSST.setGpiTV(gpiTV2);

		InventoryParameterSpec ips1 = new InventoryParameterSpec(
				new TLVParameterHeader((byte) 0), 4444,
				ProtocolId.EPC_GLOBAL_C1G2);
		InventoryParameterSpec ips2 = new InventoryParameterSpec(
				new TLVParameterHeader((byte) 0), 5555,
				ProtocolId.EPC_GLOBAL_C1G2);
		List<InventoryParameterSpec> inventList = new ArrayList<>();
		inventList.add(ips1);
		inventList.add(ips2);

		AISpec aiS = new AISpec(new TLVParameterHeader((byte) 0), idList,
				aiSST, inventList);

		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);
		aiS.setCustomList(cusList);

		// RFSurveySpec 41
		RFSurveySpecStopTrigger rfSSST = new RFSurveySpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				RFSurveySpecStopTriggerType.DURATION, 111111111L, (long) 20);
		RFSurveySpec rfSS = new RFSurveySpec(new TLVParameterHeader((byte) 0),
				6666, 33333333, 44444444, rfSSST);

		rfSS.setCusList(cusList);

		// LoopSpec 8
		LoopSpec ls = new LoopSpec(new TLVParameterHeader((byte) 0), 111111111L);

		// ROReportSpec 18
		C1G2EPCMemorySelector epcMS = new C1G2EPCMemorySelector(
				new TLVParameterHeader((byte) 0), true, true, true);
		List<C1G2EPCMemorySelector> selectorList = new ArrayList<>();
		selectorList.add(epcMS);

		TagReportContentSelector tagRS = new TagReportContentSelector(
				new TLVParameterHeader((byte) 0), true, false, true, false,
				true, false, true, false, true, false);
		tagRS.setC1g2EPCMemorySelectorList(selectorList);

		ROReportSpec roRS = new ROReportSpec(
				new TLVParameterHeader((byte) 0),
				ROReportTrigger.UPON_N_MILLISECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC,
				500, tagRS);

		// ROSpec 10
		List<Parameter> specList = new ArrayList<>();
		specList.add(aiS);
		specList.add(rfSS);
		specList.add(ls);
		ROSpec ros = new ROSpec(new TLVParameterHeader((byte) 0), (long) 9999,
				(short) 1, ROSpecCurrentState.ACTIVE, robs, specList);
		ros.setRoReportSpec(roRS);

		//
		List<ROSpec> roSpecList = new ArrayList<>();
		roSpecList.add(ros);
		getROSRes.setRoSpecList(roSpecList);
		int messageLength = (int) serializer.getLength(getROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.GET_ROSPECS_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		getROSRes = serializer.deserializeGetROSpecsResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_ROSPECS_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(getROSRes), 229);
	}

	@Test
	public void getROSpecs() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		GetROSpecs getROSs = new GetROSpecs(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014));
		int messageLength = (int) serializer.getLength(getROSs);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getROSs, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.GET_ROSPECS.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		getROSs = serializer.deserializeGetROSpecs(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getROSs.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_ROSPECS);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(getROSs), 10);
	}

	@Test
	public void disableROSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		DisableROSpecResponse disableROSRes = new DisableROSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(disableROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(disableROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DISABLE_ROSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		disableROSRes = serializer.deserializeDisableROSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = disableROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DISABLE_ROSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(disableROSRes), messageLength);
	}

	@Test
	public void disableROSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		DisableROSpec disableROS = new DisableROSpec(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(disableROS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(disableROS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DISABLE_ROSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		disableROS = serializer.deserializeDisableROSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = disableROS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DISABLE_ROSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(disableROS.getRoSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(disableROS), 14);
	}

	@Test
	public void enableROSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		EnableROSpecResponse enableROSRes = new EnableROSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(enableROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(enableROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ENABLE_ROSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		enableROSRes = serializer.deserializeEnableROSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = enableROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ENABLE_ROSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(enableROSRes), messageLength);
	}

	@Test
	public void enableROSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		EnableROSpec enableROS = new EnableROSpec(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(enableROS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(enableROS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ENABLE_ROSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		enableROS = serializer.deserializeEnableROSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = enableROS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ENABLE_ROSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(enableROS.getRoSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(enableROS), 14);
	}

	@Test
	public void stopROSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		StopROSpecResponse stopROSRes = new StopROSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(stopROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(stopROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.STOP_ROSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		stopROSRes = serializer.deserializeStopROSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = stopROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.STOP_ROSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(stopROSRes), messageLength);
	}

	@Test
	public void stopROSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		StopROSpec stopROS = new StopROSpec(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(stopROS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(stopROS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.STOP_ROSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		stopROS = serializer.deserializeStopROSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = stopROS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.STOP_ROSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(stopROS.getRoSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(stopROS), 14);
	}

	@Test
	public void startROSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		StartROSpecResponse startROSRes = new StartROSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(startROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(startROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.START_ROSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		startROSRes = serializer.deserializeStartROSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = startROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.START_ROSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(startROSRes), messageLength);
	}

	@Test
	public void startROSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		StartROSpec startROS = new StartROSpec(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(startROS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(startROS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.START_ROSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		startROS = serializer.deserializeStartROSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = startROS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.START_ROSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(startROS.getRoSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(startROS), 14);
	}

	@Test
	public void deleteROSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		DeleteROSpecResponse deleteROSRes = new DeleteROSpecResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(deleteROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(deleteROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DELETE_ROSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		deleteROSRes = serializer.deserializeDeleteROSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = deleteROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DELETE_ROSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(deleteROSRes), messageLength);
	}

	@Test
	public void deleteROSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		DeleteROSpec deleteROS = new DeleteROSpec(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_0_1, 2014), 4567);
		int messageLength = (int) serializer.getLength(deleteROS);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(deleteROS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.DELETE_ROSPEC.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.getInt(), 4567);
		data.rewind();

		// deserialize
		deleteROS = serializer.deserializeDeleteROSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = deleteROS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.DELETE_ROSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(deleteROS.getRoSpecID(), 4567);

		// getLength
		Assert.assertEquals(serializer.getLength(deleteROS), 14);
	}

	@Test
	public void addROSpecResponse() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		AddROSpecResponse addROSRes = new AddROSpecResponse(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014), llrpStatus);
		int messageLength = (int) serializer.getLength(addROSRes);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(addROSRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.ADD_ROSPEC_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		addROSRes = serializer.deserializeAddROSpecResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = addROSRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ADD_ROSPEC_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(addROSRes), messageLength);
	}

	@Test
	public void addROSpec() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// ROBoundarySpec 53
		PeriodicTriggerValue pTV = new PeriodicTriggerValue(
				new TLVParameterHeader((byte) 0), 111111111L, 111111112L);
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		pTV.setUtc(utc);
		ROSpecStartTrigger roSStartT = new ROSpecStartTrigger(
				new TLVParameterHeader((byte) 0), ROSpecStartTriggerType.GPI);
		roSStartT.setPeriodicTV(pTV);

		GPITriggerValue gpiTV1 = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		ROSpecStopTrigger roSST = new ROSpecStopTrigger(new TLVParameterHeader(
				(byte) 0), ROSpecStopTriggerType.GPI_WITH_TIMEOUT_VALUE,
				(long) 0);
		roSST.setGpiTriggerValue(gpiTV1);

		ROBoundarySpec robs = new ROBoundarySpec(new TLVParameterHeader(
				(byte) 0), roSStartT, roSST);

		// AISpec 58
		List<Integer> idList = new ArrayList<>();
		idList.add(1);
		idList.add(2);

		GPITriggerValue gpiTV2 = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		AISpecStopTrigger aiSST = new AISpecStopTrigger(new TLVParameterHeader(
				(byte) 0), AISpecStopTriggerType.GPI_WITH_TIMEOUT, (long) 0);
		aiSST.setGpiTV(gpiTV2);

		InventoryParameterSpec ips1 = new InventoryParameterSpec(
				new TLVParameterHeader((byte) 0), 4444,
				ProtocolId.EPC_GLOBAL_C1G2);
		InventoryParameterSpec ips2 = new InventoryParameterSpec(
				new TLVParameterHeader((byte) 0), 5555,
				ProtocolId.EPC_GLOBAL_C1G2);
		List<InventoryParameterSpec> inventList = new ArrayList<>();
		inventList.add(ips1);
		inventList.add(ips2);

		AISpec aiS = new AISpec(new TLVParameterHeader((byte) 0), idList,
				aiSST, inventList);

		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);
		aiS.setCustomList(cusList);

		// RFSurveySpec 41
		RFSurveySpecStopTrigger rfSSST = new RFSurveySpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				RFSurveySpecStopTriggerType.DURATION, 111111111L, (long) 20);
		RFSurveySpec rfSS = new RFSurveySpec(new TLVParameterHeader((byte) 0),
				6666, 33333333, 44444444, rfSSST);

		rfSS.setCusList(cusList);

		// LoopSpec 8
		LoopSpec ls = new LoopSpec(new TLVParameterHeader((byte) 0), 111111111L);

		// ROReportSpec 18
		C1G2EPCMemorySelector epcMS = new C1G2EPCMemorySelector(
				new TLVParameterHeader((byte) 0), true, true, true);
		List<C1G2EPCMemorySelector> selectorList = new ArrayList<>();
		selectorList.add(epcMS);

		TagReportContentSelector tagRS = new TagReportContentSelector(
				new TLVParameterHeader((byte) 0), true, false, true, false,
				true, false, true, false, true, false);
		tagRS.setC1g2EPCMemorySelectorList(selectorList);

		ROReportSpec roRS = new ROReportSpec(
				new TLVParameterHeader((byte) 0),
				ROReportTrigger.UPON_N_MILLISECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC,
				500, tagRS);

		// ROSpec
		List<Parameter> specList = new ArrayList<>();
		specList.add(aiS);
		specList.add(rfSS);
		specList.add(ls);
		ROSpec ros = new ROSpec(new TLVParameterHeader((byte) 0), (long) 9999,
				(short) 1, ROSpecCurrentState.ACTIVE, robs, specList);
		ros.setRoReportSpec(roRS);

		AddROSpec addROS = new AddROSpec(new MessageHeader((byte) 0,
				ProtocolVersion.LLRP_V1_1, 2014), ros);

		int messageLength = 10 + 188;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(addROS, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (0 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.ADD_ROSPEC.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2014);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 188);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 9999);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 1);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 2);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_BOUNDARY_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 53);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_START_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 29);
		byte[] place = new byte[29 - 4];
		data.get(place);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 20);
		place = new byte[20 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.AI_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 58);
		place = new byte[58 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 41);
		place = new byte[41 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.LOOP_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		place = new byte[8 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_REPORT_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);

		data.rewind();

		// deserialize
		addROS = serializer.deserializeAddROSpec(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = addROS.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 0);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.ADD_ROSPEC);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		TLVParameterHeader tlvParameterHeader = addROS.getRoSpec()
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(), 188);
		Assert.assertEquals(addROS.getRoSpec().getRoSpecID(), 9999);
		Assert.assertEquals(addROS.getRoSpec().getPriority(), 1);
		Assert.assertEquals(addROS.getRoSpec().getCurrentState(),
				ROSpecCurrentState.ACTIVE);

		TLVParameterHeader header = addROS.getRoSpec().getRoBoundarySpec()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RO_BOUNDARY_SPEC);
		Assert.assertEquals(header.getParameterLength(), 53);

		header = (TLVParameterHeader) ros.getSpecList().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.AI_SPEC);
		Assert.assertEquals(header.getParameterLength(), 58);
		Assert.assertEquals(((AISpec) ros.getSpecList().get(0))
				.getInventoryParameterList().get(1).getSpecID(), 5555);

		header = (TLVParameterHeader) ros.getSpecList().get(1)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RF_SURVEY_SPEC);
		Assert.assertEquals(header.getParameterLength(), 41);
		Assert.assertEquals(((RFSurveySpec) ros.getSpecList().get(1))
				.getCusList().get(0).getData(), dataArray);

		header = (TLVParameterHeader) ros.getSpecList().get(2)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.LOOP_SPEC);
		Assert.assertEquals(header.getParameterLength(), 8);

		header = addROS.getRoSpec().getRoReportSpec().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RO_REPORT_SPEC);
		Assert.assertEquals(header.getParameterLength(), 18);
		Assert.assertEquals(addROS.getRoSpec().getRoReportSpec()
				.getTagReportContentSelector().getC1g2EPCMemorySelectorList()
				.get(0).isEnableCRC(), true);

		// getLength
		Assert.assertEquals(serializer.getLength(addROS), messageLength);
	}

	@Test
	public void setReaderConfigResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		SetReaderConfigResponse setRCR = new SetReaderConfigResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(setRCR);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(setRCR, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.SET_READER_CONFIG_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		setRCR = serializer.deserializeSetReaderConfigResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = setRCR.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.SET_READER_CONFIG_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(setRCR), messageLength);
	}

	@Test
	public void setReaderConfig() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		SetReaderConfig setRC = new SetReaderConfig(new MessageHeader((byte) 0,
				ProtocolVersion.LLRP_V1_1, 2014), false);
		// AntennaProperties, length = 18
		AntennaProperties ap1 = new AntennaProperties(new TLVParameterHeader(
				(byte) 0), true, 1000, (short) 500);
		AntennaProperties ap2 = new AntennaProperties(new TLVParameterHeader(
				(byte) 0), true, 2000, (short) -500);
		List<AntennaProperties> apList = new ArrayList<>();
		apList.add(ap1);
		apList.add(ap2);
		setRC.setAntennaPropertiesList(apList);
		// AntennaConfiguration, length = 70
		RFReceiver rfr = new RFReceiver(new TLVParameterHeader((byte) 0), 10000);
		RFTransmitter rft = new RFTransmitter(new TLVParameterHeader((byte) 0),
				1000, 500, 1500);
		BitSet mask = new BitSet();// 1111000010101010
		mask.set(0);
		mask.set(1);
		mask.set(2);
		mask.set(3);
		mask.set(8);
		mask.set(10);
		mask.set(12);
		mask.set(14);
		C1G2TagInventoryMask tIMask = new C1G2TagInventoryMask(
				new TLVParameterHeader((byte) 0), (byte) 3, 100, mask);
		tIMask.setMaskBitCount(16);

		C1G2TagInventoryStateAwareFilterAction tISAFA = new C1G2TagInventoryStateAwareFilterAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0,
				(short) 165);

		C1G2TagInventoryStateUnawareFilterAction tISUFA = new C1G2TagInventoryStateUnawareFilterAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT);

		C1G2Filter filter = new C1G2Filter(new TLVParameterHeader((byte) 0),
				C1G2FilterTruncateAction.TRUNCATE, tIMask);
		filter.setC1g2TagInventoryStateAwareFilterAction(tISAFA);
		filter.setC1g2TagInventoryStateUnawareFilterAction(tISUFA);
		List<C1G2Filter> filterList = new ArrayList<>();
		filterList.add(filter);

		C1G2TagInventoryStateAwareSingulationAction iSASA = new C1G2TagInventoryStateAwareSingulationAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareSingulationActionI.STATE_B,
				C1G2TagInventoryStateAwareSingulationActionS.SL,
				C1G2TagInventoryStateAwareSingulationActionSAll.ALL);
		C1G2SingulationControl sControl = new C1G2SingulationControl(
				new TLVParameterHeader((byte) 0), (byte) 2, 100, 100000000L);
		sControl.setC1g2TagInventoryStateAwareSingulationAction(iSASA);

		C1G2InventoryCommand iCommand = new C1G2InventoryCommand(
				new TLVParameterHeader((byte) 0), true);
		iCommand.setC1g2FilterList(filterList);
		iCommand.setC1g2SingulationControl(sControl);
		List<C1G2InventoryCommand> invenCommandList = new ArrayList<>();
		invenCommandList.add(iCommand);
		int antennaID = 1111;
		AntennaConfiguration ac = new AntennaConfiguration(
				new TLVParameterHeader((byte) 0), antennaID);
		ac.setRfReceiver(rfr);
		ac.setRfTransmitter(rft);
		ac.setC1g2InventoryCommandList(invenCommandList);

		List<AntennaConfiguration> acList = new ArrayList<>();
		acList.add(ac);
		setRC.setAntennaConfigurationList(acList);

		// ReaderEventNotificationSpec, 18
		EventNotificationState eventNotificationState1 = new EventNotificationState(
				new TLVParameterHeader((byte) 0),
				EventNotificationStateEventType.AISPEC_EVENT, true);
		EventNotificationState eventNotificationState2 = new EventNotificationState(
				new TLVParameterHeader((byte) 0),
				EventNotificationStateEventType.AISPEC_EVENT_WITH_SINGULATION_DETAILS,
				false);
		List<EventNotificationState> eventNotificationStateList = new ArrayList<>();
		eventNotificationStateList.add(eventNotificationState1);
		eventNotificationStateList.add(eventNotificationState2);
		ReaderEventNotificationSpec rENS = new ReaderEventNotificationSpec(
				new TLVParameterHeader((byte) 0), eventNotificationStateList);
		setRC.setReaderEventNotificationSpec(rENS);

		// ROReportSpec, 18
		C1G2EPCMemorySelector epcMS = new C1G2EPCMemorySelector(
				new TLVParameterHeader((byte) 0), true, true, true);
		List<C1G2EPCMemorySelector> selectorList = new ArrayList<>();
		selectorList.add(epcMS);

		TagReportContentSelector tagRS = new TagReportContentSelector(
				new TLVParameterHeader((byte) 0), true, false, true, false,
				true, false, true, false, true, false);
		tagRS.setC1g2EPCMemorySelectorList(selectorList);

		ROReportSpec roRS = new ROReportSpec(
				new TLVParameterHeader((byte) 0),
				ROReportTrigger.UPON_N_MILLISECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC,
				500, tagRS);
		setRC.setRoReportSpec(roRS);

		// AccessReportSpec, 5
		AccessReportSpec ars = new AccessReportSpec(new TLVParameterHeader(
				(byte) 0), AccessReportTrigger.END_OF_ACCESSSPEC);
		setRC.setAccessReportSpec(ars);

		// KeepaliveSpec, 9
		KeepaliveSpec kas = new KeepaliveSpec(new TLVParameterHeader((byte) 0),
				KeepaliveSpecTriggerType.PERIODIC, 4000000000L);
		setRC.setKeepaliveSpec(kas);
		// GPIPortCurrentState, 8
		GPIPortCurrentState gpiCS = new GPIPortCurrentState(
				new TLVParameterHeader((byte) 0), 1111, false,
				GPIPortCurrentStateGPIState.UNKNOWN);
		List<GPIPortCurrentState> gpiList = new ArrayList<>();
		gpiList.add(gpiCS);
		setRC.setGpiPortCurrentStateList(gpiList);
		// GPOWriteData, 7
		GPOWriteData gpo = new GPOWriteData(new TLVParameterHeader((byte) 0),
				2222, false);
		List<GPOWriteData> gpoList = new ArrayList<>();
		gpoList.add(gpo);
		setRC.setGpoWriteDataList(gpoList);
		// EventsAndReports, 5
		EventsAndReports eAR = new EventsAndReports(new TLVParameterHeader(
				(byte) 0), true);
		setRC.setEventAndReports(eAR);
		// Custom, 35
		byte[] cusData3 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus3 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData3);
		byte[] cusData4 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus4 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData4);
		List<Custom> cusList2 = new ArrayList<>();
		cusList2.add(cus3);
		cusList2.add(cus4);
		setRC.setCustomList(cusList2);

		int messageLength = 193 + 11;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(setRC, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (0 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.SET_READER_CONFIG.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2014);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);

		Assert.assertEquals(
				DataTypeConverter.ushort(data.getShort()),
				(0 << 10)
						+ ParameterType.READER_EVENT_NOTIFICATION_SPEC
								.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		byte[] place = new byte[18 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ANTENNA_PROPERTIES.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ANTENNA_PROPERTIES.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ANTENNA_CONFIGURATION.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 22 + 48);
		place = new byte[22 + 48 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.RO_REPORT_SPEC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		place = new byte[18 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ACCESS_REPORT_SPEC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		place = new byte[5 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.KEEPALIVE_SPEC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.GPO_WRITE_DATA.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		place = new byte[7 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.GPI_PORT_CURRENT_STATE.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		place = new byte[8 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.EVENTS_AND_REPORTS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		place = new byte[5 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		place = new byte[17 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		place = new byte[18 - 4];

		data.rewind();

		// deserialize
		setRC = serializer.deserializeSetReaderConfig(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = setRC.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 0);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.SET_READER_CONFIG);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		Assert.assertEquals(setRC.getAntennaPropertiesList().get(0)
				.getParameterHeader().getParameterLength(), 9);
		Assert.assertEquals(setRC.getAntennaPropertiesList().get(1)
				.getAntennaGain(), -500);

		Assert.assertEquals(setRC.getAntennaConfigurationList().get(0)
				.getParameterHeader().getParameterLength(), 22 + 48);
		Assert.assertEquals(setRC.getAntennaConfigurationList().get(0)
				.getRfTransmitter().getChannelIndex(), 500);
		Assert.assertEquals(setRC.getAntennaConfigurationList().get(0)
				.getC1g2InventoryCommandList().get(0).getC1g2FilterList()
				.get(0).getC1g2TagInventoryMask().getTagMask(), mask);

		Assert.assertEquals(setRC.getReaderEventNotificationSpec()
				.getParameterHeader().getParameterLength(), 18);
		Assert.assertEquals(setRC.getReaderEventNotificationSpec()
				.getEventNotificationStateList().get(0).getEventType(),
				EventNotificationStateEventType.AISPEC_EVENT);

		Assert.assertEquals(setRC.getRoReportSpec().getParameterHeader()
				.getParameterLength(), 18);
		Assert.assertEquals(setRC.getRoReportSpec()
				.getTagReportContentSelector().getC1g2EPCMemorySelectorList()
				.get(0).isEnableCRC(), true);

		Assert.assertEquals(setRC.getAccessReportSpec().getParameterHeader()
				.getParameterLength(), 5);
		Assert.assertEquals(setRC.getAccessReportSpec()
				.getAccessReportTrigger(),
				AccessReportTrigger.END_OF_ACCESSSPEC);

		Assert.assertEquals(setRC.getKeepaliveSpec().getParameterHeader()
				.getParameterLength(), 9);
		Assert.assertEquals(setRC.getKeepaliveSpec().getTimeInterval(),
				4000000000L);

		Assert.assertEquals(setRC.getGpiPortCurrentStateList().get(0)
				.getParameterHeader().getParameterLength(), 8);
		Assert.assertEquals(setRC.getGpiPortCurrentStateList().get(0)
				.getState(), GPIPortCurrentStateGPIState.UNKNOWN);

		Assert.assertEquals(setRC.getGpoWriteDataList().get(0)
				.getParameterHeader().getParameterLength(), 7);
		Assert.assertEquals(setRC.getGpoWriteDataList().get(0).getGpoPortNum(),
				2222);

		Assert.assertEquals(setRC.getEventAndReports().getParameterHeader()
				.getParameterLength(), 5);
		Assert.assertEquals(setRC.getEventAndReports().getHold(), true);

		Assert.assertEquals(setRC.getCustomList().get(0).getParameterHeader()
				.getParameterLength(), 17);
		Assert.assertEquals(setRC.getCustomList().get(1).getParameterHeader()
				.getParameterLength(), 18);

		// getLength
		Assert.assertEquals(serializer.getLength(setRC), messageLength);
	}

	@Test
	public void getReaderConfigResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// LLRPStatus, length = 19
		String errorDesc = "hello world";
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 0), LLRPStatusCode.M_SUCCESS,
				errorDesc);
		GetReaderConfigResponse getRConR = new GetReaderConfigResponse(
				new MessageHeader((byte) 0, ProtocolVersion.LLRP_V1_1, 2014),
				llrpStatus);
		// Identification, length = 9
		byte[] readerID = { 12, 34 };
		Identification ident = new Identification(new TLVParameterHeader(
				(byte) 0), IdentificationIDType.EPC, readerID);
		getRConR.setIdentification(ident);
		// AntennaProperties, length = 18
		AntennaProperties ap1 = new AntennaProperties(new TLVParameterHeader(
				(byte) 0), true, 1000, (short) 500);
		AntennaProperties ap2 = new AntennaProperties(new TLVParameterHeader(
				(byte) 0), true, 2000, (short) -500);
		List<AntennaProperties> apList = new ArrayList<>();
		apList.add(ap1);
		apList.add(ap2);
		getRConR.setAntennaPropertiesList(apList);
		// AntennaConfiguration, length = 70
		RFReceiver rfr = new RFReceiver(new TLVParameterHeader((byte) 0), 10000);
		RFTransmitter rft = new RFTransmitter(new TLVParameterHeader((byte) 0),
				1000, 500, 1500);
		BitSet mask = new BitSet();// 1111000010101010
		mask.set(0);
		mask.set(1);
		mask.set(2);
		mask.set(3);
		mask.set(8);
		mask.set(10);
		mask.set(12);
		mask.set(14);
		C1G2TagInventoryMask tIMask = new C1G2TagInventoryMask(
				new TLVParameterHeader((byte) 0), (byte) 3, 100, mask);
		tIMask.setMaskBitCount(16);

		C1G2TagInventoryStateAwareFilterAction tISAFA = new C1G2TagInventoryStateAwareFilterAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0,
				(short) 165);

		C1G2TagInventoryStateUnawareFilterAction tISUFA = new C1G2TagInventoryStateUnawareFilterAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT);

		C1G2Filter filter = new C1G2Filter(new TLVParameterHeader((byte) 0),
				C1G2FilterTruncateAction.TRUNCATE, tIMask);
		filter.setC1g2TagInventoryStateAwareFilterAction(tISAFA);
		filter.setC1g2TagInventoryStateUnawareFilterAction(tISUFA);
		List<C1G2Filter> filterList = new ArrayList<>();
		filterList.add(filter);

		C1G2TagInventoryStateAwareSingulationAction iSASA = new C1G2TagInventoryStateAwareSingulationAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareSingulationActionI.STATE_B,
				C1G2TagInventoryStateAwareSingulationActionS.SL,
				C1G2TagInventoryStateAwareSingulationActionSAll.ALL);
		C1G2SingulationControl sControl = new C1G2SingulationControl(
				new TLVParameterHeader((byte) 0), (byte) 2, 100, 100000000L);
		sControl.setC1g2TagInventoryStateAwareSingulationAction(iSASA);

		C1G2InventoryCommand iCommand = new C1G2InventoryCommand(
				new TLVParameterHeader((byte) 0), true);
		iCommand.setC1g2FilterList(filterList);
		iCommand.setC1g2SingulationControl(sControl);
		List<C1G2InventoryCommand> invenCommandList = new ArrayList<>();
		invenCommandList.add(iCommand);
		int antennaID = 1111;
		AntennaConfiguration ac = new AntennaConfiguration(
				new TLVParameterHeader((byte) 0), antennaID);
		ac.setRfReceiver(rfr);
		ac.setRfTransmitter(rft);
		ac.setC1g2InventoryCommandList(invenCommandList);

		List<AntennaConfiguration> acList = new ArrayList<>();
		acList.add(ac);
		getRConR.setAntennaConfigurationList(acList);

		// ReaderEventNotificationSpec, 18
		EventNotificationState eventNotificationState1 = new EventNotificationState(
				new TLVParameterHeader((byte) 0),
				EventNotificationStateEventType.AISPEC_EVENT, true);
		EventNotificationState eventNotificationState2 = new EventNotificationState(
				new TLVParameterHeader((byte) 0),
				EventNotificationStateEventType.AISPEC_EVENT_WITH_SINGULATION_DETAILS,
				false);
		List<EventNotificationState> eventNotificationStateList = new ArrayList<>();
		eventNotificationStateList.add(eventNotificationState1);
		eventNotificationStateList.add(eventNotificationState2);
		ReaderEventNotificationSpec rENS = new ReaderEventNotificationSpec(
				new TLVParameterHeader((byte) 0), eventNotificationStateList);
		getRConR.setReaderEventNotificationSpec(rENS);

		// ROReportSpec, 18
		C1G2EPCMemorySelector epcMS = new C1G2EPCMemorySelector(
				new TLVParameterHeader((byte) 0), true, true, true);
		List<C1G2EPCMemorySelector> selectorList = new ArrayList<>();
		selectorList.add(epcMS);

		TagReportContentSelector tagRS = new TagReportContentSelector(
				new TLVParameterHeader((byte) 0), true, false, true, false,
				true, false, true, false, true, false);
		tagRS.setC1g2EPCMemorySelectorList(selectorList);

		ROReportSpec roRS = new ROReportSpec(new TLVParameterHeader((byte) 0),
				ROReportTrigger.UPON_N_MILLISECONDS_OR_END_OF_ROSPEC, 500,
				tagRS);
		getRConR.setRoReportSpec(roRS);

		// AccessReportSpec, 5
		AccessReportSpec ars = new AccessReportSpec(new TLVParameterHeader(
				(byte) 0), AccessReportTrigger.END_OF_ACCESSSPEC);
		getRConR.setAccessReportSpec(ars);
		// LLRPConfigurationStateValue, 8
		LLRPConfigurationStateValue llrpCSV = new LLRPConfigurationStateValue(
				new TLVParameterHeader((byte) 0), 222222222L);
		getRConR.setLlrpConfigurationStateValue(llrpCSV);
		// KeepaliveSpec, 9
		KeepaliveSpec kas = new KeepaliveSpec(new TLVParameterHeader((byte) 0),
				KeepaliveSpecTriggerType.PERIODIC, 4000000000L);
		getRConR.setKeepaliveSpec(kas);
		// GPIPortCurrentState, 8
		GPIPortCurrentState gpiCS = new GPIPortCurrentState(
				new TLVParameterHeader((byte) 0), 1111, false,
				GPIPortCurrentStateGPIState.UNKNOWN);
		List<GPIPortCurrentState> gpiList = new ArrayList<>();
		gpiList.add(gpiCS);
		getRConR.setGpiPortCurrentStateList(gpiList);
		// GPOWriteData, 7
		GPOWriteData gpo = new GPOWriteData(new TLVParameterHeader((byte) 0),
				2222, false);
		List<GPOWriteData> gpoList = new ArrayList<>();
		gpoList.add(gpo);
		getRConR.setGpoWriteDataList(gpoList);
		// EventsAndReports, 5
		EventsAndReports eAR = new EventsAndReports(new TLVParameterHeader(
				(byte) 0), true);
		getRConR.setEventAndReports(eAR);
		// Custom, 35
		byte[] cusData3 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus3 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData3);
		byte[] cusData4 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus4 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData4);
		List<Custom> cusList2 = new ArrayList<>();
		cusList2.add(cus3);
		cusList2.add(cus4);
		getRConR.setCustomList(cusList2);

		int messageLength = 229 + 10;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getRConR, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (0 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_READER_CONFIG_RESPONSE.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2014);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.LLRP_STATUS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 19);
		byte[] place = new byte[19 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.IDENTIFICATION.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ANTENNA_PROPERTIES.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ANTENNA_PROPERTIES.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ANTENNA_CONFIGURATION.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 22 + 48);
		place = new byte[22 + 48 - 4];
		data.get(place);

		Assert.assertEquals(
				DataTypeConverter.ushort(data.getShort()),
				(0 << 10)
						+ ParameterType.READER_EVENT_NOTIFICATION_SPEC
								.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		place = new byte[18 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.RO_REPORT_SPEC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		place = new byte[18 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.ACCESS_REPORT_SPEC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		place = new byte[5 - 4];
		data.get(place);

		Assert.assertEquals(
				DataTypeConverter.ushort(data.getShort()),
				(0 << 10)
						+ ParameterType.LLRP_CONFIGURATION_STATE_VALUE
								.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		place = new byte[8 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.KEEPALIVE_SPEC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		place = new byte[9 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.GPI_PORT_CURRENT_STATE.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		place = new byte[8 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.GPO_WRITE_DATA.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		place = new byte[7 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.EVENTS_AND_REPORTS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		place = new byte[5 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		place = new byte[17 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		place = new byte[18 - 4];

		data.rewind();

		// deserialize
		getRConR = serializer.deserializeGetReaderConfigResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getRConR.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 0);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_READER_CONFIG_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		Assert.assertEquals(getRConR.getStatus().getParameterHeader()
				.getParameterLength(), 19);
		Assert.assertEquals(getRConR.getStatus().getErrorDescription(),
				errorDesc);

		Assert.assertEquals(getRConR.getIdentification().getParameterHeader()
				.getParameterLength(), 9);
		Assert.assertArrayEquals(getRConR.getIdentification().getReaderID(),
				readerID);

		Assert.assertEquals(getRConR.getAntennaPropertiesList().get(0)
				.getParameterHeader().getParameterLength(), 9);
		Assert.assertEquals(getRConR.getAntennaPropertiesList().get(1)
				.getAntennaGain(), -500);

		Assert.assertEquals(getRConR.getAntennaConfigurationList().get(0)
				.getParameterHeader().getParameterLength(), 22 + 48);
		Assert.assertEquals(getRConR.getAntennaConfigurationList().get(0)
				.getRfTransmitter().getChannelIndex(), 500);
		Assert.assertEquals(getRConR.getAntennaConfigurationList().get(0)
				.getC1g2InventoryCommandList().get(0).getC1g2FilterList()
				.get(0).getC1g2TagInventoryMask().getTagMask(), mask);

		Assert.assertEquals(getRConR.getReaderEventNotificationSpec()
				.getParameterHeader().getParameterLength(), 18);
		Assert.assertEquals(getRConR.getReaderEventNotificationSpec()
				.getEventNotificationStateList().get(0).getEventType(),
				EventNotificationStateEventType.AISPEC_EVENT);

		Assert.assertEquals(getRConR.getRoReportSpec().getParameterHeader()
				.getParameterLength(), 18);
		Assert.assertEquals(getRConR.getRoReportSpec()
				.getTagReportContentSelector().getC1g2EPCMemorySelectorList()
				.get(0).isEnableCRC(), true);

		Assert.assertEquals(getRConR.getAccessReportSpec().getParameterHeader()
				.getParameterLength(), 5);
		Assert.assertEquals(getRConR.getAccessReportSpec()
				.getAccessReportTrigger(),
				AccessReportTrigger.END_OF_ACCESSSPEC);

		Assert.assertEquals(getRConR.getLlrpConfigurationStateValue()
				.getParameterHeader().getParameterLength(), 8);
		Assert.assertEquals(getRConR.getLlrpConfigurationStateValue()
				.getLlrpConfigStateValue(), 222222222L);

		Assert.assertEquals(getRConR.getKeepaliveSpec().getParameterHeader()
				.getParameterLength(), 9);
		Assert.assertEquals(getRConR.getKeepaliveSpec().getTimeInterval(),
				4000000000L);

		Assert.assertEquals(getRConR.getGpiPortCurrentStateList().get(0)
				.getParameterHeader().getParameterLength(), 8);
		Assert.assertEquals(getRConR.getGpiPortCurrentStateList().get(0)
				.getState(), GPIPortCurrentStateGPIState.UNKNOWN);

		Assert.assertEquals(getRConR.getGpoWriteDataList().get(0)
				.getParameterHeader().getParameterLength(), 7);
		Assert.assertEquals(getRConR.getGpoWriteDataList().get(0)
				.getGpoPortNum(), 2222);

		Assert.assertEquals(getRConR.getEventAndReports().getParameterHeader()
				.getParameterLength(), 5);
		Assert.assertEquals(getRConR.getEventAndReports().getHold(), true);

		Assert.assertEquals(getRConR.getCustomList().get(0)
				.getParameterHeader().getParameterLength(), 17);
		Assert.assertEquals(getRConR.getCustomList().get(1)
				.getParameterHeader().getParameterLength(), 18);

		// getLength
		Assert.assertEquals(serializer.getLength(getRConR), messageLength);
	}

	@Test
	public void getReaderConfig() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		GetReaderConfig getRCon = new GetReaderConfig(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_1, 4000000000L), 4444,
				GetReaderConfigRequestedData.ALL, 5555, 6666);
		byte[] cusData1 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus1 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData1);
		byte[] cusData2 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus2 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData2);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus1);
		cusList.add(cus2);
		getRCon.setCustomList(cusList);
		int messageLength = 17 + 35;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getRCon, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_READER_CONFIG.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000000L);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 4444);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5555);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6666);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		byte[] dst = new byte[5];
		data.get(dst, 0, 5);
		Assert.assertArrayEquals(dst, cusData1);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		dst = new byte[6];
		data.get(dst, 0, 6);
		Assert.assertArrayEquals(dst, cusData2);
		data.rewind();

		// deserialize
		getRCon = serializer.deserializeGetReaderConfig(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getRCon.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_READER_CONFIG);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(getRCon.getRequestedData(),
				GetReaderConfigRequestedData.ALL);
		Assert.assertEquals(getRCon.getAntennaID(), 4444);
		Assert.assertEquals(getRCon.getGpiPortNum(), 5555);
		Assert.assertEquals(getRCon.getGpoPortNum(), 6666);
		Assert.assertEquals(getRCon.getCustomList().get(0).getParameterHeader()
				.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(getRCon.getCustomList().get(0).getParameterHeader()
				.getParameterLength(), 17);
		Assert.assertEquals(getRCon.getCustomList().get(1).getParameterHeader()
				.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(getRCon.getCustomList().get(1).getParameterHeader()
				.getParameterLength(), 18);

		// getLength
		Assert.assertEquals(serializer.getLength(getRCon), messageLength);
	}

	@Test
	public void getReaderCapabilitiesResponse_OnlyStatus()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		String errorDesc = "hello world";
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 0), LLRPStatusCode.M_SUCCESS,
				errorDesc);

		GetReaderCapabilitiesResponse getRCapRes = new GetReaderCapabilitiesResponse(
				new MessageHeader((byte) 0, ProtocolVersion.LLRP_V1_1, 2014),
				llrpStatus);

		int messageLength = 29;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getRCapRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (0 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_READER_CAPABILITIES_RESPONSE.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2014);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.LLRP_STATUS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 19);

		data.rewind();

		// deserialize
		getRCapRes = serializer.deserializeGetReaderCapabilitiesResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getRCapRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 0);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_READER_CAPABILITIES_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(getRCapRes.getStatus().getErrorDescription(),
				errorDesc);

		// getLength
		Assert.assertEquals(serializer.getLength(getRCapRes), messageLength);
	}

	@Test
	public void getReaderCapabilitiesResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		String errorDesc = "hello world";
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 0), LLRPStatusCode.M_SUCCESS,
				errorDesc);

		long deviceManufacturerName = 123456789;
		long modelName = 987654321;
		String firmwareVersion = "APP_02-08-00_03-02-00_01-03-02";
		int maximumNumberOfAntennasSupported = 555;
		boolean canSetAntennaProperties = false;
		boolean hasUTCClockCapability = true;
		GPIOCapabilities gpioSupport = new GPIOCapabilities(
				new TLVParameterHeader((byte) 0), 20, 20);

		List<ReceiveSensitivityTabelEntry> receiveSensitivityTable = new ArrayList<>();
		ReceiveSensitivityTabelEntry rste1 = new ReceiveSensitivityTabelEntry(
				new TLVParameterHeader((byte) 0), 1000, 1000);
		ReceiveSensitivityTabelEntry rste2 = new ReceiveSensitivityTabelEntry(
				new TLVParameterHeader((byte) 0), 2000, 2000);
		receiveSensitivityTable.add(rste1);
		receiveSensitivityTable.add(rste2);

		List<PerAntennaAirProtocol> airProtocolSupportedPerAntenna = new ArrayList<>();
		List<ProtocolId> pID1 = new ArrayList<>();
		pID1.add(ProtocolId.EPC_GLOBAL_C1G2);
		pID1.add(ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		PerAntennaAirProtocol paap1 = new PerAntennaAirProtocol(
				new TLVParameterHeader((byte) 0), 500, pID1);

		List<ProtocolId> pID2 = new ArrayList<>();
		pID2.add(ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		pID2.add(ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		PerAntennaAirProtocol paap2 = new PerAntennaAirProtocol(
				new TLVParameterHeader((byte) 0), 600, pID2);

		airProtocolSupportedPerAntenna.add(paap1);
		airProtocolSupportedPerAntenna.add(paap2);

		GeneralDeviceCapabilities gdc = new GeneralDeviceCapabilities(
				new TLVParameterHeader((byte) 0), deviceManufacturerName,
				modelName, firmwareVersion, maximumNumberOfAntennasSupported,
				canSetAntennaProperties, receiveSensitivityTable,
				airProtocolSupportedPerAntenna, gpioSupport,
				hasUTCClockCapability);

		byte[] cusData1 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus1 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData1);
		byte[] cusData2 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus2 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData2);
		List<Custom> cusList1 = new ArrayList<>();
		cusList1.add(cus1);
		cusList1.add(cus2);
		RegulatoryCapabilities regCap = new RegulatoryCapabilities(
				new TLVParameterHeader((byte) 0),
				CountryCodes.CountryCode.AFGHANISTAN,
				CommunicationsStandard.AUSTRALIA_LIPD_1W);
		regCap.setCustomExtensionPoint(cusList1);

		byte[] cusData3 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus3 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData3);
		byte[] cusData4 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus4 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData4);
		List<Custom> cusList2 = new ArrayList<>();
		cusList2.add(cus3);
		cusList2.add(cus4);

		GetReaderCapabilitiesResponse getRCapRes = new GetReaderCapabilitiesResponse(
				new MessageHeader((byte) 0, ProtocolVersion.LLRP_V1_1, 2014),
				llrpStatus, gdc);
		getRCapRes.setRegulatoryCap(regCap);
		getRCapRes.setCustomExtensionPoint(cusList2);

		int messageLength = 199;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getRCapRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (0 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_READER_CAPABILITIES_RESPONSE.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2014);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.LLRP_STATUS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 19);
		byte[] place = new byte[15];
		data.get(place);

		Assert.assertEquals(
				DataTypeConverter.ushort(data.getShort()),
				(0 << 10)
						+ ParameterType.GENERAL_DEVICE_CAPABILITIES.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 92);
		place = new byte[92 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.REGULATORY_CAPABILITIES.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 43);
		place = new byte[43 - 4];
		data.get(place);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		place = new byte[17 - 4];
		data.get(place);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		data.rewind();

		// deserialize
		getRCapRes = serializer.deserializeGetReaderCapabilitiesResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getRCapRes.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 0);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_READER_CAPABILITIES_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(getRCapRes.getRegulatoryCap().getParameterHeader()
				.getParameterLength(), 43);
		Assert.assertEquals(getRCapRes.getStatus().getParameterHeader()
				.getParameterLength(), 19);
		Assert.assertEquals(getRCapRes.getGeneralDeviceCap()
				.getParameterHeader().getParameterLength(), 92);
		Assert.assertEquals(getRCapRes.getCustomExtensionPoint().get(0)
				.getParameterHeader().getParameterLength(), 17);
		Assert.assertEquals(getRCapRes.getCustomExtensionPoint().get(1)
				.getParameterHeader().getParameterLength(), 18);

		// getLength
		Assert.assertEquals(serializer.getLength(getRCapRes), messageLength);
	}

	@Test
	public void getReaderCapabilities() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		GetReaderCapabilities getRCap = new GetReaderCapabilities(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_1,
						4000000000L), GetReaderCapabilitiesRequestedData.ALL);
		byte[] cusData1 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus1 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData1);
		byte[] cusData2 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus2 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData2);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus1);
		cusList.add(cus2);
		getRCap.setCustomExtensionPoint(cusList);
		int messageLength = 46;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(getRCap, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_READER_CAPABILITIES.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				messageLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000000L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		byte[] dst = new byte[5];
		data.get(dst, 0, 5);
		Assert.assertArrayEquals(dst, cusData1);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				(0 << 10) + ParameterType.CUSTOM.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		dst = new byte[6];
		data.get(dst, 0, 6);
		Assert.assertArrayEquals(dst, cusData2);
		data.rewind();

		// deserialize
		getRCap = serializer.deserializeGetReaderCapabilities(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = getRCap.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_READER_CAPABILITIES);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(getRCap.getRequestedData().getValue(), 0);
		Assert.assertEquals(getRCap.getCustomExtensionPoint().get(0)
				.getParameterHeader().getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(getRCap.getCustomExtensionPoint().get(0)
				.getParameterHeader().getParameterLength(), 17);
		Assert.assertEquals(getRCap.getCustomExtensionPoint().get(1)
				.getParameterHeader().getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(getRCap.getCustomExtensionPoint().get(1)
				.getParameterHeader().getParameterLength(), 18);
		// getLength
		Assert.assertEquals(serializer.getLength(getRCap), messageLength);
	}

	@Test
	public void getSupportedVersion() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		GetSupportedVersion ren = new GetSupportedVersion(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_1, 2014));
		int messageLength = 10;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_SUPPORTED_VERSION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		ren = serializer.deserializeGetSupportedVersion(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ren.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_SUPPORTED_VERSION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ren), messageLength);
	}

	@Test
	public void getSupportedVersionResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		ProtocolVersion current = ProtocolVersion.LLRP_V1_1;
		ProtocolVersion supported = ProtocolVersion.LLRP_V1_0_1;

		GetSupportedVersionResponse ren = new GetSupportedVersionResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_1, 2014),
				current, supported, llrpStatus);
		int messageLength = (int) serializer.getLength(ren);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_SUPPORTED_VERSION_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		ren = serializer.deserializeGetSupportedVersionResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ren.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_SUPPORTED_VERSION_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ren), messageLength);
	}

	/**
	 * Create a byte buffer with a <code>GetSupportedVersionResponse</code>
	 * message and replace the parameter type <code>LLRPStatus</code> with
	 * another type.
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalidParameterTypeException}
	 * </ul>
	 * </p>
	 * 
	 * @throws InvalidMessageTypeException
	 */
	@Test
	public void getSupportedVersionResponseError()
			throws InvalidProtocolVersionException, InvalidMessageTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		ProtocolVersion current = ProtocolVersion.LLRP_V1_1;
		ProtocolVersion supported = ProtocolVersion.LLRP_V1_0_1;

		GetSupportedVersionResponse ren = new GetSupportedVersionResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_1, 2014),
				current, supported, llrpStatus);
		int messageLength = (int) serializer.getLength(ren);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();

		// change parameter type 'LLRPStatus' to 'FieldError'
		data.position(ByteBufferSerializer.MESSAGE_HEADER_LENGTH + 2);
		data.putShort((short) ((2 << 10) + ParameterType.FIELD_ERROR.getValue()));
		data.rewind();

		// deserialize
		try {
			ren = serializer.deserializeGetSupportedVersionResponse(
					serializer.deserializeMessageHeader(data), data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type FIELD_ERROR"));
		}
	}

	@Test
	public void setProtocolVersion() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ProtocolVersion version = ProtocolVersion.LLRP_V1_0_1;
		SetProtocolVersion ren = new SetProtocolVersion(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_1, 2014), version);
		int messageLength = 11;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.SET_PROTOCOL_VERSION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		Assert.assertEquals(data.get(), version.getValue());
		data.rewind();

		// deserialize
		ren = serializer.deserializeSetProtocolVersion(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ren.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.SET_PROTOCOL_VERSION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ren), messageLength);
	}

	@Test
	public void setProtocolVersionResponse()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		SetProtocolVersionResponse ren = new SetProtocolVersionResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(ren);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.SET_PROTOCOL_VERSION_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		ren = serializer.deserializeSetProtocolVersionResponse(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ren.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.SET_PROTOCOL_VERSION_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ren), messageLength);
	}

	/**
	 * Create a byte buffer with a <code>SetProtocolVersionResponse</code>
	 * message and replace the parameter type <code>LLRPStatus</code> with
	 * another type.
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalidParameterTypeException}
	 * </ul>
	 * </p>
	 * 
	 * @throws InvalidMessageTypeException
	 */
	@Test
	public void setProtocolVersionResponseError()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		FieldError fieldErr = new FieldError(new TLVParameterHeader((byte) 2),
				1000, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setFieldError(fieldErr);
		ParameterError paraErr = new ParameterError(new TLVParameterHeader(
				(byte) 1), 77, LLRPStatusCode.M_SUCCESS);
		llrpStatus.setParameterError(paraErr);

		SetProtocolVersionResponse ren = new SetProtocolVersionResponse(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 2014),
				llrpStatus);
		int messageLength = (int) serializer.getLength(ren);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		// change parameter type 'LLRPStatus' to 'ParameterError'
		data.position(ByteBufferSerializer.MESSAGE_HEADER_LENGTH);
		data.putShort((short) ((1 << 10) + ParameterType.PARAMETER_ERROR
				.getValue()));
		data.rewind();

		// deserialize
		try {
			ren = serializer.deserializeSetProtocolVersionResponse(
					serializer.deserializeMessageHeader(data), data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type PARAMETER_ERROR"));
		}
	}

	@Test
	public void readerEventNotification_MoreParameter()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		// Uptime 12
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 18),
				new BigInteger("300"));

		ReaderEventNotificationData rend = new ReaderEventNotificationData(
				new TLVParameterHeader((byte) 17), uptime);

		// HoppingEvent 8
		HoppingEvent hE = new HoppingEvent(new TLVParameterHeader((byte) 0), 9,
				10);
		rend.setHoppingEvent(hE);

		// ReaderExceptionEvent 34
		String errorDesc = "Parameter Test";
		byte[] error = errorDesc.getBytes(StandardCharsets.UTF_8);
		ReaderExceptionEvent ree = new ReaderExceptionEvent(
				new TLVParameterHeader((byte) 0), errorDesc);
		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);
		ree.setCustomList(cusList);
		rend.setReaderExceptionEvent(ree);

		// Custom 14
		byte[] dataArrayBig = { 90, 80 };
		Custom cusBig = new Custom(new TLVParameterHeader((byte) 0), 111, 222,
				dataArrayBig);
		List<Custom> cusBigList = new ArrayList<>();
		cusBigList.add(cusBig);
		rend.setCustomList(cusBigList);

		ReaderEventNotification ren = new ReaderEventNotification(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 3),
				rend);
		int messageLength = (int) serializer.getLength(ren);
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.READER_EVENT_NOTIFICATION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 3);

		Assert.assertEquals(data.getShort(), (17 << 10)
				+ ParameterType.READER_EVENT_NOTIFICATION_DATA.getValue());
		Assert.assertEquals(data.getShort(), 72);
		data.rewind();

		// deserialize
		ren = serializer.deserializeReaderEventNotification(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ren.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.READER_EVENT_NOTIFICATION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(ren.getReaderEventNotificationData()
				.getReaderExceptionEvent().getStringByteCount(), error.length);
		Assert.assertArrayEquals(ren.getReaderEventNotificationData()
				.getCustomList().get(0).getData(), dataArrayBig);

		// getLength
		Assert.assertEquals(serializer.getLength(ren), 72 + 10);
	}

	@Test
	public void readerEventNotification()
			throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 18),
				new BigInteger("300"));
		ConnectionAttemptEvent cae = new ConnectionAttemptEvent(
				new TLVParameterHeader((byte) 19),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON);
		ConnectionCloseEvent cce = new ConnectionCloseEvent(
				new TLVParameterHeader((byte) 13));
		ReaderEventNotificationData rend = new ReaderEventNotificationData(
				new TLVParameterHeader((byte) 17), uptime);
		rend.setConnectionAttemptEvent(cae);
		rend.setConnectionCloseEvent(cce);
		ReaderEventNotification ren = new ReaderEventNotification(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 3),
				rend);
		int messageLength = 36;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.READER_EVENT_NOTIFICATION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 3);

		Assert.assertEquals(data.getShort(), (17 << 10)
				+ ParameterType.READER_EVENT_NOTIFICATION_DATA.getValue());
		Assert.assertEquals(data.getShort(), 26);
		data.rewind();

		// deserialize
		ren = serializer.deserializeReaderEventNotification(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = ren.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.READER_EVENT_NOTIFICATION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(ren), messageLength);
	}

	/**
	 * Create a byte buffer with a <code>ReaderEventNotification</code> message
	 * and replace the parameter type <code>ReaderEventNotificationData</code>
	 * with another type.
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalidParameterTypeException}
	 * </ul>
	 * </p>
	 * 
	 * @throws InvalidMessageTypeException
	 */
	@Test
	public void readerEventNotificationError()
			throws InvalidProtocolVersionException, InvalidMessageTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 18),
				new BigInteger("300"));
		ReaderEventNotificationData rend = new ReaderEventNotificationData(
				new TLVParameterHeader((byte) 17), uptime);
		ReaderEventNotification ren = new ReaderEventNotification(
				new MessageHeader((byte) 1, ProtocolVersion.LLRP_V1_0_1, 3),
				rend);
		int messageLength = 32;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(ren, data);
		data.flip();
		// change parameter type 'ReaderEventNotificationData' to 'Uptime'
		data.position(ByteBufferSerializer.MESSAGE_HEADER_LENGTH);
		data.putShort((short) ((17 << 10) + ParameterType.UPTIME.getValue()));
		data.rewind();

		// deserialize
		try {
			ren = serializer.deserializeReaderEventNotification(
					serializer.deserializeMessageHeader(data), data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type UPTIME"));
		}
	}

	@Test
	public void message() throws InvalidProtocolVersionException,
			InvalidMessageTypeException, InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// ----------ReaderEventNotification----------//
		// serialize
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 18),
				new BigInteger("300"));
		ConnectionAttemptEvent cae = new ConnectionAttemptEvent(
				new TLVParameterHeader((byte) 19),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON);
		ReaderEventNotificationData rend = new ReaderEventNotificationData(
				new TLVParameterHeader((byte) 17), uptime);
		rend.setConnectionAttemptEvent(cae);
		Message message = new ReaderEventNotification(new MessageHeader(
				(byte) 1, ProtocolVersion.LLRP_V1_0_1, 3), rend);
		int messageLength = 32;
		ByteBuffer data = ByteBuffer.allocate(messageLength);
		serializer.serialize(message, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_0_1.getValue() << 10)
				+ MessageType.READER_EVENT_NOTIFICATION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 3);

		Assert.assertEquals(data.getShort(), (17 << 10)
				+ ParameterType.READER_EVENT_NOTIFICATION_DATA.getValue());
		Assert.assertEquals(data.getShort(), 22);
		data.rewind();

		// deserialize
		message = serializer.deserializeMessage(
				serializer.deserializeMessageHeader(data), data);
		MessageHeader messageHeader = message.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.READER_EVENT_NOTIFICATION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(message), messageLength);

		// ----------GetSupportedVersion----------//
		// serialize
		message = new GetSupportedVersion(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_1, 2014));
		messageLength = 10;
		data = ByteBuffer.allocate(messageLength);
		serializer.serialize(message, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_SUPPORTED_VERSION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		message = serializer.deserializeMessage(
				serializer.deserializeMessageHeader(data), data);
		messageHeader = message.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_SUPPORTED_VERSION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(message), messageLength);

		// ----------GetSupportedVersionResponse----------//
		// serialize
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 1), LLRPStatusCode.M_SUCCESS,
				"harting");
		ProtocolVersion current = ProtocolVersion.LLRP_V1_1;
		ProtocolVersion supported = ProtocolVersion.LLRP_V1_0_1;
		message = new GetSupportedVersionResponse(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_1, 2014), current, supported,
				llrpStatus);
		messageLength = (int) serializer.getLength(message);
		data = ByteBuffer.allocate(messageLength);
		serializer.serialize(message, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.GET_SUPPORTED_VERSION_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		message = serializer.deserializeMessage(
				serializer.deserializeMessageHeader(data), data);
		messageHeader = message.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.GET_SUPPORTED_VERSION_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(message), messageLength);

		// ----------SetProtocolVersion----------//
		// serialize
		message = new SetProtocolVersion(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_1, 2014), ProtocolVersion.LLRP_V1_0_1);
		messageLength = 11;
		data = ByteBuffer.allocate(messageLength);
		serializer.serialize(message, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.SET_PROTOCOL_VERSION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		message = serializer.deserializeMessage(
				serializer.deserializeMessageHeader(data), data);
		messageHeader = message.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.SET_PROTOCOL_VERSION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(message), messageLength);

		// ----------SetProtocolVersionResponse----------//
		// serialize
		message = new SetProtocolVersionResponse(new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_1, 2014), llrpStatus);
		messageLength = (int) serializer.getLength(message);
		data = ByteBuffer.allocate(messageLength);
		serializer.serialize(message, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.SET_PROTOCOL_VERSION_RESPONSE.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 2014);
		data.rewind();

		// deserialize
		message = serializer.deserializeMessage(
				serializer.deserializeMessageHeader(data), data);
		messageHeader = message.getMessageHeader();
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.SET_PROTOCOL_VERSION_RESPONSE);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);

		// getLength
		Assert.assertEquals(serializer.getLength(message), messageLength);
	}

	@Test
	public void messageHeader() throws InvalidProtocolVersionException,
			InvalidMessageTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		long messageLength = 4;
		MessageHeader messageHeader = new MessageHeader((byte) 1,
				ProtocolVersion.LLRP_V1_1, 8);
		messageHeader.setMessageType(MessageType.READER_EVENT_NOTIFICATION);
		messageHeader.setMessageLength(messageLength);
		ByteBuffer data = ByteBuffer
				.allocate(ByteBufferSerializer.MESSAGE_HEADER_LENGTH);
		serializer.serialize(messageHeader, data);
		data.flip();
		Assert.assertEquals(data.getShort(), (1 << 13)
				+ (ProtocolVersion.LLRP_V1_1.getValue() << 10)
				+ MessageType.READER_EVENT_NOTIFICATION.getValue());
		Assert.assertEquals(data.getInt(), messageLength);
		Assert.assertEquals(data.getInt(), 8);
		data.rewind();

		// deserialize
		messageHeader = serializer.deserializeMessageHeader(data);
		Assert.assertEquals(messageHeader.getReserved(), 1);
		Assert.assertEquals(messageHeader.getVersion(),
				ProtocolVersion.LLRP_V1_1);
		Assert.assertEquals(messageHeader.getMessageType(),
				MessageType.READER_EVENT_NOTIFICATION);
		Assert.assertEquals(messageHeader.getMessageLength(), messageLength);
		Assert.assertEquals(messageHeader.getId(), 8);
	}

	/**
	 * Create a byte buffer with a message header and replace the protocol
	 * version with an invalid version.
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalideProtocolVersionException}
	 * </ul>
	 * </p>
	 * Create a byte buffer with a message header and replace the message type
	 * with an invalid type.
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalideMessageTypeException}
	 * </ul>
	 * </p>
	 */
	@Test
	public void messageHeaderError() {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		MessageHeader messageHeader = new MessageHeader((byte) 0,
				ProtocolVersion.LLRP_V1_0_1, 8);
		messageHeader.setMessageType(MessageType.READER_EVENT_NOTIFICATION);
		messageHeader.setMessageLength(8);
		ByteBuffer data = ByteBuffer.allocate(10);
		serializer.serialize(messageHeader, data);
		data.flip();
		// change protocol version to 7
		data.putShort((short) ((7 << 10) + MessageType.READER_EVENT_NOTIFICATION
				.getValue()));
		data.rewind();

		// deserialize
		try {
			messageHeader = serializer.deserializeMessageHeader(data);
			Assert.fail();
		} catch (InvalidMessageTypeException e) {
			Assert.fail();
		} catch (InvalidProtocolVersionException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid protocol version 7"));
		}

		// serialize
		data.clear();
		messageHeader = new MessageHeader((byte) 0,
				ProtocolVersion.LLRP_V1_0_1, 8);
		messageHeader.setMessageType(MessageType.READER_EVENT_NOTIFICATION);
		messageHeader.setMessageLength(8);
		serializer.serialize(messageHeader, data);
		data.flip();
		// change message type to 1000
		data.putShort((short) ((ProtocolVersion.LLRP_V1_0_1.getValue() << 10) + 1000));
		data.rewind();

		// deserialize
		try {
			messageHeader = serializer.deserializeMessageHeader(data);
			Assert.fail();
		} catch (InvalidMessageTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid message type 1000"));
		} catch (InvalidProtocolVersionException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
