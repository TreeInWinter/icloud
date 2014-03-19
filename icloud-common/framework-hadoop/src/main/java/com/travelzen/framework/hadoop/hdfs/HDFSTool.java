package com.travelzen.framework.hadoop.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDFSTool {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Configuration configuration;
	private FileSystem fileSystem;

	public HDFSTool(Configuration configuration) {
		this.configuration = configuration;
		getFileSystem();
	}

	public HDFSTool(String... files) {
		configuration = new Configuration();
		for (String file : files)
			configuration.addResource(file);
		getFileSystem();
	}

	public FileSystem getFileSystem() {
		if (fileSystem == null)
			try {
				fileSystem = FileSystem.get(configuration);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		return fileSystem;
	}

	public boolean close() {
		if (fileSystem != null)
			try {
				fileSystem.close();
				return true;
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		return false;
	}

	public FileStatus[] list(String dir) {
		return list(new Path(dir));
	}

	public FileStatus[] list(Path path) {
		try {
			if (!fileSystem.exists(path)) {
				logger.warn("Dir " + path + " does not exists");
				return null;
			}
			return fileSystem.listStatus(path);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public boolean addFile(String file) {
		return addFile(new Path(file));
	}

	public boolean addFile(Path path) {
		try {
			if (fileSystem.exists(path)) { // Check if the file already exists
				logger.warn("File " + path + " already exists");
				return false;
			}
			return fileSystem.createNewFile(path);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public int addFile(String source, String dest) {
		return addFile(new File(source), new Path(dest));
	}

	public int addFile(File source, String dest) {
		return addFile(source, new Path(dest));
	}

	public int addFile(InputStream in, String dest) {
		return addFile(in, new Path(dest));
	}

	public int addFile(String source, Path dest) {
		return addFile(new File(source), dest);
	}

	public int addFile(File source, Path dest) {
		try {
			return addFile(new FileInputStream(source), dest);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	public int addFile(InputStream in, Path dest) {
		OutputStream out = null;
		try {
			if (fileSystem.exists(dest)) { // Check if the file already exists
				logger.warn("File " + dest + " already exists");
				return 0;
			}
			return IOUtils.copy(in, out = fileSystem.create(dest)); // Create a new file and write data to it.
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally { // Close all the file descripters
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return 0;
	}

	public boolean copy(String source, Path dest) {
		try {
			if (!fileSystem.exists(dest)) {
				logger.warn("File " + dest + " does not exists");
				return false;
			}
			fileSystem.copyFromLocalFile(new Path(source), dest);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public boolean copy(Path source, String dest) {
		try {
			if (!fileSystem.exists(source)) {
				logger.warn("File " + source + " does not exists");
				return false;
			}
			fileSystem.copyToLocalFile(source, new Path(dest));
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public byte[] readBytes(String file) {
		return readBytes(new Path(file));
	}

	public byte[] readBytes(Path path) {
		InputStream in = null;
		try {
			return IOUtils.toByteArray(in = read(path));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally { // Close all the file descripters
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return null;
	}

	public List<String> readLines(String file) {
		return readLines(new Path(file));
	}

	public List<String> readLines(Path path) {
		InputStream in = null;
		try {
			return IOUtils.readLines(in = read(path));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally { // Close all the file descripters
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return null;
	}

	public InputStream read(String file) {
		return read(new Path(file));
	}

	public InputStream read(Path path) {
		try {
			if (!fileSystem.exists(path)) {
				logger.warn("File " + path + " does not exists");
				return null;
			}
			return fileSystem.open(path);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public boolean writeFile(byte[] data, String file) {
		return writeFile(data, new Path(file));
	}

	public boolean writeFile(byte[] data, String file, boolean create) {
		return writeFile(data, new Path(file), create);
	}

	public boolean writeFile(byte[] data, Path path) {
		return writeFile(data, path, false);
	}

	public boolean writeFile(byte[] data, Path path, boolean create) {
		OutputStream out = null;
		try {
			if (!fileSystem.exists(path)) {
				logger.warn("File " + path + " does not exists");
				if (create)
					addFile(path);
				else
					return false;
			}
			IOUtils.write(data, out = fileSystem.create(path)); // write data to it.
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally { // Close all the file descripters
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return false;
	}

	public boolean writeFile(CharSequence data, String file) {
		return writeFile(data, new Path(file));
	}

	public boolean writeFile(CharSequence data, String file, boolean create) {
		return writeFile(data, new Path(file), create);
	}

	public boolean writeFile(CharSequence data, Path path) {
		return writeFile(data, path, false);
	}

	public boolean writeFile(CharSequence data, Path path, boolean create) {
		OutputStream out = null;
		try {
			if (!fileSystem.exists(path)) {
				logger.warn("File " + path + " does not exists");
				if (create)
					addFile(path);
				else
					return false;
			}
			IOUtils.write(data, out = fileSystem.create(path)); // write data to it.
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally { // Close all the file descripters
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return false;
	}

	public boolean deleteFile(String file) {
		return deleteFile(new Path(file));
	}

	public boolean deleteFile(Path path) {
		try {
			if (!fileSystem.exists(path)) {
				logger.warn("File " + path + " does not exists");
				return false;
			}
			return fileSystem.delete(path, true); // Delete file
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public boolean mkdir(String dir) {
		return mkdir(new Path(dir));
	}

	public boolean mkdir(Path path) {
		try {
			if (fileSystem.exists(path)) {
				logger.warn("Dir " + path + " already exists");
				return false;
			}
			return fileSystem.mkdirs(path); // Create directories
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
}
