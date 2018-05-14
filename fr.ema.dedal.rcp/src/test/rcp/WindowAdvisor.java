//package test.rcp;
//
//import org.eclipse.jface.action.IContributionItem;
//import org.eclipse.jface.action.IMenuManager;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.ui.application.ActionBarAdvisor;
//import org.eclipse.ui.application.IActionBarConfigurer;
//import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
//import org.eclipse.ui.application.WorkbenchWindowAdvisor;
//
///**
// * RCP's window advisor <!-- begin-user-doc --> <!-- end-user-doc -->
// * 
// * @generated
// */
//public class WindowAdvisor extends WorkbenchWindowAdvisor {
//	/**
//	 * @see WorkbenchWindowAdvisor#WorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
//	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
//	 * @generated
//	 */
//	public WindowAdvisor(IWorkbenchWindowConfigurer configurer) {
//		super(configurer);
//	}
//
//	/**
//	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
//	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
//	 * @generated
//	 */
//	@Override
//	public void preWindowOpen() {
//		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
//		configurer.setInitialSize(new Point(600, 450));
//		configurer.setShowCoolBar(false);
//		configurer.setShowStatusLine(true);
//		configurer.setTitle(DedalEditorAdvisor.getString("_UI_Application_title"));
//	}
//
//	/**
//	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
//	 *      <!-- begin-user-doc --> <!-- end-user-doc -->
//	 * @generated
//	 */
//	@Override
//	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
//		return null;
//	}
//
//	
//	@Override
//	public void postWindowOpen() {
//		super.postWindowOpen();
//		
//		IWorkbenchWindowConfigurer workbenchWindowConfigurer = getWindowConfigurer();
//		IActionBarConfigurer actionBarConfigurer = workbenchWindowConfigurer.getActionBarConfigurer();
//		IMenuManager menuManager = actionBarConfigurer.getMenuManager();
//		
//		//Removes the "Run" menu
//		IContributionItem[] menuItems = menuManager.getItems();
//		for (int i = 0; i < menuItems.length; i++) {
//			IContributionItem menuItem = menuItems[i];
//			if ("org.eclipse.ui.run".equals(menuItem.getId())) {
//				menuManager.remove(menuItem);
//			}
//		}
//		menuManager.update(true);
//	}
//}
