package de.qabel.core.drop;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import de.qabel.core.config.Entity;

public class DropMessage implements Serializable {
	/**
	 * Acknowledge ID indicating that the sender does not
	 * wish to receive an acknowledgement.
	 */
	public static final String NOACK = "0";
	/**
	 * Model object reserved for internal drop protocol purposes only.
	 */
	public static final String INTERNAL_MODEL_OBJECT = "drop";

	private static final int VERSION = 1;


    private Date created;
    private String acknowledgeId;
    private Entity sender;
    private String senderKeyId;
	private String dropPayload;
	private String dropPayloadType;

	public DropMessage(Entity sender, String dropPayload, String dropPayloadType) {
    	this.sender = sender;
		this.dropPayload = dropPayload;
		this.dropPayloadType = dropPayloadType;
    	this.created = new Date();
    	this.acknowledgeId = NOACK;
    }

    /**
     * Constructor used for deserialization.
     * registerSender has to be called to complete creation.
     */
	DropMessage(String senderKeyId, String dropPayload, String dropPayloadType, Date created, String acknowledgeId) {
		this.senderKeyId = senderKeyId;
		this.dropPayload = dropPayload;
		this.dropPayloadType = dropPayloadType;
		this.created = created;
		this.acknowledgeId = acknowledgeId;
	}

	public static int getVersion() {
        return VERSION;
    }

    public Date getCreationDate() {
        return created;
    }

    public String getAcknowledgeID() {
        return acknowledgeId;
    }

    public Entity getSender() {
        return sender;
    }

    public String getSenderKeyId() {
    	return senderKeyId;
    }

    /**
     * Register the given Entity as sender of this drop message
     * and check if it matches the senderKeyId.
     * This is used to complete the deserialization of DropMessage.
     *
     * @param sender Entity to be registered as sender.
     * @return true if the given sender matches the senderKeyId, otherwise false.
     */
    public boolean registerSender(Entity sender) {
    	if (!senderKeyId.equals(sender.getKeyIdentifier())) {
    		return false;
    	}
    	this.sender = sender;
    	return true;
    }

	public String getDropPayload() {
		return dropPayload;
	}

    public String getDropPayloadType() {
        return dropPayloadType;
    }

    /**
     * Enable/disable acknowledge request for this drop message.
     * Acknowledging is disabled by default.
     *
     * @param enabled true enables acknowledging
     */
    public void enableAcknowledging(boolean enabled) {
    	if (enabled && this.acknowledgeId.equals(NOACK)) {
    		this.acknowledgeId = generateAcknowledgeId();
    	} else if (!enabled) {
    		this.acknowledgeId = NOACK;
    	}
    }
    
    private static String generateAcknowledgeId() {
    	return UUID.randomUUID().toString();
    }
}
