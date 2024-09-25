package edu.ndsu.cs.estimate.pages.tasks;


import org.apache.tapestry5.annotations.Property;
/*import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;*/
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;


public class ViewTasks {
	
	@Inject
	private TaskDatabaseService taskDatabaseService;
	
	//@Inject 
	//private BeanModelSource beanModelSource;
	
	//@Inject
	//private Messages messages; 
	
	
	
	@Property
	private Task task; 
	
	@Property
	private Integer taskPK;
	
	void onActivate(Integer taskPK) {
		this.taskPK = taskPK;
	}
	
	Integer onPassivate() {
		return taskPK;
	}
	
	void setupRender() {
		if(taskPK != null) {
			task = taskDatabaseService.getTask(taskPK);
		}
	}
	
	/* This method is used by the ViewProduct.tml file which has the model attribute
	 *  of the BeanDisplay component set its value to "productBeanModel". The controller
	 *  class can either have a field with that identifier or a method with an identifier
	 *  getProductBeanModel which will supply the value when called. 
	 * In this case where the display of information is customized extensively, it makes
	 *  some sense to have a method like this so that any page which wants to display
	 *  information about a Product using BeanDisplay doesn't need to include custom
	 *  mark-up in order to only display necessary information in a properly formatted way.  
	 */
	//public BeanModel<Product> getProductBeanModel() {
		//return product.getBeanModel(beanModelSource, messages);
	//}
}

