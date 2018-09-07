package me.modmuss50.crowdin;

import me.modmuss50.crowdin.tasks.ConvertTranslationTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;

public class CrowdinGradlePlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		CrowdinGradleExtension extension = project.getExtensions().create("crowdin", CrowdinGradleExtension.class, project);

		TaskProvider<ConvertTranslationTask> convertCrowdin = project.getTasks().register("convertCrowdin", ConvertTranslationTask.class);

		convertCrowdin.configure(task -> {
			task.setExportType(extension.crowdinFormat);
			task.setInput(extension.langDir);
			task.setDest(new File(project.getBuildDir(), "translations"));
		});
	}
}
