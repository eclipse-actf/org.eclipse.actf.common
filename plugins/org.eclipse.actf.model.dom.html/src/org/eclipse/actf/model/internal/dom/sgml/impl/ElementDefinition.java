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

package org.eclipse.actf.model.internal.dom.sgml.impl;

import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.eclipse.actf.model.internal.dom.sgml.modelgroup.IModelGroup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Instance of this class is made from DTD.
 */
public class ElementDefinition implements IModelGroup {
	private boolean start, end;

	private String name;

	private IModelGroup contentModel;

	private ElementDefinition inclusions[];

	private ElementDefinition exclusions[];

	private AttributeDefinition attributeDefs[] = new AttributeDefinition[32];

	private int attributeDefHashes[] = new int[32];

	private int attrNum = 0;

	protected ElementDefinition(String name) {
		this.name = name;
	}

	protected ElementDefinition(String name, IModelGroup contentModel) {
		this.name = name;
		this.contentModel = contentModel;
	}

	final void addAttributeDefinition(AttributeDefinition def) {
		if (def != null) {
			if (attrNum == attributeDefs.length) {
				AttributeDefinition newDefs[] = new AttributeDefinition[attrNum * 2];
				int newHashes[] = new int[attrNum * 2];
				for (int i = 0; i < attrNum; i++) {
					newDefs[i] = attributeDefs[i];
					newHashes[i] = attributeDefHashes[i];
				}
				attributeDefs = newDefs;
				attributeDefHashes = newHashes;
			}
			attributeDefs[attrNum] = def;
			attributeDefHashes[attrNum++] = hashCode(def.getName()
					.toCharArray());
		}
	}

	/**
	 * Check if an end tag is omittable or not
	 * 
	 * @return true if an end tag is omittable, otherwise false.
	 */
	public final boolean endTagOmittable() {
		return end;
	}

	/**
	 * Matches <code>child</code> to <code>parent</code> by
	 * <code>parser</code>.
	 * 
	 * @return <code>true</code> if succeeded. Otherwise, <code>false</code>
	 */
	public boolean match(ISGMLParser parser, Node parent, Node child) {
		if (child instanceof Element
				&& child.getNodeName().equalsIgnoreCase(name)) {
			parent.appendChild(child);
			return true;
		}
		if (start) {
			Element tmp = parser.getDocument().createElement(
					parser.changeDefaultTagCase(name));
			// check excepiton
			if (child instanceof Element) {
				Element ec = (Element) child;
				if (exclusion(ec)) {
					return false;
				} else if (inclusion(ec)) {
					tmp.appendChild(child);
					parent.appendChild(tmp);
					parser.addAutoGenerated(tmp);
					return true;
				}
			}
			boolean ret = contentModel.match(parser, tmp, child);
			if (ret) {
				parent.appendChild(tmp);
				parser.addAutoGenerated(tmp);
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets content model
	 * 
	 * @return content model
	 */
	final IModelGroup getContentModel() {
		return contentModel;
	}

	/**
	 * Matches <code>child</code> to <code>parent</code> by
	 * <code>parser</code> as a conent. Suppose this instance is a definition
	 * derived from <code>&lt;!ELEMENT TBODY O O (TR)+&gt;,
	 * &lt;TR&gt; doesn't match &lt;TBODY&gt; in this method but in the {@link
	 * #match(ISGMLParser,org.w3c.dom.Node,org.w3c.dom.Node)} method.
	 * @return <code>true</code> if succeeded. Otherwise, <code>false</code>
	 */
	public boolean contentMatch(ISGMLParser parser, Node parent, Node child) {
		return contentModel.match(parser, parent, child);
	}

	/**
	 * gets default value of a specified attribute.
	 * 
	 * @param attrName
	 *            name of attribute
	 * @return default value. If <code>attrName</code> is not an attribute of
	 *         this object, null.
	 */
	public final String getDefaultValue(String attrName) {
		int hash = hashCode(attrName.toCharArray());
		for (int i = 0; i < attrNum; i++) {
			if (attributeDefHashes[i] == hash) {
				AttributeDefinition ad = attributeDefs[i];
				if (ad.getName().equalsIgnoreCase(attrName)) {
					return ad.getDefaultValue();
				}
			}
		}
		return null;
	}

	final boolean exclusion(Element element) {
		if (exclusions == null)
			return false;
		for (int i = exclusions.length - 1; i >= 0; i--) {
			if (exclusions[i].name.equalsIgnoreCase(element.getNodeName())) {
				return true;
			}
		}
		return false;
	}

	final boolean inclusion(Element element) {
		if (inclusions == null)
			return false;
		for (int i = inclusions.length - 1; i >= 0; i--) {
			if (inclusions[i].name.equalsIgnoreCase(element.getNodeName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Name of element defined by this object.
	 * 
	 * @return name.
	 */
	public final String getName() {
		return name;
	}

	final boolean instance(Node node) {
		return node instanceof Element
				&& node.getNodeName().equalsIgnoreCase(name);
	}

	/**
	 * set content model
	 */
	final void setContentModel(IModelGroup arg) {
		contentModel = arg;
	}

	final void setEndTag(boolean end) {
		this.end = end;
	}

	final void setExclusion(ElementDefinition exclusions[]) {
		this.exclusions = exclusions;
	}

	final void setInclusion(ElementDefinition inclusions[]) {
		this.inclusions = inclusions;
	}

	final void setStartTag(boolean start) {
		this.start = start;
	}

	/**
	 * checks if defining element's start tag is omittable or not
	 * 
	 * @return true if and only if the tag is omittable, otherwise false.
	 */
	public final boolean startTagOmittable() {
		return start;
	}

	/**
	 * For debug.
	 */
	public String toString() {
		String ret = "<!ELEMENT " + name + ' ' + (start ? 'O' : '-') + ' '
				+ (end ? 'O' : '-') + ' ' + '>';
		return ret;
	}

	/**
	 * @return <code>null</code> if not found.
	 */
	public AttributeDefinition getAttributeDef(String attrName) {
		int hash = hashCode(attrName.toCharArray());
		for (int i = attrNum - 1; i >= 0; i--) {
			if (attributeDefHashes[i] == hash
					&& attributeDefs[i].getName().equalsIgnoreCase(attrName)) {
				return attributeDefs[i];
			}
		}
		return null;
	}

	private int hashCode(char key[]) {
		int ret = 0;
		for (int i = key.length - 1; i >= 0; i--) {
			ret = 25 * ret + (key[i] & 0x1f) - 1;
		}
		return ret;
	}

	/**
	 * @return always <code>false</code>
	 */
	public boolean optional() {
		return false;
	}

	private int refercount = 0;

	/**
	 * Sets reference flag.
	 * 
	 * @param infinite
	 *            <code>true</code> if referenced infinitely. Otherwise,
	 *            <code>false</code>
	 */
	public void refer(boolean infinite) {
		if (infinite) {
			this.infinite = infinite;
		} else {
			refercount++;
		}
		if (inclusions != null) {
			for (int i = 0; i < inclusions.length; i++) {
				inclusions[i].refer(true);
			}
		}
		if (contentModel != null) {
			contentModel.refer(infinite);
		}
	}

	private boolean infinite = false;

	/**
	 * Checks if this is a singleton element in a document.
	 * 
	 * @return <code>true</code> if this element is singleton. Otherwise
	 *         <code>false</code>
	 */
	public boolean isSingleton() {
		return !infinite;
	}

	int number;

	/**
	 * Gets the Magic Number of this definition. A Magic Number is used for
	 * comparison to nodes.
	 */
	public int getMagicNumber() {
		return this.number;
	}

	/**
	 * Just checks a node with <code>number</code> as Magic Number can match
	 * to a parent defined by this instance. Rather than {@link
	 * #match(SGMLParser,org.w3c.dom.Node,org.w3c.dom.Node)} or {@link
	 * #contentMatch(SGMLParser,org.w3c.dom.Node,org.w3c.dom.Node)}, this has no
	 * side effects.
	 */
	public boolean match(int number) {
		return hash[number];
	}

	private boolean inclusionVector[];

	final boolean[] getInclusionVector() {
		return inclusionVector;
	}

	final boolean inclusion(int elementNumber) {
		return inclusionVector[elementNumber];
	}

	private boolean exclusionVector[];

	final boolean[] getExclusionVector() {
		return exclusionVector;
	}

	final boolean exclusion(int elementNumber) {
		return exclusionVector[elementNumber];
	}

	private boolean hash[] = null;

	/**
	 * Rehashes Magic Number vectors.
	 * 
	 * @param totalSize
	 *            size of the vectors.
	 */
	public boolean[] rehash(int totalSize) {
		if (this.hash != null)
			return this.hash;
		inclusionVector = new boolean[totalSize];
		if (inclusions != null) {
			for (int i = 0; i < inclusions.length; i++) {
				inclusionVector[inclusions[i].number] = true;
			}
		}
		exclusionVector = new boolean[totalSize];
		if (exclusions != null) {
			for (int i = 0; i < exclusions.length; i++) {
				exclusionVector[exclusions[i].number] = true;
			}
		}
		this.hash = new boolean[totalSize];
		boolean contentModelHash[] = contentModel.rehash(totalSize);
		if (start) {
			if (contentModelHash == null) {
				contentModelHash = new boolean[totalSize];
			}
			for (int i = totalSize - 1; i >= 0; i--) {
				hash[i] = (inclusionVector[i] | contentModelHash[i])
						& !exclusionVector[i];
			}
		}
		hash[this.number] = true;
		return hash;
	}
}