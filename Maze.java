package maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Maze {
	static int x=0;
	static int y=0;
	static String lab[]= new String[33]; //oshwdem2018

	static int floor[][]= new int[16][16];
	static int  it=0;
	static String path="";

	public static void main(String[] args) {

		File file = new File("/Users/sergio/Documents/logs/laberintos.txt");
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			while ((reader.readLine()) != null) {
				for (int l=0;l<33;l++) {
					lab[l]=reader.readLine();
				}
				solveMaze();
				paintLaberynth();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}

	}

	public static void solveMaze() {
		it=0;
		x=0;
		y=0;		
		path=" ";

		for (int x=0;x<16;x++){
			for (int y=0;y<16;y++){
				floor[x][y]=0;
			}
		}
		floor[0][0]=1;
		int anterior=5;

		while (!center()){
			double max=0;
			int dir=0;
			int exits=0;

			boolean goN=canIGoN(x,y);
			boolean goS=canIGoS(x,y);
			boolean goE=canIGoE(x,y);
			boolean goW=canIGoO(x,y);

			if (goN) exits++;	
			if (goS) exits++;	
			if (goE) exits++;	
			if (goW) exits++;
			if (exits<2) {floor[x][y]++;floor[x][y]++;floor[x][y]++;floor[x][y]++;anterior=5;}


			double scoreN=(goN?1:0)*40*(y>0?10-floor[x][y-1]:0)+20-distanceToCenter(x,y-1)-(anterior==1?10:0); 
			double scoreS=(goS?1:0)*40*(y<15?10-floor[x][y+1]:0)+20-distanceToCenter(x,y+1)-(anterior==0?10:0); 
			double scoreE=(goE?1:0)*40*(x<15?10-floor[x+1][y]:0)+20-distanceToCenter(x+1,y)-(anterior==3?10:0); 
			double scoreW=(goW?1:0)*40*(x>0?10-floor[x-1][y]:0)+20-distanceToCenter(x-1,y)-(anterior==2?10:0); 

			max=scoreN;
			if (max<scoreS) {dir=1;max=scoreS;}
			if (max<scoreE) {dir=2;max=scoreE;}
			if (max<scoreW) {dir=3;max=scoreW;}


			String lastDir=path.substring(path.length() - 1);

			switch (dir){
			case 0:
				y--;
				if(floor[x][y]<2)floor[x][y]++;
				if ("S".equals(lastDir)) {path=path.substring(0, path.length()-1);}else {path+="N";}
				break;
			case 1:
				y++;
				if(floor[x][y]<2)floor[x][y]++;
				if ("N".equals(lastDir)) {path=path.substring(0, path.length()-1);}else {path+="S";}
				break;
			case 2:
				x++;
				if(floor[x][y]<2)floor[x][y]++;
				if ("O".equals(lastDir)) {path=path.substring(0, path.length()-1);}else {path+="E";}
				break;
			case 3:
				x--;
				if(floor[x][y]<2)floor[x][y]++;
				if ("E".equals(lastDir)) {path=path.substring(0, path.length()-1);}else {path+="O";}
				break;
			}
			anterior=dir;	
			it++;


		}  

		paintLaberynth();
	}

	static double distanceToCenter(int x, int y) {
		return Math.sqrt((7.5-x)*(7.5-x)+(7.5-y)*(7.5-y));
	}

	static void paintLaberynth(){
		System.out.println("Iteración:"+it);
		System.out.println("Solución: "+path);
		System.out.println("Solución Distancia:"+(path.length()-1));

		for (int yy=0;yy<33; yy++){
			for (int xx=0;xx<33; xx++){
				String s =lab[yy].substring(xx,xx+1);
				if (" ".equals(s)){
					if ((x*2+1==xx) && (y*2+1==yy)){
						s="x";
					}else if ((xx%2==0)||(yy%2==0)){
						s=" ";
					}else {
						s=Integer.toString(floor[xx/2][yy/2]);
					}
				}

				System.out.print(s);
			}
			System.out.println();

		}


	}




	static boolean center(){
		return ((x>6)&&(x<9)&&(y>6)&&(y<9));
	}


	static String whatIsAtXYD(int x, int y, int d){
		if (d==0){
			String s=lab[y*2].substring(x*2+1, x*2+2);
			return s;
		}
		else if (d==1){
			String s=lab[y*2+2].substring(x*2+1, x*2+2);
			return s;
		}
		else if (d==2){
			String s=lab[y*2+1].substring(x*2+2, x*2+3);
			return s;
		}
		else if (d==3){
			String s=lab[y*2+1].substring(x*2, x*2+1);
			return s;
		}
		return("Error");
	}

	static Boolean canIGoN(int x, int y){
		if ((y>0)&&" ".equals(whatIsAtXYD(x,y,0))){ return true; } else {return false;}
	}	
	static Boolean canIGoS(int x, int y){
		if ((y<15)&&" ".equals(whatIsAtXYD(x,y,1))){ return true; } else {return false;}
	}	
	static Boolean canIGoE(int x, int y){
		if ((x<15)&&" ".equals(whatIsAtXYD(x,y,2))){ return true; } else {return false;}
	}	
	static Boolean canIGoO(int x, int y){
		if ((x>0)&&" ".equals(whatIsAtXYD(x,y,3))){ return true; } else {return false;}
	}	

	static Boolean canIGo(int x, int y, int d) {
		switch (d) {
		case 0: return canIGoN(x,y);
		case 1: return canIGoS(x,y);
		case 2: return canIGoE(x,y);
		case 3: return canIGoO(x,y);
		}
		return false;
	}

}

