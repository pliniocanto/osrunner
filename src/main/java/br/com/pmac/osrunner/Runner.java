package br.com.pmac.osrunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

class RunnerThread extends Thread {

	InputStream is;
	String type;
	private String answer;

	RunnerThread(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			
			
			
			answer = sb.toString();

			

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}

public class Runner {

	private static Logger LOG = Logger.getLogger(Runner.class.getName());
	private int exitVal;
	private String output;

	public String exec(String comando) {

		Process proc = null;

		try {

			String[] cmd = new String[3];

			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("linux")) {
				cmd[0] = "bash";
				cmd[1] = "-c";
				cmd[2] = comando;
			} else {
				cmd[0] = "cmd.exe";
				cmd[1] = "/C";
				cmd[2] = comando;
			}

			Runtime rt = Runtime.getRuntime();
			LOG.info("Running " + cmd[0] + " " + cmd[1] + " " + cmd[2]);

			proc = rt.exec(cmd);

			// any error message?
			RunnerThread error = new RunnerThread(proc.getErrorStream(), "ERROR ");
			// any output?
			RunnerThread runnerOutput = new RunnerThread(proc.getInputStream(), "OUTPUT ");

			error.start();
			runnerOutput.start();

			exitVal = proc.waitFor();
			LOG.info("ExitValue: " + exitVal);
			
			/*
			 * wait error and output finish up
			 */
			error.join();
			runnerOutput.join();
			
			if (error.getAnswer() != null && !error.getAnswer().equals("")) {
				output = error.getAnswer();
				return error.getAnswer();
			} else {
				output = runnerOutput.getAnswer();
				return runnerOutput.getAnswer();
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}

		return "";

	}

	public int getExitVal() {
		return exitVal;
	}

	public void setExitVal(int exitVal) {
		this.exitVal = exitVal;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}
