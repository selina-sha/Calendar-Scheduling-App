package UseCase;

import Entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A class to create user, and check, change some certain features like password and email
 * It has six parameters: permanentUsers, trialUsers, suspensions, freezes, lastLoginTime,
 * and tempInfo.
 *
 * permanentUsers: An ArrayList of PermanentUser objects that contains all the regular and admin users
 * trailUsers: An ArrayList of User object that contains all the trial users
 * suspensions: A HashMap which key is the user's email, value is Date object of the date that user got unsuspended
 * freezes: An ArrayList of user's email who got freeze
 * lastLoginTime: A HashMap which key is the user's email, value is their last login time in Date object
 * tempInfo: A HashMap which key is the user's email, value is their temporary password.
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class UserManager{
	private ArrayList<PermanentUser> permanentUsers;
	private ArrayList<User> trialUsers;
	private HashMap<String, Date> suspensions;
	private ArrayList<String> freezes;
	private HashMap<String, Date> lastLoginTime;
	private HashMap<String, String> tempInfo;

	/**
	 * A constructor for UserManager and Initialize the six attributes.
	 */
	public UserManager() {
		permanentUsers = new ArrayList<>();
		trialUsers = new ArrayList<>();
		suspensions = new HashMap<>();
		freezes = new ArrayList<>();
		lastLoginTime = new HashMap<>();
		tempInfo = new HashMap<>();
	}

	/**
	 * Sets permanentUsers attribute.
	 * It is a setter for permanentUsers attribute
	 * @param permanentUsers ArrayList of permanentUsers that contains all the info
	 *                       of regular and admin
	 */
	public void setPermanentUsers(ArrayList<PermanentUser> permanentUsers) {
		this.permanentUsers = permanentUsers;
	}

	/**
	 * Returns a ArrayList that contains PermanentUser objects
	 * It is a getter for permanentUsers attribute
	 * @return ArrayList of PermanentUser objects
	 */
	public ArrayList<PermanentUser> getPermanentUsers() {
		return permanentUsers;
	}

	/**
	 * Return a ArrayList that contains userId who get freeze by admin
	 * It is a getter for freezes attribute
	 * @return ArrayList of user's id that got freeze by admin user
	 */
	public ArrayList<String> getFreezes() {
		return freezes;
	}

	/**
	 * A setter for freezes
	 * @param freezes which is the arraylist of String
	 */
	public void setFreezes(ArrayList<String> freezes) {
		this.freezes = freezes;
	}

	/**
	 * It is a getter for lastLoginTime attribute
	 * @return lastLoginTime
	 */
	public HashMap<String, Date> getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * A setter for lastLoginTime
	 * @param lastLoginTime the input HashMap
	 */
	public void setLastLoginTime(HashMap<String, Date> lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * It is a getter for suspensions attribute
	 * @return suspensions
	 */
	public HashMap<String, Date> getSuspensions() {
		return suspensions;
	}

	/**
	 * A setter for suspensions
	 * @param suspensions the input suspensions
	 */
	public void setSuspensions(HashMap<String, Date> suspensions) {
		this.suspensions = suspensions;
	}

	/**
	 * It is a getter for tempInfo attribute
	 * @return tempInfo
	 */
	public HashMap<String, String> getTempInfo() {
		return tempInfo;
	}

	/**
	 * A setter for tempInfo
	 * @param tempInfo the input tempInfo
	 */
	public void setTempInfo(HashMap<String, String> tempInfo) {
		this.tempInfo = tempInfo;
	}

	/**
	 * Return true if and only if this user add friend successfully,
	 * Both ownEmail and friendEmail do not belong to admin user, and they are different.
	 * Update both users' friendList.
	 * @param ownEmail this user's email
	 * @param friendEmail the friend's email to be added
	 * @return true iff added friend successfully
	 */
	public boolean addFriend(String ownEmail, String friendEmail){
		if (!ownEmail.equals(friendEmail) && !getUserTypeByEmail(ownEmail).equals("admin")
				&& !getUserTypeByEmail(friendEmail).equals("admin")){
			for (PermanentUser pu: permanentUsers){
				if (pu.getEmail().equals(ownEmail)){
					ArrayList<String> friends = pu.getFriends();
					if (!friends.contains(friendEmail)){
						friends.add(friendEmail);
					}
				}else if (pu.getEmail().equals(friendEmail)){
					ArrayList<String> friends = pu.getFriends();
					if (!friends.contains(ownEmail)){
						friends.add(ownEmail);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Delete a friend of this user by entering the email of his/her friends
	 * @param ownEmail this user's email
	 * @param friendEmail the friend's email to be added
	 */

	public void deleteFriend(String ownEmail, String friendEmail){
		for (PermanentUser pu: permanentUsers){
			if (pu.getEmail().equals(ownEmail)){
				ArrayList<String> friends = pu.getFriends();
				friends.remove(friendEmail);
			}
			else if (pu.getEmail().equals(friendEmail)){
				ArrayList<String> friends = pu.getFriends();
				friends.remove(ownEmail);
			}
		}
	}

	/**
	 * Return an arraylist of email's that is user's friend
	 * @param userId this user's id
	 * @return an ArrayList that contains all of this user's friends' emails
	 */
	public ArrayList<String> getFriends(String userId){
		for (PermanentUser pu: permanentUsers){
			if (pu.getUserId().equals(userId)){
				return pu.getFriends();
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Return an arraylist of id's that is user's friend
	 * @param userId this user's id
	 * @return an ArrayList that contains all of this user's friends' ids
	 */
	public ArrayList<String> getFriendsId(String userId){
		ArrayList<String> friends = getFriends(userId);
		ArrayList<String> friendIds = new ArrayList<>();
		for (String email: friends){
			friendIds.add(getUserId(email));
		}
		return friendIds;
	}

	/**
	 * Print out the ArrayList of this user's friends
	 * @param userId this user's id
	 */
	public void displayUserFriend(String userId){
		ArrayList<String> friends = getFriends(userId);
		if (friends.isEmpty()){
			System.out.println("You don't have any friends yet!");
		} else {
			for (int i = 0; i < friends.size(); i++) {
				System.out.println(i + 1 + ") " + friends.get(i));
			}
		}
	}

	/**
	 * Returns true if this App only contains this user with email. Returns false and prints out list of email's that
	 * the app registered except this user's email and admin users' emails if there are more than one user in this app.
	 * @param email of this user
	 */
	public boolean displayInfo(String email) {
		if (permanentUsers.size() == 1){
			return true;
		}else{
			for (PermanentUser pu: permanentUsers){
				if (!pu.getEmail().equals(email) && !pu.getUserType().equals("admin")){
					System.out.println(pu.getEmail());
				}
			}
		}
		return false;
	}

	/**
	 * Suspend this user by enter in user's email, suspend days(length) and start time
	 * @param email this user's email
	 * @param period suspend days
	 * @param suspendTime the start time
	 */
	public void suspendUser(String email, int period, long suspendTime){
		Date endTime = new Date(((long) period *24*60*60*1000) + suspendTime);
		suspensions.put(email, endTime);
	}

	/**
	 * Return true if and only if this user is not got suspend at the time he/she logins
	 * Otherwise return false
	 * @param email of this user
	 * @param loginTime the time this user login at this time
	 * @return true iff this user is not suspended at the time he/she logins
	 */
	public boolean checkSuspension(String email, long loginTime){
		if (suspensions.containsKey(email)){
			Date currTime = new Date(loginTime);
			if (currTime.after(suspensions.get(email))){
				suspensions.remove(email);
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * Return a Date object that represents the time this user can login back
	 * @param email this user's email
	 * @return a Date object that represents the time this user can login back
	 */
	public Date displayLeftTime(String email){
		return suspensions.get(email);
	}

	/**
	 * Freeze users by enter in freeze days
	 * @param period the length of the freeze(freeze days)
	 */
	public void freezeUser(int period){
		long freezePeriod = System.currentTimeMillis() - (long) period *24*60*60*1000;
		Date freezeTime = new Date(freezePeriod);
		for (String id: lastLoginTime.keySet()){
			if (lastLoginTime.get(id).before(freezeTime)){
				freezes.add(id);
			}
		}
	}

	/**
	 * Unfreeze the users by admin so that all users are not freezing
	 */
	public void unfreezeUser(){
		freezes.clear();
	}

	/**
	 * Returns an ArrayList that contains all users' ids that are not freezing right now.
	 * @return an ArrayList that contains all users' ids that are not freezing right now.
	 */
	public ArrayList<String> checkUnfreeze(){
		ArrayList<String> notFreeze = new ArrayList<>();
		for (PermanentUser pu: permanentUsers){
			if (!freezes.contains(pu.getUserId())){
				notFreeze.add(pu.getUserId());
			}
		}
		for (User user: trialUsers){
			notFreeze.add(user.getUserId());
		}
		return notFreeze;
	}

	/**
	 * Set user's login time to loginTime
	 * @param email of this user
	 * @param loginTime of this user for this time
	 */
	public void setLoginTime(String email, long loginTime){
		for (PermanentUser pu: permanentUsers){
			if (pu.getEmail().equals(email)){
				lastLoginTime.put(pu.getUserId(), new Date(loginTime));
				break;
			}
		}
	}

	/**
	 * Unfreeze this user when this user logins
	 * @param userId of this user
	 */
	public void userUnfreeze(String userId){
		freezes.remove(userId);
	}

	/**
	 * Creates user account by pass in three parameters: email, password, and userType
	 * and return this user's id.
	 * Returns "duplicate email" for email duplication.
	 * For trail user, the parameters email and password will be empty strings.
	 * @param email email for this user
	 * @param password password for this user
	 * @param userType this user's type
	 * @return user's id or "duplicate email" indicates email duplication
	 */
	public String createUser(String email, String password, String userType){
		if (userType.equals("trial")) {
			User user = new User();
			trialUsers.add(user);
			return user.getUserId();
		}
		else {
			for (PermanentUser permanentUser : permanentUsers) {
				if (permanentUser.getEmail().equals(email)) {
					return "duplicate email";
				}
			}
			PermanentUser user = new PermanentUser(email, password, userType);
			permanentUsers.add(user);
			return user.getUserId();
		}
	}

	/**
	 * Returns true iff email this user entered has not been signed up, or the email
	 * this user wants to change is the same as before.
	 * Otherwise, return false.
	 * Call this method when this user want to change their email address.
	 *
	 * @param id this user's id
	 * @param newEmail the new email this user entered in to change to
	 * @return a boolean shows that whether the email changes to the new one or not
	 */
	public boolean changeEmail(String id, String newEmail){
		int index = 0;
		for (int i = 0; i < permanentUsers.size(); i++){
			if (!permanentUsers.get(i).getUserId().equals(id) && permanentUsers.get(i).getEmail().equals(newEmail))
				return false;
			else if (permanentUsers.get(i).getUserId().equals(id))
				index = i;
		}
		if (!permanentUsers.isEmpty())
			permanentUsers.get(index).setEmail(newEmail);
		return true;
	}

	/**
	 * Return user's password type based on user entered and change
	 * the password.
	 * Call this method when this user wants to change their password.
	 *
	 * @param id this user's id
	 * @param password the new password this user entered in
	 * @return password type
	 */
	public String changePwd(String id, String password){
		String type = pwdType(password);
		if (type.equals("too weak")){
			return type;
		}
		for (PermanentUser pu: permanentUsers) {
			if (pu.getUserId().equals(id)) {
				pu.setPassword(password);
				return type;
			}
		}
		return type;
	}

	/**
	 * Checks the type of password based on three criteria, "too weak", "weak", "good" and returns the type.
	 * It is called when user signs up or change password
	 *
	 * A 'too weak' password contains only letters or only numbers,and 0 or more special character(s). A password
	 *  only contains special characters or with length less than 6 is also considered as 'too weak'.
	 * A 'weak' password contains at least one letter(only uppercase or lowercase) and one number,
	 * and 0 or more special character(s).
	 * A 'good' password contains at least one uppercase letter and one lowercase letter and one number and
	 * 0 or more special character(s).
	 *
	 * Reference: https://stackoverflow.com/questions/11533474/java-how-to-test-if-a-string-contains-both-letters-and-numbers
	 * @param password which is the input password by user
	 * @return the password type
	 */
	public String pwdType(String password){
		if (password.matches("^(?=.*[a-zA-Z])[A-Za-z-+_!@#$%^&*., ?]+$") ||
				password.matches("^(?=.*[0-9])[0-9-+_!@#$%^&*., ?]+$") ||
				password.matches("^[-+_!@#$%^&*., ?]+$") || password.length() < 6){
			return "too weak";
		}else if (password.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9-+_!@#$%^&*., ?]+$") ||
				password.matches("^(?=.*[a-z])(?=.*[0-9])[a-z0-9-+_!@#$%^&*., ?]+$")){
			return "weak";
		}else if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9-+_!@#$%^&*., ?]+$"))
			return "good";
		else{
			return "too weak";
		}
	}

	/**
	 * Return true iff email entered in is a valid email address
	 * Otherwise return false.
	 *
	 * This method uses a regular expression for String object to check.
	 * It is a helper method which is called when user signs up or changes email.
	 * A valid email would have this format: at least one letter or number or any character from '_', '+', '&', '*',
	 * '-' followed by a '@' and at least one letter or number and a dot '.' and at least one letter or number.
	 *
	 * @param email user's email address
	 * @return true if this email is a valid email address
	 */
	public boolean isValidEmail(String email) {
		return email.matches("^[a-zA-Z0-9_+&*-]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$");
	}

	/**
	 * Returns true iff the userId matches with the password user entered in
	 * If the userId does not exist, return false.
	 *
	 * @param email this user's email
	 * @param password the password this user entered in
	 * @return a boolean that shows whether the id matches to password or not
	 */
	public boolean checkEmailPwd(String email, String password){
		for (PermanentUser permanentUser : permanentUsers) {
			if (permanentUser.getEmail().equals(email)) {
				return permanentUser.getPassword().equals(password);
			}
		}
		return false;
	}

	/**
	 * Returns user's type if the userId entered in is match to
	 * one of the user in ArrayList trialUsers or permanentUsers.
	 * Otherwise, return 'No such user'.
	 * It is a getter for userType.
	 *
	 * @param id the userId for this user
	 * @return this user's type
	 */
	public String getUserType(String id){
		for (User tu: trialUsers){
			if (tu.getUserId().equals(id)){
				return "trial";
			}
		}
		for (PermanentUser pu: permanentUsers){
			if (pu.getUserId().equals(id)){
				return pu.getUserType();
			}
		}
		return "No such user";
	}

	/**
	 * Returns user's type if the email entered in is match to
	 * one of the user in ArrayList permanentUsers.
	 * Otherwise, return 'No such user'.
	 * It is a getter for userType.
	 *
	 * @param email the email for this user
	 * @return this user's type
	 */
	public String getUserTypeByEmail(String email){
		for (PermanentUser pu: permanentUsers){
			if (pu.getEmail().equals(email)){
				return pu.getUserType();
			}
		}
		return "No such user";
	}

	/**
	 * Returns user's id if the email entered in is match to
	 * one of the user in ArrayList permanentUsers.
	 * Otherwise, return -1 for not found the user.
	 *
	 * @param email user's email that they entered id
	 * @return user's id or "not found" for not found
	 */
	public String getUserId(String email){
		for (PermanentUser pu: permanentUsers){
			if (pu.getEmail().equals(email))
				return pu.getUserId();
		}
		return "not found";
	}

	/**
	 * Returns true iff this user's id can be found in objects in permanentUsers.
	 * This method is to check if the the user with this id exists.
	 *
	 * @param email user's email
	 * @return true if it id exists
	 */
	public boolean checkEmail(String email){
		for (PermanentUser pu: permanentUsers){
			if (pu.getEmail().equals(email))
				return true;
		}
		return false;
	}
}
