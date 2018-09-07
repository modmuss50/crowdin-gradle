package me.modmuss50.crowdin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class TranslationUtil {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public enum TranslationFormat{
		LANG,
		JSON
	}

	public static Map<String, String> readTranslation(File file) throws IOException {
		Map<String, String> map = new HashMap<>();
		if(file.getName().endsWith(".lang")){
			List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8.toString());
			for(String line : lines){
				readLangTranslation(line, map);
			}
		} else if (file.getName().endsWith(".json")){
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			map = GSON.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8.toString()), type);
		} else {
			throw new RuntimeException("Lang format not supported!");
		}
		return map;
	}

	private static void readLangTranslation(String line, Map<String, String> map){
		if(line.startsWith("#") || !line.contains("=")){
			return;
		}
		String[] split = line.split("=");
		if(split.length < 2){
			return;
		}
		map.put(split[0], split[1]);
	}

	public static String writeTranslation(Map<String, String> map, TranslationFormat format){
		String output = "";
		if(format == TranslationFormat.LANG){
			StringJoiner stringJoiner = new StringJoiner("\n");
			map.forEach((key, value) -> stringJoiner.add(key + "=" + value));
			output = stringJoiner.toString();
		} else {
			output = GSON.toJson(map);
		}
		return output;
	}

	//TODO horrible, but it should work
	public static String toggleFileName(String fileName){
		if(fileName.endsWith(".lang")){
			return fileName.replace(".lang", ".json");
		}
		if(fileName.endsWith(".json")){
			return fileName.replace(".json", ".lang");
		}
		return fileName;
	}


}
