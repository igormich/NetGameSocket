package game;

public class Tree extends GameObject{
    public Tree(float x, float y) {
        this.x = x;
        this.y = y;
        this.size = 8;
        this.z = 10;
    }
    public void onCollision(GameObject other, Game game) {
        if(other instanceof Bullet || other instanceof Player) {
            game.remove(this);
        }
    }
}
