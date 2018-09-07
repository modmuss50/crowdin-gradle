package me.modmuss50.crowdin.tasks;

import me.modmuss50.crowdin.TranslationUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConvertTranslationTask extends DefaultTask {

	/*
	This is the input dir or file
	 */
	@Input
	private File input;
	public File getInput() { return input; }
	public void setInput(File input) { this.input = input; }

	/*
	This is the output dir
	 */
	@OutputDirectory
	private File dest;
	public File getDest() { return dest; }
	public void setDest(File dest) { this.dest = dest; }

	/**
	 * This is the format that the translations should be exported as, LANG or JSON is valid
	 */
	@Input
	private String exportType;
	public String getExportType() { return exportType; }
	public void setExportType(String exportType) { this.exportType = exportType; }

	@TaskAction
	public void runTask() throws IOException {
		if(!input.exists()){
			throw new FileNotFoundException(input.getAbsolutePath() + " file not found");
		}
		if(!dest.isDirectory()){
			throw new RuntimeException("dest must be a directory");
		}
		if(exportType.isEmpty()){
			throw new RuntimeException("Export type not set");
		}

		List<Pair<File, File>> translationList = new ArrayList<>();
		if(input.isDirectory() && input.listFiles() != null){
			Arrays.stream(input.listFiles())
				.filter(file -> file.exists() && !file.isDirectory())
				.forEach(file -> translationList.add(Pair.of(file, new File(dest, TranslationUtil.toggleFileName(file.getName())))));
		} else {
			translationList.add(Pair.of(input, dest));
		}

		for (Pair<File, File> fileFilePair : translationList) {
			translate(fileFilePair);
		}

	}

	private void translate(Pair<File, File> files) throws IOException {
		File source = files.getLeft();
		File target = files.getRight();
		if(target.exists()){
			target.delete();
		}

		Map<String, String> translations = TranslationUtil.readTranslation(source);
		String output = TranslationUtil.writeTranslation(translations, exportType.equalsIgnoreCase("json") ? TranslationUtil.TranslationFormat.JSON : TranslationUtil.TranslationFormat.LANG);
		FileUtils.writeStringToFile(target, output, StandardCharsets.UTF_8.toString());
	}

}
