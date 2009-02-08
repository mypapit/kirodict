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
 * Settings.java
 * For handling settings and persistency
 */

import javax.microedition.rms.*;
import javax.microedition.io.*;
import java.io.*;

public class Settings {
public RecordStore record=null;
public int dictionary=1;
public int progress=1;
public static final String dict_type[] = {"wn","web1913","moby-thes","jargon","foldoc"};

public void Settings ()
{


}


public void load() throws SecurityException,RecordStoreNotFoundException,
	RecordStoreException,RecordStoreFullException,
	IllegalArgumentException,IOException
{
	ByteArrayInputStream bin;
	DataInputStream din;


	record = RecordStore.openRecordStore("settings",true);

	if (record.getNumRecords() == 0) {
		save(0,0,true);
		return;
	}

	byte[] raw = record.getRecord(1);
	bin = new ByteArrayInputStream(raw);
	din = new DataInputStream(bin);

	this.dictionary = din.readInt();
	this.progress = din.readInt();

	try {
		din.close();
		bin.close();
	} catch (Exception ex) {}


}

public void save(int dict, int progress, boolean init) throws SecurityException,
	RecordStoreNotFoundException,RecordStoreException,
	RecordStoreFullException,IllegalArgumentException,IOException
{
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(bos);

	dos.writeInt(dict);
	dos.writeInt(progress);


	byte[] data = bos.toByteArray();

	if (init != true) {
		record.setRecord(1,data,0,data.length);

	} else {
		record.addRecord(data,0,data.length);
	}

	this.progress = progress;
	this.dictionary = dict;

	try {

		dos.close();
		bos.close();
	} catch (Exception e) {

	}


}

public String getDict()
{
	return ( dict_type[this.dictionary]);
}

/*public boolean isProgress() {
	if ( this.progress == 0) {
		return true;
	}

	return false;
}*/

public void close() {
	if (record != null) {
			try {
					record.closeRecordStore();

			} catch (Exception e) {


			}

	}
}

}
