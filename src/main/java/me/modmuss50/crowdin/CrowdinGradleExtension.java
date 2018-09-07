package me.modmuss50.crowdin;

import org.gradle.api.Project;

import javax.inject.Inject;
import java.io.File;

public class CrowdinGradleExtension {

	//The project name on crowdin
	public String projectID;
	//The private key for the crowdin api
	public String projectKey;
	//The format that is being used on crowdin
	public String crowdinFormat;
	//The directory that the lang files are stored in, usually the resources
	public File langDir;

	@Inject
	public CrowdinGradleExtension(Project project) {

	}
}
