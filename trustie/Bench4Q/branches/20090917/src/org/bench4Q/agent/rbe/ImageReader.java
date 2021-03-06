package org.bench4Q.agent.rbe;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.bench4Q.agent.rbe.communication.EBStats;

public class ImageReader {

	protected String imgURLStr;
	protected URL srcURL;
	protected URL imgURL;
	protected byte[] buffer;
	protected BufferedInputStream imgIn = null;
	protected int tot;

	public ImageReader(URL srcURL, String url, byte[] buffer) {
		imgURLStr = url;
		this.buffer = buffer;
		imgURL = null;
		this.srcURL = srcURL;
	}

	// Read as much of the image as we can without blocking.
	// Returns true if there is more image to read.
	// Returns false if the image has been read.
	public boolean readImage(int state) throws InterruptedException {
		int r;

		// Open the image URL if necessary.
		if (imgURL == null) {
			try {

				imgURL = new URL(srcURL, imgURLStr);
			} catch (MalformedURLException mue) {
				// This one means the received HTML is bad.
				EBStats.getEBStats().error(state, "Malformed image URL.", imgURLStr);
				return false;
			} catch (NullPointerException e) {
				EBStats.getEBStats().error(state, "NullPointerException in readImage ", imgURL.toString());
				return true;
			}

			try {
				imgIn = new BufferedInputStream(imgURL.openStream());
			} catch (IOException ioe) {
				EBStats.getEBStats().error(state, "EB# Unable to open image.", imgURL.toString());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException intE) {
					System.out.println("In readImage, caught interrupted exception!");
				} catch (NullPointerException e) {
					return true;
				}
				return true;
			}
			tot = 0;
		}

		try {
			r = imgIn.read(buffer, 0, buffer.length);
		} catch (IOException ioe) {
			EBStats.getEBStats().error(state, "Unable to read complete image.", imgURLStr);
			close(state);
			return true;
		} catch (NullPointerException e) {
			return false;
		}

		if (r == -1) {
			close(state);
			return false;
		} else {
			tot += r;
		}

		return true;
	}

	private void close(int state) {
		try {
			imgIn.close();
		} catch (IOException ioe) {
			EBStats.getEBStats().error(state, "Unable to close image.", imgURLStr);
		}

		imgIn = null;
		imgURL = null;

	}
}
