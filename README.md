CS3211-Project-1
================
#2 Specification of the system#
**You will be constructing at least three variants of your system:**
1. A version which exhibits deadlock due to some plausible failure of your system (perhaps in the communication protocol).
2. A version which exhibits incorrect calculations due to a plausible interleaving of transactions (as discussed in lecture 3).
~~3. A version which has these two errors fixed.~~

#3.1 Construct CSP/PAT models (6 marks)#
**In your documentation for the model, you should describe**

what extra components of the specification you have added (if any);

what elements have been modelled,
1.	ATM
2.	Cloud
3.	Database
4.	Unreliable network

and what model design decisions were made;

what abstractions have been used, and why.

#3.2 Develop assertions (6 marks)#
##You should develop assertions that test the behaviour of the models, corresponding to elements of the specification, and also demonstrating the specified failures of the systems.##

1. #assert System() deadlockfree
2. #assert System() |= [](authenticateCard -> <> withdrawal)

Other possible assertions
Must autheticate first before doing other actions
The 3 possible cases of unreliable network
retrieve -> retrieve -> update -> update	(Variant 2: interleaving of transactions)

Depending on your model, you may be able to make extra assertions about
(for example) withdrawals being processed in order,	(Don't think our model can do this)
or that a withdrawal will only be done once.		(Our model can withdraw as many times as we want)
You may think of many other useful things you can assert about your system.

**In your documentation for the assertions for each model, you should list your assertions, giving an “English” interpretation of the assert clause, and give the results of model checking.**

#3.3 Java simulator (6 marks)#
Your simulation should allow someone to exercise all the behaviours of the system
**for example it should be possible to trigger a series of transactions.**
This may be done with a GUI, or by a text file input, or by command-line input.
**In your documentation for the simulator, you should describe the structure of your code, relating it to the CSP/PAT model, and describe how to run/test the simulator.**