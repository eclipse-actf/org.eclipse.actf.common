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


package org.eclipse.actf.model.ui.editors.ie.editor;

import org.eclipse.actf.model.ModelServiceUtils;
import org.eclipse.actf.model.DummyEditorInput;
import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.IModelServiceHolder;
import org.eclipse.actf.model.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editors.ie.events.INewWiondow2EventListener;
import org.eclipse.actf.model.ui.editors.ie.events.IWindowClosedEventListener;
import org.eclipse.actf.model.ui.editors.ie.impl.IWebBrowserPart;
import org.eclipse.actf.model.ui.editors.ie.impl.WebBrowserEventExtension;
import org.eclipse.actf.model.ui.editors.ie.impl.WebBrowserIEImpl;
import org.eclipse.actf.model.ui.editors.ie.internal.events.NewWindow2Parameters;
import org.eclipse.actf.util.ui.PlatformUIUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;




public class WebBrowserEditor extends EditorPart implements IModelServiceHolder, IWebBrowserPart {

    public static final String ID = WebBrowserEditor.class.getName();

    WebBrowserIEImpl webBrowser;

    IEditorInput input;

    public WebBrowserEditor() {
        super();
    }

    public void createPartControl(Composite parent) {
        String targetUrl = "about:blank";
        if (input instanceof DummyEditorInput) {
            targetUrl = ((DummyEditorInput) input).getUrl();
            if ("".equals(targetUrl)) {
                targetUrl = "about:blank";
            }
        }

        webBrowser = new WebBrowserIEImpl(this, parent, targetUrl);
        webBrowser.setNewWindow2EventListener(new INewWiondow2EventListener() {

            public void newWindow2(NewWindow2Parameters param) {
                IEditorPart newEditor = ModelServiceUtils.launch("about:blank", ID);
                if (newEditor instanceof WebBrowserEditor) {
                    IWebBrowserACTF browser = (IWebBrowserACTF) ((WebBrowserEditor) newEditor).getModelService();
                    param.setBrowserAddress(browser.getIWebBrowser2());
                    WebBrowserEventExtension.newWindow(browser);
                } else {
                    // TODO
                }
            }
        });

        webBrowser.setWindowClosedEventListener(new IWindowClosedEventListener() {
            public void windowClosed() {
                IWorkbenchPage page = PlatformUIUtil.getActivePage();
                if (page != null) {
                    IEditorReference[] editorRefs = page.getEditorReferences();
                    for (IEditorReference i : editorRefs) {
                        if (WebBrowserEditor.this == i.getEditor(false)) {
                            PlatformUIUtil.getActivePage().closeEditor(WebBrowserEditor.this, false);
                        }
                    }
                }
            }
        });

    }

    public void dispose() {
        WebBrowserEventExtension.browserDisposed(webBrowser, getPartName());
    }

    public void setFocus() {
        WebBrowserEventExtension.focusChange(webBrowser);
    }

    public void doSave(IProgressMonitor monitor) {
        // TODO
    }

    public void doSaveAs() {
        // TODO
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        this.input = input;
    }

    public boolean isDirty() {
        return false;
    }

    public boolean isSaveAsAllowed() {
        return false;
    }

    public IModelService getModelService() {
        return (this.webBrowser);
    }

    public IEditorPart getEditorPart() {
        return this;
    }

    public void changeTitle(IWebBrowserACTF webBrowser, String title) {
        if (this.webBrowser == webBrowser) {
            setPartName(title);
        }
    }

}