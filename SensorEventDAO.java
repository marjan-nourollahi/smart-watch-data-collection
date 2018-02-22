package eecs.wsu.datacollection;

public class SensorEventDAO {
	
	public final float[] values;
	public final int type;
	
	public SensorEventDAO(int type, float[] values) {
		this.values = values;
		this.type = type;
	}

}
