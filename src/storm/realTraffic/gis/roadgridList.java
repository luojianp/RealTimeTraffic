package storm.realTraffic.gis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.xml.xsi.XSISimpleTypes.Int;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.postgis.MultiLineString;

import backtype.storm.tuple.Values;





public class roadgridList {
	//private ArrayList<Sect> gridList;
	//public int sectCount;
	//ArrayList<SimpleFeature> RoadList;  //<roadID, SimpleFeature>
	//HashMap<String,RoadList> gridList;       //<mapID, roadList>
	public class RoadList extends ArrayList<SimpleFeature>{
		private static final long serialVersionUID = 1L;
		//ArrayList<SimpleFeature> road;
		SimpleFeature road;
		RoadList(){}
		RoadList(SimpleFeature road){
			this.road=road;
		}
		
	}
	/*public class Grid
	{
		public String mapId;
		HashMap<String,SimpleFeature> roadList;  //<roadID, roadList>
		//public HashMap<String,Date> viechleIDList; //存放车辆Id的集合,也要把时间存者，以对每一辆车进行计算时间距离
		//public HashMap<String,String> vieLngLatIDList; //存放车辆Id的集合,也要把时间存者，以对每一辆车进行计算时间距离
	
	    Grid(){}
		Grid(String mapId,HashMap<String,SimpleFeature>  roadList){
		   this.mapId=mapId;
		   this.roadList=roadList;
	   }   
		
		String getMapID(){
			return this.mapId;
		}
		
		Map<String,SimpleFeature> getRoadList(){
			return this.roadList;
		}
		
		
	}*/
	
	
	  public  RoadList getGridByID(String mapId){
		   for(Map.Entry<String, RoadList> g : gridList.entrySet()){
				if(g.getKey().equals(mapId)){
					return g.getValue();
				}
			}		   
		return null;		   
	   }
	
	
	

	
	/*public  Boolean isDisExits(List<Grid> gridList,  String mapId){
		for(Grid g : gridList){
			if(g.equals(mapId)){
				return true;
			}
		}
		return false;
	}*/

	public  Boolean isExits(HashMap<String,RoadList> gridList,  String mapId){
		for(Map.Entry<String,RoadList> g : gridList.entrySet()){
			if(g.getKey().equals(mapId)){
				return true;
			}
		}
		return false;
	}

	
	
	
	public roadgridList(String path) throws SQLException, IOException{
		this.gridList = this.read(path);
		//this.sectCount = this.gridList.size();
	}
	
	


		
/*	public class GridList{
		String mapID;
		List<Grid> gridList;
		
		GridList(){	}
		GridList(String mapID,List<Grid> gridList){
			this.mapID=mapID;;
			this.gridList=gridList;
		}
		
		public  Boolean isDisExits(List<Grid> gridList,  String mapId){
			for(Grid g : gridList){
				if(g.equals(mapId)){
					return true;
				}
			}
			return false;
		}		
	}
*/
	
	HashMap<String,RoadList> gridList =new HashMap<String,RoadList>();	
	private HashMap<String, RoadList> read(String path) throws SQLException, IOException{
		//ArrayList<Sect> sectList = new ArrayList<Sect>();
		
	
		
		
		File file = new File(path);
		//FileDataStoreFactorySpi factory = FileDataStoreFinder.getDataStoreFactory("shp");
		//Map params = Collections.singletonMap( "url", file.toURL() );
		ShapefileDataStore shpDataStore = null;
		shpDataStore = new ShapefileDataStore(file.toURL());
		shpDataStore.setStringCharset(Charset.forName("GBK")); 
		
		//Feature Access
		String typeName = shpDataStore.getTypeNames()[0];  
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;  
		featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>)shpDataStore.getFeatureSource(typeName);  
		FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();  
		FeatureIterator<SimpleFeature> itertor = result.features();  
		while(itertor.hasNext()){  
			//Data Reader
		    SimpleFeature feature = itertor.next();  
		    
		    //Fields Attributes
		    List<Object> fields = feature.getAttributes();
		    
		    //int roadID = Integer.parseInt(feature.getAttribute("ID").toString());
		    //int roadWidth=Integer.parseInt(feature.getAttribute("WIDTH").toString());
		    String mapID=feature.getAttribute("MapID").toString();
		    
		    if (!isExits(gridList, mapID)) {
				 //没有此小区，则新建一个小区，并存起来				
				//System.out.println("gridListID:"+gridListID+"dateTime:"+dateTime+"viechId"+viechId);
				//Grid grid = new Grid();
		    	
		    	//ArrayList<SimpleFeature> roadList =new ArrayList<SimpleFeature>();
		    	RoadList roadList=new RoadList();
		    	roadList.add(feature);
		  
		    	//RoadList roadList=new RoadList();
				//roadList = new ArrayList<SimpleFeature>() ; //存放车辆Id的集合,也要把时间存者，以对每一辆车进行计算时间距离
				
				//grid.mapId = mapID;
		    
				
				
				
				gridList.put(mapID,roadList);  //添加网格
				
				}
		    else{
		    	//Grid grid= getGridByID(mapID);	
		    	RoadList roadList=getGridByID(mapID);
				roadList.add(feature); 		
				//roadList.cnt++;
				//System.out.println("count=\t"+roadList.cnt);
		    }
		    

			/*String geoStr = feature.getDefaultGeometry().toString();			
			MultiLineString linearRing= new MultiLineString(geoStr);
			
			Sect sect;
			ArrayList<Point> ps = new ArrayList<Point>();			
			
			for (int idx = 0; idx < linearRing.getLine(0).numPoints(); idx++) {
				Point p = new Point(linearRing.getLine(0).getPoint(idx).x,linearRing.getLine(0).getPoint(idx).y);//,linearRing.getPoint(idx).y);
				ps.add(p);
			}
			sect = new Sect(ps,roadID,roadWidth,mapID);
			sectList.add(sect); */
			
		}  
	    itertor.close(); 
	    
/*	    System.out.println("gridList= :");	    
	    for(Map.Entry<String, RoadList> g : gridList.entrySet()){


	    	System.out.print(g.getKey()+":\tsize="+g.getValue().size()+"\t");
	    	int n= g.getValue().size();
	    	for(int i=0;i<n;i++ ){
	    		// for(ArrayList<SimpleFeature> road:g){

	    		System.out.print(g.getValue().get(i).getAttribute("ID")  +"\t"  )  ;
	    	}
	    	System.out.println("\n");
	    }*/
	    
	    
		return gridList;
	}

//	public int fetchSect(Point p){
//		int sectID = -1;
//		for (Grid sect : this.gridList) {
//			if(sect.contains(p)){
//				return sect.getID();
//			}
//		}
//		return sectID;
//	}
	
//	public int fetchRoadID(Point p){
//		int sectID = -1;
//		
//	    Integer mapID_x=(int)(p.x*10);
//		Integer mapID_y=(int)(p.y*10);
//		String mapID= mapID_y.toString()+"_" +mapID_x.toString();		
//		
//		for (Sect sect : this.gridList) {
//			int width=sect.getroadWidth();
//			if(sect.getMapID()==mapID &&  sect.matchToRoad(p,width)){
//				return sect.getID();
//			}
//		}
//		return sectID;
//	}
	
	/*public int fetchRoadID(Point p){
		int sectID = -1;
		
	    Integer mapID_x=(int)(p.x*10);
		Integer mapID_y=(int)(p.y*10);
		String mapID= mapID_y.toString()+"_" +mapID_x.toString();
		
		for (Sect sect : this.gridList) {			
			int width=sect.getroadWidth();
			String s=sect.getMapID();
			if(!sect.getMapID().equals(mapID)){
				continue;
			}
			else if(sect.matchToRoad(p,width,sect.points)){
				return sect.getID();
			}
		
		}
		return sectID;
	}*/
	
	public int fetchRoadID(Point p) throws Exception{
		int roadID = -1;
   
		Integer mapID_lon=(int)(p.x*10);
		Integer mapID_lan=(int)(p.y*10);
		String mapID= mapID_lan.toString()+"_" +mapID_lon.toString();
		double minD=Double.MAX_VALUE;
		int width=0;
	
		for (Map.Entry<String, RoadList> grid : this.gridList.entrySet()) {			
		     
			String s=grid.getKey();  //mapID
			int sect_mapID_lan=Integer.parseInt(s.split("_")[0]);
			int sect_mapID_lon=Integer.parseInt(s.split("_")[1]);

			/*if (Math.abs(sect_mapID_lon-mapID_lon)>1 || Math.abs(sect_mapID_lan-mapID_lan)>1 ){
				continue;  //不在GPS点所在的九个格子里
			}*/
			if (!mapID.equals(s) ){
				continue;  //不在GPS点所在的九个格子里
			}
			else {  //在相邻的九个格子内
				for (SimpleFeature feature: grid.getValue()) {
					//SimpleFeature feature=road.getValue();

					int  returnRoadID = Integer.parseInt(feature.getAttribute("ID").toString());
					width=Integer.parseInt(feature.getAttribute("WIDTH").toString());
					if(width<=0)width=10;
					String geoStr=feature.getDefaultGeometry().toString();	
					MultiLineString linearRing= new MultiLineString(geoStr);   				
					ArrayList<Point> ps = new ArrayList<Point>();
					for (int idx = 0; idx < linearRing.getLine(0).numPoints(); idx++) {
						Point pt = new Point(linearRing.getLine(0).getPoint(idx).x,linearRing.getLine(0).getPoint(idx).y);//,linearRing.getPoint(idx).y);
						ps.add(pt);
					}

					int n =ps.size();
					for (int i = 0; i < n - 1; i++) {
						double distance=Polygon.pointToLine(ps.get(i).x,ps.get(i).y,ps.get(i+1).x,ps.get(i+1).y,p.x,p.y)*111.2*1000 ;
						//double distance=Polygon.DistancePointToLine(ps.get(i).x,ps.get(i).y,ps.get(i+1).x,ps.get(i+1).y,p.x,p.y);
						if(distance<minD) {
							minD=distance;
							roadID=returnRoadID;

						}
					}
				}
			}
		}
		//System.out.println("#  ---- ---    count="+count);
		System.out.print("The minimum distance="+minD+"\t");
		if (minD<Math.sqrt(Math.pow(width,2)+ Math.pow(10,2) ))   //sqrt(2) * width
			return roadID;
		else 
			return -1;
	}

	
	public static void main(String[] args) throws Exception {
		
		//Initializations
		//String path = "E:/datasource/sztb/dat/base/gridList/gridList.shp";
		String path = "D:\\shenzhenGIS\\深圳路网信息\\shape-file\\SZRoads.shp";
		roadgridList gridList = new roadgridList(path);
		/*GPSRcrd[] record=new GPSRcrd[14];
		
		record[0] = new GPSRcrd(113.874794,22.558666,100,100);
		record[1] = new GPSRcrd(113.927803,22.5049,100,100);
		record[2] = new GPSRcrd(114.033997,22.761633,100,100);
		record[3] = new GPSRcrd(113.802132,22.732483,100,100);
		record[4] = new GPSRcrd(113.820999,22.7672,100,100);
		record[5] = new GPSRcrd(114.222984,22.556999,100,100);
		record[6] = new GPSRcrd(113.932243,22.692842,100,100);
		record[7] = new GPSRcrd(114.284218,22.777582,100,100);
		record[8] = new GPSRcrd(114.334946,22.68395,100,100);
		record[9] = new GPSRcrd(114.364197,22.689432,100,100);
		record[10] = new GPSRcrd(114.298584,22.75935,100,100);
		record[11] = new GPSRcrd(114.3162,22.706499,100,100);
		record[12] = new GPSRcrd(113.781181,22.742884,100,100);
		record[13] = new GPSRcrd(113.930557,22.770355,100,100);*/
		
		String file="D:\\siat-code\\real-time-traf\\gps",line;
		FileReader fileReader =new FileReader(new File(file)); 		
		BufferedReader access= new BufferedReader(fileReader);
		int count1=0,count2=0;
        try 
        {  		   
            while ((line = access.readLine()) != null)
            { 
                if (line !=null)
                {
               	  // for (int i=0;i<3;i++) {System.out.println("\n");}
							String[] record =line.split(",");
							Point point=new Point(Double.parseDouble(record[0]),Double.parseDouble(record[1]));
							int roadID = gridList.fetchRoadID(point);
							//int id = gridList.fetchSect(record);
							if(roadID==-1)
								System.out.print(++count1 +":"+"no gridList contain this record\n");
							else
								System.out.print(++count2 +":"+"GPS Point falls into Sect No." + roadID+"\n");							
                 
                }          
            } 
        } catch (IOException ex) {System.out.println(ex); } 
 

		/*int count=0;

		for(int i=0;i<record.length;i++){
			int roadID = gridList.fetchRoadID(record);
			//int id = gridList.fetchSect(record);
			if(roadID==-1)
				System.out.println(count++ +":"+"no gridList contain this record\n");
			else
				System.out.println("GPS Point falls into Sect No." + roadID+"\n");
			
		}*/

		
		return;
	}
	
	
	
}