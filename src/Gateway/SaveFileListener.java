package Gateway;

import Controller.LoginSystem;
import UseCase.MessageManager;
import UseCase.ScheduleManager;
import UseCase.TemplateManager;
import UseCase.UserManager;
import java.io.*;
import java.util.ArrayList;

/**
 * A observer class. It implements FileListener interface.
 * It has seven attributes.
 *
 * filePaths: an arraylist of all the file paths
 * um: UserManager object
 * tm: TemplateManager object
 * sm: ScheduleManager object
 * ls: LoginSystem object
 * mm: MessageManager object
 * info: an arraylist of user info
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class SaveFileListener implements FileListener{
	private ArrayList<String> filePaths;
	private UserManager um;
	private TemplateManager tm;
	private ScheduleManager sm;
	private MessageManager mm;
	private LoginSystem ls;
	private ArrayList<String> info;

	/**
	 * Constructor for SaveFileListener object. It initialize all the variables.
	 * And call addFilePath method
	 *
	 * @param tm template manager
	 * @param sm schedule manager
	 * @param mm message manager
	 * @param ls login system
	 */
	public SaveFileListener(TemplateManager tm, ScheduleManager sm, MessageManager mm, LoginSystem ls){
		this.tm = tm;
		this.sm = sm;
		this.mm = mm;
		this.ls = ls;
		this.um = this.ls.getUm();
		this.filePaths = new ArrayList<>();
		addFilePath();
	}

	/**
	 * Set info attribute.
	 * It is a setter.
	 *
	 * @param info arraylist of user's info
	 */
	public void setInfo(ArrayList<String> info) {
		this.info = info;
	}

	/**
	 * Add all the file path to the attribute filePaths
	 */
	private void addFilePath() {
		filePaths.add("phase2/UserInfo.ser");
		filePaths.add("phase2/SuspendAccount.ser");
		filePaths.add("phase2/FreezeAccount.ser");
		filePaths.add("phase2/UserLoginTime.ser");
		filePaths.add("phase2/TempInfo.ser");
		filePaths.add("phase2/TemplateData.ser");
		filePaths.add("phase2/ScheduleData.ser");
		filePaths.add("phase2/ScheduleData2.ser");
		filePaths.add("phase2/ScheduleData3.ser");
		filePaths.add("phase2/CommonInbox.ser");
		filePaths.add("phase2/ChatHistory.ser");
	}

	/**
	 * Save welcome message to txt file
	 */
	public void saveWelMsg() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("phase2/WelcomeMsg.txt"));
		bw.write(ls.getWelMsg());
		bw.close();
	}

	/**
	 * Save temporary password to txt file named by user's email.
	 */
	public void saveTempPwd() throws IOException {
		String filePath = "phase2/" + info.get(1) + ".txt";
		File file = new File(filePath);
		file.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		bw.write("Hi,\n\n");
		bw.write("Since you have selected 'Forgot Password' before, then you can use the following " +
				"temporary password to log back into your account.\n\n");
		bw.write("Here are the information of your account:\n");
		bw.write("User ID: " + info.get(0) + "\n");
		bw.write("Email: " + info.get(1) + "\n");
		bw.write("Temporary Password: " + info.get(2) + "\n");
		bw.close();
	}

	/**
	 * Save the ser file based on actionType
	 *
	 * @param actionType type of files need to save
	 */
	public void saveSerFile(String actionType){
		for (String filePath: filePaths){
			if (filePath.contains(actionType.substring(4))){
				try{
					OutputStream file = new FileOutputStream(filePath);
					OutputStream buffer = new BufferedOutputStream(file);
					ObjectOutput output = new ObjectOutputStream(buffer);

					if (filePath.contains("UserInfo")){
						output.writeObject(um.getPermanentUsers());
					}else if (filePath.contains("SuspendAccount")){
						output.writeObject(um.getSuspensions());
					}else if (filePath.contains("FreezeAccount")){
						output.writeObject(um.getFreezes());
					}else if (filePath.contains("UserLoginTime")){
						output.writeObject(um.getLastLoginTime());
					}else if (filePath.contains("TempInfo")){
						output.writeObject(um.getTempInfo());
					}else if (filePath.contains("TemplateData")){
						output.writeObject(tm.getIdToTemplate());
					}else if (filePath.contains("CommonInbox")){
						output.writeObject(mm.getCommonInbox());
					}else if (filePath.contains("ChatHistory")){
						output.writeObject(mm.getChatHistory());
					}

					if (filePath.contains("ScheduleData") && !filePath.contains("ScheduleData2") &&
							!filePath.contains("ScheduleData3")){
						output.writeObject(sm.getSchedulesList());
					}else if (filePath.contains("ScheduleData2")){
						output.writeObject(sm.getScheduleTempMap());
					}else if (filePath.contains("ScheduleData3")){
						output.writeObject(sm.getSchedulesFriend());
					}
					output.close();
					buffer.close();
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Override the method from its interface FileListener.
	 * Update all the changes. Save files according to the action type
	 *
	 * @param actionType save what type of files
	 * @throws IOException exceptions occurs when we call saveWelMsg and saveTempPwd methods.
	 */
	@Override
	public void update(String actionType) throws IOException {
		if (actionType.equals("saveWelMsg")){
			saveWelMsg();
		}else if (actionType.equals("saveTempPwd")){
			saveTempPwd();
		}else{
			saveSerFile(actionType);
		}
	}
}
