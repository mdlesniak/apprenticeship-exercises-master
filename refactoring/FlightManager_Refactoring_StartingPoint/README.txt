The EM-Pathy exercise is designed to help programmers learn the following principles and practices:

	-- Naming / Renaming modules
	-- Keeping modules really, really small
	-- Cyclomatic Complexity
	-- The Single Responsibility Principle
	-- The Composed Method pattern
	-- The Extract Method/Variable refactorings
	-- The Inline refactoring
	-- Basic Legacy Code Rescue (Boy Scout Rule)

You can read about these principles and practices in the draft of the book, The Agile Programming Path, 
here:  

http://docs.google.com/Edit?docid=dhszqxnr_2gmbrr2dq

--------------------

You can do 3 exercises using EM-Pathy:

1. Run the tests in FlightManagerTest.java. Keep them running green at all times. Focus on FlightManagerTest.java 
and FlightDataManagerAgentUtilProcessorClass.java, and focus specifically on the following practices:

   a. Renaming variables, methods, classes
   b. Extracting Methods in FlightDataManagerAgentUtilProcessorClass until you cannot extract any more. 
   
For detailed guidance in the craft of method extraction, see the book draft linked above, the book Refactoring, by Martin Fowler, 
and the following blog post by Robert Martin:  

http://blog.objectmentor.com/articles/2009/09/11/one-thing-extract-till-you-drop

For guidance on naming and renaming, see both Chapter 2 of Robert C Martin's book Clean Code, as well as the 
Agile Programming Path book draft (linked above). 

Rename everything in the codebase that feels badly named to you. Extract all the behavior  
into the smallest methods you can in FlightDataManagerAgentUtilProcessorClass, then 
delete the entire codebase (or revert it), and start with a fresh copy from the Google Code 
repository.
 
Repeat this first step a total of 10 times, or until you have all of the steps memorized, and can 
perform all of the renamings and extractions by hand. For now, you can ignore class boundaries. 
Focus your work on only on the small list of patterns and practices listed above. 


....

....

[to be continued]


	