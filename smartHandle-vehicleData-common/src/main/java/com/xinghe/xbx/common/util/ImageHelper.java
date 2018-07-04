package com.xinghe.xbx.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

@SuppressWarnings("unused")
public class ImageHelper {

    private static Logger logger = Logger.getLogger(ImageHelper.class);

	private static ImageHelper instance = null;

	public static ImageHelper getInstance(){
		if (instance == null) {
			synchronized (ImageHelper.class) {
				if (instance == null) {
					instance = new ImageHelper();
				}
			}
		}
		return instance;
	}
	
	public HashMap<String,Integer> getWeightHeight(String srcPath){
		Integer height = 0;
		Integer width = 0;
		HashMap<String,Integer> map = null;
		try {
			Builder<File> builder = Thumbnails.of(srcPath);
			BufferedImage bufferedImage = builder.scale(1.0f).asBufferedImage();
			height = bufferedImage.getHeight();
			width = bufferedImage.getWidth();
			logger.info("ImageHelper-->getWeightHeight suc-->srcPath:" +srcPath + "|height:"+height + "|width:"+width);
		} catch (Exception e) {
			logger.error("Exception->ImageHelper-->getWeightHeight fail-->srcPath:" +srcPath + "|height:"+height + "|width:"+width);
			e.printStackTrace();
		}
		
		if(height != 0 && width != 0){
			if(map == null){
				map = new HashMap<String,Integer>();
			}
			map.put("h",height);
			map.put("w",width);
		}
		return map;
	}
	
	
	/**
	 * 指定大小进行缩放
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean sizeByAspectRatio(String srcPath,String descPath,int width,int height) {
		//size(宽度, 高度)
		boolean b = true;
        /*  
         * 若图片横比200小，高比300小，不变  
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变  
         * 若图片横比200大，高比300小，横缩小到200，图片比例不变  
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300  
         */ 
		try {
			Thumbnails.of(srcPath)
			        .size(width, height)
			        .toFile(descPath);
			
			logger.info("ImageHelper-->sizeByAspectRatio suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height );
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->sizeByAspectRatio fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height );
		}
		return b;
	}
	
	/**
	 * 不按照比例，指定大小进行缩放
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @return
	 */
	private boolean sizeNoByAspectRatio(String srcPath,String descPath,int width,int height){
		//keepAspectRatio(false) 默认是按照比例缩放的
		boolean b = true;
		try {
			Thumbnails.of(srcPath) 
			        .size(width, height) 
			        .keepAspectRatio(false) 
			        .toFile(descPath);
			logger.info("ImageHelper-->sizeNoByAspectRatio suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height );
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->sizeNoByAspectRatio fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height );
		}
		return b;
	}
	
	/**
	 * 按照比例进行缩放
	 * @param srcPath
	 * @param descPath
	 * @param scale
	 * @return
	 */
	private boolean scale(String srcPath,String descPath,Double scale) {
		//scale(比例) .scale(0.25f)
		boolean b = true;
		try {
			Thumbnails.of(srcPath) 
			        .scale(scale)
			        .toFile(descPath);
			
			logger.info("ImageHelper-->scale suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|scale:"+scale);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->scale fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|scale:"+scale);
		}
		return b;
	}
	
	/**
	 * 旋转
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @param angle
	 * @return
	 */
	private boolean rotate(String srcPath,String descPath,int width,int height,int angle) {
		//rotate(角度),正数：顺时针 负数：逆时针
		boolean b = true;
		
		try {
			Thumbnails.of(srcPath) 
					.size(width, height)
			        .rotate(angle) 
			        .toFile(descPath);
			logger.info("ImageHelper-->rotate suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height+ "|angle:"+angle );
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->rotate fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height + "|angle:"+angle );
		} 
		
		return b;
	}
	
	/**
	 * 添加水印
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @param positions
	 * @param watermarkPath
	 * @param watermarkQuality
	 * @param outputQuality
	 * @return
	 */
	private boolean watermark(String srcPath,String descPath,int width,int height,
			Positions positions,String watermarkPath,float watermarkQuality ,float outputQuality){
		//watermark(位置，水印图，透明度)  Positions.BOTTOM_RIGHT
		
		boolean b = true;
		
		try {
			Thumbnails.of(srcPath) 
					.size(width, height)
			        .watermark(positions, ImageIO.read(new File(watermarkPath)), watermarkQuality) 
			        .outputQuality(outputQuality)
			        .toFile(descPath);
			logger.info("ImageHelper-->watermark suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height
					+ "|positions:"+positions + "|watermarkPath:"+watermarkPath+ "|watermarkQuality:"+watermarkQuality+ "|outputQuality:"+outputQuality);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->watermark fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height
					+ "|positions:"+positions + "|watermarkPath:"+watermarkPath+ "|watermarkQuality:"+watermarkQuality+ "|outputQuality:"+outputQuality);
		}
		
		
		return b;
		
//		Thumbnails.of("images/a380_1280x1024.jpg") 
//				.size(1280, 1024)
//		        .watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f) 
//		        .outputQuality(0.8f) 
//		        .toFile("c:/a380_watermark_center.jpg");
	}
	
	/**
	 * 按区域裁剪
	 * @param srcPath
	 * @param descPath
	 * @param descWidth
	 * @param descHeight
	 * @param regionWidth
	 * @param regionHeight
	 * @param positions
	 * @return
	 */
	private boolean sourceRegionByArea(String srcPath,String descPath,int descWidth,int descHeight,
			int regionWidth ,int regionHeight,Positions positions) {
		//sourceRegion(positions,width,height) Positions.CENTER
		
		boolean b = true;
		
		//图片中心regionWidth*regionHeight的区域
		try {
			Thumbnails.of(srcPath)
					.sourceRegion(positions, regionWidth,regionHeight)
					.size(descWidth, descHeight)
			        .keepAspectRatio(false) 
			        .toFile(descPath);
			logger.info("ImageHelper-->sourceRegionByArea suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|descWidth:"+descWidth+ "|descHeight:"+descHeight
					+ "|regionWidth:"+regionWidth + "|regionHeight:"+regionHeight+ "|positions:"+positions);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->sourceRegionByArea fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|descWidth:"+descWidth+ "|descHeight:"+descHeight
					+ "|regionWidth:"+regionWidth + "|regionHeight:"+regionHeight+ "|positions:"+positions);
		}
		
		return b;
		
		//图片右下400*400的区域
//		Thumbnails.of("images/a380_1280x1024.jpg")
//				.sourceRegion(Positions.BOTTOM_RIGHT, 400,400)
//				.size(200, 200)
//                .keepAspectRatio(false) 
//		        .toFile("c:/a380_region_bootom_right.jpg");
	}
	
	/**
	 * 按坐标裁剪
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @param regionX
	 * @param regionY
	 * @param regionWidth
	 * @param regionHeight
	 * @param positions
	 * @return
	 */
	private boolean sourceRegionByCoordinate(String srcPath,String descPath,int width,int height,
			int regionX ,int regionY,int regionWidth ,int regionHeight,Positions positions){
		//sourceRegion(positions,width,height) Positions.CENTER
		
		boolean b = true;
		
		//指定坐标
		try {
			Thumbnails.of(srcPath)
					.sourceRegion(regionX, regionY, regionWidth, regionHeight)
					.size(width, height)
			        .keepAspectRatio(false) 
			        .toFile(descPath);
			logger.info("ImageHelper-->sourceRegionByCoordinate suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height
					+ "|regionX:"+regionX + "|regionY:"+regionY+ "|regionWidth:"+regionWidth+ "|regionHeight:"+regionHeight+ "|positions:"+positions);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->sourceRegionByCoordinate suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height
					+ "|regionX:"+regionX + "|regionY:"+regionY+ "|regionWidth:"+regionWidth+ "|regionHeight:"+regionHeight+ "|positions:"+positions);
		}
		
		return b;
	}

	/**
	 * 转化图像格式
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @param suffix
	 * @return
	 */
	private boolean changeFormat(String srcPath,String descPath,int width,int height,String suffix){
		//outputFormat(图像格式) png
		
		boolean b = true;
		
		try {
			Thumbnails.of(srcPath) 
					.size(width, height)
			        .outputFormat(suffix) 
			        .toFile(descPath);
			logger.info("ImageHelper-->changeFormat suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height+ "|suffix:"+suffix);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->changeFormat fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height+ "|suffix:"+suffix);
		} 

		
		return b;
		
	}

	/**
	 * 输出到OutputStream
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @return
	 */
	private boolean outStream(String srcPath,String descPath,int width,int height){
		//toOutputStream(流对象)
		boolean b = true;
		
		
		try {
			OutputStream os = new FileOutputStream(descPath);
			Thumbnails.of(srcPath) 
					.size(width, height)
			        .toOutputStream(os);
			
			logger.info("ImageHelper-->outStream suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->outStream fail-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height);
		}
		
		
		return b;
	}
	
	/**
	 * 输出到BufferedImage
	 * @param srcPath
	 * @param descPath
	 * @param width
	 * @param height
	 * @param suffix
	 * @return
	 */
	private boolean outBufferedImage(String srcPath,String descPath,int width,int height,String suffix){
		//asBufferedImage() 返回BufferedImage
		
		boolean b = true;
		
		BufferedImage thumbnail;
		try {
			thumbnail = Thumbnails.of(srcPath)
					.size(width, height)
					.asBufferedImage();
			
			ImageIO.write(thumbnail, suffix, new File(descPath));
			logger.info("ImageHelper-->outBufferedImage suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height+ "|suffix:"+suffix);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
			logger.error("Exception->ImageHelper-->outBufferedImage suc-->srcPath:" +srcPath + "|descPath:"+descPath+ "|width:"+width+ "|height:"+height+ "|suffix:"+suffix);
		}
		
		return b;
	}
	
	public static void main(String[] args) {
		ImageHelper e = new ImageHelper();
		
		try {
			//上线注册红包12.14（549_412）.jpg  
			String srcPath =  "C:/Users/xhxh/Desktop/update/静态文件/陪你选车H5最新及轮播图12.16/638681572333539870.jpg";
			String descPath =  "C:/Users/xhxh/Desktop/update/静态文件/陪你选车H5最新及轮播图12.16/638681572333539870_y.jpg";
//			"C:/Users/xhxh/Desktop/update/静态文件/陪你选车H5最新及轮播图12.16/638681572333539870.jpg";
//			String descPath =  "C:/Users/xhxh/Desktop/update/静态文件/1214/12.14_y.jpg";
			
			HashMap<String,Integer> map = e. getWeightHeight( srcPath);
//			552*414
			int width =  549;
			int height =  412;
			boolean b = e.sizeByAspectRatio( srcPath, descPath, width, height);
			logger.info(b);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
}