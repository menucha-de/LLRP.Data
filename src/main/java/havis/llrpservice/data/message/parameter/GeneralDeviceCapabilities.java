package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class GeneralDeviceCapabilities extends Parameter {

	private static final long serialVersionUID = 5335824442711582733L;

	private TLVParameterHeader parameterHeader;
	private long deviceManufacturerName;
	private long modelName;
	private String firmwareVersion;
	private int maxNumberOfAntennaSupported;
	private boolean canSetAntennaProperties;
	private boolean hasUTCClockCapability;
	private MaximumReceiveSensitivity maximumReceiveSensitivity;
	private GPIOCapabilities gpioCapabilities;
	private List<ReceiveSensitivityTabelEntry> receiveSensitivityTableEntry;
	private List<PerAntennaReceiveSensitivityRange> perAntennaReceiveSensitivityRange;
	private List<PerAntennaAirProtocol> perAntennaAirProtocol;
	private int firmwareVersionByteCount;

	public GeneralDeviceCapabilities() {
	}

	public GeneralDeviceCapabilities(TLVParameterHeader header, long deviceManufacturerName, long modelName, String firmwareVersion,
			int maximumNumberOfAntennasSupported, boolean canSetAntennaProperties, List<ReceiveSensitivityTabelEntry> receiveSensitivityTable,
			List<PerAntennaAirProtocol> airProtocolSupportedPerAntenna, GPIOCapabilities gpioSupport, boolean hasUTCClockCapability) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.GENERAL_DEVICE_CAPABILITIES);
		this.deviceManufacturerName = deviceManufacturerName;
		this.modelName = modelName;
		this.firmwareVersion = firmwareVersion;
		this.maxNumberOfAntennaSupported = maximumNumberOfAntennasSupported;
		this.canSetAntennaProperties = canSetAntennaProperties;
		this.receiveSensitivityTableEntry = receiveSensitivityTable;
		this.perAntennaAirProtocol = airProtocolSupportedPerAntenna;
		this.gpioCapabilities = gpioSupport;
		this.hasUTCClockCapability = hasUTCClockCapability;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getDeviceManufacturerName() {
		return deviceManufacturerName;
	}

	public void setDeviceManufacturerName(long deviceManufacturerName) {
		this.deviceManufacturerName = deviceManufacturerName;
	}

	public long getModelName() {
		return modelName;
	}

	public void setModelName(long modelName) {
		this.modelName = modelName;
	}

	public int getFirmwareVersionByteCount() {
		return firmwareVersionByteCount;
	}

	public void setFirmwareVersionByteCount(int firmwareVersionByteCount) {
		this.firmwareVersionByteCount = firmwareVersionByteCount;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public int getMaxNumberOfAntennasSupported() {
		return maxNumberOfAntennaSupported;
	}

	public void setMaxNumberOfAntennasSupported(int maxNumberOfAntennasSupported) {
		this.maxNumberOfAntennaSupported = maxNumberOfAntennasSupported;
	}

	public boolean isCanSetAntennaProperties() {
		return canSetAntennaProperties;
	}

	public void setCanSetAntennaProperties(boolean canSetAntennaProperties) {
		this.canSetAntennaProperties = canSetAntennaProperties;
	}

	public boolean isHasUTCClockCapability() {
		return hasUTCClockCapability;
	}

	public void setHasUTCClockCapability(boolean hasUTCClockCapability) {
		this.hasUTCClockCapability = hasUTCClockCapability;
	}

	public MaximumReceiveSensitivity getMaximumReceiveSensitivity() {
		return maximumReceiveSensitivity;
	}

	public void setMaximumReceiveSensitivity(MaximumReceiveSensitivity maximumReceiveSensitivity) {
		this.maximumReceiveSensitivity = maximumReceiveSensitivity;
	}

	public GPIOCapabilities getGpioCapabilities() {
		return gpioCapabilities;
	}

	public void setGpioCapabilities(GPIOCapabilities gpioSupport) {
		this.gpioCapabilities = gpioSupport;
	}

	public List<ReceiveSensitivityTabelEntry> getReceiveSensitivityTableEntry() {
		return receiveSensitivityTableEntry;
	}

	public void setReceiveSensitivityTableEntry(List<ReceiveSensitivityTabelEntry> receiveSensitivityTableEntry) {
		this.receiveSensitivityTableEntry = receiveSensitivityTableEntry;
	}

	public List<PerAntennaReceiveSensitivityRange> getPerAntennaReceiveSensitivityRange() {
		return perAntennaReceiveSensitivityRange;
	}

	public void setPerAntennaReceiveSensitivityRange(List<PerAntennaReceiveSensitivityRange> perAntennaReceiveSensitivityRange) {
		this.perAntennaReceiveSensitivityRange = perAntennaReceiveSensitivityRange;
	}

	public List<PerAntennaAirProtocol> getPerAntennaAirProtocol() {
		return perAntennaAirProtocol;
	}

	public void setPerAntennaAirProtocol(List<PerAntennaAirProtocol> perAntennaAirProtocol) {
		this.perAntennaAirProtocol = perAntennaAirProtocol;
	}

	@Override
	public String toString() {
		return "GeneralDeviceCapabilities [parameterHeader=" + parameterHeader + ", deviceManufacturerName=" + deviceManufacturerName + ", modelName="
				+ modelName + ", firmwareVersion=" + firmwareVersion + ", maxNumberOfAntennaSupported=" + maxNumberOfAntennaSupported
				+ ", canSetAntennaProperties=" + canSetAntennaProperties + ", hasUTCClockCapability=" + hasUTCClockCapability + ", maximumReceiveSensitivity="
				+ maximumReceiveSensitivity + ", gpioCapabilities=" + gpioCapabilities + ", receiveSensitivityTableEntry=" + receiveSensitivityTableEntry
				+ ", perAntennaReceiveSensitivityRange=" + perAntennaReceiveSensitivityRange + ", perAntennaAirProtocol=" + perAntennaAirProtocol
				+ ", firmwareVersionByteCount=" + firmwareVersionByteCount + "]";
	}
}