package Presenter;

import Controller.Facade;
import Controller.LoginSystem;
import Gateway.FileReadWriter;
import Gateway.SaveFileListener;
import UseCase.ScheduleNotFoundException;
import UseCase.UserNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is a controller for User UI, which is to interact with human-user by showing instructions
 * on console and getting user-input.
 *
 * ls: a LoginSystem object
 * facade: a Facade object
 * sc: a Scanner object to read input
 * userType: A string user's type
 * userId: A string of user's id
 * email: A string of user's email
 * scheduleUI: A ScheduleUI object
 * templateUI: A templateUI object
 * messageUI: A MessageUI object
 * frw: A FileReadWriter object
 *
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 * @author Siqing Xu
 * @author Qing Lyu
 * @author Zhen Cheng
 * @author Christine Chen
 * @author Chuanrun Zhang
 */
public class UserUI {
	private LoginSystem ls;
	private Facade facade;
	private Scanner sc;
	private String userType = "";
	private String userId = "";
	private String email;
	private ScheduleUI scheduleUI;
	private TemplateUI templateUI;
	private MessageUI messageUI;
	private FileReadWriter frw;

	/**
	 * A constructor for UserUI object with parameters facade passed in.
	 * It initializes the Scanner object sc, scheduleUI, templateUI,
	 * FileReadWriter object, and LoginSystem object.
	 *
	 * @param facade a Facade object
	 */
	public UserUI(Facade facade) {
		this.ls = facade.ls;
		this.facade = facade;
		sc = new Scanner(System.in);
		scheduleUI = new ScheduleUI(this.facade, this.sc);
		templateUI = new TemplateUI(this.facade, this.sc);
		this.frw = facade.frw;
	}

	/**
	 * Reads all files at once from outside of the program.
	 * Prints the welcome menu on console with instructions and gets user-input by letting users choose options.
	 * Users can choose to play as a trial user without sign up or log in, or sign up an account, or log in with their
	 * email and password, or exit.
	 * This method is called in Main to run this program.
	 *
	 * @throws IOException when <code>userCreation</code> and <code>process()</code> throws IOException
	 * @throws ScheduleNotFoundException exception occurs when we call signup, process methods
	 * @throws UserNotFoundException exception occurs when we call signup, process methods
	 */
	public void run() throws IOException, ScheduleNotFoundException, UserNotFoundException {
		frw.readFile();//read all files at once
		String option;
		boolean notExit = true;
		System.out.println(ls.getWelMsg());
		while(notExit) {
			System.out.println("Do you want to try as a trial user, sign up, or log in? \n" +
					"Enter '0' for trial user, \n'1' for sign up, \n'2' for log in, \n'3' to exit: ");
			option = sc.nextLine();
			if (option.equals("0")){
				userType = "trial";
				userId = ls.userCreation("", "", userType);
				scheduleUI.setUserId(userId);
				scheduleUI.setUserType(userType);
				process();
				break;
			}
			else if (option.equals("1")){
				notExit = signUp(false);
			}
			else if (option.equals("2")){
				if (login(false) == 0){
					process();
					break;
				}
			}
			else if (option.equals("3")) break;
			else{
				System.out.println("The option you entered does not exist! Please try again.");
			}
		}
	}

	/**
	 * Prints the main menu on console with instructions to let users choose the options they want.
	 * All three types of users can choose to exit the program.
	 * This method is being called by run method after user logged in.
	 * It shows different instruction for different types of users.
	 * When exit from this method, it means user logout.
	 * Saves to files outside of the program according to the change.
	 *
	 * @throws IOException when <code>saveInfo()</code> throws IOException
	 * @throws ScheduleNotFoundException exception occurs when we call signup, runByRegular, runByAdmin methods
	 * @throws UserNotFoundException exception occurs when we call signup, runByRegular, runByAdmin methods
	 */
	private void process() throws IOException, ScheduleNotFoundException, UserNotFoundException {
		System.out.println("Welcome to main menu!");
		String option;
		while (true){
			if (userType.equals("regular") || userType.equals("public") || userType.equals("private")) {
				System.out.println("Do you want to select a template, change your password, " +
						"change email, play with schedule, edit your friend list, send messages, or exit?");
				System.out.println("Enter '0' to select a template, \n'1' for change password, " +
						"\n'2' for change email, \n'3' for play with schedule, \n'4' for edit your friend list, " +
						"\n'5' for send messages, \nor '-1' for exit");
			}else if (userType.equals("trial")) {
				System.out.println("Do you want to select a template, play with schedule, or exit?");
				System.out.println("Enter '0' to select a template, \n'1' for play with schedule, \nor '-1' for exit");
			}else if (userType.equals("admin")) {
				System.out.println("Do you want to play with template, change your password, " +
						"change email, play with schedule, \nchange welcome message, suspend a user, freeze users, " +
						"unfreeze users, create another admin user, send messages, or exit?");
				System.out.println("Enter '0' for play with template, \n'1' for change password, " +
						"\n'2' for change email, \n'3' for play with schedule, \n'4' for change welcome message, " +
						"\n'5' for suspend a user, \n'6' for freeze users, \n'7' for unfreeze users, " +
						"\n'8' for create another admin user, \n'9' for send messages, \nor '-1' for exit");
			}
			option = sc.nextLine();
			if (option.equals("1")) {
				if (!userType.equals("trial")){
					changePwd();
				}else{
					scheduleUI.runSchedule(null);
				}
			} else if (option.equals("2") && !userType.equals("trial")) {
				System.out.println("Enter your new email: ");
				String newEmail = sc.nextLine();
				if (!ls.isEmailValid(newEmail)){
					System.out.println("The new email you entered is not a valid email! Please try again.");
				}
				else if (!ls.emailChange(userId, newEmail)) {
					System.out.println("This email has been signed up! Please use another one.");
				}else{
					frw.saveFile("saveUserInfo");
					email = newEmail;
					System.out.println("Email Changed Successfully!");
				}
			} else if (option.equals("0")) {
				if (!userType.equals("admin")){
					templateUI.selectTemplate(scheduleUI);
				} else{
					templateUI.editTemplate(scheduleUI);
				}
			} else if (option.equals("3") && !userType.equals("trial")) {
				scheduleUI.runSchedule(null);
			} else if (option.equals("-1")){
				break;
			} else if (option.equals("4") && !userType.equals("trial")){
				if (userType.equals("admin")){
					System.out.println("Please enter the new welcome message: ");
					ls.setWelMsg(sc.nextLine());
					frw.saveFile("saveWelMsg");
					System.out.println("Message changed successfully!");
				}else{
					editFriend();
				}
			} else if (option.equals("5") && !userType.equals("trial")){
				if (userType.equals("admin")){
					System.out.println("The following is the list of all users' emails: ");
					ls.displayUserInfo(email);
					System.out.println("Please enter the email of the user you want to suspend: ");
					String userEmail = sc.nextLine();
					if (!ls.emailExist(userEmail)){
						System.out.println("The user's email you entered does not exist! Please try again.");
					}else if (ls.getType(ls.getId(userEmail)).equals("admin")){
						System.out.println("The user you want to suspend is an admin user!");
					} else{
						System.out.println("Please enter the number of days you want to suspend this user:");
						try{
							int period = Integer.parseInt(sc.nextLine());
							ls.userSuspension(userEmail, period, System.currentTimeMillis());
							frw.saveFile("saveSuspend");
							System.out.println("This user has been suspended successfully!");
						}catch (NumberFormatException e){
							System.out.println("The number of days you entered is not an integer. Please try again.");
						}
					}
				}
				else{
					messageUI = new MessageUI(facade, sc, userId);
					messageUI.runByRegular();
				}
			}
			else if (option.equals("6") && userType.equals("admin")){
				System.out.println("Please enter the period you want to freeze: ");
				try{
					int times = Integer.parseInt(sc.nextLine());
					ls.userFreeze(times);
					frw.saveFile("saveFreeze");
					System.out.println("Freeze successfully!");
				}catch (NumberFormatException e){
					System.out.println("The number of days you entered is not an integer. Please try again.");
				}
			}
			else if (option.equals("7") && userType.equals("admin")){
				ls.adminUnfreeze();
				System.out.println("Unfreeze all users successfully!");
				frw.saveFile("saveFreeze");
			}
			else if (option.equals("8") && userType.equals("admin")){
				signUp(true);
			}
			else if (option.equals("9") && userType.equals("admin")){
				messageUI = new MessageUI(facade, sc,userId);
				messageUI.runByAdmin();
			}
			else{
				System.out.println("The option you entered does not exist! Please try again.");
			}
		}
	}

	/**
	 * A helper method that allow user to chose to add or delete user.
	 *
	 * @throws IOException exception occurs when we call friendEdition
	 */
	private void editFriend() throws IOException {
		System.out.println("Enter '1' to add a new friend, \n'2' to delete a friend, \nor others to return to " +
				"the previous menu:");
		System.out.println("The followings are all your friends: ");
		ls.displayFriend(email);
		String option = sc.nextLine();
		if (option.equals("1")){
			friendEdition("add");
		} else if (option.equals("2")){
			friendEdition("delete");
		}
	}

	/**
	 * A helper method that add/delete user's friend
	 *
	 * @param option user's option add or delete friend
	 * @throws IOException exception occurs when we call saveFile method.
	 */
	private void friendEdition(String option) throws IOException {
		if (option.equals("add")){
			System.out.println("The followings are all the users: ");
			ls.displayUserInfo(email);
		}
		System.out.println("Please enter the email of the user you want to "+option+": ");
		String friendEmail = sc.nextLine();
		if (!ls.emailExist(friendEmail)) {
			System.out.println("The email you entered does not exist! Please try again.");
		} else if (ls.friendList(email).contains(friendEmail) && option.equals("add")){
			System.out.println("This user is already your friend.");
		} else if (!ls.friendList(email).contains(friendEmail) && option.equals("delete")){
			System.out.println("This user is not your friend. \nDelete Failed");
		} else{
			if (option.equals("add")){
				if (ls.friendAddition(email,friendEmail)){
					frw.saveFile("saveUserInfo");
					facade.ss.updateFriends(ls.getId(email),ls.getId(friendEmail));
					System.out.println("Added successfully!");
				}else if (ls.getType(ls.getId(friendEmail)).equals("admin")){
					System.out.println("You can not add an admin as your friend! Please try again.");
				}
				else System.out.println("You cannot add yourself as friend! Please try again");
			}
			else{
				ls.friendDeletion(email,friendEmail);
				frw.saveFile("saveUserInfo");
				System.out.println("Deleted successfully!");
			}
		}
	}

	/**
	 * A helper method that change user's password
	 *
	 * @throws IOException exception occurs when we call saveFile method
	 */
	private void changePwd() throws IOException {
		while (true){
			System.out.println("Enter your new password: ");
			String newPwd = sc.nextLine();
			String type = ls.pwdChange(userId, newPwd);
			System.out.println("Your password is " + type + ".");
			if (type.equals("too weak")){
				pwdNotice();
			}else{
				frw.saveFile("saveUserInfo");
				System.out.println("Password Changed Successfully!");
				System.out.println("The password you entered is " + type + ".");
				break;
			}
		}
	}

	/**
	 * Return 0 when user login successfully, and -1 to return to the previous menu, which is the welcome menu.
	 * Call this method when a user wants to login in run method.
	 *
	 * @return int 0 for login successfully and -1 for return to the previous menu.
	 */
	private int login(boolean loginDirect) throws IOException {
		while (true){
			String option;
			if (loginDirect){
				System.out.println("Enter '0' to log in,\nor enter any other thing to return to the previous menu: ");
			}else{
				System.out.println("Enter '0' to log in,\n'1' for 'Forgot Password'," +
						"\n or enter any other thing to return to the previous menu: ");
			}
			option = sc.nextLine();
			if (option.equals("0")) {
				loginDirect = false;
				String pwd;
				System.out.println("Please enter your email: ");
				email = sc.nextLine();
				if (!ls.emailExist(email)) {
					System.out.println("The email you entered does not exist! Please try again.");
				} else {
					System.out.println("Please enter your password: ");
					pwd = sc.nextLine();
					if (ls.checkSusp(email, System.currentTimeMillis())){
						if (ls.emailLogin(email, pwd) || ls.checkTemp(email, pwd)) {
							ls.userUnfreeze(email);
							ls.setLoginTime(email, System.currentTimeMillis());
							frw.saveFile("saveFreeze");
							frw.saveFile("saveLoginTime");
							userId = ls.getId(email);
							System.out.println("Log in successfully! Your user id is: " + userId + ".");
							scheduleUI.setUserId(userId);
							userType = ls.getTypeByEmail(email);
							scheduleUI.setUserType(userType);
							if (ls.checkTemp(email, pwd)){
								System.out.println("Since you used the temporary password to login, please change it" +
										" to a new permanent password.");
								ls.deleteTemp(email);
								frw.saveFile("saveTempInfo");
								changePwd();
							}
							return 0;
						}else {
							System.out.println("Your password is incorrect! Please try again.");
						}
					}
					else{
						System.out.println("Your account has been suspend.");
						System.out.println("You can login after: " +
								ls.suspTime(email) + ".");
					}
				}
			}
			else if (option.equals("1") && !loginDirect){
					System.out.println("Please enter your email: ");
					String userEmail = sc.nextLine();
					if (!ls.emailExist(userEmail)) {
						System.out.println("The email you entered does not exist! Please try again.");
					}
					else{
						ArrayList<String> info = ls.forgotPwd(userEmail);
						frw.saveFile("saveTempInfo");
						SaveFileListener sf = new SaveFileListener(facade.tm, facade.sm, facade.mm, ls);
						sf.setInfo(info);
						frw.events.subscribe("saveTempPwd", sf);
						frw.saveFile("saveTempPwd");
						frw.events.unsubscribe("saveTempPwd", sf);
						System.out.println("An 'email'(the text file with your email as the file name)" +
								" has been created that contains a temporary password that you" +
								" can user to log in. \nAfter you use it to login, " +
								"please change to a new permanent password.");
						return -1;
					}
				}
			else {
				return -1;
			}
		}
	}

	/**
	 * Return true if and only if users sign up successfully.
	 * A helper method that use when user want to sign up.
	 *
	 * @param createAdmin a boolean that show to create an admin user or not
	 * @return true if user sign up successfully
	 * @throws IOException exception occurs when we call saveFile method
	 * @throws UserNotFoundException exception occurs when we call process method.
	 * @throws ScheduleNotFoundException exception occurs when we call process method.
	 */
	private boolean signUp(boolean createAdmin) throws UserNotFoundException, ScheduleNotFoundException, IOException {
		while (true){
			if (!createAdmin){
				System.out.println("Do you want to sign up as a regular user, public user, private user " +
						"or admin user? \n" +
						"Enter '0' for regular user, \n'1' for public user,\n'2' for private user, " +
						"\n'3' for admin user, \n or anything else to return to the previous menu:");
			}
			else{
				System.out.println("Enter '3' create other admin user or " +
						"anything else to return to the previous menu:");
			}
			String option = sc.nextLine();
			if (((option.equals("0") || option.equals("1") || option.equals("2") || option.equals("3")) && !createAdmin)
			|| (option.equals("3") && createAdmin)){
				System.out.println("Please enter the email address: ");
				email = sc.nextLine();
				if (!ls.isEmailValid(email)){
					System.out.println("The email you entered is not a valid email! Please try again.");
				}else {
					if (ls.emailExist(email))
						System.out.println("The email you entered has been signed up! Please try again.");
					else{
						String pwd;
						while(true) {
							System.out.println("Please enter the password: ");
							pwd = sc.nextLine();
							String pwdType = ls.getPwdType(pwd);
							if (pwdType.equals("too weak")) {
								pwdNotice();
							}else{
								System.out.println("The password you entered is " + pwdType + ".");
								break;
							}
						}
						if (option.equals("0"))
							userType = "regular";
						else if (option.equals("1"))
							userType = "public";
						else if (option.equals("2"))
							userType = "private";
						else
							userType = "admin";
						scheduleUI.setUserType(userType);
						String id = ls.userCreation(email, pwd, userType);
						if (!createAdmin){
							this.userId = id;
							scheduleUI.setUserId(userId);
						}
						if (!id.equals("duplicate email") && !id.equals("")){
							frw.saveFile("saveUserInfo");
							if (!createAdmin){
								System.out.println("Sign up successfully! Your user id is: " + this.userId);
								if (login(true) == 0){
									process();
									return false;
								}
							}
							else{
								System.out.println("Sign up successfully! His/Her user id is: " + id);
								return true;
							}
							return true;
						}
					}
				}
			}
			else{
				return true;
			}
		}
	}

	/**
	 * A helper method that print out password notice.
	 */
	private void pwdNotice(){
		System.out.println("The password you entered is too weak! \nA 'too weak' password contains only letters or only " +
				"numbers,and 0 or more special character(s). \nA password only contains special characters or " +
				"with length less than 6 is also considered as 'too weak'.\nA 'weak' password contains at least one " +
				"letter(only uppercase or lowercase) and one number, and 0 or more special character(s).\nA 'good' " +
				"password contains at least one uppercase letter and one lowercase letter and one number and\n" +
				"0 or more special character(s).\nPlease try to enter a password that is at least 'weak'.");
	}
}




