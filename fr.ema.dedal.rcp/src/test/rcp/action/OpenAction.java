package test.rcp.action;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.common.ui.action.WorkbenchWindowActionDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;

import test.rcp.DedalEditorAdvisor;

/**
 * Open action for the objects from the Dedal model.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OpenAction{
	/**
	 * Opens the editors for the files selected using the file dialog.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void run(IWorkbench workbench, Shell shell) {
		String[] filePaths = DedalEditorAdvisor.openFilePathDialog(shell, SWT.OPEN, null);
		if (filePaths.length > 0) {
			DedalEditorAdvisor.openEditor(workbench, URI.createFileURI(filePaths[0]));
		}
	}
	
	@Execute
	public void execute(IWorkbench workbench, Shell shell) {
		run(workbench,shell);
	}
}
