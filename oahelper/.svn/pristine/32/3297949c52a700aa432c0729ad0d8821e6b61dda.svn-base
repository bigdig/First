package com.tfzq.util;

import java.util.ArrayList;
import java.util.List;

import com.tfzq.pb.model.generator.PbFile;

public class PbFileUtils {
	public static List<PbFile> fillFiles(String[] fileids,String[] filenames){
		List<PbFile> fileList = new ArrayList<PbFile>();
		for(int i=0;i<fileids.length;i++){
			if(filenames[i]!=null||!filenames[i].equals("")){
				PbFile file = new PbFile();
				file.setActualName(filenames[i]);
				file.setId(fileids[i]);
				fileList.add(file);
			}
		}
		return fileList;
	}

}
