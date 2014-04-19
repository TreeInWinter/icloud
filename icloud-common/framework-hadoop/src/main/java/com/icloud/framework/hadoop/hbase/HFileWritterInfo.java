package com.icloud.framework.hadoop.hbase;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.io.hfile.HFile;

public class HFileWritterInfo {

	Path path;
	HFile.Writer writer;
	FSDataOutputStream ostream;

	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public HFile.Writer getWriter() {
		return writer;
	}
	public void setWriter(HFile.Writer writer) {
		this.writer = writer;
	}
	public FSDataOutputStream getOstream() {
		return ostream;
	}
	public void setOstream(FSDataOutputStream ostream) {
		this.ostream = ostream;
	}


}
