/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: xamjadmin@users.sourceforge.net
*/
/*
 * Created on Apr 17, 2005
 */
package org.lobobrowser.html.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Iterator;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.ModelNode;
import org.lobobrowser.html.domimpl.NodeImpl;
import org.lobobrowser.html.domimpl.UINode;
import org.lobobrowser.html.style.RenderState;

/**
 * @author J. H. S.
 */
class RUIControl extends BaseElementRenderable implements RElement {
	public final UIControl widget;
	private final ModelNode modelNode;
	private final FrameContext frameContext;
	
	public RUIControl(ModelNode me, UIControl widget, RenderableContainer container, FrameContext frameContext, UserAgentContext ucontext) {
		super(container, me, ucontext);
		this.modelNode = me;
		this.widget = widget;
		this.frameContext = frameContext;
		widget.setRUIControl(this);
	}
	
	public void focus() {
		super.focus();
		java.awt.Component c = this.widget.getComponent();
		c.requestFocus();
	}
	
	public final void invalidateLayoutLocal() {
		// Invalidate widget (some redundancy)
		super.invalidateLayoutLocal();
		this.widget.invalidate();
		// Invalidate cached values
		this.lastAvailHeight = -1;
		this.lastAvailWidth = -1;
	}
	
	public int getVAlign() {
		return this.widget.getVAlign();
	}

	public final void paint(Graphics g) {
		// We need to paint the GUI component.
		// For various reasons, we need to do that
		// instead of letting AWT do it.
		this.widget.paint(g);
		// Super implementation marks style validated,
		// and must always be called.
		super.paint(g);
	}
	
	public boolean onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		if(me != null) {
			return HtmlController.getInstance().onMouseClick(me);
		}
		else {
			return true;
		}
	}

	public boolean onDoubleClick(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		if(me != null) {
			return HtmlController.getInstance().onDoubleClick(me);
		}
		else {
			return true;
		}
	}

	public boolean onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		if(me != null) {
			return HtmlController.getInstance().onMouseDown(me);
		}
		else {
			return true;
		}
	}

	public boolean onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		if(me != null) {
			return HtmlController.getInstance().onMouseUp(me);
		}
		else {
			return true;
		}
	}
	
	public boolean onMouseDisarmed(java.awt.event.MouseEvent event) {
		ModelNode me = this.modelNode;
		if(me != null) {
			return HtmlController.getInstance().onMouseDisarmed(me);
		}
		else {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#invalidateState(org.xamjwg.html.renderer.RenderableContext)
	 */
	public void invalidateRenderStyle() {
		//NOP - No RenderStyle below this node.
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.ContainingBlockContext#repaint(org.xamjwg.html.renderer.RenderableContext)
	 */
	public void repaint(ModelNode modelNode) {
		Object widget = this.widget;
		if(widget instanceof UINode) {
			((UINode) widget).repaint(modelNode);
		}
		else {
			this.repaint();
		}
	}
	
	public void updateWidgetBounds(int guiX, int guiY) {
		// Overrides
		super.updateWidgetBounds(guiX, guiY);
		this.widget.setBounds(guiX, guiY, this.width, this.height);
	}
	
	public Color getBlockBackgroundColor() {
		return this.widget.getBackgroundColor();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
		inSelection = super.paintSelection(g, inSelection, startPoint, endPoint);
		if(inSelection) {
			Color over = new Color(0, 0, 255, 50);
			if(over != null) {
				Color oldColor = g.getColor();
				try {
					g.setColor(over);
					g.fillRect(0, 0, this.width, this.height);
				} finally {
					g.setColor(oldColor);
				}				
			}
		}
		return inSelection;
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
		// No text here
		return inSelection;
	}
	
	public RenderableSpot getLowestRenderableSpot(int x, int y) {
		// Nothing draggable - return self
		return new RenderableSpot(this, x, y);
	}

	private int lastAvailWidth = -1;
	private int lastAvailHeight = -1;
	private int declaredWidth = -1;
	private int declaredHeight = -1;
	
	public void doLayout(int availWidth, int availHeight, boolean expandWidth, boolean expandHeight) {
		if(availWidth != this.lastAvailWidth || availHeight != this.lastAvailHeight) {
			this.applyStyle();
			this.lastAvailWidth = availWidth;
			this.lastAvailHeight = availHeight;
			UIControl widget = this.widget;
			widget.reset(availWidth, availHeight);
			RenderState renderState = this.modelNode.getRenderState();
			int dw = this.getDeclaredWidth(renderState, availWidth);
			int dh = this.getDeclaredHeight(renderState, availHeight);
			this.declaredWidth = dw;
			this.declaredHeight = dh;
			if(dw == -1 || dh == -1) {
				Dimension size = widget.getPreferredSize();
				if(dw == -1) {
					dw = size.width;
				}
				if(dh == -1) {
					dh = size.height;
				}
			}
			this.width = dw;
			this.height = dh;
		}
	}

	/** 
	 * May be called by controls when
	 * they wish to modifiy their preferred
	 * size (e.g. an image after it's loaded). 
	 * This method must be called
	 * in the GUI thread.
	 */
	public final void preferredSizeInvalidated() {
		int dw = RUIControl.this.declaredWidth;
		int dh = RUIControl.this.declaredHeight;
		if(dw == -1 || dh == -1) {
			this.frameContext.delayedRelayout((NodeImpl) this.modelNode);
		}
		else {
			RUIControl.this.repaint();
		}
	}
	
	public Iterator getRenderables() {
		// No children for GUI controls
		return null;
	}

	public Color getPaintedBackgroundColor() {
		return this.container.getPaintedBackgroundColor();
	}

	public Insets getInsets() {
		return RBlockViewport.ZERO_INSETS;
	}
}
