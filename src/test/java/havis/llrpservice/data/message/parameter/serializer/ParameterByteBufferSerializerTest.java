package havis.llrpservice.data.message.parameter.serializer;

import havis.llrpservice.data.DataTypeConverter;
import havis.llrpservice.data.message.parameter.*;
import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ParameterByteBufferSerializerTest {

	public void readerEventNotificationData_full()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ReaderEventNotificationData rend = null;

		// Uptime 12
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 0),
				new BigInteger("300"));
		rend = new ReaderEventNotificationData(
				new TLVParameterHeader((byte) 0), uptime);

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

		int parameterLength = serializer.getLength(rend);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rend, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.READER_EVENT_NOTIFICATION_DATA
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UPTIME.getValue()));
		Assert.assertEquals(data.getShort(), 12);
		data.getLong();

		data.rewind();

		// deserialize
		rend = serializer.deserializeReaderEventNotificationData(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rend.getParameterHeader();

		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.READER_EVENT_NOTIFICATION_DATA);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		TLVParameterHeader header = rend.getReaderExceptionEvent()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterLength(), 34);
		Assert.assertEquals(
				rend.getReaderExceptionEvent().getStringByteCount(),
				error.length);
		Assert.assertEquals(rend.getReaderExceptionEvent().getCustomList()
				.size(), 1);

		Assert.assertEquals(rend.getCustomList().get(0).getData(), dataArrayBig);

		// getLength
		Assert.assertEquals(serializer.getLength(rend), 4 + 12 + 8 + 34 + 14);
	}

	@Test
	public void hoppingEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		HoppingEvent hE = new HoppingEvent(new TLVParameterHeader((byte) 0), 9,
				10);

		int parameterLength = serializer.getLength(hE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(hE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.HOPPING_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		data.rewind();

		// deserialize
		hE = serializer.deserializeHoppingEvent((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = hE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.HOPPING_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(hE.getHopTableID(), 9);
		Assert.assertEquals(hE.getNextChannelIndex(), 10);

		// getLength
		Assert.assertEquals(serializer.getLength(hE), 8);
	}

	@Test
	public void gpiEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		GPIEvent gpiE = new GPIEvent(new TLVParameterHeader((byte) 0), 9, true);

		int parameterLength = serializer.getLength(gpiE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(gpiE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPI_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		data.rewind();

		// deserialize
		gpiE = serializer.deserializeGPIEvent((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = gpiE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.GPI_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(gpiE.getGpiPortNumber(), 9);
		Assert.assertEquals(gpiE.isState(), true);

		// getLength
		Assert.assertEquals(serializer.getLength(gpiE), 7);
	}

	@Test
	public void roSpecEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		ROSpecEvent roSE = new ROSpecEvent(new TLVParameterHeader((byte) 0),
				ROSpecEventType.START_OF_ROSPEC, (long) 9999, (long) 999);

		int parameterLength = serializer.getLength(roSE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(roSE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ROSpecEventType.START_OF_ROSPEC.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 9999);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 999);
		data.rewind();

		// deserialize
		roSE = serializer.deserializeROSpecEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = roSE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_SPEC_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(roSE.getEventType(),
				ROSpecEventType.START_OF_ROSPEC);
		Assert.assertEquals(roSE.getRoSpecID(), 9999);
		Assert.assertEquals(roSE.getPreemptingROSpecID(), 999);

		// getLength
		Assert.assertEquals(serializer.getLength(roSE), 13);
	}

	@Test
	public void reportBufferLevelWarningEvent()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ReportBufferLevelWarningEvent eW = new ReportBufferLevelWarningEvent(
				new TLVParameterHeader((byte) 0), (short) 234);
		int parameterLength = serializer.getLength(eW);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(eW, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.REPORT_BUFFER_LEVEL_WARNING_EVENT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 234);
		data.rewind();

		// deserialize
		eW = serializer.deserializeReportBufferLevelWarningEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = eW.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.REPORT_BUFFER_LEVEL_WARNING_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(eW.getReportBufferPercentageFull(), 234);

		// getLength
		Assert.assertEquals(serializer.getLength(eW), 5);
	}

	@Test
	public void reportBufferOverflowErrorEvent()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ReportBufferOverflowErrorEvent eE = new ReportBufferOverflowErrorEvent(
				new TLVParameterHeader((byte) 25));
		int parameterLength = 4;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(eE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((25 << 10) + ParameterType.REPORT_BUFFER_OVERFLOW_ERROR_EVENT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		data.rewind();

		// deserialize
		eE = serializer.deserializeReportBufferOverflowErrorEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = eE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 25);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.REPORT_BUFFER_OVERFLOW_ERROR_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		// getLength
		Assert.assertEquals(serializer.getLength(eE), parameterLength);
	}

	@Test
	public void readerExceptionEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize

		String errorDesc = "Parameter Test";
		byte[] error = errorDesc.getBytes(StandardCharsets.UTF_8);
		ReaderExceptionEvent ree = new ReaderExceptionEvent(
				new TLVParameterHeader((byte) 0), errorDesc);

		SpecIndex si = new SpecIndex(new TVParameterHeader(), 1234);
		AntennaId antID = new AntennaId(new TVParameterHeader(), 1234);
		OpSpecID opSID = new OpSpecID(new TVParameterHeader(), 6666);
		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);

		ree.setSpecIndex(si);
		ree.setAntennaId(antID);
		ree.setOpSpecID(opSID);
		ree.setCustomList(cusList);

		int parameterLength = serializer.getLength(ree);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ree, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.READER_EXCEPTION_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				error.length);
		byte[] dst = new byte[error.length];
		data.get(dst, 0, error.length);
		Assert.assertArrayEquals(dst, error);

		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.SPEC_INDEX.getValue());
		byte[] place = new byte[3 - 1];
		data.get(place);

		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.ANTENNA_ID.getValue());
		place = new byte[3 - 1];
		data.get(place);

		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.OP_SPEC_ID.getValue());
		place = new byte[3 - 1];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 14);
		data.rewind();

		// deserialize
		ree = serializer.deserializeReaderExceptionEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ree.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.READER_EXCEPTION_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		Assert.assertEquals(ree.getStringByteCount(), error.length);
		Assert.assertEquals(ree.getStringMessage(), errorDesc);
		Assert.assertEquals(ree.getAntennaId().getAntennaId(), 1234);
		Assert.assertEquals(ree.getCustomList().get(0).getParameterHeader()
				.getParameterType(), ParameterType.CUSTOM);
		// getLength 43
		Assert.assertEquals(serializer.getLength(ree), 29 + errorDesc.length());
	}

	@Test
	public void opSpecID() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		OpSpecID opSID = new OpSpecID(new TVParameterHeader(), 6666);
		int parameterLength = serializer.getLength(opSID);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(opSID, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.OP_SPEC_ID.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6666);
		data.rewind();

		// deserialize
		opSID = serializer
				.deserializeOpSpecID((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = opSID.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.OP_SPEC_ID);
		Assert.assertEquals(opSID.getOpSpecID(), 6666);

		// getLength
		Assert.assertEquals(serializer.getLength(opSID), 3);
	}

	@Test
	public void rfSurveyEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		RFSurveyEvent rfSE = new RFSurveyEvent(
				new TLVParameterHeader((byte) 0),
				RFSurveyEventType.START_OF_RFSURVEY, (long) 9999, 999);

		int parameterLength = serializer.getLength(rfSE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfSE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				RFSurveyEventType.START_OF_RFSURVEY.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 9999);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 999);
		data.rewind();

		// deserialize
		rfSE = serializer.deserializeRFSurveyEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfSE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_SURVEY_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rfSE.getEventType(),
				RFSurveyEventType.START_OF_RFSURVEY);
		Assert.assertEquals(rfSE.getRoSpecID(), 9999);
		Assert.assertEquals(rfSE.getSpecIndex(), 999);

		// getLength
		Assert.assertEquals(serializer.getLength(rfSE), 11);
	}

	@Test
	public void aiSpecEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AISpecEvent aiSE = new AISpecEvent(new TLVParameterHeader((byte) 0),
				AISpecEventType.END_OF_AISPEC, (long) 9999, 999);
		C1G2SingulationDetails sDet = new C1G2SingulationDetails(
				new TVParameterHeader(), 50, 60);
		aiSE.setC1g2SingulationDetails(sDet);

		int parameterLength = serializer.getLength(aiSE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(aiSE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.AI_SPEC_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				AISpecEventType.END_OF_AISPEC.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 9999);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 999);
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.C1G2_SINGULATION_DETAILS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 50);
		data.rewind();

		// deserialize
		aiSE = serializer.deserializeAISpecEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = aiSE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.AI_SPEC_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(aiSE.getEventType(), AISpecEventType.END_OF_AISPEC);
		Assert.assertEquals(aiSE.getRoSpecID(), 9999);
		Assert.assertEquals(aiSE.getSpecIndex(), 999);

		TVParameterHeader tvParameterHeader = aiSE.getC1g2SingulationDetails()
				.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.C1G2_SINGULATION_DETAILS);
		Assert.assertEquals(aiSE.getC1g2SingulationDetails()
				.getNumCollisionSlots(), 50);

		// getLength
		Assert.assertEquals(serializer.getLength(aiSE), 16);
	}

	@Test
	public void antennaEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AntennaEventType type = AntennaEventType.ANTENNA_CONNECTED;
		int aID = 5698;
		AntennaEvent aE = new AntennaEvent(new TLVParameterHeader((byte) 0),
				type, aID);
		int parameterLength = serializer.getLength(aE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(aE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ANTENNA_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				AntennaEventType.ANTENNA_CONNECTED.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5698);
		data.rewind();

		// deserialize
		aE = serializer.deserializeAntennaEvent((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = aE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ANTENNA_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(aE.getEventType(), type);
		Assert.assertEquals(aE.getAntennaID(), aID);
		// getLength
		Assert.assertEquals(serializer.getLength(aE), 7);
	}

	@Test
	public void specLoopEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		long min = 999999999L;
		long max = 4294967295L;
		SpecLoopEvent slE = new SpecLoopEvent(new TLVParameterHeader((byte) 0),
				min, max);
		int parameterLength = serializer.getLength(slE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(slE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.SPEC_LOOP_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 999999999L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4294967295L);
		data.rewind();

		// deserialize
		slE = serializer.deserializeSpecLoopEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = slE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.SPEC_LOOP_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(slE.getRoSpecID(), min);
		Assert.assertEquals(slE.getLoopCount(), max);
		// getLength
		Assert.assertEquals(serializer.getLength(slE), 12);
	}

	@Test
	public void rfSurveyReportData() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		FrequencyRSSILevelEntry fle = new FrequencyRSSILevelEntry(
				new TLVParameterHeader((byte) 0), (long) 10000, (long) 20000,
				(byte) -5, (byte) 20, utc);

		List<FrequencyRSSILevelEntry> frequencyRSSILevelEntryList = new ArrayList<>();
		frequencyRSSILevelEntryList.add(fle);
		RFSurveyReportData rfSRD = new RFSurveyReportData(
				new TLVParameterHeader((byte) 0), frequencyRSSILevelEntryList);

		ROSpecID roSID = new ROSpecID(new TVParameterHeader(), 11111111L);
		rfSRD.setRoSpecID(roSID);

		int parameterLength = serializer.getLength(rfSRD);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfSRD, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_REPORT_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.RO_SPEC_ID.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_RSSI_LEVEL_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 26);
		data.rewind();

		// deserialize
		rfSRD = serializer.deserializeRFSurveyReportData(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfSRD.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_SURVEY_REPORT_DATA);

		TVParameterHeader tvHeader = rfSRD.getRoSpecID().getParameterHeader();
		Assert.assertEquals(tvHeader.getParameterType(),
				ParameterType.RO_SPEC_ID);

		TLVParameterHeader header = rfSRD.getFrequencyRSSILevelEntryList()
				.get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.FREQUENCY_RSSI_LEVEL_ENTRY);
		Assert.assertEquals(header.getParameterLength(), 26);

		// getLength
		Assert.assertEquals(serializer.getLength(rfSRD), 4 + 5 + 26);
	}

	@Test
	public void frequencyRSSILevelEntry() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		FrequencyRSSILevelEntry fle = new FrequencyRSSILevelEntry(
				new TLVParameterHeader((byte) 0), (long) 10000, (long) 20000,
				(byte) -5, (byte) 20, utc);

		int parameterLength = serializer.getLength(fle);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(fle, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_RSSI_LEVEL_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 10000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 20000);
		Assert.assertEquals(data.get(), -5);
		Assert.assertEquals(data.get(), 20);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UTC_TIMESTAMP.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 12);
		data.rewind();

		// deserialize
		fle = serializer.deserializeFrequencyRSSILevelEntry(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = fle.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.FREQUENCY_RSSI_LEVEL_ENTRY);
		TLVParameterHeader header = fle.getUtcTimestamp().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.UTC_TIMESTAMP);
		Assert.assertEquals(header.getParameterLength(), 12);
		Assert.assertEquals(fle.getBandWidth(), 20000);
		Assert.assertEquals(fle.getAverageRSSI(), -5);
		// getLength
		Assert.assertEquals(serializer.getLength(fle), 26);
	}

	@Test
	public void tagReportData() throws InvalidParameterTypeException {
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

		int parameterLength = serializer.getLength(tagRD);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tagRD, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TAG_REPORT_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(data.get(),
				(byte) (1 << 7) + ParameterType.EPC_96.getValue());
		byte[] place = new byte[13 - 1];
		data.get(place);

		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.RO_SPEC_ID.getValue());
		data.rewind();

		// deserialize
		tagRD = serializer.deserializeTagReportData(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = tagRD.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.TAG_REPORT_DATA);

		Assert.assertArrayEquals(tagRD.getEpc96().getEpc(), epc);

		byte[] dst = { 0x10, 0x20, (byte) 0xFE, 0x00 };
		Assert.assertArrayEquals(((C1G2ReadOpSpecResult) tagRD.getOpSpecResultList()
				.get(0)).getReadData(), dst);
		Assert.assertEquals(((C1G2ReadOpSpecResult) tagRD.getOpSpecResultList()
				.get(0)).getReadDataWordCount(), 2);

		// getLength
		Assert.assertEquals(serializer.getLength(tagRD), 71);
	}

	@Test
	public void c1g2GetBlockPermalockStatusOpSpecResult()
			throws InvalidParameterTypeException {
		// serialize
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// Muster_even
		// byte[] permalockStatus = {0x10,0x20,(byte)0xFE};
		// Muster_odd
		byte[] permalockStatus = { 0x10, 0x20, (byte) 0xFE, (byte) 0xEE };
		C1G2GetBlockPermalockStatusOpSpecResult c1g2GetxxxResult = new C1G2GetBlockPermalockStatusOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2GetBlockPermalockStatusOpSpecResultValues.NO_RESPONSE_FROM_TAG,
				1234, permalockStatus);
		int parameterLength = serializer.getLength(c1g2GetxxxResult);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2GetxxxResult, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(
				DataTypeConverter.ubyte(data.get()),
				C1G2GetBlockPermalockStatusOpSpecResultValues.NO_RESPONSE_FROM_TAG
						.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		// even
		// byte[] dst = {0x10,0x20,(byte)0xFE,0x00};
		// byte[] tryget=new byte[4];
		// data.get(tryget, 0, 4);
		// Assert.assertEquals(tryget, dst);
		// odd
		byte[] dst = new byte[permalockStatus.length];
		data.get(dst, 0, permalockStatus.length);
		Assert.assertArrayEquals(dst, permalockStatus);

		data.rewind();

		// deserialize
		c1g2GetxxxResult = serializer
				.deserializeC1G2GetBlockPermalockStatusOpSpecResult(
						(TLVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2GetxxxResult
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(
				c1g2GetxxxResult.getResult(),
				C1G2GetBlockPermalockStatusOpSpecResultValues.NO_RESPONSE_FROM_TAG);
		Assert.assertEquals(c1g2GetxxxResult.getOpSpecID(), 1234);
		Assert.assertEquals(c1g2GetxxxResult.getStatusWordCount(), 2);

		// even
		// Assert.assertEquals(c1g2GetxxxResult.getPermalockStatus(), tryget);
		// odd
		Assert.assertArrayEquals(c1g2GetxxxResult.getPermalockStatus(),
				permalockStatus);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2GetxxxResult),
				9 + c1g2GetxxxResult.getStatusWordCount() * 2);
	}

	@Test
	public void c1g2BlockPermalockOpSpecResult()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2BlockPermalockOpSpecResult c1g2BPOSR = new C1G2BlockPermalockOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2BlockPermalockOpSpecResultValues.TAG_MEMORY_OVERRUN_ERROR,
				1234);
		int parameterLength = serializer.getLength(c1g2BPOSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2BPOSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2BlockPermalockOpSpecResultValues.TAG_MEMORY_OVERRUN_ERROR
						.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		c1g2BPOSR = serializer.deserializeC1G2BlockPermalockOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2BPOSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2BPOSR.getResult(),
				C1G2BlockPermalockOpSpecResultValues.TAG_MEMORY_OVERRUN_ERROR);
		Assert.assertEquals(c1g2BPOSR.getOpSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2BPOSR), 7);
	}

	@Test
	public void c1g2BlockWriteOpSpecResult()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2BlockWriteOpSpecResult c1g2BWOSR = new C1G2BlockWriteOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2BlockWriteOpSpecResultValues.INSUFFICIENT_POWER_TO_PERFORM_MEMORY_WRITE_OPERATION,
				1234, 5678);
		int parameterLength = serializer.getLength(c1g2BWOSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2BWOSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_BLOCK_WRITE_OP_SPEC_RESULT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(
				DataTypeConverter.ubyte(data.get()),
				C1G2BlockWriteOpSpecResultValues.INSUFFICIENT_POWER_TO_PERFORM_MEMORY_WRITE_OPERATION
						.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5678);
		data.rewind();

		// deserialize
		c1g2BWOSR = serializer.deserializeC1G2BlockWriteOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2BWOSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_BLOCK_WRITE_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(
				c1g2BWOSR.getResult(),
				C1G2BlockWriteOpSpecResultValues.INSUFFICIENT_POWER_TO_PERFORM_MEMORY_WRITE_OPERATION);
		Assert.assertEquals(c1g2BWOSR.getOpSpecID(), 1234);
		Assert.assertEquals(c1g2BWOSR.getNumWordsWritten(), 5678);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2BWOSR), 9);
	}

	@Test
	public void c1g2BlockEraseOpSpecResult()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2BlockEraseOpSpecResult c1g2BEOSR = new C1G2BlockEraseOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2BlockEraseOpSpecResultValues.SUCCESS, 1234);
		int parameterLength = serializer.getLength(c1g2BEOSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2BEOSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_BLOCK_ERASE_OP_SPEC_RESULT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2BlockEraseOpSpecResultValues.SUCCESS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		c1g2BEOSR = serializer.deserializeC1G2BlockEraseOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2BEOSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_BLOCK_ERASE_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2BEOSR.getResult(),
				C1G2BlockEraseOpSpecResultValues.SUCCESS);
		Assert.assertEquals(c1g2BEOSR.getOpSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2BEOSR), 7);
	}

	@Test
	public void c1g2LockOpSpecResult() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2LockOpSpecResult c1g2LOSR = new C1G2LockOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2LockOpSpecResultValues.SUCCESS, 1234);
		int parameterLength = serializer.getLength(c1g2LOSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2LOSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_LOCK_OP_SPEC_RESULT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2LockOpSpecResultValues.SUCCESS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		c1g2LOSR = serializer.deserializeC1G2LockOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2LOSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_LOCK_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2LOSR.getResult(),
				C1G2LockOpSpecResultValues.SUCCESS);
		Assert.assertEquals(c1g2LOSR.getOpSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2LOSR), 7);
	}

	@Test
	public void c1g2RecommissionOpSpecResult()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2RecommissionOpSpecResult c1g2ROSR = new C1G2RecommissionOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2RecommissionOpSpecResultValues.SUCCESS, 1234);
		int parameterLength = serializer.getLength(c1g2ROSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2ROSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_RECOMMISSION_OP_SPEC_RESULT
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		c1g2ROSR = serializer.deserializeC1G2RecommissionOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2ROSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_RECOMMISSION_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2ROSR.getResult(),
				C1G2RecommissionOpSpecResultValues.SUCCESS);
		Assert.assertEquals(c1g2ROSR.getOpSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2ROSR), 7);
	}

	@Test
	public void c1g2KillOpSpecResult() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2KillOpSpecResult c1g2KOSR = new C1G2KillOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2KillOpSpecResultValues.SUCCESS, 1234);
		int parameterLength = serializer.getLength(c1g2KOSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2KOSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_KILL_OP_SPEC_RESULT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2KillOpSpecResultValues.SUCCESS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		c1g2KOSR = serializer.deserializeC1G2KillOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2KOSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_KILL_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2KOSR.getResult(),
				C1G2KillOpSpecResultValues.SUCCESS);
		Assert.assertEquals(c1g2KOSR.getOpSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2KOSR), 7);
	}

	@Test
	public void c1g2WriteOpSpecResult() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2WriteOpSpecResult c1g2WOSR = new C1G2WriteOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2WriteOpSpecResultValues.SUCCESS, 1234, 5678);
		int parameterLength = serializer.getLength(c1g2WOSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2WOSR, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_WRITE_OP_SPEC_RESULT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2WriteOpSpecResultValues.SUCCESS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5678);
		data.rewind();

		// deserialize
		c1g2WOSR = serializer.deserializeC1G2WriteOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2WOSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_WRITE_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2WOSR.getResult(),
				C1G2WriteOpSpecResultValues.SUCCESS);
		Assert.assertEquals(c1g2WOSR.getOpSpecID(), 1234);
		Assert.assertEquals(c1g2WOSR.getNumWordsWritten(), 5678);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2WOSR), 9);
	}

	@Test
	public void c1g2ReadOpSpecResult() throws InvalidParameterTypeException {
		// serialize
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// Muster_even
		byte[] readData = { 0x10, 0x20, (byte) 0xFE };
		// Muster_odd
		// byte[] readData = { 0x10, 0x20, (byte) 0xFE, (byte) 0xEE };
		C1G2ReadOpSpecResult c1g2ROSR = new C1G2ReadOpSpecResult(
				new TLVParameterHeader((byte) 0),
				C1G2ReadOpSpecResultValues.SUCCESS, 1234, readData);
		int parameterLength = serializer.getLength(c1g2ROSR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2ROSR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_READ_OP_SPEC_RESULT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		// even
		byte[] dst = { 0x10, 0x20, (byte) 0xFE, 0x00 };
		byte[] tryget = new byte[4];
		data.get(tryget, 0, 4);
		Assert.assertArrayEquals(tryget, dst);
		// odd
		// byte[] dst = new byte[readData.length];
		// data.get(dst, 0, readData.length);
		// Assert.assertEquals(dst, readData);

		data.rewind();

		// deserialize
		c1g2ROSR = serializer.deserializeC1G2ReadOpSpecResult(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2ROSR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_READ_OP_SPEC_RESULT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2ROSR.getResult(),
				C1G2ReadOpSpecResultValues.SUCCESS);
		Assert.assertEquals(c1g2ROSR.getOpSpecID(), 1234);
		Assert.assertEquals(c1g2ROSR.getReadDataWordCount(), 2);

		// even
		Assert.assertArrayEquals(c1g2ROSR.getReadData(), tryget);
		// odd
		// Assert.assertEquals(c1g2ROSR.getReadData(), readData);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2ROSR),
				9 + c1g2ROSR.getReadDataWordCount() * 2);
	}

	@Test
	public void c1g2SingulationDetails() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2SingulationDetails sinDetails = new C1G2SingulationDetails(
				new TVParameterHeader(), 1234, 5678);
		int parameterLength = serializer.getLength(sinDetails);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(sinDetails, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.C1G2_SINGULATION_DETAILS.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5678);
		data.rewind();

		// deserialize
		sinDetails = serializer
				.deserializeC1G2SingulationDetails(
						(TVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = sinDetails.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.C1G2_SINGULATION_DETAILS);
		Assert.assertEquals(sinDetails.getNumCollisionSlots(), 1234);
		Assert.assertEquals(sinDetails.getNumEmptySlots(), 5678);

		// getLength
		Assert.assertEquals(serializer.getLength(sinDetails), 5);
	}

	@Test
	public void c1g2CRC() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2CRC crc = new C1G2CRC(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(crc);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(crc, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.C1G2_CRC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		crc = serializer
				.deserializeC1G2CRC((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = crc.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.C1G2_CRC);
		Assert.assertEquals(crc.getCrc(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(crc), 3);
	}

	@Test
	public void c1g2XPCW2() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2XPCW2 xpcw2 = new C1G2XPCW2(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(xpcw2);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(xpcw2, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.C1G2_XPCW2.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		xpcw2 = serializer
				.deserializeC1G2XPCW2((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = xpcw2.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.C1G2_XPCW2);
		Assert.assertEquals(xpcw2.getXpcW2(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(xpcw2), 3);
	}

	@Test
	public void c1g2XPCW1() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2XPCW1 xpcw1 = new C1G2XPCW1(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(xpcw1);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(xpcw1, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.C1G2_XPCW1.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		xpcw1 = serializer
				.deserializeC1G2XPCW1((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = xpcw1.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.C1G2_XPCW1);
		Assert.assertEquals(xpcw1.getXpcW1(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(xpcw1), 3);
	}

	@Test
	public void c1g2PC() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2PC pc = new C1G2PC(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(pc);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(pc, data);
		data.flip();
		Assert.assertEquals(data.get(),
				(byte) (1 << 7) + ParameterType.C1G2_PC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		pc = serializer
				.deserializeC1G2PC((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = pc.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.C1G2_PC);
		Assert.assertEquals(pc.getPcBits(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(pc), 3);
	}

	@Test
	public void accessSpecID() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AccessSpecId asID = new AccessSpecId(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(asID);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(asID, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.ACCESS_SPEC_ID.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1234);
		data.rewind();

		// deserialize
		asID = serializer
				.deserializeAccessSpecID((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = asID.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.ACCESS_SPEC_ID);
		Assert.assertEquals(asID.getAccessSpecId(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(asID), 5);
	}

	@Test
	public void clientRequestOpSpecResult()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		ClientRequestOpSpecResult crosR = new ClientRequestOpSpecResult(
				new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(crosR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(crosR, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.CLIENT_REQUEST_OP_SPEC_RESULT.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		crosR = serializer
				.deserializeClientRequestOpSpecResult(
						(TVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = crosR.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.CLIENT_REQUEST_OP_SPEC_RESULT);
		Assert.assertEquals(crosR.getOpSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(crosR), 3);
	}

	@Test
	public void tagSeenCount() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		TagSeenCount tSC = new TagSeenCount(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(tSC);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tSC, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.TAG_SEEN_COUNT.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		tSC = serializer
				.deserializeTagSeenCount((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = tSC.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.TAG_SEEN_COUNT);
		Assert.assertEquals(tSC.getTagCount(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(tSC), 3);
	}

	@Test
	public void LastSeenTimestampUptime() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		LastSeenTimestampUptime lsUptime = new LastSeenTimestampUptime(
				new TVParameterHeader(), new BigInteger("111111113"));
		int parameterLength = serializer.getLength(lsUptime);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(lsUptime, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.LAST_SEEN_TIMESTAMP_UPTIME.getValue());
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				111111113);
		data.rewind();

		// deserialize
		lsUptime = serializer
				.deserializeLastSeenTimestampUptime(
						(TVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = lsUptime.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.LAST_SEEN_TIMESTAMP_UPTIME);
		Assert.assertEquals(lsUptime.getMicroseconds(), new BigInteger(
				"111111113"));

		// getLength
		Assert.assertEquals(serializer.getLength(lsUptime), 9);
	}

	@Test
	public void lastSeenTimestampUTC() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		LastSeenTimestampUTC lsUTC = new LastSeenTimestampUTC(
				new TVParameterHeader(), new BigInteger("111111113"));
		int parameterLength = serializer.getLength(lsUTC);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(lsUTC, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.LAST_SEEN_TIMESTAMP_UTC.getValue());
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				111111113);
		data.rewind();

		// deserialize
		lsUTC = serializer
				.deserializeLastSeenTimestampUTC((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = lsUTC.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.LAST_SEEN_TIMESTAMP_UTC);
		Assert.assertEquals(lsUTC.getMicroseconds(),
				new BigInteger("111111113"));

		// getLength
		Assert.assertEquals(serializer.getLength(lsUTC), 9);
	}

	@Test
	public void firstSeenTimestampUptime() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		FirstSeenTimestampUptime fsUptime = new FirstSeenTimestampUptime(
				new TVParameterHeader(), new BigInteger("111111113"));
		int parameterLength = serializer.getLength(fsUptime);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(fsUptime, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.FIRST_SEEN_TIMESTAMP_UPTIME.getValue());
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				111111113);
		data.rewind();

		// deserialize
		fsUptime = serializer
				.deserializeFirstSeenTimestampUptime(
						(TVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = fsUptime.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.FIRST_SEEN_TIMESTAMP_UPTIME);
		Assert.assertEquals(fsUptime.getMicroseconds(), new BigInteger(
				"111111113"));

		// getLength
		Assert.assertEquals(serializer.getLength(fsUptime), 9);
	}

	@Test
	public void firstSeenTimestampUTC() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		FirstSeenTimestampUTC fsUTC = new FirstSeenTimestampUTC(
				new TVParameterHeader(), new BigInteger("111111113"));
		int parameterLength = serializer.getLength(fsUTC);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(fsUTC, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.FIRST_SEEN_TIMESTAMP_UTC.getValue());
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				111111113);
		data.rewind();

		// deserialize
		fsUTC = serializer
				.deserializeFirstSeenTimestampUTC(
						(TVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = fsUTC.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.FIRST_SEEN_TIMESTAMP_UTC);
		Assert.assertEquals(fsUTC.getMicroseconds(),
				new BigInteger("111111113"));

		// getLength
		Assert.assertEquals(serializer.getLength(fsUTC), 9);
	}

	@Test
	public void channelIndex() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		ChannelIndex ci = new ChannelIndex(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(ci);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ci, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.CHANNEL_INDEX.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		ci = serializer
				.deserializeChannelIndex((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = ci.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.CHANNEL_INDEX);
		Assert.assertEquals(ci.getChannelIndex(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(ci), 3);
	}

	@Test
	public void peakRSSI() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		PeakRSSI rssi = new PeakRSSI(new TVParameterHeader(), (byte) -12);
		int parameterLength = serializer.getLength(rssi);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rssi, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.PEAK_RSSI.getValue());
		Assert.assertEquals(data.get(), -12);
		data.rewind();

		// deserialize
		rssi = serializer
				.deserializePeakRSSI((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = rssi.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.PEAK_RSSI);
		Assert.assertEquals(rssi.getPeakRSSI(), -12);

		// getLength
		Assert.assertEquals(serializer.getLength(rssi), 2);
	}

	@Test
	public void antennaID() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AntennaId antID = new AntennaId(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(antID);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(antID, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.ANTENNA_ID.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		antID = serializer
				.deserializeAntennaID((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = antID.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.ANTENNA_ID);
		Assert.assertEquals(antID.getAntennaId(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(antID), 3);
	}

	@Test
	public void inventoryParameterSpecID() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		InventoryParameterSpecID ipsID = new InventoryParameterSpecID(
				new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(ipsID);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ipsID, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.INVENTORY_PARAMETER_SPEC_ID.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		ipsID = serializer
				.deserializeInventoryParameterSpecID(
						(TVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = ipsID.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.INVENTORY_PARAMETER_SPEC_ID);
		Assert.assertEquals(ipsID.getInventoryParameterSpecID(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(ipsID), 3);
	}

	@Test
	public void specIndex() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		SpecIndex si = new SpecIndex(new TVParameterHeader(), 1234);
		int parameterLength = serializer.getLength(si);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(si, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.SPEC_INDEX.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		data.rewind();

		// deserialize
		si = serializer
				.deserializeSpecIndex((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = si.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.SPEC_INDEX);
		Assert.assertEquals(si.getSpecIndex(), 1234);

		// getLength
		Assert.assertEquals(serializer.getLength(si), 3);
	}

	@Test
	public void roSpecID() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		ROSpecID roSID = new ROSpecID(new TVParameterHeader(), 11111111L);
		int parameterLength = serializer.getLength(roSID);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(roSID, data);
		data.flip();
		Assert.assertEquals(data.get(), (byte) (1 << 7)
				+ ParameterType.RO_SPEC_ID.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);
		data.rewind();

		// deserialize
		roSID = serializer
				.deserializeROSpecID((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = roSID.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.RO_SPEC_ID);
		Assert.assertEquals(roSID.getRoSpecID(), 11111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(roSID), 5);
	}

	@Test
	public void epc96() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		byte[] epc = { 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, (byte) 0x80,
				(byte) 0x90, (byte) 0xA0, (byte) 0xB0, (byte) 0xC0 };
		EPC96 epc96 = new EPC96(new TVParameterHeader(), epc);

		int parameterLength = serializer.getLength(epc96);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(epc96, data);
		data.flip();
		Assert.assertEquals(data.get(),
				(byte) (1 << 7) + ParameterType.EPC_96.getValue());
		byte[] place = new byte[12];
		data.get(place);
		Assert.assertArrayEquals(place, epc);
		data.rewind();

		// deserialize
		epc96 = serializer
				.deserializeEPC96((TVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TVParameterHeader tvParameterHeader = epc96.getParameterHeader();
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.EPC_96);
		Assert.assertArrayEquals(epc96.getEpc(), epc);

		// getLength
		Assert.assertEquals(serializer.getLength(epc96), 13);
	}

	@Test
	public void accessSpec() throws InvalidParameterTypeException {
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

		int parameterLength = serializer.getLength(aSpec);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(aSpec, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
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
		aSpec = serializer.deserializeAccessSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = aSpec.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ACCESS_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(aSpec.getAccessSpecId(), 11111111L);
		Assert.assertEquals(aSpec.getAntennaId(), 7777);
		Assert.assertEquals(aSpec.getProtocolId(), ProtocolId.EPC_GLOBAL_C1G2);
		Assert.assertEquals(aSpec.isCurrentState(), true);
		Assert.assertEquals(aSpec.getRoSpecId(), 22222222L);

		TLVParameterHeader header = aSpec.getAccessSpecStopTrigger()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_SPEC_STOP_TRIGGER);
		Assert.assertEquals(header.getParameterLength(), 7);

		header = aSpec.getAccessCommand().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_COMMAND);
		Assert.assertEquals(header.getParameterLength(), 4 + 34 + 6 + 15);

		header = aSpec.getAccessCommand().getC1g2TagSpec().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_SPEC);
		Assert.assertEquals(header.getParameterLength(), 34);

		header = (TLVParameterHeader) aSpec.getAccessCommand().getOpSpecList()
				.get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.CLIENT_REQUEST_OP_SPEC);
		Assert.assertEquals(header.getParameterLength(), 6);
		Assert.assertEquals(((ClientRequestOpSpec) aSpec.getAccessCommand()
				.getOpSpecList().get(0)).getOpSpecID(), 5555);

		header = (TLVParameterHeader) aSpec.getAccessCommand().getOpSpecList()
				.get(1).getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.C1G2_READ);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(((C1G2Read) aSpec.getAccessCommand()
				.getOpSpecList().get(1)).getWordCount(), 10000);

		header = aSpec.getAccessReportSpec().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ACCESS_REPORT_SPEC);
		Assert.assertEquals(header.getParameterLength(), 5);

		// getLength
		Assert.assertEquals(serializer.getLength(aSpec), 16 + 7 + 4 + 34 + 6
				+ 15 + 5);
	}

	@Test
	public void accessSpecStopTrigger() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AccessSpecStopTrigger aSSTrigger = new AccessSpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				AccessSpecStopTriggerType.OPERATION_COUNT, 555);

		int parameterLength = serializer.getLength(aSSTrigger);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(aSSTrigger, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				AccessSpecStopTriggerType.OPERATION_COUNT.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 555);
		data.rewind();

		// deserialize
		aSSTrigger = serializer.deserializeAccessSpecStopTrigger(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = aSSTrigger.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ACCESS_SPEC_STOP_TRIGGER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(aSSTrigger.getAccessSpecStopTriggerType(),
				AccessSpecStopTriggerType.OPERATION_COUNT);
		Assert.assertEquals(aSSTrigger.getOperationCountValue(), 555);

		// getLength
		Assert.assertEquals(serializer.getLength(aSSTrigger), 7);
	}

	@Test
	public void accessCommand() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize

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

		int parameterLength = serializer.getLength(ac);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ac, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_COMMAND.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 34);
		byte[] place = new byte[34 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CLIENT_REQUEST_OP_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		place = new byte[6 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_READ.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 15);
		data.rewind();

		// deserialize
		ac = serializer.deserializeAccessCommand(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ac.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ACCESS_COMMAND);
		TLVParameterHeader header = ac.getC1g2TagSpec().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_SPEC);
		Assert.assertEquals(header.getParameterLength(), 34);

		header = (TLVParameterHeader) ac.getOpSpecList().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.CLIENT_REQUEST_OP_SPEC);
		Assert.assertEquals(header.getParameterLength(), 6);
		Assert.assertEquals(
				((ClientRequestOpSpec) ac.getOpSpecList().get(0)).getOpSpecID(),
				5555);

		header = (TLVParameterHeader) ac.getOpSpecList().get(1)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.C1G2_READ);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(
				((C1G2Read) ac.getOpSpecList().get(1)).getWordCount(), 10000);

		// getLength
		Assert.assertEquals(serializer.getLength(ac), 34 + 6 + 15 + 4);
	}

	@Test
	public void clientRequestOpSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		ClientRequestOpSpec crOpSpec = new ClientRequestOpSpec(
				new TLVParameterHeader((byte) 0), 5555);
		int parameterLength = serializer.getLength(crOpSpec);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(crOpSpec, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CLIENT_REQUEST_OP_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5555);
		data.rewind();

		// deserialize
		crOpSpec = serializer.deserializeClientRequestOpSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = crOpSpec.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.CLIENT_REQUEST_OP_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(crOpSpec.getOpSpecID(), 5555);

		// getLength
		Assert.assertEquals(serializer.getLength(crOpSpec), 6);
	}

	@Test
	public void clientRequestResponse() throws InvalidParameterTypeException {
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

		int parameterLength = serializer.getLength(crRes);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(crRes, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CLIENT_REQUEST_RESPONSE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EPC_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		byte[] temp = new byte[8 - 4];
		data.get(temp);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_READ.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 15);
		temp = new byte[15 - 4];
		data.get(temp);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_RECOMMISSION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 11);
		data.rewind();

		// deserialize
		crRes = serializer.deserializeClientRequestResponse(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = crRes.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.CLIENT_REQUEST_RESPONSE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		TLVParameterHeader header = crRes.getEpcData().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(), ParameterType.EPC_DATA);
		Assert.assertEquals(header.getParameterLength(), 8);
		Assert.assertEquals(crRes.getEpcData().getEpc(), epc);

		header = (TLVParameterHeader) crRes.getOpSpecList().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.C1G2_READ);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(
				((C1G2Read) crRes.getOpSpecList().get(0)).getWordCount(), 10000);

		header = (TLVParameterHeader) crRes.getOpSpecList().get(1)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_RECOMMISSION);
		Assert.assertEquals(header.getParameterLength(), 11);
		Assert.assertEquals(
				((C1G2Recommission) crRes.getOpSpecList().get(1)).isLsb(), true);

		// getLength
		Assert.assertEquals(serializer.getLength(crRes), 42);
	}

	@Test
	public void epcData() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		// 11110000 10101010 00000001 00000000
		BitSet epc = new BitSet();
		epc.set(0);
		epc.set(1);
		epc.set(2);
		epc.set(3);
		epc.set(8);
		epc.set(10);
		epc.set(12);
		epc.set(14);
		epc.set(23);
		EPCData epcData = new EPCData(new TLVParameterHeader((byte) 0), epc);
		epcData.setEpcLengthBits(32);
		int parameterLength = serializer.getLength(epcData);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(epcData, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EPC_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);

		byte[] maskArray = { (byte) 0b11110000, (byte) 0b10101010,
				(byte) 0b00000001, (byte) 0b00000000 };
		byte[] dst = new byte[maskArray.length];
		data.get(dst, 0, maskArray.length);
		Assert.assertArrayEquals(dst, maskArray);
		data.rewind();

		// deserialize
		epcData = serializer.deserializeEPCData((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = epcData.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.EPC_DATA);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(epcData.getEpcLengthBits(), 32);
		Assert.assertEquals(epcData.getEpc(), epc);

		// getLength
		Assert.assertEquals(serializer.getLength(epcData), 10);
	}

	@Test
	public void epcDataNoEPC() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		BitSet epc = new BitSet();
		EPCData epcData = new EPCData(new TLVParameterHeader((byte) 0), epc);
		int parameterLength = serializer.getLength(epcData);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(epcData, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EPC_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 0);
		data.rewind();

		// deserialize
		epcData = serializer.deserializeEPCData((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = epcData.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.EPC_DATA);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(epcData.getEpcLengthBits(), 0);
		Assert.assertEquals(epcData.getEpc(), epc);

		// getLength
		Assert.assertEquals(serializer.getLength(epcData), 6);
	}

	@Test
	public void c1g2TagSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
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

		int parameterLength = serializer.getLength(c1g2TagSpec);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2TagSpec, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TARGET_TAG.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 15);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 224);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		byte[] maskArray = { (byte) 0b11110000, (byte) 0b10101010 };
		byte[] dst = new byte[maskArray.length];
		data.get(dst, 0, maskArray.length);
		Assert.assertArrayEquals(dst, maskArray);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		byte[] tagDataArray = { (byte) 0b01111110, (byte) 0b11001100 };
		byte[] dstTagData = new byte[maskArray.length];
		data.get(dstTagData, 0, tagDataArray.length);
		Assert.assertArrayEquals(dstTagData, tagDataArray);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TARGET_TAG.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 15);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 224);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		byte[] dst_opt = new byte[maskArray.length];
		data.get(dst_opt, 0, maskArray.length);
		Assert.assertArrayEquals(dst_opt, maskArray);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		byte[] dstTagData_opt = new byte[tagDataArray.length];
		data.get(dstTagData_opt, 0, tagDataArray.length);
		Assert.assertArrayEquals(dstTagData_opt, tagDataArray);
		data.rewind();

		// deserialize
		c1g2TagSpec = serializer.deserializeC1G2TagSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2TagSpec
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_TAG_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		TLVParameterHeader header = c1g2TagSpec.getTagPattern1()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TARGET_TAG);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(c1g2TagSpec.getTagPattern1().getMemoryBank(), 3);
		Assert.assertEquals(c1g2TagSpec.getTagPattern1().getPointer(), 100);
		Assert.assertEquals(c1g2TagSpec.getTagPattern1().getMaskBitCount(), 16);
		Assert.assertEquals(c1g2TagSpec.getTagPattern1().getTagMask(), mask);
		Assert.assertEquals(c1g2TagSpec.getTagPattern1().getDataBitCount(), 16);
		Assert.assertEquals(c1g2TagSpec.getTagPattern1().getTagData(), tagData);

		header = c1g2TagSpec.getTagPattern2().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TARGET_TAG);
		Assert.assertEquals(header.getParameterLength(), 15);
		Assert.assertEquals(c1g2TagSpec.getTagPattern2().getMemoryBank(), 3);
		Assert.assertEquals(c1g2TagSpec.getTagPattern2().getPointer(), 100);
		Assert.assertEquals(c1g2TagSpec.getTagPattern2().getMaskBitCount(), 16);
		Assert.assertEquals(c1g2TagSpec.getTagPattern2().getTagMask(), mask);
		Assert.assertEquals(c1g2TagSpec.getTagPattern2().getDataBitCount(), 16);
		Assert.assertEquals(c1g2TagSpec.getTagPattern2().getTagData(), tagData);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2TagSpec), 34);
	}

	@Test
	public void c1g2TargetTag() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		// 11110000 10101010 10000000 00000000
		BitSet mask = new BitSet();
		mask.set(0);
		mask.set(1);
		mask.set(2);
		mask.set(3);
		mask.set(8);
		mask.set(10);
		mask.set(12);
		mask.set(14);
		mask.set(16);

		// 01111110 11001100 00000000 10000000
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
		tagData.set(24);

		C1G2TargetTag c1g2TT = new C1G2TargetTag(new TLVParameterHeader(
				(byte) 0), (byte) 3, true, 100, mask, tagData);
		c1g2TT.setDataBitCount(32);
		c1g2TT.setMaskBitCount(32);
		int parameterLength = serializer.getLength(c1g2TT);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2TT, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TARGET_TAG.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 224);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);
		byte[] maskArray = { (byte) 0b11110000, (byte) 0b10101010,
				(byte) 0b10000000, (byte) 0b00000000 };
		byte[] dst = new byte[maskArray.length];
		data.get(dst, 0, maskArray.length);
		Assert.assertArrayEquals(dst, maskArray);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);
		byte[] tagDataArray = { (byte) 0b01111110, (byte) 0b11001100,
				(byte) 0b00000000, (byte) 0b10000000 };
		byte[] dstTagData = new byte[tagDataArray.length];
		data.get(dstTagData, 0, tagDataArray.length);
		Assert.assertArrayEquals(dstTagData, tagDataArray);
		data.rewind();

		// deserialize
		c1g2TT = serializer.deserializeC1G2TargetTag(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2TT.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_TARGET_TAG);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2TT.getMemoryBank(), 3);
		Assert.assertEquals(c1g2TT.getPointer(), 100);
		Assert.assertEquals(c1g2TT.getMaskBitCount(), 32);
		Assert.assertEquals(c1g2TT.getTagMask(), mask);
		Assert.assertEquals(c1g2TT.getDataBitCount(), 32);
		Assert.assertEquals(c1g2TT.getTagData(), tagData);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2TT), 19);
	}

	@Test
	public void c1g2Read() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2Read c1g2Read = new C1G2Read(new TLVParameterHeader((byte) 0),
				33333, 111111111L, (byte) 3, 1234, 10000);
		int parameterLength = serializer.getLength(c1g2Read);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2Read, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_READ.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 33333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10000);
		data.rewind();

		// deserialize
		c1g2Read = serializer.deserializeC1G2Read(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2Read.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_READ);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2Read.getOpSpecId(), 33333);
		Assert.assertEquals(c1g2Read.getAccessPw(), 111111111L);
		Assert.assertEquals(c1g2Read.getMemoryBank(), 3);
		Assert.assertEquals(c1g2Read.getWordPointer(), 1234);
		Assert.assertEquals(c1g2Read.getWordCount(), 10000);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2Read), 15);
	}

	@Test
	public void c1g2Write() throws InvalidParameterTypeException {
		// serialize
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// Muster_even
		// byte[] writeData = {0x10,0x20,(byte)0xFE};
		// Muster_odd
		byte[] writeData = { 0x10, 0x20, (byte) 0xFE, (byte) 0xEE };

		C1G2Write c1g2Write = new C1G2Write(new TLVParameterHeader((byte) 0),
				33333, 111111111L, (byte) 3, 1234, writeData);
		int parameterLength = serializer.getLength(c1g2Write);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2Write, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_WRITE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 33333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);

		// even
		// byte[] dst = {0x10,0x20,(byte)0xFE,0x00};
		// byte[] tryget=new byte[4];
		// data.get(tryget, 0, 4);
		// Assert.assertEquals(dst, tryget);
		// odd
		byte[] dst = new byte[writeData.length];
		data.get(dst, 0, writeData.length);
		Assert.assertArrayEquals(dst, writeData);

		data.rewind();

		// deserialize
		c1g2Write = serializer.deserializeC1G2Write(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2Write.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_WRITE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2Write.getOpSpecId(), 33333);
		Assert.assertEquals(c1g2Write.getAccessPw(), 111111111L);
		Assert.assertEquals(c1g2Write.getMemoryBank(), 3);
		Assert.assertEquals(c1g2Write.getWordPointer(), 1234);
		Assert.assertEquals(c1g2Write.getWriteDataWordCount(), 2);

		// even
		// Assert.assertEquals(c1g2Write.getWriteData(), tryget);
		// odd
		Assert.assertArrayEquals(c1g2Write.getWriteData(), writeData);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2Write),
				15 + c1g2Write.getWriteDataWordCount() * 2);
	}

	@Test
	public void c1g2Kill() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2Kill c1g2Kill = new C1G2Kill(new TLVParameterHeader((byte) 0),
				1000, 11111111L);
		int parameterLength = serializer.getLength(c1g2Kill);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2Kill, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_KILL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);
		data.rewind();

		// deserialize
		c1g2Kill = serializer.deserializeC1G2Kill(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2Kill.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_KILL);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2Kill.getKillPw(), 11111111L);
		Assert.assertEquals(c1g2Kill.getOpSpecId(), 1000);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2Kill), 10);
	}

	@Test
	public void c1g2Recommission() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2Recommission c1g2Re = new C1G2Recommission(new TLVParameterHeader(
				(byte) 0), 1000, 11111111L, true, false, false);
		int parameterLength = serializer.getLength(c1g2Re);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2Re, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_RECOMMISSION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 1);
		data.rewind();

		// deserialize
		c1g2Re = serializer.deserializeC1G2Recommission(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2Re.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_RECOMMISSION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2Re.getKillPW(), 11111111L);
		Assert.assertEquals(c1g2Re.getOpSpecID(), 1000);
		Assert.assertEquals(c1g2Re.isLsb(), true);
		Assert.assertEquals(c1g2Re.isSb2(), false);
		Assert.assertEquals(c1g2Re.isSb3(), false);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2Re), 11);
	}

	@Test
	public void c1g2Lock() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		List<C1G2LockPayload> c1g2LPList = new ArrayList<>();
		C1G2LockPayload c1g2LP = new C1G2LockPayload(new TLVParameterHeader(
				(byte) 0), C1G2LockPayloadPrivilege.READ_WRITE,
				C1G2LockPayloadDataField.USER_MEMORY);
		c1g2LPList.add(c1g2LP);

		C1G2Lock c1g2Lock = new C1G2Lock(new TLVParameterHeader((byte) 0),
				1000, 11111111L, c1g2LPList);
		int parameterLength = serializer.getLength(c1g2Lock);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2Lock, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_LOCK.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 11111111L);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_LOCK_PAYLOAD.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 4);
		data.rewind();

		// deserialize
		c1g2Lock = serializer.deserializeC1G2Lock(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2Lock.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_LOCK);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2Lock.getAccessPw(), 11111111L);
		Assert.assertEquals(c1g2Lock.getOpSpecId(), 1000);
		Assert.assertEquals(c1g2Lock.getC1g2LockPayloadList().get(0)
				.getParameterHeader().getParameterType(),
				ParameterType.C1G2_LOCK_PAYLOAD);
		Assert.assertEquals(c1g2Lock.getC1g2LockPayloadList().get(0)
				.getDataField(), C1G2LockPayloadDataField.USER_MEMORY);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2Lock), 16);
	}

	@Test
	public void c1g2LockPayload() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2LockPayload c1g2LP = new C1G2LockPayload(new TLVParameterHeader(
				(byte) 0), C1G2LockPayloadPrivilege.READ_WRITE,
				C1G2LockPayloadDataField.USER_MEMORY);
		int parameterLength = serializer.getLength(c1g2LP);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2LP, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_LOCK_PAYLOAD.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 4);
		data.rewind();

		// deserialize
		c1g2LP = serializer.deserializeC1G2LockPayload(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2LP.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_LOCK_PAYLOAD);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2LP.getPrivilege(),
				C1G2LockPayloadPrivilege.READ_WRITE);
		Assert.assertEquals(c1g2LP.getDataField(),
				C1G2LockPayloadDataField.USER_MEMORY);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2LP), 6);
	}

	@Test
	public void c1g2BlockErase() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2BlockErase c1g2BE = new C1G2BlockErase(new TLVParameterHeader(
				(byte) 0), 33333, 111111111L, (byte) 3, 1234, 10000);
		int parameterLength = serializer.getLength(c1g2BE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2BE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_BLOCK_ERASE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 33333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10000);
		data.rewind();

		// deserialize
		c1g2BE = serializer.deserializeC1G2BlockErase(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2BE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_BLOCK_ERASE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2BE.getOpSpecID(), 33333);
		Assert.assertEquals(c1g2BE.getAccessPW(), 111111111L);
		Assert.assertEquals(c1g2BE.getmB(), 3);
		Assert.assertEquals(c1g2BE.getWordPointer(), 1234);
		Assert.assertEquals(c1g2BE.getWordCount(), 10000);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2BE), 15);
	}

	@Test
	public void c1g2BlockWrite() throws InvalidParameterTypeException {
		// serialize
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// Muster_even
		byte[] writeData = { 0x10, 0x20, (byte) 0xFE };
		// Muster_odd
		// byte[] writeData = { 0x10, 0x20, (byte) 0xFE, (byte) 0xEE };

		// System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(blockMask));
		C1G2BlockWrite c1g2BW = new C1G2BlockWrite(new TLVParameterHeader(
				(byte) 0), 33333, 111111111L, (byte) 3, 1234, writeData);
		int parameterLength = serializer.getLength(c1g2BW);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2BW, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_BLOCK_WRITE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 33333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);

		// even
		byte[] dst = { 0x10, 0x20, (byte) 0xFE, 0x00 };
		byte[] tryget = new byte[4];
		data.get(tryget, 0, 4);
		Assert.assertArrayEquals(dst, tryget);
		// odd
		// byte[] dst = new byte[writeData.length];
		// data.get(dst, 0, writeData.length);
		// Assert.assertEquals(dst, writeData);

		data.rewind();

		// deserialize
		c1g2BW = serializer.deserializeC1G2BlockWrite(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2BW.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_BLOCK_WRITE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2BW.getOpSpecID(), 33333);
		Assert.assertEquals(c1g2BW.getAccessPW(), 111111111L);
		Assert.assertEquals(c1g2BW.getmB(), 3);
		Assert.assertEquals(c1g2BW.getWordPointer(), 1234);
		Assert.assertEquals(c1g2BW.getWriteDataWordCount(), 2);

		// even
		Assert.assertArrayEquals(c1g2BW.getWriteData(), tryget);
		// odd
		// Assert.assertEquals(c1g2BW.getWriteData(), writeData);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2BW),
				15 + c1g2BW.getWriteDataWordCount() * 2);
	}

	@Test
	public void c1g2BlockPermalock() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize

		// Muster_even
		// byte[] blockMask = {0x10,0x20,(byte)0xFE};
		// Muster_odd
		byte[] blockMask = { 0x10, 0x20, (byte) 0xFE, (byte) 0xEE };

		C1G2BlockPermalock c1g2BP = new C1G2BlockPermalock(
				new TLVParameterHeader((byte) 0), 33333, 111111111L, (byte) 3,
				1234, blockMask);
		int parameterLength = serializer.getLength(c1g2BP);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2BP, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_BLOCK_PERMALOCK.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 33333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);

		// even
		// byte[] dst = {0x10,0x20,(byte)0xFE,0x00};
		// byte[] tryget=new byte[4];
		// data.get(tryget, 0, 4);
		// Assert.assertEquals(dst, tryget);
		// odd
		byte[] dst = new byte[blockMask.length];
		data.get(dst, 0, blockMask.length);
		Assert.assertArrayEquals(dst, blockMask);

		data.rewind();

		// deserialize
		c1g2BP = serializer.deserializeC1G2BlockPermalock(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2BP.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_BLOCK_PERMALOCK);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2BP.getOpSpecID(), 33333);
		Assert.assertEquals(c1g2BP.getAccessPW(), 111111111L);
		Assert.assertEquals(c1g2BP.getmB(), 3);
		Assert.assertEquals(c1g2BP.getBlockPointer(), 1234);
		Assert.assertEquals(c1g2BP.getBlockMaskWordCount(), 2);

		// even
		// Assert.assertEquals(c1g2BP.getBlockMask(), tryget);
		// odd
		Assert.assertArrayEquals(c1g2BP.getBlockMask(), blockMask);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2BP),
				15 + c1g2BP.getBlockMaskWordCount() * 2);
	}

	@Test
	public void c1g2GetBlockPermalockStatus()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2GetBlockPermalockStatus c1g2GBPS = new C1G2GetBlockPermalockStatus(
				new TLVParameterHeader((byte) 0), 33333, 111111111L, (byte) 3,
				1234, 10000);
		int parameterLength = serializer.getLength(c1g2GBPS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2GBPS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_GET_BLOCK_PERMALOCK_STATUS
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 33333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1234);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10000);
		data.rewind();

		// deserialize
		c1g2GBPS = serializer.deserializeC1G2GetBlockPermalockStatus(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2GBPS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_GET_BLOCK_PERMALOCK_STATUS);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2GBPS.getOpSpecID(), 33333);
		Assert.assertEquals(c1g2GBPS.getAccessPW(), 111111111L);
		Assert.assertEquals(c1g2GBPS.getmB(), 3);
		Assert.assertEquals(c1g2GBPS.getBlockPointer(), 1234);
		Assert.assertEquals(c1g2GBPS.getBlockRange(), 10000);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2GBPS), 15);
	}

	@Test
	public void roSpec() throws InvalidParameterTypeException {
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
				ROReportTrigger.UPON_N_SECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC,
				500, tagRS);

		// ROSpec
		List<Parameter> specList = new ArrayList<>();
		specList.add(aiS);
		specList.add(rfSS);
		specList.add(ls);
		ROSpec ros = new ROSpec(new TLVParameterHeader((byte) 0), (long) 9999,
				(short) 1, ROSpecCurrentState.ACTIVE, robs, specList);
		ros.setRoReportSpec(roRS);

		int parameterLength = serializer.getLength(ros);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ros, data);

		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
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
		ros = serializer.deserializeROSpec((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ros.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(ros.getRoSpecID(), 9999);
		Assert.assertEquals(ros.getPriority(), 1);
		Assert.assertEquals(ros.getCurrentState(), ROSpecCurrentState.ACTIVE);

		TLVParameterHeader header = ros.getRoBoundarySpec()
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
		Assert.assertArrayEquals(((RFSurveySpec) ros.getSpecList().get(1))
				.getCusList().get(0).getData(), dataArray);

		header = (TLVParameterHeader) ros.getSpecList().get(2)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.LOOP_SPEC);
		Assert.assertEquals(header.getParameterLength(), 8);

		header = ros.getRoReportSpec().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RO_REPORT_SPEC);
		Assert.assertEquals(header.getParameterLength(), 18);
		Assert.assertEquals(ros.getRoReportSpec().getTagReportContentSelector()
				.getC1g2EPCMemorySelectorList().get(0).isEnableCRC(), true);

		// getLength
		Assert.assertEquals(serializer.getLength(ros), 10 + 53 + 58 + 41 + 8
				+ 18);
	}

	@Test
	public void roBoundarySpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		PeriodicTriggerValue pTV = new PeriodicTriggerValue(
				new TLVParameterHeader((byte) 0), 111111111L, 111111112L);
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		pTV.setUtc(utc);
		ROSpecStartTrigger roSStartT = new ROSpecStartTrigger(
				new TLVParameterHeader((byte) 0), ROSpecStartTriggerType.GPI);
		roSStartT.setPeriodicTV(pTV);

		GPITriggerValue gpiTV = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		ROSpecStopTrigger roSST = new ROSpecStopTrigger(new TLVParameterHeader(
				(byte) 0), ROSpecStopTriggerType.GPI_WITH_TIMEOUT_VALUE,
				(long) 0);
		roSST.setGpiTriggerValue(gpiTV);

		ROBoundarySpec robs = new ROBoundarySpec(new TLVParameterHeader(
				(byte) 0), roSStartT, roSST);

		int parameterLength = serializer.getLength(robs);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(robs, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_BOUNDARY_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_START_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 29);
		byte[] place = new byte[29 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 20);
		data.rewind();

		// deserialize
		robs = serializer.deserializeROBoundarySpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = robs.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_BOUNDARY_SPEC);
		TLVParameterHeader header = robs.getRoSStartTrigger()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RO_SPEC_START_TRIGGER);
		Assert.assertEquals(header.getParameterLength(), 29);

		header = robs.getRoSStopTrigger().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RO_SPEC_STOP_TRIGGER);
		Assert.assertEquals(header.getParameterLength(), 20);

		// getLength
		Assert.assertEquals(serializer.getLength(robs), 4 + 29 + 20);
	}

	@Test
	public void roSpecStartTrigger() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		PeriodicTriggerValue pTV = new PeriodicTriggerValue(
				new TLVParameterHeader((byte) 0), 111111111L, 111111112L);
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		pTV.setUtc(utc);

		ROSpecStartTrigger roSStartT = new ROSpecStartTrigger(
				new TLVParameterHeader((byte) 0), ROSpecStartTriggerType.GPI);
		roSStartT.setPeriodicTV(pTV);
		int parameterLength = serializer.getLength(roSStartT);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(roSStartT, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_START_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 3);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.PERIODIC_TRIGGER_VALUE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 24);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111112L);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UTC_TIMESTAMP.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 12);
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				111111113);
		data.rewind();

		// deserialize
		roSStartT = serializer.deserializeROSpecStartTrigger(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = roSStartT.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_SPEC_START_TRIGGER);
		TLVParameterHeader header = roSStartT.getPeriodicTV()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.PERIODIC_TRIGGER_VALUE);
		Assert.assertEquals(header.getParameterLength(), 24);
		Assert.assertEquals(roSStartT.getPeriodicTV().getOffSet(), 111111111L);
		Assert.assertEquals(roSStartT.getPeriodicTV().getPeriod(), 111111112L);
		Assert.assertEquals(roSStartT.getPeriodicTV().getUtc()
				.getMicroseconds(), new BigInteger("111111113"));

		// getLength
		Assert.assertEquals(serializer.getLength(roSStartT), 5 + 24);
	}

	@Test
	public void periodicTriggerValue() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		PeriodicTriggerValue pTV = new PeriodicTriggerValue(
				new TLVParameterHeader((byte) 0), 111111111L, 111111112L);
		UTCTimestamp utc = new UTCTimestamp(new TLVParameterHeader((byte) 0),
				new BigInteger("111111113"));
		pTV.setUtc(utc);

		int parameterLength = serializer.getLength(pTV);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(pTV, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.PERIODIC_TRIGGER_VALUE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111112L);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UTC_TIMESTAMP.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 12);
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				111111113);
		data.rewind();

		// deserialize
		pTV = serializer.deserializePeriodicTriggerValue(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = pTV.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.PERIODIC_TRIGGER_VALUE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(pTV.getOffSet(), 111111111L);
		Assert.assertEquals(pTV.getPeriod(), 111111112L);
		Assert.assertEquals(pTV.getUtc().getMicroseconds(), new BigInteger(
				"111111113"));

		// getLength
		Assert.assertEquals(serializer.getLength(pTV), 24);
	}

	@Test
	public void aiSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		List<Integer> idList = new ArrayList<>();
		idList.add(1);
		idList.add(2);

		GPITriggerValue gpiTV = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		AISpecStopTrigger aiSST = new AISpecStopTrigger(new TLVParameterHeader(
				(byte) 0), AISpecStopTriggerType.GPI_WITH_TIMEOUT, (long) 0);
		aiSST.setGpiTV(gpiTV);

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

		int parameterLength = serializer.getLength(aiS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(aiS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.AI_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.AI_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 20);
		byte[] place = new byte[20 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.INVENTORY_PARAMETER_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		place = new byte[7 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.INVENTORY_PARAMETER_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		place = new byte[7 - 4];
		data.get(place);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 14);
		data.rewind();

		// deserialize
		aiS = serializer.deserializeAISpec((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = aiS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.AI_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(aiS.getAntennaCount(), 2);
		Assert.assertEquals(aiS.getAntennaIdList().get(0), (Integer) 1);
		Assert.assertEquals(aiS.getAntennaIdList().get(1), (Integer) 2);
		TLVParameterHeader header = aiS.getAiSpecStopTrigger()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.AI_SPEC_STOP_TRIGGER);
		Assert.assertEquals(header.getParameterLength(), 20);
		Assert.assertEquals(aiS.getAiSpecStopTrigger()
				.getAiSpecStopTriggerType(),
				AISpecStopTriggerType.GPI_WITH_TIMEOUT);
		header = aiS.getAiSpecStopTrigger().getGpiTV().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.GPI_TRIGGER_VALUE);
		Assert.assertEquals(header.getParameterLength(), 11);
		header = aiS.getInventoryParameterList().get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.INVENTORY_PARAMETER_SPEC);
		Assert.assertEquals(header.getParameterLength(), 7);

		header = aiS.getCustomList().get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(header.getParameterLength(), 14);

		// getLength
		Assert.assertEquals(serializer.getLength(aiS), 10 + 20 + 7 * 2 + 14);
	}

	@Test
	public void aiSpecStopTrigger() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		GPITriggerValue gpiTV = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		AISpecStopTrigger aiSST = new AISpecStopTrigger(new TLVParameterHeader(
				(byte) 0), AISpecStopTriggerType.GPI_WITH_TIMEOUT, (long) 0);
		aiSST.setGpiTV(gpiTV);
		int parameterLength = serializer.getLength(aiSST);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(aiSST, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.AI_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 2);
		data.getInt();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPI_TRIGGER_VALUE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 11);
		data.rewind();

		// deserialize
		aiSST = serializer.deserializeAISpecStopTrigger(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = aiSST.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.AI_SPEC_STOP_TRIGGER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(aiSST.getAiSpecStopTriggerType(),
				AISpecStopTriggerType.GPI_WITH_TIMEOUT);
		TLVParameterHeader header = aiSST.getGpiTV().getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.GPI_TRIGGER_VALUE);
		Assert.assertEquals(header.getParameterLength(), 11);

		// getLength
		Assert.assertEquals(serializer.getLength(aiSST), 20);
	}

	@Test
	public void roSpecStopTrigger() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		GPITriggerValue gpiTV = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		ROSpecStopTrigger roSST = new ROSpecStopTrigger(new TLVParameterHeader(
				(byte) 0), ROSpecStopTriggerType.GPI_WITH_TIMEOUT_VALUE,
				(long) 0);
		roSST.setGpiTriggerValue(gpiTV);
		int parameterLength = serializer.getLength(roSST);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(roSST, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_SPEC_STOP_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 2);
		data.getInt();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPI_TRIGGER_VALUE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 11);
		data.rewind();

		// deserialize
		roSST = serializer.deserializeROSpecStopTrigger(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = roSST.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_SPEC_STOP_TRIGGER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(roSST.getRoSpecStopTriggerType().getValue(), 2);
		TLVParameterHeader header = roSST.getGpiTriggerValue()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.GPI_TRIGGER_VALUE);
		Assert.assertEquals(header.getParameterLength(), 11);
		Assert.assertEquals(roSST.getGpiTriggerValue().isGpiEvent(), true);
		Assert.assertEquals(roSST.getGpiTriggerValue().getTimeOut(), 111111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(roSST), 20);
	}

	@Test
	public void gpiTriggerValue() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		GPITriggerValue gpiTV = new GPITriggerValue(new TLVParameterHeader(
				(byte) 0), 20, true, 111111111L);
		int parameterLength = serializer.getLength(gpiTV);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(gpiTV, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPI_TRIGGER_VALUE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 20);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		data.rewind();

		// deserialize
		gpiTV = serializer.deserializeGPITriggerValue(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = gpiTV.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.GPI_TRIGGER_VALUE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(gpiTV.getGpiPortNum(), 20);
		Assert.assertEquals(gpiTV.isGpiEvent(), true);
		Assert.assertEquals(gpiTV.getTimeOut(), 111111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(gpiTV), 11);
	}

	@Test
	public void tagObservationTrigger() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		TagObservationTrigger tagOT = new TagObservationTrigger(
				new TLVParameterHeader((byte) 0),
				TagObservationTriggerType.UPON_SEEING_NO_MORE_NEW_TAG_OBSERVATIONS_FOR_T_MS_OR_TIMEOUT,
				1000, 500, 600, 111111111L);
		int parameterLength = serializer.getLength(tagOT);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tagOT, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TAG_OBSERVATION_TRIGGER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 1);
		byte[] place = new byte[5];
		data.get(place);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 600);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		data.rewind();

		// deserialize
		tagOT = serializer.deserializeTagObservationTrigger(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = tagOT.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.TAG_OBSERVATION_TRIGGER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(
				tagOT.getTriggerType(),
				TagObservationTriggerType.UPON_SEEING_NO_MORE_NEW_TAG_OBSERVATIONS_FOR_T_MS_OR_TIMEOUT);
		Assert.assertEquals(tagOT.getT(), 600);
		Assert.assertEquals(tagOT.getTimeOut(), 111111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(tagOT), 16);
	}

	@Test
	public void inventoryParameterSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		InventoryParameterSpec ips = new InventoryParameterSpec(
				new TLVParameterHeader((byte) 0), 4444,
				ProtocolId.EPC_GLOBAL_C1G2);

		AntennaConfiguration ac = new AntennaConfiguration(
				new TLVParameterHeader((byte) 0), 3333);
		List<AntennaConfiguration> antennaConfigList = new ArrayList<>();
		antennaConfigList.add(ac);
		ips.setAntennaConfigList(antennaConfigList);

		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);
		ips.setCusList(cusList);

		int parameterLength = serializer.getLength(ips);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ips, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.INVENTORY_PARAMETER_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 4444);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ProtocolId.EPC_GLOBAL_C1G2.getValue());
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ANTENNA_CONFIGURATION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 3333);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 14);
		data.rewind();

		// deserialize
		ips = serializer.deserializeInventoryParameterSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ips.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.INVENTORY_PARAMETER_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		TLVParameterHeader header = ips.getAntennaConfigList().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.ANTENNA_CONFIGURATION);
		header = ips.getCusList().get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(ips.getCusList().get(0).getVendorID(), 123);

		// getLength
		Assert.assertEquals(serializer.getLength(ips), 7 + 6 + 14);
	}

	@Test
	public void rfSurveySpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		RFSurveySpecStopTrigger rfSSST = new RFSurveySpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				RFSurveySpecStopTriggerType.DURATION, 111111111L, (long) 20);
		RFSurveySpec rfSS = new RFSurveySpec(new TLVParameterHeader((byte) 0),
				6666, 33333333, 44444444, rfSSST);

		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);

		rfSS.setCusList(cusList);

		int parameterLength = serializer.getLength(rfSS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfSS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6666);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 33333333);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 44444444);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_SPEC_STOP_TRIGGER
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 13);
		byte[] place = new byte[13 - 4];
		data.get(place);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 14);
		data.rewind();

		// deserialize
		rfSS = serializer.deserializeRFSurveySpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfSS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_SURVEY_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		TLVParameterHeader header = rfSS.getRfSSStopTrigger()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RF_SURVEY_SPEC_STOP_TRIGGER);
		header = rfSS.getCusList().get(0).getParameterHeader();
		Assert.assertEquals(header.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(rfSS.getRfSSStopTrigger().getTriggerType(),
				RFSurveySpecStopTriggerType.DURATION);
		Assert.assertEquals(rfSS.getRfSSStopTrigger().getDuration(), 111111111L);
		Assert.assertEquals(rfSS.getCusList().get(0).getVendorID(), 123);

		// getLength
		Assert.assertEquals(serializer.getLength(rfSS), 14 + 13 + 14);
	}

	@Test
	public void rfSurveySpecStopTrigger() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		RFSurveySpecStopTrigger rfSSST = new RFSurveySpecStopTrigger(
				new TLVParameterHeader((byte) 0),
				RFSurveySpecStopTriggerType.DURATION, 111111111L, (long) 20);
		int parameterLength = serializer.getLength(rfSSST);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfSSST, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_SPEC_STOP_TRIGGER
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 1);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		data.rewind();

		// deserialize
		rfSSST = serializer.deserializeRFSurveySpecStopTrigger(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfSSST.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_SURVEY_SPEC_STOP_TRIGGER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rfSSST.getTriggerType(),
				RFSurveySpecStopTriggerType.DURATION);
		Assert.assertEquals(rfSSST.getDuration(), 111111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(rfSSST), 13);
	}

	@Test
	public void loopSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		LoopSpec ls = new LoopSpec(new TLVParameterHeader((byte) 0), 111111111L);
		int parameterLength = serializer.getLength(ls);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ls, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.LOOP_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 111111111L);
		data.rewind();

		// deserialize
		ls = serializer.deserializeLoopSpec((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ls.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.LOOP_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(ls.getLoopCount(), 111111111L);

		// getLength
		Assert.assertEquals(serializer.getLength(ls), 8);
	}

	@Test
	public void accessReportSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AccessReportSpec ars = new AccessReportSpec(new TLVParameterHeader(
				(byte) 0), AccessReportTrigger.END_OF_ACCESSSPEC);
		int parameterLength = serializer.getLength(ars);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ars, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ACCESS_REPORT_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 1);
		data.rewind();

		// deserialize
		ars = serializer.deserializeAccessReportSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ars.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ACCESS_REPORT_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(ars.getAccessReportTrigger(),
				AccessReportTrigger.END_OF_ACCESSSPEC);
		;

		// getLength
		Assert.assertEquals(serializer.getLength(ars), 5);
	}

	@Test
	public void c1g2EPCMemorySelector() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2EPCMemorySelector epcMS = new C1G2EPCMemorySelector(
				new TLVParameterHeader((byte) 0), true, true, true);
		int parameterLength = serializer.getLength(epcMS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(epcMS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_EPC_MEMORY_SELECTOR.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128 + 64 + 32);
		data.rewind();

		// deserialize
		epcMS = serializer.deserializeC1G2EPCMemorySelector(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = epcMS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_EPC_MEMORY_SELECTOR);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(epcMS.isEnableCRC(), true);
		Assert.assertEquals(epcMS.isEnablePCBits(), true);
		Assert.assertEquals(epcMS.isEnableXPCBits(), true);

		// getLength
		Assert.assertEquals(serializer.getLength(epcMS), 5);
	}

	@Test
	public void tagReportContentSelector() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2EPCMemorySelector epcMS = new C1G2EPCMemorySelector(
				new TLVParameterHeader((byte) 0), true, true, true);
		List<C1G2EPCMemorySelector> selectorList = new ArrayList<>();
		selectorList.add(epcMS);

		byte[] dataArray = { 100, -100 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 123, 456,
				dataArray);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);

		TagReportContentSelector tagRS = new TagReportContentSelector(
				new TLVParameterHeader((byte) 0), true, false, true, false,
				true, false, true, false, true, false);
		tagRS.setC1g2EPCMemorySelectorList(selectorList);
		tagRS.setCusList(cusList);

		int parameterLength = serializer.getLength(tagRS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tagRS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TAG_REPORT_CONTENT_SELECTOR
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 170);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_EPC_MEMORY_SELECTOR.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128 + 64 + 32);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 14);
		data.rewind();

		// deserialize
		tagRS = serializer.deserializeTagReportContentSelector(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = tagRS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.TAG_REPORT_CONTENT_SELECTOR);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(tagRS.isEnableROSpecID(), true);
		Assert.assertEquals(tagRS.isEnableSpecIndex(), !true);
		Assert.assertEquals(tagRS.isEnableInventoryParameterSpecID(), true);
		Assert.assertEquals(tagRS.isEnableAntennaID(), !true);
		Assert.assertEquals(tagRS.isEnableChannelIndex(), true);
		Assert.assertEquals(tagRS.isEnablePeakRSSI(), !true);
		Assert.assertEquals(tagRS.isEnableFirstSeenTimestamp(), true);
		Assert.assertEquals(tagRS.isEnableLastSeenTimestamp(), !true);
		Assert.assertEquals(tagRS.isEnableTagSeenCount(), true);
		Assert.assertEquals(tagRS.isEnableAccessSpecID(), !true);

		Assert.assertEquals(tagRS.getC1g2EPCMemorySelectorList().get(0)
				.getParameterHeader().getParameterType(),
				ParameterType.C1G2_EPC_MEMORY_SELECTOR);
		Assert.assertEquals(tagRS.getC1g2EPCMemorySelectorList().get(0)
				.isEnableCRC(), true);

		Assert.assertEquals(tagRS.getCusList().get(0).getParameterHeader()
				.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(tagRS.getCusList().get(0).getSubType(), 456);
		Assert.assertEquals(tagRS.getCusList().get(0).getVendorID(), 123);
		Assert.assertArrayEquals(tagRS.getCusList().get(0).getData(), dataArray);

		// getLength
		Assert.assertEquals(serializer.getLength(tagRS), 25);
	}

	@Test
	public void roReportSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
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
				ROReportTrigger.UPON_N_SECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC,
				500, tagRS);
		int parameterLength = serializer.getLength(roRS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(roRS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RO_REPORT_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 3);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 500);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TAG_REPORT_CONTENT_SELECTOR
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 11);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 170);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_EPC_MEMORY_SELECTOR.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128 + 64 + 32);

		data.rewind();

		// deserialize
		roRS = serializer.deserializeROReportSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = roRS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RO_REPORT_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(
				roRS.getRoReportTrigger(),
				ROReportTrigger.UPON_N_SECONDS_OR_END_OF_AISPEC_OR_END_OF_RFSURVEYSPEC);
		Assert.assertEquals(roRS.getN(), 500);

		TLVParameterHeader header = roRS.getTagReportContentSelector()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterLength(), 11);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableROSpecID(), true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableSpecIndex(), !true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableInventoryParameterSpecID(), true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableAntennaID(), !true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableChannelIndex(), true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnablePeakRSSI(), !true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableFirstSeenTimestamp(), true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableLastSeenTimestamp(), !true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableTagSeenCount(), true);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.isEnableAccessSpecID(), !true);

		Assert.assertEquals(roRS.getTagReportContentSelector()
				.getC1g2EPCMemorySelectorList().get(0).getParameterHeader()
				.getParameterType(), ParameterType.C1G2_EPC_MEMORY_SELECTOR);
		Assert.assertEquals(roRS.getTagReportContentSelector()
				.getC1g2EPCMemorySelectorList().get(0).isEnableCRC(), true);

		// getLength
		Assert.assertEquals(serializer.getLength(roRS), 18);
	}

	@Test
	public void eventNotificationState() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		EventNotificationState eventNotificationState = new EventNotificationState(
				new TLVParameterHeader((byte) 0),
				EventNotificationStateEventType.AISPEC_EVENT, true);
		int parameterLength = serializer.getLength(eventNotificationState);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(eventNotificationState, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EVENT_NOTIFICATION_STATE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				EventNotificationStateEventType.AISPEC_EVENT.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		data.rewind();

		// deserialize
		eventNotificationState = serializer.deserializeEventNotificationState(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = eventNotificationState
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.EVENT_NOTIFICATION_STATE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		Assert.assertEquals(eventNotificationState.getEventType(),
				EventNotificationStateEventType.AISPEC_EVENT);
		Assert.assertEquals(eventNotificationState.isNotificationState(), true);
		// getLength
		Assert.assertEquals(serializer.getLength(eventNotificationState), 7);
	}

	@Test
	public void readerEventNotificationSpec()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
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
		int parameterLength = serializer.getLength(rENS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rENS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.READER_EVENT_NOTIFICATION_SPEC
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EVENT_NOTIFICATION_STATE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				EventNotificationStateEventType.AISPEC_EVENT.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EVENT_NOTIFICATION_STATE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7);
		Assert.assertEquals(
				DataTypeConverter.ushort(data.getShort()),
				EventNotificationStateEventType.AISPEC_EVENT_WITH_SINGULATION_DETAILS
						.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		data.rewind();

		// deserialize
		rENS = serializer.deserializeReaderEventNotificationSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rENS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.READER_EVENT_NOTIFICATION_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		TLVParameterHeader header = rENS.getEventNotificationStateList().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.EVENT_NOTIFICATION_STATE);
		Assert.assertEquals(header.getParameterLength(), 7);

		Assert.assertEquals(rENS.getEventNotificationStateList().get(0)
				.getEventType(), EventNotificationStateEventType.AISPEC_EVENT);
		Assert.assertEquals(rENS.getEventNotificationStateList().get(0)
				.isNotificationState(), true);

		header = rENS.getEventNotificationStateList().get(1)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.EVENT_NOTIFICATION_STATE);
		Assert.assertEquals(header.getParameterLength(), 7);

		Assert.assertEquals(
				rENS.getEventNotificationStateList().get(1).getEventType(),
				EventNotificationStateEventType.AISPEC_EVENT_WITH_SINGULATION_DETAILS);
		Assert.assertEquals(rENS.getEventNotificationStateList().get(1)
				.isNotificationState(), false);

		// getLength
		Assert.assertEquals(serializer.getLength(rENS), 18);
	}

	@Test
	public void eventsAndReports() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		Boolean h = true;
		EventsAndReports eAR = new EventsAndReports(new TLVParameterHeader(
				(byte) 0), h);
		int parameterLength = serializer.getLength(eAR);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(eAR, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.EVENTS_AND_REPORTS.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		data.rewind();

		// deserialize
		eAR = serializer.deserializeEventsAndReports(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = eAR.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.EVENTS_AND_REPORTS);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(), 5);

		Assert.assertEquals(eAR.getHold(), true);
		// getLength
		Assert.assertEquals(serializer.getLength(eAR), 5);
	}

	@Test
	public void gpiPortCurrentState() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		GPIPortCurrentState gpiCS = new GPIPortCurrentState(
				new TLVParameterHeader((byte) 0), 1111, false,
				GPIPortCurrentStateGPIState.UNKNOWN);
		int parameterLength = serializer.getLength(gpiCS);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(gpiCS, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPI_PORT_CURRENT_STATE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1111);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 2);
		data.rewind();

		// deserialize
		gpiCS = serializer.deserializeGPIPortCurrentState(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = gpiCS.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.GPI_PORT_CURRENT_STATE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(gpiCS.getGpiPortNum(), 1111);
		Assert.assertEquals(gpiCS.getGpiConfig(), false);
		Assert.assertEquals(gpiCS.getState(),
				GPIPortCurrentStateGPIState.UNKNOWN);
		// getLength
		Assert.assertEquals(serializer.getLength(gpiCS), 8);
	}

	@Test
	public void rfTransmitter() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		RFTransmitter rft = new RFTransmitter(new TLVParameterHeader((byte) 0),
				1000, 500, 1500);
		int parameterLength = serializer.getLength(rft);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rft, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_TRANSMITTER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 500);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1500);

		data.rewind();

		// deserialize
		rft = serializer.deserializeRFTransmitter(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rft.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_TRANSMITTER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rft.getHopTableID(), 1000);
		Assert.assertEquals(rft.getChannelIndex(), 500);
		Assert.assertEquals(rft.getTransmitPower(), 1500);
		// getLength
		Assert.assertEquals(serializer.getLength(rft), 10);
	}

	@Test
	public void rfReceiver() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		RFReceiver rfr = new RFReceiver(new TLVParameterHeader((byte) 0), 10000);
		int parameterLength = serializer.getLength(rfr);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfr, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_RECEIVER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10000);
		data.rewind();

		// deserialize
		rfr = serializer.deserializeRFReceiver((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfr.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_RECEIVER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rfr.getReceiverSensitivity(), 10000);

		// getLength
		Assert.assertEquals(serializer.getLength(rfr), 6);
	}

	@Test
	public void c1g2TagInventoryStateAwareSingulationAction()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2TagInventoryStateAwareSingulationAction iSASA = new C1G2TagInventoryStateAwareSingulationAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareSingulationActionI.STATE_B,
				C1G2TagInventoryStateAwareSingulationActionS.SL,
				C1G2TagInventoryStateAwareSingulationActionSAll.ALL);
		int parameterLength = serializer.getLength(iSASA);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(iSASA, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 160);
		data.rewind();

		// deserialize
		iSASA = serializer
				.deserializeC1G2TagInventoryStateAwareSingulationAction(
						(TLVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = iSASA.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(iSASA.getI(),
				C1G2TagInventoryStateAwareSingulationActionI.STATE_B);
		Assert.assertEquals(iSASA.getS(),
				C1G2TagInventoryStateAwareSingulationActionS.SL);
		Assert.assertEquals(iSASA.getA(),
				C1G2TagInventoryStateAwareSingulationActionSAll.ALL);

		// getLength
		Assert.assertEquals(serializer.getLength(iSASA), 5);
	}

	@Test
	public void c1g2SingulationControl() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2TagInventoryStateAwareSingulationAction iSASA = new C1G2TagInventoryStateAwareSingulationAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareSingulationActionI.STATE_B,
				C1G2TagInventoryStateAwareSingulationActionS.SL,
				C1G2TagInventoryStateAwareSingulationActionSAll.ALL);
		C1G2SingulationControl sControl = new C1G2SingulationControl(
				new TLVParameterHeader((byte) 0), (byte) 2, 100, 100000000L);
		sControl.setC1g2TagInventoryStateAwareSingulationAction(iSASA);
		int parameterLength = serializer.getLength(sControl);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(sControl, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_SINGULATION_CONTROL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 100000000L);
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);

		data.rewind();

		// deserialize
		sControl = serializer.deserializeC1G2SingulationControl(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = sControl.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_SINGULATION_CONTROL);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(sControl.getSession(), 2);
		Assert.assertEquals(sControl.getTagPopulation(), 100);
		Assert.assertEquals(sControl.getTagTransitTime(), 100000000L);

		TLVParameterHeader header = iSASA.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION);
		Assert.assertEquals(header.getParameterLength(), 5);
		// getLength
		Assert.assertEquals(serializer.getLength(sControl), 5 + 11);
	}

	@Test
	public void c1g2RFControl() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2RFControl rfControl = new C1G2RFControl(new TLVParameterHeader(
				(byte) 0), 6000, 8000);
		int parameterLength = serializer.getLength(rfControl);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfControl, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_RF_CONTROL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6000);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8000);
		data.rewind();

		// deserialize
		rfControl = serializer.deserializeC1G2RFControl(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfControl.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_RF_CONTROL);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rfControl.getModelIndex(), 6000);
		Assert.assertEquals(rfControl.getTari(), 8000);

		// getLength
		Assert.assertEquals(serializer.getLength(rfControl), 8);
	}

	@Test
	public void c1g2TagInventoryStateUnawareFilterAction()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2TagInventoryStateUnawareFilterAction tISUFA = new C1G2TagInventoryStateUnawareFilterAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT);
		int parameterLength = serializer.getLength(tISUFA);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tISUFA, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT
						.getValue());
		data.rewind();

		// deserialize
		tISUFA = serializer
				.deserializeC1G2TagInventoryStateUnawareFilterAction(
						(TLVParameterHeader) serializer
								.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = tISUFA.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(tISUFA.getAction(),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT);

		// getLength
		Assert.assertEquals(serializer.getLength(tISUFA), 5);
	}

	@Test
	public void c1g2TagInventoryStateAwareFilterAction()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		C1G2TagInventoryStateAwareFilterAction tISAFA = new C1G2TagInventoryStateAwareFilterAction(
				new TLVParameterHeader((byte) 0),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0,
				(short) 155);
		int parameterLength = serializer.getLength(tISAFA);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tISAFA, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(
				DataTypeConverter.ubyte(data.get()),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0
						.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 155);
		data.rewind();

		// deserialize
		tISAFA = serializer.deserializeC1G2TagInventoryStateAwareFilterAction(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = tISAFA.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(
				tISAFA.getTarget(),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0);
		Assert.assertEquals(tISAFA.getAction(), 155);

		// getLength
		Assert.assertEquals(serializer.getLength(tISAFA), 6);
	}

	@Test
	public void c1g2TagInventoryMask() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		// 11110000 10101010 00000001 00000000
		BitSet mask = new BitSet();
		mask.set(0);
		mask.set(1);
		mask.set(2);
		mask.set(3);
		mask.set(8);
		mask.set(10);
		mask.set(12);
		mask.set(14);
		mask.set(23);
		C1G2TagInventoryMask tIMask = new C1G2TagInventoryMask(
				new TLVParameterHeader((byte) 0), (byte) 3, 100, mask);
		tIMask.setMaskBitCount(32);
		int parameterLength = serializer.getLength(tIMask);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(tIMask, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_MASK.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);

		byte[] maskArray = { (byte) 0b11110000, (byte) 0b10101010,
				(byte) 0b00000001, (byte) 0b00000000 };
		byte[] dst = new byte[maskArray.length];
		data.get(dst, 0, maskArray.length);
		Assert.assertArrayEquals(dst, maskArray);
		data.rewind();

		// deserialize
		tIMask = serializer.deserializeC1G2TagInventoryMask(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = tIMask.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_MASK);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(tIMask.getmB(), 3);
		Assert.assertEquals(tIMask.getPointer(), 100);
		Assert.assertEquals(tIMask.getMaskBitCount(), 32);
		Assert.assertEquals(tIMask.getTagMask(), mask);

		// getLength
		Assert.assertEquals(serializer.getLength(tIMask), 13);
	}

	@Test
	public void c1g2Filter() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
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

		int parameterLength = serializer.getLength(filter);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(filter, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_FILTER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_MASK.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 11);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		data.getShort();

		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		Assert.assertEquals(
				DataTypeConverter.ubyte(data.get()),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0
						.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 165);

		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT
						.getValue());
		data.rewind();

		// deserialize
		filter = serializer.deserializeC1G2Filter(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = filter.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_FILTER);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(filter.getT(), C1G2FilterTruncateAction.TRUNCATE);
		TLVParameterHeader header = filter.getC1g2TagInventoryMask()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_MASK);
		Assert.assertEquals(header.getParameterLength(), 11);
		Assert.assertEquals(filter.getC1g2TagInventoryMask().getMaskBitCount(),
				16);
		Assert.assertEquals(filter.getC1g2TagInventoryMask().getTagMask(), mask);

		header = filter.getC1g2TagInventoryStateAwareFilterAction()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION);
		Assert.assertEquals(header.getParameterLength(), 6);

		header = filter.getC1g2TagInventoryStateUnawareFilterAction()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION);
		Assert.assertEquals(header.getParameterLength(), 5);
		// getLength
		Assert.assertEquals(serializer.getLength(filter), 27);
	}

	@Test
	public void c1g2InventoryCommand() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
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

		int parameterLength = serializer.getLength(iCommand);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(iCommand, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_INVENTORY_COMMAND.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_FILTER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 27);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_MASK.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 11);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		data.getShort();

		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		Assert.assertEquals(
				DataTypeConverter.ubyte(data.get()),
				C1G2TagInventoryStateAwareFilterActionTarget.INVENTORIED_STATE_FOR_SESSION_S0
						.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 165);

		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				C1G2TagInventoryStateUnawareFilterActionValues.SELECT__UNSELECT
						.getValue());

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_SINGULATION_CONTROL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 100);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 100000000L);
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 5);
		data.rewind();

		// deserialize
		iCommand = serializer.deserializeC1G2InventoryCommand(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = iCommand.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_INVENTORY_COMMAND);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(iCommand.isTagInventStateAware(), true);
		TLVParameterHeader header = iCommand.getC1g2FilterList().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_FILTER);
		Assert.assertEquals(header.getParameterLength(), 27);
		Assert.assertEquals(iCommand.getC1g2FilterList().get(0).getT(),
				C1G2FilterTruncateAction.TRUNCATE);

		header = iCommand.getC1g2FilterList().get(0).getC1g2TagInventoryMask()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_MASK);
		Assert.assertEquals(header.getParameterLength(), 11);
		Assert.assertEquals(iCommand.getC1g2FilterList().get(0)
				.getC1g2TagInventoryMask().getMaskBitCount(), 16);
		Assert.assertEquals(iCommand.getC1g2FilterList().get(0)
				.getC1g2TagInventoryMask().getTagMask(), mask);

		header = iCommand.getC1g2FilterList().get(0)
				.getC1g2TagInventoryStateAwareFilterAction()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION);
		Assert.assertEquals(header.getParameterLength(), 6);

		header = iCommand.getC1g2FilterList().get(0)
				.getC1g2TagInventoryStateUnawareFilterAction()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION);
		Assert.assertEquals(header.getParameterLength(), 5);

		header = iCommand.getC1g2SingulationControl().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_SINGULATION_CONTROL);
		Assert.assertEquals(header.getParameterLength(), 16);
		header = iCommand.getC1g2SingulationControl()
				.getC1g2TagInventoryStateAwareSingulationAction()
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION);
		Assert.assertEquals(header.getParameterLength(), 5);

		// getLength
		Assert.assertEquals(serializer.getLength(iCommand), 48);
	}

	@Test
	public void antennaConfiguration() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		RFReceiver rfr = new RFReceiver(new TLVParameterHeader((byte) 0), 10000);
		RFTransmitter rft = new RFTransmitter(new TLVParameterHeader((byte) 0),
				1000, 500, 1500);
		byte[] cusData = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x05, 0x05 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus);

		int antennaID = 1111;
		AntennaConfiguration ac = new AntennaConfiguration(
				new TLVParameterHeader((byte) 0), antennaID);
		ac.setRfReceiver(rfr);
		ac.setRfTransmitter(rft);
		ac.setCustomList(cusList);

		int parameterLength = serializer.getLength(ac);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ac, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ANTENNA_CONFIGURATION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1111);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_RECEIVER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		data.getShort();

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_TRANSMITTER.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		data.getShort();
		data.getInt();

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 19);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		byte[] dst = new byte[7];
		data.get(dst, 0, 7);
		Assert.assertArrayEquals(dst, cusData);
		data.rewind();

		// deserialize
		ac = serializer.deserializeAntennaConfiguration(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ac.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ANTENNA_CONFIGURATION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(ac.getAntennaID(), 1111);

		TLVParameterHeader header = ac.getRfReceiver().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RF_RECEIVER);
		Assert.assertEquals(header.getParameterLength(), 6);
		Assert.assertEquals(ac.getRfReceiver().getReceiverSensitivity(), 10000);

		header = ac.getRfTransmitter().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RF_TRANSMITTER);
		Assert.assertEquals(header.getParameterLength(), 10);
		Assert.assertEquals(ac.getRfTransmitter().getHopTableID(), 1000);

		header = ac.getCustomList().get(0).getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(header.getParameterLength(), 19);

		// getLength
		Assert.assertEquals(serializer.getLength(ac), 19 + 10 + 6 + 6);
	}

	@Test
	public void antennaProperties() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		AntennaProperties ap = new AntennaProperties(new TLVParameterHeader(
				(byte) 0), true, 1000, (short) -500);
		int parameterLength = serializer.getLength(ap);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ap, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.ANTENNA_PROPERTIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 128);

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(data.getShort(), -500);
		data.rewind();

		// deserialize
		ap = serializer.deserializeAntennaProperties(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ap.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.ANTENNA_PROPERTIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(), 9);
		Assert.assertEquals(ap.isConnected(), true);
		Assert.assertEquals(ap.getAntennaGain(), -500);
		Assert.assertEquals(ap.getAntennaID(), 1000);
		// getLength
		Assert.assertEquals(serializer.getLength(ap), 9);
	}

	@Test
	public void keepaliveSpec() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		KeepaliveSpec kas = new KeepaliveSpec(new TLVParameterHeader((byte) 0),
				KeepaliveSpecTriggerType.PERIODIC, 4000000000L);
		int parameterLength = serializer.getLength(kas);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(kas, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.KEEPALIVE_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), (short) 1);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000000L);
		data.rewind();

		// deserialize
		kas = serializer.deserializeKeepaliveSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = kas.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.KEEPALIVE_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(), 9);
		Assert.assertEquals(kas.getTriggerType(),
				KeepaliveSpecTriggerType.PERIODIC);
		Assert.assertEquals(kas.getTimeInterval(), 4000000000L);

		// getLength
		Assert.assertEquals(serializer.getLength(kas), 9);
	}

	@Test
	public void keepaliveSpec1() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		KeepaliveSpec kas = new KeepaliveSpec(new TLVParameterHeader((byte) 0),
				KeepaliveSpecTriggerType.NULL);
		int parameterLength = serializer.getLength(kas);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(kas, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.KEEPALIVE_SPEC.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), (short) 0);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 0);
		data.rewind();

		// deserialize
		kas = serializer.deserializeKeepaliveSpec(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = kas.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.KEEPALIVE_SPEC);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(), 9);
		Assert.assertEquals(kas.getTriggerType(), KeepaliveSpecTriggerType.NULL);
		Assert.assertEquals(kas.getTimeInterval(), 0);

		// getLength
		Assert.assertEquals(serializer.getLength(kas), 9);
	}

	@Test
	public void gpoWriteData() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		GPOWriteData gpo = new GPOWriteData(new TLVParameterHeader((byte) 0),
				2222, false);
		int parameterLength = serializer.getLength(gpo);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(gpo, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPO_WRITE_DATA.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2222);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		data.rewind();

		// deserialize
		gpo = serializer.deserializeGPOWriteData(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = gpo.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.GPO_WRITE_DATA);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(gpo.getGpoPortNum(), 2222);
		Assert.assertFalse(gpo.getGpoState());

		// getLength
		Assert.assertEquals(serializer.getLength(gpo), 7);
	}

	@Test
	public void identification() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		byte[] readerID = { 12, 34 };
		Identification ident = new Identification(new TLVParameterHeader(
				(byte) 0), IdentificationIDType.EPC, readerID);
		int parameterLength = serializer.getLength(ident);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(ident, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.IDENTIFICATION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);

		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				IdentificationIDType.EPC.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				readerID.length);
		byte[] dst = new byte[readerID.length];
		data.get(dst, 0, readerID.length);
		Assert.assertArrayEquals(dst, readerID);
		data.rewind();

		// deserialize
		ident = serializer.deserializeIdentification(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = ident.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.IDENTIFICATION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(ident.getByteCount(), 2);
		Assert.assertEquals(ident.getIdType(), IdentificationIDType.EPC);
		Assert.assertArrayEquals(ident.getReaderID(), readerID);

		// getLength
		Assert.assertEquals(serializer.getLength(ident), 9);
	}

	@Test
	public void llrpConfigurationStateValue()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		LLRPConfigurationStateValue llrpCSV = new LLRPConfigurationStateValue(
				new TLVParameterHeader((byte) 0), 222222222L);
		int parameterLength = serializer.getLength(llrpCSV);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(llrpCSV, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.LLRP_CONFIGURATION_STATE_VALUE
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 222222222L);
		data.rewind();

		// deserialize
		llrpCSV = serializer.deserializeLLRPConfigurationStateValue(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = llrpCSV.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.LLRP_CONFIGURATION_STATE_VALUE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(llrpCSV.getLlrpConfigStateValue(), 222222222L);

		// getLength
		Assert.assertEquals(serializer.getLength(llrpCSV), 8);
	}

	@Test
	public void c1g2LLRPCapabilities() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		Boolean canSupportBlockErase = true;
		Boolean canSupportBlockWrite = true;
		Boolean canSupportBlockPermalock = true;
		Boolean canSupportTagRecommissioning = true;
		Boolean canSupportUMIMethod2 = true;
		Boolean canSupportXPC = true;
		int maxNumSelectFiltersPerQuery = 12345;
		C1G2LLRPCapabilities c1g2LLRPCap = new C1G2LLRPCapabilities(
				new TLVParameterHeader((byte) 0), canSupportBlockErase,
				canSupportBlockWrite, canSupportBlockPermalock,
				canSupportTagRecommissioning, canSupportUMIMethod2,
				canSupportXPC, maxNumSelectFiltersPerQuery);
		int parameterLength = serializer.getLength(c1g2LLRPCap);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(c1g2LLRPCap, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.C1G2_LLRP_CAPABILITIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 252);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 12345);
		data.rewind();

		// deserialize
		c1g2LLRPCap = serializer.deserializeC1G2LLRPCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = c1g2LLRPCap
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.C1G2_LLRP_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(c1g2LLRPCap.getMaxNumSelectFiltersPerQuery(), 12345);
		Assert.assertEquals(c1g2LLRPCap.getCanSupportBlockErase(),
				(Boolean) true);
		Assert.assertEquals(c1g2LLRPCap.getCanSupportBlockPermalock(),
				(Boolean) true);
		Assert.assertEquals(c1g2LLRPCap.getCanSupportBlockWrite(),
				(Boolean) true);
		Assert.assertEquals(c1g2LLRPCap.getCanSupportTagRecommissioning(),
				(Boolean) true);
		Assert.assertEquals(c1g2LLRPCap.getCanSupportUMIMethod2(),
				(Boolean) true);
		Assert.assertEquals(c1g2LLRPCap.getCanSupportXPC(), (Boolean) true);

		// getLength
		Assert.assertEquals(serializer.getLength(c1g2LLRPCap), 7);
	}

	@Test
	public void cUstom() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		byte[] cusData = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x05, 0x05 };
		Custom cus = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData);

		int parameterLength = serializer.getLength(cus);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(cus, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		byte[] dst = new byte[7];
		data.get(dst, 0, 7);
		Assert.assertArrayEquals(dst, cusData);
		data.rewind();

		// deserialize
		cus = serializer.deserializeCustom((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = cus.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.CUSTOM);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(cus.getVendorID(), 1000);
		Assert.assertEquals(cus.getSubType(), 2000);
		Assert.assertArrayEquals(cus.getData(), cusData);
		// getLength
		Assert.assertEquals(serializer.getLength(cus), 19);
	}

	@Test
	public void regulatoryCapabilities() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		byte[] cusData1 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Custom cus1 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData1);
		byte[] cusData2 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
		Custom cus2 = new Custom(new TLVParameterHeader((byte) 0), 1000, 2000,
				cusData2);
		List<Custom> cusList = new ArrayList<>();
		cusList.add(cus1);
		cusList.add(cus2);
		RegulatoryCapabilities regCap = new RegulatoryCapabilities(
				new TLVParameterHeader((byte) 0),
				CountryCodes.CountryCode.AFGHANISTAN,
				CommunicationsStandard.AUSTRALIA_LIPD_1W);
		regCap.setCustomExtensionPoint(cusList);

		int parameterLength = serializer.getLength(regCap);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(regCap, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.REGULATORY_CAPABILITIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				CountryCodes.CountryCode.AFGHANISTAN.getValue());
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				CommunicationsStandard.AUSTRALIA_LIPD_1W.getValue());
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 17);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		byte[] dst = new byte[5];
		data.get(dst, 0, 5);
		Assert.assertArrayEquals(dst, cusData1);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.CUSTOM.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 18);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 2000);
		dst = new byte[6];
		data.get(dst, 0, 6);
		Assert.assertArrayEquals(dst, cusData2);
		data.rewind();

		// deserialize
		regCap = serializer.deserializeRegulatoryCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = regCap.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.REGULATORY_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(regCap.getCountryCode(),
				CountryCodes.CountryCode.AFGHANISTAN);
		Assert.assertEquals(regCap.getCommunicationsStandard(),
				CommunicationsStandard.AUSTRALIA_LIPD_1W);
		Assert.assertEquals(regCap.getUhfBandCapabilities(), null);

		TLVParameterHeader cusHeader = regCap.getCustomExtensionPoint().get(0)
				.getParameterHeader();
		Assert.assertEquals(cusHeader.getReserved(), 0);
		Assert.assertEquals(cusHeader.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(cusHeader.getParameterLength(), 17);

		cusHeader = regCap.getCustomExtensionPoint().get(1)
				.getParameterHeader();
		Assert.assertEquals(cusHeader.getReserved(), 0);
		Assert.assertEquals(cusHeader.getParameterType(), ParameterType.CUSTOM);
		Assert.assertEquals(cusHeader.getParameterLength(), 18);
		// getLength
		Assert.assertEquals(serializer.getLength(regCap), 43);
	}

	@Test
	public void uhfBandCapabilities() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		// TransmitPowerLevelTableEntry
		TransmitPowerLevelTableEntry transPLTE1 = new TransmitPowerLevelTableEntry(
				new TLVParameterHeader((byte) 0), 1, (short) -100);
		TransmitPowerLevelTableEntry transPLTE2 = new TransmitPowerLevelTableEntry(
				new TLVParameterHeader((byte) 0), 2, (short) 100);
		List<TransmitPowerLevelTableEntry> transmitPowerTable = new ArrayList<>();
		transmitPowerTable.add(transPLTE1);
		transmitPowerTable.add(transPLTE2);
		// FrequencyInformation
		List<Long> fixedFreq = new ArrayList<>();
		fixedFreq.add(77777777L);
		FixedFrequencyTable fixedFT = new FixedFrequencyTable(
				new TLVParameterHeader((byte) 0), fixedFreq);
		FrequencyInformation fiF = new FrequencyInformation(
				new TLVParameterHeader((byte) 0), fixedFT);
		// UHFC1G2RFModeTable
		long modeIdentifier = 4000000000L;
		UHFC1G2RFModeTableEntryDivideRatio drValue = UHFC1G2RFModeTableEntryDivideRatio._64DIV3;
		boolean epcHAGConformance = true;
		UHFC1G2RFModeTableEntryModulation mValue = UHFC1G2RFModeTableEntryModulation._2;
		UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModukation = UHFC1G2RFModeTableEntryForwardLinkModulation.PR_ASK;
		UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator = UHFC1G2RFModeTableEntrySpectralMaskIndicator.SINGLE_INTERROGATOR_MODE_MASK;
		int bdrValue = 10001;
		int pieValue = 10002;
		int minTariValue = 10003;
		int maxTariValue = 10004;
		int stepTariValue = 10005;
		UHFC1G2RFModeTableEntry uhfRFMTE = new UHFC1G2RFModeTableEntry(
				new TLVParameterHeader((byte) 0), modeIdentifier, drValue,
				epcHAGConformance, mValue, forwardLinkModukation,
				spectralMaskIndicator, bdrValue, pieValue, minTariValue,
				maxTariValue, stepTariValue);

		List<UHFC1G2RFModeTableEntry> uhfC1G2RFModeSet = new ArrayList<>();
		uhfC1G2RFModeSet.add(uhfRFMTE);
		UHFC1G2RFModeTable uhfRFMTable1 = new UHFC1G2RFModeTable(
				new TLVParameterHeader((byte) 0), uhfC1G2RFModeSet);
		UHFC1G2RFModeTable uhfRFMTable2 = new UHFC1G2RFModeTable(
				new TLVParameterHeader((byte) 0), uhfC1G2RFModeSet);
		List<UHFC1G2RFModeTable> uhfC1G2RFModeTable = new ArrayList<>();
		uhfC1G2RFModeTable.add(uhfRFMTable1);
		uhfC1G2RFModeTable.add(uhfRFMTable2);
		// RFSurveyFrequencyCapabilities
		long min = 999999999L;
		long max = 4294967295L;
		RFSurveyFrequencyCapabilities rfSFCap = new RFSurveyFrequencyCapabilities(
				new TLVParameterHeader((byte) 0), min, max);
		// UHFBandCapabilities
		UHFBandCapabilities uhfBandCap = new UHFBandCapabilities(
				new TLVParameterHeader((byte) 0), transmitPowerTable, fiF,
				uhfC1G2RFModeTable);
		uhfBandCap.setRfSurveyFrequencyCapabilities(rfSFCap);

		int parameterLength = serializer.getLength(uhfBandCap);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(uhfBandCap, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_BAND_CAPABILITIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1);
		Assert.assertEquals(data.getShort(), -100);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		Assert.assertEquals(data.getShort(), 100);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_INFORMATION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 15);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.FIXED_FREQUENCY_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 77777777L);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 36);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				modeIdentifier);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				mValue.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				forwardLinkModukation.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				spectralMaskIndicator.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), bdrValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), pieValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), minTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), maxTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				stepTariValue);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 36);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				modeIdentifier);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				mValue.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				forwardLinkModukation.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				spectralMaskIndicator.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), bdrValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), pieValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), minTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), maxTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				stepTariValue);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_FREQUENCY_CAPABILITIES
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 12);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 999999999L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4294967295L);
		data.rewind();

		// deserialize
		uhfBandCap = serializer.deserializeUHFBandCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = uhfBandCap.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.UHF_BAND_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		TLVParameterHeader transmitPowerLevelTableEntryHeader = uhfBandCap
				.getTransmitPowerTable().get(0).getParameterHeader();
		Assert.assertEquals(transmitPowerLevelTableEntryHeader.getReserved(), 0);
		Assert.assertEquals(
				transmitPowerLevelTableEntryHeader.getParameterType(),
				ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY);
		Assert.assertEquals(
				transmitPowerLevelTableEntryHeader.getParameterLength(), 8);
		Assert.assertEquals(uhfBandCap.getTransmitPowerTable().get(0)
				.getIndex(), 1);
		Assert.assertEquals(uhfBandCap.getTransmitPowerTable().get(0)
				.getTransmitPowerValue(), -100);

		transmitPowerLevelTableEntryHeader = uhfBandCap.getTransmitPowerTable()
				.get(1).getParameterHeader();
		Assert.assertEquals(transmitPowerLevelTableEntryHeader.getReserved(), 0);
		Assert.assertEquals(
				transmitPowerLevelTableEntryHeader.getParameterType(),
				ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY);
		Assert.assertEquals(
				transmitPowerLevelTableEntryHeader.getParameterLength(), 8);
		Assert.assertEquals(uhfBandCap.getTransmitPowerTable().get(1)
				.getIndex(), 2);
		Assert.assertEquals(uhfBandCap.getTransmitPowerTable().get(1)
				.getTransmitPowerValue(), 100);

		TLVParameterHeader fiHeader = uhfBandCap.getFrequencyInformation()
				.getParameterHeader();
		Assert.assertEquals(fiHeader.getReserved(), 0);
		Assert.assertEquals(fiHeader.getParameterType(),
				ParameterType.FREQUENCY_INFORMATION);
		Assert.assertEquals(fiHeader.getParameterLength(), 15);
		Assert.assertEquals(fiF.isHopping(), false);
		TLVParameterHeader headerFFT = uhfBandCap.getFrequencyInformation()
				.getFixedFreqInfo().getParameterHeader();
		Assert.assertEquals(headerFFT.getReserved(), 0);
		Assert.assertEquals(headerFFT.getParameterType(),
				ParameterType.FIXED_FREQUENCY_TABLE);
		Assert.assertEquals(headerFFT.getParameterLength(), 10);
		Assert.assertEquals(uhfBandCap.getFrequencyInformation()
				.getFixedFreqInfo().getNumFrequencies(), 1);
		Assert.assertEquals(uhfBandCap.getFrequencyInformation()
				.getFixedFreqInfo().getFrequency().get(0).longValue(),
				77777777L);

		TLVParameterHeader uhfHeader = uhfBandCap.getUhfC1G2RFModeTable()
				.get(0).getParameterHeader();
		Assert.assertEquals(uhfHeader.getReserved(), 0);
		Assert.assertEquals(uhfHeader.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE);
		Assert.assertEquals(uhfHeader.getParameterLength(), 36);
		TLVParameterHeader header = uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY);
		Assert.assertEquals(header.getParameterLength(), 32);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getBdrValue(), bdrValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getForwardLinkModulation(),
				forwardLinkModukation);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getMaxTariValue(), maxTariValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getMinTariValue(), minTariValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getmValue(), mValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getPieValue(), pieValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getSpectralMaskIndicator(),
				spectralMaskIndicator);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getStepTariValue(), stepTariValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).getDrValue(), drValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(0)
				.getUhfC1G2RFModeSet().get(0).isEpcHAGConformance(),
				epcHAGConformance);

		uhfHeader = uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getParameterHeader();
		Assert.assertEquals(uhfHeader.getReserved(), 0);
		Assert.assertEquals(uhfHeader.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE);
		Assert.assertEquals(uhfHeader.getParameterLength(), 36);
		header = uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY);
		Assert.assertEquals(header.getParameterLength(), 32);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getBdrValue(), bdrValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getForwardLinkModulation(),
				forwardLinkModukation);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getMaxTariValue(), maxTariValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getMinTariValue(), minTariValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getmValue(), mValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getPieValue(), pieValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getSpectralMaskIndicator(),
				spectralMaskIndicator);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getStepTariValue(), stepTariValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).getDrValue(), drValue);
		Assert.assertEquals(uhfBandCap.getUhfC1G2RFModeTable().get(1)
				.getUhfC1G2RFModeSet().get(0).isEpcHAGConformance(),
				epcHAGConformance);

		TLVParameterHeader rfSurveyHeader = uhfBandCap
				.getRfSurveyFrequencyCapabilities().getParameterHeader();
		Assert.assertEquals(rfSurveyHeader.getReserved(), 0);
		Assert.assertEquals(rfSurveyHeader.getParameterType(),
				ParameterType.RF_SURVEY_FREQUENCY_CAPABILITIES);
		Assert.assertEquals(rfSurveyHeader.getParameterLength(), 12);
		Assert.assertEquals(uhfBandCap.getRfSurveyFrequencyCapabilities()
				.getMaximumFrequency(), max);
		Assert.assertEquals(uhfBandCap.getRfSurveyFrequencyCapabilities()
				.getMinimumFrequency(), min);

		// getLength
		Assert.assertEquals(serializer.getLength(uhfBandCap), 119);
	}

	@Test
	public void uhfC1G2RFModeTable() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		long modeIdentifier = 4000000000L;
		UHFC1G2RFModeTableEntryDivideRatio drValue = UHFC1G2RFModeTableEntryDivideRatio._64DIV3;
		boolean epcHAGConformance = true;
		UHFC1G2RFModeTableEntryModulation mValue = UHFC1G2RFModeTableEntryModulation._2;
		UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModukation = UHFC1G2RFModeTableEntryForwardLinkModulation.PR_ASK;
		UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator = UHFC1G2RFModeTableEntrySpectralMaskIndicator.SINGLE_INTERROGATOR_MODE_MASK;
		int bdrValue = 10001;
		int pieValue = 10002;
		int minTariValue = 10003;
		int maxTariValue = 10004;
		int stepTariValue = 10005;
		UHFC1G2RFModeTableEntry uhfRFMTE = new UHFC1G2RFModeTableEntry(
				new TLVParameterHeader((byte) 0), modeIdentifier, drValue,
				epcHAGConformance, mValue, forwardLinkModukation,
				spectralMaskIndicator, bdrValue, pieValue, minTariValue,
				maxTariValue, stepTariValue);

		List<UHFC1G2RFModeTableEntry> uhfC1G2RFModeSet = new ArrayList<>();
		uhfC1G2RFModeSet.add(uhfRFMTE);
		UHFC1G2RFModeTable uhfRFMTable = new UHFC1G2RFModeTable(
				new TLVParameterHeader((byte) 0), uhfC1G2RFModeSet);

		int parameterLength = serializer.getLength(uhfRFMTable);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(uhfRFMTable, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 32);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				modeIdentifier);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				mValue.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				forwardLinkModukation.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				spectralMaskIndicator.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), bdrValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), pieValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), minTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), maxTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				stepTariValue);
		data.rewind();

		// deserialize
		uhfRFMTable = serializer.deserializeUHFC1G2RFModeTable(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = uhfRFMTable
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		TLVParameterHeader header = uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY);
		Assert.assertEquals(header.getParameterLength(),
				header.getParameterLength());
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getBdrValue(), bdrValue);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getForwardLinkModulation(), forwardLinkModukation);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getMaxTariValue(), maxTariValue);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getMinTariValue(), minTariValue);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getmValue(), mValue);

		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getPieValue(), pieValue);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getSpectralMaskIndicator(), spectralMaskIndicator);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getStepTariValue(), stepTariValue);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.getDrValue(), drValue);
		Assert.assertEquals(uhfRFMTable.getUhfC1G2RFModeSet().get(0)
				.isEpcHAGConformance(), epcHAGConformance);

		// getLength
		Assert.assertEquals(serializer.getLength(uhfRFMTable), 36);
	}

	@Test
	public void uhfC1G2RFModeTableEntry() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		long modeIdentifier = 4000000000L;
		UHFC1G2RFModeTableEntryDivideRatio drValue = UHFC1G2RFModeTableEntryDivideRatio._64DIV3;
		boolean epcHAGConformance = true;
		UHFC1G2RFModeTableEntryModulation mValue = UHFC1G2RFModeTableEntryModulation._2;
		UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModukation = UHFC1G2RFModeTableEntryForwardLinkModulation.PR_ASK;
		UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator = UHFC1G2RFModeTableEntrySpectralMaskIndicator.SINGLE_INTERROGATOR_MODE_MASK;
		int bdrValue = 10001;
		int pieValue = 10002;
		int minTariValue = 10003;
		int maxTariValue = 10004;
		int stepTariValue = 10005;
		UHFC1G2RFModeTableEntry uhfRFMTE = new UHFC1G2RFModeTableEntry(
				new TLVParameterHeader((byte) 0), modeIdentifier, drValue,
				epcHAGConformance, mValue, forwardLinkModukation,
				spectralMaskIndicator, bdrValue, pieValue, minTariValue,
				maxTariValue, stepTariValue);
		int parameterLength = serializer.getLength(uhfRFMTE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(uhfRFMTE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				modeIdentifier);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 192);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				mValue.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				forwardLinkModukation.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				spectralMaskIndicator.getValue());
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), bdrValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), pieValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), minTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), maxTariValue);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				stepTariValue);
		data.rewind();

		// deserialize
		uhfRFMTE = serializer.deserializeUHFC1G2RFModeTableEntry(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = uhfRFMTE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(uhfRFMTE.getBdrValue(), bdrValue);
		Assert.assertEquals(uhfRFMTE.getForwardLinkModulation(),
				forwardLinkModukation);
		Assert.assertEquals(uhfRFMTE.getMaxTariValue(), maxTariValue);
		Assert.assertEquals(uhfRFMTE.getMinTariValue(), minTariValue);
		Assert.assertEquals(uhfRFMTE.getmValue(), mValue);

		Assert.assertEquals(uhfRFMTE.getPieValue(), pieValue);
		Assert.assertEquals(uhfRFMTE.getSpectralMaskIndicator(),
				spectralMaskIndicator);
		Assert.assertEquals(uhfRFMTE.getStepTariValue(), stepTariValue);
		Assert.assertEquals(uhfRFMTE.getDrValue(), drValue);
		Assert.assertEquals(uhfRFMTE.isEpcHAGConformance(), epcHAGConformance);

		// getLength
		Assert.assertEquals(serializer.getLength(uhfRFMTE), 32);
	}

	@Test
	public void transmitPowerLevelTableEntry()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		short pValue = -5050;
		int index = 666;
		TransmitPowerLevelTableEntry transPLTE = new TransmitPowerLevelTableEntry(
				new TLVParameterHeader((byte) 0), index, pValue);
		int parameterLength = serializer.getLength(transPLTE);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(transPLTE, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 666);
		Assert.assertEquals(data.getShort(), -5050);
		data.rewind();

		// deserialize
		transPLTE = serializer.deserializeTransmitPowerLevelTableEntry(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = transPLTE.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(transPLTE.getIndex(), 666);
		Assert.assertEquals(transPLTE.getTransmitPowerValue(), -5050);

		// getLength
		Assert.assertEquals(serializer.getLength(transPLTE), 8);
	}

	@Test
	public void frequencyInformation() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize hopping
		short tID = 50;
		List<Long> freq1 = new ArrayList<>();
		freq1.add(4000000000L);
		freq1.add(4000000001L);
		List<Long> freq2 = new ArrayList<>();
		freq2.add(4000000002L);
		freq2.add(4000000003L);
		FrequencyHopTable freqHT1 = new FrequencyHopTable(
				new TLVParameterHeader((byte) 0), tID, freq1);
		FrequencyHopTable freqHT2 = new FrequencyHopTable(
				new TLVParameterHeader((byte) 0), tID, freq2);
		List<FrequencyHopTable> freqHopInfo = new ArrayList<>();
		freqHopInfo.add(freqHT1);
		freqHopInfo.add(freqHT2);
		FrequencyInformation fiH = new FrequencyInformation(
				new TLVParameterHeader((byte) 0), freqHopInfo);
		int parameterLength = serializer.getLength(fiH);
		ByteBuffer dataH = ByteBuffer.allocate(parameterLength);
		serializer.serialize(fiH, dataH);
		dataH.flip();
		Assert.assertEquals(dataH.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_INFORMATION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(dataH.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(dataH.get()), 128);
		Assert.assertEquals(dataH.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_HOP_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(dataH.getShort()),
				serializer.getLength(freqHT1));

		Assert.assertEquals(DataTypeConverter.ubyte(dataH.get()), 50);
		Assert.assertEquals(DataTypeConverter.ubyte(dataH.get()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(dataH.getShort()), 2);
		Assert.assertEquals(DataTypeConverter.uint(dataH.getInt()), 4000000000L);
		Assert.assertEquals(DataTypeConverter.uint(dataH.getInt()), 4000000001L);
		Assert.assertEquals(dataH.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_HOP_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(dataH.getShort()),
				serializer.getLength(freqHT2));

		Assert.assertEquals(DataTypeConverter.ubyte(dataH.get()), 50);
		Assert.assertEquals(DataTypeConverter.ubyte(dataH.get()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(dataH.getShort()), 2);
		Assert.assertEquals(DataTypeConverter.uint(dataH.getInt()), 4000000002L);
		Assert.assertEquals(DataTypeConverter.uint(dataH.getInt()), 4000000003L);
		dataH.rewind();

		// serialize fixed
		List<Long> fixedFreq = new ArrayList<>();
		fixedFreq.add(77777777L);
		FixedFrequencyTable fixedFT = new FixedFrequencyTable(
				new TLVParameterHeader((byte) 0), fixedFreq);
		FrequencyInformation fiF = new FrequencyInformation(
				new TLVParameterHeader((byte) 0), fixedFT);
		int lengthF = serializer.getLength(fiF);
		ByteBuffer dataF = ByteBuffer.allocate(lengthF);
		serializer.serialize(fiF, dataF);
		dataF.flip();
		Assert.assertEquals(dataF.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_INFORMATION.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(dataF.getShort()), lengthF);
		Assert.assertEquals(DataTypeConverter.ubyte(dataF.get()), 0);
		Assert.assertEquals(dataF.getShort(),
				((0 << 10) + ParameterType.FIXED_FREQUENCY_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(dataF.getShort()),
				serializer.getLength(fixedFT));

		Assert.assertEquals(DataTypeConverter.ushort(dataF.getShort()), 1);
		Assert.assertEquals(DataTypeConverter.uint(dataF.getInt()), 77777777L);
		dataF.rewind();

		// deserialize hopping
		fiH = serializer.deserializeFrequencyInformation(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(dataH), dataH);
		TLVParameterHeader tlvParameterHeader = fiH.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.FREQUENCY_INFORMATION);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(fiH.isHopping(), true);
		TLVParameterHeader header = fiH.getFreqHopInfo().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.FREQUENCY_HOP_TABLE);
		Assert.assertEquals(header.getParameterLength(), 16);
		Assert.assertEquals(fiH.getFreqHopInfo().get(0).getHopTableID(), 50);
		Assert.assertEquals(fiH.getFreqHopInfo().get(0).getNumHops(), 2);
		Assert.assertEquals(fiH.getFreqHopInfo().get(0).getHopFrequency()
				.get(0).longValue(), 4000000000L);
		Assert.assertEquals(fiH.getFreqHopInfo().get(0).getHopFrequency()
				.get(1).longValue(), 4000000001L);
		header = fiH.getFreqHopInfo().get(1).getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.FREQUENCY_HOP_TABLE);
		Assert.assertEquals(header.getParameterLength(), 16);
		Assert.assertEquals(fiH.getFreqHopInfo().get(1).getHopTableID(), 50);
		Assert.assertEquals(fiH.getFreqHopInfo().get(1).getNumHops(), 2);
		Assert.assertEquals(fiH.getFreqHopInfo().get(1).getHopFrequency()
				.get(0).longValue(), 4000000002L);
		Assert.assertEquals(fiH.getFreqHopInfo().get(1).getHopFrequency()
				.get(1).longValue(), 4000000003L);

		// deserialize fixed
		fiF = serializer.deserializeFrequencyInformation(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(dataF), dataF);
		TLVParameterHeader headerF = fiF.getParameterHeader();
		Assert.assertEquals(headerF.getReserved(), 0);
		Assert.assertEquals(headerF.getParameterType(),
				ParameterType.FREQUENCY_INFORMATION);
		Assert.assertEquals(headerF.getParameterLength(), lengthF);
		Assert.assertEquals(fiF.isHopping(), false);
		TLVParameterHeader headerFFT = fiF.getFixedFreqInfo()
				.getParameterHeader();
		Assert.assertEquals(headerFFT.getReserved(), 0);
		Assert.assertEquals(headerFFT.getParameterType(),
				ParameterType.FIXED_FREQUENCY_TABLE);
		Assert.assertEquals(headerFFT.getParameterLength(), 10);
		Assert.assertEquals(fiF.getFixedFreqInfo().getNumFrequencies(), 1);
		Assert.assertEquals(fiF.getFixedFreqInfo().getFrequency().get(0)
				.longValue(), 77777777L);

		// getLength
		Assert.assertEquals(serializer.getLength(fiH), 37);
		Assert.assertEquals(serializer.getLength(fiF), 15);
	}

	@Test
	public void frequencyHopTable() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		short tID = 50;
		List<Long> freq = new ArrayList<>();
		freq.add(4000000000L);
		freq.add(4000000001L);
		freq.add(4000000002L);
		FrequencyHopTable freqHT = new FrequencyHopTable(
				new TLVParameterHeader((byte) 0), tID, freq);
		int parameterLength = serializer.getLength(freqHT);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(freqHT, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.FREQUENCY_HOP_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 50);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 3);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000000L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000001L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000002L);
		data.rewind();

		// deserialize
		freqHT = serializer.deserializeFrequencyHopTable(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = freqHT.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.FREQUENCY_HOP_TABLE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(freqHT.getHopTableID(), 50);
		Assert.assertEquals(freqHT.getNumHops(), 3);
		Assert.assertEquals(freqHT.getHopFrequency().get(0).longValue(),
				4000000000L);
		Assert.assertEquals(freqHT.getHopFrequency().get(1).longValue(),
				4000000001L);
		Assert.assertEquals(freqHT.getHopFrequency().get(2).longValue(),
				4000000002L);
		// getLength
		Assert.assertEquals(serializer.getLength(freqHT), 20);
	}

	@Test
	public void fixedFrequencyTable() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		List<Long> freq = new ArrayList<>();
		freq.add(4000000000L);
		freq.add(4000000001L);
		freq.add(4000000002L);
		FixedFrequencyTable fixedFT = new FixedFrequencyTable(
				new TLVParameterHeader((byte) 0), freq);
		int parameterLength = serializer.getLength(fixedFT);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(fixedFT, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.FIXED_FREQUENCY_TABLE.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 3);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000000L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000001L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4000000002L);
		data.rewind();

		// deserialize
		fixedFT = serializer.deserializeFixedFrequencyTable(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = fixedFT.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.FIXED_FREQUENCY_TABLE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(fixedFT.getNumFrequencies(), 3);
		Assert.assertEquals(fixedFT.getFrequency().get(0).longValue(),
				4000000000L);
		Assert.assertEquals(fixedFT.getFrequency().get(1).longValue(),
				4000000001L);
		Assert.assertEquals(fixedFT.getFrequency().get(2).longValue(),
				4000000002L);
		// getLength
		Assert.assertEquals(serializer.getLength(fixedFT), 18);
	}

	@Test
	public void rfSurveyFrequencyCapabilities()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		long min = 999999999L;
		long max = 4294967295L;
		RFSurveyFrequencyCapabilities rfSFCap = new RFSurveyFrequencyCapabilities(
				new TLVParameterHeader((byte) 0), min, max);
		int parameterLength = serializer.getLength(rfSFCap);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rfSFCap, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RF_SURVEY_FREQUENCY_CAPABILITIES
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 999999999L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4294967295L);
		data.rewind();

		// deserialize
		rfSFCap = serializer.deserializeRFSurveyFrequencyCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rfSFCap.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RF_SURVEY_FREQUENCY_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rfSFCap.getMaximumFrequency(), max);
		Assert.assertEquals(rfSFCap.getMinimumFrequency(), min);
		// getLength
		Assert.assertEquals(serializer.getLength(rfSFCap), 12);
	}

	@Test
	public void generalDeviceCapabilities()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		long deviceManufacturerName = 123456789;
		long modelName = 987654321;
		String firmwareVersion = "APP_02-08-00_03-02-00_01-03-02";
		int maximumNumberOfAntennasSupported = 555;
		boolean canSetAntennaProperties = false;
		boolean hasUTCClockCapability = true;
		MaximumReceiveSensitivity maximumReceiveSensitivity = new MaximumReceiveSensitivity(
				new TLVParameterHeader((byte) 0), 2000);
		GPIOCapabilities gpioSupport = new GPIOCapabilities(
				new TLVParameterHeader((byte) 0), 20, 20);

		List<ReceiveSensitivityTabelEntry> receiveSensitivityTable = new ArrayList<>();
		ReceiveSensitivityTabelEntry rste1 = new ReceiveSensitivityTabelEntry(
				new TLVParameterHeader((byte) 0), 1000, 1000);
		ReceiveSensitivityTabelEntry rste2 = new ReceiveSensitivityTabelEntry(
				new TLVParameterHeader((byte) 0), 2000, 2000);
		receiveSensitivityTable.add(rste1);
		receiveSensitivityTable.add(rste2);

		List<PerAntennaReceiveSensitivityRange> perAntennaReceiveSensitivityRange = new ArrayList<>();
		PerAntennaReceiveSensitivityRange parsr1 = new PerAntennaReceiveSensitivityRange(
				new TLVParameterHeader((byte) 0), 1, 0, 65535);
		PerAntennaReceiveSensitivityRange parsr2 = new PerAntennaReceiveSensitivityRange(
				new TLVParameterHeader((byte) 0), 2, 1, 655);
		perAntennaReceiveSensitivityRange.add(parsr1);
		perAntennaReceiveSensitivityRange.add(parsr2);

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
		gdc.setPerAntennaReceiveSensitivityRange(perAntennaReceiveSensitivityRange);
		gdc.setMaximumReceiveSensitivity(maximumReceiveSensitivity);

		int parameterLength = serializer.getLength(gdc);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(gdc, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GENERAL_DEVICE_CAPABILITIES
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 555);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 16384);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				deviceManufacturerName);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), modelName);
		byte[] firmVer = firmwareVersion.getBytes(StandardCharsets.UTF_8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				firmVer.length);
		byte[] dst = new byte[firmVer.length];
		data.get(dst, 0, firmVer.length);
		Assert.assertArrayEquals(dst, firmVer);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1000);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2000);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2000);
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 65535);
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 1);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 655);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPIO_CAPABILITIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 20);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 20);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.PER_ANTENNA_AIR_PROTOCOL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 500);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 1);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);

		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.PER_ANTENNA_AIR_PROTOCOL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 600);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 0);
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.MAXIMUM_RECEIVE_SENSITIVITY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 6);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				maximumReceiveSensitivity.getMaximumSensitivityValue());
		data.rewind();

		// deserialize
		gdc = serializer.deserializeGeneralDeviceCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = gdc.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.GENERAL_DEVICE_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(gdc.getMaxNumberOfAntennasSupported(),
				maximumNumberOfAntennasSupported);
		Assert.assertEquals(gdc.isCanSetAntennaProperties(), false);
		Assert.assertEquals(gdc.isHasUTCClockCapability(), true);
		Assert.assertEquals(gdc.getDeviceManufacturerName(), 123456789);
		Assert.assertEquals(gdc.getModelName(), 987654321);
		Assert.assertEquals(gdc.getFirmwareVersionByteCount(), firmVer.length);
		Assert.assertEquals(gdc.getFirmwareVersion(), firmwareVersion);
		TLVParameterHeader header = gdc.getReceiveSensitivityTableEntry().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY);
		Assert.assertEquals(gdc.getReceiveSensitivityTableEntry().get(0).getIndex(),
				1000);
		Assert.assertEquals(gdc.getReceiveSensitivityTableEntry().get(0)
				.getReceiveSensitivityValue(), 1000);
		header = gdc.getReceiveSensitivityTableEntry().get(1).getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY);
		Assert.assertEquals(gdc.getReceiveSensitivityTableEntry().get(1).getIndex(),
				2000);
		Assert.assertEquals(gdc.getReceiveSensitivityTableEntry().get(1)
				.getReceiveSensitivityValue(), 2000);
		header = gdc.getPerAntennaReceiveSensitivityRange().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE);
		Assert.assertEquals(gdc.getPerAntennaReceiveSensitivityRange().get(0)
				.getAntennaID(), 1);
		Assert.assertEquals(gdc.getPerAntennaReceiveSensitivityRange().get(0)
				.getReceiveSensitivityIndexMin(), 0);
		Assert.assertEquals(gdc.getPerAntennaReceiveSensitivityRange().get(0)
				.getReceiveSensitivityIndexMax(), 65535);
		header = gdc.getPerAntennaReceiveSensitivityRange().get(1)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE);
		Assert.assertEquals(gdc.getPerAntennaReceiveSensitivityRange().get(1)
				.getAntennaID(), 2);
		Assert.assertEquals(gdc.getPerAntennaReceiveSensitivityRange().get(1)
				.getReceiveSensitivityIndexMin(), 1);
		Assert.assertEquals(gdc.getPerAntennaReceiveSensitivityRange().get(1)
				.getReceiveSensitivityIndexMax(), 655);

		header = gdc.getGpioCapabilities().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.GPIO_CAPABILITIES);
		Assert.assertEquals(gdc.getGpioCapabilities().getNumGPIs(), 20);
		Assert.assertEquals(gdc.getGpioCapabilities().getNumGPOs(), 20);

		header = gdc.getPerAntennaAirProtocol().get(0)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.PER_ANTENNA_AIR_PROTOCOL);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(0)
				.getAntennaID(), 500);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(0)
				.getNumProtocols(), 2);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(0)
				.getAirProtocolsSupported().get(0), ProtocolId.EPC_GLOBAL_C1G2);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(0)
				.getAirProtocolsSupported().get(1),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		header = gdc.getPerAntennaAirProtocol().get(1)
				.getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.PER_ANTENNA_AIR_PROTOCOL);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(1)
				.getAntennaID(), 600);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(1)
				.getNumProtocols(), 2);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(1)
				.getAirProtocolsSupported().get(0),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		Assert.assertEquals(gdc.getPerAntennaAirProtocol().get(1)
				.getAirProtocolsSupported().get(1),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL);

		header = gdc.getMaximumReceiveSensitivity().getParameterHeader();
		Assert.assertEquals(header.getReserved(), 0);
		Assert.assertEquals(header.getParameterType(),
				ParameterType.MAXIMUM_RECEIVE_SENSITIVITY);
		Assert.assertEquals(gdc.getMaximumReceiveSensitivity()
				.getMaximumSensitivityValue(), 2000);

		// getLength
		Assert.assertEquals(serializer.getLength(gdc), 88 + firmVer.length);
	}

	@Test
	public void maximumReceiveSensitivity()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		int maxSensValue = 10000;
		MaximumReceiveSensitivity mrs = new MaximumReceiveSensitivity(
				new TLVParameterHeader((byte) 0), maxSensValue);
		int parameterLength = serializer.getLength(mrs);

		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(mrs, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.MAXIMUM_RECEIVE_SENSITIVITY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10000);
		data.rewind();

		// deserialize
		mrs = serializer.deserializeMximumReceiveSensitivity(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = mrs.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.MAXIMUM_RECEIVE_SENSITIVITY);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(mrs.getMaximumSensitivityValue(), maxSensValue);
		// getLength
		Assert.assertEquals(serializer.getLength(mrs), 6);
	}

	@Test
	public void receiveSensitivityTabelEntry()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		int index = 500;
		int sensValue = 10000;
		ReceiveSensitivityTabelEntry rste = new ReceiveSensitivityTabelEntry(
				new TLVParameterHeader((byte) 0), index, sensValue);
		int parameterLength = serializer.getLength(rste);

		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rste, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 500);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 10000);
		data.rewind();

		// deserialize
		rste = serializer.deserializeReceiveSensitivityTabelEntry(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rste.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(rste.getIndex(), index);
		Assert.assertEquals(rste.getReceiveSensitivityValue(), sensValue);
		// getLength
		Assert.assertEquals(serializer.getLength(rste), 8);
	}

	@Test
	public void perAntennaReceiveSensitivityRange()
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		int antID = 500;
		int min = 0;
		int max = 65535;
		PerAntennaReceiveSensitivityRange parsr = new PerAntennaReceiveSensitivityRange(
				new TLVParameterHeader((byte) 0), antID, min, max);
		int parameterLength = serializer.getLength(parsr);

		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(parsr, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((0 << 10) + ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 500);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 65535);
		data.rewind();

		// deserialize
		parsr = serializer.deserializePerAntennaReceiveSensitivityRange(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = parsr.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(parsr.getAntennaID(), antID);
		Assert.assertEquals(parsr.getReceiveSensitivityIndexMin(), min);
		Assert.assertEquals(parsr.getReceiveSensitivityIndexMax(), max);
		// getLength
		Assert.assertEquals(serializer.getLength(parsr), 10);
	}

	@Test
	public void perAntennaAirProtocol() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		int antID = 500;
		List<ProtocolId> pID = new ArrayList<>();
		pID.add(ProtocolId.EPC_GLOBAL_C1G2);
		pID.add(ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		pID.add(ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		pID.add(ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		PerAntennaAirProtocol paap = new PerAntennaAirProtocol(
				new TLVParameterHeader((byte) 0), antID, pID);
		int parameterLength = serializer.getLength(paap);

		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(paap, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.PER_ANTENNA_AIR_PROTOCOL.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 500);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 4);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ProtocolId.EPC_GLOBAL_C1G2.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL.getValue());
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL.getValue());
		data.rewind();

		// deserialize
		paap = serializer.deserializePerAntennaAirProtocol(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = paap.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.PER_ANTENNA_AIR_PROTOCOL);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(paap.getAntennaID(), antID);
		Assert.assertEquals(paap.getNumProtocols(), paap
				.getAirProtocolsSupported().size());
		Assert.assertEquals(paap.getAirProtocolsSupported().get(0),
				ProtocolId.EPC_GLOBAL_C1G2);
		Assert.assertEquals(paap.getAirProtocolsSupported().get(1),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		Assert.assertEquals(paap.getAirProtocolsSupported().get(2),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL);
		Assert.assertEquals(paap.getAirProtocolsSupported().get(3),
				ProtocolId.UNSPECIFIED_AIR_PROTOCOL);

		// getLength
		Assert.assertEquals(serializer.getLength(paap), 12);
	}

	@Test
	public void gpioCapabilities() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		// serialize
		int gpi = 0;
		int gpo = 65535;
		GPIOCapabilities gpio = new GPIOCapabilities(new TLVParameterHeader(
				(byte) 0), gpi, gpo);
		int parameterLength = serializer.getLength(gpio);

		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(gpio, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.GPIO_CAPABILITIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 0);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 65535);
		data.rewind();

		// deserialize
		gpio = serializer.deserializeGPIOCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = gpio.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.GPIO_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(gpio.getNumGPIs(), 0);
		Assert.assertEquals(gpio.getNumGPOs(), 65535);
		// getLength
		Assert.assertEquals(serializer.getLength(gpio), 8);
	}

	@Test
	public void llrpCapabilities() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		boolean c = true;
		boolean r = true;
		boolean s = true;
		boolean t = false;
		boolean h = false;
		byte maxPriLevelSup = 6;
		int clientRequestOpSpecTimeout = 7000;
		long maxNumROSpecs = 4294967295L;// Max value
		long maxNumSpecsPerROSpec = 4294967295L - 5;
		long maxNumInventoryParameterSpecsPerAISpec = 4294967295L - 20;
		long maxNumAccessSpecs = 21;
		long maxNumOpSpecsPerAccessSpec = 1;
		LLRPCapabilities llrpCapabilities = new LLRPCapabilities(
				new TLVParameterHeader((byte) 0), c, r, s, t, h,
				maxPriLevelSup, clientRequestOpSpecTimeout, maxNumROSpecs,
				maxNumSpecsPerROSpec, maxNumInventoryParameterSpecsPerAISpec,
				maxNumAccessSpecs, maxNumOpSpecsPerAccessSpec);
		int parameterLength = serializer.getLength(llrpCapabilities);

		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(llrpCapabilities, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((0 << 10) + ParameterType.LLRP_CAPABILITIES.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 224);
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), 6);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 7000);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 4294967295L);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				4294967295L - 5);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()),
				4294967295L - 20);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 21);
		Assert.assertEquals(DataTypeConverter.uint(data.getInt()), 1);
		data.rewind();

		// deserialize
		LLRPCapabilities llrpC = serializer.deserializeLLRPCapabilities(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = llrpC.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 0);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.LLRP_CAPABILITIES);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(llrpCapabilities.isCanDoRFSurvey(), true);
		Assert.assertEquals(llrpCapabilities.isCanReportBufferFillWarning(),
				true);
		Assert.assertEquals(llrpCapabilities.isSupportsClientRequestOpSpec(),
				true);
		Assert.assertEquals(
				llrpCapabilities.isCanDoTagInventoryStateAwareSingulation(),
				false);
		Assert.assertEquals(llrpCapabilities.isSupportsEventAndReportHolding(),
				false);
		Assert.assertEquals(llrpCapabilities.getMaxPriorityLevelSupported(), 6);
		Assert.assertEquals(llrpCapabilities.getClientRequestOpSpecTimeout(),
				7000);
		Assert.assertEquals(llrpCapabilities.getMaxNumROSpecs(), 4294967295L);
		Assert.assertEquals(llrpCapabilities.getMaxNumSpecsPerROSpec(),
				4294967295L - 5);
		Assert.assertEquals(
				llrpCapabilities.getMaxNumInventoryParameterSpecsPerAISpec(),
				4294967295L - 20);
		Assert.assertEquals(llrpCapabilities.getMaxNumAccessSpecs(), 21);
		Assert.assertEquals(llrpCapabilities.getMaxNumOpSpecsPerAccessSpec(), 1);
		// getLength
		Assert.assertEquals(serializer.getLength(llrpCapabilities),
				parameterLength);
	}

	@Test
	public void llrpStatus() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		LLRPStatusCode statusCode = LLRPStatusCode.M_SUCCESS;
		String errorDesc = "hello world";
		byte[] error = errorDesc.getBytes(StandardCharsets.UTF_8);
		LLRPStatus llrpStatus = new LLRPStatus(
				new TLVParameterHeader((byte) 5), statusCode, errorDesc);

		FieldError fieldError = new FieldError(
				new TLVParameterHeader((byte) 6), 2014, statusCode);
		llrpStatus.setFieldError(fieldError);

		ParameterError paraError = new ParameterError(new TLVParameterHeader(
				(byte) 7), 9999, statusCode);
		llrpStatus.setParameterError(paraError);

		int parameterLength = serializer.getLength(llrpStatus);
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(llrpStatus, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((5 << 10) + ParameterType.LLRP_STATUS.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				statusCode.getValue());

		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				error.length);
		byte[] dst = new byte[error.length];
		data.get(dst, 0, error.length);
		Assert.assertArrayEquals(dst, error);

		Assert.assertEquals(data.getShort(),
				((6 << 10) + ParameterType.FIELD_ERROR.getValue()));

		Assert.assertEquals(data.getShort(), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2014);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 0);

		Assert.assertEquals(data.getShort(),
				((7 << 10) + ParameterType.PARAMETER_ERROR.getValue()));

		Assert.assertEquals(data.getShort(), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9999);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 0);
		data.rewind();

		// deserialize
		llrpStatus = serializer.deserializeLLRPStatus(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = llrpStatus.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 5);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.LLRP_STATUS);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(llrpStatus.getStatusCode(), statusCode);
		Assert.assertEquals(llrpStatus.getErrorDescriptionByteCount(),
				error.length);
		Assert.assertEquals(llrpStatus.getErrorDescription(), errorDesc);
		Assert.assertEquals(llrpStatus.getFieldError().getFieldNum(), 2014);
		Assert.assertEquals(llrpStatus.getFieldError().getErrorCode(),
				statusCode);
		Assert.assertEquals(llrpStatus.getParameterError().getParameterType(),
				9999);
		Assert.assertEquals(llrpStatus.getParameterError().getErrorCode(),
				statusCode);

		// getLength
		Assert.assertEquals(serializer.getLength(llrpStatus), parameterLength);
	}

	@Test
	public void fieldError() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		int fieldNum = 1000;
		LLRPStatusCode errorCode = LLRPStatusCode.P_OVERFLOW_FIELD;
		FieldError fieldError = new FieldError(
				new TLVParameterHeader((byte) 5), fieldNum, errorCode);

		int parameterLength = 8;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(fieldError, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((5 << 10) + ParameterType.FIELD_ERROR.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), fieldNum);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				errorCode.getValue());
		data.rewind();

		// deserialize
		fieldError = serializer.deserializeFieldError(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = fieldError.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 5);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.FIELD_ERROR);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(fieldError.getFieldNum(), fieldNum);
		Assert.assertEquals(fieldError.getErrorCode(), errorCode);

		// getLength
		Assert.assertEquals(serializer.getLength(fieldError), parameterLength);
	}

	@Test
	public void parameterError() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		int paramType = 1111;
		LLRPStatusCode errorCode = LLRPStatusCode.M_UNSUPPORTED_MESSAGE;
		ParameterError paramError = new ParameterError(new TLVParameterHeader(
				(byte) 11), paramType, errorCode);

		LLRPStatusCode errorCodeSec = LLRPStatusCode.P_PARAMETER_ERROR;
		FieldError fieldError = new FieldError(
				new TLVParameterHeader((byte) 6), 2222, errorCodeSec);
		paramError.setFieldError(fieldError);

		ParameterError paramErrorSec = new ParameterError(
				new TLVParameterHeader((byte) 7), 9999, errorCodeSec);
		paramError.setParameterError(paramErrorSec);

		int parameterLength = 24;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(paramError, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((11 << 10) + ParameterType.PARAMETER_ERROR.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				paramType);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				errorCode.getValue());

		Assert.assertEquals(data.getShort(),
				((6 << 10) + ParameterType.FIELD_ERROR.getValue()));
		Assert.assertEquals(data.getShort(), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 2222);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				errorCodeSec.getValue());

		Assert.assertEquals(data.getShort(),
				((7 << 10) + ParameterType.PARAMETER_ERROR.getValue()));
		Assert.assertEquals(data.getShort(), 8);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()), 9999);
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				errorCodeSec.getValue());
		data.rewind();

		// deserialize
		paramError = serializer.deserializeParameterError(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = paramError.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 11);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.PARAMETER_ERROR);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(paramError.getParameterType(), paramType);
		Assert.assertEquals(paramError.getErrorCode(), errorCode);

		Assert.assertEquals(paramError.getFieldError().getParameterHeader()
				.getReserved(), 6);
		Assert.assertEquals(paramError.getFieldError().getParameterHeader()
				.getParameterType(), ParameterType.FIELD_ERROR);
		Assert.assertEquals(paramError.getFieldError().getParameterHeader()
				.getParameterLength(), 8);
		Assert.assertEquals(paramError.getFieldError().getFieldNum(), 2222);
		Assert.assertEquals(paramError.getFieldError().getErrorCode(),
				errorCodeSec);

		Assert.assertEquals(paramError.getParameterError().getParameterHeader()
				.getReserved(), 7);
		Assert.assertEquals(paramError.getParameterError().getParameterHeader()
				.getParameterType(), ParameterType.PARAMETER_ERROR);
		Assert.assertEquals(paramError.getParameterError().getParameterHeader()
				.getParameterLength(), 8);
		Assert.assertEquals(paramError.getParameterError().getParameterType(),
				9999);
		Assert.assertEquals(paramError.getParameterError().getErrorCode(),
				errorCodeSec);

		// getLength
		Assert.assertEquals(serializer.getLength(paramError), parameterLength);
	}

	@Test
	public void readerEventNotificationData()
			throws InvalidParameterTypeException {
		readerEventNotificationData(ParameterType.UPTIME);
		readerEventNotificationData(ParameterType.UTC_TIMESTAMP);
	}

	private void readerEventNotificationData(ParameterType parameterType)
			throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ReaderEventNotificationData rend = null;
		if (parameterType == ParameterType.UPTIME) {
			Uptime uptime = new Uptime(new TLVParameterHeader((byte) 18),
					new BigInteger("300"));
			rend = new ReaderEventNotificationData(new TLVParameterHeader(
					(byte) 17), uptime);
		} else {
			UTCTimestamp utcTimestamp = new UTCTimestamp(
					new TLVParameterHeader((byte) 18), new BigInteger("300"));
			rend = new ReaderEventNotificationData(new TLVParameterHeader(
					(byte) 17), utcTimestamp);
		}
		ConnectionAttemptEvent cae = new ConnectionAttemptEvent(
				new TLVParameterHeader((byte) 19),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON);
		rend.setConnectionAttemptEvent(cae);
		ConnectionCloseEvent cce = new ConnectionCloseEvent(
				new TLVParameterHeader((byte) 21));
		rend.setConnectionCloseEvent(cce);
		int parameterLength = 26;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rend, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((17 << 10) + ParameterType.READER_EVENT_NOTIFICATION_DATA
						.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(data.getShort(),
				((18 << 10) + parameterType.getValue()));
		Assert.assertEquals(data.getShort(), 12);
		data.getLong();
		Assert.assertEquals(
				data.getShort(),
				((19 << 10) + ParameterType.CONNECTION_ATTEMPT_EVENT.getValue()));
		Assert.assertEquals(data.getShort(), 6);
		data.position(data.position() + 2);
		Assert.assertEquals(data.getShort(),
				((21 << 10) + ParameterType.CONNECTION_CLOSE_EVENT.getValue()));
		Assert.assertEquals(data.getShort(), 4);
		data.rewind();

		// deserialize
		rend = serializer.deserializeReaderEventNotificationData(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = rend.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 17);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.READER_EVENT_NOTIFICATION_DATA);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		TLVParameterHeader header = parameterType == ParameterType.UPTIME ? rend
				.getUptime().getParameterHeader() : rend.getUtcTimestamp()
				.getParameterHeader();
		Assert.assertEquals(header.getParameterType(), parameterType);
		Assert.assertEquals(header.getParameterLength(), 12);
		TLVParameterHeader caeHeader = rend.getConnectionAttemptEvent()
				.getParameterHeader();
		Assert.assertEquals(caeHeader.getParameterType(),
				ParameterType.CONNECTION_ATTEMPT_EVENT);
		Assert.assertEquals(caeHeader.getParameterLength(), 6);
		TLVParameterHeader cceHeader = rend.getConnectionCloseEvent()
				.getParameterHeader();
		Assert.assertEquals(cceHeader.getParameterType(),
				ParameterType.CONNECTION_CLOSE_EVENT);
		Assert.assertEquals(cceHeader.getParameterLength(), 4);

		// getLength
		Assert.assertEquals(serializer.getLength(rend), parameterLength);
	}

	/**
	 * Create a byte buffer with a <code>ReaderEventNotificationData</code>
	 * parameter and replace the type of the <code>Uptime</code> parameter with
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
	 * <p>
	 * Create a byte buffer with a <code>ReaderEventNotificationData</code>
	 * parameter and replace the type of the <code>ConnectionAttemptEvent</code>
	 * parameter with another type.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalidParameterTypeException}
	 * </ul>
	 * </p>
	 * <p>
	 */
	public void readerEventNotificationDataError() {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ReaderEventNotificationData rend = null;
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 18),
				new BigInteger("300"));
		rend = new ReaderEventNotificationData(
				new TLVParameterHeader((byte) 17), uptime);
		ConnectionAttemptEvent cae = new ConnectionAttemptEvent(
				new TLVParameterHeader((byte) 19),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON);
		rend.setConnectionAttemptEvent(cae);
		int parameterLength = 22;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(rend, data);
		data.flip();
		// change parameter type 'Uptime' to 'ConnectionAttemptEvent'
		data.position(4);
		data.putShort((short) ((18 << 10) + ParameterType.CONNECTION_ATTEMPT_EVENT
				.getValue()));
		data.rewind();

		// deserialize
		try {
			rend = serializer.deserializeReaderEventNotificationData(
					(TLVParameterHeader) serializer
							.deserializeParameterHeader(data), data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type CONNECTION_ATTEMPT_EVENT"));
		}

		// serialize
		data.clear();
		serializer.serialize(rend, data);
		data.flip();
		// change parameter type 'ConnectionAttemptEvent' to 'Uptime'
		data.position(4 + serializer.getLength(uptime));
		data.putShort((short) ((18 << 10) + ParameterType.UPTIME.getValue()));
		data.rewind();

		// deserialize
		try {
			rend = serializer.deserializeReaderEventNotificationData(
					(TLVParameterHeader) serializer
							.deserializeParameterHeader(data), data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type UPTIME"));
		}
	}

	@Test
	public void connectionAttemptEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ConnectionAttemptEvent cae = new ConnectionAttemptEvent(
				new TLVParameterHeader((byte) 17),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON);
		int parameterLength = 6;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(cae, data);
		data.flip();
		Assert.assertEquals(
				data.getShort(),
				((17 << 10) + ParameterType.CONNECTION_ATTEMPT_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(data.getShort(),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON
						.getValue());
		data.rewind();

		// deserialize
		cae = serializer.deserializeConnectionAttemptEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = cae.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 17);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.CONNECTION_ATTEMPT_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(cae.getStatus(),
				ConnectionAttemptEventStatusType.FAILED_ANY_OTHER_REASON);

		// getLength
		Assert.assertEquals(serializer.getLength(cae), parameterLength);
	}

	@Test
	public void connectionCloseEvent() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		ConnectionCloseEvent cce = new ConnectionCloseEvent(
				new TLVParameterHeader((byte) 25));
		int parameterLength = 4;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(cce, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((25 << 10) + ParameterType.CONNECTION_CLOSE_EVENT.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		data.rewind();

		// deserialize
		cce = serializer.deserializeConnectionCloseEvent(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = cce.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 25);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.CONNECTION_CLOSE_EVENT);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		// getLength
		Assert.assertEquals(serializer.getLength(cce), parameterLength);
	}

	@Test
	public void uptime() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		Uptime uptime = new Uptime(new TLVParameterHeader((byte) 17),
				new BigInteger("300"));
		int parameterLength = 12;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(uptime, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((17 << 10) + ParameterType.UPTIME.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				300);
		data.rewind();

		// deserialize
		uptime = serializer.deserializeUptime((TLVParameterHeader) serializer
				.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = uptime.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 17);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.UPTIME);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(uptime.getMicroseconds().intValue(), 300);

		// getLength
		Assert.assertEquals(serializer.getLength(uptime), parameterLength);
	}

	@Test
	public void utcTimestamp() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize
		UTCTimestamp utcTimestamp = new UTCTimestamp(new TLVParameterHeader(
				(byte) 17), new BigInteger("300"));
		int parameterLength = 12;
		ByteBuffer data = ByteBuffer.allocate(parameterLength);
		serializer.serialize(utcTimestamp, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((17 << 10) + ParameterType.UTC_TIMESTAMP.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		Assert.assertEquals(DataTypeConverter.ulong(data.getLong()).intValue(),
				300);
		data.rewind();

		// deserialize
		utcTimestamp = serializer.deserializeUTCTimestamp(
				(TLVParameterHeader) serializer
						.deserializeParameterHeader(data), data);
		TLVParameterHeader tlvParameterHeader = utcTimestamp
				.getParameterHeader();
		Assert.assertEquals(tlvParameterHeader.getReserved(), 17);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.UTC_TIMESTAMP);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);
		Assert.assertEquals(utcTimestamp.getMicroseconds().intValue(), 300);

		// getLength
		Assert.assertEquals(serializer.getLength(utcTimestamp), parameterLength);
	}

	@Test
	public void parameterHeader() throws InvalidParameterTypeException {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize TLV
		int parameterLength = 7;
		TLVParameterHeader tlvParameterHeader = new TLVParameterHeader(
		/* reserved */(byte) 17);
		tlvParameterHeader.setParameterType(ParameterType.UPTIME);
		tlvParameterHeader.setParameterLength(parameterLength);
		ByteBuffer data = ByteBuffer.allocate(4);
		serializer.serialize(tlvParameterHeader, data);
		data.flip();
		Assert.assertEquals(data.getShort(),
				((17 << 10) + ParameterType.UPTIME.getValue()));
		Assert.assertEquals(DataTypeConverter.ushort(data.getShort()),
				parameterLength);
		data.rewind();

		// deserialize TLV
		tlvParameterHeader = (TLVParameterHeader) serializer
				.deserializeParameterHeader(data);
		Assert.assertEquals(tlvParameterHeader.getReserved(), 17);
		Assert.assertEquals(tlvParameterHeader.getParameterType(),
				ParameterType.UPTIME);
		Assert.assertEquals(tlvParameterHeader.getParameterLength(),
				parameterLength);

		// serialize TV
		TVParameterHeader tvParameterHeader = new TVParameterHeader();
		tvParameterHeader.setParameterType(ParameterType.EPC_96);
		data = ByteBuffer.allocate(1);
		serializer.serialize(tvParameterHeader, data);
		data.flip();
		Assert.assertEquals(DataTypeConverter.ubyte(data.get()), (1 << 7)
				+ ParameterType.EPC_96.getValue());
		data.rewind();

		// deserialize TV
		tvParameterHeader = (TVParameterHeader) serializer
				.deserializeParameterHeader(data);
		Assert.assertEquals(tvParameterHeader.getParameterType(),
				ParameterType.EPC_96);
	}

	/**
	 * Create a byte buffer with a <code>TLVParameterHeader</code> and replace
	 * the parameter type with an invalid type.
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalidParameterTypeException}
	 * </ul>
	 * </p>
	 * <p>
	 * Create a byte buffer with a <code>TVParameterHeader</code> and replace
	 * the parameter type with an invalid type.
	 * </p>
	 * <p>
	 * Deserialize the byte buffer.
	 * </p>
	 * <p>
	 * Expected:
	 * <ul>
	 * <li>{@link InvalidParameterTypeException}
	 * </ul>
	 * </p>
	 */
	@Test
	public void parameterHeaderError() {
		ByteBufferSerializer serializer = new ByteBufferSerializer();

		// serialize TLV
		int parameterLength = 7;
		TLVParameterHeader tlvParameterHeader = new TLVParameterHeader((byte) 0);
		tlvParameterHeader.setParameterType(ParameterType.UPTIME);
		tlvParameterHeader.setParameterLength(parameterLength);
		ByteBuffer data = ByteBuffer.allocate(4);
		serializer.serialize(tlvParameterHeader, data);
		data.flip();
		// change parameter type to 1000
		data.putShort((short) 1000);
		data.rewind();

		// deserialize TLV
		try {
			tlvParameterHeader = (TLVParameterHeader) serializer
					.deserializeParameterHeader(data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type 1000"));
		}

		// serialize TV
		TVParameterHeader tvParameterHeader = new TVParameterHeader();
		tvParameterHeader.setParameterType(ParameterType.EPC_96);
		data = ByteBuffer.allocate(1);
		serializer.serialize(tvParameterHeader, data);
		data.flip();
		// change parameter type to 127
		data.put((byte) ((1 << 7) | 127));
		data.rewind();

		// deserialize TV
		try {
			tvParameterHeader = (TVParameterHeader) serializer
					.deserializeParameterHeader(data);
			Assert.fail();
		} catch (InvalidParameterTypeException e) {
			Assert.assertTrue(e.getMessage().contains(
					"Invalid parameter type 127"));
		}
	}
}
