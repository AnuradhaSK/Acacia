/**
Copyright 2015 Acacia Team

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.acacia.partitioner.local.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.acacia.localstore.java.AcaciaHashMapLocalStore;

public class PartitionWriter{
	private File file;
	private BufferedWriter bw;
	private FileWriter fw;
	private String partitionFilePath;
	private AcaciaHashMapLocalStore store;
	
	public PartitionWriter(String fileFullPath){
		this.partitionFilePath = fileFullPath;
		
		file = new File(fileFullPath);
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PartitionWriter(int graphID, int partitionID, String fileFullPath){
		this.partitionFilePath = fileFullPath;
		store = new AcaciaHashMapLocalStore(fileFullPath);
	}
	
	public void compress(){    
		store.storeGraph();
		Runtime r = Runtime.getRuntime();
		Process p;
		try {
			p = r.exec("zip -rj "+ partitionFilePath + ".zip " + partitionFilePath);
			p.waitFor();
			
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			
			while((line=b.readLine())!= null){
				System.out.println(line);
			};
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void writeEdge(long firstVertex, long secondVertex){
//		try {
//			bw.write(""+firstVertex+" "+secondVertex+"\r\n");
//			bw.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		store.addEdge(firstVertex, secondVertex);		
	}
	
	public String getOutputFilePath(){
		return partitionFilePath;
	}
	
	public void close(){
//		try {
//			bw.close();
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}