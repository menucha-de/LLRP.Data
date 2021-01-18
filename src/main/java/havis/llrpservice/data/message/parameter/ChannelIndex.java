package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ChannelIndex extends Parameter {

	private static final long serialVersionUID = 8774824510688594732L;

	private TVParameterHeader parameterHeader;
	private int channelIndex;

	public ChannelIndex() {
	}

	public ChannelIndex(TVParameterHeader parameterHeader, int channelIndex) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.CHANNEL_INDEX);
		this.channelIndex = channelIndex;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(int channelIndex) {
		this.channelIndex = channelIndex;
	}

	@Override
	public String toString() {
		return "ChannelIndex [parameterHeader=" + parameterHeader + ", channelIndex=" + channelIndex + "]";
	}
}