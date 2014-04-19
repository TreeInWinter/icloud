package org.lobobrowser.html.renderer;

import org.lobobrowser.html.domimpl.ModelNode;

/**
 * A controller that takes care of renderer events.
 */
interface Controller {	
	public boolean onPressed(ModelNode node, int x, int y);
	public boolean onEnterPressed(ModelNode node);
	public boolean onMouseClick(ModelNode node);
	public boolean onMouseDown(ModelNode node);
	public boolean onMouseUp(ModelNode node);
	public boolean onMouseDisarmed(ModelNode node);	
}
