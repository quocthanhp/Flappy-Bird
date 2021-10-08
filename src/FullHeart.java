import bagel.Image;

public class FullHeart extends Heart {
    private static final Image FULL_HEART_IMAGE = new Image("res/level/fullLife.png");

    public FullHeart() {
        super(FULL_HEART_IMAGE);
    }
}
