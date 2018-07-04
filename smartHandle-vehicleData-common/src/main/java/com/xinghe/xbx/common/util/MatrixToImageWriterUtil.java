package com.xinghe.xbx.common.util;  
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;  
       
import javax.imageio.ImageIO;  
import java.io.File;  
import java.io.OutputStream;
import java.util.Hashtable;
import java.io.IOException;  
import java.awt.image.BufferedImage;  
       
/**
 * 
 *工具类:生成二维码
 * @author ccwang
 * @version 1.0
 * @Date 2016-09-06
 */      
public final class MatrixToImageWriterUtil {  
       
       private static final int BLACK = 0xFF000000;  
       private static final int WHITE = 0xFFFFFFFF;
       private static final int WIDTH = 300;  
       private static final int HEIGHT = 300; 
       private static final String FORMAT  = "gif";
       private static final String FILEPATH  = "D:";
       private MatrixToImageWriterUtil() {}  
       
         
       public static BufferedImage toBufferedImage(BitMatrix matrix) {  
         int width = matrix.getWidth();  
         int height = matrix.getHeight();  
         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
         for (int x = 0; x < width; x++) {  
           for (int y = 0; y < height; y++) {  
             image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);  
           }  
         }  
         return image;  
       }  
       
         
       public static void writeToFile(BitMatrix matrix, String format, File file)  
           throws IOException {  
         BufferedImage image = toBufferedImage(matrix);  
         if (!ImageIO.write(image, format, file)) {  
           throw new IOException("Could not write an image of format " + format + " to " + file);  
         }  
       }  
       
         
       public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)  
           throws IOException {  
         BufferedImage image = toBufferedImage(matrix);  
         if (!ImageIO.write(image, format, stream)) {  
           throw new IOException("Could not write an image of format " + format);  
         }  
       }  
       
       public static void generatedQrcode(String text,String fileName) throws WriterException, IOException{
           Hashtable hints = new Hashtable();   
           //内容所使用编码  
           hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
           BitMatrix bitMatrix = new MultiFormatWriter().encode(text,  
                   BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);  
           //生成二维码  
           File outputFile = new File(FILEPATH+File.separator+fileName+"."+FORMAT);  
		   MatrixToImageWriterUtil.writeToFile(bitMatrix, FORMAT, outputFile);
		}
     
}