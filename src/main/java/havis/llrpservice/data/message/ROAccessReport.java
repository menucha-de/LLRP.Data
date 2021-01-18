package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.Custom;
import havis.llrpservice.data.message.parameter.RFSurveyReportData;
import havis.llrpservice.data.message.parameter.TagReportData;

import java.util.List;

public class ROAccessReport implements Message {

	private static final long serialVersionUID = -730455566694991399L;

	private MessageHeader messageHeader;
	private List<TagReportData> tagReportDataList;
	private List<RFSurveyReportData> rfSurveyReportDataList;
	private List<Custom> cusList;

	public ROAccessReport() {
	}

	public ROAccessReport(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.RO_ACCESS_REPORT);
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public List<TagReportData> getTagReportDataList() {
		return tagReportDataList;
	}

	public void setTagReportDataList(List<TagReportData> tagReportDataList) {
		this.tagReportDataList = tagReportDataList;
	}

	public List<RFSurveyReportData> getRfSurveyReportDataList() {
		return rfSurveyReportDataList;
	}

	public void setRfSurveyReportDataList(List<RFSurveyReportData> rfSurveyReportDataList) {
		this.rfSurveyReportDataList = rfSurveyReportDataList;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "ROAccessReport [messageHeader=" + messageHeader + ", tagReportDataList=" + tagReportDataList + ", rfSurveyReportDataList="
				+ rfSurveyReportDataList + ", cusList=" + cusList + "]";
	}
}