package game;

public class Bullet extends GameObject{

    private long ownerID;

    public Bullet(float x, float y, int sx, int sy, long ownerID) {
        this.ownerID = ownerID;
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        System.out.println(sx);
        this.size = 10;
    }

    public long getOwnerID() {
        return ownerID;
    }
    public void onCollision(GameObject other, GameImpl game) {
        if (other instanceof Player p && getOwnerID() == p.getID()) {
           return;
        }
        game.remove(this);
    }

}
