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
 * Created on Oct 22, 2005
 */
package org.lobobrowser.html.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.gui.SelectionChangeEvent;
import org.lobobrowser.html.gui.SelectionChangeListener;

/**
 * A Swing frame that can be used to test the
 * Cobra HTML rendering engine. 
 */
public class TestFrame extends JFrame {	
	private static final Logger logger = Logger.getLogger(TestFrame.class.getName());
	private final SimpleHtmlRendererContext rcontext;
	private final JTree tree;
	private final HtmlPanel htmlPanel;
	private final JTextArea textArea;
	
	public TestFrame() throws HeadlessException {
		this("");
	}
	
	public TestFrame(String title) throws HeadlessException {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		final JTextField textField = new JTextField();
		JButton button = new JButton("Parse & Render");
		final JTabbedPane tabbedPane = new JTabbedPane();
		final JTree tree = new JTree();
		final JScrollPane scrollPane = new JScrollPane(tree);
		
		this.tree = tree;
		
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(bottomPanel, BorderLayout.CENTER);
		
		topPanel.add(new JLabel("URL: "), BorderLayout.WEST);
		topPanel.add(textField, BorderLayout.CENTER);
		topPanel.add(button, BorderLayout.EAST);
		
		bottomPanel.add(tabbedPane, BorderLayout.CENTER);
		
		final HtmlPanel panel = new HtmlPanel();
		panel.addSelectionChangeListener(new SelectionChangeListener() {
			public void selectionChanged(SelectionChangeEvent event) {
				if(logger.isLoggable(Level.INFO)) {
					logger.info("selectionChanged(): selection node: " + panel.getSelectionNode());
				}
			}
		});
		this.htmlPanel = panel;	
		this.rcontext = new SimpleHtmlRendererContext(panel);
		
		final JTextArea textArea = new JTextArea();
		this.textArea = textArea;
		textArea.setEditable(false);
		final JScrollPane textAreaSp = new JScrollPane(textArea);
		
		tabbedPane.addTab("HTML", panel);
		tabbedPane.addTab("Tree", scrollPane);
		tabbedPane.addTab("Source", textAreaSp);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Component component = tabbedPane.getSelectedComponent();
				if(component == scrollPane) {
					tree.setModel(new NodeTreeModel(panel.getRootNode()));
				}
				else if(component == textAreaSp) {
					textArea.setText(rcontext.getSourceCode());
				}
			}
		});
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				process(textField.getText());
			}
		});
	}
	
	private void process(String uri) {
		try {
			URL url;
			try {
				url = new URL(uri);
			} catch(java.net.MalformedURLException mfu) {
				int idx = uri.indexOf(':');
				if(idx == -1 || idx == 1) {
					// try file
					url = new URL("file:" + uri);
				}
				else {
					throw mfu;
				}
			}
			// Call SimpleHtmlRendererContext.navigate()
			// which implements incremental rendering.
			this.rcontext.navigate(url, null);
		} catch(Exception err) {
			logger.log(Level.SEVERE, "Error trying to load URI=[" + uri + "].", err);
		}
	}

// Old parse+render method commented out below:
	
//	private void process(String uri) {
//		try {
//			URL url;
//			try {
//				url = new URL(uri);
//			} catch(java.net.MalformedURLException mfu) {
//				int idx = uri.indexOf(':');
//				if(idx == -1 || idx == 1) {
//					// try file
//					url = new URL("file:" + uri);
//				}
//				else {
//					throw mfu;
//				}
//			}
//			logger.info("process(): Loading URI=[" + uri + "].");
//			long time0 = System.currentTimeMillis();
//			URLConnection connection = url.openConnection();
//			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible;) Cobra/0.96.1+");
//			connection.setRequestProperty("Cookie", "");
//			if(connection instanceof HttpURLConnection) {
//				HttpURLConnection hc = (HttpURLConnection) connection;
//				hc.setInstanceFollowRedirects(true);
//				int responseCode = hc.getResponseCode();
//				logger.info("process(): HTTP response code: " + responseCode);
//			}
//			InputStream in = connection.getInputStream();
//			byte[] content;
//			try {
//				content = IO.load(in, 8192);
//			} finally {
//				in.close();
//			}
//			String source = new String(content, "ISO-8859-1");
//			this.textArea.setText(source);
//			long time1 = System.currentTimeMillis();
//			InputStream bin = new MyByteArrayInputStream(content);
//			HtmlParserContext context = new SimpleHtmlParserContext();
//			HtmlRendererContext rcontext = new SimpleHtmlRendererContext(this.htmlPanel, context);
//			DocumentBuilderImpl builder = new DocumentBuilderImpl(context, rcontext);
//			// Provide a proper URI, in case it was a file.
//			String actualURI = url.toExternalForm();
//			Document document = builder.parse(new InputSourceImpl(bin, actualURI, "ISO-8859-1"));
//			long time2 = System.currentTimeMillis();
//			logger.info("Parsed URI=[" + uri + "]: Parse elapsed: " + (time2 - time1) + " ms. Load elapsed: " + (time1 - time0) + " ms.");
//			this.tree.setModel(new NodeTreeModel(document));
//			this.htmlPanel.setDocument(document, rcontext, context);
//		} catch(Exception err) {
//			logger.log(Level.SEVERE, "Error trying to load URI=[" + uri + "].", err);
//		}
//	}
}
