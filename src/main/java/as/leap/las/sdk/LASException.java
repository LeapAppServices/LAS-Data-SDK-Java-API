package as.leap.las.sdk;

/**
 * @author sneaky
 * @since 3.0.0
 */
public class LASException extends RuntimeException {
  public int code;

  /**
   * @param code
   * @param msg   the detail message
   * @param cause the root cause
   */
  public LASException(int code, String msg, Throwable cause) {
    super(msg, cause);
    this.code = code;
  }

  public LASException(int theCode, String theMessage) {
    super(theMessage);
    this.code = theCode;
  }

  @Override
  public String toString() {
    return "Sun Exception [code=" + code + ", error=" + getMessage() + "]";
  }

}
