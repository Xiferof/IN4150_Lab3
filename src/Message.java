import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by vincent on 11/27/16.
 */
public class Message implements Serializable, Comparable<Message>
{
    private Timestamp timestamp;
    private ProcId senderID;

    public Message(int level, ProcId senderID)
    {
        this.timestamp = new Timestamp(level, senderID);
        this.senderID = senderID;
    }

    public Message(Timestamp timestamp, ProcId senderID)
    {
        this.timestamp = timestamp;
        this.senderID = senderID;
    }

    public String toString()
    {
        return "Message timestamp: " + timestamp + ", senderId: " + senderID;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof Message))
            return false;
        return (this.timestamp.equals(((Message)(obj)).timestamp) && (this.senderID == ((Message)(obj)).senderID));
    }

    public int compareTo(Message that)
    {
        return this.timestamp.compareTo(that.timestamp);
    }

    public Timestamp getTimestamp() {return timestamp;}

    public ProcId getSenderID() {return senderID;}
}
