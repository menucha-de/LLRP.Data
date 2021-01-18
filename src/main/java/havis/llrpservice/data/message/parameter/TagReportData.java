package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class TagReportData extends Parameter {

	private static final long serialVersionUID = 8189330079504191743L;

	private TLVParameterHeader parameterHeader;
	private EPCData epcData;
	private EPC96 epc96;

	private ROSpecID roSpecID;
	private SpecIndex specIndex;
	private InventoryParameterSpecID invParaSpecID;
	private AntennaId antID;
	private PeakRSSI peakRSSI;
	private ChannelIndex channelInd;
	private FirstSeenTimestampUTC firstSTUTC;
	private FirstSeenTimestampUptime firstSTUptime;
	private LastSeenTimestampUTC lastSTUTC;
	private LastSeenTimestampUptime lastSTUptime;
	private TagSeenCount tagSC;
	private List<Parameter> c1g2TagDataList;
	private AccessSpecId accessSpecID;
	private List<Parameter> opSpecResultList;
	private List<Custom> cusList;

	private void init(TLVParameterHeader header) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.TAG_REPORT_DATA);
	}

	public TagReportData() {
	}

	public TagReportData(TLVParameterHeader header, EPCData epcData) {
		init(header);
		this.epcData = epcData;
	}

	public TagReportData(TLVParameterHeader header, EPC96 epc96) {
		init(header);
		this.epc96 = epc96;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public EPCData getEpcData() {
		return epcData;
	}

	public void setEpcData(EPCData epcData) {
		this.epcData = epcData;
	}

	public EPC96 getEpc96() {
		return epc96;
	}

	public void setEpc96(EPC96 epc96) {
		this.epc96 = epc96;
	}

	public ROSpecID getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(ROSpecID roSpecID) {
		this.roSpecID = roSpecID;
	}

	public SpecIndex getSpecIndex() {
		return specIndex;
	}

	public void setSpecIndex(SpecIndex specIndex) {
		this.specIndex = specIndex;
	}

	public InventoryParameterSpecID getInvParaSpecID() {
		return invParaSpecID;
	}

	public void setInvParaSpecID(InventoryParameterSpecID invParaSpecID) {
		this.invParaSpecID = invParaSpecID;
	}

	public AntennaId getAntID() {
		return antID;
	}

	public void setAntID(AntennaId antID) {
		this.antID = antID;
	}

	public PeakRSSI getPeakRSSI() {
		return peakRSSI;
	}

	public void setPeakRSSI(PeakRSSI peakRSSI) {
		this.peakRSSI = peakRSSI;
	}

	public ChannelIndex getChannelInd() {
		return channelInd;
	}

	public void setChannelInd(ChannelIndex channelInd) {
		this.channelInd = channelInd;
	}

	public FirstSeenTimestampUTC getFirstSTUTC() {
		return firstSTUTC;
	}

	public void setFirstSTUTC(FirstSeenTimestampUTC firstSTUTC) {
		this.firstSTUTC = firstSTUTC;
	}

	public FirstSeenTimestampUptime getFirstSTUptime() {
		return firstSTUptime;
	}

	public void setFirstSTUptime(FirstSeenTimestampUptime firstSTUptime) {
		this.firstSTUptime = firstSTUptime;
	}

	public LastSeenTimestampUTC getLastSTUTC() {
		return lastSTUTC;
	}

	public void setLastSTUTC(LastSeenTimestampUTC lastSTUTC) {
		this.lastSTUTC = lastSTUTC;
	}

	public LastSeenTimestampUptime getLastSTUptime() {
		return lastSTUptime;
	}

	public void setLastSTUptime(LastSeenTimestampUptime lastSTUptime) {
		this.lastSTUptime = lastSTUptime;
	}

	public TagSeenCount getTagSC() {
		return tagSC;
	}

	public void setTagSC(TagSeenCount tagSC) {
		this.tagSC = tagSC;
	}

	public List<Parameter> getC1g2TagDataList() {
		return c1g2TagDataList;
	}

	public void setC1g2TagDataList(List<Parameter> c1g2TagDataList) {
		this.c1g2TagDataList = c1g2TagDataList;
	}

	public AccessSpecId getAccessSpecID() {
		return accessSpecID;
	}

	public void setAccessSpecID(AccessSpecId accessSpecID) {
		this.accessSpecID = accessSpecID;
	}

	public List<Parameter> getOpSpecResultList() {
		return opSpecResultList;
	}

	public void setOpSpecResultList(List<Parameter> opSpecResultList) {
		this.opSpecResultList = opSpecResultList;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "TagReportData [parameterHeader=" + parameterHeader + ", epcData=" + epcData + ", epc96=" + epc96 + ", roSpecID=" + roSpecID + ", specIndex="
				+ specIndex + ", invParaSpecID=" + invParaSpecID + ", antID=" + antID + ", peakRSSI=" + peakRSSI + ", channelInd=" + channelInd
				+ ", firstSTUTC=" + firstSTUTC + ", firstSTUptime=" + firstSTUptime + ", lastSTUTC=" + lastSTUTC + ", lastSTUptime=" + lastSTUptime
				+ ", tagSC=" + tagSC + ", c1g2TagDataList=" + c1g2TagDataList + ", accessSpecID=" + accessSpecID + ", opSpecResultList=" + opSpecResultList
				+ ", cusList=" + cusList + "]";
	}
}