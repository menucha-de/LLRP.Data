package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class TagReportContentSelector extends Parameter {

	private static final long serialVersionUID = -611988937581181265L;

	private TLVParameterHeader parameterHeader;
	private boolean enableROSpecID;
	private boolean enableSpecIndex;
	private boolean enableInventoryParameterSpecID;
	private boolean enableAntennaID;
	private boolean enableChannelIndex;
	private boolean enablePeakRSSI;
	private boolean enableFirstSeenTimestamp;
	private boolean enableLastSeenTimestamp;
	private boolean enableTagSeenCount;
	private boolean enableAccessSpecID;
	private List<C1G2EPCMemorySelector> c1g2EPCMemorySelectorList;
	private List<Custom> cusList;

	public TagReportContentSelector() {
	}

	public TagReportContentSelector(TLVParameterHeader header, boolean enableROSpecID, boolean enableSpecIndex, boolean enableInventoryParameterSpecID,
			boolean enableAntennaID, boolean enableChannelIndex, boolean enablePeakRSSI, boolean enableFirstSeenTimestamp, boolean enableLastSeenTimestamp,
			boolean enableTagSeenCount, boolean enableAccessSpecID) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.TAG_REPORT_CONTENT_SELECTOR);
		this.enableROSpecID = enableROSpecID;
		this.enableSpecIndex = enableSpecIndex;
		this.enableInventoryParameterSpecID = enableInventoryParameterSpecID;
		this.enableAntennaID = enableAntennaID;
		this.enableChannelIndex = enableChannelIndex;
		this.enablePeakRSSI = enablePeakRSSI;
		this.enableFirstSeenTimestamp = enableFirstSeenTimestamp;
		this.enableLastSeenTimestamp = enableLastSeenTimestamp;
		this.enableTagSeenCount = enableTagSeenCount;
		this.enableAccessSpecID = enableAccessSpecID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public boolean isEnableROSpecID() {
		return enableROSpecID;
	}

	public void setEnableROSpecID(boolean enableROSpecID) {
		this.enableROSpecID = enableROSpecID;
	}

	public boolean isEnableSpecIndex() {
		return enableSpecIndex;
	}

	public void setEnableSpecIndex(boolean enableSpecIndex) {
		this.enableSpecIndex = enableSpecIndex;
	}

	public boolean isEnableInventoryParameterSpecID() {
		return enableInventoryParameterSpecID;
	}

	public void setEnableInventoryParameterSpecID(boolean enableInventoryParameterSpecID) {
		this.enableInventoryParameterSpecID = enableInventoryParameterSpecID;
	}

	public boolean isEnableAntennaID() {
		return enableAntennaID;
	}

	public void setEnableAntennaID(boolean enableAntennaID) {
		this.enableAntennaID = enableAntennaID;
	}

	public boolean isEnableChannelIndex() {
		return enableChannelIndex;
	}

	public void setEnableChannelIndex(boolean enableChannelIndex) {
		this.enableChannelIndex = enableChannelIndex;
	}

	public boolean isEnablePeakRSSI() {
		return enablePeakRSSI;
	}

	public void setEnablePeakRSSI(boolean enablePeakRSSI) {
		this.enablePeakRSSI = enablePeakRSSI;
	}

	public boolean isEnableFirstSeenTimestamp() {
		return enableFirstSeenTimestamp;
	}

	public void setEnableFirstSeenTimestamp(boolean enableFirstSeenTimestamp) {
		this.enableFirstSeenTimestamp = enableFirstSeenTimestamp;
	}

	public boolean isEnableLastSeenTimestamp() {
		return enableLastSeenTimestamp;
	}

	public void setEnableLastSeenTimestamp(boolean enableLastSeenTimestamp) {
		this.enableLastSeenTimestamp = enableLastSeenTimestamp;
	}

	public boolean isEnableTagSeenCount() {
		return enableTagSeenCount;
	}

	public void setEnableTagSeenCount(boolean enableTagSeenCount) {
		this.enableTagSeenCount = enableTagSeenCount;
	}

	public boolean isEnableAccessSpecID() {
		return enableAccessSpecID;
	}

	public void setEnableAccessSpecID(boolean enableAccessSpecID) {
		this.enableAccessSpecID = enableAccessSpecID;
	}

	public List<C1G2EPCMemorySelector> getC1g2EPCMemorySelectorList() {
		return c1g2EPCMemorySelectorList;
	}

	public void setC1g2EPCMemorySelectorList(List<C1G2EPCMemorySelector> c1g2epcMemorySelectorList) {
		c1g2EPCMemorySelectorList = c1g2epcMemorySelectorList;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "TagReportContentSelector [parameterHeader=" + parameterHeader + ", enableROSpecID=" + enableROSpecID + ", enableSpecIndex=" + enableSpecIndex
				+ ", enableInventoryParameterSpecID=" + enableInventoryParameterSpecID + ", enableAntennaID=" + enableAntennaID + ", enableChannelIndex="
				+ enableChannelIndex + ", enablePeakRSSI=" + enablePeakRSSI + ", enableFirstSeenTimestamp=" + enableFirstSeenTimestamp
				+ ", enableLastSeenTimestamp=" + enableLastSeenTimestamp + ", enableTagSeenCount=" + enableTagSeenCount + ", enableAccessSpecID="
				+ enableAccessSpecID + ", c1g2EPCMemorySelectorList=" + c1g2EPCMemorySelectorList + ", cusList=" + cusList + "]";
	}
}