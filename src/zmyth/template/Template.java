/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.template;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import zlib.set.ArrayList;
import zlib.set.MapThreadLocal;
import zlib.set.ObjectArray;
import zlib.text.CharBuffer;
import zlib.text.CharBufferThreadLocal;
import zlib.xml.Element;
import zlib.xml.XmlReader;

/**
 * 类说明：模板类
 * 
 * @version 1.0
 * @author hy
 */

public class Template
{

	/* static fields */
	/** 标志常量定义， 文本标志，变量标志，模板标志 */
	public static final int TEXT=0,VARIABLE=1,TEMPLATE=2;
	/** 标志常量的文字描述 */
	public static final String[] TYPES={"TEXT","VARIABLE","TEMPLATE"};
	/** 变量前缀，变量后缀 */
	public static final String PREFIX="<!--[",SUFFIX="]-->";
	/** 缺省变量，代表元素中第一段文字 */
	public static final String DEFAULT_VAR="$";
	/** 变量域标志符和变量结束标志符 */
	public static final char FIELD_CHAR='.',END_CHAR='/';
	/** 默认的缓存容量大小 */
	public static final char BUFFER_CAPACITY=190;

	/* fields */
	/** 父模板 */
	Template parent;
	/** 变量前缀 */
	String prefix=PREFIX;
	/** 变量后缀 */
	String suffix=SUFFIX;
	/** 变量结束标志符 */
	char endChar=END_CHAR;
	/** 内容列表 */
	ObjectArray contentList=new ObjectArray();

	/** 缓存容量大小 */
	int bufferCapacity=BUFFER_CAPACITY;

	/* constructors */
	/** 构造默认的模板 */
	public Template()
	{
		this(null);
	}
	/** 构造指定父模板的模板 */
	public Template(Template parent)
	{
		this.parent=parent;
	}
	/* properties */
	/** 获得父模板 */
	public Template getParent()
	{
		return parent;
	}
	/** 获得变量前缀 */
	public String getPrefix()
	{
		return prefix;
	}
	/** 设置变量前缀 */
	public void setPrefix(String str)
	{
		if(str.length()<=0) return;
		prefix=str;
	}
	/** 获得变量后缀 */
	public String getSuffix()
	{
		return suffix;
	}
	/** 设置变量后缀 */
	public void setSuffix(String str)
	{
		if(str.length()<=0) return;
		suffix=str;
	}
	/** 获得变量结束标志符 */
	public char getEndChar()
	{
		return endChar;
	}
	/** 设置变量结束标志符 */
	public void setEndChar(char c)
	{
		endChar=c;
	}
	/** 获得缓存容量大小（仅在读取时使用） */
	public int getBufferCapacity()
	{
		return bufferCapacity;
	}
	/** 设置缓存容量大小 */
	public void setBufferCapacity(int c)
	{
		bufferCapacity=c;
	}
	/* methods */
	/** 读取内容方法 */
	public void read(Reader reader)
	{
		CharBuffer buffer=CharBufferThreadLocal.getCharBuffer();
		buffer.clear();
		if(fillBuffer(reader,buffer)<0) return;
		read(reader,buffer,null);
	}
	/** 读取内容方法 */
	protected void read(Reader reader,CharBuffer buffer,String name)
	{
		int top=buffer.top(),offset=buffer.offset();
		char[] array=buffer.getArray();
		char[] chars1=prefix.toCharArray();
		char[] chars2=suffix.toCharArray();
		int c1=chars1[0],c2=chars2[0];
		int n1=chars1.length,n2=chars2.length;
		ArrayList list=new ArrayList();
		int r=0,first=-1,last=-1,record=offset,t=0;
		String var=null;
		int i,j;
		while(true)
		{
			// 寻找前缀
			if(first<0)
			{
				for(i=offset;i<top;i++)
				{
					if(array[i]!=c1) continue;
					t=i;
					for(j=1,i++;j<n1&&i<top;j++,i++)
					{
						if(array[i]!=chars1[j]) break;
					}
					// 发现前缀
					if(j>=n1)
					{
						first=t;
						offset=i;
						break;
					}
					// 前缀未分析完
					else if(i>=top)
					{
						offset=t;
						break;
					}
				}
				// 未发现前缀，重置缓存或扩大缓存容量后继续读取
				if(first<0)
				{
					if(offset<t) offset=top;
					// 如果有已经记录的数据，则重置缓存
					if(record>0)
					{
						resetBuffer(buffer,record);
						offset-=record;
						top=buffer.top();
						record=0;
					}
					else
						buffer.setCapacity(array.length+bufferCapacity);
					r=fillBuffer(reader,buffer);
					if(r<0) break;
					array=buffer.getArray();
					top=buffer.top();
					continue;
				}
			}
			// 寻找后缀
			for(i=offset;i<top;i++)
			{
				// 变量中不允许有空格，返回到寻找前缀
				if(array[i]==' ')
				{
					offset=i+1;
					first=-1;
					break;
				}
				if(array[i]!=c2) continue;
				t=i;
				for(j=1,i++;j<n2&&i<top;j++,i++)
				{
					if(array[i]!=chars2[j]) break;
				}
				// 发现后缀
				if(j>=n2)
				{
					last=t;
					offset=i;
					break;
				}
				// 后缀未分析完
				else if(i>=top)
				{
					offset=t;
					break;
				}
			}
			if(first<0) continue;
			// 未发现后缀，重置缓存或扩大缓存容量后继续读取
			if(last<0)
			{
				if(offset<t) offset=top;
				// 如果有已经记录的数据，则重置缓存
				if(record>0)
				{
					first-=record;
					offset-=record;
					resetBuffer(buffer,record);
					top=buffer.top();
					record=0;
				}
				else
					buffer.setCapacity(array.length+bufferCapacity);
				r=fillBuffer(reader,buffer);
				if(r<0) break;
				array=buffer.getArray();
				top=buffer.top();
				continue;
			}
			// 根据前后缀位置，获取变量
			t=first+n1;
			// 如果变量长度为0，则为普通文本，返回到寻找前缀
			if(last==t)
			{
				last=first=-1;
				continue;
			}
			// 记录变量前的文本
			var=new String(array,record,first-record);
			list.add(new Item(TEXT,var,null));
			// 根据变量结束标志符的位置，判断是否加入变量或模板
			// 如果为模板结束标记
			if(array[t]=='/')
			{
				t++;
				var=new String(array,t,last-t);
				// 如果不是自身的模板名
				if(!var.equals(name))
					throw new TemplateException(getClass().getName()
						+" read, template="+name+", invalid end tag:"+var);
				buffer.setOffset(offset);
				break;
			}
			// 如果为变量结束标记
			else if(array[last-1]=='/')
			{
				var=new String(array,t,last-t-1);
				list.add(new Item(VARIABLE,var,null));
				buffer.setOffset(offset);
			}
			// 如果为模板开始标记
			else
			{
				var=new String(array,t,last-t);
				Template template=new Template();
				buffer.setOffset(offset);
				template.read(reader,buffer,var);
				list.add(new Item(TEMPLATE,var,template));
				array=buffer.getArray();
				top=buffer.top();
				offset=buffer.offset();
			}
			last=first=-1;
			record=offset;
		}
		// 如果流结束
		if(r<0)
		{
			// 如果子模板结束
			if(name!=null)
				throw new TemplateException(getClass().getName()
					+" read, unfinished template="+name);
			var=new String(array,record,top-record);
			list.add(new Item(TEXT,var,null));
		}
		contentList.add(list.getArray(),0,list.size());
	}
	/** 重置缓存方法 */
	void resetBuffer(CharBuffer buffer,int record)
	{
		char[] array=buffer.getArray();
		buffer.setOffset(buffer.offset()-record);
		buffer.setTop(buffer.top()-record);
		System.arraycopy(array,record,array,0,buffer.top());
	}
	/** 填充缓存方法 */
	int fillBuffer(Reader reader,CharBuffer buffer)
	{
		try
		{
			char[] array=buffer.getArray();
			int top=buffer.top();
			int r=reader.read(array,top,array.length-top);
			if(r<0) return -1;
			top+=r;
			buffer.setTop(top);
			return top;
		}
		catch(IOException e)
		{
			throw new TemplateException(e);
		}
	}
	/** 写入内容方法 */
	public void write(Writer writer,String key,String value)
	{
		Element e=new Element();
		e.setAttribute(key,value);
		write(writer,e);
	}
	/** 写入内容方法 */
	public void write(Writer writer,Element el)
	{
		if(contentList==null) return;
		if(el!=null)
		{
			Map map=MapThreadLocal.getMap();
			map.clear();
			write(writer,el,map);
		}
		else
			write(writer);
	}
	/** 写入内容方法 */
	protected void write(Writer writer)
	{
		Object[] array=contentList.getArray();
		Item it;
		try
		{
			for(int i=0;i<array.length;i++)
			{
				it=(Item)array[i];
				if(it.type==TEXT)
					writer.write(it.textVar);
				else if(it.type==VARIABLE)
					writeVariable(writer,it.textVar);
				else if(it.type==TEMPLATE)
					writeTemplate(writer,it.textVar,it.template);
			}
		}
		catch(IOException e)
		{
			throw new TemplateException(e);
		}
	}
	/** 写入变量 */
	void writeVariable(Writer writer,String var) throws IOException
	{
		writer.write(prefix);
		writer.write(var);
		writer.write(endChar);
		writer.write(suffix);
	}
	/** 写入模板 */
	void writeTemplate(Writer writer,String var,Template template)
		throws IOException
	{
		writer.write(prefix);
		writer.write(var);
		writer.write(suffix);
		template.write(writer);
		writer.write(prefix);
		writer.write(endChar);
		writer.write(var);
		writer.write(suffix);
	}
	/** 写入内容方法 */
	protected void write(Writer writer,Element root,Map map)
	{
		Object[] array=contentList.getArray();
		Item it;
		try
		{
			for(int i=0;i<array.length;i++)
			{
				it=(Item)array[i];
				if(it.type==TEXT)
					writer.write(it.textVar);
				else if(it.type==VARIABLE)
					writeVariable(writer,it.textVar,root,map);
				else if(it.type==TEMPLATE)
					writeTemplate(writer,it.textVar,it.template,root,map);
			}
		}
		catch(IOException e)
		{
			throw new TemplateException(e);
		}
	}
	/** 写入变量 */
	boolean writeVariable(Writer writer,String var,Element root,Map map)
		throws IOException
	{
		int i=var.lastIndexOf(FIELD_CHAR);
		if(i==0) return false;
		String name;
		if(i>0)
		{
			name=var.substring(i+1);
			Element el=(Element)map.get(var.substring(0,i));
			// 如果没有当前所在元素，则寻找第一个可用元素
			if(el==null)
			{
				el=root;
				int j=0;
				while((i=var.indexOf(FIELD_CHAR,j))>0)
				{
					el=el.getFirstElement(var.substring(j,i));
					if(el==null) return false;
					j=i+1;
				}
			}
			name=getContent(el,name);
		}
		else
			name=getContent(root,var);
		if(name==null) return false;
		writer.write(name);
		return true;
	}
	/** 获得内容 */
	String getContent(Element el,String name)
	{
		return (DEFAULT_VAR.equals(name))?el.getFirstText():el
			.getAttribute(name);
	}
	/** 写入模板 */
	boolean writeTemplate(Writer writer,String var,Template template,
		Element root,Map map)
	{
		Element el=null;
		// 倒溯寻找所在的元素
		int i=var.length();
		while((i=var.lastIndexOf(FIELD_CHAR,i-1))>0)
		{
			el=(Element)map.get(var.substring(0,i));
			if(el!=null) break;
		}
		if(el==null) el=root;
		// 正溯寻找第一个可用元素
		int j=(i<0)?0:i+1;
		while((i=var.indexOf(FIELD_CHAR,j))>0)
		{
			el=el.getFirstElement(var.substring(j,i));
			if(el==null) return false;
			map.put(var.substring(0,i),el);
			j=i+1;
		}
		String name=var;
		if(j>0) name=name.substring(j);
		Element e;
		boolean b=false;
		for(i=0,j=el.getContentCount();i<j;i++)
		{
			if(el.getType(i)!=XmlReader.ELEMENT) continue;
			e=(Element)el.getContent(i);
			if(!name.equals(e.getName())) continue;
			map.put(var,e);
			template.write(writer,root,map);
			map.remove(var);
			b=true;
		}
		return b;
	}
	/** 清除模板的所有内容 */
	public void clear()
	{
		contentList.clear();
	}

	/* inner classes */
	/** 类说明：内容条目，包括类型和内容 */
	class Item
	{

		/* fields */
		/** 内容类型 */
		int type;
		/** 文字或变量 */
		String textVar;
		/** 模板 */
		Template template;

		/* constructors */
		/** 构建一个指定类型的内容条目 */
		Item(int type,String var,Template t)
		{
			this.type=type;
			textVar=var;
			template=t;
		}
	}

}