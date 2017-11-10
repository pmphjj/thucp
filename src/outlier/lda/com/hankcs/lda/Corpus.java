/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/1/29 17:03</create-date>
 *
 * <copyright file="Corpus.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package outlier.lda.com.hankcs.lda;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import view.InputData;
import view.InputDataRowType;
import view.FrameworkMain;

/**
 * a set of documents
 * 语料库，也就是文档集合
 *
 * @author hankcs
 */
public class Corpus
{
    List<int[]> documentList;
    Vocabulary vocabulary;

    public Corpus()
    {
        documentList = new LinkedList<int[]>();
        vocabulary = new Vocabulary();
    }

    public int[] addDocument(List<String> document)
    {
        int[] doc = new int[document.size()];
        int i = 0;
        for (String word : document)
        {
            doc[i++] = vocabulary.getId(word, true);
        }
        documentList.add(doc);
        return doc;
    }

    public int[][] toArray()
    {
        return documentList.toArray(new int[0][]);
    }

    public int getVocabularySize()
    {
        return vocabulary.size();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for (int[] doc : documentList)
        {
            sb.append(Arrays.toString(doc)).append("\n");
        }
        sb.append(vocabulary);
        return sb.toString();
    }

    /**
     * Load documents from disk
     *
     * @param folderPath is a folder, which contains text documents.
     * @return a corpus
     * @throws IOException
     */
    public static Corpus load(String folderPath) throws IOException
    {
        Corpus corpus = new Corpus();
        File folder = new File(folderPath);
        for (File file : folder.listFiles())
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            List<String> wordList = new LinkedList<String>();
            while ((line = br.readLine()) != null)
            {
                String[] words = line.split(" ");
                for (String word : words)
                {
                    if (word.trim().length() < 2) continue;
                    wordList.add(word);
                }
            }
            br.close();
            corpus.addDocument(wordList);
        }
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }
    
    public static Corpus load(Map<String, InputData> dataSet) throws IOException
    {
        Corpus corpus = new Corpus();
        
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
		for(Map.Entry<String, InputData> entry:FrameworkMain.paramInputDataSetOfFunc.entrySet()){
			String key=entry.getKey();
			if(key.contains(".csv")){//只处理csv文件
				InputData value=entry.getValue();
				for(InputDataRowType row:value.getData()){
					String visitId=row.getVisitId();
					String date=df.format(row.getTime());
					String event=row.getEvent();
					if(map.containsKey(visitId+"#"+date)){
						map.get(visitId+"#"+date).add(event);
					}else{
						ArrayList<String> temp=new ArrayList<String>();
						temp.add(event);
						map.put(visitId+"#"+date, temp);
					}
				}
			}
		}
		
		for(Map.Entry<String, ArrayList<String>> entry:map.entrySet()){
			ArrayList<String> lines=entry.getValue();
            List<String> wordList = new LinkedList<String>();
            for(String word:lines){
            	if (word.trim().length() < 2) continue;
                wordList.add(word);
            }
            corpus.addDocument(wordList);
		}
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }

    public Vocabulary getVocabulary()
    {
        return vocabulary;
    }

    public int[][] getDocument()
    {
        return toArray();
    }

    public static int[] loadDocument(String path, Vocabulary vocabulary) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        List<Integer> wordList = new LinkedList<Integer>();
        while ((line = br.readLine()) != null)
        {
            String[] words = line.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                Integer id = vocabulary.getId(word);
                if (id != null)
                    wordList.add(id);
            }
        }
        br.close();
        int[] result = new int[wordList.size()];
        int i = 0;
        for (Integer integer : wordList)
        {
            result[i++] = integer;
        }
        return result;
    }
}
