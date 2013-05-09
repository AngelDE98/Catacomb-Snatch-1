package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.event.input.Key;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.Difficulty;
import net.catacombsnatch.game.core.world.World;
import net.catacombsnatch.game.core.world.World.MapRotation;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;

import com.badlogic.gdx.math.Rectangle;

public class InGameScene extends Scene {
	protected boolean initialized = false;
	public PauseScreen paused;
	
	/** The world we are playing in */
	protected World world;
	
	protected List<View> views;
	
	public InGameScene() {
		super();
	}
	
	public void init(Level level) {
		world = new World(Difficulty.EASY, MapRotation.ONCE);
		world.getLevels().add(level);
		
		views = new ArrayList<View>();
		views.add(new View(level));
		
		initialized = true;
		update(true);
	}
	
	@Override
	public void render(float delta) {
		if(!initialized) return;
		
		// Check keyboard inputs
		int mx = 0, my = 0;
		
		if (paused == null) {
			if(InputManager.isPressed(Key.MOVE_LEFT)){
				mx = mx-10;
			}
			if(InputManager.isPressed(Key.MOVE_RIGHT)){
				mx = mx+10;
			}
			if(InputManager.isPressed(Key.MOVE_UP)){
				my = my+10;
			}
			if(InputManager.isPressed(Key.MOVE_DOWN)){
				my = my-10;
			}
			
			if (InputManager.isPressed(Key.BACK)){
				//SceneManager.switchTo(TitleScreen.class, true); // TODO
				SceneManager.switchTo(PauseScreen.class, false, this).blockBack = true; // TODO
			}
		}

		for(View view : views) {
			view.setTarget(view.getOffset().x + mx, view.getOffset().y + my);
		}
		
		// Just some overlays
		super.draw();
		getSpriteBatch().begin();
		
		if (paused == null) {
		// Tick, tock - the world is just a clock...
			world.tick(delta);
		}
		
		// Open the windows to actually see the outside!
		for(View view : views) {
			view.render(this);
		}
	}
	
	public void destroy(){
		initialized = false;
		world = null;
		views = null;
	}
	
	@Override
	public void update(boolean resize) {
		if(!initialized) return;
		
		if(resize) {
			for(View view : views) {
				view.setViewport(new Rectangle(0, 0, Screen.getWidth(), Screen.getHeight()));
				view.update(true);
			}
		}
	}
}
