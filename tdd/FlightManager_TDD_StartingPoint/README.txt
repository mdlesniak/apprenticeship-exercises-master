

This exercise tests your ability to test-drive an object model that is resilient to big structural changes. 
You are implementing requirements from the standpoint of a passenger who, for example, has had a flight cancelled, and wants 
a new flight (or trip). 

And you are also implementing requirements from the perspective of an airline CEO who, for example, has purchased another airline, 
and wants to eliminate duplicate routes and personnel, thereby saving money. 

Finally, you will also implement requirements from the standpoint of an airport, that wishes to attract new airlines, keep the airline it has, 
perhaps become a hub for an airline. 

And so on. These different kinds of stakeholds (and others) exert pressure on the codebase and object model along different "axes of change." 
Code changes that make it easy to support further changes for a passenger might actually make it harder to make changes on behalf of 
airline owners. 

The best path through this kind of requirements push and pull is exactly what this exercise tries to let you learn. 

/Test List
  
  //default to using single-segment Trip if single flight between origin and destination exist
  //Default to using flights from same airline as cancelled Trip if they all exist
  //Use fewest possible segments from other airlines
  //Only use flight segments when "enough" time exists for passenger connection
  
  //Test List:
	//Get any new single-segment Trip for Passenger
	//Assign Segment to Passenger
	//if default to same SeatClass as that of cancelled flight
	//next available, not just any available flight
	//next available flight is not on same airline as cancelled flight
  //upgrade new seat to FirstClass if Passenger has enough FrequentFlyerMiles
  
  //Test List
  //Can create repository with one airline and one single-segment trip between two airports
  //Can create repository with one airline and single-segment trips between all major Eastern Seaboard airports 
  //  less than 900 miles apart
  //Can create repository with one airline and single- and multi-segment trips between all major Eastern Seaboard airports
  //Can create repository with multiple airlines, with 20% route overlap, with trips between all major airports 
  //  in contiguous U.S.
  