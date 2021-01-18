package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class TagSeenCount extends Parameter {

	private static final long serialVersionUID = -905393903512649132L;

	private TVParameterHeader parameterHeader;
	private int tagCount;

	public TagSeenCount() {
	}

	public TagSeenCount(TVParameterHeader parameterHeader, int tagCount) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.TAG_SEEN_COUNT);
		this.tagCount = tagCount;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getTagCount() {
		return tagCount;
	}

	public void setTagCount(int tagCount) {
		this.tagCount = tagCount;
	}

	@Override
	public String toString() {
		return "TagSeenCount [parameterHeader=" + parameterHeader + ", tagCount=" + tagCount + "]";
	}
}