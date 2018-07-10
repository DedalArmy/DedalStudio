package test.rcp.action;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.common.ui.action.WorkbenchWindowActionDelegate;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;

import test.rcp.DedalWizard;

/**
 * Action to create objects from the Dedal model. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class NewAction {
	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void run(IWorkbench workbench, Shell shell) {
		DedalWizard wizard = new DedalWizard();
		wizard.init(workbench, StructuredSelection.EMPTY);
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.open();
	}

	@Execute
	public void execute(IWorkbench workbench, Shell shell) {
		run(workbench,shell);
	}
}