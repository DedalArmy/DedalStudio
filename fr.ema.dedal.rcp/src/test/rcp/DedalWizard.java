package test.rcp;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.helper.SiriusResourceHelper;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.factory.SessionFactory;
import org.eclipse.sirius.tools.api.command.semantic.AddSemanticResourceCommand;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelection;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallbackWithConfimation;
import org.eclipse.sirius.ui.business.internal.commands.ChangeViewpointSelectionCommand;
import org.eclipse.sirius.ui.tools.api.project.ModelingProjectManager;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.xtext.resource.XtextResourceSet;

import dedal.DedalDiagram;
import dedal.DedalFactory;
import dedal.DedalPackage;
import dedal.presentation.DedalEditorPlugin;

@SuppressWarnings("restriction")
public class DedalWizard extends Wizard implements INewWizard {

	protected DedalPackage dedalPackage = DedalPackage.eINSTANCE;
	protected DedalFactory dedalFactory = dedalPackage.getDedalFactory();

	protected DedalWizardPage initialPage;

	protected IStructuredSelection selection;
	protected IWorkbench workbench;

	/**
	 * This just records the information. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(DedalEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE
				.getImageDescriptor(DedalEditorPlugin.INSTANCE.getImage("full/wizban/NewDedal")));
	}

	protected EObject createInitialModel() {
		DedalDiagram diagram = dedalFactory.createDedalDiagram();
		diagram.setName("New Diagram");
		return diagram;
	}

	/**
	 * Do the work after everything is specified.
	 */
	@Override
	public boolean performFinish() {
		try {
			// Get the URI of the model file.
			//
			final URI semanticURI = getModelURI();
			if (new File(semanticURI.toFileString()).exists()) {
				if (!MessageDialog.openQuestion(getShell(), DedalEditorPlugin.INSTANCE.getString("_UI_Question_title"),
						DedalEditorPlugin.INSTANCE.getString("_WARN_FileConflict",
								new String[] { semanticURI.toFileString() }))) {
					initialPage.selectFileField();
					return false;
				}
			}

			// Do the work within an operation.
			//
			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor progressMonitor) {
					try {
						// Create a new project
						IProgressMonitor monitor = new NullProgressMonitor();
						IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
						IProject project = root.getProject(initialPage.getProjectName());
						project.create(monitor);
						project.open(monitor);

						// Create the initial model
						XtextResourceSet resourceSet = new XtextResourceSet();
						Resource resource = resourceSet.createResource(semanticURI);
						EObject rootObject = createInitialModel();
						resource.getContents().add(rootObject);
						resource.save(null);

						// Create a new sirius representation file
						URI representationURI = URI.createPlatformResourceURI(
								"/" + initialPage.getProjectName() + "/representations.aird", true);
						Session session = SessionFactory.INSTANCE.createDefaultSession(representationURI);

						// Link model to the representation and init viewpoint
						ModelingProjectManager.INSTANCE.convertToModelingProject(project, monitor);
						AddSemanticResourceCommand addCommandToSession = new AddSemanticResourceCommand(session,
								semanticURI, monitor);
						session.getTransactionalEditingDomain().getCommandStack().execute(addCommandToSession);
						
						// This is supposed to add the dedal viewpoint to the current session
						// For now, this has to be manually done after project creation
						Set<Viewpoint> viewpoints = new HashSet<Viewpoint>();
						for (Viewpoint p : ViewpointRegistry.getInstance().getViewpoints()) {
							viewpoints.add(SiriusResourceHelper.getCorrespondingViewpoint(session, p));
						}
						ViewpointSelection.Callback callback = new ViewpointSelectionCallbackWithConfimation();
						RecordingCommand command = new ChangeViewpointSelectionCommand(session, callback, viewpoints,
								new HashSet<Viewpoint>(), true, monitor);
						session.getTransactionalEditingDomain().getCommandStack().execute(command);
						
						// Save changes and refresh
						session.save(monitor);
						project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					} catch (Exception exception) {
						DedalEditorPlugin.INSTANCE.log(exception);
					} finally {
						progressMonitor.done();
					}
				}
			};

			getContainer().run(false, false, operation);

			//return DedalEditorAdvisor.openEditor(workbench, semanticURI);
			return true;
		} catch (Exception exception) {
			DedalEditorPlugin.INSTANCE.log(exception);
			return false;
		}
	}

	/**
	 * The framework calls this to create the contents of the wizard. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void addPages() {
		initialPage = new DedalWizardPage("Whatever2");
		initialPage.setTitle(DedalEditorPlugin.INSTANCE.getString("_UI_DedalModelWizard_label"));
		initialPage.setDescription(DedalEditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description"));
		addPage(initialPage);
	}

	/**
	 * Get the URI from the page. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public URI getModelURI() {
		return initialPage.getFileURI();
	}

}