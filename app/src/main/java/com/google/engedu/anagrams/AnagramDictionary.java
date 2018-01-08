/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    ArrayList<String> wordList;
    HashSet<String> wordSet;
    HashMap<String,ArrayList<String>> lettersToWord;
    HashMap<Integer,ArrayList<String>> sizeToWords;
    int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        wordList=new ArrayList<String>();
        wordSet=new HashSet<String>();
        lettersToWord = new HashMap<String,ArrayList<String>>();
        sizeToWords = new HashMap<Integer,ArrayList<String>>();
        wordLength=DEFAULT_WORD_LENGTH;
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> listWords = lettersToWord.get(sortedWord);
                listWords.add(word);
                lettersToWord.put(sortedWord, listWords);
            } else {
                ArrayList<String> listWords = new ArrayList<>();
                listWords.add(word);
                lettersToWord.put(sortedWord, listWords);
            }
            if(sizeToWords.containsKey(word.length())){
                ArrayList<String> listWords = sizeToWords.get(word.length());
                listWords.add(word);
                sizeToWords.put(word.length(), listWords);
            } else {
                ArrayList<String> listWords = new ArrayList<>();
                listWords.add(word);
                sizeToWords.put(word.length(), listWords);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {

        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
       /* String sortedWord=sortLetters(targetWord);
        boolean flag=false;
        Iterator<String> i = wordList.iterator();
        while (i.hasNext()) {
            String str=i.next();
            flag=false;
            if (str.length()==sortedWord.length()){
                for(int j=0;j<sortedWord.length();j++){
                   if(str.contains(sortedWord.substring(j,j+1))){
                      flag=true;
                   }
                   else {
                       flag = false;
                       break;
                   }
                }
            }
            if(flag){
               result.add(str);
            }
        }
*/
        result=lettersToWord.get(sortLetters(targetWord));
        List<String> list=getAnagramsWithOneMoreLetter(targetWord);
        for (String anagram:list) {
            result.add(anagram);
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char letter : alphabets) {
            if (lettersToWord.containsKey(sortLetters(word + letter))) {
                result = lettersToWord.get(sortLetters(word + letter));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int length = 0;
        /*ArrayList<String> temp = sizeToWords.get(wordLength);
        int sizeArrayLength = temp.size();*/
        while (length < MIN_NUM_ANAGRAMS) {
            //String word = temp.get(new Random().nextInt(sizeArrayLength));
            String word = wordList.get(new Random().nextInt(wordList.size()));
            length = lettersToWord.get(sortLetters(word)).size();
            if (length >= MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) wordLength++;
                return word;
            }
            /*else {
                temp.remove(wordLocate);
            }*/
        }
        return "stop";
    }


   public String sortLetters(String word){
        char[] charArray=word.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
   }
}
