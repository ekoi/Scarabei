
package com.jfixby.red.io;

import java.io.OutputStream;

import com.jfixby.cmns.api.io.GZipOutputStream;
import com.jfixby.cmns.api.io.JavaOutputStreamOperator;

public class RedGZipOutputStreamOperator implements JavaOutputStreamOperator {

	private final com.jfixby.cmns.api.io.OutputStream os;
	private GZipOutputStream gzip;

	public RedGZipOutputStreamOperator (final com.jfixby.cmns.api.io.OutputStream os) {
		this.os = os;
		throw new Error("Not implemented yet!");
	}

	@Override
	public void closeStream () {
// IO.forceClose(this.os);
// this.os = null;
	}

	@Override
	public OutputStream getJavaStream () {
		return null;
// if (this.os == null) {
// this.os = new ByteArrayOutputStream();
// }
// return this.os;
	}

}
