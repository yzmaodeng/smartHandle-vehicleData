package com.xinghe.xbx.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class FileHelper {
	private static Logger log = Logger.getLogger(FileHelper.class.getName());
	
	/**
	 * 字符串写入文件
	 *
	 * @param fileName
	 * @param src
	 */
	public static boolean writeStringToFile(String fileName, String content,String encoding) {
		if (content == null) {
			//content = "";
			return false;
		}
		boolean isOk = true;
		OutputStreamWriter out = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName, false);
			out = new OutputStreamWriter(fos,encoding);
			out.write(content);
			out.close();
			fos.close();
			log.info("writeStringToFile " + fileName + " success "); 
		} catch (IOException e) {
			isOk = false;
			log.error("writeStringToFile " + fileName + " occur IOException"); 
			log.error(e.getMessage());
			//e.printStackTrace();
		} catch (Exception e) {
			isOk = false;
			log.error("writeStringToFile " + fileName + " occur Exception"); 
			log.error(e.getMessage()); 
			//e.printStackTrace();
		} finally {
			out = null;
			fos = null;
		}
		return isOk;
	}
	
	/**
	 * 创建目录
	 *
	 * @param path 文件地址
	 */
	@SuppressWarnings("finally")
	public static boolean makeDirs(String path) {
		boolean isOk = true;
		File file = new File(path);
		try {
			file.getParentFile().mkdirs(); 
			//file.mkdirs();
			log.info(file.getParent() + " folder create success ");
		} catch (Exception e) {
			isOk = false;
			log.error(file.getParent() + " folder create fail ");
		} finally {
			file = null;
			return isOk;
		}
	}
	/** 
	 * 按行读取文件
	 */ 
	@SuppressWarnings("finally")
	public static List<String> readFileByLines(String fileName){ 
		ArrayList<String> list = new ArrayList<String>();
		File file = new File(fileName);  
		BufferedReader reader = null;  
		int line = 0;  
		try {  
			reader = new BufferedReader(new FileReader(file));  
			String tempString = null;  

			while ((tempString = reader.readLine()) != null){  
				//System.out.println("line " + line + ": " + tempString);
				list.add(tempString);
				line++; 
			}
			reader.close();  
		} catch (Exception e) {
			log.error("readFileByLines "+ fileName +" occur Exception");
			log.error(e.getMessage()); 
		} finally {  
			if (reader != null){
				try {  
					reader.close();  
				} catch (Exception e1) {  
					log.error("readFileByLines "+ fileName +" occur Exception");
					log.error(e1.getMessage()); 
				}  
			} 
			log.info("read data from " + fileName + "  total :" + line); 
			return list;
		}  
	}
	
	/**
	 * new UnicodeReader 为了去除windows 下 保存 utf-8 编码时的 字符前的 不可见字符 Bom
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("finally")
//	public static List<String> readFileByLinesStream(String fileName) throws FileNotFoundException {  
//
//		ArrayList<String> list = new ArrayList<String>();
//		File file = new File(fileName);
//		FileInputStream in = new FileInputStream(file);
//		BufferedReader reader = null;  
//		int line = 0;  
//		try {
//			// 指定读取文件时以UTF-8的格式读取  
//			//windows 下文本utf-8编码时，保存文件会多一个不可见字符（bom）读入 
//			reader = new BufferedReader(new UnicodeReader(in,"UTF-8"));
////			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
//			
////			reader = new BufferedReader(new FileReader(file));  
//			String tempString = null;  
//
//			while ((tempString = reader.readLine()) != null){  
//				//System.out.println("line " + line + ": " + tempString);
//				list.add(tempString);
//				line++; 
//			}
//			reader.close();  
//		} catch (Exception e) {
//			log.error("readFileByLines "+ fileName +" occur Exception");
//			log.error(e.getMessage()); 
//		} finally {  
//			if (reader != null){  
//				try {  
//					reader.close();  
//				} catch (Exception e1) {  
//					log.error("readFileByLines "+ fileName +" occur Exception");
//					log.error(e1.getMessage()); 
//				}  
//			} 
//			log.info("read data from " + fileName + "  total :" + line); 
//			return list;
//		}     
//	 }
//	BufferedReader br = new BufferedReader(new UnicodeReader(in,"UTF-8"));  
//    String line = br.readLine();  
//        while(line!=null){  
//      sb.append(line);  
//      line = br.readLine();  
//        } 
	/** 
	 * 文件中追加内容 
	 */ 
	public static void appendMethod(String fileName, String content,boolean append){ 
		FileWriter writer = null ;
		BufferedWriter bw = null ;
		try {
			writer = new FileWriter(fileName, append);
			bw = new BufferedWriter(writer);
			bw.write(content);    
			bw.newLine();    
			bw.flush();    
//			bw.close();    
//			writer.close();
		} catch (IOException e) {
			log.error("appendMethod "+ fileName +" occur Exception");
			log.error(e.getMessage()); 
		}finally {
			try { 
				if(bw != null){
					bw.close();
				}
				if(writer != null){
					writer.close();
				}
			}catch (Exception e) {
				log.error("appendMethod FileWriter,BufferedWriter "+ fileName +" occur Exception");
				log.error(e.getMessage()); 
			}
		}
	}
	
    /**
     * 符合 以fitler开头的文件列表
     * @param directory
     * @param fitler  file name 
     * @return
     */
	public static List<File> listfile(File directory,String fitler){
		ArrayList<File>  retu = new ArrayList<File>();
		if(directory.isFile()){
			if(directory.getName().startsWith(fitler)){
				retu.add(directory);
			}
		}else{
			File[] list = directory.listFiles();
			if (list != null){
				for (File file : list){
					retu.addAll(listfile(file,fitler));
				}
			}
		}
		return retu;
	}
	
    /**
     * 符合 以fitler结尾的文件列表
     * @param directory
     * @param fitler  file name 
     * @return
     */
	public static List<String> listfile(String filepath,String fitler){
		ArrayList<String>  retu = new ArrayList<String>();
		File file_f = new File(filepath);
		if(file_f.isFile()){
			if(file_f.getName().endsWith(fitler)){
				retu.add(file_f.getAbsolutePath());
			}
		}else{
			File[] list = file_f.listFiles();
			if (list != null){
				for (File file : list){
					retu.addAll(listfile(file.getAbsolutePath(),fitler));
				}
			}
		}
		return retu;
	}
	
	/**
	 * 文件复制
	 *
	 * @param input
	 * @param output
	 * @return 
	 * @throws Exception
	 */
	public static boolean copy(String input, String output) throws Exception {
		int BUFSIZE = 2097152;
		FileInputStream fis =null;
		FileOutputStream fos =null;
		try {
			fis = new FileInputStream(input);
			fos = new FileOutputStream(output);
			int s;
			byte[] buf = new byte[BUFSIZE];
			while ((s = fis.read(buf)) > -1) {
				fos.write(buf, 0, s);
			}
		} catch (Exception ex) {
			throw new Exception("Can not copy file:" + input + " to " + output
					+ ex.getMessage());
		} finally {
			fis.close();
			fos.close();

		}
		return true;
	}
	
	@SuppressWarnings("finally")
	public static boolean copyforChannel(String input, String output){
		boolean retu = true;
        int length=2097152;
		FileInputStream in =null;
		FileOutputStream out =null;
		FileChannel inC = null;
		FileChannel outC = null;
        try{
        	in=new FileInputStream(input);
            out=new FileOutputStream(output);
            inC=in.getChannel();
            outC=out.getChannel();
            ByteBuffer b=null;
            while(true){
                if(inC.position()==inC.size()){
                    inC.close();
                    outC.close();
                    break;
                }
                if((inC.size()-inC.position())<length){
                    length=(int)(inC.size()-inC.position());
                }else
                    length=2097152;
                b=ByteBuffer.allocateDirect(length);
                inC.read(b);
                b.flip();
                outC.write(b);
                outC.force(false);
            }
        }catch (Exception ex) {
        	log.error("Can not copy file:" + input + " to " + output
					+ ex.getMessage());
        	retu = false;
        }finally {
        	try{
        		inC.close();
            	outC.close();
            	in.close();
            	out.close();
        	}catch (Exception ex) {
        		log.error("Can not copy file:" + input + " to " + output
    					+ ex.getMessage());
        		retu = false;
        	}finally {
        		inC = null;
            	outC = null;
            	in = null;
            	out = null;
        	}
        	return retu;
		}
        
    }
//	public static boolean renameFile(String input, String output) {
//        boolean isOk = false;
//        File file_i = new File(input);
//        File file_o = new File(input);
//        file_f.renameTo(output);
//        try {
//            isOk = file.delete();
//        } catch (Exception e) {
//            isOk = false;
//        }
//        return isOk;
//	}
	
	/**
	 * 删除文件
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
        boolean isOk = false;
        try {
            isOk = file.delete();
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
	}
	
	/**
	 * 判断文件是否存在
	 * @param file
	 * @return
	 */
	public static boolean existFile(File file) {
        boolean isOk = false;
        try {
            if(file.exists()){
            	isOk = true;
            }
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
	}
	
	/**
	 * 判断文件是否存在
	 *
	 * @param fileName
	 * @return 
	 */
	public static boolean existFile(String fileName) {
		return existFile(new File(fileName));
	}
	
	/**
	 * 删除文件
	 *
	 * @param fileName
	 * @return --false
	 */
	public static boolean deleteFile(String fileName) {
		return deleteFile(new File(fileName));
	}
	
	
	/**
	 * 判断目录是否存在
	 *
	 * @param fileName 文件地址 全路径
	 * @return 
	 */
	public static boolean existFolder(String fileName) {
		return existFile(new File(fileName));
	}
	
	
	/**
	 * 判断文件是否存在
	 * @param file
	 * @return
	 */
	public static boolean existFolder(File file) {
        boolean isOk = false;
        try {
            if(file.getParentFile().exists()){
            	isOk = true;
            }
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
	}
	
	/**
	 * 创建 文件 所在的 目录
	 * @param file 文件地址 全路径
	 * @return
	 */
	public static boolean mkDirs(File file) {
        boolean isOk = false;
        try {
            if(file.getParentFile().exists()){
            	isOk = true;
            }else{
            	isOk = file.getParentFile().mkdirs();
            }
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
	}
	
	public static boolean mkDirs(String fileName) {
        return mkDirs(new File(fileName));
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
