import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Maze2 {
	static int real_x=0;
	static int real_y=0;
	static int real_o=1;
	
	static String lab[]= new String[33]; //oshwdem2018
	static int floor[][]= new int[16][16];
	static int soft_o=0;
	static int soft_x=0;
	static int soft_y=0;
	static int orientacion_laberinto=0;
	static String path="";
	static int cuenta_atras=-1;

	public static void main(String[] args) {
	

		File file = new File("laberintos.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((reader.readLine()) != null) {
				for (int l=0;l<33;l++) {
					lab[l]=reader.readLine();
				}
				soft_x=0;
				soft_y=0;
				soft_o=1;
				path="";
				initFloor();
				real_x=0;
				real_y=0;
				real_o=1;
				
			while (!center(soft_x,soft_y)){
				
				double score[]= {0,0,0,0};
				double max_score=0; 
				int max_direction=0; 
				for (int i=0;i<4;i++) {
					
					int score1=(canIGo(i)?100:0);
					int score2=(10-floorDir(i))*3;
					double score3=(20-distanceToCenter(soft_x,soft_y,i))*2;
					int score4=(i==2?10:0);
					score[i]=score1+score2+score3-score4;
					if (score[i]> max_score) {
						max_score=score[i];
						max_direction=i;
						}
				}
				go(max_direction);
				

				
			}
			System.out.println(path);
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

	static boolean center(int x, int y){
		return ((x>6)&&(x<9)&&(y>6)&&(y<9));
	}
	
	public static void go(int dir) {
			real_o=(dir+real_o)%4;
			soft_o=(dir+soft_o)%4;
			path=path+(char)(48+dir);

			floor[soft_x][soft_y]++;
		
			
			if (dir==2) {
				floor[soft_x][soft_y]+=4;//si es marcha atras súbele el peso para que no vuelva
				cuenta_atras=0;
			}
		 
			switch (real_o){
			case 0: real_y--;
					soft_y--;
					break;
			case 1: real_x++;
					soft_x++;
					break;
			case 2: real_y++;
					soft_y++;
					break;
			case 3: real_x--;
					soft_x--;
					break;
			}
		
			
			if ((cuenta_atras>-1)&&(floor[soft_x][soft_y]==0)) {
					path=path.substring(0,path.length()-cuenta_atras*2-1);
					char c=path.charAt(path.length()-1);
					
				
					int cuenta_o=(soft_o+c-48)%4;
					path+=(char)(48+(4-cuenta_o));
					
				cuenta_atras=-1;//desactiva cuenta atras
				
				
			}
			if (cuenta_atras>-1)cuenta_atras++;

	}
	public static void solveMaze() {
		initFloor();
	}

	public static double distanceToCenter(int x, int y,int dir) {
		int dir2=(dir+soft_o)%4;
		if (dir2==0)y--;
		if (dir2==1)x++;
		if (dir2==2)y++;
		if (dir2==3)x--;
		return Math.sqrt((8-x)*(8-x)+(8-y)*(8-y));
	}
	
	public static boolean  canIGo( int dir) { // dir:0=frente, 1= dcha, 2= atrás, 3=izqda //ori: 0N,1E,2S,3W
		
		int d=(dir+soft_o+orientacion_laberinto)%4;
		return whatIsAtXYD(real_x,real_y,d);
	}
	
	public static int floorDir(int dir) {
		int d=(dir+soft_o+orientacion_laberinto)%4;
		if (d==0){
			if (soft_y>0)return floor[soft_x][soft_y-1];	
		}
		else if (d==2){
			if (soft_y<15)return floor[soft_x][soft_y+1];
		}
		else if (d==1){
			if (soft_x<15)return floor[soft_x+1][soft_y];
		}
		else if (d==3){
			if (soft_x>0)return floor[soft_x-1][soft_y];
		}
		return 5;
	}
		
	
	
	static boolean whatIsAtXYD(int x, int y, int d){
		
		if (d==0){
			String s=lab[y*2].substring(x*2+1, x*2+2);
			return s.equals(" ");	
		}
		else if (d==2){
			String s=lab[y*2+2].substring(x*2+1, x*2+2);
			return s.equals(" ");
		}
		else if (d==1){
			String s=lab[y*2+1].substring(x*2+2, x*2+3);
			return s.equals(" ");
		}
		else if (d==3){
			String s=lab[y*2+1].substring(x*2, x*2+1);
			return s.equals(" ");
		}
		return(false);
	}
	
	public static void initFloor() {
		
		for (int x=0;x<16;x++){
			for (int y=0;y<16;y++){
				floor[x][y]=0;
			}
		}	
		floor[0][0]=1;
	}

	static void paintLaberynth(){
	//	System.out.println("Iteración:"+it);
	//	System.out.println("Solución: "+path);
	//	System.out.println("Solución Distancia:"+(path.length()-1));
		System.out.println("Mem used="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		Runtime.getRuntime().gc();
		for (int yy=0;yy<33; yy++){
			for (int xx=0;xx<33; xx++){
				String s =lab[yy].substring(xx,xx+1);
				if (" ".equals(s)){
					if ((real_x*2+1==xx) && (real_y*2+1==yy)){
						String symbols="^>v<";
						s=symbols.substring(real_o,real_o+1);
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

}

