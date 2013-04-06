package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Minimap implements Renderable {
	protected Level level;
	protected Sprite sprite;
	
	protected Texture map;
	protected Pixmap pm;
	
	public Minimap(Level level) {
		this.level = level;
		
		sprite = new Sprite(Art.skin.getAtlas().findRegion("minimap-frame"));
		
		pm = new Pixmap( 40, 40, Pixmap.Format.RGBA8888 );
		map = new Texture( pm, true);
		pm = new Pixmap( 40, 40, Pixmap.Format.RGBA8888 );
	}

	@Override
	public void render(Scene scene) {
		sprite.draw(scene.getSpriteBatch());
		
		for (Layer layer : level.layers) {
			for (Tile tile : layer.tiles) {
				Color c = tile.getMinimapColor();
				int x = (int) tile.getBounds().x;
				int y = (int) tile.getBounds().y;
				pm.setColor(c);
				pm.drawRectangle(x, y, 1, 1);
			}
		}
		map.draw(pm, 0, 0);
		
		scene.getSpriteBatch().draw(map, sprite.getX() + 6, sprite.getY() + 5, 80, 80);
	}
	
	public void update(boolean resize) {
		if(resize) {
			sprite.setPosition(Screen.getWidth() - sprite.getWidth() - 2, Screen.getHeight() - sprite.getHeight());
		}
	}
	
}