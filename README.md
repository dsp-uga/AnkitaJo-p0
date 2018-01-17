# AnkitaJo-p0
Project 0 Implementation: Word Count using Spark

Compile the code: 
```
sbt assembly
```
Run the code 
```
spark-submit --master local[*] --class "books" target/scala-2.10/Project0-assembly-1.0.jar "\path to data files" "\path to stopwords file"
```
For distributed use:
```
--deploy-mode cluster
```

The AutoLab submission works and gives a score for files
sp1.json,
sp2.json,
sp3.json

Problems:
However, it throws an error when sp4.json is submitted and also gives 0 score for the rest of the files, even when they are not changed. The error in sp4.json is:
```
UnicodeDecodeError: 'ascii' codec can't decode byte 0xe2 in position 360: ordinal not in range(128)
```

I am not sure which character in the file it is not able to identify.




