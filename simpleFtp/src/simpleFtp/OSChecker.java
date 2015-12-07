package simpleFtp;

public class OSChecker{

	static public boolean isWindows(){
		String os=System.getProperty("os.name");
		if(os!=null && os.startsWith("Windows")){
			return true;
		}
		else
			return false;
	}
	public static boolean isMacOSX(){
		String os=System.getProperty("os.name");
		if(os!=null && os.startsWith("Mac OS X")){
			return true;
		}
		else{
			return false;
		}
	}
	public static boolean isLinux(){
		String os=System.getProperty("os.name");
		if(os!=null && os.startsWith("Linux")){
			return true;
		}
		else{
			return false;
		}
	}
}