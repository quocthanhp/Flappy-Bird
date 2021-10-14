import bagel.Image;


/** Represent plastic pipe set in the game
 *
 */
public class PlasticPipeSet extends PipeSet{
    private static final Image PLASTIC_PIPE_IMAGE = new Image("res/level/plasticPipe.png");


    /** Create plastic pipe set
     *
     */
    public PlasticPipeSet() {
        super(PLASTIC_PIPE_IMAGE);
    }
}
