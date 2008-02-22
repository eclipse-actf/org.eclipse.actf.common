/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui.editors.ooo.actions;

import org.eclipse.actf.model.ModelServiceUtils;
import org.eclipse.actf.model.ui.editors.ooo.OOoEditorPlugin;
import org.eclipse.actf.model.ui.editors.ooo.editor.OOoEditor;
import org.eclipse.actf.util.ui.PlatformUIUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;




public class OpenODFAction extends Action {

	private final String message = OOoEditorPlugin
			.getResourceString("ODFEditorAction.Open");

	public OpenODFAction() {
		setText(message);
		setToolTipText(message);
		setImageDescriptor(PlatformUIUtil
				.getSharedImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
	}

	public void run() {
		FileDialog openDialog = new FileDialog(PlatformUI.getWorkbench()
				.getDisplay().getActiveShell(), SWT.OPEN);
		openDialog
				.setFilterNames(new String[] { "OpenDocument (*.odt; *.odp; *.ods)" });
		openDialog.setFilterExtensions(new String[] { "*.odt; *.odp; *.ods" });
		String sUrl = openDialog.open();

		if (sUrl != null) {
			ModelServiceUtils.launch(sUrl, OOoEditor.ID);
		}
	}
}
