package Gateway;

import Controller.LoginSystem;
import Entity.Message;
import Entity.PermanentUser;
import Entity.Schedule;
import Entity.Template;
import UseCase.MessageManager;
import UseCase.ScheduleManager;
import UseCase.TemplateManager;
import UseCase.UserManager;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A observer class. It implements FileListener interface.
 * It has six attributes.
 *
 * filePaths: an arraylist of all the file paths
 * um: UserManager object
 * tm: TemplateManager object
 * sm: ScheduleManager object
 * ls: LoginSystem object
 * mm: MessageManager object
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class ReadFileListener implements FileListener{
	private ArrayList<String> filePaths;
	private UserManager um;
	private TemplateManager tm;
	private ScheduleManager sm;
	private LoginSystem ls;
	private MessageManager mm;

	/**
	 * Constructor for ReadFileListener object. It initialize all the variables.
	 * And call addFilePath method
	 *
	 * @param tm template manager
	 * @param sm schedule manager
	 * @param mm message manager
	 * @param ls login system
	 */
	public ReadFileListener(TemplateManager tm, ScheduleManager sm, MessageManager mm, LoginSystem ls){
		this.tm = tm;
		this.sm = sm;
		this.ls = ls;
		this.um = ls.getUm();
		this.mm = mm;
		this.filePaths = new ArrayList<>();
		addFilePath();
	}

	/**
	 * Add all the file path to the attribute filePaths
	 */
	public void addFilePath(){
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
	 * Read welcome message txt file at once
	 * @throws IOException exception occurs when we call createNewFile method
	 */
	public void readWelMsg() throws IOException {
		File file = new File("phase2/WelcomeMsg.txt");
		if (file.exists() && file.length() != 0) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader br = new BufferedReader(reader);

				String msg = br.readLine();
				ls.setWelMsg(msg);
				br.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.createNewFile();
		}
	}

	/**
	 * Read all the ser files at once
	 *
	 * @throws IOException exception occurs when we call createNewFile method
	 */
	public void readSerFile() throws IOException {
		for (String filePath: filePaths){
			File file = new File(filePath);
			if (file.exists() && file.length() != 0) {
				try {
					InputStream is = new FileInputStream(filePath);
					InputStream buffer = new BufferedInputStream(is);
					ObjectInput input = new ObjectInputStream(buffer);

					if (filePath.contains("UserInfo")){
						um.setPermanentUsers((ArrayList<PermanentUser>) input.readObject());
					}else if (filePath.contains("SuspendAccount")){
						um.setSuspensions((HashMap<String, Date>) input.readObject());
					}else if (filePath.contains("FreezeAccount")){
						um.setFreezes((ArrayList<String>) input.readObject());
					}else if (filePath.contains("UserLoginTime")){
						um.setLastLoginTime((HashMap<String, Date>) input.readObject());
					}else if (filePath.contains("TempInfo")){
						um.setTempInfo((HashMap<String, String>) input.readObject());
					}else if (filePath.contains("TemplateData")){
						tm.setIdToTemplate((HashMap<String, Template>) input.readObject());
					}else if (filePath.contains("CommonInbox")){
						mm.setCommonInbox((ArrayList<Message>) input.readObject());
					}else if (filePath.contains("ChatHistory")){
						mm.setChatHistory((HashMap<String, ArrayList<Message>>) input.readObject());
					}

					if (filePath.contains("ScheduleData") && !filePath.contains("ScheduleData2") &&
							!filePath.contains("ScheduleData3")){
						sm.setSchedulesList((HashMap<String, List<Schedule>>) input.readObject());
					}else if (filePath.contains("ScheduleData2")){
						sm.setScheduleTempMap((HashMap<String, String>) input.readObject());
					}else if (filePath.contains("ScheduleData3")){
						sm.setSchedulesFriend((HashMap<String, List<Schedule>>) input.readObject());
					}

					input.close();
					buffer.close();
					is.close();
				} catch (IOException | ClassNotFoundException ex) {
					System.out.println("We cannot read from input." + ex);
				}
			} else {
				file.createNewFile();
			}
		}
	}

	/**
	 * Override the method from its interface FileListener.
	 * Update all the changes. (Read all the files at once)
	 *
	 * @param actionType read actions
	 * @throws IOException exceptions occurs when we call readWelMsg and readSerFile methods.
	 */
	@Override
	public void update(String actionType) throws IOException {
		readWelMsg();
		readSerFile();
	}
}
