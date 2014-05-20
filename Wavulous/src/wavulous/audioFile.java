package wavulous;

/**
 *
 * @author Luis
 */
public class audioFile {
    
    private String nombre;
    private float volume;
    
    public audioFile(String name)
    {
        nombre = name;
    }
    
    public String getNombre()
    {
        return nombre;
    }
    public float getVolume()
    {
        return volume;
    }

}
