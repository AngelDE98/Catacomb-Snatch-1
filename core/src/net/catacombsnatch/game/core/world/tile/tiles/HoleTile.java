package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.Direction;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;
import net.catacombsnatch.game.core.world.tile.StaticTile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HoleTile extends StaticTile {
	
	public HoleTile() {
		super(0x000000FF); // Black
	}

	@Override
	public void init(Level level, int x, int y) {
		this.level = level;
		setRandomTexture(Art.tiles_hole);
		
		super.init(level, x, y);
	}
	
	@Override
	public void update() {}
	
	protected Tile tmptile;
	
	@Override
	public void render(SpriteBatch graphics, View view) {
		Tile northtile = getRelative(Direction.NORTH);
		if (northtile != null && (northtile instanceof WallTile || northtile instanceof DestroyableWallTile)) {
			if (tmptile == null) {
				if (northtile instanceof WallTile) {
					tmptile = new FloorTile();
				}
				if (northtile instanceof DestroyableWallTile) {
					Class<? extends Tile> tileclass = ((DestroyableWallTile)northtile).destroy();
					if (tileclass != null) {
						try {
							tmptile = tileclass.newInstance();
						} catch (Exception e) {
						}
					}
				}
				if (tmptile != null) {
					tmptile.init(level, (int)bb.x/WIDTH, (int)bb.y/HEIGHT-1);
				}
			}
			northtile = tmptile;
		}
		if (northtile != null && !(northtile instanceof HoleTile)) {
			northtile.getBounds().y+=HEIGHT;
			northtile.render(graphics, view);
            northtile.getBounds().y-=HEIGHT;
		}
		renderTile(graphics, view, region);
	}

	@Override
	public boolean canPass(long entity) {
		// TODO Kill entity by "falling"
		
		return true;
	}

}
