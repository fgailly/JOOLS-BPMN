/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public abstract class AbstractBpmn2PropertySection extends GFPropertySection implements IBpmn2PropertySection {
	
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	protected Composite parent;
	protected DiagramEditor editor;
	private IWorkbenchWindow cachedWorkbenchWindow;
	
	private IPartListener partActivationListener = new IPartListener() {

		public void partActivated(IWorkbenchPart part) {
			Object bpmn2Editor = part.getAdapter(DiagramEditor.class);
			if (bpmn2Editor instanceof DiagramEditor) {
				editor = (DiagramEditor)bpmn2Editor;
			}
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};
	
	public AbstractBpmn2PropertySection() {
		cachedWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (cachedWorkbenchWindow != null) {
			cachedWorkbenchWindow.getPartService().addPartListener(
				partActivationListener);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (cachedWorkbenchWindow != null) {
			cachedWorkbenchWindow.getPartService().removePartListener(
				partActivationListener);
			cachedWorkbenchWindow = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (e.widget instanceof Composite) {
					Composite parent = ((Composite)e.widget);
					Control[] kids = parent.getChildren();
					for (Control c : kids)
						c.dispose();
				}
			}
			
		});
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;
		this.parent = parent;
		parent.setLayout(new GridLayout(1, false));
//		editor = DiagramEditor.getActiveEditor();
	}

	/**
	 * Returns the section's parent composite. This is the composite that was
	 * created by the TabbedPropertySheetPage, not the "root" composite for
	 * this section.
	 * 
	 * @return the TabbedPropertySheetPage composite.
	 */
	public Composite getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.GFPropertySection#getDiagramEditor()
	 */
	@Override
	protected DiagramEditor getDiagramEditor() {
		return editor;
	}

//	protected ModelHandler getModelHandler() {
//		if (editor!=null)
//			return editor.getModelHandler();
//		return null;
//	}
	
	/**
	 * Returns the property section's TabbedPropertySheetPage
	 * 
	 * @return the TabbedPropertySheetPage that owns this section.
	 */
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	/**
	 * Get the section's root composite, which is a subclass of AbstractBpmn2DetailComposite.
	 * Create the composite if it has not been created yet.
	 * 
	 * @return the composite
	 */
	public AbstractDetailComposite getSectionRoot() {
		AbstractDetailComposite sectionRoot = null;
		if (parent!=null && !parent.isDisposed()) {
			if (parent.getChildren().length==0) {
				sectionRoot = createSectionRoot();
				sectionRoot.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
			}
			sectionRoot = (AbstractDetailComposite)parent.getChildren()[0];
		}
		return sectionRoot;
	}
	
	/**
	 * The subclass must provide the parent Composite it created in createControls()
	 * so that we can pass the PropertySheetPage down to the Composite. This is
	 * useful for allowing the Composite to resize itself based on the parent's size.
	 * 
	 * @return
	 */
	protected abstract AbstractDetailComposite createSectionRoot();
	
	public abstract AbstractDetailComposite createSectionRoot(Composite parent, int style);

	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		return BusinessObjectUtil.getBusinessObjectForPictogramElement(pe);
	}
	
	protected EObject getBusinessObjectForSelection(ISelection selection) {
		return BusinessObjectUtil.getBusinessObjectForSelection(selection);
	}
	
	protected EObject getBusinessObjectForSelection() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null)
			return getBusinessObjectForPictogramElement(pe);
		return null;
	}
	
	/* (non-Javadoc)
	 * Yet another ugly hack: this restores the current property sheet page parent
	 * composite when a different DiagramEditor is activated. Apparently TabbedPropertySheetPage
	 * does not do this for us automatically!
	 *  
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Object bpmn2Editor = part.getAdapter(DiagramEditor.class);
		if (bpmn2Editor instanceof DiagramEditor) {
			editor = (DiagramEditor)bpmn2Editor;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			final EObject be = getBusinessObjectForPictogramElement(pe);
			if (be!=null) {
				AbstractDetailComposite sectionRoot = getSectionRoot();
				if (sectionRoot!=null) {
					if (sectionRoot.getBusinessObject() != be) {
						sectionRoot.setDiagramEditor((DiagramEditor) getDiagramEditor());
						if (!parent.isLayoutDeferred())
							parent.setLayoutDeferred(true);
						sectionRoot.setBusinessObject(be);
						if (parent.isLayoutDeferred())
							parent.setLayoutDeferred(false);
					}
					sectionRoot.refresh();
				}
			}
		}
	}

	@Override
	// make this public!
	public PictogramElement getSelectedPictogramElement() {
		return super.getSelectedPictogramElement();
	}

	/**
	 * Force a layout of the property sheet page.
	 */
	public void layout() {
		Control sectionRoot = (AbstractDetailComposite)parent.getChildren()[0];
		Composite composite = (Composite)tabbedPropertySheetPage.getControl();
		composite.layout(true);
		tabbedPropertySheetPage.resizeScrolledComposite();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
	 */
	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}

	/* (non-Javadoc)
	 * Override this to allow the section to decide whether or not it will be rendered.
	 * @see org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection#appliesTo(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		editor = (DiagramEditor)part.getAdapter(DiagramEditor.class);
		if (editor!=null) {
			PictogramElement pe = BusinessObjectUtil.getPictogramElementForSelection(selection);
			EObject selectionBO = BusinessObjectUtil.getBusinessObjectForSelection(selection);
			ModelEnablementDescriptor modelEnablement = getModelEnablement(selection);
			
			if (selectionBO!=null && modelEnablement.isEnabled(selectionBO.eClass())) {
				EObject thisBO = getBusinessObjectForPictogramElement(pe);
				return thisBO!=null;			
			}
		}
		return false;
	}
	
	protected ModelEnablementDescriptor getModelEnablement(ISelection selection) {
		EObject selectionBO = BusinessObjectUtil.getBusinessObjectForSelection(selection);
		if (selectionBO!=null) {
			TargetRuntime rt = (TargetRuntime) getDiagramEditor().getAdapter(TargetRuntime.class);
			if (rt!=null)
				return rt.getModelEnablements(selectionBO);
		}
		return null;
	}
	
	public boolean doReplaceTab(String id, IWorkbenchPart part, ISelection selection) {
		return true;
	}
}
