/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as published by
 *  the Free Software Foundation
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Kirodict 1.0 <info@mypapit.net>
 * Copyright 2008 Mohammad Hafiz bin Ismail. All rights reserved.
 *
 * testrpc.java
 * The main MIDlet source code
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import javax.microedition.io.*;
import java.io.*;
import net.mypapit.java.StringTokenizer;

//#if polish.api.mmapi
import javax.wireless.messaging.*;
//#endif

import java.util.*;


public class testrpc extends MIDlet implements CommandListener, ItemCommandListener {

	Form form = new Form("KiroDict");
	Form definitionForm;
	private List list;
	TextField tf = new TextField("Word","",50,TextField.ANY);
	boolean searchMode = false;


	String definition;
//#if polish.usePolishGUI
	Command cmdDefine = new Command("Define", Command.SCREEN,1);
//#else
	Command cmdDefine = new Command("Define", Command.ITEM,1);
//#endif
	Command cmdSearch = new Command("Search", Command.SCREEN,5);
	Command cmdBack = new Command("Back", Command.BACK,80);
	Command cmdBackList = new Command("Back", Command.BACK,80);
	Command cmdExit = new Command("Exit", Command.EXIT,99);
	Command cmdAbout = new Command("About",Command.HELP,70);

//#if polish.api.mmapi
	Command cmdSMS = new Command("SMS definition", Command.ITEM,75);

	Command cmdSend = new Command("Send", Command.SCREEN,77);
//#endif
	AboutForm aboutForm = new AboutForm("About","KiroDict 1.0","/i.png");


	Settings settings = new Settings();
	//SettingsForm settingsForm;
	ChoiceGroup cgDictionary;

//#if polish.api.mmapi
	SendResult sendResult;
//#endif
	//ScrollableMessagesBox scroller;

	Display display;
	Displayable prev=form;

	public testrpc()

	{

		String dict[] = {"Wordnet","Webster 1913","Moby Thesaurus","Jargon","Computing"};

//#style .popupChoiceGroup
		cgDictionary = new ChoiceGroup("Dictionary",Choice.POPUP,dict,null);


		display = Display.getDisplay(this);
//#if !polish.usePolishGUI
		tf.addCommand(cmdDefine);
		tf.setDefaultCommand(cmdDefine);
		tf.setItemCommandListener(this);

//#else
		form.addCommand(cmdDefine);
//#endif



	form.append(tf);
		//form.addCommand(cmdDefine);
		form.addCommand(cmdSearch);
		form.addCommand(cmdExit);
		form.addCommand(cmdAbout);
		form.setCommandListener(this);

		form.append(cgDictionary);


		aboutForm.setCommandListener(this);
		aboutForm.setHyperlink("http://kirostudio.com",this);
		aboutForm.setCopyright("Mohammad Hafiz","2007");
		aboutForm.append("Compact Mobile Dictionary");
		aboutForm.append("\n\nThis program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 2.0");



	try {
		settings.load();
	} catch (Exception ex) {
		//showAlert("Error",ex.getMessage());
	}

		cgDictionary.setSelectedIndex(this.settings.dictionary,true);
	display.setCurrent(form);

	}

	public void startApp()
	{
		/**
		* Called exactly after the application start
		*
		* EFFECTS: display main menu for the first time
		*/

	}


	public void pauseApp() {

	}

	public void showAlert(String title,String text) {
		showAlert(title,text,form);

	}

	public void showAlert(String title,String text,Displayable d) {

		Alert a = new Alert(title,text,null,AlertType.WARNING);
		a.setTimeout(Alert.FOREVER);
		display.setCurrent(a,d);

	}


	public void destroyApp(boolean flag){
		settings.close();
		settings = null;
		notifyDestroyed();
	}

	public void commandAction (Command cmd, Item item) {
		if (cmd == cmdDefine) {
			if (tf.getString().length() < 3) {
							this.showAlert("Warning","Please enter word with more than two letters");
							prev=form;
						} else {

							saveSetting();
							defineWord("define");
						}
		searchMode=false;
		}
//#if polish.api.mmapi
		 else if (cmd == cmdSMS) {
			sendSMS();

		}
//#endif



	}

	public void commandAction(Command c, Displayable d)
	{
		if (c == cmdDefine) {
					if (tf.getString().length() < 3) {
						this.showAlert("Warning","Please enter word with more than two letters");
						prev=form;
					} else {

						saveSetting();
						defineWord("define");
					}
		searchMode=false;

		} else 	if (c == cmdSearch) {
			if (tf.getString().length() < 3) {
						this.showAlert("Warning","Please enter word with more than two letters");
			} else {
				saveSetting();
				searchWord();

				searchMode = true;
			}
		}

		else if (c == cmdBack) {
			if (searchMode==false) {
				display.setCurrent(form);
				//System.out.println("back in definition");
			} else {
				display.setCurrent(list);
				//System.out.println("back in list, searchMode value is " + searchMode);
			}

		} 	else if (c == cmdBackList) {
			display.setCurrent(form);
			list=null;

		}	else if (c== cmdExit) {
			destroyApp(false);

		} else if (c == list.SELECT_COMMAND ) {
			String word=list.getString(list.getSelectedIndex());
			//System.out.println(word);
			defineWord(word,"define");

		} else if (c == cmdAbout) {
			display.setCurrent(aboutForm);

		} else if (c == aboutForm.DISMISS_COMMAND) {
			display.setCurrent(form);
		}

//#if polish.api.mmapi
		  else if (c == cmdSend) {

			sendResult = new SendResult();
			sendResult.tfPhoneNo.addCommand(cmdSMS);
			sendResult.tfPhoneNo.setItemCommandListener(this);
			sendResult.addCommand(cmdBack);
			sendResult.setCommandListener(this);
			display.setCurrent(sendResult);

		}
//#endif
	}

//#if polish.api.mmapi

	public void sendSMS() {

		if (sendResult.tfPhoneNo.getString().length() < 5) {
			showAlert("Alert","Please enter a valid phone number",sendResult);
			return;
		};

		SendSMS s = new SendSMS(this);
		s.start();


	}

//#endif
	public void displaySearchResult(String res)
	{
		String s = (String) res;

		if (s.equals("Can't find match")) {
			showAlert("Alert","Can't find match");
			return;
		}
		Vector v = new Vector(15);
		StringTokenizer tok = new StringTokenizer(s,",");

		while (tok.hasMoreTokens()) {
			v.addElement(tok.nextToken());
		}

		String array[] = new String[v.size()];

		v.copyInto(array);
		v=null;
		list = new List("List of words", List.IMPLICIT, array, null);

		list.addCommand(cmdBackList);
		//list.addCommand(cmdExit);
		list.setCommandListener(this);
		//prev=list;
		searchMode = true;
		display.setCurrent(list);



	}


	public void displayResult(String res)
		{
			this.definition = res;
			definitionForm = new Form(cgDictionary.getString(cgDictionary.getSelectedIndex()) + " definition");
			definitionForm.append(res);
			//searchMode = false;
			definitionForm.addCommand(cmdBack);
			definitionForm.addCommand(cmdExit);
//#if polish.api.mmapi
			definitionForm.addCommand(cmdSend);
			//definitionForm.addCommand(cmdExit);
//#endif
			definitionForm.setCommandListener(this);
			display.setCurrent(definitionForm);

   }

	public void defineWord(String method)
	{
		String word=tf.getString();
		this.defineWord(word,method);
		//Retriever ret = new Retriever(this, "http://localhost/dict/dict.php?define=" + tf.getString());
	}

	public void defineWord(String word, String method)
	{
		WaitForm wf = new WaitForm("Please Wait", this);
		Retriever ret = new Retriever(this,wf, "http://blog.fakap.net/test/dict.php?"+method+"=" +
URLencode(word) +"&db="+URLencode(settings.getDict()));
		display.setCurrent(wf);
		ret.start();
	}

	public void searchWord() {
		Retriever ret = new Retriever(this, "http://blog.fakap.net/test/dict.php?search=" + URLencode(tf.getString())+"&db="+settings.getDict(),1);
		display.setCurrent(new WaitForm("Please wait",this));
		ret.start();
	}



	public String URLencode(String s)
	{
		if (s!=null) {
			StringBuffer tmp = new StringBuffer();
			int i=0;
			try {
				while (true) {
					int b = (int)s.charAt(i++);
					if ((b>=0x30 && b<=0x39) || (b>=0x41 && b<=0x5A) || (b>=0x61 && b<=0x7A)) {
						tmp.append((char)b);
					}
					else {
						tmp.append("%");
						if (b <= 0xf) tmp.append("0");
						tmp.append(Integer.toHexString(b));
					}
				}
			}
			catch (Exception e) {}
			return tmp.toString();
		}
		return null;
}

public void saveSetting() {
		try {
			settings.save(cgDictionary.getSelectedIndex(),0,false);
			} catch (SecurityException se) {
				showAlert("Application Security Error","Not allowed to save settings");
			} catch (RecordStoreFullException rsfe){
				showAlert("Storage Error","Out of allocated space, unable to save settings");
			} catch (RecordStoreException rsnfe) {
				showAlert("Storage Error","Unable to save settings");
			} catch (IOException ioe) {

			} catch (Exception ex) {
				showAlert("Error",ex.getMessage());
}

}


}


class SendSMS implements Runnable {
private testrpc midlet;
private Display display;
private Gauge g;
private Form formRunning;

public SendSMS(testrpc midlet)
{
		this.midlet = midlet;
		display = Display.getDisplay(this.midlet);
		formRunning = new Form("Sending Result");
		g = new Gauge("Processing...",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
		formRunning.append(g);
		display.setCurrent(formRunning);

}

public void start() {
       Thread t = new Thread(this);
       t.start();
}

public void run() {

StringBuffer sb = new StringBuffer("");

boolean prob=false;
//#if polish.api.mmapi

	try {
		String addr = "sms://" + midlet.sendResult.getPhoneNo();
		MessageConnection conn = (MessageConnection) Connector.open(addr);
		TextMessage msg =
		(TextMessage)conn.newMessage(MessageConnection.TEXT_MESSAGE);
		//sb.append("Definition for : " + midlet.tf.getString() +" \n\n" );
		sb.append(midlet.definition);
		sb.append("\n\n-----------\n[Kirodict]\nhttp://dict.kirostudio.com/");
		msg.setPayloadText(sb.toString());
		sb = null;
		conn.send(msg);

		} catch (IllegalArgumentException iae) {
			midlet.showAlert("Information","Please fill in the form",midlet.sendResult);
			prob=true;

		} catch (SecurityException sex) {
			midlet.showAlert("Error","Not Allowed To Send SMS, check security settings",midlet.sendResult);
			prob=true;
		} catch (InterruptedIOException iioex) {
			midlet.showAlert("Sending Timed-Out","Please retry later",midlet.sendResult);
			prob=true;
		}

		catch (Exception e) {
			midlet.showAlert("Error","Send Result failed :" + e.toString(),midlet.sendResult);
			prob=true;
		}

		display.setCurrent(midlet.form);
		if (prob==false){
			midlet.showAlert("Information","Definition sent!");
		}
		midlet.sendResult = null;

//#endif
	}


}
