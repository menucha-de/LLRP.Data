package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2TagSpec extends Parameter {

	private static final long serialVersionUID = 742355131548819971L;

	private TLVParameterHeader parameterHeader;
	private C1G2TargetTag tagPattern1;
	private C1G2TargetTag tagPattern2;

	public C1G2TagSpec() {
		this(new TLVParameterHeader(), null);
	}

	public C1G2TagSpec(TLVParameterHeader parameterHeader, C1G2TargetTag tagPattern1) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_TAG_SPEC);
		this.tagPattern1 = tagPattern1;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2TargetTag getTagPattern1() {
		return tagPattern1;
	}

	public void setTagPattern1(C1G2TargetTag tagPattern1) {
		this.tagPattern1 = tagPattern1;
	}

	public C1G2TargetTag getTagPattern2() {
		return tagPattern2;
	}

	public void setTagPattern2(C1G2TargetTag c1g2TargetTag) {
		this.tagPattern2 = c1g2TargetTag;
	}

	@Override
	public String toString() {
		return "C1G2TagSpec [parameterHeader=" + parameterHeader + ", tagPattern1=" + tagPattern1 + ", tagPattern2=" + tagPattern2 + "]";
	}
}