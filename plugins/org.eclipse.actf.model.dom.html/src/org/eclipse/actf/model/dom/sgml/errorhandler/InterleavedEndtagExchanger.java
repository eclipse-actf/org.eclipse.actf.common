/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.dom.sgml.errorhandler;

import java.io.IOException;

import org.eclipse.actf.model.dom.html.HTMLParser;
import org.eclipse.actf.model.dom.sgml.ElementDefinition;
import org.eclipse.actf.model.dom.sgml.EndTag;
import org.eclipse.actf.model.dom.sgml.IErrorLogListener;
import org.eclipse.actf.model.dom.sgml.ParseException;
import org.eclipse.actf.model.dom.sgml.SGMLParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 *  A sample implementation class of {@link IErrorHandler}.
 *  This class is experimental.
 */
public class InterleavedEndtagExchanger implements IErrorHandler {
    /**
     *  Exchanges interleaved end tags.  For example, changes
     * <pre>
     *   &lt;center&gt;&lt;font&gt;...&lt;/center&gt;&lt;/font&gt;
     * </pre>
     * to
     * <pre>
     *   &lt;center&gt;&lt;font&gt;...&lt;/font&gt;&lt;/center&gt;
     * </pre>
     * @param code If {@link SGMLParser#SUDDEN_ENDTAG}, tries to exchange.  
     * @param errorNode sudden endtag.  It must be an instance of 
     * {@link EndTag}
     * Otherwise, does nothing.
     */
    public boolean handleError(int code, SGMLParser parser, Node errorNode)
	throws ParseException, IOException, SAXException {
	if (!(code == SUDDEN_ENDTAG && errorNode instanceof EndTag)) {
	    return false;
	}
	// searches an element without an unomittable endtag.
	for (Node pNode = parser.getContext();
	     pNode instanceof Element; pNode = pNode.getParentNode()) {
	    if (!pNode.getNodeName().equalsIgnoreCase(errorNode.getNodeName())) {
		ElementDefinition ed = parser.getDTD().getElementDefinition(pNode.getNodeName());
		// if found, try to exchange endtags.
		if (!ed.endTagOmittable() && exchangeEndtag(parser, errorNode, ed.getName())) {
		    return true;
		}
	    }
	}
	return false;
    }
    /**
     * @param target target endtag's name.
     */
    private boolean exchangeEndtag(SGMLParser parser, Node errorNode, String target)
	throws ParseException, IOException, SAXException {
	// makes look ahead buffer
	Node foBuf[] = new Node[SGMLParser.BUF_SIZ/2];  
	int i;
	// searchs an target endtag and store nodes to buffer simultaneously
	for (i = 0; i < SGMLParser.BUF_SIZ/2; i++) {
	    Node fo = parser.getNode();
	    if (fo instanceof EndTag) {
		// If found exchange endtags and pushes back them to the parser
		if (fo.getNodeName().equalsIgnoreCase(target)) {
		    parser.error(SUDDEN_ENDTAG, errorNode + " and " + fo + 
				 " are interleaved. So exchange it by an error handler.");
		    parser.pushBackNode(errorNode);
		    while (i > 0) parser.pushBackNode(foBuf[--i]);
		    parser.pushBackNode(fo);
		    return true;
		}
	    }
	    foBuf[i] = fo;
	}
	while (i > 0) parser.pushBackNode(foBuf[--i]);
	return false;
    }
    /**
     *  Just for tests.
     */
    public static void main(String args[]) {
	try {
	    HTMLParser parser = new HTMLParser();
	    parser.addErrorLogListener(new IErrorLogListener() {
		    public void errorLog(int code, String msg) {
			System.err.println(msg); 
		    }
		});
	    parser.setErrorHandler(new InterleavedEndtagExchanger());
	    parser.parse(new java.io.FileInputStream(args[0]));
	} catch (Exception e) {
	}
    }
}
