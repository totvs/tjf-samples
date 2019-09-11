package teste.treports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ReportRequest {

	private GenerateParams generateParams = new GenerateParams();
	private ScheduleParams scheduleParams = new ScheduleParams();
	private String user = "admin";
	
	@JsonProperty("GenerateParams")
	public GenerateParams getGenerateParams() {
		return generateParams;
	}

	@JsonProperty("ScheduleParams")
	public ScheduleParams getScheduleParams() {
		return scheduleParams;
	}

	@JsonProperty("User")
	public String getUser() {
		return user;
	}

	public class GenerateParams {
		private boolean isView = true; 
		private boolean stopExecutionOnError = true;
		Object[] parameters = new Object[0];
		
		@JsonProperty("IsView")
		public boolean isView() {
			return isView;
		}
		
		@JsonProperty("StopExecutionOnError")
		public boolean isStopExecutionOnError() {
			return stopExecutionOnError;
		}
		
		@JsonProperty("Parameters")
		public Object[] getParameters() {
			return parameters;
		}
	}	
	
	public class ScheduleParams {
		private int type = 0;
	
		@JsonProperty("Type")
		public int getType() {
			return type;
		}
	}
	
	@Override
	public String toString() {
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		String json = null;
		
		try {
			json = ow.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
			
		return json;
	}
}
