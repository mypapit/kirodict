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
 * Retriever.java
 * For retrieving Dictionary definition and results.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.util.*;
import java.io.*;


class Retriever extends Thread {
	/**
	* OVERVIEW
	*
	* Handles connection to the remote server and data retrieving.
	*
	*
	*/
	//private HttpTransport req;
	private testrpc midlet;
	private String server;
	private HttpConnection http;
	private InputStream in;
	private int method;
	private Form  wf;

	/**
	* Constructor
	*
	*/
	public Retriever(testrpc midlet, Form  wf, String server)
	{
		/**
		  * Constructor
		  *
		  * EFFECTS: Initialise the server and store midlet information
		  *
		  * @param midlet The main application midlet
		  * @param server Forecast Server URL
		  *
		  */
		this.midlet = midlet;
		this.server = server;
		this.wf = wf;

	}

	public Retriever(testrpc midlet, String server, int method)
	{

		this(midlet,new Form(""), server);
		this.method = method;
	}

	public void run()
	{
		/**
		* Entry point of the thread
		*
		* EFFECTS: call to connect() method
		*/
		connect();


	}

	public void connect()
	{
		try{
				http = (HttpConnection) Connector.open(server);
				in = http.openInputStream();

				if (http.getResponseCode() == http.HTTP_NOT_FOUND){
					throw new InvalidServerException(this.server);
				} else if (http.getResponseCode() == http.HTTP_MOVED_PERM) {

					this.server = "http://dict.kirostudio.com/define";
				} else if (http.getResponseCode() != http.HTTP_OK) {
						throw new InvalidServerException(this.server);
				}

				int contentLength = (int) http.getLength();
				wf.append("Connected!");
				if (contentLength == -1) {
					contentLength = 2950;
				}


				byte[] raw = new byte[contentLength];
				int length =0,curr=0;
				//int length=in.read(raw);
				StringBuffer sbuffer = new StringBuffer(512);
				while ( (length=in.read(raw)) >= 0) {

					sbuffer.append(new String(raw,curr,length));
					curr+=length;
				}


				wf.append("Formatting contents");
				if (this.method!=1) {
					//midlet.displayResult(new String(raw,0,length));
					midlet.displayResult(sbuffer.toString());
				} else if (sbuffer.toString().equals("Can't find match")) {
						midlet.showAlert("Alert","Can't find match");
				} else {
					midlet.displaySearchResult(sbuffer.toString());
				}
				raw =null;
				wf= null;


		} catch (SecurityException se) 	{
					//se.printStackTrace ();
					this.midlet.showAlert("Unable to access network","This application requires active network connection!");
		} catch (InvalidServerException ise) {
			this.midlet.showAlert("Server Not Found","Please try again or upgrade this application - http://kirostudio.com/");
		} catch (IOException ioe) 	{
					//ioe.printStackTrace ();
					this.midlet.showAlert("Unable to access network","Please try again later");
		}	catch (Exception e) 	{
					//e.printStackTrace ();
					this.midlet.showAlert("Error",e.toString());
					e.printStackTrace();

		} finally {
			if (in !=null) {
				try {in.close();} catch (Exception ex) {}
			}
			if (http !=null) {
				try {http.close();} catch (Exception ex){}
			}
			http=null;
			in = null;


		}


	}

}

class InvalidServerException extends Exception {
	public InvalidServerException (String s)
	{
		super(s);
	}

}
