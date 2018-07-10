	package test.rcp;

	import java.io.File;
	import java.net.URL;
	import java.util.Arrays;

	import org.eclipse.core.resources.ResourcesPlugin;
	import org.eclipse.core.runtime.IAdaptable;
	import org.eclipse.core.runtime.Platform;
	import org.eclipse.emf.common.ui.URIEditorInput;
	import org.eclipse.emf.common.util.URI;
	import org.eclipse.emf.edit.ui.util.EditUIUtil;
	import org.eclipse.jface.dialogs.MessageDialog;
	import org.eclipse.jface.resource.ImageDescriptor;
	import org.eclipse.swt.SWT;
	import org.eclipse.swt.widgets.FileDialog;
	import org.eclipse.swt.widgets.Shell;
	import org.eclipse.ui.IEditorDescriptor;
	import org.eclipse.ui.IWorkbench;
	import org.eclipse.ui.IWorkbenchPage;
	import org.eclipse.ui.IWorkbenchWindow;
	import org.eclipse.ui.PartInitException;
	import org.eclipse.ui.application.IWorkbenchConfigurer;
	import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
	import org.eclipse.ui.application.WorkbenchAdvisor;
	import org.eclipse.ui.application.WorkbenchWindowAdvisor;
	import org.eclipse.ui.ide.IDE;
	import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
	import org.osgi.framework.Bundle;

import dedal.presentation.DedalEditor;
import dedal.presentation.DedalEditorPlugin;
public class DedalEditorAdvisor extends WorkbenchAdvisor {

	/**
	 * This looks up a string in the plugin's plugin.properties file. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static String getString(String key) {
		return DedalEditorPlugin.INSTANCE.getString(key);
	}

	/**
	 * This looks up a string in plugin.properties, making a substitution. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public static String getString(String key, Object s1) {
		return DedalEditorPlugin.INSTANCE.getString(key, new Object[] { s1 });
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return null;
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
		IDE.registerAdapters();
		final String ICONS_PATH = "icons/full/";
		Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);
		declareWorkbenchImage(configurer, ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT, ICONS_PATH + "obj16/prj_obj.png",
				true);
		declareWorkbenchImage(configurer, ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED,
				ICONS_PATH + "obj16/cprj_obj.png", true);

	}

	private void declareWorkbenchImage(IWorkbenchConfigurer configurer_p, Bundle ideBundle, String symbolicName,
			String path, boolean shared) {
		URL url = ideBundle.getEntry(path);
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		configurer_p.declareImage(symbolicName, desc, shared);
	}

//	/**
//	 * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
//	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
//	 * @generated
//	 */
//	@Override
//	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
//		return new WindowAdvisor(configurer);
//	}

	/**
	 * The default file extension filters for use in dialogs. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final String[] FILE_EXTENSION_FILTERS = DedalEditor.FILE_EXTENSION_FILTERS.toArray(new String[0]);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static String[] openFilePathDialog(Shell shell, int style, String[] fileExtensionFilters) {
		return openFilePathDialog(shell, style, fileExtensionFilters, (style & SWT.OPEN) != 0, (style & SWT.OPEN) != 0,
				(style & SWT.SAVE) != 0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static String[] openFilePathDialog(Shell shell, int style, String[] fileExtensionFilters,
			boolean includeGroupFilter, boolean includeAllFilter, boolean addExtension) {
		FileDialog fileDialog = new FileDialog(shell, style);
		if (fileExtensionFilters == null) {
			fileExtensionFilters = FILE_EXTENSION_FILTERS;
		}

		// If requested, augment the file extension filters by adding a group of
		// all the other filters (*.ext1;*.ext2;...)
		// at the beginning and/or an all files wildcard (*.*) at the end.
		//
		includeGroupFilter &= fileExtensionFilters.length > 1;
		int offset = includeGroupFilter ? 1 : 0;

		if (includeGroupFilter || includeAllFilter) {
			int size = fileExtensionFilters.length + offset + (includeAllFilter ? 1 : 0);
			String[] allFilters = new String[size];
			StringBuilder group = includeGroupFilter ? new StringBuilder() : null;

			for (int i = 0; i < fileExtensionFilters.length; i++) {
				if (includeGroupFilter) {
					if (i != 0) {
						group.append(';');
					}
					group.append(fileExtensionFilters[i]);
				}
				allFilters[i + offset] = fileExtensionFilters[i];
			}

			if (includeGroupFilter) {
				allFilters[0] = group.toString();
			}
			if (includeAllFilter) {
				allFilters[allFilters.length - 1] = "*.*";
			}

			fileDialog.setFilterExtensions(allFilters);
		} else {
			fileDialog.setFilterExtensions(fileExtensionFilters);
		}
		fileDialog.open();

		String[] filenames = fileDialog.getFileNames();
		String[] result = new String[filenames.length];
		String path = fileDialog.getFilterPath() + File.separator;
		String extension = null;

		// If extension adding requested, get the dotted extension corresponding
		// to the selected filter.
		//
		if (addExtension) {
			int i = fileDialog.getFilterIndex();
			if (i != -1 && (!includeAllFilter || i != fileExtensionFilters.length)) {
				i = includeGroupFilter && i == 0 ? 0 : i - offset;
				String filter = fileExtensionFilters[i];
				int dot = filter.lastIndexOf('.');
				if (dot == 1 && filter.charAt(0) == '*') {
					extension = filter.substring(dot);
				}
			}
		}

		// Build the result by adding the selected path and, if needed,
		// auto-appending the extension.
		//
		for (int i = 0; i < filenames.length; i++) {
			String filename = path + filenames[i];
			if (extension != null) {
				int dot = filename.lastIndexOf('.');
				if (dot == -1 || !Arrays.asList(fileExtensionFilters).contains("*" + filename.substring(dot))) {
					filename += extension;
				}
			}
			result[i] = filename;
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static boolean openEditor(IWorkbench workbench, URI uri) {
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();

		IEditorDescriptor editorDescriptor = EditUIUtil.getDefaultEditor(uri, null);
		if (editorDescriptor == null) {
			MessageDialog.openError(workbenchWindow.getShell(), DedalEditorAdvisor.getString("_UI_Error_title"),
					DedalEditorAdvisor.getString("_WARN_No_Editor", uri.lastSegment()));
			return false;
		} else {
			try {
				page.openEditor(new URIEditorInput(uri), editorDescriptor.getId());
			} catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(),
						DedalEditorAdvisor.getString("_UI_OpenEditorError_label"), exception.getMessage());
				return false;
			}
		}
		return true;
	}
	
	@Override
	public IAdaptable getDefaultPageInput() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

}
