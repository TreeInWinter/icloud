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
 * Created on Nov 19, 2005
 */
package org.lobobrowser.html.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.HTMLTableElementImpl;
import org.lobobrowser.html.domimpl.ModelNode;

class RTable extends BaseElementRenderable {
	private final TableMatrix tableMatrix;
	
	public RTable(HTMLTableElementImpl modelNode, UserAgentContext pcontext, HtmlRendererContext rcontext, FrameContext frameContext, RenderableContainer container) {
		super(container, modelNode, pcontext);
		this.tableMatrix = new TableMatrix(modelNode, pcontext, rcontext, frameContext, this, this);
	}

	public int getVAlign() {
		// Not used
		return VALIGN_BASELINE;
	}

	protected void applyStyle() {
		super.applyStyle();
		Insets bi = this.borderInsets;
		if(bi == null) {
			HTMLTableElementImpl element = (HTMLTableElementImpl) this.modelNode;
			String borderText = element.getAttribute("border");
			int border = 0;
			if(borderText != null) {
				try {
					border = Integer.parseInt(borderText);
					if(border < 0) {
						border = 0;
					}
				} catch(NumberFormatException nfe) {
					// ignore
				}
			}
			if(border > 0) {
				this.borderInsets = new Insets(border, border, border, border);
			}
		}
		if(this.borderTopColor == null && this.borderLeftColor == null) {
			this.borderTopColor = this.borderLeftColor = Color.LIGHT_GRAY;
		}
		if(this.borderBottomColor == null && this.borderRightColor == null) {
			this.borderBottomColor = this.borderRightColor = Color.DARK_GRAY;
		}
	}

	public void paint(Graphics g) {
		try {
			this.prePaint(g);
			Dimension size = this.getSize();
			//TODO: No scrollbars
			TableMatrix tm = this.tableMatrix;
			tm.paint(g, size);
		} finally {
			// Must always call super implementation
			super.paint(g);
		}
	}
	
	private volatile int lastAvailWidth = -1;
	private volatile int lastAvailHeight = -1;
//	private volatile Dimension layoutSize = null;
	
	public void doLayout(int availWidth, int availHeight, boolean expandWidth, boolean expandHeight) {
		if (availWidth != this.lastAvailWidth || availHeight != this.lastAvailHeight) {
			this.clearGUIComponents();
			this.clearDelayedPairs();
			this.applyStyle();
			this.lastAvailHeight = availHeight;
			this.lastAvailWidth = availWidth;
			TableMatrix tm = this.tableMatrix;
			Insets insets = this.getInsets(false, false);
			tm.reset(insets, availWidth, availHeight);
			//TODO: No scrollbars
			tm.build(availWidth, availHeight);
			tm.doLayout(insets);
			this.width = tm.getTableWidth();
			this.height = tm.getTableHeight();
			
			//TODO: Important: Absolutely
			//positioned blocks below an absolutely
			//positioned table go missing.
			
		} else {
			// Nothing to do - dimensions already set.
		}
		this.sendGUIComponentsToParent();
		this.sendDelayedPairsToParent();
	}
	
//	/* (non-Javadoc)
//	 * @see org.xamjwg.html.renderer.UIControl#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
//	 */
//	public boolean paintSelection(Graphics g, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
//		return this.tableMatrix.paintSelection(g, inSelection, startPoint, endPoint);
//	}
//
//	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
//		return this.tableMatrix.extractSelectionText(buffer, inSelection, startPoint, endPoint);
//	}
	
	public void invalidateLayoutLocal() {
		super.invalidateLayoutLocal();
		this.lastAvailHeight = -1;
		this.lastAvailWidth = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderablePoint(int, int)
	 */
	public RenderableSpot getLowestRenderableSpot(int x, int y) {
		RenderableSpot rs = this.tableMatrix.getLowestRenderableSpot(x, y);
		if(rs != null) {
			return rs;
		}
		return new RenderableSpot(this, x, y);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseClick(java.awt.event.MouseEvent, int, int)
	 */
	public boolean onMouseClick(MouseEvent event, int x, int y) {
		return this.tableMatrix.onMouseClick(event, x, y);
	}

	public boolean onDoubleClick(MouseEvent event, int x, int y) {
		return this.tableMatrix.onDoubleClick(event, x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseDisarmed(java.awt.event.MouseEvent)
	 */
	public boolean onMouseDisarmed(MouseEvent event) {
		return this.tableMatrix.onMouseDisarmed(event);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMousePressed(java.awt.event.MouseEvent, int, int)
	 */
	public boolean onMousePressed(MouseEvent event, int x, int y) {
		return this.tableMatrix.onMousePressed(event, x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseReleased(java.awt.event.MouseEvent, int, int)
	 */
	public boolean onMouseReleased(MouseEvent event, int x, int y) {
		return this.tableMatrix.onMouseReleased(event, x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RCollection#getRenderables()
	 */
	public Iterator getRenderables() {
		return this.tableMatrix.getRenderables();
	}

	public void repaint(ModelNode modelNode) {
		//NOP
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContainer#getBackground()
	 */
	public Color getPaintedBackgroundColor() {
		return this.container.getPaintedBackgroundColor();
	}
	
	
	public String toString() {
		return "RTable[this=" + System.identityHashCode(this) + ",node=" + this.modelNode + "]";
	}
}
