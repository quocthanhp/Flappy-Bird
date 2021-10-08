import bagel.Image;

public class EmptyHeart extends Heart {
    private static final Image EMPTY_HEART_IMAGE = new Image("res/level/noLife.png");

    public EmptyHeart() {
        super(EMPTY_HEART_IMAGE);
    }
}
