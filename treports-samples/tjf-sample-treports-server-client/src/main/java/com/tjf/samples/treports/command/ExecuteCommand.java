package com.tjf.samples.treports.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecuteCommand {

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
		private boolean isView = false; 
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
}
