package com.cengage.apprenticeship;

public class Roman {
	public String convert(int number){
		 int units= number % 10;
		 int tens=(number/10 % 10);
		 int hundreds=(number/100 % 10);
		 int thousands=(number/1000 %10);

		 return
		 thousands(thousands)+
		 convert(hundreds, "C", "D", "M")+
		 convert(tens, "X","L","C")+
		 convert(units, "I","V","X");
		}

		private String thousands(int number){
		 String retValue="";
		 for ( int i=0;i<number;i++){
		   retValue+="M";
		 }
		 return retValue;
		}

		private String convert(int number,String unit, String five, String ten){
		 if (number<=3){
		   String retValue="";
		   for (int i=0;i<number;i++){
		     retValue+=unit;
		   }
		   return retValue;
		 }
		 if (number==4){
		   return unit+five;
		 }
		 if (number<=8){
		   String retValue=five;
		   for (int i=5;i<number;i++){
		     retValue+=unit;
		   }
		   return retValue;
		 }
		 return unit+ten;
		}

}
