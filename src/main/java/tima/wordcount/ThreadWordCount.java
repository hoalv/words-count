package tima.wordcount;

import java.io.BufferedReader;
import java.util.concurrent.ConcurrentMap;

public class ThreadWordCount extends Thread{
	private String buffer;
	private String filePath;
	private ConcurrentMap<String, Integer> wordCount;
	public ThreadWordCount(String buffer, ConcurrentMap<String,Integer> counts) {
		super();
		this.buffer = buffer;
		this.wordCount = counts;
	}
	public ConcurrentMap<String, Integer> getWordCount() {
		return wordCount;
	}
	
	public static String readFileAsString(BufferedReader reader, int size)
	        throws java.io.IOException 
	    {
	        StringBuffer fileData = new StringBuffer(size);
	        int numRead=0;

	        while(size > 0) {
	            int bufsz = 1024 > size ? size : 1024;
	            char[] buf = new char[bufsz];
	            numRead = reader.read(buf,0,bufsz);
	            if (numRead == -1)
	                break;
	            String readData = String.valueOf(buf, 0, numRead);
	            fileData.append(readData);
	            size -= numRead;
	        }
	        return fileData.toString();
	    }

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		System.out.println("bf: "+buffer);
//		buffer = buffer.replaceAll("[^a-zA-Z0-9]+","");
		for(String word: buffer.split("\\s+|\\t+|,|;|\\.|:")){
			word = word.replaceFirst("[^\\p{L}]*", "");
            word = new StringBuffer(word).reverse().toString();
            word = word.replaceFirst("[^\\p{L}]*", "");
            word = new StringBuffer(word).reverse().toString();
            
//            System.out.println(word);
            Integer count = this.wordCount.get(word);
            this.wordCount.put(word, count == null ? 1 : count + 1);
			}
						
	}
	
}
