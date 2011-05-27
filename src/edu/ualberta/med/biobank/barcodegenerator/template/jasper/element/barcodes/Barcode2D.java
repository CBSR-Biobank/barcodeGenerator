package edu.ualberta.med.biobank.barcodegenerator.template.jasper.element.barcodes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.eclipse.swt.graphics.Rectangle;

import edu.ualberta.med.biobank.barcodegenerator.template.jasper.element.Element;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.exceptions.BarcodeCreationException;
import edu.ualberta.med.biobank.barcodegenerator.template.jasper.exceptions.ElementCreationException;

public class Barcode2D extends Element {

	public Barcode2D(Rectangle rect, String message)
			throws ElementCreationException {

		if (message == null || message.length() == 0
				|| message.replaceAll("[^a-zA-Z0-9]", "").length() != 12)
			throw new ElementCreationException(
					"only a 12 character alphanumeric message is allowed for the 2D barcode element.");

		if (rect == null)
			throw new ElementCreationException(
					"null dimensions specified to 2D barcode element.");

		this.rect = rect;
		this.type = Element.TYPE.DataMatrix;
		this.message = message;

	}

	public void render(Graphics2D g, int scale) throws BarcodeCreationException {
		BufferedImage barcode2DImg;
		try {
			barcode2DImg = BarcodeGenerator.generate2DBarcode(message, 300);
		} catch (IOException e) {
			throw new BarcodeCreationException(
					"Failed to create image buffer for barcode");
		}
		g.drawImage(barcode2DImg, mmToPixel(rect.x,scale), mmToPixel(rect.y,scale), mmToPixel(rect.width,scale),
				mmToPixel(rect.height,scale), null);

	}
}
