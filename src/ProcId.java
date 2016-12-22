import java.io.Serializable;

/**
 * Created by vincent on 12/22/16.
 */
public class ProcId implements Serializable
{
    private int id;
    private String binding;

    public ProcId(int id, String binding)
    {
        this.id = id;
        this.binding = binding;
    }

    public ProcId(ProcId copy)
    {
        this.id = copy.id;
        this.binding = copy.binding;
    }

    public int getId()
    {
        return id;
    }

    public String getBinding()
    {
        return binding;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof ProcId))
            return false;
        return ((this.id == ((ProcId)(other)).id) && (this.binding.equals(((ProcId)(other)).binding)));
    }

    public String toString()
    {
        return binding + "/" + id;
    }
}
