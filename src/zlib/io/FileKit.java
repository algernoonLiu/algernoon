/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import zlib.codec.CodecKit;
import zlib.set.ArrayList;
import zlib.text.CharBuffer;

/**
 * ��˵���������ļ��������ܺ�����
 * 
 * @version 1.0
 * @author hy
 */

public final class FileKit
{

	/* static fields */
	/** ����Ϣ */
	public static final String toString=FileKit.class.getName();
	/** һ�����ֵĳ������� */
	public static final int LEN_LIMIT=80;
	/** ���������� */
	public static final int BUFFER_SIZE=2048;
	/** �㳤���ֽ����� */
	public static final byte[] EMPTY={};

	/* static methods */
	/** �ϳ�ָ����·�����ļ��������غϳɺ���ļ��� */
	public static String synthesizeFile(String path,String file)
	{
		if(file==null||file.length()==0) return path;
		// ����/��
		if('/'==file.charAt(0)) return file;
		// ����:��
		if(file.indexOf(':')>0) return file;
		if(path==null||path.length()==0) return file;
		CharBuffer cb=new CharBuffer(path.length()+file.length());
		cb.append(path);
		int i=cb.top()-1;
		if(path.charAt(i)!='/'&&path.charAt(i)!='#')
			cb.append('/');
		else
			i--;
		// ����./��
		if(file.startsWith("./"))
			return cb.append(file.substring(2)).getString();
		// ����../����../../��
		int j=0,n=0;
		for(;(file.indexOf("../",j))==j;j+=3,n++)
			;
		if(n<=0) return cb.append(file).getString();
		// ��õ�n��ĸ�·��
		n--;
		char c;
		for(;i>=0;i--)
		{
			c=cb.read(i);
			if(c!='/'&&c!='#') continue;
			if(n<=0) break;
			n--;
		}
		if(i<0) return file.substring(j-1);
		cb.setTop(i+1);
		return cb.append(file.substring(j)).getString();
	}
	/** ��һ���ļ��Զ��������ݷ�ʽ���� */
	public static byte[] readFile(File file) throws IOException
	{
		return readFile(file,0,Integer.MAX_VALUE);
	}
	/** ����һ���ļ�ָ��λ�õ����� */
	public static byte[] readFile(File file,long offset,int length)
		throws IOException
	{
		if(offset<0)
			throw new IllegalArgumentException(toString+" readFile, file="
				+file+", invalid offset:"+offset);
		if(length<0)
			throw new IllegalArgumentException(toString+" readFile, file="
				+file+" invalid length:"+length);
		if(!file.exists()) return null;
		if(length==0) return EMPTY;
		RandomAccessFile accessFile=null;
		try
		{
			accessFile=new RandomAccessFile(file,"r");
			// �õ��ļ���С
			long size=accessFile.length();
			if(offset>=size) return EMPTY;
			if(length>size-offset) length=(int)(size-offset);
			byte[] data=new byte[length];
			if(offset>0) accessFile.seek(offset);
			length=accessFile.read(data);
			if(length<data.length)
			{
				byte[] temp=new byte[length];
				System.arraycopy(data,0,temp,0,length);
				data=temp;
			}
			return data;
		}
		finally
		{
			try
			{
				if(accessFile!=null) accessFile.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	/**
	 * �����������ݷ�ʽд�뵽ָ�����ļ����Զ�����Ŀ¼��
	 */
	public static void writeFile(File file,byte[] data) throws IOException
	{
		writeFile(file,data,0,data.length,false);
	}
	/**
	 * �����������ݷ�ʽд�뵽ָ�����ļ���
	 * �Զ�����Ŀ¼������appendΪ�Ƿ�׷�ӷ�ʽ
	 */
	public static void writeFile(File file,byte[] data,boolean append)
		throws IOException
	{
		writeFile(file,data,0,data.length,append);
	}
	/**
	 * �����������ݷ�ʽд�뵽ָ�����ļ���
	 * �Զ�����Ŀ¼������appendΪ�Ƿ�׷�ӷ�ʽ
	 */
	public static void writeFile(File file,byte[] data,int offset,
		int length,boolean append) throws IOException
	{
		long position=0;
		if(append&&file.exists()) position=file.length();
		writeFile(file,position,data,offset,length,true);
	}
	/**
	 * �����������ݷ�ʽд�뵽ָ�����ļ���ָ��λ�ã�
	 * �Զ�����Ŀ¼������end��ʾ�ļ����ȵ��˽�����
	 */
	public static void writeFile(File file,long position,byte[] data,
		int offset,int length,boolean end) throws IOException
	{
		if(offset<0||offset>data.length)
			throw new IllegalArgumentException(toString+" writeFile, file="
				+file+", invalid offset:"+offset);
		if(length<0||offset+length>data.length)
			throw new IllegalArgumentException(toString+" writeFile, file="
				+file+" invalid length:"+length);
		if(!file.exists())
		{
			File parent=file.getParentFile();
			if(parent!=null&&((!parent.exists())||!parent.isDirectory())
				&&!parent.mkdirs())
				throw new IOException(toString
					+" writeFile, mkdirs fail, file="+file);
		}
		else if(!file.isFile())
			throw new IOException(toString+" writeFile, not file, file="
				+file);
		RandomAccessFile accessFile=null;
		try
		{
			accessFile=new RandomAccessFile(file,"rw");
			accessFile.seek(position);
			accessFile.write(data,offset,length);
			if(end) accessFile.setLength(position+length);
		}
		finally
		{
			try
			{
				if(accessFile!=null) accessFile.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	/**
	 * ����ָ��Ŀ¼�����е��ļ���Ŀ¼��
	 * ���ص��ַ������������Ŀ¼�������ļ���Ŀ¼����Ե�ַ������ڸ�Ŀ¼��
	 */
	public static String[] listFileName(File directory)
	{
		return listFileName(directory,true);
	}
	/**
	 * ����ָ��Ŀ¼�����е��ļ���Ŀ¼�� ����dir��ʾ���ص��������Ƿ����Ŀ¼
	 * ���ص��ַ������������Ŀ¼�������ļ���Ŀ¼�����·����Ŀ¼��'/'��β
	 */
	public static String[] listFileName(File directory,boolean dir)
	{
		if(!directory.exists()) return null;
		if(!directory.isDirectory()) return null;
		ArrayList fileList=new ArrayList();
		listFileName(directory,"",fileList,dir);
		String[] strs=new String[fileList.size()];
		fileList.toArray(strs);
		return strs;
	}
	/** ����ָ��Ŀ¼�����е��ļ���Ŀ¼�������ŵ������� */
	private static void listFileName(File directory,String path,
		ArrayList fileList,boolean dir)
	{
		File[] files=directory.listFiles();
		if(files==null) return;
		if(dir)
		{
			String name;
			for(int i=0;i<files.length;i++)
			{
				if(files[i].isDirectory())
				{
					name=path+files[i].getName()+'/';
					fileList.add(name);
					listFileName(files[i],name,fileList,dir);
				}
				else
					fileList.add(path+files[i].getName());
			}
		}
		else
		{
			for(int i=0;i<files.length;i++)
			{
				if(files[i].isDirectory())
					listFileName(files[i],path+files[i].getName()+'/',
						fileList,dir);
				else
					fileList.add(path+files[i].getName());
			}
		}
	}
	/**
	 * ����ָ��Ŀ¼�����е��ļ���Ŀ¼�� ����dir��ʾ���ص��������Ƿ����Ŀ¼
	 */
	public static File[] listFile(File directory,boolean dir)
	{
		if(!directory.exists()) return null;
		if(!directory.isDirectory()) return null;
		ArrayList fileList=new ArrayList();
		listFile(directory,fileList,dir);
		File[] files=new File[fileList.size()];
		fileList.toArray(files);
		return files;
	}
	/** ����ָ��Ŀ¼�����е��ļ���Ŀ¼�������ŵ������� */
	private static void listFile(File directory,ArrayList fileList,
		boolean dir)
	{
		File[] files=directory.listFiles();
		if(files==null) return;
		if(dir)
		{
			for(int i=0;i<files.length;i++)
			{
				if(files[i].isDirectory())
				{
					fileList.add(files[i]);
					listFile(files[i],fileList,dir);
				}
				else
					fileList.add(files[i]);
			}
		}
		else
		{
			for(int i=0;i<files.length;i++)
			{
				if(files[i].isDirectory())
					listFile(files[i],fileList,dir);
				else
					fileList.add(files[i]);
			}
		}
	}
	/** ���ָ���ļ���crc32 */
	public static int getFileCrc(File file) throws IOException
	{
		RandomAccessFile f=null;
		try
		{
			f=new RandomAccessFile(file,"r");
			int crc=getFileCrc(f);
			return crc;
		}
		finally
		{
			try
			{
				if(f!=null) f.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	/** ���ָ���ļ���crc32 */
	public static int getFileCrc(RandomAccessFile f) throws IOException
	{
		byte[] data=new byte[BUFFER_SIZE];
		int crc=0xffffffff,r;
		f.seek(0);
		while((r=f.read(data))>0)
			crc=CodecKit.getCrc32(data,0,r,crc);
		return ~crc;
	}
	/** ���ָ���ļ���crc32 */
	public static boolean checkFileCrc(File file) throws IOException
	{
		RandomAccessFile f=null;
		try
		{
			f=new RandomAccessFile(file,"r");
			boolean b=checkFileCrc(f);
			return b;
		}
		finally
		{
			try
			{
				if(f!=null) f.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	/** ���ָ���ļ���crc32 */
	public static boolean checkFileCrc(RandomAccessFile f)
		throws IOException
	{
		return CodecKit.CRC32==~getFileCrc(f);
	}
	/** ���ָ���ļ���crc32������ԭ�ļ���crc32 */
	public static int addFileCrc(File file) throws IOException
	{
		RandomAccessFile f=null;
		try
		{
			f=new RandomAccessFile(file,"rw");
			int crc=addFileCrc(file);
			return crc;
		}
		finally
		{
			try
			{
				if(f!=null) f.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	/** ���ָ���ļ���crc32������ԭ�ļ���crc32 */
	public static int addFileCrc(RandomAccessFile f) throws IOException
	{
		int crc=getFileCrc(f);
		f.seek(f.length());
		f.writeByte(crc&0xff);
		f.writeByte((crc>>>8)&0xff);
		f.writeByte((crc>>>16)&0xff);
		f.writeByte((crc>>>24)&0xff);
		return crc;
	}

	/* constructors */
	private FileKit()
	{
	}

}