package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.AccessReportSpec;
import havis.llrpservice.data.message.parameter.AntennaConfiguration;
import havis.llrpservice.data.message.parameter.AntennaProperties;
import havis.llrpservice.data.message.parameter.Custom;
import havis.llrpservice.data.message.parameter.EventsAndReports;
import havis.llrpservice.data.message.parameter.GPIPortCurrentState;
import havis.llrpservice.data.message.parameter.GPOWriteData;
import havis.llrpservice.data.message.parameter.Identification;
import havis.llrpservice.data.message.parameter.KeepaliveSpec;
import havis.llrpservice.data.message.parameter.LLRPConfigurationStateValue;
import havis.llrpservice.data.message.parameter.LLRPStatus;
import havis.llrpservice.data.message.parameter.ROReportSpec;
import havis.llrpservice.data.message.parameter.ReaderEventNotificationSpec;

import java.util.List;

public class GetReaderConfigResponse implements Message {

	private static final long serialVersionUID = 8205811594411255461L;

	private MessageHeader messageHeader;
	private LLRPStatus status;
	private Identification identification;
	private List<AntennaProperties> antennaPropertiesList;
	private List<AntennaConfiguration> antennaConfigurationList;
	private ReaderEventNotificationSpec readerEventNotificationSpec;
	private ROReportSpec roReportSpec;
	private AccessReportSpec accessReportSpec;
	private LLRPConfigurationStateValue llrpConfigurationStateValue;
	private KeepaliveSpec keepaliveSpec;
	private List<GPIPortCurrentState> gpiPortCurrentStateList;
	private List<GPOWriteData> gpoWriteDataList;
	private EventsAndReports eventAndReports;
	private List<Custom> customList;

	public GetReaderConfigResponse() {
	}

	public GetReaderConfigResponse(MessageHeader header, LLRPStatus status) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.GET_READER_CONFIG_RESPONSE);
		this.status = status;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public LLRPStatus getStatus() {
		return status;
	}

	public void setStatus(LLRPStatus status) {
		this.status = status;
	}

	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		this.identification = identification;
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

	public LLRPConfigurationStateValue getLlrpConfigurationStateValue() {
		return llrpConfigurationStateValue;
	}

	public void setLlrpConfigurationStateValue(LLRPConfigurationStateValue llrpConfigurationStateValue) {
		this.llrpConfigurationStateValue = llrpConfigurationStateValue;
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
		return "GetReaderConfigResponse [messageHeader=" + messageHeader + ", status=" + status + ", identification=" + identification
				+ ", antennaPropertiesList=" + antennaPropertiesList + ", antennaConfigurationList=" + antennaConfigurationList
				+ ", readerEventNotificationSpec=" + readerEventNotificationSpec + ", roReportSpec=" + roReportSpec + ", accessReportSpec=" + accessReportSpec
				+ ", llrpConfigurationStateValue=" + llrpConfigurationStateValue + ", keepaliveSpec=" + keepaliveSpec + ", gpiPortCurrentStateList="
				+ gpiPortCurrentStateList + ", gpoWriteDataList=" + gpoWriteDataList + ", eventAndReports=" + eventAndReports + ", customList=" + customList
				+ "]";
	}
}