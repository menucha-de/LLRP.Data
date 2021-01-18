package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class ROReportSpec extends Parameter {

	private static final long serialVersionUID = 139512387344221434L;

	private TLVParameterHeader parameterHeader;
	private ROReportTrigger roReportTrigger;
	private int n;
	private TagReportContentSelector tagReportContentSelector;
	private List<Custom> customList;

	public ROReportSpec() {
	}

	public ROReportSpec(TLVParameterHeader header, ROReportTrigger roReportTrigger, int n, TagReportContentSelector tagReportContentSelector) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.RO_REPORT_SPEC);
		this.roReportTrigger = roReportTrigger;
		this.n = n;
		this.tagReportContentSelector = tagReportContentSelector;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public ROReportTrigger getRoReportTrigger() {
		return roReportTrigger;
	}

	public void setRoReportTrigger(ROReportTrigger roReportTrigger) {
		this.roReportTrigger = roReportTrigger;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public TagReportContentSelector getTagReportContentSelector() {
		return tagReportContentSelector;
	}

	public void setTagReportContentSelector(TagReportContentSelector tagReportContentSelector) {
		this.tagReportContentSelector = tagReportContentSelector;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "ROReportSpec [parameterHeader=" + parameterHeader + ", roReportTrigger=" + roReportTrigger + ", n=" + n + ", tagReportContentSelector="
				+ tagReportContentSelector + ", customList=" + customList + "]";
	}
}