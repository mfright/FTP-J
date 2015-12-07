package simpleFtp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.omg.CORBA.portable.ApplicationException;

public class mainWindow extends JFrame implements ActionListener,MouseListener,WindowListener{

	private JPanel contentPane;

	private JButton btnConnect = new JButton("CONNECT");
	private JLabel lblHostName = new JLabel("Server host:");
	private final JLabel lblUserID = new JLabel("User-ID:");
	private final JLabel lblPassword = new JLabel("Password:");
	private JTextField txtServerHost;
	private JTextField txtUserID;
	private JTextField txtPassword;
	private JList lstLocal;
	private JList lstRemote;
	private JLabel lblLocalPath = new JLabel("Local path");
	private JLabel lblRemotePath = new JLabel("(Not connected)");
	private final JLabel lblStatus = new JLabel("Enter Server-host, user-ID, Password and click the CONNECT button.");
	protected DefaultListModel modelLocal;
	protected DefaultListModel modelRemote;
	private JButton btnDisconnect = new JButton("DISCONNECT");
	FileOutputStream ostream = null;
    FTPClient ftpclient = new FTPClient();
    private final JButton btnDownload = new JButton("<");
    JButton btnParentLocal = new JButton("^ PARENT");
    private final JButton btnParentRemote = new JButton("^ PARENT");
    private final JScrollPane scrollPaneRemote = new JScrollPane();
    private JButton btnUpload = new JButton(">");
    private final JButton btnReloadLocal = new JButton("* Reload");
    private final JButton btnReloadRemote = new JButton("* Reload");
    private final JButton btnDeleteLocal = new JButton(">< Delete");
    private final JButton btnDeleteRemote = new JButton(">< Delete");
    JCheckBox chkPassiveMode = new JCheckBox("use PASSIVE mode.");
    JCheckBox chkNlst = new JCheckBox("use NLST");
    private JTextField txtFilter;
    JButton btnFilter = new JButton("FILTER");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow frame = new mainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainWindow() {
		setResizable(false);

		// Show elements in main form.
		setTitle("FTP-J");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 874, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		lblHostName.setBounds(12, 10, 73, 13);
		contentPane.add(lblHostName);


		btnConnect.addActionListener(this);
		btnConnect.setBounds(619, 30, 111, 21);
		contentPane.add(btnConnect);

		lblUserID.setBounds(343, 10, 50, 13);
		contentPane.add(lblUserID);

		lblPassword.setBounds(609, 7, 67, 13);
		contentPane.add(lblPassword);

		txtServerHost = new JTextField();
		txtServerHost.setBounds(85, 7, 221, 19);
		contentPane.add(txtServerHost);
		txtServerHost.setColumns(10);

		txtUserID = new JTextField();
		txtUserID.setBounds(392, 7, 169, 19);
		contentPane.add(txtUserID);
		txtUserID.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(677, 4, 146, 19);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);

		modelLocal = new DefaultListModel();

		modelRemote = new DefaultListModel();

		lblLocalPath.setBounds(12, 107, 412, 13);
		contentPane.add(lblLocalPath);


		lblRemotePath.setBounds(471, 107, 385, 13);
		contentPane.add(lblRemotePath);
		lblStatus.setFont(new Font("MS UI Gothic", Font.PLAIN, 18));
		lblStatus.setForeground(Color.RED);

		lblStatus.setBounds(12, 498, 619, 30);

		contentPane.add(lblStatus);
		btnDisconnect.setEnabled(false);
		btnDisconnect.addActionListener(this);


		btnDisconnect.setBounds(732, 29, 111, 23);
		contentPane.add(btnDisconnect);

		btnDownload.addActionListener(this);
		btnDownload.setBounds(426, 302, 45, 82);
		contentPane.add(btnDownload);


		btnParentLocal.addActionListener(this);
		btnParentLocal.setBounds(125, 87, 91, 21);
		contentPane.add(btnParentLocal);
		btnParentRemote.setEnabled(false);

		btnParentRemote.addActionListener(this);
		btnParentRemote.setBounds(561, 87, 91, 21);
		contentPane.add(btnParentRemote);

		JScrollPane scrollPaneLocal = new JScrollPane();
		scrollPaneLocal.setBounds(12, 130, 412, 358);
		contentPane.add(scrollPaneLocal);
		lstLocal =  new JList(modelLocal);
		scrollPaneLocal.setViewportView(lstLocal);
		scrollPaneRemote.setBounds(471, 130, 385, 330);

		contentPane.add(scrollPaneRemote);
		lstRemote = new JList(modelRemote);
		scrollPaneRemote.setViewportView(lstRemote);

		btnUpload.addActionListener(this);
		btnUpload.setBounds(426, 165, 45, 82);
		contentPane.add(btnUpload);

		btnReloadLocal.addActionListener(this);
		btnReloadLocal.setBounds(228, 87, 91, 21);
		contentPane.add(btnReloadLocal);
		btnReloadRemote.setEnabled(false);

		btnReloadRemote.addActionListener(this);
		btnReloadRemote.setBounds(664, 87, 91, 21);
		contentPane.add(btnReloadRemote);

		btnDeleteLocal.addActionListener(this);
		btnDeleteLocal.setBounds(333, 87, 91, 21);
		contentPane.add(btnDeleteLocal);
		btnDeleteRemote.setEnabled(false);

		btnDeleteRemote.addActionListener(this);
		btnDeleteRemote.setBounds(767, 87, 91, 21);
		contentPane.add(btnDeleteRemote);

		JLabel lblLocal = new JLabel("Local");
		lblLocal.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		lblLocal.setBounds(12, 76, 73, 30);
		contentPane.add(lblLocal);

		JLabel lblRemote = new JLabel("Remote");
		lblRemote.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		lblRemote.setBounds(471, 76, 78, 30);
		contentPane.add(lblRemote);

		JLabel lblUpload = new JLabel("UP");
		lblUpload.setBounds(439, 149, 32, 13);
		contentPane.add(lblUpload);

		JLabel lblDownload = new JLabel("Down");
		lblDownload.setBounds(433, 284, 38, 13);
		contentPane.add(lblDownload);
		chkNlst.setBounds(508, 30, 103, 21);
		contentPane.add(chkNlst);


		chkPassiveMode.setBounds(353, 30, 138, 21);
		contentPane.add(chkPassiveMode);

		txtFilter = new JTextField();
		txtFilter.setBounds(519, 470, 237, 19);
		contentPane.add(txtFilter);
		txtFilter.setColumns(10);

		JLabel lblFilter = new JLabel("Filter:");
		lblFilter.setBounds(471, 475, 50, 13);
		contentPane.add(lblFilter);

		btnFilter.addActionListener(this);
		btnFilter.setBounds(767, 470, 91, 21);
		contentPane.add(btnFilter);



		lstRemote.addMouseListener(this);
		lstLocal.addMouseListener(this);



		// Initialize lstLocal
		String path = new File(".").getAbsoluteFile().getParent();
		loadLocal(path);

		// load account.ini
		loadAccount();
	}

	/**
	 * Event Handler.
	 * When pushed buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton sourceButton = (JButton)e.getSource();

		if(sourceButton.equals(btnConnect)){
			connect();

		}else if(sourceButton.equals(btnDisconnect)){
			disconnect();

		}else if(sourceButton.equals(btnDownload)){
			//download();
			downloads();

		}else if(sourceButton.equals(btnParentLocal)){
			parentLocal();

		}else if(sourceButton.equals(btnParentRemote)){
			parentRemote();

		}else if(sourceButton.equals(btnUpload)){
			//upload();
			uploads();

		}else if(sourceButton.equals(btnReloadLocal)){
			loadLocal(lblLocalPath.getText());

		}else if(sourceButton.equals(btnReloadRemote)){
			loadRemote();

		}else if(sourceButton.equals(btnDeleteLocal)){
			deleteLocal();

		}else if(sourceButton.equals(btnDeleteRemote)){
			deleteRemote();

		}else if(sourceButton.equals(btnFilter)){
			filter();
		}
	}

	/*
	 * Event handler.
	 * When The lists clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {

		Object clickedObject = mouseEvent.getSource();

		int clickedCount = mouseEvent.getClickCount();




		if(clickedObject.equals(lstLocal) && clickedCount > 1){
			String selectedString = ((String)lstLocal.getSelectedValue()).substring(9);

			File clickedFile = new File(lblLocalPath.getText()+"\\"+selectedString);

			if(clickedFile.isDirectory()){
				lblLocalPath.setText(clickedFile.getPath());
				loadLocal(lblLocalPath.getText());
			}else if(clickedFile.isFile()){
				runFile(lblLocalPath.getText()+"\\"+selectedString);
			}

		}else if(clickedObject.equals(lstRemote) && clickedCount > 1){
			String selectedString = (String)lstRemote.getSelectedValue();
			String newPath = lblRemotePath.getText() + "/" + selectedString;

			try {
				ftpclient.changeWorkingDirectory(newPath);
				loadRemote();

			} catch (IOException e) {
				System.out.println("Changedir error.");
				JOptionPane.showMessageDialog(null, "Changedir error.");

				e.printStackTrace();
			}

		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}








	// Delete a local file.
	private void deleteLocal(){

		Object[] selectedObjects = lstLocal.getSelectedValues();
		String[] deleteFileNames = new String[selectedObjects.length];
		for(int i=0;i<selectedObjects.length;i++){

			deleteFileNames[i] = lblLocalPath.getText() + "\\" + ((String)selectedObjects[i]).substring(9);

			int option = JOptionPane.showConfirmDialog(this, "Do you want to delete "+(String)selectedObjects[i]+" ?");
			if (option == JOptionPane.YES_OPTION){
				File target = new File(deleteFileNames[i]);
				if(target.exists()){
					target.delete();

					System.out.println("File deleted: "+ deleteFileNames[i]);
					lblStatus.setText("File deleted: "+ deleteFileNames[i]);

				}else{
					System.out.println("ERROR: File not exists: "+ deleteFileNames[i]);
					lblStatus.setText("ERROR: File not exists: "+ deleteFileNames[i]);
				}
			}
		}

		loadLocal(lblLocalPath.getText());
	}

	// Delete a remote file.
	private void deleteRemote(){

		Object[] selectedObjects = lstRemote.getSelectedValues();
		String[] deleteFileNames = new String[selectedObjects.length];
		for(int i=0;i<selectedObjects.length;i++){
			deleteFileNames[i] = (String)selectedObjects[i];

			int option = JOptionPane.showConfirmDialog(this, "Do you want to delete "+(String)selectedObjects[i]+" ?");
			if (option == JOptionPane.YES_OPTION){
				try{
					ftpclient.deleteFile(deleteFileNames[i]);

				}catch(Exception ex){

				}

				try{
					ftpclient.removeDirectory(deleteFileNames[i]);

				}catch(Exception ex){

				}

			}
		}

		loadRemote();

		/*
		String deleteFile = (String)lstRemote.getSelectedValue();

		int option = JOptionPane.showConfirmDialog(this, "Do you want to delete "+deleteFile+" ?");
		if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION){
			return;
		}

		try{
			ftpclient.deleteFile(deleteFile);

		}catch(Exception ex){

		}

		try{
			ftpclient.removeDirectory(deleteFile);

		}catch(Exception ex){

		}

		loadRemote();
		*/
	}


	// ChangeDir to parent on remote.
	private void parentRemote(){

		String currentPath = lblRemotePath.getText();

		if(currentPath.length()<2){
			return;
		}
		/*
		int end = currentPath.lastIndexOf('/');
		String newPath = currentPath.substring(0,end);
		System.out.println(newPath);
		*/

		try {
			ftpclient.changeWorkingDirectory("..");
			loadRemote();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ChangeDir to parent on local.
	private void parentLocal(){
		File currentDir = new File(lblLocalPath.getText());

		String currentPath = currentDir.getPath();

		if(currentPath.length()>4){
			lblLocalPath.setText(currentDir.getParent());
			loadLocal(lblLocalPath.getText());
		}
	}

	// Load local files.
	private void loadLocal(String path){
		modelLocal.clear();

		File dir = new File(path);
	    File[] files = dir.listFiles();
	    for (int i = 0; i < files.length; i++) {
	        File file = files[i];
	        String fileName = file.getName();

	        if(file.isDirectory()){
	        	modelLocal.addElement("[Folder] "+fileName);
	        }else if(file.isFile()){
	        	modelLocal.addElement("[_File_] "+fileName);
	        }else{
	        	modelLocal.addElement("[      ] "+fileName);
	        }
	    }
	    lblLocalPath.setText(path);

	}

	// Connect to the server.
	private void connect(){

		System.out.println("Connecting to " + txtServerHost.getText() + " ...");
		lblStatus.setText("Connecting to " + txtServerHost.getText() + " ...");

		try {
            // Connect to the server.
            ftpclient.connect( txtServerHost.getText() );
            int reply = ftpclient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.err.println("CONNECT FAILED");
                lblStatus.setText("CONNECT FAILED. Check Server-host.");
                return;
            }

            // Log in.
            if (ftpclient.login(txtUserID.getText(), txtPassword.getText()) == false) {
                System.err.println("LOGIN FAILED");
                lblStatus.setText("LOGIN FAILED. Check your password.");
                return;
            }

            // Use binary mode.
            ftpclient.setFileType(FTP.BINARY_FILE_TYPE);

            // Set passive mode.
            if(chkPassiveMode.isSelected()){
            	ftpclient.enterLocalPassiveMode();
            	System.out.println("Passive mode.");
            }else{
            	System.out.println("Active mode");
            }

            System.out.println("Connected to: "+txtServerHost.getText());
            lblStatus.setText("Connected to: "+txtServerHost.getText());

            loadRemote();

            btnDisconnect.setEnabled(true);
            btnConnect.setEnabled(false);
            txtServerHost.setEnabled(false);
            txtUserID.setEnabled(false);
            txtPassword.setEnabled(false);
            btnParentRemote.setEnabled(true);
            btnReloadRemote.setEnabled(true);
            btnDeleteRemote.setEnabled(true);
            chkPassiveMode.setEnabled(false);
            chkNlst.setEnabled(false);

            saveAccount();

        }
        catch(Exception e) {
            e.printStackTrace();
        }


	}

	// Save account information to accoutn.ini.
	private void saveAccount(){
		String account = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\r\n";
		account +="<root>\r\n";
		account +="  <host>" + txtServerHost.getText() + "</host>\r\n";
		account +="  <user>" + txtUserID.getText() + "</user>\r\n";
		account +="  <pass>" + txtPassword.getText() + "</pass>\r\n";

		if(chkPassiveMode.isSelected()){
			account +="  <pasv>true</pasv>\r\n";
		}else{
			account +="  <pasv>false</pasv>\r\n";
		}

		if(chkNlst.isSelected()){
			account +="  <nlst>true</nlst>\r\n";
		}else{
			account +="  <nlst>false</nlst>\r\n";
		}


		account +="</root>\r\n";

		try{
			  File file = new File("account.ini");
			  FileWriter filewriter = new FileWriter(file);

			  filewriter.write(account);
			  filewriter.close();
			}catch(IOException e){
			  System.out.println(e);
			}
	}

	// Load account information from account.ini
	private void loadAccount(){
		String account = "";

		try{
			  File file = new File("account.ini");
			  BufferedReader br = new BufferedReader(new FileReader(file));

			  String str;
			  while((str = br.readLine()) != null){
			    account += str;
			  }

			  br.close();
		}catch(FileNotFoundException e){
			  System.out.println(e);
		}catch(IOException e){
			  System.out.println(e);
		}

		int start = 0, end = 0;

		start = account.indexOf("<host>")+6;
		end = account.indexOf("</host>",start);
		if(start > 6 && end > start){
			txtServerHost.setText(account.substring(start,end));
		}

		start = account.indexOf("<user>")+6;
		end = account.indexOf("</user>",start);
		if(start > 6 && end > start){
			txtUserID.setText(account.substring(start,end));
		}

		start = account.indexOf("<pass>")+6;
		end = account.indexOf("</pass>",start);
		if(start > 6 && end > start){
			txtPassword.setText(account.substring(start,end));
		}

		if(account.indexOf("<pasv>true</pasv>") > 0){
			chkPassiveMode.setSelected(true);
		}

		if(account.indexOf("<nlst>true</nlst>") > 0){
			chkNlst.setSelected(true);
		}
	}

	// Disconnect
	private void disconnect(){


		modelRemote.clear();

		if (ftpclient.isConnected()){
			try {
				ftpclient.disconnect();

				lblStatus.setText("Disconnected");
				System.out.println("Disconnected.");


			} catch (IOException e1) {
				e1.printStackTrace();
				lblStatus.setText("Disconnect ERROR");
				System.out.println(e1.getMessage());
			}

			lblRemotePath.setText("(Not connected)");
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);
            txtServerHost.setEnabled(true);
            txtUserID.setEnabled(true);
            txtPassword.setEnabled(true);
            btnParentRemote.setEnabled(false);
            btnReloadRemote.setEnabled(false);
            btnDeleteRemote.setEnabled(false);
            chkPassiveMode.setEnabled(true);
            chkNlst.setEnabled(true);
        }

        if (ostream != null) {
            try {
                ostream.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

	}

	// Set filter on modelRemote.
	private void filter(){
		ArrayList arrayFiles = new ArrayList();
		String fileName = "";
		String keyword = txtFilter.getText();
		System.out.println("Filtered: "+keyword);

		for(int i=0;i<modelRemote.getSize();i++){
			fileName = (String) modelRemote.getElementAt(i);
			if(fileName.indexOf(keyword) > -1){
				arrayFiles.add(fileName);
			}
		}

		modelRemote.clear();

		if(arrayFiles.size() < 1){
			return;
		}

		for(int i=0;i<arrayFiles.size();i++){
			modelRemote.addElement(arrayFiles.get(i));
		}

		lblStatus.setText("Filtered with "+keyword);
	}

	// Load files on remote.
	private void loadRemote(){
		modelRemote.clear();

		try{
			if(chkNlst.isSelected()){
				////////////////listFiles() ///////////
				FTPFile[] list = ftpclient.listFiles();
	            for (int i=0; i<list.length; i++) {
	                String name = list[i].getName();
	                String group = list[i].getGroup();
	                //System.out.println("group : "+group);
	                //System.out.println("name  : "+name);
	                modelRemote.addElement(name);
	            }

			}else{
				////////////////listNames() //////////////
				String[] names = ftpclient.listNames();
	            List<String> listNames = Arrays.asList(names);
	            if(listNames.size()<20){
	            	Collections.sort(listNames);
	            }
	            for (int i=0; i<listNames.size(); i++) {
	                modelRemote.addElement(listNames.get(i));

	            }

			}

			// Print path.
            String path = ftpclient.printWorkingDirectory();
            int start = path.indexOf('"')+1;
            int end = path.indexOf('"',start+1);
            if(start > 0 && end > start){
            	path = path.substring(start, end);
            }



            lblRemotePath.setText(path);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void downloads(){
		String downloadedDirPath = lblLocalPath.getText();
		@SuppressWarnings("deprecation")
		//String[] downloadFileNames = (String[]) lstRemote.getSelectedValues();

		Object[] selectedObjects = lstRemote.getSelectedValues();
		String[] downloadFileNames = new String[selectedObjects.length];
		for(int i=0;i<selectedObjects.length;i++){
			downloadFileNames[i] = (String)selectedObjects[i];
		}


		if(downloadFileNames.length < 1){
			return;
		}

		// Check file exists before downloading...
		for(int i=0;i<modelLocal.size();i++){
			for(int j=0;j<downloadFileNames.length;j++){
				if(((String)modelLocal.get(i)).substring(9).equals(downloadFileNames[j])){
					int option = JOptionPane.showConfirmDialog(this, "File:"+downloadFileNames[j]+" is still exists in the folder. Override?");
					if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION){
					      return;
					}
				}
			}
		}

		try{
			// Receive file.
			for(int j=0;j<downloadFileNames.length;j++){
	            ostream = new FileOutputStream(downloadedDirPath+"\\"+downloadFileNames[j]);
	            ftpclient.retrieveFile(downloadFileNames[j], ostream);
	            ostream.close();
	            System.out.println("Downloaded: "+downloadFileNames[j]);
			}

            lblStatus.setText("Downloaded: "+downloadFileNames[downloadFileNames.length-1]);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			lblStatus.setText("DOWNLOAD ERROR.");
		}

		loadLocal(lblLocalPath.getText());


	}

	/*
	// Download file from the server.
	private void download(){

		String downloadedDirPath = lblLocalPath.getText();
		String downloadFileName = (String) lstRemote.getSelectedValue();

		// Check file exists before downloading...
		for(int i=0;i<modelLocal.size();i++){
			if(((String)modelLocal.get(i)).substring(9).equals(downloadFileName)){
				int option = JOptionPane.showConfirmDialog(this, "File:"+downloadFileName+" is still exists in the folder. Override?");
				if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION){
				      return;
				}
			}
		}

		try{
			// Receive file.
            ostream = new FileOutputStream(downloadedDirPath+"\\"+downloadFileName);
            ftpclient.retrieveFile(downloadFileName, ostream);
            ostream.close();

            System.out.println("Downloaded: "+downloadFileName);
            lblStatus.setText("Downloaded: "+downloadFileName);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			lblStatus.setText("DOWNLOAD ERROR.");
		}

		loadLocal(lblLocalPath.getText());


	}
	*/

	private void uploads(){
		String uploadFileBase = lblLocalPath.getText() + "\\";

		Object[] uploadFileObjects = lstLocal.getSelectedValues();
		if(uploadFileObjects.length < 1){
			return;
		}

		String[] uploadFileNames = new String[uploadFileObjects.length];
		for(int i=0;i<uploadFileObjects.length;i++){
			uploadFileNames[i] = ((String)uploadFileObjects[i]).substring(9);
		}

		// Check file exists before uploading...
		for(int i=0;i<modelRemote.size();i++){
			for(int j=0;j<uploadFileNames.length;j++){
				if(uploadFileNames[j].equals((String)modelRemote.get(i))){
					int option = JOptionPane.showConfirmDialog(this, "File:"+uploadFileNames[j]+" is still exists on the server. Override?");
					if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION){
					      return;
					}
				}
			}
		}



		try{
			// Storeing file.
			for(int j=0;j<uploadFileNames.length;j++){
				FileInputStream fis = new FileInputStream(uploadFileBase + uploadFileNames[j]);
	            ftpclient.storeFile(uploadFileNames[j], fis);
	            fis.close();
	            printFtpReply(ftpclient);

	            System.out.println("Uploaded: "+ uploadFileNames[j]);
			}
            lblStatus.setText("Uploaded: "+ uploadFileNames[uploadFileNames.length-1]);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			lblStatus.setText("UPLOAD ERROR.");
		}

		loadRemote();
	}


	private void upload(){


		String uploadFileName = ((String) lstLocal.getSelectedValue()).substring(9);
		String uploadFilePath = lblLocalPath.getText() + "\\" + uploadFileName;

		for(int i=0;i<modelRemote.size();i++){
			if(uploadFileName.equals((String)modelRemote.get(i))){
				int option = JOptionPane.showConfirmDialog(this, "File:"+uploadFileName+" is still exists on the server. Override?");
				if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION){
				      return;
				}
			}
		}

		System.out.println("uploadFilePath:"+uploadFilePath);

		try{
			// Storeing file.
			FileInputStream fis = new FileInputStream(uploadFilePath);
            ftpclient.storeFile(uploadFileName, fis);
            fis.close();
            printFtpReply(ftpclient);

            System.out.println("Uploaded: "+ uploadFileName);
            lblStatus.setText("Uploaded: "+ uploadFileName);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			lblStatus.setText("UPLOAD ERROR.");
		}

		loadRemote();
	}

	private static void printFtpReply(FTPClient ftpClient)throws ApplicationException {

        System.out.print(ftpClient.getReplyString());

        int replyCode = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(replyCode)) {
            System.out.println("Sent FTP command had failed!");
        }
    }

	private void runFile(String filePath){
		if(OSChecker.isWindows()){
			Runtime r = Runtime.getRuntime();

			try {
				Process process = r.exec("cmd /c "+filePath);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		disconnect();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}
}
