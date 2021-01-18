package havis.llrpservice.data.message.parameter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({ AccessCommand.class, AccessReportSpec.class, AccessSpec.class, AccessSpecId.class, AccessSpecStopTrigger.class, AISpec.class, AISpecEvent.class,
		AISpecStopTrigger.class, AntennaConfiguration.class, AntennaEvent.class, AntennaId.class, AntennaProperties.class, C1G2BlockErase.class,
		C1G2BlockEraseOpSpecResult.class, C1G2BlockPermalock.class, C1G2BlockPermalockOpSpecResult.class, C1G2BlockWrite.class,
		C1G2BlockWriteOpSpecResult.class, C1G2CRC.class, C1G2EPCMemorySelector.class, C1G2Filter.class, C1G2GetBlockPermalockStatus.class,
		C1G2GetBlockPermalockStatusOpSpecResult.class, C1G2InventoryCommand.class, C1G2Kill.class, C1G2KillOpSpecResult.class, C1G2LLRPCapabilities.class,
		C1G2Lock.class, C1G2LockOpSpecResult.class, C1G2LockPayload.class, C1G2PC.class, C1G2Read.class, C1G2ReadOpSpecResult.class, C1G2Recommission.class,
		C1G2RecommissionOpSpecResult.class, C1G2RFControl.class, C1G2SingulationControl.class, C1G2SingulationDetails.class, C1G2TagInventoryMask.class,
		C1G2TagInventoryStateAwareFilterAction.class, C1G2TagInventoryStateAwareSingulationAction.class, C1G2TagInventoryStateUnawareFilterAction.class,
		C1G2TagSpec.class, C1G2TargetTag.class, C1G2Write.class, C1G2WriteOpSpecResult.class, C1G2XPCW1.class, C1G2XPCW2.class, ChannelIndex.class,
		ClientRequestOpSpec.class, ClientRequestOpSpecResult.class, ClientRequestResponse.class, ConnectionAttemptEvent.class, ConnectionCloseEvent.class,
		Custom.class, EPC96.class, EPCData.class, EventNotificationState.class, EventsAndReports.class, FieldError.class, FirstSeenTimestampUptime.class,
		FirstSeenTimestampUTC.class, FixedFrequencyTable.class, FrequencyHopTable.class, FrequencyInformation.class, FrequencyRSSILevelEntry.class,
		GeneralDeviceCapabilities.class, GPIEvent.class, GPIOCapabilities.class, GPIPortCurrentState.class, GPITriggerValue.class, GPOWriteData.class,
		HoppingEvent.class, Identification.class, InventoryParameterSpec.class, InventoryParameterSpecID.class, KeepaliveSpec.class,
		LastSeenTimestampUptime.class, LastSeenTimestampUTC.class, LLRPCapabilities.class, LLRPConfigurationStateValue.class, LLRPStatus.class, LoopSpec.class,
		MaximumReceiveSensitivity.class, OpSpecID.class, ParameterError.class, PeakRSSI.class, PerAntennaAirProtocol.class,
		PerAntennaReceiveSensitivityRange.class, PeriodicTriggerValue.class, ReaderEventNotificationData.class, ReaderEventNotificationSpec.class,
		ReaderExceptionEvent.class, ReceiveSensitivityTabelEntry.class, RegulatoryCapabilities.class, ReportBufferLevelWarningEvent.class,
		ReportBufferOverflowErrorEvent.class, RFReceiver.class, RFSurveyEvent.class, RFSurveyEvent.class, RFSurveyFrequencyCapabilities.class,
		RFSurveyReportData.class, RFSurveySpec.class, RFSurveySpecStopTrigger.class, RFTransmitter.class, ROBoundarySpec.class, ROReportSpec.class,
		ROSpec.class, ROSpecEvent.class, ROSpecID.class, ROSpecStartTrigger.class, ROSpecStopTrigger.class, SpecIndex.class, SpecLoopEvent.class,
		TagObservationTrigger.class, TagReportContentSelector.class, TagReportData.class, TagSeenCount.class, TransmitPowerLevelTableEntry.class,
		UHFBandCapabilities.class, UHFC1G2RFModeTable.class, UHFC1G2RFModeTableEntry.class, Uptime.class, UTCTimestamp.class })
public abstract class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract ParameterHeader getParameterHeader();
}