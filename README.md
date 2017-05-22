# Translator
## 0. Info
### Translate your words list!
Program allows you to translate words list from both: Polish to English and English to Polish.

## 0.2. Translations source
Dictionary from https://diki.pl/

## 0.3. Purpose
Use your generated file to run with Google Play app Wordi (https://play.google.com/store/apps/details?id=pl.voowl)

## 1. How to install
```
git clone https://github.com/mdjdrn1/translator.git
cd translator
mvn install
cd target
```

## 2. How to run
Use jar ```translator-1.0-jar-with-dependencies.jar``` from ```target``` directory
```
java -jar translator-1.0-jar-with-dependencies.jar <INPUT_FILE> -o <OUTPUT_FILE>
```
### Arguments
+ ```INPUT_FILE```: required - input file path
+ ```-o```: optional - flag for OUTPUT_FILE
+ ```OUTPUT_FILE```: required ONLY when specified "-o" flag - output file path

## 3. Input file
Allowed input file formats:
+ .xls
+ .xlsx

## 4. File structures
### Input file structure
Input spreadsheet file should contain words list in first column.
E.g.

|         | A        | B           | C  |
| -------------: |:-------------:| :-----:|:-----:|
| 1|	word1	|	|	|
| 2|	word2	|	|	|
| 3|	word3	|	|	|


### Output file structure
|         | A        | B           | C  |
| -------------: |:-------------:| :-----:|:-----:|
| 1|	word1	|	translatedWord1	|	|
| 2|	word2	|	translatedWord2	|	|
| 3|	word3	|	translatedWord3 |	|

