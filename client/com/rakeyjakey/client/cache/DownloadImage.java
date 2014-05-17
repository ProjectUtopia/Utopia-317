package com.rakeyjakey.client.cache;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.rakeyjakey.client.sign.SignLink;

public class DownloadImage {

	public static Image getImage(String url, String name) {
		try {
			File f = new File(SignLink.findcachedir() + "Logo/" + name);
			
			if (f.exists())
				return ImageIO.read(f.toURI().toURL());
			
			Image img = ImageIO.read(new URL(url));
			if (img != null) {
				ImageIO.write((RenderedImage) img, "PNG", f);
				return img;
			}
		} catch (MalformedURLException e) {
			System.out.println("Error connecting to image URL: " + url);
		} catch (IOException e) {
			System.out.println("Error reading file: " + name);
		}
		return null;
	}

}
