package codeInGameTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CodeInGameTool {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String pathInput = args[0]; // "src/main/resources/Test.txt"
		String pathOutput = args[1]; // "src/main/resources/Result.txt"
		System.out.println("pathInput : " + pathInput);
		System.out.println("pathOutput : " + pathOutput);

		File file = new File(pathInput);
		System.out.println("absolutePathInput :" + file.getAbsolutePath());
		if (file.exists()) {
			try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
				Set<String> imports = new HashSet<>();
				Set<String> packages = new HashSet<>();
				StringBuffer stringBufferImports = new StringBuffer();
				StringBuffer stringBufferCodes = new StringBuffer();
				buffer.lines().forEach(line -> {
					if (line.startsWith("package")) {
						packages.add(line);
					} else if (line.startsWith("import")) {
						imports.add(line);
					} else {
						String newLine = line;
						if (newLine.startsWith("public ")) {
							newLine = line.substring("public ".length());
						} else if (newLine.startsWith("protected ")) {
							newLine = line.substring("protected ".length());
						} else if (line.startsWith("private ")) {
							newLine = line.substring("private ".length());
						}
						stringBufferCodes.append(newLine);
						stringBufferCodes.append("\n");
					}
				});
				Collection<String> importsClone = new HashSet<>(imports);
				packages.stream().forEach(p -> {
					String importPattern = packageToImportPatternWithoutSemicolon(p);
					importsClone.stream().forEach(i -> {
						if (i.startsWith(importPattern)) {
							imports.remove(i);
						}
					});
				});
				File resultFile = new File(pathOutput);
				imports.stream().forEach(i -> {
					stringBufferImports.append(i);
					stringBufferImports.append("\n");
				});
				if (stringBufferImports.length() > 0) {
					stringBufferImports.append("\n");
				}
				
				if (resultFile.exists()) {
					resultFile.delete();
				}

				try {
					resultFile.createNewFile();
					try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
						bufferedWriter.write(stringBufferImports.toString());
						bufferedWriter.write(stringBufferCodes.toString());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			throw new FileNotFoundException(pathInput + " is not found");
		}
	}

	static String packageToImportPatternWithoutSemicolon(String p) {
		String s = "import " + p.substring("package ".length());
		return s.substring(0, s.length() - 1);
	}
}
