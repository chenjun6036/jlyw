package com.jlyw.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.eps.EPSCanvasProvider;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * 生成条形码图片
 * 
 * @author Administrator
 * 
 */
public class BarCodeUtil {
	/**
	 * 生成一维码图片（png格式）
	 * @param codeStr：要生成的字符串
	 * @param outFile：导出的文件
	 * @return
	 */
	public static boolean GenerateBarcodeOld(String codeStr, File outFile) throws Exception {
		OutputStream out = null;
		try {
			// Create the barcode bean
			EAN128Bean bean = new EAN128Bean();
			// Code39Bean bean = new Code39Bean();
			// Code128Bean bean = new Code128Bean();
			
			final int dpi = 80;
			final String format = MimeTypes.MIME_PNG;// 图片格式

			// Configure the barcode generator
			bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); // makes the narrow bar width exactly one pixel
			// bean.setWideFactor(3);
			bean.doQuietZone(false);
			bean.setBarHeight(6);	//条形码高度
			bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);	//不显示条形码下的字符

			out = new FileOutputStream(outFile);
			// Set up the canvas provider for monochrome PNG output
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, format,
					dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

			// Generate the barcode
			bean.generateBarcode(canvas, codeStr);
			// Signal end of generation
			canvas.finish();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			if(out != null){
				try{
					out.close();
					out = null;
				}catch(Exception e){}
			}
		}
	}
	
	public static void main(String[]args) throws Exception{
		//BarCodeUtil.GenerateBarcode("2012200000", new File("C:/test2.jpg"));
		Boolean c= true;
		System.out.println(((Object)c).toString());
	}
	
	public static boolean GenerateBarcode(String codeStr, File outFile) throws Exception {
		OutputStream bout = null;
		try {
			final String format = MimeTypes.MIME_JPEG;//.MIME_PNG;// 图片格式
            int orientation = 0;

            Configuration cfg = buildCfg();

            String msg = codeStr;

            BarcodeUtil util = BarcodeUtil.getInstance();
            BarcodeGenerator gen = util.createBarcodeGenerator(cfg);

            bout = new FileOutputStream(outFile);
            
            if (format.equals(MimeTypes.MIME_SVG)) {
                //Create Barcode and render it to SVG
                SVGCanvasProvider svg = new SVGCanvasProvider(false, orientation);
                gen.generateBarcode(svg, msg);
                org.w3c.dom.DocumentFragment frag = svg.getDOMFragment();

                //Serialize SVG barcode
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer trans = factory.newTransformer();
                Source src = new javax.xml.transform.dom.DOMSource(frag);
                Result res = new javax.xml.transform.stream.StreamResult(bout);
                trans.transform(src, res);
            } else if (format.equals(MimeTypes.MIME_EPS)) {
                EPSCanvasProvider eps = new EPSCanvasProvider(bout, orientation);
                gen.generateBarcode(eps, msg);
                eps.finish();
            } else {
                String resText = null;//"100";//request.getParameter(BARCODE_IMAGE_RESOLUTION);
                int resolution = 300; //dpi
                if (resText != null) {
                    resolution = Integer.parseInt(resText);
                }
                if (resolution > 2400) {
                    throw new IllegalArgumentException(
                        "Resolutions above 2400dpi are not allowed");
                }
                if (resolution < 10) {
                    throw new IllegalArgumentException(
                        "Minimum resolution must be 10dpi");
                }
                String gray = "true";//request.getParameter(BARCODE_IMAGE_GRAYSCALE);
                BitmapCanvasProvider bitmap = ("true".equalsIgnoreCase(gray)
                    ? new BitmapCanvasProvider(
                            bout, format, resolution,
                            BufferedImage.TYPE_BYTE_GRAY, true, orientation)
                    : new BitmapCanvasProvider(
                            bout, format, resolution,
                            BufferedImage.TYPE_BYTE_BINARY, false, orientation));
                gen.generateBarcode(bitmap, msg);
                bitmap.finish();
            }
            
            return true;
		}catch (Exception e) {
			throw e;
		} finally {
			if(bout != null){
				try{
					bout.close();
					bout = null;
				}catch(Exception e){}
			}
		}
	}

    /**
     * Build an Avalon Configuration object from the request.
     * @param request the request to use
     * @return the newly built COnfiguration object
     * @todo Change to bean API
     */
    protected static Configuration buildCfg() {
        DefaultConfiguration cfg = new DefaultConfiguration("barcode");
        //Get type
        String type = null;//request.getParameter(BARCODE_TYPE);
        if (type == null) type = "code128";
        DefaultConfiguration child = new DefaultConfiguration(type);
        cfg.addChild(child);
        //Get additional attributes
        DefaultConfiguration attr;
        String height = "10";//request.getParameter(BARCODE_HEIGHT);
        if (height != null) {
            attr = new DefaultConfiguration("height");
            attr.setValue(height);
            child.addChild(attr);
        }
        String moduleWidth = "0.2";//;//request.getParameter(BARCODE_MODULE_WIDTH);
        if (moduleWidth != null) {
            attr = new DefaultConfiguration("module-width");
            attr.setValue(moduleWidth);
            child.addChild(attr);
        }
        String wideFactor = null;//request.getParameter(BARCODE_WIDE_FACTOR);
        if (wideFactor != null) {
            attr = new DefaultConfiguration("wide-factor");
            attr.setValue(wideFactor);
            child.addChild(attr);
        }
        String quietZone = "disable";//request.getParameter(BARCODE_QUIET_ZONE);
        if (quietZone != null) {
            attr = new DefaultConfiguration("quiet-zone");
            if (quietZone.startsWith("disable")) {
                attr.setAttribute("enabled", "false");
            } else {
                attr.setValue(quietZone);
            }
            child.addChild(attr);
        }

        // creating human readable configuration according to the new Barcode Element Mappings
        // where the human-readable has children for font name, font size, placement and
        // custom pattern.
        String humanReadablePosition = "none";//request.getParameter(BARCODE_HUMAN_READABLE_POS);
        String pattern = null;//request.getParameter(BARCODE_HUMAN_READABLE_PATTERN);
        String humanReadableSize = null;//request.getParameter(BARCODE_HUMAN_READABLE_SIZE);
        String humanReadableFont = null;//request.getParameter(BARCODE_HUMAN_READABLE_FONT);

        if (!((humanReadablePosition == null)
                && (pattern == null)
                && (humanReadableSize == null)
                && (humanReadableFont == null))) {
            attr = new DefaultConfiguration("human-readable");

            DefaultConfiguration subAttr;
            if (pattern != null) {
                subAttr = new DefaultConfiguration("pattern");
                subAttr.setValue(pattern);
                attr.addChild(subAttr);
            }
            if (humanReadableSize != null) {
                subAttr = new DefaultConfiguration("font-size");
                subAttr.setValue(humanReadableSize);
                attr.addChild(subAttr);
            }
            if (humanReadableFont != null) {
                subAttr = new DefaultConfiguration("font-name");
                subAttr.setValue(humanReadableFont);
                attr.addChild(subAttr);
            }
            if (humanReadablePosition != null) {
              subAttr = new DefaultConfiguration("placement");
              subAttr.setValue(humanReadablePosition);
              attr.addChild(subAttr);
            }

            child.addChild(attr);
        }

        return cfg;
    }
}
