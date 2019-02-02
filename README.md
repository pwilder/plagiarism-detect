# plagiarism-detect

## Introduction

The purpose of this project is to write a command-line program that performs plagiarism detection using a N-tuple comparison algorithm allowing for synonyms in the text. Please see Plagiarism_Detection_At_Home_Home_Coding_Exercise.docx for more instructions.

## Build instructions
### Requirements
* [JavaSE 1.8 or higher] (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
** Tested on 1.8
* [Maven] (https://maven.apache.org/)
** Tested on 3.6.0

### Build steps
* Run mvn package
** This will generate a target directory containing among other things.
*** Compiled Classes
*** plagiarism-detect-1.0.0.jar executable jar
*** plagiarism-detect-1.0.0-distribution.zip
*** plagiarism-detect-1.0.0-distribution.tar.gz

## Binaries
Binaries can be fond [here] (www.google.com)

### Execution
#### Command
* Extract the binary distribution appropriate to your environment.
** Options
*** Invoke main class `java -jar plagiarism-detect-1.0.0.jar`
*** Run script 
**** Change permissions on script (e.g. `chmod 755 run.sh`)
**** `./run.sh <args>`
**** `./run.bat <args>`
*** Include as lib.
**** Main class in the jar file is `edu.pwilder.plagiarism_detect.Main`
**** Another interesting class for test harnesses is `edu.pwilder.plagiarism_detect.QuadraticPlagiarismDetector`. Takes in as Main does but returns a double permitting further processing. 

#### Usage
```
Usage: <synonyms> <input file 1> <input file 2> [tupleSize]
  synonyms: Groups of synonyms separated by newlines
  input file 1: The path to the first input file, both absolute paths and paths relevant to the working directory accepted.
  input file 2: The path to the second input file, both absolute paths and paths relevant to the working directory accepted.
  tupleSize: tupleSize for comparison, must be >= 1
```

#### Tested on 

```
Java version: 1.8.0_74, vendor: Oracle Corporation
Default locale: en_CA, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.2", arch: "x86_64", family: "mac"
```

## Design Considerations

### What worked
* Streaming of file 1 content to reduce footprint in memory.
* Storage of file 2 content more efficient than storage of list of tuples via elimination of duplicate tuples.
* Strategy and Factory patterns loosen some code coupling.
* Logging has been included.
** The minor performance considerations of a good logger should almost always outweigh the performance costs. 

### Areas of improvement
* Use open source helper libraries to maximize code reuse.
** Obvious candidates
*** Apply injection (Guice, Spring, etc.) 
*** Apache Commons
*** Better logging framework.
*** Mockito to isolate unit tests to a single class. 
*** Investigate replacement for StreamTokenizer which is finicky to use.
* Revisit primary algorithm to see if we can find performance improvements.
* Increase test coverage.
* Improve the build process to facilitate smoother upversioning.
* Possibly consider replacing Maven with Gradle and having a bootstrap with less dependencies. 

### Additional design considerations
* There are [advantages] (https://softwareengineering.stackexchange.com/a/98703) to using `final` where possible in java but I've found it typically only works with IDE auto-formatting or *very* diligent developers. Since this was a greenfield project developed in eclipse I turned on auto insertion of finals.
* Camel casing acronyms can be [contentious] (https://stackoverflow.com/questions/15526107/acronyms-in-camelcase) but I tend to be a fan.
* I am a fan of [semantic versioning] (https://semver.org/)
* I tend not to use formal Javadoc comments instead preferring only to add comments where I believe they 
add value beyond something someone can derive from looking at the code.
