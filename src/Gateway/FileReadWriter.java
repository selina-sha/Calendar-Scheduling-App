package Gateway;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A observable class for file read and save. Its methods can be called directly when we want to notify observers by
 * specifying the action type.
 * It has one attribute, events.
 * When writing this class, we looked at this web:
 * https://refactoring.guru/design-patterns/observer/java/example
 * 
 * events: A FilePublisher object
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class FileReadWriter {
	public FilePublisher events;

	/**
	 * Constructor for FileReadWriter object. It initialize events attribute by
	 * passing in the operations ArrayList, which contains:
	 * readFile(read all files),
	 * saveScheduleData(save the ser files named 'ScheduleData', 'ScheduleData2', 'ScheduleData3'),
	 * saveTemplateData(save the ser file named 'TemplateData'),
	 * saveWelMsg(save the txt file named 'WelcomeMessage'),
	 * saveUserInfo(save the ser file named 'UserInfo'),
	 * saveSuspend(save the ser file named 'SuspendAccount'),
	 * saveFreeze(save the ser file named 'FreezeAccount'),
	 * saveLoginTime(save the ser file named 'UserLoginTime'),
	 * saveTempInfo(save the ser file named 'TempInfo'),
	 * saveTempPwd(save the txt file named by user's email who chose 'Forgot Password')
	 *
	 * @param operations arraylist of action operations
	 */
	public FileReadWriter(ArrayList<String> operations) {
		this.events = new FilePublisher(operations);
	}

	/**
	 * Notify observers to read the file
	 *
	 * @throws IOException exception occur when we call notify method
	 */
	public void readFile() throws IOException {
		events.notify("readFile");
	}

	/**
	 * Notify observers to save the file according to the passed in action type
	 * action types are: saveScheduleData, saveTemplateData, saveWelMsg, saveUserInfo, saveSuspend,
	 * saveFreeze, saveLoginTime, saveTempInfo, saveTempPwd
	 *
	 * @param actionType save which file
	 * @throws IOException exception occur when we call notify method
	 */
	public void saveFile(String actionType) throws IOException {
		events.notify(actionType);
	}
}
