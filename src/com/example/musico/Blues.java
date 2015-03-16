package com.example.musico;

public class Blues {

	static int lastNote = 0;
	
	static int[] scale = new int[]{50,53,55,56,57,60,62,65,67,68,69,72,74};
	
	static int[] memory;
	
	static int direction = 1;
	
	public static int[] blueScale(int count){
		int[] improv = new int[count];
		memory = new int[count];
		for(int i = 0; i < count; i++)
		{
			// inits
			double randomInterval = Math.random();
			double randomDirection = Math.random();
			int interval = 1;
			
			// choose interval
			if(randomInterval < .6) interval = 1;
			else if(randomInterval < .7) interval = 2;
			else if (randomInterval < .8) interval = 3;
			else if (randomInterval < .96) interval = 4;
			else interval = 0;
			
			// change direction
			if(randomDirection > .8) direction *= -1;
			
			// keep inside pitch limits by changing direction
			int index = lastNote+(direction*interval);
			if(index >= scale.length || index < 0)
			{
				direction *= -1;
				index = lastNote+(direction*interval);
			}
			lastNote = index;
			memory[i]=interval;			
			improv[i] = scale[index];
		}
		
		//int chosenNote = (int)(Math.random()*(scale.length-1));
		/*note = scale[i];
		i++;
		if(i >= scale.length-1) i = 0;*/				
		return improv;		
	}	
}
