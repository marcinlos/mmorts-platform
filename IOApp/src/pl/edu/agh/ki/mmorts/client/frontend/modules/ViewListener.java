package pl.edu.agh.ki.mmorts.client.frontend.modules;

import android.graphics.Canvas;

public interface ViewListener {

	/**
	 * Method called by the View on each of subscribed Presenters. Use canvas to draw everything
	 * you feel like. We are adults - if implemented incorrectly (especially among
	 * few presenters that are subscribed to one view) elements will overlap and universes will explode.
	 * @param c Android drawing field
	 */
	public void drawStuff(Canvas c);
	
	/**
	 * Example event handling (could be multitude of methods, each attached to a listener
	 * within view). Here you implement logic of reacting to player input.
	 * Like building or destroying a building. Or something.
	 */
	public void touchEvent(float x, float y);
}
