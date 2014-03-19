package org.lobobrowser.html.renderer;

import org.lobobrowser.html.*;
import org.lobobrowser.html.domimpl.*;
import org.lobobrowser.html.js.*;
import org.mozilla.javascript.*;
import java.util.logging.*;

class HtmlController implements Controller {
	private static final Logger logger = Logger.getLogger(HtmlController.class.getName());
	private static final HtmlController instance = new HtmlController();
	
	public static HtmlController getInstance() {
		return instance;
	}
	
	/**
	 * @return True to propagate further and false if the event was consumed.
	 */
	public boolean onEnterPressed(ModelNode node) {
		if(node instanceof HTMLInputElementImpl) {
			HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
			if(hie.isSubmittableWithEnterKey()) {
				hie.submitForm(null);
				return false; 
			}
		}
		// No propagation
		return false;
	}

	/**
	 * @return True to propagate further and false if the event was consumed.
	 */
	public boolean onMouseClick(ModelNode node) {
		if(logger.isLoggable(Level.INFO)) {
			logger.info("onMouseClick(): node=" + node + ",class=" + node.getClass().getName());
		}
		if(node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			Function f = uiElement.getOnclick();
			if(f != null) {
				if(!Executor.executeFunction(uiElement, f)) {
					return false;
				}
			}
		}
		if(node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).navigate();
			return false;
		}
		else if(node instanceof HTMLButtonElementImpl) {
			HTMLButtonElementImpl button = (HTMLButtonElementImpl) node;
			String rawType = button.getAttribute("type");
			String type;
			if(rawType == null) {
				type = "submit";
			}
			else {
				type = rawType.trim().toLowerCase();
			}
			if("submit".equals(type)) {
				FormInput[] formInputs = new FormInput[] { new FormInput(button.getName(), button.getValue()) };
				button.submitForm(formInputs);
			}
			else if("reset".equals(type)) {
				button.resetForm();
			}
			else {
				// NOP for "button"!
			}
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if(parent == null) {
			return true;
		}
		return this.onMouseClick(parent);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onDoubleClick(ModelNode node) {
		if(logger.isLoggable(Level.INFO)) {
			logger.info("onDoubleClick(): node=" + node + ",class=" + node.getClass().getName());
		}
		if(node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			Function f = uiElement.getOndblclick();
			if(f != null) {
				if(!Executor.executeFunction(uiElement, f)) {
					return false;
				}
			}
		}
		ModelNode parent = node.getParentModelNode();
		if(parent == null) {
			return true;
		}
		return this.onDoubleClick(parent);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseDisarmed(ModelNode node) {
		if(node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor(null);
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if(parent == null) {
			return true;
		}
		return this.onMouseUp(parent);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseDown(ModelNode node) {
		boolean pass = true;
		if(node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			Function f = uiElement.getOnmousedown();
			if(f != null) {
				pass = Executor.executeFunction(uiElement, f);
			}
		}
		if(node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor("#9090FF80");
			return false;
		}
		if(!pass) {
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if(parent == null) {
			return true;
		}
		return this.onMouseDown(parent);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseUp(ModelNode node) {
		boolean pass = true;
		if(node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			Function f = uiElement.getOnmouseup();
			if(f != null) {
				pass = Executor.executeFunction(uiElement, f);
			}
		}
		if(node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor(null);
			return false;
		}
		if(!pass) {
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if(parent == null) {
			return true;
		}
		return this.onMouseUp(parent);
	}

	/**
	 * @param node The node generating the event.
	 * @param x For images only, x coordinate of mouse click.
	 * @param y For images only, y coordinate of mouse click.
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onPressed(ModelNode node, int x, int y) {
		if(node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			Function f = uiElement.getOnclick();
			if(f != null) {
				if(!Executor.executeFunction(uiElement, f)) {
					return false;
				}
			}
		}
		if(node instanceof HTMLInputElementImpl) {
			HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
			if(hie.isSubmitInput()) {
				hie.submitForm(new FormInput[] { new FormInput(hie.getName(), hie.getValue()) });
			}
			else if(hie.isImageInput()) {
				String name = hie.getName();
				String prefix = name == null ? "" : name + ".";
				FormInput[] extraFormInputs = new FormInput[] {
					new FormInput(prefix + "x", String.valueOf(x)),
					new FormInput(prefix + "y", String.valueOf(y))
				};
				hie.submitForm(extraFormInputs);
			}
			else if(hie.isResetInput()) {
				hie.resetForm();
			}
		}
		// No propagate
		return false;
	}	
}
