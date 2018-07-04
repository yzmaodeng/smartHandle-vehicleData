package com.xinghe.xbx.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.xinghe.xbx.common.constant.CommonConstantConfig;

/**
 * 上传、压缩文件处理类
 * @author 007
 */
public class UploadUtil{
	private static final Logger logger = Logger.getLogger(UploadUtil.class);
	
	private static UploadUtil instance = null;

	/** 文件上传根目录 */
	private  String rootPath;
	
	/** 文件名称域名 */
	private  String urlPrefix;
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}


	/**
	 * 上传图片并压缩
	 * 返回文件已写入的字节数（断点续传）、生成文件的访问url
	 * {
	 *  picUrl:"https://www.51chesong.com/1.jpg,
	 *  position: 47000"
	 * }
	 * @param originalFilename 文件名
	 * @param bytes 断点续传文件的bytes数组
	 * @param position 断点续传定位，若无断点，则可以传null 或 0
	 * @param md5 完整文件的md5码
	 * @param path 文件存放中间路径
	 * @param width 图片压缩宽度
	 * @return
	 */
	public Map<String,Object> uploadImg(String originalFilename, byte[] bytes, Long position, String md5, String path, Integer width){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		RandomAccessFile file = null;
		// 缩略图访问url
		String thumbUrl = "";
		// 文件已写入的size（断点续传）
		Long fileSize = position;
		// 服务器文件存放目录
		String destPath = rootPath + File.separator + path;
		// 文件存放目录中间值
		// 临时文件存放绝对路径
//		String tmpSavePath = ConstantBuy.UPLOAD_TMP_PATH + File.separator + path;
	    File fileDir = new File(destPath);
	    //判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
	    // 缩略图文件名
	    String thumbFileName = "";
		try{
			// 获取文件后缀
			int index = originalFilename.lastIndexOf(".");
			// 如.jpg
			String ext = originalFilename.substring(index);
			// 原文件名称
			String orgFileName = originalFilename.substring(0, index);
			// 文件名前缀
			String filePrefix = orgFileName + "_" + System.currentTimeMillis();
			if(position != null && position.longValue() > 0){
				filePrefix = orgFileName;
			}
			// 临时保存原始文件.tmp
			String tmpFileName = filePrefix + ".tmp";
			// 真实文件名
			String fileName = filePrefix + "_l" + ext;
			// 缩略图文件名
			thumbFileName = filePrefix + ext;
			
			String tmpFilePath = destPath + File.separator + tmpFileName;
			
			File newFile = new File(tmpFilePath);
			// 文件第一次上传
			if(position == null || position.longValue() == 0){
				// 若文件存在，则删除文件
				if(newFile.exists()){
					newFile.delete();
				}
			}
		
			file = new RandomAccessFile(tmpFilePath, "rwd");
			if(position != null && position.longValue() > 0){
				file.seek(position);
			}
			
//			断点续传测试
//			byte[] bytesTemp = new byte[10000];
//			for(int i=0;i<10000;i++){
//				bytesTemp[i] = bytes[i];
//			}
//			byte[] bytesTemp = new byte[bytes.length - 10000];
//			for(int i=10000;i<bytes.length;i++){
//				bytesTemp[i-10000] = bytes[i];
//			}
//			file.write(bytesTemp);
			
			file.write(bytes);
			fileSize = file.length();
			// 判断文件是否已上传完毕
			HashCode hashCode = Files.hash(newFile, Hashing.md5());  
			String md5Tmp = hashCode.toString();
			file.close();
			// 图片上传完毕
			if(md5.equals(md5Tmp)){
//					mulFile.transferTo(newFile);
				// 将临时文件.tmp 重命名为 --》 图片原本的后缀（.jpg）
				boolean isSuccess = newFile.renameTo(new File(destPath + File.separator + fileName));
				if(isSuccess){
					// 获取原始图片尺寸
//					HashMap<String,Integer> imgMap = ImageHelper.getInstance().getWeightHeight(destPath + File.separator + fileName);
					
					// 缩略图压缩完成标识
					boolean thumbFlag = ImageHelper.getInstance().sizeByAspectRatio(destPath + File.separator + fileName, 
							destPath + File.separator + thumbFileName, width, Integer.MAX_VALUE);
					
					if(thumbFlag){
						// 缩略图访问地址
						thumbUrl = CommonConstantConfig.URL_SEPARATOR + path + CommonConstantConfig.URL_SEPARATOR + thumbFileName;
						// url格式
						thumbUrl = urlPrefix + thumbUrl.replaceAll("\\\\",CommonConstantConfig.URL_SEPARATOR).replace("//", "/");
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			logger.error("Exception->UploadUtil-->upload-->msg:" + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Exception->UploadUtil-->upload-->msg:" + e.getMessage(), e);
		} finally {
			if(file != null){
				try {
					file.close();
				} catch (IOException e) {
					logger.error("Exception->UploadUtil-->upload-->msg:" + e.getMessage(), e);
				}
			}
		}
		resultMap.put("position", fileSize);
		resultMap.put("picUrl", thumbUrl);
		String filePath = path + File.separator + thumbFileName;
		resultMap.put("filePath", filePath.replaceAll("\\\\",CommonConstantConfig.URL_SEPARATOR).replace("//", "/"));
		
		return resultMap;
	}
	/**
	 * 上传图片到服务器，
	 * 返回的生成文件的访问url
	 * @param fileName
	 * @param fis
	 * @param path
	 * @return
	 */
	public String uploadPic(String fileName,byte[] bytes,String path){
		String url = "";
		//rootPath=/data/img/pdf
		//urlPrefix=http://10.10.1.205/img/pdf
		int start=rootPath.lastIndexOf("/");
		//获取服务器图片文件夹地址。
		String destPath=rootPath.substring(0,start+1)+path;
		File fileDir = new File(destPath);
		 // 判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
	    // 文件服务器路径
    	String filePath = destPath + File.separator + fileName;
    	FileOutputStream out = null;
    	try {
    		//设置 最多500KB图片大小
    		out = new FileOutputStream(filePath);
			out.write(bytes);
			// 访问地址
			url = CommonConstantConfig.URL_SEPARATOR + path + CommonConstantConfig.URL_SEPARATOR + fileName;
			// url格式
			int urlStart=urlPrefix.lastIndexOf("/");
			String urlPath=urlPrefix.substring(0,urlStart);
			url = urlPath + url.replaceAll("\\\\",CommonConstantConfig.URL_SEPARATOR).replace("//", "/");
			logger.info("INFO-->UploadUtil-->uploadPic-->return url="+url);
		}  catch (FileNotFoundException e) {
			logger.error("FileNotFoundException->UploadUtil-->uploadPic-->msg:" + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IOException->UploadUtil-->uploadPic-->msg:" + e.getMessage(), e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("IOException->UploadUtil-->uploadPic-->msg:" + e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
		return url;
	}
	
	/**
	 * 上传文件
	 * 返回生成文件的访问url
	 *  url:"https://www.51chesong.com/1.pdf,
	 * @param filename 文件名
	 * @param bytes 文件的bytes数组
	 * @param path  文件上传子目录
	 * @return
	 */
	public String uploadFile(String fileName, byte[] bytes, String path){
		String url = "";
		String destPath = rootPath + File.separator + path+ File.separator + DateHelper.getYearMonDayString();
		File fileDir = new File(destPath);
	    // 判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
	    // 文件服务器路径
    	String filePath = destPath + File.separator + fileName;
    	FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			out.write(bytes);
			// 访问地址
			url = CommonConstantConfig.URL_SEPARATOR + path + CommonConstantConfig.URL_SEPARATOR + DateHelper.getYearMonDayString() + CommonConstantConfig.URL_SEPARATOR + fileName;
			// url格式
			url = urlPrefix + url.replaceAll("\\\\",CommonConstantConfig.URL_SEPARATOR).replace("//", "/");
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException->UploadUtil-->uploadFile-->msg:" + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IOException->UploadUtil-->uploadFile-->msg:" + e.getMessage(), e);
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("IOException->UploadUtil-->uploadFile-->msg:" + e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
		return url;
	}
	/**
	 * 通过文件名以以及文件子目录path(不同种类的文件path不同，比如报告存放的path是report)
	 * 返回生成文件的访问url
	 *  url:"http://www.xingbaoxin.com/img/pdf/report/201706/20/QJ-1034291069.pdf,
	 * @param filename 文件名[QJ-1034291069.pdf]
	 * @param path  文件上传子目录[report]
	 * @return
	 */
	public  String getFileUrl(String fileName, String path){
		String url = "";
    	url = CommonConstantConfig.URL_SEPARATOR + path + CommonConstantConfig.URL_SEPARATOR + DateHelper.getYearMonDayString() + CommonConstantConfig.URL_SEPARATOR + fileName;
    	url = urlPrefix + url.replaceAll("\\\\",CommonConstantConfig.URL_SEPARATOR).replace("//", "/");
		return url;
	}
	
	/**
	 * 上传文件
	 * 返回上传结果
	 * @param filename 文件名
	 * @param bytes 文件的bytes数组
	 * @param path  文件上传子目录
	 * @return true[成功] false[失败]
	 */
	public  boolean uploadFileByIautos(String fileName, byte[] bytes, String path){
		String destPath = path;
		File fileDir = new File(destPath);
	    // 判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
	    // 文件服务器路径
    	String filePath = destPath + File.separator + fileName;
    	FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			out.write(bytes);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException->UploadUtil-->uploadFile-->msg:" + e.getMessage(), e);
			return false;
		} catch (IOException e) {
			logger.error("IOException->UploadUtil-->uploadFile-->msg:" + e.getMessage(), e);
			return false;
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("IOException->UploadUtil-->uploadFile-->msg:" + e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	/** 
     * 合并文件 [OK]
     * @param dirFile  
     * @param tempFile 
     * @param fileName 
     * @throws IOException 
     */  
    public  void unionFile(String dirFile, String[] files, String fileName) throws IOException {  
		File fileDir = new File(dirFile);
	    // 判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
	    String filePath = dirFile + File.separator + fileName;
    	FileChannel outChannel = null;   
        try {   
            outChannel = new FileOutputStream(filePath).getChannel();   
            for(String f : files){   
                Charset charset=Charset.forName("GBK");   
                CharsetDecoder chdecoder=charset.newDecoder();   
                CharsetEncoder chencoder=charset.newEncoder();   
                FileChannel fc = new FileInputStream(f).getChannel();    
                ByteBuffer bb = ByteBuffer.allocate(1024*8);   
                CharBuffer charBuffer=chdecoder.decode(bb);   
                ByteBuffer nbuBuffer=chencoder.encode(charBuffer);   
                while(fc.read(nbuBuffer) != -1){   
                    bb.flip();     
                    nbuBuffer.flip();   
                    outChannel.write(nbuBuffer);   
                    bb.clear();   
                    nbuBuffer.clear();   
                }   
                fc.close();   
            }   
        } catch (IOException ioe) {   
            ioe.printStackTrace();   
        } finally {   
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}   
        }   
    }  

   //合并文件 [OK]
   public  boolean mergeFile(String[] arrFileNames, String targetFileName){
        int iSumFile = arrFileNames.length;
        File f=new File(targetFileName);
        //以合并后的文件名称和打开方式来创建、初始化FileStream文件流
        FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
        //以FileStream文件流来初始化BinaryWriter书写器，此用以合并分割的文件
        /*循环合并小文件，并生成合并文件 */
		List<byte[]> byteslist = new ArrayList<byte[]>();
        for (int i = 0; i < iSumFile; i++){
            FileInputStream fis= null;
			try {
				System.out.println(arrFileNames[i]);
				fis = new FileInputStream(arrFileNames[i]);
				//以小文件所对应的文件名称和打开模式来初始化FileStream文件流，起读取分割作用
				byte[] tmp= new byte[fis.available()];
				fis.read(tmp);
				byteslist.add(tmp);
				fis.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        int len = 0;
	    //计算一维数组长度
	    for(int i = 0;i < byteslist.size();i++){
	       len += byteslist.get(i).length;
	    }
	    //复制元素
	    byte[] data=new byte[len];
	    int index = 0;
	    for(int i = 0;i < byteslist.size();i++){
	        for(int j = 0;j < byteslist.get(i).length;j++){
	           data[index++] = byteslist.get(i)[j];
	        }
	    }
	    try {
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return true;
    }
   
    //合并文件[OK]
    public  boolean merge(File[] fileNames,String zipSavePath, String TargetFileName) {
		File fileDir = new File(zipSavePath);
	    // 判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
    	boolean flag = true;//表示解压是否成功的返回结果
		// 构建文件输出流
    	File fin = null;
		File fout = new File(zipSavePath+TargetFileName);
		try {
			FileOutputStream out = new FileOutputStream(fout);
			for (int i = 0; i < fileNames.length; i++) {
				// 打开文件输入流
				fin = fileNames[i];
				FileInputStream in = new FileInputStream(fin);
				// 从输入流中读取数据，并写入到文件数出流中
				byte[] data = new byte[in.available()];
				in.read(data);
				out.write(data);
				in.close();
			}	
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			flag= false;
		} catch (IOException e) {
			e.printStackTrace();
			flag= false;
		}
		return flag;
    }
    
    /**
     * 
    * @Title: unGzipFile
    * @Description: TODO
    * @param @param gzfilename
    * @param @param unzipfilename
    * @return boolean    返回类型
     */
    public  boolean  unGzipFile(String gzfilename,String unZipSavePath, String unzipfilename) {
		File fileDir = new File(unZipSavePath);
	    // 判断文件夹是否存在,如果不存在则创建文件夹
	    if(!(fileDir.exists() && fileDir.isDirectory())){
	    	fileDir.mkdirs();
	    }
    	boolean flag = true;//表示解压是否成功的返回结果
        try {  
            //建立gzip压缩文件输入流 
            FileInputStream fin = new FileInputStream(gzfilename);   
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);   
            //建立解压文件输出流  
            FileOutputStream fout = new FileOutputStream(unZipSavePath+unzipfilename);   
            
            int num;
            byte[] buf=new byte[1024];

            while ((num = gzin.read(buf,0,buf.length)) != -1)
            {   
                fout.write(buf,0,num);   
            }
            gzin.close();   
            fout.close();   
            fin.close();   
        } catch (Exception ex){  
            ex.printStackTrace();
            flag = false;
        }  
        return flag;
    }
    /**
     * 
    * @author ccwang
    * @Title: deleteFile
    * @Description: TODO
    * @param @param fileName
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public  boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
    			logger.info("INFO->UploadUtil-->deleteFile-->msg:deleteFile success|param:fileName="+fileName );
                return true;
            } else {
       			logger.error("ERROR->UploadUtil-->deleteFile-->msg:deleteFile error|param:fileName="+fileName );
       		    return false;
            }
        }else{
   			logger.info("INFO->UploadUtil-->deleteFile-->msg: file does not exist|param:fileName="+fileName );
        	return true;
        }
    }
}