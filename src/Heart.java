import bagel.Image;

public abstract class Heart {
    private final Image HEART_IMAGE;
    private final double INITIAL_X = 100.0;
    private final double INITIAL_Y = 15.0;

    public Heart(Image image) {
        this.HEART_IMAGE = image;
    }

    public void renderHeart(int space) {
        HEART_IMAGE.drawFromTopLeft(INITIAL_X + space, INITIAL_Y);
    }


}
