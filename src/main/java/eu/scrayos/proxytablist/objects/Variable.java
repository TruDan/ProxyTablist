package eu.scrayos.proxytablist.objects;

/**
 * Thats the interface for custom Variables. You should locate them into the ProxyTablist/variables/ - directory.
 *
 * @author Scrayos
 */
public interface Variable {

    /**
     * Used to determine which text should replaced by this variable.
     *
     * @return The Pattern for this Variable
     */
    public String getPattern();

    /**
     * The Text or Value this Variable contains. The found matches get replaced by this text.
     *
     * @return
     */
    public String getText();
}
