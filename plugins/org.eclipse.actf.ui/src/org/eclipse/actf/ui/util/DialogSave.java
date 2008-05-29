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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class DialogSave {
    public static final int HTML = 0;

    public static final int BMP = 1;

    public static final int TYPE_MAX = 1;

    private static final int MAX_FILENAME_LENGTH = 100;

    private static final String[][] FILTER = { { "HTML Files (*.htm, *.html)" }, { "BMP Files (*.bmp)" } };

    private static final String[][] FILTER_EXT = { { "*.htm", "*.html" }, { "*.bmp" } };

    private static final String[][] EXT_CHECK = { { ".htm", ".html" }, { ".bmp" } };

    private static final String[] DEFAULT_EXT = { ".htm", ".bmp" };

    /**
     * @param arg0
     *            parent shell
     * @param type
     *            File Type
     * @param targetNameBase
     *            file name to save
     */
    public static String open(Shell arg0, int type, String targetNameBase, String ext) {

        String saveFileName = null;
        if (type > -1 && type <= TYPE_MAX && ext != null) {

            FileDialog fileD = new FileDialog(arg0, SWT.SAVE);
            fileD.setFilterNames(FILTER[type]);
            fileD.setFilterExtensions(FILTER_EXT[type]);

            saveFileName = targetNameBase;
            if (saveFileName != null) {

                //TODO use System.property
                int iPos = saveFileName.indexOf("//");
                if (iPos == -1)
                    iPos = 0;
                saveFileName = saveFileName.substring(iPos + 2);
                //				saveFileName = saveFileName.replace('/', '_');
                //				saveFileName = saveFileName.replace('\\', '_');
                //				saveFileName = saveFileName.replace(':', '_');
                //				saveFileName = saveFileName.replace('?', '_');
                //				saveFileName = saveFileName.replace('%', '_');
                saveFileName = saveFileName.replaceAll("\\p{Punct}", "_");

                if (saveFileName.indexOf(".") > 0) {
                    saveFileName = saveFileName.substring(0, saveFileName.lastIndexOf("."));
                }

                if (saveFileName.length() > MAX_FILENAME_LENGTH) {
                    saveFileName = saveFileName.substring(0, MAX_FILENAME_LENGTH);
                }
                saveFileName += ext;
                fileD.setFileName(saveFileName);
                saveFileName = fileD.open();

                if (saveFileName != null) {
                    //TODO check
                    //					if (saveFileName.endsWith(".htm")
                    //						|| saveFileName.endsWith(".html")) {
                    //						saveFileName =
                    //							saveFileName.substring(
                    //								0,
                    //								saveFileName.lastIndexOf("."));
                    //					}
                    //
                    //					int pos =
                    //						saveFileName.lastIndexOf(ADesignerConst.DIR_SEP) + 1;
                    //					String strSub =
                    //						saveFileName.substring(pos, saveFileName.length());
                    //					if (strSub.length() > 100) {
                    //						saveFileName = saveFileName.substring(0, pos);
                    //						saveFileName += strSub.substring(0, 100);
                    //					}

                    boolean flag = true;

                    String tmpS = saveFileName.toLowerCase();
                    for (int i = 0; i < EXT_CHECK[type].length; i++) {
                        if (tmpS.endsWith(EXT_CHECK[type][i])) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        saveFileName += DEFAULT_EXT[type];
                    }

                }

            }
        }
        return (saveFileName);
    }

}
