
package com.jfixby.scarabei.api.image;

public interface ColorMap extends ColoredλImage {

	int getWidth ();

	int getHeight ();

	ColoredλImage getLambdaImage ();

	GrayMap getAlpha ();

	GrayMap getRed ();

	GrayMap getGreen ();

	GrayMap getBlue ();

}
