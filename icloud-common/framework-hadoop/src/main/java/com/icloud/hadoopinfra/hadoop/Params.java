package com.icloud.hadoopinfra.hadoop;

public class Params {

	private String inputSource;

	private String outputSource;

	private int mapNum;

	private int reduceNum;

	private String queueName;

	private String[] more = new String[0];

	public String getInputSource() {
		return inputSource;
	}

	public void setInputSource(String inputSource) {
		this.inputSource = inputSource;
	}

	public String getOutputSource() {
		return outputSource;
	}

	public void setOutputSource(String outputSource) {
		this.outputSource = outputSource;
	}

	public int getMapNum() {
		return mapNum;
	}

	public void setMapNum(int mapNum) {
		this.mapNum = mapNum;
	}

	public int getReduceNum() {
		return reduceNum;
	}

	public void setReduceNum(int reduceNum) {
		this.reduceNum = reduceNum;
	}

	public String[] getMore() {
		return more;
	}

	public void setMore(String[] more) {
		this.more = more;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public static void printTip(String... tips) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------- params 出错了，以下是提示-----------\n");
		buffer.append("input ").append("mapNum ").append("reducenum ").append("output ").append("<-Dmapred.queue.name=queueName>");
		if (tips != null) {
			for (String tip : tips)
				buffer.append(tip + " ");
		}
		buffer.append("\n");
		buffer.append("其中<>表示可以填写的");
		System.out.println(buffer.toString());
	}

	public static Params getParams(String[] args) {
		Params params = new Params();
		params.setInputSource(args[0]);
		params.setMapNum(Integer.parseInt(args[1]));
		params.setReduceNum(Integer.parseInt(args[2]));
		params.setOutputSource(args[3]);

		// -Dmapred.queue.name=
		if (args.length > 4) {
			String[] temp = new String[args.length - 4];
			for (int i = 4; i < args.length; i++) {
				temp[i - 4] = args[i];
				if (args[i].indexOf("-Dmapred.queue.name=") != -1) {
					params.queueName = args[i].substring(args[i].indexOf("=") + 1);
				}
			}
			params.setMore(temp);
		}

		System.out.println();

		return params;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------- params -----------\n");
		buffer.append("input      = ").append(inputSource).append("\n");
		buffer.append("mapNum     = ").append(mapNum).append("\n");
		buffer.append("reduceNum  = ").append(reduceNum).append("\n");
		buffer.append("output     = ").append(outputSource).append("\n");
		if (queueName != null)
			buffer.append("queueName  = ").append(queueName).append("\n");
		if (null != more) {
			for (int i = 0; i < more.length; i++) {
				buffer.append("more[").append(i).append("]     = ").append(more[i]).append("\n");
			}
		}
		buffer.append("------------------------------\n");
		return buffer.toString();
	}

	public static boolean checkParams(String[] args, int limit) {
		// TODO Auto-generated method stub
		if (null == args || args.length < limit) {
			return false;
		}
		return true;
	}

}
