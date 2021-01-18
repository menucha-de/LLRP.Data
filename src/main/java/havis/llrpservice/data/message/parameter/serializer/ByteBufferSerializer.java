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

/**
 * On deserializing empty lists will be generated for mandatory and optional
 * lists. On serializing the mandatory lists must not be null, but the optional
 * lists can be null.
 */
public class ByteBufferSerializer {

	private static final byte TLV_PARAMETER_HEADER_LENGTH = 4;

	private static final byte TV_PARAMETER_HEADER_LENGTH = 1;

	public ParameterHeader deserializeParameterHeader(ByteBuffer data)
			throws InvalidParameterTypeException {
		byte b1 = data.get();
		// TV flag 1 bit
		boolean isTVParameter = (b1 & 0x80) > 0;
		if (isTVParameter) {
			// type 7 bits
			short parameterType = (short) (b1 & 0x7F);
			ParameterType type = ParameterTypes.get(parameterType);
			if (type == null) {
				throw new InvalidParameterTypeException(
						"Invalid parameter type " + parameterType);
			}
			TVParameterHeader header = new TVParameterHeader();
			header.setParameterType(type);
			return header;
		} else {
			// reserved 6 bits
			byte reserved = (byte) ((b1 & 0xFF) >> 2);
			byte b2 = data.get();
			// type 10 bits
			short parameterType = (short) (((b1 & 0x03) << 8) | (b2 & 0xFF));
			ParameterType type = ParameterTypes.get(parameterType);
			if (type == null) {
				throw new InvalidParameterTypeException(
						"Invalid parameter type " + parameterType);
			}
			// length 16 bits
			int length = DataTypeConverter.ushort(data.getShort());
			TLVParameterHeader header = new TLVParameterHeader(reserved);
			header.setParameterType(type);
			header.setParameterLength(length);
			return header;
		}
	}

	public void serialize(ParameterHeader parameterHeader, ByteBuffer data) {
		if (parameterHeader.isTLV()) {
			TLVParameterHeader tlvParameterHeader = (TLVParameterHeader) parameterHeader;
			// reserved 6 bits
			short s = (short) (tlvParameterHeader.getReserved() << 10);
			// type 10 bits
			s |= tlvParameterHeader.getParameterType().getValue();
			data.putShort(s);
			// length 16 bits
			data.putShort((short) tlvParameterHeader.getParameterLength());
		} else {
			// TLV flag 1 bit
			// type 7 bits
			data.put((byte) (0x80 | parameterHeader.getParameterType()
					.getValue()));
		}
	}

	/**
	 * Section 17.2.2.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 */
	public UTCTimestamp deserializeUTCTimestamp(TLVParameterHeader header,
			ByteBuffer data) {
		// deserialize parameter
		// microseconds 64 bits
		BigInteger microseconds = DataTypeConverter.ulong(data.getLong());
		return new UTCTimestamp(header, microseconds);
	}

	public void serialize(UTCTimestamp utcTimestamp, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = utcTimestamp.getParameterHeader();
		header.setParameterLength(getLength(utcTimestamp));
		// serialize parameter
		serialize(header, data);
		// microseconds 64 bits
		data.putLong(utcTimestamp.getMicroseconds().longValue());
	}

	public int getLength(UTCTimestamp utcTimestamp) {
		return TLV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.2.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 */
	public Uptime deserializeUptime(TLVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		// microseconds 64 bits
		BigInteger microseconds = DataTypeConverter.ulong(data.getLong());
		return new Uptime(header, microseconds);
	}

	public void serialize(Uptime uptime, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = uptime.getParameterHeader();
		header.setParameterLength(getLength(uptime));
		// serialize parameter
		serialize(header, data);
		// microseconds 64 bits
		data.putLong(uptime.getMicroseconds().longValue());
	}

	public int getLength(Uptime uptime) {
		return TLV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.3.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GeneralDeviceCapabilities deserializeGeneralDeviceCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		GeneralDeviceCapabilities genDevCap = null;
		int maximumNumberOfAntennasSupported = DataTypeConverter.ushort(data
				.getShort());
		byte b = data.get();
		boolean canSetAntennaProperties = (b & 0x80) > 0;
		boolean hasUTCClockCapability = (b & 0x40) > 0;
		data.get();
		long deviceManufacturerName = DataTypeConverter.uint(data.getInt());
		long modelName = DataTypeConverter.uint(data.getInt());
		int fvbc = DataTypeConverter.ushort(data.getShort());
		byte[] dst = new byte[fvbc];
		data.get(dst, 0, fvbc);
		String firmwareVersion = new String(dst, StandardCharsets.UTF_8);
		GPIOCapabilities gpioCap = null;
		MaximumReceiveSensitivity mrs = null;
		List<ReceiveSensitivityTabelEntry> receiveSensitivityTable = new ArrayList<>();
		List<PerAntennaReceiveSensitivityRange> perAntennaReceiveSensitivityRange = new ArrayList<>();
		List<PerAntennaAirProtocol> airProtocolSupportedPerAntenna = new ArrayList<>();

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RECEIVE_SENSITIVITY_TABLE_ENTRY:
				ReceiveSensitivityTabelEntry rste = deserializeReceiveSensitivityTabelEntry(
						(TLVParameterHeader) parameterHeader, data);
				receiveSensitivityTable.add(rste);
				break;
			case PER_ANTENNA_RECEIVE_SENSITIVITY_RANGE:
				PerAntennaReceiveSensitivityRange parsr = deserializePerAntennaReceiveSensitivityRange(
						(TLVParameterHeader) parameterHeader, data);
				perAntennaReceiveSensitivityRange.add(parsr);
				break;
			case GPIO_CAPABILITIES:
				gpioCap = deserializeGPIOCapabilities(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case PER_ANTENNA_AIR_PROTOCOL:
				PerAntennaAirProtocol paap = deserializePerAntennaAirProtocol(
						(TLVParameterHeader) parameterHeader, data);
				airProtocolSupportedPerAntenna.add(paap);
				break;
			case MAXIMUM_RECEIVE_SENSITIVITY:
				mrs = deserializeMximumReceiveSensitivity(
						(TLVParameterHeader) parameterHeader, data);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		genDevCap = new GeneralDeviceCapabilities(header,
				deviceManufacturerName, modelName, firmwareVersion,
				maximumNumberOfAntennasSupported, canSetAntennaProperties,
				receiveSensitivityTable, airProtocolSupportedPerAntenna,
				gpioCap, hasUTCClockCapability);
		genDevCap.setFirmwareVersionByteCount(fvbc);
		genDevCap
				.setPerAntennaReceiveSensitivityRange(perAntennaReceiveSensitivityRange);
		genDevCap.setMaximumReceiveSensitivity(mrs);
		return genDevCap;
	}

	public void serialize(GeneralDeviceCapabilities genDevCap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = genDevCap.getParameterHeader();
		header.setParameterLength(getLength(genDevCap));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) genDevCap.getMaxNumberOfAntennasSupported());
		byte b = (byte) 0x00;
		if (genDevCap.isCanSetAntennaProperties()) {
			b = (byte) (b | 0x80);
		}
		if (genDevCap.isHasUTCClockCapability()) {
			b = (byte) (b | 0x40);
		}
		data.put(b);
		byte reserved = 0x00;
		data.put(reserved);
		data.putInt((int) genDevCap.getDeviceManufacturerName());
		data.putInt((int) genDevCap.getModelName());

		String readerFV = genDevCap.getFirmwareVersion();
		byte[] byteReaderFV = readerFV.getBytes(StandardCharsets.UTF_8);
		int fvbc = byteReaderFV.length;
		genDevCap.setFirmwareVersionByteCount(fvbc);
		data.putShort((short) fvbc);
		data.put(byteReaderFV);

		for (ReceiveSensitivityTabelEntry rste : genDevCap
				.getReceiveSensitivityTableEntry()) {
			serialize(rste, data);
		}

		if (genDevCap.getPerAntennaReceiveSensitivityRange() != null
				&& !genDevCap.getPerAntennaReceiveSensitivityRange().isEmpty()) {
			for (PerAntennaReceiveSensitivityRange parsr : genDevCap
					.getPerAntennaReceiveSensitivityRange()) {
				serialize(parsr, data);
			}
		}
		GPIOCapabilities gpio = genDevCap.getGpioCapabilities();
		serialize(gpio, data);

		for (PerAntennaAirProtocol paap : genDevCap
				.getPerAntennaAirProtocol()) {
			serialize(paap, data);
		}

		MaximumReceiveSensitivity mrs = genDevCap
				.getMaximumReceiveSensitivity();
		if (mrs != null) {
			serialize(mrs, data);
		}
	}

	public int getLength(GeneralDeviceCapabilities genDevCap) {
		String readerFV = genDevCap.getFirmwareVersion();
		int fvbc = readerFV.getBytes(StandardCharsets.UTF_8).length;
		int length = TLV_PARAMETER_HEADER_LENGTH + 14 + fvbc;
		for (ReceiveSensitivityTabelEntry rste : genDevCap
				.getReceiveSensitivityTableEntry()) {
			length += getLength(rste);
		}
		if (genDevCap.getPerAntennaReceiveSensitivityRange() != null
				&& !genDevCap.getPerAntennaReceiveSensitivityRange().isEmpty()) {
			for (PerAntennaReceiveSensitivityRange parsr : genDevCap
					.getPerAntennaReceiveSensitivityRange()) {
				length += getLength(parsr);
			}
		}
		GPIOCapabilities gpio = genDevCap.getGpioCapabilities();
		length += getLength(gpio);
		for (PerAntennaAirProtocol paap : genDevCap
				.getPerAntennaAirProtocol()) {
			length += getLength(paap);
		}
		MaximumReceiveSensitivity mrs = genDevCap
				.getMaximumReceiveSensitivity();
		if (mrs != null) {
			length += getLength(mrs);
		}
		return length;
	}

	/**
	 * Section 17.2.3.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public MaximumReceiveSensitivity deserializeMximumReceiveSensitivity(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int maximumSensitivityValue = DataTypeConverter.ushort(data.getShort());
		return new MaximumReceiveSensitivity(header, maximumSensitivityValue);
	}

	public void serialize(MaximumReceiveSensitivity mrs, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = mrs.getParameterHeader();
		header.setParameterLength(getLength(mrs));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) mrs.getMaximumSensitivityValue());
	}

	public int getLength(MaximumReceiveSensitivity mrs) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		return length;
	}

	/**
	 * Section 17.2.3.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public ReceiveSensitivityTabelEntry deserializeReceiveSensitivityTabelEntry(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int index = DataTypeConverter.ushort(data.getShort());
		int receiveSensitivityValue = DataTypeConverter.ushort(data.getShort());
		return new ReceiveSensitivityTabelEntry(header, index,
				receiveSensitivityValue);
	}

	public void serialize(ReceiveSensitivityTabelEntry rst, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rst.getParameterHeader();
		header.setParameterLength(getLength(rst));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) rst.getIndex());
		data.putShort((short) rst.getReceiveSensitivityValue());
	}

	public int getLength(ReceiveSensitivityTabelEntry rst) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.2.3.1.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public PerAntennaReceiveSensitivityRange deserializePerAntennaReceiveSensitivityRange(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int antennaID = DataTypeConverter.ushort(data.getShort());
		int receiveSensitivityIndexMin = DataTypeConverter.ushort(data
				.getShort());
		int receiveSensitivityIndexMax = DataTypeConverter.ushort(data
				.getShort());
		return new PerAntennaReceiveSensitivityRange(header, antennaID,
				receiveSensitivityIndexMin, receiveSensitivityIndexMax);
	}

	public void serialize(PerAntennaReceiveSensitivityRange parsr,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = parsr.getParameterHeader();
		header.setParameterLength(getLength(parsr));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) parsr.getAntennaID());
		data.putShort((short) parsr.getReceiveSensitivityIndexMin());
		data.putShort((short) parsr.getReceiveSensitivityIndexMax());
	}

	public int getLength(PerAntennaReceiveSensitivityRange parsr) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 6;
		return length;
	}

	/**
	 * Section 17.2.3.1.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public PerAntennaAirProtocol deserializePerAntennaAirProtocol(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		PerAntennaAirProtocol paap = null;
		int antennaID = DataTypeConverter.ushort(data.getShort());
		int numProtocols = DataTypeConverter.ushort(data.getShort());

		List<ProtocolId> airProtocolsSupported = new ArrayList<ProtocolId>();
		for (int i = 0; i < numProtocols; i++) {
			airProtocolsSupported.add(ProtocolId.get(DataTypeConverter
					.ubyte(data.get())));
		}

		paap = new PerAntennaAirProtocol(header, antennaID,
				airProtocolsSupported);
		paap.setNumProtocols(numProtocols);
		return paap;
	}

	public void serialize(PerAntennaAirProtocol paap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = paap.getParameterHeader();
		header.setParameterLength(getLength(paap));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) paap.getAntennaID());

		int numProtocols = paap.getAirProtocolsSupported().size();
		paap.setNumProtocols(numProtocols);
		data.putShort((short) numProtocols);

		for (ProtocolId airProtocolSupported : paap.getAirProtocolsSupported()) {
			data.put((byte) airProtocolSupported.getValue());
		}
	}

	public int getLength(PerAntennaAirProtocol paap) {
		return TLV_PARAMETER_HEADER_LENGTH + 4
				+ paap.getAirProtocolsSupported().size();
	}

	/**
	 * Section 17.2.3.1.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GPIOCapabilities deserializeGPIOCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int numGPIs = DataTypeConverter.ushort(data.getShort());
		int numGPOs = DataTypeConverter.ushort(data.getShort());
		return new GPIOCapabilities(header, numGPIs, numGPOs);
	}

	public void serialize(GPIOCapabilities gpioCap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = gpioCap.getParameterHeader();
		header.setParameterLength(getLength(gpioCap));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) gpioCap.getNumGPIs());
		data.putShort((short) gpioCap.getNumGPOs());
	}

	public int getLength(GPIOCapabilities gpioCap) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.2.3.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public LLRPCapabilities deserializeLLRPCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {

		byte b = data.get();
		boolean canDoRFSurvey = (b & 0x80) > 0;
		boolean canReportBufferFillWarning = (b & 0x40) > 0;
		boolean supportsClientRequestOpSpec = (b & 0x20) > 0;
		boolean canDoTagInventoryStateAwareSingulation = (b & 0x10) > 0;
		boolean supportsEventAndReportHolding = (b & 0x08) > 0;
		byte maxPriorityLevelSupported = data.get();
		int clientRequestOpSpecTimeout = DataTypeConverter.ushort(data
				.getShort());
		long maxNumROSpecs = DataTypeConverter.uint(data.getInt());
		long maxNumSpecsPerROSpec = DataTypeConverter.uint(data.getInt());
		long maxNumInventoryParameterSpecsPerAISpec = DataTypeConverter
				.uint(data.getInt());
		long maxNumAccessSpecs = DataTypeConverter.uint(data.getInt());
		long maxNumOpSpecsPerAccessSpec = DataTypeConverter.uint(data.getInt());

		return new LLRPCapabilities(header, canDoRFSurvey,
				canReportBufferFillWarning, supportsClientRequestOpSpec,
				canDoTagInventoryStateAwareSingulation,
				supportsEventAndReportHolding, maxPriorityLevelSupported,
				clientRequestOpSpecTimeout, maxNumROSpecs,
				maxNumSpecsPerROSpec, maxNumInventoryParameterSpecsPerAISpec,
				maxNumAccessSpecs, maxNumOpSpecsPerAccessSpec);
	}

	public void serialize(LLRPCapabilities llrpCap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = llrpCap.getParameterHeader();
		header.setParameterLength(getLength(llrpCap));
		// serialize parameter
		serialize(header, data);
		llrpCap.isCanDoRFSurvey();
		byte b = (byte) 0x00;
		if (llrpCap.isCanDoRFSurvey()) {
			b = (byte) (b | 0x80);
		}
		if (llrpCap.isCanReportBufferFillWarning()) {
			b = (byte) (b | 0x40);
		}
		if (llrpCap.isSupportsClientRequestOpSpec()) {
			b = (byte) (b | 0x20);
		}
		if (llrpCap.isCanDoTagInventoryStateAwareSingulation()) {
			b = (byte) (b | 0x10);
		}
		if (llrpCap.isSupportsEventAndReportHolding()) {
			b = (byte) (b | 0x08);
		}
		data.put(b);
		data.put(llrpCap.getMaxPriorityLevelSupported());
		data.putShort((short) llrpCap.getClientRequestOpSpecTimeout());
		data.putInt((int) llrpCap.getMaxNumROSpecs());
		data.putInt((int) llrpCap.getMaxNumSpecsPerROSpec());
		data.putInt((int) llrpCap.getMaxNumInventoryParameterSpecsPerAISpec());
		data.putInt((int) llrpCap.getMaxNumAccessSpecs());
		data.putInt((int) llrpCap.getMaxNumOpSpecsPerAccessSpec());
	}

	public int getLength(LLRPCapabilities llrpCap) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 24;
		return length;
	}

	/**
	 * Section 17.2.3.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public RegulatoryCapabilities deserializeRegulatoryCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		CountryCodes.CountryCode countryCode = CountryCodes
				.get(DataTypeConverter.ushort(data.getShort()));
		CommunicationsStandard communicationsStandard = CommunicationsStandard
				.get(DataTypeConverter.ushort(data.getShort()));
		List<Custom> CusExPoint = new ArrayList<Custom>();
		RegulatoryCapabilities regCap = new RegulatoryCapabilities(header,
				countryCode, communicationsStandard);
		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case UHF_BAND_CAPABILITIES:
				UHFBandCapabilities uhfBandCap = deserializeUHFBandCapabilities(
						(TLVParameterHeader) parameterHeader, data);
				regCap.setUhfBandCapabilities(uhfBandCap);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				CusExPoint.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		regCap.setCustomExtensionPoint(CusExPoint);
		return regCap;
	}

	public void serialize(RegulatoryCapabilities regCap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = regCap.getParameterHeader();
		header.setParameterLength(getLength(regCap));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) regCap.getCountryCode().getValue());
		data.putShort((short) regCap.getCommunicationsStandard().getValue());

		UHFBandCapabilities uhfBandCap = regCap.getUhfBandCapabilities();
		if (uhfBandCap != null) {
			serialize(uhfBandCap, data);
		}
		if (regCap.getCustomExtensionPoint() != null
				&& !regCap.getCustomExtensionPoint().isEmpty()) {
			for (Custom cus : regCap.getCustomExtensionPoint()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(RegulatoryCapabilities regCap) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		UHFBandCapabilities uhfBandCap = regCap.getUhfBandCapabilities();
		if (uhfBandCap != null) {
			length += getLength(uhfBandCap);
		}
		if (regCap.getCustomExtensionPoint() != null
				&& !regCap.getCustomExtensionPoint().isEmpty()) {
			for (Custom cus : regCap.getCustomExtensionPoint()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.3.4.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public UHFBandCapabilities deserializeUHFBandCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		UHFBandCapabilities uhfBandCap = null;
		List<TransmitPowerLevelTableEntry> transmitPowerTable = new ArrayList<TransmitPowerLevelTableEntry>();
		FrequencyInformation frequencyInformation = null;
		List<UHFC1G2RFModeTable> uhfC1G2RFModeTable = new ArrayList<UHFC1G2RFModeTable>();
		RFSurveyFrequencyCapabilities rfSurveyFreqCap = null;
		loop: while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case TRANSMIT_POWER_LEVEL_TABLE_ENTRY:
				TransmitPowerLevelTableEntry tpltEntry = deserializeTransmitPowerLevelTableEntry(
						(TLVParameterHeader) parameterHeader, data);
				transmitPowerTable.add(tpltEntry);
				break;
			case FREQUENCY_INFORMATION:
				if (transmitPowerTable.size() == 0) {
					throw new InvalidParameterTypeException(
							"At least one parameter expected of type "
									+ ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY);
				}
				frequencyInformation = deserializeFrequencyInformation(
						(TLVParameterHeader) parameterHeader, data);
				break loop;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case UHF_C1G2_RF_MODE_TABLE:
				UHFC1G2RFModeTable uhfc1g2RFModeTable = deserializeUHFC1G2RFModeTable(
						(TLVParameterHeader) parameterHeader, data);
				uhfC1G2RFModeTable.add(uhfc1g2RFModeTable);
				break;
			case RF_SURVEY_FREQUENCY_CAPABILITIES:
				if (uhfC1G2RFModeTable.size() == 0) {
					throw new InvalidParameterTypeException(
							"At least one parameter expected of type "
									+ ParameterType.UHF_C1G2_RF_MODE_TABLE);
				}
				rfSurveyFreqCap = deserializeRFSurveyFrequencyCapabilities(
						(TLVParameterHeader) parameterHeader, data);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		if (uhfC1G2RFModeTable.size() == 0) {
			throw new InvalidParameterTypeException(
					"At least one parameter expected of type "
							+ ParameterType.UHF_C1G2_RF_MODE_TABLE);
		}
		uhfBandCap = new UHFBandCapabilities(header, transmitPowerTable,
				frequencyInformation, uhfC1G2RFModeTable);
		uhfBandCap.setRfSurveyFrequencyCapabilities(rfSurveyFreqCap);
		return uhfBandCap;
	}

	public void serialize(UHFBandCapabilities uhfBandCap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = uhfBandCap.getParameterHeader();
		header.setParameterLength(getLength(uhfBandCap));
		// serialize parameter
		serialize(header, data);

		for (TransmitPowerLevelTableEntry tpltEntry : uhfBandCap
				.getTransmitPowerTable()) {
			serialize(tpltEntry, data);
		}
		FrequencyInformation frequencyInformation = uhfBandCap
				.getFrequencyInformation();
		serialize(frequencyInformation, data);
		for (UHFC1G2RFModeTable uhfc1g2RFModeTable : uhfBandCap
				.getUhfC1G2RFModeTable()) {
			serialize(uhfc1g2RFModeTable, data);
		}
		RFSurveyFrequencyCapabilities rfSurveyFreqCap = uhfBandCap
				.getRfSurveyFrequencyCapabilities();
		if (rfSurveyFreqCap != null) {
			serialize(rfSurveyFreqCap, data);
		}
	}

	public int getLength(UHFBandCapabilities uhfBandCap) {
		int length = TLV_PARAMETER_HEADER_LENGTH;

		for (TransmitPowerLevelTableEntry tpltEntry : uhfBandCap
				.getTransmitPowerTable()) {
			length += getLength(tpltEntry);
		}
		FrequencyInformation frequencyInformation = uhfBandCap
				.getFrequencyInformation();
		length += getLength(frequencyInformation);
		for (UHFC1G2RFModeTable uhfc1g2RFModeTable : uhfBandCap
				.getUhfC1G2RFModeTable()) {
			length += getLength(uhfc1g2RFModeTable);
		}

		RFSurveyFrequencyCapabilities rfSurveyFreqCap = uhfBandCap
				.getRfSurveyFrequencyCapabilities();
		if (rfSurveyFreqCap != null) {
			length += getLength(rfSurveyFreqCap);
		}
		return length;
	}

	/**
	 * Section 17.2.3.4.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public TransmitPowerLevelTableEntry deserializeTransmitPowerLevelTableEntry(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int index = DataTypeConverter.ushort(data.getShort());
		short transmitPowerValue = data.getShort();
		return new TransmitPowerLevelTableEntry(header, index,
				transmitPowerValue);
	}

	public void serialize(TransmitPowerLevelTableEntry tplte, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = tplte.getParameterHeader();
		header.setParameterLength(getLength(tplte));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) tplte.getIndex());
		data.putShort(tplte.getTransmitPowerValue());
	}

	public int getLength(TransmitPowerLevelTableEntry tplte) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.2.3.4.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public FrequencyInformation deserializeFrequencyInformation(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		FrequencyInformation freqInfo = null;
		List<FrequencyHopTable> freqHopInfo = new ArrayList<FrequencyHopTable>();

		byte b = data.get();
		boolean hopping = (b & 0x80) > 0;

		if (hopping) {
			while (header.getParameterLength() > (data.position()
					- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
				ParameterHeader parameterHeader = deserializeParameterHeader(data);
				switch (parameterHeader.getParameterType()) {
				case FREQUENCY_HOP_TABLE:
					FrequencyHopTable frequencyHopTable = deserializeFrequencyHopTable(
							(TLVParameterHeader) parameterHeader, data);
					freqHopInfo.add(frequencyHopTable);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ parameterHeader.getParameterType());
				}
			}
			freqInfo = new FrequencyInformation(header, freqHopInfo);
		} else {
			loop: while (header.getParameterLength() > (data.position()
					- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
				ParameterHeader parameterHeader = deserializeParameterHeader(data);
				switch (parameterHeader.getParameterType()) {
				case FIXED_FREQUENCY_TABLE:
					FixedFrequencyTable fixedFreqInfo = deserializeFixedFrequencyTable(
							(TLVParameterHeader) parameterHeader, data);
					freqInfo = new FrequencyInformation(header, fixedFreqInfo);
					break loop;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ parameterHeader.getParameterType());
				}
			}
		}
		return freqInfo;
	}

	public void serialize(FrequencyInformation freqInfo, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = freqInfo.getParameterHeader();
		header.setParameterLength(getLength(freqInfo));
		// serialize parameter
		serialize(header, data);
		byte b = (byte) 0x00;
		if (freqInfo.isHopping()) {
			b = (byte) 0x80;
			data.put(b);
			if (freqInfo.getFreqHopInfo() != null
					&& !freqInfo.getFreqHopInfo().isEmpty()) {
				for (FrequencyHopTable frequencyHopTable : freqInfo
						.getFreqHopInfo()) {
					serialize(frequencyHopTable, data);
				}
			}
		} else {
			data.put(b);
			if (freqInfo.getFixedFreqInfo() != null) {
				FixedFrequencyTable fixedFreqInfo = freqInfo.getFixedFreqInfo();
				serialize(fixedFreqInfo, data);
			}
		}
	}

	public int getLength(FrequencyInformation freqInfo) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		if (freqInfo.getFreqHopInfo() != null
				&& !freqInfo.getFreqHopInfo().isEmpty()) {
			for (FrequencyHopTable freqHopTable : freqInfo.getFreqHopInfo()) {
				length += getLength(freqHopTable);
			}
		} else if (freqInfo.getFixedFreqInfo() != null) {
			FixedFrequencyTable fixedFreqInfo = freqInfo.getFixedFreqInfo();
			length += getLength(fixedFreqInfo);
		}
		return length;
	}

	/**
	 * Section 17.2.3.4.1.2.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public FrequencyHopTable deserializeFrequencyHopTable(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		FrequencyHopTable freqHopTable = null;
		short hopTableID = DataTypeConverter.ubyte(data.get());
		data.get();
		int numHops = DataTypeConverter.ushort(data.getShort());

		List<Long> hopFrequency = new ArrayList<Long>();
		for (int i = 0; i < numHops; i++) {
			hopFrequency.add(DataTypeConverter.uint(data.getInt()));
		}

		freqHopTable = new FrequencyHopTable(header, hopTableID, hopFrequency);
		freqHopTable.setNumHops(numHops);
		return freqHopTable;
	}

	public void serialize(FrequencyHopTable freqHopTable, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = freqHopTable.getParameterHeader();
		header.setParameterLength(getLength(freqHopTable));
		// serialize parameter
		serialize(header, data);
		data.put((byte) freqHopTable.getHopTableID());
		byte reserved = 0x00;
		data.put(reserved);
		int numHops = freqHopTable.getHopFrequency().size();
		freqHopTable.setNumHops(numHops);
		data.putShort((short) numHops);
		if (freqHopTable.getHopFrequency() != null
				&& !freqHopTable.getHopFrequency().isEmpty()) {
			for (Long freq : freqHopTable.getHopFrequency()) {
				data.putInt((int) freq.longValue());
			}
		}
	}

	public int getLength(FrequencyHopTable freqHopTable) {
		return TLV_PARAMETER_HEADER_LENGTH + 4
				+ freqHopTable.getHopFrequency().size() * 4;
	}

	/**
	 * Section 17.2.3.4.1.2.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public FixedFrequencyTable deserializeFixedFrequencyTable(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		FixedFrequencyTable fixedFreqTable = null;
		int numFreqs = DataTypeConverter.ushort(data.getShort());
		List<Long> fixedFrequency = new ArrayList<Long>();
		for (int i = 0; i < numFreqs; i++) {
			fixedFrequency.add(DataTypeConverter.uint(data.getInt()));
		}

		fixedFreqTable = new FixedFrequencyTable(header, fixedFrequency);
		fixedFreqTable.setNumFrequencies(numFreqs);
		return fixedFreqTable;
	}

	public void serialize(FixedFrequencyTable fixedFreqTable, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = fixedFreqTable.getParameterHeader();
		header.setParameterLength(getLength(fixedFreqTable));
		// serialize parameter
		serialize(header, data);

		int numFreqs = fixedFreqTable.getFrequency().size();
		fixedFreqTable.setNumFrequencies(numFreqs);
		data.putShort((short) numFreqs);
		if (fixedFreqTable.getFrequency() != null
				&& !fixedFreqTable.getFrequency().isEmpty()) {
			for (Long fixedFreq : fixedFreqTable.getFrequency()) {
				data.putInt((int) fixedFreq.longValue());
			}
		}
	}

	public int getLength(FixedFrequencyTable fixedFreqTable) {
		return TLV_PARAMETER_HEADER_LENGTH + 2
				+ fixedFreqTable.getFrequency().size() * 4;
	}

	/**
	 * Section 17.2.3.4.1.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public RFSurveyFrequencyCapabilities deserializeRFSurveyFrequencyCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		RFSurveyFrequencyCapabilities rfSFC = null;
		long minFreq = DataTypeConverter.uint(data.getInt());
		long maxFreq = DataTypeConverter.uint(data.getInt());
		rfSFC = new RFSurveyFrequencyCapabilities(header, minFreq, maxFreq);
		return rfSFC;
	}

	public void serialize(RFSurveyFrequencyCapabilities rfSFC, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfSFC.getParameterHeader();
		header.setParameterLength(getLength(rfSFC));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) rfSFC.getMinimumFrequency());
		data.putInt((int) rfSFC.getMaximumFrequency());
	}

	public int getLength(RFSurveyFrequencyCapabilities rfSFC) {
		return TLV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.4.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public ROSpec deserializeROSpec(TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		ROSpec ros = null;
		ROBoundarySpec roBS = null;
		List<Parameter> specList = new ArrayList<>();
		AISpec aiSpec = null;
		RFSurveySpec rfSurveySpec = null;
		LoopSpec loopSpec = null;
		Custom custom = null;
		ROReportSpec roReportSpec = null;
		long specID = DataTypeConverter.uint(data.getInt());
		short priotity = DataTypeConverter.ubyte(data.get());
		ROSpecCurrentState currentState = ROSpecCurrentState
				.get(DataTypeConverter.ubyte(data.get()));

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case RO_BOUNDARY_SPEC:
			roBS = deserializeROBoundarySpec(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException(
					"Parameter expected of type "
							+ ParameterType.RO_BOUNDARY_SPEC);
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {

			case AI_SPEC:
				aiSpec = deserializeAISpec(
						(TLVParameterHeader) parameterHeader, data);
				specList.add(aiSpec);
				break;
			case RF_SURVEY_SPEC:
				rfSurveySpec = deserializeRFSurveySpec(
						(TLVParameterHeader) parameterHeader, data);
				specList.add(rfSurveySpec);
				break;
			case LOOP_SPEC:
				loopSpec = deserializeLoopSpec(
						(TLVParameterHeader) parameterHeader, data);
				specList.add(loopSpec);
				break;
			case CUSTOM:
				custom = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				specList.add(custom);
				break;
			case RO_REPORT_SPEC:
				roReportSpec = deserializeROReportSpec(
						(TLVParameterHeader) parameterHeader, data);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		if (!specList.isEmpty()) {
			ros = new ROSpec(header, specID, priotity, currentState, roBS,
					specList);
		} else {
			throw new InvalidParameterTypeException(
					"At least one parameter expected of types "
							+ ParameterType.AI_SPEC + " "
							+ ParameterType.RF_SURVEY_SPEC + " "
							+ ParameterType.LOOP_SPEC + " "
							+ ParameterType.CUSTOM);
		}

		if (roReportSpec != null) {
			ros.setRoReportSpec(roReportSpec);
		}
		return ros;
	}

	public void serialize(ROSpec ros, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set parameter length
		TLVParameterHeader header = ros.getParameterHeader();
		header.setParameterLength(getLength(ros));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) ros.getRoSpecID());
		data.put((byte) ros.getPriority());
		data.put((byte) ros.getCurrentState().getValue());
		ROBoundarySpec roBS = ros.getRoBoundarySpec();
		serialize(roBS, data);

		for (Parameter par : ros.getSpecList()) {
			switch (par.getParameterHeader().getParameterType()) {
			case AI_SPEC:
				serialize((AISpec) par, data);
				break;
			case RF_SURVEY_SPEC:
				serialize((RFSurveySpec) par, data);
				break;
			case LOOP_SPEC:
				serialize((LoopSpec) par, data);
				break;
			case CUSTOM:
				serialize((Custom) par, data);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ par.getParameterHeader().getParameterType());
			}
		}

		if (ros.getRoReportSpec() != null) {
			serialize(ros.getRoReportSpec(), data);
		}
	}

	public int getLength(ROSpec ros) throws InvalidParameterTypeException {
		int length = TLV_PARAMETER_HEADER_LENGTH + 6;
		ROBoundarySpec roBS = ros.getRoBoundarySpec();
		length += getLength(roBS);

		for (Parameter par : ros.getSpecList()) {
			switch (par.getParameterHeader().getParameterType()) {
			case AI_SPEC:
				length += getLength((AISpec) par);
				break;
			case RF_SURVEY_SPEC:
				length += getLength((RFSurveySpec) par);
				break;
			case LOOP_SPEC:
				length += getLength((LoopSpec) par);
				break;
			case CUSTOM:
				length += getLength((Custom) par);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ par.getParameterHeader().getParameterType());
			}
		}

		if (ros.getRoReportSpec() != null) {
			length += getLength(ros.getRoReportSpec());
		}
		return length;
	}

	/**
	 * Section 17.2.4.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public ROBoundarySpec deserializeROBoundarySpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		ROSpecStartTrigger roSStartT = null;
		ROSpecStopTrigger roSStopT = null;
		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RO_SPEC_START_TRIGGER:
				roSStartT = deserializeROSpecStartTrigger(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case RO_SPEC_STOP_TRIGGER:
				roSStopT = deserializeROSpecStopTrigger(
						(TLVParameterHeader) parameterHeader, data);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return new ROBoundarySpec(header, roSStartT, roSStopT);
	}

	public void serialize(ROBoundarySpec roBS, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = roBS.getParameterHeader();
		header.setParameterLength(getLength(roBS));
		// serialize parameter
		serialize(header, data);

		ROSpecStartTrigger roSStartT = roBS.getRoSStartTrigger();
		serialize(roSStartT, data);
		ROSpecStopTrigger roSStopT = roBS.getRoSStopTrigger();
		serialize(roSStopT, data);
	}

	public int getLength(ROBoundarySpec roBS) {
		int length = TLV_PARAMETER_HEADER_LENGTH;
		ROSpecStartTrigger roSStartT = roBS.getRoSStartTrigger();
		length += getLength(roSStartT);
		ROSpecStopTrigger roSStopT = roBS.getRoSStopTrigger();
		length += getLength(roSStopT);
		return length;
	}

	/**
	 * Section 17.2.4.1.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public ROSpecStartTrigger deserializeROSpecStartTrigger(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		ROSpecStartTrigger roSpecST = null;
		ROSpecStartTriggerType type = ROSpecStartTriggerType
				.get(DataTypeConverter.ubyte(data.get()));
		roSpecST = new ROSpecStartTrigger(header, type);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case PERIODIC_TRIGGER_VALUE:
				PeriodicTriggerValue ptv = deserializePeriodicTriggerValue(
						(TLVParameterHeader) parameterHeader, data);
				roSpecST.setPeriodicTV(ptv);
				break;
			case GPI_TRIGGER_VALUE:
				GPITriggerValue gtv = deserializeGPITriggerValue(
						(TLVParameterHeader) parameterHeader, data);
				roSpecST.setGpiTV(gtv);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return roSpecST;
	}

	public void serialize(ROSpecStartTrigger roSpecST, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = roSpecST.getParameterHeader();
		header.setParameterLength(getLength(roSpecST));
		// serialize parameter
		serialize(header, data);
		data.put((byte) roSpecST.getRoSpecStartTriggerType().getValue());

		PeriodicTriggerValue ptv = roSpecST.getPeriodicTV();
		if (ptv != null) {
			serialize(ptv, data);
		}

		GPITriggerValue gtv = roSpecST.getGpiTV();
		if (gtv != null) {
			serialize(gtv, data);
		}
	}

	public int getLength(ROSpecStartTrigger roSpecST) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		PeriodicTriggerValue ptv = roSpecST.getPeriodicTV();
		if (ptv != null) {
			length += getLength(ptv);
		}

		GPITriggerValue gtv = roSpecST.getGpiTV();
		if (gtv != null) {
			length += getLength(gtv);
		}
		return length;
	}

	/**
	 * Section 17.2.4.1.1.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public PeriodicTriggerValue deserializePeriodicTriggerValue(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		PeriodicTriggerValue ptv = null;
		long offSet = DataTypeConverter.uint(data.getInt());
		long period = DataTypeConverter.uint(data.getInt());
		ptv = new PeriodicTriggerValue(header, offSet, period);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case UTC_TIMESTAMP:
				UTCTimestamp utc = deserializeUTCTimestamp(
						(TLVParameterHeader) parameterHeader, data);
				ptv.setUtc(utc);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return ptv;
	}

	public void serialize(PeriodicTriggerValue ptv, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = ptv.getParameterHeader();
		header.setParameterLength(getLength(ptv));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) ptv.getOffSet());
		data.putInt((int) ptv.getPeriod());
		UTCTimestamp utc = ptv.getUtc();
		if (utc != null) {
			serialize(utc, data);
		}
	}

	public int getLength(PeriodicTriggerValue ptv) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 8;
		UTCTimestamp utc = ptv.getUtc();
		if (utc != null) {
			length += getLength(utc);
		}
		return length;
	}

	/**
	 * Section 17.2.4.1.1.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public GPITriggerValue deserializeGPITriggerValue(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int gpiPortNum = DataTypeConverter.ushort(data.getShort());
		byte b = data.get();
		boolean e = (b & 0x80) > 0;
		long timeOut = DataTypeConverter.uint(data.getInt());
		return new GPITriggerValue(header, gpiPortNum, e, timeOut);
	}

	public void serialize(GPITriggerValue gpiTriggerValue, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = gpiTriggerValue.getParameterHeader();
		header.setParameterLength(getLength(gpiTriggerValue));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) gpiTriggerValue.getGpiPortNum());
		byte b = (byte) 0x00;
		if (gpiTriggerValue.isGpiEvent()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
		data.putInt((int) gpiTriggerValue.getTimeOut());
	}

	public int getLength(GPITriggerValue gpiTriggerValue) {
		return TLV_PARAMETER_HEADER_LENGTH + 7;
	}

	/**
	 * Section 17.2.4.1.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public ROSpecStopTrigger deserializeROSpecStopTrigger(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		ROSpecStopTrigger roSST = null;
		ROSpecStopTriggerType roSSTType = ROSpecStopTriggerType
				.get(DataTypeConverter.ubyte(data.get()));
		long duration = DataTypeConverter.uint(data.getInt());
		roSST = new ROSpecStopTrigger(header, roSSTType, duration);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case GPI_TRIGGER_VALUE:
				GPITriggerValue gpiTV = deserializeGPITriggerValue(
						(TLVParameterHeader) parameterHeader, data);
				roSST.setGpiTriggerValue(gpiTV);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return roSST;
	}

	public void serialize(ROSpecStopTrigger roSST, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = roSST.getParameterHeader();
		header.setParameterLength(getLength(roSST));
		// serialize parameter
		serialize(header, data);
		data.put((byte) roSST.getRoSpecStopTriggerType().getValue());
		data.putInt((int) roSST.getDurationTriggerValue());
		GPITriggerValue gpiTV = roSST.getGpiTriggerValue();
		if (gpiTV != null) {
			serialize(gpiTV, data);
		}
	}

	public int getLength(ROSpecStopTrigger roSST) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		GPITriggerValue gpiTV = roSST.getGpiTriggerValue();
		if (gpiTV != null) {
			length += getLength(gpiTV);
		}
		return length;
	}

	/**
	 * Section 17.2.4.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 */
	public AISpec deserializeAISpec(TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();

		AISpec aiSpec = null;
		AISpecStopTrigger aiSST = null;
		List<InventoryParameterSpec> inventPSList = new ArrayList<>();
		List<Custom> cusList = new ArrayList<>();

		int antCount = DataTypeConverter.ushort(data.getShort());
		List<Integer> antIDList = new ArrayList<Integer>();
		for (int i = 0; i < antCount; i++) {
			antIDList.add(DataTypeConverter.ushort(data.getShort()));
		}

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case AI_SPEC_STOP_TRIGGER:
			aiSST = deserializeAISpecStopTrigger(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case INVENTORY_PARAMETER_SPEC:
				InventoryParameterSpec iPS = deserializeInventoryParameterSpec(
						(TLVParameterHeader) parameterHeader, data);
				inventPSList.add(iPS);
				break;
			case CUSTOM:
				Custom custom = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(custom);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		aiSpec = new AISpec(header, antIDList, aiSST, inventPSList);
		aiSpec.setAntennaCount(antCount);
		aiSpec.setCustomList(cusList);

		return aiSpec;
	}

	public void serialize(AISpec aiSpec, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = aiSpec.getParameterHeader();
		header.setParameterLength(getLength(aiSpec));
		// serialize parameter
		serialize(header, data);

		int antCount = aiSpec.getAntennaIdList().size();
		aiSpec.setAntennaCount(antCount);
		data.putShort((short) antCount);
		if (aiSpec.getAntennaIdList() != null
				&& !aiSpec.getAntennaIdList().isEmpty()) {
			for (Integer id : aiSpec.getAntennaIdList()) {
				data.putShort((short) id.intValue());
			}
		}

		AISpecStopTrigger aiSST = aiSpec.getAiSpecStopTrigger();
		serialize(aiSST, data);

		for (InventoryParameterSpec iPS : aiSpec.getInventoryParameterList()) {
			serialize(iPS, data);
		}

		if (aiSpec.getCustomList() != null && !aiSpec.getCustomList().isEmpty()) {
			for (Custom cus : aiSpec.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(AISpec aiSpec) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		if (aiSpec.getAntennaIdList() != null
				&& !aiSpec.getAntennaIdList().isEmpty()) {
			length += aiSpec.getAntennaIdList().size() * 2;
		}
		length += getLength(aiSpec.getAiSpecStopTrigger());
		for (InventoryParameterSpec iPS : aiSpec.getInventoryParameterList()) {
			length += getLength(iPS);
		}
		if (aiSpec.getCustomList() != null && !aiSpec.getCustomList().isEmpty()) {
			for (Custom cus : aiSpec.getCustomList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.4.2.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public AISpecStopTrigger deserializeAISpecStopTrigger(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		AISpecStopTrigger aiSST = null;
		short aiSSTType = DataTypeConverter.ubyte(data.get());
		long duration = DataTypeConverter.uint(data.getInt());
		aiSST = new AISpecStopTrigger(header,
				AISpecStopTriggerType.get(aiSSTType), duration);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case GPI_TRIGGER_VALUE:
				GPITriggerValue gpiTV = deserializeGPITriggerValue(
						(TLVParameterHeader) parameterHeader, data);
				aiSST.setGpiTV(gpiTV);
				break;
			case TAG_OBSERVATION_TRIGGER:
				TagObservationTrigger tagOT = deserializeTagObservationTrigger(
						(TLVParameterHeader) parameterHeader, data);
				aiSST.setTagOT(tagOT);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return aiSST;
	}

	public void serialize(AISpecStopTrigger aiSST, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = aiSST.getParameterHeader();
		header.setParameterLength(getLength(aiSST));
		// serialize parameter
		serialize(header, data);
		data.put((byte) aiSST.getAiSpecStopTriggerType().getValue());
		data.putInt((int) aiSST.getDurationTrigger());
		GPITriggerValue gpiTV = aiSST.getGpiTV();
		if (gpiTV != null) {
			serialize(gpiTV, data);
		}
		TagObservationTrigger tagOT = aiSST.getTagOT();
		if (tagOT != null) {
			serialize(tagOT, data);
		}
	}

	public int getLength(AISpecStopTrigger aiSST) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		GPITriggerValue gpiTV = aiSST.getGpiTV();
		if (gpiTV != null) {
			length += getLength(gpiTV);
		}
		TagObservationTrigger tagOT = aiSST.getTagOT();
		if (tagOT != null) {
			length += getLength(tagOT);
		}
		return length;
	}

	/**
	 * Section 17.2.4.2.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 * @throws DeserializationException
	 */
	public TagObservationTrigger deserializeTagObservationTrigger(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		TagObservationTriggerType triggerType = TagObservationTriggerType
				.get(DataTypeConverter.ubyte(data.get()));
		data.get();
		int nOT = DataTypeConverter.ushort(data.getShort());
		int nOA = DataTypeConverter.ushort(data.getShort());
		int t = DataTypeConverter.ushort(data.getShort());
		long timeOut = DataTypeConverter.uint(data.getInt());
		return new TagObservationTrigger(header, triggerType, nOT, nOA, t,
				timeOut);
	}

	public void serialize(TagObservationTrigger tagObsTri, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = tagObsTri.getParameterHeader();
		header.setParameterLength(getLength(tagObsTri));
		// serialize parameter
		serialize(header, data);
		data.put((byte) tagObsTri.getTriggerType().getValue());
		byte reserved = 0x00;
		data.put(reserved);
		data.putShort((short) tagObsTri.getNumberOfTags());
		data.putShort((short) tagObsTri.getNumberOfAttempts());
		data.putShort((short) tagObsTri.getT());
		data.putInt((int) tagObsTri.getTimeOut());
	}

	public int getLength(TagObservationTrigger tagObsTri) {
		return TLV_PARAMETER_HEADER_LENGTH + 12;
	}

	/**
	 * Section 17.2.4.2.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 */
	public InventoryParameterSpec deserializeInventoryParameterSpec(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		InventoryParameterSpec inventPS = null;
		List<AntennaConfiguration> antConfList = new ArrayList<>();
		List<Custom> cusList = new ArrayList<>();

		int inventPSID = DataTypeConverter.ushort(data.getShort());
		ProtocolId pID = ProtocolId.get(DataTypeConverter.ubyte(data.get()));
		inventPS = new InventoryParameterSpec(header, inventPSID, pID);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case ANTENNA_CONFIGURATION:
				AntennaConfiguration ac = deserializeAntennaConfiguration(
						(TLVParameterHeader) parameterHeader, data);
				antConfList.add(ac);
				break;
			case CUSTOM:
				Custom custom = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(custom);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		inventPS.setAntennaConfigList(antConfList);
		inventPS.setCusList(cusList);
		return inventPS;
	}

	public void serialize(InventoryParameterSpec inventPS, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = inventPS.getParameterHeader();
		header.setParameterLength(getLength(inventPS));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) inventPS.getSpecID());
		data.put((byte) inventPS.getProtocolID().getValue());
		if (inventPS.getAntennaConfigList() != null
				&& !inventPS.getAntennaConfigList().isEmpty()) {
			for (AntennaConfiguration ac : inventPS.getAntennaConfigList()) {
				serialize(ac, data);
			}
		}
		if (inventPS.getCusList() != null && !inventPS.getCusList().isEmpty()) {
			for (Custom cus : inventPS.getCusList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(InventoryParameterSpec inventPS) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 3;
		if (inventPS.getAntennaConfigList() != null
				&& !inventPS.getAntennaConfigList().isEmpty()) {
			for (AntennaConfiguration ac : inventPS.getAntennaConfigList()) {
				length += getLength(ac);
			}
		}
		if (inventPS.getCusList() != null && !inventPS.getCusList().isEmpty()) {
			for (Custom cus : inventPS.getCusList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.4.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance The instance
	 */
	public RFSurveySpec deserializeRFSurveySpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		RFSurveySpec rfSurvey = null;
		RFSurveySpecStopTrigger rfSSST = null;
		List<Custom> cusList = new ArrayList<>();

		int antennaID = DataTypeConverter.ushort(data.getShort());
		long sFreq = DataTypeConverter.uint(data.getInt());
		long eFreq = DataTypeConverter.uint(data.getInt());

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case RF_SURVEY_SPEC_STOP_TRIGGER:
			rfSSST = deserializeRFSurveySpecStopTrigger(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		rfSurvey = new RFSurveySpec(header, antennaID, sFreq, eFreq, rfSSST);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case CUSTOM:
				Custom custom = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(custom);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		rfSurvey.setCusList(cusList);
		return rfSurvey;
	}

	public void serialize(RFSurveySpec rfSurveySpec, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfSurveySpec.getParameterHeader();
		header.setParameterLength(getLength(rfSurveySpec));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) rfSurveySpec.getAntennaID());
		data.putInt((int) rfSurveySpec.getStartFreq());
		data.putInt((int) rfSurveySpec.getEndFreq());

		RFSurveySpecStopTrigger rfSSST = rfSurveySpec.getRfSSStopTrigger();
		serialize(rfSSST, data);
		if (rfSurveySpec.getCusList() != null
				&& !rfSurveySpec.getCusList().isEmpty()) {
			for (Custom cus : rfSurveySpec.getCusList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(RFSurveySpec rfSurveySpec) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 10;
		length += getLength(rfSurveySpec.getRfSSStopTrigger());
		if (rfSurveySpec.getCusList() != null
				&& !rfSurveySpec.getCusList().isEmpty()) {
			for (Custom cus : rfSurveySpec.getCusList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.4.3.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public RFSurveySpecStopTrigger deserializeRFSurveySpecStopTrigger(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		RFSurveySpecStopTriggerType stopTriggerType = RFSurveySpecStopTriggerType
				.get(DataTypeConverter.ubyte(data.get()));
		long duration = DataTypeConverter.uint(data.getInt());
		long n = DataTypeConverter.uint(data.getInt());
		return new RFSurveySpecStopTrigger(header, stopTriggerType, duration, n);
	}

	public void serialize(RFSurveySpecStopTrigger rfSSST, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfSSST.getParameterHeader();
		header.setParameterLength(getLength(rfSSST));
		// serialize parameter
		serialize(header, data);
		data.put((byte) rfSSST.getTriggerType().getValue());
		data.putInt((int) rfSSST.getDuration());
		data.putInt((int) rfSSST.getN());
	}

	public int getLength(RFSurveySpecStopTrigger rfSSST) {
		return TLV_PARAMETER_HEADER_LENGTH + 9;
	}

	/**
	 * Section 17.2.4.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public LoopSpec deserializeLoopSpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		long count = DataTypeConverter.uint(data.getInt());
		return new LoopSpec(header, count);
	}

	public void serialize(LoopSpec loopSpec, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = loopSpec.getParameterHeader();
		header.setParameterLength(getLength(loopSpec));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) loopSpec.getLoopCount());
	}

	public int getLength(LoopSpec loopSpec) {
		return TLV_PARAMETER_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.2.5.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AccessSpec deserializeAccessSpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();

		long accessSpecID = DataTypeConverter.uint(data.getInt());
		int antennaID = DataTypeConverter.ushort(data.getShort());
		ProtocolId pID = ProtocolId.get(DataTypeConverter.ubyte(data.get()));
		byte b = data.get();
		boolean currentState = (b & 0x80) > 0;
		long roSpecID = DataTypeConverter.uint(data.getInt());

		AccessSpec accessSpec = null;
		AccessSpecStopTrigger accessSpecStopTrigger = null;
		AccessCommand accessCommand = null;
		AccessReportSpec accessReportSpec = null;
		List<Custom> cusList = new ArrayList<>();

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case ACCESS_SPEC_STOP_TRIGGER:
			accessSpecStopTrigger = deserializeAccessSpecStopTrigger(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException(
					"Parameter expected of type "
							+ ParameterType.ACCESS_SPEC_STOP_TRIGGER);
		}

		parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case ACCESS_COMMAND:
			accessCommand = deserializeAccessCommand(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException(
					"Parameter expected of type "
							+ ParameterType.ACCESS_COMMAND);
		}

		accessSpec = new AccessSpec(header, accessSpecID, antennaID, pID,
				currentState, roSpecID, accessSpecStopTrigger, accessCommand);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case ACCESS_REPORT_SPEC:
				accessReportSpec = deserializeAccessReportSpec(
						(TLVParameterHeader) parameterHeader, data);
				break;
			case CUSTOM:
				Custom custom = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(custom);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		accessSpec.setAccessReportSpec(accessReportSpec);
		accessSpec.setCustomList(cusList);
		return accessSpec;
	}

	public void serialize(AccessSpec accessSpec, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set parameter length
		TLVParameterHeader header = accessSpec.getParameterHeader();
		header.setParameterLength(getLength(accessSpec));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) accessSpec.getAccessSpecId());
		data.putShort((short) accessSpec.getAntennaId());
		data.put((byte) accessSpec.getProtocolId().getValue());
		byte b = (byte) 0x00;
		if (accessSpec.isCurrentState()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
		data.putInt((int) accessSpec.getRoSpecId());

		AccessSpecStopTrigger accessSpecStopTrigger = accessSpec
				.getAccessSpecStopTrigger();
		serialize(accessSpecStopTrigger, data);
		AccessCommand accessCommand = accessSpec.getAccessCommand();
		serialize(accessCommand, data);

		AccessReportSpec accessReportSpec = accessSpec.getAccessReportSpec();
		if (accessReportSpec != null) {
			serialize(accessReportSpec, data);
		}

		if (accessSpec.getCustomList() != null
				&& !accessSpec.getCustomList().isEmpty()) {
			for (Custom cus : accessSpec.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(AccessSpec accessSpec)
			throws InvalidParameterTypeException {
		int length = TLV_PARAMETER_HEADER_LENGTH + 12;

		AccessSpecStopTrigger accessSpecStopTrigger = accessSpec
				.getAccessSpecStopTrigger();
		length += getLength(accessSpecStopTrigger);
		AccessCommand accessCommand = accessSpec.getAccessCommand();
		length += getLength(accessCommand);

		AccessReportSpec accessReportSpec = accessSpec.getAccessReportSpec();
		if (accessReportSpec != null) {
			length += getLength(accessReportSpec);
		}

		if (accessSpec.getCustomList() != null
				&& !accessSpec.getCustomList().isEmpty()) {
			for (Custom cus : accessSpec.getCustomList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.5.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AccessSpecStopTrigger deserializeAccessSpecStopTrigger(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {

		short accessSpecStopTrigger = DataTypeConverter.ubyte(data.get());
		int ocv = DataTypeConverter.ushort(data.getShort());
		return new AccessSpecStopTrigger(header,
				AccessSpecStopTriggerType.get(accessSpecStopTrigger), ocv);
	}

	public void serialize(AccessSpecStopTrigger accessSpecStopTrigger,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = accessSpecStopTrigger.getParameterHeader();
		header.setParameterLength(getLength(accessSpecStopTrigger));
		// serialize parameter
		serialize(header, data);
		data.put((byte) accessSpecStopTrigger.getAccessSpecStopTriggerType()
				.getValue());
		data.putShort((short) accessSpecStopTrigger.getOperationCountValue());
	}

	public int getLength(AccessSpecStopTrigger accessSpecStopTrigger) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.2.5.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AccessCommand deserializeAccessCommand(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		AccessCommand accessCommand = null;
		C1G2TagSpec c1g2TagSpec = null;
		List<Parameter> opSpecList = new ArrayList<>();
		List<Custom> cusList = new ArrayList<>();

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case C1G2_TAG_SPEC:
			c1g2TagSpec = deserializeC1G2TagSpec(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException(
					"Parameter expected of type " + ParameterType.C1G2_TAG_SPEC);
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_READ:
				C1G2Read c1g2Read = deserializeC1G2Read(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Read);
				break;
			case C1G2_WRITE:
				C1G2Write c1g2Write = deserializeC1G2Write(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Write);
				break;
			case C1G2_KILL:
				C1G2Kill c1g2Kill = deserializeC1G2Kill(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Kill);
				break;
			case C1G2_RECOMMISSION:
				C1G2Recommission c1g2Recommission = deserializeC1G2Recommission(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Recommission);
				break;
			case C1G2_LOCK:
				C1G2Lock c1g2Lock = deserializeC1G2Lock(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Lock);
				break;
			case C1G2_LOCK_PAYLOAD:
				C1G2LockPayload c1g2LockPayload = deserializeC1G2LockPayload(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2LockPayload);
				break;
			case C1G2_BLOCK_ERASE:
				C1G2BlockErase c1g2BlockErase = deserializeC1G2BlockErase(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2BlockErase);
				break;
			case C1G2_BLOCK_WRITE:
				C1G2BlockWrite c1g2BlockWrite = deserializeC1G2BlockWrite(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2BlockWrite);
				break;
			case C1G2_BLOCK_PERMALOCK:
				C1G2BlockPermalock c1g2BlockPermalock = deserializeC1G2BlockPermalock(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2BlockPermalock);
				break;
			case C1G2_GET_BLOCK_PERMALOCK_STATUS:
				C1G2GetBlockPermalockStatus c1g2GetBlockPermalockStatus = deserializeC1G2GetBlockPermalockStatus(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2GetBlockPermalockStatus);
				break;
			case CLIENT_REQUEST_OP_SPEC:
				ClientRequestOpSpec clientRequestOpSpec = deserializeClientRequestOpSpec(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(clientRequestOpSpec);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		accessCommand = new AccessCommand(header, c1g2TagSpec, opSpecList);
		accessCommand.setCustomList(cusList);
		return accessCommand;
	}

	public void serialize(AccessCommand accessCommand, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set parameter length
		TLVParameterHeader header = accessCommand.getParameterHeader();
		header.setParameterLength(getLength(accessCommand));
		// serialize parameter
		serialize(header, data);
		C1G2TagSpec c1g2TagSpec = accessCommand.getC1g2TagSpec();
		serialize(c1g2TagSpec, data);

		for (Parameter par : accessCommand.getOpSpecList()) {
			switch (par.getParameterHeader().getParameterType()) {
			case C1G2_READ:
				serialize((C1G2Read) par, data);
				break;
			case C1G2_WRITE:
				serialize((C1G2Write) par, data);
				break;
			case C1G2_KILL:
				serialize((C1G2Kill) par, data);
				break;
			case C1G2_RECOMMISSION:
				serialize((C1G2Recommission) par, data);
				break;
			case C1G2_LOCK:
				serialize((C1G2Lock) par, data);
				break;
			case C1G2_LOCK_PAYLOAD:
				serialize((C1G2LockPayload) par, data);
				break;
			case C1G2_BLOCK_ERASE:
				serialize((C1G2BlockErase) par, data);
				break;
			case C1G2_BLOCK_WRITE:
				serialize((C1G2BlockWrite) par, data);
				break;
			case C1G2_BLOCK_PERMALOCK:
				serialize((C1G2BlockPermalock) par, data);
				break;
			case C1G2_GET_BLOCK_PERMALOCK_STATUS:
				serialize((C1G2GetBlockPermalockStatus) par, data);
				break;
			case CLIENT_REQUEST_OP_SPEC:
				serialize((ClientRequestOpSpec) par, data);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ par.getParameterHeader().getParameterType());
			}
		}

		if (accessCommand.getCustomList() != null
				&& !accessCommand.getCustomList().isEmpty()) {
			for (Custom cus : accessCommand.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(AccessCommand accessCommand)
			throws InvalidParameterTypeException {
		int length = TLV_PARAMETER_HEADER_LENGTH;

		C1G2TagSpec c1g2TagSpec = accessCommand.getC1g2TagSpec();
		length += getLength(c1g2TagSpec);

		for (Parameter par : accessCommand.getOpSpecList()) {
			switch (par.getParameterHeader().getParameterType()) {
			case C1G2_READ:
				length += getLength((C1G2Read) par);
				break;
			case C1G2_WRITE:
				length += getLength((C1G2Write) par);
				break;
			case C1G2_KILL:
				length += getLength((C1G2Kill) par);
				break;
			case C1G2_RECOMMISSION:
				length += getLength((C1G2Recommission) par);
				break;
			case C1G2_LOCK:
				length += getLength((C1G2Lock) par);
				break;
			case C1G2_LOCK_PAYLOAD:
				length += getLength((C1G2LockPayload) par);
				break;
			case C1G2_BLOCK_ERASE:
				length += getLength((C1G2BlockErase) par);
				break;
			case C1G2_BLOCK_WRITE:
				length += getLength((C1G2BlockWrite) par);
				break;
			case C1G2_BLOCK_PERMALOCK:
				length += getLength((C1G2BlockPermalock) par);
				break;
			case C1G2_GET_BLOCK_PERMALOCK_STATUS:
				length += getLength((C1G2GetBlockPermalockStatus) par);
				break;
			case CLIENT_REQUEST_OP_SPEC:
				length += getLength((ClientRequestOpSpec) par);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ par.getParameterHeader().getParameterType());
			}
		}

		if (accessCommand.getCustomList() != null
				&& !accessCommand.getCustomList().isEmpty()) {
			for (Custom cus : accessCommand.getCustomList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.5.1.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ClientRequestOpSpec deserializeClientRequestOpSpec(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		return new ClientRequestOpSpec(header, opSpecID);
	}

	public void serialize(ClientRequestOpSpec clientRequestOpSpec,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = clientRequestOpSpec.getParameterHeader();
		header.setParameterLength(getLength(clientRequestOpSpec));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) clientRequestOpSpec.getOpSpecID());
	}

	public int getLength(ClientRequestOpSpec clientRequestOpSpec) {
		return TLV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.5.1.3.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ClientRequestResponse deserializeClientRequestResponse(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		long accessSpecID = DataTypeConverter.uint(data.getInt());
		EPCData epcData = null;
		List<Parameter> opSpecList = new ArrayList<>();
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case EPC_DATA:
			epcData = deserializeEPCData((TLVParameterHeader) parameterHeader,
					data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		ClientRequestResponse cRR = new ClientRequestResponse(header,
				accessSpecID, epcData);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_READ:
				C1G2Read c1g2Read = deserializeC1G2Read(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Read);
				break;
			case C1G2_WRITE:
				C1G2Write c1g2Write = deserializeC1G2Write(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Write);
				break;
			case C1G2_KILL:
				C1G2Kill c1g2Kill = deserializeC1G2Kill(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Kill);
				break;
			case C1G2_RECOMMISSION:
				C1G2Recommission c1g2Recommission = deserializeC1G2Recommission(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Recommission);
				break;
			case C1G2_LOCK:
				C1G2Lock c1g2Lock = deserializeC1G2Lock(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2Lock);
				break;
			case C1G2_LOCK_PAYLOAD:
				C1G2LockPayload c1g2LockPayload = deserializeC1G2LockPayload(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2LockPayload);
				break;
			case C1G2_BLOCK_ERASE:
				C1G2BlockErase c1g2BlockErase = deserializeC1G2BlockErase(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2BlockErase);
				break;
			case C1G2_BLOCK_WRITE:
				C1G2BlockWrite c1g2BlockWrite = deserializeC1G2BlockWrite(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2BlockWrite);
				break;
			case C1G2_BLOCK_PERMALOCK:
				C1G2BlockPermalock c1g2BlockPermalock = deserializeC1G2BlockPermalock(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2BlockPermalock);
				break;
			case C1G2_GET_BLOCK_PERMALOCK_STATUS:
				C1G2GetBlockPermalockStatus c1g2GetBlockPermalockStatus = deserializeC1G2GetBlockPermalockStatus(
						(TLVParameterHeader) parameterHeader, data);
				opSpecList.add(c1g2GetBlockPermalockStatus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		cRR.setOpSpecList(opSpecList);
		return cRR;
	}

	public void serialize(ClientRequestResponse clientRequestResponse,
			ByteBuffer data) throws InvalidParameterTypeException {
		// set parameter length
		TLVParameterHeader header = clientRequestResponse.getParameterHeader();
		header.setParameterLength(getLength(clientRequestResponse));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) clientRequestResponse.getAccessSpecID());
		EPCData epcData = clientRequestResponse.getEpcData();
		serialize(epcData, data);

		if (clientRequestResponse.getOpSpecList() != null
				&& !clientRequestResponse.getOpSpecList().isEmpty()) {
			for (Parameter par : clientRequestResponse.getOpSpecList()) {
				switch (par.getParameterHeader().getParameterType()) {
				case C1G2_READ:
					serialize((C1G2Read) par, data);
					break;
				case C1G2_WRITE:
					serialize((C1G2Write) par, data);
					break;
				case C1G2_KILL:
					serialize((C1G2Kill) par, data);
					break;
				case C1G2_RECOMMISSION:
					serialize((C1G2Recommission) par, data);
					break;
				case C1G2_LOCK:
					serialize((C1G2Lock) par, data);
					break;
				case C1G2_LOCK_PAYLOAD:
					serialize((C1G2LockPayload) par, data);
					break;
				case C1G2_BLOCK_ERASE:
					serialize((C1G2BlockErase) par, data);
					break;
				case C1G2_BLOCK_WRITE:
					serialize((C1G2BlockWrite) par, data);
					break;
				case C1G2_BLOCK_PERMALOCK:
					serialize((C1G2BlockPermalock) par, data);
					break;
				case C1G2_GET_BLOCK_PERMALOCK_STATUS:
					serialize((C1G2GetBlockPermalockStatus) par, data);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ par.getParameterHeader()
											.getParameterType());
				}
			}
		}
	}

	public int getLength(ClientRequestResponse clientRequestResponse)
			throws InvalidParameterTypeException {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;

		EPCData epcData = clientRequestResponse.getEpcData();
		length += getLength(epcData);

		if (clientRequestResponse.getOpSpecList() != null
				&& !clientRequestResponse.getOpSpecList().isEmpty()) {
			for (Parameter par : clientRequestResponse.getOpSpecList()) {
				switch (par.getParameterHeader().getParameterType()) {
				case C1G2_READ:
					length += getLength((C1G2Read) par);
					break;
				case C1G2_WRITE:
					length += getLength((C1G2Write) par);
					break;
				case C1G2_KILL:
					length += getLength((C1G2Kill) par);
					break;
				case C1G2_RECOMMISSION:
					length += getLength((C1G2Recommission) par);
					break;
				case C1G2_LOCK:
					length += getLength((C1G2Lock) par);
					break;
				case C1G2_LOCK_PAYLOAD:
					length += getLength((C1G2LockPayload) par);
					break;
				case C1G2_BLOCK_ERASE:
					length += getLength((C1G2BlockErase) par);
					break;
				case C1G2_BLOCK_WRITE:
					length += getLength((C1G2BlockWrite) par);
					break;
				case C1G2_BLOCK_PERMALOCK:
					length += getLength((C1G2BlockPermalock) par);
					break;
				case C1G2_GET_BLOCK_PERMALOCK_STATUS:
					length += getLength((C1G2GetBlockPermalockStatus) par);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ par.getParameterHeader()
											.getParameterType());
				}
			}
		}
		return length;
	}

	/**
	 * Section 17.2.6.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public LLRPConfigurationStateValue deserializeLLRPConfigurationStateValue(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		long llrpCSV = DataTypeConverter.uint(data.getInt());
		return new LLRPConfigurationStateValue(header, llrpCSV);
	}

	public void serialize(LLRPConfigurationStateValue llrpCSV, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = llrpCSV.getParameterHeader();
		header.setParameterLength(getLength(llrpCSV));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) llrpCSV.getLlrpConfigStateValue());
	}

	public int getLength(LLRPConfigurationStateValue llrpCSV) {
		int length = 8;
		return length;
	}

	/**
	 * Section 17.2.6.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public Identification deserializeIdentification(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		IdentificationIDType idType = IdentificationIDType
				.get(DataTypeConverter.ubyte(data.get()));
		int byteCount = DataTypeConverter.ushort(data.getShort());
		byte[] dst = new byte[byteCount];
		data.get(dst, 0, byteCount);
		Identification ident = new Identification(header, idType, dst);
		ident.setByteCount(dst.length);
		return ident;
	}

	public void serialize(Identification ident, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = ident.getParameterHeader();
		header.setParameterLength(getLength(ident));
		// serialize parameter
		serialize(header, data);
		data.put((byte) ident.getIdType().getValue());

		byte[] rederID = ident.getReaderID();
		int bC = rederID.length;
		ident.setByteCount(bC);
		data.putShort((short) bC);
		data.put(rederID);
	}

	public int getLength(Identification ident) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 3
				+ ident.getReaderID().length;
		return length;
	}

	/**
	 * Section 17.2.6.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GPOWriteData deserializeGPOWriteData(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int gpoPortNum = DataTypeConverter.ushort(data.getShort());
		byte b = data.get();
		boolean w = (b & 0x80) > 0;
		return new GPOWriteData(header, gpoPortNum, w);
	}

	public void serialize(GPOWriteData gpoWriteData, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = gpoWriteData.getParameterHeader();
		header.setParameterLength(getLength(gpoWriteData));
		// serialize parameter
		serialize(header, data);
		int gpoPortNum = gpoWriteData.getGpoPortNum();
		data.putShort((short) gpoPortNum);
		byte b = (byte) 0x00;
		if (gpoWriteData.getGpoState()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
	}

	public int getLength(GPOWriteData gpoWriteData) {
		int length = 7;
		return length;
	}

	/**
	 * Section 17.2.6.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public KeepaliveSpec deserializeKeepaliveSpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		long interval = 0;
		KeepaliveSpecTriggerType timeTrType = KeepaliveSpecTriggerType
				.get(DataTypeConverter.ubyte(data.get()));
		interval = DataTypeConverter.uint(data.getInt());

		return new KeepaliveSpec(header, timeTrType, interval);

	}

	public void serialize(KeepaliveSpec keepAliveS, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = keepAliveS.getParameterHeader();
		header.setParameterLength(getLength(keepAliveS));
		// serialize parameter
		serialize(header, data);
		data.put((byte) keepAliveS.getTriggerType().getValue());
		data.putInt((int) keepAliveS.getTimeInterval());

	}

	public int getLength(KeepaliveSpec keepAliveS) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		return length;
	}

	/**
	 * Section 17.2.6.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AntennaProperties deserializeAntennaProperties(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		byte b = data.get();
		boolean connected = (b & 0x80) > 0;
		int antennaID = DataTypeConverter.ushort(data.getShort());
		short antennaGain = data.getShort();
		return new AntennaProperties(header, connected, antennaID, antennaGain);
	}

	public void serialize(AntennaProperties antennaProp, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = antennaProp.getParameterHeader();
		header.setParameterLength(getLength(antennaProp));
		// serialize parameter
		serialize(header, data);
		byte b = (byte) 0x00;
		if (antennaProp.isConnected()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
		data.putShort((short) antennaProp.getAntennaID());
		data.putShort(antennaProp.getAntennaGain());
	}

	public int getLength(AntennaProperties antennaProp) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		return length;
	}

	/**
	 * Section 17.2.6.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AntennaConfiguration deserializeAntennaConfiguration(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		int antennaID = DataTypeConverter.ushort(data.getShort());
		AntennaConfiguration ac = new AntennaConfiguration(header, antennaID);
		List<C1G2InventoryCommand> c1g2InventoryCommandList = new ArrayList<>();
		List<Custom> cusList = new ArrayList<>();
		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RF_RECEIVER:
				RFReceiver rfReceiver = deserializeRFReceiver(
						(TLVParameterHeader) parameterHeader, data);
				ac.setRfReceiver(rfReceiver);
				break;
			case RF_TRANSMITTER:
				RFTransmitter rfTransmitter = deserializeRFTransmitter(
						(TLVParameterHeader) parameterHeader, data);
				ac.setRfTransmitter(rfTransmitter);
				break;
			case C1G2_INVENTORY_COMMAND:
				C1G2InventoryCommand ic = deserializeC1G2InventoryCommand(
						(TLVParameterHeader) parameterHeader, data);
				c1g2InventoryCommandList.add(ic);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		ac.setC1g2InventoryCommandList(c1g2InventoryCommandList);
		ac.setCustomList(cusList);
		return ac;
	}

	public void serialize(AntennaConfiguration ac, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = ac.getParameterHeader();
		header.setParameterLength(getLength(ac));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) ac.getAntennaID());

		RFReceiver rfR = ac.getRfReceiver();
		if (rfR != null) {
			serialize(rfR, data);
		}

		RFTransmitter rfT = ac.getRfTransmitter();
		if (rfT != null) {
			serialize(rfT, data);
		}

		if (ac.getC1g2InventoryCommandList() != null
				&& !ac.getC1g2InventoryCommandList().isEmpty()) {
			for (C1G2InventoryCommand c1g2IC : ac.getC1g2InventoryCommandList()) {
				serialize(c1g2IC, data);
			}
		}

		if (ac.getCustomList() != null && !ac.getCustomList().isEmpty()) {
			for (Custom cus : ac.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(AntennaConfiguration ac) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		RFReceiver rfR = ac.getRfReceiver();
		if (rfR != null) {
			length += getLength(rfR);
		}

		RFTransmitter rfT = ac.getRfTransmitter();
		if (rfT != null) {
			length += getLength(rfT);
		}

		if (ac.getC1g2InventoryCommandList() != null
				&& !ac.getC1g2InventoryCommandList().isEmpty()) {
			for (C1G2InventoryCommand c1g2IC : ac.getC1g2InventoryCommandList()) {
				length += getLength(c1g2IC);
			}
		}

		if (ac.getCustomList() != null && !ac.getCustomList().isEmpty()) {
			for (Custom cus : ac.getCustomList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.6.7
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public RFReceiver deserializeRFReceiver(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int receiverSens = DataTypeConverter.ushort(data.getShort());
		return new RFReceiver(header, receiverSens);
	}

	public void serialize(RFReceiver rfReceiver, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfReceiver.getParameterHeader();
		header.setParameterLength(getLength(rfReceiver));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) rfReceiver.getReceiverSensitivity());
	}

	public int getLength(RFReceiver rfReceiver) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		return length;
	}

	/**
	 * Section 17.2.6.8
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public RFTransmitter deserializeRFTransmitter(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int hopTableID = DataTypeConverter.ushort(data.getShort());
		int channelIndex = DataTypeConverter.ushort(data.getShort());
		int transmitPower = DataTypeConverter.ushort(data.getShort());
		return new RFTransmitter(header, hopTableID, channelIndex,
				transmitPower);
	}

	public void serialize(RFTransmitter rfT, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfT.getParameterHeader();
		header.setParameterLength(getLength(rfT));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) rfT.getHopTableID());
		data.putShort((short) rfT.getChannelIndex());
		data.putShort((short) rfT.getTransmitPower());
	}

	public int getLength(RFTransmitter rfT) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 6;
		return length;
	}

	/**
	 * Section 17.2.6.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public GPIPortCurrentState deserializeGPIPortCurrentState(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int gpiPortNum = DataTypeConverter.ushort(data.getShort());
		byte b = data.get();
		boolean gpiConfig = (b & 0x80) > 0;
		GPIPortCurrentStateGPIState state = GPIPortCurrentStateGPIState
				.get(DataTypeConverter.ubyte(data.get()));
		return new GPIPortCurrentState(header, gpiPortNum, gpiConfig, state);
	}

	public void serialize(GPIPortCurrentState gpiPCS, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = gpiPCS.getParameterHeader();
		header.setParameterLength(getLength(gpiPCS));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) gpiPCS.getGpiPortNum());
		byte b = (byte) 0x00;
		if (gpiPCS.getGpiConfig()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
		data.put((byte) gpiPCS.getState().getValue());
	}

	public int getLength(GPIPortCurrentState gpiPCS) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.2.6.10
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EventsAndReports deserializeEventsAndReports(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		byte b = data.get();
		boolean hold = (b & 0x80) > 0;
		return new EventsAndReports(header, hold);
	}

	public void serialize(EventsAndReports eAr, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = eAr.getParameterHeader();
		header.setParameterLength(getLength(eAr));
		// serialize parameter
		serialize(header, data);
		byte b = (byte) 0x00;
		if (eAr.getHold()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
	}

	public int getLength(EventsAndReports eAr) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		return length;
	}

	/**
	 * Section 17.2.7.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ROReportSpec deserializeROReportSpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		ROReportTrigger roReportTrigger = ROReportTrigger.get(DataTypeConverter
				.ubyte(data.get()));
		int n = DataTypeConverter.ushort(data.getShort());
		ROReportSpec roReportSpec = null;
		List<Custom> cusList = new ArrayList<Custom>();
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case TAG_REPORT_CONTENT_SELECTOR:
			TagReportContentSelector tagReportContentSelector = deserializeTagReportContentSelector(
					(TLVParameterHeader) parameterHeader, data);
			roReportSpec = new ROReportSpec(header, roReportTrigger, n,
					tagReportContentSelector);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		roReportSpec.setCustomList(cusList);
		return roReportSpec;
	}

	public void serialize(ROReportSpec roReportSpec, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = roReportSpec.getParameterHeader();
		header.setParameterLength(getLength(roReportSpec));
		// serialize parameter
		serialize(header, data);
		data.put((byte) roReportSpec.getRoReportTrigger().getValue());
		data.putShort((short) roReportSpec.getN());
		TagReportContentSelector tagReportContentSelector = roReportSpec
				.getTagReportContentSelector();
		serialize(tagReportContentSelector, data);

		if (roReportSpec.getCustomList() != null
				&& !roReportSpec.getCustomList().isEmpty()) {
			for (Custom cus : roReportSpec.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(ROReportSpec roReportSpec) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 3;
		length += getLength(roReportSpec.getTagReportContentSelector());
		if (roReportSpec.getCustomList() != null
				&& !roReportSpec.getCustomList().isEmpty()) {
			for (Custom cus : roReportSpec.getCustomList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.7.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public TagReportContentSelector deserializeTagReportContentSelector(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		byte b = data.get();
		boolean enableROSpecID = (b & 0x80) > 0;
		boolean enableSpecIndex = (b & 0x40) > 0;
		boolean enableInventoryParameterSpecID = (b & 0x20) > 0;
		boolean enableAntennaID = (b & 0x10) > 0;
		boolean enableChannelIndex = (b & 0x08) > 0;
		boolean enablePeakRSSI = (b & 0x04) > 0;
		boolean enableFirstSeenTimestamp = (b & 0x02) > 0;
		boolean enableLastSeenTimestamp = (b & 0x01) > 0;
		b = data.get();
		boolean enableTagSeenCount = (b & 0x80) > 0;
		boolean enableAccessSpecID = (b & 0x40) > 0;
		TagReportContentSelector tagReportConSel = new TagReportContentSelector(
				header, enableROSpecID, enableSpecIndex,
				enableInventoryParameterSpecID, enableAntennaID,
				enableChannelIndex, enablePeakRSSI, enableFirstSeenTimestamp,
				enableLastSeenTimestamp, enableTagSeenCount, enableAccessSpecID);
		List<C1G2EPCMemorySelector> c1g2EPCMemorySelectorList = new ArrayList<C1G2EPCMemorySelector>();
		List<Custom> cusList = new ArrayList<Custom>();

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_EPC_MEMORY_SELECTOR:
				C1G2EPCMemorySelector c1g2EPCMemorySelector = deserializeC1G2EPCMemorySelector(
						(TLVParameterHeader) parameterHeader, data);
				c1g2EPCMemorySelectorList.add(c1g2EPCMemorySelector);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		tagReportConSel.setC1g2EPCMemorySelectorList(c1g2EPCMemorySelectorList);
		tagReportConSel.setCusList(cusList);
		return tagReportConSel;
	}

	public void serialize(TagReportContentSelector tagReportConSel,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = tagReportConSel.getParameterHeader();
		header.setParameterLength(getLength(tagReportConSel));
		// serialize parameter
		serialize(header, data);
		byte b = (byte) 0x00;
		if (tagReportConSel.isEnableROSpecID()) {
			b = (byte) (b | 0x80);
		}
		if (tagReportConSel.isEnableSpecIndex()) {
			b = (byte) (b | 0x40);
		}
		if (tagReportConSel.isEnableInventoryParameterSpecID()) {
			b = (byte) (b | 0x20);
		}
		if (tagReportConSel.isEnableAntennaID()) {
			b = (byte) (b | 0x10);
		}
		if (tagReportConSel.isEnableChannelIndex()) {
			b = (byte) (b | 0x08);
		}
		if (tagReportConSel.isEnablePeakRSSI()) {
			b = (byte) (b | 0x04);
		}
		if (tagReportConSel.isEnableFirstSeenTimestamp()) {
			b = (byte) (b | 0x02);
		}
		if (tagReportConSel.isEnableLastSeenTimestamp()) {
			b = (byte) (b | 0x01);
		}
		data.put(b);

		b = (byte) 0x00;
		if (tagReportConSel.isEnableTagSeenCount()) {
			b = (byte) (b | 0x80);
		}
		if (tagReportConSel.isEnableAccessSpecID()) {
			b = (byte) (b | 0x40);
		}
		data.put(b);

		if (tagReportConSel.getC1g2EPCMemorySelectorList() != null
				&& !tagReportConSel.getC1g2EPCMemorySelectorList().isEmpty()) {
			for (C1G2EPCMemorySelector c1g2EPCMemorySelector : tagReportConSel
					.getC1g2EPCMemorySelectorList()) {
				serialize(c1g2EPCMemorySelector, data);
			}
		}

		if (tagReportConSel.getCusList() != null
				&& !tagReportConSel.getCusList().isEmpty()) {
			for (Custom cus : tagReportConSel.getCusList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(TagReportContentSelector tagReportConSel) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		if (tagReportConSel.getC1g2EPCMemorySelectorList() != null
				&& !tagReportConSel.getC1g2EPCMemorySelectorList().isEmpty()) {
			for (C1G2EPCMemorySelector c1g2EPCMemorySelector : tagReportConSel
					.getC1g2EPCMemorySelectorList()) {
				length += getLength(c1g2EPCMemorySelector);
			}
		}
		if (tagReportConSel.getCusList() != null
				&& !tagReportConSel.getCusList().isEmpty()) {
			for (Custom cus : tagReportConSel.getCusList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.7.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AccessReportSpec deserializeAccessReportSpec(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		AccessReportTrigger accessReportTrigger = AccessReportTrigger
				.get(DataTypeConverter.ubyte(data.get()));
		return new AccessReportSpec(header, accessReportTrigger);
	}

	public void serialize(AccessReportSpec ars, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = ars.getParameterHeader();
		header.setParameterLength(getLength(ars));
		// serialize parameter
		serialize(header, data);
		data.put((byte) ars.getAccessReportTrigger().getValue());
	}

	public int getLength(AccessReportSpec ars) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		return length;
	}

	/**
	 * Section 17.2.7.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public TagReportData deserializeTagReportData(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		TagReportData tagRD = null;

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case EPC_DATA:
			EPCData epcData = deserializeEPCData(
					(TLVParameterHeader) parameterHeader, data);
			tagRD = new TagReportData(header, epcData);
			break;
		case EPC_96:
			EPC96 epc96 = deserializeEPC96((TVParameterHeader) parameterHeader,
					data);
			tagRD = new TagReportData(header, epc96);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		ROSpecID roSpecID = null;
		SpecIndex specIndex = null;
		InventoryParameterSpecID invParaSpecID = null;
		AntennaId antID = null;
		PeakRSSI peakRSSI = null;
		ChannelIndex channelInd = null;
		FirstSeenTimestampUTC firstSTUTC = null;
		FirstSeenTimestampUptime firstSTUptime = null;
		LastSeenTimestampUTC lastSTUTC = null;
		LastSeenTimestampUptime lastSTUptime = null;
		TagSeenCount tagSC = null;
		List<Parameter> c1g2TagDataList = new ArrayList<>();
		AccessSpecId accessSpecID = null;
		List<Parameter> opSpecResultList = new ArrayList<>();
		List<Custom> cusList = new ArrayList<>();

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RO_SPEC_ID:
				roSpecID = deserializeROSpecID(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setRoSpecID(roSpecID);
				break;
			case SPEC_INDEX:
				specIndex = deserializeSpecIndex(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setSpecIndex(specIndex);
				break;
			case INVENTORY_PARAMETER_SPEC_ID:
				invParaSpecID = deserializeInventoryParameterSpecID(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setInvParaSpecID(invParaSpecID);
				break;
			case ANTENNA_ID:
				antID = deserializeAntennaID(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setAntID(antID);
				break;
			case PEAK_RSSI:
				peakRSSI = deserializePeakRSSI(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setPeakRSSI(peakRSSI);
				break;
			case CHANNEL_INDEX:
				channelInd = deserializeChannelIndex(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setChannelInd(channelInd);
				break;
			case FIRST_SEEN_TIMESTAMP_UTC:
				firstSTUTC = deserializeFirstSeenTimestampUTC(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setFirstSTUTC(firstSTUTC);
				break;
			case FIRST_SEEN_TIMESTAMP_UPTIME:
				firstSTUptime = deserializeFirstSeenTimestampUptime(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setFirstSTUptime(firstSTUptime);
				break;
			case LAST_SEEN_TIMESTAMP_UTC:
				lastSTUTC = deserializeLastSeenTimestampUTC(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setLastSTUTC(lastSTUTC);
				break;
			case LAST_SEEN_TIMESTAMP_UPTIME:
				lastSTUptime = deserializeLastSeenTimestampUptime(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setLastSTUptime(lastSTUptime);
				break;
			case TAG_SEEN_COUNT:
				tagSC = deserializeTagSeenCount(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setTagSC(tagSC);
				break;
			case C1G2_PC:
				C1G2PC c1g2PC = deserializeC1G2PC(
						(TVParameterHeader) parameterHeader, data);
				c1g2TagDataList.add(c1g2PC);
				break;
			case C1G2_XPCW1:
				C1G2XPCW1 c1g2XPCW1 = deserializeC1G2XPCW1(
						(TVParameterHeader) parameterHeader, data);
				c1g2TagDataList.add(c1g2XPCW1);
				break;
			case C1G2_XPCW2:
				C1G2XPCW2 c1g2XPCW2 = deserializeC1G2XPCW2(
						(TVParameterHeader) parameterHeader, data);
				c1g2TagDataList.add(c1g2XPCW2);
				break;
			case C1G2_CRC:
				C1G2CRC c1g2CRC = deserializeC1G2CRC(
						(TVParameterHeader) parameterHeader, data);
				c1g2TagDataList.add(c1g2CRC);
				break;
			case ACCESS_SPEC_ID:
				accessSpecID = deserializeAccessSpecID(
						(TVParameterHeader) parameterHeader, data);
				tagRD.setAccessSpecID(accessSpecID);
				break;
			case C1G2_READ_OP_SPEC_RESULT:
				C1G2ReadOpSpecResult c1g2R = deserializeC1G2ReadOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2R);
				break;
			case C1G2_WRITE_OP_SPEC_RESULT:
				C1G2WriteOpSpecResult c1g2W = deserializeC1G2WriteOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2W);
				break;
			case C1G2_KILL_OP_SPEC_RESULT:
				C1G2KillOpSpecResult c1g2K = deserializeC1G2KillOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2K);
				break;
			case C1G2_RECOMMISSION_OP_SPEC_RESULT:
				C1G2RecommissionOpSpecResult c1g2Recomm = deserializeC1G2RecommissionOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2Recomm);
				break;
			case C1G2_LOCK_OP_SPEC_RESULT:
				C1G2LockOpSpecResult c1g2L = deserializeC1G2LockOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2L);
				break;
			case C1G2_BLOCK_ERASE_OP_SPEC_RESULT:
				C1G2BlockEraseOpSpecResult c1g2BE = deserializeC1G2BlockEraseOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2BE);
				break;
			case C1G2_BLOCK_WRITE_OP_SPEC_RESULT:
				C1G2BlockWriteOpSpecResult c1g2BW = deserializeC1G2BlockWriteOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2BW);
				break;
			case C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT:
				C1G2BlockPermalockOpSpecResult c1g2BP = deserializeC1G2BlockPermalockOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2BP);
				break;
			case C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT:
				C1G2GetBlockPermalockStatusOpSpecResult c1g2GetBP = deserializeC1G2GetBlockPermalockStatusOpSpecResult(
						(TLVParameterHeader) parameterHeader, data);
				opSpecResultList.add(c1g2GetBP);
				break;
			case CLIENT_REQUEST_OP_SPEC_RESULT:
				ClientRequestOpSpecResult clRR = deserializeClientRequestOpSpecResult(
						(TVParameterHeader) parameterHeader, data);
				opSpecResultList.add(clRR);
				break;
			case CUSTOM:
				Custom custom = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(custom);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		tagRD.setC1g2TagDataList(c1g2TagDataList);
		tagRD.setOpSpecResultList(opSpecResultList);
		tagRD.setCusList(cusList);
		return tagRD;
	}

	public void serialize(TagReportData tagRD, ByteBuffer data)
			throws InvalidParameterTypeException {
		// set parameter length
		TLVParameterHeader header = tagRD.getParameterHeader();
		header.setParameterLength(getLength(tagRD));
		// serialize parameter
		serialize(header, data);
		EPCData epcData = tagRD.getEpcData();
		if (epcData != null) {
			serialize(epcData, data);
		} else {
			serialize(tagRD.getEpc96(), data);
		}

		if (tagRD.getRoSpecID() != null) {
			serialize(tagRD.getRoSpecID(), data);
		}
		if (tagRD.getSpecIndex() != null) {
			serialize(tagRD.getSpecIndex(), data);
		}
		if (tagRD.getInvParaSpecID() != null) {
			serialize(tagRD.getInvParaSpecID(), data);
		}
		if (tagRD.getAntID() != null) {
			serialize(tagRD.getAntID(), data);
		}
		if (tagRD.getPeakRSSI() != null) {
			serialize(tagRD.getPeakRSSI(), data);
		}
		if (tagRD.getChannelInd() != null) {
			serialize(tagRD.getChannelInd(), data);
		}
		if (tagRD.getFirstSTUTC() != null) {
			serialize(tagRD.getFirstSTUTC(), data);
		}
		if (tagRD.getFirstSTUptime() != null) {
			serialize(tagRD.getFirstSTUptime(), data);
		}
		if (tagRD.getLastSTUTC() != null) {
			serialize(tagRD.getLastSTUTC(), data);
		}
		if (tagRD.getLastSTUptime() != null) {
			serialize(tagRD.getLastSTUptime(), data);
		}
		if (tagRD.getTagSC() != null) {
			serialize(tagRD.getTagSC(), data);
		}

		if (tagRD.getC1g2TagDataList() != null
				&& !tagRD.getC1g2TagDataList().isEmpty()) {
			for (Parameter par : tagRD.getC1g2TagDataList()) {
				switch (par.getParameterHeader().getParameterType()) {
				case C1G2_PC:
					serialize((C1G2PC) par, data);
					break;
				case C1G2_XPCW1:
					serialize((C1G2XPCW1) par, data);
					break;
				case C1G2_XPCW2:
					serialize((C1G2XPCW2) par, data);
					break;
				case C1G2_CRC:
					serialize((C1G2CRC) par, data);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ par.getParameterHeader()
											.getParameterType());
				}
			}
		}

		if (tagRD.getAccessSpecID() != null) {
			serialize(tagRD.getAccessSpecID(), data);
		}

		if (tagRD.getOpSpecResultList() != null
				&& !tagRD.getOpSpecResultList().isEmpty()) {
			for (Parameter par : tagRD.getOpSpecResultList()) {
				switch (par.getParameterHeader().getParameterType()) {
				case C1G2_READ_OP_SPEC_RESULT:
					serialize((C1G2ReadOpSpecResult) par, data);
					break;
				case C1G2_WRITE_OP_SPEC_RESULT:
					serialize((C1G2WriteOpSpecResult) par, data);
					break;
				case C1G2_KILL_OP_SPEC_RESULT:
					serialize((C1G2KillOpSpecResult) par, data);
					break;
				case C1G2_RECOMMISSION_OP_SPEC_RESULT:
					serialize((C1G2RecommissionOpSpecResult) par, data);
					break;
				case C1G2_LOCK_OP_SPEC_RESULT:
					serialize((C1G2LockOpSpecResult) par, data);
					break;
				case C1G2_BLOCK_ERASE_OP_SPEC_RESULT:
					serialize((C1G2BlockEraseOpSpecResult) par, data);
					break;
				case C1G2_BLOCK_WRITE_OP_SPEC_RESULT:
					serialize((C1G2BlockWriteOpSpecResult) par, data);
					break;
				case C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT:
					serialize((C1G2BlockPermalockOpSpecResult) par, data);
					break;
				case C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT:
					serialize((C1G2GetBlockPermalockStatusOpSpecResult) par,
							data);
					break;
				case CLIENT_REQUEST_OP_SPEC_RESULT:
					serialize((C1G2ReadOpSpecResult) par, data);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ par.getParameterHeader()
											.getParameterType());
				}
			}
		}

		if (tagRD.getCusList() != null && !tagRD.getCusList().isEmpty()) {
			for (Custom cus : tagRD.getCusList()) {
				serialize(cus, data);
			}
		}

	}

	public int getLength(TagReportData tagRD)
			throws InvalidParameterTypeException {
		int length = TLV_PARAMETER_HEADER_LENGTH;
		EPCData epcData = tagRD.getEpcData();
		if (epcData != null) {
			length += getLength(epcData);
		} else {
			length += getLength(tagRD.getEpc96());
		}

		if (tagRD.getRoSpecID() != null) {
			length += getLength(tagRD.getRoSpecID());
		}
		if (tagRD.getSpecIndex() != null) {
			length += getLength(tagRD.getSpecIndex());
		}
		if (tagRD.getInvParaSpecID() != null) {
			length += getLength(tagRD.getInvParaSpecID());
		}
		if (tagRD.getAntID() != null) {
			length += getLength(tagRD.getAntID());
		}
		if (tagRD.getPeakRSSI() != null) {
			length += getLength(tagRD.getPeakRSSI());
		}
		if (tagRD.getChannelInd() != null) {
			length += getLength(tagRD.getChannelInd());
		}
		if (tagRD.getFirstSTUTC() != null) {
			length += getLength(tagRD.getFirstSTUTC());
		}
		if (tagRD.getFirstSTUptime() != null) {
			length += getLength(tagRD.getFirstSTUptime());
		}
		if (tagRD.getLastSTUTC() != null) {
			length += getLength(tagRD.getLastSTUTC());
		}
		if (tagRD.getLastSTUptime() != null) {
			length += getLength(tagRD.getLastSTUptime());
		}
		if (tagRD.getTagSC() != null) {
			length += getLength(tagRD.getTagSC());
		}

		if (tagRD.getC1g2TagDataList() != null
				&& !tagRD.getC1g2TagDataList().isEmpty()) {
			for (Parameter par : tagRD.getC1g2TagDataList()) {
				switch (par.getParameterHeader().getParameterType()) {
				case C1G2_PC:
					length += getLength((C1G2PC) par);
					break;
				case C1G2_XPCW1:
					length += getLength((C1G2XPCW1) par);
					break;
				case C1G2_XPCW2:
					length += getLength((C1G2XPCW2) par);
					break;
				case C1G2_CRC:
					length += getLength((C1G2CRC) par);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ par.getParameterHeader()
											.getParameterType());
				}
			}
		}

		if (tagRD.getAccessSpecID() != null) {
			length += getLength(tagRD.getAccessSpecID());
		}

		if (tagRD.getOpSpecResultList() != null
				&& !tagRD.getOpSpecResultList().isEmpty()) {
			for (Parameter par : tagRD.getOpSpecResultList()) {
				switch (par.getParameterHeader().getParameterType()) {
				case C1G2_READ_OP_SPEC_RESULT:
					length += getLength((C1G2ReadOpSpecResult) par);
					break;
				case C1G2_WRITE_OP_SPEC_RESULT:
					length += getLength((C1G2WriteOpSpecResult) par);
					break;
				case C1G2_KILL_OP_SPEC_RESULT:
					length += getLength((C1G2KillOpSpecResult) par);
					break;
				case C1G2_RECOMMISSION_OP_SPEC_RESULT:
					length += getLength((C1G2RecommissionOpSpecResult) par);
					break;
				case C1G2_LOCK_OP_SPEC_RESULT:
					length += getLength((C1G2LockOpSpecResult) par);
					break;
				case C1G2_BLOCK_ERASE_OP_SPEC_RESULT:
					length += getLength((C1G2BlockEraseOpSpecResult) par);
					break;
				case C1G2_BLOCK_WRITE_OP_SPEC_RESULT:
					length += getLength((C1G2BlockWriteOpSpecResult) par);
					break;
				case C1G2_BLOCK_PERMALOCK_OP_SPEC_RESULT:
					length += getLength((C1G2BlockPermalockOpSpecResult) par);
					break;
				case C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT:
					length += getLength((C1G2GetBlockPermalockStatusOpSpecResult) par);
					break;
				case CLIENT_REQUEST_OP_SPEC_RESULT:
					length += getLength((C1G2ReadOpSpecResult) par);
					break;
				default:
					throw new InvalidParameterTypeException(
							"Invalid parameter type "
									+ par.getParameterHeader()
											.getParameterType());
				}
			}
		}

		if (tagRD.getCusList() != null && !tagRD.getCusList().isEmpty()) {
			for (Custom cus : tagRD.getCusList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.7.3.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EPCData deserializeEPCData(TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int lengthBits = DataTypeConverter.ushort(data.getShort());

		byte[] bytes;
		if (lengthBits % 8 == 0) {
			bytes = new byte[lengthBits / 8];
		} else {
			bytes = new byte[lengthBits / 8 + 1];
		}
		data.get(bytes);
		BitSet epc = toBitSet(bytes);

		EPCData epcData = new EPCData(header, epc);
		epcData.setEpcLengthBits(bytes.length * 8);
		return epcData;
	}

	public void serialize(EPCData epcData, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = epcData.getParameterHeader();
		header.setParameterLength(getLength(epcData));
		// serialize parameter
		serialize(header, data);

		int byteCount = epcData.getEpcLengthBits() / 8;
		if (epcData.getEpcLengthBits() % 8 != 0) {
			byteCount++;
		}
		int lengthBits = byteCount * 8;
		epcData.setEpcLengthBits(lengthBits);
		data.putShort((short) lengthBits);
		if (lengthBits > 0) {
			byte[] invertedBytes = epcData.getEpc().toByteArray();
			byte[] fullBytes = new byte[byteCount];
			for (int i = 0; i < invertedBytes.length; i++) {
				fullBytes[i] = reverseBits(invertedBytes[i]);
			}
			data.put(fullBytes);
		}
	}

	public int getLength(EPCData epcData) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		int byteCount = epcData.getEpcLengthBits() / 8;
		if (epcData.getEpcLengthBits() % 8 != 0) {
			byteCount++;
		}
		length += byteCount;
		return length;
	}

	/**
	 * Section 17.2.7.3.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EPC96 deserializeEPC96(TVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		byte[] epc = new byte[12];
		data.get(epc);
		EPC96 epc96 = new EPC96(header, epc);
		return epc96;
	}

	public void serialize(EPC96 epc96, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = epc96.getParameterHeader();
		header.setParameterLength(getLength(epc96));
		// serialize parameter
		serialize(header, data);
		data.put(epc96.getEpc());
	}

	public int getLength(EPC96 epc96) {
		return TV_PARAMETER_HEADER_LENGTH + 12;
	}

	/**
	 * Section 17.2.7.3.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ROSpecID deserializeROSpecID(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		long rosID = DataTypeConverter.uint(data.getInt());
		return new ROSpecID(header, rosID);
	}

	public void serialize(ROSpecID roSpecID, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = roSpecID.getParameterHeader();
		header.setParameterLength(getLength(roSpecID));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) roSpecID.getRoSpecID());
	}

	public int getLength(ROSpecID roSpecID) {
		return TV_PARAMETER_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.2.7.3.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public SpecIndex deserializeSpecIndex(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int si = DataTypeConverter.ushort(data.getShort());
		return new SpecIndex(header, si);
	}

	public void serialize(SpecIndex specIndex, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = specIndex.getParameterHeader();
		header.setParameterLength(getLength(specIndex));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) specIndex.getSpecIndex());
	}

	public int getLength(SpecIndex specIndex) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.3.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public InventoryParameterSpecID deserializeInventoryParameterSpecID(
			TVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int iID = DataTypeConverter.ushort(data.getShort());
		return new InventoryParameterSpecID(header, iID);
	}

	public void serialize(InventoryParameterSpecID ipsID, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = ipsID.getParameterHeader();
		header.setParameterLength(getLength(ipsID));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) ipsID.getInventoryParameterSpecID());
	}

	public int getLength(InventoryParameterSpecID ipsID) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.3.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AntennaId deserializeAntennaID(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int aID = DataTypeConverter.ushort(data.getShort());
		return new AntennaId(header, aID);
	}

	public void serialize(AntennaId antennaID, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = antennaID.getParameterHeader();
		header.setParameterLength(getLength(antennaID));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) antennaID.getAntennaId());
	}

	public int getLength(AntennaId antennaID) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.3.7
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public PeakRSSI deserializePeakRSSI(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		byte r = data.get();
		return new PeakRSSI(header, r);
	}

	public void serialize(PeakRSSI rssi, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = rssi.getParameterHeader();
		header.setParameterLength(getLength(rssi));
		// serialize parameter
		serialize(header, data);
		data.put(rssi.getPeakRSSI());
	}

	public int getLength(PeakRSSI rssi) {
		return TV_PARAMETER_HEADER_LENGTH + 1;
	}

	/**
	 * Section 17.2.7.3.8
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ChannelIndex deserializeChannelIndex(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int ci = DataTypeConverter.ushort(data.getShort());
		return new ChannelIndex(header, ci);
	}

	public void serialize(ChannelIndex channelIndex, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = channelIndex.getParameterHeader();
		header.setParameterLength(getLength(channelIndex));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) channelIndex.getChannelIndex());
	}

	public int getLength(ChannelIndex channelIndex) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.3.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public FirstSeenTimestampUTC deserializeFirstSeenTimestampUTC(
			TVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		// microseconds 64 bits
		BigInteger microseconds = DataTypeConverter.ulong(data.getLong());
		return new FirstSeenTimestampUTC(header, microseconds);
	}

	public void serialize(FirstSeenTimestampUTC FSTutc, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = FSTutc.getParameterHeader();
		header.setParameterLength(getLength(FSTutc));
		// serialize parameter
		serialize(header, data);
		// microseconds 64 bits
		data.putLong(FSTutc.getMicroseconds().longValue());
	}

	public int getLength(FirstSeenTimestampUTC FSTutc) {
		return TV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.7.3.10
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public FirstSeenTimestampUptime deserializeFirstSeenTimestampUptime(
			TVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		// microseconds 64 bits
		BigInteger microseconds = DataTypeConverter.ulong(data.getLong());
		return new FirstSeenTimestampUptime(header, microseconds);
	}

	public void serialize(FirstSeenTimestampUptime FSTuptime, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = FSTuptime.getParameterHeader();
		header.setParameterLength(getLength(FSTuptime));
		// serialize parameter
		serialize(header, data);
		// microseconds 64 bits
		data.putLong(FSTuptime.getMicroseconds().longValue());
	}

	public int getLength(FirstSeenTimestampUptime FSTuptime) {
		return TV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.7.3.11
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public LastSeenTimestampUTC deserializeLastSeenTimestampUTC(
			TVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		// microseconds 64 bits
		BigInteger microseconds = DataTypeConverter.ulong(data.getLong());
		return new LastSeenTimestampUTC(header, microseconds);
	}

	public void serialize(LastSeenTimestampUTC LSTutc, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = LSTutc.getParameterHeader();
		header.setParameterLength(getLength(LSTutc));
		// serialize parameter
		serialize(header, data);
		// microseconds 64 bits
		data.putLong(LSTutc.getMicroseconds().longValue());
	}

	public int getLength(LastSeenTimestampUTC LSTutc) {
		return TV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.7.3.12
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public LastSeenTimestampUptime deserializeLastSeenTimestampUptime(
			TVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		// microseconds 64 bits
		BigInteger microseconds = DataTypeConverter.ulong(data.getLong());
		return new LastSeenTimestampUptime(header, microseconds);
	}

	public void serialize(LastSeenTimestampUptime LSTuptime, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = LSTuptime.getParameterHeader();
		header.setParameterLength(getLength(LSTuptime));
		// serialize parameter
		serialize(header, data);
		// microseconds 64 bits
		data.putLong(LSTuptime.getMicroseconds().longValue());
	}

	public int getLength(LastSeenTimestampUptime LSTuptime) {
		return TV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.7.3.13
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public TagSeenCount deserializeTagSeenCount(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int tc = DataTypeConverter.ushort(data.getShort());
		return new TagSeenCount(header, tc);
	}

	public void serialize(TagSeenCount tagSeenCount, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = tagSeenCount.getParameterHeader();
		header.setParameterLength(getLength(tagSeenCount));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) tagSeenCount.getTagCount());
	}

	public int getLength(TagSeenCount tagSeenCount) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.3.14
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ClientRequestOpSpecResult deserializeClientRequestOpSpecResult(
			TVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int opsID = DataTypeConverter.ushort(data.getShort());
		return new ClientRequestOpSpecResult(header, opsID);
	}

	public void serialize(ClientRequestOpSpecResult clientReOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = clientReOpSpecResult.getParameterHeader();
		header.setParameterLength(getLength(clientReOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) clientReOpSpecResult.getOpSpecID());
	}

	public int getLength(ClientRequestOpSpecResult clientReOpSpecResult) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.3.15
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public AccessSpecId deserializeAccessSpecID(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		long asID = DataTypeConverter.uint(data.getInt());
		return new AccessSpecId(header, asID);
	}

	public void serialize(AccessSpecId accessSpecID, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = accessSpecID.getParameterHeader();
		header.setParameterLength(getLength(accessSpecID));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) accessSpecID.getAccessSpecId());
	}

	public int getLength(AccessSpecId accessSpecID) {
		return TV_PARAMETER_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.2.7.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public RFSurveyReportData deserializeRFSurveyReportData(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		ROSpecID roSID = null;
		SpecIndex si = null;
		List<FrequencyRSSILevelEntry> fRSSIleList = new ArrayList<FrequencyRSSILevelEntry>();
		List<Custom> cusList = new ArrayList<Custom>();

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RO_SPEC_ID:
				roSID = deserializeROSpecID(
						(TVParameterHeader) parameterHeader, data);
				break;
			case SPEC_INDEX:
				si = deserializeSpecIndex((TVParameterHeader) parameterHeader,
						data);
				break;
			case FREQUENCY_RSSI_LEVEL_ENTRY:
				FrequencyRSSILevelEntry fRSSIle = deserializeFrequencyRSSILevelEntry(
						(TLVParameterHeader) parameterHeader, data);
				fRSSIleList.add(fRSSIle);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		RFSurveyReportData rfSRD = new RFSurveyReportData(header, fRSSIleList);
		rfSRD.setRoSpecID(roSID);
		rfSRD.setSpecIndex(si);
		rfSRD.setCusList(cusList);
		return rfSRD;
	}

	public void serialize(RFSurveyReportData rfSRD, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfSRD.getParameterHeader();
		header.setParameterLength(getLength(rfSRD));
		// serialize parameter
		serialize(header, data);
		if (rfSRD.getRoSpecID() != null) {
			serialize(rfSRD.getRoSpecID(), data);
		}
		if (rfSRD.getSpecIndex() != null) {
			serialize(rfSRD.getSpecIndex(), data);
		}

		for (FrequencyRSSILevelEntry fRSSIle : rfSRD
				.getFrequencyRSSILevelEntryList()) {
			serialize(fRSSIle, data);
		}

		if (rfSRD.getCusList() != null && !rfSRD.getCusList().isEmpty()) {
			for (Custom cus : rfSRD.getCusList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(RFSurveyReportData rfSRD) {
		int length = TLV_PARAMETER_HEADER_LENGTH;

		if (rfSRD.getRoSpecID() != null) {
			length += getLength(rfSRD.getRoSpecID());
		}
		if (rfSRD.getSpecIndex() != null) {
			length += getLength(rfSRD.getSpecIndex());
		}

		for (FrequencyRSSILevelEntry fRSSIle : rfSRD
				.getFrequencyRSSILevelEntryList()) {
			length += getLength(fRSSIle);
		}

		if (rfSRD.getCusList() != null && !rfSRD.getCusList().isEmpty()) {
			for (Custom cus : rfSRD.getCusList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.7.4.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public FrequencyRSSILevelEntry deserializeFrequencyRSSILevelEntry(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		long freq = DataTypeConverter.uint(data.getInt());
		long bandWidth = DataTypeConverter.uint(data.getInt());
		byte averRSSI = data.get();
		byte peekRSSI = data.get();

		FrequencyRSSILevelEntry fRSSIle = null;
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case UTC_TIMESTAMP:
			UTCTimestamp utcTimestamp = deserializeUTCTimestamp(
					(TLVParameterHeader) parameterHeader, data);
			fRSSIle = new FrequencyRSSILevelEntry(header, freq, bandWidth,
					averRSSI, peekRSSI, utcTimestamp);
			break;
		case UPTIME:
			Uptime uptime = deserializeUptime(
					(TLVParameterHeader) parameterHeader, data);
			fRSSIle = new FrequencyRSSILevelEntry(header, freq, bandWidth,
					averRSSI, peekRSSI, uptime);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}
		return fRSSIle;
	}

	public void serialize(FrequencyRSSILevelEntry fRSSIle, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = fRSSIle.getParameterHeader();
		header.setParameterLength(getLength(fRSSIle));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) fRSSIle.getFrequency());
		data.putInt((int) fRSSIle.getBandWidth());
		data.put(fRSSIle.getAverageRSSI());
		data.put(fRSSIle.getPeekRSSI());
		UTCTimestamp utcTimestamp = fRSSIle.getUtcTimestamp();
		if (utcTimestamp != null) {
			serialize(utcTimestamp, data);
		} else {
			serialize(fRSSIle.getUpTime(), data);
		}
	}

	public int getLength(FrequencyRSSILevelEntry fRSSIle) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 10;
		UTCTimestamp utcTimestamp = fRSSIle.getUtcTimestamp();
		if (utcTimestamp != null) {
			length += getLength(utcTimestamp);
		} else {
			length += getLength(fRSSIle.getUpTime());
		}
		return length;
	}

	/**
	 * Section 17.2.7.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ReaderEventNotificationSpec deserializeReaderEventNotificationSpec(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		ReaderEventNotificationSpec readerENSpec = null;
		List<EventNotificationState> eventNotificationStateList = new ArrayList<EventNotificationState>();
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case EVENT_NOTIFICATION_STATE:
			EventNotificationState ens = deserializeEventNotificationState(
					(TLVParameterHeader) parameterHeader, data);
			eventNotificationStateList.add(ens);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}
		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case EVENT_NOTIFICATION_STATE:
				EventNotificationState ens = deserializeEventNotificationState(
						(TLVParameterHeader) parameterHeader, data);
				eventNotificationStateList.add(ens);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		readerENSpec = new ReaderEventNotificationSpec(header,
				eventNotificationStateList);
		return readerENSpec;
	}

	public void serialize(ReaderEventNotificationSpec readerENSpec,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = readerENSpec.getParameterHeader();
		header.setParameterLength(getLength(readerENSpec));
		// serialize parameter
		serialize(header, data);
		for (EventNotificationState ens : readerENSpec
				.getEventNotificationStateList()) {
			serialize(ens, data);
		}
	}

	public int getLength(ReaderEventNotificationSpec readerENSpec) {
		int length = TLV_PARAMETER_HEADER_LENGTH;
		for (EventNotificationState ens : readerENSpec
				.getEventNotificationStateList()) {
			length += getLength(ens);
		}
		return length;
	}

	/**
	 * Section 17.2.7.5.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public EventNotificationState deserializeEventNotificationState(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		EventNotificationStateEventType eventType = EventNotificationStateEventType
				.get(DataTypeConverter.ushort(data.getShort()));

		byte b = data.get();
		boolean notificationState = (b & 0x80) > 0;
		return new EventNotificationState(header, eventType, notificationState);
	}

	public void serialize(EventNotificationState ens, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = ens.getParameterHeader();
		header.setParameterLength(getLength(ens));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) ens.getEventType().getValue());

		byte b = (byte) 0x00;
		if (ens.isNotificationState()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
	}

	public int getLength(EventNotificationState ens) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 3;
		return length;
	}

	/**
	 * Section 17.2.7.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public ReaderEventNotificationData deserializeReaderEventNotificationData(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		ReaderEventNotificationData ret = null;
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case UTC_TIMESTAMP:
			UTCTimestamp utcTimestamp = deserializeUTCTimestamp(
					(TLVParameterHeader) parameterHeader, data);
			ret = new ReaderEventNotificationData(header, utcTimestamp);
			break;
		case UPTIME:
			Uptime uptime = deserializeUptime(
					(TLVParameterHeader) parameterHeader, data);
			ret = new ReaderEventNotificationData(header, uptime);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		List<Custom> cusList = new ArrayList<>();

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case HOPPING_EVENT:
				HoppingEvent hE = deserializeHoppingEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setHoppingEvent(hE);
				break;
			case GPI_EVENT:
				GPIEvent gpiE = deserializeGPIEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setGpiEvent(gpiE);
				break;
			case RO_SPEC_EVENT:
				ROSpecEvent roSE = deserializeROSpecEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setRoSpecEvent(roSE);
				break;
			case REPORT_BUFFER_LEVEL_WARNING_EVENT:
				ReportBufferLevelWarningEvent reBLWE = deserializeReportBufferLevelWarningEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setReportBufferLevelWarningEvent(reBLWE);
				break;
			case REPORT_BUFFER_OVERFLOW_ERROR_EVENT:
				ReportBufferOverflowErrorEvent reBOEE = deserializeReportBufferOverflowErrorEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setReportBufferOverflowErrorEvent(reBOEE);
				break;
			case READER_EXCEPTION_EVENT:
				ReaderExceptionEvent reEE = deserializeReaderExceptionEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setReaderExceptionEvent(reEE);
				break;
			case RF_SURVEY_EVENT:
				RFSurveyEvent rfSE = deserializeRFSurveyEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setRfSurveyEvent(rfSE);
				break;
			case AI_SPEC_EVENT:
				AISpecEvent aiSE = deserializeAISpecEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setAiSpecEvent(aiSE);
				break;
			case ANTENNA_EVENT:
				AntennaEvent aE = deserializeAntennaEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setAntennaEvent(aE);
				break;

			case CONNECTION_ATTEMPT_EVENT:
				ConnectionAttemptEvent cae = deserializeConnectionAttemptEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setConnectionAttemptEvent(cae);
				break;
			case CONNECTION_CLOSE_EVENT:
				ConnectionCloseEvent cce = deserializeConnectionCloseEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setConnectionCloseEvent(cce);
				break;
			case SPEC_LOOP_EVENT:
				SpecLoopEvent slE = deserializeSpecLoopEvent(
						(TLVParameterHeader) parameterHeader, data);
				ret.setSpecLoopEvent(slE);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		ret.setCustomList(cusList);

		return ret;
	}

	public void serialize(ReaderEventNotificationData rend, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rend.getParameterHeader();
		header.setParameterLength(getLength(rend));
		// serialize parameter
		serialize(header, data);
		UTCTimestamp utcTimestamp = rend.getUtcTimestamp();
		if (utcTimestamp != null) {
			serialize(utcTimestamp, data);
		} else {
			serialize(rend.getUptime(), data);
		}

		if (rend.getHoppingEvent() != null) {
			serialize(rend.getHoppingEvent(), data);
		}

		if (rend.getGpiEvent() != null) {
			serialize(rend.getGpiEvent(), data);
		}

		if (rend.getRoSpecEvent() != null) {
			serialize(rend.getRoSpecEvent(), data);
		}

		if (rend.getReportBufferLevelWarningEvent() != null) {
			serialize(rend.getReportBufferLevelWarningEvent(), data);
		}

		if (rend.getReportBufferOverflowErrorEvent() != null) {
			serialize(rend.getReportBufferOverflowErrorEvent(), data);
		}

		if (rend.getReaderExceptionEvent() != null) {
			serialize(rend.getReaderExceptionEvent(), data);
		}

		if (rend.getRfSurveyEvent() != null) {
			serialize(rend.getRfSurveyEvent(), data);
		}

		if (rend.getAiSpecEvent() != null) {
			serialize(rend.getAiSpecEvent(), data);
		}

		if (rend.getAntennaEvent() != null) {
			serialize(rend.getAntennaEvent(), data);
		}

		ConnectionAttemptEvent cae = rend.getConnectionAttemptEvent();
		if (cae != null) {
			serialize(cae, data);
		}
		ConnectionCloseEvent cce = rend.getConnectionCloseEvent();
		if (cce != null) {
			serialize(cce, data);
		}

		if (rend.getSpecLoopEvent() != null) {
			serialize(rend.getSpecLoopEvent(), data);
		}

		if (rend.getCustomList() != null && !rend.getCustomList().isEmpty()) {
			for (Custom cus : rend.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(ReaderEventNotificationData rend) {
		int length = TLV_PARAMETER_HEADER_LENGTH;
		UTCTimestamp utcTimestamp = rend.getUtcTimestamp();
		if (utcTimestamp != null) {
			length += getLength(utcTimestamp);
		} else {
			length += getLength(rend.getUptime());
		}

		if (rend.getHoppingEvent() != null) {
			length += getLength(rend.getHoppingEvent());
		}

		if (rend.getGpiEvent() != null) {
			length += getLength(rend.getGpiEvent());
		}

		if (rend.getRoSpecEvent() != null) {
			length += getLength(rend.getRoSpecEvent());
		}

		if (rend.getReportBufferLevelWarningEvent() != null) {
			length += getLength(rend.getReportBufferLevelWarningEvent());
		}

		if (rend.getReportBufferOverflowErrorEvent() != null) {
			length += getLength(rend.getReportBufferOverflowErrorEvent());
		}

		if (rend.getReaderExceptionEvent() != null) {
			length += getLength(rend.getReaderExceptionEvent());
		}

		if (rend.getRfSurveyEvent() != null) {
			length += getLength(rend.getRfSurveyEvent());
		}

		if (rend.getAiSpecEvent() != null) {
			length += getLength(rend.getAiSpecEvent());
		}

		if (rend.getAntennaEvent() != null) {
			length += getLength(rend.getAntennaEvent());
		}

		ConnectionAttemptEvent cae = rend.getConnectionAttemptEvent();
		if (cae != null) {
			length += getLength(cae);
		}
		ConnectionCloseEvent cce = rend.getConnectionCloseEvent();
		if (cce != null) {
			length += getLength(cce);
		}

		if (rend.getSpecLoopEvent() != null) {
			length += getLength(rend.getSpecLoopEvent());
		}

		if (rend.getCustomList() != null && !rend.getCustomList().isEmpty()) {
			for (Custom cus : rend.getCustomList()) {
				length += getLength(cus);
			}
		}

		return length;
	}

	/**
	 * Section 17.2.7.6.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public HoppingEvent deserializeHoppingEvent(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int hID = DataTypeConverter.ushort(data.getShort());
		int nCI = DataTypeConverter.ushort(data.getShort());
		return new HoppingEvent(header, hID, nCI);
	}

	public void serialize(HoppingEvent hE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = hE.getParameterHeader();
		header.setParameterLength(getLength(hE));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) hE.getHopTableID());
		data.putShort((short) hE.getNextChannelIndex());
	}

	public int getLength(HoppingEvent hE) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.2.7.6.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public GPIEvent deserializeGPIEvent(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int portNr = DataTypeConverter.ushort(data.getShort());
		byte b = data.get();
		boolean e = (b & 0x80) > 0;
		return new GPIEvent(header, portNr, e);
	}

	public void serialize(GPIEvent gpiE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = gpiE.getParameterHeader();
		header.setParameterLength(getLength(gpiE));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) gpiE.getGpiPortNumber());
		byte b = (byte) 0x00;
		if (gpiE.isState()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);
	}

	public int getLength(GPIEvent gpiE) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 3;
		return length;
	}

	/**
	 * Section 17.2.7.6.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ROSpecEvent deserializeROSpecEvent(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		ROSpecEventType eType = ROSpecEventType.get(DataTypeConverter
				.ubyte(data.get()));
		long roSpecID = DataTypeConverter.uint(data.getInt());
		long pROSpecID = DataTypeConverter.uint(data.getInt());

		return new ROSpecEvent(header, eType, roSpecID, pROSpecID);
	}

	public void serialize(ROSpecEvent roSE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = roSE.getParameterHeader();
		header.setParameterLength(getLength(roSE));
		// serialize parameter
		serialize(header, data);
		data.put((byte) roSE.getEventType().getValue());
		data.putInt((int) roSE.getRoSpecID());
		data.putInt((int) roSE.getPreemptingROSpecID());
	}

	public int getLength(ROSpecEvent roSE) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 9;
		return length;
	}

	/**
	 * Section 17.2.7.6.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ReportBufferLevelWarningEvent deserializeReportBufferLevelWarningEvent(
			TLVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		short reportBufferPercentageFull = DataTypeConverter.ubyte(data.get());
		return new ReportBufferLevelWarningEvent(header,
				reportBufferPercentageFull);
	}

	public void serialize(ReportBufferLevelWarningEvent rblwE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rblwE.getParameterHeader();
		header.setParameterLength(getLength(rblwE));
		// serialize parameter
		serialize(header, data);
		data.put((byte) rblwE.getReportBufferPercentageFull());
	}

	public int getLength(ReportBufferLevelWarningEvent rblwE) {
		return TLV_PARAMETER_HEADER_LENGTH + 1;
	}

	/**
	 * Section 17.2.7.6.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ReportBufferOverflowErrorEvent deserializeReportBufferOverflowErrorEvent(
			TLVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		return new ReportBufferOverflowErrorEvent(header);
	}

	public void serialize(ReportBufferOverflowErrorEvent rfoeE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfoeE.getParameterHeader();
		header.setParameterLength(getLength(rfoeE));
		// serialize parameter
		serialize(header, data);
	}

	public int getLength(ReportBufferOverflowErrorEvent rfoeE) {
		return TLV_PARAMETER_HEADER_LENGTH;
	}

	/**
	 * Section 17.2.7.6.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ReaderExceptionEvent deserializeReaderExceptionEvent(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		int stringByteCount = DataTypeConverter.ushort(data.getShort());
		byte[] dst = new byte[stringByteCount];
		data.get(dst, 0, stringByteCount);
		String sMessage = new String(dst, StandardCharsets.UTF_8);

		ReaderExceptionEvent rxe = new ReaderExceptionEvent(header, sMessage);
		rxe.setStringByteCount(dst.length);
		List<Custom> cusList = new ArrayList<>();

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case RO_SPEC_ID:
				ROSpecID roSID = deserializeROSpecID(
						(TVParameterHeader) parameterHeader, data);
				rxe.setRoSpecID(roSID);
				break;
			case SPEC_INDEX:
				SpecIndex si = deserializeSpecIndex(
						(TVParameterHeader) parameterHeader, data);
				rxe.setSpecIndex(si);
				break;
			case INVENTORY_PARAMETER_SPEC_ID:
				InventoryParameterSpecID ipSID = deserializeInventoryParameterSpecID(
						(TVParameterHeader) parameterHeader, data);
				rxe.setIpSpecID(ipSID);
				break;
			case ANTENNA_ID:
				AntennaId aID = deserializeAntennaID(
						(TVParameterHeader) parameterHeader, data);
				rxe.setAntennaId(aID);
				break;
			case ACCESS_SPEC_ID:
				AccessSpecId accessSID = deserializeAccessSpecID(
						(TVParameterHeader) parameterHeader, data);
				rxe.setAccessSpecId(accessSID);
				break;
			case OP_SPEC_ID:
				OpSpecID opSID = deserializeOpSpecID(
						(TVParameterHeader) parameterHeader, data);
				rxe.setOpSpecID(opSID);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		rxe.setCustomList(cusList);
		return rxe;
	}

	public void serialize(ReaderExceptionEvent rxe, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rxe.getParameterHeader();
		header.setParameterLength(getLength(rxe));
		// serialize parameter
		serialize(header, data);
		String sMessage = rxe.getStringMessage();
		byte[] byteMessage = sMessage.getBytes(StandardCharsets.UTF_8);
		int byteCount = byteMessage.length;
		rxe.setStringByteCount(byteCount);
		data.putShort((short) byteCount);
		data.put(byteMessage);

		if (rxe.getRoSpecID() != null) {
			serialize(rxe.getRoSpecID(), data);
		}

		if (rxe.getSpecIndex() != null) {
			serialize(rxe.getSpecIndex(), data);
		}

		if (rxe.getIpSpecID() != null) {
			serialize(rxe.getIpSpecID(), data);
		}

		if (rxe.getAntennaId() != null) {
			serialize(rxe.getAntennaId(), data);
		}

		if (rxe.getAccessSpecId() != null) {
			serialize(rxe.getAccessSpecId(), data);
		}

		if (rxe.getOpSpecID() != null) {
			serialize(rxe.getOpSpecID(), data);
		}

		if (rxe.getCustomList() != null && !rxe.getCustomList().isEmpty()) {
			for (Custom cus : rxe.getCustomList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(ReaderExceptionEvent rxe) {
		String sMessage = rxe.getStringMessage();
		int byteCount = sMessage.getBytes(StandardCharsets.UTF_8).length;
		int length = TLV_PARAMETER_HEADER_LENGTH + 2 + byteCount;

		if (rxe.getRoSpecID() != null) {
			length += getLength(rxe.getRoSpecID());
		}

		if (rxe.getSpecIndex() != null) {
			length += getLength(rxe.getSpecIndex());
		}

		if (rxe.getIpSpecID() != null) {
			length += getLength(rxe.getIpSpecID());
		}

		if (rxe.getAntennaId() != null) {
			length += getLength(rxe.getAntennaId());
		}

		if (rxe.getAccessSpecId() != null) {
			length += getLength(rxe.getAccessSpecId());
		}

		if (rxe.getOpSpecID() != null) {
			length += getLength(rxe.getOpSpecID());
		}

		if (rxe.getCustomList() != null && !rxe.getCustomList().isEmpty()) {
			for (Custom cus : rxe.getCustomList()) {
				length += getLength(cus);
			}
		}
		return length;
	}

	/**
	 * Section 17.2.7.6.6.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public OpSpecID deserializeOpSpecID(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int opSID = DataTypeConverter.ushort(data.getShort());
		return new OpSpecID(header, opSID);
	}

	public void serialize(OpSpecID opSID, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = opSID.getParameterHeader();
		header.setParameterLength(getLength(opSID));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) opSID.getOpSpecID());
	}

	public int getLength(OpSpecID opSID) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.6.7
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public RFSurveyEvent deserializeRFSurveyEvent(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		RFSurveyEventType eType = RFSurveyEventType.get(DataTypeConverter
				.ubyte(data.get()));
		long roSpecID = DataTypeConverter.uint(data.getInt());
		int si = DataTypeConverter.ushort(data.getShort());

		return new RFSurveyEvent(header, eType, roSpecID, si);
	}

	public void serialize(RFSurveyEvent rfSE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rfSE.getParameterHeader();
		header.setParameterLength(getLength(rfSE));
		// serialize parameter
		serialize(header, data);
		data.put((byte) rfSE.getEventType().getValue());
		data.putInt((int) rfSE.getRoSpecID());
		data.putShort((short) rfSE.getSpecIndex());
	}

	public int getLength(RFSurveyEvent rfSE) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 7;
		return length;
	}

	/**
	 * Section 17.2.7.6.8
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public AISpecEvent deserializeAISpecEvent(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		AISpecEventType eType = AISpecEventType.get(DataTypeConverter
				.ubyte(data.get()));
		long roSpecID = DataTypeConverter.uint(data.getInt());
		int si = DataTypeConverter.ushort(data.getShort());

		AISpecEvent siSE = new AISpecEvent(header, eType, roSpecID, si);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_SINGULATION_DETAILS:
				C1G2SingulationDetails c1g2SD = deserializeC1G2SingulationDetails(
						(TVParameterHeader) parameterHeader, data);
				siSE.setC1g2SingulationDetails(c1g2SD);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return siSE;
	}

	public void serialize(AISpecEvent siSE, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = siSE.getParameterHeader();
		header.setParameterLength(getLength(siSE));
		// serialize parameter
		serialize(header, data);
		data.put((byte) siSE.getEventType().getValue());
		data.putInt((int) siSE.getRoSpecID());
		data.putShort((short) siSE.getSpecIndex());

		C1G2SingulationDetails c1g2SD = siSE.getC1g2SingulationDetails();
		if (c1g2SD != null) {
			serialize(c1g2SD, data);
		}
	}

	public int getLength(AISpecEvent siSE) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 7;
		C1G2SingulationDetails c1g2SD = siSE.getC1g2SingulationDetails();
		if (c1g2SD != null) {
			length += getLength(c1g2SD);
		}
		return length;
	}

	/**
	 * Section 17.2.7.6.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public AntennaEvent deserializeAntennaEvent(TLVParameterHeader header,
			ByteBuffer data) {
		// deserialize parameter
		AntennaEventType eType = AntennaEventType.get(DataTypeConverter
				.ubyte(data.get()));
		int aID = DataTypeConverter.ushort(data.getShort());

		return new AntennaEvent(header, eType, aID);
	}

	public void serialize(AntennaEvent ae, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = ae.getParameterHeader();
		header.setParameterLength(getLength(ae));
		// serialize parameter
		serialize(header, data);
		data.put((byte) ae.getEventType().getValue());
		data.putShort((short) ae.getAntennaID());
	}

	public int getLength(AntennaEvent ae) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.2.7.6.10
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ConnectionAttemptEvent deserializeConnectionAttemptEvent(
			TLVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		// status 16 bits
		ConnectionAttemptEventStatusType status = ConnectionAttemptEventStatusType
				.get(data.getShort());
		return new ConnectionAttemptEvent(header, status);
	}

	public void serialize(ConnectionAttemptEvent cae, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = cae.getParameterHeader();
		header.setParameterLength(getLength(cae));
		// serialize parameter
		serialize(header, data);
		// status 16 bits
		data.putShort(cae.getStatus().getValue());
	}

	public int getLength(ConnectionAttemptEvent cae) {
		return TLV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.2.7.6.11
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ConnectionCloseEvent deserializeConnectionCloseEvent(
			TLVParameterHeader header, ByteBuffer data) {
		// deserialize parameter
		return new ConnectionCloseEvent(header);
	}

	public void serialize(ConnectionCloseEvent cce, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = cce.getParameterHeader();
		header.setParameterLength(getLength(cce));
		// serialize parameter
		serialize(header, data);
	}

	public int getLength(ConnectionCloseEvent cce) {
		return TLV_PARAMETER_HEADER_LENGTH;
	}

	/**
	 * Section 17.2.7.6.12
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public SpecLoopEvent deserializeSpecLoopEvent(TLVParameterHeader header,
			ByteBuffer data) {
		// deserialize parameter
		long opSpecID = DataTypeConverter.uint(data.getInt());
		long lc = DataTypeConverter.uint(data.getInt());

		return new SpecLoopEvent(header, opSpecID, lc);
	}

	public void serialize(SpecLoopEvent sle, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = sle.getParameterHeader();
		header.setParameterLength(getLength(sle));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) sle.getRoSpecID());
		data.putInt((int) sle.getLoopCount());
	}

	public int getLength(SpecLoopEvent sle) {
		return TLV_PARAMETER_HEADER_LENGTH + 8;
	}

	/**
	 * Section 17.2.8.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public LLRPStatus deserializeLLRPStatus(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		LLRPStatusCode statusCode = LLRPStatusCode.get(DataTypeConverter
				.ushort(data.getShort()));
		int iEDBC = DataTypeConverter.ushort(data.getShort());
		byte[] dst = new byte[iEDBC];
		data.get(dst, 0, iEDBC);
		String errorDescription = new String(dst, StandardCharsets.UTF_8);
		LLRPStatus ret = new LLRPStatus(header, statusCode, errorDescription);
		ret.setErrorDescriptionByteCount(dst.length);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case FIELD_ERROR:
				FieldError fieldError = deserializeFieldError(
						(TLVParameterHeader) parameterHeader, data);
				ret.setFieldError(fieldError);
				break;
			case PARAMETER_ERROR:
				ParameterError parameterError = deserializeParameterError(
						(TLVParameterHeader) parameterHeader, data);
				ret.setParameterError(parameterError);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return ret;
	}

	public void serialize(LLRPStatus status, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = status.getParameterHeader();
		header.setParameterLength(getLength(status));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) status.getStatusCode().getValue());

		String errorDescription = status.getErrorDescription();
		byte[] error = errorDescription.getBytes(StandardCharsets.UTF_8);
		int iEDBC = error.length;
		status.setErrorDescriptionByteCount(iEDBC);
		data.putShort((short) iEDBC);
		data.put(error);

		FieldError fieldError = status.getFieldError();
		if (fieldError != null) {
			serialize(fieldError, data);
		}

		ParameterError parameterError = status.getParameterError();
		if (parameterError != null) {
			serialize(parameterError, data);
		}
	}

	public int getLength(LLRPStatus llrpStatus) {
		String errorDescription = llrpStatus.getErrorDescription();
		int iEDBC = errorDescription.getBytes(StandardCharsets.UTF_8).length;
		int length = TLV_PARAMETER_HEADER_LENGTH + 4 + iEDBC;
		FieldError fieldError = llrpStatus.getFieldError();
		if (fieldError != null) {
			length += getLength(fieldError);
		}

		ParameterError parameterError = llrpStatus.getParameterError();
		if (parameterError != null) {
			length += getLength(parameterError);
		}
		return length;
	}

	/**
	 * Section 17.2.8.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public FieldError deserializeFieldError(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int fieldNum = DataTypeConverter.ushort(data.getShort());
		LLRPStatusCode errorCode = LLRPStatusCode.get(DataTypeConverter
				.ushort(data.getShort()));
		return new FieldError(header, fieldNum, errorCode);
	}

	public void serialize(FieldError rend, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rend.getParameterHeader();
		header.setParameterLength(getLength(rend));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) rend.getFieldNum());
		data.putShort((short) rend.getErrorCode().getValue());
	}

	public int getLength(FieldError rend) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.2.8.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public ParameterError deserializeParameterError(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		int parameterType = DataTypeConverter.ushort(data.getShort());
		LLRPStatusCode errorCode = LLRPStatusCode.get(DataTypeConverter
				.ushort(data.getShort()));
		ParameterError ret = new ParameterError(header, parameterType,
				errorCode);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case FIELD_ERROR:
				FieldError fieldError = deserializeFieldError(
						(TLVParameterHeader) parameterHeader, data);
				ret.setFieldError(fieldError);
				break;
			case PARAMETER_ERROR:
				ParameterError parameterError = deserializeParameterError(
						(TLVParameterHeader) parameterHeader, data);
				ret.setParameterError(parameterError);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		return ret;
	}

	public void serialize(ParameterError rend, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = rend.getParameterHeader();
		header.setParameterLength(getLength(rend));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) rend.getParameterType());
		data.putShort((short) rend.getErrorCode().getValue());

		FieldError fieldError = rend.getFieldError();
		if (fieldError != null) {
			serialize(fieldError, data);
		}

		ParameterError parameterError = rend.getParameterError();
		if (parameterError != null) {
			serialize(parameterError, data);
		}
	}

	public int getLength(ParameterError rend) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		FieldError fieldError = rend.getFieldError();
		if (fieldError != null) {
			length += getLength(fieldError);
		}

		ParameterError parameterError = rend.getParameterError();
		if (parameterError != null) {
			length += getLength(parameterError);
		}
		return length;
	}

	/**
	 * Section 17.2.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public Custom deserializeCustom(TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		long vendorID = DataTypeConverter.uint(data.getInt());
		long subType = DataTypeConverter.uint(data.getInt());

		byte[] vpv = new byte[header.getParameterLength() - data.position()
				+ bufferStartPosition - TLV_PARAMETER_HEADER_LENGTH];
		data.get(vpv);
		return new Custom(header, vendorID, subType, vpv);
	}

	public void serialize(Custom cus, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = cus.getParameterHeader();
		header.setParameterLength(getLength(cus));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) cus.getVendorID());
		data.putInt((int) cus.getSubType());
		data.put(cus.getData());
	}

	public int getLength(Custom cus) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 8 + cus.getData().length;
		return length;
	}

	/**
	 * Section 17.3.1.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2LLRPCapabilities deserializeC1G2LLRPCapabilities(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		byte b = data.get();
		boolean canSupportBlockErase = (b & 0x80) > 0;
		boolean canSupportBlockWrite = (b & 0x40) > 0;
		boolean canSupportBlockPermalock = (b & 0x20) > 0;
		boolean canSupportTagRecommissioning = (b & 0x10) > 0;
		boolean canSupportUMIMethod2 = (b & 0x08) > 0;
		boolean canSupportXPC = (b & 0x04) > 0;
		int maxNumSelectFiltersPerQuery = DataTypeConverter.ushort(data
				.getShort());
		return new C1G2LLRPCapabilities(header, canSupportBlockErase,
				canSupportBlockWrite, canSupportBlockPermalock,
				canSupportTagRecommissioning, canSupportUMIMethod2,
				canSupportXPC, maxNumSelectFiltersPerQuery);
	}

	public void serialize(C1G2LLRPCapabilities c1g2LLRPCap, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2LLRPCap.getParameterHeader();
		header.setParameterLength(getLength(c1g2LLRPCap));
		// serialize parameter
		serialize(header, data);

		byte b = (byte) 0x00;
		if (c1g2LLRPCap.getCanSupportBlockErase()) {
			b = (byte) (b | 0x80);
		}
		if (c1g2LLRPCap.getCanSupportBlockWrite()) {
			b = (byte) (b | 0x40);
		}
		if (c1g2LLRPCap.getCanSupportBlockPermalock()) {
			b = (byte) (b | 0x20);
		}
		if (c1g2LLRPCap.getCanSupportTagRecommissioning()) {
			b = (byte) (b | 0x10);
		}
		if (c1g2LLRPCap.getCanSupportUMIMethod2()) {
			b = (byte) (b | 0x08);
		}
		if (c1g2LLRPCap.getCanSupportXPC()) {
			b = (byte) (b | 0x04);
		}
		data.put(b);
		data.putShort((short) c1g2LLRPCap.getMaxNumSelectFiltersPerQuery());
	}

	public int getLength(C1G2LLRPCapabilities c1g2LLRPCap) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 3;
		return length;
	}

	/**
	 * Section 17.3.1.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public UHFC1G2RFModeTable deserializeUHFC1G2RFModeTable(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		List<UHFC1G2RFModeTableEntry> uhfC1G2RFModeSet = new ArrayList<>();
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case UHF_C1G2_RF_MODE_TABLE_ENTRY:
			UHFC1G2RFModeTableEntry entry = deserializeUHFC1G2RFModeTableEntry(
					(TLVParameterHeader) parameterHeader, data);
			uhfC1G2RFModeSet.add(entry);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case UHF_C1G2_RF_MODE_TABLE_ENTRY:
				UHFC1G2RFModeTableEntry entry = deserializeUHFC1G2RFModeTableEntry(
						(TLVParameterHeader) parameterHeader, data);
				uhfC1G2RFModeSet.add(entry);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return new UHFC1G2RFModeTable(header, uhfC1G2RFModeSet);
	}

	public void serialize(UHFC1G2RFModeTable uhfC1G2RFMT, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = uhfC1G2RFMT.getParameterHeader();
		header.setParameterLength(getLength(uhfC1G2RFMT));
		// serialize parameter
		serialize(header, data);
		for (UHFC1G2RFModeTableEntry entry : uhfC1G2RFMT.getUhfC1G2RFModeSet()) {
			serialize(entry, data);
		}
	}

	public int getLength(UHFC1G2RFModeTable uhfC1G2RFMT) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 32
				* uhfC1G2RFMT.getUhfC1G2RFModeSet().size();
		return length;
	}

	/**
	 * Section 17.3.1.1.2.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public UHFC1G2RFModeTableEntry deserializeUHFC1G2RFModeTableEntry(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		long modeIdentifier = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		UHFC1G2RFModeTableEntryDivideRatio drValue = UHFC1G2RFModeTableEntryDivideRatio
				.get((b & 0x80) > 0);
		boolean epcHAGConformance = (b & 0x40) > 0;
		UHFC1G2RFModeTableEntryModulation mValue = UHFC1G2RFModeTableEntryModulation
				.get(DataTypeConverter.ubyte(data.get()));
		UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModukation = UHFC1G2RFModeTableEntryForwardLinkModulation
				.get(DataTypeConverter.ubyte(data.get()));
		UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator = UHFC1G2RFModeTableEntrySpectralMaskIndicator
				.get(DataTypeConverter.ubyte(data.get()));
		int bdrValue = data.getInt();
		int pieValue = data.getInt();
		int minTariValue = data.getInt();
		int maxTariValue = data.getInt();
		int stepTariValue = data.getInt();
		return new UHFC1G2RFModeTableEntry(header, modeIdentifier, drValue,
				epcHAGConformance, mValue, forwardLinkModukation,
				spectralMaskIndicator, bdrValue, pieValue, minTariValue,
				maxTariValue, stepTariValue);
	}

	public void serialize(UHFC1G2RFModeTableEntry uhfC1G2RFMTEntry,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = uhfC1G2RFMTEntry.getParameterHeader();
		header.setParameterLength(getLength(uhfC1G2RFMTEntry));
		// serialize parameter
		serialize(header, data);
		data.putInt((int) uhfC1G2RFMTEntry.getModeIdentifier());
		byte b = (byte) 0x00;
		if (uhfC1G2RFMTEntry.getDrValue().getValue()) {
			b = (byte) (b | 0x80);
		}
		if (uhfC1G2RFMTEntry.isEpcHAGConformance()) {
			b = (byte) (b | 0x40);
		}
		data.put(b);
		data.put((byte) uhfC1G2RFMTEntry.getmValue().getValue());
		data.put((byte) uhfC1G2RFMTEntry.getForwardLinkModulation().getValue());
		data.put((byte) uhfC1G2RFMTEntry.getSpectralMaskIndicator().getValue());
		data.putInt(uhfC1G2RFMTEntry.getBdrValue());
		data.putInt(uhfC1G2RFMTEntry.getPieValue());
		data.putInt(uhfC1G2RFMTEntry.getMinTariValue());
		data.putInt(uhfC1G2RFMTEntry.getMaxTariValue());
		data.putInt(uhfC1G2RFMTEntry.getStepTariValue());
	}

	public int getLength(UHFC1G2RFModeTableEntry uhfC1G2RFMTEntry) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 28;
		return length;
	}

	/**
	 * Section 17.3.1.2.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2InventoryCommand deserializeC1G2InventoryCommand(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		C1G2InventoryCommand c1g2InventoryCommand = null;
		List<C1G2Filter> c1g2FilterList = new ArrayList<>();
		List<Custom> cusList = new ArrayList<>();

		byte b = data.get();
		boolean tagInventorySA = (b & 0x80) > 0;
		c1g2InventoryCommand = new C1G2InventoryCommand(header, tagInventorySA);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);

			switch (parameterHeader.getParameterType()) {
			case C1G2_FILTER:
				C1G2Filter filter = deserializeC1G2Filter(
						(TLVParameterHeader) parameterHeader, data);
				c1g2FilterList.add(filter);
				break;
			case C1G2_RF_CONTROL:
				C1G2RFControl control = deserializeC1G2RFControl(
						(TLVParameterHeader) parameterHeader, data);
				c1g2InventoryCommand.setC1g2RFControl(control);
				break;
			case C1G2_SINGULATION_CONTROL:
				C1G2SingulationControl singulationControl = deserializeC1G2SingulationControl(
						(TLVParameterHeader) parameterHeader, data);
				c1g2InventoryCommand
						.setC1g2SingulationControl(singulationControl);
				break;
			case CUSTOM:
				Custom cus = deserializeCustom(
						(TLVParameterHeader) parameterHeader, data);
				cusList.add(cus);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}

		c1g2InventoryCommand.setC1g2FilterList(c1g2FilterList);
		c1g2InventoryCommand.setCusList(cusList);
		return c1g2InventoryCommand;
	}

	public void serialize(C1G2InventoryCommand c1g2InventoryCommand,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2InventoryCommand.getParameterHeader();
		header.setParameterLength(getLength(c1g2InventoryCommand));
		// serialize parameter
		serialize(header, data);

		byte b = (byte) 0x00;
		if (c1g2InventoryCommand.isTagInventStateAware()) {
			b = (byte) (b | 0x80);
		}
		data.put(b);

		if (c1g2InventoryCommand.getC1g2FilterList() != null
				&& !c1g2InventoryCommand.getC1g2FilterList().isEmpty()) {
			for (C1G2Filter filter : c1g2InventoryCommand.getC1g2FilterList()) {
				serialize(filter, data);
			}
		}

		C1G2RFControl control = c1g2InventoryCommand.getC1g2RFControl();
		if (control != null) {
			serialize(control, data);
		}

		C1G2SingulationControl singControl = c1g2InventoryCommand
				.getC1g2SingulationControl();
		if (singControl != null) {
			serialize(singControl, data);
		}

		if (c1g2InventoryCommand.getCusList() != null
				&& !c1g2InventoryCommand.getCusList().isEmpty()) {
			for (Custom cus : c1g2InventoryCommand.getCusList()) {
				serialize(cus, data);
			}
		}
	}

	public int getLength(C1G2InventoryCommand c1g2InventoryCommand) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;

		if (c1g2InventoryCommand.getC1g2FilterList() != null
				&& !c1g2InventoryCommand.getC1g2FilterList().isEmpty()) {
			for (C1G2Filter filter : c1g2InventoryCommand.getC1g2FilterList()) {
				length += getLength(filter);
			}
		}

		C1G2RFControl control = c1g2InventoryCommand.getC1g2RFControl();
		if (control != null) {
			length += getLength(control);
		}

		C1G2SingulationControl singControl = c1g2InventoryCommand
				.getC1g2SingulationControl();
		if (singControl != null) {
			length += getLength(singControl);
		}

		if (c1g2InventoryCommand.getCusList() != null
				&& !c1g2InventoryCommand.getCusList().isEmpty()) {
			for (Custom cus : c1g2InventoryCommand.getCusList()) {
				length += getLength(cus);
			}
		}

		return length;
	}

	/**
	 * Section 17.3.1.2.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2Filter deserializeC1G2Filter(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		C1G2Filter c1g2Filter = null;
		C1G2TagInventoryMask c1g2TIM = null;
		byte b = data.get();
		C1G2FilterTruncateAction t = C1G2FilterTruncateAction
				.get((byte) ((b & 0xFF) >> 6));

		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case C1G2_TAG_INVENTORY_MASK:
			c1g2TIM = deserializeC1G2TagInventoryMask(
					(TLVParameterHeader) parameterHeader, data);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		c1g2Filter = new C1G2Filter(header, t, c1g2TIM);

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION:
				C1G2TagInventoryStateAwareFilterAction c1g2TagInventoryStateAwareFilterAction = deserializeC1G2TagInventoryStateAwareFilterAction(
						(TLVParameterHeader) parameterHeader, data);
				c1g2Filter
						.setC1g2TagInventoryStateAwareFilterAction(c1g2TagInventoryStateAwareFilterAction);
				break;
			case C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION:
				C1G2TagInventoryStateUnawareFilterAction c1g2TagInventoryStateUnawareFilterAction = deserializeC1G2TagInventoryStateUnawareFilterAction(
						(TLVParameterHeader) parameterHeader, data);
				c1g2Filter
						.setC1g2TagInventoryStateUnawareFilterAction(c1g2TagInventoryStateUnawareFilterAction);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return c1g2Filter;
	}

	public void serialize(C1G2Filter c1g2Filter, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2Filter.getParameterHeader();
		header.setParameterLength(getLength(c1g2Filter));
		// serialize parameter
		serialize(header, data);
		byte t = c1g2Filter.getT().getValue();
		byte b = (byte) ((t & 0xFF) << 6);
		data.put(b);
		C1G2TagInventoryMask c1g2TIM = c1g2Filter.getC1g2TagInventoryMask();
		serialize(c1g2TIM, data);

		C1G2TagInventoryStateAwareFilterAction c1g2TagInventoryStateAwareFilterAction = c1g2Filter
				.getC1g2TagInventoryStateAwareFilterAction();
		if (c1g2TagInventoryStateAwareFilterAction != null) {
			serialize(c1g2TagInventoryStateAwareFilterAction, data);
		}
		C1G2TagInventoryStateUnawareFilterAction c1g2TagInventoryStateUnawareFilterAction = c1g2Filter
				.getC1g2TagInventoryStateUnawareFilterAction();
		if (c1g2TagInventoryStateUnawareFilterAction != null) {
			serialize(c1g2TagInventoryStateUnawareFilterAction, data);
		}
	}

	public int getLength(C1G2Filter c1g2Filter) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		length += getLength(c1g2Filter.getC1g2TagInventoryMask());
		C1G2TagInventoryStateAwareFilterAction c1g2TagInventoryStateAwareFilterAction = c1g2Filter
				.getC1g2TagInventoryStateAwareFilterAction();
		if (c1g2TagInventoryStateAwareFilterAction != null) {
			length += getLength(c1g2TagInventoryStateAwareFilterAction);
		}
		C1G2TagInventoryStateUnawareFilterAction c1g2TagInventoryStateUnawareFilterAction = c1g2Filter
				.getC1g2TagInventoryStateUnawareFilterAction();
		if (c1g2TagInventoryStateUnawareFilterAction != null) {
			length += getLength(c1g2TagInventoryStateUnawareFilterAction);
		}
		return length;
	}

	/**
	 * Section 17.3.1.2.1.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2TagInventoryMask deserializeC1G2TagInventoryMask(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2TagInventoryMask c1g2TagInventoryMask = null;
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int pointer = DataTypeConverter.ushort(data.getShort());
		int maskBitCount = DataTypeConverter.ushort(data.getShort());

		byte[] bytes;
		if (maskBitCount % 8 == 0) {
			bytes = new byte[maskBitCount / 8];
		} else {
			bytes = new byte[maskBitCount / 8 + 1];
		}
		data.get(bytes);
		BitSet tagMask = toBitSet(bytes);

		c1g2TagInventoryMask = new C1G2TagInventoryMask(header, mB, pointer,
				tagMask);
		c1g2TagInventoryMask.setMaskBitCount(bytes.length * 8);
		return c1g2TagInventoryMask;
	}

	public static BitSet toBitSet(byte[] bytes) {
		BitSet bits = new BitSet(bytes.length * 8);
		// for each bit
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[i / 8] & (0x80 >> (i % 8))) > 0) {
				bits.set(i);
			}
		}
		return bits;
	}

	public static byte reverseBits(byte b) {
		int in = (b & 0xFF);
		int out = 0;
		for (int i = 0; i < 8; i++) {
			int bit = in & 1;
			out = (out << 1) | bit;
			in >>= 1;
		}
		return (byte) out;
	}

	public void serialize(C1G2TagInventoryMask c1g2TagInventMask,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2TagInventMask.getParameterHeader();
		header.setParameterLength(getLength(c1g2TagInventMask));
		// serialize parameter
		serialize(header, data);

		byte mB = c1g2TagInventMask.getmB();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2TagInventMask.getPointer());
		BitSet tagMask = c1g2TagInventMask.getTagMask();

		int byteCount = c1g2TagInventMask.getMaskBitCount() / 8;
		if (c1g2TagInventMask.getMaskBitCount() % 8 != 0) {
			byteCount++;
		}
		int maskBitCount = byteCount * 8;
		c1g2TagInventMask.setMaskBitCount(maskBitCount);
		data.putShort((short) maskBitCount);
		if (maskBitCount > 0) {
			byte[] invertedBytes = tagMask.toByteArray();
			byte[] fullBytes = new byte[byteCount];
			for (int i = 0; i < invertedBytes.length; i++) {
				fullBytes[i] = reverseBits(invertedBytes[i]);
			}
			data.put(fullBytes);
		}
	}

	public int getLength(C1G2TagInventoryMask c1g2TagInventMask) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		int byteCount = c1g2TagInventMask.getMaskBitCount() / 8;
		if (c1g2TagInventMask.getMaskBitCount() % 8 != 0) {
			byteCount++;
		}
		length += byteCount;
		return length;
	}

	/**
	 * Section 17.3.1.2.1.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2TagInventoryStateAwareFilterAction deserializeC1G2TagInventoryStateAwareFilterAction(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		C1G2TagInventoryStateAwareFilterActionTarget target = C1G2TagInventoryStateAwareFilterActionTarget
				.get(DataTypeConverter.ubyte(data.get()));
		short action = DataTypeConverter.ubyte(data.get());
		return new C1G2TagInventoryStateAwareFilterAction(header, target,
				action);
	}

	public void serialize(
			C1G2TagInventoryStateAwareFilterAction c1g2TagInventSAFA,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2TagInventSAFA.getParameterHeader();
		header.setParameterLength(getLength(c1g2TagInventSAFA));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2TagInventSAFA.getTarget().getValue());
		data.put((byte) c1g2TagInventSAFA.getAction());
	}

	public int getLength(
			C1G2TagInventoryStateAwareFilterAction c1g2TagInventSAFA) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		return length;
	}

	/**
	 * Section 17.3.1.2.1.1.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2TagInventoryStateUnawareFilterAction deserializeC1G2TagInventoryStateUnawareFilterAction(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		C1G2TagInventoryStateUnawareFilterActionValues action = C1G2TagInventoryStateUnawareFilterActionValues
				.get(DataTypeConverter.ubyte(data.get()));
		return new C1G2TagInventoryStateUnawareFilterAction(header, action);
	}

	public void serialize(
			C1G2TagInventoryStateUnawareFilterAction c1g2TagInventSUFA,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2TagInventSUFA.getParameterHeader();
		header.setParameterLength(getLength(c1g2TagInventSUFA));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2TagInventSUFA.getAction().getValue());
	}

	public int getLength(
			C1G2TagInventoryStateUnawareFilterAction c1g2TagInventSUFA) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		return length;
	}

	/**
	 * Section 17.3.1.2.1.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2RFControl deserializeC1G2RFControl(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int modeIndex = DataTypeConverter.ushort(data.getShort());
		int tari = DataTypeConverter.ushort(data.getShort());
		return new C1G2RFControl(header, modeIndex, tari);
	}

	public void serialize(C1G2RFControl c1g2RFC, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2RFC.getParameterHeader();
		header.setParameterLength(getLength(c1g2RFC));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2RFC.getModelIndex());
		data.putShort((short) c1g2RFC.getTari());
	}

	public int getLength(C1G2RFControl c1g2RFC) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 4;
		return length;
	}

	/**
	 * Section 17.3.1.2.1.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2SingulationControl deserializeC1G2SingulationControl(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int bufferStartPosition = data.position();
		C1G2SingulationControl c1g2singCont = null;
		byte b = data.get();
		byte session = (byte) ((b & 0xFF) >> 6);
		int tagPop = DataTypeConverter.ushort(data.getShort());
		long tagTT = DataTypeConverter.uint(data.getInt());
		c1g2singCont = new C1G2SingulationControl(header, session, tagPop,
				tagTT);
		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			ParameterHeader parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION:
				C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction = deserializeC1G2TagInventoryStateAwareSingulationAction(
						(TLVParameterHeader) parameterHeader, data);
				c1g2singCont
						.setC1g2TagInventoryStateAwareSingulationAction(c1g2TagInventoryStateAwareSingulationAction);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return c1g2singCont;
	}

	public void serialize(C1G2SingulationControl c1g2singCont, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2singCont.getParameterHeader();
		header.setParameterLength(getLength(c1g2singCont));
		// serialize parameter
		serialize(header, data);
		byte session = c1g2singCont.getSession();
		byte b = (byte) ((session & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2singCont.getTagPopulation());
		data.putInt((int) c1g2singCont.getTagTransitTime());

		C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction = c1g2singCont
				.getC1g2TagInventoryStateAwareSingulationAction();
		if (c1g2TagInventoryStateAwareSingulationAction != null) {
			serialize(c1g2TagInventoryStateAwareSingulationAction, data);
		}
	}

	public int getLength(C1G2SingulationControl c1g2singCont) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 7;
		C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction = c1g2singCont
				.getC1g2TagInventoryStateAwareSingulationAction();
		if (c1g2TagInventoryStateAwareSingulationAction != null) {
			length += getLength(c1g2TagInventoryStateAwareSingulationAction);
		}
		return length;
	}

	/**
	 * Section 17.3.1.2.1.3.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2TagInventoryStateAwareSingulationAction deserializeC1G2TagInventoryStateAwareSingulationAction(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		byte b = data.get();
		C1G2TagInventoryStateAwareSingulationActionI i = C1G2TagInventoryStateAwareSingulationActionI
				.get((b & 0x80) > 0);
		C1G2TagInventoryStateAwareSingulationActionS s = C1G2TagInventoryStateAwareSingulationActionS
				.get((b & 0x40) > 0);
		C1G2TagInventoryStateAwareSingulationActionSAll a = C1G2TagInventoryStateAwareSingulationActionSAll
				.get((b & 0x20) > 0);
		return new C1G2TagInventoryStateAwareSingulationAction(header, i, s, a);
	}

	public void serialize(
			C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2TagInventoryStateAwareSingulationAction
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2TagInventoryStateAwareSingulationAction));
		// serialize parameter
		serialize(header, data);
		byte b = (byte) 0x00;
		if (c1g2TagInventoryStateAwareSingulationAction.getI().getValue()) {
			b = (byte) (b | 0x80);
		}
		if (c1g2TagInventoryStateAwareSingulationAction.getS().getValue()) {
			b = (byte) (b | 0x40);
		}
		if (c1g2TagInventoryStateAwareSingulationAction.getA().getValue()) {
			b = (byte) (b | 0x20);
		}
		data.put(b);
	}

	public int getLength(
			C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		return length;
	}

	/**
	 * Section 17.3.1.3.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2TagSpec deserializeC1G2TagSpec(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		C1G2TargetTag c1g2TargetTag = null;
		C1G2TargetTag c1g2TargetTag_optional = null;
		C1G2TagSpec c1g2TagSpec = null;
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case C1G2_TARGET_TAG:
			c1g2TargetTag = deserializeC1G2TargetTag(
					(TLVParameterHeader) parameterHeader, data);
			c1g2TagSpec = new C1G2TagSpec(header, c1g2TargetTag);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_TARGET_TAG:
				c1g2TargetTag_optional = deserializeC1G2TargetTag(
						(TLVParameterHeader) parameterHeader, data);
				c1g2TagSpec.setTagPattern2(c1g2TargetTag_optional);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return c1g2TagSpec;
	}

	public void serialize(C1G2TagSpec c1g2TagSpec, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2TagSpec.getParameterHeader();
		header.setParameterLength(getLength(c1g2TagSpec));
		// serialize parameter
		serialize(header, data);
		C1G2TargetTag c1g2TargetTag = c1g2TagSpec.getTagPattern1();
		serialize(c1g2TargetTag, data);
		C1G2TargetTag c1g2TargetTag_optional = c1g2TagSpec.getTagPattern2();
		if (c1g2TargetTag_optional != null) {
			serialize(c1g2TargetTag_optional, data);
		}
	}

	public int getLength(C1G2TagSpec c1g2TagSpec) {
		int length = TLV_PARAMETER_HEADER_LENGTH;
		length += getLength(c1g2TagSpec.getTagPattern1());
		C1G2TargetTag c1g2TargetTag_optional = c1g2TagSpec.getTagPattern2();
		if (c1g2TargetTag_optional != null) {
			length += getLength(c1g2TagSpec.getTagPattern2());
		}
		return length;
	}

	/**
	 * Section 17.3.1.3.1.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2TargetTag deserializeC1G2TargetTag(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		boolean match = (b & 0x20) > 0;
		int pointer = DataTypeConverter.ushort(data.getShort());
		int maskBitCount = DataTypeConverter.ushort(data.getShort());

		byte[] bytes;
		if (maskBitCount % 8 == 0) {
			bytes = new byte[maskBitCount / 8];
		} else {
			bytes = new byte[maskBitCount / 8 + 1];
		}
		data.get(bytes);
		BitSet tagMask = toBitSet(bytes);

		int dataBitCount = DataTypeConverter.ushort(data.getShort());
		byte[] dataBytes;
		if (dataBitCount % 8 == 0) {
			dataBytes = new byte[dataBitCount / 8];
		} else {
			dataBytes = new byte[dataBitCount / 8 + 1];
		}
		data.get(dataBytes);
		BitSet tagData = toBitSet(dataBytes);

		C1G2TargetTag c1g2TargetTag = new C1G2TargetTag(header, mB, match,
				pointer, tagMask, tagData);
		c1g2TargetTag.setMaskBitCount(bytes.length * 8);
		c1g2TargetTag.setDataBitCount(dataBytes.length * 8);

		return c1g2TargetTag;
	}

	public void serialize(C1G2TargetTag c1g2TargetTag, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2TargetTag.getParameterHeader();
		header.setParameterLength(getLength(c1g2TargetTag));
		// serialize parameter
		serialize(header, data);

		byte mB = c1g2TargetTag.getMemoryBank();
		byte b = (byte) ((mB & 0xFF) << 6);
		if (c1g2TargetTag.isMatch()) {
			b = (byte) (b | 0x20);
		}
		data.put(b);
		data.putShort((short) c1g2TargetTag.getPointer());

		BitSet tagMask = c1g2TargetTag.getTagMask();
		int byteCount = c1g2TargetTag.getMaskBitCount() / 8;
		if (c1g2TargetTag.getMaskBitCount() % 8 != 0) {
			byteCount++;
		}
		int maskBitCount = byteCount * 8;
		c1g2TargetTag.setMaskBitCount(maskBitCount);
		data.putShort((short) maskBitCount);
		if (maskBitCount > 0) {
			byte[] invertedBytes = tagMask.toByteArray();
			byte[] fullBytes = new byte[byteCount];
			for (int i = 0; i < invertedBytes.length; i++) {
				fullBytes[i] = reverseBits(invertedBytes[i]);
			}
			data.put(fullBytes);
		}

		BitSet tagData = c1g2TargetTag.getTagData();
		int dataByteCount = c1g2TargetTag.getDataBitCount() / 8;
		if (c1g2TargetTag.getDataBitCount() % 8 != 0) {
			dataByteCount++;
		}
		int dataBitCount = dataByteCount * 8;
		c1g2TargetTag.setDataBitCount(dataBitCount);
		data.putShort((short) dataBitCount);
		if (dataBitCount > 0) {
			byte[] invertedBytesTagData = tagData.toByteArray();
			byte[] fullBytesTagData = new byte[dataByteCount];
			for (int i = 0; i < invertedBytesTagData.length; i++) {
				fullBytesTagData[i] = reverseBits(invertedBytesTagData[i]);
			}
			data.put(fullBytesTagData);
		}
	}

	public int getLength(C1G2TargetTag c1g2TargetTag) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 7;
		int byteCount = c1g2TargetTag.getMaskBitCount() / 8;
		if (c1g2TargetTag.getMaskBitCount() % 8 != 0) {
			byteCount++;
		}
		length += byteCount;

		int byteCountTagData = c1g2TargetTag.getDataBitCount() / 8;
		if (c1g2TargetTag.getDataBitCount() % 8 != 0) {
			byteCountTagData++;
		}
		length += byteCountTagData;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2Read deserializeC1G2Read(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int wordPointer = DataTypeConverter.ushort(data.getShort());
		int wordCount = DataTypeConverter.ushort(data.getShort());
		return new C1G2Read(header, opSpecID, accessPW, mB, wordPointer,
				wordCount);
	}

	public void serialize(C1G2Read c1g2Read, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2Read.getParameterHeader();
		header.setParameterLength(getLength(c1g2Read));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2Read.getOpSpecId());
		data.putInt((int) c1g2Read.getAccessPw());
		byte mB = c1g2Read.getMemoryBank();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2Read.getWordPointer());
		data.putShort((short) c1g2Read.getWordCount());
	}

	public int getLength(C1G2Read c1g2Read) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 11;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2Write deserializeC1G2Write(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int wordPointer = DataTypeConverter.ushort(data.getShort());
		int writeDataWordCount = DataTypeConverter.ushort(data.getShort());
		byte[] writeData = new byte[writeDataWordCount * 2];
		data.get(writeData, 0, writeDataWordCount * 2);
		C1G2Write c1g2Write = new C1G2Write(header, opSpecID, accessPW, mB,
				wordPointer, writeData);
		c1g2Write.setWriteDataWordCount(writeData.length / 2);
		return c1g2Write;
	}

	public void serialize(C1G2Write c1g2Write, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2Write.getParameterHeader();
		header.setParameterLength(getLength(c1g2Write));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2Write.getOpSpecId());
		data.putInt((int) c1g2Write.getAccessPw());
		byte mB = c1g2Write.getMemoryBank();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2Write.getWordPointer());
		byte[] writeData = c1g2Write.getWriteData();

		int writeDataWordCount = writeData.length / 2;
		if (writeData.length % 2 != 0) {
			writeDataWordCount++;
		}
		c1g2Write.setWriteDataWordCount(writeDataWordCount);
		data.putShort((short) writeDataWordCount);
		data.put(writeData);
		if (writeData.length % 2 != 0) {
			data.put((byte) 0);
		}
	}

	public int getLength(C1G2Write c1g2Write) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 11;
		byte[] writeData = c1g2Write.getWriteData();
		int writeDataByteCount = writeData.length;
		if (writeData.length % 2 != 0) {
			writeDataByteCount++;
		}
		length += writeDataByteCount;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2Kill deserializeC1G2Kill(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long killPW = DataTypeConverter.uint(data.getInt());
		return new C1G2Kill(header, opSpecID, killPW);
	}

	public void serialize(C1G2Kill c1g2Kill, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2Kill.getParameterHeader();
		header.setParameterLength(getLength(c1g2Kill));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2Kill.getOpSpecId());
		data.putInt((int) c1g2Kill.getKillPw());
	}

	public int getLength(C1G2Kill c1g2Kill) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 6;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2Recommission deserializeC1G2Recommission(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long killPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		boolean lsb = (b & 0x01) > 0;
		boolean sb2 = (b & 0x02) > 0;
		boolean sb3 = (b & 0x04) > 0;
		return new C1G2Recommission(header, opSpecID, killPW, lsb, sb2, sb3);
	}

	public void serialize(C1G2Recommission c1g2Recommission, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2Recommission.getParameterHeader();
		header.setParameterLength(getLength(c1g2Recommission));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2Recommission.getOpSpecID());
		data.putInt((int) c1g2Recommission.getKillPW());
		byte b = (byte) 0x00;
		if (c1g2Recommission.isLsb()) {
			b = (byte) (b | 0x01);
		}
		if (c1g2Recommission.isSb2()) {
			b = (byte) (b | 0x02);
		}
		if (c1g2Recommission.isSb3()) {
			b = (byte) (b | 0x04);
		}
		data.put(b);
	}

	public int getLength(C1G2Recommission c1g2Recommission) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 7;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2Lock deserializeC1G2Lock(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int bufferStartPosition = data.position();
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		List<C1G2LockPayload> c1g2LockPayloadList = new ArrayList<>();
		ParameterHeader parameterHeader = deserializeParameterHeader(data);
		switch (parameterHeader.getParameterType()) {
		case C1G2_LOCK_PAYLOAD:
			C1G2LockPayload c1g2LP = deserializeC1G2LockPayload(
					(TLVParameterHeader) parameterHeader, data);
			c1g2LockPayloadList.add(c1g2LP);
			break;
		default:
			throw new InvalidParameterTypeException("Invalid parameter type "
					+ parameterHeader.getParameterType());
		}

		while (header.getParameterLength() > (data.position()
				- bufferStartPosition + TLV_PARAMETER_HEADER_LENGTH)) {
			parameterHeader = deserializeParameterHeader(data);
			switch (parameterHeader.getParameterType()) {
			case C1G2_LOCK_PAYLOAD:
				C1G2LockPayload c1g2LP = deserializeC1G2LockPayload(
						(TLVParameterHeader) parameterHeader, data);
				c1g2LockPayloadList.add(c1g2LP);
				break;
			default:
				throw new InvalidParameterTypeException(
						"Invalid parameter type "
								+ parameterHeader.getParameterType());
			}
		}
		return new C1G2Lock(header, opSpecID, accessPW, c1g2LockPayloadList);
	}

	public void serialize(C1G2Lock c1g2Lock, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2Lock.getParameterHeader();
		header.setParameterLength(getLength(c1g2Lock));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2Lock.getOpSpecId());
		data.putInt((int) c1g2Lock.getAccessPw());
		for (C1G2LockPayload c1g2LP : c1g2Lock.getC1g2LockPayloadList()) {
			serialize(c1g2LP, data);
		}
	}

	public int getLength(C1G2Lock c1g2Lock) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 6 + 6
				* c1g2Lock.getC1g2LockPayloadList().size();
		return length;
	}

	/**
	 * Section 17.3.1.3.2.5.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2LockPayload deserializeC1G2LockPayload(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {

		short privilege = DataTypeConverter.ubyte(data.get());
		short dataField = DataTypeConverter.ubyte(data.get());

		return new C1G2LockPayload(header,
				C1G2LockPayloadPrivilege.get(privilege),
				C1G2LockPayloadDataField.get(dataField));
	}

	public void serialize(C1G2LockPayload c1g2LockPayload, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2LockPayload.getParameterHeader();
		header.setParameterLength(getLength(c1g2LockPayload));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2LockPayload.getPrivilege().getValue());
		data.put((byte) c1g2LockPayload.getDataField().getValue());
	}

	public int getLength(C1G2LockPayload c1g2LockPayload) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 2;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2BlockErase deserializeC1G2BlockErase(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int wordPointer = DataTypeConverter.ushort(data.getShort());
		int wordCount = DataTypeConverter.ushort(data.getShort());
		return new C1G2BlockErase(header, opSpecID, accessPW, mB, wordPointer,
				wordCount);
	}

	public void serialize(C1G2BlockErase c1g2BlockErase, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2BlockErase.getParameterHeader();
		header.setParameterLength(getLength(c1g2BlockErase));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2BlockErase.getOpSpecID());
		data.putInt((int) c1g2BlockErase.getAccessPW());
		byte mB = c1g2BlockErase.getmB();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2BlockErase.getWordPointer());
		data.putShort((short) c1g2BlockErase.getWordCount());
	}

	public int getLength(C1G2BlockErase c1g2BlockErase) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 11;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.7
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2BlockWrite deserializeC1G2BlockWrite(TLVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int wordPointer = DataTypeConverter.ushort(data.getShort());
		int writeDataWordCount = DataTypeConverter.ushort(data.getShort());
		byte[] writeData = new byte[writeDataWordCount * 2];
		data.get(writeData, 0, writeDataWordCount * 2);
		C1G2BlockWrite c1g2BlockWrite = new C1G2BlockWrite(header, opSpecID,
				accessPW, mB, wordPointer, writeData);
		c1g2BlockWrite.setWriteDataWordCount(writeData.length / 2);
		return c1g2BlockWrite;
	}

	public void serialize(C1G2BlockWrite c1g2BlockWrite, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2BlockWrite.getParameterHeader();
		header.setParameterLength(getLength(c1g2BlockWrite));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2BlockWrite.getOpSpecID());
		data.putInt((int) c1g2BlockWrite.getAccessPW());
		byte mB = c1g2BlockWrite.getmB();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2BlockWrite.getWordPointer());
		byte[] writeData = c1g2BlockWrite.getWriteData();

		int writeDataWordCount = writeData.length / 2;
		if (writeData.length % 2 != 0) {
			writeDataWordCount++;
		}
		c1g2BlockWrite.setWriteDataWordCount(writeDataWordCount);
		data.putShort((short) writeDataWordCount);
		data.put(writeData);
		if (writeData.length % 2 != 0) {
			data.put((byte) 0);
		}
	}

	public int getLength(C1G2BlockWrite c1g2BlockWrite) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 11;
		byte[] writeData = c1g2BlockWrite.getWriteData();
		int writeDataByteCount = writeData.length;
		if (writeData.length % 2 != 0) {
			writeDataByteCount++;
		}
		length += writeDataByteCount;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.8
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2BlockPermalock deserializeC1G2BlockPermalock(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int blockPointer = DataTypeConverter.ushort(data.getShort());
		int blockMaskWordCount = DataTypeConverter.ushort(data.getShort());
		byte[] blockMask = new byte[blockMaskWordCount * 2];
		data.get(blockMask, 0, blockMaskWordCount * 2);
		C1G2BlockPermalock c1g2BlockPermalock = new C1G2BlockPermalock(header,
				opSpecID, accessPW, mB, blockPointer, blockMask);
		c1g2BlockPermalock.setBlockMaskWordCount(blockMask.length / 2);
		return c1g2BlockPermalock;
	}

	public void serialize(C1G2BlockPermalock c1g2BlockPermalock, ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2BlockPermalock.getParameterHeader();
		header.setParameterLength(getLength(c1g2BlockPermalock));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2BlockPermalock.getOpSpecID());
		data.putInt((int) c1g2BlockPermalock.getAccessPW());
		byte mB = c1g2BlockPermalock.getmB();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2BlockPermalock.getBlockPointer());
		byte[] blockMask = c1g2BlockPermalock.getBlockMask();

		int blockMaskWordCount = blockMask.length / 2;
		if (blockMask.length % 2 != 0) {
			blockMaskWordCount++;
		}
		c1g2BlockPermalock.setBlockMaskWordCount(blockMaskWordCount);
		data.putShort((short) blockMaskWordCount);
		data.put(blockMask);
		if (blockMask.length % 2 != 0) {
			data.put((byte) 0);
		}
	}

	public int getLength(C1G2BlockPermalock c1g2BlockPermalock) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 11;
		byte[] blockMask = c1g2BlockPermalock.getBlockMask();
		int blockMaskByteCount = blockMask.length;
		if (blockMask.length % 2 != 0) {
			blockMaskByteCount++;
		}
		length += blockMaskByteCount;
		return length;
	}

	/**
	 * Section 17.3.1.3.2.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2GetBlockPermalockStatus deserializeC1G2GetBlockPermalockStatus(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		long accessPW = DataTypeConverter.uint(data.getInt());
		byte b = data.get();
		byte mB = (byte) ((b & 0xFF) >> 6);
		int blockPointer = DataTypeConverter.ushort(data.getShort());
		int blockRange = DataTypeConverter.ushort(data.getShort());
		return new C1G2GetBlockPermalockStatus(header, opSpecID, accessPW, mB,
				blockPointer, blockRange);
	}

	public void serialize(
			C1G2GetBlockPermalockStatus c1g2GetBlockPermalockStatus,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2GetBlockPermalockStatus
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2GetBlockPermalockStatus));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2GetBlockPermalockStatus.getOpSpecID());
		data.putInt((int) c1g2GetBlockPermalockStatus.getAccessPW());
		byte mB = c1g2GetBlockPermalockStatus.getmB();
		byte b = (byte) ((mB & 0xFF) << 6);
		data.put(b);
		data.putShort((short) c1g2GetBlockPermalockStatus.getBlockPointer());
		data.putShort((short) c1g2GetBlockPermalockStatus.getBlockRange());
	}

	public int getLength(C1G2GetBlockPermalockStatus c1g2GetBlockPermalockStatus) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 11;
		return length;
	}

	/**
	 * Section 17.3.1.5.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2EPCMemorySelector deserializeC1G2EPCMemorySelector(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		byte b = data.get();
		boolean c = (b & 0x80) > 0;
		boolean p = (b & 0x40) > 0;
		boolean x = (b & 0x20) > 0;
		return new C1G2EPCMemorySelector(header, c, p, x);
	}

	public void serialize(C1G2EPCMemorySelector c1g2EPCMemorySelector,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2EPCMemorySelector.getParameterHeader();
		header.setParameterLength(getLength(c1g2EPCMemorySelector));
		// serialize parameter
		serialize(header, data);
		byte b = (byte) 0x00;
		if (c1g2EPCMemorySelector.isEnableCRC()) {
			b = (byte) (b | 0x80);
		}
		if (c1g2EPCMemorySelector.isEnablePCBits()) {
			b = (byte) (b | 0x40);
		}
		if (c1g2EPCMemorySelector.isEnableXPCBits()) {
			b = (byte) (b | 0x20);
		}
		data.put(b);
	}

	public int getLength(C1G2EPCMemorySelector c1g2EPCMemorySelector) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 1;
		return length;
	}

	/**
	 * Section 17.3.1.5.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2PC deserializeC1G2PC(TVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int pc = DataTypeConverter.ushort(data.getShort());
		return new C1G2PC(header, pc);
	}

	public void serialize(C1G2PC c1g2PC, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = c1g2PC.getParameterHeader();
		header.setParameterLength(getLength(c1g2PC));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2PC.getPcBits());
	}

	public int getLength(C1G2PC c1g2PC) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.3.1.5.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2XPCW1 deserializeC1G2XPCW1(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int xpcW1 = DataTypeConverter.ushort(data.getShort());
		return new C1G2XPCW1(header, xpcW1);
	}

	public void serialize(C1G2XPCW1 c1g2XPCW1, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = c1g2XPCW1.getParameterHeader();
		header.setParameterLength(getLength(c1g2XPCW1));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2XPCW1.getXpcW1());
	}

	public int getLength(C1G2XPCW1 c1g2XPCW1) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.3.1.5.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2XPCW2 deserializeC1G2XPCW2(TVParameterHeader header,
			ByteBuffer data) throws InvalidParameterTypeException {
		// deserialize parameter
		int xpcW2 = DataTypeConverter.ushort(data.getShort());
		return new C1G2XPCW2(header, xpcW2);
	}

	public void serialize(C1G2XPCW2 c1g2XPCW2, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = c1g2XPCW2.getParameterHeader();
		header.setParameterLength(getLength(c1g2XPCW2));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2XPCW2.getXpcW2());
	}

	public int getLength(C1G2XPCW2 c1g2XPCW2) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.3.1.5.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2CRC deserializeC1G2CRC(TVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int crc = DataTypeConverter.ushort(data.getShort());
		return new C1G2CRC(header, crc);
	}

	public void serialize(C1G2CRC c1g2CRC, ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = c1g2CRC.getParameterHeader();
		header.setParameterLength(getLength(c1g2CRC));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2CRC.getCrc());
	}

	public int getLength(C1G2CRC c1g2CRC) {
		return TV_PARAMETER_HEADER_LENGTH + 2;
	}

	/**
	 * Section 17.3.1.5.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2SingulationDetails deserializeC1G2SingulationDetails(
			TVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		// deserialize parameter
		int numCollisionSlots = DataTypeConverter.ushort(data.getShort());
		int numEmptySlots = DataTypeConverter.ushort(data.getShort());
		return new C1G2SingulationDetails(header, numCollisionSlots,
				numEmptySlots);
	}

	public void serialize(C1G2SingulationDetails c1g2SingulationDetails,
			ByteBuffer data) {
		// set parameter length
		TVParameterHeader header = c1g2SingulationDetails.getParameterHeader();
		header.setParameterLength(getLength(c1g2SingulationDetails));
		// serialize parameter
		serialize(header, data);
		data.putShort((short) c1g2SingulationDetails.getNumCollisionSlots());
		data.putShort((short) c1g2SingulationDetails.getNumEmptySlots());
	}

	public int getLength(C1G2SingulationDetails c1g2SingulationDetails) {
		return TV_PARAMETER_HEADER_LENGTH + 4;
	}

	/**
	 * Section 17.3.1.5.7.1
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2ReadOpSpecResult deserializeC1G2ReadOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2ReadOpSpecResultValues result = C1G2ReadOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		int readDataWordCount = DataTypeConverter.ushort(data.getShort());
		byte[] readData = new byte[readDataWordCount * 2];
		data.get(readData, 0, readDataWordCount * 2);
		C1G2ReadOpSpecResult c1g2ReadOpSpecResult = new C1G2ReadOpSpecResult(
				header, result, opSpecID, readData);
		c1g2ReadOpSpecResult.setReadDataWordCount(readData.length / 2);
		return c1g2ReadOpSpecResult;
	}

	public void serialize(C1G2ReadOpSpecResult c1g2ReadOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2ReadOpSpecResult.getParameterHeader();
		header.setParameterLength(getLength(c1g2ReadOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2ReadOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2ReadOpSpecResult.getOpSpecID());
		byte[] readData = c1g2ReadOpSpecResult.getReadData();
		int readDataWordCount = 0;
		if (readData != null) {
			readDataWordCount = readData.length / 2;
			if (readData.length % 2 != 0) {
				readDataWordCount++;
			}
		}
		c1g2ReadOpSpecResult.setReadDataWordCount(readDataWordCount);
		data.putShort((short) readDataWordCount);
		if (readData != null) {
			data.put(readData);
			if (readData.length % 2 != 0) {
				data.put((byte) 0);
			}
		}
	}

	public int getLength(C1G2ReadOpSpecResult c1g2ReadOpSpecResult) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		byte[] readData = c1g2ReadOpSpecResult.getReadData();
		if (readData != null) {
			int readDatabyteCount = readData.length;
			if (readData.length % 2 != 0) {
				readDatabyteCount++;
			}
			length += readDatabyteCount;
		}
		return length;
	}

	/**
	 * Section 17.3.1.5.7.2
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2WriteOpSpecResult deserializeC1G2WriteOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2WriteOpSpecResultValues result = C1G2WriteOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		int numWordsWritten = DataTypeConverter.ushort(data.getShort());
		return new C1G2WriteOpSpecResult(header, result, opSpecID,
				numWordsWritten);
	}

	public void serialize(C1G2WriteOpSpecResult c1g2WriteOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2WriteOpSpecResult.getParameterHeader();
		header.setParameterLength(getLength(c1g2WriteOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2WriteOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2WriteOpSpecResult.getOpSpecID());
		data.putShort((short) c1g2WriteOpSpecResult.getNumWordsWritten());
	}

	public int getLength(C1G2WriteOpSpecResult c1g2WriteOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 5;
	}

	/**
	 * Section 17.3.1.5.7.3
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2KillOpSpecResult deserializeC1G2KillOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2KillOpSpecResultValues result = C1G2KillOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());

		return new C1G2KillOpSpecResult(header, result, opSpecID);
	}

	public void serialize(C1G2KillOpSpecResult c1g2KillOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2KillOpSpecResult.getParameterHeader();
		header.setParameterLength(getLength(c1g2KillOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2KillOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2KillOpSpecResult.getOpSpecID());
	}

	public int getLength(C1G2KillOpSpecResult c1g2KillOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.3.1.5.7.4
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2RecommissionOpSpecResult deserializeC1G2RecommissionOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2RecommissionOpSpecResultValues result = C1G2RecommissionOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());

		return new C1G2RecommissionOpSpecResult(header, result, opSpecID);
	}

	public void serialize(
			C1G2RecommissionOpSpecResult c1g2RecommissionOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2RecommissionOpSpecResult
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2RecommissionOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2RecommissionOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2RecommissionOpSpecResult.getOpSpecID());
	}

	public int getLength(
			C1G2RecommissionOpSpecResult c1g2RecommissionOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.3.1.5.7.5
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2LockOpSpecResult deserializeC1G2LockOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2LockOpSpecResultValues result = C1G2LockOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());

		return new C1G2LockOpSpecResult(header, result, opSpecID);
	}

	public void serialize(C1G2LockOpSpecResult c1g2LockOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2LockOpSpecResult.getParameterHeader();
		header.setParameterLength(getLength(c1g2LockOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2LockOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2LockOpSpecResult.getOpSpecID());
	}

	public int getLength(C1G2LockOpSpecResult c1g2LockOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.3.1.5.7.6
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2BlockEraseOpSpecResult deserializeC1G2BlockEraseOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2BlockEraseOpSpecResultValues result = C1G2BlockEraseOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());

		return new C1G2BlockEraseOpSpecResult(header, result, opSpecID);
	}

	public void serialize(
			C1G2BlockEraseOpSpecResult c1g2BlockEraseOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2BlockEraseOpSpecResult
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2BlockEraseOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2BlockEraseOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2BlockEraseOpSpecResult.getOpSpecID());
	}

	public int getLength(C1G2BlockEraseOpSpecResult c1g2BlockEraseOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.3.1.5.7.7
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2BlockWriteOpSpecResult deserializeC1G2BlockWriteOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2BlockWriteOpSpecResultValues result = C1G2BlockWriteOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		int numWordsWritten = DataTypeConverter.ushort(data.getShort());
		return new C1G2BlockWriteOpSpecResult(header, result, opSpecID,
				numWordsWritten);
	}

	public void serialize(
			C1G2BlockWriteOpSpecResult c1g2BlockWriteOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2BlockWriteOpSpecResult
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2BlockWriteOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2BlockWriteOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2BlockWriteOpSpecResult.getOpSpecID());
		data.putShort((short) c1g2BlockWriteOpSpecResult.getNumWordsWritten());
	}

	public int getLength(C1G2BlockWriteOpSpecResult c1g2BlockWriteOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 5;
	}

	/**
	 * Section 17.3.1.5.7.8
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 * @throws DeserializationException
	 */
	public C1G2BlockPermalockOpSpecResult deserializeC1G2BlockPermalockOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2BlockPermalockOpSpecResultValues result = C1G2BlockPermalockOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());

		return new C1G2BlockPermalockOpSpecResult(header, result, opSpecID);
	}

	public void serialize(
			C1G2BlockPermalockOpSpecResult c1g2BlockPermalockOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2BlockPermalockOpSpecResult
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2BlockPermalockOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2BlockPermalockOpSpecResult.getResult().getValue());
		data.putShort((short) c1g2BlockPermalockOpSpecResult.getOpSpecID());
	}

	public int getLength(
			C1G2BlockPermalockOpSpecResult c1g2BlockPermalockOpSpecResult) {
		return TLV_PARAMETER_HEADER_LENGTH + 3;
	}

	/**
	 * Section 17.3.1.5.7.9
	 * 
	 * @param header
	 * @param data
	 * @return The instance
	 */
	public C1G2GetBlockPermalockStatusOpSpecResult deserializeC1G2GetBlockPermalockStatusOpSpecResult(
			TLVParameterHeader header, ByteBuffer data)
			throws InvalidParameterTypeException {
		C1G2GetBlockPermalockStatusOpSpecResultValues result = C1G2GetBlockPermalockStatusOpSpecResultValues
				.get(DataTypeConverter.ubyte(data.get()));
		int opSpecID = DataTypeConverter.ushort(data.getShort());
		int statusWordCount = DataTypeConverter.ushort(data.getShort());
		byte[] permalockStatus = new byte[statusWordCount * 2];
		data.get(permalockStatus, 0, statusWordCount * 2);
		C1G2GetBlockPermalockStatusOpSpecResult c1g2GetBlockPermalockStatusOpSpecResult = new C1G2GetBlockPermalockStatusOpSpecResult(
				header, result, opSpecID, permalockStatus);
		c1g2GetBlockPermalockStatusOpSpecResult
				.setStatusWordCount(permalockStatus.length / 2);
		return c1g2GetBlockPermalockStatusOpSpecResult;
	}

	public void serialize(
			C1G2GetBlockPermalockStatusOpSpecResult c1g2GetBlockPermalockStatusOpSpecResult,
			ByteBuffer data) {
		// set parameter length
		TLVParameterHeader header = c1g2GetBlockPermalockStatusOpSpecResult
				.getParameterHeader();
		header.setParameterLength(getLength(c1g2GetBlockPermalockStatusOpSpecResult));
		// serialize parameter
		serialize(header, data);
		data.put((byte) c1g2GetBlockPermalockStatusOpSpecResult.getResult()
				.getValue());
		data.putShort((short) c1g2GetBlockPermalockStatusOpSpecResult
				.getOpSpecID());
		byte[] permalockStatus = c1g2GetBlockPermalockStatusOpSpecResult
				.getPermalockStatus();
		int statusWordCount = permalockStatus.length / 2;
		if (permalockStatus.length % 2 != 0) {
			statusWordCount++;
		}
		c1g2GetBlockPermalockStatusOpSpecResult
				.setStatusWordCount(statusWordCount);
		data.putShort((short) statusWordCount);
		data.put(permalockStatus);
		if (permalockStatus.length % 2 != 0) {
			data.put((byte) 0);
		}
	}

	public int getLength(
			C1G2GetBlockPermalockStatusOpSpecResult c1g2GetBlockPermalockStatusOpSpecResult) {
		int length = TLV_PARAMETER_HEADER_LENGTH + 5;
		byte[] permalockStatus = c1g2GetBlockPermalockStatusOpSpecResult
				.getPermalockStatus();
		int statusbyteCount = permalockStatus.length;
		if (permalockStatus.length % 2 != 0) {
			statusbyteCount++;
		}
		length += statusbyteCount;
		return length;
	}
}
