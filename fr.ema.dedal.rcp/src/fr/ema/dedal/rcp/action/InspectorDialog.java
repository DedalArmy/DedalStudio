package fr.ema.dedal.rcp.action;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class InspectorDialog extends TitleAreaDialog {

	private TreeViewer viewer;

	private static final DateFormat dateFormat = DateFormat.getDateInstance();

	public InspectorDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Architecture Reconstructor");
		setMessage("Select the folder you want to inspect", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(1, true);
		container.setLayout(layout);

		viewer = new TreeViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.getTree().setHeaderVisible(true);

		TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
		mainColumn.getColumn().setText("Name");
		mainColumn.getColumn().setWidth(250);
		mainColumn.setLabelProvider(
				new DelegatingStyledCellLabelProvider(new ViewLabelProvider(createImageDescriptor())));

		TreeViewerColumn modifiedColumn = new TreeViewerColumn(viewer, SWT.NONE);
		modifiedColumn.getColumn().setText("Last Modified");
		modifiedColumn.getColumn().setWidth(100);
		modifiedColumn.getColumn().setAlignment(SWT.RIGHT);
		modifiedColumn
				.setLabelProvider(new DelegatingStyledCellLabelProvider(new FileModifiedLabelProvider(dateFormat)));

		TreeViewerColumn fileSizeColumn = new TreeViewerColumn(viewer, SWT.NONE);
		fileSizeColumn.getColumn().setText("Size");
		fileSizeColumn.getColumn().setWidth(50);
		fileSizeColumn.getColumn().setAlignment(SWT.RIGHT);
		fileSizeColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new FileSizeLabelProvider()));

		viewer.setInput(File.listRoots());

		return container;
	}

	private ImageDescriptor createImageDescriptor() {
		Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/folder.png"), null);
		return ImageDescriptor.createFromURL(url);
	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return (File[]) inputElement;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			File file = (File) parentElement;
			return file.listFiles();
		}

		@Override
		public Object getParent(Object element) {
			File file = (File) element;
			return file.getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			File file = (File) element;
			if (file.isDirectory()) {
				return true;
			}
			return false;
		}

	}

	class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

		private ImageDescriptor directoryImage;
		private ResourceManager resourceManager;

		public ViewLabelProvider(ImageDescriptor directoryImage) {
			this.directoryImage = directoryImage;
		}

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof File) {
				File file = (File) element;
				StyledString styledString = new StyledString(getFileName(file));
				String[] files = file.list();
				if (files != null) {
					styledString.append(" ( " + files.length + " ) ", StyledString.COUNTER_STYLER);
				}
				return styledString;
			}
			return null;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof File) {
				if (((File) element).isDirectory()) {
					return getResourceManager().createImage(directoryImage);
				}
			}

			return super.getImage(element);
		}

		@Override
		public void dispose() {
			// garbage collection system resources
			if (resourceManager != null) {
				resourceManager.dispose();
				resourceManager = null;
			}
		}

		protected ResourceManager getResourceManager() {
			if (resourceManager == null) {
				resourceManager = new LocalResourceManager(JFaceResources.getResources());
			}
			return resourceManager;
		}

		private String getFileName(File file) {
			String name = file.getName();
			return name.isEmpty() ? file.getPath() : name;
		}
	}

	class FileModifiedLabelProvider extends LabelProvider implements IStyledLabelProvider {

		private DateFormat dateLabelFormat;

		public FileModifiedLabelProvider(DateFormat dateFormat) {
			dateLabelFormat = dateFormat;
		}

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof File) {
				File file = (File) element;
				long lastModified = file.lastModified();
				return new StyledString(dateLabelFormat.format(new Date(lastModified)));
			}
			return null;
		}
	}

	class FileSizeLabelProvider extends LabelProvider implements IStyledLabelProvider {

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof File) {
				File file = (File) element;
				if (file.isDirectory()) {
					// a directory is just a container and has no size
					return new StyledString("0");
				}
				return new StyledString(String.valueOf(file.length()));
			}
			return null;
		}
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}


	@Override
	protected void okPressed() {
		ISelection selection = viewer.getSelection();
		String location = selection.toString();
		location = location.substring(1, location.length() - 1);
		if (!location.equals("empty selection")) {
			new File(getSelectedProjectPath() + "/generated_metrics_results").mkdirs();
			new File(getSelectedProjectPath() + "/generated").mkdirs();
			System.out.println(location);
			super.okPressed();
			fr.ema.dedal.componentinspector.main.Main.laucnhReconstruction((new String[] { "-lib", location }), "", "",
					"", getSelectedProjectPath());
			
			try {
				getSelectedProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor() );
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
		}
	}

	private String getSelectedProjectPath() {
		if (getSelectedProject() != null) {
			IProject project = getSelectedProject();
			IPath path = project.getLocation();
			return path.toString();
		}
		return "";
	}

	protected IProject getSelectedProject() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {
				IProject project = (IProject) ((IAdaptable) firstElement).getAdapter(IProject.class);
				return project;
			}
		}
		return null;
	}
}
