/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class HighlightStringListener implements LineStyleListener {

    private class HighlightInfo {
        String targetS;

        Color fgColor;

        Color bgColor;

        int style;

        /**
         *  
         */
        public HighlightInfo(String targetS, Color fgColor) {
            this(targetS, fgColor, Display.getDefault().getSystemColor(SWT.COLOR_WHITE), SWT.NONE);
        }

        //TODO error check
        public HighlightInfo(String targetS, Color fgColor, Color bgColor, int style) {
            this.targetS = targetS;
            this.fgColor = fgColor;
            this.bgColor = bgColor;
            this.style = style;
        }

        public Color getBgColor() {
            return bgColor;
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
        }

        public Color getFgColor() {
            return fgColor;
        }

        public void setFgColor(Color fgColor) {
            this.fgColor = fgColor;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public String getTargetS() {
            return targetS;
        }

        public void setTargetS(String targetS) {
            this.targetS = targetS;
        }
    };

    private Vector<HighlightInfo> highlightInfoV;

    public HighlightStringListener() {
        highlightInfoV = new Vector<HighlightInfo>();
    }

    public void addTarget(String target, Color fgcolor) {
        HighlightInfo hi = new HighlightInfo(target, fgcolor);
        highlightInfoV.add(hi);
    }

    public void addTarget(String target, Color fgcolor, int style) {
        HighlightInfo hi = new HighlightInfo(target, fgcolor);
        hi.setStyle(style);
        highlightInfoV.add(hi);
    }

    public void lineGetStyle(LineStyleEvent arg0) {
        String text = arg0.lineText;
        int lastIndex = 0;
        int index = 0;
        ArrayList<StyleRange> styles = new ArrayList<StyleRange>();
        for (HighlightInfo hi : highlightInfoV) {
            String target = hi.getTargetS();
            while ((index = text.indexOf(target, lastIndex)) != -1) {
                StyleRange range = new StyleRange();
                range.start = arg0.lineOffset + index;
                range.length = target.length();
                range.foreground = hi.getFgColor();
                range.background = hi.getBgColor();
                range.fontStyle = hi.getStyle();
                styles.add(range);
                lastIndex = index + target.length();
            }
        }
        arg0.styles = (StyleRange[]) styles.toArray(new StyleRange[styles.size()]);
    }

}