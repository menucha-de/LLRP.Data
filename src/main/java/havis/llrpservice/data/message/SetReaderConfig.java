package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.AccessReportSpec;
import havis.llrpservice.data.message.parameter.AntennaConfiguration;
import havis.llrpservice.data.message.parameter.AntennaProperties;
import havis.llrpservice.data.message.parameter.Custom;
import havis.llrpservice.data.message.parameter.EventsAndReports;
import havis.llrpservice.data.message.parameter.GPIPortCurrentState;
import havis.llrpservice.data.message.parameter.GPOWriteData;
import havis.llrpservice.data.message.parameter.KeepaliveSpec;
import havis.llrpservice.data.message.parameter.ROReportSpec;
import havis.llrpservice.data.message.parameter.ReaderEventNotificationSpec;

import java.util.List;

public class SetReaderConfig implements Message {

	private static final long serialVersionUID = -4891253410876400004L;

	private MessageHeader messageHeader;
	private boolean resetToFactoryDefaults;
	private ReaderEventNotificationSpec readerEventNotificationSpec;
	private List<AntennaProperties> antennaPropertiesList;
	private List<AntennaConfiguration> antennaConfigurationList;
	private ROReportSpec roReportSpec;
	private AccessReportSpec accessReportSpec;
	private KeepaliveSpec keepaliveSpec;
	private List<GPOWriteData> gpoWriteDataList;
	private List<GPIPortCurrentState> gpiPortCurrentStateList;
	private EventsAndReports eventAndReports;
	private List<Custom> customList;

	public SetReaderConfig() {
	}

	public SetReaderConfig(MessageHeader messageHeader, boolean resetToFactoryDefaults) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.SET_READER_CONFIG);
		this.resetToFactoryDefaults = resetToFactoryDefaults;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public boolean isResetToFactoryDefaults() {
		return resetToFactoryDefaults;
	}

	public void setResetToFactoryDefaults(boolean resetToFactoryDefaults) {
		this.resetToFactoryDefaults = resetToFactoryDefaults;
	}

	public List<AntennaProperties> getAntennaPropertiesList() {
		return antennaPropertiesList;
	}

	public void setAntennaPropertiesList(List<AntennaProperties> antennaPropertiesList) {
		this.antennaPropertiesList = antennaPropertiesList;
	}

	public List<AntennaConfiguration> getAntennaConfigurationList() {
		return antennaConfigurationList;
	}

	public void setAntennaConfigurationList(List<AntennaConfiguration> antennaConfigurationList) {
		this.antennaConfigurationList = antennaConfigurationList;
	}

	public ReaderEventNotificationSpec getReaderEventNotificationSpec() {
		return readerEventNotificationSpec;
	}

	public void setReaderEventNotificationSpec(ReaderEventNotificationSpec readerEventNotificationSpec) {
		this.readerEventNotificationSpec = readerEventNotificationSpec;
	}

	public ROReportSpec getRoReportSpec() {
		return roReportSpec;
	}

	public void setRoReportSpec(ROReportSpec roReportSpec) {
		this.roReportSpec = roReportSpec;
	}

	public AccessReportSpec getAccessReportSpec() {
		return accessReportSpec;
	}

	public void setAccessReportSpec(AccessReportSpec accessReportSpec) {
		this.accessReportSpec = accessReportSpec;
	}

	public KeepaliveSpec getKeepaliveSpec() {
		return keepaliveSpec;
	}

	public void setKeepaliveSpec(KeepaliveSpec keepaliveSpec) {
		this.keepaliveSpec = keepaliveSpec;
	}

	public List<GPIPortCurrentState> getGpiPortCurrentStateList() {
		return gpiPortCurrentStateList;
	}

	public void setGpiPortCurrentStateList(List<GPIPortCurrentState> gpiPortCurrentStateList) {
		this.gpiPortCurrentStateList = gpiPortCurrentStateList;
	}

	public List<GPOWriteData> getGpoWriteDataList() {
		return gpoWriteDataList;
	}

	public void setGpoWriteDataList(List<GPOWriteData> gpoWriteDataList) {
		this.gpoWriteDataList = gpoWriteDataList;
	}

	public EventsAndReports getEventAndReports() {
		return eventAndReports;
	}

	public void setEventAndReports(EventsAndReports eventAndReports) {
		this.eventAndReports = eventAndReports;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "SetReaderConfig [messageHeader=" + messageHeader + ", resetToFactoryDefaults=" + resetToFactoryDefaults + ", readerEventNotificationSpec="
				+ readerEventNotificationSpec + ", antennaPropertiesList=" + antennaPropertiesList + ", antennaConfigurationList=" + antennaConfigurationList
				+ ", roReportSpec=" + roReportSpec + ", accessReportSpec=" + accessReportSpec + ", keepaliveSpec=" + keepaliveSpec + ", gpoWriteDataList="
				+ gpoWriteDataList + ", gpiPortCurrentStateList=" + gpiPortCurrentStateList + ", eventAndReports=" + eventAndReports + ", customList="
				+ customList + "]";
	}
}