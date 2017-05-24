
package com.jfixby.scarabei.red.aws.test;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.aws.api.AWS;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.api.s3.S3FileSystem;
import com.jfixby.scarabei.aws.api.s3.S3FileSystemConfig;
import com.jfixby.scarabei.aws.desktop.DesktopAWS;

public class ListAMZFSFiles {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		AWS.installComponent(new DesktopAWS());
		final S3 s3 = AWS.getS3();
		final S3FileSystemConfig specs = s3.newFileSystemConfig();
		specs.setRegionName("eu-central-1");
		specs.setBucketName("com.red-triplane.assets");//
		final S3FileSystem fileSystem = s3.newFileSystem(specs);

// specs.setBucketName("jfix.by");// amzfs

		fileSystem.ROOT().listDirectChildren()//
			.print("root" + " direct");

		fileSystem.ROOT().listAllChildren()//
			.print("root" + " all");

	}

}
