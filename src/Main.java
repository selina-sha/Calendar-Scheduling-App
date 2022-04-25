import Controller.*;
import Presenter.UserUI;
import UseCase.ScheduleNotFoundException;
import UseCase.UserNotFoundException;

import java.io.IOException;

/**
 * This class is the Main class, a Controller, which has a main method to run this program.
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
public class Main {

	/**
	 * Facade object facade, UserUi ui are created by passing facade as the parameter.
	 *
	 * This method is called when we runs this program.
	 * It calls the run method in UserUI to runs the text UI of this program.
	 *
	 * @param args is an argument
	 * @throws IOException when the run method in ui throw IOException
	 */
	public static void main(String[] args) throws IOException, UserNotFoundException, ScheduleNotFoundException {
		Facade facade = new Facade();
		UserUI ui = new UserUI(facade);
		ui.run();
	}
}
