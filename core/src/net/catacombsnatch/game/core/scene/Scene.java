package net.catacombsnatch.game.core.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.screen.Updateable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Scene extends Stage implements Updateable, Tickable {
	/** The background image */
	protected TextureRegion background;
    protected Color backgroundColor = new Color(1f, 1f, 1f, 1f);
	
	private boolean exit = false;
	private boolean drawBackground = true;
	private final Rectangle currentActorRect = new Rectangle();
	private Vector2 mousePos = new Vector2();

    public Scene() {
        super(new ScreenViewport());
    }
	
	/**
	 * Called whenever this scene is getting created (or opened again).
	 * For creation only actions use the constructor.
	 * 
	 * @param scene The {@link Scene} that was previously open.
	 */
	public void enter(Scene scene) {
		EventManager.registerListener(this);
	}
	
	/**
	 * Called when the game switches to a new scene while the old one
	 * still remains "open".
	 * 
	 * @param scene The {@link Scene} being switched to.
	 */
	public void leave(Scene scene) {
		EventManager.unregisterListener(this);
	}
	
	/**
	 * Called whenever the scene is getting closed.
	 * This function should be used to dispose resources that are
	 * no longer needed.
	 */
	public void exit() {
		this.dispose();
		exit = true;
	}
	
	@Override
	public void update(boolean resize) {}
	
	@Override
	public void tick(float delta) {
		// Draw background
		if(drawBackground && background != null) {
			getSpriteBatch().begin();
            getSpriteBatch().setColor(backgroundColor);
			getSpriteBatch().draw(background, 0, 0, Screen.getWidth(), Screen.getHeight());
			getSpriteBatch().end();
		}
		
		// Similar to Google's Android "Project Butter":
		// Updating mouse dependent stuff in render() 
		if (!Gdx.input.isTouched()) {
			mousePos = screenToStageCoordinates(mousePos.set(Gdx.input.getX(), Gdx.input.getY()));
			
			for (Actor a : getActors()) {
				currentActorRect.set(a.getX(), a.getY(), a.getWidth(), a.getHeight());
				
				for (EventListener el : a.getListeners()) {
					if (!(el instanceof InputListener)) continue;
					
					InputListener il = (InputListener) el;
					if (currentActorRect.contains(mousePos.x, mousePos.y)) {
						il.enter(null, mousePos.x, mousePos.y, -1, null);
					} else {
						il.exit(null, mousePos.x, mousePos.y, -1, null);
					}
				}
			}
		}
		
		super.draw();
		getSpriteBatch().begin();
	}
	
	/**
	 * Sets the scene background
	 * 
	 * @param background The background texture to set
	 */
	public void setBackground(TextureRegion background) {
		this.background = background;
	}

    /**
     * Returns the background color as mutable object
     *
     * @return Modifiable color instance
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }
	
	public boolean shouldDrawBackground() {
		return drawBackground;
	}
	
	public void setDrawBackground(boolean draw) {
		this.drawBackground = draw;
	}
	
	public boolean hasBeenClosed() {
		return exit;
	}
	
	/**
	 * Adds a new text button
	 * 
	 * @param text The text it contains
	 * @param x The x-position
	 * @param y The y-position
	 * @return The created {@link TextButton}
	 */
	public TextButton addTextButton(String text, int x, int y) {
		final TextButton button = new TextButton(text, Art.skin);
		button.setPosition(x, y);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				button.act(Gdx.graphics.getDeltaTime());
			}
		});
		addActor(button);
		
		return button;
	}
	
	public TextButton getTextButton(int index) {
		int size = getActors().size;
		for(int i = 0; i < size; i++) {
			Actor actor = getActors().get(i);
			if(actor instanceof TextButton && i == index) return (TextButton) actor;
		}
		
		return null;
	}

    public SpriteBatch getSpriteBatch() {
        return (SpriteBatch) getBatch();
    }
}
