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
 * SendResult.java
 * Send SMS, Bluetooth Text interface..
 */

import javax.microedition.lcdui.*;


public class SendResult extends Form
{

public Command cmdSMS;
public Command cmdBack;

public TextField tfName, tfPhoneNo;

public SendResult() {
	super("Send Definition");

	StringItem si = new StringItem("Instruction","Enter phone number to send the dictionary definition to");
	tfPhoneNo = new TextField("Phone Number ","",48,TextField.PHONENUMBER);

	this.append(si);
	this.append(tfPhoneNo);


}

public String getName()
{

	return tfName.getString();
}

public String getPhoneNo()
{

	return tfPhoneNo.getString();
}


}
