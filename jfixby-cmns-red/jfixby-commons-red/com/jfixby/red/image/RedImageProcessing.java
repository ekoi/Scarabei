package com.jfixby.red.image;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ArrayColorMapSpecs;
import com.jfixby.cmns.api.image.ArrayGrayMap;
import com.jfixby.cmns.api.image.ArrayGrayMapSpecs;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ColorMapSpecs;
import com.jfixby.cmns.api.image.ColoredλImage;
import com.jfixby.cmns.api.image.ColoredλImageCache;
import com.jfixby.cmns.api.image.GrayIndexedλImage;
import com.jfixby.cmns.api.image.GrayMap;
import com.jfixby.cmns.api.image.GrayMapSpecs;
import com.jfixby.cmns.api.image.GrayλImage;
import com.jfixby.cmns.api.image.ImageProcessingComponent;
import com.jfixby.cmns.api.image.IndexedColorMapSpecs;

public class RedImageProcessing implements ImageProcessingComponent {

    private static final GrayλImage ONE = new GrayλImage() {
	@Override
	public float valueAt(float x, float y) {
	    return 1;
	}
    };
    private static final GrayλImage ZERO = new GrayλImage() {
	@Override
	public float valueAt(float x, float y) {
	    return 0;
	}
    };

    @Override
    public ArrayColorMap newArrayColorMap(ArrayColorMapSpecs color_function_specs) {
	return new RedArrayColorMap(color_function_specs);
    }

    @Override
    public ArrayColorMapSpecs newArrayColorMapSpecs() {
	return new RedArrayColorMapSpecs();
    }

    @Override
    public ColorMapSpecs newColorMapSpecs() {
	return new RedColorMapSpecs();
    }

    @Override
    public ColoredλImageCache newImageCache(int width, int height) {
	return new RedColoredλImageCache(width, height);
    }

    @Override
    public ColorMap newColorMap(ColorMapSpecs lambda_specs) {
	return new RedColorMap(lambda_specs);
    }

    @Override
    public ColorMap newColorMap(ColoredλImage lambda, int width, int height) {
	return new RedColorMap(lambda, width, height);
    }

    @Override
    public GrayλImage scale(final GrayλImage base, final float scaleX, final float scaleY) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		return base.valueAt(x / scaleX, y / scaleY);
	    }
	};
    }

    @Override
    public GrayλImage minus(final GrayλImage base, final GrayλImage diff) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		return base.valueAt(x, y) - diff.valueAt(x, y);
	    }
	};
    }

    @Override
    public GrayλImage multiply(final GrayλImage image, final float mult) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		return image.valueAt(x, y) * mult;
	    }
	};
    }

    @Override
    public GrayMap newGrayMap(GrayλImage lambda, int width, int height) {
	return new RedGrayMap(lambda, width, height);
    }

    @Override
    public GrayMapSpecs newGrayMapSpecs() {
	return new RedGrayMapSpecs();
    }

    @Override
    public GrayλImage roundArguments(final GrayλImage image) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		// return image.valueAt(FloatMath.round(x), FloatMath.round(y));
		// return image.valueAt(FloatMath.floorDown(x),
		// FloatMath.floorDown(y));
		return image.valueAt(roundArgument(x), roundArgument(y));
		// return image.valueAt(x, y);
	    }

	};
    }

    @Override
    public final int roundArgument(final float x) {
	return (int) x;
	// return (int) FloatMath.component().floorUp(x);
	// return (int) FloatMath.component().round(x + 0.5f);
    }

    @Override
    public ArrayGrayMapSpecs newArrayGrayMapSpecs() {
	return new RedArrayGrayMapSpecs();
    }

    @Override
    public ArrayGrayMap newArrayGrayMap(ArrayGrayMapSpecs specs) {
	return new RedArrayGrayMap(specs);
    }

    @Override
    public GrayλImage plus(final GrayλImage base, final GrayλImage add) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		return base.valueAt(x, y) + add.valueAt(x, y);
	    }
	};
    }

    @Override
    public GrayλImage ONE() {
	return ONE;
    }

    @Override
    public GrayλImage ZERO() {
	return ZERO;
    }

    @Override
    public ColoredλImage scale(final ColoredλImage base, final float scaleX, final float scaleY) {
	return new ColoredλImage() {
	    @Override
	    public Color valueAt(float x, float y) {
		return base.valueAt(x / scaleX, y / scaleY);
	    }
	};
    }

    @Override
    public IndexedColorMapSpecs newIndexedColorMapSpecs() {
	return new RedIndexedColorMapSpecs();
    }

    @Override
    public GrayIndexedλImage index(final GrayλImage base, final GraySet palette) {
	return new GrayIndexedλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		return palette.findClosest(base.valueAt(x, y));
	    }

	    @Override
	    public GraySet getPalette() {
		return palette;
	    }
	};
    }

}
