package edu.isel.csee.jchecker.core;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.isel.csee.jchecker.core.stage.*;
import edu.isel.csee.jchecker.score.*;
import edu.isel.csee.jchecker.statics.*;
import edu.isel.csee.jchecker.statics.utils.EntireContentParser;
import edu.isel.csee.jchecker.statics.utils.FileGenerationChecker;

public class CoreGrader {
	private boolean flag = false;
	private ArrayList<String> inputViolations = new ArrayList();
	private ArrayList<String> checksumViolations = new ArrayList();
	private List<String> srcList = new ArrayList();
	private int iViolationCount = 0;
	private int cViolationCount = 0;
	private int totalViolationCount = 0;
	private int checkCompile;
	private boolean isChecksum = false;
	private String mainPath = "";
	private String libPath = "";
	private String workpath = "";
	private String gradlePath = "";

	public String start(String path, String policy) {
		workpath = path;

		EvaluationSchemeMapper scheme = new EvaluationSchemeMapper();
		JsonObject policyObject = (JsonObject)(new Gson()).fromJson(policy, JsonObject.class);
		JsonObject score = new JsonObject();
		EntireContentParser source = new EntireContentParser();

		// validate the policy JSON file before parsing policy data
		new PolicyValidator().validator(policyObject);

		new PolicyParser().parse(scheme, policyObject);

		score.addProperty("token", scheme.getToken());
		score.addProperty("isDirect", scheme.isDirect());
		score.addProperty("className", scheme.getClassName());
		score.addProperty("instructor", scheme.getInstructor());
		score.addProperty("point", scheme.getPoint());

		flag = scheme.isBTool() ? true : false;
		IGradeStage grader = scheme.isBTool() ? new GradleStage() : new JavaStage();

		if (scheme.isBTool()) {
			gradlePath = findFilePath(path, ".", "build.gradle");

			if (gradlePath != null) {
				workpath = path + gradlePath.replace("build.gradle", "").substring(1);
			}

			System.out.println(workpath);
		}

		isChecksum = scheme.isChecksum();

		checkCompile = grader.compile(workpath);

		libPath = findFilePath(workpath, "build/libs", "*.jar");
		srcList = source.getAllFiles(workpath);

		if (!scheme.isBTool()) {
			mainPath = scheme.getReqClass().get(0);
		}

		JsonObject item_compile = new JsonObject();;
		JsonObject item_oracle = new JsonObject();

		if (checkCompile == 0) {
			item_compile.addProperty("violation", false);
			item_compile.addProperty("deductedPoint", 0);

			score.add("compile", item_compile);

			if (scheme.isTest()) {
				for (int i = 0; i < scheme.getInputs().size(); i ++) {
					boolean iResult = false;
					boolean cResult = !isChecksum;

					if (!flag) {
						iResult = grader.build(grader.getTest(mainPath, scheme.getInputs().get(i), scheme.isTest()), scheme.getOutputs().get(i), workpath);
					} else {
						iResult = grader.build(grader.getTest(scheme.getInputs().get(i), scheme.isTest()), scheme.getOutputs().get(i), workpath);
					}

					if (!iResult) {
						inputViolations.add(String.valueOf(i + 1));
						iViolationCount ++;
					}

					if (isChecksum) {
						try {
							if (scheme.getChecksums().get(i).isEmpty()) {
								cResult = true;
							} else {
								cResult = FileGenerationChecker.compareChecksum(scheme.getChecksums().get(i), scheme.getReqFilePath().get(i), workpath);
							}
						} catch (Exception e) {
							System.out.println("Error part : checksum");
							e.printStackTrace();
						}

						if (!cResult) {
							checksumViolations.add(String.valueOf(i + 1));
							cViolationCount++;
						}
					}

					if (!cResult || !iResult) {
						totalViolationCount ++;
					}
				}

				if (iViolationCount > 0) {
					item_oracle.addProperty("violation", true);
				} else {
					item_oracle.addProperty("violation", false);
				}

				if (cViolationCount > 0) {
					item_oracle.addProperty("checksum", true);
				} else {
					item_oracle.addProperty("checksum", false);
				}

				item_oracle.add("violationNumber", (new Gson()).toJsonTree(inputViolations));
				item_oracle.addProperty("inputViolationCount", iViolationCount);
				item_oracle.add("checksumNumber", (new Gson()).toJsonTree(checksumViolations));
				item_oracle.addProperty("checksumViolationCount", cViolationCount);

				double deducted = scheme.getRuntime_deduct_point() * (double)totalViolationCount;

				if (deducted > scheme.getRuntime_max_deduct()) {
					deducted = scheme.getRuntime_max_deduct();
				}

				scheme.deduct_point((float)deducted);
				item_oracle.addProperty("deductedPoint", (float)deducted);
				score.add("oracle", item_oracle);
			}
		} else {
			item_compile.addProperty("violation", true);
			item_compile.addProperty("deductedPoint", scheme.getCompiled_deduct_point());
			score.add("compile", item_compile);

			if (scheme.isTest()) {
				for (int i = 0; i < scheme.getInputs().size(); i ++) {
					inputViolations.add(String.valueOf(i + 1));
				}

				for (int i = 0; i < scheme.getChecksums().size(); i ++) {
					checksumViolations.add(String.valueOf(i + 1));
				}

				if (scheme.getInputs().size() > 0) {
					item_oracle.addProperty("violation", true);
				} else {
					item_oracle.addProperty("violation", false);
				}

				if (scheme.getChecksums().size() > 0) {
					item_oracle.addProperty("checksum", true);
				} else {
					item_oracle.addProperty("checksum", false);
				}

				item_oracle.add("violationNumber", (new Gson()).toJsonTree(inputViolations));
				item_oracle.addProperty("inputViolationCount", scheme.getInputs().size());
				item_oracle.add("checksumNumber", (new Gson()).toJsonTree(checksumViolations));
				item_oracle.addProperty("checksumViolationCount", scheme.getChecksums().size());

				double deducted = scheme.getRuntime_max_deduct();
				scheme.deduct_point(deducted);
				item_oracle.addProperty("deductedPoint", (float)deducted);

				score.add("oracle", item_oracle);
			}

			scheme.deduct_point(scheme.getCompiled_deduct_point());
		}


		try {
			new OOPChecker(scheme, srcList, "", workpath, libPath).run(score, scheme.isBTool());
			new ImplementationChecker(scheme, srcList, "", workpath, libPath).run(score, scheme.isBTool());
		} catch (Exception e) {
			e.printStackTrace();
		}


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date dueDate = format.parse(scheme.getDueDate());
			Date nowDate = new Date();

			int compare = nowDate.compareTo(dueDate);
			double calDate = (double)Math.abs(nowDate.getTime() - dueDate.getTime());

			double calDateDays = Math.ceil(calDate / (double)(24*60*60*1000));

			JsonObject item_delay = new JsonObject();

			if (compare > 0) {
				item_delay.addProperty("violation", true);
				item_delay.addProperty("deductedPoint", calDateDays);
				score.add("delay", item_delay);

				scheme.deduct_point(calDateDays);
			} else {
				item_delay.addProperty("violation", false);
				item_delay.addProperty("deductedPoint", 0);

				score.add("delay", item_delay);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (scheme.getResult_point() > 0.0D) {
			score.addProperty("result", (float)scheme.getResult_point());
		} else {
			score.addProperty("result", 0);
		}

		Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
		String sheet = gson.toJson(score);

		return sheet;
	}

	private String findFilePath(String dpath, String findPath, String fileName) {
		ArrayList<String> command = new ArrayList();

		command.add("bash");
		command.add("-c");
		command.add("find " + findPath + " -name " + fileName);

		ProcessBuilder builder = null;
		BufferedReader stdout = null;
		Process process = null;
		String path = null;

		try {
			builder = new ProcessBuilder(command);
			builder.directory(new File(dpath));

			process = builder.start();

			stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

			while((path = stdout.readLine()) != null) {
				if (path.contains("build.gradle")) {
					return path;
				}

				if (path.contains("jar")) {
					return path;
				}
			}

			process.waitFor();
			process.destroy();

		} catch (Exception e) {
			System.out.println("File Error: No java files in the path: " + dpath);
			e.printStackTrace();
		}

		return path;
	}
}
