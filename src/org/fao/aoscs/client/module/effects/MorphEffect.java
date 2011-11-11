package org.fao.aoscs.client.module.effects;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MorphEffect {
	private static final double DEFAULT_OPACITY = 0.50;
	private static final String DEFAULT_BACKGROUND_COLOR = "#99CCFF";
	private static final String DEFAULT_BORDER_STYLE = "1px solid #0C9DD5";
	
	private static final int DEFAULT_DURATION = 150;
	private static final int DEFAULT_STEPS = 7;
	private static final int DEFAULT_DELAY = 1;
	
	private int durationStep;
	
	private boolean hasStart, hasEnd;
	private int startLeft, startTop, startWidth, startHeight;
	private int endLeft, endTop, endWidth, endHeight;
	private Command command;
	
	private double opacity = DEFAULT_OPACITY;
	private String backgroundColor = DEFAULT_BACKGROUND_COLOR;
	private String borderStyle = DEFAULT_BORDER_STYLE;
	private int duration = DEFAULT_DURATION;
	private int steps = DEFAULT_STEPS;
	private int delay = DEFAULT_DELAY;
	
	private Timer timer;
	private int currentStep;
	private double currentLeft, currentTop, currentWidth, currentHeight;
	private double incrementLeft, incrementTop, incrementWidth, incrementHeight;
	
	private AbsolutePanel box;
	
	public MorphEffect start(Widget start) {
		this.startLeft = start.getAbsoluteLeft();
		this.startTop = start.getAbsoluteTop();
		
		this.startWidth = start.getOffsetWidth();
		this.startHeight = start.getOffsetHeight();
		this.hasStart = true;
		
		return this;
	}
	
	public MorphEffect start(int startLeft, int startTop, int startWidth, int startHeight) {
		this.startLeft = startLeft;
		this.startTop = startTop;
		this.startWidth = startWidth;
		this.startHeight = startHeight;
		this.hasStart = true;
		
		return this;
	}
	
	public MorphEffect end(Widget end) {
		//this.endLeft = end.getAbsoluteLeft();
		//this.endTop = end.getAbsoluteTop();
		this.endLeft = ( Window.getClientWidth( ) - end.getOffsetWidth( )) / 2; 
		this.endTop = ( Window.getClientHeight( ) - end.getOffsetHeight( )) / 2; 
		this.endWidth = end.getOffsetWidth();
		this.endHeight = end.getOffsetHeight();
		this.hasEnd = true;
		
		return this;
	}
	
	public MorphEffect end(int endLeft, int endTop, int endWidth, int endHeight) {
		this.endLeft = endLeft;
		this.endTop = endTop;
		this.endWidth = endWidth;
		this.endHeight = endHeight;
		this.hasEnd = true;
		
		return this;
	}
	
	public MorphEffect opacity(double opacity) {
		if ( opacity < 0 || opacity > 1 ) throw new IllegalArgumentException(
				"opacity should be between 0 and 1 inclusive.");
		this.opacity = opacity;
		
		return this;
	}
	
	/**
	 * Sets the colour of the inside of the morphbox, then returns itself.
	 * <p>
	 * Default: Pastelly blue (#6666FF).
	 */
	public MorphEffect background(String colorString) {
		this.backgroundColor = colorString;
		return this;
	}
	
	/**
	 * Sets the full style of the border of the morphbox, then returns itself.
	 * You can set 'null' to not have a border at all.
	 * <p>
	 * Default: 1px solid somewhat darker blue than the default morphbox.
	 */
	public MorphEffect border(String borderStyle) {
		this.borderStyle = borderStyle;
		return this;
	}
	
	/**
	 * Sets the duration of the morph effect. Note that the command hook is called
	 * more than 'this' millis after starting the effect unless delay is 0.
	 * <p>
	 * Default: 150 milliseconds.
	 */
	public MorphEffect duration(int millis) {
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
	public MorphEffect steps(int steps) {
		this.steps = steps;
		
		return this;
	}
	
	/**
	 * How many 'steps' to wait after the animation is complete before calling
	 * the hook. In other words, duration/steps * this number milliseconds.
	 * <p>
	 * Default: 2
	 */
	public MorphEffect delay(int delay) {
		this.delay = delay;
		
		return this;
	}
	
	/**
	 * Configure the 'hook' to be called after the animation is over, and the extra
	 * delay time has passed. There can be only one hook, though you can pass 'null'
	 * to have no hook at all.
	 */
	public MorphEffect hook(Command hook) {
		this.command = hook;
		
		return this;
	}
	
	public MorphEffect go() {
		if ( duration == 0 ) {
			if ( command != null ) command.execute();
			return this;
		}
		
		if ( !hasStart ) throw new IllegalStateException("Define a start point");
		if ( !hasEnd ) throw new IllegalStateException("Define an end point");
		
		if ( timer != null ) timer.cancel();
		
		incrementLeft = ((double)(endLeft - startLeft)) / steps;
		incrementTop = ((double)(endTop - startTop)) / steps;
		incrementWidth = ((double)(endWidth - startWidth)) / steps;
		incrementHeight = ((double)(endHeight - startHeight)) / steps;
		currentStep = 0;
		
		durationStep = duration / steps;
		
		currentLeft = startLeft;
		currentTop = startTop;
		currentWidth = startWidth;
		currentHeight = startHeight;
		
		if ( durationStep < 1 ) durationStep = 1;
		
		if ( box == null ) box = new AbsolutePanel();
		Element h = box.getElement();
		
		ExtDOM.setOpacity(box, opacity);
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
		if ( currentStep <= steps ) {
			currentLeft += incrementLeft;
			currentTop += incrementTop;
			currentWidth += incrementWidth;
			currentHeight += incrementHeight;
		}
		
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
		box.setPixelSize((int)currentWidth, (int)currentHeight);
		RootPanel.get().add(box, (int)currentLeft, (int)currentTop);
	}
}
