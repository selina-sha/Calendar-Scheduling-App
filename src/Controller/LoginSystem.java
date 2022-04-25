package Controller;

import UseCase.UserManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * A class called LoginSystem which is a controller of UserManager.
 * It has two attributes. One is um, which is an UserManager object. The other one is welMsg,
 * which is a string of welcome message
 *
 * LoginSystem can be created by the constructor immediately with the given UserManager.
 *
 * um: an UserManager object
 * welMsg: a string of welcome message with default: Hi! Welcome to our Calendar and Scheduling App!
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public class LoginSystem {
    private UserManager um;
    private String welMsg = "Hi! Welcome to our Calendar and Scheduling App!";

    /**
     *  A constructor that creates a LoginSystem object.
     *  It initializes an UserReadWriter object by passing um as a parameter,
     *  and assigns this object to the attribute urw.
     *
     * @param um an UserManager object
     * @throws IOException exception occurs when we initialize the UserManager um.
     */
    public LoginSystem(UserManager um) throws IOException {
        this.um = um;
    }

    /**
     * It is a getter for um attribute
     * @return userManager object
     */
    public UserManager getUm() {
        return um;
    }

    /**
     * It is a getter for welMsg attribute
     * @return welcome message
     */
    public String getWelMsg(){
        return welMsg;
    }

    /**
     * It is a setter for welMsg attribute
     * @param welMsg welcome message admin want to change to
     */
    public void setWelMsg(String welMsg){
        this.welMsg = welMsg;
    }

    /**
     * Return true if and only if this user add friend successfully,
     * friendEmail is valid and register in the app.
     * Admin does not have this feature
     *
     * @param ownEmail email of this user
     * @param friendEmail email of the friend this user want to add
     * @return true if add friend successful
     */
    public boolean friendAddition(String ownEmail, String friendEmail) {
        return um.addFriend(ownEmail, friendEmail);
    }

    /**
     * Delete a friend of this user by entering the email of his/her friend
     *
     * @param ownEmail email of this user
     * @param friendEmail email of the friend this user want to delete
     */
    public void friendDeletion(String ownEmail, String friendEmail){
        um.deleteFriend(ownEmail, friendEmail);
    }

    /**
     * Return an arraylist of email's that is user's friend
     * @param email this uesr's email
     * @return an arraylist of friends' email
     */
    public ArrayList<String> friendList(String email){
        return um.getFriends(getId(email));
    }

    /**
     * Print out the ArrayList of this user's friends
     * @param email user's email
     */
    public void displayFriend(String email){
        um.displayUserFriend(getId(email));
    }

    /**
     * Returns true if this App only contains this user with email. Returns false and prints out list of email's that
     * 	 * the app registered except this user's email and admin users' emails if there are more than one user in this app.
     * @param email user's email
     */
    public boolean displayUserInfo(String email){
        return um.displayInfo(email);
    }

    /**
     * Creates user account by pass in three parameters: email, password, and userType
     * and returns this user's id.
     * Returns -1 for email duplication.
     * This method is called when user wants to sign up.
     * It calls the um method createUser and return its result.
     *
     * @param email email for this user
     * @param password password for this user
     * @param userType user type for this user
     * @return user's id or "duplicate email" indicates email duplicating
     */
    public String userCreation(String email, String password, String userType){
        return um.createUser(email, password, userType);
    }

    /**
     * Suspend a user by enter in user's email, suspend days and start time
     * @param email suspend user's email
     * @param period number of days that user got suspend
     * @param suspendTime the start time of suspension
     */
    public void userSuspension(String email, int period, long suspendTime){
        um.suspendUser(email, period, suspendTime);
    }

    /**
     * Freeze users by enter in freeze days
     * @param period number of days that user got frozen
     */
    public void userFreeze(int period){
        um.freezeUser(period);
    }

    /**
     * Unfreeze the users by admin
     */
    public void adminUnfreeze(){
        um.unfreezeUser();
    }

    /**
     * Unfreeze this user when the user logins
     * @param email user's email
     */
    public void userUnfreeze(String email){
        um.userUnfreeze(getId(email));
    }

    /**
     * Returns true iff user login successfully.
     * Returns false iff user's id or password is incorrect.
     * This method is called when user wants to log in by using id and password.
     * It calls the method checkIdPwd of um to return whether the userId matches with the password user entered in.
     *
     * @param email this user's email
     * @param password the password this user entered in
     * @return a boolean that shows whether the id matches to password or not
     */
    public boolean emailLogin(String email, String password){
        return um.checkEmailPwd(email, password);
    }

    /**
     * Return true if and only if the user is not got suspend he/she logins
     * Otherwise return false
     *
     * @param email user's email
     * @param loginTime user's logintime
     * @return true if this user is not got suspend
     */
    public boolean checkSusp(String email,long loginTime){
        return um.checkSuspension(email, loginTime);
    }

    /**
     * Set user's login time to loginTime
     *
     * @param email user's email
     * @param loginTime user's login time
     */
    public void setLoginTime(String email, long loginTime){
        um.setLoginTime(email, loginTime);
    }

    /**
     * Return a Date object that represents the time that user can login back
     *
     * @param email user's email
     * @return a Date object that represent user could log in again
     */
    public Date suspTime(String email){
        return um.displayLeftTime(email);
    }

    /**
     * Returns true iff email this user entered has not been signed up.
     * Otherwise, returns false.
     * This method is called when the user wants to change original email to email, which is the parameter.
     * It calls the method of um, changeEmail to return whether this user changes email successfully.
     *
     * @param id this user's id
     * @param email the new email this user entered in to change to
     * @return a boolean shows that whether the email changes to the new one or not
     */
    public boolean emailChange(String id, String email){
        return um.changeEmail(id, email);
    }

    /**
     * Return password type. Changes this user's password to a new one based on user entered.
     * This method is called when this user wants to change their password.
     *
     * @param id this user's id
     * @param password the new password this user entered in
     * @return password type
     */
    public String pwdChange(String id, String password){
        return um.changePwd(id, password);
    }

    /**
     * Return user's type if the userId entered in exists.
     * Returns 'trial' if this user is a trial user.
     * Returns 'regular' if this user is a regular user.
     * Returns 'admin' if this user is an admin user.
     * Otherwise, return 'No such user'.
     *
     * This method is called to returns this user's type based on user's id.
     * It calls the method getUserType of um
     *
     * @param id the userId for this user
     * @return this user's type
     */
    public String getType(String id){
        return um.getUserType(id);
    }

    /**
     * Return user's type by entering their email
     *
     * @param email user's email
     * @return user's type
     */
    public String getTypeByEmail(String email){
        return um.getUserTypeByEmail(email);
    }

    /**
     * Return user's password type.(too weak, weak, good)
     *
     * @param password user's password
     * @return user's password type
     */
    public String getPwdType(String password){
        return um.pwdType(password);
    }

    /**
     * Return true if and only if the email entered in is valid
     *
     * @param email email entered in
     * @return true if the email entered is valid
     */
    public boolean isEmailValid(String email){
        return um.isValidEmail(email);
    }

    /**
     * Return an arraylist that contains user's id, email and temporary password
     *
     * @param email user's email
     * @return arraylist that contains user's info
     */
    public ArrayList<String> forgotPwd(String email){
        ArrayList<String> info = new ArrayList<>();
        String userId = getId(email);
        String pwd = UUID.randomUUID().toString();//weak
        setTempPwd(userId, pwd);
        info.add(userId);
        info.add(email);
        info.add(pwd);
        return info;
    }

    /**
     * Add the element where key is this user's email and value is temporary password to the HashMap in um.
     * This method is called when this user chooses "Forgot Password"
     *
     * @param userId user's id
     * @param pwd temporary password
     */
    private void setTempPwd(String userId, String pwd){
        HashMap<String, String> tempInfo = um.getTempInfo();
        tempInfo.put(userId, pwd);
    }

    /**
     * Return true if and only if user request forgot password and
     * use temporary password to login
     *
     * @param email user's email
     * @param password user's password
     * @return true if user use temporary password to login when request forgor password
     */
    public boolean checkTemp(String email, String password){
        if (um.getTempInfo().containsKey(getId(email))){
            return um.getTempInfo().get(getId(email)).equals(password);
        }
        return false;
    }

    /**
     * Delete user's temporary password when they login and change their password after
     * they request forgot password
     *
     * @param email user's email
     */
    public void deleteTemp(String email){
        um.getTempInfo().remove(getId(email));
    }

    /**
     * Return user's id if the email entered in exists.
     * Otherwise, return -1 for not found the user.
     *
     * This method is called to returns this user's id based on user's email.
     * It calls the method getId of um. It is not used in Phase 1, but may be used in Phase 2.
     *
     * @param email user's email that they entered id
     * @return user's id or -1 for not found
     */
    public String getId(String email){
        return um.getUserId(email);
    }

    /**
     * Returns true iff this user's id exists.
     *
     * This method is calls checkId method in um and returns its result.
     * It is called when user enters an invalid user's id to log in.
     *
     * @param email user's email
     * @return true if it id exists
     */
    public boolean emailExist(String email){
        return um.checkEmail(email);
    }
}
