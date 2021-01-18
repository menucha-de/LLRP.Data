package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class ReaderEventNotificationData extends Parameter {

	private static final long serialVersionUID = -222695322243918350L;

	private TLVParameterHeader parameterHeader;
	private UTCTimestamp utcTimestamp;
	private Uptime uptime;
	private HoppingEvent hoppingEvent;
	private GPIEvent gpiEvent;
	private ROSpecEvent roSpecEvent;
	private ReportBufferLevelWarningEvent reportBufferLevelWarningEvent;
	private ReportBufferOverflowErrorEvent reportBufferOverflowErrorEvent;
	private ReaderExceptionEvent readerExceptionEvent;
	private RFSurveyEvent rfSurveyEvent;
	private AISpecEvent aiSpecEvent;
	private AntennaEvent antennaEvent;
	private ConnectionAttemptEvent connectionAttemptEvent;
	private ConnectionCloseEvent connectionCloseEvent;
	private SpecLoopEvent specLoopEvent;
	private List<Custom> customList;

	public ReaderEventNotificationData() {
	}

	public ReaderEventNotificationData(TLVParameterHeader header, UTCTimestamp utcTimestamp) {
		this(header);
		this.utcTimestamp = utcTimestamp;
	}

	public ReaderEventNotificationData(TLVParameterHeader header, Uptime uptime) {
		this(header);
		this.uptime = uptime;
	}

	private ReaderEventNotificationData(TLVParameterHeader header) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.READER_EVENT_NOTIFICATION_DATA);
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	/**
	 * @return <code>null</code> if {@link #getUptime()} returns a value
	 */
	public UTCTimestamp getUtcTimestamp() {
		return utcTimestamp;
	}

	public void setUtcTimestamp(UTCTimestamp utcTimestamp) {
		this.utcTimestamp = utcTimestamp;
	}

	/**
	 * @return <code>null</code> if {@link #getUtcTimestamp()} returns a value
	 */
	public Uptime getUptime() {
		return uptime;
	}

	public void setUptime(Uptime uptime) {
		this.uptime = uptime;
	}

	/**
	 * Optional
	 * 
	 * @return The hopping event
	 */
	public HoppingEvent getHoppingEvent() {
		return hoppingEvent;
	}

	public void setHoppingEvent(HoppingEvent hoppingEvent) {
		this.hoppingEvent = hoppingEvent;
	}

	public GPIEvent getGpiEvent() {
		return gpiEvent;
	}

	public void setGpiEvent(GPIEvent gpiEvent) {
		this.gpiEvent = gpiEvent;
	}

	public ROSpecEvent getRoSpecEvent() {
		return roSpecEvent;
	}

	public void setRoSpecEvent(ROSpecEvent roSpecEvent) {
		this.roSpecEvent = roSpecEvent;
	}

	public ReportBufferLevelWarningEvent getReportBufferLevelWarningEvent() {
		return reportBufferLevelWarningEvent;
	}

	public void setReportBufferLevelWarningEvent(ReportBufferLevelWarningEvent reportBufferLevelWarningEvent) {
		this.reportBufferLevelWarningEvent = reportBufferLevelWarningEvent;
	}

	public ReportBufferOverflowErrorEvent getReportBufferOverflowErrorEvent() {
		return reportBufferOverflowErrorEvent;
	}

	public void setReportBufferOverflowErrorEvent(ReportBufferOverflowErrorEvent reportBufferOverflowErrorEvent) {
		this.reportBufferOverflowErrorEvent = reportBufferOverflowErrorEvent;
	}

	public ReaderExceptionEvent getReaderExceptionEvent() {
		return readerExceptionEvent;
	}

	public void setReaderExceptionEvent(ReaderExceptionEvent readerExceptionEvent) {
		this.readerExceptionEvent = readerExceptionEvent;
	}

	public RFSurveyEvent getRfSurveyEvent() {
		return rfSurveyEvent;
	}

	public void setRfSurveyEvent(RFSurveyEvent rfSurveyEvent) {
		this.rfSurveyEvent = rfSurveyEvent;
	}

	public AISpecEvent getAiSpecEvent() {
		return aiSpecEvent;
	}

	public void setAiSpecEvent(AISpecEvent aiSpecEvent) {
		this.aiSpecEvent = aiSpecEvent;
	}

	public AntennaEvent getAntennaEvent() {
		return antennaEvent;
	}

	public void setAntennaEvent(AntennaEvent antennaEvent) {
		this.antennaEvent = antennaEvent;
	}

	public ConnectionAttemptEvent getConnectionAttemptEvent() {
		return connectionAttemptEvent;
	}

	public void setConnectionAttemptEvent(ConnectionAttemptEvent connectionAttemptEvent) {
		this.connectionAttemptEvent = connectionAttemptEvent;
	}

	public ConnectionCloseEvent getConnectionCloseEvent() {
		return connectionCloseEvent;
	}

	public void setConnectionCloseEvent(ConnectionCloseEvent connectionCloseEvent) {
		this.connectionCloseEvent = connectionCloseEvent;
	}

	public SpecLoopEvent getSpecLoopEvent() {
		return specLoopEvent;
	}

	public void setSpecLoopEvent(SpecLoopEvent specLoopEvent) {
		this.specLoopEvent = specLoopEvent;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "ReaderEventNotificationData [parameterHeader=" + parameterHeader + ", utcTimestamp=" + utcTimestamp + ", uptime=" + uptime + ", hoppingEvent="
				+ hoppingEvent + ", gpiEvent=" + gpiEvent + ", roSpecEvent=" + roSpecEvent + ", reportBufferLevelWarningEvent=" + reportBufferLevelWarningEvent
				+ ", reportBufferOverflowErrorEvent=" + reportBufferOverflowErrorEvent + ", readerExceptionEvent=" + readerExceptionEvent + ", rfSurveyEvent="
				+ rfSurveyEvent + ", aiSpecEvent=" + aiSpecEvent + ", antennaEvent=" + antennaEvent + ", connectionAttemptEvent=" + connectionAttemptEvent
				+ ", connectionCloseEvent=" + connectionCloseEvent + ", specLoopEvent=" + specLoopEvent + ", customList=" + customList + "]";
	}
}