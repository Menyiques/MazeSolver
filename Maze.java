package Maze;

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
	static String pathRelativo="";
	static long mem=0;
	static int orientacion=1; //0N,1E,2S,3W

	public static void main(String[] args) {

		File file = new File("c://d//maze//laberintos.txt");
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			while ((reader.readLine()) != null) {
				for (int l=0;l<33;l++) {
					lab[l]=reader.readLine();
				}
				solveMaze();
				//paintLaberynth();
				System.out.println("Suelo:"+floor[0][0]);
				Runtime.getRuntime().halt(0);
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
		//inicializa el suelo
		for (int x=0;x<16;x++){
			for (int y=0;y<16;y++){
				floor[x][y]=0;
			}
		}
		floor[0][0]=1;
		int anterior=5;
		//Repite hasta llegar al centro
		while (!center()){
			
		
			double max=0;
			int dir=0;
			int exits=0;
			boolean goN=canIGo(x,y,0);
			boolean goE=canIGo(x,y,1);
			boolean goS=canIGo(x,y,2);
			boolean goW=canIGo(x,y,3);

			if (goN) exits++;	
			if (goS) exits++;	
			if (goE) exits++;	
			if (goW) exits++;
			if (exits<2) {floor[x][y]+=4;anterior=5;}//sólo hay una salida, es un bloqueo, súbele el peso para que no vuelva


			double scoreN=(goN?1:0)*40*(y>0?10-floor[x][y-1]:0)+20-distanceToCenter(x,y-1)-(anterior==2?10:0); 
			double scoreE=(goE?1:0)*40*(x<15?10-floor[x+1][y]:0)+20-distanceToCenter(x+1,y)-(anterior==3?10:0); 
			double scoreS=(goS?1:0)*40*(y<15?10-floor[x][y+1]:0)+20-distanceToCenter(x,y+1)-(anterior==0?10:0); 
			double scoreW=(goW?1:0)*40*(x>0?10-floor[x-1][y]:0)+20-distanceToCenter(x-1,y)-(anterior==1?10:0);

			max=scoreN;
			if (max<scoreE) {dir=1;max=scoreE;}
			if (max<scoreS) {dir=2;max=scoreS;}
			if (max<scoreW) {dir=3;max=scoreW;}


			String lastDir=path.substring(path.length() - 1);

			
			switch (dir){
			case 0:
				y--;
				if(floor[x][y]<2)floor[x][y]++;
				if ("S".equals(lastDir)) {
						path=path.substring(0, path.length()-1);
						pathRelativo=pathRelativo.substring(0,pathRelativo.length()-1);
						}else 
						{path+="N";pathRelativo+=letraGiro(dir);}
				go(dir);
				break;
			case 2:
				y++;
				if(floor[x][y]<2)floor[x][y]++;
				if ("N".equals(lastDir)) {
					path=path.substring(0, path.length()-1);
					pathRelativo=pathRelativo.substring(0,pathRelativo.length()-1);
					}else {path+="S";pathRelativo+=letraGiro(dir);}
				go(dir);
				break;
			case 1:
				x++;
				if(floor[x][y]<2)floor[x][y]++;
				if ("W".equals(lastDir)) {
					path=path.substring(0, path.length()-1);
					pathRelativo=pathRelativo.substring(0,pathRelativo.length()-1);
					}else {path+="E";pathRelativo+=letraGiro(dir);}
				go(dir);
				break;
			case 3:
				x--;
				if(floor[x][y]<2)floor[x][y]++;
				if ("E".equals(lastDir)) {
					path=path.substring(0, path.length()-1);
					pathRelativo=pathRelativo.substring(0,pathRelativo.length()-1);
					}else {path+="W";pathRelativo+=letraGiro(dir);}
				go(dir);
				break;
			}
			
			anterior=dir;	
			it++;	
		}  


	}
	
	
	static String letraGiro(int dir){
		
		int giro=dir-orientacion;
		if (giro==-3) giro=1;// es más rápido girar una vez a la dcha que 3 a la izqda
		if (giro==3) giro=-1;
		
		String letraGiro="";
		if (giro==0)letraGiro="F";
		if (giro==-1)letraGiro="I";
		if (giro==1)letraGiro="D";
		if (Math.abs(giro)==2)letraGiro="V";
		return letraGiro;
	}
	static void go(int destino){
		
		

		String pc="NESW";
		System.out.println("X:"+x+" , Y:"+y);
		System.out.println("Go:"+pc.substring(destino,destino+1)+" Orientacion:"+pc.substring(orientacion,orientacion+1));
		orientacion=destino;
		paintLaberynth();
	}
	
	static double distanceToCenter(int x, int y) {
		return Math.sqrt((7.5-x)*(7.5-x)+(7.5-y)*(7.5-y));
	}

	static void paintLaberynth(){
		System.out.println("Iteración:"+it);
		System.out.println("Solución:         "+path);
		System.out.println("SoluciónRelativa:  "+pathRelativo);
		System.out.println("Solución Distancia:"+(path.length()-1));
		System.out.println("Mem used="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		Runtime.getRuntime().gc();
		for (int yy=0;yy<20; yy++){
			for (int xx=0;xx<33; xx++){
				String s =lab[yy].substring(xx,xx+1);
				if (" ".equals(s)){
					if ((x*2+1==xx) && (y*2+1==yy)){
						String symbols="^>v<";
						s=symbols.substring(orientacion,orientacion+1);
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
		else if (d==2){
			String s=lab[y*2+2].substring(x*2+1, x*2+2);
			return s;
		}
		else if (d==1){
			String s=lab[y*2+1].substring(x*2+2, x*2+3);
			return s;
		}
		else if (d==3){
			String s=lab[y*2+1].substring(x*2, x*2+1);
			return s;
		}
		return("Error");
	}

	
	
	static Boolean canIGo(int x, int y, int d) {
		if ((d==0)&&(y>0)&&" ".equals(whatIsAtXYD(x,y,0))){ return true; } else
		if ((d==2)&&(y<15)&&" ".equals(whatIsAtXYD(x,y,2))){ return true; } else	
		if ((d==1)&&(x<15)&&" ".equals(whatIsAtXYD(x,y,1))){ return true; } else 
		if ((d==3)&&(x>0)&&" ".equals(whatIsAtXYD(x,y,3))){ return true; } else 
		{return false;}
	}

}
