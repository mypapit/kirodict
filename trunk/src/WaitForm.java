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
 * WaitForm.java
 * For displaying Progress bar (Gauge)
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

class WaitForm extends Form {
	testrpc midlet;
	//Gauge gauge;
	/**
	* OVERVIEW
	*
	* Display continuous progress bar, while connecting to the server.
	*
	*
	*/

	public WaitForm(String title, testrpc midlet) {
		/**
		* Contructors
		*
		* EFFECTS : Display progressbar/animation  to provide user feedback
		*
		* @param  title title of the progress bar
		* @param  midlet  the calling application midlet
		*
		*/
		super(title);
		//this.midlet = midlet;

		Gauge displayable = new Gauge("Processing",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
		displayable.setLayout(Item.LAYOUT_VCENTER);
		this.append(displayable);


	}



}
