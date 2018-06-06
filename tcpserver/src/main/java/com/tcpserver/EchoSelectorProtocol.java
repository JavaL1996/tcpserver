package com.tcpserver;


import com.tcpserver.utils.AnalysisUtils;
import com.tcpserver.utils.CarData;
import com.tcpserver.utils.CarLock;
import com.tcpserver.utils.ProtocolData;
import com.tcpserver.utils.Result;
import com.tcpserver.utils.TimeValue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.experimental.results.PrintableResult;

public class EchoSelectorProtocol implements TCPProtocol{

	 private byte[] bytes= new byte[1024];
	 private int bufSize;
	 private Result result;
	  public EchoSelectorProtocol(int bufSize){
	    this.bufSize = bufSize;
	  }

	  public void handleAccept(SelectionKey key) throws IOException {
	    SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();
	    clntChan.configureBlocking(false);
	    clntChan.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
	  }

	  public void handleRead(SelectionKey key) throws IOException{
	    SocketChannel clntChan = (SocketChannel) key.channel();

	    ByteBuffer buf = (ByteBuffer) key.attachment();
	    
	    int length=0;
	    while((length=clntChan.read(buf))>0){
			for (int i=0;i<length;i++){
				bytes[i]=buf.get(i);
			}
			//获取读取的结果
			result=AnalysisUtils.analysis(bytes,length);
			PrintResult(result);
	    }
		if (length==-1){
			clntChan.close();
		}else{
			clntChan.register(key.selector(), SelectionKey.OP_WRITE,ByteBuffer.allocate(bufSize));
		}
	  }
	  private void PrintResult(Result result) {
		  if (result.getType()==1) {
			CarData data = (CarData) result.getData();
			System.out.println("车牌号："+data.getCarID());
			System.out.println("车闸编号:"+data.getLockId());
			System.out.println("车牌颜色:"+data.getCarColor());
			System.out.println("车牌类型:"+data.getCarType());
			System.out.println("运动方向:"+data.getDirection());
			System.out.println("白名单验证:"+data.getWhiteList());
		}else if (result.getType()==4) {
			TimeValue data = (TimeValue) result.getData();
			System.out.println("日期："+data.getDate().toString());
			System.out.println("时间："+data.getTime().toString());
		}else if (result.getType()==5) {
			CarLock data = (CarLock) result.getData();
			System.out.println("车闸编号："+data.getLockId());
			System.out.println("IP地址"+data.getIP());
			System.out.println("X坐标"+data.getPositionX());
			System.out.println("Y坐标"+data.getPositionY());
		}
	  }
	  public void handleWrite(SelectionKey key) throws IOException {
	    ByteBuffer buf = (ByteBuffer) key.attachment();
	    buf.clear();

	    if (result!=null&&result.getStatus().equals("true")){//表示传输成功，需要回传
	    	if (result.getType()==1){//表示正确接受车辆数据，返回响应成功
				buf.put(ProtocolData.successed);
			}else if (result.getType()==4){//表示需要向硬件回传数据
				buf.put(ProtocolData.getTime());
			}else if (result.getType()==5){//表示成功获取位置信息，返回接收成功
				buf.put(ProtocolData.successed);
			}
		}else {
	    	buf.put(ProtocolData.failed);
		}
	    buf.flip();

	    SocketChannel clntChan = (SocketChannel) key.channel();
	    clntChan.write(buf);
		clntChan.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufSize));
	  }
}
