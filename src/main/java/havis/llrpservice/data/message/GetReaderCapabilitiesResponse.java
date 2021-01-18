package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.C1G2LLRPCapabilities;
import havis.llrpservice.data.message.parameter.Custom;
import havis.llrpservice.data.message.parameter.GeneralDeviceCapabilities;
import havis.llrpservice.data.message.parameter.LLRPCapabilities;
import havis.llrpservice.data.message.parameter.LLRPStatus;
import havis.llrpservice.data.message.parameter.RegulatoryCapabilities;

import java.util.List;

public class GetReaderCapabilitiesResponse implements Message {

	private static final long serialVersionUID = -849074208147701117L;

	private MessageHeader messageHeader;
	private LLRPStatus status;
	private GeneralDeviceCapabilities generalDeviceCap;
	private LLRPCapabilities llrpCap;
	private RegulatoryCapabilities regulatoryCap;
	private C1G2LLRPCapabilities c1g2llrpCap;
	private List<Custom> customExtensionPoint;

	public GetReaderCapabilitiesResponse() {
	}

	public GetReaderCapabilitiesResponse(MessageHeader header, LLRPStatus status) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.GET_READER_CAPABILITIES_RESPONSE);
		this.status = status;
	}

	public GetReaderCapabilitiesResponse(MessageHeader header, LLRPStatus status, GeneralDeviceCapabilities generalDeviceCap) {
		this(header, status);
		this.generalDeviceCap = generalDeviceCap;
	}

	public GetReaderCapabilitiesResponse(MessageHeader header, LLRPStatus status, LLRPCapabilities llrpCap) {
		this(header, status);
		this.llrpCap = llrpCap;
	}

	public GetReaderCapabilitiesResponse(MessageHeader header, LLRPStatus status, RegulatoryCapabilities regulatoryCap) {
		this(header, status);
		this.regulatoryCap = regulatoryCap;
	}

	public GetReaderCapabilitiesResponse(MessageHeader header, LLRPStatus status, C1G2LLRPCapabilities c1g2LLRPCap) {
		this(header, status);
		c1g2llrpCap = c1g2LLRPCap;
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

	public GeneralDeviceCapabilities getGeneralDeviceCap() {
		return generalDeviceCap;
	}

	public void setGeneralDeviceCap(GeneralDeviceCapabilities generalDeviceCap) {
		this.generalDeviceCap = generalDeviceCap;
	}

	public LLRPCapabilities getLlrpCap() {
		return llrpCap;
	}

	public void setLlrpCap(LLRPCapabilities llrpCap) {
		this.llrpCap = llrpCap;
	}

	public RegulatoryCapabilities getRegulatoryCap() {
		return regulatoryCap;
	}

	public void setRegulatoryCap(RegulatoryCapabilities regulatoryCap) {
		this.regulatoryCap = regulatoryCap;
	}

	public C1G2LLRPCapabilities getC1g2llrpCap() {
		return c1g2llrpCap;
	}

	public void setC1g2llrpCap(C1G2LLRPCapabilities c1g2llrpCap) {
		this.c1g2llrpCap = c1g2llrpCap;
	}

	public List<Custom> getCustomExtensionPoint() {
		return customExtensionPoint;
	}

	public void setCustomExtensionPoint(List<Custom> customExtensionPoint) {
		this.customExtensionPoint = customExtensionPoint;
	}

	@Override
	public String toString() {
		return "GetReaderCapabilitiesResponse [messageHeader=" + messageHeader + ", status=" + status + ", generalDeviceCap=" + generalDeviceCap + ", llrpCap="
				+ llrpCap + ", regulatoryCap=" + regulatoryCap + ", c1g2llrpCap=" + c1g2llrpCap + ", customExtensionPoint=" + customExtensionPoint + "]";
	}
}