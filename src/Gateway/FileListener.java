package Gateway;

import java.io.IOException;

/**
 * An interface that contains one method update, which is implemented by each listener(ReadFileListener, SendFileListener)
 *
 * @author Qing Lyu
 * @author Zhen Cheng
 */
public interface FileListener {
	/**
	 * called by notify method in FilePublisher when there is any action (read/save) happens.
	 * @param fileType is the fileType that would be read/saved when this method is called
	 * @throws IOException when its implemented methods throw exception
	 */
	void update(String fileType) throws IOException;

}
