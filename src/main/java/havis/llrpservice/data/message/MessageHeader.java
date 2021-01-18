package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;

import java.io.Serializable;

public class MessageHeader implements Serializable {

	private static final long serialVersionUID = 1055892162553764980L;

	private byte reserved;
	private ProtocolVersion version;
	private MessageType messageType;
	private long messageLength;
	private long id;

	public MessageHeader() {
	}

	public MessageHeader(byte reserved, ProtocolVersion version, long id) {
		this.reserved = reserved;
		this.version = version;
		this.id = id;
	}

	public byte getReserved() {
		return reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}

	public ProtocolVersion getVersion() {
		return version;
	}

	public void setVersion(ProtocolVersion version) {
		this.version = version;
	}

	/**
	 * Returns the message type. It is set when the root DTO of the message is
	 * instantiated.
	 * 
	 * @return message type
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType type) {
		this.messageType = type;
	}

	/**
	 * Returns the length of the message in bytes. It set during the
	 * de-/serialization.
	 * 
	 * @return The message length
	 */
	public long getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(long length) {
		messageLength = length;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MessageHeader [reserved=" + reserved + ", version=" + version + ", messageType=" + messageType + ", messageLength=" + messageLength + ", id="
				+ id + "]";
	}
}
