import java.io.Serializable;

/**
 * Created by vincent on 11/27/16.
 */
public class Timestamp implements Serializable, Comparable<Timestamp>
{
    private int level;
    private ProcId id;

    public Timestamp(int level, ProcId id)
    {
        this.level = level;
        this.id = id;
    }

    public Timestamp(Timestamp timestamp)
    {
        this.level = timestamp.level;
        this.id = timestamp.id;
    }

    public int getLevel() {return level;}

    public ProcId getPId() {return id;}

    public String toString()
    {
        return "Time " + level + ", ID: " + id;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof Timestamp))
            return false;
        return ((this.level == ((Timestamp)(obj)).level) && (this.id == ((Timestamp)(obj)).id));
    }

    public int compareTo(Timestamp that)
    {
        if(this.level == that.level)
        {
            return this.id.getId() - that.id.getId();
        }
        else
        {
            return this.level - that.level;
        }
    }



}
