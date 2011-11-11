package org.fao.aoscs.client.module.effects;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlashEffect {
	private static final String DEFAULT_BACKGROUND_COLOR = "#FFDDDD";
	private static final String DEFAULT_BORDER_STYLE = "none";
	
	private static final int DEFAULT_DURATION = 350;
	private static final int DEFAULT_STEPS = 7;
	private static final int DEFAULT_DELAY = 0;
	
	private int durationStep;
	private int top, left, width, height;
	private boolean hasCoords;
	private Command command;
	
	private String backgroundColor = DEFAULT_BACKGROUND_COLOR;
	private String borderStyle = DEFAULT_BORDER_STYLE;
	private int duration = DEFAULT_DURATION;
	private int steps = DEFAULT_STEPS;
	private int delay = DEFAULT_DELAY;
	
	private Timer timer;
	private int currentStep;
	private double decrement;
	private double current;
	
	private AbsolutePanel box;
	
	public FlashEffect widget(Widget widget) {
		hasCoords = true;
		
		left = widget.getAbsoluteLeft();
		top = widget.getAbsoluteTop();
		width = widget.getOffsetWidth();
		height = widget.getOffsetHeight();
		
		return this;
	}
	
	public FlashEffect coordinates(int left, int top, int width, int height) {
		hasCoords = true;
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		
		return this;
	}
	
	/**
	 * 
	 * Sets the colour of the inside of the flash box, then returns itself.
	 * <p>
	 * Default: Creamy Yellow (#FFFF66).
	 */
	public FlashEffect background(String colorString) {
		this.backgroundColor = colorString;
		return this;
	}
	
	/**
	 * Sets the full style of the border of the flash box, then returns itself.
	 * You can set 'null' to not have a border at all.
	 * <p>
	 * Default: no border.
	 */
	public FlashEffect border(String borderStyle) {
		this.borderStyle = borderStyle;
		return this;
	}
	
	/**
	 * Sets the duration of the flash effect. Note that the command hook is called
	 * more than 'this' millis after starting the effect unless delay is 0.
	 * <p>
	 * Default: 350 milliseconds.
	 */
	public FlashEffect duration(int millis) {
		if ( millis < 0 ) throw new IllegalStateException(
				"Durations must be positive");
		this.duration = millis;
		
		return this;
	}
	
	/**
	 * How many 'steps' the animation covers.
	 * More is smoother but also more CPU intensive. Make sure this is an exact
	 * divisor of the duration!
	 * <p>
	 * Default: 7
	 */
	public FlashEffect steps(int steps) {
		this.steps = steps;
		
		return this;
	}
	
	/**
	 * How many 'steps' to wait after the animation is complete before calling
	 * the hook. In other words, duration/steps * this number milliseconds.
	 * <p>
	 * Default: 0
	 */
	public FlashEffect delay(int delay) {
		this.delay = delay;
		
		return this;
	}
	
	/**
	 * Configure the 'hook' to be called after the animation is over, and the extra
	 * delay time has passed. There can be only one hook, though you can pass 'null'
	 * to have no hook at all.
	 */
	public FlashEffect hook(Command hook) {
		this.command = hook;
		
		return this;
	}
	
	public FlashEffect go() {
		if ( duration == 0 ) {
			if ( command != null ) command.execute();
			return this;
		}
		
		if ( !hasCoords ) throw new IllegalStateException("Define a widget or coordinates");
		
		if ( timer != null ) timer.cancel();
		
		currentStep = 0;
		
		current = 1;
		decrement = (1.0 / steps);
		
		durationStep = duration / steps;
		
		if ( durationStep < 1 ) durationStep = 1;
		
		if ( box == null ) box = new AbsolutePanel();
		Element h = box.getElement();
		
		ExtDOM.setOpacity(box, current);
		DOM.setStyleAttribute(h, "position", "absolute");
		DOM.setStyleAttribute(h, "backgroundColor", backgroundColor);
		if ( borderStyle == null ) DOM.setStyleAttribute(h, "borderStyle", "none");
		else DOM.setStyleAttribute(h, "border", borderStyle);
		
		startTimer();
		
		return this;
	}
	
	private void startTimer() {
		if ( timer == null ) timer = new Timer() { public void run() {
			nextStep();
		}};
		
		timer.schedule(durationStep);
	}
	
	private void nextStep() {
		currentStep++;
		if ( currentStep <= steps ) current -= decrement;
		
		if ( currentStep <= steps + delay ) {
			drawBox();
			startTimer();
		} else {
			hideBox();
			if ( command != null ) command.execute();
		}
	}
	
	private void hideBox() {
		if ( box != null ) RootPanel.get().remove(box);
	}
	
	private void drawBox() {
		box.setPixelSize(width, height);
		RootPanel.get().add(box, left, top);
		ExtDOM.setOpacity(box, current);
	}
}
