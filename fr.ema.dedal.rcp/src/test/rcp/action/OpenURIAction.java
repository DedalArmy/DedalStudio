package test.rcp.action;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.common.ui.action.WorkbenchWindowActionDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;

import test.rcp.DedalEditorAdvisor;



/**
 * Open URI action for the objects from the Dedal model.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OpenURIAction{
	/**
	 * Opens the editors for the files selected using the LoadResourceDialog.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void run(IWorkbench workbench, Shell shell) {
		LoadResourceAction.LoadResourceDialog loadResourceDialog = new LoadResourceAction.LoadResourceDialog(shell);
		if (Window.OK == loadResourceDialog.open()) {
			for (URI uri : loadResourceDialog.getURIs()) {
				DedalEditorAdvisor.openEditor(workbench, uri);
			}
		}
	}
	
	@Execute
	public void execute(IWorkbench workbench, Shell shell) {
		run(workbench,shell);
	}
}