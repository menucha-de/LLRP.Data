package havis.llrpservice.data.message;

import java.io.Serializable;

public interface Message extends Serializable {

	public MessageHeader getMessageHeader();
}